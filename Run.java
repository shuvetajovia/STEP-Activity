import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Run {

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("   BANKING ACCOUNT SYSTEM - ALL ACTIVITIES (1-15)");
        System.out.println("=================================================");

        // Detect base directory where activity folders live
        File baseDir = new File(".");
        if (!new File(baseDir, "activity1").exists() && new File(baseDir, "java-gdb-activities-10/activity1").exists()) {
            baseDir = new File(baseDir, "java-gdb-activities-10");
        }

        int total = 0;
        int passed = 0;

        for (int i = 1; i <= 15; i++) {
            File actDir = new File(baseDir, "activity" + i);
            if (!actDir.exists() || !actDir.isDirectory()) {
                continue;
            }

            String mainClass = getMainClass(i);
            if (mainClass == null) {
                continue; // Skip activity 5 (exceptions only)
            }

            total++;
            System.out.println("\n-------------------------------------------------");
            System.out.println(" Running ACTIVITY " + i + " [" + mainClass + "]");
            System.out.println("-------------------------------------------------");

            File binDir = new File(actDir, "bin");
            if (!binDir.exists()) {
                binDir.mkdirs();
            }

            File srcDir = new File(actDir, "src");
            if (!srcDir.exists()) {
                System.out.println("No src folder found in " + actDir.getPath());
                continue;
            }

            List<String> javaFiles;
            try {
                javaFiles = Files.walk(srcDir.toPath())
                        .filter(p -> p.toString().endsWith(".java"))
                        .map(Path::toString)
                        .collect(Collectors.toList());
            } catch (IOException e) {
                System.err.println("Error reading files for " + actDir + ": " + e.getMessage());
                continue;
            }

            if (javaFiles.isEmpty()) {
                System.out.println("No source files found in " + srcDir.getPath());
                continue;
            }

            // Compile
            List<String> compileCmd = new ArrayList<>();
            compileCmd.add("javac");
            compileCmd.add("-d");
            compileCmd.add(binDir.getAbsolutePath());
            for (String f : javaFiles) {
                compileCmd.add(f);
            }

            boolean compileSuccess = runProcess(compileCmd, actDir);
            if (!compileSuccess) {
                System.err.println("[FAIL] Compilation failed for activity " + i);
                continue;
            }

            // Run
            List<String> runCmd = new ArrayList<>();
            runCmd.add("java");
            runCmd.add("-cp");
            runCmd.add(binDir.getAbsolutePath());
            runCmd.add(mainClass);

            boolean runSuccess = runProcess(runCmd, actDir);
            if (runSuccess) {
                passed++;
            }
        }

        System.out.println("\n=================================================");
        System.out.println(" SUMMARY: " + passed + " / " + total + " Activities Executed Successfully!");
        System.out.println("=================================================");
    }

    private static String getMainClass(int activityNum) {
        switch (activityNum) {
            case 1:
            case 2:
            case 3:
            case 4:
                return "com.gdb.tests.TestAccount";
            case 6:
                return "com.gdb.tests.TestAccountExceptions";
            case 7:
            case 8:
                return "com.gdb.tests.TestAccountSubclasses";
            case 9:
            case 10:
                return "com.gdb.tests.TestAbstractAccount";
            case 11:
            case 12:
                return "com.gdb.tests.TestInterfaceFactory";
            case 13:
                return "com.gdb.tests.TestAccountRulesEngine";
            case 14:
                return "com.gdb.tests.TestAccountRulesEngineProperties";
            case 15:
                return "com.gdb.tests.TestTransferService";
            default:
                return null;
        }
    }

    private static boolean runProcess(List<String> command, File workingDir) {
        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            if (workingDir != null && workingDir.exists()) {
                pb.directory(workingDir);
            }
            pb.inheritIO();
            Process process = pb.start();
            int exitCode = process.waitFor();
            return exitCode == 0;
        } catch (Exception e) {
            System.err.println("Error executing command: " + e.getMessage());
            return false;
        }
    }
}
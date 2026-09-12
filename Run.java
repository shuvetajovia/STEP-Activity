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
        System.out.println("   BANKING ACCOUNT SYSTEM - ALL ACTIVITIES (1-12)");
        System.out.println("=================================================");

        int total = 0;
        int passed = 0;

        for (int i = 1; i <= 12; i++) {
            String actDir = "activity" + i;
            File dir = new File(actDir);
            if (!dir.exists() || !dir.isDirectory()) {
                continue;
            }

            String mainClass = getMainClass(i);
            if (mainClass == null) {
                continue; // Skip activity 5 (contains exception classes only)
            }

            total++;
            System.out.println("\n-------------------------------------------------");
            System.out.println(" Running " + actDir.toUpperCase() + " [" + mainClass + "]");
            System.out.println("-------------------------------------------------");

            File binDir = new File(actDir, "bin");
            if (!binDir.exists()) {
                binDir.mkdirs();
            }

            // Find all .java files in the activity's src directory
            List<String> javaFiles;
            try {
                javaFiles = Files.walk(Paths.get(actDir, "src"))
                        .filter(p -> p.toString().endsWith(".java"))
                        .map(Path::toString)
                        .collect(Collectors.toList());
            } catch (IOException e) {
                System.err.println("Error reading source files for " + actDir + ": " + e.getMessage());
                continue;
            }

            if (javaFiles.isEmpty()) {
                System.out.println("No source files found in " + actDir + "/src");
                continue;
            }

            // Step 1: Compile all source files into bin
            List<String> compileCmd = new ArrayList<>();
            compileCmd.add("javac");
            compileCmd.add("-d");
            compileCmd.add(binDir.getPath());
            compileCmd.addAll(javaFiles);

            boolean compileSuccess = runProcess(compileCmd);
            if (!compileSuccess) {
                System.err.println("[FAIL] Compilation failed for " + actDir);
                continue;
            }

            // Step 2: Run the test runner class
            List<String> runCmd = new ArrayList<>();
            runCmd.add("java");
            runCmd.add("-cp");
            runCmd.add(binDir.getPath());
            runCmd.add(mainClass);

            boolean runSuccess = runProcess(runCmd);
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
            default:
                return null;
        }
    }

    private static boolean runProcess(List<String> command) {
        try {
            ProcessBuilder pb = new ProcessBuilder(command);
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
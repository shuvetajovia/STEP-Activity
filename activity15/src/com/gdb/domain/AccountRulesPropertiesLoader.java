package com.gdb.domain;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class AccountRulesPropertiesLoader {
    private Properties properties = new Properties();

    public AccountRulesPropertiesLoader(String configPath) {
        loadProperties(configPath);
    }

    public static Properties loadRules(String accountType) {
        String fileName = accountType.toLowerCase() + ".properties";
        AccountRulesPropertiesLoader loader = new AccountRulesPropertiesLoader(fileName);
        return loader.properties;
    }

    private void loadProperties(String configPath) {
        InputStream is = null;
        try {
            String cleanName = new File(configPath).getName();
            
            // 1. Classpath
            is = getClass().getClassLoader().getResourceAsStream(configPath);
            if (is == null) {
                is = getClass().getClassLoader().getResourceAsStream("config/rules/" + cleanName);
            }
            if (is == null) {
                is = getClass().getClassLoader().getResourceAsStream("main/resources/config/rules/" + cleanName);
            }

            // 2. Filesystem
            if (is == null) {
                String[] candidates = {
                    configPath,
                    cleanName,
                    "config/rules/" + cleanName,
                    "src/main/resources/config/rules/" + cleanName,
                    "../config/rules/" + cleanName,
                    "../../config/rules/" + cleanName,
                    "15/activity15 Java/config/rules/" + cleanName,
                    "15/activity15 Java/src/main/resources/config/rules/" + cleanName,
                    "15/activity15 Python/config/rules/" + cleanName,
                    "activity15/config/rules/" + cleanName,
                    "activity15/src/main/resources/config/rules/" + cleanName
                };
                for (String path : candidates) {
                    File file = new File(path);
                    if (file.exists() && file.isFile()) {
                        is = new FileInputStream(file);
                        break;
                    }
                }
            }

            if (is != null) {
                properties.load(is);
            }
        } catch (Exception e) {
            System.err.println("Warning: Failed to load properties for " + configPath + ": " + e.getMessage());
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception ignored) {
                }
            }
        }
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public double getDouble(String key, double defaultValue) {
        String val = properties.getProperty(key);
        if (val == null) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(val.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public int getInt(String key, int defaultValue) {
        String val = properties.getProperty(key);
        if (val == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(val.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public Properties getProperties() {
        return properties;
    }
}

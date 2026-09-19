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

    private void loadProperties(String configPath) {
        InputStream is = null;
        try {
            is = getClass().getClassLoader().getResourceAsStream(configPath);
            if (is == null) {
                File file = new File(configPath);
                if (file.exists()) {
                    is = new FileInputStream(file);
                }
            }
            if (is != null) {
                properties.load(is);
            } else {
                System.err.println("Warning: Config file not found at " + configPath);
            }
        } catch (Exception e) {
            System.err.println("Warning: Failed to load properties from " + configPath + ": " + e.getMessage());
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
}

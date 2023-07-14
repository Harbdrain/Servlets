package com.danil.servlets.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FileUtils {
    public static final String FILE_DIR;

    static {
        Properties properties = new Properties();
        try (InputStream inputStream = FileUtils.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (inputStream == null) {
                System.out.println("ERROR: Unable to load application.properties file!");
                System.exit(1);
            }
            properties.load(inputStream);
        } catch (IOException e) {
            System.out.println("ERROR: Unable to load application.properties file! " + e);
            System.exit(1);
        }
        String fileDir = properties.getProperty("file_dir");
        if (!fileDir.endsWith("/")) {
            fileDir += "/";
        }
        FILE_DIR = fileDir;
    }
}

package com.framework;

import com.framework.utils.Logger;
import com.framework.utils.XMLParser;

import java.io.*;
import java.util.Map;

public class ProjectSettings {
    static {
        try {
            String projectSettingsPath = ProjectSettings.class.getResource("/").getPath().substring(1).replaceAll("%20", " ") + "project.xml";
            File file = new File(projectSettingsPath);
            if (file.exists()) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
                StringBuilder stringBuilder = new StringBuilder();
                String lineBuffer;
                while ((lineBuffer = bufferedReader.readLine()) != null) {
                    stringBuilder.append(lineBuffer);
                }
                bufferedReader.close();
                projectSettings_ = XMLParser.convertMapFromXML(stringBuilder.toString());
            }
        }
        catch (Exception exception) {
            Logger.error(exception.getMessage());
        }
    }

    public static long getId() {
        try {
            if (projectSettings_ != null && projectSettings_.get("id") != null) {
                return Long.parseLong(projectSettings_.get("id").toString());
            }
        }
        catch (NumberFormatException exception) {

        }

        return 0;
    }

    public static String getName() {
        try {
            if (projectSettings_ != null && projectSettings_.get("name") != null) {
                return projectSettings_.get("name").toString();
            }
        }
        catch (NumberFormatException exception) {

        }

        return "";
    }

    public static long getIdWorkerSeed() {
        try {
            if (projectSettings_ != null && projectSettings_.get("idWorkerSeed") != null) {
                return Long.parseLong(projectSettings_.get("idWorkerSeed").toString());
            }
        }
        catch (NumberFormatException exception) {

        }

        return 0;
    }

    private static Map<String, Object> projectSettings_;
}

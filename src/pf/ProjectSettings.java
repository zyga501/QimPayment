package pf;

import framework.utils.Logger;
import framework.utils.XMLParser;

import java.io.*;
import java.util.Map;

public class ProjectSettings {
    static {
        try {
            String projectSettingsPath = getProjectPath() + "project.xml";
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

    public static String getProjectPath() {
        return ProjectSettings.class.getResource("/").getPath().substring(1).replaceAll("%20", " ");
    }

    public static String getTopPackagePath() {
        String projectPath = getProjectPath();
        String topPackageName = ProjectSettings.class.getPackage().getName();
        topPackageName = topPackageName.substring(0, topPackageName.indexOf('.'));
        return projectPath.concat("/").concat(topPackageName);
    }

    public static long getId() {
        try {
            if (projectSettings_ != null && projectSettings_.containsKey("id")) {
                return Long.parseLong(projectSettings_.get("id").toString());
            }
        }
        catch (NumberFormatException exception) {

        }

        Logger.error("ProjectSettings.getId() Failed!");
        return 0;
    }

    public static String getName() {
        try {
            if (projectSettings_ != null && projectSettings_.containsKey("name")) {
                return projectSettings_.get("name").toString();
            }
        }
        catch (NumberFormatException exception) {

        }

        Logger.error("ProjectSettings.getName() Failed!");
        return "";
    }

    public static long getIdWorkerSeed() {
        try {
            if (projectSettings_ != null && projectSettings_.containsKey("idWorkerSeed")) {
                return Long.parseLong(projectSettings_.get("idWorkerSeed").toString());
            }
        }
        catch (NumberFormatException exception) {

        }

        Logger.error("ProjectSettings.getIdWorkerSeed() Failed!");
        return 0;
    }

    public static int getNotifyPort(){
        try {
            if (projectSettings_ != null && projectSettings_.containsKey("notifyPort")) {
                return Integer.parseInt(projectSettings_.get("notifyPort").toString());
            }
        }
        catch (NumberFormatException exception) {

        }

        return 2016;
    }

    public static Object getData(String key) {
        if (projectSettings_ != null && projectSettings_.containsKey(key)) {
            return projectSettings_.get(key);
        }

        return  null;
    }

    private static Map<String, Object> projectSettings_;
}

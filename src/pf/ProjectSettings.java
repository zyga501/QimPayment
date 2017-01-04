package pf;

import QimCommon.utils.PathUtils;
import QimCommon.utils.XMLParser;

import java.util.Map;

public class ProjectSettings {
    static {
        try {
            String projectSettingsPath = PathUtils.getProjectPath() + "project.xml";
            projectSettings_ = XMLParser.convertMapFromXmlFile(projectSettingsPath);
        }
        catch (Exception exception) {
            ProjectLogger.error(exception.getMessage());
        }
    }

    public static String getTopPackagePath() {
        String projectPath = PathUtils.getProjectPath();
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

        ProjectLogger.error("ProjectSettings.getId() Failed!");
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

        ProjectLogger.error("ProjectSettings.getName() Failed!");
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

        ProjectLogger.error("ProjectSettings.getIdWorkerSeed() Failed!");
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

    public static Map<String, Object> getMapData(String key) {
        return (Map<String, Object>)getData(key);
    }

    private static Map<String, Object> projectSettings_;
}

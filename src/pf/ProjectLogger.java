package pf;

import framework.utils.Logger;

public class ProjectLogger extends Logger{
    public static void debug(String message) {
        debug("PrjectName : " + ProjectSettings.getName(), message);
    }

    public static void info(String message) {
        info("PrjectName : " + ProjectSettings.getName(), message);
    }

    public static void warn(String message) {
        warn("PrjectName : " + ProjectSettings.getName(), message);
    }

    public static void error(String message) {
        error("PrjectName : " + ProjectSettings.getName(), message);
    }
}

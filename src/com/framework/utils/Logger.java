package com.framework.utils;

import org.apache.log4j.PropertyConfigurator;

public class Logger {
    private static org.apache.log4j.Logger logger_ = org.apache.log4j.Logger.getLogger("Logger");

    public static void info(String message) {
        logger_.info(message);
    }

    public static void warn(String message) {
        logger_.warn(message);
    }

    public static void error(String message) {
        logger_.error(message);
    }

    static {
        PropertyConfigurator.configureAndWatch("./log4j.properties", 60000);
    }
}

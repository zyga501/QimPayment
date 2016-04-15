package com.framework.utils;

import java.util.logging.Level;

public class Logger {
    private static java.util.logging.Logger logger_ = java.util.logging.Logger.getLogger("Loggers");

    public static void info(String message) {
        logger_.log(Level.INFO, message);
    }

    public static void warn(String message) {
        logger_.log(Level.WARNING, message);
    }

    public static void server(String message) {
        logger_.log(Level.SEVERE, message);
    }
}

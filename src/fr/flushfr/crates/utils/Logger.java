package fr.flushfr.crates.utils;

import java.util.logging.Level;

import static fr.flushfr.crates.Main.getMainInstance;

public class Logger {
    private static Logger instance;
    public Logger () {
        instance = this;
    }
    public static Logger getInstance() {
        return instance;
    }
    public void log(Level level, String message) {getMainInstance().getLogger().log(level, "\033[38;5;244m#~ " + message);}
    public void spacer() {getMainInstance().getLogger().log(Level.INFO, "\033[38;5;244m#~ ------------------------------------------------ ~#\033[0m");}
}
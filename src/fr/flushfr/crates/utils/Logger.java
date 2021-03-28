package fr.flushfr.crates.utils;

import java.util.logging.Level;

import static fr.flushfr.crates.Main.getMainInstance;

public class Logger {
    public void log(Level level, String message) {getMainInstance().getLogger().log(level, "\033[38;5;244m#~ " + message);}
    public void spacer() {getMainInstance().getLogger().log(Level.INFO, "\033[38;5;244m#~ ------------------------------------------------ ~#\033[0m");}
}
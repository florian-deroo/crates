package fr.flushfr.crates.managers;

import fr.flushfr.crates.objects.Messages;
import fr.flushfr.crates.utils.Utils;
import org.bukkit.configuration.file.FileConfiguration;

import static fr.flushfr.crates.Main.getErrorManager;

public class DataManager {

    public static void initData (FileConfiguration messageFile) {
        Messages.errorCommand = Utils.colorListToArray(getErrorManager().getStringList(messageFile,"command-error"));
        Messages.reloadSuccess = Utils.colorListToArray(getErrorManager().getStringList(messageFile,"reload-success"));
        Messages.reloadFailed = Utils.colorListToArray(getErrorManager().getStringList(messageFile,"reload-failed"));
        Messages.noKey = Utils.colorListToArray(getErrorManager().getStringList(messageFile, "no-key"));
        Messages.successfullyGive = Utils.colorListToArray(getErrorManager().getStringList(messageFile, "successfully-give"));
        Messages.noMissedRewards = Utils.colorListToArray(getErrorManager().getStringList(messageFile, "no-missed-reward"));

    }
}
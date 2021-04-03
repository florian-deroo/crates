package fr.flushfr.crates.managers;

import fr.flushfr.crates.objects.Messages;
import fr.flushfr.crates.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;


public class DataManager {

    private static DataManager instance;
    public DataManager () {
        instance = this;
    }
    public static DataManager getInstance() {
        return instance;
    }


    public static void initData (FileConfiguration messageFile) {
        Messages.errorCommand = Utils.colorListToArray(ErrorManager.getInstance().getStringList(messageFile,"command-error"));
        Messages.reloadSuccess = Utils.colorListToArray(ErrorManager.getInstance().getStringList(messageFile,"reload-success"));
        Messages.reloadFailed = Utils.colorListToArray(ErrorManager.getInstance().getStringList(messageFile,"reload-failed"));
        Messages.noKey = Utils.colorListToArray(ErrorManager.getInstance().getStringList(messageFile, "no-key"));
        Messages.successfullyGive = Utils.colorListToArray(ErrorManager.getInstance().getStringList(messageFile, "successfully-give"));
        Messages.noMissedRewards = Utils.colorListToArray(ErrorManager.getInstance().getStringList(messageFile, "no-missed-reward"));

    }
}
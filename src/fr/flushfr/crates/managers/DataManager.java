package fr.flushfr.crates.managers;

import fr.flushfr.crates.objects.Data;
import fr.flushfr.crates.objects.error.Error;
import fr.flushfr.crates.objects.Messages;
import fr.flushfr.crates.utils.Convert;
import fr.flushfr.crates.objects.error.*;
import org.bukkit.configuration.file.FileConfiguration;

import static fr.flushfr.crates.Main.getMainInstance;


public class DataManager {

    private static DataManager instance;
    public DataManager () {
        instance = this;
    }
    public static DataManager getInstance() {
        return instance;
    }

    public static void initData (FileConfiguration messageFile) {
        Messages.errorCommandPlayer = Convert.colorListToArray(ErrorManager.getInstance().getStringList(messageFile,"command-error-player"));
        Messages.errorCommandAdmin = Convert.colorListToArray(ErrorManager.getInstance().getStringList(messageFile,"command-error-admin"));
        Messages.reloadSuccess = Convert.colorListToArray(ErrorManager.getInstance().getStringList(messageFile,"reload-success"));
        Messages.reloadFailed = Convert.colorListToArray(ErrorManager.getInstance().getStringList(messageFile,"reload-failed"));
        Messages.noKey = Convert.colorListToArray(ErrorManager.getInstance().getStringList(messageFile, "no-key"));
        Messages.successfullyGive = Convert.colorListToArray(ErrorManager.getInstance().getStringList(messageFile, "successfully-give"));
        Messages.successfullyGiveAll = Convert.colorListToArray(ErrorManager.getInstance().getStringList(messageFile, "successfully-give-all"));
        Messages.noMissedRewards = Convert.colorListToArray(ErrorManager.getInstance().getStringList(messageFile, "no-missed-reward"));
        Messages.cratesLocationSet = Convert.colorListToArray(ErrorManager.getInstance().getStringList(messageFile, "crates-location-set"));
        Messages.cratesLocationRemoved = Convert.colorListToArray(ErrorManager.getInstance().getStringList(messageFile, "crates-location-remove"));
        Messages.addMissedKeyDueToFullInventory = Convert.colorListToArray(ErrorManager.getInstance().getStringList(messageFile, "add-missed-key-due-to-full-inventory"));
        Messages.noCrateMatch = Convert.colorListToArray(ErrorManager.getInstance().getStringList(messageFile, "no-crate-found"));
        Messages.animationRunning = Convert.colorListToArray(ErrorManager.getInstance().getStringList(messageFile, "animation-started"));
        Messages.receivedKey = Convert.colorListToArray(ErrorManager.getInstance().getStringList(messageFile, "key-received"));

        Data.adminPermission = ErrorManager.getInstance().getString(getMainInstance().getConfig(), "permission.admin", new Error(ErrorCategory.CONFIG, "config","permission","admin"));
        Data.playerPermission = ErrorManager.getInstance().getString(getMainInstance().getConfig(), "permission.player", new Error(ErrorCategory.CONFIG, "config","permission","player"));
        Data.colorListHologram = ErrorManager.getInstance().getStringList(getMainInstance().getConfig(), "hologram.colored-animation.color-list", new Error(ErrorCategory.CONFIG, "config", "hologram.colored-animation", "color-list"));
        Data.refreshTime = ErrorManager.getInstance().getInt(getMainInstance().getConfig(), "hologram.colored-animation.refreshColorTime", new Error(ErrorCategory.CONFIG, "config", "hologram.colored-animation", "refreshColorTime"));
        Data.spaceBetweenHolograms = ErrorManager.getInstance().getDouble(getMainInstance().getConfig(), "hologram.space-between-holograms", new Error(ErrorCategory.CONFIG, "config", "hologram", "space-between-holograms"));
        Data.spaceBetweenHologramsAndBlock = ErrorManager.getInstance().getDouble(getMainInstance().getConfig(), "hologram.space-between-holograms-and-crates", new Error(ErrorCategory.CONFIG, "config", "hologram", "space-between-holograms-and-crates"));
        Data.minTPS = ErrorManager.getInstance().getDouble(getMainInstance().getConfig(), "lag-security.tps-min", new Error(ErrorCategory.CONFIG, "config", "lag-security", "tps-min"));
        Data.minTPSProtectionEnable = ErrorManager.getInstance().getBoolean(getMainInstance().getConfig(), "lag-security.should-plugin-disable", new Error(ErrorCategory.CONFIG, "config", "lag-security", "should-plugin-disable"));
    }
}
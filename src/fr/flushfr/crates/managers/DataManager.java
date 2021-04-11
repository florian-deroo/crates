package fr.flushfr.crates.managers;

import fr.flushfr.crates.objects.Data;
import fr.flushfr.crates.objects.Error;
import fr.flushfr.crates.objects.Messages;
import fr.flushfr.crates.utils.Convert;
import fr.flushfr.crates.objects.ErrorCategory;
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
        Messages.errorCommand = Convert.colorListToArray(ErrorManager.getInstance().getStringList(messageFile,"command-error"));
        Messages.reloadSuccess = Convert.colorListToArray(ErrorManager.getInstance().getStringList(messageFile,"reload-success"));
        Messages.reloadFailed = Convert.colorListToArray(ErrorManager.getInstance().getStringList(messageFile,"reload-failed"));
        Messages.noKey = Convert.colorListToArray(ErrorManager.getInstance().getStringList(messageFile, "no-key"));
        Messages.successfullyGive = Convert.colorListToArray(ErrorManager.getInstance().getStringList(messageFile, "successfully-give"));
        Messages.successfullyGiveAll = Convert.colorListToArray(ErrorManager.getInstance().getStringList(messageFile, "successfully-give-all"));
        Messages.noMissedRewards = Convert.colorListToArray(ErrorManager.getInstance().getStringList(messageFile, "no-missed-reward"));
        Messages.cratesLocationSet = Convert.colorListToArray(ErrorManager.getInstance().getStringList(messageFile, "crates-location-set"));
        Messages.cratesLocationRemoved = Convert.colorListToArray(ErrorManager.getInstance().getStringList(messageFile, "crates-location-remove"));
        Messages.addMissedKeyDueToFullInventory = Convert.colorListToArray(ErrorManager.getInstance().getStringList(messageFile, "add-missed-key-due-to-full-inventory"));
        Messages.dropItemInventoryFull = Convert.colorListToArray(ErrorManager.getInstance().getStringList(messageFile, "drop-item-inventory-full"));
        Messages.noCrateMatch = Convert.colorListToArray(ErrorManager.getInstance().getStringList(messageFile, "no-crate-found"));

        Data.adminPermission = ErrorManager.getInstance().getString(getMainInstance().getConfig(), "permission.admin", new Error(ErrorCategory.CONFIG, "config","permission","admin"));
        Data.playerPermission = ErrorManager.getInstance().getString(getMainInstance().getConfig(), "permission.player", new Error(ErrorCategory.CONFIG, "config","permission","player"));
        Data.colorListHologram = ErrorManager.getInstance().getStringList(getMainInstance().getConfig(), "hologram.colored-animation.color-list", new Error(ErrorCategory.CONFIG, "config", "hologram.colored-animation", "color-list"));
        Data.refreshTime = ErrorManager.getInstance().getInt(getMainInstance().getConfig(), "hologram.colored-animation.refreshColorTime", new Error(ErrorCategory.CONFIG, "config", "hologram.colored-animation", "refreshColorTime"));
    }
}
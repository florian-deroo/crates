package fr.flushfr.crates;

import fr.flushfr.crates.animations.Animations;
import fr.flushfr.crates.commands.CratesCommand;
import fr.flushfr.crates.managers.FileManager;
import fr.flushfr.crates.listeners.PlayerListener;
import fr.flushfr.crates.managers.*;
import fr.flushfr.crates.objects.Messages;
import fr.flushfr.crates.utils.Logger;
import fr.flushfr.crates.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class Main extends JavaPlugin {

    private static Main mainInstance;
    public static Main getMainInstance() {return mainInstance;}

    public List<String> errorList = new ArrayList<>();
    public boolean isDisable = false;

    @Override
    public void onEnable () {
        initInstance();

        FileManager.getInstance().saveDefaultCratesFiles();
        FileManager.getInstance().saveDefaultLanguageFile();

        reload();
        saveDefaultConfig();

        getCommand("crates").setExecutor(new CratesCommand());
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
    }

    public void initInstance () {
        mainInstance=this;
        new FileManager();
        new HologramManager();
        new AnimationManager();
        new CratesDataManager();
        new CratesManager();
        new RewardManager();
        new Animations();
        new ErrorManager();
        new Logger();
    }

    public void onDisable () {
        disable();
    }

    public void reload() {
        reload(null);
   }

    public void sendInformationConsole() {
        if (errorList.isEmpty()) {
            Logger.getInstance().spacer();
            Logger.getInstance().log(Level.INFO, "          Crates Plugin by Flush#3805");
            Logger.getInstance().log(Level.INFO, "");
            Logger.getInstance().log(Level.INFO, "> Loaded successfully");
            Logger.getInstance().spacer();
        } else {
            Logger.getInstance().spacer();
            Logger.getInstance().log(Level.INFO, "          Crates Plugin by Flush#3805");
            Logger.getInstance().log(Level.INFO, "");
            Logger.getInstance().log(Level.WARNING, "An error occurred while enabling");
            for (String error: errorList) {
                Logger.getInstance().log(Level.WARNING,  "- " + error );
            }
            Logger.getInstance().spacer();
        }
    }

    public void reload(Player p) {

        disable();
        errorList.clear();
        isDisable = false;

        ZonedDateTime start = ZonedDateTime.now();

        initInstance();

        FileManager.getInstance().saveDefaultCratesFiles();
        FileManager.getInstance().reloadCratesLocationConfig();
        FileManager.getInstance().reloadMissedRewardsConfig();

        DataManager.initData(FileManager.getInstance().getLanguageConfig());


        FileManager.getInstance().initCratesFiles();
        CratesDataManager.getInstance().saveDataFromCratesFile();

        CratesManager.getInstance().initProtectedLocation();

        HologramManager.getInstance().launchHologramMultiColor();
        HologramManager.getInstance().displayAllHologram();

        Duration duration = Duration.between(start, ZonedDateTime.now());
        if (p!=null) {
            if (!errorList.isEmpty()) {
                p.sendMessage(Messages.reloadFailed);
            } else {
                p.sendMessage(Utils.replace(Messages.reloadSuccess, "%reload-time%",duration.toMillis()+""));
            }
        }
        if (!errorList.isEmpty()) {disable();}
        sendInformationConsole();
    }

    public void disable () {
        isDisable = true;
        AnimationManager.getInstance().stopLoopAllAnimation();
        HologramManager.getInstance().removeAllHologram();
    }

}

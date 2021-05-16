package fr.flushfr.crates;

import fr.flushfr.crates.animations.Animations;
import fr.flushfr.crates.commands.CrateTabCompleter;
import fr.flushfr.crates.commands.CratesCommand;
import fr.flushfr.crates.license.LicenceVerif;
import fr.flushfr.crates.license.VersionChecker;
import fr.flushfr.crates.listeners.PlayerListener;
import fr.flushfr.crates.managers.*;
import fr.flushfr.crates.objects.Messages;
import fr.flushfr.crates.utils.Convert;
import fr.flushfr.crates.utils.Logger;
import fr.flushfr.crates.utils.TitleBar;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
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
    public boolean isTPSProtectionStarted = false;

    @Override
    public void onEnable () {
        reload();
        saveDefaultConfig();
        getCommand("crates").setExecutor(new CratesCommand());
        getCommand("crates").setTabCompleter(new CrateTabCompleter());
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
        new TitleBar();
        new VersionChecker(46);
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
            Logger.getInstance().log(Level.INFO, "          Crates Plugin by Flush#0001");
            Logger.getInstance().log(Level.INFO, "");
            Logger.getInstance().log(Level.INFO, "> Loaded successfully");
            Logger.getInstance().spacer();
        } else {
            Logger.getInstance().spacer();
            Logger.getInstance().log(Level.INFO, "          Crates Plugin by Flush#0001");
            Logger.getInstance().log(Level.INFO, "");
            Logger.getInstance().log(Level.WARNING, "An error occurred while enabling");
            for (String error: errorList) {
                Logger.getInstance().log(Level.INFO, "");
                Logger.getInstance().log(Level.WARNING,  "- " + error );
            }
            Logger.getInstance().log(Level.INFO, "");
            Logger.getInstance().spacer();
        }
    }

    public void reload(CommandSender p) {
        disable();
        errorList.clear();
        isDisable = false;

        ZonedDateTime start = ZonedDateTime.now();

        initInstance();
        LicenceVerif dataLicense = new LicenceVerif(getConfig().getString("license-key"), "GROUPE_5ddf8c2cb8a9d95");
        Logger.getInstance().log(Level.INFO, "Checking your license key, please wait.");
        dataLicense.check();


        FileManager.getInstance().saveDefaultCratesFiles();
        FileManager.getInstance().saveDefaultLanguageFile();
        FileManager.getInstance().reloadLanguageConfig();
        FileManager.getInstance().reloadMissedRewardsConfig();
        reloadConfig();

        DataManager.initData(FileManager.getInstance().getLanguageConfig());


        FileManager.getInstance().initCratesFiles();
        CratesDataManager.getInstance().saveDataFromCratesFile();

        CratesManager.getInstance().initProtected();
        CratesManager.getInstance().initTPSChecker();

        HologramManager.getInstance().launchHologramMultiColor();
        HologramManager.getInstance().displayAllHologram();

        Duration duration = Duration.between(start, ZonedDateTime.now());
        if (p!=null) {
            if (!errorList.isEmpty()) {
                p.sendMessage(Messages.reloadFailed);
            } else {
                p.sendMessage(Convert.replaceValues(Messages.reloadSuccess, "%reload-time%",duration.toMillis()+""));
            }
        }
        if (!dataLicense.isValid()) {
            errorList.add("License key invalid");
            disable();
        }
        sendInformationConsole();
    }

    public void disable () {
        isDisable = true;
        if (AnimationManager.getInstance()!=null)
            AnimationManager.getInstance().stopLoopAllAnimation();
        if (HologramManager.getInstance()!=null)
            HologramManager.getInstance().removeAllHologram();
    }

}

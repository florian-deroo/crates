package fr.flushfr.crates;

import fr.flushfr.crates.animations.Animations;
import fr.flushfr.crates.commands.CratesCommand;
import fr.flushfr.crates.folders.CratesFile;
import fr.flushfr.crates.folders.CratesLocationFile;
import fr.flushfr.crates.folders.LanguageFile;
import fr.flushfr.crates.folders.MissedRewardsFile;
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

    private static CratesFile cratesInstance;
    public static CratesFile getCratesFile() {return cratesInstance;}

    private static CratesLocationFile cratesLocationFile;
    public static CratesLocationFile getCratesLocationFile() {return cratesLocationFile;}

    private static MissedRewardsFile missedRewardsFile;
    public static MissedRewardsFile getMissedRewardsFile() {return missedRewardsFile;}

    private static LanguageFile languageFile;
    public static LanguageFile getLanguageFile() {return languageFile;}

    private static HologramManager hologramManager;
    public static HologramManager getHologramManager() {return hologramManager;}

    private static AnimationManager animationManager;
    public static AnimationManager getAnimationManager() {return animationManager;}

    private static CratesDataManager cratesDataManager;
    public static CratesDataManager getCratesDataManager() {return cratesDataManager;}

    private static CratesManager cratesManager;
    public static CratesManager getCratesManager() {return cratesManager;}

    private static Animations animations;
    public static Animations getAnimations() {return animations;}

    private static RewardManager rewardManager;
    public static RewardManager getRewardManager() {return rewardManager;}

    private static ErrorManager errorManager;
    public static ErrorManager getErrorManager() {return errorManager;}

    private static Logger wLogger;
    public static Logger getWLogger() {return wLogger;}

    public List<String> errorList = new ArrayList<>();
    public boolean isDisable = false;

    @Override
    public void onEnable () {
        initInstance();

        getCratesLocationFile().saveDefaultCratesLocationFile();
        getCratesFile().saveDefaultCratesFiles();
        getLanguageFile().saveDefaultLanguageFile();

        reload();
        saveDefaultConfig();

        getCommand("crates").setExecutor(new CratesCommand());
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
    }

    public void initInstance () {
        mainInstance=this;
        cratesInstance=new CratesFile();
        cratesLocationFile = new CratesLocationFile();
        missedRewardsFile = new MissedRewardsFile();
        hologramManager = new HologramManager();
        animationManager = new AnimationManager();
        cratesDataManager = new CratesDataManager();
        cratesManager = new CratesManager();
        rewardManager = new RewardManager();
        languageFile = new LanguageFile();
        animations = new Animations();
        errorManager = new ErrorManager();
        wLogger = new Logger();
    }

    public void onDisable () {
        disable();
    }

    public void reload() {
        reload(null);
   }

    public void sendInformationConsole() {
        if (errorList.isEmpty()) {
            getWLogger().spacer();
            getWLogger().log(Level.INFO, "          Crates Plugin by Flush#3805");
            getWLogger().log(Level.INFO, "");
            getWLogger().log(Level.INFO, "> Loaded successfully");
            getWLogger().spacer();
        } else {
            getWLogger().spacer();
            getWLogger().log(Level.INFO, "          Crates Plugin by Flush#3805");
            getWLogger().log(Level.INFO, "");
            getWLogger().log(Level.WARNING, "An error occurred while enabling");
            for (String error: errorList) {
                getWLogger().log(Level.WARNING,  "- " + error );
            }
            getWLogger().spacer();
        }
    }

    public void reload(Player p) {

        disable();
        errorList.clear();
        isDisable = false;

        ZonedDateTime start = ZonedDateTime.now();

        initInstance();

        getCratesFile().saveDefaultCratesFiles();
        getCratesLocationFile().reloadCratesLocationFile();
        getMissedRewardsFile().reloadMissedRewardsFile();

        DataManager.initData(getLanguageFile().getLanguageFile());

        getCratesFile().initCratesFiles();
        getCratesDataManager().saveDataFromCratesFile();

        getCratesManager().initProtectedLocation();

        getHologramManager().launchHologramMultiColor();
        getHologramManager().displayAllHologram();

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
        getAnimationManager().stopLoopAllAnimation();
        getHologramManager().removeAllHologram();
    }

}

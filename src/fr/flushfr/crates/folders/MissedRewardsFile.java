package fr.flushfr.crates.folders;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.logging.Level;

import static fr.flushfr.crates.Main.getMainInstance;

public class MissedRewardsFile {

    private FileConfiguration customConfig = null;
    private File customConfigFile = null;

    public void reloadMissedRewardsFile() {
        customConfigFile = new File(getMainInstance().getDataFolder(), "data/missed-rewards.yml");
        customConfig = YamlConfiguration.loadConfiguration(customConfigFile);
        Reader defConfigStream = null;
        try {
            defConfigStream = new InputStreamReader(getMainInstance().getResource("data/missed-rewards.yml"), "UTF8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            customConfig.setDefaults(defConfig);
        }
    }

    public FileConfiguration getMissedRewardsFile() {
        if (customConfig == null) {
            reloadMissedRewardsFile();
        }
        return customConfig;
    }

    public void saveMissedRewardsFile() {
        if (customConfig == null || customConfigFile == null) {
            return;
        }
        try {
            getMissedRewardsFile().save(customConfigFile);
        } catch (IOException ex) {
            getMainInstance().getLogger().log(Level.SEVERE, "Could not save config to " + customConfigFile, ex);
        }
    }

    public void saveDefaultMissedRewardsFile() {
        if (customConfigFile == null) {
            customConfigFile = new File(getMainInstance().getDataFolder(), "data/missed-rewards.yml");
        }
        if (!customConfigFile.exists()) {
            getMainInstance().saveResource("data/missed-rewards.yml", false);
        }
    }

}

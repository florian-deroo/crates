package fr.flushfr.crates.folders;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.logging.Level;

import static fr.flushfr.crates.Main.getMainInstance;

public class CratesLocationFile {

    private FileConfiguration customConfig = null;
    private File customConfigFile = null;

    public void reloadCratesLocationFile() {
        customConfigFile = new File(getMainInstance().getDataFolder(), "data/crates-location.yml");
        customConfig = YamlConfiguration.loadConfiguration(customConfigFile);
        Reader defConfigStream = null;
        try {
            defConfigStream = new InputStreamReader(getMainInstance().getResource("data/crates-location.yml"), "UTF8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            customConfig.setDefaults(defConfig);
        }
    }

    public FileConfiguration getCratesLocationFile() {
        if (customConfig == null) {
            reloadCratesLocationFile();
        }
        return customConfig;
    }

    public void saveCratesLocationFile() {
        if (customConfig == null || customConfigFile == null) {
            return;
        }
        try {
            getCratesLocationFile().save(customConfigFile);
        } catch (IOException ex) {
            getMainInstance().getLogger().log(Level.SEVERE, "Could not save config to " + customConfigFile, ex);
        }
    }

    public void saveDefaultCratesLocationFile() {
        if (customConfigFile == null) {
            customConfigFile = new File(getMainInstance().getDataFolder(), "data/crates-location.yml");
        }
        if (!customConfigFile.exists()) {
            getMainInstance().saveResource("data/crates-location.yml", false);
        }
    }
}

package fr.flushfr.crates.folders;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.logging.Level;

import static fr.flushfr.crates.Main.getMainInstance;

public class LanguageFile {
    private FileConfiguration customConfig = null;
    private File customConfigFile = null;

    public void reloadLanguageFile() {
        if (customConfigFile == null) {
            customConfigFile = new File(getMainInstance().getDataFolder(), "lang/"+getMainInstance().getConfig().getString("language")+".yml");
        }
        customConfig = YamlConfiguration.loadConfiguration(customConfigFile);
        Reader defConfigStream = null;
        try {
            defConfigStream = new InputStreamReader(getMainInstance().getResource("lang/"+getMainInstance().getConfig().getString("language")+".yml"), "UTF8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            customConfig.setDefaults(defConfig);
        }
    }

    public FileConfiguration getLanguageFile() {
        if (customConfig == null) {
            reloadLanguageFile();
        }
        return customConfig;
    }

    public void saveLanguageFile() {
        if (customConfig == null || customConfigFile == null) {
            return;
        }
        try {
            getLanguageFile().save(customConfigFile);
        } catch (IOException ex) {
            getMainInstance().getLogger().log(Level.SEVERE, "Could not save config to " + customConfigFile, ex);
        }
    }

    public void saveDefaultLanguageFile() {
        if (customConfigFile == null) {
            customConfigFile = new File(getMainInstance().getDataFolder(), "lang/"+getMainInstance().getConfig().getString("language")+".yml");
        }
        if (!customConfigFile.exists()) {
            getMainInstance().saveResource("lang/"+getMainInstance().getConfig().getString("language")+".yml", false);
        }
    }
}

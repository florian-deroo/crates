package fr.flushfr.crates.managers;

import fr.flushfr.crates.utils.Logger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.*;
import java.util.logging.Level;

import static fr.flushfr.crates.Main.getMainInstance;

public class FileManager {

    private static FileManager instance;
    public FileManager () {
        instance = this;
    }
    public static FileManager getInstance() {
        return instance;
    }

    private FileConfiguration cratesLocationConfig = null;
    private FileConfiguration languageConfig = null;
    private FileConfiguration missedRewardConfig = null;

    public FileConfiguration initConfigFile(String filePath, File file) {
        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(file);
        Reader defConfigStream = null;
        try {
            defConfigStream = new InputStreamReader(getMainInstance().getResource(filePath), "UTF8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            fileConfiguration.setDefaults(defConfig);
        }
        return fileConfiguration;
    }

    //
    // CratesLocation Configuration
    //

    String pathCratesLocationConfig = "data/crates-location.yml";

    public void reloadCratesLocationConfig() {
        cratesLocationConfig = initConfigFile(pathCratesLocationConfig, new File(getMainInstance().getDataFolder(), pathCratesLocationConfig));
    }

    public FileConfiguration getCratesLocationConfig() {
        if (cratesLocationConfig == null) {
            reloadCratesLocationConfig();
        }
        return cratesLocationConfig;
    }

    public void saveCratesLocationConfig() {
        reloadCratesLocationConfig();
        try {
            getCratesLocationConfig().save(new File(getMainInstance().getDataFolder(),  pathCratesLocationConfig));
        } catch (IOException ex) {
            Logger.getInstance().log(Level.SEVERE, "Error while saving"+ pathCratesLocationConfig);
        }
    }

    //
    // Language Configuration
    //

    String pathLanguageConfig = "lang/"+getMainInstance().getConfig().getString("language")+".yml";

    public void reloadLanguageConfig() {
        languageConfig = initConfigFile(pathLanguageConfig, new File(getMainInstance().getDataFolder(), pathLanguageConfig));
    }

    public FileConfiguration getLanguageConfig() {
        if (languageConfig == null) {
            reloadLanguageConfig();
        }
        return languageConfig;
    }

    public void saveLanguageConfig() {
        reloadLanguageConfig();
        try {
            getCratesLocationConfig().save(new File(getMainInstance().getDataFolder(),  pathLanguageConfig));
        } catch (IOException ex) {
            Logger.getInstance().log(Level.SEVERE, "Error while saving"+ pathLanguageConfig);
        }
    }

    public void saveDefaultLanguageFile() {
        if (!new File(getMainInstance().getDataFolder(), pathLanguageConfig).exists()) {
            getMainInstance().saveResource(pathLanguageConfig, false);
        }
    }

    //
    // MissedRewards Configuration
    //

    String pathMissedRewardsConfig = "data/missed-rewards.yml";

    public void reloadMissedRewardsConfig () {
        missedRewardConfig = initConfigFile(pathMissedRewardsConfig, new File(getMainInstance().getDataFolder(), pathMissedRewardsConfig));
    }

    public FileConfiguration getMissedRewardConfig() {
        if (missedRewardConfig == null) {
            reloadMissedRewardsConfig();
        }
        return missedRewardConfig;
    }

    public void saveMissedRewardConfig() {
        reloadLanguageConfig();
        try {
            getCratesLocationConfig().save(new File(getMainInstance().getDataFolder(),  pathMissedRewardsConfig));
        } catch (IOException ex) {
            Logger.getInstance().log(Level.SEVERE, "Error while saving"+ pathMissedRewardsConfig);
        }
    }

    //
    // Crates Configuration
    //

    public List<String> cratesFilesName = new ArrayList<>();
    public HashMap<FileConfiguration, String> cratesFiles = new HashMap<>();

    public void saveDefaultCratesFiles () {
        try {
            cratesFilesName.addAll(Arrays.asList(Objects.requireNonNull(new File(getMainInstance().getDataFolder().getAbsolutePath() + "/crates").list())));
            if (cratesFilesName.size()==0) saveCratesRessources();
        } catch (NullPointerException e) {
            saveCratesRessources();
        }
    }

    private void saveCratesRessources () {
        getMainInstance().saveResource("crates/EpicCrate.yml", false);
        getMainInstance().saveResource("crates/MasterCrate.yml", false);
    }

    public void initCratesFiles() {
        for (String file: cratesFilesName) {
            File customConfigFile = new File(getMainInstance().getDataFolder(), "crates/"+file);
            FileConfiguration customConfigFileConfig = YamlConfiguration.loadConfiguration(customConfigFile);
            Reader defConfigStream = null;
            try {
                defConfigStream = new InputStreamReader(getMainInstance().getResource("crates/"+file), "UTF8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (defConfigStream != null) {
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
                customConfigFileConfig.setDefaults(defConfig);
            }
            cratesFiles.put(customConfigFileConfig,file.replaceAll(".yml",""));
        }
    }


}

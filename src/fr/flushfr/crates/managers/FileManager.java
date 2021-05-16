package fr.flushfr.crates.managers;

import fr.flushfr.crates.utils.Logger;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
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
        try {
            getMissedRewardConfig().save(new File(getMainInstance().getDataFolder(),  pathMissedRewardsConfig));
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
        getMainInstance().saveResource("crates/BlockCrate.yml", false);
        saveDefaultCratesFiles();
    }

    public static YamlConfiguration loadConfiguration(File file) {
        Validate.notNull(file, "File cannot be null");
        YamlConfiguration config = new YamlConfiguration();

        try {
            config.load(file);
        } catch (FileNotFoundException ignored) {
        } catch (IOException | InvalidConfigurationException var4) {
            Logger.getInstance().spacer();
            Logger.getInstance().log(Level.SEVERE, "Please put space between data and data name, example:");
            Logger.getInstance().log(Level.SEVERE, "type: CHAT_MESSAGE");
            Logger.getInstance().log(Level.SEVERE, "AND NOT type:CHAT_MESSAGE");
            Logger.getInstance().spacer();
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load " + file, var4);
        }

        return config;
    }

    public void initCratesFiles() {
        try {
            for (String file: cratesFilesName) {
                File customConfigFile = new File(getMainInstance().getDataFolder(), "crates/"+file);
                FileConfiguration customConfigFileConfig = loadConfiguration(customConfigFile);
                if (file.equals("EpicCrate.yml") && customConfigFileConfig.getKeys(true).size()==0) {
                    fillDefaultConfigurationCratesEpicCrates(customConfigFileConfig);
                    customConfigFileConfig.save(new File(getMainInstance().getDataFolder(), "crates/"+file));
                    customConfigFileConfig = initConfigFile("crates/"+file, new File(getMainInstance().getDataFolder(), "crates/"+file));
                } else if (file.equals("BlockCrate.yml") && customConfigFileConfig.getKeys(true).size()==0) {
                    fillDefaultConfigurationCratesBlockCrates(customConfigFileConfig);
                    customConfigFileConfig.save(new File(getMainInstance().getDataFolder(), "crates/"+file));
                    customConfigFileConfig = initConfigFile("crates/"+file, new File(getMainInstance().getDataFolder(), "crates/"+file));
                }
                cratesFiles.put(customConfigFileConfig,file.replaceAll(".yml",""));
            }
        }catch (IOException e) {
            Logger.getInstance().log(Level.SEVERE, "Error while saving crates files");
        }
    }


    public void fillDefaultConfigurationCratesEpicCrates (FileConfiguration f) {
        f.set("open-key.id", 339);
        f.set("open-key.data", 0);
        f.set("open-key.name", "&c[&cEpicCrate&c]");
        f.set("open-key.lore", new ArrayList<>(Arrays.asList("&7Cliquez sur la caisse", "&7pour récupérer vos gains")));

        f.set("preview.enabled", true);
        f.set("preview.inventory-name", "&c» &4Caisse : &cEpicCrate");
        f.set("preview.rows", 2);

        f.set("animation.animation0.type", "CHAT_MESSAGE");
        f.set("animation.animation0.everyone", false);
        f.set("animation.animation0.message", "&4≫ &cVous avez ouvert la caisse &4%crate% &c!");

        f.set("animation.animation1.type", "ACTION_BAR_MESSAGE");
        f.set("animation.animation1.everyone", false);
        f.set("animation.animation1.message", "&2≫ &aVous avez ouvert la caisse &2%crate% &a!");

        f.set("animation.animation2.type", "ROLLANIMATION");
        f.set("animation.animation2.sound1", "NOTE_PIANO");
        f.set("animation.animation2.sound2", "NOTE_PLING");
        f.set("animation.animation2.hologram-on-rolling", "&6%item-name% x%amount% &e(Chance: &6%chance%&e)");

        f.set("animation.animation3.type", "FIREWORK");
        f.set("animation.animation3.shape", "BALL");
        f.set("animation.animation3.lifetime", 30);
        f.set("animation.animation3.power", 1);
        f.set("animation.animation3.trail", true);
        f.set("animation.animation3.flicker", true);


        List<String> hologram = new ArrayList<>();
        hologram.add("&c[Clic gauche] Voir les lots");
        hologram.add("&a[Clic droit] Ouvrir la caisse");
        hologram.add("");
        hologram.add("&u&m+                                    +");
        hologram.add("&a≫  Caisse : &uEpicCrate &a≪");
        hologram.add("&u&m+                                    +");
        hologram.add("");
        f.set("hologram", hologram);

        f.set("rewards.1.chance", 20);
        f.set("rewards.1.display.id", 35);
        f.set("rewards.1.display.data", 13);
        f.set("rewards.1.display.amount", 5);
        f.set("rewards.1.display.glow", false);
        f.set("rewards.1.display.name", "&3➤ &b&lx5 &3&lClé de la caisse BlockCrate");
        f.set("rewards.1.display.lore", new ArrayList<>(Arrays.asList("&7Obtenez 5 clé de la caisse BlockCrate !")));
        f.set("rewards.1.reward.give-item-display", false);
        f.set("rewards.1.reward.commands", new ArrayList<>(Arrays.asList("cr give to %player% BlockCrate 5")));

        f.set("rewards.2.chance", 20);
        f.set("rewards.2.display.id", 310);
        f.set("rewards.2.display.data", 0);
        f.set("rewards.2.display.amount", 1);
        f.set("rewards.2.display.enchantment", new ArrayList<>(Arrays.asList("DURABILITY:3","PROTECTION_ENVIRONMENTAL:4")));
        f.set("rewards.2.reward.give-item-display", true);


        f.set("rewards.3.chance", 20);
        f.set("rewards.3.display.id", 311);
        f.set("rewards.3.display.data", 0);
        f.set("rewards.3.display.amount", 1);
        f.set("rewards.3.display.enchantment", new ArrayList<>(Arrays.asList("DURABILITY:3","PROTECTION_ENVIRONMENTAL:4")));
        f.set("rewards.3.reward.give-item-display", true);

        f.set("rewards.4.chance", 20);
        f.set("rewards.4.display.id", 312);
        f.set("rewards.4.display.data", 0);
        f.set("rewards.4.display.amount", 1);
        f.set("rewards.4.display.enchantment", new ArrayList<>(Arrays.asList("DURABILITY:3","PROTECTION_ENVIRONMENTAL:4")));
        f.set("rewards.4.reward.give-item-display", true);

        f.set("rewards.5.chance", 20);
        f.set("rewards.5.display.id", 313);
        f.set("rewards.5.display.data", 0);
        f.set("rewards.5.display.amount", 1);
        f.set("rewards.5.display.enchantment", new ArrayList<>(Arrays.asList("DURABILITY:3","PROTECTION_ENVIRONMENTAL:4")));
        f.set("rewards.5.reward.give-item-display", true);
    }

    public void fillDefaultConfigurationCratesBlockCrates (FileConfiguration f) {
        f.set("open-key.id", 35);
        f.set("open-key.data", 13);
        f.set("open-key.name", "&3[&bBlockCrate&3]");
        f.set("open-key.lore", new ArrayList<>(Arrays.asList("&7Cliquez sur la caisse", "&7pour récupérer vos gains")));

        f.set("preview.enabled", true);
        f.set("preview.inventory-name", "&b» &3Caisse : &bBlockCrate");
        f.set("preview.rows", 2);

        f.set("animation.animation0.type", "CHAT_MESSAGE");
        f.set("animation.animation0.everyone", false);
        f.set("animation.animation0.message", "&b≫ &3Vous avez ouvert la caisse &b%crate% &3!");

        f.set("animation.animation1.type", "ACTION_BAR_MESSAGE");
        f.set("animation.animation1.everyone", false);
        f.set("animation.animation1.message", "&b≫ &3Vous avez ouvert la caisse &b%crate% &3!");

        f.set("animation.animation2.type", "CSGO");
        f.set("animation.animation2.inventory-name", "&aOuverture…");
        f.set("animation.animation2.roll-sound", "NOTE_PIANO");
        f.set("animation.animation2.roll-volume", 1);
        f.set("animation.animation2.roll-pitch", 1);
        f.set("animation.animation2.end-sound", "LEVEL_UP");
        f.set("animation.animation2.end-volume", 1);
        f.set("animation.animation2.end-pitch", 1);

        f.set("animation.animation3.type", "ACTION_BAR_MESSAGE");
        f.set("animation.animation3.everyone", true);
        f.set("animation.animation3.message", "&b≫ %player% a gagné &b%reward%x(%amount%) &3avec une chance de &b%chance% &3!");


        List<String> hologram = new ArrayList<>();
        hologram.add("&c[Clic gauche] Voir les lots");
        hologram.add("&a[Clic droit] Ouvrir la caisse");
        hologram.add("");
        hologram.add("&u&m+                                    +");
        hologram.add("&a≫  Caisse : &uBlockCrate &a≪");
        hologram.add("&u&m+                                    +");
        hologram.add("");
        f.set("hologram", hologram);

        f.set("rewards.1.chance", 20);
        f.set("rewards.1.display.id", 339);
        f.set("rewards.1.display.data", 0);
        f.set("rewards.1.display.amount", 1);
        f.set("rewards.1.display.glow", false);
        f.set("rewards.1.display.name", "&4➤ &c&lx1 &4&lClé de la caisse &cEpicCrate");
        f.set("rewards.1.display.lore", new ArrayList<>(Arrays.asList("&7Obtenez une clé de la caisse EpicCrate !")));
        f.set("rewards.1.reward.give-item-display", false);
        f.set("rewards.1.reward.commands", new ArrayList<>(Arrays.asList("cr give to %player% EpicCrate")));

        f.set("rewards.2.chance", 20);
        f.set("rewards.2.display.id", 49);
        f.set("rewards.2.display.data", 0);
        f.set("rewards.2.display.amount", 64);
        f.set("rewards.2.reward.give-item-display", true);

        f.set("rewards.3.chance", 20);
        f.set("rewards.3.display.id", 1);
        f.set("rewards.3.display.data", 0);
        f.set("rewards.3.display.amount", 1);
        f.set("rewards.3.reward.give-item-display", true);

        f.set("rewards.4.chance", 20);
        f.set("rewards.4.display.id", 41);
        f.set("rewards.4.display.data", 0);
        f.set("rewards.4.display.amount", 16);
        f.set("rewards.4.reward.give-item-display", true);

        f.set("rewards.5.chance", 20);
        f.set("rewards.5.display.id", 54);
        f.set("rewards.5.display.data", 0);
        f.set("rewards.5.display.amount", 32);
        f.set("rewards.5.reward.give-item-display", true);
    }

}

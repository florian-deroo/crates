package fr.flushfr.crates.folders;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.*;

import static fr.flushfr.crates.Main.getMainInstance;

public class CratesFile {

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

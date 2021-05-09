package fr.flushfr.crates.managers;

import fr.flushfr.crates.objects.*;
import fr.flushfr.crates.objects.error.*;
import fr.flushfr.crates.objects.error.Error;
import fr.flushfr.crates.objects.animation.data.*;
import fr.flushfr.crates.utils.*;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.*;

import static fr.flushfr.crates.Main.getMainInstance;

public class CratesDataManager {

    private static CratesDataManager instance;
    public CratesDataManager () {
        instance = this;
    }
    public static CratesDataManager getInstance() {
        return instance;
    }

    public void saveDataFromCratesFile() {
        for (FileConfiguration f: FileManager.getInstance().cratesFiles.keySet()) {
            getDataCrates(f,FileManager.getInstance().cratesFiles.get(f));
        }
    }

    public void getDataCrates (FileConfiguration f, String file) {
        ItemBuilder keyItem = new ItemBuilder(new ItemStack(Material.getMaterial(ErrorManager.getInstance().getInt(f, "open-key.id", new Error(ErrorCategory.ITEM, file, "open-key", "id", ErrorType.UNDEFINED)))));
        keyItem.data(ErrorManager.getInstance().getInt(f, "open-key.data"));
        keyItem.name(Convert.colorString(ErrorManager.getInstance().getString(f,"open-key.name")));
        keyItem.addLore(Convert.colorList(ErrorManager.getInstance().getStringList(f,"open-key.lore")));
        List<String> hologramAmbient = Convert.colorList(ErrorManager.getInstance().getStringList(f,"hologram"));
        List<Reward> rewards = getRewards(f, file);
        int totalProbability = 0;
        for (Reward r:rewards) {totalProbability+=r.getProbability();}
        String worldString = FileManager.getInstance().getCratesLocationConfig().getString("location."+ file.toLowerCase()+".world");
        World world = Bukkit.getWorld(worldString==null ? "world" : worldString);
        double x = FileManager.getInstance().getCratesLocationConfig().getInt("location."+ file.toLowerCase()+".x");
        double y = FileManager.getInstance().getCratesLocationConfig().getDouble("location."+ file.toLowerCase()+".y");
        double z = FileManager.getInstance().getCratesLocationConfig().getInt("location."+ file.toLowerCase()+".z");
        Location crateLocation = new Location(world, x, y, z);
        Location hologramLocation = new Location(world, x+0.5, y, z+0.5);
        boolean isPreviewEnable = f.getBoolean("preview.enable");
        int rowsPreview = ErrorManager.getInstance().getInt(f,"preview.rows", new Error(ErrorCategory.PREVIEW, file, "preview", "rows", ErrorType.UNDEFINED));
        String inventoryNamePreview = Convert.colorString(ErrorManager.getInstance().getString(f,"preview.inventory-name", new Error(ErrorCategory.PREVIEW, file, "preview", "inventory-name", ErrorType.UNDEFINED)));
        CratesManager.getInstance().crates.add(new Crates(file, getAnimationList(f, file), keyItem, rewards, isPreviewEnable, rowsPreview, crateLocation, hologramLocation, inventoryNamePreview, hologramAmbient, totalProbability));
    }

    public List<Object> getAnimationList (FileConfiguration f, String fileName) {
        List<Object> animationList = new ArrayList<>();
        Set<String> animationStringList;
        try {
            animationStringList = f.getConfigurationSection("animation").getKeys(false);
        } catch (NullPointerException ignored) {animationStringList = new HashSet<>();}
        for (String s: animationStringList) {
            switch (ErrorManager.getInstance().getString(f,"animation."+s+".type", new Error(ErrorCategory.ANIMATION, fileName,s, "type")).toLowerCase()) {
                case "firework":
                    animationList.add(extractFireworkData(getDataFromAnimation("animation."+s, f),"animation."+s, fileName));
                    break;
                case "epicsword":
                    animationList.add(extractEpicSwordData(f, "animation."+s, fileName));
                    break;
                case "rollanimation":
                    animationList.add(extractRollData(f, "animation."+s, fileName));
                    break;
                case "chat_message":
                    animationList.add(extractChatMessageData(f, "animation."+s, fileName));
                    break;
                case "action_bar_message":
                    animationList.add(extractActionBarData(f, "animation."+s, fileName));
                    break;
                case "title_message":
                    animationList.add(extractTitleData(f, "animation."+s, fileName));
                    break;
                case "playsound":
                    animationList.add(getSoundInformation(f, "animation."+s, fileName, "sound", "volume", "pitch"));
                    break;
                case "simple_rotation":
                    animationList.add(extractSimpleRotationAnimation(f, "animation."+s, fileName));
                    break;
                case "csgo":
                    animationList.add(extractCSGOAnimation(f, "animation."+s, fileName));
                    break;
            }
        }
        return animationList;
    }


    public HashMap<String, String> getDataFromAnimation (String path, FileConfiguration f) {
        HashMap<String, String> data = new HashMap<>();
        for (String dataName: f.getConfigurationSection(path).getKeys(false)) {
            data.put(dataName, f.getString(path+"."+dataName));
        }
        return data;
    }

    public SimpleRotationData extractSimpleRotationAnimation (FileConfiguration f, String path, String fileName) {
        HashMap<String, String> data = getDataFromAnimation(path, f);
        String rewardNameHologram = "";
        for (String dataName : data.keySet()) {
            if (dataName.equals("reward-name")) {
                rewardNameHologram = data.get(dataName);
            }
        }
        if (rewardNameHologram.equals("")) {
            ErrorManager.getInstance().addError(new Error(ErrorCategory.ANIMATION, fileName, path, "reward-name", ErrorType.UNDEFINED));
        }
        return new SimpleRotationData(rewardNameHologram);
    }


    public TitleMessage extractTitleData (FileConfiguration f, String path, String fileName) {
        HashMap<String, String> data = getDataFromAnimation(path, f);
        boolean everyone = false;
        int stay = 1;
        int fadeOut = 0;
        int fadeIn = 0;
        String title = "";
        String subtitle = "";
        for (String dataName : data.keySet()) {
            if (dataName.equals("everyone")) {
                everyone = Boolean.parseBoolean(data.get(dataName));
            }
            //if (dataName.equals("stay-time")) {
            //    stay = ErrorManager.getInstance().parseInt(data.get(dataName), new Error(ErrorCategory.ANIMATION, fileName, path, "stay-time"));
            //}
            //if (dataName.equals("fadein-time")) {
            //    fadeIn = ErrorManager.getInstance().parseInt(data.get(dataName), new Error(ErrorCategory.ANIMATION, fileName, path, "fadein-time"));
            //}
            //if (dataName.equals("fadeout-time")) {
            //    fadeOut = ErrorManager.getInstance().parseInt(data.get(dataName), new Error(ErrorCategory.ANIMATION, fileName, path, "fadeout-time"));
            //}
            if (dataName.equals("title")) {
                title = data.get(dataName);
            }
            if (dataName.equals("subtitle")) {
                subtitle = data.get(dataName);
            }
        }
        return new TitleMessage(title, subtitle, stay, fadeIn, fadeOut, everyone);
    }

    public ChatMessage extractChatMessageData (FileConfiguration f, String path, String fileName) {
        HashMap<String, String> data = getDataFromAnimation(path, f);
        boolean everyone = false;
        String[] message = new String[]{};
        for (String dataName : data.keySet()) {
            if (dataName.equals("everyone")) {
                everyone = Boolean.parseBoolean(data.get(dataName));
            }
            if (dataName.equals("message")) {
                message = Convert.colorListToArray(ErrorManager.getInstance().getStringList(f, path+".message", new Error(ErrorCategory.ANIMATION, fileName, path, "message")));
            }
        }
        return new ChatMessage(message, everyone);
    }

    public ActionBarMessage extractActionBarData (FileConfiguration f, String path, String fileName) {
        HashMap<String, String> data = getDataFromAnimation(path, f);
        boolean everyone = false;
        String actionBarMessage = "";
        for (String dataName : data.keySet()) {
            if (dataName.equals("everyone")) {
                everyone = Boolean.parseBoolean(data.get(dataName));
            }
            if (dataName.equals("message")) {
                actionBarMessage = Convert.colorString(ErrorManager.getInstance().getString(f, path+".message", new Error(ErrorCategory.ANIMATION, fileName, path, "message")));
            }
        }
        return new ActionBarMessage(actionBarMessage, everyone);
    }

    public CSGOData extractCSGOAnimation (FileConfiguration f, String path, String fileName) {
        HashMap<String, String> data = getDataFromAnimation(path, f);
        SoundData endSound = getSoundInformation(f, path, fileName, "end-sound", "end-volume", "end-pitch");
        SoundData rollSound = getSoundInformation(f, path, fileName, "roll-sound", "roll-volume", "roll-pitch");
        String inventoryName = "";
        for (String dataName : data.keySet()) {
            if (dataName.equals("inventory-name")) {
                inventoryName = data.get(dataName);
            }
        }
        if (inventoryName.equals("")) {
            ErrorManager.getInstance().addError(new Error(ErrorCategory.ANIMATION, fileName, path, "inventory-name", ErrorType.UNDEFINED));
        }
        return new CSGOData(Convert.colorString(inventoryName), rollSound, endSound);
    }

    public SoundData getSoundInformation (FileConfiguration f, String path, String fileName, String soundVariableName, String volumeVariableName, String pitchVariableName) {
        String sound = "";
        float volume = 1;
        float pitch = 1;
        for (String dataName: f.getConfigurationSection(path).getKeys(false)) {
            if (dataName.toLowerCase().equals(soundVariableName)) {
                sound = f.getString(path+"."+soundVariableName);
            }
            if (dataName.toLowerCase().equals(path+"."+volumeVariableName)) {
                volume = f.getFloat(path);
            }
            if (dataName.toLowerCase().equals(path+"."+pitchVariableName)) {
                pitch = f.getFloat(path);
            }
        }
        return ErrorManager.getInstance().getSound(new Error(ErrorCategory.SOUND, fileName, path, soundVariableName), sound, volume, pitch);
    }

    public RollData extractRollData(FileConfiguration f, String path, String fileName) {
        HashMap<String, String> data = getDataFromAnimation(path, f);
        String hologramOnRolling = "";
        for (String dataName : data.keySet()) {
            if (dataName.equals("hologram-on-rolling")) {
                hologramOnRolling = data.get(dataName);
            }
        }
        if (hologramOnRolling.equals("")) {
            ErrorManager.getInstance().addError(new Error(ErrorCategory.ANIMATION, fileName, path, "hologram-on-rolling", ErrorType.UNDEFINED));
        }
        return new RollData(getSoundInformation(f, path, fileName, "sound1", "volume1", "pitch1"),getSoundInformation(f, path, fileName, "sound2", "volume2", "pitch2"), hologramOnRolling);
    }


    public EpicSwordData extractEpicSwordData (FileConfiguration f, String path, String fileName) {
        HashMap<String, String> data = getDataFromAnimation(path, f);
        int position = 0;
        ItemStack item1 = new ItemStack(Material.DIAMOND_SWORD);
        ItemStack item2 = new ItemStack(Material.GOLD_SWORD);
        Material m;
        for (String dataName : data.keySet()) {
            switch (dataName) {
                case "startPosition":
                    if (data.get(dataName).toLowerCase().equals("north") || data.get(dataName).toLowerCase().equals("south")) {
                        position = 0;
                    } else {
                        position = 1;
                    }
                    break;
                case "item1":
                    m = Material.getMaterial(data.get(dataName));
                    if (m!=null) {
                        item1 = new ItemStack(m);
                    }
                    break;
                case "item2":
                    m = Material.getMaterial(data.get(dataName));
                    if (m!=null) {
                        item2 = new ItemStack(m);
                    }
                    break;
            }
        }
        return new EpicSwordData(position, item1, item2);
    }

    public Effect getParticle (String particleName, String path, String fileName) {
        Effect particle = Effect.HAPPY_VILLAGER;
        try {
            particle = Effect.valueOf(particleName);
        } catch (IllegalArgumentException e) {
            ErrorManager.getInstance().addError(new Error(ErrorCategory.ANIMATION, fileName, path, "particle", ErrorType.INCORRECT_PARTICLE));
        }
        return particle;
    }

    public FireworkData extractFireworkData (HashMap<String, String> data, String path, String fileName) {
        Color fireworkColor = Color.AQUA;
        Color fadeColor = Color.AQUA;
        int lifeTime = 1;
        int power = 1;
        boolean trail = false;
        boolean flicker = false;
        FireworkEffect.Type shape = FireworkEffect.Type.STAR;
        try {
            for (String f: data.keySet()) {
                String value = data.get(f);
                switch (f.toLowerCase()) {
                    case "fireworkcolor":
                        fireworkColor = Utils.getColor(value);
                        break;
                    case "fadecolor":
                        fadeColor = Utils.getColor(value);
                        break;
                    case "lifetime":
                        lifeTime = ErrorManager.getInstance().parseInt(value, new Error(ErrorCategory.ANIMATION, fileName, path, "lifetime"));
                        break;
                    case "power":
                        power = ErrorManager.getInstance().parseInt(value, new Error(ErrorCategory.ANIMATION, fileName, path, "power"));
                        break;
                    case "trail":
                        trail = Boolean.parseBoolean(value);
                        break;
                    case "flicker":
                        flicker = Boolean.parseBoolean(value);
                        break;
                    case "shape":
                        try {
                            shape = FireworkEffect.Type.valueOf(value);
                        } catch (Exception ignored) {
                            ErrorManager.getInstance().addError(new Error(ErrorCategory.ANIMATION, fileName, path, "shape", ErrorType.INCORRECT_FIREWORK_SHAPE));
                        }
                        break;
                }
            }
        } catch (NumberFormatException ignored) {}
        return new FireworkData(fireworkColor, fadeColor, lifeTime, power, trail, flicker, shape);
    }

    public List<Reward> getRewards(FileConfiguration f, String crateName) {
        List<Reward> rewardList = new ArrayList<>();
        for (String i : f.getConfigurationSection("rewards").getKeys(false))
            rewardList.add(getReward(i,f,crateName));
        return rewardList;
    }

    public Reward getReward(String rewardName, FileConfiguration f, String crateName) {
        int probability = ErrorManager.getInstance().getInt(f ,"rewards."+rewardName+".chance" , new Error (ErrorCategory.ITEM, crateName,rewardName,"chance"));
        ItemBuilder itemToGive = ErrorManager.getInstance().getItemFromConfig(f, "rewards."+rewardName, crateName);
        ItemBuilder itemPresentation = itemToGive.copy();
        HashMap<String, String> replace = new HashMap<>();
        replace.put("%chance%",f.getInt("rewards."+rewardName+".chance")+"");
        itemPresentation.addLore(Convert.replaceValues(Convert.listToArray(Convert.colorList(ErrorManager.getInstance().getStringList(getMainInstance().getConfig(), "preview-crates.lore-under"))), replace));
        boolean glowItemPresentation = ErrorManager.getInstance().getBoolean(f, "rewards." + rewardName + ".display.glow");
        itemPresentation.setGlowing(glowItemPresentation);
        boolean giveItemDisplay = ErrorManager.getInstance().getBoolean(f, "rewards." + rewardName + ".reward.give-item-display", new Error(ErrorCategory.ITEM,crateName ,rewardName , "giveItemDisplay"));
        List<String> command = ErrorManager.getInstance().getStringList(f, "rewards." + rewardName + ".reward.commands");
        return new Reward(probability,command,itemPresentation,itemToGive,giveItemDisplay);
    }
}

package fr.flushfr.crates.managers;

import fr.flushfr.crates.objects.Crates;
import fr.flushfr.crates.objects.Error;
import fr.flushfr.crates.objects.Reward;
import fr.flushfr.crates.objects.animation.data.*;
import fr.flushfr.crates.utils.ErrorCategory;
import fr.flushfr.crates.utils.ErrorType;
import fr.flushfr.crates.utils.ItemBuilder;
import fr.flushfr.crates.utils.Utils;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static fr.flushfr.crates.Main.*;

public class CratesDataManager {

    public void saveDataFromCratesFile() {
        for (FileConfiguration f: getCratesFile().cratesFiles.keySet()) {
            getDataCrates(f,getCratesFile().cratesFiles.get(f));
        }
    }

    public void getDataCrates (FileConfiguration f, String file) {
        boolean isBuyEnable = f.getBoolean("buy.enable");
        int costKey = f.getInt("buy.cost");
        ItemBuilder keyItem = new ItemBuilder(new ItemStack(Material.getMaterial(f.getInt("open-key.id"))));
        keyItem.data(f.getInt("open-key.data"));
        keyItem.name(Utils.color(f.getString("open-key.name")));
        keyItem.addLore(Utils.colorList(f.getStringList("open-key.lore")));
        List<String> hologramAmbient = Utils.colorList(f.getStringList("hologram"));
        List<Reward> rewards = getRewards(f, file);
        int totalProbability = 0;
        for (Reward r:rewards) {totalProbability+=r.getProbability();}
        World world = Bukkit.getWorld(getCratesLocationFile().getCratesLocationFile().getString("location."+ file.toLowerCase()+".world"));
        double x = getCratesLocationFile().getCratesLocationFile().getInt("location."+ file.toLowerCase()+".x");
        double y = getCratesLocationFile().getCratesLocationFile().getDouble("location."+ file.toLowerCase()+".y");
        double z = getCratesLocationFile().getCratesLocationFile().getInt("location."+ file.toLowerCase()+".z");
        Location crateLocation = new Location(world, x, y, z);
        Location hologramLocation = new Location(world, x+0.5, y, z+0.5);
        boolean isPreviewEnable = f.getBoolean("preview.enable");
        int rowsPreview = f.getInt("preview.rows");
        String inventoryNamePreview = Utils.color(f.getString("preview.inventory-name"));
        String openMessageToPlayer = Utils.color(f.getString("message.open"));
        String finishBroadcast = Utils.color(f.getString("message.broadcast-on-finish"));
        getCratesManager().crates.add(new Crates(file, getAnimationList(f, file), isBuyEnable, costKey, keyItem, rewards, isPreviewEnable, rowsPreview, openMessageToPlayer, crateLocation, hologramLocation, finishBroadcast, inventoryNamePreview, hologramAmbient, totalProbability));
    }

    public List<Object> getAnimationList (FileConfiguration f, String fileName) {
        List<Object> animationList = new ArrayList<>();
        for (String s: f.getConfigurationSection("animation").getKeys(false)) {
            switch (getErrorManager().getString(f,"animation."+s+".type", new Error(ErrorCategory.ANIMATION, fileName,s, "type")).toLowerCase()) {
                case "firework":
                    animationList.add(extractFireworkData(getDataFromAnimation("animation."+s, f)));
                    break;
                case "epicsword":
                    animationList.add(extractEpicSwordData(f, "animation."+s, fileName));
                    break;
                case "rollanimation":
                    animationList.add(extractRollData(f, "animation."+s, fileName));
                    break;
                case "message_to_player":
                    animationList.add(new MessageData(Utils.colorListToArray(getErrorManager().getStringList(f, "animation."+s+".message")), null));
                    break;
                case "broadcast":
                    animationList.add(new MessageData(Utils.colorListToArray(getErrorManager().getStringList(f, "animation."+s+".message")), true));
                    break;
                case "playsound":
                    animationList.add(getSoundInformation(f, "animation."+s, fileName, "sound", "volume", "pitch"));
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
        return getErrorManager().getSound(new Error(ErrorCategory.SOUND, fileName, path, soundVariableName), sound, volume, pitch);
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
            getErrorManager().addError(new Error(ErrorCategory.ANIMATION, fileName, path, "hologram-on-rolling", ErrorType.UNDEFINED));
        }
        return new RollData(getSoundInformation(f, path, fileName, "sound1", "volume1", "pitch1"),getSoundInformation(f, path, fileName, "sound2", "volume2", "pitch2"), hologramOnRolling);
    }


    public EpicSwordData extractEpicSwordData (FileConfiguration f, String path, String fileName) {
        HashMap<String, String> data = getDataFromAnimation(path, f);
        int position = 0;
        ItemStack item1 = new ItemStack(Material.DIAMOND_SWORD);
        ItemStack item2 = new ItemStack(Material.GOLD_SWORD);
        Effect particle = null;
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
            getErrorManager().addError(new Error(ErrorCategory.ANIMATION, fileName, path, "particle", ErrorType.INCORRECT_PARTICLE));
        }
        return particle;
    }

    public FireworkData extractFireworkData (HashMap<String, String> data) {
        Color fireworkColor = Color.AQUA;
        Color fadeColor = Color.AQUA;
        int lifeTime = 1;
        int power = 1;
        boolean trail = false;
        boolean flicker = false;
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
                    lifeTime = Integer.parseInt(value);
                    break;
                case "power":
                    power = Integer.parseInt(value);
                    break;
                case "trail":
                    trail = Boolean.parseBoolean(value);
                    break;
                case "flicker":
                    flicker = Boolean.parseBoolean(value);
                    break;
            }
        }
        return new FireworkData(fireworkColor, fadeColor, lifeTime, power, trail, flicker);
    }

    public List<Reward> getRewards(FileConfiguration f, String crateName) {
        List<Reward> rewardList = new ArrayList<>();
        for (String i : f.getConfigurationSection("rewards").getKeys(false))
            rewardList.add(getReward(i,f,crateName));
        return rewardList;
    }

    public Reward getReward(String rewardName, FileConfiguration f, String crateName) {
        int probability = getErrorManager().getInt(f ,"rewards."+rewardName+".chance" , new Error (ErrorCategory.ITEM, crateName,rewardName,"chance"));
        int id = getErrorManager().getInt(f ,"rewards."+rewardName+".display.id" , new Error (ErrorCategory.ITEM,crateName, rewardName,"id"));
        int amount = getErrorManager().getInt(f, "rewards." + rewardName + ".display.amount");
        ItemBuilder itemPresentation = new ItemBuilder(new ItemStack(Material.getMaterial(id), amount));
        itemPresentation.data(f.getInt("rewards." + rewardName + ".display.data"));
        itemPresentation.name(Utils.color(getErrorManager().getString(f, "rewards."+rewardName+".display.name")));
        itemPresentation.addLore(Utils.colorList(getErrorManager().getStringList(f, "rewards." + rewardName + ".display.lore")));
        HashMap<String, String> replace = new HashMap<>();
        replace.put("%chance%",f.getInt("rewards."+rewardName+".chance")+"");
        itemPresentation.addLore(Utils.colorListReplace(getErrorManager().getStringList(f, "preview-crates.lore-under"), replace));
        ItemBuilder itemToGive = new ItemBuilder(new ItemStack(Material.getMaterial(id), amount));
        itemToGive.data(itemPresentation.getData());
        itemToGive.name(Utils.color(getErrorManager().getString(f,"rewards." + rewardName + ".display.name")));
        itemToGive.addLore(Utils.colorList(getErrorManager().getStringList(f, "rewards." + rewardName + ".display.lore")));
        boolean glowItemPresentation = getErrorManager().getBoolean(f, "rewards." + rewardName + ".display.glow");
        itemPresentation.setGlowing(glowItemPresentation);
        boolean giveItemDisplay = getErrorManager().getBoolean(f, "rewards." + rewardName + ".reward.give-item-display", new Error(ErrorCategory.ITEM,crateName ,rewardName , "giveItemDisplay"));
        List<String> command = getErrorManager().getStringList(f, "rewards." + rewardName + ".reward.commands");
        return new Reward(probability,command,itemPresentation,itemToGive,glowItemPresentation,giveItemDisplay);
    }
}

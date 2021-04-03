package fr.flushfr.crates.managers;

import fr.flushfr.crates.animations.Animations;
import fr.flushfr.crates.objects.Crates;
import fr.flushfr.crates.objects.Reward;
import fr.flushfr.crates.objects.animation.data.*;
import fr.flushfr.crates.objects.animation.process.AnimationStatus;
import fr.flushfr.crates.objects.animation.process.SimpleRotationAnimation;
import fr.flushfr.crates.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static fr.flushfr.crates.Main.*;

public class CratesManager {

    private static CratesManager instance;
    public CratesManager () {
        instance = this;
    }
    public static CratesManager getInstance() {
        return instance;
    }

    public List<Crates> crates = new ArrayList<>();
    public List<String> cratesName = new ArrayList<>();
    public HashMap<Location, String> protectedLocation = new HashMap<>();

    public void startAnimationList (Crates c, Player p) {
        List<AnimationStatus> animationStatus= new ArrayList<>();
        List<Object> animationList = new ArrayList<>(c.getAnimationList());
        for (Object ignored : animationList) {animationStatus.add(new AnimationStatus(false , false));}
        HologramManager.getInstance().hideOrRevealHologram(HologramManager.getInstance().crateHologram.get(c.getCrateName()), false);
        Reward reward = RewardManager.getInstance().getRandomRewardFromCrate(c);
        new BukkitRunnable() {
            @Override
            public void run() {
                AnimationStatus currentAnimationStatus = animationStatus.get(0);
                if (!currentAnimationStatus.isStarted()) {
                    currentAnimationStatus.setStarted(true);
                    Object animation = animationList.get(0);
                    if (animation instanceof EpicSwordData) {
                        Animations.getInstance().startEpicSwordAnimation(c, (EpicSwordData) animation, currentAnimationStatus);
                    } else if (animation instanceof RollData) {
                        Animations.getInstance().startRollAnimation(c,(RollData) animation, currentAnimationStatus);
                    } else if (animation instanceof FireworkData) {
                        Animations.getInstance().launchFirework(c.getCrateLocation(), (FireworkData) animation);
                        currentAnimationStatus.setEnded(true);
                    } else if (animation instanceof MessageData) {
                        MessageData msg = (MessageData) animation;
                        if (msg.isEveryone()) {
                            for (Player player:Bukkit.getOnlinePlayers()) {player.sendMessage(Utils.replace(msg.getMessage(), "%player%", ChatColor.stripColor(p.getDisplayName()), "%crate%", c.getCrateName()));}
                        } else {
                            p.sendMessage(Utils.replace(msg.getMessage(), "%player%",  ChatColor.stripColor(p.getDisplayName()), "%crate%", c.getCrateName()));
                        }
                        currentAnimationStatus.setEnded(true);
                    } else if (animation instanceof SoundData) {
                        SoundData soundData = (SoundData) animation;
                        p.playSound(c.getCrateLocation(), soundData.getSound(), soundData.getVolume(),soundData.getPitch());
                        currentAnimationStatus.setEnded(true);
                    } else if (animation instanceof SimpleRotationData) {
                        Animations.getInstance().startSimpleAnimation(c,(SimpleRotationData) animation,  currentAnimationStatus, reward);
                    }
                }
                if (currentAnimationStatus.isEnded()) {
                    animationStatus.remove(0);
                    animationList.remove(0);
                }
                if (animationList.isEmpty()) {
                    RewardManager.getInstance().giveRewardToPlayer(RewardManager.getInstance().getRandomRewardFromCrate(c), p);
                    HologramManager.getInstance().hideOrRevealHologram(HologramManager.getInstance().crateHologram.get(c.getCrateName()), true);
                    cancel();
                }
            }
        }.runTaskTimer(getMainInstance(), 1L, 1L);
    }

    public Crates matchCrate (String crateName) {
        for (Crates crate : CratesManager.getInstance().crates) {
            if (crate.getCrateName().toLowerCase().equals(crateName.toLowerCase())) {
                return crate;
            }
        }
        return null;
    }

    public void setCratePosition (String crateName, Location loc) {
        FileManager.getInstance().getCratesLocationConfig().set("location."+crateName+".world", loc.getWorld().getName());
        FileManager.getInstance().getCratesLocationConfig().set("location."+crateName+".x",loc.getX());
        FileManager.getInstance().getCratesLocationConfig().set("location."+crateName+".y",loc.getY());
        FileManager.getInstance().getCratesLocationConfig().set("location."+crateName+".z",loc.getZ());
        FileManager.getInstance().saveCratesLocationConfig();
        FileManager.getInstance().reloadCratesLocationConfig();
        for (Crates c : crates) {
            if (crateName.equals(c.getCrateName())) {
                c.setCrateLocation(loc);
            }
        }
        getMainInstance().reload();
    }

    public void removeCratePosition (String crateName) {
        FileManager.getInstance().getCratesLocationConfig().set("location."+crateName, "");
        FileManager.getInstance().saveCratesLocationConfig();
        FileManager.getInstance().reloadCratesLocationConfig();
        for (Crates c : crates) {
            if (crateName.equals(c.getCrateName())) {
                c.setCrateLocation(null);
            }
        }
        getMainInstance().reload();
    }

    public void initProtectedLocation () {
        protectedLocation.clear();
        cratesName.clear();
        for (Crates c: CratesManager.getInstance().crates) {
            cratesName.add(c.getInventoryNamePreview());
            protectedLocation.put(c.getCrateLocation(), c.getCrateName());
        }
    }
}

package fr.flushfr.crates.managers;

import fr.flushfr.crates.objects.Crates;
import fr.flushfr.crates.objects.animation.data.*;
import fr.flushfr.crates.objects.animation.process.AnimationStatus;
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

    public List<Crates> crates = new ArrayList<>();
    public List<String> cratesName = new ArrayList<>();
    public HashMap<Location, String> protectedLocation = new HashMap<>();

    public void setCratePosition (String crateName, Location loc) {
        getCratesLocationFile().getCratesLocationFile().set("location."+crateName+".world", loc.getWorld().getName());
        getCratesLocationFile().getCratesLocationFile().set("location."+crateName+".x",loc.getX());
        getCratesLocationFile().getCratesLocationFile().set("location."+crateName+".y",loc.getY());
        getCratesLocationFile().getCratesLocationFile().set("location."+crateName+".z",loc.getZ());
        getCratesLocationFile().saveCratesLocationFile();
        getCratesLocationFile().reloadCratesLocationFile();
        for (Crates c : crates) {
            if (crateName.equals(c.getCrateName())) {
                c.setCrateLocation(loc);
            }
        }
        getMainInstance().reload();
    }

    public void startAnimationList (Crates c, Player p) {
        List<AnimationStatus> animationStatus= new ArrayList<>();
        List<Object> animationList = new ArrayList<>(c.getAnimationList());
        for (Object ignored : animationList) {animationStatus.add(new AnimationStatus(false , false));}
        getHologramManager().hideOrRevealHologram(getHologramManager().crateHologram.get(c.getCrateName()), false);

        new BukkitRunnable() {
            @Override
            public void run() {
                AnimationStatus currentAnimationStatus = animationStatus.get(0);
                if (!currentAnimationStatus.isStarted()) {
                    currentAnimationStatus.setStarted(true);
                    Object animation = animationList.get(0);
                    if (animation instanceof EpicSwordData) {
                        getAnimations().startEpicSwordAnimation(c, (EpicSwordData) animation, currentAnimationStatus);
                    } else if (animation instanceof RollData) {
                        getAnimations().startRollAnimation(c,(RollData) animation, currentAnimationStatus);
                    } else if (animation instanceof FireworkData) {
                        getAnimations().launchFirework(c.getCrateLocation(), (FireworkData) animation);
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
                    }
                }
                if (currentAnimationStatus.isEnded()) {
                    animationStatus.remove(0);
                    animationList.remove(0);
                }
                if (animationList.isEmpty()) {
                    getRewardManager().giveRewardToPlayer(getRewardManager().getRandomRewardFromCrate(c), p);
                    getHologramManager().hideOrRevealHologram(getHologramManager().crateHologram.get(c.getCrateName()), true);
                    cancel();
                }
            }
        }.runTaskTimer(getMainInstance(), 1L, 1L);
    }

    public Crates matchCrate (String crateName) {
        for (Crates crate : getCratesManager().crates) {
            if (crate.getCrateName().toLowerCase().equals(crateName.toLowerCase())) {
                return crate;
            }
        }
        return null;
    }

    public void removeCratePosition (String crateName) {
        getCratesLocationFile().getCratesLocationFile().set("location."+crateName, "");
        getCratesLocationFile().saveCratesLocationFile();
        getCratesLocationFile().reloadCratesLocationFile();
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
        for (Crates c: getCratesManager().crates) {
            cratesName.add(c.getInventoryNamePreview());
            protectedLocation.put(c.getCrateLocation(), c.getCrateName());
        }
    }
}

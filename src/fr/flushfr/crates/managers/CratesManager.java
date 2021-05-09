package fr.flushfr.crates.managers;

import fr.flushfr.crates.Main;
import fr.flushfr.crates.animations.Animations;
import fr.flushfr.crates.objects.Crates;
import fr.flushfr.crates.objects.Data;
import fr.flushfr.crates.objects.Reward;
import fr.flushfr.crates.objects.animation.data.*;
import fr.flushfr.crates.objects.animation.process.AnimationStatus;
import fr.flushfr.crates.utils.Logger;
import fr.flushfr.crates.utils.TitleBar;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import static fr.flushfr.crates.Main.getMainInstance;

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
    public List<String> protectedInventory = new ArrayList<>();
    public HashMap<Location, String> protectedLocation = new HashMap<>();
    public HashMap<Crates, Boolean> animationRunning = new HashMap<>();

    public void startAnimationList (Crates c, Player p) {
        List<AnimationStatus> animationStatus= new ArrayList<>();
        List<Object> animationList = new ArrayList<>(c.getAnimationList());
        for (Object ignored : animationList) {animationStatus.add(new AnimationStatus(false , false));}
        HologramManager.getInstance().hideOrRevealHologram(HologramManager.getInstance().crateHologram.get(c.getCrateName()), false);
        Reward reward = RewardManager.getInstance().getRandomRewardFromCrate(c);
        if (animationStatus.isEmpty()) {
            RewardManager.getInstance().giveRewardToPlayer(reward, p);
            HologramManager.getInstance().hideOrRevealHologram(HologramManager.getInstance().crateHologram.get(c.getCrateName()), true);
            return;
        }
        for (Object o : animationList) {
            if (o instanceof EpicSwordData || o instanceof RollData || o instanceof SimpleRotationData) {
                animationRunning.replace(c, true);
            }
        }
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
                        Animations.getInstance().startRollAnimation(c, reward, (RollData) animation, currentAnimationStatus);
                    } else if (animation instanceof FireworkData) {
                        Animations.getInstance().launchFirework(c.getCrateLocation(), (FireworkData) animation);
                        currentAnimationStatus.setEnded(true);
                    } else if (animation instanceof ChatMessage) {
                        Animations.getInstance().sendChatMessage(c, reward, (ChatMessage) animation, p);
                        currentAnimationStatus.setEnded(true);
                    } else if (animation instanceof TitleMessage) {
                        Animations.getInstance().sendTitleMessage(c, reward, (TitleMessage) animation, p);
                        currentAnimationStatus.setEnded(true);
                    } else if (animation instanceof ActionBarMessage) {
                        Animations.getInstance().sendActionBarMessage(c, reward, (ActionBarMessage) animation, p);
                        currentAnimationStatus.setEnded(true);
                    } else if (animation instanceof SoundData) {
                        SoundData soundData = (SoundData) animation;
                        p.playSound(c.getCrateLocation(), soundData.getSound(), soundData.getVolume(),soundData.getPitch());
                        currentAnimationStatus.setEnded(true);
                    } else if (animation instanceof SimpleRotationData) {
                        Animations.getInstance().startSimpleAnimation(c,(SimpleRotationData) animation,  currentAnimationStatus, reward);
                    } else if (animation instanceof CSGOData) {
                        Animations.getInstance().startCSGOAnimation(c, (CSGOData) animation,   currentAnimationStatus,p, reward);
                    }
                }
                if (currentAnimationStatus.isEnded()) {
                    animationStatus.remove(0);
                    animationList.remove(0);
                }
                if (animationList.isEmpty()) {
                    RewardManager.getInstance().giveRewardToPlayer(reward, p);
                    HologramManager.getInstance().hideOrRevealHologram(HologramManager.getInstance().crateHologram.get(c.getCrateName()), true);
                    animationRunning.replace(c, false);
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
        getMainInstance().reload();
    }

    public void removeCratePosition (String crateName) {
        FileManager.getInstance().getCratesLocationConfig().set("location."+crateName, "");
        FileManager.getInstance().saveCratesLocationConfig();
        FileManager.getInstance().reloadCratesLocationConfig();
        getMainInstance().reload();
    }

    public void initProtected () {
        protectedLocation.clear();
        cratesName.clear();
        for (Crates c: CratesManager.getInstance().crates) {
            animationRunning.put(c, false);
            cratesName.add(c.getCrateName());
            protectedInventory.add(c.getInventoryNamePreview());
            for (Object o : c.getAnimationList()) {
                if (o instanceof CSGOData) {
                    protectedInventory.add(((CSGOData) o).getInventoryName());
                }
            }
            protectedLocation.put(c.getCrateLocation(), c.getCrateName());
        }
    }

    private Object serverInstance;
    private Field tpsField;
    private boolean disableDueToLowTPS;

    public void initTPSChecker () {
        try {
            serverInstance = TitleBar.getNMSClass("MinecraftServer").getMethod("getServer").invoke(null);
            tpsField = serverInstance.getClass().getField("recentTps");
        } catch (NoSuchFieldException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | NullPointerException e) {
            e.printStackTrace();
        }
        if (!Data.minTPSProtectionEnable) {
            return;
        }
        if (getMainInstance().isTPSProtectionStarted) {
            return;
        }
        getMainInstance().isTPSProtectionStarted=true;
        new BukkitRunnable() {
            @Override
            public void run() {
                if (getServerTps(2) <= Data.minTPS) {
                    Logger.getInstance().log(Level.SEVERE, "Loss of TPS detected (lower than: "+Data.minTPS+"), crates will be unusable until the TPS go up");
                    Main.getMainInstance().disable();
                    disableDueToLowTPS=true;
                } else {
                    if (disableDueToLowTPS) {
                        Logger.getInstance().log(Level.INFO, "The plugin is relaunched because the TPS has increased");
                        Main.getMainInstance().reload();
                        disableDueToLowTPS=false;
                    }
                }
            }
        }.runTaskTimer(getMainInstance(), 100L, 10*20L);
    }

    public Double getServerTps (int time) {
        try {
            double[] tps = ((double[]) tpsField.get(serverInstance));
            return tps[time];
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


}

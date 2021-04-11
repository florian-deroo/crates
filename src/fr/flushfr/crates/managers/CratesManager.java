package fr.flushfr.crates.managers;

import fr.flushfr.crates.animations.Animations;
import fr.flushfr.crates.objects.Crates;
import fr.flushfr.crates.objects.Reward;
import fr.flushfr.crates.objects.animation.data.*;
import fr.flushfr.crates.objects.animation.process.AnimationStatus;
import fr.flushfr.crates.utils.ActionBar;
import fr.flushfr.crates.utils.Convert;
import fr.flushfr.crates.utils.TitleBar;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    public void startAnimationList (Crates c, Player p) {
        List<AnimationStatus> animationStatus= new ArrayList<>();
        List<Object> animationList = new ArrayList<>(c.getAnimationList());
        for (Object ignored : animationList) {animationStatus.add(new AnimationStatus(false , false));}
        HologramManager.getInstance().hideOrRevealHologram(HologramManager.getInstance().crateHologram.get(c.getCrateName()), false);
        Reward reward = RewardManager.getInstance().getRandomRewardFromCrate(c);
        if (animationStatus.isEmpty()) {
            RewardManager.getInstance().giveRewardToPlayer(RewardManager.getInstance().getRandomRewardFromCrate(c), p);
            HologramManager.getInstance().hideOrRevealHologram(HologramManager.getInstance().crateHologram.get(c.getCrateName()), true);
            return;
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
                        Animations.getInstance().startRollAnimation(c,(RollData) animation, currentAnimationStatus);
                    } else if (animation instanceof FireworkData) {
                        Animations.getInstance().launchFirework(c.getCrateLocation(), (FireworkData) animation);
                        currentAnimationStatus.setEnded(true);
                    } else if (animation instanceof MessageData) {
                        MessageData msg = (MessageData) animation;
                        String[] msgToSend = Convert.replaceValues(msg.getMessage().length==0 ? new String[]{} : msg.getMessage(), "%player%",  ChatColor.stripColor(p.getDisplayName()), "%crate%", c.getCrateName(), "%amount%", reward.getItemToGive().build().getAmount()+"", "%chance%", reward.getProbability()+"", "%reward%",  reward.getItemToGive().getName().equals("")?reward.getItemToGive().build().getType()+"":reward.getItemToGive().getName());
                        if (msg.isChatMessage()) {
                            if (msg.isEveryone()) {
                                for (String s:msgToSend) {
                                    Bukkit.broadcastMessage(s);
                                }
                            } else {
                                p.sendMessage(msgToSend);
                            }
                        } else if (msg.isTitleMessage()) {
                            String[] title = Convert.replaceValues(new String[]{msg.getTitle()}, "%player%",  ChatColor.stripColor(p.getDisplayName()), "%crate%", c.getCrateName(), "%amount%", reward.getItemToGive().build().getAmount()+"", "%chance%", reward.getProbability()+"", "%reward%",  reward.getItemToGive().getName().equals("")?reward.getItemToGive().build().getType()+"":reward.getItemToGive().getName());
                            String[] subtitle = Convert.replaceValues(new String[]{msg.getSubtitle()}, "%player%",  ChatColor.stripColor(p.getDisplayName()), "%crate%", c.getCrateName(), "%amount%", reward.getItemToGive().build().getAmount()+"", "%chance%", reward.getProbability()+"", "%reward%",  reward.getItemToGive().getName().equals("")?reward.getItemToGive().build().getType()+"":reward.getItemToGive().getName());
                            TitleBar.sendFullTitle(p, msg.getFadeIn(), msg.getStay(), msg.getFadeOut(), title[0], subtitle[0]);
                        } else if (msg.isActionBar()) {
                            ActionBar.sendActionBarMessage(p, msg.getActionBarMessage());
                        }
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
}

package fr.flushfr.crates.animations;

import fr.flushfr.crates.managers.AnimationManager;
import fr.flushfr.crates.managers.HologramManager;
import fr.flushfr.crates.objects.Crates;
import fr.flushfr.crates.objects.Reward;
import fr.flushfr.crates.objects.animation.data.*;
import fr.flushfr.crates.objects.animation.process.*;
import fr.flushfr.crates.utils.ActionBar;
import fr.flushfr.crates.utils.Convert;
import fr.flushfr.crates.utils.TitleBar;
import fr.flushfr.crates.utils.Utils;
import org.apache.logging.log4j.core.filter.BurstFilter;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftFirework;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static fr.flushfr.crates.Main.getMainInstance;

public class Animations {

    private static Animations instance;
    public Animations () {
        instance = this;
    }
    public static Animations getInstance() {
        return instance;
    }


    public List<Player> reOpenInventory = new ArrayList<>();

    public int updateInventory (Crates c, Inventory inv, int k, Location location, SoundData sound) {
        location.getWorld().playSound(location, sound.getSound(), sound.getVolume(),sound.getPitch());
        for (int i = 0; i<27; i++) {
            inv.setItem( i, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) (k%15)));
        }
        for (int i = 9; i<16; i++) {
            inv.setItem(i+1, c.getRewards().get(k%c.getRewards().size()).getItemPresentation().build());
            k++;
        }
        return k;
    }

    public void startCSGOAnimation (Crates c, CSGOData csgoData, AnimationStatus animationStatus, Player p, Reward reward) {
        CSGOAnimation csgoAnimation = new CSGOAnimation(p);
        Inventory csgoInv = Bukkit.createInventory(null, 27, csgoData.getInventoryName());
        p.openInventory(csgoInv);
        AnimationManager.getInstance().csgoAnimations.add(csgoAnimation);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (reOpenInventory.contains(p)) {
                    p.openInventory(csgoInv);
                }
                if (csgoAnimation.getA()<40 && csgoAnimation.getA()%4==0) {
                    csgoAnimation.setB(updateInventory(c, csgoInv, csgoAnimation.getB(), p.getLocation(), csgoData.getSoundOnRolling()));
                } else if (csgoAnimation.getA()>=50 && csgoAnimation.getA()<100 && csgoAnimation.getA()%10==0) {
                    csgoAnimation.setB(updateInventory(c, csgoInv, csgoAnimation.getB(), p.getLocation(), csgoData.getSoundOnRolling()));
                } else if (csgoAnimation.getA()>=100 && csgoAnimation.getA()<=140 && csgoAnimation.getA()%20==0) {
                    csgoAnimation.setB(updateInventory(c, csgoInv, csgoAnimation.getB(), p.getLocation(), csgoData.getSoundOnRolling()));
                } else if (csgoAnimation.getA()>140 && csgoAnimation.getA()%20==0) {
                    if (Utils.isItemSimilar(reward.getItemPresentation().build(), csgoInv.getItem(13), false)) {
                        p.getLocation().getWorld().playSound(p.getLocation(), csgoData.getSoundEnd().getSound(), csgoData.getSoundEnd().getVolume(),csgoData.getSoundEnd().getPitch());
                        p.closeInventory();
                        animationStatus.setEnded(true);
                        cancel();
                    } else {
                        csgoAnimation.setB(updateInventory(c, csgoInv, csgoAnimation.getB(), p.getLocation(), csgoData.getSoundOnRolling()));
                    }
                }
                csgoAnimation.setA(csgoAnimation.getA()+1);
            }
        }.runTaskTimer(getMainInstance(),0L,1L);
    }

    public void startSimpleAnimation (Crates c, SimpleRotationData simpleRotationData, AnimationStatus animationStatus, Reward reward) {
        Location l = c.getCrateLocation().clone();
        l.setX(l.getX()+0.5);
        l.setZ(l.getZ()+0.5);
        SimpleRotationAnimation s = new SimpleRotationAnimation(HologramManager.getInstance().spawnHologram(l, "", "toRemove"), l);
        HologramManager.getInstance().addHologramToRemove(s.getArmorStand());
        s.getArmorStand().setVisible(true);
        ItemStack item = new ItemStack(Material.ENDER_CHEST);
        s.getArmorStand().setVisible(false);
        s.getArmorStand().setCanPickupItems(false);
        s.getArmorStand().setItemInHand(item);
        s.getArmorStand().setCustomName("DO NOT TOUCH");
        s.getArmorStand().setCustomNameVisible(false);
        s.getArmorStand().setRightArmPose(new EulerAngle(Math.toRadians(-20),Math.toRadians(-45),Math.toRadians(0)));
        new BukkitRunnable() {
            @Override
            public void run() {
                if (s.isStop()) {
                    cancel();
                }
                if (s.getIterator()<72) {
                    s.getLocation().setY(s.getLocation().getY()+0.03);
                    s.getLocation().setYaw(s.getLocation().getYaw()+10);
                    s.getArmorStand().teleport(s.getLocation());
                    if (s.getIterator()%2==0) {
                        Location particleLoc = s.getLocation().clone();
                        particleLoc.setY(particleLoc.getY()+0.45);
                        s.getLocation().getWorld().playEffect(particleLoc, Effect.HAPPY_VILLAGER,10);
                    }

                } else if (s.getIterator()==73) {
                    s.setArmorStandRewardName(HologramManager.getInstance().spawnHologram(l, Convert.colorString(simpleRotationData.getRewardNameHologram().replaceAll("%reward%", reward.getItemPresentation().getName().equals("") ? reward.getItemPresentation().build().getType().name() : reward.getItemPresentation().getName()).replaceAll("%amount%", reward.getItemPresentation().build().getAmount()+"").replaceAll("%chance%", reward.getProbability()+"")), "toRemove"));
                    HologramManager.getInstance().addHologramToRemove(s.getArmorStandRewardName());
                    for (int i = 0; i<10;i++) {
                        s.getLocation().getWorld().playEffect(s.getLocation(), Effect.FIREWORKS_SPARK, 10, 10);
                    }
                } else if (s.getIterator()>100) {
                    animationStatus.setEnded(true);
                    s.getArmorStand().remove();
                    s.getArmorStandRewardName().remove();
                    cancel();
                }

                s.setIterator(s.getIterator()+1);
            }
        }.runTaskTimer(getMainInstance(),1L,1L);
    }

    public void startEpicSwordAnimation(Crates c, EpicSwordData epicSwordData, AnimationStatus animationStatus) {
        EpicSwordAnimation AO=new EpicSwordAnimation(c.getCrateLocation());
        AnimationManager.getInstance().animationStarted.add(AO);
        HologramManager.getInstance().addHologramToRemove(AO.getArmorstand());
        AO.getArmorstand().setGravity(false);
        AO.getArmorstand().setVisible(false);
        AO.getArmorstand().setBodyPose(new EulerAngle(Math.toRadians(0),Math.toRadians(0),Math.toRadians(0)));
        AO.getArmorstand().setRightArmPose(new EulerAngle(Math.toRadians(81),0,0));
        AO.getArmorstand().setItemInHand(epicSwordData.getItem1());
        AO.getArmorstand().setCanPickupItems(false);
        AO.getArmorstand().setCustomName("DO NOT TOUCH");
        AO.getArmorstand().setCustomNameVisible(false);
        AO.setParticleLocation(new Location(c.getCrateLocation().getWorld(), Math.round(c.getCrateLocation().getX())+0.5, Math.round(c.getCrateLocation().getY()), Math.round(c.getCrateLocation().getZ()+0.5)));
        new BukkitRunnable() {
            @Override
            public void run() {
                if (AO.isStop()) {
                    cancel();
                }
                if (AO.getTeta() > 1050) {
                    Location strikeLocation = new Location(AO.getDefaultArmorstandLocation().getWorld(), AO.getDefaultArmorstandLocation().getX()-1,AO.getDefaultArmorstandLocation().getY(),AO.getDefaultArmorstandLocation().getZ()-1);
                    c.getCrateLocation().getWorld().strikeLightningEffect(strikeLocation);
                    AO.getArmorstand().teleport(AO.getDefaultArmorstandLocation());
                    AO.getArmorstand().remove();
                    animationStatus.setEnded(true);
                    cancel();
                } else if (AO.getTeta() > 360 * 2.5){
                    AO.getArmorstand().setGravity(true);
                } else {
                    if (AO.getTeta() % 100 == 0) {
                        AO.getArmorstand().setItemInHand(new ItemStack((AO.isChangeSword()) ? epicSwordData.getItem1().getType() : epicSwordData.getItem2().getType()));
                        AO.setChangeSword(!AO.isChangeSword());
                    }
                    AO.getParticleLocation().setX(Math.round(c.getCrateLocation().getX()) + 0.5 + Math.cos(Math.toRadians(AO.getTeta())));
                    AO.getArmorstandLocation().setY(Math.round(c.getCrateLocation().getY()) + AO.getDeltahigh());
                    AO.getArmorstand().teleport(AO.getArmorstandLocation());
                    AO.getParticleLocation().setY(Math.round(c.getCrateLocation().getY()) + AO.getDeltahigh());
                    AO.getParticleLocation().setZ(Math.round(c.getCrateLocation().getZ()) + 0.5 + Math.sin(Math.toRadians(AO.getTeta())));
                    AO.setDeltahigh(AO.getDeltahigh() + 0.05);
                }
                AO.setTeta(AO.getTeta() + 10);
                AO.getParticleLocation().getWorld().playEffect(AO.getParticleLocation(), Effect.HAPPY_VILLAGER, 1);
            }
        }.runTaskTimer(getMainInstance(),1L,1L);
    }

    public void launchFirework(Location l, FireworkData fireworkData){
        Firework fw = l.getWorld().spawn(l,Firework.class);
        FireworkMeta meta = fw.getFireworkMeta();
        meta.addEffect(FireworkEffect.builder().flicker(fireworkData.isFlicker()).with(fireworkData.getType()).trail(fireworkData.isTrail()).withFade(fireworkData.getFadeColor()).withColor(fireworkData.getFireworkColor()).build());
        meta.setPower(fireworkData.getPower());
        fw.setFireworkMeta(meta);
        ((CraftFirework)fw).getHandle().expectedLifespan = fireworkData.getLifeTime();
    }

    public void startRollAnimation(Crates c, Reward reward, RollData rollData, AnimationStatus animationStatus) {
        AtomicBoolean end = new AtomicBoolean(false);
        Location itemHologramLocation = c.getCrateLocation().clone();
        itemHologramLocation.setX(itemHologramLocation.getX()+0.5);
        itemHologramLocation.setZ(itemHologramLocation.getZ()+0.5);
        itemHologramLocation.setY(itemHologramLocation.getY()+0.35);
        ArmorStand itemHologram = HologramManager.getInstance().spawnItemHologram(itemHologramLocation, c.getRewards().get(0).getItemPresentation().build(), "toRemove");
        Location nameHologramLocation = itemHologramLocation.clone();
        nameHologramLocation.setY(nameHologramLocation.getY()+0.35);
        ArmorStand nameHologram = HologramManager.getInstance().spawnHologram(nameHologramLocation,c.getRewards().get(0).getItemPresentation().build().getItemMeta().getDisplayName(), "toRemove");
        nameHologram.setCustomNameVisible(true);
        itemHologram.setCanPickupItems(false);
        itemHologram.setCustomName("DO NOT TOUCH");
        itemHologram.setCustomNameVisible(false);
        RollAnimation rollAnimation= new RollAnimation(c.getRewards(), rollData.getHologramOnRolling());
        new BukkitRunnable() {
            @Override
            public void run() {
                if (rollAnimation.isStop()) {
                    cancel();
                }
                int i = rollAnimation.getI();
                if (i<70 && i%5 == 0) {
                    Bukkit.getWorld(c.getCrateLocation().getWorld().getName()).playSound(itemHologramLocation, rollAnimation.isSoundChange() ? rollData.getSound1().getSound() : rollData.getSound2().getSound(), rollData.getSound2().getVolume(), rollData.getSound2().getPitch());
                    rollAnimation.setSoundChange(!rollAnimation.isSoundChange());
                    HologramManager.getInstance().updateRollArmorstand(rollAnimation, itemHologram, nameHologram);
                } else if (i<131 && i%20 == 0) {
                    Bukkit.getWorld(c.getCrateLocation().getWorld().getName()).playSound(itemHologramLocation, rollAnimation.isSoundChange() ? rollData.getSound1().getSound() : rollData.getSound2().getSound(), rollData.getSound2().getVolume(), rollData.getSound2().getPitch());
                    rollAnimation.setSoundChange(!rollAnimation.isSoundChange());
                    HologramManager.getInstance().updateRollArmorstand(rollAnimation, itemHologram, nameHologram);
                } else if (i>130 && i%20 == 0) {

                    if (i>189& end.get()) {
                        itemHologram.getPassenger().remove();
                        itemHologram.remove();
                        nameHologram.remove();
                        animationStatus.setEnded(true);
                        cancel();
                        return;
                    }
                    if (!end.get()) {
                        if (Utils.isItemSimilar(reward.getItemToGive().build(), rollAnimation.getRewards().get((rollAnimation.getIndexInList()-1)%rollAnimation.getRewards().size()).getItemToGive().build(), false)) {
                            end.set(true);
                        } else {
                            Bukkit.getWorld(c.getCrateLocation().getWorld().getName()).playSound(itemHologramLocation, rollAnimation.isSoundChange() ? rollData.getSound1().getSound() : rollData.getSound2().getSound(), rollData.getSound2().getVolume(), rollData.getSound2().getPitch());
                            rollAnimation.setSoundChange(!rollAnimation.isSoundChange());
                            HologramManager.getInstance().updateRollArmorstand(rollAnimation, itemHologram, nameHologram);
                        }
                    }
                }
                rollAnimation.setI(i+1);
            }
        }.runTaskTimer(getMainInstance(), 1L, 1L);
    }


    public void sendTitleMessage(Crates c,Reward reward, TitleMessage msg, Player p) {
        String title = Convert.replaceValues(msg.getTitle(), "%player%",  ChatColor.stripColor(p.getDisplayName()), "%crate%", c.getCrateName(), "%amount%", reward.getItemToGive().build().getAmount()+"", "%chance%", reward.getProbability()+"", "%reward%",  reward.getItemToGive().getName().equals("")?reward.getItemToGive().build().getType()+"":reward.getItemToGive().getName());
        String subtitle = Convert.replaceValues(msg.getSubtitle(), "%player%",  ChatColor.stripColor(p.getDisplayName()), "%crate%", c.getCrateName(), "%amount%", reward.getItemToGive().build().getAmount()+"", "%chance%", reward.getProbability()+"", "%reward%",  reward.getItemToGive().getName().equals("")?reward.getItemToGive().build().getType()+"":reward.getItemToGive().getName());
        if (msg.isEveryone()) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                TitleBar.sendFullTitle(player, msg.getFadeIn(), msg.getStay(), msg.getFadeOut(), title, subtitle);
            }
        } else {
            TitleBar.sendFullTitle(p, msg.getFadeIn(), msg.getStay(), msg.getFadeOut(), title, subtitle);
        }
    }

    public void sendChatMessage(Crates c,Reward reward, ChatMessage msg, Player p) {
        String[] msgToSend = Convert.replaceValues(msg.getMessage().length==0 ? new String[]{} : msg.getMessage(), "%player%",  ChatColor.stripColor(p.getDisplayName()), "%crate%", c.getCrateName(), "%amount%", reward.getItemToGive().build().getAmount()+"", "%chance%", reward.getProbability()+"", "%reward%",  reward.getItemToGive().getName().equals("")?reward.getItemToGive().build().getType()+"":reward.getItemToGive().getName());
        if (msg.isEveryone()) {
            for (String s:msgToSend) {
                Bukkit.broadcastMessage(s);
            }
        } else {
            p.sendMessage(msgToSend);
        }
    }

    public void sendActionBarMessage(Crates c,Reward reward, ActionBarMessage msg, Player p) {
        String msgToSend = Convert.replaceValues(msg.getMessage(), "%player%",  ChatColor.stripColor(p.getDisplayName()), "%crate%", c.getCrateName(), "%amount%", reward.getItemToGive().build().getAmount()+"", "%chance%", reward.getProbability()+"", "%reward%",  reward.getItemToGive().getName().equals("")?reward.getItemToGive().build().getType()+"":reward.getItemToGive().getName());
        if (msg.isEveryone()) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                ActionBar.sendActionBarMessage(player, msgToSend);
            }
        } else {
            ActionBar.sendActionBarMessage(p, msgToSend);
        }
    }

}

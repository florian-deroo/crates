package fr.flushfr.crates.animations;

import fr.flushfr.crates.managers.AnimationManager;
import fr.flushfr.crates.managers.HologramManager;
import fr.flushfr.crates.objects.Crates;
import fr.flushfr.crates.objects.animation.data.EpicSwordData;
import fr.flushfr.crates.objects.animation.data.FireworkData;
import fr.flushfr.crates.objects.animation.data.RollData;
import fr.flushfr.crates.objects.animation.process.AnimationStatus;
import fr.flushfr.crates.objects.animation.process.EpicSwordAnimation;
import fr.flushfr.crates.objects.animation.process.RollAnimation;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftFirework;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;

import static fr.flushfr.crates.Main.getMainInstance;

public class Animations {

    private static Animations instance;

    public Animations () {
        instance = this;
    }

    public static Animations getInstance() {
        return instance;
    }


    public void startEpicSwordAnimation(Crates c, EpicSwordData epicSwordData, AnimationStatus animationStatus) {
        EpicSwordAnimation AO=new EpicSwordAnimation(c.getCrateLocation());
        AnimationManager.getInstance().animationStarted.add(AO);
        HologramManager.getInstance().addHologramToRemove(AO.getArmorstand());
        AO.getArmorstand().setGravity(false);
        AO.getArmorstand().setCanPickupItems(false);
        AO.getArmorstand().setVisible(false);
        AO.getArmorstand().setCanPickupItems(false);
        AO.getArmorstand().setBodyPose(new EulerAngle(Math.toRadians(0),Math.toRadians(0),Math.toRadians(0)));
        AO.getArmorstand().setRightArmPose(new EulerAngle(Math.toRadians(81),0,0));
        AO.getArmorstand().setItemInHand(epicSwordData.getItem1());
        AO.getArmorstand().setCanPickupItems(false);
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
                        AO.getArmorstand().setCanPickupItems(false);
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
        meta.addEffect(FireworkEffect.builder().flicker(fireworkData.isFlicker()).with(FireworkEffect.Type.STAR).trail(fireworkData.isTrail()).withFade(fireworkData.getFadeColor()).withColor(fireworkData.getFireworkColor()).build());
        meta.setPower(fireworkData.getPower());
        fw.setFireworkMeta(meta);
        ((CraftFirework)fw).getHandle().expectedLifespan = fireworkData.getLifeTime();
    }

    public void startRollAnimation(Crates c, RollData rollData, AnimationStatus animationStatus) {
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
                } else if (i==190) {
                    itemHologram.getPassenger().remove();
                    itemHologram.remove();
                    nameHologram.remove();
                    animationStatus.setEnded(true);
                    cancel();
                }
                rollAnimation.setI(i+1);
            }
        }.runTaskTimer(getMainInstance(), 1L, 1L);
    }
}

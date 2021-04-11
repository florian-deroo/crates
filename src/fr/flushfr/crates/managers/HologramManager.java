package fr.flushfr.crates.managers;

import fr.flushfr.crates.objects.Crates;
import fr.flushfr.crates.objects.Data;
import fr.flushfr.crates.objects.Reward;
import fr.flushfr.crates.objects.animation.process.EpicSwordAnimation;
import fr.flushfr.crates.objects.animation.process.RollAnimation;
import fr.flushfr.crates.utils.Convert;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static fr.flushfr.crates.Main.getMainInstance;

public class HologramManager {

    private static HologramManager instance;
    public HologramManager () {
        instance = this;
    }
    public static HologramManager getInstance() {
        return instance;
    }

    public HashMap<String, List<ArmorStand>> crateHologram = new HashMap<>();

    public List<Item> itemToRemove = new ArrayList<>();
    public HashMap<ArmorStand, String> hologramColored = new HashMap<>();
    public List<String> colorList = new ArrayList<>();
    boolean isRunnableMultiColorStarted;
    int i = 0;

    public void removeAllHologram () {
        for (EpicSwordAnimation ao : AnimationManager.getInstance().animationStarted) {ao.getArmorstand().remove();}
        for (String s: crateHologram.keySet()) {for (ArmorStand h : crateHologram.get(s)) {h.remove();}}
        for (ArmorStand s: hologramColored.keySet()) {s.remove();}
        for (Item s: HologramManager.getInstance().itemToRemove) {s.remove();}
    }

    public void addHologramToRemove (ArmorStand o) {
        List<ArmorStand> l = HologramManager.getInstance().crateHologram.containsKey("toRemove") ? HologramManager.getInstance().crateHologram.get("toRemove") : new ArrayList<>();
        l.add(o);
        HologramManager.getInstance().crateHologram.put("toRemove", l);
    }

    public void displayAllHologram () {
        for (Crates o : CratesManager.getInstance().crates) {
            displayHologram(o.getHologramLocation(),o.getHologramAmbient(), o.getCrateName());
        }
    }

    public void displayHologram (Location hologramLocation, List<String> hologramLines, String crateName) {
        Location loc = hologramLocation.clone();
        for (String l : hologramLines) {
            spawnHologram(loc, l, crateName);
            loc.setY(loc.getY()+0.3);
        }
    }

    public ArmorStand spawnItemHologram (Location l, ItemStack itemStack, String crateName) {
        Item i = Bukkit.getWorld("world").dropItem(l, itemStack);
        Location loc = l.clone();
        loc.setY(loc.getY()+0.5);
        i.setPickupDelay(1000000);
        ArmorStand as = (ArmorStand) l.getWorld().spawnCreature(loc, EntityType.ARMOR_STAND);
        as.setPassenger(i);
        as.setGravity(false);
        as.setVisible(false);
        itemToRemove.add(i);
        List<ArmorStand> li = HologramManager.getInstance().crateHologram.containsKey(crateName) ? HologramManager.getInstance().crateHologram.get(crateName) : new ArrayList<>();
        li.add(as);
        HologramManager.getInstance().crateHologram.put(crateName, li);
        return as;
    }


    public void hideOrRevealHologram (List<ArmorStand> armorStandList, boolean visible) {
        if (armorStandList.isEmpty()) {
            return;
        }
        if (!visible) {
            for (ArmorStand armorStand : armorStandList) {
                armorStand.setCustomNameVisible(false);
            }
        } else {
            for (ArmorStand armorStand : armorStandList) {
                if (armorStand.getCustomName()!=null) {
                    armorStand.setCustomNameVisible(true);
                }
            }
        }
    }

    public void updateRollArmorstand (RollAnimation rollAnimation, ArmorStand itemHologram, ArmorStand nameHologram) {
        int indexInList = rollAnimation.getIndexInList()%rollAnimation.getRewards().size();
        Reward reward = rollAnimation.getRewards().get(indexInList);
        ItemStack item = reward.getItemPresentation().build();
        String customName = Convert.colorString(rollAnimation.getHologramOnRolling().replaceAll("%item-name%",(reward.getItemPresentation().build().hasItemMeta() && reward.getItemPresentation().build().getItemMeta().hasDisplayName()) ? item.getItemMeta().getDisplayName() : item.getType().name()).replaceAll("%chance%", reward.getProbability()+"").replaceAll("%amount%", item.getAmount()+""));
        nameHologram.setCustomName(customName);
        ItemStack itemInHologram = item.clone();
        itemInHologram.setAmount(1);
        ((Item) itemHologram.getPassenger()).setItemStack(itemInHologram);
        rollAnimation.setIndexInList(rollAnimation.getIndexInList()+1);

    }

    public ArmorStand spawnHologram (Location l, String line, String crateName) {
        ArmorStand as = (ArmorStand) Bukkit.getWorld("world").spawnEntity(l, EntityType.ARMOR_STAND);
        as.setGravity(false);
        if (line!=null && !line.equals("")) {
            if (line.contains("&u")) {
                hologramColored.put(as,line);
            }
            as.setCustomName(line);
            as.setCustomNameVisible(true);
        } else {
            as.setCustomNameVisible(false);
        }
        as.setVisible(false);
        List<ArmorStand> li = HologramManager.getInstance().crateHologram.containsKey(crateName) ? HologramManager.getInstance().crateHologram.get(crateName) : new ArrayList<>();
        li.add(as);
        HologramManager.getInstance().crateHologram.put(crateName, li);
        return as;
    }

    public void launchHologramMultiColor() {
        colorList= Data.colorListHologram;
        if (!isRunnableMultiColorStarted) {
            isRunnableMultiColorStarted=true;
            new BukkitRunnable() {
                @Override
                public void run() {
                    List<ArmorStand> copy = new ArrayList<>(hologramColored.keySet());
                    for (ArmorStand a: copy) {
                        a.setCustomName(hologramColored.get(a).replaceAll("&u",Convert.colorString(colorList.get(i%colorList.size()))));
                    }
                    i++;
                }
            }.runTaskTimer(getMainInstance(),0,Data.refreshTime);
        }
    }
}

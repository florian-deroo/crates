package fr.flushfr.crates.managers;

import fr.flushfr.crates.objects.Crates;
import fr.flushfr.crates.objects.Reward;
import fr.flushfr.crates.objects.animation.process.EpicSwordAnimation;
import fr.flushfr.crates.objects.animation.process.RollAnimation;
import fr.flushfr.crates.utils.Utils;
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

import static fr.flushfr.crates.Main.*;

public class HologramManager {

    public HashMap<String, List<ArmorStand>> crateHologram = new HashMap<>();

    public List<Item> itemToRemove = new ArrayList<>();
    public HashMap<ArmorStand, String> hologramColored = new HashMap<>();
    public List<String> colorList = new ArrayList<>();
    boolean isRunnableMultiColorStarted;
    int i = 0;

    public void removeAllHologram () {
        for (EpicSwordAnimation ao : getAnimationManager().animationStarted) {ao.getArmorstand().remove();}
        for (String s: crateHologram.keySet()) {for (ArmorStand h : crateHologram.get(s)) {h.remove();}}
        for (ArmorStand s: hologramColored.keySet()) {s.remove();}
        for (Item s: getHologramManager().itemToRemove) {s.remove();}
    }

    public void addHologramToRemove (ArmorStand o) {
        List<ArmorStand> l = getHologramManager().crateHologram.containsKey("toRemove") ? getHologramManager().crateHologram.get("toRemove") : new ArrayList<>();
        l.add(o);
        getHologramManager().crateHologram.put("toRemove", l);
    }

    public void displayAllHologram () {
        for (Crates o : getCratesManager().crates) {
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
        List<ArmorStand> li = getHologramManager().crateHologram.containsKey(crateName) ? getHologramManager().crateHologram.get(crateName) : new ArrayList<>();
        li.add(as);
        getHologramManager().crateHologram.put(crateName, li);
        return as;
    }


    public void hideOrRevealHologram (List<ArmorStand> armorStandList, boolean visible) {
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
        String customName = Utils.color(rollAnimation.getHologramOnRolling().replaceAll("%item-name%", item.getItemMeta().getDisplayName()).replaceAll("%chance%", reward.getProbability()+"").replaceAll("%amount%", item.getAmount()+""));
        if (reward.getItemPresentation().build().getItemMeta().getDisplayName().equals("")) {
            customName = Utils.color(rollAnimation.getHologramOnRolling().replaceAll("%item-name%", item.getType().name()).replaceAll("%chance%", reward.getProbability()+"").replaceAll("%amount%", item.getAmount()+""));
        }
        nameHologram.setCustomName(customName);
        ItemStack itemInHologram = item.clone();
        itemInHologram.setAmount(1);
        ((Item) itemHologram.getPassenger()).setItemStack(itemInHologram);
        rollAnimation.setIndexInList(rollAnimation.getIndexInList()+1);

    }

    public ArmorStand spawnHologram (Location l, String line, String crateName) {
        ArmorStand as = (ArmorStand) Bukkit.getWorld("world").spawnEntity(l, EntityType.ARMOR_STAND);
        as.setGravity(false);
        if (!line.equals("")) {
            if (line.contains("&u")) {
                hologramColored.put(as,line);
            }
            as.setCustomName(line);
            as.setCustomNameVisible(true);
        } else {
            as.setCustomNameVisible(false);
        }
        as.setVisible(false);
        List<ArmorStand> li = getHologramManager().crateHologram.containsKey(crateName) ? getHologramManager().crateHologram.get(crateName) : new ArrayList<>();
        li.add(as);
        getHologramManager().crateHologram.put(crateName, li);
        return as;
    }

    public void launchHologramMultiColor() {
        colorList=getMainInstance().getConfig().getStringList("hologram.colored-animation.color-list");
        if (!isRunnableMultiColorStarted) {
            isRunnableMultiColorStarted=true;
            new BukkitRunnable() {
                @Override
                public void run() {
                    List<ArmorStand> copy = new ArrayList<>(hologramColored.keySet());
                    for (ArmorStand a: copy) {
                        a.setCustomName(hologramColored.get(a).replaceAll("&u",Utils.color(colorList.get(i%colorList.size()))));
                    }
                    i++;
                }
            }.runTaskTimer(getMainInstance(),getMainInstance().getConfig().getInt("hologram.colored-animation.refreshColorTime"),getMainInstance().getConfig().getInt("hologram.colored-animation.refreshColorTime"));
        }
    }
}

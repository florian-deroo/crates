package fr.flushfr.crates.listeners;

import fr.flushfr.crates.animations.Animations;
import fr.flushfr.crates.managers.AnimationManager;
import fr.flushfr.crates.managers.CratesManager;
import fr.flushfr.crates.objects.Crates;
import fr.flushfr.crates.objects.Messages;
import fr.flushfr.crates.objects.Reward;
import fr.flushfr.crates.objects.animation.process.CSGOAnimation;
import fr.flushfr.crates.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static fr.flushfr.crates.Main.getMainInstance;


public class PlayerListener implements Listener {

    @EventHandler
    public void inventoryCloseEvent (InventoryCloseEvent e) {
        for (CSGOAnimation c : AnimationManager.getInstance().csgoAnimations) {
            if (c.getPlayer().equals(e.getPlayer())) {
                Animations.getInstance().reOpenInventory.add((Player) e.getPlayer());
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getClickedBlock()==null) {
            return;
        }
        if (!(e.getAction()== Action.RIGHT_CLICK_BLOCK || e.getAction()== Action.LEFT_CLICK_BLOCK)) {
            return;
        }
        if (getMainInstance().isDisable) {
            return;
        }
        for (Crates c: CratesManager.getInstance().crates) {
            if(e.getClickedBlock().getLocation().getX()==c.getCrateLocation().getX() && e.getClickedBlock().getLocation().getY()==c.getCrateLocation().getY() && e.getClickedBlock().getLocation().getZ()==c.getCrateLocation().getZ()) {
                if (e.getAction()== Action.RIGHT_CLICK_BLOCK) {
                    if (playerHoldKeyAndRemove(e.getPlayer(), c)) {
                        CratesManager.getInstance().startAnimationList(c, e.getPlayer());
                    } else {
                        e.getPlayer().sendMessage(Messages.noKey);
                        Location l = e.getClickedBlock().getLocation();
                        l.setY(l.getY()-1);
                        e.getPlayer().setVelocity(l.toVector().subtract(e.getPlayer().getLocation().toVector()).normalize().multiply(-1));
                    }
                } else {
                    openCrateInventory(e.getClickedBlock().getLocation(), e.getPlayer());
                }
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void inventoryClick (InventoryClickEvent e) {
        if (getMainInstance().isDisable) {return;}
        if(e.getSlot()!=-999 && CratesManager.getInstance().crates!= null && CratesManager.getInstance().cratesName.contains(e.getClickedInventory().getName()) && !getMainInstance().isDisable) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void blockBreak (BlockBreakEvent e) {
        if (getMainInstance().isDisable) {return;}
        for (Location loc: CratesManager.getInstance().protectedLocation.keySet()) {
            if (e.getBlock().getLocation().getX()==loc.getX() && e.getBlock().getLocation().getY()==loc.getY() && e.getBlock().getLocation().getZ()==loc.getZ()) {
                e.setCancelled(true);
            }
        }
    }

    public boolean playerHoldKeyAndRemove (Player p, Crates crates) {
        ItemStack itemInHand =  p.getInventory().getItemInHand();
        ItemStack item = crates.getKeyItem().build();
        if (Utils.isSimilar(itemInHand, item, true)) {
            if(p.getInventory().getItemInHand().getAmount()==1) {
                p.getInventory().setItemInHand(new ItemStack(Material.AIR));
            } else {
                p.getInventory().getItemInHand().setAmount(p.getInventory().getItemInHand().getAmount()-1);
            }
            return true;
        }
        return false;
    }

    public void openCrateInventory (Location loc, Player p) {
        Inventory inv = null;
        for (Crates c : CratesManager.getInstance().crates) {
            if(CratesManager.getInstance().protectedLocation.get(loc).equals(c.getCrateName())) {
                inv = Bukkit.createInventory(null, c.getRowsPreview()*9, c.getInventoryNamePreview());
                for (Reward r: c.getRewards()) {
                    inv.addItem(r.getItemPresentation().build());
                }
            }
        }
        p.openInventory(inv);
    }
}

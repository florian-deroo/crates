package fr.flushfr.crates.managers;

import fr.flushfr.crates.objects.Crates;
import fr.flushfr.crates.objects.Messages;
import fr.flushfr.crates.objects.Reward;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static fr.flushfr.crates.Main.*;

public class RewardManager {

    public boolean hasPlayerMissedRewards (String player) {
        return !getMissedRewardsFile().getMissedRewardsFile().getStringList(player).isEmpty();
    }
    public void addMissedReward (String player, String crateName, int amount) {
        List<String> listPreviousReward = getMissedRewardsFile().getMissedRewardsFile().getStringList(player);
        List<String> listReward = getMissedRewardsFile().getMissedRewardsFile().getStringList(player);
        boolean alreadyAdded = false;
        for (String rewards: listPreviousReward) {
            if (rewards.split(":")[0].equals(crateName) && !alreadyAdded) {
                listReward.add(crateName+":"+Integer.parseInt(rewards.split(":")[1])+amount);
                alreadyAdded = true;
            } else {
                listReward.add(rewards);
            }
        }
        if (!alreadyAdded) {
            listReward.add(crateName+":"+amount);
        }
        getMissedRewardsFile().getMissedRewardsFile().set(player, listReward);
        getMissedRewardsFile().saveMissedRewardsFile();
        getMissedRewardsFile().reloadMissedRewardsFile();
    }

    public Reward getRandomRewardFromCrate (Crates c) {
        int totalProbability = c.getTotalProbability();
        int numberSelected = new Random().nextInt(totalProbability);
        int i = 1;
        for (Reward r : c.getRewards()) {
            i+=r.getProbability();
            if (numberSelected<i) {
                return r;
            }
        }
        return null;
    }

    public void giveKeyToPlayer (String player, Crates crate, int amount) {
        if (Bukkit.getPlayer(player).isOnline()) {
            Player p = Bukkit.getPlayer(player);
            if (!getRewardManager().isInvFull(p.getInventory())) {
                ItemStack key = crate.getKeyItem().build();
                key.setAmount(amount);
                p.getInventory().addItem(key);

            } else {
                getRewardManager().addMissedReward(player, crate.getCrateName(), amount);
                p.sendMessage(Messages.addMissedKeyDueToFullInventory);
            }
        } else {
            getRewardManager().addMissedReward(player, crate.getCrateName(), amount);
        }
    }

    public void giveRewardToPlayer (Reward r, Player p) {
        for (String commands : r.getCommands()) {Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commands.replaceAll("%player%",p.getName()));}
        giveItemToPlayer(p, r.getItemToGive().build());
    }

    public void giveMissedRewards (String player) {
        for (String rewards: getMissedRewardsFile().getMissedRewardsFile().getStringList(player)) {
            int amount = Integer.parseInt(rewards.split(":")[1]);
            Crates crate = getCratesManager().matchCrate(rewards.split(":")[0]);
            if (crate!=null) {
                giveKeyToPlayer(player, crate, amount);
                getMissedRewardsFile().getMissedRewardsFile().set(player, new ArrayList<>());
            }
        }
        getMissedRewardsFile().saveMissedRewardsFile();
        getMissedRewardsFile().reloadMissedRewardsFile();
    }

    public void giveItemToPlayer (Player p, ItemStack reward) {
        if (!isInvFull(p.getInventory())) {
            p.getInventory().addItem(reward);
        } else {
            Bukkit.getWorld(p.getWorld().getName()).dropItemNaturally(p.getLocation(), reward);
            p.sendMessage(Messages.dropItemInventoryFull);
        }
    }

    public boolean isInvFull(Inventory playerInventory) {
        return !Arrays.asList(playerInventory.getContents()).contains(null);
    }


}

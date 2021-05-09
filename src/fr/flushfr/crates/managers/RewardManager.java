package fr.flushfr.crates.managers;

import fr.flushfr.crates.objects.Crates;
import fr.flushfr.crates.objects.Messages;
import fr.flushfr.crates.objects.Reward;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RewardManager {

    private static RewardManager instance;
    public RewardManager () {
        instance = this;
    }
    public static RewardManager getInstance() {
        return instance;
    }

    public boolean hasPlayerMissedRewards (String player) {
        return !FileManager.getInstance().getMissedRewardConfig().getStringList(player).isEmpty();
    }
    public void addMissedReward (String player, String crateName, int amount) {
        List<String> listPreviousReward = FileManager.getInstance().getMissedRewardConfig().getStringList(player);
        List<String> listReward = new ArrayList<>();
        boolean alreadyAdded = false;
        for (String rewards: listPreviousReward) {
            if (rewards.split(":")[0].equals(crateName)) {
                int i = Integer.parseInt(rewards.split(":")[1])+amount;
                listReward.add(crateName+":"+i);
                alreadyAdded=true;
                break;
            } else {
                listReward.add(rewards);
            }
        }
        if (!alreadyAdded) {
            listReward.add(crateName+":"+amount);
        }
        FileManager.getInstance().getMissedRewardConfig().set(player, listReward);
        FileManager.getInstance().saveMissedRewardConfig();
        FileManager.getInstance().reloadMissedRewardsConfig();
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
        try {
            Player p = Bukkit.getPlayerExact(player);
            if (RewardManager.getInstance().isInvFull(p.getInventory())) {
                ItemStack key = crate.getKeyItem().build();
                key.setAmount(amount);
                p.getInventory().addItem(key);
                p.sendMessage(Messages.receivedKey);
            } else {
                RewardManager.getInstance().addMissedReward(player, crate.getCrateName(), amount);
                p.sendMessage(Messages.addMissedKeyDueToFullInventory);
            }
        } catch (NullPointerException e) {
            RewardManager.getInstance().addMissedReward(player, crate.getCrateName(), amount);
        }
    }

    public void giveRewardToPlayer (Reward r, Player p) {
        for (String commands : r.getCommands()) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commands.replaceAll("%player%",p.getName()));
        }
        if (r.isGiveItemDisplay()) {
            giveItemToPlayer(p, r.getItemToGive().build());
        }
    }

    public void giveMissedRewards (String player) {
        for (String rewards: FileManager.getInstance().getMissedRewardConfig().getStringList(player)) {
            int amount = Integer.parseInt(rewards.split(":")[1]);
            Crates crate = CratesManager.getInstance().matchCrate(rewards.split(":")[0]);
            if (crate!=null) {
                giveKeyToPlayer(player, crate, amount);
                FileManager.getInstance().getMissedRewardConfig().set(player, new ArrayList<>());
            }
        }
        FileManager.getInstance().saveMissedRewardConfig();
        FileManager.getInstance().reloadMissedRewardsConfig();
    }

    public void giveItemToPlayer (Player p, ItemStack reward) {
        if (isInvFull(p.getInventory())) {
            p.getInventory().addItem(reward);
        } else {
            Bukkit.getWorld(p.getWorld().getName()).dropItemNaturally(p.getLocation(), reward);
            p.sendMessage(Messages.inventoryFull);
        }
    }

    public boolean isInvFull(Inventory playerInventory) {
        return Arrays.asList(playerInventory.getContents()).contains(null);
    }


}

package fr.flushfr.crates.commands;

import fr.flushfr.crates.managers.CratesManager;
import fr.flushfr.crates.managers.FileManager;
import fr.flushfr.crates.managers.RewardManager;
import fr.flushfr.crates.objects.Crates;
import fr.flushfr.crates.objects.Data;
import fr.flushfr.crates.objects.Messages;
import fr.flushfr.crates.utils.Convert;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

import static fr.flushfr.crates.Main.getMainInstance;

public class CratesCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length == 0) {
            if (commandSender.hasPermission(Data.adminPermission)) {
                commandSender.sendMessage(Messages.errorCommandAdmin);
            } else {
                commandSender.sendMessage(Messages.errorCommandPlayer);
            }
            return true;
        }
        if (!commandSender.hasPermission(Data.playerPermission)) {
            commandSender.sendMessage(Messages.permissionDenied);
            return true;
        }
        if (args[0].toLowerCase().equals("claim")) {
            if (RewardManager.getInstance().hasPlayerMissedRewards(commandSender.getName())) {
                RewardManager.getInstance().giveMissedRewards(commandSender.getName());
            } else {
                commandSender.sendMessage(Messages.noMissedRewards);
            }
            return true;
        }
        if (!commandSender.hasPermission(Data.adminPermission)) {
            commandSender.sendMessage(Messages.permissionDenied);
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "give":
                if (args.length <= 3 ) {
                    commandSender.sendMessage(Messages.errorCommandAdmin);
                    return true;
                }
                Crates crate;
                switch (args[1].toLowerCase()) {
                    case "to":
                        crate = CratesManager.getInstance().matchCrate(args[3]);
                        if (crate==null) {
                            commandSender.sendMessage(Convert.replaceValues(Messages.noCrateMatch, "%crate%", args[3]));
                            return true;
                        }
                        if (!(args.length == 4 || args.length == 5)) {
                            commandSender.sendMessage(Messages.errorCommandAdmin);
                            return true;
                        }
                        try {
                            RewardManager.getInstance().giveKeyToPlayer(args[2], crate, args.length==4 ? 1 : Integer.parseInt(args[4]));
                            commandSender.sendMessage(Convert.replaceValues(Messages.successfullyGive, "%player%", args[2]));
                        } catch (NumberFormatException e) {
                            commandSender.sendMessage(Messages.errorCommandAdmin);
                        }
                        break;
                    case "all":
                        crate = CratesManager.getInstance().matchCrate(args[2]);
                        if (crate==null) {
                            commandSender.sendMessage(Convert.replaceValues(Messages.noCrateMatch, "%crate%", args[2]));
                            return true;
                        }
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            RewardManager.getInstance().giveKeyToPlayer(p.getName(), crate, args.length==4 ? Integer.parseInt(args[3]) : 1);
                        }
                        commandSender.sendMessage(Messages.successfullyGiveAll);
                        return true;
                }
                break;
            case "reload":
            case "enable":
                getMainInstance().reload(commandSender);
                break;
            case "disable":
                 getMainInstance().disable();
                 break;
            case "list":
                 commandSender.sendMessage(FileManager.getInstance().cratesFilesName.toString());
                 break;
        }
        if (args.length == 2) {
            switch (args[0].toLowerCase()) {
                case "set":
                    commandSender.sendMessage(Convert.replaceValues(Messages.cratesLocationSet, "%crate%", args[1]));
                    CratesManager.getInstance().setCratePosition(args[1].toLowerCase(), (((Player) commandSender).getTargetBlock((Set<Material>) null, 10)).getLocation());
                    break;
                case "remove":
                    commandSender.sendMessage(Convert.replaceValues(Messages.cratesLocationRemoved, "%crate%", args[1]));
                    CratesManager.getInstance().removeCratePosition(args[1].toLowerCase());
                    break;
                default:
                    commandSender.sendMessage(Messages.errorCommandAdmin);
                    break;
            }
            return true;
        }

        return true;
    }
}

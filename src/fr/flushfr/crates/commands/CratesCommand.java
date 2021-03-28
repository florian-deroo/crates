package fr.flushfr.crates.commands;

import fr.flushfr.crates.objects.Crates;
import fr.flushfr.crates.objects.Messages;
import fr.flushfr.crates.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;

import static fr.flushfr.crates.Main.*;

public class CratesCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length == 0) {
            Effect.valueOf("eeeee");
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "give":
                if (args.length < 4) {
                    commandSender.sendMessage(Messages.errorArgGiveCommand);
                    break;
                }
                Crates crate = getCratesManager().matchCrate(args[3]);
                if (crate==null) {
                    commandSender.sendMessage(Messages.noCrateMatch);
                    return true;
                }
                switch (args[1].toLowerCase()) {
                    case "to":
                        try {
                            getRewardManager().giveKeyToPlayer(args[2], crate, args.length<5 ? 1 : Integer.parseInt(args[4]));
                            commandSender.sendMessage(Utils.replace(Messages.successfullyGive, "%player%", args[2]));
                        } catch (NumberFormatException | NullPointerException e) {
                            Bukkit.broadcastMessage(args[2]);
                        }
                        break;
                    case "all":
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            getRewardManager().giveKeyToPlayer(p.getName(), crate, args.length<5 ? 1 : Integer.parseInt(args[4]));
                        }
                        commandSender.sendMessage(Messages.successfullyGiveAll);
                        return true;
                }
                break;
            case "claim":
                if (getRewardManager().hasPlayerMissedRewards(commandSender.getName())) {
                    getRewardManager().giveMissedRewards(commandSender.getName());
                } else {
                    commandSender.sendMessage(Messages.noMissedRewards);
                }
                break;
            case "reload":
            case "enable":
                 getMainInstance().reload((Player) commandSender);
                 break;
            case "disable":
                 getMainInstance().disable();
                 break;
            case "list":
                 commandSender.sendMessage(getCratesFile().cratesFilesName.toString());
                 break;
        }
        if (args.length == 2) {
            switch (args[0].toLowerCase()) {
                case "info":
                    sendCratesInformation(commandSender, args[1].toLowerCase());
                    break;
                case "preview":

                    break;
                case "set":
                    commandSender.sendMessage(Messages.cratesLocationSet);
                    getCratesManager().setCratePosition(args[1].toLowerCase(), ((Player) commandSender).getTargetBlock((HashSet<Byte>) null, 5).getLocation());
                    break;
                case "remove":
                    commandSender.sendMessage(Messages.cratesLocationRemoved);
                    getCratesManager().removeCratePosition(args[1].toLowerCase());
                    break;
                default:
                    commandSender.sendMessage(Messages.errorCommand);
                    break;
            }
            return true;
        }

        return true;
    }


    public void sendCratesInformation (CommandSender commandSender, String crate) {

    }

}

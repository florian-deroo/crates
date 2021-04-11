package fr.flushfr.crates.commands;

import fr.flushfr.crates.managers.CratesManager;
import fr.flushfr.crates.objects.Data;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class CrateTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        List<String> newList = new ArrayList<>();
        if (args.length==1) {
            if (commandSender.hasPermission(Data.adminPermission)) {
                newList.add("give");
                newList.add("reload");
                newList.add("enable");
                newList.add("disable");
                newList.add("list");
                newList.add("info");
                newList.add("set");
                newList.add("remove");
            }
            newList.add("claim");
        }
        if (commandSender.hasPermission(Data.adminPermission)) {
            if (args.length==2) {
                switch (args[0].toLowerCase()) {
                    case "give":
                        newList.add("to");
                        newList.add("all");
                        break;
                    case "remove":
                    case "set":
                        newList.addAll(CratesManager.getInstance().cratesName);
                        break;
                }
            }
            if (args.length==4) {
                if (args[0].toLowerCase().equals("give") && (args[1].toLowerCase().equals("to") || args[1].toLowerCase().equals("all"))) {
                    newList.addAll(CratesManager.getInstance().cratesName);
                }
            }
        }
        if (newList.isEmpty()) {
            return null;
        }
        return newList;
    }

}

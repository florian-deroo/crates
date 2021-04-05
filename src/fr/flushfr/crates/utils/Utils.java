package fr.flushfr.crates.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Utils {
    public static String color(String line) {
        return ChatColor.translateAlternateColorCodes('&', line);
    }


    public static String[] colorListToArray(List<String> args) {
        List<String> coloredList = new ArrayList<>();
        for (String s:args)
            coloredList.add(ChatColor.translateAlternateColorCodes('&', s));
        return listToArray(coloredList);
    }

    public static String[] listToArray(List<String> args) {
        String[] array = new String[args.size()];
        array = args.toArray(array);
        return array;
    }

    public static List<String> colorList(List<String> args) {
        List<String> coloredList = new ArrayList<>();
        for (String s:args)
            coloredList.add(ChatColor.translateAlternateColorCodes('&', s));
        return coloredList;
    }


    public static List<String> colorListReplace(List<String> args, HashMap<String, String> toReplace) {
        List<String> coloredList = new ArrayList<>();
        for (String s:args) {
            for (String replace: toReplace.keySet()) {
                coloredList.add(ChatColor.translateAlternateColorCodes('&', s.replaceAll(replace, toReplace.get(replace))));
            }
        }
        return coloredList;
    }

    public static String[] replace (String[] args, HashMap<String, String> toReplace) {
        List<String> replacedList = new ArrayList<>();
        String replaced;
        for (String s:args) {
            replaced = s;
            for (String stringToReplace: toReplace.keySet()) {
                replaced = replaced.replaceAll(stringToReplace, toReplace.get(stringToReplace));
            }
            replacedList.add(replaced);
        }
        return listToArray(replacedList);
    }

    public static boolean isSimilar(ItemStack first, ItemStack second, boolean ignoreAmount){

        boolean similar = false;

        if(first == null || second == null){
            return similar;
        }

        boolean sameTypeId = (first.getTypeId() == second.getTypeId());
        boolean sameDurability = (first.getDurability() == second.getDurability());
        boolean sameAmount = (first.getAmount() == second.getAmount());
        boolean sameHasItemMeta = (first.hasItemMeta() == second.hasItemMeta());
        boolean sameEnchantments = (first.getEnchantments().equals(second.getEnchantments()));
        boolean sameItemMeta = true;

        if(sameHasItemMeta) {
            sameItemMeta = Bukkit.getItemFactory().equals(first.getItemMeta(), second.getItemMeta());
        }

        if(sameTypeId && sameDurability && (sameAmount || ignoreAmount) && sameHasItemMeta && sameEnchantments && sameItemMeta){
            similar = true;
        }

        return similar;

    }

    public static String[] replace (String[] args, String value, String toReplace) {
        HashMap<String, String> toReplaceHashMap = new HashMap<>();
        toReplaceHashMap.put(value, toReplace);
        return replace(args, toReplaceHashMap);
    }

    public static String[] replace (String[] args, String value1, String toReplace1,  String value2, String toReplace2) {
        HashMap<String, String> toReplaceHashMap = new HashMap<>();
        toReplaceHashMap.put(value1, toReplace1);
        toReplaceHashMap.put(value2, toReplace2);
        return replace(args, toReplaceHashMap);
    }

    public static String[] replace (String[] args, String value1, String toReplace1,  String value2, String toReplace2, String value3, String toReplace3) {
        HashMap<String, String> toReplaceHashMap = new HashMap<>();
        toReplaceHashMap.put(value1, toReplace1);
        toReplaceHashMap.put(value2, toReplace2);
        toReplaceHashMap.put(value3, toReplace3);
        return replace(args, toReplaceHashMap);
    }

    public static String[] replace (String[] args, String value1, String toReplace1,  String value2, String toReplace2, String value3, String toReplace3, String value4, String toReplace4) {
        HashMap<String, String> toReplaceHashMap = new HashMap<>();
        toReplaceHashMap.put(value1, toReplace1);
        toReplaceHashMap.put(value2, toReplace2);
        toReplaceHashMap.put(value3, toReplace3);
        toReplaceHashMap.put(value4, toReplace4);
        return replace(args, toReplaceHashMap);
    }

    public static String[] replace (String[] args, String value1, String toReplace1,  String value2, String toReplace2, String value3, String toReplace3, String value4, String toReplace4,  String value5, String toReplace5) {
        HashMap<String, String> toReplaceHashMap = new HashMap<>();
        toReplaceHashMap.put(value1, toReplace1);
        toReplaceHashMap.put(value2, toReplace2);
        toReplaceHashMap.put(value3, toReplace3);
        toReplaceHashMap.put(value4, toReplace4);
        toReplaceHashMap.put(value5, toReplace5);
        return replace(args, toReplaceHashMap);
    }

    public static Color getColor (String color) {
        switch (color.toLowerCase()){
            case "aqua":
                return Color.AQUA;
            case "white":
                return Color.WHITE;
            case "black":
                return Color.BLACK;
            case "yellow":
                return Color.YELLOW;
            case "blue":
                return Color.BLUE;
            case "orange":
                return Color.ORANGE;
            case "green":
                return Color.GREEN;
            case "fuchsia":
                return Color.FUCHSIA;
            case "gray":
                return Color.GRAY;
            case "lime":
                return Color.LIME;
            case "maroon":
                return Color.MAROON;
            case "navy":
                return Color.NAVY;
            case "olive":
                return Color.OLIVE;
            case "purple":
                return Color.PURPLE;
            case "silver":
                return Color.SILVER;
            case "red":
                return Color.RED;
            case "teal":
                return Color.TEAL;
        }
        return null;
    }

    public static void sendMessageList(CommandSender p, List<String> l) {for (String s:colorList(l))p.sendMessage(s);}
}

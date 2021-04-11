package fr.flushfr.crates.utils;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Convert {

    public static String colorString(String line) {
        return ChatColor.translateAlternateColorCodes('&', line);
    }

    public static String[] colorArray(String[] args) {
        List<String> coloredList = new ArrayList<>();
        for (String s:args)
            coloredList.add(ChatColor.translateAlternateColorCodes('&', s));
        return Convert.listToArray(coloredList);
    }

    public static List<String> colorList(List<String> args) {
        List<String> coloredList = new ArrayList<>();
        for (String s:args)
            coloredList.add(colorString(s));
        return coloredList;
    }

    public static String[] colorListToArray(List<String> args) {
        List<String> coloredList = new ArrayList<>();
        for (String s:args)
            coloredList.add(colorString(s));
        return listToArray(coloredList);
    }


    public static String[] listToArray(List<String> args) {
        String[] array = new String[args.size()];
        array = args.toArray(array);
        return array;
    }

    public static String[] replaceValues (String[] args, HashMap<String, String> toReplace) {
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

    public static String[] replaceValues (String[] args, String value, String toReplace) {
        HashMap<String, String> toReplaceHashMap = new HashMap<>();
        toReplaceHashMap.put(value, toReplace);
        return replaceValues(args, toReplaceHashMap);
    }

    public static String[] replaceValues (String[] args, String value1, String toReplace1,  String value2, String toReplace2) {
        HashMap<String, String> toReplaceHashMap = new HashMap<>();
        toReplaceHashMap.put(value1, toReplace1);
        toReplaceHashMap.put(value2, toReplace2);
        return replaceValues(args, toReplaceHashMap);
    }

    public static String[] replaceValues (String[] args, String value1, String toReplace1,  String value2, String toReplace2, String value3, String toReplace3) {
        HashMap<String, String> toReplaceHashMap = new HashMap<>();
        toReplaceHashMap.put(value1, toReplace1);
        toReplaceHashMap.put(value2, toReplace2);
        toReplaceHashMap.put(value3, toReplace3);
        return replaceValues(args, toReplaceHashMap);
    }

    public static String[] replace (String[] args, String value1, String toReplace1,  String value2, String toReplace2, String value3, String toReplace3, String value4, String toReplace4) {
        HashMap<String, String> toReplaceHashMap = new HashMap<>();
        toReplaceHashMap.put(value1, toReplace1);
        toReplaceHashMap.put(value2, toReplace2);
        toReplaceHashMap.put(value3, toReplace3);
        toReplaceHashMap.put(value4, toReplace4);
        return replaceValues(args, toReplaceHashMap);
    }

    public static String[] replaceValues (String[] args, String value1, String toReplace1,  String value2, String toReplace2, String value3, String toReplace3, String value4, String toReplace4,  String value5, String toReplace5) {
        HashMap<String, String> toReplaceHashMap = new HashMap<>();
        toReplaceHashMap.put(value1, toReplace1);
        toReplaceHashMap.put(value2, toReplace2);
        toReplaceHashMap.put(value3, toReplace3);
        toReplaceHashMap.put(value4, toReplace4);
        toReplaceHashMap.put(value5, toReplace5);
        return replaceValues(args, toReplaceHashMap);
    }

}

package fr.flushfr.crates.utils;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;

public class Utils {

    public static boolean isItemSimilar(ItemStack first, ItemStack second, boolean ignoreAmount){

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
}

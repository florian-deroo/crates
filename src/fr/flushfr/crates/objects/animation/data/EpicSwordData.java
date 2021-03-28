package fr.flushfr.crates.objects.animation.data;

import org.bukkit.inventory.ItemStack;

public class EpicSwordData {
    private int startSwordPosition;
    private ItemStack item1;
    private ItemStack item2;

    public EpicSwordData(int startSwordPosition, ItemStack item1, ItemStack item2) {
        this.startSwordPosition = startSwordPosition;
        this.item1 = item1;
        this.item2 = item2;
    }

    public int getStartSwordPosition() {return startSwordPosition;}
    public ItemStack getItem1() {return item1;}
    public ItemStack getItem2() {return item2;}
}

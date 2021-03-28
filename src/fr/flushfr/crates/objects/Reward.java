package fr.flushfr.crates.objects;

import fr.flushfr.crates.utils.ItemBuilder;

import java.util.List;

public class Reward {
    private int probability;
    private ItemBuilder itemPresentation;
    private ItemBuilder itemToGive;
    private boolean glowItemPresentation;
    private boolean giveItemDisplay;
    private List<String> command;

    public Reward(int probability, List<String> command, ItemBuilder itemPresentation, ItemBuilder itemToGive, boolean glowItemPresentation, boolean giveItemDisplay) {
        this.probability = probability;
        this.command = command;
        this.itemPresentation = itemPresentation;
        this.itemToGive = itemToGive;
        this.glowItemPresentation = glowItemPresentation;
        this.giveItemDisplay = giveItemDisplay;
    }

    public List<String> getCommands() {return command;}
    public int getProbability() {return probability;}
    public ItemBuilder getItemPresentation() {return itemPresentation;}
    public ItemBuilder getItemToGive() {return itemToGive;}
    public boolean isGlowItemPresentation() {return glowItemPresentation;}
    public boolean isGiveItemDisplay() {return giveItemDisplay;}
}

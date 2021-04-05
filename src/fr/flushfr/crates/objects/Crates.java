package fr.flushfr.crates.objects;

import fr.flushfr.crates.utils.ItemBuilder;
import org.bukkit.Location;

import java.util.List;

public class Crates {
    private String crateName;
    private List<Object> animationList;
    private ItemBuilder keyItem;
    private List<Reward> rewards;
    private boolean isPreviewEnable;
    private int rowsPreview;
    private Location crateLocation;
    private Location hologramLocation;
    private String inventoryNamePreview;
    private List<String> hologramAmbient;
    private int totalProbability;

    public Crates(String crateName, List<Object> animationList, ItemBuilder keyItem, List<Reward> rewards, boolean isPreviewEnable, int rowsPreview, Location crateLocation, Location hologramLocation, String inventoryNamePreview, List<String> hologramAmbient, int totalProbability) {
        this.crateName = crateName;
        this.animationList = animationList;
        this.keyItem = keyItem;
        this.rewards = rewards;
        this.isPreviewEnable = isPreviewEnable;
        this.rowsPreview = rowsPreview;
        this.crateLocation = crateLocation;
        this.hologramLocation = hologramLocation;
        this.inventoryNamePreview = inventoryNamePreview;
        this.hologramAmbient = hologramAmbient;
        this.totalProbability = totalProbability;
    }

    public boolean isPreviewEnable() {return isPreviewEnable;}
    public int getRowsPreview() {return rowsPreview;}
    public List<Reward> getRewards() {return rewards;}
    public ItemBuilder getKeyItem() {return keyItem;}
    public String getCrateName() {return crateName;}
    public List<String> getHologramAmbient() {return hologramAmbient;}
    public String getInventoryNamePreview() {return inventoryNamePreview;}
    public Location getCrateLocation() {return crateLocation;}
    public void setCrateLocation(Location l) {crateLocation = l;}
    public Location getHologramLocation() {return hologramLocation;}
    public int getTotalProbability() {return totalProbability;}
    public List<Object> getAnimationList() {return animationList;}
}

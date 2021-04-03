package fr.flushfr.crates.objects.animation.process;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

public class SimpleRotationAnimation {
    private ArmorStand armorStand;
    private ArmorStand armorStandRewardName;
    private Location location;
    private int iterator;
    private boolean stop;

    public SimpleRotationAnimation(ArmorStand armorStand, Location location) {
        this.location = location;
        this.armorStand = armorStand;
        this.iterator = 0;
        this.stop = false;
    }


    public ArmorStand getArmorStand() {
        return armorStand;
    }

    public void setArmorStand(ArmorStand armorStand) {
        this.armorStand = armorStand;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getIterator() {
        return iterator;
    }

    public void setIterator(int iterator) {
        this.iterator = iterator;
    }

    public boolean isStop() {
        return stop;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    public ArmorStand getArmorStandRewardName() {
        return armorStandRewardName;
    }

    public void setArmorStandRewardName(ArmorStand armorStandRewardName) {
        this.armorStandRewardName = armorStandRewardName;
    }
}

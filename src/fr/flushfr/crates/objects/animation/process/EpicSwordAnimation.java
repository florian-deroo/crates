package fr.flushfr.crates.objects.animation.process;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

public class EpicSwordAnimation {
    private Location defaultArmorstandLocation;
    private Location armorstandLocation;
    private ArmorStand as;
    private int teta;
    private double deltahigh;
    private Location particleLocation;
    private boolean changeSword;
    private boolean stop;

    public EpicSwordAnimation(Location l) {
        defaultArmorstandLocation= new Location(l.getWorld(),  Math.round(l.getX())+1,Math.round(l.getY())+0.2,Math.round(l.getZ())+1);
        this.armorstandLocation= new Location(l.getWorld(),  Math.round(l.getX())+1,Math.round(l.getY())+0.2,Math.round(l.getZ())+1);
        this.deltahigh=0;
        this.changeSword=false;
        this.as=(ArmorStand) defaultArmorstandLocation.getWorld().spawnEntity(defaultArmorstandLocation, EntityType.ARMOR_STAND);
    }

    public int getTeta() {return teta;}
    public void setTeta(int teta) {this.teta=teta;}

    public ArmorStand getArmorstand() {return as;}

    public Location getParticleLocation() {return particleLocation;}
    public void setParticleLocation(Location particleLocation) {this.particleLocation = particleLocation;}

    public double getDeltahigh() {return deltahigh;}
    public void setDeltahigh(double deltahigh) {this.deltahigh = deltahigh;}

    public Location getArmorstandLocation() {return armorstandLocation;}
    public Location getDefaultArmorstandLocation() {return defaultArmorstandLocation;}

    public boolean isChangeSword() {return changeSword;}
    public void setChangeSword(boolean changeSword) {this.changeSword = changeSword;}

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    public boolean isStop() {
        return stop;
    }
}

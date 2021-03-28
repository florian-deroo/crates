package fr.flushfr.crates.objects.animation.data;

import org.bukkit.Color;

public class FireworkData {

    private Color fireworkColor;
    private Color fadeColor;
    private int lifeTime;
    private int power;
    private boolean trail;
    private boolean flicker;

    public FireworkData(Color fireworkColor, Color fadeColor, int lifeTime, int power, boolean trail, boolean flicker) {
        this.fireworkColor = fireworkColor;
        this.fadeColor = fadeColor;
        this.lifeTime = lifeTime;
        this.power = power;
        this.trail = trail;
        this.flicker = flicker;
    }

    public Color getFadeColor() {
        return fadeColor;
    }

    public Color getFireworkColor() {
        return fireworkColor;
    }

    public int getLifeTime() {
        return lifeTime;
    }

    public int getPower() {
        return power;
    }

    public boolean isTrail() {
        return trail;
    }

    public boolean isFlicker() {
        return flicker;
    }
}

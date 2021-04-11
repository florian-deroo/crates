package fr.flushfr.crates.objects.animation.data;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;

public class FireworkData {

    private Color fireworkColor;
    private Color fadeColor;
    private int lifeTime;
    private FireworkEffect.Type type;
    private int power;
    private boolean trail;
    private boolean flicker;

    public FireworkData(Color fireworkColor, Color fadeColor, int lifeTime, int power, boolean trail, boolean flicker, FireworkEffect.Type type) {
        this.fireworkColor = fireworkColor;
        this.fadeColor = fadeColor;
        this.lifeTime = lifeTime;
        this.power = power;
        this.trail = trail;
        this.flicker = flicker;
        this.type = type;
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

    public FireworkEffect.Type getType() { return type; }
}

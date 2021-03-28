package fr.flushfr.crates.objects.animation.data;

import org.bukkit.Sound;

public class SoundData {
    private Sound sound;
    private float volume;
    private float pitch;

    public SoundData(Sound sound, float volume, float pitch) {
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    public SoundData() {}

    public Sound getSound() {
        return sound;
    }

    public void setSound(Sound sound) { this.sound = sound; }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }
}

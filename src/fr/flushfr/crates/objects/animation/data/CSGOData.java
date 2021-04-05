package fr.flushfr.crates.objects.animation.data;

import org.bukkit.Sound;

public class CSGOData {
    private String inventoryName;
    private SoundData soundOnRolling;
    private SoundData soundEnd;

    public CSGOData(String inventoryName, SoundData soundOnRolling, SoundData soundEnd) {
        this.inventoryName = inventoryName;
        this.soundOnRolling = soundOnRolling;
        this.soundEnd = soundEnd;
    }

    public String getInventoryName() {
        return inventoryName;
    }

    public SoundData getSoundOnRolling() {
        return soundOnRolling;
    }

    public SoundData getSoundEnd() {
        return soundEnd;
    }
}

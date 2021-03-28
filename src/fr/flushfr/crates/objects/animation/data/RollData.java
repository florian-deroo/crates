package fr.flushfr.crates.objects.animation.data;

public class RollData {
    private SoundData sound1;
    private SoundData sound2;
    private String hologramOnRolling;

    public RollData(SoundData sound1, SoundData sound2, String hologramOnRolling) {
        this.sound1 = sound1;
        this.sound2 = sound2;
        this.hologramOnRolling = hologramOnRolling;
    }

    public SoundData getSound2() {
        return sound2;
    }
    public SoundData getSound1() {
        return sound1;
    }
    public String getHologramOnRolling() {
        return hologramOnRolling;
    }
}

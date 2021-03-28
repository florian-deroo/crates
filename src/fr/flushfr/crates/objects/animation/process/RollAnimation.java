package fr.flushfr.crates.objects.animation.process;

import fr.flushfr.crates.objects.Reward;

import java.util.List;

public class RollAnimation {
    private List<Reward> rewards;
    private int i;
    private int indexInList;
    private boolean stop;
    private boolean soundChange;
    private String hologramOnRolling;

    public RollAnimation(List<Reward> rewards, String hologramOnRolling) {
        this.rewards = rewards;
        this.hologramOnRolling = hologramOnRolling;
    }

    public List<Reward> getRewards() {
        return rewards;
    }
    public int getI() {
        return i;
    }
    public void setI(int i) {
        this.i = i;
    }
    public boolean isStop() {
        return stop;
    }
    public void setStop(boolean stop) {
        this.stop = stop;
    }
    public int getIndexInList() {
        return indexInList;
    }
    public void setIndexInList(int indexInList) {
        this.indexInList = indexInList;
    }
    public boolean isSoundChange() {
        return soundChange;
    }
    public void setSoundChange(boolean soundChange) {
        this.soundChange = soundChange;
    }

    public String getHologramOnRolling() {
        return hologramOnRolling;
    }
}

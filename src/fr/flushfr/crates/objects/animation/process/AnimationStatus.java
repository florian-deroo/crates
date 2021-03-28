package fr.flushfr.crates.objects.animation.process;

public class AnimationStatus {
    private boolean isStarted;
    private boolean isEnded;

    public AnimationStatus(boolean isStarted, boolean isEnded) {
        this.isStarted = isStarted;
        this.isEnded = isEnded;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void setStarted(boolean started) {
        isStarted = started;
    }

    public boolean isEnded() {
        return isEnded;
    }

    public void setEnded(boolean ended) {
        isEnded = ended;
    }
}

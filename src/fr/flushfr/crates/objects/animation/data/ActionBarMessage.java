package fr.flushfr.crates.objects.animation.data;

public class ActionBarMessage {
    private String message;
    private boolean everyone;

    public ActionBarMessage(String message, boolean everyone) {
        this.message = message;
        this.everyone = everyone;
    }

    public String getMessage() {
        return message;
    }

    public boolean isEveryone() {
        return everyone;
    }
}

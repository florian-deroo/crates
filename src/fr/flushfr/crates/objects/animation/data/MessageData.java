package fr.flushfr.crates.objects.animation.data;

public class MessageData {
    private String[] message;
    private String player;
    private boolean everyone;

    public MessageData(String[] message, String player) {
        this.message = message;
        this.player = player;
    }
    public MessageData(String[] message, boolean everyone) {
        this.message = message;
        this.everyone = everyone;
    }
    public String[] getMessage() {
        return message;
    }

    public String getPlayer() {
        return player;
    }

    public boolean isEveryone() {
        return everyone;
    }
}

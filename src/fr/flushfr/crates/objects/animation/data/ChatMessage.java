package fr.flushfr.crates.objects.animation.data;

public class ChatMessage {
    private String[] message;
    private boolean everyone;

    public ChatMessage(String[] message, boolean everyone) {
        this.message = message;
        this.everyone = everyone;
    }

    public boolean isEveryone() {
        return everyone;
    }

    public String[] getMessage() {
        return message;
    }
}

package fr.flushfr.crates.objects.animation.data;

public class MessageData {
    private String[] message;
    private boolean everyone;

    private boolean actionBar;
    private String actionBarMessage;

    private boolean chatMessage;

    private boolean titleMessage;
    private Integer stay;
    private Integer fadeOut;
    private Integer fadeIn;
    private String title;
    private String subtitle;

    public MessageData(String actionBarMessage, boolean everyone) {
        this.actionBarMessage = actionBarMessage;
        this.everyone = everyone;
        actionBar = true;
    }


    public MessageData(String[] message, boolean everyone) {
        this.message = message;
        this.everyone = everyone;
        this.chatMessage = true;
    }

    public MessageData(boolean everyone, int stay, int  fadeIn, int  fadeOut, String title, String subtitle) {
        this.everyone = everyone;
        this.stay = stay;
        this.fadeIn = fadeIn;
        this.fadeOut = fadeOut;
        this.title = title;
        this.subtitle = subtitle;
        titleMessage = true;
    }

    public String[] getMessage() {
        return message;
    }
    public boolean isEveryone() {
        return everyone;
    }
    public boolean isActionBar() { return actionBar; }
    public boolean isChatMessage() { return chatMessage; }
    public boolean isTitleMessage() { return titleMessage; }

    public Integer getStay() {
        return stay;
    }

    public Integer getFadeOut() {
        return fadeOut;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public Integer getFadeIn() {
        return fadeIn;
    }

    public String getActionBarMessage() {
        return actionBarMessage;
    }
}

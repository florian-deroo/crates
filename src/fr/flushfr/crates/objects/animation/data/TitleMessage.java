package fr.flushfr.crates.objects.animation.data;

public class TitleMessage {
    private boolean everyone;
    private Integer stay;
    private Integer fadeOut;
    private Integer fadeIn;
    private String title;
    private String subtitle;

    public TitleMessage(String title, String subtitle, int stay, int  fadeIn, int  fadeOut, boolean everyone) {
        this.everyone = everyone;
        this.stay = stay;
        this.fadeIn = fadeIn;
        this.fadeOut = fadeOut;
        this.title = title;
        this.subtitle = subtitle;
    }

    public boolean isEveryone() {
        return everyone;
    }

    public Integer getStay() {
        return stay;
    }

    public Integer getFadeOut() {
        return fadeOut;
    }

    public Integer getFadeIn() {
        return fadeIn;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

}

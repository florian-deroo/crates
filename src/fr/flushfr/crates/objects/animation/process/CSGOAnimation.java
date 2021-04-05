package fr.flushfr.crates.objects.animation.process;

import org.bukkit.entity.Player;

public class CSGOAnimation {
    private Player player;
    private int a;
    private int b;
    private boolean stop;

    public CSGOAnimation(Player player) {
        this.player = player;
        this.a=0;
        this.b=0;
    }

    public Player getPlayer() {
        return player;
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public boolean isStop() {
        return stop;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }
}

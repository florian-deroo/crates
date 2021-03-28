package fr.flushfr.crates.managers;

import fr.flushfr.crates.objects.animation.process.EpicSwordAnimation;
import fr.flushfr.crates.objects.animation.process.RollAnimation;

import java.util.ArrayList;
import java.util.List;

public class AnimationManager {
    public List<EpicSwordAnimation> animationStarted = new ArrayList<>();
    public List<RollAnimation> rollAnimations = new ArrayList<>();

    public void stopLoopAllAnimation () {
        for (EpicSwordAnimation ao : animationStarted) {ao.setStop(true);}
        for (RollAnimation r : rollAnimations) {r.setStop(true);}
    }
}

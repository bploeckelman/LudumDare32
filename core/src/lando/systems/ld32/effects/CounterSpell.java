package lando.systems.ld32.effects;

import lando.systems.ld32.Assets;

public class CounterSpell extends Effect {

    public static final float counterspell_time = 0.05f;
    public static final float counterspell_scale = 3f;

    @Override
    public void init(float x, float y) {
        init(x, y, counterspell_scale);
    }

    public void init(float x, float y, float scale) {
        this.scale = scale;
        stateTime = 0f;
        position.set(x, y);
        animation = Assets.counterSpellAnimation;
        keyFrame = animation.getKeyFrame(stateTime);
        alive = true;
    }

}

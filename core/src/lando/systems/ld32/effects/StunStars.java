package lando.systems.ld32.effects;

import lando.systems.ld32.Assets;

public class StunStars extends Effect {
    public static final float stunstars_time = .1f;
    public static final float stunstars_scale = 2.5f;

    public StunStars() {
        super();
    }

    @Override
    public void init(float x, float y) {
        init(x, y, stunstars_scale);
    }

    public void init(float x, float y, float scale) {
        this.scale = scale;
        stateTime = 0f;
        position.set(x, y);
        animation = Assets.stunStarsAnimation;
        keyFrame = animation.getKeyFrame(stateTime);
        alive = true;
    }

    @Override
    public void update(float delta) {
        stateTime += delta;
        keyFrame = animation.getKeyFrame(stateTime);
    }
}

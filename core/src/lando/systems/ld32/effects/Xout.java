package lando.systems.ld32.effects;

import com.badlogic.gdx.utils.Pool;
import lando.systems.ld32.Assets;

public class Xout extends Effect {
    public static Pool<Xout> xoutPool = new Pool<Xout>() {
        @Override
        protected Xout newObject() {
            return new Xout();
        }
    };

    public static final float xout_time = 0.1f;
    public static final float xout_scale = 2f;

    public Xout() {
        super();
    }

    @Override
    public void init(float x, float y) {
        init(x, y, xout_scale);
    }

    public void init(float x, float y, float scale) {
        this.scale = scale;
        stateTime = 0f;
        position.set(x, y);
        animation = Assets.xoutAnimation;
        keyFrame = animation.getKeyFrame(stateTime);
        alive = true;
    }
}

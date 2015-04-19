package lando.systems.ld32.effects;

import com.badlogic.gdx.utils.Pool;
import lando.systems.ld32.Assets;

public class Explode extends Effect {
    public static Pool<Explode> explodePool = new Pool<Explode>() {
        @Override
        protected Explode newObject() {
            return new Explode();
        }
    };

    public static final float explode_time = 0.075f;
    public static final float explode_scale = 2f;

    public Explode() {
        super();
    }

    @Override
    public void init(float x, float y) {
        init(x, y, explode_scale);
    }

    public void init(float x, float y, float scale) {
        this.scale = scale;
        stateTime = 0f;
        position.set(x, y);
        animation = Assets.explodeAnimation;
        keyFrame = animation.getKeyFrame(stateTime);
        alive = true;
    }
}

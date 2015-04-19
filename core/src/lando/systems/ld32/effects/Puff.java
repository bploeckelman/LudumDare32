package lando.systems.ld32.effects;

import com.badlogic.gdx.utils.Pool;
import lando.systems.ld32.Assets;

public class Puff extends Effect {
    public static Pool<Puff> puffPool = new Pool<Puff>() {
        @Override
        protected Puff newObject() {
            return new Puff();
        }
    };

    public static final float puff_time = 0.075f;
    public static final float puff_scale = 1f;

    public Puff() {
        super();
    }

    @Override
    public void init(float x, float y) {
        init(x, y, puff_scale);
    }

    public void init(float x, float y, float scale) {
        this.scale = scale;
        stateTime = 0f;
        position.set(x, y);
        animation = Assets.puffAnimation;
        keyFrame = animation.getKeyFrame(stateTime);
        alive = true;
    }
}

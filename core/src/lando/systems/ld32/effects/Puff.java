package lando.systems.ld32.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import lando.systems.ld32.Assets;

public class Puff {

    // TODO: extra core stuff to Effect super class and just set animation and default frame duration, pool all effects
    public static Pool<Puff> puffPool = new Pool<Puff>() {
        @Override
        protected Puff newObject() {
            return new Puff();
        }
    };

    private static final float puff_time = 0.075f;

    float         scale;
    float         stateTime;
    Vector2       position;
    Animation     animation;
    TextureRegion keyFrame;
    // TODO: add color field?

    public boolean alive;

    public Puff() {
        scale = 1f;
        stateTime = 0f;
        position = new Vector2();
        animation = new Animation(0);
        keyFrame = null;
        alive = false;
    }

    public void init(float x, float y) {
        init(x, y, 1);
    }

    public void init(float x, float y, float scale) {
        this.scale = scale;
        stateTime = 0f;
        position.set(x, y);
        animation = new Animation(puff_time,
                                  Assets.effectsRegions[2][3],
                                  Assets.effectsRegions[2][4],
                                  Assets.effectsRegions[10][4],
                                  Assets.effectsRegions[10][5],
                                  Assets.effectsRegions[10][6],
                                  Assets.effectsRegions[10][7]);
        keyFrame = animation.getKeyFrame(stateTime);
        alive = true;
    }

    public void update(float delta) {
        stateTime += delta;
        keyFrame = animation.getKeyFrame(stateTime);
        alive = !animation.isAnimationFinished(stateTime);
    }

    public void render(SpriteBatch batch) {
        float w = keyFrame.getRegionWidth() * scale;
        float h = keyFrame.getRegionHeight() * scale;
        batch.draw(keyFrame, position.x - w/2f, position.y - h/2f, w, h);
    }

}

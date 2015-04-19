package lando.systems.ld32.effects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Effect {
    float         scale;
    float         stateTime;
    public Vector2       position;
    Animation     animation;
    public TextureRegion keyFrame;
    // TODO: add color field?

    public boolean alive;

    public Effect() {
        position = new Vector2();
        alive = false;
    }

    public void init(float x, float y) {}

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

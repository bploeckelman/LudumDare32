package lando.systems.ld32.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public abstract class Entity {
    Animation animation;
    float animationTimer;
    public Vector2 position;
    TextureRegion keyFrame;

    public void render(SpriteBatch batch) {
        batch.draw(keyFrame, position.x, position.y);
    }

    public void update(float delta) {
        animationTimer += delta;
        keyFrame = animation.getKeyFrame(animationTimer);
    }
}

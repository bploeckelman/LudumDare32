package lando.systems.ld32.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld32.Assets;
import lando.systems.ld32.Constants;

public class Player extends Entity {

    float scale = 2.5f;

    public Player() {
        TextureRegion frame1 = new TextureRegion(Assets.enemyRegions[2][10]);
        TextureRegion frame2 = new TextureRegion(Assets.enemyRegions[3][10]);
        frame1.flip(true, false);
        frame2.flip(true, false);
        // TODO: Temporary animation
        animation = new Animation(.2f, frame1, frame2);
        animation.setPlayMode(Animation.PlayMode.LOOP);

        position = new Vector2(Constants.win_width*.1f, Constants.win_height*.1f);
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(keyFrame,
                   position.x,
                   position.y,
                   keyFrame.getRegionWidth() * 2f,
                   keyFrame.getRegionHeight() * 2f);
    }
}

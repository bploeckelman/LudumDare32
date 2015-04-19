package lando.systems.ld32.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld32.Assets;
import lando.systems.ld32.Constants;

public class Player extends Entity {
    public Player() {
        // TODO: Temporary animation
        animation = new Animation(
            .3f,
            new TextureRegion(Assets.speechBubbleTexture),
            new TextureRegion(Assets.killphraseBox));
        animation.setPlayMode(Animation.PlayMode.LOOP);

        position = new Vector2(Constants.win_width*.25f, Constants.win_height*.25f);
    }
}

package lando.systems.ld32.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld32.Assets;
import lando.systems.ld32.Constants;
import lando.systems.ld32.attackwords.AttackWord;

public class Enemy extends Entity {
    float attackTimer;
    float attackDelay;
    BitmapFont font;

    public Enemy(BitmapFont font) {
        this.font = font;

        // TODO: Temporary animation
        animation = new Animation(
            .3f,
            new TextureRegion(Assets.speechBubbleTexture),
            new TextureRegion(Assets.killphraseBox));
        animation.setPlayMode(Animation.PlayMode.LOOP);

        attackTimer = 0;
        attackDelay = 3;
        position = new Vector2(Constants.win_width*.75f, Constants.win_height*.25f);
    }

    public AttackWord generateAttack() {
        if(attackTimer >= attackDelay) {
            attackTimer = 0;
            return new AttackWord("ATTACK", font).fire(position.x, position.y);
        }

        return null;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        attackTimer += delta;
    }
}

package lando.systems.ld32.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld32.Constants;
import lando.systems.ld32.attackwords.AttackWord;

public class Enemy extends Entity {
    float attackTimer;
    float attackDelay;
    BitmapFont font;
    public float scale = 2.5f;
    String[] attackDictionary;
    public String killPhrase;

    public Enemy(
        BitmapFont font,
        Animation animation,
        float attackDelay,
        String[] attackDictionary,
        String killPhrase)
    {
        this.font = font;
        this.animation = animation;
        this.attackDelay = attackDelay;
        this.attackDictionary = attackDictionary;
        this.killPhrase = killPhrase;

        keyFrame = animation.getKeyFrame(animationTimer);

        attackTimer = 0;
        position = new Vector2(Constants.win_width*.8f, Constants.win_height*.1f);
    }

    public AttackWord generateAttack() {
        if(attackTimer >= attackDelay) {
            attackTimer = 0;
            AttackWord attackWord = new AttackWord(
                attackDictionary[MathUtils.random(0, attackDictionary.length-1)], font);
            float x = position.x - attackWord.bounds.width - 5f;
            float y = position.y + keyFrame.getRegionHeight() * scale - 5f;
            return attackWord.fire(x, y);
        }

        return null;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        attackTimer += delta;
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(keyFrame,
                   position.x,
                   position.y,
                   keyFrame.getRegionWidth() * scale,
                   keyFrame.getRegionHeight() * scale);
    }
}

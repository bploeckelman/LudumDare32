package lando.systems.ld32.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld32.Constants;
import lando.systems.ld32.attackwords.AttackWord;
import lando.systems.ld32.spellwords.Darkness;
import lando.systems.ld32.spellwords.Silence;
import lando.systems.ld32.spellwords.SpellWord;

public class Enemy extends Entity {
    float attackTimer;
    float attackDelay;
    float spellTimer;
    float spellDelay;
    BitmapFont font;
    public float scale = 2.5f;
    String[] attackDictionary;
    SpellWord.Type[] spellDictionary;
    public String killPhrase;

    public Enemy(
        BitmapFont font,
        Animation animation,
        float attackDelay,
        String[] attackDictionary,
        SpellWord.Type[] spellDictionary,
        String killPhrase)
    {
        this.font = font;
        this.animation = animation;
        this.attackDelay = attackDelay;
        this.attackDictionary = attackDictionary;
        this.spellDictionary = spellDictionary;
        this.killPhrase = killPhrase;

        keyFrame = animation.getKeyFrame(animationTimer);

        attackTimer = 0;
        position = new Vector2(Constants.win_width*.8f, Constants.win_height*.1f);

        spellTimer = 0;
        spellDelay = 10f;
    }

    public AttackWord generateAttack() {
        if(attackTimer >= attackDelay) {
            attackTimer = 0;
            AttackWord attackWord = new AttackWord(
                attackDictionary[MathUtils.random(0, attackDictionary.length-1)], font);
            float x = position.x - attackWord.bounds.width - 5f;
            float y = position.y + keyFrame.getRegionHeight() * scale - 5f;
            attackWord.origin.set(x, y);
            return attackWord.fire(x, y);
        }

        return null;
    }

    public SpellWord generateSpell() {
        SpellWord spellWord = null;
        if (spellTimer > spellDelay) {
            spellWord = SpellWord.create(
                spellDictionary[MathUtils.random(0, spellDictionary.length-1)].num, font);
        }
        return spellWord;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        attackTimer += delta;
        spellTimer += delta;
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(keyFrame,
                   position.x,
                   position.y,
                   keyFrame.getRegionWidth() * scale,
                   keyFrame.getRegionHeight() * scale);
    }

    public void setSpellTimer(float spellTimer) {
        this.spellTimer = spellTimer;
    }

}

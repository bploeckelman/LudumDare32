package lando.systems.ld32.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld32.Constants;
import lando.systems.ld32.Statistics;
import lando.systems.ld32.attackwords.AttackWord;
import lando.systems.ld32.spellwords.SpellWord;

public class Enemy extends Entity {
    static final float ventriloquist_offset = Constants.win_width / 5f;
    static final float default_spell_delay = 10f;
    static final float spell_delay_offset = 4f;

    float attackTimer;
    float attackDelay;
    float spellTimer;
    float spellDelay;
    public float scale = 2.5f;
    public TextureRegion backgroundRegion;
    String[] attackDictionary;
    SpellWord.Type[] spellDictionary;
    boolean didCastNewSpell = false;
    public String killPhrase;
    public boolean ventriloquist = false;
    Color oldColor;

    public Enemy(
        Animation animation,
        float attackDelay,
        String[] attackDictionary,
        SpellWord.Type[] spellDictionary,
        String killPhrase)
    {
        this.animation = animation;
        this.attackDelay = attackDelay;
        this.attackDictionary = attackDictionary;
        this.spellDictionary = spellDictionary;
        this.killPhrase = killPhrase;

        keyFrame = animation.getKeyFrame(animationTimer);

        attackTimer = 0;
        position = new Vector2(Constants.win_width*.8f, Constants.win_height*.1f);

        spellTimer = 0;
        spellDelay = 10f; // TODO: randomly change delay when resetting spellTimer
    }

    public AttackWord generateAttack() {
        if(attackTimer >= attackDelay) {
            attackTimer = 0;
            AttackWord attackWord = new AttackWord(attackDictionary[MathUtils.random(0, attackDictionary.length-1)]);
            float x = position.x - attackWord.bounds.width - 5f;
            float y = position.y + keyFrame.getRegionHeight() * scale - 5f;
            if (ventriloquist) x -= ventriloquist_offset;
            attackWord.origin.set(x, y);
            return attackWord.fire(x, y, ventriloquist);
        }

        return null;
    }

    public SpellWord generateSpell() {
        SpellWord spellWord = null;
        if (spellTimer > spellDelay) {
            int spellDictIndex;
            if (!didCastNewSpell) {
                didCastNewSpell = true;
                spellDictIndex = spellDictionary.length - 1;
            } else {
                spellDictIndex = MathUtils.random(0, spellDictionary.length - 1);
            }
            spellWord = SpellWord.create(spellDictionary[spellDictIndex].num);
        }
        if (spellWord != null) {
            Statistics.numSpellsCast++;
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

        if (ventriloquist) {
            oldColor = batch.getColor();
            batch.setColor(Color.GREEN);
            batch.draw(keyFrame,
                       position.x - ventriloquist_offset - 5f,
                       position.y - 5f,
                       keyFrame.getRegionWidth() * scale * 1.2f,
                       keyFrame.getRegionHeight() * scale * 1.2f);
            batch.setColor(Color.BLACK);
            batch.draw(keyFrame,
                       position.x - ventriloquist_offset,
                       position.y,
                       keyFrame.getRegionWidth() * scale,
                       keyFrame.getRegionHeight() * scale);
            batch.setColor(oldColor);
        }
    }

    public void setSpellTimer(float spellTimer) {
        this.spellDelay = MathUtils.random(default_spell_delay - spell_delay_offset,
                                           default_spell_delay + spell_delay_offset);
        this.spellTimer = spellTimer;
    }

}

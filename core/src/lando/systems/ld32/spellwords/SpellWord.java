package lando.systems.ld32.spellwords;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.equations.Sine;
import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld32.Assets;
import lando.systems.ld32.Constants;
import lando.systems.ld32.GameInstance;
import lando.systems.ld32.killphrase.KillPhrase;
import lando.systems.ld32.screens.FightScreen;

public abstract class SpellWord {
    public static enum Type {
        DARKNESS(1),
        QUIET(2);

        public int num;
        Type(int i) {
            num = i;
        }
    }

    public static SpellWord create(String spellName) {
        return create(Type.valueOf(spellName).num);
    }
    public static SpellWord create(int spellNum) {
        switch (spellNum) {
            case 1: return new Darkness();
            case 2: return new Quiet();
        }

        return null;
    }

    public static final float margin                = 10f;
    public static final float row_height            = 32f;
    public static final float narrative_row_height  = 64f; // TODO: move me to narrative manager or something
    public static final float killphrase_row_height = KillPhrase.boxSize + 16;

    final String     word;
    final String     reverseWord;
    final BitmapFont font;
    final float      font_scale = 1.5f;
    final float      offset_increment;

    TextBounds   textBounds;
    String       typed;
    String[]     letters;
    MutableFloat offset;

    public Rectangle bounds;

    public SpellWord(String word) {
        this.word = word.toUpperCase();
        String temp = "";
        for (int i = this.word.length() - 1; i >= 0; --i) {
            temp += this.word.charAt(i);
        }
        this.reverseWord = temp;
        this.font = Assets.font16;
        this.typed = "";
        this.letters = new String[word.length()];
        for (int i = 0; i < this.word.length(); ++i) {
            letters[i] = "" + this.word.charAt(i);
        }

        font.setScale(font_scale);
        textBounds = new TextBounds(font.getBounds(word));
        font.setScale(1);

        bounds = new Rectangle(
                Constants.win_width - margin - textBounds.width,
                Constants.win_height - narrative_row_height - killphrase_row_height - row_height,
                Constants.win_width, Constants.win_height);

        offset = new MutableFloat(0f);

        final float x0 = bounds.x;
        final float x1 = Constants.win_width / 2f;
        final float end_offset = (x1 - x0);
        offset_increment = end_offset / (word.length() - 1);
    }

    public abstract void applySpell(FightScreen fightScreen);

    public abstract void removeSpell(FightScreen fightScreen);

    public void render(SpriteBatch batch) {
        // TODO: draw an underlay?
        font.setScale(font_scale);
        font.setColor(Color.ORANGE);
        font.draw(batch, word, bounds.x + offset.floatValue(), bounds.y);

        font.setColor(Color.CYAN);
        font.draw(batch, typed, margin - offset.floatValue(), bounds.y);

        font.setScale(1f);
        font.setColor(Color.WHITE);
    }

    public void keyTyped(int keycode) {
        final int lastIndex = letters.length - 1;

        int i = typed.length();
        if (i >= letters.length) {
            return;
        }

//        if (letters[i].equals(" ")) {
//            typed += letters[i];
//            if (++i >= letters.length) {
//                return;
//            }
//        }

        int letter = Input.Keys.valueOf(letters[lastIndex - i]);
        if (keycode == letter) {
            typed += letters[lastIndex - i];
            final float target = offset.floatValue() + offset_increment;
            Tween.to(offset, -1, 0.2f)
                 .target(target)
                 .ease(Sine.OUT)
                 .start(GameInstance.tweens);
        }
    }

    public boolean isComplete() {
        return typed.equalsIgnoreCase(reverseWord);
    }

}

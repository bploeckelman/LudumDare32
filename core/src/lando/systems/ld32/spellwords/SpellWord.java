package lando.systems.ld32.spellwords;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld32.Assets;
import lando.systems.ld32.Constants;
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
    final float fontScale = 2f;

    TextBounds textBounds;
    Rectangle  bounds;
    String     typed;
    String[]   letters;

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

        font.setScale(fontScale);
        textBounds = new TextBounds(font.getBounds(word));
        font.setScale(1f);

        bounds = new Rectangle(
                Constants.win_width - margin - textBounds.width,
                Constants.win_height - narrative_row_height - killphrase_row_height - row_height,
                Constants.win_width, Constants.win_height);
    }

    public abstract void applySpell(FightScreen fightScreen);

    public abstract void removeSpell(FightScreen fightScreen);

    public void render(SpriteBatch batch) {
        // TODO: draw an underlay?
        font.setScale(fontScale);
        font.setColor(Color.WHITE);
        font.draw(batch, word, bounds.x, bounds.y);

        font.setColor(Color.ORANGE);
        font.draw(batch, typed, margin, bounds.y);

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
        }
    }

    public boolean isComplete() {
        return typed.equalsIgnoreCase(reverseWord);
    }

}

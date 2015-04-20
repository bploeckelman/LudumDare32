package lando.systems.ld32.spellwords;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import lando.systems.ld32.attackwords.AttackWord;
import lando.systems.ld32.screens.FightScreen;

public class Silence extends SpellWord {

    public Silence(BitmapFont font) {
        super("SILENCE", font);
    }

    public void applySpell(FightScreen fightScreen) {
        AttackWord.smallWords = true;
    }

    public void removeSpell(FightScreen fightScreen) {
        AttackWord.smallWords = false;
    }

}

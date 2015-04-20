package lando.systems.ld32.spellwords;

import lando.systems.ld32.attackwords.AttackWord;
import lando.systems.ld32.screens.FightScreen;

public class Silence extends SpellWord {

    public Silence() {
        super("SILENCE");
    }

    public void applySpell(FightScreen fightScreen) {
        AttackWord.smallWords = true;
    }

    public void removeSpell(FightScreen fightScreen) {
        AttackWord.smallWords = false;
    }

}

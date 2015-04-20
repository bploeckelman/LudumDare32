package lando.systems.ld32.spellwords;

import lando.systems.ld32.attackwords.AttackWord;
import lando.systems.ld32.screens.FightScreen;

public class Shhh extends SpellWord {

    public Shhh() {
        super("SHHH");
    }

    @Override
    public void applySpell(FightScreen fightScreen) {
        AttackWord.smallWords = true;
    }

    @Override
    public void removeSpell(FightScreen fightScreen) {
        AttackWord.smallWords = false;
    }

}

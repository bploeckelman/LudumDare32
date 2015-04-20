package lando.systems.ld32.spellwords;

import lando.systems.ld32.attackwords.AttackWord;
import lando.systems.ld32.screens.FightScreen;

public class Crosseyed extends SpellWord {

    public Crosseyed() {
        super("CROSSEYED");
    }

    @Override
    public void applySpell(FightScreen fightScreen) {
        AttackWord.crosseyeMode = true;
    }

    @Override
    public void removeSpell(FightScreen fightScreen) {
        AttackWord.crosseyeMode = false;
    }
}

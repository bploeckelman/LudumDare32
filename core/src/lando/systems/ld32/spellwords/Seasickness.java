package lando.systems.ld32.spellwords;

import lando.systems.ld32.screens.FightScreen;

public class Seasickness extends SpellWord {

    public Seasickness() {
        super("SEASICKNESS");
    }

    @Override
    public void applySpell(FightScreen fightScreen) {
        fightScreen.seasick = true;
//        AttackWord.seasick = true;
    }

    @Override
    public void removeSpell(FightScreen fightScreen) {
        fightScreen.seasick = false;
//        AttackWord.seasick = false;
    }
}

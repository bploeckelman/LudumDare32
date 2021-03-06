package lando.systems.ld32.spellwords;

import lando.systems.ld32.attackwords.AttackWord;
import lando.systems.ld32.screens.FightScreen;

public class Darkness extends SpellWord {

    public Darkness() {
        super("DARKNESS");
    }

    @Override
    public void applySpell(FightScreen fightScreen) {
        AttackWord.darkMode = true;
        fightScreen.tweenBgColor(0.2f, 0.2f, 0.2f, 1.0f);
    }

    @Override
    public void removeSpell(FightScreen fightScreen) {
        AttackWord.darkMode = false;
        fightScreen.tweenBgColor(1, 1, 1, 1);
    }

}

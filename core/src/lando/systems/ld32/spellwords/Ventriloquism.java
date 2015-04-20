package lando.systems.ld32.spellwords;

import lando.systems.ld32.screens.FightScreen;

public class Ventriloquism extends SpellWord {

    public Ventriloquism() {
        super("VENTRILOQUISM");
    }

    @Override
    public void applySpell(FightScreen fightScreen) {
        fightScreen.enemy.ventriloquist = true;
    }

    @Override
    public void removeSpell(FightScreen fightScreen) {
        fightScreen.enemy.ventriloquist = false;
    }
}

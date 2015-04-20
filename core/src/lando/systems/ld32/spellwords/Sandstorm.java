package lando.systems.ld32.spellwords;

import lando.systems.ld32.screens.FightScreen;

public class Sandstorm extends SpellWord {

    public Sandstorm() {
        super("SANDSTORM");
    }

    @Override
    public void applySpell(FightScreen fightScreen) {
        fightScreen.doNoise = true;
    }

    @Override
    public void removeSpell(FightScreen fightScreen) {
        fightScreen.doNoise = false;
    }

}

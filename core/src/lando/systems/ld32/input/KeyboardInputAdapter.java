package lando.systems.ld32.input;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld32.attackwords.AttackWord;
import lando.systems.ld32.killphrase.KillPhrase;

public class KeyboardInputAdapter extends InputAdapter {

    public Array<AttackWord> attackWords;
    public KillPhrase killPhrase;
    public boolean staggerWindow = false;

    public KeyboardInputAdapter(Array<AttackWord> attackWords, KillPhrase killPhrase) {
        this.attackWords = attackWords;
        this.killPhrase = killPhrase;
    }

    public boolean keyDown(int keycode) {
        if (keycode <= 0) return false;

        // TODO: delegate to appropriate handler, attackWords.first(), SpellWord, or KillPhrase
        if (staggerWindow) {
            killPhrase.keyTyped(keycode);
            return false;
        }

        if (attackWords.size > 0) {
            attackWords.first().keyTyped(keycode);
            return false;
        }

        return false;
    }

}

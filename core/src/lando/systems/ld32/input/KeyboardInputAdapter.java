package lando.systems.ld32.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld32.attackwords.AttackWord;
import lando.systems.ld32.killphrase.KillPhrase;
import lando.systems.ld32.spellwords.SpellWord;

public class KeyboardInputAdapter extends InputAdapter {

    public Array<AttackWord> attackWords;
    public KillPhrase killPhrase;
    public SpellWord  spellWord;
    public boolean staggerWindow = false;
    public boolean shiftDown = false;

    public KeyboardInputAdapter(Array<AttackWord> attackWords, KillPhrase killPhrase) {
        this.attackWords = attackWords;
        this.killPhrase = killPhrase;
        this.spellWord = null;
    }

    public boolean keyDown(int keycode) {
        if (keycode <= 0) return false;

        if (keycode == Input.Keys.SHIFT_LEFT || keycode == Input.Keys.SHIFT_RIGHT) {
            shiftDown = true;
        }

        if (staggerWindow) {
            killPhrase.keyTyped(keycode);
            return false;
        }

        if (spellWord != null && shiftDown) {
            spellWord.keyTyped(keycode);
            return false;
        }

        if (attackWords.size > 0) {
            attackWords.first().keyTyped(keycode);
            return false;
        }

        return false;
    }

    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.SHIFT_LEFT || keycode == Input.Keys.SHIFT_RIGHT) {
            shiftDown = false;
        }

        if (keycode == Input.Keys.ENTER) {

        }

        return false;
    }


}

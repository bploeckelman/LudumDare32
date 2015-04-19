package lando.systems.ld32.input;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld32.attackwords.AttackWord;

public class KeyboardInputAdapter extends InputAdapter {

    Array<AttackWord> attackWords;

    public KeyboardInputAdapter(Array<AttackWord> attackWords) {
        this.attackWords = attackWords;
    }

    public boolean keyDown(int keycode) {
        if (keycode <= 0) return false;

        // TODO: delegate to appropriate handler, attackWords.first(), SpellWord, or KillPhrase

        if (attackWords.size > 0) {
            attackWords.first().keyTyped(keycode);
        }

        return false;
    }

}

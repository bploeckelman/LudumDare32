package lando.systems.ld32.killphrase;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld32.Assets;
import lando.systems.ld32.Constants;

public class KillPhrase {
    private final String space = " ";

    public static Rectangle bounds = new Rectangle(
        5, Constants.win_height-100, Constants.win_width, Constants.win_height
    );
    public static int boxSize = 32;
    public static int boxSpacing = 4;
    public static int spaceSize = 16;

    public boolean[] enabled;
    public TextBounds[] charBounds;
    public Vector2[] charOrigins;
    public Vector2[] boxOrigins;

    private String[] phrase;
    private BitmapFont font;

    public KillPhrase(String phrase, BitmapFont font) {
        this.font = font;
        this.phrase = new String[phrase.length()];
        enabled = new boolean[this.phrase.length];
        charBounds = new TextBounds[this.phrase.length];
        charOrigins = new Vector2[this.phrase.length];
        boxOrigins = new Vector2[this.phrase.length];

        float xOffset = bounds.x;
        for(int i=0; i<this.phrase.length; i++) {
            this.phrase[i] = String.valueOf(phrase.charAt(i));
            enabled[i] = false;
            TextBounds charBounds = font.getBounds(this.phrase[i]);

            if(this.phrase[i].equals(space)) {
                xOffset += spaceSize + boxSpacing;
                continue;
            } else if(i != 0) {
                xOffset += boxSize + boxSpacing;
            }
            charOrigins[i] = new Vector2(
                xOffset + boxSize/2f - charBounds.width/2f,
                bounds.y + (boxSize/2f) + (charBounds.height/2f)
            );
            boxOrigins[i] = new Vector2(xOffset, bounds.y);
        }
    }

    public void enableLetter() {
        for(int i=0; i<phrase.length; i++) {
            if(!enabled[i]) {
                enabled[i] = true;
                if(phrase[i].equals(space)) {
                    continue;
                }
                break;
            }
        }
    }

    public void disableLetter() {
        for(int i=phrase.length; i>0; i--) {
            if(enabled[i]) {
                enabled[i] = false;
                if(phrase[i].equals(space)) {
                    continue;
                }
                break;
            }
        }
    }

    public boolean isComplete() {
        for(int i=0; i<phrase.length; i++) {
            if(!enabled[i]) {
                return false;
            }
        }

        return true;
    }

    public void clean() {
        for(int i=0; i<phrase.length; i++) {
            enabled[i] = false;
        }
    }

    public void render(SpriteBatch batch) {
        for(int i=0; i<phrase.length; i++) {
            if(phrase[i].equals(space)) {
                continue;
            }

            batch.draw(Assets.killphraseBox, boxOrigins[i].x, boxOrigins[i].y, boxSize, boxSize);

            if(enabled[i]) {
                font.draw(batch, phrase[i], charOrigins[i].x, charOrigins[i].y);
            }
        }
    }
}

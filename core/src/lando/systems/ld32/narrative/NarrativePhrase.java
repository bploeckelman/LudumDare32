package lando.systems.ld32.narrative;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class NarrativePhrase {

    private final BitmapFont font;
    private final String phrase;
    private final float charTime;

    private static BitmapFont.TextBounds textBounds;

    private float updateTime = 0;
    private boolean revealComplete = false;

    public NarrativePhrase(BitmapFont font, String phrase, float cps) {
        this.font = font;
        this.phrase = phrase;
        this.charTime = 1f / cps;
    }

    public float getSpaceWidth() {
        return font.getBounds(" ").width;
    }
    public boolean isComplete() {
        return revealComplete;
    }

    /**
     *
     * @param batch The batch
     * @param x Bottom left corner (x)
     * @param y Bottom left corner (y)
     * @return The text bounds of the characters drawn.
     */
    public BitmapFont.TextBounds render(SpriteBatch batch, float x, float y) {
        String drawString = phrase;
        if (!revealComplete) {
            int charCount = MathUtils.floor(updateTime / charTime);
            if (charCount >= phrase.length()) {
                revealComplete = true;
            } else {
                drawString = phrase.substring(0, charCount);
            }
        }
        textBounds = font.getBounds(drawString);
        return font.draw(batch, drawString, x, y + textBounds.height);
    }

    public void showAll() {
        revealComplete = true;
    }

    public void update(float delta) {
        updateTime += delta;
    }

}

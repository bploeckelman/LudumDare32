package lando.systems.ld32.narrative;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class NarrativePhrase {

    private final BitmapFont font;
    private final String text;
    private final float charTime;
    private final float cps;

    private float updateTime = 0;
    private boolean revealComplete = false;


    // -----------------------------------------------------------------------------------------------------------------

    /**
     *
     * @param font The font
     * @param text The phrase text
     * @param cps The rate: characters per second
     */
    public NarrativePhrase(BitmapFont font, String text, float cps) {
        this.font = font;
        this.text = text;
        this.cps = cps;
        this.charTime = 1f / cps;
    }


    // -----------------------------------------------------------------------------------------------------------------

    public float getCharactersPerSecond() { return this.cps; }

    public BitmapFont getFont() { return font; }

    /**
     * @return The width of a space as rendered by this phrase's font.
     */
    public float getSpaceWidth() {
        return font.getBounds(" ").width;
    }

    public String getText() { return text; }

    /**
     * @return The width of the full phrase as rendered by this phrase's font.
     */
    public float getWidth() {
        return font.getBounds(text).width;
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
        String drawString = text;
        if (!revealComplete) {
            int charCount = MathUtils.floor(updateTime / charTime);
            if (charCount >= text.length()) {
                revealComplete = true;
            } else {
                drawString = text.substring(0, charCount);
            }
        }
        BitmapFont.TextBounds textBounds = font.getBounds(drawString);
        return font.draw(batch, drawString, x, y + textBounds.height);
    }

    public void showAll() {
        revealComplete = true;
    }

    public void update(float delta) {
        updateTime += delta;
    }

    // todo: move drawString stuff into update
}

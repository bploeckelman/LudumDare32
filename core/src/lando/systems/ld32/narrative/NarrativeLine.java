package lando.systems.ld32.narrative;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

// todo word wrap

public class NarrativeLine {

    private ArrayList<NarrativePhrase> phrases = new ArrayList<NarrativePhrase>();

    private float updateTime = 0;

    public NarrativeLine(NarrativePhrase phrase) {
        this.phrases.add(phrase);
    }

    public void addPhrase(NarrativePhrase phrase) {
        phrases.add(phrase);
    }

    public void showAll() {
        for (NarrativePhrase narrativePhrase : phrases) {
            narrativePhrase.showAll();
        }
    }

    /**
     *
     * @param batch The batch
     * @param x Bottom left corner where the line should be positioned
     * @param y Bottom left corner where the line should be positioned
     * @return Returns a rectangle indicating the bounding box of the render that just occurred.
     */
    public Rectangle render(SpriteBatch batch, float x, float y) {

        float currentX = x;
        float currentY = y;
        BitmapFont.TextBounds phraseBounds = null;
        NarrativePhrase phrase;

        for (int i = 0; i < phrases.size(); i++) {
            phrase = phrases.get(i);
            if (i > 0) {
                // Pad
                currentX += phrase.getSpaceWidth();
            }
            phraseBounds = phrase.render(batch, currentX, currentY);
            currentX += phraseBounds.width;
            if (!phrase.isComplete()) {
                break;
            }
        }

        float boundingHeight = phraseBounds == null ? 0 : phraseBounds.height;
        Rectangle r = new Rectangle(x, y, currentX, boundingHeight);
        return r;
    }

    public void update(float delta) {
        updateTime += delta;
        for (NarrativePhrase phrase : phrases) {
            phrase.update(delta);
            if (!phrase.isComplete()) {
                break;
            }
        }
    }


}

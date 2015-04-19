package lando.systems.ld32.narrative;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

public class NarrativeManager {

    public final static int LINE_HEIGHT = 18;
    private final static int PADDING = 10;
    private final static int PARAGRAPH_PAD_Y = 10;

    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private float updateTime = 0;

    private ArrayList<NarrativeParagraph> paragraphs = new ArrayList<NarrativeParagraph>();

    /**
     * When updating paragraphs, which paragraph should we begin with?  This index will be updated as paragraphs are
     * pushed off the screen and updates are no longer required.
     */
    private int paragraphUpdateStartingIndex = 0;

    // -----------------------------------------------------------------------------------------------------------------


    public NarrativeManager(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

//        // TEST
//        // todo: remove debug
//        font.setColor(new Color(1, 1, 1, 1));
//        addPhraseToCurrentParagraph(new NarrativePhrase(font, "This is sentence number 3", 20));

    }

    // -----------------------------------------------------------------------------------------------------------------


    public void addParagraph(NarrativePhrase phrase) {

        // Set the state of all previous paragraphs (still being updated) to "shown"
        for (int i = paragraphUpdateStartingIndex; i < paragraphs.size(); i++) {
            paragraphs.get(i).showAll();
        }

        paragraphs.add(new NarrativeParagraph(phrase, width - (PADDING * 2)));
    }

    public void addPhraseToCurrentParagraph(NarrativePhrase phrase) {
        if (paragraphs.size() == 0) {
            addParagraph(phrase);
        } else {
            paragraphs.get(paragraphs.size()-1).addPhrase(phrase);
        }
    }

    public void render(SpriteBatch batch) {
        Rectangle paragraphBounds = null;
        NarrativeParagraph paragraph;
        float currentX = x + PADDING;
        float currentY = y + PADDING;
        for (int i = paragraphs.size() - 1; i >= paragraphUpdateStartingIndex; i--) {
            paragraph = paragraphs.get(i);
            paragraphBounds = paragraph.render(batch, currentX, currentY);
            currentY += paragraphBounds.height + PARAGRAPH_PAD_Y;
            if ((i - 1) > paragraphUpdateStartingIndex && currentY > y + height) {
                paragraphUpdateStartingIndex = Math.max(i - 1, 0);
//                Gdx.app.log("NarrativeManager.render", "updated starting index to " + paragraphUpdateStartingIndex);
                break;
            }
        }
    }

    public void update(float delta) {
        updateTime += delta;
        for (int i = paragraphUpdateStartingIndex; i < paragraphs.size(); i++) {
            paragraphs.get(i).update(delta);
        }
    }

}

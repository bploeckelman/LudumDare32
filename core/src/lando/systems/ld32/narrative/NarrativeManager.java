package lando.systems.ld32.narrative;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld32.Constants;

import java.util.ArrayList;

public class NarrativeManager {

    private final static int PARAGRAPH_PAD_Y = 10;
    private final static int PADDING = 10;

    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private float updateTime = 0;

    private ArrayList<NarrativeParagraph> paragraphs = new ArrayList<NarrativeParagraph>();
    private BitmapFont font;

    /**
     * When updating paragraphs, which paragraph should we begin with?  This index will be updated as paragraphs are
     * pushed off the screen and updates are no longer required.
     */
    private int paragraphUpdateStartingIndex = 0;

    // -----------------------------------------------------------------------------------------------------------------


    public NarrativeManager(BitmapFont font, int x, int y, int width, int height) {
        this.font = font;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        // todo: remove debug
        font.setColor(new Color(1, 1, 1, 1));

        // TEST
        addParagraph(new NarrativePhrase(font, "This is the first line.", 2));
        addPhraseToCurrentParagraph(new NarrativePhrase(font, "Additional words are now added.", 40));
        addPhraseToCurrentParagraph(new NarrativePhrase(font, "SLOW", 2));

    }

    // -----------------------------------------------------------------------------------------------------------------


    public void addParagraph(NarrativePhrase phrase) {

        // Set the state of all previous paragraphs (still being updated) to "shown"
        for (int i = paragraphUpdateStartingIndex; i < paragraphs.size(); i++) {
            paragraphs.get(i).showAll();
        }

        paragraphs.add(new NarrativeParagraph(phrase, width - PADDING));
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
        for (int i = paragraphs.size() - 1; i >= 0; i--) {
            paragraph = paragraphs.get(i);
            paragraphBounds = paragraph.render(batch, currentX, currentY);
            currentY += paragraphBounds.height + PARAGRAPH_PAD_Y;
            if (currentY > Constants.win_height) {
                paragraphUpdateStartingIndex = Math.max(i - 1, 0);
                break;
            }
        }
    }

    boolean debugFlag = false;

    public void update(float delta) {
        updateTime += delta;
        for (int i = paragraphUpdateStartingIndex; i < paragraphs.size(); i++) {
            paragraphs.get(i).update(delta);
        }
        if (updateTime > 5 && !debugFlag) {
            debugFlag = true;
            addParagraph(new NarrativePhrase(font, "Second line, coming at you!", 15));
        }
    }

}

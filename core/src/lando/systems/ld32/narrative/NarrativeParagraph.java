package lando.systems.ld32.narrative;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

// todo word wrap

public class NarrativeParagraph {

    private ArrayList<NarrativePhrase> phrases = new ArrayList<NarrativePhrase>();

    private ArrayList<ArrayList<NarrativePhrase>> phrasesByLine = new ArrayList<ArrayList<NarrativePhrase>>();

    private final int width;
    private float updateTime = 0;


    // -----------------------------------------------------------------------------------------------------------------

    public NarrativeParagraph(NarrativePhrase phrase, int width) {

        this.width = width;
        addPhrase(phrase);

    }


    // -----------------------------------------------------------------------------------------------------------------

    public void addPhrase(NarrativePhrase phrase) {
        phrases.add(phrase);
        ArrayList<NarrativePhrase> currentLine = phrasesByLine.get(phrasesByLine.size() - 1);
        int currentLinePhraseCount = currentLine.size();
        float lineWidth = currentLinePhraseCount == 0 ? 0 : getWidthOfLine(currentLine);
        float phraseSpaceWidth = phrase.getSpaceWidth();
        float phraseWidth = (currentLinePhraseCount > 0 ? phraseSpaceWidth : 0) + phrase.getWidth();
        if (lineWidth + phraseWidth > width) {
            // New line is needed.  Split the phrase?
            float widthRemaining = width - lineWidth;
            BitmapFont phraseFont = phrase.getFont();
            String phraseText = phrase.getText();
            String[] phraseWords = phraseText.split(" ");
            String newText = "";
            int wordIndex;
            float newPhraseWidth;
            for (wordIndex = 0; wordIndex < phraseWords.length; wordIndex++) {
                newText += phraseWords[wordIndex];
                newPhraseWidth = (currentLinePhraseCount > 0 ? phraseSpaceWidth : 0) + phraseFont.getBounds(newText).width;
                if (newPhraseWidth > widthRemaining) {
                    if (wordIndex == 0) {
                        // Even the first word overflows
                        if (currentLinePhraseCount == 0) {
                            // Nothing more can be done, this first word just needs to overflow a line.
                            currentLine.add(new NarrativePhrase(phraseFont, newText, phrase.getCharactersPerSecond()));
                            // Create a new line
                            phrasesByLine.add(new ArrayList<NarrativePhrase>());
                            // If there are words remaining, make a new phrase and add it.
                            if (phraseWords.length > 1) {

                            }
                        }
                    }
                }
            }
        }
    }

    private float getWidthOfLine(ArrayList<NarrativePhrase> line) {
        float width = 0;
        for (int i = 0; i < line.size(); i++) {
            if (i > 0) {
                width += line.get(i).getSpaceWidth();
            }
            width += line.get(i).getWidth();
        }
        return width;
    }

    public void showAll() {
        for (NarrativePhrase narrativePhrase : phrases) {
            narrativePhrase.showAll();
        }
    }

    /**
     *
     * @param batch The batch
     * @param x Bottom left corner where the paragraph should be positioned
     * @param y Bottom left corner where the paragraph should be positioned
     * @return Returns a rectangle indicating the bounding box of the render that just occurred.  Not all text may
     *      yet be shown.
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

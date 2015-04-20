package lando.systems.ld32.narrative;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld32.Utils;

import java.util.ArrayList;

// todo word wrap

public class NarrativeParagraph {

    private ArrayList<ArrayList<NarrativePhrase>> phrasesByLine = new ArrayList<ArrayList<NarrativePhrase>>();

    private final int width;
    private float updateTime = 0;


    // -----------------------------------------------------------------------------------------------------------------

    public NarrativeParagraph(int width) {

        this.width = width;
        phrasesByLine.add(new ArrayList<NarrativePhrase>());

    }


    // -----------------------------------------------------------------------------------------------------------------

    public NarrativePhrase addPhrase(NarrativePhrase phrase) {
        ArrayList<NarrativePhrase> currentLine = phrasesByLine.get(phrasesByLine.size() - 1);
        int currentLinePhraseCount = currentLine.size();
        float currentLineWidth = currentLinePhraseCount == 0 ? 0 : getWidthOfLine(currentLine);
        float phraseSpaceWidth = phrase.getSpaceWidth();
        // Compensate for space buffer if this is not the first phrase
        float phraseWidth = (currentLinePhraseCount > 0 ? phraseSpaceWidth : 0) + phrase.getWidth();
//        Gdx.app.error("NarrativeParagraph.addPhrase", "phrase = '" + phrase.getText() + "'");
        if (currentLineWidth + phraseWidth > width) {
//            Gdx.app.error("NarrativeParagraph.addPhrase", "\t" + "Overflow");
//            Gdx.app.error("NarrativeParagraph.addPhrase", "\t" + "cLW="+currentLineWidth+", pW="+phraseWidth+", w=" + width);
            // Add a new line for all subsequent calls to use, provided that the current line has at least one phrase
            if (currentLinePhraseCount > 0) {
                phrasesByLine.add(new ArrayList<NarrativePhrase>());
            }
            // Determine how much of the phrase will fit onto the current line.
            float pCPS = phrase.getCharactersPerSecond();
            BitmapFont pFont = phrase.getFont();
            String[] pWords = phrase.getText().split(" ");
            float widthRemaining = width - currentLineWidth;
            String measuringString = "";
            int wordIndex;
            float newPhraseWidth;
            for (wordIndex = 0; wordIndex < pWords.length; wordIndex++) {
                if (wordIndex > 0) {
                    measuringString += " ";
                }
                measuringString += pWords[wordIndex];
                // Compensate for space buffer if this is not the first phrase
                newPhraseWidth = (currentLinePhraseCount > 0 ? phraseSpaceWidth : 0) + pFont.getBounds(measuringString).width;
//                Gdx.app.error("NarrativeParagraph.addPhrase", "\t\t" + "measuringString='"+measuringString+"', w=" + newPhraseWidth);

                if (newPhraseWidth > widthRemaining) {

                    NarrativePhrase lastPhrase;
//                    Gdx.app.error("NarrativeParagraph.addPhrase", "\t\t\t" + "overflow found! splitting");

                    // We've found the point of overflow

                    if (wordIndex == 0) {
                        // Even the first word overflows
                        if (currentLinePhraseCount == 0) {
                            // Nothing more can be done, this first word just needs to overflow a line.
                            Gdx.app.error("NarrativeParagraph.addPhrase", "A single word has overflowed the paragraph.");
                            currentLine.add(new NarrativePhrase(pFont, pWords[0], pCPS));
                            // If there are words remaining, make a new phrase and add it.
                            if (pWords.length > 1) {
                                String overflowText = Utils.joinStringArray(pWords, 1, pWords.length, " ");
                                return addPhrase(new NarrativePhrase(pFont,overflowText,pCPS));
                            }
                        } else {
                            // Put this phrase on its own line
                            return addPhrase(phrase);
                        }

                    } else {
                        // This one didn't work, but the previous one did
                        String fitText = Utils.joinStringArray(pWords, 0, wordIndex, " ");
                        String overflowText = Utils.joinStringArray(pWords, wordIndex, pWords.length, " ");
                        currentLine.add(new NarrativePhrase(pFont, fitText, pCPS));
                        return addPhrase(new NarrativePhrase(pFont,overflowText,pCPS));
                    }

                    // Our work here is done.
                }
            }

            // We shoudln't be here
            Gdx.app.error("NarrativeParagraph.addPhrase", "Phrase didn't fit, then did.");
            currentLine.add(phrase);
            return phrase;

        } else {
            // The phrase will fit perfectly well.
            currentLine.add(phrase);
            return phrase;
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
        for (ArrayList<NarrativePhrase> line : phrasesByLine) {
            for (NarrativePhrase phrase : line) {
                phrase.showAll();
            }
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

        float currentX;
        float currentY;
        float maxWidth = 0;

        BitmapFont.TextBounds phraseBounds = null;
        NarrativePhrase phrase;
        ArrayList<NarrativePhrase> line;
        int i, j;

        // How many lines will be displayed this render?
        int linesToRender = phrasesByLine.size();
        for (i = 0; i < phrasesByLine.size(); i++) {
            line = phrasesByLine.get(i);
            // Is the last phrase complete?
            if (!line.get(line.size() - 1).isComplete()) {
                linesToRender = i + 1;
                break;
            }
        }

        lineLoop:for (i = 0; i < linesToRender; i++) {
            line = phrasesByLine.get(i);
            currentX = x;
            currentY = y + ((linesToRender - i - 1) * NarrativeManager.LINE_HEIGHT);
            for (j = 0; j < line.size(); j++) {
                phrase = line.get(j);
                if (j > 0) {
                    currentX += phrase.getSpaceWidth();
                }
                // Render and measure
                phraseBounds = phrase.render(batch, currentX, currentY);
                currentX += phraseBounds.width;
                if (currentX - x > maxWidth) {
                    maxWidth = currentX - x;
                }
                // Abort on the first incomplete phrase
                if (!phrase.isComplete()) {
                    break lineLoop;
                }
            }
        }

        return new Rectangle(x, y, maxWidth, linesToRender * NarrativeManager.LINE_HEIGHT);
    }

    public void update(float delta) {
        updateTime += delta;
        lineLoop:for (ArrayList<NarrativePhrase> line: phrasesByLine) {
            for (NarrativePhrase phrase: line) {
                phrase.update(delta);
                if (!phrase.isComplete()) {
                    break lineLoop;
                }
            }
        }
    }


}

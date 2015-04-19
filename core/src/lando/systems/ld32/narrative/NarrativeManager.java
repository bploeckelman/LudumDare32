package lando.systems.ld32.narrative;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld32.Constants;

import java.util.ArrayList;

public class NarrativeManager {

    private final static int CHAR_SPEED_DEFAULT = 20;   // Characters per second
    private final static int WIDTH = Constants.win_width;
    private final static int HEIGHT = 60;
    private final static int LINE_PAD = 10;
    private final static int PADDING = 10;

    private float updateTime = 0;


    private ArrayList<NarrativeLine> lines = new ArrayList<NarrativeLine>();
    private Rectangle bounds = new Rectangle(0, Constants.win_height - HEIGHT, WIDTH, HEIGHT);
    private BitmapFont font;
    /**
     * When updating lines, which line should we begin with?  This will be updated as lines are pushed off the screen
     * and updates are no longer required.
     */
    private int lineUpdateStartingIndex = 0;

    public NarrativeManager(BitmapFont font) {
        this.font = font;
        font.setColor(new Color(1, 1, 1, 1));

        // TEST
        addLine(new NarrativePhrase(font, "This is the first line.", 2));
        addPhraseToCurrentLine(new NarrativePhrase(font, "Additional words are now added.", 40));
        addPhraseToCurrentLine(new NarrativePhrase(font, "SLOW", 2));

    }

    public void addLine(NarrativePhrase phrase) {
        // Set the state of all previous lines (still being updated) to "shown"
        for (int i = lineUpdateStartingIndex; i < lines.size(); i++) {
            lines.get(i).showAll();
        }

        lines.add(new NarrativeLine(phrase));
    }

    public void addPhraseToCurrentLine(NarrativePhrase phrase) {
        if (lines.size() == 0) {
            addLine(phrase);
        } else {
            lines.get(lines.size()-1).addPhrase(phrase);
        }
    }

    public void render(SpriteBatch batch) {
        Rectangle lineBounds = null;
        NarrativeLine narrativeLine;
        float currentX = bounds.x + PADDING;
        float currentY = bounds.y + PADDING;
        for (int i = lines.size() - 1; i >= 0; i--) {
            narrativeLine = lines.get(i);
            lineBounds = narrativeLine.render(batch, currentX, currentY);
            currentY += lineBounds.height + LINE_PAD;
            if (currentY > Constants.win_height) {
                lineUpdateStartingIndex = Math.max(i - 1, 0);
                break;
            }
        }
    }

    boolean debugFlag = false;

    public void update(float delta) {
        updateTime += delta;
        for (int i = lineUpdateStartingIndex; i < lines.size(); i++) {
            lines.get(i).update(delta);
        }
        if (updateTime > 5 && !debugFlag) {
            debugFlag = true;
            addLine(new NarrativePhrase(font, "Second line, coming at you!", 15));
        }
    }

}

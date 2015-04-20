package lando.systems.ld32.narrative;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Cubic;
import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld32.Assets;
import lando.systems.ld32.GameInstance;

import java.util.ArrayList;

public class NarrativeManager {

    public final static int LINE_HEIGHT = 18;
    private final static int PADDING = 10;
    private final static int PARAGRAPH_PAD_Y = 10;
    private final static float BACKGROUND_ALPHA = 0.8f;
    public final static float CPS = 34f;

    private final float x;
    private final float y;
    private final float width;
    private float height;
    private float updateTime = 0;

    private MutableFloat tweenHeight = new MutableFloat(0);
    public boolean heightTweenInProgress = false;

    private ArrayList<NarrativeParagraph> paragraphs = new ArrayList<NarrativeParagraph>();

    /**
     * When updating paragraphs, which paragraph should we begin with?  This index will be updated as paragraphs are
     * pushed off the screen and updates are no longer required.
     */
    private int paragraphUpdateStartingIndex = 0;

    // -----------------------------------------------------------------------------------------------------------------


    public NarrativeManager(float x, float y, float width, float height) {
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


    public NarrativePhrase addParagraph(NarrativePhrase phrase) {

        // Set the state of all previous paragraphs (still being updated) to "shown"
        for (int i = paragraphUpdateStartingIndex; i < paragraphs.size(); i++) {
            paragraphs.get(i).showAll();
        }

        NarrativeParagraph narrativeParagraph = new NarrativeParagraph((int)width - (PADDING * 2));
        paragraphs.add(narrativeParagraph);
        return narrativeParagraph.addPhrase(phrase);

    }

    public NarrativePhrase addPhraseToCurrentParagraph(NarrativePhrase phrase) {
        if (paragraphs.size() == 0) {
            return addParagraph(phrase);
        } else {
            return paragraphs.get(paragraphs.size()-1).addPhrase(phrase);
        }
    }

    public void render(SpriteBatch batch) {
        batch.setColor(1, 1, 1, BACKGROUND_ALPHA);
        float thisHeight = heightTweenInProgress ? tweenHeight.floatValue() : height;
        batch.draw(Assets.black, x, y, width, thisHeight);
        batch.setColor(1,1,1,1);
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

    // -----------------------------------------------------------------------------------------------------------------

    public void setHeight(float height, float duration) {
        heightTweenInProgress = true;
        tweenHeight.setValue(this.height);
        Tween.to(tweenHeight, -1, duration)
                .target(height)
                .ease(Cubic.INOUT)
                .setCallback(
                        new TweenCallback() {
                            @Override
                            public void onEvent(int type, BaseTween<?> source) {
                                heightTweenInProgress = false;
                                NarrativeManager.this.height = tweenHeight.floatValue();
                            }
                        }).start(GameInstance.tweens);
    }

    public boolean isTweenInProgress() {
        return heightTweenInProgress;
    }

}

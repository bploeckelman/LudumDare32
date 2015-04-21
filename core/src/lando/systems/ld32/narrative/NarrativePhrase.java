package lando.systems.ld32.narrative;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Bounce;
import aurelienribon.tweenengine.equations.Sine;
import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import lando.systems.ld32.Assets;
import lando.systems.ld32.GameInstance;
import lando.systems.ld32.HSL;

public class NarrativePhrase {

    public enum Effect {
        NONE,
        BOUNCE,
        RAINBOW
    }

    private final BitmapFont font;
    private final String text;
    private String revealedText = "";
    private final float charTime;
    private final float cps;

    private final Effect effect;
    private static final float BOUNCE_DELAY = 1.4f;
    private static final float BOUNCE_DURATION = 0.6f;
    private static final float BOUNCE_HEIGHT = 10;
    private float bounceCountdown = BOUNCE_DELAY;
    private MutableFloat bounceY = new MutableFloat(0);

    private static final float RAINBOW_DURATION = 2f;
    private Color rainbowColor;

    private float updateTime = 0;
    private boolean revealComplete = false;


    // -----------------------------------------------------------------------------------------------------------------

    /**
     *
     * @param font The font
     * @param text The phrase text
     * @param cps The rate: characters per second
     */
    public NarrativePhrase(BitmapFont font, String text, float cps, Effect effect) {
        this.charTime = 1f / cps;
        this.cps = cps;
        this.effect = effect;
        this.font = font;
        this.text = text;
    }
    public NarrativePhrase(BitmapFont font, String text, float cps) {
        this(font, text, cps, Effect.NONE);
    }
    public NarrativePhrase(BitmapFont font, String text) {
        this(font, text, NarrativeManager.CPS, Effect.NONE);
    }
    public NarrativePhrase(String text) {
        this(Assets.font16, text, NarrativeManager.CPS, Effect.NONE);
    }
    public NarrativePhrase(String text, float cps) {
        this(Assets.font16, text, cps, Effect.NONE);
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
        // Measure the font without effects applied.
        BitmapFont.TextBounds textBounds = font.getBounds(revealedText);
        // Shift y because text bounds are measured from /top/ left.
        y += textBounds.height;
        // Effects
        switch(effect) {

            case BOUNCE:
                font.draw(batch, revealedText, x, y + bounceY.floatValue());
                break;

            case RAINBOW:
                Color oldColor = font.getColor().cpy();
                font.setColor(rainbowColor);
                font.draw(batch, revealedText, x, y);
                font.setColor(oldColor);
                break;

            case NONE:
            default:
                font.draw(batch, revealedText, x, y);
                break;

        }
        return textBounds;
    }

    public void showAll() {
        revealComplete = true;
        revealedText = text;
    }

    public void startEffectBounce() {
        Tween.to(bounceY, -1, BOUNCE_DURATION * 0.2f)
                .target(BOUNCE_HEIGHT)
                .ease(Sine.OUT)
                .setCallback(
                        new TweenCallback() {
                        @Override
                        public void onEvent(int type, BaseTween<?> source) {
                            Tween.to(bounceY, -1, BOUNCE_DURATION * 0.8f)
                                    .target(0)
                                    .ease(Bounce.OUT)
                                    .start(GameInstance.tweens);
                        }
                }).start(GameInstance.tweens);
    }

    public void update(float delta) {
        updateTime += delta;

        if (!revealComplete) {
            int charCount = MathUtils.floor(updateTime / charTime);
            revealedText = text.substring(0, Math.min(text.length(), charCount));
            if (charCount >= text.length()) {
                revealComplete = true;
            }
        }

        switch (effect) {

            case BOUNCE:
                bounceCountdown -= delta;
                if (bounceCountdown <= 0) {
                    bounceCountdown = BOUNCE_DELAY;
                    startEffectBounce();
                }
                break;

            case RAINBOW:

                float h = ((updateTime % RAINBOW_DURATION) / RAINBOW_DURATION);
                rainbowColor = new HSL(h, 1f, 0.5f, 1f).toRGB();
                break;

            case NONE:
            default:

        }
    }

}

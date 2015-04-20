package lando.systems.ld32.attackwords;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld32.Assets;

public class AttackWord {

    static final float bubble_margin    = 10f;
    static final float default_velocity = -120f;
    static final float flash_duration   = 0.15f;
    static final float move_delay_time  = 4.5f * flash_duration;
    static final Color dark_font_color  = new Color(0.1f, 0.1f, 0.1f, 0.2f);
    static final Color dark_bubble_color= new Color(0.3f, 0.3f, 0.3f, 0.3f);

    static BitmapFont smallFont = null;
    static BitmapFont normalFont = null;

    public static final float flyoff_duration = .8f;
    public static boolean smallWords = false;
    public static boolean darkMode = false;

    final String     word;
    final String     lowerWord;
    final String[]   letters;
    final String[]   lowercase;
    final TextBounds normalTextBounds;
    final TextBounds smallTextBounds;

    String  typed;
    float   moveDelay;
    float   flashTimer;
    boolean visible;

    public boolean    disabled;
    public Vector2    velocity;
    public Vector2    accel;
    public Vector2    origin;
    public Rectangle  bounds;
    public float dangerLevel = 0f;

    public AttackWord(String word) {
        this.word = word.toUpperCase();
        this.lowerWord = word.toLowerCase();
        this.letters = new String[word.length()];
        this.lowercase = new String[word.length()];
        for (int i = 0; i < this.word.length(); ++i) {
            letters[i] = "" + this.word.charAt(i);
            lowercase[i] = "" + this.lowerWord.charAt(i);
        }
        this.typed = "";

        if (smallFont == null) smallFont = Assets.font8;
        if (normalFont == null) normalFont = Assets.font16;

        this.smallTextBounds = new TextBounds(smallFont.getBounds(this.word));
        this.normalTextBounds = new TextBounds(normalFont.getBounds(this.word));

        this.bounds = new Rectangle(0, 0, normalTextBounds.width + 2 * bubble_margin, normalTextBounds.height + 2 * bubble_margin);
        this.velocity = new Vector2(default_velocity * 1.25f, -default_velocity * 0.75f);
        this.accel = new Vector2(0, -80);
        this.disabled = false;
        this.origin = new Vector2(bounds.x, bounds.y);
    }

    public AttackWord fire(float x, float y) {
        typed = "";
        bounds.x = x;
        bounds.y = y;
        moveDelay = move_delay_time;
        return this;
    }

    public void keyTyped(int keycode) {
        if (moveDelay > 0f) return;

        int i = typed.length();
        if (i >= letters.length) {
            return;
        }

        if (letters[i].equals(" ")) {
            typed += letters[i];
            if (++i >= letters.length) {
                return;
            }
        }

        int letter = Input.Keys.valueOf(letters[i]);
        if (keycode == letter) {
            typed += lowercase[i];
        }
    }

    public boolean isComplete() {
        return typed.equalsIgnoreCase(lowerWord);
    }

    public void update(float delta) {
        moveDelay -= delta;
        if (moveDelay <= 0f) {
            moveDelay = 0f;
            visible = true;
        } else {
            flashTimer -= delta;
            if (flashTimer <= 0f) {
                flashTimer = flash_duration;
                visible = !visible;
            }
        }

        if (moveDelay == 0f) {
            velocity.x += delta * accel.x;
            velocity.y += delta * accel.y;
            bounds.x += delta * velocity.x;
            bounds.y += delta * velocity.y;
        }
    }

    public void render(SpriteBatch batch) {
        if (!visible) return;

        if (darkMode) batch.setColor(dark_bubble_color);
        else          batch.setColor(1f, 1f - dangerLevel, 1f - dangerLevel, 1);
        Assets.speechBubble.draw(batch, bounds.x, bounds.y, bounds.width, bounds.height);
        batch.setColor(1, 1, 1, 1);

        if (disabled) return;

        float textX, textY;
        if (smallWords) {
            textX = bounds.x + bounds.width  / 2f - smallTextBounds.width  / 2f;
            textY = bounds.y + bounds.height / 2f + smallTextBounds.height / 2f;
        } else {
            textX = bounds.x + bounds.width  / 2f - normalTextBounds.width  / 2f;
            textY = bounds.y + bounds.height / 2f + normalTextBounds.height / 2f;
        }

        BitmapFont font = smallWords ? smallFont : normalFont;

        if (darkMode) font.setColor(dark_font_color);
        else          font.setColor(1, 1, 1, 1);
        font.draw(batch, lowerWord, textX, textY);
        if (darkMode) font.setColor(0, 0, 0, 1);

        font.setColor(Color.CYAN);
        font.draw(batch, typed, textX, textY);
        font.setColor(Color.WHITE);
    }

}

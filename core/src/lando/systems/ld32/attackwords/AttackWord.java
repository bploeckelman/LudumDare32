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
    static final float small_scale      = 0.8f;
    static final float normal_scale     = 1.5f;
    static final Color dark_font_color  = new Color(0.1f, 0.1f, 0.1f, 0.2f);
    static final Color dark_bubble_color= new Color(0.3f, 0.3f, 0.3f, 0.3f);

    public static final float flyoff_duration = .8f;
    public static boolean smallWords = false;
    public static boolean darkMode = false;

    final String     word;
    final String[]   letters;
    final TextBounds normalTextBounds;
    final TextBounds smallTextBounds;

    String  typed;
    float   moveDelay;
    float   flashTimer;
    boolean visible;

    public boolean    disabled;
    public Vector2    velocity;
    public Vector2    origin;
    public BitmapFont font;
    public Rectangle  bounds;
    public float dangerLevel = 0f;

    public AttackWord(String word, BitmapFont font) {
        this.word = word.toUpperCase();
        this.letters = new String[word.length()];
        for (int i = 0; i < this.word.length(); ++i) {
            letters[i] = "" + this.word.charAt(i);
        }
        this.typed = "";
        this.font = font;

        font.setScale(small_scale);
        this.smallTextBounds = new TextBounds(font.getBounds(this.word));
        font.setScale(normal_scale);
        this.normalTextBounds = new TextBounds(font.getBounds(this.word));
        font.setScale(1f);

        this.bounds = new Rectangle(0, 0, normalTextBounds.width + 2 * bubble_margin, normalTextBounds.height + 2 * bubble_margin);
        this.velocity = new Vector2(default_velocity, 0);
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
            typed += letters[i];
        }
    }

    public boolean isComplete() {
        return typed.equalsIgnoreCase(word);
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
            font.setScale(small_scale);
            textX = bounds.x + bounds.width  / 2f - smallTextBounds.width  / 2f;
            textY = bounds.y + bounds.height / 2f + smallTextBounds.height / 2f;
        } else {
            font.setScale(normal_scale);
            textX = bounds.x + bounds.width  / 2f - normalTextBounds.width  / 2f;
            textY = bounds.y + bounds.height / 2f + normalTextBounds.height / 2f;
        }

        if (darkMode) font.setColor(dark_font_color);
        else          font.setColor(1, 1, 1, 1);
        font.draw(batch, word, textX, textY);
        if (darkMode) font.setColor(0, 0, 0, 1);

        font.setColor(Color.CYAN);
        font.draw(batch, typed, textX, textY);
        font.setColor(Color.WHITE);

        font.setScale(1);
    }

}

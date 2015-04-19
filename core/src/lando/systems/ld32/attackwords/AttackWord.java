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

    final String     word;
    final String[]   letters;
    final TextBounds textBounds;

    String typed;
    float moveDelay;

    public Vector2    velocity;
    public BitmapFont font;
    public Rectangle  bounds;

    public AttackWord(String word, BitmapFont font) {
        this.word = word.toUpperCase();
        this.letters = new String[word.length()];
        for (int i = 0; i < this.word.length(); ++i) {
            letters[i] = ""+this.word.charAt(i);
        }
        this.typed = "";
        this.font = font;
        this.textBounds = new TextBounds(font.getBounds(this.word));
        this.bounds = new Rectangle(0, 0, textBounds.width + 2 * bubble_margin, textBounds.height + 2 * bubble_margin);
        this.velocity = new Vector2(default_velocity, 0);
    }

    public AttackWord fire(float x, float y) {
        typed = "";
        bounds.x = x;
        bounds.y = y;
        moveDelay = 0.5f;
        return this;
    }

    public void keyTyped(int keycode) {
        int i = typed.length();
        if (i >= letters.length) {
            return;
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
        }

        if (moveDelay == 0f) {
            bounds.x += delta * velocity.x;
            bounds.y += delta * velocity.y;
        }
    }

    public void render(SpriteBatch batch) {
        Assets.speechBubble.draw(batch, bounds.x, bounds.y, bounds.width, bounds.height);

        float textX = bounds.x + bounds.width  / 2f - textBounds.width  / 2f;
        float textY = bounds.y + bounds.height / 2f + textBounds.height / 2f;
        font.draw(batch, word, textX, textY);

        font.setColor(Color.RED);
        font.draw(batch, typed, textX, textY);
        font.setColor(Color.WHITE);
    }

}

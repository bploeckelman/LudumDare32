package lando.systems.ld32.killphrase;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Back;
import aurelienribon.tweenengine.equations.Bounce;
import aurelienribon.tweenengine.equations.Cubic;
import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld32.Assets;
import lando.systems.ld32.Constants;
import lando.systems.ld32.GameInstance;
import lando.systems.ld32.Statistics;
import lando.systems.ld32.Utils.Callback;
import lando.systems.ld32.attackwords.AttackWord;
import lando.systems.ld32.effects.Explode;
import lando.systems.ld32.effects.Xout;
import lando.systems.ld32.tweens.Vector2Accessor;

public class KillPhrase {
    private final String space = " ";

    public static float dropRate = 1f;
    public static float dropOffset = -100f;

    public static int boxSize = 32;
    public static int boxSpacing = 4;
    public static int spaceSize = 16;

    public static Rectangle bounds = new Rectangle(
        5, Constants.win_height-100, Constants.win_width, boxSize
    );

    public boolean[] enabled;
    public boolean[] visible;
    public TextBounds[] charBounds;
    public Vector2[] charOrigins;
    public Vector2[] boxOrigins;
    public MutableFloat[] yOffsets;
    public String typed;

    private Vector2 temp = new Vector2();

    private String fullPhrase;
    private String[] phrase;
    private BitmapFont font;

    Array<Explode> explodes;
    Array<Xout> xouts;

    public KillPhrase(String phrase) {
        this.font = Assets.font16;
        this.typed = "";
        this.fullPhrase = phrase.toUpperCase();
        this.phrase = new String[phrase.length()];
        enabled = new boolean[this.phrase.length];
        visible = new boolean[this.phrase.length];
        charBounds = new TextBounds[this.phrase.length];
        charOrigins = new Vector2[this.phrase.length];
        boxOrigins = new Vector2[this.phrase.length];
        yOffsets = new MutableFloat[this.phrase.length];
        explodes = new Array<Explode>();
        xouts = new Array<Xout>();

        float width = 0;
        for(int i=0; i<this.phrase.length; i++) {
            this.phrase[i] = String.valueOf(fullPhrase.charAt(i));
            if(this.phrase[i].equals(space)) {
                width += spaceSize + boxSpacing;
            } else {
                width += boxSize + boxSpacing;
            }
        }
        bounds.setX((Constants.win_width/2) - (width/2));
        bounds.setWidth(width);

        float xOffset = bounds.x;
        for(int i=0; i<this.phrase.length; i++) {
            enabled[i] = false;
            visible[i] = false;
            TextBounds charBounds = font.getBounds(this.phrase[i]);

            if(this.phrase[i].equals(space)) {
                xOffset += spaceSize + boxSpacing;
                continue;
            } else if(i != 0) {
                xOffset += boxSize + boxSpacing;
            }
            charOrigins[i] = new Vector2(
                xOffset + boxSize/2f - charBounds.width/2f,
                bounds.y + (boxSize/2f) + (charBounds.height/2f)
            );
            boxOrigins[i] = new Vector2(xOffset, bounds.y);
            yOffsets[i] = new MutableFloat(0f);
        }
    }


    public Vector2 enableLetter() {
        return enableLetter(null);
    }
    public Vector2 enableLetter(Callback cb) {
        for(int i=0; i<phrase.length; i++) {
            if(!enabled[i]) {
                enabled[i] = true;
                if(phrase[i].equals(space)) {
                    continue;
                }
                tweenExplode(i, cb);
                return temp.set(boxOrigins[i].x + boxSize / 2f, boxOrigins[i].y + boxSize / 2f).cpy();
            }
        }
        return null;
    }

    public Vector2 disableLetter() {
        for(int i=phrase.length-1; i>=0; i--) {
            if(enabled[i]) {
                enabled[i] = false;
                if(phrase[i].equals(space)) {
                    continue;
                }
                tweenXout(i);
                return temp.set(boxOrigins[i].x + boxSize / 2f, boxOrigins[i].y + boxSize / 2f).cpy();
            }
        }
        return null;
    }

    public boolean isComplete() {
        for(int i=0; i<phrase.length; i++) {
            if(!enabled[i]) {
                return false;
            }
        }

        return true;
    }

    public boolean isTyped() {
        return typed.equals(fullPhrase);
    }

    public void clean() {
        for(int i=0; i<phrase.length; i++) {
            enabled[i] = false;
        }
        typed = "";
    }

    public void update(float delta) {
        for (int i = explodes.size - 1; i >= 0; --i) {
            final Explode explode = explodes.get(i);
            explode.update(delta);
            if (explode.alive) {
                explode.position.y = boxOrigins[boxOrigins.length-1].y +
                    (boxSize/2) + yOffsets[yOffsets.length-1].floatValue();
            } else {
                Explode.explodePool.free(explode);
                explodes.removeIndex(i);
            }
        }
        for (int i = xouts.size - 1; i >= 0; --i) {
            final Xout xout = xouts.get(i);
            xout.update(delta);
            if (!xout.alive) {
                Xout.xoutPool.free(xout);
                xouts.removeIndex(i);
            }
        }
    }

    public void render(SpriteBatch batch) {
        font.setColor(Color.WHITE);
        for(int i=0; i<phrase.length; i++) {
            if(phrase[i].equals(space)) {
                continue;
            }

            batch.draw(
                Assets.killphraseBox,
                boxOrigins[i].x,
                boxOrigins[i].y + yOffsets[i].floatValue(),
                boxSize, boxSize);

            if(visible[i]) {
                font.draw(batch, phrase[i], charOrigins[i].x, charOrigins[i].y + yOffsets[i].floatValue());
            }
        }

        font.setColor(Color.RED);
        for (int i = 0; i < typed.length(); ++i) {
            if (typed.charAt(i) == ' ') continue;
            font.draw(batch, "" + typed.charAt(i), charOrigins[i].x, charOrigins[i].y + yOffsets[i].floatValue());
        }
        font.setColor(Color.WHITE);

        for (Explode explode : explodes) {
            explode.render(batch);
        }
        for (Xout xout : xouts) {
            xout.render(batch);
        }
    }

    public void keyTyped(int keycode) {
        int i = typed.length();
        if (i >= phrase.length) {
            return;
        }

        if (phrase[i].equals(" ")) {
            typed += phrase[i];
            if (++i >= phrase.length) {
                return;
            }
        }

        int letter = Input.Keys.valueOf(phrase[i]);
        if (keycode == letter) {
            typed += phrase[i];
            Statistics.numLettersTyped++;
            Assets.keyValid.play(.2f);
        }
    }

    public void tweenDown() {
        tweenUpDown(true);
    }
    public void tweenUp() {
        tweenUpDown(false);
    }

    private void tweenUpDown(boolean down) {
        Timeline timeline = Timeline.createParallel();
        for(int i=0; i<phrase.length; i++) {
            if(!phrase[i].equals(space)) {
                timeline.push(Tween.to(yOffsets[i], -1, dropRate)
                    .target(down ? dropOffset : 0)
                    .ease(Bounce.OUT)
                    .delay(i * .01f));
            }
        }
        timeline.start(GameInstance.tweens);
    }

    private void doExplode(Vector2 origin, float scale) {
        float x = origin.x + boxSize / 2f;
        float y = origin.y + boxSize / 2f;
        Explode explode = Explode.explodePool.obtain();
        explode.init(x, y);
        explodes.add(explode);
    }
    public void tweenExplode(final int letter, final Callback cb) {
        Tween.to(new MutableFloat(0f), -1, AttackWord.flyoff_duration - .2f)
            .setCallback(new TweenCallback() {
                @Override
                public void onEvent(int type, BaseTween<?> source) {
                    doExplode(boxOrigins[letter], 0f);
                    visible[letter] = true;
                    if(cb != null) cb.run();
                    if (letter != phrase.length - 1) {
                        Tween.to(boxOrigins[letter], Vector2Accessor.X, .5f)
                            .waypoint(boxOrigins[letter].x - boxSpacing)
                            .waypoint(boxOrigins[letter].x + boxSpacing)
                            .target(boxOrigins[letter].x)
                            .ease(Back.INOUT)
                            .start(GameInstance.tweens);
                        Assets.killphraseBump.play(.5f);
                    }
                }
            }).start(GameInstance.tweens);
    }
    public void tweenExplode(final int letter) {
        tweenExplode(letter, null);
    }

    private void doXout(Vector2 origin, float scale) {
        float x = origin.x + 4 + (boxSize / 2f);
        float y = origin.y + boxSize / 2f;
        Xout xout = Xout.xoutPool.obtain();
        xout.init(x, y);
        xouts.add(xout);
    }
    public void tweenXout(final int letter) {
        Tween.to(new MutableFloat(0f), -1, (AttackWord.flyoff_duration/2))
            .setCallback(new TweenCallback() {
                @Override
                public void onEvent(int type, BaseTween<?> source) {
                    doXout(boxOrigins[letter], 0f);
                    visible[letter] = false;
                    Tween.to(yOffsets[letter], -1, .2f)
                        .target(boxSize / 2)
                        .ease(Cubic.OUT)
                        .repeatYoyo(1, 0f)
                        .start(GameInstance.tweens);
                    Assets.killphraseCut.play(.5f);
                }
            }).start(GameInstance.tweens);
    }

}

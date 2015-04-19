package lando.systems.ld32.screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Quint;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld32.Assets;
import lando.systems.ld32.Constants;
import lando.systems.ld32.GameInstance;
import lando.systems.ld32.attackwords.AttackWord;
import lando.systems.ld32.effects.Puff;
import lando.systems.ld32.effects.StunStars;
import lando.systems.ld32.entities.Enemy;
import lando.systems.ld32.entities.EnemyFactory;
import lando.systems.ld32.entities.Player;
import lando.systems.ld32.input.KeyboardInputAdapter;
import lando.systems.ld32.killphrase.KillPhrase;
import lando.systems.ld32.tweens.ColorAccessor;
import lando.systems.ld32.tweens.RectangleAccessor;

public class FightScreen extends ScreenAdapter {
    GameInstance game;

    // TODO: set relative to kill phrase length or enemy strength?
    private static final float stagger_time = 4f;

    BitmapFont         font;
    Color              backgroundColor;
    FrameBuffer        sceneFBO;
    TextureRegion      sceneRegion;
    OrthographicCamera sceneCamera;

    Enemy             enemy;
    Array<AttackWord> attackWords;
    KillPhrase        killPhrase;

    Player      player;
    Array<Puff> puffs;
    StunStars stunStars;

    KeyboardInputAdapter keyboardInputAdapter;
    float                staggerTimer;

    public FightScreen(GameInstance game) {
        this.game = game;

        font = new BitmapFont();
        font.setMarkupEnabled(true);
        backgroundColor = new Color(1, 1, 1, 1);
        sceneFBO = new FrameBuffer(Pixmap.Format.RGBA8888, Constants.win_width, Constants.win_height, false);
        sceneRegion = new TextureRegion(sceneFBO.getColorBufferTexture());
        sceneRegion.flip(false, true);
        sceneCamera = new OrthographicCamera();
        sceneCamera.setToOrtho(false, sceneFBO.getWidth(), sceneFBO.getHeight());
        sceneCamera.update();

        font.setColor(0, 0, 0, 1);
        enemy = EnemyFactory.getBoss(font, 1);
        attackWords = new Array<AttackWord>();
        killPhrase = new KillPhrase(enemy.killPhrase, font);
        player = new Player();

        keyboardInputAdapter = new KeyboardInputAdapter(attackWords, killPhrase);
        Gdx.input.setInputProcessor(keyboardInputAdapter);

        puffs = new Array<Puff>();
        stunStars = new StunStars();
    }

    @Override
    public void render(float delta) {
        update(delta);
        renderScene(Assets.batch);
        renderScreen(Assets.batch);
    }

    public void update(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) game.exit();

        GameInstance.tweens.update(delta);

        for (int i = puffs.size - 1; i >= 0; --i) {
            final Puff puff = puffs.get(i);
            puff.update(delta);
            if (!puff.alive) {
                Puff.puffPool.free(puff);
                puffs.removeIndex(i);
            }
        }

        staggerTimer -= delta;
        if (staggerTimer <= 0f) {
            staggerTimer = 0f;
            if(keyboardInputAdapter.staggerWindow) {
                tweenBgColor(1f, 1f, 1f, KillPhrase.dropRate);
            }
            keyboardInputAdapter.staggerWindow = false;
            killPhrase.typed = "";
            stunStars.alive = false;
            enemy.paused = false;
        }

        enemy.update(delta);

        if (staggerTimer == 0f) {
            AttackWord word = enemy.generateAttack();
            if (word != null) {
                attackWords.add(word);
            }
        }

        for(int i=0; i<attackWords.size; i++) {
            attackWords.get(i).update(delta);
        }

        if (attackWords.size > 0) {
            final AttackWord word = attackWords.first();
            if (word.isComplete() && !word.disabled) {
                word.disabled = true;
                doPuff(word.bounds, 3f);

                Vector2 targetLetter = killPhrase.enableLetter();
                if (targetLetter == null) {
                    attackWords.removeValue(word, true);
                } else {
                    word.velocity.set(0, 0);
                    Tween.to(word.bounds, RectangleAccessor.XYWH, 0.8f)
                         .target(targetLetter.x, targetLetter.y, 0, 0)
                         .ease(Quint.OUT)
                         .setCallback(new TweenCallback() {
                             @Override
                             public void onEvent(int i, BaseTween<?> baseTween) {
                                 attackWords.removeValue(word, true);
                             }
                         })
                         .start(GameInstance.tweens);
                }

                if (killPhrase.isComplete()) {
                    staggerTimer = stagger_time;
                    keyboardInputAdapter.staggerWindow = true;
                    // TODO: puff all attack words prior to clearing
                    attackWords.clear();
                    stunStars.init(
                        enemy.position.x +
                            (Assets.stunStarsRegions[0][0].getRegionWidth() / 2) * StunStars.stunstars_scale,
                        enemy.position.y + enemy.keyFrame.getRegionHeight() * enemy.scale);
                    enemy.paused = true;
                    tweenBgColor(.5f, .5f, .5f, KillPhrase.dropRate);
                }
            }
            else if (word.bounds.x < player.position.x && !word.disabled) {
                word.disabled = true;
                doPuff(word.bounds, 2f);

                Vector2 targetLetter = killPhrase.disableLetter();
                if (targetLetter == null) {
                    attackWords.removeValue(word, true);
                } else {
                    word.velocity.set(0, 0);
                    Tween.to(word.bounds, RectangleAccessor.XYWH, 0.8f)
                         .target(targetLetter.x, targetLetter.y, 0, 0)
                         .ease(Quint.OUT)
                         .setCallback(new TweenCallback() {
                             @Override
                             public void onEvent(int i, BaseTween<?> baseTween) {
                                 attackWords.removeValue(word, true);
                             }
                         })
                         .start(GameInstance.tweens);
                }
            }
        }

        if (killPhrase.isTyped()) {
            // TODO: perform a fancy fanfare and revert back to overworld
            enemy = EnemyFactory.getBoss(font, 1);
            killPhrase = new KillPhrase(enemy.killPhrase, font);
            keyboardInputAdapter.killPhrase = killPhrase;
            tweenBgColor(1f, 1f, 1f, KillPhrase.dropRate/2);
        }

        if(stunStars.alive) {
            stunStars.update(delta);
        }

        player.update(delta);
    }

    private void renderScene(SpriteBatch batch) {
        sceneFBO.begin();
        {
            Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, 1.0f);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            batch.setProjectionMatrix(sceneCamera.combined);
            batch.setShader(null);
            batch.begin();

            /*
             * Render Stuff!
             */
            batch.setColor(backgroundColor);
            batch.draw(Assets.background1, 0, 0, sceneCamera.viewportWidth, sceneCamera.viewportHeight);

            enemy.render(batch);
            for(int i=0; i<attackWords.size; i++) {
                attackWords.get(i).render(batch);
            }
            if(stunStars.alive) {
                stunStars.render(batch);
            }

            player.render(batch);

            for (Puff puff : puffs) {
                puff.render(batch);
            }

            batch.setColor(Color.WHITE);

            killPhrase.render(batch);

            batch.end();
        }
        sceneFBO.end();
    }

    private void renderScreen(SpriteBatch batch) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(sceneRegion, 0, 0);
        batch.end();
    }

    private void doPuff(Rectangle bounds, float scale) {
        float x = bounds.x + bounds.width / 2f;
        float y = bounds.y + bounds.height / 2f;
        Puff puff = Puff.puffPool.obtain();
        puff.init(x, y);
        puffs.add(puff);
    }

    private void tweenBgColor(float r, float g, float b, float rate) {
        Tween.to(backgroundColor, ColorAccessor.RGB, rate)
            .target(r, g, b)
            .ease(Quint.INOUT)
            .start(GameInstance.tweens);
    }

}

package lando.systems.ld32.screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Linear;
import aurelienribon.tweenengine.equations.Quint;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld32.Assets;
import lando.systems.ld32.Constants;
import lando.systems.ld32.GameInstance;
import lando.systems.ld32.Utils.Callback;
import lando.systems.ld32.Utils.Shake;
import lando.systems.ld32.attackwords.AttackWord;
import lando.systems.ld32.effects.Puff;
import lando.systems.ld32.effects.StunStars;
import lando.systems.ld32.entities.Enemy;
import lando.systems.ld32.entities.EnemyFactory;
import lando.systems.ld32.entities.Player;
import lando.systems.ld32.input.KeyboardInputAdapter;
import lando.systems.ld32.killphrase.KillPhrase;
import lando.systems.ld32.spellwords.SpellWord;
import lando.systems.ld32.tweens.ColorAccessor;
import lando.systems.ld32.tweens.RectangleAccessor;

public class FightScreen extends ScreenAdapter {
    GameInstance game;

    // TODO: set relative to kill phrase length or enemy strength?
    private static final float stagger_time = 4f;
    private static final float post_timeout = 6f;

    Color              backgroundColor;
    FrameBuffer        sceneFBO;
    TextureRegion      sceneRegion;
    OrthographicCamera sceneCamera;
    OrthographicCamera screenCamera;
    Shake shake;

    Enemy             enemy;
    Array<AttackWord> attackWords;
    KillPhrase        killPhrase;

    Player      player;
    Array<Puff> puffs;
    StunStars   stunStars;
    SpellWord   spellWord;

    KeyboardInputAdapter keyboardInputAdapter;
    float                staggerTimer;

    boolean doPost = false;
    float accum = 0f;

    public FightScreen(GameInstance game) {
        this.game = game;

        backgroundColor = new Color(1, 1, 1, 1);
        sceneFBO = new FrameBuffer(Pixmap.Format.RGBA8888, Constants.win_width, Constants.win_height, false);
        sceneRegion = new TextureRegion(sceneFBO.getColorBufferTexture());
        sceneRegion.flip(false, true);
        sceneCamera = new OrthographicCamera();
        sceneCamera.setToOrtho(false, sceneFBO.getWidth(), sceneFBO.getHeight());
        sceneCamera.update();
        shake = new Shake();

        screenCamera = new OrthographicCamera();
        screenCamera.setToOrtho(false, Constants.win_width, Constants.win_height);
        screenCamera.update();

        enemy = EnemyFactory.getBoss(1);
        attackWords = new Array<AttackWord>();
        killPhrase = new KillPhrase(enemy.killPhrase);
        player = new Player();

        keyboardInputAdapter = new KeyboardInputAdapter(attackWords, killPhrase);
        Gdx.input.setInputProcessor(keyboardInputAdapter);

        puffs = new Array<Puff>();
        stunStars = new StunStars();

        spellWord = null;
    }

    @Override
    public void render(float delta) {
        update(delta);
        renderScene(Assets.batch);
        renderScreen(Assets.batch);
    }

    public void update(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) game.exit();
        if (doPost) {
            accum += delta * 2f;
            if (accum > post_timeout) {
                accum = 0f;
                doPost = false;
            }
        }

        GameInstance.tweens.update(delta);
        shake.update(delta, sceneCamera, sceneCamera.viewportWidth/2, sceneCamera.viewportHeight/2);

        for (int i = puffs.size - 1; i >= 0; --i) {
            final Puff puff = puffs.get(i);
            puff.update(delta);
            if (!puff.alive) {
                Puff.puffPool.free(puff);
                puffs.removeIndex(i);
            }
        }

        killPhrase.update(delta);

        staggerTimer -= delta;
        if (staggerTimer <= 0f) {
            staggerTimer = 0f;
            if(keyboardInputAdapter.staggerWindow) {
                tweenBgColor(1f, 1f, 1f, KillPhrase.dropRate);
                killPhrase.tweenUp();
                shake.shake(.33f);
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
            if (spellWord == null) {
                spellWord = enemy.generateSpell();
                if (spellWord != null) {
                    spellWord.applySpell(this);
                }
                keyboardInputAdapter.spellWord = spellWord;
            }
        }

        if (spellWord != null && spellWord.isComplete()) {
            spellWord.removeSpell(this);
            spellWord = null;
            keyboardInputAdapter.spellWord = null;
            enemy.setSpellTimer(0f);
        }

        for (AttackWord word : attackWords) {
            word.update(delta);
            if (!word.disabled) {
                word.dangerLevel = 1f - ((word.bounds.x - player.position.x) / (word.origin.x - player.position.x));
            }
        }

        // Handle attack word completion
        if (attackWords.size > 0) {
            final AttackWord word = attackWords.first();
            if (word.isComplete() && !word.disabled) {
                word.disabled = true;
                doPuff(word.bounds, 3f);

                Vector2 targetLetter = killPhrase.enableLetter(new Callback() {
                    @Override
                    public void run() {
                        shake.shake(.2f);
                    }
                });
                if (targetLetter == null) {
                    attackWords.removeValue(word, true);
                } else {
                    word.velocity.set(0, 0);
                    Tween.to(word.bounds, RectangleAccessor.XYWH, AttackWord.flyoff_duration)
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
                    shake.shake(1f);

                    for (AttackWord attackWord : attackWords) {
                        doPuff(attackWord.bounds, 2f);
                    }
                    attackWords.clear();

                    stunStars.init(
                        enemy.position.x +
                            (Assets.stunStarsRegions[0][0].getRegionWidth() / 2) * StunStars.stunstars_scale,
                        enemy.position.y + enemy.keyFrame.getRegionHeight() * enemy.scale);
                    enemy.paused = true;

                    killPhrase.tweenDown();

                    tweenBgColor(.5f, .5f, .5f, KillPhrase.dropRate);
                }
            }
            else if (word.bounds.x < player.position.x && !word.disabled) {
                word.disabled = true;
                doPuff(word.bounds, 2f);
                shake.shake(.5f);

                Vector2 targetLetter = killPhrase.disableLetter();
                if (targetLetter == null) {
                    attackWords.removeValue(word, true);
                } else {
                    word.velocity.set(0, 0);
                    Tween.to(word.bounds, RectangleAccessor.XYWH, AttackWord.flyoff_duration/2)
                         .target(targetLetter.x, targetLetter.y, 0, 0)
                         .ease(Linear.INOUT)
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

        // Enemy destroyed
        if (killPhrase.isTyped()) {
            // TODO: perform a fancy fanfare and revert back to overworld
            shake.shake(5f);
            stunStars.alive = false;

            enemy = EnemyFactory.getBoss(1);
            killPhrase = new KillPhrase(enemy.killPhrase);

            if (spellWord != null) {
                spellWord.removeSpell(this);
                spellWord = null;
            }

            keyboardInputAdapter.spellWord = null;
            keyboardInputAdapter.killPhrase = killPhrase;

            doPost = true;
            accum = 0f;

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
            batch.draw(Assets.forestBackground, -64, -64);

            enemy.render(batch);
            for(int i=0; i<attackWords.size; i++) {
                attackWords.get(i).render(batch);
            }
            if(stunStars.alive) {
                stunStars.render(batch);
            }

            player.render(batch);

            if (spellWord != null) {
                spellWord.render(batch);
            }

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
        final ShaderProgram postShader = doPost ? Assets.postShader : null;

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(screenCamera.combined);
        batch.setShader(postShader);
        batch.begin();
        if (doPost) {
            Assets.postShader.setUniformf("u_time", accum);
            Assets.postShader.setUniformf("u_resolution", screenCamera.viewportWidth, screenCamera.viewportHeight);
            Assets.postShader.setUniformf("u_screenPos",
                                          screenCamera.viewportWidth / 2f,
                                          screenCamera.viewportHeight / 2f);
        }
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

    public void tweenBgColor(float r, float g, float b, float rate) {
        Tween.to(backgroundColor, ColorAccessor.RGB, rate)
            .target(r, g, b)
            .ease(Quint.INOUT)
            .start(GameInstance.tweens);
    }

}

package lando.systems.ld32.screens;

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

    Player player;
    Array<Puff> puffs;
    StunStars stunStars;

    KeyboardInputAdapter keyboardInputAdapter;
    float                staggerTimer;

    public FightScreen(GameInstance game) {
        this.game = game;

        font = new BitmapFont();
        font.setMarkupEnabled(true);
        backgroundColor = Color.BLACK;
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
            if (attackWords.first().isComplete()) {
                doPuff(attackWords.first().bounds, 3f);

                attackWords.removeIndex(0);
                killPhrase.enableLetter();
                if (killPhrase.isComplete()) {
                    staggerTimer = stagger_time;
                    keyboardInputAdapter.staggerWindow = true;
                    // TODO: puff all attack words prior to clearing
                    attackWords.clear();
                    stunStars.init(
                        enemy.position.x +
                            (Assets.stunStarsRegions[0][0].getRegionWidth()/2) * StunStars.stunstars_scale,
                        enemy.position.y + enemy.keyFrame.getRegionHeight() * enemy.scale);
                    enemy.paused = true;
                }
            }
            else if (attackWords.first().bounds.x < player.position.x) {
                doPuff(attackWords.first().bounds, 2f);

                attackWords.removeIndex(0);
                killPhrase.disableLetter();
            }
        }

        if (killPhrase.isTyped()) {
            // TODO: perform a fancy fanfare and revert back to overworld
            enemy = EnemyFactory.getBoss(font, 1);
            killPhrase = new KillPhrase(enemy.killPhrase, font);
            keyboardInputAdapter.killPhrase = killPhrase;
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
            batch.draw(Assets.background1, 0, 0, sceneCamera.viewportWidth, sceneCamera.viewportHeight);

            enemy.render(batch);
            for(int i=0; i<attackWords.size; i++) {
                attackWords.get(i).render(batch);
            }
            killPhrase.render(batch);
            if(stunStars.alive) {
                stunStars.render(batch);
            }

            player.render(batch);

            for (Puff puff : puffs) {
                puff.render(batch);
            }

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

}

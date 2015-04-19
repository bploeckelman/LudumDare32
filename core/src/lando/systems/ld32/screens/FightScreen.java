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
import com.badlogic.gdx.utils.Array;
import lando.systems.ld32.Assets;
import lando.systems.ld32.Constants;
import lando.systems.ld32.GameInstance;
import lando.systems.ld32.attackwords.AttackWord;
import lando.systems.ld32.entities.Enemy;
import lando.systems.ld32.entities.Player;
import lando.systems.ld32.input.KeyboardInputAdapter;
import lando.systems.ld32.killphrase.KillPhrase;

import java.util.ArrayList;

public class FightScreen extends ScreenAdapter {
    GameInstance game;

    BitmapFont font;
    Color backgroundColor;
    FrameBuffer sceneFBO;
    TextureRegion sceneRegion;
    OrthographicCamera sceneCamera;

    Enemy enemy;
    Array<AttackWord> attackWords;
    KillPhrase killPhrase;

    Player player;

    KeyboardInputAdapter keyboardInputAdapter;

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
        enemy = new Enemy(font);
        attackWords = new Array<AttackWord>();
        killPhrase = new KillPhrase("MY AWESOME PHRASE", font);
        killPhrase.enableLetter();
        killPhrase.enableLetter();
        killPhrase.enableLetter();
        killPhrase.enableLetter();
        killPhrase.enableLetter();
        player = new Player();

        keyboardInputAdapter = new KeyboardInputAdapter(attackWords);
        Gdx.input.setInputProcessor(keyboardInputAdapter);
    }

    @Override
    public void render(float delta) {
        update(delta);
        renderScene(Assets.batch);
        renderScreen(Assets.batch);
    }

    public void update(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) game.exit();

        enemy.update(delta);
        AttackWord word = enemy.generateAttack();
        if(word != null) {
            attackWords.add(word);
        }
        for(int i=0; i<attackWords.size; i++) {
            attackWords.get(i).update(delta);
        }

        if (attackWords.size > 0) {
            if (attackWords.first().isComplete()) {
                attackWords.removeIndex(0);
                killPhrase.enableLetter();
            }
            else if (attackWords.first().bounds.x < player.position.x) {
                attackWords.removeIndex(0);
                killPhrase.disableLetter();
            }
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

            player.render(batch);

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
}
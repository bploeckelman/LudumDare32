package lando.systems.ld32.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import lando.systems.ld32.Assets;
import lando.systems.ld32.Constants;
import lando.systems.ld32.GameInstance;
import lando.systems.ld32.Statistics;

public class GameOverScreen extends ScreenAdapter {

    static GameInstance game;

    OrthographicCamera camera;



    public GameOverScreen(GameInstance game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.win_width, Constants.win_height);
        camera.update();
    }

    @Override
    public void render(float delta) {
        float yOffset = 38;
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) game.exit();

        Gdx.gl20.glClearColor(0, 0, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        final SpriteBatch batch = Assets.batch;
        batch.setProjectionMatrix(camera.combined);
        batch.setShader(null);
        batch.begin();
        batch.draw(Assets.background1, 0, 0, camera.viewportWidth, camera.viewportHeight);
        int i = 0;
        Assets.font32.draw(batch, "GAME OVER",38, Constants.win_height - yOffset*++i);
        ++i;
        Assets.font32.draw(batch, Statistics.numLettersTypedStr + " " + Statistics.numLettersTyped, 38, Constants.win_height - yOffset*++i);
        Assets.font32.draw(batch, Statistics.numAttackWordsFiredStr + " " + Statistics.numAttackWordsFired, 38, Constants.win_height - yOffset*++i);
        Assets.font32.draw(batch, Statistics.numWordsDefendedStr + " " + Statistics.numWordsDefended, 38, Constants.win_height - yOffset*++i);
        Assets.font32.draw(batch, Statistics.numWordsHitStr + " " + Statistics.numWordsHit, 38, Constants.win_height - yOffset*++i);
        Assets.font32.draw(batch, Statistics.numSpellsCastStr + " " + Statistics.numSpellsCast, 38, Constants.win_height - yOffset*++i);
        Assets.font32.draw(batch, Statistics.numSpellsCounteredStr + " " + Statistics.numSpellsCountered, 38, Constants.win_height - yOffset*++i);
        Assets.font32.draw(batch, Statistics.numFearsUncoveredStr + " " + Statistics.numFearsUncovered, 38, Constants.win_height - yOffset*++i);
        Assets.font32.draw(batch, Statistics.numStaggersStr + " " + Statistics.numStaggers, 38, Constants.win_height - yOffset*++i);
        Assets.font32.draw(batch, Statistics.playTimeStr + " " + MathUtils.round(Statistics.playTime/60) + " Min", 38, Constants.win_height - yOffset*++i);
        batch.end();
    }

}

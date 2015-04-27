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

    float yOffset = 38;
    float waitBetween = 1f;
    float waitTimer;
    String[] statStrings;
    int[] statNums;

    public GameOverScreen(GameInstance game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.win_width, Constants.win_height);
        camera.update();

        statStrings = Statistics.getStatStrings();
        waitTimer = 0f;
    }

    @Override
    public void render(float delta) {
        statNums = Statistics.getStatNums();
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) game.exit();

        Gdx.gl20.glClearColor(0, 0, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        final SpriteBatch batch = Assets.batch;
        batch.setProjectionMatrix(camera.combined);
        batch.setShader(null);
        batch.begin();
        batch.draw(Assets.background1, 0, 0, camera.viewportWidth, camera.viewportHeight);
        int i = 0;
        Assets.font32.draw(batch, "THE END",38, Constants.win_height - yOffset*++i);
        waitTimer += delta;
        ++i;
        for(;i-2<statStrings.length; i++) {
            if(waitTimer >= waitBetween*i-1) {
                if(i-2 == statStrings.length-1) {
                    Assets.font32.draw(batch, Statistics.playTimeStr + " " + MathUtils.round(Statistics.playTime/60) + " Min", 38, Constants.win_height - yOffset*++i);
                } else {
                    Assets.font32.draw(batch, statStrings[i - 2] + " " + statNums[i - 2], 38, Constants.win_height - yOffset * i);
                }
            }
        }
        batch.end();
    }

}

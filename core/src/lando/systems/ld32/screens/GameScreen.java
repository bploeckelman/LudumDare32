package lando.systems.ld32.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import lando.systems.ld32.Assets;
import lando.systems.ld32.Constants;
import lando.systems.ld32.GameInstance;

public class GameScreen extends ScreenAdapter {


    GameInstance game;
    OrthographicCamera camera;

    final float overworld_offset_x;
    final float speed = 120f;
    float yPos = 0;

    public GameScreen(GameInstance game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.win_width, Constants.win_height);
        camera.update();
        overworld_offset_x = camera.viewportWidth / 2f - Assets.overworld.getWidth() / 2f;
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) game.exit();

        if      (Gdx.input.isKeyPressed(Input.Keys.UP))   yPos -= speed * delta;
        else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) yPos += speed * delta;

        Gdx.gl20.glClearColor(0, 0, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Assets.batch.setProjectionMatrix(camera.combined);
        Assets.batch.setShader(null);
        Assets.batch.begin();
        Assets.batch.draw(Assets.overworld, overworld_offset_x, yPos);
        Assets.batch.end();
    }

}

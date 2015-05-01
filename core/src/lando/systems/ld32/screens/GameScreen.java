package lando.systems.ld32.screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.*;
import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import lando.systems.ld32.Assets;
import lando.systems.ld32.Constants;
import lando.systems.ld32.GameInstance;
import lando.systems.ld32.Utils;

public class GameScreen extends ScreenAdapter {

    public static final float tower_position   = -2040;
    public static final float dungeon_position = -1740;
    public static final float water_position   = -1380;
    public static final float desert_position  = -1020;
    public static final float cave_position    = -650;
    public static final float forest_position  = -300;
    public static final float library_position = 0;

    GameInstance       game;
    OrthographicCamera camera;

    final float overworld_offset_x;
    final float speed = 240f;

    int currentLevel = 0;
    FrameBuffer fbo;
    OrthographicCamera fboCamera;
    TextureRegion fboRegion;

    MutableFloat yPos = new MutableFloat(0f);

    public boolean introTweenComplete = false;

    public GameScreen(GameInstance game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.win_width, Constants.win_height);
        camera.update();

        fboCamera = new OrthographicCamera();
        fboCamera.setToOrtho(false, Constants.win_width, Constants.win_height);
        fboCamera.update();
        fbo = new FrameBuffer(Pixmap.Format.RGBA8888, (int) camera.viewportWidth, (int) camera.viewportHeight, false);
        fboRegion = new TextureRegion(fbo.getColorBufferTexture());
        fboRegion.flip(false, true);

        overworld_offset_x = camera.viewportWidth / 2f - Assets.overworld.getWidth() / 2f - 24f;
        yPos.setValue(library_position);
        Tween.to(yPos, -1, 10f)
             .target(tower_position)
             .delay(1)
             .ease(Sine.INOUT)
             .setCallback(new TweenCallback() {
                 @Override
                 public void onEvent(int type, BaseTween<?> source) {
                     Tween.to(yPos, -1, 7)
                          .target(library_position)
                          .delay(2)
                          .ease(Sine.INOUT)
                          .setCallback(new TweenCallback() {
                              @Override
                              public void onEvent(int type, BaseTween<?> source) {
                                  introTweenComplete = true;
                              }
                          })
                          .start(GameInstance.tweens);
                 }
             })
             .start(GameInstance.tweens);
    }

    @Override
    public void render(float delta) {
        GameInstance.tweens.update(delta);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) game.exit();

//        if      (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
//            nextLevel();
//            scrollCamera(yPos.floatValue() - speed, 1f);
//            placeCamera(yPos.floatValue() - 1f);
//        }
//        else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
//            prevLevel();
//            scrollCamera(yPos.floatValue() + speed, 1f);
//            placeCamera(yPos.floatValue() + 1f);
//        }

        fbo.begin();
        {
            Gdx.gl20.glClearColor(0, 0, 0, 1);
            Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

            Assets.batch.setProjectionMatrix(camera.combined);
            Assets.batch.setShader(null);
            Assets.batch.begin();
            Assets.batch.draw(Assets.overworld, overworld_offset_x, yPos.floatValue());
            Assets.batch.end();
        }
        fbo.end();

        Gdx.gl20.glClearColor(0, 0, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Assets.batch.setProjectionMatrix(fboCamera.combined);
        Assets.batch.setShader(null);
        Assets.batch.begin();
        Assets.batch.draw(fboRegion, 0, 0, fboCamera.viewportWidth, fboCamera.viewportHeight);
        Assets.batch.end();

    }

    public TextureRegion getColorBuffer() { return fboRegion; }

    public void placeCamera(float yPosition) {
        yPos.setValue(yPosition);
    }

    public void scrollCamera(float newYPosition, float duration) {
        scrollCamera(newYPosition, duration, null);
    }

    public void scrollCamera(float newYPosition, float duration, final Utils.Callback callback) {
        Tween.to(yPos, -1, duration)
             .target(newYPosition)
             .delay(0.5f)
             .ease(Sine.INOUT)
             .setCallback(new TweenCallback() {
                 @Override
                 public void onEvent(int type, BaseTween<?> source) {
                     if (callback != null) callback.run();
                 }
             })
             .start(GameInstance.tweens);
    }

    public void nextLevel() {
        int level = currentLevel + 1;
        if (level >= 7) level = 0;
        changeLevel(level);
    }

    public void prevLevel() {
        int level = currentLevel - 1;
        if (level < 0) level = 6;
        changeLevel(level);
    }

    public void changeLevel(int level) {
        changeLevel(level, null);
    }

    public void changeLevel(int level, Utils.Callback callback) {
        currentLevel = level;
        float target = library_position;
        switch (currentLevel) {
            case 0: target = library_position; break;
            case 1: target = forest_position; break;
            case 2: target = cave_position; break;
            case 3: target = desert_position; break;
            case 4: target = water_position; break;
            case 5: target = dungeon_position; break;
            case 6: target = tower_position; break;
        }
        if (callback == null) {
            scrollCamera(target, 3f);
        } else {
            scrollCamera(target, 3f, callback);
        }
    }

}

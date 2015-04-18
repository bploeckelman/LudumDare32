package lando.systems.ld32.screens;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.equations.Back;
import aurelienribon.tweenengine.equations.Quint;
import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld32.Assets;
import lando.systems.ld32.Constants;
import lando.systems.ld32.GameInstance;
import lando.systems.ld32.tweens.ColorAccessor;

public class TestScreen extends ScreenAdapter {

    GameInstance game;

    BitmapFont         font;
    Texture            img;
    Texture            ldLogo;
    Color              backgroundColor;
    FrameBuffer        sceneFBO;
    TextureRegion      sceneRegion;
    OrthographicCamera sceneCamera;

    Vector2      bouncerPos = new Vector2();
    Vector2      bouncerVel = new Vector2(MathUtils.random() * 2f - 1.0f, MathUtils.random() / 2f + 0.25f);
    Vector2      center     = new Vector2();
    MutableFloat floor_y    = new MutableFloat(109f);
    MutableFloat textScale  = new MutableFloat(2f);
    Tween        textPulse;

    public TestScreen(GameInstance game) {
        this.game = game;

        font = new BitmapFont();
        img = new Texture("badlogic.jpg");
        ldLogo = new Texture("ludum-dare-logo.png");
        backgroundColor = new Color(0, 0, 0, 1);
        sceneFBO = new FrameBuffer(Pixmap.Format.RGBA8888, Constants.win_width, Constants.win_height, false);
        sceneRegion = new TextureRegion(sceneFBO.getColorBufferTexture());
        sceneRegion.flip(false, true);
        sceneCamera = new OrthographicCamera();
        sceneCamera.setToOrtho(false, sceneFBO.getWidth(), sceneFBO.getHeight());
        sceneCamera.update();

        final float halfViewWidth = sceneCamera.viewportWidth / 2f;
        final float halfViewHeight = (sceneCamera.viewportHeight - floor_y.floatValue()) / 2f;
        final float halfImgWidth   = img.getWidth()  / 2f;
        final float halfImgHeight  = img.getHeight() / 2f;
        center.set(halfViewWidth, halfViewHeight + floor_y.floatValue());
        bouncerPos.set(halfViewWidth - halfImgWidth, halfViewHeight - halfImgHeight + floor_y.floatValue());

        changeBackgroundColor();
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

        final float bouncer_speed = 150.f;
        bouncerPos.add(bouncerVel.cpy().nor().scl(delta * bouncer_speed));
        if (bouncerPos.x < 0f) {
            bouncerPos.x = 0f;
            bouncerVel.x = -1f * Math.signum(bouncerVel.x) * (MathUtils.random() / 2f + 0.5f);
            changeBackgroundColor();
            pulseText(0.4f);
        }
        if (bouncerPos.x > sceneCamera.viewportWidth - img.getWidth()) {
            bouncerPos.x = sceneCamera.viewportWidth - img.getWidth();
            bouncerVel.x = -1f * Math.signum(bouncerVel.x) * (MathUtils.random() / 2f + 0.5f);
            changeBackgroundColor();
            pulseText(0.4f);
        }

        if (bouncerPos.y < floor_y.floatValue()) {
            bouncerPos.y = floor_y.floatValue();
            bouncerVel.y = -1f * Math.signum(bouncerVel.y) * (MathUtils.random() / 2f + 0.5f);
            changeBackgroundColor();
            bounceFooter();
            pulseText(1f);
        }
        if (bouncerPos.y > sceneCamera.viewportHeight - img.getHeight()) {
            bouncerPos.y = sceneCamera.viewportHeight - img.getHeight();
            bouncerVel.y = -1f * Math.signum(bouncerVel.y) * (MathUtils.random() / 2f + 0.5f);
            changeBackgroundColor();
            pulseText(0.4f);
        }
    }

    private void renderScene(SpriteBatch batch) {
        sceneFBO.begin();
        {
            Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, 1.0f);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            batch.setProjectionMatrix(sceneCamera.combined);
            batch.setShader(null);
            batch.begin();
            batch.draw(img, bouncerPos.x, bouncerPos.y);
            batch.draw(ldLogo, 0, floor_y.floatValue() - 109f);
            font.setColor(1f - backgroundColor.r, 1f - backgroundColor.g, 1f - backgroundColor.b, 1f);
            font.setScale(textScale.floatValue());
            final BitmapFont.TextBounds bounds = font.getBounds("32");
            font.draw(batch, "32", center.x - bounds.width / 2f, center.y + bounds.height / 2f);
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

    private void changeBackgroundColor() {
        Tween.to(backgroundColor, ColorAccessor.R, 0.5f)
             .target(MathUtils.random())
             .ease(Quint.INOUT)
             .start(GameInstance.tweens);
        Tween.to(backgroundColor, ColorAccessor.G, 0.5f)
             .target(MathUtils.random())
             .ease(Quint.INOUT)
             .start(GameInstance.tweens);
        Tween.to(backgroundColor, ColorAccessor.B, 0.5f)
             .target(MathUtils.random())
             .ease(Quint.INOUT)
             .start(GameInstance.tweens);
    }

    private void bounceFooter() {
        Tween.to(floor_y, -1, 0.33f)
             .target(95f)
             .ease(Back.OUT)
             .repeatYoyo(1, 0f)
             .start(GameInstance.tweens);
    }

    private void pulseText(float strength) {
        final float max_text_pulse_speed = 0.4f;
        final float max_text_scale = 10.f;
        final float initial_text_scale = 2.f;

        if (textPulse != null && !textPulse.isFinished()) {
            textScale.setValue(initial_text_scale);
        }

        textPulse = Tween.to(textScale, -1, max_text_pulse_speed * strength)
                         .target(max_text_scale * strength)
                         .ease(Back.INOUT)
                         .repeatYoyo(1, 0f)
                         .start(GameInstance.tweens);
    }

}

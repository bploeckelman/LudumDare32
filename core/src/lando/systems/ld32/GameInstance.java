package lando.systems.ld32;

import aurelienribon.tweenengine.*;
import aurelienribon.tweenengine.equations.*;
import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import lando.systems.ld32.tweens.ColorAccessor;
import lando.systems.ld32.tweens.Vector2Accessor;
import lando.systems.ld32.tweens.Vector3Accessor;

public class GameInstance extends ApplicationAdapter {

	public static final TweenManager tweens = new TweenManager();

	SpriteBatch        batch;
	Texture            img;
	Texture            ldLogo;
	Color              backgroundColor;
	FrameBuffer        sceneFBO;
	TextureRegion      sceneRegion;
	OrthographicCamera sceneCamera;

	Vector2      bouncerPos = new Vector2();
	Vector2      bouncerVel = new Vector2(MathUtils.random() * 2f - 1.0f, MathUtils.random() / 2f - 0.25f);
	MutableFloat floor_y    = new MutableFloat(109f);

	@Override
	public void create() {
		Tween.registerAccessor(Color.class, new ColorAccessor());
		Tween.registerAccessor(Vector2.class, new Vector2Accessor());
		Tween.registerAccessor(Vector3.class, new Vector3Accessor());

		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		ldLogo = new Texture("ludum-dare-logo.png");
		backgroundColor = new Color(0, 0, 0, 1);
		sceneFBO = new FrameBuffer(Pixmap.Format.RGBA8888, Constants.win_width, Constants.win_height, false);
		sceneRegion = new TextureRegion(sceneFBO.getColorBufferTexture());
		sceneRegion.flip(false, true);
		sceneCamera = new OrthographicCamera();
		sceneCamera.setToOrtho(false, sceneFBO.getWidth(), sceneFBO.getHeight());
		sceneCamera.update();

		final float halfViewWidth  =  sceneCamera.viewportWidth / 2f;
		final float halfViewHeight = (sceneCamera.viewportHeight - floor_y.floatValue()) / 2f;
		final float halfImgWidth   = img.getWidth()  / 2f;
		final float halfImgHeight  = img.getHeight() / 2f;
		bouncerPos.set(halfViewWidth - halfImgWidth, halfViewHeight - halfImgHeight + floor_y.floatValue());

		changeBackgroundColor();
	}

	@Override
	public void render() {
		update();
		renderScene();
		renderScreen();
	}

	public void update() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) Gdx.app.exit();

		final float delta = Gdx.graphics.getDeltaTime();
		tweens.update(delta);

		final float bouncer_speed = 100.f;
		bouncerPos.add(bouncerVel.cpy().nor().scl(delta * bouncer_speed));
		if (bouncerPos.x < 0f) {
			bouncerPos.x = 0f;
			bouncerVel.x = -1f * Math.signum(bouncerVel.x) * (MathUtils.random() / 2f + 0.5f);
			changeBackgroundColor();
		}
		if (bouncerPos.x > sceneCamera.viewportWidth - img.getWidth()) {
			bouncerPos.x = sceneCamera.viewportWidth - img.getWidth();
			bouncerVel.x = -1f * Math.signum(bouncerVel.x) * (MathUtils.random() / 2f + 0.5f);
			changeBackgroundColor();
		}

		if (bouncerPos.y < floor_y.floatValue()) {
			bouncerPos.y = floor_y.floatValue();
			bouncerVel.y = -1f * Math.signum(bouncerVel.y) * (MathUtils.random() / 2f + 0.5f);
			changeBackgroundColor();
			bounceFooter();
		}
		if (bouncerPos.y > sceneCamera.viewportHeight - img.getHeight()) {
			bouncerPos.y = sceneCamera.viewportHeight - img.getHeight();
			bouncerVel.y = -1f * Math.signum(bouncerVel.y) * (MathUtils.random() / 2f + 0.5f);
			changeBackgroundColor();
		}
	}

	public void renderScene() {
		sceneFBO.begin();
		{
			Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, 1.0f);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

			batch.setProjectionMatrix(sceneCamera.combined);
			batch.setShader(null);
			batch.begin();
			batch.draw(img, bouncerPos.x, bouncerPos.y);
			batch.draw(ldLogo, 0, floor_y.floatValue() - 109f);
			batch.end();
		}
		sceneFBO.end();
	}

	public void renderScreen() {
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
		     .start(tweens);
		Tween.to(backgroundColor, ColorAccessor.G, 0.5f)
		     .target(MathUtils.random())
		     .ease(Quint.INOUT)
		     .start(tweens);
		Tween.to(backgroundColor, ColorAccessor.B, 0.5f)
		     .target(MathUtils.random())
		     .ease(Quint.INOUT)
		     .start(tweens);
	}

	private void bounceFooter() {
		Tween.to(floor_y, -1, 0.33f)
		     .target(95f)
		     .ease(Back.OUT)
		     .repeatYoyo(1, 0f)
		     .start(tweens);
	}

}

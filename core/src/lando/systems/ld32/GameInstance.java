package lando.systems.ld32;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

public class GameInstance extends ApplicationAdapter {
	SpriteBatch        batch;
	Texture            img;
	FrameBuffer        sceneFBO;
	TextureRegion      sceneRegion;
	OrthographicCamera sceneCamera;

	@Override
	public void create() {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		sceneFBO = new FrameBuffer(Pixmap.Format.RGBA8888, Constants.win_width, Constants.win_height, false);
		sceneRegion = new TextureRegion(sceneFBO.getColorBufferTexture());
		sceneRegion.flip(false, true);
		sceneCamera = new OrthographicCamera();
		sceneCamera.setToOrtho(false, sceneFBO.getWidth(), sceneFBO.getHeight());
		sceneCamera.update();
	}

	@Override
	public void render() {
		renderScene();
		renderScreen();
	}

	public void renderScene() {
		sceneFBO.begin();
		{
			Gdx.gl.glClearColor(1, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

			batch.setProjectionMatrix(sceneCamera.combined);
			batch.setShader(null);
			batch.begin();
			batch.draw(img, 0, 0);
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

}

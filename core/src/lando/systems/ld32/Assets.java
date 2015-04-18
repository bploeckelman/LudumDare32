package lando.systems.ld32;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Assets {

    public static SpriteBatch batch;
    public static ShapeRenderer shapes;


    public static void load() {
        batch = new SpriteBatch();
        shapes = new ShapeRenderer();
    }

    public static void dispose() {
        batch.dispose();
        shapes.dispose();
    }

}

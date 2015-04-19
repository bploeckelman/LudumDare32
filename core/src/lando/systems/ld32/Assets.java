package lando.systems.ld32;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Assets {

    public static SpriteBatch batch;
    public static ShapeRenderer shapes;
    public static Texture background1;
    public static Texture killphraseBox;
    public static Texture speechBubbleTexture;
    public static Texture enemyTexture;
    public static Texture effectsTexture;
    public static TextureRegion[][] enemyRegions;
    public static TextureRegion[][] effectsRegions;
    public static NinePatch speechBubble;

    public static Animation defaultEnemyAnimation;
    public static Animation defaultEnemyAnimation2;

    public static void load() {
        batch = new SpriteBatch();
        shapes = new ShapeRenderer();

        background1 = new Texture("background1.png");
        killphraseBox = new Texture("killphrase-box.png");
        speechBubbleTexture = new Texture("speech-bubble.png");
        enemyTexture = new Texture("oryx_16bit_scifi_creatures_extra_trans.png");
        effectsTexture = new Texture("oryx_16bit_scifi_FX_lg_trans.png");
        enemyRegions = TextureRegion.split(enemyTexture, 24, 24);
        effectsRegions = TextureRegion.split(effectsTexture, 32, 32);
        speechBubble = new NinePatch(speechBubbleTexture, 5, 5, 5, 5);


        // TODO: Temporary animations
        defaultEnemyAnimation = new Animation(
            .3f,
            new TextureRegion(enemyRegions[0][0]),
            new TextureRegion(enemyRegions[1][0]));
        defaultEnemyAnimation.setPlayMode(Animation.PlayMode.LOOP);
        defaultEnemyAnimation2 = new Animation(
                .3f,
                new TextureRegion(enemyRegions[2][0]),
                new TextureRegion(enemyRegions[3][0]));
        defaultEnemyAnimation2.setPlayMode(Animation.PlayMode.LOOP);
    }

    public static void dispose() {
        batch.dispose();
        shapes.dispose();
        background1.dispose();
        killphraseBox.dispose();
        speechBubbleTexture.dispose();
        enemyTexture.dispose();
        effectsTexture.dispose();
    }

}

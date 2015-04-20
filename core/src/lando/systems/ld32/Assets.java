package lando.systems.ld32;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import lando.systems.ld32.effects.*;

public class Assets {

    public static SpriteBatch   batch;
    public static ShapeRenderer shapes;

    public static BitmapFont font8;
    public static BitmapFont font16;
    public static BitmapFont font32;

    public static ShaderProgram postShader;

    public static Texture[] timerTextures;
    public static Texture background1;
    public static Texture libraryBackground;
    public static Texture forestBackground;
    public static Texture caveBackground;
    public static Texture desertBackground;
    public static Texture waterBackground;
    public static Texture dungeonBackground;
    public static Texture towerBackground;
    public static Texture killphraseBox;
    public static Texture speechBubbleTexture;
    public static Texture enemyTexture;
    public static Texture effectsTexture;
    public static Texture stunStarsTexture;
    public static Texture noiseTexture;
    public static TextureRegion noiseRegionX;
    public static TextureRegion noiseRegionY;
    public static TextureRegion noiseRegionXY;
    public static TextureRegion forestBackgroundRegion;
    public static TextureRegion libraryBackgroundRegion;
    public static TextureRegion caveBackgroundRegion;
    public static TextureRegion desertBackgroundRegion;
    public static TextureRegion waterBackgroundRegion;
    public static TextureRegion dungeonBackgroundRegion;
    public static TextureRegion towerBackgroundRegion;
    public static TextureRegion[][] enemyRegions;
    public static TextureRegion[][] effectsRegions;
    public static TextureRegion[][] stunStarsRegions;
    public static NinePatch speechBubble;

    public static Animation timerAnimation;
    public static Animation puffAnimation;
    public static Animation stunStarsAnimation;
    public static Animation explodeAnimation;
    public static Animation xoutAnimation;
    public static Animation counterSpellAnimation;

    public static Animation defaultEnemyAnimation;
    public static Animation defaultEnemyAnimation2;
    public static Animation librarianAnimation;
    public static Animation ogreAnimation;
    public static Animation batAnimation;
    public static Animation scorpionAnimation;
    public static Animation waterCubeAnimation;
    public static Animation beholderAnimation;
    public static Animation chickenAnimation;

    public static void load() {
        batch = new SpriteBatch();
        shapes = new ShapeRenderer();

        font8 = new BitmapFont(Gdx.files.internal("fonts/monaco8.fnt"));
        font8.setMarkupEnabled(true);
        font16 = new BitmapFont(Gdx.files.internal("fonts/monaco16.fnt"));
        font16.setMarkupEnabled(true);
        font32 = new BitmapFont(Gdx.files.internal("fonts/monaco32.fnt"));
        font32.setMarkupEnabled(true);

        postShader = new ShaderProgram(
                Gdx.files.internal("shaders/default.vert"),
                Gdx.files.internal("shaders/post.frag"));


        timerTextures = new Texture[6];
        for (int i = 0; i < 6; ++i) {
            timerTextures[i] = new Texture("timer"+i+".png");
        }
        background1 = new Texture("background1.png");
        libraryBackground = new Texture("fight-screen-library.png");
        forestBackground = new Texture("fight-screen-forest.png");
        caveBackground = new Texture("fight-screen-cave.png");
        desertBackground = new Texture("fight-screen-desert.png");
        waterBackground = new Texture("fight-screen-water.png");
        dungeonBackground = new Texture("fight-screen-cellar.png");
        towerBackground = new Texture("fight-screen-book.png");
        killphraseBox = new Texture("killphrase-box.png");
        speechBubbleTexture = new Texture("speech-bubble.png");
        enemyTexture = new Texture("oryx_16bit_scifi_creatures_extra_trans.png");
        effectsTexture = new Texture("oryx_16bit_scifi_FX_lg_trans.png");
        stunStarsTexture = new Texture("stun-stars.png");
        noiseTexture = new Texture("sandstorm.png");
        noiseTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        noiseRegionX = new TextureRegion(noiseTexture);
        noiseRegionY = new TextureRegion(noiseTexture);
        noiseRegionXY = new TextureRegion(noiseTexture);
        libraryBackgroundRegion = new TextureRegion(libraryBackground);
        forestBackgroundRegion = new TextureRegion(forestBackground);
        caveBackgroundRegion = new TextureRegion(caveBackground);
        desertBackgroundRegion = new TextureRegion(desertBackground);
        waterBackgroundRegion = new TextureRegion(waterBackground);
        dungeonBackgroundRegion = new TextureRegion(dungeonBackground);
        towerBackgroundRegion = new TextureRegion(towerBackground);
        enemyRegions = TextureRegion.split(enemyTexture, 24, 24);
        effectsRegions = TextureRegion.split(effectsTexture, 32, 32);
        stunStarsRegions = TextureRegion.split(stunStarsTexture, 24, 16);
        speechBubble = new NinePatch(speechBubbleTexture, 5, 5, 5, 5);

        // Animations
        timerAnimation = new Animation(
            1.f,
            new TextureRegion(timerTextures[0]),
            new TextureRegion(timerTextures[1]),
            new TextureRegion(timerTextures[2]),
            new TextureRegion(timerTextures[3]),
            new TextureRegion(timerTextures[4]),
            new TextureRegion(timerTextures[5]));
        puffAnimation = new Animation(
            Puff.puff_time,
            effectsRegions[2][3],
            effectsRegions[2][4],
            effectsRegions[10][4],
            effectsRegions[10][5],
            effectsRegions[10][6],
            effectsRegions[10][7]);
        stunStarsAnimation = new Animation(
            StunStars.stunstars_time,
            stunStarsRegions[0][0],
            stunStarsRegions[0][1],
            stunStarsRegions[0][2]);
        stunStarsAnimation.setPlayMode(Animation.PlayMode.LOOP);
        explodeAnimation = new Animation(
            Explode.explode_time,
            effectsRegions[0][0],
            effectsRegions[0][1],
            effectsRegions[10][0],
            effectsRegions[10][1]);
        xoutAnimation = new Animation(
            Xout.xout_time,
            effectsRegions[1][2],
            effectsRegions[1][3]);
        counterSpellAnimation = new Animation(
            CounterSpell.counterspell_time,
            effectsRegions[0][3],
            effectsRegions[0][2],
            effectsRegions[0][5],
            effectsRegions[0][4],
            effectsRegions[9][3],
            effectsRegions[9][2],
            effectsRegions[1][0],
            effectsRegions[1][1],
            effectsRegions[1][6],
            effectsRegions[1][7],
            effectsRegions[2][5],
            effectsRegions[10][6],
            effectsRegions[10][7]
        );

        // TODO: Temporary animations
        defaultEnemyAnimation = new Animation(.3f,
            new TextureRegion(enemyRegions[0][0]),
            new TextureRegion(enemyRegions[1][0]));
        defaultEnemyAnimation.setPlayMode(Animation.PlayMode.LOOP);
        defaultEnemyAnimation2 = new Animation(.3f,
            new TextureRegion(enemyRegions[2][0]),
            new TextureRegion(enemyRegions[3][0]));
        defaultEnemyAnimation2.setPlayMode(Animation.PlayMode.LOOP);
        librarianAnimation = new Animation(0.25f,
            new TextureRegion(enemyRegions[2][6]),
            new TextureRegion(enemyRegions[3][6]));
        librarianAnimation.setPlayMode(Animation.PlayMode.LOOP);
        ogreAnimation = new Animation(0.25f,
            new TextureRegion(enemyRegions[0][1]),
            new TextureRegion(enemyRegions[1][1]));
        ogreAnimation.setPlayMode(Animation.PlayMode.LOOP);
        batAnimation = new Animation(0.25f,
            new TextureRegion(enemyRegions[6][2]),
            new TextureRegion(enemyRegions[7][2]));
        batAnimation.setPlayMode(Animation.PlayMode.LOOP);
        scorpionAnimation = new Animation(0.25f,
            new TextureRegion(enemyRegions[0][7]),
            new TextureRegion(enemyRegions[1][7]));
        scorpionAnimation.setPlayMode(Animation.PlayMode.LOOP);
        waterCubeAnimation = new Animation(0.25f,
            new TextureRegion(enemyRegions[2][11]),
            new TextureRegion(enemyRegions[3][11]));
        waterCubeAnimation.setPlayMode(Animation.PlayMode.LOOP);
        beholderAnimation = new Animation(0.25f,
            new TextureRegion(enemyRegions[6][4]),
            new TextureRegion(enemyRegions[7][4]));
        beholderAnimation.setPlayMode(Animation.PlayMode.LOOP);
        chickenAnimation = new Animation(0.25f,
            new TextureRegion(enemyRegions[4][14]),
            new TextureRegion(enemyRegions[5][14]));
        chickenAnimation.setPlayMode(Animation.PlayMode.LOOP);
    }

    public static void dispose() {
        batch.dispose();
        shapes.dispose();
        background1.dispose();
        killphraseBox.dispose();
        speechBubbleTexture.dispose();
        enemyTexture.dispose();
        effectsTexture.dispose();
        noiseTexture.dispose();
        postShader.dispose();
        font32.dispose();
        font16.dispose();
        font8.dispose();
        for (int i = 0; i < timerTextures.length; ++i) {
            timerTextures[i].dispose();
        }
    }

}

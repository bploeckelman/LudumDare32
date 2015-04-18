package lando.systems.ld32;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import lando.systems.ld32.screens.TestScreen;
import lando.systems.ld32.tweens.ColorAccessor;
import lando.systems.ld32.tweens.Vector2Accessor;
import lando.systems.ld32.tweens.Vector3Accessor;

import java.util.HashMap;
import java.util.Map;

public class GameInstance extends Game {

    public static final TweenManager tweens = new TweenManager();
    public static final Map<String, Screen> screens = new HashMap<String, Screen>();

    @Override
    public void create() {
        Assets.load();

        Tween.registerAccessor(Color.class, new ColorAccessor());
        Tween.registerAccessor(Vector2.class, new Vector2Accessor());
        Tween.registerAccessor(Vector3.class, new Vector3Accessor());

        screens.put(Constants.test_screen, new TestScreen(this));
        setScreen(screens.get(Constants.test_screen));
    }

    public void exit() {
        Assets.dispose();
        Gdx.app.exit();
    }

}

package lando.systems.ld32;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import lando.systems.ld32.screens.FightScreen;
import lando.systems.ld32.screens.GameOverScreen;
import lando.systems.ld32.screens.TestScreen;
import lando.systems.ld32.tweens.ColorAccessor;
import lando.systems.ld32.tweens.RectangleAccessor;
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

        Tween.setCombinedAttributesLimit(4);
        Tween.setWaypointsLimit(2);
        Tween.registerAccessor(Color.class, new ColorAccessor());
        Tween.registerAccessor(Vector2.class, new Vector2Accessor());
        Tween.registerAccessor(Vector3.class, new Vector3Accessor());
        Tween.registerAccessor(Rectangle.class, new RectangleAccessor());

        screens.put(Constants.fight_screen, new FightScreen(this));
        screens.put(Constants.game_over_screen, new GameOverScreen(this));
        setScreen(screens.get(Constants.fight_screen));
//        setScreen(screens.get(Constants.game_over_screen));
    }

    public void exit() {
        Gdx.app.exit();
    }

}

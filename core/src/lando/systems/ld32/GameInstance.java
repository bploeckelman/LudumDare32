package lando.systems.ld32;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import lando.systems.ld32.screens.FightScreen;
import lando.systems.ld32.screens.GameOverScreen;
import lando.systems.ld32.screens.GameScreen;
import lando.systems.ld32.story.StoryManager;
import lando.systems.ld32.tweens.ColorAccessor;
import lando.systems.ld32.tweens.RectangleAccessor;
import lando.systems.ld32.tweens.Vector2Accessor;
import lando.systems.ld32.tweens.Vector3Accessor;

import java.util.HashMap;
import java.util.Map;

public class GameInstance extends Game {

    public static final TweenManager tweens = new TweenManager();
    public static final Map<String, Screen> screens = new HashMap<String, Screen>();

    public static StoryManager storyManager = new StoryManager();

    @Override
    public void create() {

        Assets.load();

        storyManager.game = this;

        Tween.setCombinedAttributesLimit(4);
        Tween.setWaypointsLimit(2);
        Tween.registerAccessor(Color.class, new ColorAccessor());
        Tween.registerAccessor(Vector2.class, new Vector2Accessor());
        Tween.registerAccessor(Vector3.class, new Vector3Accessor());
        Tween.registerAccessor(Rectangle.class, new RectangleAccessor());

        screens.put(Constants.game_screen, new GameScreen(this));
        screens.put(Constants.fight_screen, new FightScreen(this));
        screens.put(Constants.game_over_screen, new GameOverScreen(this));
//        setScreen(screens.get(Constants.game_screen));
//        setScreen(screens.get(Constants.fight_screen));
//        setScreen(screens.get(Constants.game_over_screen));
    }

    @Override
    public void render() {
        Gdx.gl20.glClearColor(0, 0, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        GameInstance.tweens.update(Gdx.graphics.getDeltaTime());

        super.render();

        if (getScreen() == screens.get(Constants.game_over_screen)) {
            return;
        }

        Assets.batch.begin();
        storyManager.render(Assets.batch);
        Assets.batch.end();
    }

    public void exit() {
        Gdx.app.exit();
    }

    public void changeScreen(String screenName) {
        Screen screen = screens.get(screenName);
        if (screen != null) {
            setScreen(screen);
        }
    }

}

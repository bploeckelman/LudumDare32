package lando.systems.ld32.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import lando.systems.ld32.Constants;
import lando.systems.ld32.GameInstance;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width  = Constants.win_width;
		config.height = Constants.win_height;
		config.resizable = Constants.resizable;
		new LwjglApplication(new GameInstance(), config);
	}
}

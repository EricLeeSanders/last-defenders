package com.lastdefenders.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.lastdefenders.LDGame;


public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 800;//640x360 480x320
        config.height = 400;
		config.vSyncEnabled = false;
		config.foregroundFPS = 0;
		new LwjglApplication(new LDGame(), config);
	}
}

package com.eric.mtd.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.eric.mtd.MTDGame;


public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 640;//640x360 480x320
        config.height = 360;
		new LwjglApplication(new MTDGame(), config);
	}
}

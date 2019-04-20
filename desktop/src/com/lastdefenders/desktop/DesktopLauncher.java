package com.lastdefenders.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.lastdefenders.LDGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 640;//640x360 480x320
        config.height = 360;
		config.vSyncEnabled = false;
		config.foregroundFPS = 0;
		config.addIcon("icon/icon_128.png", FileType.Internal); // Mac
		config.addIcon("icon/icon_32.png", FileType.Internal);// Windows and Linux
		config.addIcon("icon/icon_16.png", FileType.Internal); // Windows
		new LwjglApplication(new LDGame(new GooglePlayServicesHelper(), new AdControllerImpl()), config);
	}
}

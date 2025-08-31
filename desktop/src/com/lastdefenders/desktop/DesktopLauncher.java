package com.lastdefenders.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.pay.PurchaseManager;
import com.lastdefenders.LDGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = LwjglApplicationConfiguration.getDesktopDisplayMode().width;
		config.height = LwjglApplicationConfiguration.getDesktopDisplayMode().height;
		config.fullscreen = true;
		config.vSyncEnabled = false;
		config.foregroundFPS = 0;
		config.addIcon("icon/icon_128.png", FileType.Internal); // Mac
		config.addIcon("icon/icon_32.png", FileType.Internal);// Windows and Linux
		config.addIcon("icon/icon_16.png", FileType.Internal); // Windows
		PurchaseManager purchaseManager = new DesktopPurchaseManager();
		new LwjglApplication(new LDGame(new GooglePlayServicesHelper(), new AdControllerImpl(),
			new EventLoggerImpl(), purchaseManager, new ErrorReporterImpl()), config);
	}
}

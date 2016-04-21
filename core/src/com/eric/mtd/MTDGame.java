package com.eric.mtd;

import com.badlogic.gdx.Game;
import com.eric.mtd.game.GameScreen;
import com.eric.mtd.levelselect.LevelSelectScreen;
import com.eric.mtd.menu.MenuScreen;
import com.eric.mtd.screen.state.ScreenStateManager;
import com.eric.mtd.screen.state.IScreenStateObserver;
import com.eric.mtd.screen.state.ScreenStateManager.ScreenState;
import com.eric.mtd.state.GameStateManager;
import com.eric.mtd.util.Resources;

public class MTDGame extends Game implements IScreenStateObserver {
	private ScreenStateManager screenStateManager;
	private GameStateManager gameStateManager;
	public static float gameSpeed = Resources.NORMAL_SPEED; 

	@Override
	public void create() {
		screenStateManager = new ScreenStateManager();
		screenStateManager.attach(this);
		gameStateManager = new GameStateManager();
		setScreen(new MenuScreen(screenStateManager, gameStateManager));

	}

	@Override
	public void dispose() {
		Resources.dispose();
		this.getScreen().dispose();
		super.dispose();
	}

	@Override
	public void changeScreenState(ScreenState state) {
		switch (state) {
		case LEVEL_SELECTION:
			this.getScreen().dispose(); // dispose current screen
			this.setScreen(new LevelSelectScreen(screenStateManager,gameStateManager));
			break;
		case MENU:
			this.getScreen().dispose(); // dispose current screen
			this.setScreen(new MenuScreen(screenStateManager,gameStateManager));
			break;
		case LEVEL_1_SELECTED:
			this.getScreen().dispose(); // dispose current screen
			this.setScreen(new GameScreen(1, gameStateManager, screenStateManager));
			break;
		case LEVEL_2_SELECTED:
			this.getScreen().dispose(); // dispose current screen
			this.setScreen(new GameScreen(2, gameStateManager, screenStateManager));
			break;
		default:
			break;
		}

	}
}

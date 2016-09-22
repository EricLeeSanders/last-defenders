package com.eric.mtd;

import com.badlogic.gdx.Game;
import com.eric.mtd.game.GameScreen;
import com.eric.mtd.levelselect.LevelSelectScreen;
import com.eric.mtd.menu.MenuScreen;
import com.eric.mtd.screen.state.ScreenStateManager;
import com.eric.mtd.screen.state.IScreenStateObserver;
import com.eric.mtd.screen.state.ScreenStateManager.ScreenState;
import com.eric.mtd.state.GameStateManager;
import com.eric.mtd.util.MTDAudio;
import com.eric.mtd.util.Resources;
import com.eric.mtd.util.UserPreferences;

public class MTDGame extends Game implements IScreenStateObserver {
	private ScreenStateManager screenStateManager;
	private GameStateManager gameStateManager;
	private Resources resources;
	private UserPreferences userPreferences;
	private MTDAudio audio;

	@Override
	public void create() {
		userPreferences = new UserPreferences();
		resources = new Resources(userPreferences);
		audio = new MTDAudio(userPreferences);
		screenStateManager = new ScreenStateManager();
		screenStateManager.attach(this);
		gameStateManager = new GameStateManager();
		setScreen(new MenuScreen(screenStateManager, gameStateManager, resources, audio));

	}

	@Override
	public void dispose() {
		resources.dispose();
		audio.dispose();
		this.getScreen().dispose();
		super.dispose();
	}

	@Override
	public void changeScreenState(ScreenState state) {
		switch (state) {
		case LEVEL_SELECTION:
			this.getScreen().dispose(); // dispose current screen
			this.setScreen(new LevelSelectScreen(screenStateManager,gameStateManager,resources, audio));
			break;
		case MENU:
			this.getScreen().dispose(); 
			this.setScreen(new MenuScreen(screenStateManager,gameStateManager, resources, audio));
			break;
		case LEVEL_1_SELECTED:
			this.getScreen().dispose(); 
			this.setScreen(new GameScreen(1, gameStateManager, screenStateManager, resources, audio));
			break;
		case LEVEL_2_SELECTED:
			this.getScreen().dispose(); 
			this.setScreen(new GameScreen(2, gameStateManager, screenStateManager, resources, audio));
			break;
		case LEVEL_3_SELECTED:
			this.getScreen().dispose(); // dispose current screen
			this.setScreen(new GameScreen(3, gameStateManager, screenStateManager, resources, audio));                                                
		default:
			break;
		}

	}
}

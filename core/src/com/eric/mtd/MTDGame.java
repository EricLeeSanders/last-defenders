package com.eric.mtd;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.eric.mtd.game.GameScreen;
import com.eric.mtd.game.model.actor.ActorGroups;
import com.eric.mtd.game.service.factory.ActorFactory;
import com.eric.mtd.levelselect.LevelSelectScreen;
import com.eric.mtd.load.GameLoadingScreen;
import com.eric.mtd.load.LevelLoadingScreen;
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
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		userPreferences = new UserPreferences();
		resources = new Resources(userPreferences);
		audio = new MTDAudio(userPreferences);
		gameStateManager = new GameStateManager();
		screenStateManager = new ScreenStateManager();
		GameLoadingScreen loadingScreen = new GameLoadingScreen(gameStateManager, screenStateManager, resources, audio);
		setScreen(loadingScreen);
		screenStateManager.attach(this);

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
		this.getScreen().dispose(); // dispose current screen
		switch (state) {
		case LEVEL_SELECTION:
			this.setScreen(new LevelSelectScreen(screenStateManager,gameStateManager,resources, audio));
			break;
		case MENU:
			this.setScreen(new MenuScreen(screenStateManager,gameStateManager, resources, audio));
			break;
		case PLAY_LEVEL_1:
			this.setScreen(new GameScreen(1, gameStateManager, screenStateManager, resources, audio));
			break;
		case PLAY_LEVEL_2:
			this.setScreen(new GameScreen(2, gameStateManager, screenStateManager, resources, audio));
			break;
		case PLAY_LEVEL_3:
			this.setScreen(new GameScreen(3, gameStateManager, screenStateManager, resources, audio)); 
			break;
		case PLAY_LEVEL_4:
			this.setScreen(new GameScreen(4, gameStateManager, screenStateManager, resources, audio));
			break;
		case PLAY_LEVEL_5:
			this.setScreen(new GameScreen(5, gameStateManager, screenStateManager, resources, audio));    
			break;
		case LOAD_LEVEL_1:
			this.setScreen(new LevelLoadingScreen(gameStateManager, screenStateManager, resources, ScreenState.PLAY_LEVEL_1, 1, audio));
			break;
		case LOAD_LEVEL_2:
			this.setScreen(new LevelLoadingScreen(gameStateManager, screenStateManager, resources, ScreenState.PLAY_LEVEL_2, 2, audio));
			break;
		case LOAD_LEVEL_3:
			this.setScreen(new LevelLoadingScreen(gameStateManager, screenStateManager, resources, ScreenState.PLAY_LEVEL_3, 3, audio)); 
			break;
		case LOAD_LEVEL_4:
			this.setScreen(new LevelLoadingScreen(gameStateManager, screenStateManager, resources, ScreenState.PLAY_LEVEL_4, 4, audio));
			break;
		case LOAD_LEVEL_5:
			this.setScreen(new LevelLoadingScreen(gameStateManager, screenStateManager, resources, ScreenState.PLAY_LEVEL_5, 5, audio));
			break;
		default:
			this.setScreen(new MenuScreen(screenStateManager,gameStateManager, resources, audio));
			break;
		}

	}
}

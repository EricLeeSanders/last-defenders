package com.foxholedefense;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.foxholedefense.game.GameScreen;
import com.foxholedefense.game.model.actor.ActorGroups;
import com.foxholedefense.game.service.factory.ActorFactory;
import com.foxholedefense.levelselect.LevelSelectScreen;
import com.foxholedefense.load.GameLoadingScreen;
import com.foxholedefense.load.LevelLoadingScreen;
import com.foxholedefense.menu.MenuScreen;
import com.foxholedefense.screen.state.IScreenStateObserver;
import com.foxholedefense.screen.state.ScreenStateManager;
import com.foxholedefense.screen.state.ScreenStateManager.ScreenState;
import com.foxholedefense.state.GameStateManager;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.Resources;
import com.foxholedefense.util.UserPreferences;

public class FHDGame extends Game implements IScreenStateObserver {
	private ScreenStateManager screenStateManager;
	private GameStateManager gameStateManager;
	private Resources resources;
	private UserPreferences userPreferences;
	private FHDAudio audio;

	@Override
	public void create() {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		userPreferences = new UserPreferences();
		resources = new Resources(userPreferences);
		audio = new FHDAudio(userPreferences);
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
			this.setScreen(new LevelLoadingScreen(gameStateManager, screenStateManager, resources, ScreenState.PLAY_LEVEL_1, 1));
			break;
		case LOAD_LEVEL_2:
			this.setScreen(new LevelLoadingScreen(gameStateManager, screenStateManager, resources, ScreenState.PLAY_LEVEL_2, 2));
			break;
		case LOAD_LEVEL_3:
			this.setScreen(new LevelLoadingScreen(gameStateManager, screenStateManager, resources, ScreenState.PLAY_LEVEL_3, 3)); 
			break;
		case LOAD_LEVEL_4:
			this.setScreen(new LevelLoadingScreen(gameStateManager, screenStateManager, resources, ScreenState.PLAY_LEVEL_4, 4));
			break;
		case LOAD_LEVEL_5:
			this.setScreen(new LevelLoadingScreen(gameStateManager, screenStateManager, resources, ScreenState.PLAY_LEVEL_5, 5));
			break;
		default:
			this.setScreen(new MenuScreen(screenStateManager,gameStateManager, resources, audio));
			break;
		}

	}
}

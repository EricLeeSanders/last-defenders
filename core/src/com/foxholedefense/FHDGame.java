package com.foxholedefense;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.foxholedefense.game.GameScreen;
import com.foxholedefense.levelselect.LevelSelectScreen;
import com.foxholedefense.load.GameLoadingScreen;
import com.foxholedefense.load.LevelLoadingScreen;
import com.foxholedefense.menu.MenuScreen;
import com.foxholedefense.screen.state.ScreenStateManager.ScreenState;
import com.foxholedefense.state.GameStateManager;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.Resources;
import com.foxholedefense.util.UserPreferences;

	private GameStateManager gameStateManager;
	private Resources resources;
	private UserPreferences userPreferences;
	private FHDAudio audio;
	private IPlayServices playServices;
	public FHDGame(IPlayServices playServices){
		this.playServices = playServices;
		playServices.signIn();
	}
	public FHDGame(){

	}

	@Override
	public void create() {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		userPreferences = new UserPreferences();
		resources = new Resources(userPreferences);
		audio = new FHDAudio(userPreferences);
		gameStateManager = new GameStateManager();
		setScreen(loadingScreen);

	}

	@Override
	public void dispose() {
		resources.dispose();
		audio.dispose();
		this.getScreen().dispose();
		super.dispose();
	}

	@Override
		this.getScreen().dispose(); // dispose current screen

	}
}

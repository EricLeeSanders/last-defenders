package com.foxholedefense;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.foxholedefense.game.GameScreen;
import com.foxholedefense.levelselect.LevelSelectScreen;
import com.foxholedefense.load.GameLoadingScreen;
import com.foxholedefense.load.LevelLoadingScreen;
import com.foxholedefense.menu.MenuScreen;
import com.foxholedefense.screen.ScreenChanger;
import com.foxholedefense.state.GameStateManager;
import com.foxholedefense.state.GameStateManager.GameState;
import com.foxholedefense.state.GameStateObserver;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.Resources;
import com.foxholedefense.util.UserPreferences;

public class FHDGame extends Game implements ScreenChanger, GameStateObserver {
	private GameStateManager gameStateManager;
	private Resources resources;
	private FHDAudio audio;
	private IPlayServices playServices;

	// Needed for launcher without play services
	public FHDGame(){

	}

	public FHDGame(IPlayServices playServices){
		this.playServices = playServices;
		//playServices.signIn();
	}

	@Override
	public void create() {
		Logger.info("FHDGame: Creating");
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		UserPreferences userPreferences = new UserPreferences();
		resources = new Resources(userPreferences);
		audio = new FHDAudio(userPreferences);
		gameStateManager = new GameStateManager();
		GameLoadingScreen loadingScreen = new GameLoadingScreen(gameStateManager, this, resources, audio);
		setScreen(loadingScreen);
		gameStateManager.attach(this);
		Gdx.input.setCatchBackKey(true);
	}

	@Override
	public void resume (){
		Logger.info("FHDGame: resuming");
		resources.activityResume();
		super.resume();
	}

	@Override
	public void dispose() {
		Logger.info("FHDGame: disposing");
		resources.dispose();
		audio.dispose();
		this.getScreen().dispose();
		super.dispose();
	}


	@Override
	public void changeToMenu(){
		Logger.info("FHDGame: Changing to menu");
		this.getScreen().dispose(); // dispose current screen
		this.setScreen(new MenuScreen(this,gameStateManager, resources, audio));
	}

	@Override
	public void changeToLevelSelect() {
		Logger.info("FHDGame: Changing to level select");
		this.getScreen().dispose(); // dispose current screen
		this.setScreen(new LevelSelectScreen(this,gameStateManager,resources, audio));
	}

	@Override
	public void changeToLevelLoad(int level) {
		Logger.info("FHDGame: Changing to level load");
		this.getScreen().dispose(); // dispose current screen
		this.setScreen(new LevelLoadingScreen(gameStateManager, this, resources, level));
	}

	@Override
	public void changeToLevel(int level) {
		Logger.info("FHDGame: Changing to level: " + level);
		this.getScreen().dispose(); // dispose current screen
		this.setScreen(new GameScreen(level, gameStateManager, this, resources, audio));
	}

	@Override
	public void stateChange(GameState state) {
		switch(state){
			case QUIT:
				Gdx.app.exit();
				break;
		}
	}
}

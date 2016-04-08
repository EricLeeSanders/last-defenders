package com.eric.mtd.game.ui.presenter;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.eric.mtd.MTDGame;
import com.eric.mtd.game.model.IPlayerObserver;
import com.eric.mtd.game.model.Player;
import com.eric.mtd.game.model.level.state.ILevelStateObserver;
import com.eric.mtd.game.model.level.state.LevelStateManager;
import com.eric.mtd.game.model.level.state.LevelStateManager.LevelState;
import com.eric.mtd.game.ui.state.GameUIStateManager;
import com.eric.mtd.game.ui.state.GameUIStateManager.GameUIState;
import com.eric.mtd.game.ui.state.IGameUIStateObserver;
import com.eric.mtd.game.ui.view.interfaces.IHUDView;
import com.eric.mtd.state.GameStateManager;
import com.eric.mtd.state.GameStateManager.GameState;
import com.eric.mtd.util.Resources;

public class HUDPresenter implements IGameUIStateObserver, ILevelStateObserver, IPlayerObserver{
	private LevelStateManager levelStateManager;
	private GameUIStateManager uiStateManager;
	private GameStateManager gameStateManager;
	private Player player;
	private boolean normalSpeed = true;
	private IHUDView view;
	public HUDPresenter(GameUIStateManager uiStateManager, LevelStateManager levelStateManager, GameStateManager gameStateManager, Player player){
		this.levelStateManager = levelStateManager;
		levelStateManager.attach(this);
		this.uiStateManager = uiStateManager;
		uiStateManager.attach(this);
		this.gameStateManager = gameStateManager;
		this.player = player;
		player.attachObserver(this);
	}
	public void setView(IHUDView view){
		this.view = view;
		changeUIState(uiStateManager.getState());
		playerAttributeChange();
	}
	public void options() {
		uiStateManager.setState(GameUIState.OPTIONS);
		gameStateManager.setState(GameState.PAUSE);
		
	}

	public void quit() {
		//gameStateManager.setState(LevelState.QUIT);
		
	}

	public void startWave() {
		levelStateManager.setState(LevelState.SPAWNING_ENEMIES);
		
	}

	public void enlist() {
		uiStateManager.setState(GameUIState.ENLISTING);
		
	}

	public void perks() {
		//stateManager.setState(State.PERKS);
		
	}

	public void changeGameSpeed() {
		if(normalSpeed){
			MTDGame.gameSpeed = (Resources.DOUBLE_SPEED);
			normalSpeed = false;
		}
		else{
			MTDGame.gameSpeed = (Resources.NORMAL_SPEED);
			normalSpeed = true;
		}
		view.changeSpeed(normalSpeed);
		
	}
	@Override
	public void changeUIState(GameUIState state) {
		switch(state){
		case GAME_OVER:
			view.gameOverState();
			break;
		case OPTIONS:
			view.optionsState();
			break;
		case ENLISTING:
			view.enlistingState();
			break;
		case STANDBY:
			view.standByState();
			//Sync with Level State
			changeLevelState(levelStateManager.getState());
			break;
		default:
			break;
		}
	}
	@Override
	public void playerAttributeChange() {
		view.setLives(String.valueOf(player.getLives()));
		view.setMoney(String.valueOf(player.getMoney()));
		view.setWaveCount(String.valueOf(player.getWaveCount()));
	}
	@Override
	public void changeLevelState(LevelState state) {
		switch(state){
		case SPAWNING_ENEMIES:
		case WAVE_IN_PROGRESS:
			view.waveInProgress();
			break;
		case STANDBY:
			view.standByState();
			break;
		default:
			break;
		}
		
	}




	

}

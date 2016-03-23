package com.eric.mtd.game.ui.controller;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.eric.mtd.MTDGame;
import com.eric.mtd.game.model.Player;
import com.eric.mtd.game.model.level.state.LevelStateManager;
import com.eric.mtd.game.model.level.state.LevelStateManager.LevelState;
import com.eric.mtd.game.ui.controller.interfaces.IHUDController;
import com.eric.mtd.game.ui.state.GameUIStateManager;
import com.eric.mtd.game.ui.state.GameUIStateManager.GameUIState;
import com.eric.mtd.state.GameStateManager;
import com.eric.mtd.state.GameStateManager.GameState;

public class HUDController implements IHUDController{
	private LevelStateManager levelStateManager;
	private GameUIStateManager uiStateManager;
	private GameStateManager gameStateManager;
	private Player player;
	public HUDController(GameUIStateManager uiStateManager, LevelStateManager levelStateManager, GameStateManager gameStateManager, Player player){
		this.levelStateManager = levelStateManager;
		this.uiStateManager = uiStateManager;
		this.gameStateManager = gameStateManager;
		this.player = player;
	}

	@Override
	public void options() {
		uiStateManager.setState(GameUIState.OPTIONS);
		gameStateManager.setState(GameState.PAUSE);
		
	}

	@Override
	public void quit() {
		//gameStateManager.setState(LevelState.QUIT);
		
	}

	@Override
	public void wave() {
		levelStateManager.setState(LevelState.SPAWNING_ENEMIES);
		
	}

	@Override
	public void enlist() {
		uiStateManager.setState(GameUIState.ENLIST);
		
	}
	
	@Override
	public void perks() {
		//stateManager.setState(State.PERKS);
		
	}
	@Override
	public int getMoney() {
		return player.getMoney();
	}

	@Override
	public int getLives() {
		return player.getLives();
	}
	@Override
	public int getWaveCount() {
		return player.getWaveCount();
	}

	@Override
	public void setGameSpeed(float speed) {
		MTDGame.gameSpeed = (speed);
		
	}




	

}

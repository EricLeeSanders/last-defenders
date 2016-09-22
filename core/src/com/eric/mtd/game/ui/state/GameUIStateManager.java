package com.eric.mtd.game.ui.state;

import java.util.ArrayList;
import java.util.List;

import com.eric.mtd.game.model.level.state.ILevelStateObserver;
import com.eric.mtd.game.model.level.state.LevelStateManager;
import com.eric.mtd.game.model.level.state.LevelStateManager.LevelState;
import com.eric.mtd.util.Logger;

public class GameUIStateManager implements ILevelStateObserver {
	private LevelStateManager levelStateManager;
	private GameUIState state;
	private List<IGameUIStateObserver> observers = new ArrayList<IGameUIStateObserver>();

	public GameUIStateManager(LevelStateManager levelStateManager) {
		this.levelStateManager = levelStateManager;
		levelStateManager.attach(this);
		this.setState(GameUIState.STANDBY);
	}

	public void attach(IGameUIStateObserver observer) {
		observers.add(observer);
	}

	public void notifyObservers() {
		Logger.info("Notify Observers");
		for (IGameUIStateObserver observer : observers) {
			Logger.info("Notifying: " + observer.getClass().getName());
			observer.changeUIState(state);
		}
	}

	// Determine the state to return to
	public void setStateReturn() {
		syncWithLevelState();
	}

	private void syncWithLevelState() {
		switch (levelStateManager.getState()) {
		case WAVE_IN_PROGRESS:
		case SPAWNING_ENEMIES:
			this.setState(GameUIState.WAVE_IN_PROGRESS);
			break;
		case STANDBY:
		default:
			this.setState(GameUIState.STANDBY);
			break;
		}
	}

	public void setState(GameUIState state) {
		Logger.info("Chaning UI state: " + this.getState() + " to state: " + state);
		this.state = state;
		notifyObservers();
	}

	public GameUIState getState() {
		return state;
	}

	public enum GameUIState {
		ENLISTING, SUPPORT, INSPECTING, HIGH_SCORES, OPTIONS, STANDBY, GAME_OVER
		, PLACING_TOWER, PLACING_SUPPORT, PLACING_AIRSTRIKE, WAVE_IN_PROGRESS, LEVEL_COMPLETED;

	}

	@Override
	public void changeLevelState(LevelState state) {
		switch (state) {
		case STANDBY:
			if (this.getState() == GameUIState.WAVE_IN_PROGRESS) {
				this.setState(GameUIState.STANDBY);
			}
		default:
		}
	}
}

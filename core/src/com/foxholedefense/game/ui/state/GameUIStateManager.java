package com.foxholedefense.game.ui.state;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.utils.SnapshotArray;
import com.foxholedefense.game.model.level.state.ILevelStateObserver;
import com.foxholedefense.game.model.level.state.LevelStateManager;
import com.foxholedefense.game.model.level.state.LevelStateManager.LevelState;
import com.foxholedefense.util.Logger;

public class GameUIStateManager implements ILevelStateObserver {
	private LevelStateManager levelStateManager;
	private GameUIState state;
	private SnapshotArray<IGameUIStateObserver> observers = new SnapshotArray<IGameUIStateObserver>();

	public GameUIStateManager(LevelStateManager levelStateManager) {
		this.levelStateManager = levelStateManager;
		levelStateManager.attach(this);
		this.setState(GameUIState.STANDBY);
	}

	public void attach(IGameUIStateObserver observer) {
		observers.add(observer);
	}

	public void notifyObservers() {
		Logger.info("GameUIState Actor: Notify Observers");
		Object[] objects = observers.begin();
		for(int i = observers.size - 1; i >= 0; i--){
			IGameUIStateObserver observer = (IGameUIStateObserver) objects[i];
			Logger.info("GameUIState Notifying: " + observer.getClass().getName());
			observer.changeUIState(state);
		}
		observers.end();
	}

	// Determine the state to return to
	public void setStateReturn() {
		syncWithLevelState();
	}

	private void syncWithLevelState() {
		Logger.info("Game UI State: syncWithLevelState");
		switch (levelStateManager.getState()) {
		case WAVE_IN_PROGRESS:
			this.setState(GameUIState.WAVE_IN_PROGRESS);
			break;
		case STANDBY:
		default:
			this.setState(GameUIState.STANDBY);
			break;
		}
	}

	public void setState(GameUIState state) {
		Logger.info("Changing UI state: " + this.getState() + " to state: " + state);
		this.state = state;
		notifyObservers();
	}

	public GameUIState getState() {
		return state;
	}

	public enum GameUIState {
		ENLISTING, SUPPORT, INSPECTING, HIGH_SCORES, OPTIONS, STANDBY, GAME_OVER, DEBUG
		, PLACING_TOWER, PLACING_SUPPORT, PLACING_AIRSTRIKE, PLACING_SUPPLYDROP, WAVE_IN_PROGRESS, LEVEL_COMPLETED;

	}

	@Override
	public void changeLevelState(LevelState state) {
		Logger.info("Game UI State: changeLevelState: " + state.name());
		switch (state) {
		case STANDBY:
			if (this.getState() == GameUIState.WAVE_IN_PROGRESS) {
				this.setState(GameUIState.STANDBY);
			}
		default:
		}
	}
}

package com.eric.mtd.game.model.level.state;

import java.util.ArrayList;
import java.util.List;

import com.eric.mtd.util.Logger;

/**
 * State Manager class that contains various states that the Level can be in
 * 
 * @author Eric
 *
 */
public class LevelStateManager {

	private LevelState state;
	private List<ILevelStateObserver> observers = new ArrayList<ILevelStateObserver>();

	public LevelStateManager() {
		this.setState(LevelState.STANDBY);
	}

	/**
	 * Attach an observer and add it to observers list.
	 * 
	 * @param observer
	 */
	public void attach(ILevelStateObserver observer) {
		observers.add(observer);
	}

	/**
	 * Notify all observers of state change
	 */
	public void notifyObservers() {
		Logger.info("Level state: Notify Observers");
		for (ILevelStateObserver observer : observers) {
			Logger.info("Level state: Notifying: " + observer.getClass().getName());
			observer.changeLevelState(state);
		}
	}

	/**
	 * Set the state of the Level
	 * 
	 * @param state
	 */
	public void setState(LevelState state) {
		Logger.info("Changing Level state: " + this.getState() + " to state: " + state);
		this.state = state;
		notifyObservers();
	}

	public LevelState getState() {
		return state;
	}

	public enum LevelState {
		WAVE_IN_PROGRESS, STANDBY, GAME_OVER;
	}
}

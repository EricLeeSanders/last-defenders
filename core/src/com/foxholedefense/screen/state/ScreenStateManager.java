package com.foxholedefense.screen.state;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.utils.SnapshotArray;
import com.foxholedefense.util.Logger;

/**
 * Screen State Manager class that manages the state of the screen and which
 * screen should be active
 * 
 * @author Eric
 *
 */
public class ScreenStateManager {

	private ScreenState state;
	private SnapshotArray<IScreenStateObserver> observers = new SnapshotArray<IScreenStateObserver>();

	public ScreenStateManager() {
		// this.setState(ScreenState.LOADING);
		//this.setState(ScreenState.MENU);
	}

	/**
	 * Attach an observer and add it to observers list.
	 * 
	 * @param observer
	 */
	public void attach(IScreenStateObserver observer) {
		observers.add(observer);
	}

	/**
	 * Notify all observers of state change
	 */
	public void notifyObservers() {
		Logger.info("Notify Observers");
		for (IScreenStateObserver observer : observers) {
			Logger.info("Notifying: " + observer.getClass().getName());
			observer.changeScreenState(state);
		}
	}

	/**
	 * Set the state of the Screen
	 * 
	 * @param state
	 */
	public void setState(ScreenState state) {
		Logger.info("Changing Screen state: " + this.getState() + " to state: " + state);
		this.state = state;
		notifyObservers();
	}

	public ScreenState getState() {
		return state;
	}

	public enum ScreenState {
		PLAY_LEVEL_1, PLAY_LEVEL_2, PLAY_LEVEL_3, PLAY_LEVEL_4, PLAY_LEVEL_5
		, LOAD_LEVEL_1, LOAD_LEVEL_2, LOAD_LEVEL_3, LOAD_LEVEL_4, LOAD_LEVEL_5
		, SETTINGS, QUIT, HIGH_SCORES, LOADING, MENU, LEVEL_SELECTION;

	}
}

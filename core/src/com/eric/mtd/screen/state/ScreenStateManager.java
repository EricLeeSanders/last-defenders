package com.eric.mtd.screen.state;

import java.util.ArrayList;
import java.util.List;
import com.eric.mtd.util.Logger;

/**
 * Screen State Manager class that manages the state of the screen and which
 * screen should be active
 * 
 * @author Eric
 *
 */
public class ScreenStateManager {

	private ScreenState state;
	private List<IScreenStateObserver> observers = new ArrayList<IScreenStateObserver>();

	public ScreenStateManager() {
		// this.setState(ScreenState.LOADING);
		this.setState(ScreenState.MENU);
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
		Logger.info("Chaning Screen state: " + this.getState() + " to state: " + state);
		this.state = state;
		notifyObservers();
	}

	public ScreenState getState() {
		return state;
	}

	public enum ScreenState {
		LEVEL_1_SELECTED, LEVEL_2_SELECTED, LEVEL_3_SELECTED, SETTINGS, QUIT, HIGH_SCORES, LOADING, MENU, LEVEL_SELECTION;

	}
}

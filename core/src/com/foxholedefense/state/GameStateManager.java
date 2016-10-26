package com.foxholedefense.state;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.utils.SnapshotArray;
import com.foxholedefense.util.Logger;

/**
 * Game State Manager class that manages the state of the game
 * 
 * @author Eric
 *
 */
public class GameStateManager {

	private GameState state;
	private SnapshotArray<IGameStateObserver> observers = new SnapshotArray<IGameStateObserver>();

	public GameStateManager() {
		this.setState(GameState.PLAY);
	}

	/**
	 * Attach an observer and add it to observers list.
	 * 
	 * @param observer
	 */
	public void attach(IGameStateObserver observer) {
		observers.add(observer);
	}

	/**
	 * Notify all observers of state change
	 */
	public void notifyObservers() {
		Logger.info("Game State: Notify Observers");
		for (IGameStateObserver observer : observers) {
			Logger.info("Game State Notifying: " + observer.getClass().getName());
			observer.changeGameState(state);
		}
	}

	/**
	 * Set the state of the game
	 * 
	 * @param state
	 */
	public void setState(GameState state) {
		Logger.info("Changing Game state: " + this.getState() + " to state: " + state);
		this.state = state;
		notifyObservers();
	}

	public GameState getState() {
		return state;
	}

	public enum GameState {
		PLAY, PAUSE, QUIT;
	}
}

package com.eric.mtd.state;

import java.util.ArrayList;
import java.util.List;
import com.eric.mtd.util.Logger;

/**
 * Game State Manager class that manages the state of the game
 * 
 * @author Eric
 *
 */
public class GameStateManager {

	private GameState state;
	private List<IGameStateObserver> observers = new ArrayList<IGameStateObserver>();

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
		if (Logger.DEBUG)
			System.out.println("Notify Observers");
		for (IGameStateObserver observer : observers) {
			if (Logger.DEBUG)
				System.out.println("Notifying: " + observer.getClass().getName());
			observer.changeGameState(state);
		}
	}

	/**
	 * Set the state of the game
	 * 
	 * @param state
	 */
	public void setState(GameState state) {
		if (Logger.DEBUG)
			System.out.println("Chaning Game state: " + this.getState() + " to state: " + state);
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

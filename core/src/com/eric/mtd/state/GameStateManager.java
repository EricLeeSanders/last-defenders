package com.eric.mtd.state;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameStateManager {
	
	private GameState state;
	private CopyOnWriteArrayList<IGameStateObserver> observers = new CopyOnWriteArrayList<IGameStateObserver>(); //QUESTION: Do I need to use some other sort of list? Like a libgdx list?
	public GameStateManager(){
		this.setState(GameState.PLAY);
	}
	public void attach(IGameStateObserver observer){
		observers.add(observer);
	}
	public void notifyObservers(){
		//if(Logger.DEBUG)System.out.println("Notify Observers");
		for(IGameStateObserver observer : observers){
			//if(Logger.DEBUG)System.out.println("Notifying: " + observer.getClass().getName());
			observer.changeGameState(state);
		}
	}
	public void setState(GameState state){
		//if(Logger.DEBUG)System.out.println("Chaning Game state: " + this.getState() + " to state: " + state);
		this.state = state;
		notifyObservers();
	}
	public GameState getState(){
		return state;
	}
	
	public enum GameState {
	    PLAY,
	    PAUSE,
	    QUIT;
	}
}

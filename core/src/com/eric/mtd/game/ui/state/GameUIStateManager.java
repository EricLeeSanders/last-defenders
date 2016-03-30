package com.eric.mtd.game.ui.state;

import java.util.concurrent.CopyOnWriteArrayList;

import com.eric.mtd.game.ui.state.GameUIStateManager.GameUIState;

public class GameUIStateManager {
	
	private GameUIState state;
	private CopyOnWriteArrayList<IGameUIStateObserver> observers = new CopyOnWriteArrayList<IGameUIStateObserver>(); //QUESTION: Do I need to use some other sort of list? Like a libgdx list?
	public GameUIStateManager(){
		this.setState(GameUIState.STANDBY);
	}
	public void attach(IGameUIStateObserver observer){
		observers.add(observer);
	}
	public void notifyObservers(){
		//if(Logger.DEBUG)System.out.println("Notify Observers");
		for(IGameUIStateObserver observer : observers){
			//if(Logger.DEBUG)System.out.println("Notifying: " + observer.getClass().getName());
			observer.changeUIState(state);
		}
	}
	public void setState(GameUIState state){
		//if(Logger.DEBUG)System.out.println("Chaning UI state: " + this.getState() + " to state: " + state);
		this.state = state;
		notifyObservers();
	}
	public GameUIState getState(){
		return state;
	}
	
	public enum GameUIState {
	    ENLIST,
	    PERKS,
	    INSPECTING,
	    HIGH_SCORES,
	    OPTIONS,
	    STANDBY,
	    LEVEL_OVER,
	    PLACING_TOWER,
	    PLACING_SANDBAG;
	    
	}
}

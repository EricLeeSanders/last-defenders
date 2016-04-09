package com.eric.mtd.game.model.level.state;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.eric.mtd.util.Logger;

public class LevelStateManager {
	
	private LevelState state;
	private CopyOnWriteArrayList<ILevelStateObserver> observers = new CopyOnWriteArrayList<ILevelStateObserver>(); //QUESTION: Do I need to use some other sort of list? Like a libgdx list?
	public LevelStateManager(){
		this.setState(LevelState.STANDBY);
	}
	public void attach(ILevelStateObserver observer){
		observers.add(observer);
	}
	public void notifyObservers(){
		if(Logger.DEBUG)System.out.println("Notify Observers");
		for(ILevelStateObserver observer : observers){
			if(Logger.DEBUG)System.out.println("Notifying: " + observer.getClass().getName());
			observer.changeLevelState(state);
		}
	}
	public void setState(LevelState state){
		if(Logger.DEBUG)System.out.println("Chaning Level state: " + this.getState() + " to state: " + state);
		this.state = state;
		notifyObservers();
	}
	public LevelState getState(){
		return state;
	}
	
	public enum LevelState {
	    WAVE_IN_PROGRESS,
	    STANDBY,
	    GAME_OVER,
	    SPAWNING_ENEMIES;
	}
}

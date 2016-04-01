package com.eric.mtd.screen.state;

import java.util.concurrent.CopyOnWriteArrayList;

import com.eric.mtd.util.Logger;


public class ScreenStateManager {
	
	private ScreenState state;
	private CopyOnWriteArrayList<IScreenStateObserver> observers = new CopyOnWriteArrayList<IScreenStateObserver>(); //QUESTION: Do I need to use some other sort of list? Like a libgdx list?
	public ScreenStateManager(){
		//this.setState(ScreenState.LOADING);
		this.setState(ScreenState.MENU);
	}
	public void attach(IScreenStateObserver observer){
		observers.add(observer);
	}
	public void notifyObservers(){
		if(Logger.DEBUG)System.out.println("Notify Observers");
		for(IScreenStateObserver observer : observers){
			if(Logger.DEBUG)System.out.println("Notifying: " + observer.getClass().getName());
			observer.changeScreenState(state);
		}
	}
	public void setState(ScreenState state){
		if(Logger.DEBUG)System.out.println("Chaning Screen state: " + this.getState() + " to state: " + state);
		this.state = state;
		notifyObservers();
	}
	public ScreenState getState(){
		return state;
	}
	
	public enum ScreenState {
		LEVEL_1_SELECTED,
		LEVEL_2_SELECTED,
	    SETTINGS,
	    QUIT,
	    HIGH_SCORES,
	    LOADING,
	    MENU,
	    LEVEL_SELECTION;

	}
}

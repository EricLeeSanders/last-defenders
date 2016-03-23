package com.eric.mtd.game.ui.state;

import java.util.concurrent.CopyOnWriteArrayList;

import com.eric.mtd.game.ui.state.UIStateManager.UIState;

public class UIStateManager {
	
	private UIState state;
	private CopyOnWriteArrayList<IUIStateObserver> observers = new CopyOnWriteArrayList<IUIStateObserver>(); //QUESTION: Do I need to use some other sort of list? Like a libgdx list?
	public UIStateManager(){
		this.setState(UIState.STANDBY);
	}
	public void attach(IUIStateObserver observer){
		observers.add(observer);
	}
	public void notifyObservers(){
		//if(Logger.DEBUG)System.out.println("Notify Observers");
		for(IUIStateObserver observer : observers){
			//if(Logger.DEBUG)System.out.println("Notifying: " + observer.getClass().getName());
			observer.changeUIState(state);
		}
	}
	public void setState(UIState state){
		//if(Logger.DEBUG)System.out.println("Chaning UI state: " + this.getState() + " to state: " + state);
		this.state = state;
		notifyObservers();
	}
	public UIState getState(){
		return state;
	}
	
	public enum UIState {
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

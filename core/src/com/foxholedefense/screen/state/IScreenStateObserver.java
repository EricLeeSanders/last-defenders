package com.foxholedefense.screen.state;

import com.foxholedefense.screen.state.ScreenStateManager.ScreenState;

/**
 * Interface for an observer to observe the ScreenStateManager
 * 
 * @author Eric
 *
 */
public interface IScreenStateObserver {
	public void changeScreenState(ScreenState state);
}

package com.eric.mtd.screen.state;

import com.eric.mtd.screen.state.ScreenStateManager.ScreenState;

/**
 * Interface for an observer to observe the ScreenStateManager
 * 
 * @author Eric
 *
 */
public interface IScreenStateObserver {
	public void changeScreenState(ScreenState state);
}

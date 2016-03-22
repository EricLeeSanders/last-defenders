package com.eric.mtd.screen.state;

import com.eric.mtd.screen.state.ScreenStateManager.ScreenState;

public interface IScreenStateObserver {
	public void changeScreenState(ScreenState state);
}

package com.eric.mtd.ui.state;

import com.eric.mtd.ui.state.UIStateManager.UIState;

public interface IUIStateObserver {
	public void changeUIState(UIState state);
}


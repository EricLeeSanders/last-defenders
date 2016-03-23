package com.eric.mtd.game.ui.state;

import com.eric.mtd.game.ui.state.UIStateManager.UIState;

public interface IUIStateObserver {
	public void changeUIState(UIState state);
}


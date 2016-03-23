package com.eric.mtd.game.ui.state;

import com.eric.mtd.game.ui.state.GameUIStateManager.GameUIState;

public interface GameIUIStateObserver {
	public void changeUIState(GameUIState state);
}


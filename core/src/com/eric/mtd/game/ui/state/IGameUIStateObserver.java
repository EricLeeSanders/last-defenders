package com.eric.mtd.game.ui.state;

import com.eric.mtd.game.ui.state.GameUIStateManager.GameUIState;

public interface IGameUIStateObserver {
	public void changeUIState(GameUIState state);
}

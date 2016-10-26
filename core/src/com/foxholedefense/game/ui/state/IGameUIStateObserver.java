package com.foxholedefense.game.ui.state;

import com.foxholedefense.game.ui.state.GameUIStateManager.GameUIState;

public interface IGameUIStateObserver {
	public void changeUIState(GameUIState state);
}

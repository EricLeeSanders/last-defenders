package com.foxholedefense.game.ui.state;

import com.foxholedefense.game.ui.state.GameUIStateManager.GameUIState;

public interface IGameUIStateObserver {
	void changeUIState(GameUIState state);
}

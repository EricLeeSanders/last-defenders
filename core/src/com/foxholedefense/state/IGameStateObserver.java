package com.foxholedefense.state;

import com.foxholedefense.state.GameStateManager.GameState;

public interface IGameStateObserver {
	void changeGameState(GameState state);
}

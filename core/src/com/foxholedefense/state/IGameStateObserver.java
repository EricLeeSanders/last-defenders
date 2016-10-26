package com.foxholedefense.state;

import com.foxholedefense.state.GameStateManager.GameState;

public interface IGameStateObserver {
	public void changeGameState(GameState state);
}

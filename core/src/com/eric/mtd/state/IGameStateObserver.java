package com.eric.mtd.state;

import com.eric.mtd.state.GameStateManager.GameState;

public interface IGameStateObserver {
	public void changeGameState(GameState state);
}

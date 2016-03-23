package com.eric.mtd.game.model.level.state;

import com.eric.mtd.game.model.level.state.LevelStateManager.LevelState;

public interface ILevelStateObserver {
	public void changeLevelState(LevelState state);
}

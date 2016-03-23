package com.eric.mtd.model.level.state;

import com.eric.mtd.model.level.state.LevelStateManager.LevelState;

public interface ILevelStateObserver {
	public void changeLevelState(LevelState state);
}

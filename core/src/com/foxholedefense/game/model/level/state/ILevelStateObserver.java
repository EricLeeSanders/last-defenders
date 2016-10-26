package com.foxholedefense.game.model.level.state;

import com.foxholedefense.game.model.level.state.LevelStateManager.LevelState;

/**
 * Interface for an observer to observe the LevelStateManager
 * 
 * @author Eric
 *
 */
public interface ILevelStateObserver {
	public void changeLevelState(LevelState state);
}

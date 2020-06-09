package com.lastdefenders.game.model.actor.combat.tower.state;

import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.model.actor.combat.tower.state.states.TowerStateEnum;

public interface TowerStateObserver {
    void stateChange(TowerStateEnum state, Tower tower);
}

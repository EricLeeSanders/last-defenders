package com.lastdefenders.game.model.actor.combat.tower.state;

import com.lastdefenders.game.model.actor.combat.state.CombatActorState;
import com.lastdefenders.game.model.actor.combat.state.StateManager;
import com.lastdefenders.game.model.actor.combat.state.states.CombatActorDeadState;
import com.lastdefenders.game.model.actor.combat.state.states.CombatActorStandByState;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.model.actor.combat.tower.state.states.TowerActiveState;
import com.lastdefenders.game.model.actor.combat.tower.state.states.TowerStateEnum;
import com.lastdefenders.game.model.actor.combat.tower.state.states.TowerWaveEndState;
import com.lastdefenders.game.service.factory.EffectFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eric on 5/8/2017.
 */

public class TowerStateManager extends StateManager<TowerStateEnum, Tower>{

    private Map<TowerStateEnum, CombatActorState> towerStates = new HashMap<>();

    public TowerStateManager(Tower tower, EffectFactory effectFactory) {
        super(tower);
        towerStates.put(TowerStateEnum.ACTIVE, new TowerActiveState(tower, this));
        towerStates.put(TowerStateEnum.DEAD,
            new CombatActorDeadState(tower, effectFactory));
        towerStates.put(TowerStateEnum.WAVE_END,
            new TowerWaveEndState(tower, effectFactory));
        towerStates.put(TowerStateEnum.STANDBY, new CombatActorStandByState());
    }

    @Override
    protected Map<TowerStateEnum, CombatActorState> getStates() {

        return towerStates;
    }
}

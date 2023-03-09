package com.lastdefenders.game.model.actor.combat.tower.state.states;

import com.lastdefenders.game.model.actor.combat.state.CombatActorState;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.model.actor.effects.label.TowerHealEffect;
import com.lastdefenders.game.service.factory.EffectFactory;
import java.util.Map;

/**
 * Created by Eric on 12/25/2019.
 */

public class TowerWaveEndState implements CombatActorState {

    private Tower tower;
    private EffectFactory effectFactory;

    public TowerWaveEndState(Tower tower, EffectFactory effectFactory){
        this.tower = tower;
        this.effectFactory = effectFactory;
    }

    @Override
    public void loadParameters(Map<String, Object> parameters) {

    }

    @Override
    public void preState() {
        if ( tower.getHealthPercent() < 1){
            TowerHealEffect effect = effectFactory
                .loadEffect(TowerHealEffect.class, true);
            effect.initialize(tower);

            tower.heal();
        }

        if(tower.hasArmor()){
            tower.resetArmor();
        }
    }

    @Override
    public void immediateStep() {

    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void postState() {

    }
}

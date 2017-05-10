package com.foxholedefense.game.model.actor.combat.enemy.state.states;

import com.foxholedefense.game.model.Player;
import com.foxholedefense.game.model.actor.combat.enemy.Enemy;
import com.foxholedefense.game.model.actor.combat.enemy.state.EnemyStateManager.EnemyState;
import com.foxholedefense.game.model.actor.combat.state.CombatActorState;
import com.foxholedefense.game.model.actor.combat.state.StateTransitioner;
import com.foxholedefense.game.model.actor.effects.texture.animation.EnemyCoinEffect;
import com.foxholedefense.game.service.factory.EffectFactory;

import java.util.Map;

/**
 * Created by Eric on 5/5/2017.
 */

public class EnemyDyingState implements CombatActorState {

    private final Enemy enemy;
    private final StateTransitioner<EnemyState> stateTransitioner;
    private final EffectFactory effectFactory;
    private final Player player;

    public EnemyDyingState(Enemy enemy, StateTransitioner<EnemyState> stateTransitioner, EffectFactory effectFactory, Player player) {
        this.enemy = enemy;
        this.stateTransitioner = stateTransitioner;
        this.effectFactory = effectFactory;
        this.player = player;
    }

    @Override
    public void loadParameters(Map<String, Object> parameters) {

    }

    @Override
    public void preState() {
        // Has to be put in preState because Update is never called (Enemy is removed from stage)
        effectFactory.loadDeathEffect(enemy.getDeathEffectType()).initialize(enemy.getPositionCenter());
        effectFactory.loadAnimationEffect(EnemyCoinEffect.class).initialize(enemy.getPositionCenter());
        player.giveMoney(enemy.getKillReward());
        stateTransitioner.transition(EnemyState.STANDBY);
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void postState() {

    }
}

package com.lastdefenders.game.model.actor.combat.enemy.state.states;

import com.lastdefenders.game.model.Player;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.actor.combat.enemy.state.EnemyStateEnum;
import com.lastdefenders.game.model.actor.combat.state.states.CombatActorDeadState;
import com.lastdefenders.game.model.actor.effects.texture.animation.EnemyCoinEffect;
import com.lastdefenders.game.service.factory.EffectFactory;
import java.util.Map;

/**
 * Created by Eric on 5/5/2017.
 */

public class EnemyDeadState extends CombatActorDeadState {

    private final Enemy enemy;
    private final EffectFactory effectFactory;
    private final Player player;

    public EnemyDeadState(Enemy enemy, EffectFactory effectFactory, Player player) {

        super(enemy, effectFactory);
        this.enemy = enemy;
        this.effectFactory = effectFactory;
        this.player = player;
    }
    
    @Override
    public void preState() {
        // Has to be put in preState because Update is never called (Enemy is removed from stage)
        effectFactory.loadEffect(EnemyCoinEffect.class, true)
            .initialize(enemy.getPositionCenter());
        player.giveMoney(enemy.getKillReward());
        super.preState();
    }

}

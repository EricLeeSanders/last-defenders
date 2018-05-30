package com.lastdefenders.game.model.actor.ai.towerai;

import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.utils.ObjectMap;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.actor.combat.enemy.EnemyFlameThrower;
import com.lastdefenders.game.model.actor.combat.enemy.EnemyHumvee;
import com.lastdefenders.game.model.actor.combat.enemy.EnemyMachineGun;
import com.lastdefenders.game.model.actor.combat.enemy.EnemyRifle;
import com.lastdefenders.game.model.actor.combat.enemy.EnemyRocketLauncher;
import com.lastdefenders.game.model.actor.combat.enemy.EnemySniper;
import com.lastdefenders.game.model.actor.combat.enemy.EnemyTank;

/**
 * Created by Eric on 5/29/2018.
 */

class EnemyStrengthPriority {
    static final ObjectMap<Class<? extends Enemy>, Integer> enemyStrengthPriorties = new ObjectMap<>();
    static {
        enemyStrengthPriorties.put(EnemyTank.class, 7);
        enemyStrengthPriorties.put(EnemyRocketLauncher.class, 6);
        enemyStrengthPriorties.put(EnemyFlameThrower.class, 5);
        enemyStrengthPriorties.put(EnemySniper.class, 4);
        enemyStrengthPriorties.put(EnemyMachineGun.class, 3);
        enemyStrengthPriorties.put(EnemyRifle.class, 2);
        enemyStrengthPriorties.put(EnemyHumvee.class, 1);

    }

    static int compare(Enemy e1, Enemy e2){

        int e1Priority = enemyStrengthPriorties.get(e1.getClass());
        int e2Priority = enemyStrengthPriorties.get(e2.getClass());

        if(e1Priority < e2Priority){
            return -1;
        } else if (e1Priority > e2Priority ) {
            return 1;
        }

        return 0;
    }

}

package com.lastdefenders.game.model.actor.ai.towerai;

import com.lastdefenders.game.model.actor.combat.enemy.Enemy;

/**
 * Created by Eric on 10/28/2016.
 */

public class FarthestEnemyAI extends AbstractTowerAI {

    @Override
    protected boolean swap(Enemy currentEnemy, Enemy replacingEnemy) {

        return replacingEnemy.getLengthToEnd() > currentEnemy.getLengthToEnd();
    }
}

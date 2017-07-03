package com.foxholedefense.game.model.actor.ai.towerai;

import com.foxholedefense.game.model.actor.combat.enemy.Enemy;

/**
 * Created by Eric on 10/28/2016.
 */

public class MostHPEnemyAI extends AbstractTowerAI {

    @Override
    protected boolean swap(Enemy currentEnemy, Enemy replacingEnemy) {

        return (replacingEnemy.getHealth() + replacingEnemy.getArmor())
            > (currentEnemy.getHealth() + currentEnemy.getArmor());
    }
}

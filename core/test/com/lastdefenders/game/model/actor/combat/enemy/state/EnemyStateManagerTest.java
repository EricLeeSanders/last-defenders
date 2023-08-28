package com.lastdefenders.game.model.actor.combat.enemy.state;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.actor.combat.enemy.EnemyRifle;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.model.actor.combat.tower.TowerRifle;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import testutil.TestUtil;

/**
 * Created by Eric on 5/14/2017.
 */
public class EnemyStateManagerTest {

    @Test
    public void transitionTest() {

        Enemy enemy = TestUtil.createEnemy(EnemyRifle.class, false);

        enemy.getStateManager().transition(EnemyStateEnum.RUNNING);
        assertEquals(EnemyStateEnum.RUNNING, enemy.getStateManager().getCurrentStateName());
    }

    @Test
    public void transitionWithParametersTest() {

        Enemy enemy = TestUtil.createEnemy(EnemyRifle.class, false);
        Tower tower = TestUtil.createTower(TowerRifle.class, false, true);


        Map<String, Object> parameters = new HashMap<>();
        parameters.put("target", tower);

        enemy.getStateManager().transition(EnemyStateEnum.ATTACKING, parameters);
        assertEquals(EnemyStateEnum.ATTACKING, enemy.getStateManager().getCurrentStateName());
    }
}

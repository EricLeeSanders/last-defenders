package com.lastdefenders.game.model.actor.combat.enemy.state;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.lastdefenders.game.model.Player;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.actor.combat.enemy.EnemyRifle;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.model.actor.combat.tower.TowerRifle;
import com.lastdefenders.game.service.factory.EffectFactory;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import testutil.TestUtil;

/**
 * Created by Eric on 5/14/2017.
 */
public class EnemyStateManagerTest {


    @Before
    public void initEnemyStateManagerTest() {

        Gdx.app = mock(Application.class);
    }

    @Test
    public void transitionTest() {

        Enemy enemy = TestUtil.createEnemy(EnemyRifle.class, false);

        enemy.getStateManager().transition(EnemyStateEnum.RUNNING);
        assertEquals(EnemyStateEnum.RUNNING, enemy.getStateManager().getCurrentStateName());
    }

    @Test
    public void transitionWithParametersTest() {

        Enemy enemy = TestUtil.createEnemy(EnemyRifle.class, false);
        Tower tower = TestUtil.createTower(TowerRifle.class, false);


        Map<String, Object> parameters = new HashMap<>();
        parameters.put("target", tower);

        enemy.getStateManager().transition(EnemyStateEnum.ATTACKING, parameters);
        assertEquals(EnemyStateEnum.ATTACKING, enemy.getStateManager().getCurrentStateName());
    }
}

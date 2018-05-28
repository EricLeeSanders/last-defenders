package com.lastdefenders.game.model.actor.combat.enemy.state;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.lastdefenders.game.model.Player;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.actor.combat.enemy.state.EnemyStateManager;
import com.lastdefenders.game.model.actor.combat.enemy.state.EnemyStateManager.EnemyState;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
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

        Enemy enemy = TestUtil.createEnemy("Rifle", false);
        Player player = mock(Player.class);
        EffectFactory effectFactoryMock = mock(EffectFactory.class);

        EnemyStateManager stateManager = new EnemyStateManager(enemy, effectFactoryMock, player);
        assertEquals(EnemyState.STANDBY, stateManager.getCurrentStateName());

        stateManager.transition(EnemyState.ATTACKING);
        assertEquals(EnemyState.ATTACKING, stateManager.getCurrentStateName());
    }

    @Test
    public void transitionWithParametersTest() {

        Enemy enemy = TestUtil.createEnemy("Rifle", false);
        Tower tower = TestUtil.createTower("Rifle", false);

        Player player = mock(Player.class);
        EffectFactory effectFactoryMock = mock(EffectFactory.class);

        EnemyStateManager stateManager = new EnemyStateManager(enemy, effectFactoryMock, player);
        assertEquals(EnemyState.STANDBY, stateManager.getCurrentStateName());

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("target", tower);

        stateManager.transition(EnemyState.ATTACKING, parameters);
        assertEquals(EnemyState.ATTACKING, stateManager.getCurrentStateName());
    }
}
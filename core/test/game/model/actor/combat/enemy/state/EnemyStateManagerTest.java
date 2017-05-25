package game.model.actor.combat.enemy.state;

import com.foxholedefense.game.model.Player;
import com.foxholedefense.game.model.actor.combat.enemy.Enemy;
import com.foxholedefense.game.model.actor.combat.enemy.state.EnemyStateManager;
import com.foxholedefense.game.model.actor.combat.enemy.state.EnemyStateManager.EnemyState;
import com.foxholedefense.game.model.actor.combat.tower.Tower;
import com.foxholedefense.game.service.factory.EffectFactory;
import com.foxholedefense.util.Logger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;
import java.util.Map;

import util.TestUtil;

import static org.junit.Assert.*;

import static org.mockito.Mockito.mock;

/**
 * Created by Eric on 5/14/2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Logger.class})
public class EnemyStateManagerTest {


    @Before
    public void initEnemyStateManagerTest(){
        PowerMockito.mockStatic(Logger.class);
    }

    @Test
    public void transitionTest(){
        Enemy enemy = TestUtil.createEnemy("EnemyRifle", false);
        Player player = mock(Player.class);
        EffectFactory effectFactoryMock = mock(EffectFactory.class);

        EnemyStateManager stateManager = new EnemyStateManager(enemy, effectFactoryMock, player);
        assertEquals(EnemyState.STANDBY, stateManager.getCurrentStateName());

        stateManager.transition(EnemyState.ATTACKING);
        assertEquals(EnemyState.ATTACKING, stateManager.getCurrentStateName());
    }

    @Test
    public void transitionWithParametersTest(){
        Enemy enemy = TestUtil.createEnemy("EnemyRifle", false);
        Tower tower = TestUtil.createTower("Rifle", false);

        Player player = mock(Player.class);
        EffectFactory effectFactoryMock = mock(EffectFactory.class);

        EnemyStateManager stateManager = new EnemyStateManager(enemy, effectFactoryMock, player);
        assertEquals(EnemyState.STANDBY, stateManager.getCurrentStateName());

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("target", tower);

        stateManager.transition(EnemyState.ATTACKING, parameters);
        assertEquals(EnemyState.ATTACKING, stateManager.getCurrentStateName());
    }
}
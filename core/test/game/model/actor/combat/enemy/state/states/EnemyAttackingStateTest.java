package game.model.actor.combat.enemy.state.states;

import com.foxholedefense.game.model.Player;
import com.foxholedefense.game.model.actor.combat.enemy.Enemy;
import com.foxholedefense.game.model.actor.combat.enemy.state.EnemyStateManager;
import com.foxholedefense.game.model.actor.combat.enemy.state.EnemyStateManager.EnemyState;
import com.foxholedefense.game.model.actor.combat.enemy.state.states.EnemyAttackingState;
import com.foxholedefense.game.model.actor.combat.tower.Tower;
import com.foxholedefense.game.model.actor.interfaces.ITargetable;
import com.foxholedefense.game.service.factory.EffectFactory;
import com.foxholedefense.util.Logger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


import java.util.HashMap;
import java.util.Map;

import util.TestUtil;


import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import static org.junit.Assert.*;
/**
 * Created by Eric on 5/15/2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Logger.class})
public class EnemyAttackingStateTest {

    @Before
    public void initEnemyAttackingStateTest(){
        PowerMockito.mockStatic(Logger.class);
    }

    @Test
    public void enemyAttackingStateTest1(){
        Enemy enemy = TestUtil.createEnemy("Rifle", true);
        Tower tower = TestUtil.createTower("Rifle", false);

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("target", tower);

        EnemyStateManager stateManagerMock = mock(EnemyStateManager.class);

        EnemyAttackingState attackingState = new EnemyAttackingState(enemy, stateManagerMock);
        attackingState.loadParameters(parameters);

        doReturn(Enemy.MOVEMENT_DELAY / 2).when(enemy).getAttackSpeed();
        verify(enemy, never()).attackTarget(isA(ITargetable.class));

        attackingState.update(Enemy.MOVEMENT_DELAY / 2);
        attackingState.update(Enemy.MOVEMENT_DELAY / 4);

        verify(enemy, times(1)).attackTarget(isA(ITargetable.class));

        attackingState.update(Enemy.MOVEMENT_DELAY / 4);

        verify(stateManagerMock, times(1)).transition(EnemyState.RUNNING);

    }
}

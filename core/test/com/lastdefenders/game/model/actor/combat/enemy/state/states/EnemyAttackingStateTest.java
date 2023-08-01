package com.lastdefenders.game.model.actor.combat.enemy.state.states;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.actor.combat.enemy.EnemyRifle;
import com.lastdefenders.game.model.actor.combat.enemy.state.EnemyStateEnum;
import com.lastdefenders.game.model.actor.combat.enemy.state.EnemyStateManager;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.model.actor.combat.tower.TowerRifle;
import com.lastdefenders.game.model.actor.interfaces.Targetable;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import testutil.TestUtil;

/**
 * Created by Eric on 5/15/2017.
 */
public class EnemyAttackingStateTest {
    @Test
    public void enemyAttackingStateTest1() {

        Enemy enemy = TestUtil.createEnemy(EnemyRifle.class, true);
        Tower tower = TestUtil.createTower(TowerRifle.class, false, true);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("target", tower);

        EnemyStateManager stateManagerMock = mock(EnemyStateManager.class);

        EnemyAttackingState attackingState = new EnemyAttackingState(enemy, stateManagerMock);
        attackingState.loadParameters(parameters);

        doReturn(Enemy.MOVEMENT_DELAY / 2).when(enemy).getAttackSpeed();
        verify(enemy, never()).attackTarget(isA(Targetable.class));

        attackingState.update(Enemy.MOVEMENT_DELAY / 2);
        attackingState.update(Enemy.MOVEMENT_DELAY / 4);

        verify(enemy, times(1)).attackTarget(isA(Targetable.class));

        attackingState.update(Enemy.MOVEMENT_DELAY / 4);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NewSpawn", Boolean.FALSE);
        verify(stateManagerMock, times(1)).transition(EnemyStateEnum.RUNNING, params);

    }
}

package com.lastdefenders.game.model.actor.combat.enemy.state.states;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;
import com.lastdefenders.game.model.actor.ai.EnemyAI;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.actor.combat.enemy.EnemyRifle;
import com.lastdefenders.game.model.actor.combat.enemy.state.EnemyStateEnum;
import com.lastdefenders.game.model.actor.combat.enemy.state.EnemyStateManager;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.model.actor.combat.tower.TowerRifle;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import testutil.TestUtil;

/**
 * Created by Eric on 5/15/2017.
 */
public class EnemyRunningStateTest {
    @Test
    @SuppressWarnings("unchecked")
    public void enemyRunningStateTest1() {

        Enemy enemy = TestUtil.createEnemy(EnemyRifle.class, true);
        Tower tower = TestUtil.createTower(TowerRifle.class, false, true);

        Array<Action> arrayAction = new Array<>();
        arrayAction.add(new SequenceAction());

        doReturn(arrayAction).when(enemy).getActions();

        EnemyStateManager stateManagerMock = mock(EnemyStateManager.class);

        EnemyRunningState runningState = new EnemyRunningState(enemy, stateManagerMock);

        SnapshotArray<Tower> targetGroupArray = enemy.getEnemyGroup().getCastedChildren();
        try(MockedStatic<EnemyAI> mockedEnemyAI = mockStatic(EnemyAI.class)) {
            mockedEnemyAI.when(() -> EnemyAI.findRandomTowerInRange(enemy, targetGroupArray)).thenReturn(tower);
            runningState.update(10f);
            runningState.update(1f);
        }

        verify(stateManagerMock, times(1)).transition(eq(EnemyStateEnum.ATTACKING), isA(Map.class));

    }
}

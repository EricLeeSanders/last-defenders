package game.model.actor.combat.enemy.state.states;

import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;
import com.lastdefenders.game.model.actor.ai.EnemyAI;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.actor.combat.enemy.state.EnemyStateManager;
import com.lastdefenders.game.model.actor.combat.enemy.state.EnemyStateManager.EnemyState;
import com.lastdefenders.game.model.actor.combat.enemy.state.states.EnemyRunningState;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import testutil.TestUtil;

/**
 * Created by Eric on 5/15/2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({EnemyAI.class})
public class EnemyRunningStateTest {

    @Before
    public void initEnemyRunningStateTest() {

        Gdx.app = mock(Application.class);
        PowerMockito.mockStatic(EnemyAI.class);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void enemyRunningStateTest1() {

        Enemy enemy = TestUtil.createEnemy("Rifle", true);
        Tower tower = TestUtil.createTower("Rifle", false);

        Array<Action> arrayAction = new Array<>();
        arrayAction.add(new SequenceAction());

        doReturn(arrayAction).when(enemy).getActions();

        EnemyStateManager stateManagerMock = mock(EnemyStateManager.class);

        EnemyRunningState runningState = new EnemyRunningState(enemy, stateManagerMock);

        SnapshotArray<Actor> targetGroupArray = enemy.getTargetGroup().getChildren();
        when(EnemyAI.findNearestTower(enemy, targetGroupArray)).thenReturn(tower);
        runningState.update(Enemy.FIND_TARGET_DELAY);
        runningState.update(1f);

        verify(stateManagerMock, times(1)).transition(eq(EnemyState.ATTACKING), isA(Map.class));

    }
}

package game.model.actor.combat.enemy.state.states;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.actor.combat.enemy.state.EnemyStateManager;
import com.lastdefenders.game.model.actor.combat.enemy.state.EnemyStateManager.EnemyState;
import com.lastdefenders.game.model.actor.combat.enemy.state.states.EnemyAttackingState;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.model.actor.interfaces.Targetable;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import testutil.TestUtil;

/**
 * Created by Eric on 5/15/2017.
 */
public class EnemyAttackingStateTest {

    @Before
    public void initEnemyAttackingStateTest() {

        Gdx.app = mock(Application.class);
    }

    @Test
    public void enemyAttackingStateTest1() {

        Enemy enemy = TestUtil.createEnemy("Rifle", true);
        Tower tower = TestUtil.createTower("Rifle", false);

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

        verify(stateManagerMock, times(1)).transition(EnemyState.RUNNING);

    }
}

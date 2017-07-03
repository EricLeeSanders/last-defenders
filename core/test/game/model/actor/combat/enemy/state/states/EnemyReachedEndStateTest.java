package game.model.actor.combat.enemy.state.states;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.foxholedefense.game.model.Player;
import com.foxholedefense.game.model.actor.ai.EnemyAI;
import com.foxholedefense.game.model.actor.combat.enemy.Enemy;
import com.foxholedefense.game.model.actor.combat.enemy.state.EnemyStateManager;
import com.foxholedefense.game.model.actor.combat.enemy.state.EnemyStateManager.EnemyState;
import com.foxholedefense.game.model.actor.combat.enemy.state.states.EnemyReachedEndState;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by Eric on 5/16/2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({EnemyAI.class})
public class EnemyReachedEndStateTest {

    @Before
    public void initEnemyReachedEndStateTest() {
        Gdx.app = mock(Application.class);
        PowerMockito.mockStatic(EnemyAI.class);
    }

    @Test
    public void enemyReachedEndStateTest1() {
        Enemy enemy = mock(Enemy.class);
        EnemyStateManager stateManagerMock = mock(EnemyStateManager.class);
        Player playerMock = mock(Player.class);


        EnemyReachedEndState reachedEndState = new EnemyReachedEndState(enemy, stateManagerMock, playerMock);

        reachedEndState.update(1f);

        verify(enemy, times(1)).reachedEnd();
        verify(playerMock, times(1)).enemyReachedEnd();
        verify(stateManagerMock, times(1)).transition(EnemyState.STANDBY);
    }
}

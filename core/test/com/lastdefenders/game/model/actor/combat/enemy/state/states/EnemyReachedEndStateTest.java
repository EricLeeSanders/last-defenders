package com.lastdefenders.game.model.actor.combat.enemy.state.states;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.lastdefenders.game.model.Player;
import com.lastdefenders.game.model.actor.ai.EnemyAI;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.actor.combat.enemy.state.EnemyStateManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

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

        EnemyReachedEndState reachedEndState = new EnemyReachedEndState(enemy, stateManagerMock,
            playerMock);

        reachedEndState.immediateStep();

        verify(enemy, times(1)).reachedEnd();
        verify(playerMock, times(1)).enemyReachedEnd();
    }
}

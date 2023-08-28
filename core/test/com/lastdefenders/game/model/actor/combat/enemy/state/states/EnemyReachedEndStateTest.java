package com.lastdefenders.game.model.actor.combat.enemy.state.states;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.lastdefenders.game.model.Player;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.actor.combat.enemy.state.EnemyStateManager;
import org.junit.jupiter.api.Test;

/**
 * Created by Eric on 5/16/2017.
 */
public class EnemyReachedEndStateTest {

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

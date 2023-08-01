package com.lastdefenders.game.model.actor.combat.enemy.state.states;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.lastdefenders.game.model.Player;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.actor.combat.enemy.EnemyRifle;
import com.lastdefenders.game.model.actor.combat.enemy.state.EnemyStateManager;
import com.lastdefenders.game.model.actor.effects.texture.TextureEffect;
import com.lastdefenders.game.model.actor.effects.texture.animation.EnemyCoinEffect;
import com.lastdefenders.game.service.factory.EffectFactory;
import org.junit.jupiter.api.Test;
import testutil.TestUtil;

/**
 * Created by Eric on 5/16/2017.
 */
public class EnemyDeadStateTest {

    @Test
    public void enemyDeadStateTest1() {

        Enemy enemy = TestUtil.createEnemy(EnemyRifle.class, false);
        EffectFactory effectFactoryMock = mock(EffectFactory.class);
        Player playerMock = mock(Player.class);

        TextureEffect textureEffectMock = mock(TextureEffect.class);
        doReturn(textureEffectMock).when(effectFactoryMock)
            .loadDeathEffect(eq(enemy.getDeathEffectType()), isA(Boolean.class));

        EnemyCoinEffect enemyCoinEffectMock = mock(EnemyCoinEffect.class);
        doReturn(enemyCoinEffectMock).when(effectFactoryMock)
            .loadEffect(eq(EnemyCoinEffect.class), isA(Boolean.class));

        EnemyDeadState deadState = new EnemyDeadState(enemy, effectFactoryMock,
            playerMock);

        deadState.preState();

        verify(effectFactoryMock, times(1)).loadEffect(eq(EnemyCoinEffect.class), isA(Boolean.class));
        verify(enemyCoinEffectMock, times(1)).initialize(enemy.getPositionCenter());
        verify(playerMock, times(1)).giveMoney(enemy.getKillReward());
    }
}

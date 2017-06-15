package game.model.actor.combat.enemy.state.states;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.foxholedefense.game.model.Player;
import com.foxholedefense.game.model.actor.ai.EnemyAI;
import com.foxholedefense.game.model.actor.combat.enemy.Enemy;
import com.foxholedefense.game.model.actor.combat.enemy.state.EnemyStateManager;
import com.foxholedefense.game.model.actor.combat.enemy.state.EnemyStateManager.EnemyState;
import com.foxholedefense.game.model.actor.combat.enemy.state.states.EnemyDyingState;
import com.foxholedefense.game.model.actor.effects.texture.animation.EnemyCoinEffect;
import com.foxholedefense.game.model.actor.effects.texture.animation.death.DeathEffect;
import com.foxholedefense.game.service.factory.EffectFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import testutil.TestUtil;


import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by Eric on 5/16/2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({EnemyAI.class})
public class EnemyDyingStateTest {

    @Before
    public void initEnemyDyingStateTest() {
        Gdx.app = mock(Application.class);
        PowerMockito.mockStatic(EnemyAI.class);
    }

    @Test
    public void enemyDyingStateTest1(){
        Enemy enemy = TestUtil.createEnemy("Rifle", false);
        EnemyStateManager stateManagerMock = mock(EnemyStateManager.class);
        EffectFactory effectFactoryMock = mock(EffectFactory.class);
        Player playerMock = mock(Player.class);

        DeathEffect deathEffectMock = mock(DeathEffect.class);
        doReturn(deathEffectMock).when(effectFactoryMock).loadDeathEffect(enemy.getDeathEffectType());

        EnemyCoinEffect enemyCoinEffectMock = mock(EnemyCoinEffect.class);
        doReturn(enemyCoinEffectMock).when(effectFactoryMock).loadAnimationEffect(EnemyCoinEffect.class);

        EnemyDyingState dyingState = new EnemyDyingState(enemy, stateManagerMock, effectFactoryMock, playerMock);

        dyingState.preState();

        verify(effectFactoryMock, times(1)).loadAnimationEffect(EnemyCoinEffect.class);
        verify(enemyCoinEffectMock, times(1)).initialize(enemy.getPositionCenter());
        verify(playerMock, times(1)).giveMoney(enemy.getKillReward());
        verify(stateManagerMock, times(1)).transition(EnemyState.STANDBY);
    }
}

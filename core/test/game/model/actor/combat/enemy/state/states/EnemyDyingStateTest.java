package game.model.actor.combat.enemy.state.states;

import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.lastdefenders.game.model.Player;
import com.lastdefenders.game.model.actor.ai.EnemyAI;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.actor.combat.enemy.state.EnemyStateManager;
import com.lastdefenders.game.model.actor.combat.enemy.state.EnemyStateManager.EnemyState;
import com.lastdefenders.game.model.actor.combat.enemy.state.states.EnemyDyingState;
import com.lastdefenders.game.model.actor.effects.texture.TextureEffect;
import com.lastdefenders.game.model.actor.effects.texture.animation.EnemyCoinEffect;
import com.lastdefenders.game.service.factory.EffectFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import testutil.TestUtil;

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
    public void enemyDyingStateTest1() {

        Enemy enemy = TestUtil.createEnemy("Rifle", false);
        EnemyStateManager stateManagerMock = mock(EnemyStateManager.class);
        EffectFactory effectFactoryMock = mock(EffectFactory.class);
        Player playerMock = mock(Player.class);

        TextureEffect textureEffectMock = mock(TextureEffect.class);
        doReturn(textureEffectMock).when(effectFactoryMock)
            .loadDeathEffect(eq(enemy.getDeathEffectType()), isA(Boolean.class));

        EnemyCoinEffect enemyCoinEffectMock = mock(EnemyCoinEffect.class);
        doReturn(enemyCoinEffectMock).when(effectFactoryMock)
            .loadEffect(eq(EnemyCoinEffect.class), isA(Boolean.class));

        EnemyDyingState dyingState = new EnemyDyingState(enemy, stateManagerMock, effectFactoryMock,
            playerMock);

        dyingState.preState();

        verify(effectFactoryMock, times(1)).loadEffect(eq(EnemyCoinEffect.class), isA(Boolean.class));
        verify(enemyCoinEffectMock, times(1)).initialize(enemy.getPositionCenter());
        verify(playerMock, times(1)).giveMoney(enemy.getKillReward());
        verify(stateManagerMock, times(1)).transition(EnemyState.STANDBY);
    }
}

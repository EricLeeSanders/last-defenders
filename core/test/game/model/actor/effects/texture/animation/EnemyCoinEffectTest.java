package game.model.actor.effects.texture.animation;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.foxholedefense.game.model.actor.effects.texture.animation.EnemyCoinEffect;
import com.foxholedefense.game.service.factory.EffectFactory.DeathEffectPool;
import com.foxholedefense.util.Resources;
import com.foxholedefense.util.datastructures.pool.FHDVector2;

import org.junit.Before;
import org.junit.Test;

import testutil.TestUtil;


import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by Eric on 5/20/2017.
 */

public class EnemyCoinEffectTest {

    private DeathEffectPool deathEffectPoolMock = mock(DeathEffectPool.class);

    @Before
    public void initEnemyCoinEffectTest() {
        Gdx.app = mock(Application.class);
    }

    private EnemyCoinEffect createEnemyCoinEffect(){

        Resources resourcesMock = TestUtil.createResourcesMock();

        EnemyCoinEffect enemyCoinEffect = new EnemyCoinEffect(deathEffectPoolMock, resourcesMock.getAtlasRegion(""));

        return enemyCoinEffect;

    }

    /**
     * Tests that the EnemyCoinEffect is freed after finishing
     */
    @Test
    public void enemyCoinEffectTest1(){

        EnemyCoinEffect enemyCoinEffect = createEnemyCoinEffect();
        enemyCoinEffect.initialize(new FHDVector2(20,20));

        assertEquals(new FHDVector2(20,20), enemyCoinEffect.getPositionCenter());
        assertEquals(1, enemyCoinEffect.getActions().size);
        enemyCoinEffect.act(100f);

        verify(deathEffectPoolMock, times(1)).free(enemyCoinEffect);
    }
}

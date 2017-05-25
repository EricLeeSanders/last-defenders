package game.model.actor.effects.texture.animation;

import com.foxholedefense.game.model.actor.effects.texture.animation.EnemyCoinEffect;
import com.foxholedefense.game.service.factory.EffectFactory.DeathEffectPool;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.Resources;
import com.foxholedefense.util.datastructures.pool.FHDVector2;
import com.foxholedefense.util.datastructures.pool.UtilPool;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import util.TestUtil;


import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by Eric on 5/20/2017.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({Logger.class})
public class EnemyCoinEffectTest {

    private DeathEffectPool deathEffectPoolMock = mock(DeathEffectPool.class);

    public EnemyCoinEffect createEnemyCoinEffect(){

        Resources resourcesMock = TestUtil.createResourcesMock();

        EnemyCoinEffect enemyCoinEffect = new EnemyCoinEffect(deathEffectPoolMock, resourcesMock.getAtlasRegion(""));

        return enemyCoinEffect;

    }

    @Before
    public void initEnemyCoinEffectTest() {
        PowerMockito.mockStatic(Logger.class);
        createEnemyCoinEffect();
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
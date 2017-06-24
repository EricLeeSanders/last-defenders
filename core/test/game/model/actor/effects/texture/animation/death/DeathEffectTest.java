package game.model.actor.effects.texture.animation.death;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.foxholedefense.game.model.actor.effects.texture.animation.death.BloodSplatter;
import com.foxholedefense.game.model.actor.effects.texture.animation.death.DeathEffect;
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

public class DeathEffectTest {

    @SuppressWarnings("unchecked")
    private DeathEffectPool<BloodSplatter> deathEffectPoolMock = mock(DeathEffectPool.class);

    @Before
    public void initDeathEffectTest() {
        Gdx.app = mock(Application.class);
    }

    private DeathEffect createDeathEffect(){

        Resources resourcesMock = TestUtil.createResourcesMock();

        return new BloodSplatter(deathEffectPoolMock, resourcesMock.getAtlasRegion(""));

    }

    /**
     * Tests that the DeathEffect is freed after finishing
     */
    @Test
    public void deathEffectTest1(){

        DeathEffect deathEffect = createDeathEffect();
        deathEffect.initialize(new FHDVector2(20,20));

        assertEquals(new FHDVector2(20,20), deathEffect.getPositionCenter());

        deathEffect.act(100f);

        verify(deathEffectPoolMock, times(1)).free(deathEffect);
    }
}

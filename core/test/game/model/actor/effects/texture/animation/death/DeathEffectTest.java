package game.model.actor.effects.texture.animation.death;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.lastdefenders.game.model.actor.effects.texture.animation.death.BloodSplatter;
import com.lastdefenders.game.model.actor.effects.texture.animation.death.DeathEffect;
import com.lastdefenders.game.service.factory.EffectFactory.DeathEffectPool;
import com.lastdefenders.util.Resources;
import com.lastdefenders.util.datastructures.pool.LDVector2;
import org.junit.Before;
import org.junit.Test;
import testutil.TestUtil;

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

    private DeathEffect createDeathEffect() {

        Resources resourcesMock = TestUtil.createResourcesMock();

        return new BloodSplatter(deathEffectPoolMock, resourcesMock.getAtlasRegion(""));

    }

    /**
     * Tests that the DeathEffect is freed after finishing
     */
    @Test
    public void deathEffectTest1() {

        DeathEffect deathEffect = createDeathEffect();
        deathEffect.initialize(new LDVector2(20, 20));

        assertEquals(new LDVector2(20, 20), deathEffect.getPositionCenter());

        deathEffect.act(100f);

        verify(deathEffectPoolMock, times(1)).free(deathEffect);
    }
}

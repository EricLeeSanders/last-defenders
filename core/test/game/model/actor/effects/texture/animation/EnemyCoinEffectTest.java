package game.model.actor.effects.texture.animation;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.lastdefenders.game.model.actor.effects.texture.animation.EnemyCoinEffect;
import com.lastdefenders.game.service.factory.EffectFactory.AnimationEffectPool;
import com.lastdefenders.util.Resources;
import com.lastdefenders.util.datastructures.pool.LDVector2;
import org.junit.Before;
import org.junit.Test;
import testutil.TestUtil;

/**
 * Created by Eric on 5/20/2017.
 */

public class EnemyCoinEffectTest {

    @SuppressWarnings("unchecked")
    private AnimationEffectPool<EnemyCoinEffect> animationEffectPool = mock(
        AnimationEffectPool.class);

    @Before
    public void initEnemyCoinEffectTest() {

        Gdx.app = mock(Application.class);
    }

    private EnemyCoinEffect createEnemyCoinEffect() {

        Resources resourcesMock = TestUtil.createResourcesMock();

        return new EnemyCoinEffect(animationEffectPool, resourcesMock.getAtlasRegion(""));

    }

    /**
     * Tests that the EnemyCoinEffect is freed after finishing
     */
    @Test
    public void enemyCoinEffectTest1() {

        EnemyCoinEffect enemyCoinEffect = createEnemyCoinEffect();
        enemyCoinEffect.initialize(new LDVector2(20, 20));

        assertEquals(new LDVector2(20, 20), enemyCoinEffect.getPositionCenter());
        assertEquals(1, enemyCoinEffect.getActions().size);
        enemyCoinEffect.act(100f);

        verify(animationEffectPool, times(1)).free(enemyCoinEffect);
    }
}

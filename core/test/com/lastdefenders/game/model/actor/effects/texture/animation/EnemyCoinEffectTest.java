package com.lastdefenders.game.model.actor.effects.texture.animation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.lastdefenders.game.service.factory.EffectFactory.EffectPool;
import com.lastdefenders.util.Resources;
import com.lastdefenders.util.datastructures.pool.LDVector2;
import org.junit.jupiter.api.Test;
import testutil.ResourcesMock;

/**
 * Created by Eric on 5/20/2017.
 */

public class EnemyCoinEffectTest {

    @SuppressWarnings("unchecked")
    private EffectPool<EnemyCoinEffect> animationEffectPool = mock(
        EffectPool.class);


    private EnemyCoinEffect createEnemyCoinEffect() {

        Resources resourcesMock = ResourcesMock.create();

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
        // Call act twice so that the freeActor action is performed
        enemyCoinEffect.act(0.001f);
        verify(animationEffectPool, times(1)).free(enemyCoinEffect);
    }
}

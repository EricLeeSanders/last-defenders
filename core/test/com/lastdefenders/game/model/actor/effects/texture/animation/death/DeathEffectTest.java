package com.lastdefenders.game.model.actor.effects.texture.animation.death;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import com.lastdefenders.game.model.actor.effects.texture.TextureEffect;
import com.lastdefenders.game.service.factory.EffectFactory.EffectPool;
import com.lastdefenders.util.Resources;
import com.lastdefenders.util.datastructures.pool.LDVector2;
import org.junit.jupiter.api.Test;
import testutil.ResourcesMock;

/**
 * Created by Eric on 5/20/2017.
 */

public class DeathEffectTest {

    @SuppressWarnings("unchecked")
    private EffectPool<BloodSplatter> deathEffectPoolMock = mock(EffectPool.class);

    private TextureEffect createDeathEffect() {

        Resources resourcesMock = ResourcesMock.create();

        return new BloodSplatter(deathEffectPoolMock, resourcesMock.getTexture(""));

    }

    /**
     * Tests that the DeathEffect is freed after finishing
     */
    @Test
    public void deathEffectTest1() {

        TextureEffect deathEffect = createDeathEffect();
        deathEffect.initialize(new LDVector2(20, 20));

        assertEquals(new LDVector2(20, 20), deathEffect.getPositionCenter());

        deathEffect.act(100f);
        // Call act twice so that the freeActor action is performed
        deathEffect.act(0.001f);
        verify(deathEffectPoolMock, times(1)).free(deathEffect);
    }
}

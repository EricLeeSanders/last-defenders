package game.model.actor.effects.texture.animation.death;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.foxholedefense.game.model.actor.effects.label.WaveOverCoinEffect;
import com.foxholedefense.game.model.actor.effects.texture.animation.death.BloodSplatter;
import com.foxholedefense.game.model.actor.effects.texture.animation.death.DeathEffect;
import com.foxholedefense.game.service.factory.EffectFactory.DeathEffectPool;
import com.foxholedefense.game.service.factory.EffectFactory.LabelEffectPool;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by Eric on 5/20/2017.
 */

public class DeathEffectTest {

    private DeathEffectPool deathEffectPoolMock = mock(DeathEffectPool.class);

    @Before
    public void initDeathEffectTest() {
        Gdx.app = mock(Application.class);
    }

    public DeathEffect createDeathEffect(){

        Resources resourcesMock = TestUtil.createResourcesMock();

        DeathEffect deathEffect = new BloodSplatter(deathEffectPoolMock, resourcesMock.getAtlasRegion(""));

        return deathEffect;

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

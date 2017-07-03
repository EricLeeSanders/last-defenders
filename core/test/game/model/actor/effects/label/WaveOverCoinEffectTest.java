package game.model.actor.effects.label;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.foxholedefense.game.model.actor.effects.label.WaveOverCoinEffect;
import com.foxholedefense.game.service.factory.EffectFactory.LabelEffectPool;
import com.foxholedefense.util.Resources;

import org.junit.Before;
import org.junit.Test;

import testutil.TestUtil;


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


public class WaveOverCoinEffectTest {

    @SuppressWarnings("unchecked")
    private LabelEffectPool<WaveOverCoinEffect> labelEffectPoolMock = mock(LabelEffectPool.class);

    @Before
    public void initWaveOverCoinEffectTest() {
        Gdx.app = mock(Application.class);
    }

    private WaveOverCoinEffect createWaveOverCoinEffect() {

        Resources resourcesMock = TestUtil.createResourcesMock();
        Skin skinMock = mock(Skin.class);

        BitmapFont bitmapFontMock = mock(BitmapFont.class);
        LabelStyle style = new LabelStyle(bitmapFontMock, Color.WHITE);
        doReturn(style).when(skinMock).get(LabelStyle.class);

        WaveOverCoinEffect waveOverCoinEffect = new WaveOverCoinEffect(labelEffectPoolMock, skinMock, resourcesMock.getAtlasRegion(""));
        waveOverCoinEffect = spy(waveOverCoinEffect);
        doNothing().when(waveOverCoinEffect).pack();

        return waveOverCoinEffect;
    }

    /**
     * Tests that the WaveOverCoinEffect correctly moves along the Y axis
     * and is freed after it finishes.
     */
    @Test
    public void waveOverCoinEffectTest1() {

        WaveOverCoinEffect waveOverCoinEffect = createWaveOverCoinEffect();
        waveOverCoinEffect.initialize(500);

        assertEquals(WaveOverCoinEffect.Y_BEGIN_OFFSET, waveOverCoinEffect.getY(), TestUtil.DELTA);
        assertEquals(1, waveOverCoinEffect.getActions().size);

        waveOverCoinEffect.act(WaveOverCoinEffect.DURATION / 2);

        assertEquals(WaveOverCoinEffect.Y_BEGIN_OFFSET + WaveOverCoinEffect.Y_END_OFFSET / 2, waveOverCoinEffect.getY(), TestUtil.DELTA);

        // Finish it
        waveOverCoinEffect.act(50f);

        verify(labelEffectPoolMock, times(1)).free(waveOverCoinEffect);
    }

}

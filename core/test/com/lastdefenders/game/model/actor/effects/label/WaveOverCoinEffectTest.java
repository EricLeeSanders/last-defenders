package com.lastdefenders.game.model.actor.effects.label;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.lastdefenders.game.service.factory.EffectFactory.EffectPool;
import com.lastdefenders.util.Resources;
import org.junit.jupiter.api.Test;
import testutil.ResourcesMock;
import testutil.TestUtil;

/**
 * Created by Eric on 5/20/2017.
 */


public class WaveOverCoinEffectTest {

    @SuppressWarnings("unchecked")
    private EffectPool<WaveOverCoinEffect> labelEffectPoolMock = mock(EffectPool.class);

    private WaveOverCoinEffect createWaveOverCoinEffect() {

        Resources resourcesMock = ResourcesMock.create();
        Skin skinMock = mock(Skin.class);

        BitmapFont bitmapFontMock = mock(BitmapFont.class);
        LabelStyle style = new LabelStyle(bitmapFontMock, Color.WHITE);
        doReturn(style).when(skinMock).get(LabelStyle.class);

        WaveOverCoinEffect waveOverCoinEffect = new WaveOverCoinEffect(labelEffectPoolMock,
            skinMock, resourcesMock.getAtlasRegion(""), 1);
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
        TestUtil.mockViewportWorldWidth(500, waveOverCoinEffect);
        waveOverCoinEffect.initialize(500);

        assertEquals(WaveOverCoinEffect.Y_BEGIN_OFFSET, waveOverCoinEffect.getY(), TestUtil.DELTA);
        assertEquals(1, waveOverCoinEffect.getActions().size);

        waveOverCoinEffect.act(WaveOverCoinEffect.DURATION / 2);

        assertEquals(WaveOverCoinEffect.Y_BEGIN_OFFSET + WaveOverCoinEffect.Y_END_OFFSET / 2,
            waveOverCoinEffect.getY(), TestUtil.DELTA);

        // Finish it
        waveOverCoinEffect.act(50f);
        // Call a second time so that the FreeActorAction is called
        waveOverCoinEffect.act(0.0001f);

        verify(labelEffectPoolMock, times(1)).free(waveOverCoinEffect);
    }

}

package game.model.actor.effects.label;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.model.actor.effects.label.ArmorDestroyedEffect;
import com.lastdefenders.game.service.factory.EffectFactory.EffectPool;
import com.lastdefenders.util.Resources;
import org.junit.Before;
import org.junit.Test;
import testutil.TestUtil;

/**
 * Created by Eric on 5/20/2017.
 */

public class ArmorDestroyedEffectTest {

    @SuppressWarnings("unchecked")
    private EffectPool<ArmorDestroyedEffect> labelEffectPoolMock = mock(EffectPool.class);

    @Before
    public void initArmorDestroyedEffectTest() {

        Gdx.app = mock(Application.class);
    }

    private ArmorDestroyedEffect createArmorDestroyedEffect() {

        Resources resourcesMock = TestUtil.createResourcesMock();
        Skin skinMock = mock(Skin.class);

        BitmapFont bitmapFontMock = mock(BitmapFont.class);
        LabelStyle style = new LabelStyle(bitmapFontMock, Color.WHITE);
        doReturn(style).when(skinMock).get(LabelStyle.class);

        return new ArmorDestroyedEffect(resourcesMock.getAtlasRegion(""), labelEffectPoolMock,
            skinMock, 1);

    }

    /**
     * Tests that the ArmorDestroyedEffect correctly moves along the Y axis
     * and is freed after it finishes.
     */
    @Test
    public void armorDestroyedEffectTest1() {

        Tower tower = TestUtil.createTower("Sniper", false);
        tower.setPositionCenter(150, 150);

        ArmorDestroyedEffect armorDestroyedEffect = createArmorDestroyedEffect();
        armorDestroyedEffect.initialize(tower);

        assertEquals(150, armorDestroyedEffect.getY(), TestUtil.DELTA);
        assertEquals(1, armorDestroyedEffect.getActions().size);

        tower.setPositionCenter(165, 165); // Make sure moving the tower has no effect
        armorDestroyedEffect.act(ArmorDestroyedEffect.DURATION / 2);
        tower.setPositionCenter(175, 175); // Make sure moving the tower has no effect

        assertEquals(150 + ArmorDestroyedEffect.Y_END_OFFSET / 2, armorDestroyedEffect.getY(),
            TestUtil.DELTA);

        // Finish it
        armorDestroyedEffect.act(50f);

        verify(labelEffectPoolMock, times(1)).free(armorDestroyedEffect);

    }

    /**
     * Tests that the ArmorDestroyedEffect is freed when the actor dies
     */
    @Test
    public void armorDestroyedEffectTest2() {

        Tower tower = TestUtil.createTower("FlameThrower", false);
        tower.setPositionCenter(150, 150);

        ArmorDestroyedEffect armorDestroyedEffect = createArmorDestroyedEffect();
        armorDestroyedEffect.initialize(tower);

        assertEquals(1, armorDestroyedEffect.getActions().size);

        armorDestroyedEffect.act(0.001f);

        tower.setDead(true);

        armorDestroyedEffect.act(0.001f);

        verify(labelEffectPoolMock, times(1)).free(armorDestroyedEffect);

    }
}

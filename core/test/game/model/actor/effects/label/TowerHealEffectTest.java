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
import com.lastdefenders.game.model.actor.effects.label.TowerHealEffect;
import com.lastdefenders.game.service.factory.EffectFactory.LabelEffectPool;
import org.junit.Before;
import org.junit.Test;
import testutil.TestUtil;

/**
 * Created by Eric on 5/20/2017.
 */


public class TowerHealEffectTest {

    @SuppressWarnings("unchecked")
    private LabelEffectPool<TowerHealEffect> labelEffectPoolMock = mock(LabelEffectPool.class);

    @Before
    public void initTowerHealEffectTest() {

        Gdx.app = mock(Application.class);
    }

    private TowerHealEffect createTowerHealEffect() {

        Skin skinMock = mock(Skin.class);

        BitmapFont bitmapFontMock = mock(BitmapFont.class);
        LabelStyle style = new LabelStyle(bitmapFontMock, Color.WHITE);
        doReturn(style).when(skinMock).get(LabelStyle.class);

        return new TowerHealEffect(labelEffectPoolMock, skinMock);
    }

    /**
     * Tests that the TowerHealEffect correctly moves along the Y axis
     * and is freed after it finishes.
     */
    @Test
    public void towerHealEffectTest1() {

        Tower tower = TestUtil.createTower("Sniper", false);
        tower.setPositionCenter(150, 150);

        TowerHealEffect towerHealEffect = createTowerHealEffect();
        towerHealEffect.initialize(tower);

        assertEquals(150, towerHealEffect.getY(), TestUtil.DELTA);
        assertEquals(1, towerHealEffect.getActions().size);

        tower.setPositionCenter(165, 165); // Make sure moving the tower has no effect
        towerHealEffect.act(TowerHealEffect.DURATION / 2);
        tower.setPositionCenter(175, 175); // Make sure moving the tower has no effect

        assertEquals(150 + TowerHealEffect.Y_END_OFFSET / 2, towerHealEffect.getY(),
            TestUtil.DELTA);

        // Finish it
        towerHealEffect.act(50f);

        verify(labelEffectPoolMock, times(1)).free(towerHealEffect);
    }

    /**
     * Tests that the TowerHealEffect is freed when the actor dies
     */
    @Test
    public void towerHealEffectTest2() {

        Tower tower = TestUtil.createTower("Sniper", false);
        tower.setPositionCenter(150, 150);

        TowerHealEffect towerHealEffect = createTowerHealEffect();
        towerHealEffect.initialize(tower);

        assertEquals(1, towerHealEffect.getActions().size);

        towerHealEffect.act(0.001f);

        tower.setDead(true);

        towerHealEffect.act(0.001f);

        verify(labelEffectPoolMock, times(1)).free(towerHealEffect);
    }
}

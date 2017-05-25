package game.model.actor.health;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;
import com.foxholedefense.game.model.actor.combat.tower.Tower;
import com.foxholedefense.game.model.actor.health.ArmorIcon;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.Resources;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import util.TestUtil;


import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by Eric on 5/20/2017.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({Logger.class})
public class ArmorIconTest {

    private Batch batchMock = mock(Batch.class);
    @SuppressWarnings("rawtypes")
    private Pool poolMock = mock(Pool.class);

    public ArmorIcon createArmorIcon(){

        TextureRegion iconMock = mock(TextureRegion.class);
        doReturn(20).when(iconMock).getRegionWidth();
        doReturn(20).when(iconMock).getRegionHeight();

        @SuppressWarnings( "unchecked" )
        ArmorIcon armorIcon = new ArmorIcon(poolMock, iconMock);

        return armorIcon;
    }

    @Before
    public void initArmorIconTest() {
        PowerMockito.mockStatic(Logger.class);
    }

    /**
     * Test that the ArmorIcon is placed in the correct position
     * when the actor takes damage and that it is freed when the
     * actor is killed.
     */
    @Test
    @SuppressWarnings( "unchecked" )
    public void armorIconTest1(){

        ArmorIcon armorIcon = createArmorIcon();
        Tower tower = TestUtil.createTower("Rifle", false);
        tower.setHasArmor(true);
        tower.setPositionCenter(20,20);

        armorIcon.setActor(tower);

        armorIcon.draw(batchMock, 1);

        assertEquals(tower.getPositionCenter().y + ArmorIcon.Y_OFFSET, armorIcon.getY(), TestUtil.DELTA);
        assertEquals(tower.getPositionCenter().x + ArmorIcon.X_OFFSET, armorIcon.getX(), TestUtil.DELTA);

        tower.takeDamage(1);
        tower.setPositionCenter(50,50);
        armorIcon.draw(batchMock, 1);

        assertEquals(tower.getPositionCenter().y + ArmorIcon.Y_OFFSET, armorIcon.getY(), TestUtil.DELTA);
        assertEquals(tower.getPositionCenter().x + ArmorIcon.X_HEALTH_BAR_DISPALYING_OFFSET, armorIcon.getX(), TestUtil.DELTA);

        tower.setDead(true);
        armorIcon.act(0.001f);

        verify(poolMock, times(1)).free(armorIcon);


    }
}
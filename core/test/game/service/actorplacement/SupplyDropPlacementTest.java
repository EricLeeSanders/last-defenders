package game.service.actorplacement;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.foxholedefense.game.model.actor.support.SupplyDrop;
import com.foxholedefense.game.model.actor.support.SupplyDropCrate;
import com.foxholedefense.game.service.actorplacement.SupplyDropPlacement;
import com.foxholedefense.game.service.factory.SupportActorFactory;
import com.foxholedefense.util.datastructures.pool.FHDVector2;

import org.junit.Before;
import org.junit.Test;


import game.model.actor.support.SupplyDropCrateTest;
import util.TestUtil;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by Eric on 5/28/2017.
 */
public class SupplyDropPlacementTest {

    private SupplyDropCrateTest supplyDropCrateTest = new SupplyDropCrateTest();
    private SupportActorFactory supportActorFactory = mock(SupportActorFactory.class);

    @Before
    public void initSupplyDropPlacementTest() {
        Gdx.app = mock(Application.class);
    }

    @Test
    public void supplyDropPlacementTest1(){

        SupplyDropPlacement supplyDropPlacement = new SupplyDropPlacement(supportActorFactory);
        SupplyDropCrate supplyDropCrate = supplyDropCrateTest.createSupplyDropCrate(new Group());

        doReturn(supplyDropCrate).when(supportActorFactory).loadSupplyDropCrate();

        supplyDropPlacement.createSupplyDrop();

        assertFalse(supplyDropCrate.isVisible());
        assertFalse(supplyDropCrate.isActive());
        assertFalse(supplyDropCrate.isShowRange());
        assertEquals(0, supplyDropCrate.getX(), TestUtil.DELTA);
        assertEquals(0, supplyDropCrate.getY(), TestUtil.DELTA);

        // set location
        FHDVector2 location = new FHDVector2(10,200);
        supplyDropPlacement.setLocation(location);

        assertTrue(supplyDropCrate.isVisible());
        assertTrue(supplyDropCrate.isShowRange());
        assertEquals(location, supplyDropCrate.getPositionCenter());

        // finish
        SupplyDrop supplyDropMock = mock(SupplyDrop.class);
        doReturn(supplyDropMock).when(supportActorFactory).loadSupplyDrop();
        doNothing().when(supplyDropMock).beginSupplyDrop(isA(FHDVector2.class));

        supplyDropPlacement.placeSupplyDrop();

        assertFalse(supplyDropCrate.isShowRange());
        verify(supplyDropMock, times(1)).beginSupplyDrop(isA(FHDVector2.class));

        assertNull(supplyDropPlacement.getCurrentSupplyDropCrate());

    }
}

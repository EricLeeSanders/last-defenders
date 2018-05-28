package com.lastdefenders.game.service.actorplacement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.lastdefenders.game.model.actor.support.SupplyDrop;
import com.lastdefenders.game.model.actor.support.SupplyDropCrate;
import com.lastdefenders.game.service.factory.SupportActorFactory;
import com.lastdefenders.util.datastructures.pool.LDVector2;
import com.lastdefenders.game.model.actor.support.SupplyDropCrateTest;
import org.junit.Before;
import org.junit.Test;
import testutil.TestUtil;

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
    public void supplyDropPlacementTest1() {

        SupplyDropPlacement supplyDropPlacement = new SupplyDropPlacement(supportActorFactory);
        SupplyDropCrate supplyDropCrate = supplyDropCrateTest.createSupplyDropCrate(new Group());

        doReturn(supplyDropCrate).when(supportActorFactory).loadSupportActor(
            eq(SupplyDropCrate.class), eq(true));

        supplyDropPlacement.createSupplyDrop();

        assertFalse(supplyDropCrate.isVisible());
        assertFalse(supplyDropCrate.isActive());
        assertFalse(supplyDropCrate.isShowRange());
        assertEquals(0, supplyDropCrate.getX(), TestUtil.DELTA);
        assertEquals(0, supplyDropCrate.getY(), TestUtil.DELTA);

        // set location
        LDVector2 location = new LDVector2(10, 200);
        supplyDropPlacement.setLocation(location);

        assertTrue(supplyDropCrate.isVisible());
        assertTrue(supplyDropCrate.isShowRange());
        assertEquals(location, supplyDropCrate.getPositionCenter());

        // finish
        SupplyDrop supplyDropMock = mock(SupplyDrop.class);
        doReturn(supplyDropMock).when(supportActorFactory).loadSupportActor(
            eq(SupplyDrop.class), eq(true));
        doNothing().when(supplyDropMock).beginSupplyDrop(isA(LDVector2.class));

        supplyDropPlacement.placeSupplyDrop();

        assertFalse(supplyDropCrate.isShowRange());
        verify(supplyDropMock, times(1)).beginSupplyDrop(isA(LDVector2.class));

        assertNull(supplyDropPlacement.getCurrentSupplyDropCrate());

    }
}
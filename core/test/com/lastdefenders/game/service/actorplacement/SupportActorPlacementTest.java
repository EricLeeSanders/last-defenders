package com.lastdefenders.game.service.actorplacement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.doReturn;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.lastdefenders.game.model.actor.ActorGroups;
import com.lastdefenders.game.model.actor.support.Apache;
import com.lastdefenders.game.model.actor.support.ApacheTest;
import com.lastdefenders.game.model.actor.support.LandMine;
import com.lastdefenders.game.model.actor.support.LandMineTest;
import com.lastdefenders.game.service.factory.SupportActorFactory;
import com.lastdefenders.util.datastructures.pool.LDVector2;
import org.junit.Before;
import org.junit.Test;
import testutil.TestUtil;

/**
 * Created by Eric on 5/28/2017.
 */
public class SupportActorPlacementTest {

    private ActorGroups actorGroups = mock(ActorGroups.class);
    private SupportActorFactory supportActorFactory = mock(SupportActorFactory.class);

    @Before
    public void initSupportActorPlacementTest() {

        Gdx.app = mock(Application.class);
    }

    @Test
    public void apachePlacementTest1() {

        SupportActorPlacement supportActorPlacement = new SupportActorPlacement(supportActorFactory);

        Group supportGroup = new Group();
        doReturn(supportGroup).when(actorGroups).getSupportGroup();

        ApacheTest apacheTest = new ApacheTest();
        Apache apache = apacheTest.createApache();

        doReturn(apache).when(supportActorFactory).loadSupportActor(eq(Apache.class), eq(true));

        supportActorPlacement.createSupportActor(Apache.class);

        assertEquals(0, apache.getX(), TestUtil.DELTA);
        assertEquals(0, apache.getY(), TestUtil.DELTA);
        assertFalse(apache.isVisible());
        assertFalse(apache.isActive());
        assertFalse(apache.isShowRange());

        // Move apache
        LDVector2 moveCoords = new LDVector2(400, 200);
        supportActorPlacement.moveSupportActor(moveCoords);

        assertTrue(apache.isVisible());
        assertTrue(apache.isShowRange());
        assertEquals(moveCoords, apache.getPositionCenter());

        // Place apache
        supportActorPlacement.placeSupportActor();
        assertTrue(apache.isActive());
        assertFalse(apache.isShowRange());
        assertNull(supportActorPlacement.getCurrentSupportActor());

    }

    @Test
    public void landMinePlacementTest() {

        SupportActorPlacement supportActorPlacement = new SupportActorPlacement(supportActorFactory);

        Group landmineGroup = new Group();
        doReturn(landmineGroup).when(actorGroups).getLandmineGroup();

        LandMineTest landMineTest = new LandMineTest();
        LandMine landMine = landMineTest.createLandMine();

        doReturn(landMine).when(supportActorFactory).loadSupportActor(eq(LandMine.class), eq(true));

        supportActorPlacement.createSupportActor(LandMine.class);

        assertEquals(0, landMine.getX(), TestUtil.DELTA);
        assertEquals(0, landMine.getY(), TestUtil.DELTA);
        assertFalse(landMine.isVisible());
        assertFalse(landMine.isActive());
        assertFalse(landMine.isShowRange());

        // Move landmine
        LDVector2 moveCoords = new LDVector2(400, 200);
        supportActorPlacement.moveSupportActor(moveCoords);

        assertTrue(landMine.isVisible());
        assertTrue(landMine.isShowRange());
        assertEquals(moveCoords, landMine.getPositionCenter());

        // Place landmine
        supportActorPlacement.placeSupportActor();
        assertTrue(landMine.isActive());
        assertFalse(landMine.isShowRange());
        assertNull(supportActorPlacement.getCurrentSupportActor());
    }
}

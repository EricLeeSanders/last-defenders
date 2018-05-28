package com.lastdefenders.game.service.actorplacement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doReturn;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.lastdefenders.game.model.actor.ActorGroups;
import com.lastdefenders.game.model.actor.support.AirStrike;
import com.lastdefenders.game.model.actor.support.AirStrikeLocation;
import com.lastdefenders.game.service.factory.SupportActorFactory;
import com.lastdefenders.util.datastructures.pool.LDVector2;
import com.lastdefenders.game.model.actor.support.AirStrikeTest;
import org.junit.Before;
import org.junit.Test;
import testutil.TestUtil;

/**
 * Created by Eric on 5/28/2017.
 */

public class AirStrikePlacementTest {

    private ActorGroups actorGroups = mock(ActorGroups.class);
    private SupportActorFactory supportActorFactory = mock(SupportActorFactory.class);
    private AirStrikeTest airStrikeTest = new AirStrikeTest();

    @Before
    public void initAirStrikePlacementTest() {

        Gdx.app = mock(Application.class);
    }

    @Test
    public void airStrikePlacementTest1() {

        AirStrikePlacement airStrikePlacement = new AirStrikePlacement(supportActorFactory);

        Group supportGroup = new Group();
        doReturn(supportGroup).when(actorGroups).getSupportGroup();

        AirStrike airStrike = airStrikeTest.createAirStrike();
        airStrike = spy(airStrike);
        doNothing().when(airStrike).beginAirStrike();

        // Create AirStrike
        doReturn(airStrike).when(supportActorFactory).loadSupportActor(eq(AirStrike.class), eq(true));

        airStrikePlacement.createAirStrike();

        assertFalse(airStrike.isActive());
        assertFalse(airStrike.isVisible());
        assertEquals(0, airStrike.getX(), TestUtil.DELTA);
        assertEquals(0, airStrike.getY(), TestUtil.DELTA);

        // Add locations
        LDVector2 location1 = new LDVector2(100, 100);
        LDVector2 location2 = new LDVector2(200, 100);
        LDVector2 location3 = new LDVector2(400, 100);

        AirStrikeLocation airStrikeLocMock = mock(AirStrikeLocation.class);

        doReturn(airStrikeLocMock).when(supportActorFactory).loadSupportActor(AirStrikeLocation.class, true);

        airStrikePlacement.addLocation(location1);

        // Try to finish airStrike and ensure that it does not begin because
        // all locations have not been added
        airStrikePlacement.finishCurrentAirStrike();
        verify(airStrike, never()).beginAirStrike();

        // Finish adding locations
        airStrikePlacement.addLocation(location2);
        airStrikePlacement.addLocation(location3);
        airStrikePlacement.finishCurrentAirStrike();
        verify(airStrike, times(1)).beginAirStrike();

    }
}

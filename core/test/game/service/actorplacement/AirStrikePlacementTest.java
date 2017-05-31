package game.service.actorplacement;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.foxholedefense.game.model.actor.ActorGroups;
import com.foxholedefense.game.model.actor.support.AirStrike;
import com.foxholedefense.game.model.actor.support.AirStrikeLocation;
import com.foxholedefense.game.service.actorplacement.AirStrikePlacement;
import com.foxholedefense.game.service.factory.SupportActorFactory;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.datastructures.pool.FHDVector2;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


import game.model.actor.support.AirStrikeTest;
import util.TestUtil;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.verifyPrivate;

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
    public void airStrikePlacementTest1(){

        AirStrikePlacement airStrikePlacement = new AirStrikePlacement(actorGroups, supportActorFactory);

        Group supportGroup = new Group();
        doReturn(supportGroup).when(actorGroups).getSupportGroup();

        AirStrike airStrike = airStrikeTest.createAirStrike();
        airStrike = spy(airStrike);
        doNothing().when(airStrike).beginAirStrike();

        // Create AirStrike
        doReturn(airStrike).when(supportActorFactory).loadSupportActor(eq("AirStrike"));

        airStrikePlacement.createAirStrike();

        assertFalse(airStrike.isActive());
        assertFalse(airStrike.isVisible());
        assertEquals(0, airStrike.getX(), TestUtil.DELTA);
        assertEquals(0, airStrike.getY(), TestUtil.DELTA);

        // Add locations
        FHDVector2 location1 = new FHDVector2(100,100);
        FHDVector2 location2 = new FHDVector2(200,100);
        FHDVector2 location3 = new FHDVector2(400,100);

        AirStrikeLocation airStrikeLocMock = mock(AirStrikeLocation.class);

        doReturn(airStrikeLocMock).when(supportActorFactory).loadAirStrikeLocation(isA(FHDVector2.class), isA(Float.class));

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

        assertNull(airStrikePlacement.getCurrentAirStrike());

    }
}

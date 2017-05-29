package game.service.actorplacement;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.foxholedefense.game.model.actor.ActorGroups;
import com.foxholedefense.game.model.actor.support.Apache;
import com.foxholedefense.game.model.actor.support.LandMine;
import com.foxholedefense.game.service.actorplacement.SupportActorPlacement;
import com.foxholedefense.game.service.factory.SupportActorFactory;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.datastructures.pool.FHDVector2;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


import game.model.actor.support.ApacheTest;
import game.model.actor.support.LandMineTest;
import util.TestUtil;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.doReturn;

/**
 * Created by Eric on 5/28/2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Logger.class})
public class SupportActorPlacementTest {
    private ActorGroups actorGroups = mock(ActorGroups.class);
    private SupportActorFactory supportActorFactory = mock(SupportActorFactory.class);

    @Before
    public void initSupportActorPlacementTest() {
        PowerMockito.mockStatic(Logger.class);
    }

    @Test
    public void apachePlacementTest1(){

        SupportActorPlacement supportActorPlacement = new SupportActorPlacement(actorGroups, supportActorFactory);

        Group supportGroup = new Group();
        doReturn(supportGroup).when(actorGroups).getSupportGroup();

        ApacheTest apacheTest = new ApacheTest();
        Apache apache = apacheTest.createApache();

        doReturn(apache).when(supportActorFactory).loadSupportActor(eq("Apache"));

        supportActorPlacement.createSupportActor("Apache");

        assertEquals(0, apache.getX(), TestUtil.DELTA);
        assertEquals(0, apache.getY(), TestUtil.DELTA);
        assertFalse(apache.isVisible());
        assertFalse(apache.isActive());
        assertFalse(apache.isShowRange());

        // Move apache
        FHDVector2 moveCoords = new FHDVector2(400, 200);
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
        SupportActorPlacement supportActorPlacement = new SupportActorPlacement(actorGroups, supportActorFactory);

        Group landmineGroup = new Group();
        doReturn(landmineGroup).when(actorGroups).getLandmineGroup();

        LandMineTest landMineTest = new LandMineTest();
        LandMine landMine = landMineTest.createLandMine();

        doReturn(landMine).when(supportActorFactory).loadSupportActor(eq("LandMine"));

        supportActorPlacement.createSupportActor("LandMine");

        assertEquals(0, landMine.getX(), TestUtil.DELTA);
        assertEquals(0, landMine.getY(), TestUtil.DELTA);
        assertFalse(landMine.isVisible());
        assertFalse(landMine.isActive());
        assertFalse(landMine.isShowRange());

        // Move landmine
        FHDVector2 moveCoords = new FHDVector2(400, 200);
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

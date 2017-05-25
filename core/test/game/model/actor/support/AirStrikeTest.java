package game.model.actor.support;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Pool;
import com.foxholedefense.game.helper.Damage;
import com.foxholedefense.game.model.actor.interfaces.IAttacker;
import com.foxholedefense.game.model.actor.projectile.Rocket;
import com.foxholedefense.game.model.actor.support.AirStrike;
import com.foxholedefense.game.model.actor.support.AirStrikeLocation;
import com.foxholedefense.game.service.factory.ProjectileFactory;
import com.foxholedefense.game.service.factory.SupportActorFactory.SupportActorPool;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.Resources;
import com.foxholedefense.util.datastructures.Dimension;
import com.foxholedefense.util.datastructures.pool.FHDVector2;
import com.foxholedefense.util.datastructures.pool.UtilPool;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


import util.TestUtil;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by Eric on 5/22/2017.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({Logger.class})
public class AirStrikeTest {

    private SupportActorPool poolMock = mock(SupportActorPool.class);
    private Rocket rocketMock = mock(Rocket.class);

    @Before
    public void initAirStrikeTest() {
        PowerMockito.mockStatic(Logger.class);
    }

    public AirStrike createAirStrike(){

        ProjectileFactory projectileFactoryMock = mock(ProjectileFactory.class);
        doReturn(rocketMock).when(projectileFactoryMock).loadRocket();

        Resources resourcesMock = TestUtil.createResourcesMock();
        FHDAudio audioMock = mock(FHDAudio.class);

        AirStrike airStrike = new AirStrike(poolMock, new Group(), projectileFactoryMock, resourcesMock.getTexture(""), resourcesMock.getTexture(""), audioMock);

        return airStrike;

    }

    @Test
    public void airStrikeTest1(){
        AirStrike airStrike = createAirStrike();
        AirStrikeLocation airStrikeLocation1 = mock(AirStrikeLocation.class);
        AirStrikeLocation airStrikeLocation2 = mock(AirStrikeLocation.class);
        AirStrikeLocation airStrikeLocation3 = mock(AirStrikeLocation.class);

        assertFalse(airStrike.readyToBegin());
        airStrike.addLocation(airStrikeLocation1);
        airStrike.addLocation(airStrikeLocation2);
        assertFalse(airStrike.readyToBegin());
        airStrike.addLocation(airStrikeLocation3);
        assertTrue(airStrike.readyToBegin());

        assertFalse(airStrike.isActive());

        airStrike.beginAirStrike();

        assertTrue(airStrike.isActive());
        assertEquals(new FHDVector2(-airStrike.getWidth() / 2, Resources.VIRTUAL_HEIGHT/2), airStrike.getPositionCenter());

        verify(airStrikeLocation1, times(1)).setShowRange(eq(false));
        verify(airStrikeLocation2, times(1)).setShowRange(eq(false));
        verify(airStrikeLocation3, times(1)).setShowRange(eq(false));
        verify(rocketMock, times(3)).initialize(any(IAttacker.class), any(FHDVector2.class), any(Dimension.class), any(Float.class));

        airStrike.act(AirStrike.AIRSTRIKE_DURATION / 2);
        verify(poolMock, never()).free(airStrike);
        airStrike.act(AirStrike.AIRSTRIKE_DURATION / 2);
        verify(poolMock, times(1)).free(airStrike);



    }

}

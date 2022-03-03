package com.lastdefenders.game.model.actor.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.lastdefenders.game.model.actor.groups.EnemyGroup;
import com.lastdefenders.game.model.actor.interfaces.Attacker;
import com.lastdefenders.game.model.actor.projectile.Rocket;
import com.lastdefenders.game.model.actor.support.AirStrike.AirStrikeLocation;
import com.lastdefenders.game.service.factory.ProjectileFactory;
import com.lastdefenders.game.service.factory.SupportActorFactory.SupportActorPool;
import com.lastdefenders.sound.LDAudio;
import com.lastdefenders.util.Resources;
import com.lastdefenders.util.datastructures.Dimension;
import com.lastdefenders.util.datastructures.pool.LDVector2;
import org.junit.Before;
import org.junit.Test;
import testutil.TestUtil;

/**
 * Created by Eric on 5/22/2017.
 */

public class AirStrikeTest {

    @SuppressWarnings("unchecked")
    private SupportActorPool<AirStrike> poolMock = mock(SupportActorPool.class);
    private Rocket rocketMock = mock(Rocket.class);
    private Resources resourcesMock = TestUtil.createResourcesMock();

    @Before
    public void initAirStrikeTest() {

        Gdx.app = mock(Application.class);
    }

    public AirStrike createAirStrike() {

        ProjectileFactory projectileFactoryMock = mock(ProjectileFactory.class);
        doReturn(rocketMock).when(projectileFactoryMock).loadProjectile(Rocket.class);


        LDAudio audioMock = mock(LDAudio.class);
        Array<AirStrikeLocation> locations = getAirStrikeLocations(AirStrike.MAX_AIRSTRIKES);
        AirStrike airStrike =  new AirStrike(poolMock, new EnemyGroup(), projectileFactoryMock,
            resourcesMock.getTexture(""), resourcesMock.getTexture(""),locations, audioMock);

        AirStrike airStrikeSpy = spy(airStrike);
        doReturn(new Group()).when(airStrikeSpy).getParent();

        return airStrikeSpy;
    }

    private Array<AirStrikeLocation> getAirStrikeLocations(int count){
        Array<AirStrikeLocation> locations = new Array<>();
        for(int i = 0; i < count; i++){
            AirStrikeLocation location = new AirStrikeLocation(resourcesMock.getTexture(""));
            locations.add(location);
        }

        return locations;
    }

    @Test
    public void airStrikeTest1() {

        AirStrike airStrike = createAirStrike();
        LDVector2 airStrikeLocation1 = new LDVector2(100,100);
        LDVector2 airStrikeLocation2 = new LDVector2(200,200);
        LDVector2 airStrikeLocation3 = new LDVector2(300,300);

        airStrike.initialize();
        assertFalse(airStrike.isReadyToBegin());
        airStrike.setPlacement(airStrikeLocation1);
        airStrike.setPlacement(airStrikeLocation2);
        assertFalse(airStrike.isReadyToBegin());
        airStrike.setPlacement(airStrikeLocation3);
        assertTrue(airStrike.isReadyToBegin());

        assertFalse(airStrike.isActive());

        airStrike.ready();

        assertTrue(airStrike.isActive());
        assertEquals(new LDVector2(-airStrike.getWidth() / 2, Resources.VIRTUAL_HEIGHT / 2),
            airStrike.getPositionCenter());

        verify(rocketMock, times(3))
            .initialize(any(Attacker.class), any(LDVector2.class), any(Dimension.class),
                any(Float.class));

        airStrike.act(AirStrike.AIRSTRIKE_DURATION / 2);
        verify(poolMock, never()).free(airStrike);
        airStrike.act(AirStrike.AIRSTRIKE_DURATION / 2);
        // Act a second time so that the FreeActorAction is called
        airStrike.act(0.0001f);
        verify(poolMock, times(1)).free(airStrike);

    }
}

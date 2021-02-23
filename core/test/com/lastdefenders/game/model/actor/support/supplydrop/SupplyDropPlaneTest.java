package com.lastdefenders.game.model.actor.support.supplydrop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.lastdefenders.game.model.actor.support.supplydrop.SupplyDropPlane;
import com.lastdefenders.game.service.factory.SupportActorFactory;
import com.lastdefenders.game.service.factory.SupportActorFactory.SupportActorPool;
import com.lastdefenders.util.LDAudio;
import com.lastdefenders.util.Resources;
import org.junit.Before;
import org.junit.Test;
import testutil.TestUtil;

/**
 * Created by Eric on 5/23/2017.
 */

public class SupplyDropPlaneTest {

    private LDAudio audioMock = mock(LDAudio.class);

    @Before
    public void initSupplyDropPlaneTest() {

        Gdx.app = mock(Application.class);
    }


    private SupplyDropPlane createSupplyDropPlane() {

        Resources resources = TestUtil.createResourcesMock();
        return new SupplyDropPlane(resources.getTexture(""), audioMock);
    }

    @Test
    public void supplyDropTest1() {

        SupplyDropPlane plane = createSupplyDropPlane();
        Vector2 destination = new Vector2(100, 125);

        assertFalse(plane.isActive());

        plane.beginSupplyDrop(destination);

        assertTrue(plane.isActive());
        assertEquals(-plane.getWidth(), plane.getPositionCenter().x, TestUtil.DELTA);
        assertEquals(destination.y, plane.getPositionCenter().y, TestUtil.DELTA);
        assertEquals(1, plane.getActions().size);

        plane.act(10f);
        assertEquals(plane.getEndPositionByDestination(destination), plane.getPositionCenter());
        // act again so that the FreeActorAction is called
        plane.act(0.0001f);

        assertEquals(0, plane.getActions().size);

    }
}

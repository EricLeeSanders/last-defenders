package com.lastdefenders.game.model.actor.support.supplydrop;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.lastdefenders.sound.SoundPlayer;
import com.lastdefenders.util.Resources;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import testutil.ResourcesMock;
import testutil.TestUtil;

/**
 * Created by Eric on 5/23/2017.
 */

public class SupplyDropPlaneTest {

    private SoundPlayer soundPlayerMock = mock(SoundPlayer.class);

    @BeforeAll
    public static void init() {

        Gdx.app = mock(Application.class);
    }
    private SupplyDropPlane createSupplyDropPlane() {

        Resources resources = ResourcesMock.create();
        return new SupplyDropPlane(resources.getTexture(""), soundPlayerMock);
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

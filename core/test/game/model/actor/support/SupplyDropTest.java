package game.model.actor.support;

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
import com.lastdefenders.game.model.actor.support.SupplyDrop;
import com.lastdefenders.game.model.actor.support.SupplyDropCrate;
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

public class SupplyDropTest {

    @SuppressWarnings("unchecked")
    private SupportActorPool<SupplyDrop> poolMock = mock(SupportActorPool.class);
    private SupplyDropCrate supplyDropCrateMock = mock(SupplyDropCrate.class);
    private LDAudio audioMock = mock(LDAudio.class);

    @Before
    public void initSupplyDropTest() {

        Gdx.app = mock(Application.class);
    }

    private SupplyDrop createSupplyDrop() {

        SupportActorFactory supportActorFactoryMock = mock(SupportActorFactory.class);
        doReturn(supplyDropCrateMock).when(supportActorFactoryMock).loadSupportActor(
            eq(SupplyDropCrate.class), eq(true));
        doReturn(supplyDropCrateMock).when(supplyDropCrateMock)
            .beginDrop(isA(Float.class), isA(Vector2.class));
        Resources resources = TestUtil.createResourcesMock();

        return new SupplyDrop(resources.getTexture(""), poolMock, supportActorFactoryMock, audioMock);
    }

    @Test
    public void supplyDropTest1() {

        SupplyDrop supplyDrop = createSupplyDrop();
        Vector2 destination = new Vector2(100, 125);

        assertFalse(supplyDrop.isActive());

        supplyDrop.beginSupplyDrop(destination);

        assertTrue(supplyDrop.isActive());
        assertEquals(-supplyDrop.getWidth(), supplyDrop.getPositionCenter().x, TestUtil.DELTA);
        assertEquals(destination.y, supplyDrop.getPositionCenter().y, TestUtil.DELTA);
        assertEquals(1, supplyDrop.getActions().size);
        verify(supplyDropCrateMock, times(1)).beginDrop(isA(Float.class), eq(destination));

        supplyDrop.act(10f);
        // act again so that the FreeActorAction is called
        supplyDrop.act(0.0001f);

        assertEquals(0, supplyDrop.getActions().size);
        verify(poolMock, times(1)).free(supplyDrop);

    }
}

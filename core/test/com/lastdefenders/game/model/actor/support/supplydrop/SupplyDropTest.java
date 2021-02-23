package com.lastdefenders.game.model.actor.support.supplydrop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.model.actor.combat.tower.TowerRifle;
import com.lastdefenders.game.model.actor.combat.tower.TowerRocketLauncher;
import com.lastdefenders.game.model.actor.combat.tower.TowerTank;
import com.lastdefenders.game.model.actor.effects.label.TowerHealEffect;
import com.lastdefenders.game.model.actor.groups.TowerGroup;
import com.lastdefenders.game.service.factory.EffectFactory;
import com.lastdefenders.game.service.factory.SupportActorFactory.SupportActorPool;
import com.lastdefenders.util.LDAudio;
import com.lastdefenders.util.Resources;
import com.lastdefenders.util.datastructures.pool.LDVector2;
import org.junit.Before;
import org.junit.Test;
import testutil.TestUtil;

/**
 * Created by Eric on 5/23/2017.
 */

public class SupplyDropTest {

    @SuppressWarnings("unchecked")
    private SupportActorPool<SupplyDrop> supplyDropPoolMock = mock(SupportActorPool.class);
    private TowerHealEffect towerHealEffectMock = mock(TowerHealEffect.class);
    private LDAudio audioMock = mock(LDAudio.class);
    private Resources resourcesMock = mock(Resources.class);

    @Before
    public void initSupplyDropCrateTest() {

        Gdx.app = mock(Application.class);
    }

    public SupplyDrop createSupplyDrop(TowerGroup towerGroup) {

        SupplyDropPlane plane = createSupplyDropPlane();

        return createSupplyDrop(towerGroup, plane);
    }

    public SupplyDrop createSupplyDrop(TowerGroup towerGroup, SupplyDropPlane plane) {
        EffectFactory effectFactoryMock = mock(EffectFactory.class);
        doReturn(towerHealEffectMock).when(effectFactoryMock)
            .loadEffect(eq(TowerHealEffect.class), isA(Boolean.class));

        SupplyDrop supplyDrop = new SupplyDrop(resourcesMock.getTexture(""), resourcesMock.getTexture(""),
            supplyDropPoolMock, towerGroup, effectFactoryMock, plane);

        SupplyDrop supplyDropSpy = spy(supplyDrop);

        doReturn(new Group()).when(supplyDropSpy).getParent();

        return supplyDropSpy;

    }

    public SupplyDropPlane createSupplyDropPlane(){
        SupplyDropPlane plane = new SupplyDropPlane(resourcesMock.getTexture(""), audioMock);
        plane = spy(plane);

        return plane;
    }

    @Test
    public void supplyDropTest1() {
        LDVector2 destination = new LDVector2(280, 360);

        Tower tower1 = TestUtil.createTower(TowerRifle.class, true, true);
        tower1.takeDamage(1);
        tower1.setPositionCenter(new Vector2(275, 355));

        Tower tower2 = TestUtil.createTower(TowerTank.class, true, true);
        tower2.takeDamage(3);
        tower2.setPositionCenter(new Vector2(284, 362));

        Tower tower3 = TestUtil.createTower(TowerRocketLauncher.class, true, true);
        tower3.setHasArmor(true);
        tower3.takeDamage(1);
        tower3.setPositionCenter(new Vector2(270, 345));

        TowerGroup towerGroup = new TowerGroup();
        towerGroup.addActor(tower1);
        towerGroup.addActor(tower2);
        towerGroup.addActor(tower3);

        SupplyDropPlane plane = createSupplyDropPlane();
        SupplyDrop supplyDrop = createSupplyDrop(towerGroup, plane);
        supplyDrop.initialize();

        assertFalse(supplyDrop.isActive());
        supplyDrop.setPlacement(destination);
        supplyDrop.ready();

        assertTrue(supplyDrop.isActive());
        assertFalse(supplyDrop.isVisible());
        verify(plane, times(1)).beginSupplyDrop(eq(destination));


        float dropDelay = supplyDrop.calculateDropDelay(destination);
        // Delay Action
        supplyDrop.act(dropDelay);
        assertEquals(1, supplyDrop.getActions().size);
        assertTrue(tower1.getHealthPercent() < 1);
        assertTrue(tower2.getHealthPercent() < 1);
        assertTrue(tower3.getArmorPercent() < 1);

        // Visible action
        supplyDrop.act(0.0001f);
        // Scale action
        supplyDrop.act(10f);
        // Heal actors action
        supplyDrop.act(0.0001f);
        // Free action
        supplyDrop.act(0.0001f);

        assertEquals(1, tower1.getHealthPercent(), TestUtil.DELTA);
        assertEquals(1, tower2.getHealthPercent(), TestUtil.DELTA);
        assertEquals(1, tower3.getHealthPercent(), TestUtil.DELTA);

        verify(towerHealEffectMock, times(1)).initialize(eq(tower1));
        verify(towerHealEffectMock, times(1)).initialize(eq(tower2));
        verify(towerHealEffectMock, times(1)).initialize(eq(tower3));

        verify(supplyDropPoolMock, times(1)).free(supplyDrop);

    }

}

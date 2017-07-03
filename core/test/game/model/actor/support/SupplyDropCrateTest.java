package game.model.actor.support;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.foxholedefense.game.model.actor.combat.tower.Tower;
import com.foxholedefense.game.model.actor.effects.label.TowerHealEffect;
import com.foxholedefense.game.model.actor.support.SupplyDropCrate;
import com.foxholedefense.game.service.factory.EffectFactory;
import com.foxholedefense.game.service.factory.SupportActorFactory.SupplyDropCratePool;
import com.foxholedefense.util.Resources;
import com.foxholedefense.util.datastructures.pool.FHDVector2;

import org.junit.Before;
import org.junit.Test;

import testutil.TestUtil;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by Eric on 5/23/2017.
 */

public class SupplyDropCrateTest {
    private SupplyDropCratePool supplyDropCratePoolMock = mock(SupplyDropCratePool.class);
    private TowerHealEffect towerHealEffectMock = mock(TowerHealEffect.class);

    @Before
    public void initSupplyDropCrateTest() {
        Gdx.app = mock(Application.class);
    }

    public SupplyDropCrate createSupplyDropCrate(Group towerGroup) {
        Resources resourcesMock = mock(Resources.class);
        EffectFactory effectFactoryMock = mock(EffectFactory.class);
        doReturn(towerHealEffectMock).when(effectFactoryMock).loadLabelEffect(eq(TowerHealEffect.class));

        return new SupplyDropCrate(resourcesMock.getTexture(""), resourcesMock.getTexture(""),
                supplyDropCratePoolMock, towerGroup, effectFactoryMock);
    }

    @Test
    public void supplyDropCrateTest1() {
        float dropDelay = .75f;
        FHDVector2 destination = new FHDVector2(280, 360);

        Tower tower1 = TestUtil.createTower("Rifle", true);
        tower1.takeDamage(1);
        tower1.setPositionCenter(new Vector2(275, 355));

        Tower tower2 = TestUtil.createTower("Tank", true);
        tower2.takeDamage(3);
        tower2.setPositionCenter(new Vector2(284, 362));

        Tower tower3 = TestUtil.createTower("RocketLauncher", true);
        tower3.setHasArmor(true);
        tower3.takeDamage(1);
        tower3.setPositionCenter(new Vector2(270, 345));

        Group towerGroup = new Group();
        towerGroup.addActor(tower1);
        towerGroup.addActor(tower2);
        towerGroup.addActor(tower3);

        SupplyDropCrate supplyDropCrate = createSupplyDropCrate(towerGroup);

        assertFalse(supplyDropCrate.isActive());

        supplyDropCrate.beginDrop(dropDelay, destination);

        assertTrue(supplyDropCrate.isActive());
        assertFalse(supplyDropCrate.isVisible());
        assertEquals(2, supplyDropCrate.getActions().size);
        assertEquals(destination, supplyDropCrate.getPositionCenter());

        supplyDropCrate.act(dropDelay);
        assertEquals(1, supplyDropCrate.getActions().size);
        assertTrue(tower1.getHealthPercent() < 1);
        assertTrue(tower2.getHealthPercent() < 1);
        assertTrue(tower3.getArmorPercent() < 1);

        supplyDropCrate.act(10f);
        assertEquals(0, supplyDropCrate.getActions().size);

        assertTrue(tower1.getHealthPercent() == 1);
        assertTrue(tower2.getHealthPercent() == 1);
        assertTrue(tower3.getArmorPercent() == 1);

        verify(towerHealEffectMock, times(1)).initialize(eq(tower1));
        verify(towerHealEffectMock, times(1)).initialize(eq(tower2));
        verify(towerHealEffectMock, times(1)).initialize(eq(tower3));

        verify(supplyDropCratePoolMock, times(1)).free(supplyDropCrate);

    }
}

package com.lastdefenders.game.model.actor.health;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.lastdefenders.game.model.actor.combat.CombatActor;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.model.actor.combat.tower.TowerRifle;
import org.junit.Before;
import org.junit.Test;
import testutil.TestUtil;

/**
 * Created by Eric on 5/20/2017.
 */

public class ArmorIconTest {

    private Batch batchMock = mock(Batch.class);

    @Before
    public void initArmorIconTest() {

        Gdx.app = mock(Application.class);
    }

    /**
     * Tower does not have Armor. Test that the armor Icon is not visible.
     */
    @Test
    public void armorIconTest1() {


        Tower tower = TestUtil.createTower(TowerRifle.class, false);

        ArmorIcon armorIcon = new ArmorIconTestUtil.ArmorIconBuilder().build();

        HealthBar healthBar = new HealthBarTestUtil.HealthBarBuilder()
            .setActor(tower).setArmorIcon(armorIcon).build();

        tower.setHasArmor(false);

        assertFalse(armorIcon.isVisible());


//        armorIcon.draw(batchMock, 1);

//        assertEquals(tower.getPositionCenter().y + HealthBar.Y_OFFSET, armorIcon.getY(),
//            TestUtil.DELTA);
//
//        tower.takeDamage(1);
//        tower.setPositionCenter(50, 50);
//        armorIcon.draw(batchMock, 1);
//
//        assertEquals(tower.getPositionCenter().y + ArmorIcon.Y_OFFSET, armorIcon.getY(),
//            TestUtil.DELTA);
//        assertEquals(tower.getPositionCenter().x + ArmorIcon.X_HEALTH_BAR_DISPLAYING_OFFSET,
//            armorIcon.getX(), TestUtil.DELTA);


    }

    /**
     * Tower does have Armor. Test that the armor Icon is visible.
     */
    @Test
    public void armorIconTest2() {

        Tower tower = TestUtil.createTower(TowerRifle.class, false);

        ArmorIcon armorIcon = new ArmorIconTestUtil.ArmorIconBuilder().build();

        HealthBar healthBar = new HealthBarTestUtil.HealthBarBuilder()
            .setActor(tower).setArmorIcon(armorIcon).build();

        tower.setHasArmor(true);

        healthBar.act(1f);
        healthBar.draw(batchMock, 1);

        assertTrue(armorIcon.isVisible());

    }

    /**
     * Test Armor Destroyed
     */
    @Test
    public void armorIconTest3() {

        Tower tower = TestUtil.createTower(TowerRifle.class, false);

        TextureRegion iconMock = mock(TextureRegion.class);
        TextureRegion destroyedIconMock = mock(TextureRegion.class);
        ArmorIcon armorIcon = new ArmorIconTestUtil.ArmorIconBuilder().setIcon(iconMock)
            .setDestroyedIcon(destroyedIconMock).build();

        HealthBar healthBar = new HealthBarTestUtil.HealthBarBuilder()
            .setActor(tower).setArmorIcon(armorIcon).build();

        tower.setHasArmor(true);

        assertTrue(armorIcon.isVisible());

        int armorAmount = tower.getArmorCost();
        tower.takeDamage(armorAmount);
        assertTrue(armorIcon.isVisible());
        healthBar.draw(batchMock, 1);

    }
}

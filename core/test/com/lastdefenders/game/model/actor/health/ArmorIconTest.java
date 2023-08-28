package com.lastdefenders.game.model.actor.health;

import static com.lastdefenders.game.model.actor.health.ArmorIcon.TEXTURE_PADDING_HEALTH_BAR_SHOWING;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Align;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.model.actor.combat.tower.TowerRifle;
import org.junit.jupiter.api.Test;
import testutil.TestUtil;

/**
 * Created by Eric on 5/20/2017.
 */

public class ArmorIconTest {

    private Batch batchMock = mock(Batch.class);

    /**
     * Tower has armor and takes no damage. Verify that the armor icon is visible and centered.
     */
    @Test
    public void armorIconTest1() {


        Tower tower = TestUtil.createTower(TowerRifle.class, false, false);
        tower.setHasArmor(false);

        ArmorIcon armorIcon = new ArmorIconTestUtil.ArmorIconBuilder().build();

        HealthBar healthBar = new HealthBarTestUtil.HealthBarBuilder()
            .setActor(tower).setArmorIcon(armorIcon).build();

        tower.ready();

        assertFalse(armorIcon.isVisible());

        tower.setHasArmor(true);

        assertTrue(armorIcon.isVisible());

        healthBar.act(1f);
        healthBar.draw(batchMock, 1);

        assertEquals(0, armorIcon.getX(Align.center), TestUtil.DELTA);
        assertEquals(0, armorIcon.getY(Align.center), TestUtil.DELTA);

    }

    /**
     * Tower has armor and takes damage. Verify that the armor icon is visible and positioned to the left.
     */
    @Test
    public void armorIconTest2() {

        Tower tower = TestUtil.createTower(TowerRifle.class, false, false);

        ArmorIcon armorIcon = new ArmorIconTestUtil.ArmorIconBuilder().build();

        HealthBar healthBar = new HealthBarTestUtil.HealthBarBuilder()
            .setActor(tower).setArmorIcon(armorIcon).build();

        tower.ready();
        tower.setHasArmor(true);

        assertTrue(armorIcon.isVisible());

        tower.takeDamage(tower.getArmor() / 2);

        // Act twice. Once so that the healthbar progressbar is made visible. And 2nd so that the
        // ArmorIcon is moved after the progressbar is made visible.
        healthBar.act(1f);
        healthBar.draw(batchMock, 1);
        healthBar.act(1f);
        healthBar.draw(batchMock, 1);

        assertTrue(healthBar.getProgressBar().isVisible());

        assertEquals(TEXTURE_PADDING_HEALTH_BAR_SHOWING.getWidth(), armorIcon.getX(Align.center), TestUtil.DELTA);
        assertEquals(0, armorIcon.getY(Align.center), TestUtil.DELTA);
    }

    /**
     * Test Armor Destroyed
     */
    @Test
    public void armorIconTest3() {

        Tower tower = TestUtil.createTower(TowerRifle.class, false, false);

        TextureRegion icon = mock(TextureRegion.class);
        TextureRegion destroyedIcon = mock(TextureRegion.class);
        ArmorIcon armorIcon = new ArmorIconTestUtil.ArmorIconBuilder()
            .setIcon(icon).setDestroyedIcon(destroyedIcon).build();

        HealthBar healthBar = new HealthBarTestUtil.HealthBarBuilder()
            .setActor(tower).setArmorIcon(armorIcon).build();

        tower.ready();
        tower.setHasArmor(true);

        assertTrue(armorIcon.isVisible());

        tower.takeDamage(tower.getArmor());

        assertEquals(destroyedIcon, armorIcon.getTextureRegion());
    }
}

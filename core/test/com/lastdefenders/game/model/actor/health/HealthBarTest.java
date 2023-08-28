package com.lastdefenders.game.model.actor.health;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.model.actor.combat.tower.TowerRifle;
import org.junit.jupiter.api.Test;
import testutil.TestUtil;

/**
 * Created by Eric on 5/20/2017.
 */
public class HealthBarTest {

    private Batch batchMock = mock(Batch.class);
    private TextureRegionDrawable greenMock = mock(TextureRegionDrawable.class);
    private TextureRegionDrawable orangeMock = mock(TextureRegionDrawable.class);
    private TextureRegionDrawable redMock = mock(TextureRegionDrawable.class);
    private TextureRegionDrawable grayMock = mock(TextureRegionDrawable.class);
    private TextureRegionDrawable unfilledMock = mock(TextureRegionDrawable.class);

    {
        HealthBarTestUtil.setupBarMocks(greenMock, orangeMock, redMock, grayMock, unfilledMock);
    }

    /**
     * Test that Green bar, Orange bar, and Red bar are displayed
     */
    @Test
    public void healthBarNoArmorTest1() {

        Tower tower = TestUtil.createTower(TowerRifle.class, true, false);
        tower.setPositionCenter(20, 20);


        HealthBar healthBar = new HealthBarTestUtil.HealthBarBuilder()
                                .setGreenBar(greenMock)
                                .setOrangeBar(orangeMock)
                                .setRedBar(redMock)
                                .setGrayBar(grayMock)
                                .setUnfilledBar(unfilledMock)
                                .setActor(tower).build();

        assertFalse(healthBar.isVisible());

        tower.ready();

        assertTrue(healthBar.isVisible());
        assertFalse(healthBar.getProgressBar().isVisible());

        // Show green bar
        doReturn(.9f).when(tower).getHealthPercent();
        healthBar.act(1f);
        healthBar.draw(batchMock, 1f);

        assertTrue(healthBar.getProgressBar().isVisible());

        // Show orange bar
        doReturn(.6f).when(tower).getHealthPercent();
        healthBar.act(1f);
        healthBar.draw(batchMock, 1f);

        // Show red bar
        doReturn(.2f).when(tower).getHealthPercent();
        healthBar.act(1f);
        healthBar.draw(batchMock, 1f);

        verify(batchMock, times(3))
            .draw(eq(unfilledMock.getRegion().getTexture()), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Integer.class), isA(Integer.class),
                isA(Integer.class), isA(Integer.class), isA(Boolean.class),
                isA(Boolean.class));

        verify(batchMock, times(1))
            .draw(eq(greenMock.getRegion()), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Float.class), isA(Float.class),
                isA(Float.class));

        verify(batchMock, times(1))
            .draw(eq(orangeMock.getRegion()), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Float.class), isA(Float.class),
                isA(Float.class));

        verify(batchMock, times(1))
            .draw(eq(redMock.getRegion()), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Float.class), isA(Float.class),
                isA(Float.class));

        verify(batchMock, never())
            .draw(eq(grayMock.getRegion()), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Float.class), isA(Float.class),
                isA(Float.class));

        // kill tower
        tower.takeDamage(1000);
        healthBar.act(1f);

        assertFalse(healthBar.isVisible());

    }

    /**
     * Healthbar with gray bar is displayed
     */
    @Test
    public void healthBarWithArmorTest1() {


        Tower tower = TestUtil.createTower(TowerRifle.class, true, false);
        tower.setHasArmor(true);
        tower.setPositionCenter(20, 20);


        HealthBar healthBar = new HealthBarTestUtil.HealthBarBuilder()
            .setGreenBar(greenMock)
            .setOrangeBar(orangeMock)
            .setRedBar(redMock)
            .setGrayBar(grayMock)
            .setUnfilledBar(unfilledMock)
            .setActor(tower).build();

        assertFalse(healthBar.isVisible());

        tower.ready();

        doReturn(.3f).when(tower).getArmorPercent();
        healthBar.act(1f);
        healthBar.draw(batchMock, 1f);

        assertTrue(healthBar.isVisible());

        verify(batchMock, times(1))
            .draw(eq(grayMock.getRegion()), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Float.class), isA(Float.class),
                isA(Float.class));

        verify(batchMock, times(1))
            .draw(eq(unfilledMock.getRegion().getTexture()), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Integer.class), isA(Integer.class),
                isA(Integer.class), isA(Integer.class), isA(Boolean.class),
                isA(Boolean.class));

        verify(batchMock, never())
            .draw(eq(greenMock.getRegion()), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Float.class), isA(Float.class),
                isA(Float.class));

        verify(batchMock, never())
            .draw(eq(orangeMock.getRegion()), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Float.class), isA(Float.class),
                isA(Float.class));

        verify(batchMock, never())
            .draw(eq(redMock.getRegion()), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Float.class), isA(Float.class),
                isA(Float.class), isA(Float.class), isA(Float.class),
                isA(Float.class));

        // kill tower
        tower.takeDamage(1000);
        healthBar.act(1f);

        assertFalse(healthBar.isVisible());

    }

}

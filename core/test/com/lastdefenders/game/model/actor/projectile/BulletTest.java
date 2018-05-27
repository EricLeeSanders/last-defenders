package com.lastdefenders.game.model.actor.projectile;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.lastdefenders.game.helper.Damage;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.model.actor.interfaces.Attacker;
import com.lastdefenders.game.model.actor.interfaces.Targetable;
import com.lastdefenders.game.model.actor.projectile.Bullet;
import com.lastdefenders.game.service.factory.ProjectileFactory.BulletPool;
import com.lastdefenders.util.datastructures.Dimension;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import testutil.TestUtil;

/**
 * Created by Eric on 5/21/2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Damage.class})
public class BulletTest {

    @SuppressWarnings("unchecked")
    private BulletPool poolMock = mock(BulletPool.class);

    @Before
    public void initBulletTest() {

        Gdx.app = mock(Application.class);
        PowerMockito.mockStatic(Damage.class);
    }

    private Bullet createBullet() {

        TextureRegion textureRegionMock = mock(TextureRegion.class);

        return new Bullet(poolMock, textureRegionMock);
    }

    // Bullet is initialized correctly and is freed when the action finishes
    @Test
    public void bulletTest1() {

        Tower attacker = TestUtil.createTower("Rifle", false);
        attacker.setPositionCenter(40, 40);
        Enemy target = TestUtil.createEnemy("Rifle", false);
        target.setPositionCenter(140, 140);

        Bullet bullet = createBullet();

        bullet.initialize(attacker, target, new Dimension(5, 5));

        assertEquals(1, bullet.getActions().size);

        bullet.act(100f);

        assertEquals(target.getPositionCenter(), bullet.getPositionCenter());
        assertEquals(0, bullet.getActions().size);
        verify(poolMock, times(1)).free(bullet);
        verifyStatic(times(1));
        Damage.dealBulletDamage(eq(attacker), eq(target));

    }

    /**
     * Target is killed
     */
    @Test
    public void bulletTest2() {

        Tower attacker = TestUtil.createTower("Rifle", false);
        attacker.setPositionCenter(40, 40);
        Enemy target = TestUtil.createEnemy("Rifle", true);
        target.setPositionCenter(140, 140);

        Bullet bullet = createBullet();
        bullet.initialize(attacker, target, new Dimension(5, 5));

        assertEquals(1, bullet.getActions().size);

        doReturn(true).when(target).isDead();

        bullet.act(100f);

        assertEquals(target.getPositionCenter(), bullet.getPositionCenter());
        assertEquals(0, bullet.getActions().size);
        verify(poolMock, times(1)).free(bullet);
        verifyStatic(never());
        Damage.dealBulletDamage(any(Attacker.class), any(Targetable.class));

    }
}

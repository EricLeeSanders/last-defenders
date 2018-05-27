package com.lastdefenders.game.model.actor.projectile;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.lastdefenders.game.helper.Damage;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.model.actor.projectile.Explosion;
import com.lastdefenders.game.model.actor.projectile.Rocket;
import com.lastdefenders.game.service.factory.ProjectileFactory;
import com.lastdefenders.game.service.factory.ProjectileFactory.RocketPool;
import com.lastdefenders.util.ActorUtil;
import com.lastdefenders.util.Resources;
import com.lastdefenders.util.datastructures.Dimension;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import testutil.TestUtil;

/**
 * Created by Eric on 5/22/2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Damage.class})
public class RocketTest {


    private RocketPool poolMock = mock(RocketPool.class);
    private Explosion explosionMock = mock(Explosion.class);

    @Before
    public void initRocketTest() {

        Gdx.app = mock(Application.class);
        PowerMockito.mockStatic(Damage.class);
    }

    public Rocket createRocket() {

        ProjectileFactory projectileFactoryMock = mock(ProjectileFactory.class);
        doReturn(explosionMock).when(projectileFactoryMock).loadExplosion();
        Resources resourcesMock = TestUtil.createResourcesMock();

        return new Rocket(poolMock, projectileFactoryMock, resourcesMock.getTexture(""));
    }

    @Test
    public void rocketTest1() {

        Tower tower = TestUtil.createTower("Rifle", false);
        tower.setPositionCenter(40, 80);
        Vector2 destination = new Vector2(100, 100);
        Dimension size = new Dimension(20, 20);
        float radius = 50;
        Rocket rocket = createRocket();

        rocket.initialize(tower, destination, size, radius);

        //check rotation
        assertEquals(ActorUtil.calculateRotation(destination, tower.getPositionCenter()),
            rocket.getRotation(), TestUtil.DELTA);
        //check size
        assertEquals(size.getWidth(), rocket.getWidth(), TestUtil.DELTA);
        assertEquals(size.getHeight(), rocket.getHeight(), TestUtil.DELTA);
        //check origin
        assertEquals(size.getWidth() / 2, rocket.getOriginX(), TestUtil.DELTA);
        assertEquals(size.getHeight() / 2, rocket.getOriginY(), TestUtil.DELTA);

        assertEquals(1, rocket.getActions().size);

        rocket.act(100f);

        assertEquals(0, rocket.getActions().size);
        verify(poolMock).free(rocket);
        verify(explosionMock).initialize(eq(tower), eq(radius), eq(destination));

    }
}

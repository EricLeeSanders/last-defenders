package com.lastdefenders.game.service.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.lastdefenders.game.model.actor.ActorGroups;
import com.lastdefenders.game.model.actor.projectile.Bullet;
import com.lastdefenders.game.service.factory.ProjectileFactory;
import com.lastdefenders.util.LDAudio;
import com.lastdefenders.util.Resources;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import testutil.TestUtil;

/**
 * Created by Eric on 5/25/2018.
 */

public class ProjectileFactoryTest {
    @Spy
    private ActorGroups actorGroups = new ActorGroups();
    @Mock
    private Resources resources = TestUtil.createResourcesMock();
    @Mock
    private LDAudio audio;

    @InjectMocks
    private ProjectileFactory projectileFactory;

    @Before
    public void initProjectileFactoryTest() {
        Gdx.app = mock(Application.class);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void loadProjectileTest(){

        Bullet bullet = projectileFactory.loadProjectile(Bullet.class);

        assertNotNull(bullet);
        assertEquals(1, actorGroups.getProjectileGroup().getChildren().size);
    }

}

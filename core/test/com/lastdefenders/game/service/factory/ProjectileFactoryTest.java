package com.lastdefenders.game.service.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.lastdefenders.game.model.actor.groups.ActorGroups;
import com.lastdefenders.game.model.actor.projectile.Bullet;
import com.lastdefenders.util.Resources;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import testutil.ResourcesMock;

/**
 * Created by Eric on 5/25/2018.
 */

public class ProjectileFactoryTest {
    @Spy
    private ActorGroups actorGroups = new ActorGroups();
    @Mock
    private Resources resources = ResourcesMock.create();

    @InjectMocks
    private ProjectileFactory projectileFactory;

    private AutoCloseable closeable;

    @BeforeAll
    public static void initProjectileFactoryTest() {
        Gdx.app = mock(Application.class);
    }

    @BeforeEach
    public void startMocks() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }


    @Test
    public void loadProjectileTest(){

        Bullet bullet = projectileFactory.loadProjectile(Bullet.class);

        assertNotNull(bullet);
        assertEquals(1, actorGroups.getProjectileGroup().getChildren().size);
    }

}

package com.lastdefenders.game.model.actor.support;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.actor.combat.enemy.EnemyFlameThrower;
import com.lastdefenders.game.model.actor.combat.enemy.EnemyRifle;
import com.lastdefenders.game.model.actor.combat.enemy.EnemyRocketLauncher;
import com.lastdefenders.game.model.actor.groups.EnemyGroup;
import com.lastdefenders.game.model.actor.projectile.Bullet;
import com.lastdefenders.game.service.factory.ProjectileFactory;
import com.lastdefenders.game.service.factory.SupportActorFactory.SupportActorPool;
import com.lastdefenders.util.ActorUtil;
import com.lastdefenders.sound.LDAudio;
import com.lastdefenders.util.Resources;
import com.lastdefenders.util.datastructures.Dimension;
import com.lastdefenders.util.datastructures.pool.LDVector2;
import org.junit.jupiter.api.Test;
import testutil.ResourcesMock;
import testutil.TestUtil;

/**
 * Created by Eric on 5/22/2017.
 */

public class ApacheTest {

    @SuppressWarnings("unchecked")
    private SupportActorPool<Apache> poolMock = mock(SupportActorPool.class);
    private Bullet bulletMock = mock(Bullet.class);

    public Apache createApache() {

        Resources resourcesMock = ResourcesMock.create();
        LDAudio audioMock = mock(LDAudio.class);
        ProjectileFactory projectileFactoryMock = mock(ProjectileFactory.class);
        doReturn(bulletMock).when(projectileFactoryMock).loadProjectile(Bullet.class);

        return new Apache(poolMock, new EnemyGroup(), projectileFactoryMock,
            resourcesMock.getTexture(""), new TextureRegion[]{resourcesMock.getTexture("")},
            resourcesMock.getTexture(""), audioMock);

    }

    private Enemy createEnemy(Class<? extends Enemy> enemyClass, float lengthToEnd, Vector2 posCenter) {

        Enemy enemy = TestUtil.createEnemy(enemyClass, true);
        enemy.setPositionCenter(posCenter);
        doReturn(lengthToEnd).when(enemy).getLengthToEnd();
        enemy.setDead(false);
        enemy.setActive(true);

        return enemy;
    }

    /**
     * Initialize an apahce
     */
    @Test
    public void apacheTest1() {

        LDVector2 destination = new LDVector2(250, 250);

        Apache apache = createApache();
        apache.initialize();
        Enemy enemy1 = createEnemy(EnemyRifle.class, 200, new Vector2(250, 240));
        Enemy enemy2 = createEnemy(EnemyFlameThrower.class, 100, new Vector2(250, 260));
        Enemy enemy3 = createEnemy(EnemyRocketLauncher.class, 300, new Vector2(250, 150));

        Group targetGroup = apache.getEnemyGroup();
        targetGroup.addActor(enemy1);
        targetGroup.addActor(enemy2);
        targetGroup.addActor(enemy3);

        assertFalse(apache.isActive());
        assertFalse(apache.isReadyToAttack());
        apache.setPlacement(destination);
        apache.ready();

        assertEquals(1, apache.getActions().size);
        assertFalse(apache.isReadyToAttack());
        apache.setActive(true);
        assertTrue(apache.isActive());

        // get to position
        apache.act(Apache.TIME_ACTIVE_LIMIT / 2);
        // act again to call next action in sequence (the action that readies to attack)
        apache.act(0.0001f);

        assertTrue(apache.isReadyToAttack());
        verify(bulletMock, times(1)).initialize(eq(apache), eq(enemy2), isA(Dimension.class));
        assertEquals(
            ActorUtil.calculateRotation(enemy2.getPositionCenter(), apache.getPositionCenter()),
            apache.getRotation(), TestUtil.DELTA);

        apache.act(Apache.TIME_ACTIVE_LIMIT);
        // act again to call next action in sequence (the action that sets readyToAttack to false)
        apache.act(0.0001f);

        assertFalse(apache.isReadyToAttack());
        assertFalse(apache.isActive());

        apache.act(100f);
        // act again to call next action in sequence (FreeActorAction)
        apache.act(0.0001f);

        verify(poolMock, times(1)).free(apache);

    }
}

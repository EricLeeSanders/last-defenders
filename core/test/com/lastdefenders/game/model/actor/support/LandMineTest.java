package com.lastdefenders.game.model.actor.support;

import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.actor.projectile.Explosion;
import com.lastdefenders.game.model.actor.support.LandMine;
import com.lastdefenders.game.service.factory.ProjectileFactory;
import com.lastdefenders.game.service.factory.SupportActorFactory.SupportActorPool;
import com.lastdefenders.util.Resources;
import org.junit.Before;
import org.junit.Test;
import testutil.TestUtil;

/**
 * Created by Eric on 5/23/2017.
 */

public class LandMineTest {

    @SuppressWarnings("unchecked")
    private SupportActorPool<LandMine> poolMock = mock(SupportActorPool.class);
    private Explosion explosionMock = mock(Explosion.class);

    @Before
    public void initLandMinTest() {

        Gdx.app = mock(Application.class);
    }

    public LandMine createLandMine() {

        Resources resourcesMock = TestUtil.createResourcesMock();
        ProjectileFactory projectileFactoryMock = mock(ProjectileFactory.class);
        doReturn(explosionMock).when(projectileFactoryMock).loadExplosion();

        return new LandMine(poolMock, new Group(), projectileFactoryMock,
            resourcesMock.getTexture(""), resourcesMock.getTexture(""));
    }

    private Enemy createEnemy(String type, float lengthToEnd, Vector2 posCenter) {

        Enemy enemy = TestUtil.createEnemy(type, true);
        enemy.setPositionCenter(posCenter);
        doReturn(lengthToEnd).when(enemy).getLengthToEnd();
        enemy.setDead(false);
        enemy.setActive(true);

        return enemy;
    }

    @Test
    public void landMinTest1() {

        LandMine landMine = createLandMine();
        landMine.setPositionCenter(250, 255);
        landMine.setActive(true);

        Enemy enemy1 = createEnemy("Rifle", 200, new Vector2(250, 240));
        Enemy enemy2 = createEnemy("FlameThrower", 100, new Vector2(250, 260));
        Enemy enemy3 = createEnemy("RocketLauncher", 300, new Vector2(250, 150));

        Group targetGroup = landMine.getTargetGroup();
        targetGroup.addActor(enemy1);
        targetGroup.addActor(enemy2);
        targetGroup.addActor(enemy3);

        landMine.act(1f);

        verify(explosionMock, times(1))
            .initialize(eq(landMine), isA(Float.class), eq(landMine.getPositionCenter()));
        verify(poolMock, times(1)).free(landMine);

    }
}

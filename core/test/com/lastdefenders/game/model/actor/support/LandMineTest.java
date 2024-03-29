package com.lastdefenders.game.model.actor.support;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.actor.combat.enemy.EnemyFlameThrower;
import com.lastdefenders.game.model.actor.combat.enemy.EnemyRifle;
import com.lastdefenders.game.model.actor.combat.enemy.EnemyRocketLauncher;
import com.lastdefenders.game.model.actor.groups.EnemyGroup;
import com.lastdefenders.game.model.actor.projectile.Explosion;
import com.lastdefenders.game.service.factory.ProjectileFactory;
import com.lastdefenders.game.service.factory.SupportActorFactory.SupportActorPool;
import com.lastdefenders.util.Resources;
import com.lastdefenders.util.datastructures.pool.LDVector2;
import org.junit.jupiter.api.Test;
import testutil.ResourcesMock;
import testutil.TestUtil;

/**
 * Created by Eric on 5/23/2017.
 */

public class LandMineTest {

    @SuppressWarnings("unchecked")
    private SupportActorPool<LandMine> poolMock = mock(SupportActorPool.class);
    private Explosion explosionMock = mock(Explosion.class);

    public LandMine createLandMine() {

        Resources resourcesMock = ResourcesMock.create();
        ProjectileFactory projectileFactoryMock = mock(ProjectileFactory.class);
        doReturn(explosionMock).when(projectileFactoryMock).loadProjectile(Explosion.class);

        return new LandMine(poolMock, new EnemyGroup(), projectileFactoryMock,
            resourcesMock.getTexture(""), resourcesMock.getTexture(""));
    }

    private Enemy createEnemy(Class<? extends Enemy> type, float lengthToEnd, Vector2 posCenter) {

        Enemy enemy = TestUtil.createEnemy(type, true);
        enemy.setPositionCenter(posCenter);
        doReturn(lengthToEnd).when(enemy).getLengthToEnd();
        enemy.setDead(false);
        enemy.setActive(true);

        return enemy;
    }

    @Test
    public void landMineTest1() {

        LandMine landMine = createLandMine();
        landMine.initialize();
        landMine.setPlacement(new LDVector2(250, 255));

        Enemy enemy1 = createEnemy(EnemyRifle.class, 200, new Vector2(250, 240));
        Enemy enemy2 = createEnemy(EnemyFlameThrower.class, 100, new Vector2(250, 260));
        Enemy enemy3 = createEnemy(EnemyRocketLauncher.class, 300, new Vector2(250, 150));

        Group targetGroup = landMine.getEnemyGroup();
        targetGroup.addActor(enemy1);
        targetGroup.addActor(enemy2);
        targetGroup.addActor(enemy3);
        landMine.ready();
        landMine.act(1f);

        verify(explosionMock, times(1))
            .initialize(eq(landMine), isA(Float.class), eq(landMine.getPositionCenter()));
        verify(poolMock, times(1)).free(landMine);

    }
}

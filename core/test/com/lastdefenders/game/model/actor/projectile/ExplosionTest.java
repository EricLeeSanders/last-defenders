package com.lastdefenders.game.model.actor.projectile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import com.lastdefenders.game.helper.Damage;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.actor.combat.enemy.EnemyTank;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.model.actor.combat.tower.TowerHumvee;
import com.lastdefenders.game.service.factory.ProjectileFactory.ProjectilePool;
import com.lastdefenders.sound.SoundPlayer;
import com.lastdefenders.util.Resources;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import testutil.ResourcesMock;
import testutil.TestUtil;

/**
 * Created by Eric on 5/21/2017.
 */
public class ExplosionTest {
    @SuppressWarnings("unchecked")
    private ProjectilePool<Explosion> poolMock = mock(ProjectilePool.class);

    public Explosion createExplosion() {

        Resources resourcesMock = ResourcesMock.create();
        SoundPlayer soundPlayerMock = mock(SoundPlayer.class);

        return new Explosion(poolMock, resourcesMock.getAtlasRegion(""), soundPlayerMock);

    }

    @Test
    public void explosionTest1() {

        Enemy attacker = TestUtil.createEnemy(EnemyTank.class, false);
        attacker.setPositionCenter(50, 50);
        Tower target = TestUtil.createTower(TowerHumvee.class, false, true);
        target.setPositionCenter(200, 200);

        Explosion explosion = createExplosion();
        try(MockedStatic<Damage> mockedDamage = mockStatic(Damage.class)) {
            explosion.initialize(attacker, 70.0f, target.getPositionCenter());

            assertEquals(target.getPositionCenter(), explosion.getPositionCenter());
            mockedDamage.verify(() -> Damage.dealExplosionDamage(eq(attacker), eq(70.0f), eq(target.getPositionCenter()),
                eq(attacker.getEnemyGroup().getChildren())));
        }

    }

    /**
     * Explosion is freed after running for its duration
     */
    @Test
    public void explosionTest2() {

        Enemy attacker = TestUtil.createEnemy(EnemyTank.class, false);
        attacker.setPositionCenter(50, 50);
        Tower target = TestUtil.createTower(TowerHumvee.class, false, true);
        target.setPositionCenter(200, 200);

        Explosion explosion = createExplosion();
        explosion.initialize(attacker, 70.0f, target.getPositionCenter());

        explosion.act(500f);
        verify(poolMock, times(1)).free(explosion);
    }
}

package game.model.actor.projectile;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.foxholedefense.game.helper.Damage;
import com.foxholedefense.game.model.actor.combat.enemy.Enemy;
import com.foxholedefense.game.model.actor.combat.tower.Tower;
import com.foxholedefense.game.model.actor.projectile.Explosion;
import com.foxholedefense.game.service.factory.ProjectileFactory.ExplosionPool;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.Resources;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import testutil.TestUtil;


import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

/**
 * Created by Eric on 5/21/2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Damage.class})
public class ExplosionTest {

    private ExplosionPool poolMock = mock(ExplosionPool.class);

    @Before
    public void initExplosionTest() {
        Gdx.app = mock(Application.class);
        PowerMockito.mockStatic(Damage.class);
    }

    public Explosion createExplosion() {
        Resources resourcesMock = TestUtil.createResourcesMock();
        FHDAudio audioMock = mock(FHDAudio.class);

        return new Explosion(poolMock, resourcesMock.getAtlasRegion(""), audioMock);

    }

    @Test
    public void explosionTest1() {
        Enemy attacker = TestUtil.createEnemy("Tank", false);
        attacker.setPositionCenter(50, 50);
        Tower target = TestUtil.createTower("Turret", false);
        target.setPositionCenter(200, 200);

        Explosion explosion = createExplosion();
        explosion.initialize(attacker, 70.0f, target.getPositionCenter());

        assertEquals(target.getPositionCenter(), explosion.getPositionCenter());
        verifyStatic(times(1));
        Damage.dealExplosionDamage(eq(attacker), eq(70.0f), eq(target.getPositionCenter()), eq(attacker.getTargetGroup().getChildren()));

    }

    /**
     * Explosion is freed after running for its duration
     */
    @Test
    public void explosionTest2() {
        Enemy attacker = TestUtil.createEnemy("Tank", false);
        attacker.setPositionCenter(50, 50);
        Tower target = TestUtil.createTower("Turret", false);
        target.setPositionCenter(200, 200);

        Explosion explosion = createExplosion();
        explosion.initialize(attacker, 70.0f, target.getPositionCenter());

        explosion.act(500f);
        verify(poolMock, times(1)).free(explosion);
    }
}

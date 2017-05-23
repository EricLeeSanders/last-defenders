package game.model.actor.projectile;

        import com.badlogic.gdx.graphics.g2d.TextureRegion;
        import com.badlogic.gdx.utils.Pool;
        import com.foxholedefense.game.helper.Damage;
        import com.foxholedefense.game.model.actor.combat.enemy.Enemy;
        import com.foxholedefense.game.model.actor.combat.tower.Tower;
        import com.foxholedefense.game.model.actor.projectile.Bullet;
        import com.foxholedefense.util.Logger;
        import com.foxholedefense.util.datastructures.Dimension;


        import org.junit.Before;
        import org.junit.Test;
        import org.junit.runner.RunWith;
        import org.powermock.api.mockito.PowerMockito;
        import org.powermock.core.classloader.annotations.PrepareForTest;
        import org.powermock.modules.junit4.PowerMockRunner;


        import util.TestUtil;


        import static org.junit.Assert.assertEquals;
        import static org.junit.Assert.assertTrue;
        import static org.mockito.Matchers.eq;
        import static org.mockito.Mockito.mock;
        import static org.mockito.Mockito.times;
        import static org.mockito.Mockito.verify;
        import static org.powermock.api.mockito.PowerMockito.verifyStatic;

/**
 * Created by Eric on 5/21/2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Logger.class, Damage.class})
public class BulletTest {
    private Pool poolMock = mock(Pool.class);
    @Before
    public void initBulletTest() {
        PowerMockito.mockStatic(Logger.class);
        PowerMockito.mockStatic(Damage.class);
    }

    private Bullet createBullet(){
        TextureRegion textureRegionMock = mock(TextureRegion.class);

        Bullet bullet = new Bullet(poolMock, textureRegionMock);

        return bullet;

    }

    // Bullet is initialized correctly and is freed when the action finishes
    @Test
    public void bulletTest1(){
        Tower attacker = TestUtil.createTower("Rifle", false);
        attacker.setPositionCenter(40, 40);
        Enemy target = TestUtil.createEnemy("EnemyRifle", false);
        target.setPositionCenter(140,140);

        Bullet bullet = createBullet();

        bullet.initialize(attacker, target, new Dimension(5,5));

        assertEquals(1, bullet.getActions().size);

        bullet.act(100f);

        assertEquals(target.getPositionCenter(), bullet.getPositionCenter());
        assertEquals(0, bullet.getActions().size);
        verify(poolMock, times(1)).free(bullet);
        verifyStatic(times(1));
        Damage.dealBulletDamage(eq(attacker), eq(target));

    }
}

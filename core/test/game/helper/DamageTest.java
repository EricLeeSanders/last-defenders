package game.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.when;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;
import com.lastdefenders.game.helper.CollisionDetection;
import com.lastdefenders.game.helper.Damage;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.util.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import testutil.TestUtil;

/**
 * Created by Eric on 5/20/2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Logger.class, CollisionDetection.class})
public class DamageTest {

    @Before
    public void initDamageTest() {

        PowerMockito.mockStatic(Logger.class);
        PowerMockito.mockStatic(CollisionDetection.class);
    }

    @Test
    public void dealBulletDamageAttackEnemyTest() {

        Tower tower = TestUtil.createTower("Rifle", true);
        Enemy enemy = TestUtil.createEnemy("Rifle", false);

        doReturn(1f).when(tower).getAttack();

        Damage.dealBulletDamage(tower, enemy);

        assertEquals(enemy.getMaxHealth() - 1, enemy.getHealth(), TestUtil.DELTA);
    }

    @Test
    public void dealBulletDamageAttackTowerTest() {

        Tower tower = TestUtil.createTower("Rifle", false);
        Enemy enemy = TestUtil.createEnemy("Rifle", true);

        doReturn(1f).when(enemy).getAttack();

        Damage.dealBulletDamage(enemy, tower);

        assertEquals(tower.getMaxHealth() - 1, tower.getHealth(), TestUtil.DELTA);
    }

    @Test
    public void dealBulletDamageKillEnemyTest() {

        Tower tower = TestUtil.createTower("Rifle", true);
        Enemy enemy = TestUtil.createEnemy("Rifle", false);

        doReturn(enemy.getHealth()).when(tower).getAttack();

        Damage.dealBulletDamage(tower, enemy);

        assertTrue(enemy.isDead());
        assertEquals(1, tower.getNumOfKills());
    }

    @Test
    public void dealBulletDamageKillTowerTest() {

        Tower tower = TestUtil.createTower("Rifle", false);
        Enemy enemy = TestUtil.createEnemy("Rifle", true);

        doReturn(tower.getHealth()).when(enemy).getAttack();

        Damage.dealBulletDamage(enemy, tower);

        assertTrue(tower.isDead());
    }

    @Test
    public void dealBulletDamageAttackPlatedArmorTest() {

        Tower tower = TestUtil.createTower("Rifle", true);
        Enemy enemy = TestUtil.createEnemy("Tank", true);

        Damage.dealBulletDamage(tower, enemy);

        assertEquals(enemy.getMaxHealth(), enemy.getHealth(), TestUtil.DELTA);
    }

    @Test
    public void dealFlameGroupDamageTest() {

        Tower tower = TestUtil.createTower("FlameThrower", true);

        doReturn(1f).when(tower).getAttack();

        Enemy enemy1 = TestUtil.createEnemy("Rifle", false);
        Enemy enemy2 = TestUtil.createEnemy("Sniper", false);
        Enemy enemy3 = TestUtil.createEnemy("RocketLauncher", false);
        Enemy enemy4 = TestUtil.createEnemy("Tank", false);

        SnapshotArray<Actor> enemies = new SnapshotArray<>();
        enemies.addAll(enemy1, enemy2, enemy3, enemy4);

        Polygon flameBody = new Polygon();

        when(CollisionDetection.shapesIntersect(eq(enemy1.getBody()), eq(flameBody)))
            .thenReturn(true);
        when(CollisionDetection.shapesIntersect(eq(enemy2.getBody()), eq(flameBody)))
            .thenReturn(true);
        when(CollisionDetection.shapesIntersect(eq(enemy3.getBody()), eq(flameBody)))
            .thenReturn(false);
        when(CollisionDetection.shapesIntersect(eq(enemy4.getBody()), eq(flameBody)))
            .thenReturn(true);

        Damage.dealFlameGroupDamage(tower, enemies, flameBody);

        assertEquals(enemy1.getMaxHealth() - 1, enemy1.getHealth(), TestUtil.DELTA);
        assertEquals(enemy2.getMaxHealth() - 1, enemy2.getHealth(), TestUtil.DELTA);
        assertEquals(enemy3.getMaxHealth(), enemy3.getHealth(), TestUtil.DELTA);
        assertEquals(enemy4.getMaxHealth(), enemy4.getHealth(), TestUtil.DELTA);

    }

    @Test
    public void dealFlameGroupDamageKillEnemiesTest() {

        Tower tower = TestUtil.createTower("FlameThrower", true);

        doReturn(20f).when(tower).getAttack();

        Enemy enemy1 = TestUtil.createEnemy("Rifle", false);
        Enemy enemy2 = TestUtil.createEnemy("Humvee", false);
        Enemy enemy3 = TestUtil.createEnemy("RocketLauncher", false);
        Enemy enemy4 = TestUtil.createEnemy("Tank", false);

        SnapshotArray<Actor> enemies = new SnapshotArray<>();
        enemies.addAll(enemy1, enemy2, enemy3, enemy4);

        Polygon flameBody = new Polygon();

        when(CollisionDetection.shapesIntersect(eq(enemy1.getBody()), eq(flameBody)))
            .thenReturn(true);
        when(CollisionDetection.shapesIntersect(eq(enemy2.getBody()), eq(flameBody)))
            .thenReturn(true);
        when(CollisionDetection.shapesIntersect(eq(enemy3.getBody()), eq(flameBody)))
            .thenReturn(false);
        when(CollisionDetection.shapesIntersect(eq(enemy4.getBody()), eq(flameBody)))
            .thenReturn(true);

        Damage.dealFlameGroupDamage(tower, enemies, flameBody);

        assertTrue(enemy1.isDead());
        assertTrue(enemy2.isDead());
        assertFalse(enemy3.isDead());
        assertFalse(enemy4.isDead());

        assertEquals(2, tower.getNumOfKills());

    }

    @Test
    public void dealExplosionDamageTest() {

        Tower tower = TestUtil.createTower("RocketLauncher", true);

        doReturn(1f).when(tower).getAttack();

        Enemy enemy1 = TestUtil.createEnemy("Rifle", false);
        Enemy enemy2 = TestUtil.createEnemy("Sniper", false);
        Enemy enemy3 = TestUtil.createEnemy("RocketLauncher", false);
        Enemy enemy4 = TestUtil.createEnemy("Tank", false);

        SnapshotArray<Actor> enemies = new SnapshotArray<>();
        enemies.addAll(enemy1, enemy2, enemy3, enemy4);

        when(CollisionDetection.shapesIntersect(eq(enemy1.getBody()), any(Shape2D.class)))
            .thenReturn(true);
        when(CollisionDetection.shapesIntersect(eq(enemy2.getBody()), any(Shape2D.class)))
            .thenReturn(true);
        when(CollisionDetection.shapesIntersect(eq(enemy3.getBody()), any(Shape2D.class)))
            .thenReturn(false);
        when(CollisionDetection.shapesIntersect(eq(enemy4.getBody()), any(Shape2D.class)))
            .thenReturn(true);

        Damage.dealExplosionDamage(tower, 1, new Vector2(), enemies);

        assertEquals(enemy1.getMaxHealth() - 1, enemy1.getHealth(), TestUtil.DELTA);
        assertEquals(enemy2.getMaxHealth() - 1, enemy2.getHealth(), TestUtil.DELTA);
        assertEquals(enemy3.getMaxHealth(), enemy3.getHealth(), TestUtil.DELTA);
        assertEquals(enemy4.getMaxHealth() - 1, enemy4.getHealth(), TestUtil.DELTA);

    }


    @Test
    public void dealExplosionDamageKillEnemiesTest() {

        Tower tower = TestUtil.createTower("RocketLauncher", true);

        doReturn(15f).when(tower).getAttack();

        Enemy enemy1 = TestUtil.createEnemy("Rifle", false);
        Enemy enemy2 = TestUtil.createEnemy("Sniper", false);
        Enemy enemy3 = TestUtil.createEnemy("RocketLauncher", false);
        Enemy enemy4 = TestUtil.createEnemy("Tank", false);

        SnapshotArray<Actor> enemies = new SnapshotArray<>();
        enemies.addAll(enemy1, enemy2, enemy3, enemy4);

        when(CollisionDetection.shapesIntersect(eq(enemy1.getBody()), any(Shape2D.class)))
            .thenReturn(true);
        when(CollisionDetection.shapesIntersect(eq(enemy2.getBody()), any(Shape2D.class)))
            .thenReturn(true);
        when(CollisionDetection.shapesIntersect(eq(enemy3.getBody()), any(Shape2D.class)))
            .thenReturn(false);
        when(CollisionDetection.shapesIntersect(eq(enemy4.getBody()), any(Shape2D.class)))
            .thenReturn(true);

        Damage.dealExplosionDamage(tower, 1, new Vector2(), enemies);

        assertTrue(enemy1.isDead());
        assertTrue(enemy2.isDead());
        assertFalse(enemy3.isDead());
        assertFalse(enemy4.isDead());

    }
}

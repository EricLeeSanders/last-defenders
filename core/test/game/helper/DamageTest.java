package game.helper;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;
import com.foxholedefense.game.helper.CollisionDetection;
import com.foxholedefense.game.helper.Damage;
import com.foxholedefense.game.model.actor.combat.enemy.Enemy;
import com.foxholedefense.game.model.actor.combat.tower.Tower;
import com.foxholedefense.util.Logger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import util.TestUtil;

import static org.junit.Assert.*;


import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.when;

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
    public void dealBulletDamageAttackEnemyTest(){
        Tower tower = TestUtil.createTower("Rifle", true);
        Enemy enemy = TestUtil.createEnemy("EnemyRifle", false);

        doReturn(1f).when(tower).getAttack();

        Damage.dealBulletDamage(tower, enemy);

        assertEquals(enemy.getMaxHealth() - 1, enemy.getHealth(), TestUtil.DELTA);
    }

    @Test
    public void dealBulletDamageAttackTowerTest(){
        Tower tower = TestUtil.createTower("Rifle", false);
        Enemy enemy = TestUtil.createEnemy("EnemyRifle", true);

        doReturn(1f).when(enemy).getAttack();

        Damage.dealBulletDamage(enemy, tower);

        assertEquals(tower.getMaxHealth() - 1, tower.getHealth(), TestUtil.DELTA);
    }

    @Test
    public void dealBulletDamageKillEnemyTest(){
        Tower tower = TestUtil.createTower("Rifle", true);
        Enemy enemy = TestUtil.createEnemy("EnemyRifle", false);

        doReturn(enemy.getHealth()).when(tower).getAttack();

        Damage.dealBulletDamage(tower, enemy);

        assertTrue(enemy.isDead());
        assertEquals(1, tower.getNumOfKills());
    }

    @Test
    public void dealBulletDamageKillTowerTest(){
        Tower tower = TestUtil.createTower("Rifle", false);
        Enemy enemy = TestUtil.createEnemy("EnemyRifle", true);

        doReturn(tower.getHealth()).when(enemy).getAttack();

        Damage.dealBulletDamage(enemy, tower);

        assertTrue(tower.isDead());
    }

    @Test
    public void dealBulletDamageAttackPlatedArmorTest(){
        Tower tower = TestUtil.createTower("Rifle", true);
        Enemy enemy = TestUtil.createEnemy("EnemyTank", true);

        Damage.dealBulletDamage(tower, enemy);

        assertEquals(enemy.getMaxHealth(), enemy.getHealth(), TestUtil.DELTA);
    }

    @Test
    public void dealFlameGroupDamageTest(){

        Tower tower = TestUtil.createTower("FlameThrower", true);

        doReturn(1f).when(tower).getAttack();

        Enemy enemy1 = TestUtil.createEnemy("EnemyRifle", false);
        Enemy enemy2 = TestUtil.createEnemy("EnemySniper", false);
        Enemy enemy3 = TestUtil.createEnemy("EnemyRocketLauncher", false);
        Enemy enemy4 = TestUtil.createEnemy("EnemyTank", false);

        SnapshotArray<Actor> enemies = new SnapshotArray<Actor>();
        enemies.addAll(enemy1, enemy2, enemy3, enemy4);

        Polygon flameBody = new Polygon();

        when(CollisionDetection.shapesIntersect(eq(enemy1.getBody()), eq(flameBody))).thenReturn(true);
        when(CollisionDetection.shapesIntersect(eq(enemy2.getBody()), eq(flameBody))).thenReturn(true);
        when(CollisionDetection.shapesIntersect(eq(enemy3.getBody()), eq(flameBody))).thenReturn(false);
        when(CollisionDetection.shapesIntersect(eq(enemy4.getBody()), eq(flameBody))).thenReturn(true);

        Damage.dealFlameGroupDamage(tower, enemies, flameBody);

        assertEquals(enemy1.getMaxHealth() - 1, enemy1.getHealth(), TestUtil.DELTA);
        assertEquals(enemy2.getMaxHealth() - 1, enemy2.getHealth(), TestUtil.DELTA);
        assertEquals(enemy3.getMaxHealth(), enemy3.getHealth(), TestUtil.DELTA);
        assertEquals(enemy4.getMaxHealth(), enemy4.getHealth(), TestUtil.DELTA);

    }

    @Test
    public void dealFlameGroupDamageKillEnemiesTest(){

        Tower tower = TestUtil.createTower("FlameThrower", true);

        doReturn(20f).when(tower).getAttack();

        Enemy enemy1 = TestUtil.createEnemy("EnemyRifle", false);
        Enemy enemy2 = TestUtil.createEnemy("EnemyHumvee", false);
        Enemy enemy3 = TestUtil.createEnemy("EnemyRocketLauncher", false);
        Enemy enemy4 = TestUtil.createEnemy("EnemyTank", false);

        SnapshotArray<Actor> enemies = new SnapshotArray<Actor>();
        enemies.addAll(enemy1, enemy2, enemy3, enemy4);

        Polygon flameBody = new Polygon();

        when(CollisionDetection.shapesIntersect(eq(enemy1.getBody()), eq(flameBody))).thenReturn(true);
        when(CollisionDetection.shapesIntersect(eq(enemy2.getBody()), eq(flameBody))).thenReturn(true);
        when(CollisionDetection.shapesIntersect(eq(enemy3.getBody()), eq(flameBody))).thenReturn(false);
        when(CollisionDetection.shapesIntersect(eq(enemy4.getBody()), eq(flameBody))).thenReturn(true);

        Damage.dealFlameGroupDamage(tower, enemies, flameBody);

        assertTrue(enemy1.isDead());
        assertTrue(enemy2.isDead());
        assertFalse(enemy3.isDead());
        assertFalse(enemy4.isDead());

        assertEquals(2, tower.getNumOfKills());

    }

    @Test
    public void dealExplosionDamageTest(){

        Tower tower = TestUtil.createTower("RocketLauncher", true);

        doReturn(1f).when(tower).getAttack();

        Enemy enemy1 = TestUtil.createEnemy("EnemyRifle", false);
        Enemy enemy2 = TestUtil.createEnemy("EnemySniper", false);
        Enemy enemy3 = TestUtil.createEnemy("EnemyRocketLauncher", false);
        Enemy enemy4 = TestUtil.createEnemy("EnemyTank", false);

        SnapshotArray<Actor> enemies = new SnapshotArray<Actor>();
        enemies.addAll(enemy1, enemy2, enemy3, enemy4);

        when(CollisionDetection.shapesIntersect(eq(enemy1.getBody()), any(Shape2D.class))).thenReturn(true);
        when(CollisionDetection.shapesIntersect(eq(enemy2.getBody()), any(Shape2D.class))).thenReturn(true);
        when(CollisionDetection.shapesIntersect(eq(enemy3.getBody()), any(Shape2D.class))).thenReturn(false);
        when(CollisionDetection.shapesIntersect(eq(enemy4.getBody()), any(Shape2D.class))).thenReturn(true);

        Damage.dealExplosionDamage(tower, 1, new Vector2(), enemies);

        assertEquals(enemy1.getMaxHealth() - 1, enemy1.getHealth(), TestUtil.DELTA);
        assertEquals(enemy2.getMaxHealth() - 1, enemy2.getHealth(), TestUtil.DELTA);
        assertEquals(enemy3.getMaxHealth(), enemy3.getHealth(), TestUtil.DELTA);
        assertEquals(enemy4.getMaxHealth() - 1, enemy4.getHealth(), TestUtil.DELTA);

    }


    @Test
    public void dealExplosionDamageKillEnemiesTest(){

        Tower tower = TestUtil.createTower("RocketLauncher", true);

        doReturn(15f).when(tower).getAttack();

        Enemy enemy1 = TestUtil.createEnemy("EnemyRifle", false);
        Enemy enemy2 = TestUtil.createEnemy("EnemySniper", false);
        Enemy enemy3 = TestUtil.createEnemy("EnemyRocketLauncher", false);
        Enemy enemy4 = TestUtil.createEnemy("EnemyTank", false);

        SnapshotArray<Actor> enemies = new SnapshotArray<Actor>();
        enemies.addAll(enemy1, enemy2, enemy3, enemy4);

        when(CollisionDetection.shapesIntersect(eq(enemy1.getBody()), any(Shape2D.class))).thenReturn(true);
        when(CollisionDetection.shapesIntersect(eq(enemy2.getBody()), any(Shape2D.class))).thenReturn(true);
        when(CollisionDetection.shapesIntersect(eq(enemy3.getBody()), any(Shape2D.class))).thenReturn(false);
        when(CollisionDetection.shapesIntersect(eq(enemy4.getBody()), any(Shape2D.class))).thenReturn(true);

        Damage.dealExplosionDamage(tower, 1, new Vector2(), enemies);

        assertTrue(enemy1.isDead());
        assertTrue(enemy2.isDead());
        assertFalse(enemy3.isDead());
        assertFalse(enemy4.isDead());

    }

}
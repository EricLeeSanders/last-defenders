package com.lastdefenders.game.model.actor.ai;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.lastdefenders.game.model.actor.ai.towerai.ClosestEnemyAI;
import com.lastdefenders.game.model.actor.ai.towerai.FarthestEnemyAI;
import com.lastdefenders.game.model.actor.ai.towerai.LeastHPEnemyAI;
import com.lastdefenders.game.model.actor.ai.towerai.MostHPEnemyAI;
import com.lastdefenders.game.model.actor.ai.towerai.StrongestEnemyAI;
import com.lastdefenders.game.model.actor.ai.towerai.WeakestEnemyAI;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import org.junit.Before;
import org.junit.Test;
import testutil.TestUtil;

/**
 * Tests the Tower AI.
 */
public class TowerAITest {


    @Before
    public void initTowerAITest() {

        Gdx.app = mock(Application.class);
    }

    private void createEnemyGroup(Group towerTargetGroup) {

        Enemy enemy1 = createEnemy("enemy1", "Rifle", 700, 10, 2, false, false);
        Enemy enemy2 = createEnemy("enemy2", "MachineGun", 800, 3, 0, true, false);
        Enemy enemy3 = createEnemy("enemy3", "Humvee", 100, 2, 0, true, false);
        Enemy enemy4 = createEnemy("enemy4", "Sniper", 300, 9, 3, true, false);
        Enemy enemy5 = createEnemy("enemy5", "Rifle", 400, 8, 4, false, false);
        Enemy enemy6 = createEnemy("enemy6", "Sniper", 200, 4, 1, false, true);
        Enemy enemy7 = createEnemy("enemy7", "FlameThrower", 500, 11, 0, false, false);
        Enemy enemy8 = createEnemy("enemy8", "Rifle", 400, 8, 6, true, true);
        Enemy enemy9 = createEnemy("enemy9", "MachineGun", 400, 10, 5, false, false);
        Enemy enemy10 = createEnemy("enemy10", "Sniper", 200, 5, 2, false, false);
        Enemy enemy11 = createEnemy("enemy11", "FlameThrower", 700, 4, 0, false, false);

        towerTargetGroup.addActor(enemy1);
        towerTargetGroup.addActor(enemy2);
        towerTargetGroup.addActor(enemy3);
        towerTargetGroup.addActor(enemy4);
        towerTargetGroup.addActor(enemy5);
        towerTargetGroup.addActor(enemy6);
        towerTargetGroup.addActor(enemy7);
        towerTargetGroup.addActor(enemy8);
        towerTargetGroup.addActor(enemy9);
        towerTargetGroup.addActor(enemy10);
        towerTargetGroup.addActor(enemy11);

    }

    private void createEnemyGroupWithTank(Group towerTargetGroup) {

        Enemy enemy1 = createEnemy("enemy1", "Tank", 500, 10, 5, false, false);
        Enemy enemy2 = createEnemy("enemy2", "Tank", 800, 3, 0, false, true);
        Enemy enemy3 = createEnemy("enemy3", "Tank", 100, 2, 0, false, false);
        Enemy enemy4 = createEnemy("enemy4", "Sniper", 300, 9, 5, false, false);
        Enemy enemy5 = createEnemy("enemy5", "Rifle", 400, 8, 6, true, false);
        Enemy enemy6 = createEnemy("enemy6", "Sniper", 200, 4, 1, true, false);
        Enemy enemy7 = createEnemy("enemy7", "FlameThrower", 500, 9, 0, false, false);

        towerTargetGroup.addActor(enemy1);
        towerTargetGroup.addActor(enemy2);
        towerTargetGroup.addActor(enemy3);
        towerTargetGroup.addActor(enemy4);
        towerTargetGroup.addActor(enemy5);
        towerTargetGroup.addActor(enemy6);
        towerTargetGroup.addActor(enemy7);
    }

    private void createEnemyGroupWithOnlyTankInRange(Group towerTargetGroup) {

        Enemy enemy1 = createEnemy("enemy1", "Tank", 400, 10, 10, false, false);
        Enemy enemy2 = createEnemy("enemy2", "Sniper", 500, 12, 6, false, true);
        Enemy enemy3 = createEnemy("enemy3", "Rifle", 100, 2, 0, true, false);

        towerTargetGroup.addActor(enemy1);
        towerTargetGroup.addActor(enemy2);
        towerTargetGroup.addActor(enemy3);
    }

    private Enemy createEnemy(String name, String type, float lengthToEnd, float health,
        float armor, boolean outOfRange, boolean dead) {

        Enemy enemy = TestUtil.createEnemy(type, false);
        if (outOfRange) {
            enemy.setPositionCenter(300, 300);
        }
        enemy.setDead(dead);
        enemy.setName(name);

        Enemy enemySpy = spy(enemy);

        doReturn(lengthToEnd).when(enemySpy).getLengthToEnd();
        doReturn(health).when(enemySpy).getHealth();
        doReturn(armor).when(enemySpy).getArmor();

        return enemySpy;
    }

    /**
     * Finds the Closest Enemy
     */
    @Test
    public void testTowerFindClosest() {

        Tower tower = TestUtil.createTower("Rifle", false);
        Group towerTargetGroup = tower.getTargetGroup();
        createEnemyGroup(towerTargetGroup);

        Enemy enemy = new ClosestEnemyAI().findTarget(tower, towerTargetGroup.getChildren());
        Enemy expectedEnemy = tower.getTargetGroup().findActor("enemy10");

        assertEquals(expectedEnemy, enemy);
    }

    /**
     * Finds the Farthest Enemy
     */
    @Test
    public void testTowerFindFarthest() {

        Tower tower = TestUtil.createTower("FlameThrower", false);
        Group towerTargetGroup = tower.getTargetGroup();
        createEnemyGroup(towerTargetGroup);

        Enemy enemy = new FarthestEnemyAI().findTarget(tower, towerTargetGroup.getChildren());
        Enemy expectedEnemy = towerTargetGroup.findActor("enemy1");

        assertEquals(expectedEnemy, enemy);
    }

    /**
     * Finds the Least HP Enemy
     */
    @Test
    public void testTowerFindLeastHP() {

        Tower tower = TestUtil.createTower("RocketLauncher", false);
        Group towerTargetGroup = tower.getTargetGroup();
        createEnemyGroup(towerTargetGroup);

        Enemy enemy = new LeastHPEnemyAI().findTarget(tower, towerTargetGroup.getChildren());
        Enemy expectedEnemy = tower.getTargetGroup().findActor("enemy11");

        assertEquals(expectedEnemy, enemy);
    }

    /**
     * Finds the Most HP Enemy
     */
    @Test
    public void testTowerFindMostHP() {

        Tower tower = TestUtil.createTower("Tank", false);
        Group towerTargetGroup = tower.getTargetGroup();
        createEnemyGroup(towerTargetGroup);

        Enemy enemy = new MostHPEnemyAI().findTarget(tower, towerTargetGroup.getChildren());
        Enemy expectedEnemy = tower.getTargetGroup().findActor("enemy9");

        assertEquals(expectedEnemy, enemy);
    }

    /**
     * Finds the Closest Enemy and skips the Tank
     */
    @Test
    public void testTowerFindClosestSkipTank() {

        Tower tower = TestUtil.createTower("Rifle", false);
        Group towerTargetGroup = tower.getTargetGroup();
        createEnemyGroupWithTank(towerTargetGroup);

        Enemy enemy = new ClosestEnemyAI().findTarget(tower, towerTargetGroup.getChildren());
        Enemy expectedEnemy = tower.getTargetGroup().findActor("enemy4");

        assertEquals(expectedEnemy, enemy);
    }

    /**
     * Finds the Farthest Enemy and skips the Tank
     */
    @Test
    public void testTowerFindFarthestSkipTank() {

        Tower tower = TestUtil.createTower("FlameThrower", false);
        Group towerTargetGroup = tower.getTargetGroup();
        createEnemyGroupWithTank(towerTargetGroup);

        Enemy enemy = new FarthestEnemyAI().findTarget(tower, towerTargetGroup.getChildren());
        Enemy expectedEnemy = tower.getTargetGroup().findActor("enemy7");

        assertEquals(expectedEnemy, enemy);
    }

    /**
     * Finds the Least HP Enemy and skips the Tank
     */
    @Test
    public void testTowerFindLeastHPSkipTank() {

        Tower tower = TestUtil.createTower("MachineGun", false);
        Group towerTargetGroup = tower.getTargetGroup();
        createEnemyGroupWithTank(towerTargetGroup);

        Enemy enemy = new LeastHPEnemyAI().findTarget(tower, towerTargetGroup.getChildren());
        Enemy expectedEnemy = tower.getTargetGroup().findActor("enemy7");

        assertEquals(expectedEnemy, enemy);
    }

    /**
     * Finds the Most HP Enemy and skips the Tank
     */
    @Test
    public void testTowerFindMostHPSkipTank() {

        Tower tower = TestUtil.createTower("Humvee", false);
        Group towerTargetGroup = tower.getTargetGroup();
        createEnemyGroupWithTank(towerTargetGroup);

        Enemy enemy = new MostHPEnemyAI().findTarget(tower, towerTargetGroup.getChildren());
        Enemy expectedEnemy = tower.getTargetGroup().findActor("enemy4");

        assertEquals(expectedEnemy, enemy);
    }

    /**
     * Finds the Closest Enemy and attacks the Tank
     */
    @Test
    public void testTowerFindClosestAttackTank() {

        Tower tower = TestUtil.createTower("RocketLauncher", false);
        Group towerTargetGroup = tower.getTargetGroup();
        createEnemyGroupWithTank(towerTargetGroup);

        Enemy enemy = new ClosestEnemyAI().findTarget(tower, towerTargetGroup.getChildren());
        Enemy expectedEnemy = tower.getTargetGroup().findActor("enemy3");

        assertEquals(expectedEnemy, enemy);
    }

    /**
     * Finds the Farthest Enemy and attacks the Tank
     */
    @Test
    public void testTowerFindFarthestAttackTank() {

        Tower tower = TestUtil.createTower("Tank", false);
        Group towerTargetGroup = tower.getTargetGroup();
        createEnemyGroupWithTank(towerTargetGroup);

        Enemy enemy = new FarthestEnemyAI().findTarget(tower, towerTargetGroup.getChildren());
        Enemy expectedEnemy = tower.getTargetGroup().findActor("enemy1");

        assertEquals(expectedEnemy, enemy);
    }

    /**
     * Finds the Least HP Enemy and attacks the Tank
     */
    @Test
    public void testTowerFindLeastHPAttackTank() {

        Tower tower = TestUtil.createTower("Tank", false);
        Group towerTargetGroup = tower.getTargetGroup();
        createEnemyGroupWithTank(towerTargetGroup);

        Enemy enemy = new LeastHPEnemyAI().findTarget(tower, towerTargetGroup.getChildren());
        Enemy expectedEnemy = tower.getTargetGroup().findActor("enemy3");

        assertEquals(expectedEnemy, enemy);
    }

    /**
     * Finds the Most HP Enemy and attacks the Tank
     */
    @Test
    public void testTowerFindMostHPAttackTank() {

        Tower tower = TestUtil.createTower("RocketLauncher", false);
        Group towerTargetGroup = tower.getTargetGroup();
        createEnemyGroupWithTank(towerTargetGroup);

        Enemy enemy = new MostHPEnemyAI().findTarget(tower, towerTargetGroup.getChildren());
        Enemy expectedEnemy = tower.getTargetGroup().findActor("enemy1");

        assertEquals(expectedEnemy, enemy);
    }

    /**
     * Finds the Closest Enemy. All enemies are out of range except for tank.
     */
    @Test
    public void testTowerFindClosestOnlyTankInRange() {

        Tower tower = TestUtil.createTower("Rifle", false);
        Group towerTargetGroup = tower.getTargetGroup();
        createEnemyGroupWithOnlyTankInRange(towerTargetGroup);

        Enemy enemy = new ClosestEnemyAI().findTarget(tower, towerTargetGroup.getChildren());
        Enemy expectedEnemy = tower.getTargetGroup().findActor("enemy1");

        assertEquals(expectedEnemy, enemy);
    }

    /**
     * Finds the Farthest Enemy. All enemies are out of range except for tank.
     */
    @Test
    public void testTowerFindFarthestOnlyTankInRange() {

        Tower tower = TestUtil.createTower("MachineGun", false);
        Group towerTargetGroup = tower.getTargetGroup();
        createEnemyGroupWithOnlyTankInRange(towerTargetGroup);

        Enemy enemy = new FarthestEnemyAI().findTarget(tower, towerTargetGroup.getChildren());
        Enemy expectedEnemy = tower.getTargetGroup().findActor("enemy1");

        assertEquals(expectedEnemy, enemy);
    }

    /**
     * Finds the Least HP Enemy. All enemies are out of range except for tank.
     */
    @Test
    public void testTowerFindLeastHPOnlyTankInRange() {

        Tower tower = TestUtil.createTower("Humvee", false);
        Group towerTargetGroup = tower.getTargetGroup();
        createEnemyGroupWithOnlyTankInRange(towerTargetGroup);

        Enemy enemy = new LeastHPEnemyAI().findTarget(tower, towerTargetGroup.getChildren());
        Enemy expectedEnemy = tower.getTargetGroup().findActor("enemy1");

        assertEquals(expectedEnemy, enemy);
    }

    /**
     * Finds the Most HP Enemy. All enemies are out of range except for tank.
     */
    @Test
    public void testTowerFindMostHPOnlyTankInRange() {

        Tower tower = TestUtil.createTower("Rifle", false);
        Group towerTargetGroup = tower.getTargetGroup();
        createEnemyGroupWithOnlyTankInRange(towerTargetGroup);

        Enemy enemy = new MostHPEnemyAI().findTarget(tower, towerTargetGroup.getChildren());
        Enemy expectedEnemy = tower.getTargetGroup().findActor("enemy1");

        assertEquals(expectedEnemy, enemy);
    }

    /**
     * Tower target group is empty.
     */
    @Test
    public void testTargetGroupEmpty() {

        Tower tower = TestUtil.createTower("Rifle", false);
        Group towerTargetGroup = tower.getTargetGroup();

        Enemy enemy = new MostHPEnemyAI().findTarget(tower, towerTargetGroup.getChildren());

        assertNull(enemy);
    }

    /**
     * Target group size is 1 and the enemy is in range
     */
    @Test
    public void testTargetGroupSizeOneInRange() {

        Tower tower = TestUtil.createTower("Rifle", false);
        Group towerTargetGroup = tower.getTargetGroup();

        Enemy enemy1 = createEnemy("enemy1", "Rifle", 10, 10, 5, false, false);
        towerTargetGroup.addActor(enemy1);

        Enemy enemy = new MostHPEnemyAI().findTarget(tower, towerTargetGroup.getChildren());
        Enemy expectedEnemy = tower.getTargetGroup().findActor("enemy1");

        assertEquals(expectedEnemy, enemy);
    }

    /**
     * Target group size is 1 and the enemy is out of range
     */
    @Test
    public void testTargetGroupSizeOneOutOfRange() {

        Tower tower = TestUtil.createTower("Rifle", false);
        Group towerTargetGroup = tower.getTargetGroup();

        Enemy enemy1 = createEnemy("enemy1", "Rifle", 10, 10, 0, true, false);
        towerTargetGroup.addActor(enemy1);
        Enemy enemy = new MostHPEnemyAI().findTarget(tower, towerTargetGroup.getChildren());

        assertNull(enemy);
    }

    /**
     * Test the {@link StrongestEnemyAI}
     */
    @Test
    public void testStrongestEnemy(){
        Tower tower = TestUtil.createTower("Rifle", false);
        Group towerTargetGroup = tower.getTargetGroup();

        Enemy enemy1 = TestUtil.createEnemy("Rifle", false);
        Enemy enemy2 = TestUtil.createEnemy("Sniper", false);
        Enemy enemy3 = TestUtil.createEnemy("RocketLauncher", false);
        Enemy enemy4 = TestUtil.createEnemy("FlameThrower", false);

        towerTargetGroup.addActor(enemy1);
        towerTargetGroup.addActor(enemy2);
        towerTargetGroup.addActor(enemy3);
        towerTargetGroup.addActor(enemy4);


        Enemy enemy = new StrongestEnemyAI().findTarget(tower, towerTargetGroup.getChildren());

        assertEquals(enemy3, enemy);

    }

    /**
     * Test the {@link WeakestEnemyAI}
     */
    @Test
    public void testWeakestEnemy(){
        Tower tower = TestUtil.createTower("Rifle", false);
        Group towerTargetGroup = tower.getTargetGroup();

        Enemy enemy1 = TestUtil.createEnemy("MachineGun", false);
        Enemy enemy2 = TestUtil.createEnemy("Sniper", false);
        Enemy enemy3 = TestUtil.createEnemy("RocketLauncher", false);
        Enemy enemy4 = TestUtil.createEnemy("Rifle", false);
        Enemy enemy5 = TestUtil.createEnemy("FlameThrower", false);

        towerTargetGroup.addActor(enemy1);
        towerTargetGroup.addActor(enemy2);
        towerTargetGroup.addActor(enemy3);
        towerTargetGroup.addActor(enemy4);
        towerTargetGroup.addActor(enemy5);

        Enemy enemy = new WeakestEnemyAI().findTarget(tower, towerTargetGroup.getChildren());

        assertEquals(enemy4, enemy);

    }
}

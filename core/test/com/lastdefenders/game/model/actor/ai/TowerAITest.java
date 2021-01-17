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
import com.lastdefenders.game.model.actor.combat.enemy.EnemyFlameThrower;
import com.lastdefenders.game.model.actor.combat.enemy.EnemyHumvee;
import com.lastdefenders.game.model.actor.combat.enemy.EnemyMachineGun;
import com.lastdefenders.game.model.actor.combat.enemy.EnemyRifle;
import com.lastdefenders.game.model.actor.combat.enemy.EnemyRocketLauncher;
import com.lastdefenders.game.model.actor.combat.enemy.EnemySniper;
import com.lastdefenders.game.model.actor.combat.enemy.EnemyTank;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.model.actor.combat.tower.TowerFlameThrower;
import com.lastdefenders.game.model.actor.combat.tower.TowerHumvee;
import com.lastdefenders.game.model.actor.combat.tower.TowerMachineGun;
import com.lastdefenders.game.model.actor.combat.tower.TowerRifle;
import com.lastdefenders.game.model.actor.combat.tower.TowerRocketLauncher;
import com.lastdefenders.game.model.actor.combat.tower.TowerTank;
import com.lastdefenders.game.model.actor.groups.GenericGroup;
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

        Enemy enemy1 = createEnemy("enemy1", EnemyRifle.class, 700, 10, 2, false, false, true);
        Enemy enemy2 = createEnemy("enemy2", EnemyMachineGun.class, 800, 3, 0, true, false, true);
        Enemy enemy3 = createEnemy("enemy3", EnemyHumvee.class, 100, 2, 0, true, false, true);
        Enemy enemy4 = createEnemy("enemy4", EnemySniper.class, 300, 9, 3, true, false, true);
        Enemy enemy5 = createEnemy("enemy5", EnemyRifle.class, 400, 8, 4, false, false, true);
        Enemy enemy6 = createEnemy("enemy6", EnemySniper.class, 200, 4, 1, false, true, true);
        Enemy enemy7 = createEnemy("enemy7", EnemyFlameThrower.class, 500, 11, 0, false, false, true);
        Enemy enemy8 = createEnemy("enemy8", EnemyRifle.class, 400, 8, 6, true, true, true);
        Enemy enemy9 = createEnemy("enemy9", EnemyMachineGun.class, 400, 10, 5, false, false, true);
        Enemy enemy10 = createEnemy("enemy10", EnemySniper.class, 200, 5, 2, false, false, true);
        Enemy enemy11 = createEnemy("enemy11", EnemyFlameThrower.class, 700, 4, 0, false, false, true);

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

        Enemy enemy1 = createEnemy("enemy1", EnemyTank.class, 500, 10, 5, false, false, true);
        Enemy enemy2 = createEnemy("enemy2", EnemyTank.class, 800, 3, 0, false, true, true);
        Enemy enemy3 = createEnemy("enemy3", EnemyTank.class, 100, 2, 0, false, false, true);
        Enemy enemy4 = createEnemy("enemy4", EnemySniper.class, 300, 9, 5, false, false, true);
        Enemy enemy5 = createEnemy("enemy5", EnemyRifle.class, 400, 8, 6, true, false, true);
        Enemy enemy6 = createEnemy("enemy6", EnemySniper.class, 200, 4, 1, true, false, true);
        Enemy enemy7 = createEnemy("enemy7", EnemyFlameThrower.class, 500, 9, 0, false, false, true);

        towerTargetGroup.addActor(enemy1);
        towerTargetGroup.addActor(enemy2);
        towerTargetGroup.addActor(enemy3);
        towerTargetGroup.addActor(enemy4);
        towerTargetGroup.addActor(enemy5);
        towerTargetGroup.addActor(enemy6);
        towerTargetGroup.addActor(enemy7);
    }

    private void createEnemyGroupWithOnlyTankInRange(Group towerTargetGroup) {

        Enemy enemy1 = createEnemy("enemy1", EnemyTank.class, 400, 10, 10, false, false, true);
        Enemy enemy2 = createEnemy("enemy2", EnemySniper.class, 500, 12, 6, false, true, true);
        Enemy enemy3 = createEnemy("enemy3", EnemyRifle.class, 100, 2, 0, true, false, true);

        towerTargetGroup.addActor(enemy1);
        towerTargetGroup.addActor(enemy2);
        towerTargetGroup.addActor(enemy3);
    }

    private Enemy createEnemy(String name, Class<? extends Enemy> enemyClass, float lengthToEnd, float health,
        float armor, boolean outOfRange, boolean dead, boolean runningState) {

        Enemy enemy;

        if(runningState){
            enemy = TestUtil.createRunningEnemy(enemyClass, false);
        } else {
            enemy = TestUtil.createEnemy(enemyClass, false);
        }

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

        Tower tower = TestUtil.createTower(TowerRifle.class, false, true);
        Group towerTargetGroup = tower.getEnemyGroup();
        createEnemyGroup(towerTargetGroup);

        Enemy enemy = new ClosestEnemyAI().findTarget(tower, towerTargetGroup.getChildren());
        Enemy expectedEnemy = tower.getEnemyGroup().findActor("enemy10");

        assertEquals(expectedEnemy, enemy);
    }

    /**
     * Finds the Farthest Enemy
     */
    @Test
    public void testTowerFindFarthest() {

        Tower tower = TestUtil.createTower(TowerFlameThrower.class, false, true);
        Group towerTargetGroup = tower.getEnemyGroup();
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

        Tower tower = TestUtil.createTower(TowerRocketLauncher.class, false, true);
        Group towerTargetGroup = tower.getEnemyGroup();
        createEnemyGroup(towerTargetGroup);

        Enemy enemy = new LeastHPEnemyAI().findTarget(tower, towerTargetGroup.getChildren());
        Enemy expectedEnemy = tower.getEnemyGroup().findActor("enemy11");

        assertEquals(expectedEnemy, enemy);
    }

    /**
     * Finds the Most HP Enemy
     */
    @Test
    public void testTowerFindMostHP() {

        Tower tower = TestUtil.createTower(TowerTank.class, false, true);
        Group towerTargetGroup = tower.getEnemyGroup();
        createEnemyGroup(towerTargetGroup);

        Enemy enemy = new MostHPEnemyAI().findTarget(tower, towerTargetGroup.getChildren());
        Enemy expectedEnemy = tower.getEnemyGroup().findActor("enemy9");

        assertEquals(expectedEnemy, enemy);
    }

    /**
     * Finds the Closest Enemy and skips the Tank
     */
    @Test
    public void testTowerFindClosestSkipTank() {

        Tower tower = TestUtil.createTower(TowerRifle.class, false, true);
        Group towerTargetGroup = tower.getEnemyGroup();
        createEnemyGroupWithTank(towerTargetGroup);

        Enemy enemy = new ClosestEnemyAI().findTarget(tower, towerTargetGroup.getChildren());
        Enemy expectedEnemy = tower.getEnemyGroup().findActor("enemy4");

        assertEquals(expectedEnemy, enemy);
    }

    /**
     * Finds the Farthest Enemy and skips the Tank
     */
    @Test
    public void testTowerFindFarthestSkipTank() {

        Tower tower = TestUtil.createTower(TowerFlameThrower.class, false, true);
        Group towerTargetGroup = tower.getEnemyGroup();
        createEnemyGroupWithTank(towerTargetGroup);

        Enemy enemy = new FarthestEnemyAI().findTarget(tower, towerTargetGroup.getChildren());
        Enemy expectedEnemy = tower.getEnemyGroup().findActor("enemy7");

        assertEquals(expectedEnemy, enemy);
    }

    /**
     * Finds the Least HP Enemy and skips the Tank
     */
    @Test
    public void testTowerFindLeastHPSkipTank() {

        Tower tower = TestUtil.createTower(TowerMachineGun.class, false, true);
        Group towerTargetGroup = tower.getEnemyGroup();
        createEnemyGroupWithTank(towerTargetGroup);

        Enemy enemy = new LeastHPEnemyAI().findTarget(tower, towerTargetGroup.getChildren());
        Enemy expectedEnemy = tower.getEnemyGroup().findActor("enemy7");

        assertEquals(expectedEnemy, enemy);
    }

    /**
     * Finds the Most HP Enemy and skips the Tank
     */
    @Test
    public void testTowerFindMostHPSkipTank() {

        Tower tower = TestUtil.createTower(TowerHumvee.class, false, true);
        Group towerTargetGroup = tower.getEnemyGroup();
        createEnemyGroupWithTank(towerTargetGroup);

        Enemy enemy = new MostHPEnemyAI().findTarget(tower, towerTargetGroup.getChildren());
        Enemy expectedEnemy = tower.getEnemyGroup().findActor("enemy4");

        assertEquals(expectedEnemy, enemy);
    }

    /**
     * Finds the Closest Enemy and attacks the Tank
     */
    @Test
    public void testTowerFindClosestAttackTank() {

        Tower tower = TestUtil.createTower(TowerRocketLauncher.class, false, true);
        Group towerTargetGroup = tower.getEnemyGroup();
        createEnemyGroupWithTank(towerTargetGroup);

        Enemy enemy = new ClosestEnemyAI().findTarget(tower, towerTargetGroup.getChildren());
        Enemy expectedEnemy = tower.getEnemyGroup().findActor("enemy3");

        assertEquals(expectedEnemy, enemy);
    }

    /**
     * Finds the Farthest Enemy and attacks the Tank
     */
    @Test
    public void testTowerFindFarthestAttackTank() {

        Tower tower = TestUtil.createTower(TowerTank.class, false, true);
        Group towerTargetGroup = tower.getEnemyGroup();
        createEnemyGroupWithTank(towerTargetGroup);

        Enemy enemy = new FarthestEnemyAI().findTarget(tower, towerTargetGroup.getChildren());

        Enemy expectedEnemy = tower.getEnemyGroup().findActor("enemy1");

        assertEquals(expectedEnemy, enemy);
    }

    /**
     * Finds the Least HP Enemy and attacks the Tank
     */
    @Test
    public void testTowerFindLeastHPAttackTank() {

        Tower tower = TestUtil.createTower(TowerTank.class, false, true);
        Group towerTargetGroup = tower.getEnemyGroup();
        createEnemyGroupWithTank(towerTargetGroup);

        Enemy enemy = new LeastHPEnemyAI().findTarget(tower, towerTargetGroup.getChildren());

        Enemy expectedEnemy = tower.getEnemyGroup().findActor("enemy3");

        assertEquals(expectedEnemy, enemy);
    }

    /**
     * Finds the Most HP Enemy and attacks the Tank
     */
    @Test
    public void testTowerFindMostHPAttackTank() {

        Tower tower = TestUtil.createTower(TowerRocketLauncher.class, false, true);
        Group towerTargetGroup = tower.getEnemyGroup();
        createEnemyGroupWithTank(towerTargetGroup);

        Enemy enemy = new MostHPEnemyAI().findTarget(tower, towerTargetGroup.getChildren());

        Enemy expectedEnemy = tower.getEnemyGroup().findActor("enemy1");

        assertEquals(expectedEnemy, enemy);
    }

    /**
     * Finds the Closest Enemy. All enemies are out of range except for tank.
     */
    @Test
    public void testTowerFindClosestOnlyTankInRange() {

        Tower tower = TestUtil.createTower(TowerRifle.class, false, true);
        Group towerTargetGroup = tower.getEnemyGroup();
        createEnemyGroupWithOnlyTankInRange(towerTargetGroup);

        Enemy enemy = new ClosestEnemyAI().findTarget(tower, towerTargetGroup.getChildren());
        Enemy expectedEnemy = tower.getEnemyGroup().findActor("enemy1");

        assertEquals(expectedEnemy, enemy);
    }

    /**
     * Finds the Farthest Enemy. All enemies are out of range except for tank.
     */
    @Test
    public void testTowerFindFarthestOnlyTankInRange() {

        Tower tower = TestUtil.createTower(TowerMachineGun.class, false, true);
        Group towerTargetGroup = tower.getEnemyGroup();
        createEnemyGroupWithOnlyTankInRange(towerTargetGroup);

        Enemy enemy = new FarthestEnemyAI().findTarget(tower, towerTargetGroup.getChildren());

        Enemy expectedEnemy = tower.getEnemyGroup().findActor("enemy1");

        assertEquals(expectedEnemy, enemy);
    }

    /**
     * Finds the Least HP Enemy. All enemies are out of range except for tank.
     */
    @Test
    public void testTowerFindLeastHPOnlyTankInRange() {

        Tower tower = TestUtil.createTower(TowerHumvee.class, false, true);
        GenericGroup<Enemy> towerTargetGroup = tower.getEnemyGroup();
        createEnemyGroupWithOnlyTankInRange(towerTargetGroup);

        Enemy enemy = new LeastHPEnemyAI().findTarget(tower, towerTargetGroup.getChildren());
        Enemy expectedEnemy = tower.getEnemyGroup().findActor("enemy1");

        assertEquals(expectedEnemy, enemy);
    }

    /**
     * Finds the Most HP Enemy. All enemies are out of range except for tank.
     */
    @Test
    public void testTowerFindMostHPOnlyTankInRange() {

        Tower tower = TestUtil.createTower(TowerRifle.class, false, true);
        Group towerTargetGroup = tower.getEnemyGroup();
        createEnemyGroupWithOnlyTankInRange(towerTargetGroup);

        Enemy enemy = new MostHPEnemyAI().findTarget(tower, towerTargetGroup.getChildren());

        Enemy expectedEnemy = tower.getEnemyGroup().findActor("enemy1");

        assertEquals(expectedEnemy, enemy);
    }

    /**
     * Tower target group is empty.
     */
    @Test
    public void testTargetGroupEmpty() {

        Tower tower = TestUtil.createTower(TowerRifle.class, false, true);
        Group towerTargetGroup = tower.getEnemyGroup();

        Enemy enemy = new MostHPEnemyAI().findTarget(tower, towerTargetGroup.getChildren());

        assertNull(enemy);
    }

    /**
     * Target group size is 1 and the enemy is in range
     */
    @Test
    public void testTargetGroupSizeOneInRange() {

        Tower tower = TestUtil.createTower(TowerRifle.class, false, true);
        Group towerTargetGroup = tower.getEnemyGroup();

        Enemy enemy1 = createEnemy("enemy1", EnemyRifle.class, 10, 10, 5, false, false, true);
        towerTargetGroup.addActor(enemy1);

        Enemy enemy = new MostHPEnemyAI().findTarget(tower, towerTargetGroup.getChildren());
        Enemy expectedEnemy = tower.getEnemyGroup().findActor("enemy1");

        assertEquals(expectedEnemy, enemy);
    }

    /**
     * Target group size is 1 and the enemy is out of range
     */
    @Test
    public void testTargetGroupSizeOneOutOfRange() {

        Tower tower = TestUtil.createTower(TowerRifle.class, false, true);
        Group towerTargetGroup = tower.getEnemyGroup();

        Enemy enemy1 = createEnemy("enemy1", EnemyRifle.class, 10, 10, 0, true, false, true);
        towerTargetGroup.addActor(enemy1);
        Enemy enemy = new MostHPEnemyAI().findTarget(tower, towerTargetGroup.getChildren());

        assertNull(enemy);
    }

    /**
     * Test the {@link StrongestEnemyAI}
     */
    @Test
    public void testStrongestEnemy(){
        Tower tower = TestUtil.createTower(TowerRifle.class, false, true);
        Group towerTargetGroup = tower.getEnemyGroup();

        Enemy enemy1 = TestUtil.createRunningEnemy(EnemyRifle.class, false);
        Enemy enemy2 = TestUtil.createRunningEnemy(EnemySniper.class, false);
        Enemy enemy3 = TestUtil.createRunningEnemy(EnemyRocketLauncher.class, false);
        Enemy enemy4 = TestUtil.createRunningEnemy(EnemyFlameThrower.class, false);

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
        Tower tower = TestUtil.createTower(TowerRifle.class, false, true);
        Group towerTargetGroup = tower.getEnemyGroup();

        Enemy enemy1 = TestUtil.createRunningEnemy(EnemyMachineGun.class, false);
        Enemy enemy2 = TestUtil.createRunningEnemy(EnemySniper.class, false);
        Enemy enemy3 = TestUtil.createRunningEnemy(EnemyRocketLauncher.class, false);
        Enemy enemy4 = TestUtil.createRunningEnemy(EnemyRifle.class, false);
        Enemy enemy5 = TestUtil.createRunningEnemy(EnemyFlameThrower.class, false);

        towerTargetGroup.addActor(enemy1);
        towerTargetGroup.addActor(enemy2);
        towerTargetGroup.addActor(enemy3);
        towerTargetGroup.addActor(enemy4);
        towerTargetGroup.addActor(enemy5);

        Enemy enemy = new WeakestEnemyAI().findTarget(tower, towerTargetGroup.getChildren());

        assertEquals(enemy4, enemy);

    }
}

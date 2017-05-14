package game.model.actor.ai;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.foxholedefense.game.model.actor.ai.towerai.FirstEnemyAI;
import com.foxholedefense.game.model.actor.ai.towerai.LastEnemyAI;
import com.foxholedefense.game.model.actor.ai.towerai.LeastHPEnemyAI;
import com.foxholedefense.game.model.actor.ai.towerai.MostHPEnemyAI;
import com.foxholedefense.game.model.actor.combat.CombatActor;
import com.foxholedefense.game.model.actor.combat.enemy.Enemy;
import com.foxholedefense.game.model.actor.combat.tower.Tower;
import com.foxholedefense.util.Logger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import org.powermock.api.mockito.PowerMockito;
import static org.mockito.Mockito.*;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import util.TestUtil;

/**
 * Tests the Tower AI.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Logger.class)
public class TowerAITest {


    @Before
    public void initTowerAITest(){
        PowerMockito.mockStatic(Logger.class);
    }

    private void createEnemyGroup(Group towerTargetGroup){

        Enemy enemy1 = createEnemy("enemy1", "EnemyRifle", 700, 10, false, false);
        Enemy enemy2 = createEnemy("enemy2", "EnemyMachineGun", 800, 3, true, false);
        Enemy enemy3 = createEnemy("enemy3", "EnemyHumvee", 100, 2, true, false);
        Enemy enemy4 = createEnemy("enemy4", "EnemySniper", 300, 9, true, false);
        Enemy enemy5 = createEnemy("enemy5", "EnemyRifle", 400, 8, false, false);
        Enemy enemy6 = createEnemy("enemy6", "EnemySniper", 200, 4, false, true);
        Enemy enemy7 = createEnemy("enemy7", "EnemyFlameThrower", 500, 11, false, false);
        Enemy enemy8 = createEnemy("enemy8", "EnemyRifle", 400, 8, true, true);
        Enemy enemy9 = createEnemy("enemy9", "EnemySniper", 200, 5, false, false);
        Enemy enemy10 = createEnemy("enemy10", "EnemyFlameThrower", 700, 4, false, false);

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

    }

    private void createEnemyGroupWithTank(Group towerTargetGroup){

        Enemy enemy1 = createEnemy("enemy1", "EnemyTank", 500, 10, false, false);
        Enemy enemy2 = createEnemy("enemy2", "EnemyTank", 800, 3, false, true);
        Enemy enemy3 = createEnemy("enemy3", "EnemyTank", 100, 2, false, false);
        Enemy enemy4 = createEnemy("enemy4", "EnemySniper", 300, 9, false, false);
        Enemy enemy5 = createEnemy("enemy5", "EnemyRifle", 400, 8, true, false);
        Enemy enemy6 = createEnemy("enemy6", "EnemySniper", 200, 4, true, false);
        Enemy enemy7 = createEnemy("enemy7", "EnemyFlameThrower", 500, 9, false, false);

        towerTargetGroup.addActor(enemy1);
        towerTargetGroup.addActor(enemy2);
        towerTargetGroup.addActor(enemy3);
        towerTargetGroup.addActor(enemy4);
        towerTargetGroup.addActor(enemy5);
        towerTargetGroup.addActor(enemy6);
        towerTargetGroup.addActor(enemy7);
    }

    private void createEnemyGroupWithOnlyTankInRange(Group towerTargetGroup){
        Enemy enemy1 = createEnemy("enemy1", "EnemyTank", 400, 10, false, false);
        Enemy enemy2 = createEnemy("enemy2", "EnemySniper", 500, 12, false, true);
        Enemy enemy3 = createEnemy("enemy3", "EnemyRifle", 100, 2, true, false);

        towerTargetGroup.addActor(enemy1);
        towerTargetGroup.addActor(enemy2);
        towerTargetGroup.addActor(enemy3);
    }

    private Enemy createEnemy( String name, String type, float lengthToEnd, float health, boolean outOfRange, boolean dead ){
        Enemy enemy = TestUtil.createEnemy(type);
        if(outOfRange) {
            enemy.setPositionCenter(300, 300);
        }
        enemy.setDead(dead);
        enemy.setName(name);

        Enemy enemySpy = spy(enemy);

        doReturn(lengthToEnd).when(enemySpy).getLengthToEnd();
        doReturn(health).when(enemySpy).getHealth();

        return enemySpy;
    }

    /**
     * Finds the First Enemy
     */
    @Test
    public void testTowerFindFirst(){
        Tower tower = TestUtil.createTower("Rifle", false);
        Group towerTargetGroup = tower.getTargetGroup();
        createEnemyGroup(towerTargetGroup);

        Enemy enemy = new FirstEnemyAI().findTarget(tower, towerTargetGroup.getChildren());
        Enemy expectedEnemy = tower.getTargetGroup().findActor("enemy9");

        assertEquals(expectedEnemy, enemy);
    }

    /**
     * Finds the Last Enemy
     */
    @Test
    public void testTowerFindLast(){
        Tower tower = TestUtil.createTower("FlameThrower", false);
        Group towerTargetGroup = tower.getTargetGroup();
        createEnemyGroup(towerTargetGroup);

        Enemy enemy = new LastEnemyAI().findTarget(tower, towerTargetGroup.getChildren());
        Enemy expectedEnemy = towerTargetGroup.findActor("enemy1");

        assertEquals(expectedEnemy, enemy);
    }

    /**
     * Finds the Least HP Enemy
     */
    @Test
    public void testTowerFindLeastHP(){
        Tower tower = TestUtil.createTower("RocketLauncher", false);
        Group towerTargetGroup = tower.getTargetGroup();
        createEnemyGroup(towerTargetGroup);

        Enemy enemy = new LeastHPEnemyAI().findTarget(tower, towerTargetGroup.getChildren());
        Enemy expectedEnemy = tower.getTargetGroup().findActor("enemy10");

        assertEquals(expectedEnemy, enemy);
    }

    /**
     * Finds the Most HP Enemy
     */
    @Test
    public void testTowerFindMostHP(){
        Tower tower = TestUtil.createTower("Tank", false);
        Group towerTargetGroup = tower.getTargetGroup();
        createEnemyGroup(towerTargetGroup);

        Enemy enemy = new MostHPEnemyAI().findTarget(tower, towerTargetGroup.getChildren());
        Enemy expectedEnemy = tower.getTargetGroup().findActor("enemy7");

        assertEquals(expectedEnemy, enemy);
    }

    /**
     * Finds the First Enemy and skips the Tank
     */
    @Test
    public void testTowerFindFirstSkipTank(){
        Tower tower = TestUtil.createTower("Rifle", false);
        Group towerTargetGroup = tower.getTargetGroup();
        createEnemyGroupWithTank(towerTargetGroup);

        Enemy enemy = new FirstEnemyAI().findTarget(tower, towerTargetGroup.getChildren());
        Enemy expectedEnemy = tower.getTargetGroup().findActor("enemy4");

        assertEquals(expectedEnemy, enemy);
    }

    /**
     * Finds the Last Enemy and skips the Tank
     */
    @Test
    public void testTowerFindLastSkipTank(){
        Tower tower = TestUtil.createTower("FlameThrower", false);
        Group towerTargetGroup = tower.getTargetGroup();
        createEnemyGroupWithTank(towerTargetGroup);

        Enemy enemy = new LastEnemyAI().findTarget(tower, towerTargetGroup.getChildren());
        Enemy expectedEnemy = tower.getTargetGroup().findActor("enemy7");

        assertEquals(expectedEnemy, enemy);
    }

    /**
     * Finds the Least HP Enemy and skips the Tank
     */
    @Test
    public void testTowerFindLeastHPSkipTank(){
        Tower tower = TestUtil.createTower("MachineGun", false);
        Group towerTargetGroup = tower.getTargetGroup();
        createEnemyGroupWithTank(towerTargetGroup);

        Enemy enemy = new LeastHPEnemyAI().findTarget(tower,towerTargetGroup.getChildren());
        Enemy expectedEnemy = tower.getTargetGroup().findActor("enemy4");

        assertEquals(expectedEnemy, enemy);
    }

    /**
     * Finds the Most HP Enemy and skips the Tank
     */
    @Test
    public void testTowerFindMostHPSkipTank(){
        Tower tower = TestUtil.createTower("Turret", false);
        Group towerTargetGroup = tower.getTargetGroup();
        createEnemyGroupWithTank(towerTargetGroup);

        Enemy enemy = new MostHPEnemyAI().findTarget(tower, towerTargetGroup.getChildren());
        Enemy expectedEnemy = tower.getTargetGroup().findActor("enemy4");

        for(Actor actor : towerTargetGroup.getChildren()){
            System.out.println(actor.getName() + ((CombatActor)actor).getPositionCenter());
        }

        assertEquals(expectedEnemy, enemy);
    }

    /**
     * Finds the First Enemy and attacks the Tank
     */
    @Test
    public void testTowerFindFirstAttackTank(){
        Tower tower = TestUtil.createTower("RocketLauncher", false);
        Group towerTargetGroup = tower.getTargetGroup();
        createEnemyGroupWithTank(towerTargetGroup);

        Enemy enemy = new FirstEnemyAI().findTarget(tower, towerTargetGroup.getChildren());
        Enemy expectedEnemy = tower.getTargetGroup().findActor("enemy3");

        assertEquals(expectedEnemy, enemy);
    }

    /**
     * Finds the Last Enemy and attacks the Tank
     */
    @Test
    public void testTowerFindLastAttackTank(){
        Tower tower = TestUtil.createTower("Tank", false);
        Group towerTargetGroup = tower.getTargetGroup();
        createEnemyGroupWithTank(towerTargetGroup);

        Enemy enemy = new LastEnemyAI().findTarget(tower, towerTargetGroup.getChildren());
        Enemy expectedEnemy = tower.getTargetGroup().findActor("enemy1");

        assertEquals(expectedEnemy, enemy);
    }

    /**
     * Finds the Least HP Enemy and attacks the Tank
     */
    @Test
    public void testTowerFindLeastHPAttackTank(){
        Tower tower = TestUtil.createTower("Tank", false);
        Group towerTargetGroup = tower.getTargetGroup();
        createEnemyGroupWithTank(towerTargetGroup);

        Enemy enemy = new LeastHPEnemyAI().findTarget(tower,towerTargetGroup.getChildren());
        Enemy expectedEnemy = tower.getTargetGroup().findActor("enemy3");

        assertEquals(expectedEnemy, enemy);
    }

    /**
     * Finds the Most HP Enemy and attacks the Tank
     */
    @Test
    public void testTowerFindMostHPAttackTank(){
        Tower tower = TestUtil.createTower("RocketLauncher", false);
        Group towerTargetGroup = tower.getTargetGroup();
        createEnemyGroupWithTank(towerTargetGroup);

        Enemy enemy = new MostHPEnemyAI().findTarget(tower, towerTargetGroup.getChildren());
        Enemy expectedEnemy = tower.getTargetGroup().findActor("enemy1");

        assertEquals(expectedEnemy, enemy);
    }

    /**
     * Finds the First Enemy. All enemies are out of range except for tank.
     */
    @Test
    public void testTowerFindFirstOnlyTankInRange(){
        Tower tower = TestUtil.createTower("Rifle", false);
        Group towerTargetGroup = tower.getTargetGroup();
        createEnemyGroupWithOnlyTankInRange(towerTargetGroup);

        Enemy enemy = new FirstEnemyAI().findTarget(tower, towerTargetGroup.getChildren());
        Enemy expectedEnemy = tower.getTargetGroup().findActor("enemy1");

        assertEquals(expectedEnemy, enemy);
    }

    /**
     * Finds the Last Enemy. All enemies are out of range except for tank.
     */
    @Test
    public void testTowerFindLastOnlyTankInRange(){
        Tower tower = TestUtil.createTower("MachineGun", false);
        Group towerTargetGroup = tower.getTargetGroup();
        createEnemyGroupWithOnlyTankInRange(towerTargetGroup);

        Enemy enemy = new LastEnemyAI().findTarget(tower, towerTargetGroup.getChildren());
        Enemy expectedEnemy = tower.getTargetGroup().findActor("enemy1");

        assertEquals(expectedEnemy, enemy);
    }

    /**
     * Finds the Least HP Enemy. All enemies are out of range except for tank.
     */
    @Test
    public void testTowerFindLeastHPOnlyTankInRange(){
        Tower tower = TestUtil.createTower("Turret", false);
        Group towerTargetGroup = tower.getTargetGroup();
        createEnemyGroupWithOnlyTankInRange(towerTargetGroup);

        Enemy enemy = new LeastHPEnemyAI().findTarget(tower,towerTargetGroup.getChildren());
        Enemy expectedEnemy = tower.getTargetGroup().findActor("enemy1");

        assertEquals(expectedEnemy, enemy);
    }

    /**
     * Finds the Most HP Enemy. All enemies are out of range except for tank.
     */
    @Test
    public void testTowerFindMostHPOnlyTankInRange(){
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
    public void testTargetGroupEmpty(){
        Tower tower = TestUtil.createTower("Rifle", false);
        Group towerTargetGroup = tower.getTargetGroup();

        Enemy enemy = new MostHPEnemyAI().findTarget(tower, towerTargetGroup.getChildren());

        assertNull(enemy);
    }

    /**
     * Target group size is 1 and the enemy is in range
     */
    @Test
    public void testTargetGroupSizeOneInRange(){
        Tower tower = TestUtil.createTower("Rifle", false);
        Group towerTargetGroup = tower.getTargetGroup();

        Enemy enemy1 = createEnemy("enemy1", "EnemyRifle", 10, 10, false, false);
        towerTargetGroup.addActor(enemy1);

        Enemy enemy = new MostHPEnemyAI().findTarget(tower, towerTargetGroup.getChildren());
        Enemy expectedEnemy = tower.getTargetGroup().findActor("enemy1");

        assertEquals(expectedEnemy, enemy);
    }

    /**
     * Target group size is 1 and the enemy is out of range
     */
    @Test
    public void testTargetGroupSizeOneOutOfRange(){
        Tower tower = TestUtil.createTower("Rifle", false);
        Group towerTargetGroup = tower.getTargetGroup();

        Enemy enemy1 = createEnemy("enemy1", "EnemyRifle", 10, 10, true, false);
        towerTargetGroup.addActor(enemy1);
        Enemy enemy = new MostHPEnemyAI().findTarget(tower, towerTargetGroup.getChildren());

        assertNull(enemy);
    }



}

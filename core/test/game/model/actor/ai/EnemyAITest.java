package game.model.actor.ai;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.foxholedefense.game.model.actor.ai.EnemyAI;
import com.foxholedefense.game.model.actor.combat.enemy.Enemy;
import com.foxholedefense.game.model.actor.combat.tower.Tower;

import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


import testutil.TestUtil;


/**
 * Tests the Tower AI.
 */
public class EnemyAITest {

    @Before
    public void initEnemyAITest(){
        Gdx.app = mock(Application.class);
    }

    private void createTowerGroup(Group enemyTargetGroup){
        Tower tower1 = createTower("tower1", "Rifle", new Vector2(10,10), false, false, true);
        Tower tower2 = createTower("tower2", "FlameThrower", new Vector2(2,2), false, true, true);
        Tower tower3 = createTower("tower3", "MachineGun", new Vector2(5,5), false, false, true);
        Tower tower4 = createTower("tower4", "Sniper", new Vector2(2,2), false, false, false);
        Tower tower5 = createTower("tower5", "Sniper", new Vector2(2,2), false, true, false);
        Tower tower6 = createTower("tower6", "Rifle", null, true, false, true);
        Tower tower7 = createTower("tower7", "Rifle", null, true, true, true);
        Tower tower8 = createTower("tower8", "Rifle", null, true, false, false);
        Tower tower9 = createTower("tower9", "Rifle", null, true, true, false);
        Tower tower10 = createTower("tower10", "Tank", new Vector2(1,1), false, false, true);

        enemyTargetGroup.addActor(tower1);
        enemyTargetGroup.addActor(tower2);
        enemyTargetGroup.addActor(tower3);
        enemyTargetGroup.addActor(tower4);
        enemyTargetGroup.addActor(tower5);
        enemyTargetGroup.addActor(tower6);
        enemyTargetGroup.addActor(tower7);
        enemyTargetGroup.addActor(tower8);
        enemyTargetGroup.addActor(tower9);
        enemyTargetGroup.addActor(tower10);
    }

    private Tower createTower(String name, String type, Vector2 centerPos, boolean outOfRange, boolean dead, boolean active){
        Tower tower = TestUtil.createTower(type, true);
        if(outOfRange){
            tower.setPositionCenter(300,300);
        } else {
            tower.setPositionCenter(centerPos);
        }
        tower.setDead(dead);
        tower.setActive(active);

        //Tower Impls overrides getName
        doReturn(name).when(tower).getName();

        return tower;
    }

    /**
     * Finds the nearest tower
     */
    @Test
    public void testEnemyFindNearestSkipTank(){
        Enemy enemy = TestUtil.createEnemy("Rifle", false);
        enemy.setPositionCenter(new Vector2(0,0));
        Group enemyTargetGroup = enemy.getTargetGroup();
        createTowerGroup(enemyTargetGroup);

        Tower tower = EnemyAI.findNearestTower(enemy, enemyTargetGroup.getChildren());
        Tower expectedTower = enemyTargetGroup.findActor("tower3");

        assertEquals(expectedTower, tower);

    }

    /**
     * Finds the nearest tower
     */
    @Test
    public void testEnemyFindNearestAttackTank(){
        Enemy enemy = TestUtil.createEnemy("RocketLauncher", false);
        enemy.setPositionCenter(new Vector2(0,0));
        Group enemyTargetGroup = enemy.getTargetGroup();
        createTowerGroup(enemyTargetGroup);

        Tower tower = EnemyAI.findNearestTower(enemy, enemyTargetGroup.getChildren());
        Tower expectedTower = enemyTargetGroup.findActor("tower10");

        assertEquals(expectedTower, tower);

    }
}

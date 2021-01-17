package com.lastdefenders.game.model.actor.ai;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.actor.combat.enemy.EnemyRifle;
import com.lastdefenders.game.model.actor.combat.enemy.EnemyRocketLauncher;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.model.actor.combat.tower.TowerFlameThrower;
import com.lastdefenders.game.model.actor.combat.tower.TowerMachineGun;
import com.lastdefenders.game.model.actor.combat.tower.TowerRifle;
import com.lastdefenders.game.model.actor.combat.tower.TowerSniper;
import com.lastdefenders.game.model.actor.combat.tower.TowerTank;
import com.lastdefenders.game.model.actor.groups.GenericGroup;
import org.junit.Before;
import org.junit.Test;
import testutil.TestUtil;


/**
 * Tests the Tower AI.
 */
public class EnemyAITest {

    @Before
    public void initEnemyAITest() {

        Gdx.app = mock(Application.class);
    }

    private void createTowerGroup(Group enemyTargetGroup) {

        Tower tower1 = createTower("tower1", TowerRifle.class, new Vector2(10, 10), false, false, true);
        Tower tower2 = createTower("tower2", TowerFlameThrower.class, new Vector2(2, 2), false, true, true);
        Tower tower3 = createTower("tower3", TowerMachineGun.class, new Vector2(5, 5), false, false, true);
        Tower tower4 = createTower("tower4", TowerSniper.class, new Vector2(2, 2), false, false, false);
        Tower tower5 = createTower("tower5", TowerSniper.class, new Vector2(2, 2), false, true, false);
        Tower tower6 = createTower("tower6", TowerRifle.class, null, true, false, true);
        Tower tower7 = createTower("tower7", TowerRifle.class, null, true, true, true);
        Tower tower8 = createTower("tower8", TowerRifle.class, null, true, false, false);
        Tower tower9 = createTower("tower9", TowerRifle.class, null, true, true, false);
        Tower tower10 = createTower("tower10", TowerTank.class, new Vector2(1, 1), false, false, true);

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

    private Tower createTower(String name, Class<? extends Tower> towerClass, Vector2 centerPos, boolean outOfRange,
        boolean dead, boolean active) {

        Tower tower = TestUtil.createTower(towerClass, true, true);
        if (outOfRange) {
            tower.setPositionCenter(300, 300);
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
    public void testEnemyFindNearestSkipTank() {

        Enemy enemy = TestUtil.createEnemy(EnemyRifle.class, false);
        enemy.setPositionCenter(new Vector2(0, 0));
        GenericGroup<Tower> enemyTargetGroup = enemy.getEnemyGroup();
        createTowerGroup(enemyTargetGroup);

        Tower tower = EnemyAI.findNearestTower(enemy, enemyTargetGroup.getCastedChildren());
        Tower expectedTower = enemyTargetGroup.findActor("tower3");

        assertEquals(expectedTower, tower);

    }

    /**
     * Finds the nearest tower
     */
    @Test
    public void testEnemyFindNearestAttackTank() {

        Enemy enemy = TestUtil.createEnemy(EnemyRocketLauncher.class, false);
        enemy.setPositionCenter(new Vector2(0, 0));
        GenericGroup<Tower> enemyTargetGroup = enemy.getEnemyGroup();
        createTowerGroup(enemyTargetGroup);

        Tower tower = EnemyAI.findNearestTower(enemy, enemyTargetGroup.getCastedChildren());
        Tower expectedTower = enemyTargetGroup.findActor("tower10");

        assertEquals(expectedTower, tower);

    }
}

package tower.ai;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.foxholedefense.game.model.actor.ai.towerai.FirstEnemyAI;
import com.foxholedefense.game.model.actor.ai.towerai.LastEnemyAI;
import com.foxholedefense.game.model.actor.ai.towerai.LeastHPEnemyAI;
import com.foxholedefense.game.model.actor.ai.towerai.MostHPEnemyAI;
import com.foxholedefense.game.model.actor.combat.enemy.Enemy;
import com.foxholedefense.game.model.actor.combat.enemy.EnemyRifle;
import com.foxholedefense.game.model.actor.combat.tower.Tower;
import com.foxholedefense.game.model.actor.combat.tower.TowerRifle;
import com.foxholedefense.game.service.factory.CombatActorFactory.CombatActorPool;
import com.foxholedefense.util.Logger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import static org.mockito.Mockito.*;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import util.TestUtil;

/**
 * Created by Eric on 4/23/2017.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest(Logger.class)
public class AITest {

    private Tower tower;



    @Before
    public void initTowerTest(){
        PowerMockito.mockStatic(Logger.class);
        tower = TestUtil.createTower("Rifle");
        createEnemyGroup(tower.getTargetGroup());
    }

    private Group createEnemyGroup(Group towerTargetGroup){


        Enemy enemy1 = createEnemy("enemy1", 500, 10);
        Enemy enemy2 = createEnemy("enemy2", 800, 3);
        Enemy enemy3 = createEnemy("enemy3", 100, 2);
        Enemy enemy4 = createEnemy("enemy4", 300, 9);

        towerTargetGroup.addActor(enemy1);
        towerTargetGroup.addActor(enemy2);
        towerTargetGroup.addActor(enemy3);
        towerTargetGroup.addActor(enemy4);

        return towerTargetGroup;
    }

    private Enemy createEnemy( String name, float lengthToEnd, float health ){
        Enemy enemy = TestUtil.createEnemy("EnemyRifle");
        Enemy enemySpy = spy(enemy);
        enemySpy.setName(name);
        doReturn(lengthToEnd).when(enemySpy).getLengthToEnd();
        doReturn(health).when(enemySpy).getHealth();

        return enemySpy;
    }

    /**
     * Finds the First Enemy
     */
    @Test
    public void testTowerFindFirst(){
        Enemy enemy = new FirstEnemyAI().findTarget(tower, tower.getTargetGroup().getChildren());
        Enemy expectedEnemy = tower.getTargetGroup().findActor("enemy3");
        assertEquals(expectedEnemy, enemy);
    }

    /**
     * Finds the Last Enemy
     */
    @Test
    public void testTowerFindLast(){
        Enemy enemy = new LastEnemyAI().findTarget(tower, tower.getTargetGroup().getChildren());
        Enemy expectedEnemy = tower.getTargetGroup().findActor("enemy2");
        assertEquals(expectedEnemy, enemy);
    }

    /**
     * Finds the Least HP Enemy
     */
    @Test
    public void testTowerFindLeastHP(){
        Enemy enemy = new LeastHPEnemyAI().findTarget(tower, tower.getTargetGroup().getChildren());
        Enemy expectedEnemy = tower.getTargetGroup().findActor("enemy3");
        assertEquals(expectedEnemy, enemy);
    }

    /**
     * Finds the Most HP Enemy
     */
    @Test
    public void testTowerFindMostHP(){
        Enemy enemy = new MostHPEnemyAI().findTarget(tower, tower.getTargetGroup().getChildren());
        Enemy expectedEnemy = tower.getTargetGroup().findActor("enemy1");
        assertEquals(expectedEnemy, enemy);
    }
}

package com.lastdefenders.util;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.actor.combat.enemy.EnemyRocketLauncher;
import com.lastdefenders.game.model.actor.combat.enemy.EnemyTank;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.model.actor.combat.tower.TowerFlameThrower;
import com.lastdefenders.game.model.actor.combat.tower.TowerHumvee;
import com.lastdefenders.game.model.actor.combat.tower.TowerRifle;
import com.lastdefenders.util.datastructures.pool.LDVector2;
import org.junit.Before;
import org.junit.Test;
import testutil.TestUtil;

/**
 * Created by Eric on 6/14/2017.
 */

public class ActorUtilTest {

    @Before
    public void initActorUtilTest() {

        Gdx.app = mock(Application.class);
    }

    @Test
    public void calcBotLeftPointFromCenterTest() {

        Tower tower = TestUtil.createTower(TowerFlameThrower.class, false, true);
        tower.setPositionCenter(100, 200);

        float center = ActorUtil
            .calcBotLeftPointFromCenter(tower.getPositionCenter().x, tower.getWidth());

        assertEquals(72.0f, center, TestUtil.DELTA);
    }

    @Test
    public void calculateRotationTest() {

        Tower tower = TestUtil.createTower(TowerHumvee.class, false, true);
        tower.setPositionCenter(100, 100);

        Enemy enemy = TestUtil.createEnemy(EnemyTank.class, false);
        enemy.setPositionCenter(200, 200);

        float rotation = ActorUtil
            .calculateRotation(enemy.getPositionCenter(), tower.getPositionCenter());

        assertEquals(45.0f, rotation, TestUtil.DELTA);
    }

    @Test
    public void calculateRotatedCoordsTest() {

        Tower tower = TestUtil.createTower(TowerRifle.class, false, true);
        tower.setPosition(75, 60);
        tower.setRotation(.5f);

        Enemy enemy = TestUtil.createEnemy(EnemyRocketLauncher.class, false);
        enemy.setPositionCenter(200, 200);

        float targetX = 80;
        float targetY = 75;

        LDVector2 rotatedCoords = ActorUtil.calculateRotatedCoords(targetX, targetY,
            enemy.getPositionCenter().x, enemy.getPositionCenter().y,
            Math.toRadians(tower.getRotation()));

        LDVector2 expectedCoords = new LDVector2(81.09539f, 73.95757f);

        assertEquals(expectedCoords, rotatedCoords);
    }

}

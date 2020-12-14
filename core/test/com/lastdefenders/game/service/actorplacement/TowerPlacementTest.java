package com.lastdefenders.game.service.actorplacement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.when;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;
import com.lastdefenders.game.helper.CollisionDetection;
import com.lastdefenders.game.model.actor.groups.ActorGroups;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.model.actor.combat.tower.TowerRifle;
import com.lastdefenders.game.model.actor.groups.TowerGroup;
import com.lastdefenders.game.model.level.Map;
import com.lastdefenders.game.service.factory.CombatActorFactory;
import com.lastdefenders.game.service.factory.HealthFactory;
import com.lastdefenders.util.datastructures.pool.LDVector2;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import testutil.TestUtil;

/**
 * Created by Eric on 5/28/2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({CollisionDetection.class})
public class TowerPlacementTest {

    private Map map = mock(Map.class);
    private ActorGroups actorGroups = mock(ActorGroups.class);
    private CombatActorFactory combatActorFactory = mock(CombatActorFactory.class);
    private HealthFactory healthFactory = mock(HealthFactory.class);

    @Before
    public void initTowerPlacementTest() {

        Gdx.app = mock(Application.class);
        PowerMockito.mockStatic(CollisionDetection.class);
    }

    public TowerPlacement createTowerPlacement() {

        return new TowerPlacement(map, actorGroups, combatActorFactory, healthFactory);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void towerPlacementTest1() {

        TowerPlacement towerPlacement = createTowerPlacement();

        // Create tower
        TowerGroup towerGroup = new TowerGroup();
        doReturn(towerGroup).when(actorGroups).getTowerGroup();

        Tower tower = TestUtil.createTower(TowerRifle.class, false, true);
        doReturn(tower).when(combatActorFactory).loadTower(eq("Rifle"));

        towerPlacement.createTower("Rifle");

        assertFalse(tower.isVisible());
        assertEquals(0, tower.getX(), TestUtil.DELTA);
        assertEquals(0, tower.getY(), TestUtil.DELTA);

        // Move tower and mock collision detection
        Array<Rectangle> pathBoundaries = new Array<>();
        doReturn(pathBoundaries).when(map).getPathBoundaries();
        when(CollisionDetection.collisionWithPath(eq(pathBoundaries), eq(tower))).thenReturn(false);
        when(CollisionDetection.collisionWithActors(isA(SnapshotArray.class), eq(tower)))
            .thenReturn(false);

        LDVector2 moveCoords = new LDVector2(200, 100);
        towerPlacement.moveTower(moveCoords);

        assertTrue(tower.isVisible());
        assertTrue(tower.isShowRange());
        assertEquals(moveCoords, tower.getPositionCenter());
        assertFalse(tower.isTowerColliding());

        // rotate tower
        float rotation = 70;
        towerPlacement.rotateTower(rotation);
        assertEquals(-rotation, tower.getRotation(), TestUtil.DELTA);



        boolean placed = towerPlacement.placeTower();

        assertTrue(placed);

    }

    @Test
    @SuppressWarnings("unchecked")
    public void towerPlacementCollisionTest() {

        TowerPlacement towerPlacement = createTowerPlacement();

        // Create tower
        TowerGroup towerGroup = new TowerGroup();
        doReturn(towerGroup).when(actorGroups).getTowerGroup();

        Tower tower = TestUtil.createTower(TowerRifle.class, false, true);
        doReturn(tower).when(combatActorFactory).loadTower(eq("Rifle"));

        towerPlacement.createTower("Rifle");

        assertFalse(tower.isVisible());
        assertEquals(0, tower.getX(), TestUtil.DELTA);
        assertEquals(0, tower.getY(), TestUtil.DELTA);

        // Move tower and mock collision detection
        Array<Rectangle> pathBoundaries = new Array<>();
        doReturn(pathBoundaries).when(map).getPathBoundaries();
        when(CollisionDetection.collisionWithPath(eq(pathBoundaries), eq(tower))).thenReturn(true);
        when(CollisionDetection.collisionWithActors(isA(SnapshotArray.class), eq(tower)))
            .thenReturn(true);

        LDVector2 moveCoords = new LDVector2(200, 100);
        towerPlacement.moveTower(moveCoords);

        assertTrue(tower.isVisible());
        assertTrue(tower.isShowRange());
        assertEquals(moveCoords, tower.getPositionCenter());
        assertTrue(tower.isTowerColliding());

        // place tower
        towerPlacement.placeTower();
        assertEquals(tower, towerPlacement.getCurrentTower());

    }
}

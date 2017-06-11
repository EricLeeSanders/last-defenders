package game.service.actorplacement;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;
import com.foxholedefense.game.helper.CollisionDetection;
import com.foxholedefense.game.model.actor.ActorGroups;
import com.foxholedefense.game.model.actor.combat.CombatActor;
import com.foxholedefense.game.model.actor.combat.tower.Tower;
import com.foxholedefense.game.model.actor.health.ArmorIcon;
import com.foxholedefense.game.model.actor.health.HealthBar;
import com.foxholedefense.game.model.level.Map;
import com.foxholedefense.game.service.actorplacement.TowerPlacement;
import com.foxholedefense.game.service.factory.CombatActorFactory;
import com.foxholedefense.game.service.factory.HealthFactory;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.datastructures.pool.FHDVector2;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


import util.TestUtil;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by Eric on 5/28/2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({CollisionDetection.class})
public class TowerPlacementTest {

    private Map map = mock(Map.class);
    private ActorGroups actorGroups = mock(ActorGroups.class);
    private CombatActorFactory combatActorFactory = mock(CombatActorFactory.class);
    HealthFactory healthFactory = mock(HealthFactory.class);

    @Before
    public void initTowerPlacementTest() {
        Gdx.app = mock(Application.class);
        PowerMockito.mockStatic(CollisionDetection.class);
    }

    public TowerPlacement createTowerPlacement(){
        return new TowerPlacement(map, actorGroups, combatActorFactory, healthFactory);
    }

    @Test
    public void towerPlacementTest1(){

        TowerPlacement towerPlacement = createTowerPlacement();


        // Create tower
        Group towerGroup = new Group();
        doReturn(towerGroup).when(actorGroups).getTowerGroup();

        Tower tower = TestUtil.createTower("Rifle", false);
        doReturn(tower).when(combatActorFactory).loadTower(eq("Rifle"));

        towerPlacement.createTower("Rifle");

        assertFalse(tower.isVisible());
        assertEquals(0, tower.getX(), TestUtil.DELTA);
        assertEquals(0, tower.getY(), TestUtil.DELTA);

        // Move tower and mock collision detection
        Array<Rectangle> pathBoundaries = new Array<Rectangle>();
        doReturn(pathBoundaries).when(map).getPathBoundaries();
        when(CollisionDetection.collisionWithPath(eq(pathBoundaries), eq(tower))).thenReturn(false);
        when(CollisionDetection.collisionWithActors(isA(SnapshotArray.class), eq(tower))).thenReturn(false);


        FHDVector2 moveCoords = new FHDVector2(200,100);
        towerPlacement.moveTower(moveCoords);

        assertTrue(tower.isVisible());
        assertTrue(tower.isShowRange());
        assertEquals(moveCoords, tower.getPositionCenter());
        assertFalse(tower.isTowerColliding());

        // rotate tower
        float rotation = 70;
        towerPlacement.rotateTower(rotation);
        assertEquals(-rotation, tower.getRotation(), TestUtil.DELTA);

        // place tower
        HealthBar healthBar = mock(HealthBar.class);
        ArmorIcon armorIcon = mock(ArmorIcon.class);

        doReturn(healthBar).when(healthFactory).loadHealthBar();
        doReturn(armorIcon).when(healthFactory).loadArmorIcon();

        towerPlacement.placeTower();

        assertNull(towerPlacement.getCurrentTower());

    }

    @Test
    public void towerPlacementCollisionTest(){

        TowerPlacement towerPlacement = createTowerPlacement();


        // Create tower
        Group towerGroup = new Group();
        doReturn(towerGroup).when(actorGroups).getTowerGroup();

        Tower tower = TestUtil.createTower("Rifle", false);
        doReturn(tower).when(combatActorFactory).loadTower(eq("Rifle"));

        towerPlacement.createTower("Rifle");

        assertFalse(tower.isVisible());
        assertEquals(0, tower.getX(), TestUtil.DELTA);
        assertEquals(0, tower.getY(), TestUtil.DELTA);

        // Move tower and mock collision detection
        Array<Rectangle> pathBoundaries = new Array<Rectangle>();
        doReturn(pathBoundaries).when(map).getPathBoundaries();
        when(CollisionDetection.collisionWithPath(eq(pathBoundaries), eq(tower))).thenReturn(true);
        when(CollisionDetection.collisionWithActors(isA(SnapshotArray.class), eq(tower))).thenReturn(true);


        FHDVector2 moveCoords = new FHDVector2(200,100);
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
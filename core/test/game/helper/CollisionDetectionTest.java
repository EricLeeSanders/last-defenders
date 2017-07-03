package game.helper;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;
import com.foxholedefense.game.helper.CollisionDetection;
import com.foxholedefense.game.model.actor.combat.tower.Tower;
import com.foxholedefense.util.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import testutil.TestUtil;

/**
 * Created by Eric on 5/17/2017.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({Logger.class})
public class CollisionDetectionTest {

    @Before
    public void initCollisionDetectionTest() {

        PowerMockito.mockStatic(Logger.class);
    }

    @Test
    public void collisionWithPathWithCircleBodyTest1() {

        Tower tower = TestUtil.createTower("FlameThrower", false);
        Rectangle rect1 = new Rectangle(10, 10, 20, 20);
        Rectangle rect2 = new Rectangle(35, 15, 15, 20);
        Rectangle rect3 = new Rectangle(15, 10, 2, 12);

        Array<Rectangle> boundaries = new Array<>();
        boundaries.addAll(rect1, rect2, rect3);

        tower.setPositionCenter(15, 15);
        tower.rotateBy(90);

        assertTrue(CollisionDetection.collisionWithPath(boundaries, tower));

    }

    @Test
    public void collisionWithPathWithCircleBodyTest2() {

        Tower tower = TestUtil.createTower("FlameThrower", false);
        Rectangle rect1 = new Rectangle(10, 10, 12, 12);

        Array<Rectangle> boundaries = new Array<>();
        boundaries.add(rect1);

        tower.setPositionCenter(30, 30);
        tower.rotateBy(90);

        assertFalse(CollisionDetection.collisionWithPath(boundaries, tower));


    }

    @Test
    public void collisionWithPathWithPolygonBodyTest1() {

        Tower tower = TestUtil.createTower("Tank", false);
        Rectangle rect1 = new Rectangle(10, 10, 20, 20);
        Rectangle rect2 = new Rectangle(30, 30, 10, 10);

        Array<Rectangle> boundaries = new Array<>();
        boundaries.addAll(rect1, rect2);

        tower.setPositionCenter(20, 20);
        tower.rotateBy(65);

        assertTrue(CollisionDetection.collisionWithPath(boundaries, tower));

    }

    @Test
    public void collisionWithPathWithPolygonBodyTest2() {

        Tower tower = TestUtil.createTower("Tank", false);
        Rectangle rect1 = new Rectangle(10, 10, 20, 20);

        Array<Rectangle> boundaries = new Array<>();
        boundaries.add(rect1);

        tower.setPositionCenter(69, 55);
        tower.rotateBy(65);

        assertFalse(CollisionDetection.collisionWithPath(boundaries, tower));

    }

    @Test
    public void collisionWithActorsWithCircleBodyTest1() {

        Tower tower = TestUtil.createTower("Sniper", false);

        Tower tower1 = TestUtil.createTower("MachineGun", false);
        Tower tower2 = TestUtil.createTower("Rifle", false);
        Tower tower3 = TestUtil.createTower("Turret", false);

        tower1.setPositionCenter(75, 56);
        tower2.setPositionCenter(56, 75);
        tower3.setPositionCenter(95, 75);
        tower3.rotateBy(35);

        SnapshotArray<Actor> otherTowers = new SnapshotArray<>();
        otherTowers.addAll(tower1, tower2, tower3);

        tower.setPositionCenter(75, 75);

        assertTrue(CollisionDetection.collisionWithActors(otherTowers, tower));

    }

    @Test
    public void collisionWithActorsWithCircleBodyTest2() {

        Tower tower = TestUtil.createTower("Sniper", false);

        Tower tower1 = TestUtil.createTower("MachineGun", false);
        Tower tower2 = TestUtil.createTower("Rifle", false);
        Tower tower3 = TestUtil.createTower("Tank", false);

        tower1.setPositionCenter(75, 55);
        tower2.setPositionCenter(55, 75);
        tower3.setPositionCenter(133, 75);
        tower3.rotateBy(35);

        SnapshotArray<Actor> otherTowers = new SnapshotArray<>();
        otherTowers.addAll(tower1, tower2, tower3);

        tower.setPositionCenter(75, 75);

        assertFalse(CollisionDetection.collisionWithActors(otherTowers, tower));

    }


    @Test
    public void collisionWithActorsWithPolygonBodyTest1() {

        Tower tower = TestUtil.createTower("Tank", false);

        Tower tower1 = TestUtil.createTower("MachineGun", false);
        Tower tower2 = TestUtil.createTower("Rifle", false);
        Tower tower3 = TestUtil.createTower("Turret", false);

        tower1.setPositionCenter(75, 56);
        tower2.setPositionCenter(56, 75);
        tower3.setPositionCenter(95, 75);
        tower3.rotateBy(35);

        SnapshotArray<Actor> otherTowers = new SnapshotArray<>();
        otherTowers.addAll(tower1, tower2, tower3);

        tower.setPositionCenter(75, 75);
        tower.rotateBy(70);

        assertTrue(CollisionDetection.collisionWithActors(otherTowers, tower));

    }

    @Test
    public void collisionWithActorsWithPolygonBodyTest2() {

        Tower tower = TestUtil.createTower("Tank", false);

        Tower tower1 = TestUtil.createTower("MachineGun", false);
        Tower tower2 = TestUtil.createTower("Rifle", false);
        Tower tower3 = TestUtil.createTower("Turret", false);

        tower1.setPositionCenter(250, 56);
        tower2.setPositionCenter(56, 250);
        tower3.setPositionCenter(250, 75);
        tower3.rotateBy(35);

        SnapshotArray<Actor> otherTowers = new SnapshotArray<>();
        otherTowers.addAll(tower1, tower2, tower3);

        tower.setPositionCenter(75, 75);
        tower3.rotateBy(70);

        assertFalse(CollisionDetection.collisionWithActors(otherTowers, tower));

    }

    @Test
    public void shapesIntersectTest1() {

        Circle circle1 = new Circle(10, 10, 10);
        Circle circle2 = new Circle(20, 20, 20);
        Polygon poly1 = new Polygon(new float[]{0, 0, 0, 5, 10, 5, 10, 0});
        poly1.setPosition(15, 15);
        Polygon poly2 = new Polygon(new float[]{0, 0, 0, 10, 15, 10, 15, 0});
        poly2.setPosition(20, 20);

        assertTrue(CollisionDetection.shapesIntersect(circle1, circle2));
        assertTrue(CollisionDetection.shapesIntersect(circle1, poly1));
        assertTrue(CollisionDetection.shapesIntersect(poly1, poly2));
    }

    @Test
    public void shapesIntersectTest2() {

        Circle circle1 = new Circle(10, 10, 5);
        Circle circle2 = new Circle(105, 105, 10);
        Polygon poly1 = new Polygon(new float[]{0, 0, 0, 5, 10, 5, 10, 0});
        poly1.setPosition(25, 25);
        Polygon poly2 = new Polygon(new float[]{0, 0, 0, 20, 30, 20, 30, 0});
        poly2.setPosition(65, 65);

        assertFalse(CollisionDetection.shapesIntersect(circle1, circle2));
        assertFalse(CollisionDetection.shapesIntersect(circle1, poly2));
        assertFalse(CollisionDetection.shapesIntersect(poly1, poly2));
    }

    @Test
    public void towerHitTest1() {

        Tower tower1 = TestUtil.createTower("MachineGun", false);
        Tower tower2 = TestUtil.createTower("Tank", false);

        tower1.setPositionCenter(20, 20);
        tower2.setPositionCenter(250, 75);

        SnapshotArray<Actor> towers = new SnapshotArray<>();
        towers.addAll(tower1, tower2);

        assertNotNull(CollisionDetection.towerHit(towers, new Vector2(20, 20)));
        assertNotNull(CollisionDetection.towerHit(towers, new Vector2(250, 75)));
        assertNotNull(CollisionDetection.towerHit(towers, new Vector2(29, 20)));
        assertNull(CollisionDetection.towerHit(towers, new Vector2(30, 20)));
        assertNull(CollisionDetection.towerHit(towers, new Vector2(301, 75)));
    }

}

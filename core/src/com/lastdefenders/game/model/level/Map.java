package com.lastdefenders.game.model.level;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.SnapshotArray;
import com.lastdefenders.action.LDSequenceAction;
import com.lastdefenders.action.WaypointAction;
import com.lastdefenders.game.model.actor.GameActor;
import com.lastdefenders.util.ActorUtil;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.datastructures.pool.LDVector2;
import com.lastdefenders.util.datastructures.pool.UtilPool;

/**
 * Represents a TiledMap
 *
 * @author Eric
 */
public class Map implements Disposable {

    private Array<LDVector2> pathCoords = new SnapshotArray<>(true, 16);
    private Array<Rectangle> pathBoundaries = new SnapshotArray<>(true, 32);
    private TiledMap tiledMap;
    private float tiledMapScale;

    public Map(TiledMap tiledMap, float tiledMapScale) {

        this.tiledMap = tiledMap;
        this.tiledMapScale = tiledMapScale;
    }

    public void init(){
        findPath();
        findBoundaries();
    }

    /**
     * Finds the path for the enemies to follow
     */
    private void findPath() {

        PolylineMapObject path = (PolylineMapObject) tiledMap.getLayers().get("PathLine").getObjects()
            .get("PathLine");
        float[] vertices = path.getPolyline().getVertices();

        // X and Y are the first point
        float pathX = path.getPolyline().getX();
        float pathY = path.getPolyline().getY();
        for (int i = 0; i < vertices.length - 1; i = i + 2) {
            // Need to get absolute value because vertices can be negative. The points are all relative
            // to the first point (pathX and pathY)
            pathCoords
                .add(UtilPool.getVector2(Math.abs(vertices[i] + pathX) * tiledMapScale
                    , Math.abs(vertices[i + 1] + pathY) * tiledMapScale));
        }
    }

    /**
     * Finds the path boundaries from the Tiled Map.
     * *
     */
    private void findBoundaries() {

        MapObjects boundaries = tiledMap.getLayers().get("Boundary").getObjects();
        for (MapObject boundry : boundaries) {
            if (boundry instanceof RectangleMapObject) {
                Rectangle rect = ((RectangleMapObject) boundry).getRectangle();

                //Required to create new Rectangle
                Rectangle boundary = new Rectangle(rect.x * tiledMapScale,
                    rect.y * tiledMapScale, rect.width * tiledMapScale,
                    rect.height * tiledMapScale);
                pathBoundaries.add(boundary);
            }
        }
    }

    /**
     * Creates {@link WaypointAction}s for a given {@link GameActor}.
     * Places the actor at the first waypoint offscreen.
     *
     * @param actor
     * @param speed
     */
    public void createWaypointActionSet(GameActor actor, float speed){

        Array<LDVector2> path = getPath();

        //Place the actor at the start and off screen
        LDVector2 newWaypoint = path.get(0); // start
        actor.setPositionCenter(newWaypoint);

        // face the next waypoint
        actor.setRotation(Math.round(ActorUtil.calculateRotation(path.get(1), actor.getPositionCenter())));

        // Start the actor at the first waypoint, but offscreen.
        // rotatedCoords are the coords of the top/front of the actor.
        Vector2 centerPos = actor.getPositionCenter();
        LDVector2 rotatedCoords = ActorUtil
            .calculateRotatedCoords(actor.getX() + actor.getWidth(), centerPos.y, centerPos.x, centerPos.y,
                Math.toRadians(actor.getRotation()));

        // Reposition the actor so that it is off the screen
        float newX = actor.getPositionCenter().x + (actor.getPositionCenter().x - rotatedCoords.x);
        float newY = actor.getPositionCenter().y + (actor.getPositionCenter().y - rotatedCoords.y);

        rotatedCoords.free();

        actor.setPositionCenter(newX, newY); // Start off screen

        //create actions
        LDSequenceAction sequenceAction = UtilPool.getSequenceAction();
        for (int i = 1; i < path.size; i++) {
            Vector2 prevWaypoint = newWaypoint;
            newWaypoint = path.get(i);
            float distance = newWaypoint.dst(prevWaypoint);
            float duration = (distance / speed);
            float rotation = ActorUtil
                .calculateRotation(newWaypoint.x, newWaypoint.y, prevWaypoint.x, prevWaypoint.y);
            WaypointAction waypointAction = createWaypointAction(newWaypoint.x, newWaypoint.y,
                duration, rotation);
            sequenceAction.addAction(waypointAction);
        }

        actor.addAction(sequenceAction);
    }
    private WaypointAction createWaypointAction(float x, float y, float duration, float rotation) {

        return UtilPool.getWaypointAction(x, y, duration, rotation, Interpolation.linear);
    }

    public Array<Rectangle> getPathBoundaries() {

        return pathBoundaries;
    }

    public Array<LDVector2> getPath() {

        return pathCoords;
    }

    @Override
    public void dispose() {

        Logger.info("Map: Disposed");
        tiledMap.dispose();
    }
}

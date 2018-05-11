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

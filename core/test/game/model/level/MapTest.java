package game.model.level;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;
import com.foxholedefense.game.model.level.Map;
import com.foxholedefense.util.datastructures.pool.FHDVector2;

import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * Created by Eric on 5/26/2017.
 */

public class MapTest {

    private TiledMap tiledMap = mock(TiledMap.class);

    @Before
    public void initMapTest() {
        Gdx.app = mock(Application.class);
    }

    @Test
    public void mapTest1(){

        float [] pathVertices = new float[]{0.0f, -0.0f, 0.0f, 209.0f, -257.0f, 209.0f, -257.0f,
                400.0f, 0.0f, 400.0f, 0.0f, 657.0f, 514.0f, 657.0f, 514.0f, 431.0f, 192.0f, 431.0f,
                192.0f, 268.0f, 514.0f, 268.0f, 514.0f, 108.0f, 191.0f, 108.0f, 191.0f, 1.0f};

        //Mocks
        MapLayers mapLayers = mock(MapLayers.class);
        MapLayer pathLayer = mock(MapLayer.class);
        MapLayer boundaryLayer = mock(MapLayer.class);
        MapObjects mapObjectsMock = mock(MapObjects.class);
        PolylineMapObject pathLine = mock(PolylineMapObject.class);
        Polyline polyline = mock(Polyline.class);

        doReturn(mapLayers).when(tiledMap).getLayers();

        // Path
        doReturn(pathLayer).when(mapLayers).get(eq("Path"));
        doReturn(mapObjectsMock).when(pathLayer).getObjects();
        doReturn(pathLine).when(mapObjectsMock).get(eq("PathLine"));
        doReturn(polyline).when(pathLine).getPolyline();
        doReturn(pathVertices).when(polyline).getVertices();
        float pathX = 432;
        float pathY = 1;
        doReturn(pathX).when(polyline).getX();
        doReturn(pathY).when(polyline).getY();

        Array<FHDVector2> pathCoords = new SnapshotArray<>(true, 14);
        pathCoords.add(new FHDVector2(216.0f, 0.5f));
        pathCoords.add(new FHDVector2(216.0f, 105f));
        pathCoords.add(new FHDVector2(87.5f, 105f));
        pathCoords.add(new FHDVector2(87.5f, 200.5f));
        pathCoords.add(new FHDVector2(216.0f, 200.5f));
        pathCoords.add(new FHDVector2(216.0f, 329.0f));
        pathCoords.add(new FHDVector2(473.0f, 329.0f));
        pathCoords.add(new FHDVector2(473.0f, 216.0f));
        pathCoords.add(new FHDVector2(312.0f, 216.0f));
        pathCoords.add(new FHDVector2(312.0f, 134.5f));
        pathCoords.add(new FHDVector2(473.0f, 134.5f));
        pathCoords.add(new FHDVector2(473.0f, 54.5f));
        pathCoords.add(new FHDVector2(311.5f, 54.5f));
        pathCoords.add(new FHDVector2(311.5f, 1.0f));

        // Boundary

        MapObjects mapObjects = new MapObjects();
        RectangleMapObject boundary1 = mock(RectangleMapObject.class);
        Rectangle rectangle1 = new Rectangle(100, 125, 100, 55);
        RectangleMapObject boundary2 = mock(RectangleMapObject.class);
        Rectangle rectangle2 = new Rectangle(200, 125, 55, 100);
        RectangleMapObject boundary3 = mock(RectangleMapObject.class);
        Rectangle rectangle3 = new Rectangle(200, 225, 100, 55);

        doReturn(rectangle1).when(boundary1).getRectangle();
        doReturn(rectangle2).when(boundary2).getRectangle();
        doReturn(rectangle3).when(boundary3).getRectangle();

        mapObjects.add(boundary1);
        mapObjects.add(boundary2);
        mapObjects.add(boundary3);

        doReturn(boundaryLayer).when(mapLayers).get(eq("Boundary"));
        doReturn(mapObjects).when(boundaryLayer).getObjects();

        Array<Rectangle> pathBoundaries = new SnapshotArray<>(true, 3);
        pathBoundaries.add(new Rectangle(50.0f,62.5f,50.0f,27.5f));
        pathBoundaries.add(new Rectangle(100.0f,62.5f,27.5f,50.0f));
        pathBoundaries.add(new Rectangle(100.0f,112.5f,50.0f,27.5f));

        Map map = new Map(tiledMap);

        assertEquals(pathCoords, map.getPath());
        assertEquals(pathBoundaries, map.getPathBoundaries());


    }
}

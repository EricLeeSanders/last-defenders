package com.lastdefenders.game.ui.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Array;
import com.lastdefenders.game.model.actor.GameActor;
import com.lastdefenders.game.model.level.Map;
import com.lastdefenders.util.Resources;
import com.lastdefenders.util.datastructures.Dimension;
import com.lastdefenders.util.datastructures.pool.LDVector2;
import com.lastdefenders.util.UtilPool;

/**
 * Created by Eric on 4/21/2018.
 */

public class PathDisplayer extends Group {

    private static final float DISPLAY_DELAY_SPEED = 0.05f;
    private static final float DISPLAY_WAIT_TIME = 0.2f;
    private static final float FADE_OUT_SPEED = 0.5f;
    private Resources resources;
    private Map map;


    public PathDisplayer(Resources resources, Map map) {
        this.resources = resources;
        this.map = map;
        this.setTransform(false);

    }

    public void init(){
        Array<Point> points = createPoints();
        initPoints(points);

    }

    private void initPoints(Array<Point> points){

        for(int i = 0; i < points.size; i++){
            Point point = points.get(i);
            addActor(point);
            SequenceAction sequenceAction = Actions.sequence();
            sequenceAction.addAction(Actions.delay(DISPLAY_DELAY_SPEED * (i+1)));
            sequenceAction.addAction(Actions.fadeIn(0));
            sequenceAction.addAction(Actions.delay(DISPLAY_WAIT_TIME));
            sequenceAction.addAction(Actions.fadeOut(FADE_OUT_SPEED));
            sequenceAction.addAction(Actions.removeActor());
            point.addAction(sequenceAction);
        }

    }


    private Array<Point>  createPoints() {

        Array<Point> points = new Array<>();
        Array<LDVector2> mapCoords = map.getPath();

        // Add 8 to the first point so that it shows fully on screen.
        // This also fixes misalignment issues.
        addToPoint(mapCoords.get(0), mapCoords.get(1), 8);
        TextureRegion pathDisplayCircle = resources.getTexture("pathDisplayCircle");
        // Loop over each map point and create a line of points
        for(int i = 0; i < mapCoords.size - 1; i++){
            Vector2 p1 = mapCoords.get(i);
            Vector2 p2 = mapCoords.get(i + 1);

            // Add the first point
            points.add(new Point(p1, pathDisplayCircle));
            // Add the next N points along the line in increments of 16 unit spacing until the next
            // map point is reached.
            float distance = p1.dst(p2);
            float numOfPoints = distance / 16;
            Vector2 prevPoint = p1;
            for(int j=0; j < numOfPoints; j++){
                if(p2.dst(prevPoint) <= 16 ){
                    continue;
                }
                Vector2 newPoint = UtilPool.getVector2(prevPoint);
                addToPoint(newPoint, p2, 16);
                points.add(new Point(newPoint, pathDisplayCircle));
                prevPoint = newPoint;
            }
        }

        return points;
    }

    private void addToPoint(Vector2 point, Vector2 end, int amount){
        boolean isVertical = (point.x == end.x);
        if(isVertical){
            boolean isDown = (point.y > end.y);
            point.add(0, amount * (isDown ? -1 : 1));
        } else {
            boolean isLeft = (point.x > end.x);
            point.add(amount * (isLeft ? -1: 1), 0);
        }
    }

    private static class Point extends GameActor{

        public Point(Vector2 position, TextureRegion textureRegion){
            this(position, textureRegion, new Dimension(10,10));
        }

        public Point(Vector2 position, TextureRegion textureRegion, Dimension dimension){
            super(dimension);
            setTextureRegion(textureRegion);
            setPositionCenter(position);
            Color color = getColor();
            setColor(color.r, color.b, color.g, 0);
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            Color color = getColor();
            batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
            super.draw(batch, parentAlpha);
            batch.setColor(color.r, color.g, color.b, 1f);
        }
    }
}

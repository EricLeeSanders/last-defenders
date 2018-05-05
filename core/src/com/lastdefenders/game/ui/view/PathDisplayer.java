package com.lastdefenders.game.ui.view;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.lastdefenders.game.model.actor.GameActor;
import com.lastdefenders.game.model.level.Map;
import com.lastdefenders.util.Resources;
import com.lastdefenders.util.datastructures.Dimension;

/**
 * Created by Eric on 4/21/2018.
 */

public class PathDisplayer extends Group {

    private static final float SPAWN_SPEED = 0.1f;

    private Resources resources;
    private Map map;
    private float spawnDelayCount = 0f;
    private Array<Point> pointsToSpawn;

    public PathDisplayer(Resources resources, Map map) {
        this.resources = resources;
        this.map = map;
        this.setTransform(false);

        createPointsToSpawn();
    }

    private void createPointsToSpawn() {

        pointsToSpawn = new Array<>();

        // Create 10 points
        TextureRegion pointTextureRegion = resources.getTexture("pathDisplayCircle");
        for (int i = 0; i < 10; i++) {
            Point p = new Point(pointTextureRegion);
            map.createWaypointActionSet(p, Point.SPEED);
            pointsToSpawn.add(p);
        }

        // Create Arrow
        TextureRegion arrowTextureRegion = resources.getTexture("pathDisplayArrow");
        Arrow arrow = new Arrow(arrowTextureRegion);
        map.createWaypointActionSet(arrow, Point.SPEED);
        pointsToSpawn.add(arrow);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if(pointsToSpawn.size > 0){
            spawnDelayCount += delta;
            if(spawnDelayCount > SPAWN_SPEED){
                addActor(pointsToSpawn.pop());
                spawnDelayCount = 0;
            }
        }
    }

    private static class Point extends GameActor{

        public static final float SPEED = 200f;

        public Point(TextureRegion textureRegion){
            this(textureRegion, new Dimension(10,10));
        }

        public Point(TextureRegion textureRegion, Dimension dimension){
            super(dimension);
            setTextureRegion(textureRegion);
        }

        @Override
        public void act(float delta){
            super.act(delta);

            if(this.getActions().size == 0){
                remove();
            }
        }
    }

    private static class Arrow extends Point{

        public Arrow( TextureRegion textureRegion){
            super(textureRegion, new Dimension(18,20));
        }
    }
}

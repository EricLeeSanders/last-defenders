package simulate.positionweights;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;
import com.lastdefenders.game.helper.CollisionDetection;
import com.lastdefenders.game.model.actor.combat.tower.Tower;

public class TowerPositionWeight implements Comparable<TowerPositionWeight>{

    private static final float TOWERS_IN_RANGE_MODIFIER = 1;
    private float x;
    private float y;
    private int intersectionsSize;
    private float towerRange;
    private float length;
    private int depth;
    private SnapshotArray<Actor> towers;
    private Tower tower;

    public TowerPositionWeight(float x, float y, int intersectionsSize, SnapshotArray<Actor> towers, float length, int depth, Tower tower){
        this.x = x;
        this.y = y;
        this.intersectionsSize = intersectionsSize;
        this.towerRange = tower.getRangeShape().radius;
        this.towers = towers;
        this.length = length;
        this.depth = depth;
        this.tower = tower;
    }

    public float getX() {

        return x;
    }

    public float getY() {

        return y;
    }

    public int getIntersectionsSize() {

        return intersectionsSize;
    }

    public int getTowersInRange(){
        Circle currentTowerBody = new Circle(x, y, towerRange*2);
        int towersInRange = 0;
        for(Actor actor : towers){
            Tower tower = (Tower) actor;
            Shape2D towerBody = tower.getBody();
            if(!tower.ID.equals(this.tower.ID)) {
                if (CollisionDetection.shapesIntersect(currentTowerBody, towerBody)) {
                    towersInRange++;
                }
            }
        }

        return towersInRange;
    }

    @Override
    public String toString() {

        return "TowerPositionWeight{" +
            "x=" + x +
            ", y=" + y +
            ", intersectionsSize=" + intersectionsSize +
            ", towerRange=" + towerRange +
            ", length=" + length +
            ", depth=" + depth +
            ", towersInRange=" + getTowersInRange() +
            '}';
    }


    @Override
    public int compareTo(TowerPositionWeight o) {

        if(o.intersectionsSize <= 0){
            return 1;
        } else if(this.intersectionsSize <= 0){
            return -1;
        }

        if(getTowersInRange() > o.getTowersInRange()){
            return 1;
        } else if(getTowersInRange() < o.getTowersInRange()){
            return -1;
        } else {
            if (depth < o.depth) {
                return 1;
            } else if (depth > o.depth) {
                return -1;
            } else {
                if (intersectionsSize > o.getIntersectionsSize()) {
                    return 1;
                } else if (intersectionsSize < o.getIntersectionsSize()) {
                    return -1;
                } else {
                    if (length > o.length) {
                        return 1;
                    } else if (length < o.length) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            }
        }
    }
}



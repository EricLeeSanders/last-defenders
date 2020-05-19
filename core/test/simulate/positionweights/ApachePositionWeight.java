package simulate.positionweights;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;
import com.lastdefenders.game.helper.CollisionDetection;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.model.actor.support.Apache;

public class ApachePositionWeight implements Comparable<ApachePositionWeight>{

    private float x;
    private float y;
    private int intersectionsSize;
    private float range;
    private float length;
    private int depth;
    public ApachePositionWeight(float x, float y, int intersectionsSize, float length, int depth, Apache apache){
        this.x = x;
        this.y = y;
        this.intersectionsSize = intersectionsSize;
        this.range = apache.getRangeShape().radius;
        this.length = length;
        this.depth = depth;
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

    @Override
    public String toString() {

        return "ApachePositionWeight{" +
            "x=" + x +
            ", y=" + y +
            ", intersectionsSize=" + intersectionsSize +
            ", length=" + length +
            ", depth=" + depth +
            '}';
    }

    @Override
    public int compareTo(ApachePositionWeight o) {

        if (o.intersectionsSize <= 0) {
            return 1;
        } else if (this.intersectionsSize <= 0) {
            return -1;
        }

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



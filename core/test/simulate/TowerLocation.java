package simulate;

/**
 * Created by Eric on 12/18/2019.
 */

public class TowerLocation {

    private float x;
    private float y;
    private float weight;

    public TowerLocation(float x, float y, float weight) {

        this.x = x;
        this.y = y;
        this.weight = weight;
    }

    public float getX() {

        return x;
    }

    public float getY() {

        return y;
    }

    public float getWeight() {

        return weight;
    }
}

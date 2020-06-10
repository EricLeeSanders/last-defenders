package simulate.state.support;

import com.badlogic.gdx.math.Vector2;
import java.awt.Graphics2D;
import java.io.IOException;
import simulate.state.writer.SnapshotWriter;

public class LandMineState implements SupportState{

    private Vector2 location;

    public LandMineState(Vector2 location) {
        this.location = location;
    }

    public Vector2 getLocation(){
        return location;
    }

    @Override
    public void writeState(Graphics2D background) throws IOException {

        SnapshotWriter.drawLandMine(background, this);
    }

    @Override
    public String toString() {

        return "LandMineState{" +
            "location=" + location +
            '}';
    }
}

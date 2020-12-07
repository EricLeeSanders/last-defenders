package simulate.state.support;

import com.badlogic.gdx.math.Vector2;
import java.awt.Graphics2D;
import java.io.IOException;
import simulate.state.writer.SnapshotWriter;

public class SupplyDropState implements SupportState{

    private Vector2 location;

    public SupplyDropState(Vector2 location) {
        this.location = location;
    }

    public Vector2 getLocation(){
        return location;
    }

    @Override
    public void writeSnapshotState(Graphics2D background, SnapshotWriter snapshotWriter) throws IOException {

        snapshotWriter.drawSupplyDropState(background, this);
    }

    @Override
    public String getSupportName() {

        return "SupplyDrop";
    }

    @Override
    public String toString() {

        return "SupplyDropState{" +
            "location=" + location +
            '}';
    }
}

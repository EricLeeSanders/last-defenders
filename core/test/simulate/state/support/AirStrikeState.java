package simulate.state.support;

import com.badlogic.gdx.math.Vector2;
import java.awt.Graphics2D;
import java.io.IOException;
import java.util.List;
import simulate.state.writer.SnapshotWriter;

public class AirStrikeState implements SupportState{

    private List<Vector2> locations;

    public AirStrikeState(List<Vector2> locations) {
        this.locations = locations;
    }

    public List<Vector2> getLocations(){
        return locations;
    }

    @Override
    public void writeSnapshotState(Graphics2D background, SnapshotWriter snapshotWriter) throws IOException {

        snapshotWriter.drawAirStrikeState(background, this);
    }

    @Override
    public String toString() {

        return "AirStrikeState{" +
            "locations=" + locations +
            '}';
    }
}

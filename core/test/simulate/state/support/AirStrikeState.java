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
    public void writeState(Graphics2D background) throws IOException {

        SnapshotWriter.drawAirStrikeState(background, this);
    }
}

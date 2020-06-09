package simulate.state.support;

import com.badlogic.gdx.math.Vector2;
import java.awt.Graphics2D;
import java.io.IOException;
import simulate.state.writer.SnapshotWriter;

public class ApacheState implements SupportState{

    private Vector2 location;

    public ApacheState(Vector2 location) {
        this.location = location;
    }

    public Vector2 getLocation(){
        return location;
    }

    @Override
    public void writeState(Graphics2D background) throws IOException {

        SnapshotWriter.drawApacheState(background, this);
    }
}
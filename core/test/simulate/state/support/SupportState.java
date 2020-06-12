package simulate.state.support;

import java.awt.Graphics2D;
import java.io.IOException;
import simulate.state.writer.SnapshotWriter;

public interface SupportState {

    void writeSnapshotState(Graphics2D background, SnapshotWriter snapshotWriter) throws IOException;

    String getSupportName();
}

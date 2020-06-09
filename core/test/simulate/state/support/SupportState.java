package simulate.state.support;

import java.awt.Graphics2D;
import java.io.IOException;

public interface SupportState {

    void writeState(Graphics2D background) throws IOException;
}

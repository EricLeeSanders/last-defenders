package simulate.state;

import java.util.List;

/**
 * Created by Eric on 12/17/2019.
 */

public class GameState {

    GameBeginState beginState;
    GameEndState endState;

    public GameBeginState getBeginState() {

        return beginState;
    }

    public void setBeginState(GameBeginState beginState) {

        this.beginState = beginState;
    }

    public GameEndState getEndState() {

        return endState;
    }

    public void setEndState(GameEndState endState) {

        this.endState = endState;
    }
}

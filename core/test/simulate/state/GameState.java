package simulate.state;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 12/17/2019.
 */

public class GameState {

    private List<WaveState> waveStates = new ArrayList<>();

    public void addWaveState(WaveState waveState){
        waveStates.add(waveState);
    }

    public List<WaveState> getWaveStates(){
        return waveStates;
    }
}

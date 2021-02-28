package simulate.state;

import java.util.ArrayList;
import java.util.List;

public class SingleSimulationState {
    private List<WaveState> waveStates = new ArrayList<>();

    public void addWaveState(WaveState waveState){
        this.waveStates.add(waveState);
    }

    public List<WaveState> getWaveStates(){
        return waveStates;
    }
}

package simulate.state.writer;

import java.util.List;
import java.util.Map;
import simulate.SimulationRunType;
import simulate.state.WaveState;

public class AggregateSummaryStateWriter {

    public static void writeState(
        Map<SimulationRunType, Map<Integer, List<WaveState>>> waveStatesByRunType){

        for (Map.Entry<SimulationRunType, Map<Integer, List<WaveState>>> entry : waveStatesByRunType.entrySet()) {
            SimulationRunType runType = entry.getKey();
            Map<Integer, List<WaveState>> waveStates = entry.getValue();




        }
    }

}

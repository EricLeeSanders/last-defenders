package simulate.state.summary.aggregate;

import java.util.IntSummaryStatistics;
import java.util.Map;
import simulate.SimulationRunType;

public class RoundStatsAggregate {
    private SimulationRunType simulationRunType;
    private IntSummaryStatistics stats;
    private Map<Integer, Integer> numOfWavesByWaveCount;


    public RoundStatsAggregate(SimulationRunType simulationRunType,
        IntSummaryStatistics stats, Map<Integer, Integer> numOfWavesByWaveCount) {

        this.simulationRunType = simulationRunType;
        this.stats = stats;
        this.numOfWavesByWaveCount = numOfWavesByWaveCount;
    }

    public SimulationRunType getSimulationRunType() {

        return simulationRunType;
    }

    public IntSummaryStatistics getStats() {

        return stats;
    }

    public Map<Integer, Integer> getNumOfWavesByWaveCount(){
        return numOfWavesByWaveCount;
    }
}

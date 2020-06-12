package simulate.state.summary.aggregate;

import java.util.IntSummaryStatistics;
import simulate.SimulationRunType;

public class RoundStatsAggregate {
    private SimulationRunType simulationRunType;
    private IntSummaryStatistics stats;


    public RoundStatsAggregate(SimulationRunType simulationRunType,
        IntSummaryStatistics stats) {

        this.simulationRunType = simulationRunType;
        this.stats = stats;
    }

    public SimulationRunType getSimulationRunType() {

        return simulationRunType;
    }

    public IntSummaryStatistics getStats() {

        return stats;
    }
}

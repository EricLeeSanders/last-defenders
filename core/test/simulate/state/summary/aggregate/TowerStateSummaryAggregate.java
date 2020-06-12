package simulate.state.summary.aggregate;

import java.util.List;
import simulate.SimulationRunType;
import simulate.state.summary.TowerStateSummary;

public class TowerStateSummaryAggregate {

    private SimulationRunType simulationRunType;
    private List<TowerStateSummary> towerStateSummaries;

    public TowerStateSummaryAggregate(SimulationRunType simulationRunType,
        List<TowerStateSummary> towerStateSummaries) {

        this.simulationRunType = simulationRunType;
        this.towerStateSummaries = towerStateSummaries;
    }

    public SimulationRunType getSimulationRunType() {

        return simulationRunType;
    }

    public List<TowerStateSummary> getTowerStateSummaries() {

        return towerStateSummaries;
    }
}

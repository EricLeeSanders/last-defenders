package simulate.state.summary.aggregate;

import java.util.Collection;
import simulate.SimulationRunType;
import simulate.state.summary.TowerStateSummary;

public class TowerStateSummaryAggregate {

    private SimulationRunType simulationRunType;
    private Collection<TowerStateSummary> towerStateSummaries;

    public TowerStateSummaryAggregate(SimulationRunType simulationRunType,
        Collection<TowerStateSummary> towerStateSummaries) {

        this.simulationRunType = simulationRunType;
        this.towerStateSummaries = towerStateSummaries;
    }

    public SimulationRunType getSimulationRunType() {

        return simulationRunType;
    }

    public Collection<TowerStateSummary> getTowerStateSummaries() {

        return towerStateSummaries;
    }
}

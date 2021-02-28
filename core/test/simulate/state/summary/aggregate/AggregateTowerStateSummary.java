package simulate.state.summary.aggregate;

import java.util.Collection;
import simulate.SimulationRunType;
import simulate.state.summary.TowerStateSummary;

public class AggregateTowerStateSummary {

    private SimulationRunType simulationRunType;
    private Collection<TowerStateSummary> towerStateSummaries;

    public AggregateTowerStateSummary(SimulationRunType simulationRunType,
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

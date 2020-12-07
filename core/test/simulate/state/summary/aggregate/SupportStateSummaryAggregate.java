package simulate.state.summary.aggregate;

import java.util.Collection;
import simulate.SimulationRunType;
import simulate.state.summary.SupportStateSummary;

public class SupportStateSummaryAggregate {

    private SimulationRunType simulationRunType;
    private Collection<SupportStateSummary> supportStateSummaries;

    public SupportStateSummaryAggregate(SimulationRunType simulationRunType,
        Collection<SupportStateSummary> supportStateSummaries) {

        this.simulationRunType = simulationRunType;
        this.supportStateSummaries = supportStateSummaries;
    }

    public SimulationRunType getSimulationRunType() {

        return simulationRunType;
    }

    public Collection<SupportStateSummary> getSupportStateSummaries() {

        return supportStateSummaries;
    }
}

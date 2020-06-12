package simulate.state.summary.aggregate;

import java.util.List;
import simulate.SimulationRunType;
import simulate.state.summary.SupportStateSummary;

public class SupportStateSummaryAggregate {

    private SimulationRunType simulationRunType;
    private List<SupportStateSummary> supportStateSummaries;

    public SupportStateSummaryAggregate(SimulationRunType simulationRunType,
        List<SupportStateSummary> supportStateSummaries) {

        this.simulationRunType = simulationRunType;
        this.supportStateSummaries = supportStateSummaries;
    }

    public SimulationRunType getSimulationRunType() {

        return simulationRunType;
    }

    public List<SupportStateSummary> getSupportStateSummaries() {

        return supportStateSummaries;
    }
}

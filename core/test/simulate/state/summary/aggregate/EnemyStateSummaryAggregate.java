package simulate.state.summary.aggregate;

import java.util.List;
import simulate.SimulationRunType;
import simulate.state.summary.EnemyStateSummary;

public class EnemyStateSummaryAggregate {

    private SimulationRunType simulationRunType;
    private List<EnemyStateSummary> enemyStateSummaries;

    public EnemyStateSummaryAggregate(SimulationRunType simulationRunType,
        List<EnemyStateSummary> enemyStateSummaries) {

        this.simulationRunType = simulationRunType;
        this.enemyStateSummaries = enemyStateSummaries;
    }

    public SimulationRunType getSimulationRunType() {

        return simulationRunType;
    }

    public List<EnemyStateSummary> getEnemyStateSummaries() {

        return enemyStateSummaries;
    }
}

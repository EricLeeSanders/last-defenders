package simulate.state.summary.aggregate;

import java.util.Collection;
import simulate.SimulationRunType;
import simulate.state.summary.EnemyStateSummary;

public class AggregateEnemyStateSummary {

    private SimulationRunType simulationRunType;
    private Collection<EnemyStateSummary> enemyStateSummaries;

    public AggregateEnemyStateSummary(SimulationRunType simulationRunType,
        Collection<EnemyStateSummary> enemyStateSummaries) {

        this.simulationRunType = simulationRunType;
        this.enemyStateSummaries = enemyStateSummaries;
    }

    public SimulationRunType getSimulationRunType() {

        return simulationRunType;
    }

    public Collection<EnemyStateSummary> getEnemyStateSummaries() {

        return enemyStateSummaries;
    }
}

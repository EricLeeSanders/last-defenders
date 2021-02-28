package simulate.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import simulate.SimulationRunType;

public class AggregateSimulationState {

    private SimulationRunType simulationRunType;
    private List<SingleSimulationState> singleSimulationStates = new ArrayList<>();

    public AggregateSimulationState(SimulationRunType simulationRunType){
        this.simulationRunType = simulationRunType;
    }

    public void addSingleSimulationState(SingleSimulationState singleSimulationState){
        this.singleSimulationStates.add(singleSimulationState);
    }

    public List<SingleSimulationState> getSingleSimulationStates(){
        return singleSimulationStates;
    }

    public SimulationRunType getSimulationRunType(){
        return simulationRunType;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AggregateSimulationState that = (AggregateSimulationState) o;
        return simulationRunType == that.simulationRunType;
    }

    @Override
    public int hashCode() {

        return Objects.hash(simulationRunType);
    }
}

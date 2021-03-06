package simulate.state.summary.helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import simulate.state.WaveState;
import simulate.state.combatactor.TowerState;
import simulate.state.summary.TowerStateSummary;

public class TowerStateSummaryHelper {

    public  Map<String, TowerStateSummary> calculateTowerSummaries(List<WaveState> waveStates){
        Map<String, TowerStateSummary> towerSummaries = new HashMap<>();
        return calculateTowerSummaries(waveStates, towerSummaries);
    }

    public  Map<String, TowerStateSummary> calculateTowerSummaries(List<WaveState> waveStates, Map<String, TowerStateSummary> towerSummaries){

        for(int i = 0; i < waveStates.size(); i++){
            WaveState waveState = waveStates.get(i);
            for(TowerState tower : waveState.getTowerStates()){
                TowerStateSummary towerSummary = towerSummaries.get(tower.getName());

                if(towerSummary == null){
                    towerSummary = new TowerStateSummary(tower.getName());
                    towerSummaries.put(tower.getName(), towerSummary);
                }


                if(tower.isDead()) {
                    towerSummary.addDeadCount();
                }
                /*
                If the tower is dead, or we are on the last state, we add the stats
                */
                if(tower.isDead() || i == waveStates.size() - 1) {
                    towerSummary.addKills(tower.getKills());
                    towerSummary.addCount();

                    if(tower.getHasArmor()) {
                        towerSummary.addArmorCount();
                    }

                    if(tower.getAttackIncreased()){
                        towerSummary.addAttackIncreaseCount();
                    }

                    if(tower.getRangeIncreased()){
                        towerSummary.addRangeIncreaseCount();
                    }

                    if(tower.getSpeedIncreased()){
                        towerSummary.addSpeedIncreaseCount();
                    }
                }

            }
        }
        return towerSummaries;

    }



}

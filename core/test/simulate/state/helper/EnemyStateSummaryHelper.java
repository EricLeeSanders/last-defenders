package simulate.state.helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import simulate.state.WaveState;
import simulate.state.combatactor.EnemyState;

public class EnemyStateSummaryHelper {

    public Map<String, EnemyStateSummary> calculateEnemyStateSummaries(List<WaveState> waveStates){

        Map<String, EnemyStateSummary> enemySummaries = new HashMap<>();
        for(WaveState waveState : waveStates){
            for(EnemyState enemy : waveState.getEnemyStates()){
                EnemyStateSummary enemySummary = enemySummaries.get(enemy.getName());

                if(enemySummary == null){
                    enemySummary = new EnemyStateSummary(enemy.getName());
                    enemySummaries.put(enemy.getName(), enemySummary);
                }

                enemySummary.addCount();
                enemySummary.addKills(enemy.getKills());
                enemySummary.addSpawnDelay(enemy.getSpawnDelay());
                if(enemy.getHasArmor()) {
                    enemySummary.addArmorCount();
                }

                if(enemy.getReachedEnd()) {
                    enemySummary.addReachedEndCount();
                }

                if(enemy.isDead()) {
                    enemySummary.addDeadCount();
                    enemySummary.addReward(enemy.getReward());
                }

            }
        }

        return enemySummaries;
    }
}

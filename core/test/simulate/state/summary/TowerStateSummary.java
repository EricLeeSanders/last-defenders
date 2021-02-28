package simulate.state.summary;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;
import simulate.state.WaveState;
import simulate.state.combatactor.TowerState;

public class TowerStateSummary extends CombatActorStateSummary {

    private String towerName;

    private int totalAttackIncrease = 0;
    private int totalSpeedIncrease = 0;
    private int totalRangeIncrease = 0;


    public TowerStateSummary(String towerName){
        this.towerName = towerName;
    }

    public void addAttackIncreaseCount(){
        totalAttackIncrease++;
    }

    public void addSpeedIncreaseCount(){
        totalSpeedIncrease++;
    }

    public void addRangeIncreaseCount(){
        totalRangeIncrease++;
    }

    public int getTotalAttackIncrease(){

        return totalAttackIncrease;
    }

    public int getTotalSpeedIncrease(){
        return totalSpeedIncrease;
    }

    public int getTotalRangeIncrease(){
        return totalRangeIncrease;
    }

    public String getTowerName(){
        return towerName;
    }

}

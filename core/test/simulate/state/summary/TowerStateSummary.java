package simulate.state.summary;

import java.util.List;
import simulate.state.WaveState;
import simulate.state.combatactor.TowerState;

public class TowerStateSummary {

    private String towerName;

    private int totalCount = 0;
    private int totalKills = 0;
    private int maxKill = 0;
    private int totalArmor = 0;
    private int totalAttackIncrease = 0;
    private int totalSpeedIncrease = 0;
    private int totalRangeIncrease = 0;
    private int totalDead = 0;


    public TowerStateSummary(String towerName){
        this.towerName = towerName;
    }

    public void addCount(){
        totalCount++;
    }

    public void addKills(int kills){
        this.totalKills += kills;
    }


    public void addArmorCount(){
        totalArmor++;
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
    public void addDeadCount(){
        totalDead++;
    }

    public void calculateMaxKill(List<WaveState> states){
        int best = 0;
        for(WaveState waveState : states){
            for(TowerState towerState : waveState.getTowerStates()){
                if(towerState.getName().equals(this.getTowerName()) && towerState.getKills() > best){
                    best = towerState.getKills();
                }
            }
        }

        maxKill = best;
    }

    public int getMaxKill(){
        return maxKill;
    }

    public int getTotalCount() {

        return totalCount;
    }

    public int getTotalKills() {

        return totalKills;
    }

    public float getAverageKills(){
        return (float)totalKills / (float)totalCount;
    }

    public int getTotalArmor() {

        return totalArmor;
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

    public int getTotalDead() {

        return totalDead;
    }

    public String getTowerName(){
        return towerName;
    }
}

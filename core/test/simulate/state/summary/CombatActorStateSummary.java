package simulate.state.summary;

import java.util.Comparator;
import java.util.PriorityQueue;

public abstract class CombatActorStateSummary {

    private int totalCount = 0;
    private int totalKills = 0;
    private int totalArmor = 0;
    private int totalDead = 0;

    private PriorityQueue<Integer> maxKill = new PriorityQueue<>(
        new Comparator<Integer>() {
            public int compare(Integer a, Integer b) {
                return b - a;
            }
        }
    );

    public void addCount(){
        totalCount++;
    }

    public void addKills(int kills){
        this.totalKills += kills;
        maxKill.add(kills);
    }

    public void addArmorCount(){
        totalArmor++;
    }

    public void addDeadCount(){
        totalDead++;
    }

    public int getTotalDead() {

        return totalDead;
    }

    public int getMaxKill(){
        return maxKill.peek();
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
}

package simulate.state.summary;

public class EnemyStateSummary extends CombatActorStateSummary {

    private String enemyName;
    private float totalSpawnDelay = 0;
    private int totalReachedEnd = 0;
    private int totalReward = 0;


    public EnemyStateSummary(String enemyName){
        this.enemyName = enemyName;
    }

    public void addSpawnDelay(float spawnDelay){
        this.totalSpawnDelay += spawnDelay;
    }

    public void addReachedEndCount(){
        totalReachedEnd++;
    }

    public void addReward(int reward){
        this.totalReward += reward;
    }

    public float getTotalSpawnDelay() {

        return totalSpawnDelay;
    }

    public float getAverageSpawnDelay(){
        return totalSpawnDelay / getTotalCount();
    }

    public int getTotalReachedEnd() {

        return totalReachedEnd;
    }

    public int getTotalReward() {

        return totalReward;
    }

    public String getEnemyName(){
        return enemyName;
    }
}


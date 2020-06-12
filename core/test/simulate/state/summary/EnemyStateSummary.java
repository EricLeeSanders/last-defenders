package simulate.state.summary;

public class EnemyStateSummary {

    private String enemyName;

    private int totalCount = 0;
    private int totalKills = 0;
    private float totalSpawnDelay = 0;
    private int totalArmor = 0;
    private int totalReachedEnd = 0;
    private int totalDead = 0;
    private int totalReward = 0;


    public EnemyStateSummary(String enemyName){
        this.enemyName = enemyName;
    }

    public void addCount(){
        totalCount++;
    }

    public void addKills(int kills){
        this.totalKills += kills;
    }

    public void addSpawnDelay(float spawnDelay){
        this.totalSpawnDelay += spawnDelay;
    }

    public void addArmorCount(){
        totalArmor++;
    }

    public void addReachedEndCount(){
        totalReachedEnd++;
    }

    public void addDeadCount(){
        totalDead++;
    }

    public void addReward(int reward){
        this.totalReward += reward;
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

    public float getTotalSpawnDelay() {

        return totalSpawnDelay;
    }

    public float getAverageSpawnDelay(){
        return totalSpawnDelay / totalCount;
    }

    public int getTotalArmor() {

        return totalArmor;
    }

    public int getTotalReachedEnd() {

        return totalReachedEnd;
    }

    public int getTotalDead() {

        return totalDead;
    }

    public int getTotalReward() {

        return totalReward;
    }

    public String getEnemyName(){
        return enemyName;
    }
}


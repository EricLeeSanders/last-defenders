package simulate.state;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.model.level.SpawningEnemy;
import simulate.state.combatactor.EnemyState;
import simulate.state.combatactor.TowerState;
import simulate.state.support.SupportState;

public class WaveState {
    private int waveNumber;
    private int livesStart;
    private int livesEnd;
    private int moneyStart;
    private int moneyEnd;
    private Array<Tower> towers = new Array<>();
    private Queue<SpawningEnemy> enemies = new Queue<>();
    private Array<TowerState> towerStates = new Array<>();
    private Array<EnemyState> enemyStates = new Array<>();
    private Array<SupportState> supportStates = new Array<>();

    public WaveState(int waveNumber, int livesStart, int moneyStart,
        Array<Tower> towers,
        Queue<SpawningEnemy> enemies) {

        this.waveNumber = waveNumber;
        this.livesStart = livesStart;
        this.towers = towers;
        this.enemies = enemies;
        this.moneyStart = moneyStart;

        for(Tower t : towers){
            this.towerStates.add(new TowerState(waveNumber, t));
        }

        for(SpawningEnemy e : enemies){
            this.enemyStates.add(new EnemyState(e));
        }
    }

    public void setLivesEnd(int livesEnd){
        this.livesEnd = livesEnd;
    }

    public void setMoneyEnd(int moneyEnd){
        this.moneyEnd = moneyEnd;
    }

    public int getWaveNumber() {

        return waveNumber;
    }

    public int getLivesStart() {

        return livesStart;
    }

    public int getLivesEnd() {

        return livesEnd;
    }

    public int getMoneyStart() {

        return moneyStart;
    }

    public int getMoneyEnd() {

        return moneyEnd;
    }

    public Array<Tower> getTowers() {

        return towers;
    }

    public Queue<SpawningEnemy> getEnemies() {

        return enemies;
    }

    public Array<TowerState> getTowerStates() {

        return towerStates;
    }

    public Array<EnemyState> getEnemyStates() {

        return enemyStates;
    }


//  SupportStates are added as the game is played unlike towers which are currently added only at the beginning of the wave.
//  Therefore, we need to have a method that adds Support States.
    public void addSupportState(SupportState supportState){
        this.supportStates.add(supportState);
    }

    public Array<SupportState> getSupportStates(){

        return this.supportStates;
    }

}

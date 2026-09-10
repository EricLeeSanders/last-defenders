package simulate.state;

import com.badlogic.gdx.utils.Array;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
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
    private Array<TowerState> towerStates = new Array<>();
    private Array<EnemyState> enemyStates = new Array<>();
    private Array<SupportState> supportStates = new Array<>();

    private float lastSpawnTime = 0f;

    public WaveState(int waveNumber, int livesStart, int moneyStart,
        Array<Tower> towers) {

        this.waveNumber = waveNumber;
        this.livesStart = livesStart;
        this.towers = towers;
        this.moneyStart = moneyStart;

        for(Tower t : towers){
            this.towerStates.add(new TowerState(waveNumber, t));
        }
    }

    /**
     * Records an enemy at the moment it spawns.
     *
     * Waves past Level.FILE_WAVE_LIMIT create their enemies on demand, so there is no queue to
     * read up front. Recording each enemy as it appears works for every wave, and the spawn
     * delay measured here is the pacing the wave actually ran at rather than the value asked
     * for, which Level rounds up to a whole frame.
     */
    public void enemySpawned(Enemy enemy, float waveTime) {

        this.enemyStates.add(new EnemyState(enemy, waveTime - lastSpawnTime));
        this.lastSpawnTime = waveTime;
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

    public Array<TowerState> getTowerStates() {

        return towerStates;
    }

    public Array<EnemyState> getEnemyStates() {

        return enemyStates;
    }


    /*
        SupportStates are added as the game is played unlike towers which are currently added only at the beginning of the wave.
        Therefore, we need to have a method that adds Support States.
     */
    public void addSupportState(SupportState supportState){
        this.supportStates.add(supportState);
    }

    public Array<SupportState> getSupportStates(){

        return this.supportStates;
    }

    public void gameOver(){
        for(TowerState state : towerStates){
            state.gameOver();
        }

        for(EnemyState state : enemyStates){
            state.gameOver();
        }
    }

}

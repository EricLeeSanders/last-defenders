package simulate.state.combatactor;

import com.badlogic.gdx.math.Vector2;
import com.lastdefenders.game.model.actor.combat.state.CombatActorStateObserver;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.actor.combat.enemy.state.EnemyStateEnum;
import com.lastdefenders.game.model.level.SpawningEnemy;

/**
 * Created by Eric on 12/16/2019.
 */

public class EnemyState extends CombatActorState implements
    CombatActorStateObserver<EnemyStateEnum, Enemy> {

    private float spawnDelay;
    private float speed;
    private boolean reachedEnd;
    private int reward;

    private Vector2 deadPosition;

    public EnemyState(SpawningEnemy actor) {
        super(actor.getEnemy());
        this.spawnDelay = actor.getSpawnDelay();
        this.speed = actor.getEnemy().getSpeed();
        this.reward = actor.getEnemy().getKillReward();
        actor.getEnemy().getStateManager().attachObserver(this);
    }

    public Float getSpawnDelay() {

        return spawnDelay;
    }

    public Float getSpeed() {

        return speed;
    }

    public boolean getReachedEnd(){
        return reachedEnd;
    }

    public void setReachedEnd(boolean reachedEnd){
        this.reachedEnd = reachedEnd;
    }


    public Vector2 getDeadPosition() {

        return deadPosition;
    }

    public void setDeadPosition(Vector2 deadPosition) {

        this.deadPosition = deadPosition;
    }

    public int getReward(){
        return reward;
    }

    @Override
    public void stateChange(EnemyStateEnum state, Enemy enemy) {
        if(state.equals(EnemyStateEnum.DEAD)){
            setDead(true);
            setDeadPosition(new Vector2(enemy.getPositionCenter()));
            addKills(enemy.getNumOfKills());
        } else if(state.equals(EnemyStateEnum.REACHED_END)){
            setReachedEnd(true);
            addKills(enemy.getNumOfKills());
        }
    }
}

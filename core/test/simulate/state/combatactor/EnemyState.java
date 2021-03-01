package simulate.state.combatactor;

import com.badlogic.gdx.math.Vector2;
import com.lastdefenders.game.model.actor.combat.CombatActor;
import com.lastdefenders.game.model.actor.combat.enemy.event.EnemyEventEnum;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.actor.combat.enemy.state.EnemyStateEnum;
import com.lastdefenders.game.model.actor.combat.event.CombatActorEventEnum;
import com.lastdefenders.game.model.actor.combat.event.CombatActorEventObserver;
import com.lastdefenders.game.model.actor.combat.event.EventObserver;
import com.lastdefenders.game.model.level.SpawningEnemy;

/**
 * Created by Eric on 12/16/2019.
 */

public class EnemyState extends CombatActorState {

    private float spawnDelay;
    private float speed;
    private boolean reachedEnd;
    private int reward;
    private Enemy actor;

    private Vector2 deadPosition;

    private EventObserver<CombatActor, CombatActorEventEnum> combatActorEventObserver = combatActorEventObserver();
    private EventObserver<Enemy, EnemyEventEnum> enemyEventObserver = enemyEventObserver();

    public EnemyState(SpawningEnemy actor) {
        super(actor.getEnemy());
        this.actor = actor.getEnemy();
        this.spawnDelay = actor.getSpawnDelay();
        this.speed = actor.getEnemy().getSpeed();
        this.reward = actor.getEnemy().getKillReward();
        actor.getEnemy().getCombatActorEventObserverManager().attachObserver(combatActorEventObserver);
        actor.getEnemy().getEnemyEventObserverManager().attachObserver(enemyEventObserver);
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

    private void detachFromObservers(){
        actor.getEnemyEventObserverManager().detachObserver(enemyEventObserver);
        actor.getCombatActorEventObserverManager().detachObserver(combatActorEventObserver);
    }

    private void deadEvent(CombatActor combatActor){
        setDead(true);
        setDeadPosition(new Vector2(combatActor.getPositionCenter()));
        addKills(combatActor.getNumOfKills());
        detachFromObservers();
    }

    private void reachedEndEvent(Enemy enemy){
        setReachedEnd(true);
        addKills(enemy.getNumOfKills());
        detachFromObservers();
    }

    public void gameOver(){

        // If the Enemy is dead or if they have reached the end, their kills have already been totaled.
        if (!isDead() && !getReachedEnd()) {
            addKills(actor.getNumOfKills());
        }

        detachFromObservers();
    }

    public EventObserver<CombatActor, CombatActorEventEnum> combatActorEventObserver(){

        return new EventObserver<CombatActor, CombatActorEventEnum>(){
            @Override
            public void eventNotification(CombatActorEventEnum event, CombatActor observerable) {
                switch(event){
                    case DEAD:
                        deadEvent(observerable);
                        break;
                }
            }
        };
    }

    public EventObserver<Enemy, EnemyEventEnum> enemyEventObserver(){

        return new EventObserver<Enemy, EnemyEventEnum>(){
            @Override
            public void eventNotification(EnemyEventEnum event, Enemy observerable) {
                switch(event){
                    case REACHED_END:
                        reachedEndEvent(observerable);
                }
            }
        };
    }
}

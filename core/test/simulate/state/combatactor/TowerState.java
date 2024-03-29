package simulate.state.combatactor;

import com.lastdefenders.game.model.actor.combat.CombatActor;
import com.lastdefenders.game.model.actor.combat.event.CombatActorEventEnum;
import com.lastdefenders.game.model.actor.combat.event.EventObserver;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.model.actor.combat.tower.event.TowerEventEnum;

/**
 * Created by Eric on 12/16/2019.
 */

public class TowerState extends CombatActorState  {

    private Boolean rangeIncreased;
    private Boolean speedIncreased;
    private Boolean attackIncreased;
    private Tower actor;
    private int waveNum;
    private EventObserver<Tower, TowerEventEnum> towerEventObserver = towerEventObserver();
    private EventObserver<CombatActor, CombatActorEventEnum> combatActorEventObserver = combatActorEventObserver();

    public TowerState(int waveNum, Tower actor) {
        super(actor);
        this.waveNum = waveNum;
        this.actor = actor;
        this.rangeIncreased = actor.hasIncreasedRange();
        this.speedIncreased = actor.hasIncreasedSpeed();
        this.attackIncreased = actor.hasIncreasedAttack();
        actor.getCombatActorEventObserverManager().attachObserver(combatActorEventObserver);
        actor.getTowerEventObserverManager().attachObserver(towerEventObserver);

    }

    public Boolean getRangeIncreased() {

        return rangeIncreased;
    }

    public Boolean getSpeedIncreased() {

        return speedIncreased;
    }

    public Boolean getAttackIncreased() {

        return attackIncreased;
    }

    private void detachFromObservers(){
        actor.getTowerEventObserverManager().detachObserver(towerEventObserver);
        actor.getCombatActorEventObserverManager().detachObserver(combatActorEventObserver);
    }

    private void deadEvent(CombatActor actor){
        setDead(true);
        detachFromObservers();
        // subtract the states number of kills (what the tower started with)
        // from the current number so we get how many were added this wave
        addKills(actor.getNumOfKills() - getKills());
    }

    private void waveEndEvent(Tower tower){
        detachFromObservers();
        addKills(tower.getNumOfKills() - getKills());
    }

    public void gameOver(){
        // If the Tower is dead, their kills have already been totaled.
        if(!isDead()) {
            addKills(actor.getNumOfKills() - getKills());
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

    public EventObserver<Tower, TowerEventEnum> towerEventObserver(){

        return new EventObserver<Tower, TowerEventEnum>(){
            @Override
            public void eventNotification(TowerEventEnum event, Tower observerable) {
                switch(event){
                    case WAVE_END:
                        waveEndEvent(observerable);
                        break;
                }
            }
        };
    }
}

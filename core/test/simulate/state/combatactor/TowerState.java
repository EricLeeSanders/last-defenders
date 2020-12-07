package simulate.state.combatactor;

import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.model.actor.combat.CombatActorStateObserver;
import com.lastdefenders.game.model.actor.combat.tower.state.states.TowerStateEnum;

/**
 * Created by Eric on 12/16/2019.
 */

public class TowerState extends CombatActorState implements CombatActorStateObserver<TowerStateEnum, Tower> {

    private Boolean rangeIncreased;
    private Boolean speedIncreased;
    private Boolean attackIncreased;

    public TowerState(Tower actor) {
        super(actor);
        this.rangeIncreased = actor.hasIncreasedRange();
        this.speedIncreased = actor.hasIncreasedSpeed();
        this.attackIncreased = actor.hasIncreasedAttack();
        actor.getStateManger().attachObserver(this);

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

    @Override
    public void stateChange(TowerStateEnum state, Tower combatActor) {

        if(state.equals(TowerStateEnum.DEAD)){
            setDead(true);
            combatActor.getStateManger().detachObserver(this);
            // subtract the states number of kills (what the tower started with)
            // from the current number so we get how many were added this wave
            addKills(combatActor.getNumOfKills() - getKills());
        } else if(state.equals(TowerStateEnum.WAVE_END)){
            combatActor.getStateManger().detachObserver(this);
            addKills(combatActor.getNumOfKills() - getKills());
        }
    }
}

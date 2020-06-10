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
    private int kills;

    public TowerState(Tower actor) {
        super(actor);
        this.rangeIncreased = actor.hasIncreasedRange();
        this.speedIncreased = actor.hasIncreasedSpeed();
        this.attackIncreased = actor.hasIncreasedAttack();
        this.kills = actor.getNumOfKills();
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

    public Integer getKills() {

        return kills;
    }

    @Override
    public void stateChange(TowerStateEnum state, Tower combatActor) {
        if(state.equals(TowerStateEnum.DEAD)){
            setDead(true);
            combatActor.getStateManger().detachObserver(this);
        } else if(state.equals(TowerStateEnum.WAVE_END)){
            combatActor.getStateManger().detachObserver(this);
        }
    }
}

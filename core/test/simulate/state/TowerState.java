package simulate.state;

import com.lastdefenders.game.model.actor.combat.tower.Tower;

/**
 * Created by Eric on 12/16/2019.
 */

public class TowerState extends CombatActorState{

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
}

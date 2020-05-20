package simulate.state.combat;

import static org.mockito.Mockito.doNothing;

import com.badlogic.gdx.math.Vector2;
import com.lastdefenders.game.model.actor.combat.CombatActor;
import simulate.state.GameActorState;

/**
 * Created by Eric on 12/16/2019.
 */

public abstract class CombatActorState extends GameActorState {
    private Boolean hasArmor;
    private String ID;
    private boolean dead;

    public CombatActorState(CombatActor actor){
        super(actor);
        this.hasArmor = actor.hasArmor();
        this.ID = actor.ID;
    }
    public Boolean getHasArmor() {

        return hasArmor;
    }
    public String getID(){

        return ID;
    }

    public boolean isDead() {

        return dead;
    }

    public void setDead(boolean dead) {

        this.dead = dead;
    }
}

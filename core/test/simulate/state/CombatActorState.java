package simulate.state;

import static org.mockito.Mockito.doNothing;

import com.badlogic.gdx.math.Vector2;
import com.lastdefenders.game.model.actor.combat.CombatActor;

/**
 * Created by Eric on 12/16/2019.
 */

public class CombatActorState {
    private Vector2 position;
    private Boolean hasArmor;
    private String name;
    private String ID;

    public CombatActorState(CombatActor actor){
        this.position = actor.getPositionCenter();
        this.hasArmor = actor.hasArmor();
        this.name = actor.getClass().getSimpleName();
        this.ID = actor.ID;
    }

    public Vector2 getPosition() {

        return position;
    }

    public Boolean getHasArmor() {

        return hasArmor;
    }

    public String getName() {

        return name;
    }

    public String getID(){

        return ID;
    }
}

package simulate.state.combatactor;

import com.badlogic.gdx.math.Vector2;
import com.lastdefenders.game.model.actor.combat.CombatActor;


/**
 * Created by Eric on 12/16/2019.
 */

public abstract class CombatActorState {
    private Boolean hasArmor;
    private String ID;
    private boolean dead;
    private Vector2 position;
    private String name;
    private int kills;

    public CombatActorState(CombatActor actor){
        this.hasArmor = actor.hasArmor();
        this.ID = actor.ID;
        this.position = new Vector2(actor.getPositionCenter().x, actor.getPositionCenter().y);
        this.name = actor.getClass().getSimpleName();
        this.kills = actor.getNumOfKills();
    }

    public Vector2 getPosition() {

        return position;
    }

    public String getName() {

        return name;
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

    public Integer getKills() {

        return kills;
    }

    void addKills(int kills){
        this.kills += kills;
    }
}

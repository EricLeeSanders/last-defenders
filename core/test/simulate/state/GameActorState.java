package simulate.state;

import com.badlogic.gdx.math.Vector2;
import com.lastdefenders.game.model.actor.GameActor;

public abstract class GameActorState {
    private Vector2 position;
    private String name;

    public GameActorState(GameActor actor){
        this.position = new Vector2(actor.getPositionCenter().x, actor.getPositionCenter().y);
        this.name = actor.getClass().getSimpleName();
    }

    public Vector2 getPosition() {

        return position;
    }

    public String getName() {

        return name;
    }

}

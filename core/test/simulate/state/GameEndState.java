package simulate.state;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.lastdefenders.game.model.Player;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import simulate.state.combat.tower.TowerState;

/**
 * Created by Eric on 12/16/2019.
 */

public class GameEndState {

    private PlayerState playerState;
    private Array<TowerState> towers = new Array<>();

    public GameEndState(
        Player player,
        Array<Actor> towers) {

        this.playerState = new PlayerState(player);

        for(Actor t : towers){
            this.towers.add(new TowerState((Tower)t));
        }

    }

    public PlayerState getPlayerState() {

        return playerState;
    }

    public Array<TowerState> getTowers() {

        return towers;
    }


    @Override
    public String toString() {

        return "GameState{" +
            "player=" + playerState +
            ", towers=" + towers +
            '}';
    }
}

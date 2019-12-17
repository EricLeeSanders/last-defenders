package simulate.state;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import com.lastdefenders.game.model.Player;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.model.level.SpawningEnemy;

/**
 * Created by Eric on 12/16/2019.
 */

public class GameBeginState {


    private PlayerState playerState;
    private Array<TowerState> towers = new Array<>();
    private Array<EnemyState> enemies = new Array<>();


    public GameBeginState(
        Player player,
        Array<Actor> towers,
        Queue<SpawningEnemy> enemies) {

        this.playerState = new PlayerState(player);

        for(Actor t : towers){
            this.towers.add(new TowerState((Tower)t));
        }

        for(SpawningEnemy e : enemies){
            this.enemies.add(new EnemyState(e));
        }

    }

    public PlayerState getPlayerState() {

        return playerState;
    }

    public Array<TowerState> getTowers() {

        return towers;
    }

    public Array<EnemyState> getEnemies() {

        return enemies;
    }


    @Override
    public String toString() {

        return "GameState{" +
            "player=" + playerState +
            ", towers=" + towers +
            ", enemies=" + enemies +
            '}';
    }
}

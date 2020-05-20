package simulate.state.combat;

import com.lastdefenders.game.model.level.SpawningEnemy;
import org.apache.poi.ss.usermodel.Row;
import simulate.state.combat.CombatActorState;

/**
 * Created by Eric on 12/16/2019.
 */

public class EnemyState extends CombatActorState {

    private float spawnDelay;
    private float speed;

    public EnemyState(SpawningEnemy actor) {
        super(actor.getEnemy());
        this.spawnDelay = actor.getSpawnDelay();
        this.speed = actor.getEnemy().getSpeed();
    }

    public Float getSpawnDelay() {

        return spawnDelay;
    }

    public Float getSpeed() {

        return speed;
    }

}

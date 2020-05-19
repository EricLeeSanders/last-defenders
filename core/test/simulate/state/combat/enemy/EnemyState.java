package simulate.state.combat.enemy;

import com.lastdefenders.game.model.level.SpawningEnemy;
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

package simulate.state;

import com.lastdefenders.game.model.actor.combat.CombatActor;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.level.SpawningEnemy;

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

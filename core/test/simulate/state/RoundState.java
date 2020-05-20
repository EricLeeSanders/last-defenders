package simulate.state;

import com.badlogic.gdx.utils.Array;
import simulate.state.combat.EnemyState;
import simulate.state.combat.TowerState;

public class RoundState {
    private int waveNumber;
    private Array<TowerState> towers = new Array<>();
    private Array<EnemyState> enemies = new Array<>();
}

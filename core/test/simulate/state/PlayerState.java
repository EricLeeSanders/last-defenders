package simulate.state;

import com.lastdefenders.game.model.Player;

/**
 * Created by Eric on 12/17/2019.
 */

public class PlayerState {

    private int money;
    private int waves;
    private int lives;

    public PlayerState(Player player){
        this.money = player.getMoney();
        this.waves = player.getWaveCount();
        this.lives = player.getLives();
    }

    public Integer getMoney() {

        return money;
    }

    public Integer getWaves() {

        return waves;
    }

    public Integer getLives() {

        return lives;
    }

    @Override
    public String toString() {

        return "PlayerState{" +
            "money=" + money +
            ", waves=" + waves +
            ", lives=" + lives +
            '}';
    }
}

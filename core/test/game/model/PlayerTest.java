package game.model;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.lastdefenders.game.model.Player;
import com.lastdefenders.game.model.PlayerObserver;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Eric on 5/26/2017.
 */

public class PlayerTest {

    @Before
    public void initPlayerTest() {

        Gdx.app = mock(Application.class);
    }

    @Test
    public void moneyTest1() {

        Player player = new Player();

        int startingMoneyAmount = player.getMoney();

        player.spendMoney(150);
        assertEquals(startingMoneyAmount - 150, player.getMoney());

        player.giveMoney(200);
        assertEquals(startingMoneyAmount + 50, player.getMoney());
    }

    @Test
    public void livesTest1() {

        Player player = new Player();

        int startingLivesAmount = player.getLives();

        player.enemyReachedEnd();
        player.enemyReachedEnd();

        assertEquals(startingLivesAmount - 2, player.getLives());
    }

    @Test
    public void observerTest1() {

        Player player = new Player();

        PlayerObserver playerObserver = mock(PlayerObserver.class);
        player.attachObserver(playerObserver);

        player.enemyReachedEnd();
        verify(playerObserver, times(1)).playerAttributeChange();

        player.spendMoney(150);
        verify(playerObserver, times(2)).playerAttributeChange();

        player.giveMoney(200);
        verify(playerObserver, times(3)).playerAttributeChange();

    }
}

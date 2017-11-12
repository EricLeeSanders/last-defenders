package game.ui.presenter.support;

import static org.mockito.Mockito.mock;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.lastdefenders.game.model.Player;
import com.lastdefenders.game.service.actorplacement.AirStrikePlacement;
import com.lastdefenders.game.service.actorplacement.SupplyDropPlacement;
import com.lastdefenders.game.service.actorplacement.SupportActorPlacement;
import com.lastdefenders.game.ui.presenter.SupportPresenter;
import com.lastdefenders.game.ui.state.GameUIStateManager;
import com.lastdefenders.game.ui.view.MessageDisplayer;
import com.lastdefenders.game.ui.view.SupportView;
import com.lastdefenders.util.LDAudio;
import org.junit.Before;

/**
 * Created by Eric on 6/10/2017.
 */

public class SupportPresenterTest {

    //GameUIStateManager uiStateManager, Player player, LDAudio audio
    //, SupportActorPlacement supportActorPlacement, AirStrikePlacement airStrikePlacement, SupplyDropPlacement supplyDropPlacement
    //, MessageDisplayer messageDisplayer
    GameUIStateManager uiStateManager = mock(GameUIStateManager.class);
    Player player = mock(Player.class);
    SupportActorPlacement supportActorPlacement = mock(SupportActorPlacement.class);
    AirStrikePlacement airStrikePlacement = mock(AirStrikePlacement.class);
    SupplyDropPlacement supplyDropPlacement = mock(SupplyDropPlacement.class);
    SupportView supportView = mock(SupportView.class);

    SupportPresenter createSupportPresenter() {

        LDAudio audio = mock(LDAudio.class);
        MessageDisplayer messageDisplayer = mock(MessageDisplayer.class);

        return new SupportPresenter(uiStateManager, player, audio, supportActorPlacement,
            airStrikePlacement, supplyDropPlacement, messageDisplayer);
    }

    @Before
    public void initSupportPresenterTest() {

        Gdx.app = mock(Application.class);
    }

}

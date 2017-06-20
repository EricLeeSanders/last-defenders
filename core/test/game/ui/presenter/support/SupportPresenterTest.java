package game.ui.presenter.support;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.foxholedefense.game.model.Player;
import com.foxholedefense.game.model.actor.support.SupplyDropCrate;
import com.foxholedefense.game.service.actorplacement.AirStrikePlacement;
import com.foxholedefense.game.service.actorplacement.SupplyDropPlacement;
import com.foxholedefense.game.service.actorplacement.SupportActorPlacement;
import com.foxholedefense.game.ui.presenter.SupportPresenter;
import com.foxholedefense.game.ui.state.GameUIStateManager;
import com.foxholedefense.game.ui.state.GameUIStateManager.GameUIState;
import com.foxholedefense.game.ui.view.MessageDisplayer;
import com.foxholedefense.game.ui.view.SupportView;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.datastructures.pool.FHDVector2;


import org.junit.Before;
import org.junit.Test;


import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by Eric on 6/10/2017.
 */

public class SupportPresenterTest {
    //GameUIStateManager uiStateManager, Player player, FHDAudio audio
    //, SupportActorPlacement supportActorPlacement, AirStrikePlacement airStrikePlacement, SupplyDropPlacement supplyDropPlacement
    //, MessageDisplayer messageDisplayer
    GameUIStateManager uiStateManager = mock(GameUIStateManager.class);
    Player player = mock(Player.class);
    SupportActorPlacement supportActorPlacement = mock(SupportActorPlacement.class);
    AirStrikePlacement airStrikePlacement = mock(AirStrikePlacement.class);
    SupplyDropPlacement supplyDropPlacement = mock(SupplyDropPlacement.class);
    SupportView supportView = mock(SupportView.class);

    SupportPresenter createSupportPresenter(){
        FHDAudio audio = mock(FHDAudio.class);
        MessageDisplayer messageDisplayer = mock(MessageDisplayer.class);

        return new SupportPresenter(uiStateManager, player, audio, supportActorPlacement, airStrikePlacement, supplyDropPlacement, messageDisplayer);
    }

    @Before
    public void initSupportPresenterTest() {
        Gdx.app = mock(Application.class);
    }

}

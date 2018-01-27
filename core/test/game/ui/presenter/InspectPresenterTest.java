package game.ui.presenter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lastdefenders.game.helper.CollisionDetection;
import com.lastdefenders.game.model.Player;
import com.lastdefenders.game.model.actor.ai.TowerAIType;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.model.level.state.LevelStateManager;
import com.lastdefenders.game.model.level.state.LevelStateManager.LevelState;
import com.lastdefenders.game.ui.presenter.InspectPresenter;
import com.lastdefenders.game.ui.state.GameUIStateManager;
import com.lastdefenders.game.ui.state.GameUIStateManager.GameUIState;
import com.lastdefenders.game.ui.view.InspectView;
import com.lastdefenders.game.ui.view.interfaces.MessageDisplayer;
import com.lastdefenders.util.LDAudio;
import com.lastdefenders.util.datastructures.pool.LDVector2;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import testutil.TestUtil;

/**
 * Created by Eric on 6/4/2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({CollisionDetection.class})
public class InspectPresenterTest {

    private GameUIStateManager gameUIStateManagerMock = mock(GameUIStateManager.class);
    private LevelStateManager levelStateManagerMock = mock(LevelStateManager.class);
    private Player playerMock = mock(Player.class);
    private InspectView inspectView = mock(InspectView.class);
    private Viewport gameViewportMock = mock(Viewport.class);

    @Before
    public void initInspectPresenterTest() {

        Gdx.app = mock(Application.class);
        PowerMockito.mockStatic(CollisionDetection.class);
    }

    public InspectPresenter createInspectPresenter() {

        Group towerGroup = new Group();
        LDAudio audio = mock(LDAudio.class);
        MessageDisplayer messageDisplayer = mock(MessageDisplayer.class);

        return new InspectPresenter(gameUIStateManagerMock, levelStateManagerMock, playerMock,
            towerGroup, audio, messageDisplayer, gameViewportMock);
    }


    /**
     * State change to Inspecting
     */
    @Test
    public void stateChangeTest1() {

        InspectPresenter inspectPresenter = createInspectPresenter();
        doReturn(GameUIState.STANDBY).when(gameUIStateManagerMock).getState();

        inspectPresenter.setView(inspectView);
        inspectPresenter.stateChange(GameUIState.INSPECTING);

        verify(inspectView, times(1)).standByState();
        verify(inspectView, times(1)).inspectingState();

    }

    /**
     * Successfully inspect a tower
     */
    @Test
    public void inspectTowerTest1() {

        InspectPresenter inspectPresenter = createInspectPresenter();
        doReturn(GameUIState.WAVE_IN_PROGRESS).when(gameUIStateManagerMock).getState();
        Tower tower = TestUtil.createTower("Rifle", true);
        tower.setActive(true);
        when(CollisionDetection
            .towerHit(Matchers.<SnapshotArray<Actor>>any(), isA(LDVector2.class)))
            .thenReturn(tower);

        float moveX = 20;
        float moveY = 20;
        LDVector2 coords = TestUtil.nonPooledLDVector2(moveX, moveY);
        TestUtil.mockViewportUnproject(coords, gameViewportMock);

        inspectPresenter.setView(inspectView);
        inspectPresenter.inspectTower(moveX,moveY);

        verify(gameUIStateManagerMock, times(1)).setState(eq(GameUIState.INSPECTING));
        inspectPresenter.stateChange(GameUIState.INSPECTING);
        verify(inspectView, times(1)).inspectingState();
    }

    /**
     * Unsuccessfully inspect a tower because the state is not Standby or Wave in Progress
     */
    @Test
    public void inspectTowerTest2() {

        InspectPresenter inspectPresenter = createInspectPresenter();
        doReturn(GameUIState.OPTIONS).when(gameUIStateManagerMock).getState();
        Tower tower = TestUtil.createTower("Rifle", true);
        when(CollisionDetection
            .towerHit(Matchers.<SnapshotArray<Actor>>any(), isA(LDVector2.class)))
            .thenReturn(tower);

        inspectPresenter.setView(inspectView);
        inspectPresenter.inspectTower(20,20);

        verify(gameUIStateManagerMock, never()).setState(eq(GameUIState.INSPECTING));
    }

    /**
     * Unsuccessfully inspect a tower because no tower was selected
     */
    @Test
    public void inspectTowerTest3() {

        InspectPresenter inspectPresenter = createInspectPresenter();
        doReturn(GameUIState.OPTIONS).when(gameUIStateManagerMock).getState();
        when(CollisionDetection
            .towerHit(Matchers.<SnapshotArray<Actor>>any(), isA(LDVector2.class)))
            .thenReturn(null);

        inspectPresenter.setView(inspectView);
        inspectPresenter.inspectTower(20,20);

        verify(gameUIStateManagerMock, never()).setState(eq(GameUIState.INSPECTING));
    }


    @Test
    public void changeTargetPriorityTest1() {

        //TODO improve this test? Bit of code smell
        InspectPresenter inspectPresenter = createInspectPresenter();
        Tower tower = TestUtil.createTower("Rifle", true);
        initInspectPresenter(inspectPresenter, tower);

        reset(gameUIStateManagerMock);
        doReturn(GameUIState.INSPECTING).when(gameUIStateManagerMock).getState();

        assertEquals(tower.getAI(), TowerAIType.FIRST);
        inspectPresenter.changeTargetPriority();
        assertEquals(tower.getAI(), TowerAIType.LAST);
    }


    /**
     * Sucessfully increase attack
     */
    @Test
    public void increaseAttackTest1() {

        InspectPresenter inspectPresenter = createInspectPresenter();
        Tower tower = TestUtil.createTower("Rifle", false);
        initInspectPresenter(inspectPresenter, tower);

        reset(gameUIStateManagerMock);
        doReturn(GameUIState.INSPECTING).when(gameUIStateManagerMock).getState();
        doReturn(10000).when(playerMock).getMoney();

        assertFalse(tower.hasIncreasedAttack());

        inspectPresenter.stateChange(GameUIState.INSPECTING);
        inspectPresenter.increaseAttack();

        assertTrue(tower.hasIncreasedAttack());
    }

    /**
     * Unsucessfully increase attack because there isn't enough money
     */
    @Test
    public void increaseAttackTest2() {

        InspectPresenter inspectPresenter = createInspectPresenter();
        Tower tower = TestUtil.createTower("Rifle", false);
        initInspectPresenter(inspectPresenter, tower);

        reset(gameUIStateManagerMock);
        doReturn(GameUIState.INSPECTING).when(gameUIStateManagerMock).getState();
        doReturn(1).when(playerMock).getMoney();

        assertFalse(tower.hasIncreasedAttack());

        inspectPresenter.stateChange(GameUIState.INSPECTING);
        inspectPresenter.increaseAttack();

        assertFalse(tower.hasIncreasedAttack());
    }

    /**
     * Unsucessfully increase attack because the state isn't INSPECTING
     */
    @Test
    public void increaseAttackTest3() {

        InspectPresenter inspectPresenter = createInspectPresenter();
        Tower tower = TestUtil.createTower("Rifle", false);
        initInspectPresenter(inspectPresenter, tower);

        reset(gameUIStateManagerMock);
        doReturn(GameUIState.ENLISTING).when(gameUIStateManagerMock).getState();
        doReturn(10000).when(playerMock).getMoney();

        assertFalse(tower.hasIncreasedAttack());

        inspectPresenter.increaseAttack();

        assertFalse(tower.hasIncreasedAttack());
    }

    /**
     * Unsucessfully increase attack because the tower already has upgrade
     */
    @Test
    public void increaseAttackTest4() {

        InspectPresenter inspectPresenter = createInspectPresenter();
        Tower tower = TestUtil.createTower("Rifle", true);
        doReturn(true).when(tower).hasIncreasedAttack();
        initInspectPresenter(inspectPresenter, tower);

        reset(gameUIStateManagerMock);
        doReturn(GameUIState.INSPECTING).when(gameUIStateManagerMock).getState();
        doReturn(10000).when(playerMock).getMoney();

        assertTrue(tower.hasIncreasedAttack());

        inspectPresenter.increaseAttack();

        verify(tower, never()).increaseAttack();
    }

    /**
     * Sucessfully give armor
     */
    @Test
    public void giveArmorTest1() {

        InspectPresenter inspectPresenter = createInspectPresenter();
        Tower tower = TestUtil.createTower("Tank", false);
        initInspectPresenter(inspectPresenter, tower);

        reset(gameUIStateManagerMock);
        doReturn(GameUIState.INSPECTING).when(gameUIStateManagerMock).getState();
        doReturn(10000).when(playerMock).getMoney();

        assertFalse(tower.hasArmor());

        inspectPresenter.stateChange(GameUIState.INSPECTING);
        inspectPresenter.giveArmor();

        assertTrue(tower.hasArmor());
    }

    /**
     * Unsucessfully give armor because there isn't enough money
     */
    @Test
    public void giveArmorTest2() {

        InspectPresenter inspectPresenter = createInspectPresenter();
        Tower tower = TestUtil.createTower("Turret", false);
        initInspectPresenter(inspectPresenter, tower);

        reset(gameUIStateManagerMock);
        doReturn(GameUIState.INSPECTING).when(gameUIStateManagerMock).getState();
        doReturn(1).when(playerMock).getMoney();

        assertFalse(tower.hasArmor());

        inspectPresenter.stateChange(GameUIState.INSPECTING);
        inspectPresenter.giveArmor();

        assertFalse(tower.hasArmor());
    }

    /**
     * Unsucessfully give armor because the state isn't INSPECTING
     */
    @Test
    public void giveArmorTest3() {

        InspectPresenter inspectPresenter = createInspectPresenter();
        Tower tower = TestUtil.createTower("Rifle", false);
        initInspectPresenter(inspectPresenter, tower);

        reset(gameUIStateManagerMock);
        doReturn(GameUIState.ENLISTING).when(gameUIStateManagerMock).getState();
        doReturn(10000).when(playerMock).getMoney();

        assertFalse(tower.hasArmor());

        inspectPresenter.giveArmor();

        assertFalse(tower.hasArmor());
    }

    /**
     * Unsucessfully give armor because the tower already has upgrade
     */
    @Test
    public void giveArmorTest4() {

        InspectPresenter inspectPresenter = createInspectPresenter();
        Tower tower = TestUtil.createTower("FlameThrower", true);
        doReturn(true).when(tower).hasArmor();
        initInspectPresenter(inspectPresenter, tower);

        reset(gameUIStateManagerMock);
        doReturn(GameUIState.INSPECTING).when(gameUIStateManagerMock).getState();
        doReturn(10000).when(playerMock).getMoney();

        assertTrue(tower.hasArmor());

        inspectPresenter.giveArmor();

        verify(tower, never()).setHasArmor(isA(Boolean.class));
    }

    /**
     * Sucessfully increase range
     */
    @Test
    public void increaseRangeTest1() {

        InspectPresenter inspectPresenter = createInspectPresenter();
        Tower tower = TestUtil.createTower("Tank", false);
        initInspectPresenter(inspectPresenter, tower);

        reset(gameUIStateManagerMock);
        doReturn(GameUIState.INSPECTING).when(gameUIStateManagerMock).getState();
        doReturn(10000).when(playerMock).getMoney();

        assertFalse(tower.hasIncreasedRange());

        inspectPresenter.stateChange(GameUIState.INSPECTING);
        inspectPresenter.increaseRange();

        assertTrue(tower.hasIncreasedRange());
    }

    /**
     * Unsucessfully increase range because there isn't enough money
     */
    @Test
    public void increaseRangeTest2() {

        InspectPresenter inspectPresenter = createInspectPresenter();
        Tower tower = TestUtil.createTower("Turret", false);
        initInspectPresenter(inspectPresenter, tower);

        reset(gameUIStateManagerMock);
        doReturn(GameUIState.INSPECTING).when(gameUIStateManagerMock).getState();
        doReturn(1).when(playerMock).getMoney();

        assertFalse(tower.hasIncreasedRange());

        inspectPresenter.stateChange(GameUIState.INSPECTING);
        inspectPresenter.increaseRange();

        assertFalse(tower.hasIncreasedRange());
    }

    /**
     * Unsucessfully increase range because the state isn't INSPECTING
     */
    @Test
    public void increaseRangeTest3() {

        InspectPresenter inspectPresenter = createInspectPresenter();
        Tower tower = TestUtil.createTower("Rifle", false);
        initInspectPresenter(inspectPresenter, tower);

        reset(gameUIStateManagerMock);
        doReturn(GameUIState.ENLISTING).when(gameUIStateManagerMock).getState();
        doReturn(10000).when(playerMock).getMoney();

        assertFalse(tower.hasIncreasedRange());

        inspectPresenter.increaseRange();

        assertFalse(tower.hasIncreasedRange());
    }

    /**
     * Unsucessfully increase range because the tower already has upgrade
     */
    @Test
    public void increaseRangeTest4() {

        InspectPresenter inspectPresenter = createInspectPresenter();
        Tower tower = TestUtil.createTower("FlameThrower", true);
        doReturn(true).when(tower).hasIncreasedRange();
        initInspectPresenter(inspectPresenter, tower);

        reset(gameUIStateManagerMock);
        doReturn(GameUIState.INSPECTING).when(gameUIStateManagerMock).getState();
        doReturn(10000).when(playerMock).getMoney();

        assertTrue(tower.hasIncreasedRange());

        inspectPresenter.increaseRange();

        verify(tower, never()).increaseRange();
    }

    /**
     * Sucessfully increase speed
     */
    @Test
    public void increaseSpeedTest1() {

        InspectPresenter inspectPresenter = createInspectPresenter();
        Tower tower = TestUtil.createTower("Tank", false);
        initInspectPresenter(inspectPresenter, tower);

        reset(gameUIStateManagerMock);
        doReturn(GameUIState.INSPECTING).when(gameUIStateManagerMock).getState();
        doReturn(10000).when(playerMock).getMoney();

        assertFalse(tower.hasIncreasedSpeed());

        inspectPresenter.stateChange(GameUIState.INSPECTING);
        inspectPresenter.increaseSpeed();

        assertTrue(tower.hasIncreasedSpeed());
    }

    /**
     * Unsucessfully increase speed because there isn't enough money
     */
    @Test
    public void increaseSpeedTest2() {

        InspectPresenter inspectPresenter = createInspectPresenter();
        Tower tower = TestUtil.createTower("Turret", false);
        initInspectPresenter(inspectPresenter, tower);

        reset(gameUIStateManagerMock);
        doReturn(GameUIState.INSPECTING).when(gameUIStateManagerMock).getState();
        doReturn(1).when(playerMock).getMoney();

        assertFalse(tower.hasIncreasedSpeed());

        inspectPresenter.stateChange(GameUIState.INSPECTING);
        inspectPresenter.increaseSpeed();

        assertFalse(tower.hasIncreasedSpeed());
    }

    /**
     * Unsucessfully increase speed because the state isn't INSPECTING
     */
    @Test
    public void increaseSpeedTest3() {

        InspectPresenter inspectPresenter = createInspectPresenter();
        Tower tower = TestUtil.createTower("Rifle", false);
        initInspectPresenter(inspectPresenter, tower);

        reset(gameUIStateManagerMock);
        doReturn(GameUIState.ENLISTING).when(gameUIStateManagerMock).getState();
        doReturn(10000).when(playerMock).getMoney();

        assertFalse(tower.hasIncreasedSpeed());

        inspectPresenter.increaseSpeed();

        assertFalse(tower.hasIncreasedSpeed());
    }

    /**
     * Unsucessfully increase speed because the tower already has upgrade
     */
    @Test
    public void increaseSpeedTest4() {

        InspectPresenter inspectPresenter = createInspectPresenter();
        Tower tower = TestUtil.createTower("FlameThrower", true);
        doReturn(true).when(tower).hasIncreasedSpeed();
        initInspectPresenter(inspectPresenter, tower);

        reset(gameUIStateManagerMock);
        doReturn(GameUIState.INSPECTING).when(gameUIStateManagerMock).getState();
        doReturn(10000).when(playerMock).getMoney();

        assertTrue(tower.hasIncreasedSpeed());

        inspectPresenter.increaseSpeed();

        verify(tower, never()).increaseSpeed();
    }

    /**
     * Successfully discharge tower
     */
    @Test
    public void dischargeTest1() {

        InspectPresenter inspectPresenter = createInspectPresenter();
        Tower tower = TestUtil.createTower("FlameThrower", true);
        initInspectPresenter(inspectPresenter, tower);

        reset(gameUIStateManagerMock);
        doReturn(GameUIState.INSPECTING).when(gameUIStateManagerMock).getState();
        doReturn(LevelState.STANDBY).when(levelStateManagerMock).getState();

        inspectPresenter.dishcharge();

        verify(gameUIStateManagerMock, times(1)).setStateReturn();
        verify(tower, times(1)).sellTower();
    }

    /**
     * Unsuccessfully discharge tower, Level State == Wave in progress
     */
    @Test
    public void dischargeTest2() {

        InspectPresenter inspectPresenter = createInspectPresenter();
        Tower tower = TestUtil.createTower("FlameThrower", true);
        initInspectPresenter(inspectPresenter, tower);

        reset(gameUIStateManagerMock);
        doReturn(GameUIState.INSPECTING).when(gameUIStateManagerMock).getState();
        doReturn(LevelState.WAVE_IN_PROGRESS).when(levelStateManagerMock).getState();

        inspectPresenter.dishcharge();

        verify(gameUIStateManagerMock, never()).setStateReturn();
        verify(tower, never()).sellTower();
    }

    /**
     * Unsuccessfully discharge tower, Tower is dead
     */
    @Test
    public void dischargeTest3() {

        InspectPresenter inspectPresenter = createInspectPresenter();
        Tower tower = TestUtil.createTower("FlameThrower", true);
        initInspectPresenter(inspectPresenter, tower);

        reset(gameUIStateManagerMock);
        doReturn(GameUIState.INSPECTING).when(gameUIStateManagerMock).getState();
        doReturn(LevelState.STANDBY).when(levelStateManagerMock).getState();
        tower.setDead(true);

        inspectPresenter.dishcharge();

        verify(gameUIStateManagerMock, never()).setStateReturn();
        verify(tower, never()).sellTower();
    }

    private void initInspectPresenter(InspectPresenter inspectPresenter, Tower tower) {

        doReturn(GameUIState.STANDBY).when(gameUIStateManagerMock).getState();
        when(CollisionDetection
            .towerHit(Matchers.<SnapshotArray<Actor>>any(), isA(LDVector2.class)))
            .thenReturn(tower);

        float moveX = 20;
        float moveY = 20;
        LDVector2 coords = TestUtil.nonPooledLDVector2(moveX, moveY);
        TestUtil.mockViewportUnproject(coords, gameViewportMock);

        inspectPresenter.setView(inspectView);
        inspectPresenter.inspectTower(moveX,moveY);
    }
}

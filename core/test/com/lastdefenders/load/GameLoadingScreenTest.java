package com.lastdefenders.load;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lastdefenders.game.helper.CollisionDetection;
import com.lastdefenders.screen.ScreenChanger;
import com.lastdefenders.sound.LDAudio;
import com.lastdefenders.state.GameStateManager;
import com.lastdefenders.store.StoreManager;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.Resources;
import com.lastdefenders.util.UserPreferences;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import testutil.TestUtil;

@PrepareForTest(Logger.class)
public class GameLoadingScreenTest {

    @Mock private GameStateManager gameStateManager;
    @Mock private ScreenChanger screenChanger;
    @Mock private UserPreferences userPreferences;
    @Mock private LDAudio audio;
    @Mock private StoreManager storeManager;
    @Mock private Viewport viewport;
    @Mock private Stage stage;
    @Mock private AssetManager assetManager;
    @Spy private Resources resources = TestUtil.createResourcesMock();

    @InjectMocks GameLoadingScreen gameLoadingScreen;

    @Before
    public void initGameLoadingScreenTest() {

        Gdx.app = mock(Application.class);
        MockitoAnnotations.initMocks(this);
        doReturn(userPreferences).when(resources).getUserPreferences();
    }


    @Test
    public void renderTest_resources_not_loaded(){
        doReturn(assetManager).when(resources).getManager();
        doReturn(false).when(assetManager).update();

        gameLoadingScreen.render(1f);

        verify(storeManager, never()).installed();
        verify(assetManager, times(1)).update();
        verify(screenChanger, never()).changeToMenu();
    }

    @Test
    public void renderTest_too_quick(){
        doReturn(assetManager).when(resources).getManager();
        doReturn(true).when(assetManager).update();

        gameLoadingScreen.render(1f);

        verify(storeManager, never()).installed();
        verify(assetManager, times(1)).update();
        verify(screenChanger, never()).changeToMenu();
    }


    @Test
    public void renderTest_store_manager_uninstalled(){
        doReturn(assetManager).when(resources).getManager();
        doReturn(true).when(assetManager).update();
        doReturn(false).when(storeManager).installed();

        gameLoadingScreen.render(1f);
        gameLoadingScreen.render(1f);


        verify(assetManager, times(2)).update();
        verify(screenChanger, never()).changeToMenu();
    }

    @Test
    public void renderTest_store_manager_installed(){
        doReturn(assetManager).when(resources).getManager();
        doReturn(true).when(assetManager).update();
        doReturn(true).when(storeManager).installed();

        gameLoadingScreen.render(1f);
        gameLoadingScreen.render(2f);


        verify(assetManager, times(2)).update();
        verify(screenChanger, times(1)).changeToMenu();
    }

    @Test
    public void renderTest_store_manager_uninstalled_max_load(){
        doReturn(assetManager).when(resources).getManager();
        doReturn(true).when(assetManager).update();
        doReturn(false).when(storeManager).installed();

        gameLoadingScreen.render(5f);


        verify(assetManager, times(1)).update();
        verify(screenChanger, times(1)).changeToMenu();
    }

    @Test
    public void show(){
        TextureAtlas atlasMock = mock(TextureAtlas.class);
        doReturn(atlasMock).when(resources).getAsset(Resources.LOAD_ATLAS, TextureAtlas.class);
        doReturn(null).when(atlasMock).findRegion("img-loading");

        gameLoadingScreen.show();

        verify(stage, times(1)).addActor(any(Image.class));
        verify(resources, times(1)).loadSkin();
        verify(audio, times(1)).load();
    }
}

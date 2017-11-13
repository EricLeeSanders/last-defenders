package com.lastdefenders.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lastdefenders.state.GameStateManager;
import com.lastdefenders.state.GameStateManager.GameState;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.Resources;

/**
 * An abstract screen class that handles resizing/viewport/updates
 *
 * @author Eric
 */
public abstract class AbstractScreen implements Screen {

    private OrthographicCamera camera;
    private InputMultiplexer imp;
    private GameStateManager gameStateManager;
    private Viewport viewport;

    protected AbstractScreen(GameStateManager gameStateManager) {

        this.gameStateManager = gameStateManager;
        camera = new OrthographicCamera();
        imp = new InputMultiplexer();
        viewport = new FitViewport(Resources.VIRTUAL_WIDTH, Resources.VIRTUAL_HEIGHT, getCamera());
        GLProfiler.enable();
    }

    protected abstract void renderElements(float delta);

    @Override
    public void render(float delta) {

        // clear the screen with the given RGB color (black)
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        renderElements(delta);
        //profile();
    }


    private void profile() {

        System.out.println(
            "  Drawcalls: " + GLProfiler.drawCalls +
                ", Calls: " + GLProfiler.calls +
                ", TextureBindings: " + GLProfiler.textureBindings +
                ", ShaderSwitches:  " + GLProfiler.shaderSwitches +
                ", vertexCount: " + GLProfiler.vertexCount.value
        );
        GLProfiler.reset();
    }

    @Override
    public void hide() {

        Logger.info("Abstract Screen Hiding");
    }

    @Override
    public void pause() {

        Logger.info("Abstract Screen: pausing");
        gameStateManager.setState(GameState.PAUSE);
    }

    @Override
    public void resume() {

        Logger.info("Abstract Screen: resuming");
        gameStateManager.setState(GameState.PLAY);

    }

    @Override
    public void resize(int width, int height) {
        System.out.println(width + ", " + height);
        viewport.update(width, height, true);
    }

    @Override
    public void show() {

        Logger.info("Abstract Screen: show");
        Gdx.input.setInputProcessor(imp);
    }

    @Override
    public void dispose() {

        Logger.info("Abstract Screen: Disposing");
        GLProfiler.disable();

    }

    private Camera getCamera() {

        return camera;
    }

    protected Viewport getViewport() {

        return viewport;
    }

    protected InputMultiplexer getInputMultiplexer() {

        return imp;
    }

    protected void addInputProcessor(InputProcessor ip) {

        imp.addProcessor(ip);
    }

}

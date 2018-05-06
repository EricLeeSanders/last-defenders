package com.lastdefenders.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lastdefenders.state.GameStateManager;
import com.lastdefenders.state.GameStateManager.GameState;
import com.lastdefenders.util.Logger;

/**
 * An abstract screen class
 *
 * @author Eric
 */
public abstract class AbstractScreen implements Screen {

    private InputMultiplexer imp;
    private GameStateManager gameStateManager;
    private Array<Viewport> viewports = new Array<>();

    protected AbstractScreen(GameStateManager gameStateManager) {

        this.gameStateManager = gameStateManager;
        imp = new InputMultiplexer();
        //GLProfiler.enable();
    }

    protected abstract void renderElements(float delta);

    @Override
    public void render(float delta) {

        // clear the screen with the given RGB color (black)
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderElements(delta);
        //profile();

    }


    private void profile() {

//        System.out.println(
//            "  Drawcalls: " + GLProfiler.drawCalls +
//                ", Calls: " + GLProfiler.calls +
//                ", TextureBindings: " + GLProfiler.textureBindings +
//                ", ShaderSwitches:  " + GLProfiler.shaderSwitches +
//                ", vertexCount: " + GLProfiler.vertexCount.value
//        );
//        GLProfiler.reset();
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
        for(Viewport viewport : viewports){
            viewport.update(width, height, true);
            System.out.println(viewport.getScreenX() + ", " + viewport.getScreenY() + ", " + viewport.getScreenWidth()
                + ", " + viewport.getScreenHeight() + ", " + viewport.getRightGutterX() + ", " + viewport.getTopGutterY()
                + ", " + viewport.getWorldWidth() + ", " + viewport.getWorldHeight());
        }
    }

    @Override
    public void show() {

        Logger.info("Abstract Screen: show");
        Gdx.input.setInputProcessor(imp);
    }

    @Override
    public void dispose() {

        Logger.info("Abstract Screen: Disposing");
        //GLProfiler.disable();

    }

    protected void addViewport(Viewport viewport){

        viewports.add(viewport);
    }

    protected InputMultiplexer getInputMultiplexer() {

        return imp;
    }

    protected void addInputProcessor(InputProcessor ip) {

        imp.addProcessor(ip);
    }

}

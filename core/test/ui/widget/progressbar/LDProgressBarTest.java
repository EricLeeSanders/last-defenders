package ui.widget.progressbar;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.lastdefenders.ui.widget.progressbar.LDProgressBar;
import com.lastdefenders.ui.widget.progressbar.LDProgressBar.LDProgressBarPadding;
import com.lastdefenders.ui.widget.progressbar.LDProgressBar.LDProgressBarStyle;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Eric on 2/15/2018.
 */

public class LDProgressBarTest {

    @Before
    public void initLDProgressBarTest() {

        Gdx.app = mock(Application.class);
    }

    private TextureRegionDrawable setupTextureRegionDrawableMock(){
        TextureRegionDrawable drawable = mock(TextureRegionDrawable.class);
        TextureRegion textureRegionMock = mock(TextureRegion.class);
        Texture textureMock = mock(Texture.class);

        doReturn(textureRegionMock).when(drawable).getRegion();
        doReturn(textureMock).when(textureRegionMock).getTexture();

        return drawable;
    }

    @Test
    public void filledTest1() {

        //(float min, float max, float stepSize, LDProgressBarPadding padding, LDProgressBarStyle style) {
        LDProgressBarPadding padding = new LDProgressBarPadding(2f);
        TextureRegionDrawable frameMock = setupTextureRegionDrawableMock();
        TextureRegionDrawable filledMock = setupTextureRegionDrawableMock();
        TextureRegionDrawable unfilledMock = setupTextureRegionDrawableMock();
        LDProgressBarStyle style = new LDProgressBarStyle(frameMock, filledMock, unfilledMock);
        LDProgressBar progressBar = new LDProgressBar(0, 1, 0.00001f, padding, style);

        progressBar.setSize(50,20);
        progressBar.setPosition(200,220);
        progressBar.setValue(0.5f);

        Batch batchMock = mock(Batch.class);
        progressBar.draw(batchMock, 1f);

        verify(batchMock, times(1))
            .draw(eq(filledMock.getRegion()), eq(202.0f), eq(222.0f),
                eq(23.0f), eq(8.0f), eq(46.0f),
                eq(16.0f), eq(1.0f), eq(1.0f),
                eq(0.0f));


    }

    @Test
    public void unfilledTest1() {

        //(float min, float max, float stepSize, LDProgressBarPadding padding, LDProgressBarStyle style) {
        LDProgressBarPadding padding = new LDProgressBarPadding(2f);
        TextureRegionDrawable frameMock = setupTextureRegionDrawableMock();
        TextureRegionDrawable filledMock = setupTextureRegionDrawableMock();
        TextureRegionDrawable unfilledMock = setupTextureRegionDrawableMock();
        LDProgressBarStyle style = new LDProgressBarStyle(frameMock, filledMock, unfilledMock);
        LDProgressBar progressBar = new LDProgressBar(0, 1, 0.00001f, padding, style);

        progressBar.setSize(50,20);
        progressBar.setPosition(200,220);
        progressBar.setValue(0.5f);

        Batch batchMock = mock(Batch.class);
        progressBar.draw(batchMock, 1f);

        verify(batchMock, times(1))
            .draw(eq(unfilledMock.getRegion().getTexture()), eq(225.0f), eq(222.0f),
                eq(23.0f), eq(8.0f), eq(23.0f),
                eq(16.0f), eq(1.0f), eq(1.0f),
                eq(0.0f), eq(0), eq(0), eq(0),
                eq(0), eq(false), eq(false) );


    }

}

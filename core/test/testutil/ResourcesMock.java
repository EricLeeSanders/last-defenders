package testutil;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;
import com.lastdefenders.util.Resources;
import com.lastdefenders.util.UserPreferences;

public class ResourcesMock {

    public static Resources create(){
        Array<AtlasRegion> atlasRegion = new Array<>();
        atlasRegion.add(null);

        Resources resources = mock(Resources.class);
        UserPreferences userPreferences = UserPreferencesMock.create();
        when(resources.getAtlasRegion(anyString())).thenReturn(atlasRegion);
        when(resources.getTexture(anyString())).thenReturn(null);
        when(resources.getUserPreferences()).thenReturn(userPreferences);

        return resources;
    }
}

package testutil;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import com.badlogic.gdx.Preferences;
import com.lastdefenders.util.UserPreferences;

public class UserPreferencesMock {
    public static UserPreferences create(){

        UserPreferences userPreferences = mock(UserPreferences.class);
        Preferences preferences = mock(Preferences.class);

        doReturn(preferences).when(userPreferences).getPreferences();

        return userPreferences;
    }
}

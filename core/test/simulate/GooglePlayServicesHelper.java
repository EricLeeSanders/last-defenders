package simulate;

import com.lastdefenders.googleplay.GooglePlayAchievement;
import com.lastdefenders.googleplay.GooglePlayLeaderboard;
import com.lastdefenders.googleplay.GooglePlayServices;
import java.util.concurrent.CompletableFuture;

/**
 * Created by Eric on 7/22/2018.
 */

public class GooglePlayServicesHelper implements GooglePlayServices {

    @Override
    public boolean isDeviceCompatible() {

        return false;
    }

    @Override
    public CompletableFuture<Boolean> signIn() {
        return null;
    }

    @Override
    public void unlockAchievement(GooglePlayAchievement achievement) {

    }

    @Override
    public void submitScore(GooglePlayLeaderboard leaderboard, int score) {

    }

    @Override
    public void showAchievements() {

    }

    @Override
    public void showLeaderboard(GooglePlayLeaderboard leaderboard) {

    }

    @Override
    public void showLeaderboards() {

    }

    @Override
    public boolean isSignedIn() {

        return false;
    }
}

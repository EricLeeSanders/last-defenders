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
    public CompletableFuture<Boolean> signInAsync() {
        return CompletableFuture.completedFuture(false);
    }

    @Override
    public void requestSignInWithConfirmation(SignInConfirmationCallback callback) {
        // Test stub doesn't support Google Play, immediately cancel
        callback.onUserCancelled();
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
    public void showAchievementsWithSignIn() {
        // Test stub doesn't support Google Play
    }

    @Override
    public void showLeaderboardWithSignIn(GooglePlayLeaderboard leaderboard) {
        // Test stub doesn't support Google Play
    }

    @Override
    public void showLeaderboardsWithSignIn() {
        // Test stub doesn't support Google Play
    }

    @Override
    public boolean isSignedIn() {
        return false;
    }
}

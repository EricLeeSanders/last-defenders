package com.lastdefenders.android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;
import com.badlogic.gdx.Gdx;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.games.GamesSignInClient;
import com.google.android.gms.games.PlayGames;
import com.google.android.gms.games.PlayGamesSdk;
import com.google.android.gms.tasks.Task;
import com.lastdefenders.googleplay.GooglePlayAchievement;
import com.lastdefenders.googleplay.GooglePlayLeaderboard;
import com.lastdefenders.googleplay.GooglePlayServices;
import com.lastdefenders.util.Logger;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;
import org.acra.ACRA;

/**
 * Created by Eric on 6/16/2018.
 */

public class GooglePlayServicesHelper implements GooglePlayServices {

    /*
     Request Codes
     */
    private static final int RC_ACHIEVEMENT_UI = 9003;
    private static final int RC_LEADERBOARD_UI = 9004;

    // Auth state tracking
    private enum AuthState {
        UNKNOWN,           // Initial state, checking authentication
        AUTHENTICATED,     // User is signed in
        NOT_AUTHENTICATED  // User is not signed in
    }

    private AuthState authState = AuthState.UNKNOWN;
    private CompletableFuture<Boolean> initialAuthCheck;

    private AndroidLauncher androidLauncher;
    private LoadingView loadingView;

    void initialize(AndroidLauncher androidLauncher, RelativeLayout layout){
        Logger.info("GooglePlayServicesHelper: Initializing");
        this.androidLauncher = androidLauncher;
        this.loadingView = new LoadingView(layout, androidLauncher);

        PlayGamesSdk.initialize(androidLauncher);
        initialAuthCheck = signInSilently();
    }

    private void handleAndShowError(Exception exception){

        Logger.error("Google Play Services Error", exception);

        int themeFromGoogle = 5; // Matches GPS dialogs
        AlertDialog.Builder builder = new AlertDialog.Builder(androidLauncher, themeFromGoogle);

        builder.setMessage("An unexpected error occurred.");
        builder.setTitle("Google Play Games");

        builder.setPositiveButton("Ok", (DialogInterface.OnClickListener) (dialog, which) -> {
            // Do nothing
        });

        builder.create().show();

    }

    private CompletableFuture<Boolean> signInSilently(){
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        GamesSignInClient gamesSignInClient = PlayGames.getGamesSignInClient(androidLauncher);
        gamesSignInClient.isAuthenticated().addOnCompleteListener(isAuthenticatedTask -> {
            boolean isAuthenticated =
                (isAuthenticatedTask.isSuccessful() &&
                    isAuthenticatedTask.getResult().isAuthenticated());

            Logger.info("GooglePlayServicesHelper: signInSilently - " + isAuthenticated);

            // Update auth state
            authState = isAuthenticated ? AuthState.AUTHENTICATED : AuthState.NOT_AUTHENTICATED;

            future.complete(isAuthenticated);
        });

        return future;
    }

    @Override
    public CompletableFuture<Boolean> signIn(){

        GamesSignInClient gamesSignInClient = PlayGames.getGamesSignInClient(androidLauncher);

        CompletableFuture<Boolean> future = new CompletableFuture<>();

        if(!verifyGPSAvailable()){
            authState = AuthState.NOT_AUTHENTICATED;
            future.complete(false);
            return future;
        }

        gamesSignInClient.signIn()
            .addOnCompleteListener(authResult -> {
                if(authResult.isSuccessful()){
                    boolean authenticated = authResult.getResult().isAuthenticated();
                    authState = authenticated ? AuthState.AUTHENTICATED : AuthState.NOT_AUTHENTICATED;
                    future.complete(authenticated);
                } else {
                    authState = AuthState.NOT_AUTHENTICATED;
                    handleAndShowError(authResult.getException());
                    future.complete(false);
                }
            });

        return future;
    }

    @Override
    public CompletableFuture<Boolean> signInAsync() {
        // If already authenticated, return immediately
        if(authState == AuthState.AUTHENTICATED) {
            Logger.info("signInAsync: Already authenticated");
            return CompletableFuture.completedFuture(true);
        }

        // If initial check still pending, wait for it then sign in if needed
        if(initialAuthCheck != null && !initialAuthCheck.isDone()) {
            Logger.info("signInAsync: Waiting for initial auth check");
            return initialAuthCheck.thenCompose(authenticated -> {
                if(authenticated) {
                    Logger.info("signInAsync: Initial check shows authenticated");
                    return CompletableFuture.completedFuture(true);
                }
                Logger.info("signInAsync: Initial check shows not authenticated, triggering sign-in");
                return signIn();
            });
        }

        // Not authenticated, trigger sign-in
        Logger.info("signInAsync: Not authenticated, triggering sign-in");
        return signIn();
    }

    @Override
    public boolean isDeviceCompatible() {
        return true;
    }

    private void handleGPSAvailableError(int connResult){

        Logger.error("GooglePlayServicesHelper - handleGPSAvailableError: connection result " + connResult);
        if(connResult != ConnectionResult.SUCCESS) {
            androidLauncher.runOnUiThread(() -> {
                GoogleApiAvailability.getInstance().getErrorDialog(androidLauncher, connResult, 0).show();
            });
        }

    }

    private boolean verifyGPSAvailable(){
        int connResult = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(androidLauncher);
        if(connResult == ConnectionResult.SUCCESS){
            Logger.info("GooglePlayServicesHelper verifyGPSAvailable: GPS Available");
            return true;
        } else {
            handleGPSAvailableError(connResult);
            return false;
        }
    }


    @Override
    public void unlockAchievement(GooglePlayAchievement achievement) {

        try {
            PlayGames.getAchievementsClient(androidLauncher)
                .unlock(achievement.getId());
        } catch (Exception e){
            Logger.error("Error unlocking achievement " + achievement.getId(), e);
        }
    }

    @Override
    public void submitScore(final GooglePlayLeaderboard leaderboard, final int score){
        try {
            PlayGames.getLeaderboardsClient(androidLauncher)
                .submitScore(leaderboard.getId(), score);
        } catch (Exception e){
            Logger.error("Error submitting score: " + score + "for: " + leaderboard.getId(), e);
        }
    }

    private void handleGPSTaskIntent(Supplier<Task<Intent>> task, int activityReqCode){
        androidLauncher.runOnUiThread(() -> {
            loadingView.showLoadingView();

            task.get()
                .addOnSuccessListener(intent -> {
                    try {
                        androidLauncher.startActivityForResult(intent, activityReqCode);
                    } catch (Exception e) {
                        Logger.error("Google Play services failed.", e);
                        handleAndShowError(e);
                    }
                })
                .addOnFailureListener(e -> {
                    Logger.error("Google Play services failed.", e);
                    handleAndShowError(e);
                })
                .addOnCompleteListener(task1 -> loadingView.hideLoadingView());
        });
    }

    @Override
    public void showAchievements(){
        handleGPSTaskIntent(() -> PlayGames.getAchievementsClient(androidLauncher)
            .getAchievementsIntent(), RC_ACHIEVEMENT_UI);
    }

    @Override
    public void showLeaderboard(final GooglePlayLeaderboard leaderboard){
        handleGPSTaskIntent(() -> PlayGames.getLeaderboardsClient(androidLauncher)
            .getLeaderboardIntent(leaderboard.getId()), RC_LEADERBOARD_UI);
    }

    @Override
    public void showLeaderboards() {
        handleGPSTaskIntent(() -> PlayGames.getLeaderboardsClient(androidLauncher)
            .getAllLeaderboardsIntent(), RC_LEADERBOARD_UI);
    }

    @Override
    public void showAchievementsWithSignIn() {
        if(isSignedIn()) {
            showAchievements();
        } else {
            requestSignInWithConfirmation(new GooglePlayServices.SignInConfirmationCallback() {
                @Override
                public void onUserConfirmed() {
                    showAchievements();
                }

                @Override
                public void onUserCancelled() {
                    Logger.info("GooglePlayServicesHelper: User cancelled, not showing achievements");
                }
            });
        }
    }

    @Override
    public void showLeaderboardWithSignIn(GooglePlayLeaderboard leaderboard) {
        if(isSignedIn()) {
            showLeaderboard(leaderboard);
        } else {
            requestSignInWithConfirmation(new GooglePlayServices.SignInConfirmationCallback() {
                @Override
                public void onUserConfirmed() {
                    showLeaderboard(leaderboard);
                }

                @Override
                public void onUserCancelled() {
                    Logger.info("GooglePlayServicesHelper: User cancelled, not showing leaderboard");
                }
            });
        }
    }

    @Override
    public void showLeaderboardsWithSignIn() {
        if(isSignedIn()) {
            showLeaderboards();
        } else {
            requestSignInWithConfirmation(new GooglePlayServices.SignInConfirmationCallback() {
                @Override
                public void onUserConfirmed() {
                    showLeaderboards();
                }

                @Override
                public void onUserCancelled() {
                    Logger.info("GooglePlayServicesHelper: User cancelled, not showing leaderboards");
                }
            });
        }
    }

    @Override
    public boolean isSignedIn(){
        return authState == AuthState.AUTHENTICATED;
    }

    @Override
    public void requestSignInWithConfirmation(GooglePlayServices.SignInConfirmationCallback callback) {
        androidLauncher.runOnUiThread(() -> {
            int themeFromGoogle = 5; // Matches GPS dialogs
            AlertDialog.Builder builder = new AlertDialog.Builder(androidLauncher, themeFromGoogle);

            builder.setTitle("Sign In Required");
            builder.setMessage("You need to sign in to Google Play Games to use this feature. Would you like to sign in now?");

            builder.setPositiveButton("Sign In", (dialog, which) -> {
                // User confirmed, show loading view and trigger sign-in
                Logger.info("GooglePlayServicesHelper: User confirmed sign-in request");

                androidLauncher.runOnUiThread(() -> loadingView.showLoadingView());

                signInAsync().thenAccept(success -> {
                    // Hide loading view
                    androidLauncher.runOnUiThread(() -> loadingView.hideLoadingView());

                    Gdx.app.postRunnable(() -> {
                        if(success) {
                            Logger.info("GooglePlayServicesHelper: Sign-in successful");
                            callback.onUserConfirmed();
                        } else {
                            Logger.info("GooglePlayServicesHelper: Sign-in failed or cancelled");
                            callback.onUserCancelled();
                        }
                    });
                });
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> {
                Logger.info("GooglePlayServicesHelper: User cancelled sign-in request");
                Gdx.app.postRunnable(() -> callback.onUserCancelled());
            });

            builder.setCancelable(true);
            builder.setOnCancelListener(dialog -> {
                Logger.info("GooglePlayServicesHelper: User dismissed sign-in dialog");
                Gdx.app.postRunnable(() -> callback.onUserCancelled());
            });

            builder.create().show();
        });
    }

    void backButtonPressed(){
        loadingView.hideLoadingView();
    }
}

package com.lastdefenders.android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;
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

    private boolean userAuthenticated = false;
    private AndroidLauncher androidLauncher;
    private LoadingView loadingView;

    void initialize(AndroidLauncher androidLauncher, RelativeLayout layout){
        Logger.info("GooglePlayServicesHelper: Initializing");
        this.androidLauncher = androidLauncher;
        this.loadingView = new LoadingView(layout, androidLauncher);

        PlayGamesSdk.initialize(androidLauncher);
        signInSilently();
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

    private void signInSilently(){
        GamesSignInClient gamesSignInClient = PlayGames.getGamesSignInClient(androidLauncher);
        gamesSignInClient.isAuthenticated().addOnCompleteListener(isAuthenticatedTask -> {
            boolean isAuthenticated =
                (isAuthenticatedTask.isSuccessful() &&
                    isAuthenticatedTask.getResult().isAuthenticated());
            Logger.info("GooglePlayServicesHelper: signInSilently - " + isAuthenticated);
            userAuthenticated = isAuthenticated;
        });
    }

    @Override
    public CompletableFuture<Boolean> signIn(){

        GamesSignInClient gamesSignInClient = PlayGames.getGamesSignInClient(androidLauncher);

        CompletableFuture<Boolean> future = new CompletableFuture<>();

        if(!verifyGPSAvailable()){
            future.complete(false);
            return future;
        }

        gamesSignInClient.signIn()
            .addOnCompleteListener(authResult -> {
                if(authResult.isSuccessful()){
                    userAuthenticated = authResult.getResult().isAuthenticated();
                    future.complete(userAuthenticated);
                } else {
                    handleAndShowError(authResult.getException());
                    future.complete(false);
                }
            });

        return future;
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

    private Boolean syncSignIn() throws ExecutionException, InterruptedException {
        if(!isSignedIn()){
            CompletableFuture<Boolean> signedIn = signIn();
            return signedIn.get(); // Causes a block/wait
        } else {
            return true;
        }
    }

    private void handleGPSTaskIntent(Supplier<Task<Intent>> task, int activityReqCode){

        new Thread(() -> {

            Boolean signedIn = null;
            try {
                signedIn = syncSignIn();
            } catch (Exception e) {
                Logger.error("Google Play Services: Error signing in", e);
            }

            if(Boolean.FALSE.equals(signedIn)){
                return;
            }

            androidLauncher.runOnUiThread(() -> {

                loadingView.showLoadingView();

                task.get()
                    .addOnSuccessListener(intent -> {

                        try {
                            androidLauncher
                                .startActivityForResult(intent, activityReqCode);
                        } catch (Exception e) {
                            Logger.error("Google Play services failed to login.", e);
                            handleAndShowError(e);
                        }
                    })
                    .addOnFailureListener(e -> {

                        Logger.error("Google Play services failed to login.", e);
                        handleAndShowError(e);
                    }).addOnCompleteListener(task1 -> loadingView.hideLoadingView());
            });
        }).start();

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
    public boolean isSignedIn(){
        return userAuthenticated;
    }

    void backButtonPressed(){
        loadingView.hideLoadingView();
    }
}

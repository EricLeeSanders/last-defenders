package com.lastdefenders.android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.games.Games;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.lastdefenders.googleplay.GooglePlayAchievement;
import com.lastdefenders.googleplay.GooglePlayLeaderboard;
import com.lastdefenders.googleplay.GooglePlayServices;
import com.lastdefenders.util.Logger;
import org.acra.ACRA;

/**
 * Created by Eric on 6/16/2018.
 */

public class GooglePlayServicesHelper implements GooglePlayServices {

    /*
     Request Codes
     */
    static final int RC_SIGN_IN = 9001;
    private static final int RC_ACHIEVEMENT_UI = 9003;
    private static final int RC_LEADERBOARD_UI = 9004;

    private static final String GOOGLE_PLAY_GAMES_PKG_NAME = "com.google.android.play.games";

    private GoogleSignInAccount signedInAccount = null;
    private GoogleSignInClient signInClient;
    private AndroidLauncher androidLauncher;
    private LoadingView loadingView;
    private View gameView;

    void initialize(AndroidLauncher androidLauncher, RelativeLayout layout, View gameView){
        Logger.info("GooglePlayServicesHelper: Initializing");
        this.androidLauncher = androidLauncher;
        this.gameView = gameView;
        this.loadingView = new LoadingView(layout, androidLauncher);

        if(isGooglePlayGamesAppInstalled() && isGooglePlayServicesAvailable()){
            signInClient = GoogleSignIn.getClient(androidLauncher,
                GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);
            signInSilently();
        } else {
            int googlePlayServicesAvailability = googlePlayServicesAvailability();
            if(!isGooglePlayGamesAppInstalled()){
                Logger.error("Google Play Games is not installed. ");
            } else if( googlePlayServicesAvailability == ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED){
                Logger.error("Google Play Services requires update." );
            } else {
                Logger.error("Google Play Services is not available. Result Code: " + googlePlayServicesAvailability);
            }
        }
        Logger.info("GooglePlayServicesHelper: Initialized");
    }


    void handleGooglePlaySignInRequest(Intent data){

        int googlePlayServicesAvailability = googlePlayServicesAvailability();
        if(!isGooglePlayGamesAppInstalled()){
            showGooglePlayGamesNotInstalledDialog();
            return;
        } else if( googlePlayServicesAvailability == ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED){
            showServicesUpdateRequiredDialog();
            return;
        }

        if(googlePlayServicesAvailability != ConnectionResult.SUCCESS){
            //If the connection result is not success, log it and continue. Let Google Play handle what happens
            Logger.error("Google Play Services is not available. Result Code: " + googlePlayServicesAvailability);
        }

        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        if (result.isSuccess()) {
            // The signed in account is stored in the result.
            signedInAccount = result.getSignInAccount();
            handleSignIn();
            Logger.info("Logged into Google Play from Intent");
            showSignedInDialog();
        } else {
            String message = result.getStatus().getStatusMessage();

            Logger.info("Intent log in failed: " + result.getStatus() + " status code: " + result.getStatus().getStatusCode());

            if (message == null || message.isEmpty()) {
                message = androidLauncher.getString(R.string.signin_other_error);
            }
            new AlertDialog.Builder(androidLauncher).setMessage(message)
                .setNeutralButton(android.R.string.ok, null).show();
        }
    }

    private boolean isReadyForGooglePlayGamesServices(){
        return signInClient != null;
    }

    private boolean isGooglePlayGamesAppInstalled() {
        try {
            androidLauncher.getPackageManager().getPackageInfo(GOOGLE_PLAY_GAMES_PKG_NAME, 0);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    public boolean isGooglePlayServicesAvailable() {
        return googlePlayServicesAvailability() == ConnectionResult.SUCCESS;
    }

    private int googlePlayServicesAvailability() {
        return GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(androidLauncher);
    }

    private void showGooglePlayGamesNotInstalledDialog(){
        Logger.error("Google Play Games App is not installed.");

        String message = "You do not have Google Play Games installed."
            + " This app uses Google Play Games for optional features. Would you like to install?";
        String title = "Do you want to install?";

        showDialog(message, title, GOOGLE_PLAY_GAMES_PKG_NAME);

    }

    private void showServicesUpdateRequiredDialog(){
        Logger.error("Play Services requires update.");

        String message = "Your Google Play Services is out of date."
            + " This app uses Google Play Services for optional features. Would you like to update?";
        String title = "Do you want to update?";
        String packageName = "com.google.android.gms";

        showDialog(message, title, packageName);

    }

    private void showDefaultErrorDialog(String errorMessage){
        Logger.error("Google Play Services error. " + errorMessage);

        AlertDialog.Builder builder = new AlertDialog.Builder(androidLauncher);

        builder.setMessage("Sorry, there was an error with Google Play Services. " + errorMessage)
                .setTitle("Google Play Services Error");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showDialog(String message, String title, final String packageName){

        AlertDialog.Builder builder = new AlertDialog.Builder(androidLauncher);

        builder.setMessage(message).setTitle(title);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                try {
                    androidLauncher.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
                }catch (android.content.ActivityNotFoundException e) {
                    androidLauncher.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showSignedInDialog(){
        String message = "Sucessfully signed into Google Play Games";
        new AlertDialog.Builder(androidLauncher).setMessage(message)
            .setNeutralButton(android.R.string.ok, null).show();
    }

    private void handleGooglePlayServicesError(Exception exception, boolean silent){

        ACRA.getErrorReporter().handleException(exception);

        Logger.error("Google Play Services Error", exception);

        if(!silent) {
            showDefaultErrorDialog("Unexpected Error.");
        }
    }

    private void handleSignIn(){
        Games.getGamesClient(androidLauncher,signedInAccount).setViewForPopups(gameView);
        Games.getGamesClient(androidLauncher,signedInAccount).setGravityForPopups(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
    }

    private void signInSilently() {

        if(!isReadyForGooglePlayGamesServices()){
            return;
        }
        // Try to get last signed in account
        signedInAccount = GoogleSignIn.getLastSignedInAccount(androidLauncher);
        if(signedInAccount != null){
            Logger.info("Sign In Silently - Retrieved last signed in account.");
            // We were able to get the last signed in account and are now signed in.
            handleSignIn();
            return;
        }

        // Attempt to sign in silently
        signInClient.silentSignIn().addOnCompleteListener(androidLauncher,
            new OnCompleteListener<GoogleSignInAccount>() {
                @Override
                public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                    Logger.info("Attempted to sign in silently.");
                    if (task.isSuccessful()) {
                        Logger.info("Sign in silently successful");
                        // The signed in account is stored in the task's result.
                        signedInAccount = task.getResult();
                        handleSignIn();
                    }
                }
            })
            .addOnFailureListener(androidLauncher, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Logger.error("Sign In Silently. Failure.", e);
                }
            });
    }

    @Override
    public void signIn(){

            androidLauncher.runOnUiThread(new Runnable(){
                @Override
                public void run() {

                    try {
                        if (isSignedIn()) {
                            Logger.info("Is Signed In");
                            return;
                        }

                        // Check for errors
                        int googlePlayServicesAvailability = GoogleApiAvailability.getInstance()
                            .isGooglePlayServicesAvailable(androidLauncher);
                        if(!isGooglePlayGamesAppInstalled()){
                            showGooglePlayGamesNotInstalledDialog();
                        } else if(googlePlayServicesAvailability == ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED){
                            showServicesUpdateRequiredDialog();
                        } else if(googlePlayServicesAvailability != ConnectionResult.SUCCESS) {
                            showDefaultErrorDialog("Error signing in. Result Code: " + googlePlayServicesAvailability);
                        }

                        // Start sign in intent
                        Intent intent = signInClient.getSignInIntent();
                        androidLauncher.startActivityForResult(intent, RC_SIGN_IN);
                    } catch (Exception e) {
                        Logger.error("Google Play sign in failed", e);
                        handleGooglePlayServicesError(e, false);
                    }
                }
            });
        }

    @Override
    public void signOut() {
        if(!isSignedIn()){
            Logger.info("Not signing out. User is not signed in.");
            return;
        }
        try {
            signInClient.signOut().addOnCompleteListener(androidLauncher,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // at this point, the user is signed out.
                        Logger.info("User is signed out.");
                        signedInAccount = null;
                    }
                });
        } catch (Exception e){
            Logger.error("Google Play Sign Out failed." , e);
            handleGooglePlayServicesError(e, false);
        }
    }

    @Override
    public void unlockAchievement(GooglePlayAchievement achievement) {

        if(!isSignedIn()){
            signInSilently();
        }
        if(isSignedIn()) {
            try {
                Games.getAchievementsClient(androidLauncher, signedInAccount)
                    .unlock(achievement.getId());
            }  catch (Exception e){
                Logger.error("Google Play unlock achievement: " + achievement + " failed." , e);
                handleGooglePlayServicesError(e, true);
            }
        }
    }

    @Override
    public void submitScore(final GooglePlayLeaderboard leaderboard, final int score){

        if(!isSignedIn()){
            signInSilently();
        }
        if(isSignedIn()) {
            try {
                Games.getLeaderboardsClient(androidLauncher, signedInAccount)
                    .submitScore(leaderboard.getId(), score);
            } catch (Exception e) {
                Logger
                    .error("Google Play submit score for leaderboard: " + leaderboard + " failed.",
                        e);
                handleGooglePlayServicesError(e, true);
            }
        }
    }

    @Override
    public void showAchievements(){

        androidLauncher.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (!isSignedIn()) {
                        signInSilently();
                    }
                    if (isSignedIn()) {
                        loadingView.showLoadingView();
                        Games.getAchievementsClient(androidLauncher, signedInAccount)
                            .getAchievementsIntent()
                            .addOnSuccessListener(new OnSuccessListener<Intent>() {
                                @Override
                                public void onSuccess(Intent intent) {
                                    try {
                                        //throw new RuntimeException("Test exception");
                                    androidLauncher
                                        .startActivityForResult(intent, RC_ACHIEVEMENT_UI);
                                    } catch (Exception e){
                                        Logger.error("Google Play show achievements failed." , e);
                                        handleGooglePlayServicesError(e, false);
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Logger.error("Google Play show achievements failed." , e);
                                    handleGooglePlayServicesError(e, false);
                                }
                            })
                            .addOnCompleteListener(new OnCompleteListener<Intent>() {
                                @Override
                                public void onComplete(@NonNull Task<Intent> task) {
                                    loadingView.hideLoadingView();
                                }
                            });
                    }
                }  catch (Exception e){
                    Logger.error("Google Play show achievements failed." , e);
                    handleGooglePlayServicesError(e, false);
                }
            }
        });
    }

    @Override
    public void showLeaderboard(final GooglePlayLeaderboard leaderboard){

        androidLauncher.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                try {
                    if (!isSignedIn()) {
                        signInSilently();
                    }
                    if (isSignedIn()) {
                        loadingView.showLoadingView();
                        Games.getLeaderboardsClient(androidLauncher, signedInAccount)
                            .getLeaderboardIntent(leaderboard.getId())
                            .addOnSuccessListener(new OnSuccessListener<Intent>() {
                                @Override
                                public void onSuccess(Intent intent) {

                                    try {
                                        androidLauncher
                                            .startActivityForResult(intent, RC_LEADERBOARD_UI);
                                    } catch (Exception e) {
                                        Logger.error("Google Play show leaderboard: " + leaderboard
                                            + " failed.", e);
                                        handleGooglePlayServicesError(e, false);
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Logger.error("Google Play show leaderboard: " + leaderboard + " failed.",
                                        e);
                                    handleGooglePlayServicesError(e, false);
                                }
                            })
                            .addOnCompleteListener(new OnCompleteListener<Intent>() {
                                @Override
                                public void onComplete(@NonNull Task<Intent> task) {

                                    loadingView.hideLoadingView();
                                }
                            });
                    }
                } catch (Exception e) {
                    Logger.error("Google Play show leaderboard: " + leaderboard + " failed.", e);
                    handleGooglePlayServicesError(e, false);
                }
            }
        });
    }

    @Override
    public void showLeaderboards() {
        androidLauncher.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (!isSignedIn()) {
                        return;
                    }
                    loadingView.showLoadingView();
                    Games.getLeaderboardsClient(androidLauncher, signedInAccount)
                        .getAllLeaderboardsIntent()
                        .addOnSuccessListener(new OnSuccessListener<Intent>() {
                            @Override
                            public void onSuccess(Intent intent) {
                                try {
                                    androidLauncher
                                        .startActivityForResult(intent, RC_LEADERBOARD_UI);
                                }  catch (Exception e){
                                    Logger.error("Google Play show leaderboards failed." , e);
                                    handleGooglePlayServicesError(e, false);
                                }
                            }
                        })
                        .addOnCompleteListener(new OnCompleteListener<Intent>() {
                            @Override
                            public void onComplete(@NonNull Task<Intent> task) {
                                loadingView.hideLoadingView();
                            }
                        });
                }  catch (Exception e){
                    Logger.error("Google Play show leaderboard failed." , e);
                    handleGooglePlayServicesError(e, false);
                }
            }
        });
    }

    @Override
    public boolean isSignedIn(){
        return signedInAccount != null;
    }

    void onResume(){
        signInSilently();
    }

    void backButtonPressed(){
        loadingView.hideLoadingView();
    }
}

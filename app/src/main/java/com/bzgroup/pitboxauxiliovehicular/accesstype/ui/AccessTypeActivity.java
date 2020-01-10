package com.bzgroup.pitboxauxiliovehicular.accesstype.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bzgroup.pitboxauxiliovehicular.MainActivity;
import com.bzgroup.pitboxauxiliovehicular.R;
import com.bzgroup.pitboxauxiliovehicular.accesstype.AccessTypePresenter;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.bzgroup.pitboxauxiliovehicular.utils.Utils.isLoggedInFacebook;
import static com.bzgroup.pitboxauxiliovehicular.utils.Utils.isLogginFirebaseWithGoogle;
import static com.bzgroup.pitboxauxiliovehicular.utils.Utils.logoutInFacebook;
import static com.bzgroup.pitboxauxiliovehicular.utils.Utils.logoutInFirebase;

public class AccessTypeActivity extends AppCompatActivity implements IAccessTypeView {

    private static final int RC_SIGN_IN = 111;
    private static final String TAG = AccessTypeActivity.class.getSimpleName();
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.activity_access_type_sign_in_google_btn)
    SignInButton signInGoogleBtn;

    @BindView(R.id.activity_access_type_sign_in_facebook_btn)
    LoginButton activity_access_type_sign_in_facebook_btn;

    GoogleSignInClient mGoogleSignInClient;

    private FirebaseAuth mAuth;

    CallbackManager callbackManager;

    private AccessTypePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_type);
        ButterKnife.bind(this);

//        getWindow().getDecorView().setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        mPresenter = new AccessTypePresenter(this);
        mPresenter.onCreate();

        setupGoogleSignIn();
        setupFirebaseAuth();
        setupFacebookSignIn();
    }

    private void setupFacebookSignIn() {
        callbackManager = CallbackManager.Factory.create();
        activity_access_type_sign_in_facebook_btn.setReadPermissions(Arrays.asList("email", "public_profile"));

        activity_access_type_sign_in_facebook_btn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                if (loginResult != null) {
                    Log.d(TAG, "onSuccess: ");
                    Log.d(TAG, "onSuccess: " + loginResult.toString());
                    navigateToMainScreen();
                } else {
                    Log.d(TAG, "onSuccess: null");
                    Toast.makeText(AccessTypeActivity.this, "loginResult null", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancel() {
                // App code
                Log.d(TAG, "onCancel: ");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.e(TAG, "onError: ", exception);
            }
        });
    }

    private void setupFirebaseAuth() {
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void updateUIF(FirebaseUser currentUser) {
        if (currentUser != null) {
//            Toast.makeText(this, "Está autenticado", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "updateUIF DisplayName: " + currentUser.getDisplayName());
            Log.d(TAG, "updateUIF Email: " + currentUser.getEmail());
            Log.d(TAG, "updateUIF PhoneNumber: " + currentUser.getPhoneNumber());
            Log.d(TAG, "updateUIF Uid: " + currentUser.getUid());
            Log.d(TAG, "updateUIF ProviderId: " + currentUser.getProviderId());
            Log.d(TAG, "updateUIF PhotoUrl: " + currentUser.getPhotoUrl());
            Log.d(TAG, "updateUIF getIdToken: " + currentUser.getIdToken(true));

            mPresenter.handleSignInFirebase(currentUser.getUid(),
                    currentUser.getEmail(), currentUser.getDisplayName(), "", currentUser.getPhotoUrl() != null ? currentUser.getPhotoUrl().toString() : "");
//            navigateToMainScreen();
        } else {
            // El usuario NO se logueó en la app con Google
            Toast.makeText(this, "No está autenticado", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorMessage(String errorMessage) {
        if (isLoggedInFacebook()) {
            logoutInFacebook(this);
        } else if (isLogginFirebaseWithGoogle()) {
            logoutInFirebase(this);
        }
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUIF(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.activity_access_type_container), "Authenticación fallida.", Snackbar.LENGTH_SHORT).show();
                            updateUIF(null);
                        }

                        // ...
                    }
                });
    }

    private void updateUI(GoogleSignInAccount account) {
        if (account != null) {
            // El usuario ya se logueó en la app con Google
            Toast.makeText(this, "Está autenticado", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "updateUI Email: " + account.getEmail());
            Log.d(TAG, "updateUI Display Name: " + account.getDisplayName());
            Log.d(TAG, "updateUI Display Family Name: " + account.getFamilyName());
            Log.d(TAG, "updateUI Display Id: " + account.getId());
            Log.d(TAG, "updateUI Display IdToken: " + account.getIdToken());
        } else {
            // El usuario NO se logueó en la app con Google
            Toast.makeText(this, "No está autenticado", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupGoogleSignIn() {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .requestId()
                .requestIdToken(getString(R.string.default_web_client_id))
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        signInGoogleBtn.setSize(SignInButton.SIZE_STANDARD);
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void navigateToMainScreen() {
        hideProgress();
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    @Override
    public void navigateToSignInEmail() {

    }

    @OnClick(R.id.activity_access_type_sign_in_google_btn)
    @Override
    public void handleGoogleSignIn() {
        signIn();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: resultCode " + resultCode);
        Log.d(TAG, "onActivityResult: requestCode " + requestCode);
        switch (requestCode) {
            case RC_SIGN_IN:
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//                handleSignInResult(task);

                try {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    firebaseAuthWithGoogle(account);
                } catch (ApiException e) {
                    // Google Sign In failed, update UI appropriately
                    Log.e(TAG, "Google sign in failed", e);
                    hideProgress();
                }
                break;
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.d(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    @Override
    public void handleFacebookSignIn() {

    }

    private void signIn() {
        showProgress();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


}
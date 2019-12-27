package com.bzgroup.pitboxauxiliovehicular;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bzgroup.pitboxauxiliovehicular.accesstype.ui.AccessTypeActivity;
import com.bzgroup.pitboxauxiliovehicular.menu.ui.MainMenuFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import static com.bzgroup.pitboxauxiliovehicular.utils.Utils.isLoggedInFacebook;
import static com.bzgroup.pitboxauxiliovehicular.utils.Utils.isLogginFirebaseWithGoogle;
import static com.bzgroup.pitboxauxiliovehicular.utils.Utils.isLogginGoogle;
import static com.bzgroup.pitboxauxiliovehicular.utils.Utils.logoutInFacebook;
import static com.bzgroup.pitboxauxiliovehicular.utils.Utils.logoutInFirebase;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, MainMenuFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        navView.setOnNavigationItemSelectedListener(this);
        setFragment(new MainMenuFragment());
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_help, R.id.navigation_profile)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.navigation_home:
                Toast.makeText(this, "Home", Toast.LENGTH_LONG).show();

                break;
            case R.id.navigation_help:
//                Toast.makeText(this, "Help", Toast.LENGTH_LONG).show();
                setFragment(new MainMenuFragment());
                break;
            case R.id.navigation_profile:
                Toast.makeText(this, "Profile", Toast.LENGTH_LONG).show();
                signOut();
//                startActivity(new Intent(this, AccessTypeActivity.class));
//                finish();
                break;
        }
        return true;
    }

    public void signOut() {
        if (isLoggedInFacebook()) {
            logoutInFacebook(this);
        } else if (isLogginFirebaseWithGoogle()) {
            logoutInFirebase();
        }
        startActivity(new Intent(this, AccessTypeActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    private void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.nav_host_fragment, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

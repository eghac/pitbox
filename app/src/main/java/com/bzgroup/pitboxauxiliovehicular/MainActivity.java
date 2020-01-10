package com.bzgroup.pitboxauxiliovehicular;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.bzgroup.pitboxauxiliovehicular.accesstype.ui.AccessTypeActivity;
import com.bzgroup.pitboxauxiliovehicular.menu.ui.MainMenuFragment;
import com.bzgroup.pitboxauxiliovehicular.mainprofile.ui.MainProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import butterknife.ButterKnife;

import static com.bzgroup.pitboxauxiliovehicular.utils.Utils.isLoggedInFacebook;
import static com.bzgroup.pitboxauxiliovehicular.utils.Utils.isLogginFirebaseWithGoogle;
import static com.bzgroup.pitboxauxiliovehicular.utils.Utils.logoutInFacebook;
import static com.bzgroup.pitboxauxiliovehicular.utils.Utils.logoutInFirebase;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, MainMenuFragment.OnFragmentInteractionListener, OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
//        BottomNavigationView navView = findViewById(R.id.nav_view);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

//        getWindow().getDecorView().setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        navigationView.setNavigationItemSelectedListener(this);
//        navView.setOnNavigationItemSelectedListener(this);
        setFragments(new MainMenuFragment(), -1);
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

        int id = menuItem.getItemId();
        setFragments(null, id);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
//
//        switch (menuItem.getItemId()) {
//            case R.id.navigation_home:
//                Toast.makeText(this, "Home", Toast.LENGTH_LONG).show();
//
//                break;
//            case R.id.navigation_help:
////                Toast.makeText(this, "Help", Toast.LENGTH_LONG).show();
//                setFragments(new MainMenuFragment());
//                break;
//            case R.id.navigation_profile:
//                Toast.makeText(this, "Profile", Toast.LENGTH_LONG).show();
//                signOut();
////                startActivity(new Intent(this, AccessTypeActivity.class));
////                finish();
//                break;
//        }
//        return true;
    }

    public void signOut() {
        if (isLoggedInFacebook()) {
            logoutInFacebook(this);
        } else if (isLogginFirebaseWithGoogle()) {
            logoutInFirebase(this);
        }
        startActivity(new Intent(this, AccessTypeActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    private void setFragments(Fragment fragment, int id, Bundle bundle) {
        switch (id) {
//            case R.id.nav_profile:
////                if (CommonUtils.verifyInternetConnection(this)) {
////                    if (accessType != AccessTypeEvent.SIGN_IN_GUEST)
////                        startActivity(new Intent(this, ProfileActivity.class));
////                    else
//                fragment = new ProfileFragment();
////                        mostrarDialogGuest();
////                } else
//////                    Toast.makeText(this, getString(R.string.no_connection), Toast.LENGTH_LONG).show();
//                break;
//            case R.id.nav_dealers:
//                startActivity(new Intent(this, DealersActivity.class));
//                break;
//            case R.id.nav_orders:
////                fragment = new PendingOrdersFragment();
//                startActivity(new Intent(this, PendingOrdersActivity.class));
//                break;
//            case R.id.nav_approved_orders:
////                fragment = new ApprovedOrdersFragment();
//                startActivity(new Intent(this, ApprovedOrdersActivity.class));
//                break;
//            case R.id.nav_history_orders:
//                startActivity(new Intent(this, HistoryOrdersActivity.class));
//                break;
//            case R.id.nav_frequent_questions:
//                startActivity(new Intent(this, FrequentQuestionsActivity.class));
//                break;
//            case R.id.nav_wholesalers:
//                fragment = new WholesalersFragment();
//                break;
//            case R.id.nav_services:
//                startActivity(new Intent(this, ServicesActivity.class));
//                break;
//            case R.id.nav_support:
//                startActivity(new Intent(this, SupportActivity.class));
//                break;
//            case R.id.nav_sign_off:
//                if (CommonUtils.verifyInternetConnection(this))
//                    showDialogSignOffConfirm();
//                else
//                    Toast.makeText(this, getString(R.string.no_connection), Toast.LENGTH_LONG).show();
//                break;
            case R.id.nav_request_service:
                fragment = new MainMenuFragment();
                break;
            case R.id.nav_guide:
                break;
            case R.id.nav_profile:
                startActivity(new Intent(this, MainProfileActivity.class));
                break;
            case R.id.nav_sign_off:
                signOut();
                break;
        }
        if (fragment != null) {
            if (fragment instanceof MainMenuFragment)
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            else
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
            if (bundle != null)
                fragment.setArguments(bundle);
        }
    }

    private void setFragments(Fragment fragment, int id) {
        setFragments(fragment, id, null);
    }

    private void setFragments(Fragment fragment) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.add(R.id.nav_host_fragment, fragment);
//        fragmentTransaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

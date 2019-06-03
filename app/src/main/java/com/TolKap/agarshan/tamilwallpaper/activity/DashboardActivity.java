package com.TolKap.agarshan.tamilwallpaper.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.codemybrainsout.ratingdialog.RatingDialog;
import com.TolKap.agarshan.tamilwallpaper.R;
import com.TolKap.agarshan.tamilwallpaper.fragment.AllCategoryFragment;
import com.TolKap.agarshan.tamilwallpaper.fragment.FavoritesFragment;
import com.TolKap.agarshan.tamilwallpaper.fragment.HomeFragment;
import com.TolKap.agarshan.tamilwallpaper.fragment.PopularImagesFragment;
import com.TolKap.agarshan.tamilwallpaper.fragment.RecentImagesFragment;
import com.github.javiersantos.appupdater.AppUpdater;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseUser;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,FavoritesFragment.OnFragmentInteractionListener,PopularImagesFragment.OnFragmentInteractionListener,HomeFragment.OnFragmentInteractionListener,AllCategoryFragment.OnFragmentInteractionListener,RecentImagesFragment.OnFragmentInteractionListener {

    private HomeFragment homeFragment;
    private FavoritesFragment favoritesFragment;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

//        MobileAds.initialize(this, "ca-app-pub-7266672635430903~3436972622");

        mAdView = findViewById(R.id.adView);




        final RatingDialog ratingDialog = new RatingDialog.Builder(this)
                .threshold(3)
                .session(7)
                .onRatingBarFormSumbit(new RatingDialog.Builder.RatingDialogFormListener() {
                    @Override
                    public void onFormSubmitted(String feedback) {

                    }
                }).build();

        ratingDialog.show();

        homeFragment = new HomeFragment();
        favoritesFragment = new FavoritesFragment();
        pushFragment(homeFragment, R.id.dashboardContainer, false, "home_fragment");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Home");


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        AppUpdater appUpdater = new AppUpdater(this);
        appUpdater.start();


    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (!drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.openDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            this.finish();
        }
    }




    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            setTitle("Home");
            pushFragment(homeFragment, R.id.dashboardContainer, false, "home_fragment");

            // Handle the camera action
        } else if (id == R.id.nav_favourite) {
            setTitle("Favourites");
            pushFragment(favoritesFragment, R.id.dashboardContainer, false, "fav_fragment");


        } else if (id == R.id.nav_rateUs) {

            final RatingDialog ratingDialog = new RatingDialog.Builder(this)
                    .threshold(3)
                    .onRatingBarFormSumbit(new RatingDialog.Builder.RatingDialogFormListener() {
                        @Override
                        public void onFormSubmitted(String feedback) {

                        }
                    }).build();

            ratingDialog.show();


        } else if (id == R.id.nav_share) {
            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "Ovam Tamil HD Wallpapers");
                String sAux = "\nLet me recommend you this application.Download Latest Tamil HD Wallpapers\n\n";
                sAux = sAux + "https://play.google.com/store/apps/details?id=com.TolKap.agarshan.tamilwallpaper\n\n";
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "choose one"));


            } catch(Exception e) {
                //e.toString();
            }

        }else if (id == R.id.nav_feedback) {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto","ovamfeedback@gmail.com", null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Ovam feedback");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "");
            startActivity(Intent.createChooser(emailIntent, "Send feedback..."));

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void pushFragment(Fragment fragment, int container, boolean shouldAddToBackStack, String tag){
        try{
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(container, fragment);

            if(shouldAddToBackStack){
                transaction.addToBackStack(tag);
            }else {
                transaction.addToBackStack(null);
            }

            
            transaction.commitAllowingStateLoss();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void onFragmentInteraction(Uri uri) {


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        MenuItem item = menu.findItem(R.id.acionSearch);
        SearchView searchView =(SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
               Intent intent = new Intent(DashboardActivity.this,SearchimagesActivity.class);
               intent.putExtra("search",s);
               startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {



                return false;

            }
        });

        return super.onCreateOptionsMenu(menu);
    }



    @Override
    protected void onResume() {
        super.onResume();
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


    }
}

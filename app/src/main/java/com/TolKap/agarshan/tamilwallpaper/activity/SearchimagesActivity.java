package com.TolKap.agarshan.tamilwallpaper.activity;

import android.content.Context;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.InputMethodManager;

import android.widget.TextView;
import android.widget.Toast;

import com.TolKap.agarshan.tamilwallpaper.R;
import com.TolKap.agarshan.tamilwallpaper.adapter.AllImagesAdapter;
import com.TolKap.agarshan.tamilwallpaper.custom.CustomFontTextView;
import com.TolKap.agarshan.tamilwallpaper.model.AllImagesModel;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

public class SearchimagesActivity extends AppCompatActivity {

    private RecyclerView allImagesRv;
    private AllImagesAdapter allImagesAdapter;
    private FirebaseDatabase mfirebaseDatabase;
    private DatabaseReference mref,mref1;
    private ArrayList<AllImagesModel> allImagesModelList;
    private AllImagesModel model;
    private Query qry;
    private LayoutAnimationController controller;
    private AdView mAdView;
    private CustomFontTextView categoryNameTv;
    private TextView noInternetTv;
    private AVLoadingIndicatorView avi;

    private String searchTag;
    private TextView noItemsTv;
    private InterstitialAd mInterstitialAd;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_images);

        avi = findViewById(R.id.avi);
        noItemsTv = findViewById(R.id.noItemsTv);
        hideSoftKeyboard();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");




            avi.show();
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-7266672635430903/9363439923");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                finish();
            }
        });
        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();


                } else {
                    finish();
                }
            }
        });


        noInternetTv = findViewById(R.id.noInternetTv);
        if (isNetworkAvailable()){
            noInternetTv.setVisibility(View.INVISIBLE);
        allImagesRv = findViewById(R.id.allImagesRv);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        allImagesRv.setLayoutManager(gridLayoutManager);
        runAnimation(allImagesRv);


        searchTag = getIntent().getExtras().getString("search");




        mfirebaseDatabase = FirebaseDatabase.getInstance();
        mref = mfirebaseDatabase.getReference("Data");


        allImagesModelList = new ArrayList<AllImagesModel>();
        allImagesAdapter = new AllImagesAdapter(SearchimagesActivity.this, allImagesModelList);

            firebaseSearch(searchTag);
        final FloatingActionButton FAB = (FloatingActionButton) findViewById(R.id.upFAB);
        FAB.setVisibility(View.INVISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            allImagesRv.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                    if (gridLayoutManager.findFirstVisibleItemPosition() > 1) {
                        FAB.setVisibility(View.VISIBLE);
                    } else {
                        FAB.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (allImagesRv != null) {
                    allImagesRv.smoothScrollToPosition(0);
                }
            }
        });
    }else{
            avi.hide();
            noInternetTv.setVisibility(View.VISIBLE);
        }

    }

    private void firebaseSearch(final String searchImage) {

        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                allImagesModelList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                        for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {

                            if (dataSnapshot3.child("searchtag").getValue().toString().toLowerCase().contains(searchImage.toLowerCase()))
                            {
                                model = dataSnapshot3.getValue(AllImagesModel.class);
                                allImagesModelList.add(model);
                            }



                        }
                    }
                }

                allImagesAdapter.notifyDataSetChanged();
                allImagesRv.setAdapter(allImagesAdapter);

                allImagesRv.setLayoutAnimation(controller);
                allImagesRv.getAdapter().notifyDataSetChanged();
                avi.hide();
                allImagesRv.scheduleLayoutAnimation();

                noItemsTv.setVisibility(View.INVISIBLE);

                if (allImagesAdapter.getItemCount()==0){
                    noItemsTv.setVisibility(View.VISIBLE);

                }else {
                    noItemsTv.setVisibility(View.INVISIBLE);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(SearchimagesActivity.this, "Oops something went wrong", Toast.LENGTH_SHORT).show();

            }
        });



    }

    private void runAnimation(RecyclerView allImagesRv) {

        Context context = allImagesRv.getContext();
       controller = null;

        controller = AnimationUtils.loadLayoutAnimation(context,R.anim.layout_fall_down);

    }



    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        MenuItem item = menu.findItem(R.id.acionSearch);
        SearchView searchView =(SearchView) MenuItemCompat.getActionView(item);
        searchView.setQuery(searchTag,true);
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                firebaseSearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {



                return false;

            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            finish();
        }

    }
}

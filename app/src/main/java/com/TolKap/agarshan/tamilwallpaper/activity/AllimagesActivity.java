package com.TolKap.agarshan.tamilwallpaper.activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
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
import java.util.Collections;

public class AllimagesActivity extends AppCompatActivity {

    private RecyclerView allImagesRv;
    private AllImagesAdapter allImagesAdapter;
    private FirebaseDatabase mfirebaseDatabase;
    private DatabaseReference mref;
    private ArrayList<AllImagesModel> allImagesModelList;
    private AllImagesModel model;
    private Query qry;
    private LayoutAnimationController controller;
    private AdView mAdView;
    private CustomFontTextView categoryNameTv;
    private TextView noInternetTv;
    private AVLoadingIndicatorView avi;
    private InterstitialAd mInterstitialAd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allimages);

        avi = findViewById(R.id.avi);


            avi.show();
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-7266672635430903/9402682965");
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
        categoryNameTv = findViewById(R.id.categoryNameTv);
        noInternetTv = findViewById(R.id.noInternetTv);
        if (isNetworkAvailable()){
            noInternetTv.setVisibility(View.INVISIBLE);
        allImagesRv = findViewById(R.id.allImagesRv);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        allImagesRv.setLayoutManager(gridLayoutManager);
        runAnimation(allImagesRv);

        String categoryName = getIntent().getExtras().getString("Category");
        categoryNameTv.setText(categoryName);

        mfirebaseDatabase = FirebaseDatabase.getInstance();
        mref = mfirebaseDatabase.getReference("Data");
        qry = mref.orderByChild("title").equalTo(categoryName);

        allImagesModelList = new ArrayList<AllImagesModel>();
        allImagesAdapter = new AllImagesAdapter(AllimagesActivity.this, allImagesModelList);

        qry.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                allImagesModelList.clear();
                AllImagesModel object = dataSnapshot.child("allimages").getValue(AllImagesModel.class);
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                        for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {


                            model = dataSnapshot3.getValue(AllImagesModel.class);



                            allImagesModelList.add(model);

                        }
                    }
                }
                Collections.reverse(allImagesModelList);
                allImagesAdapter.notifyDataSetChanged();
                allImagesRv.setAdapter(allImagesAdapter);

                allImagesRv.setLayoutAnimation(controller);
                allImagesRv.getAdapter().notifyDataSetChanged();
                avi.hide();
                allImagesRv.scheduleLayoutAnimation();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(AllimagesActivity.this, "Oops something went wrong", Toast.LENGTH_SHORT).show();

            }
        });

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

    private void runAnimation(RecyclerView allImagesRv) {

        Context context = allImagesRv.getContext();
       controller = null;

        controller = AnimationUtils.loadLayoutAnimation(context,R.anim.layout_fall_down);

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



    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

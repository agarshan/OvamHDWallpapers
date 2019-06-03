package com.TolKap.agarshan.tamilwallpaper.activity;



import android.Manifest;

import android.app.AlertDialog;

import android.app.WallpaperManager;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dmallcott.dismissibleimageview.DismissibleImageView;
import com.TolKap.agarshan.tamilwallpaper.R;
import com.TolKap.agarshan.tamilwallpaper.database.Database;
import com.TolKap.agarshan.tamilwallpaper.model.FavouriteImage;
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
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


import java.util.UUID;

import dmax.dialog.SpotsDialog;



public class SetAsWallpaperActivity extends AppCompatActivity {

    private RelativeLayout backgroundColorRl;
    private ImageButton setAsWallpaperBtn,saveWallpaperBtn,shareImageBtn,favoriteBtn;
    private DismissibleImageView wallpaperImageIv;


    private static final int WRITE_EXTERNAL_STORAGE_CODE = 1;
    private FirebaseDatabase mfirebaseDatabase;
    private DatabaseReference mref,mrefCount;
    private Query qry;
    private int countNum;

    private int color;
    private int color1;
    private int color2;
    private int color3;
    private int color4;
    private int color5;
    private Database localdb;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    private RelativeLayout homeScreenRl,lockScreenRL,bothScreenRl;
    private  String ImageSource;
    private Target target;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_as_wallpaper);


        ImageSource = getIntent().getExtras().getString("ImageSource");

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-7266672635430903/6024441662");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener(){

            @Override
            public void onAdClosed() {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                finish();
            }
        });

        backgroundColorRl = findViewById(R.id.backgroundColorRl);
        setAsWallpaperBtn = findViewById(R.id.setAsWallpaperBtn);
        wallpaperImageIv = findViewById(R.id.wallpaperImageIv);
        saveWallpaperBtn = findViewById(R.id.saveWallpaperBtn);
        shareImageBtn = findViewById(R.id.shareImageBtn);
        favoriteBtn = findViewById(R.id.favoriteBtn);

        localdb= new Database(this);




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


        mfirebaseDatabase = FirebaseDatabase.getInstance();
        mref = mfirebaseDatabase.getReference("Data");
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    for(DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()){
                        for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()){

                            if (dataSnapshot3.child("image1").getValue().equals(ImageSource)){

                                mrefCount =dataSnapshot3.child("count").getRef();
                                countNum= Integer.valueOf(dataSnapshot3.child("count").getValue().toString());

                            }

                        }}}

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(SetAsWallpaperActivity.this, "Oops something went wrong", Toast.LENGTH_SHORT).show();

            }
        });



        setAsWallpaperBtn.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Patterns.WEB_URL.matcher(ImageSource).matches()) {
                Picasso.get()
                        .load(ImageSource)
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {


                                setAsWallpaper(bitmap);



                            }

                            @Override
                            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });

                }else {
                    Toast.makeText(SetAsWallpaperActivity.this, "Image is not supported", Toast.LENGTH_SHORT).show();

            }

            }
            });


   if (ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
           requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},WRITE_EXTERNAL_STORAGE_CODE);
       }
   }

    saveWallpaperBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (ActivityCompat.checkSelfPermission(SetAsWallpaperActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(SetAsWallpaperActivity.this, "You should grant permission", Toast.LENGTH_SHORT).show();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},WRITE_EXTERNAL_STORAGE_CODE);
                    return;
                }else {

                    AlertDialog dialog = new SpotsDialog.Builder().setContext(SetAsWallpaperActivity.this).build();
                    dialog.show();
                    dialog.setMessage("Downloading");
                    String filename = UUID.randomUUID().toString()+".jpg";
                    if(Patterns.WEB_URL.matcher(ImageSource).matches()) {

                        Picasso.get()
                                .load(ImageSource)
                                .into(new SaveImageHelper(getBaseContext(), dialog, getApplicationContext().getContentResolver(), filename, "agarshan"));
                    }else{
                        Toast.makeText(SetAsWallpaperActivity.this, "Image is not available for download", Toast.LENGTH_SHORT).show();

                    }
                }


            }else
                {

                AlertDialog dialog = new SpotsDialog.Builder().setContext(SetAsWallpaperActivity.this).build();
                dialog.show();
                dialog.setMessage("Downloading");
                String filename = UUID.randomUUID().toString()+".jpg";
                Picasso.get()
                        .load(ImageSource)
                        .into(new SaveImageHelper(getBaseContext(),dialog,getApplicationContext().getContentResolver(),filename,"agarshan"));

            }

            if (mrefCount != null) {
                mrefCount.setValue(countNum + 1);
            }
        }
    });

        mfirebaseDatabase = FirebaseDatabase.getInstance();
        mref = mfirebaseDatabase.getReference("Data");


        if (localdb.isFavorite(new FavouriteImage(ImageSource))){
            favoriteBtn.setImageResource(R.drawable.ic_favorite_black_full_24dp);

        }

        favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!localdb.isFavorite(new FavouriteImage(ImageSource))){
                    localdb.addImageUrl(new FavouriteImage(ImageSource));
                    favoriteBtn.setImageResource(R.drawable.ic_favorite_black_full_24dp);
                    Toast.makeText(SetAsWallpaperActivity.this, "Wallpaper added to Favorites", Toast.LENGTH_SHORT).show();

                }else {
                    localdb.deleteImageUrl(new FavouriteImage(ImageSource));
                    favoriteBtn.setImageResource(R.drawable.ic_favorite_white_24dp);
                    Toast.makeText(SetAsWallpaperActivity.this, "Wallpaper removed from Favorites", Toast.LENGTH_SHORT).show();



                }
            }
        });

                            shareImageBtn.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View view) {
                                    if(Patterns.WEB_URL.matcher(ImageSource).matches()) {
                                        Picasso.get()
                                                .load(ImageSource)
                                                .into(new Target() {
                                                    @Override
                                                    public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                                                        shareImage(bitmap);
                                                    }

                                                    @Override
                                                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                                                    }

                                                    @Override
                                                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                                                    }
                                                });

                                    }else {
                                        Toast.makeText(SetAsWallpaperActivity.this, "Image is not supported", Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });



                             target = new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                assert wallpaperImageIv != null;
                wallpaperImageIv.setImageBitmap(bitmap);

                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        Palette.Swatch textSwatch = palette.getVibrantSwatch();
                        Palette.Swatch textSwatch1 = palette.getDarkVibrantSwatch();
                        Palette.Swatch textSwatch2 = palette.getDominantSwatch();
                        Palette.Swatch textSwatch3 = palette.getMutedSwatch();
                        Palette.Swatch textSwatch4 = palette.getLightMutedSwatch();
                        Palette.Swatch textSwatch5 = palette.getLightVibrantSwatch();

                        if (textSwatch != null) {
                            color = textSwatch.getRgb();
                        } else {
                            color = Color.argb(175, 0, 0, 0);
                        }
                        if (textSwatch1 != null) {
                            color1 = textSwatch1.getRgb();
                        } else {
                            color1 = Color.argb(175, 0, 0, 0);
                        }

                        if (textSwatch2 != null) {
                            color2 = textSwatch2.getRgb();
                        } else {
                            color2 = Color.argb(175, 0, 0, 0);
                        }

                        if (textSwatch3 != null) {
                            color3 = textSwatch3.getRgb();
                        } else {
                            color3 = Color.argb(175, 0, 0, 0);
                        }

                        if (textSwatch4 != null) {
                            color4 = textSwatch4.getRgb();
                        } else {
                            color4 = Color.argb(175, 0, 0, 0);
                        }

                        if (textSwatch5 != null) {
                            color5 = textSwatch5.getRgb();
                        } else {
                            color5 = Color.argb(175, 0, 0, 0);
                        }

                        int[] colors = {color, color1, color2, color3, color4, color5};
                        final GradientDrawable gd = new GradientDrawable(
                                GradientDrawable.Orientation.BL_TR, colors);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {


                            backgroundColorRl.setBackground(gd);


                        } else {
                            backgroundColorRl.setBackgroundColor(Color.argb(175, 0, 0, 0));

                        }


                    }
                });
                setAsWallpaperBtn.setOnClickListener(new ImageButton.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        setAsWallpaper(bitmap);
                    }
                });
                shareImageBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        shareImage(bitmap);


                    }
                });


            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        if(Patterns.WEB_URL.matcher(ImageSource).matches()) {

            Picasso.get()
                    .load(ImageSource)
                    .into(target );



        }else {
            Toast.makeText(SetAsWallpaperActivity.this, "Image is not supported", Toast.LENGTH_SHORT).show();

        }
    }



    private void shareImage(Bitmap bitmap) {
        File file = new File(getExternalCacheDir(),"sample.png");
        try {
            FileOutputStream fout = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,fout);
            fout.flush();
            fout.close();
            file.setReadable(true,false);
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri imageUri = FileProvider.getUriForFile(SetAsWallpaperActivity.this," com.TolKap.agarshan.tamilwallpaper.provider",file);
            i.putExtra(Intent.EXTRA_STREAM, imageUri);
            i.setType("image/*");
            startActivity(Intent.createChooser(i, "Share Image"));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case WRITE_EXTERNAL_STORAGE_CODE:
            {
                if (grantResults.length >0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
                break;
        }
    }

    private void setAsWallpaper(final Bitmap bitmap) {


        final WallpaperManager myWallpaperManager
                = WallpaperManager.getInstance(getApplicationContext());
        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(this);
                final View sheetView = getLayoutInflater().inflate(R.layout.choose_screen_dialog, null);
                mBottomSheetDialog.setContentView(sheetView);

                homeScreenRl = sheetView.findViewById(R.id.homeScreenRl);
                lockScreenRL = sheetView.findViewById(R.id.lockScreenRL);
                bothScreenRl = sheetView.findViewById(R.id.bothScreenRl);

                homeScreenRl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            try {
                                mBottomSheetDialog.dismiss();
                                myWallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM);
                                Toast.makeText(sheetView.getContext(), "Wallpaper set successfully", Toast.LENGTH_SHORT).show();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });

                lockScreenRL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                mBottomSheetDialog.dismiss();
                                myWallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK);
                                Toast.makeText(sheetView.getContext(), "Wallpaper set successfully", Toast.LENGTH_SHORT).show();

                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                bothScreenRl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                mBottomSheetDialog.dismiss();
                                myWallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM);
                                myWallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK);
                            }
                            Toast.makeText(sheetView.getContext(), "Wallpaper set successfully", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
                mBottomSheetDialog.show();

            }
            else{

                myWallpaperManager.setBitmap(bitmap);
                Toast.makeText(this, "Wallpaper set successfully", Toast.LENGTH_SHORT).show();

            }





        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

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

package com.TolKap.agarshan.tamilwallpaper.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.TolKap.agarshan.tamilwallpaper.R;
import com.TolKap.agarshan.tamilwallpaper.activity.SetAsWallpaperActivity;
import com.TolKap.agarshan.tamilwallpaper.custom.CustomFontTextView;
import com.TolKap.agarshan.tamilwallpaper.event.RecyclerViewAdapter;
import com.TolKap.agarshan.tamilwallpaper.model.AllImagesModel;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;


import java.util.List;


public class AllImagesAdapter extends RecyclerView.Adapter<AllImagesAdapter.ViewHolder>implements RecyclerViewAdapter {

    Context context;
    private List<AllImagesModel> dataSet;
    CardView mCardView;
    private InterstitialAd mInterstitialAd;


    public AllImagesAdapter(Context context, List dataSet) {
        this.context=context;
        this.dataSet = dataSet;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_all_images, parent, false);
        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId("ca-app-pub-7266672635430903/4714703730");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        ViewHolder vh = new ViewHolder(itemView);
        return  vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {


        final AllImagesModel uModel = dataSet.get(position);

        if(!Patterns.WEB_URL.matcher(uModel.getImage1()).matches()){
//            URLEncoder.encode(uModel.getImage1().toString(), "utf-8");
            //Now load via Picasso
        }
        else{
            Picasso.get().load(uModel.getImage1())
                    .resize(200,300)
                    .into(holder.allImagesIv);

            //Proceed with loading via picasso
        }


        holder.allImagesIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if(Patterns.WEB_URL.matcher(uModel.getImage1()).matches()) {
//            URLEncoder.encode(uModel.getImage1().toString(), "utf-8");
                    //Now load via Picasso

                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();

                    }else{
                        Intent intent = new Intent(context, SetAsWallpaperActivity.class);
                        intent.putExtra("ImageSource", uModel.getImage1());
                        intent.putExtra("ImageSourceHigh",uModel.getImageHigh());
                        context.startActivity(intent);
                    }

                    mInterstitialAd.setAdListener(new AdListener(){

                        @Override
                        public void onAdClosed() {
                            mInterstitialAd.loadAd(new AdRequest.Builder().build());
                            Intent intent = new Intent(context, SetAsWallpaperActivity.class);
                            intent.putExtra("ImageSource", uModel.getImage1());
                            context.startActivity(intent);
                        }
                    });



                }else
                {
                    Toast.makeText(context, "Image is not available", Toast.LENGTH_SHORT).show();
                }



            }



        });

    }

    @Override
    public int getItemCount() {

            return dataSet != null ? dataSet.size() : 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        CustomFontTextView allCategoryNameTv;
            ImageView allImagesIv;

        public ViewHolder(View itemView) {
            super(itemView);
            mCardView = itemView.findViewById(R.id.cardview);
            allCategoryNameTv =itemView.findViewById(R.id.allCategoryNameTv);
            allImagesIv =itemView.findViewById(R.id.allImagesIv);


        }
    }
    @Override
    public void setDataToAdapter(Object dataSet) {
        this.dataSet = (List<AllImagesModel>) dataSet;
        notifyDataSetChanged();
    }
}

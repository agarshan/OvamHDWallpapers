package com.TolKap.agarshan.tamilwallpaper.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.TolKap.agarshan.tamilwallpaper.R;
import com.TolKap.agarshan.tamilwallpaper.activity.SetAsWallpaperActivity;
import com.TolKap.agarshan.tamilwallpaper.custom.CustomFontTextView;
import com.TolKap.agarshan.tamilwallpaper.event.RecyclerViewAdapter;
import com.TolKap.agarshan.tamilwallpaper.model.AllImagesModel;
import com.TolKap.agarshan.tamilwallpaper.model.FavouriteImage;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;


public class FavouriteImagesAdapter extends RecyclerView.Adapter<FavouriteImagesAdapter.ViewHolder>implements RecyclerViewAdapter {

    Context context;
    private List<FavouriteImage> dataSet;
    CardView mCardView;

    public FavouriteImagesAdapter(Context context, List dataSet) {
        this.context=context;
        this.dataSet = dataSet;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_all_images, parent, false);

        ViewHolder vh = new ViewHolder(itemView);
        return  vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {


        final FavouriteImage uModel = dataSet.get(position);
        Picasso.get().load(uModel.getUrlName())
                .into(holder.allImagesIv);


        holder.allImagesIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, SetAsWallpaperActivity.class);
                intent.putExtra("ImageSource",uModel.getUrlName());
                context.startActivity(intent);
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
        this.dataSet = (List<FavouriteImage>) dataSet;
        notifyDataSetChanged();
    }
}

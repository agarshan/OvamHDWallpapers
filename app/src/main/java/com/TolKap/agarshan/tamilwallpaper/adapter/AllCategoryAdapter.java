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
import com.TolKap.agarshan.tamilwallpaper.activity.AllimagesActivity;
import com.TolKap.agarshan.tamilwallpaper.custom.CustomFontTextView;
import com.TolKap.agarshan.tamilwallpaper.event.RecyclerViewAdapter;
import com.TolKap.agarshan.tamilwallpaper.model.AllCategoryModel;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;




public class AllCategoryAdapter extends RecyclerView.Adapter<AllCategoryAdapter.ViewHolder>implements RecyclerViewAdapter {

    Context context;
    private ArrayList<AllCategoryModel> dataSet;
    CardView mCardView;

    public AllCategoryAdapter(Context context, ArrayList<AllCategoryModel> p) {
        this.context=context;
        this.dataSet = p;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_all_category, parent, false);

        ViewHolder vh = new ViewHolder(itemView);
        return  vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        final AllCategoryModel uModel = dataSet.get(position);
        holder.allCategoryNameTv.setText(String.valueOf(uModel.getTitle()));
        Picasso.get()
                .load(uModel.getImage())
                .resize(200,300)
                .placeholder(R.drawable.ovamcircleimage)
                .into(holder.allCategoryImageIv);


        holder.allCategoryImageIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, AllimagesActivity.class);
                intent.putExtra("Category",uModel.getTitle());
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
        ImageView allCategoryImageIv;

        public ViewHolder(View itemView) {
            super(itemView);
            mCardView = itemView.findViewById(R.id.cardview);
            allCategoryNameTv =itemView.findViewById(R.id.allCategoryNameTv);
            allCategoryImageIv =itemView.findViewById(R.id.allCategoryImageIv);

        }
    }
    @Override
    public void setDataToAdapter(Object dataSet) {
        this.dataSet = (ArrayList<AllCategoryModel>) dataSet;
        notifyDataSetChanged();
    }
}



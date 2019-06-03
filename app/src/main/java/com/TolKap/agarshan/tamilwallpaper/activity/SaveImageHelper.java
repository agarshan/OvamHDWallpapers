package com.TolKap.agarshan.tamilwallpaper.activity;


import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.lang.ref.WeakReference;

public class SaveImageHelper implements Target {

    private WeakReference<AlertDialog> alertDialogWeakReference;
    private WeakReference<ContentResolver> contentResolverWeakReference;
    private String name;
    private String desc;
    private Context context;


    public SaveImageHelper(Context context,AlertDialog alertDialog, ContentResolver contentResolver, String name, String desc) {
        this.alertDialogWeakReference = new WeakReference<AlertDialog>(alertDialog);
        this.contentResolverWeakReference = new WeakReference<ContentResolver>(contentResolver);
        this.name = name;
        this.desc = desc;
        this.context=context;
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            ContentResolver r = contentResolverWeakReference.get();
            AlertDialog dialog = alertDialogWeakReference.get();

            if(r != null && bitmap != null){
                MediaStore.Images.Media.insertImage(r,bitmap,name,desc);
                Toast.makeText(context, "Image Downloaded", Toast.LENGTH_SHORT).show();

            }
        dialog.dismiss();

    }

    @Override
    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }
}

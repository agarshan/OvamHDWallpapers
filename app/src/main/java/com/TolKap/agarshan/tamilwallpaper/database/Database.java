package com.TolKap.agarshan.tamilwallpaper.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.TolKap.agarshan.tamilwallpaper.model.FavouriteImage;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteAssetHelper{

    private static final String DB_NAME= "tamilWallpaper.db";
    private static final int DB_VER=1;

    public Database(Context context){
        super(context,DB_NAME,null,DB_VER);



    }

    public List<FavouriteImage> getfavoriteImage(){
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlselect = {"urlName"};
        String sqlTable="Favorite";

        qb.setTables(sqlTable);
        Cursor c = qb.query(db,sqlselect,null,null,null,null,null);

        final List<FavouriteImage> result = new ArrayList<>();
        if (c.moveToFirst())
        {
            do {
                result.add(new FavouriteImage(c.getString(c.getColumnIndex("urlName")))

                );
            }while (c.moveToNext());
        }
            return result;
    }


    public void addImageUrl(FavouriteImage favouriteImage){

        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO Favorite(urlName) VALUES('%s');",
                favouriteImage.getUrlName());

            db.execSQL(query);
    }


    public void deleteImageUrl(FavouriteImage favouriteImage){

        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM Favorite WHERE urlName='%s';",favouriteImage.getUrlName());
        db.execSQL(query);
    }

    public boolean isFavorite(FavouriteImage favouriteImage){

        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("SELECT * FROM Favorite WHERE urlName='%s';",favouriteImage.getUrlName());
        Cursor cursor = db.rawQuery(query,null);
        if (cursor.getCount()<=0){

            cursor.close();
            return false;

        }
            cursor.close();
        return true;
    }

}

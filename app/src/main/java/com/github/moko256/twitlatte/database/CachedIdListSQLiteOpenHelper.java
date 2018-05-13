/*
 * Copyright 2015-2018 The twitlatte authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.moko256.twitlatte.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.github.moko256.twitlatte.BuildConfig;
import com.github.moko256.twitlatte.entity.AccessToken;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by moko256 on 2017/06/08.
 *
 * @author moko256
 */

public class CachedIdListSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String ID_LIST_TABLE_NAME = "IdList";

    private static final String POSITION_TABLE_NAME = "ListViewPosition";

    public CachedIdListSQLiteOpenHelper(Context context, AccessToken accessToken, String name){
        super(context, accessToken != null? new File(context.getCacheDir(), accessToken.getKeyString() + "/" + name + ".db").getAbsolutePath(): null, null, BuildConfig.CACHE_DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + ID_LIST_TABLE_NAME + "(id)");
        db.execSQL("create table " + POSITION_TABLE_NAME + "(position)");
        //db.execSQL("create table ListViewPositionOffset(offset);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*
        switch (oldVersion){
            case 1:
                db.execSQL("create table ListViewPositionOffset(offset);");
                break;
        }
        */
    }

    public List<Long> getIds(){
        List<Long> ids;

        synchronized (this) {
            SQLiteDatabase database = getReadableDatabase();
            Cursor c = database.query(ID_LIST_TABLE_NAME, new String[]{"id"}, null, null, null, null, null);
            ids = new ArrayList<>(c.getCount());

            while (c.moveToNext()) {
                ids.add(c.getLong(0));
            }

            c.close();
            database.close();
        }

        Collections.reverse(ids);
        return ids;
    }

    public void addIds(List<Long> ids){
        synchronized (this) {
            SQLiteDatabase database = getWritableDatabase();
            database.beginTransaction();
            addIdsAtTransaction(ids);
            database.setTransactionSuccessful();
            database.endTransaction();
            database.close();
        }
    }

    private void addIdsAtTransaction(List<Long> ids){
        SQLiteDatabase database=getWritableDatabase();

        for (int i = ids.size() - 1; i >= 0; i--) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("id", ids.get(i));

            database.insert(ID_LIST_TABLE_NAME, "", contentValues);
        }
    }

    private void addIdsOnlyAtTransaction(List<Long> ids){
        SQLiteDatabase database=getWritableDatabase();

        for (int i = ids.size() - 1; i >= 0; i--) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("id", ids.get(i));

            database.insert(ID_LIST_TABLE_NAME, "", contentValues);
        }
    }

    public void insertIds(int bottomPosition, List<Long> ids){
        List<Long> n = getIds();
        List<Long> d = n.subList(0, bottomPosition);

        synchronized (this) {
            SQLiteDatabase database = getWritableDatabase();
            database.beginTransaction();
            deleteOnlyIdsAtTransaction(d);
            addIdsAtTransaction(ids);
            addIdsOnlyAtTransaction(d);
            database.setTransactionSuccessful();
            database.endTransaction();
            database.close();
        }
    }

    public void deleteIds(List<Long> ids){
        synchronized (this) {
            SQLiteDatabase database = getWritableDatabase();
            database.beginTransaction();
            deleteIdsAtTransaction(ids);
            database.setTransactionSuccessful();
            database.endTransaction();
            database.close();
        }
    }

    private void deleteIdsAtTransaction(List<Long> ids){
        SQLiteDatabase database = getWritableDatabase();
        for (Long id : ids) {
            database.delete(ID_LIST_TABLE_NAME, "id=" + String.valueOf(id), null);
        }
    }

    private void deleteOnlyIdsAtTransaction(List<Long> ids){
        SQLiteDatabase database = getWritableDatabase();
        for (Long id : ids) {
            database.delete(ID_LIST_TABLE_NAME, "id=" + String.valueOf(id), null);
        }
    }

    public int getListViewPosition(){
        int r;

        synchronized (this) {
            SQLiteDatabase database = getReadableDatabase();
            Cursor c = database.query(POSITION_TABLE_NAME, new String[]{"position"}, null, null, null, null, null);
            if (c.moveToNext()) {
                r = c.getInt(0);
            } else {
                r = 0;
            }
            c.close();
            database.close();
        }

        return r;
    }

    public void setListViewPosition(int i){
        synchronized (this) {
            SQLiteDatabase database = getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put("position", i);

            database.beginTransaction();
            database.delete(POSITION_TABLE_NAME, null, null);
            database.insert(POSITION_TABLE_NAME, null, contentValues);
            database.setTransactionSuccessful();
            database.endTransaction();
            database.close();
        }
    }

    /*

    public synchronized int getListViewPositionOffset(){
        SQLiteDatabase database=getReadableDatabase();
        Cursor c = database.query("ListViewPositionOffset", new String[]{"offset"}, null, null, null, null, null);
        int r;
        if (c.moveToNext()){
            r = c.getInt(0);
        } else {
            r = 0;
        }
        c.close();
        database.close();
        return r;
    }

    public synchronized void setListViewPositionOffset(int i){
        SQLiteDatabase database=getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("offset", i);

        database.beginTransaction();
        database.delete("ListViewPositionOffset", null, null);
        database.insert("ListViewPositionOffset", null, contentValues);
        database.setTransactionSuccessful();
        database.endTransaction();
        database.close();
    }

    */
}

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

package com.github.moko256.twitlatte;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.moko256.twitlatte.text.TwitterStringUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import twitter4j.MediaEntity;

/**
 * Created by moko256 on 2016/06/26.
 *
 * @author moko256
 */
public class ShowImageActivity extends AppCompatActivity {
    private static final String FRAG_MEDIA_ENTITIES="MediaEntities";
    private static final String FRAG_POSITION="position";

    List<MediaEntity> mediaEntities;

    ViewPager pager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);

        mediaEntities = (List<MediaEntity>) getIntent().getSerializableExtra(FRAG_MEDIA_ENTITIES);

        int position=getIntent().getIntExtra(FRAG_POSITION,0);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_clear_white_24dp);

        pager= findViewById(R.id.activity_show_image_view_pager);
        pager.setAdapter(new ImagePagerAdapter(getSupportFragmentManager(),mediaEntities));
        pager.setCurrentItem(position);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pager=null;
        mediaEntities=null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
            contentDownload();
        } else {
            Toast.makeText(this, R.string.permission_denied,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_show_image_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_download){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
            } else {
                contentDownload();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void contentDownload(){
        String path="";
        MediaEntity mediaEntity= mediaEntities.get(pager.getCurrentItem());
        switch (mediaEntity.getType()){
            case "video":
                for(MediaEntity.Variant variant : mediaEntity.getVideoVariants()){
                    if(variant.getContentType().equals("video/mp4")){
                        path = variant.getUrl();
                    }
                }
                break;

            case "animated_gif":
                path = mediaEntity.getVideoVariants()[0].getUrl();
                break;

            case "photo":
            default:
                path = TwitterStringUtils.convertOriginalImageUrl(mediaEntity.getMediaURLHttps());
                break;
        }
        DownloadManager manager=(DownloadManager)getSystemService(DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(path);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        String lastPathSegment = uri.getLastPathSegment();
        request.setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                "/" + getString(R.string.app_name) + "/" + lastPathSegment
        );
        request.setTitle(lastPathSegment);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        manager.enqueue(request);
    }

    public static Intent getIntent(Context context, MediaEntity[] entities , int position){
        return new Intent(context,ShowImageActivity.class)
                .putExtra(FRAG_MEDIA_ENTITIES, (Serializable) Arrays.asList(entities))
                .putExtra(FRAG_POSITION,position);
    }
}

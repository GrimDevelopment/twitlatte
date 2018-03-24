/*
 * Copyright 2018 The twicalico authors
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

package com.github.moko256.twicalico;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;
import com.github.chuross.flinglayout.FlingLayout;
import com.github.moko256.twicalico.text.TwitterStringUtils;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ext.okhttp.OkHttpDataSourceFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;

import java.io.IOException;

import kotlin.Unit;
import twitter4j.MediaEntity;

/**
 * Created by moko256 on 2016/10/29.
 *
 * @author moko256
 */

public class ImagePagerChildFragment extends Fragment {

    private static String FRAG_MEDIA_ENTITY="media_entity";

    PhotoView imageView;

    PlayerView videoPlayView;
    SimpleExoPlayer player;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FlingLayout view= (FlingLayout) inflater.inflate(R.layout.fragment_image_pager_child, null);
        view.setDismissListener(() -> {
            getActivity().finish();
            return Unit.INSTANCE;
        });
        view.setPositionChangeListener((Integer top, Integer left, Float dragRangeRate) -> {
            view.setBackgroundColor(Color.argb(Math.round(255 * (1.0F - dragRangeRate)), 0, 0, 0));
            return Unit.INSTANCE;
        });

        MediaEntity mediaEntity;

        if (getArguments() == null) return view;

        mediaEntity = (MediaEntity) getArguments().getSerializable(FRAG_MEDIA_ENTITY);

        if (mediaEntity == null) return view;

        switch (mediaEntity.getType()){
            case "video":
                String videoPath = null;
                boolean isHls = false;
                for(MediaEntity.Variant variant : mediaEntity.getVideoVariants()){
                    if(variant.getContentType().equals("application/x-mpegURL")){
                        videoPath=variant.getUrl();
                        isHls = true;
                    }
                }

                if (videoPath == null){
                    videoPath = mediaEntity.getVideoVariants()[0].getUrl();
                }

                videoPlayView = view.findViewById(R.id.fragment_image_pager_video);
                videoPlayView.setVisibility(View.VISIBLE);
                videoPlayView.setControllerVisibilityListener(visibility -> {
                    if (visibility != View.VISIBLE){
                        hideSystemUI();
                    } else {
                        showSystemUI();
                    }
                });

                getActivity().getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(visibility -> {
                    if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0){
                        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
                        videoPlayView.showController();
                    }
                });

                player = ExoPlayerFactory.newSimpleInstance(
                        new DefaultRenderersFactory(getContext()),
                        new DefaultTrackSelector(
                                new AdaptiveTrackSelection.Factory(new DefaultBandwidthMeter())
                        ),
                        new DefaultLoadControl()
                );
                if (savedInstanceState != null){
                    player.seekTo(savedInstanceState.getLong("video_time", 0));
                }
                videoPlayView.setPlayer(player);
                player.prepare(
                        (isHls)?
                                new HlsMediaSource.Factory(
                                        new OkHttpDataSourceFactory(
                                                GlobalApplication.getOkHttpClient(),
                                                getResources().getText(R.string.app_name).toString(),
                                                null
                                        )
                                ).createMediaSource(Uri.parse(videoPath)):

                                new ExtractorMediaSource.Factory(
                                        new OkHttpDataSourceFactory(
                                                GlobalApplication.getOkHttpClient(),
                                                getResources().getText(R.string.app_name).toString(),
                                                null
                                        )
                                ).createMediaSource(
                                        Uri.parse(mediaEntity.getVideoVariants()[0].getUrl()),
                                        new Handler(),
                                        new ShowErrorListener()
                                )
                );
                break;

            case "animated_gif":
                videoPlayView = view.findViewById(R.id.fragment_image_pager_video);
                videoPlayView.setVisibility(View.VISIBLE);
                videoPlayView.setControllerVisibilityListener(visibility -> {
                    if (visibility != View.VISIBLE){
                        hideSystemUI();
                    } else {
                        showSystemUI();
                    }
                });

                getActivity().getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(visibility -> {
                    if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0){
                        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
                        videoPlayView.showController();
                    }
                });

                player = ExoPlayerFactory.newSimpleInstance(
                        new DefaultRenderersFactory(getContext()),
                        new DefaultTrackSelector(
                                new AdaptiveTrackSelection.Factory(new DefaultBandwidthMeter())
                        ),
                        new DefaultLoadControl()
                );
                if (savedInstanceState != null){
                    player.seekTo(savedInstanceState.getLong("video_time", 0));
                }
                videoPlayView.setPlayer(player);
                player.prepare(
                        new LoopingMediaSource(
                                new ExtractorMediaSource.Factory(
                                        new OkHttpDataSourceFactory(
                                                GlobalApplication.getOkHttpClient(),
                                                getResources().getText(R.string.app_name).toString(),
                                                null
                                        )
                                ).createMediaSource(
                                        Uri.parse(mediaEntity.getVideoVariants()[0].getUrl()),
                                        new Handler(),
                                        new ShowErrorListener()
                                )
                        )
                );
                player.setPlayWhenReady(true);

                break;

            case "photo":
            default:
                getActivity().getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(visibility -> {
                    if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0){
                        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
                    }
                });
                imageView= view.findViewById(R.id.fragment_image_pager_image);
                imageView.setVisibility(View.VISIBLE);
                imageView.setOnClickListener(v -> {
                    if ((getActivity().getWindow().getDecorView().getSystemUiVisibility() & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0){
                        hideSystemUI();
                    } else {
                        showSystemUI();
                    }
                });
                imageView.setOnScaleChangeListener((float scaleFactor, float focusX, float focusY) -> view.setDragEnabled(scaleFactor <= 1F));
                GlideApp.with(this)
                        .load(TwitterStringUtils.convertLargeImageUrl(mediaEntity.getMediaURLHttps()))
                        .fitCenter()
                        .thumbnail(
                                GlideApp.with(this)
                                        .load(TwitterStringUtils.convertSmallImageUrl(
                                                mediaEntity.getMediaURLHttps()
                                        ))
                                        .fitCenter()
                        )
                        .into(imageView);
                break;
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (player != null){
            outState.putLong("video_time", player.getCurrentPosition());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (player != null){
            player.release();
            player = null;
        }
        videoPlayView = null;
        imageView = null;
    }

    private void hideSystemUI() {
        getActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    private void showSystemUI() {
        getActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    private final class ShowErrorListener implements MediaSourceEventListener {
        @Override
        public void onLoadStarted(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs) {}

        @Override
        public void onLoadCompleted(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs, long bytesLoaded) {}

        @Override
        public void onLoadCanceled(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs, long bytesLoaded) {}

        @Override
        public void onLoadError(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs, long bytesLoaded, IOException error, boolean wasCanceled) {
            error.printStackTrace();

            Toast.makeText(
                    getContext(),
                    new StringBuilder(getString(R.string.error_occurred))
                            .append("\n\n")
                            .append(error.getMessage()),
                    Toast.LENGTH_SHORT
            ).show();
        }

        @Override
        public void onUpstreamDiscarded(int trackType, long mediaStartTimeMs, long mediaEndTimeMs) {}

        @Override
        public void onDownstreamFormatChanged(int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaTimeMs) {}
    }

    public static ImagePagerChildFragment getInstance(MediaEntity entity){
        ImagePagerChildFragment fragment=new ImagePagerChildFragment();
        Bundle bundle=new Bundle();
        bundle.putSerializable(FRAG_MEDIA_ENTITY,entity);
        fragment.setArguments(bundle);
        return fragment;
    }
}

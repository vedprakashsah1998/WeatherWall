package com.client.vpman.weatherwall.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import com.client.vpman.weatherwall.CustomeUsefullClass.VideoModel;
import com.client.vpman.weatherwall.R;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoHolder>
{
    private Context context;
    private List<VideoModel> list;

    public VideoAdapter(Context context, List<VideoModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public VideoAdapter.VideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.video_list,parent,false);
        return new VideoHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull VideoAdapter.VideoHolder holder, int position) {
     /*   Uri video=Uri.parse(list.get(position).getVideoUrl());
        holder.videoView.setVideoURI(video);
        holder.videoView.setOnPreparedListener(mp -> {
            mp.setLooping(true);
            holder.videoView.start();
        });*/

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
        holder.simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
        Uri video=Uri.parse(list.get(position).getVideoUrl());
        DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        MediaSource mediaSource = new ExtractorMediaSource(video, dataSourceFactory, extractorsFactory, null, null);

        holder.videoView.setPlayer(holder.simpleExoPlayer);
       holder. simpleExoPlayer.prepare(mediaSource);
        holder.simpleExoPlayer.setPlayWhenReady(true);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class VideoHolder extends RecyclerView.ViewHolder {
        SimpleExoPlayerView videoView;
        private SimpleExoPlayer simpleExoPlayer;

        public VideoHolder(@NonNull View itemView) {
            super(itemView);
            videoView=itemView.findViewById(R.id.videoListData);
        }
    }
}

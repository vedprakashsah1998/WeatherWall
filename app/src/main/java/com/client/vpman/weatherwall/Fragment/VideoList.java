package com.client.vpman.weatherwall.Fragment;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.client.vpman.weatherwall.Adapter.VideoAdapter;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoList extends Fragment {

    public VideoList() {
        // Required empty public constructor
    }
    private String VideoUrl="https://api.pexels.com/videos/search?query=nature+query&per_page=15&page=1";

    private List<VideoModel>list;

    VideoAdapter adapter;
    View view;
    SimpleExoPlayerView videoView;
    private SimpleExoPlayer simpleExoPlayer;

    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_video_list, container, false);
        videoView=view.findViewById(R.id.toolbarVideo);
        recyclerView=view.findViewById(R.id.videorecylerView);
        list=new ArrayList<>();
        loadVideo();
    return view;
    }
    public static VideoList newInstance(String text) {
        VideoList f = new VideoList();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }

    public void loadVideo()
    {
        StringRequest stringRequest=new StringRequest(Request.Method.GET, VideoUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("VideoResponse",response);

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("videos");
                    for (int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        JSONArray videoFiles=jsonObject1.getJSONArray("video_files");
                        for (int j=0;j<videoFiles.length();j++)
                        {
                            JSONObject link=videoFiles.getJSONObject(j);
                            String VideoLink=link.getString("link");
                            Log.d("VideoUrl",VideoLink);
                            VideoModel videoModel=new VideoModel(VideoLink);
                            list.add(videoModel);
                        }

                    }
                    Collections.shuffle(list);
                    Random random = new Random();
                    int n = random.nextInt(list.size());

                   /* videoView.setVideoURI(video);
                    videoView.setOnPreparedListener(mp -> {
                        mp.setLooping(true);
                        videoView.start();
                    });*/
                    BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                    TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
                    simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);
                    Uri video=Uri.parse(list.get(n).getVideoUrl());
                    DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
                    ExtractorMediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                            .createMediaSource(video, null, null);
                    ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                  /*  MediaSource mediaSource = new ExtractorMediaSource(video, dataSourceFactory, extractorsFactory, null, null);*/

                    videoView.setPlayer(simpleExoPlayer);
                    simpleExoPlayer.setPlayWhenReady(true);
                    simpleExoPlayer.prepare(mediaSource);


                    adapter=new VideoAdapter(getActivity(),list);

                    LinearLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(linearLayoutManager);

                    recyclerView.setHasFixedSize(true);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setNestedScrollingEnabled(true);
                    int itemViewType = 0;
                    recyclerView.getRecycledViewPool().setMaxRecycledViews(itemViewType, 0);
                    recyclerView.setAdapter(adapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        },error -> {

            NetworkResponse response = error.networkResponse;
            if (error instanceof ServerError && response != null) {
                try {
                    String res = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                    // Now you can use any deserializer to make sense of data
                    JSONObject obj = new JSONObject(res);
                } catch (UnsupportedEncodingException | JSONException e1) {
                    // Couldn't properly decode data to string
                    e1.printStackTrace();
                } // returned data is not JSONObject?

            }

        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "563492ad6f91700001000001fd351942a4524d62bb9a68308855b667");
                return params;
            }
        };
        stringRequest.setShouldCache(false);

        if (getActivity()!=null)
        {
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        }
    }
}

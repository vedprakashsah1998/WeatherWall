package com.client.vpman.weatherwall.Fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import ccy.focuslayoutmanager.FocusLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.client.vpman.weatherwall.Adapter.LatestAdapter;
import com.client.vpman.weatherwall.Model.InstagramModel;
import com.client.vpman.weatherwall.databinding.FragmentLatestBinding;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static ccy.focuslayoutmanager.FocusLayoutManager.dp2px;


public class LatestFragment extends Fragment {

    FragmentLatestBinding binding;
    View view;
    List<InstagramModel> lists;
    private LatestAdapter adapter;
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLatestBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        adapter = new LatestAdapter(getActivity(),lists);
        instagramImageApi();
        isabellandscapes();
        artOFBuilding();
        seanView();
        rachel_jones_ross();

        return view;
    }

    private void isabellandscapes()
    {
        if (getActivity()!=null)
        {
            String isabellandscapes = "https://www.instagram.com/isabellandscapes/?__a=1";
            StringRequest stringRequest=new StringRequest(Request.Method.GET, isabellandscapes, response -> {
                Log.d("betaTest", response);

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONObject graphQl=jsonObject.getJSONObject("graphql");
                    JSONObject user=graphQl.getJSONObject("user");
                    JSONObject edge_owner_to_timeline_media=user.getJSONObject("edge_owner_to_timeline_media");
                    JSONArray edges=edge_owner_to_timeline_media.getJSONArray("edges");
                    for (int i=0;i<edges.length();i++)
                    {
                        JSONObject jsonObject1=edges.getJSONObject(i);
                        JSONObject node=jsonObject1.getJSONObject("node");
                        String displayUrl=node.getString("display_url");
                        String thumbnail_src=node.getString("thumbnail_src");
                        String photGrapherUrl="https://www.instagram.com/isabellandscapes/?hl=en";
                        InstagramModel instagramModel=new InstagramModel(displayUrl,thumbnail_src,photGrapherUrl);
                        lists.add(instagramModel);
                    }
                    Collections.shuffle(lists);
                    adapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, error -> {

                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        JSONObject obj = new JSONObject(res);
                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    } catch (JSONException e2) {
                        // returned data is not JSONObject?
                        e2.printStackTrace();
                    }
                }

            });
            stringRequest.setShouldCache(false);

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        }
    }

    private void instagramImageApi() {

        if (getActivity()!=null)
        {
            lists=new ArrayList<>();
            String bestUrl = "https://www.instagram.com/madspeteriversen_photography/?__a=1";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, bestUrl, response -> {
                Log.d("instaresponse", response);

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONObject graphQl=jsonObject.getJSONObject("graphql");
                    JSONObject user=graphQl.getJSONObject("user");
                    JSONObject edge_owner_to_timeline_media=user.getJSONObject("edge_owner_to_timeline_media");
                    JSONArray edges=edge_owner_to_timeline_media.getJSONArray("edges");
                    for (int i=0;i<edges.length();i++)
                    {
                        JSONObject jsonObject1=edges.getJSONObject(i);
                        JSONObject node=jsonObject1.getJSONObject("node");
                        String displayUrl=node.getString("display_url");
                        String thumbnail_src=node.getString("thumbnail_src");
                        String photographerUrl="https://www.instagram.com/madspeteriversen_photography/?hl=en";
                        InstagramModel instagramModel=new InstagramModel(displayUrl,thumbnail_src,photographerUrl);
                        lists.add(instagramModel);
                    }
                    Collections.shuffle(lists);
                    adapter = new LatestAdapter(getActivity(),lists);

                    FocusLayoutManager focusLayoutManager=new FocusLayoutManager.Builder()
                            .layerPadding(dp2px(getActivity(), 14))
                            .normalViewGap(dp2px(getActivity(), 14))
                            .focusOrientation(FocusLayoutManager.FOCUS_LEFT)
                            .isAutoSelect(true)
                            .maxLayerCount(3)
                            .setOnFocusChangeListener((focusdPosition, lastFocusedPosition) -> {
                            }).build();
                    binding.latestRecyler.setHasFixedSize(true);
                    binding.latestRecyler.setLayoutManager(focusLayoutManager);
                    binding.latestRecyler.setAdapter(adapter);
                    binding.latestRecyler.setNestedScrollingEnabled(true);
                    adapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }, error -> {

                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        JSONObject obj = new JSONObject(res);
                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    } catch (JSONException e2) {
                        // returned data is not JSONObject?
                        e2.printStackTrace();
                    }
                }
            });
            stringRequest.setShouldCache(false);

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        }


    }

    private void artOFBuilding()
    {
        if (getActivity()!=null)
        {
           String url= "https://www.instagram.com/art_of_buildings/?__a=1";
            StringRequest stringRequest=new StringRequest(Request.Method.GET, url, response -> {
                Log.d("betaTest", response);

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONObject graphQl=jsonObject.getJSONObject("graphql");
                    JSONObject user=graphQl.getJSONObject("user");
                    JSONObject edge_owner_to_timeline_media=user.getJSONObject("edge_owner_to_timeline_media");
                    JSONArray edges=edge_owner_to_timeline_media.getJSONArray("edges");
                    for (int i=0;i<edges.length();i++)
                    {
                        JSONObject jsonObject1=edges.getJSONObject(i);
                        JSONObject node=jsonObject1.getJSONObject("node");
                        String displayUrl=node.getString("display_url");
                        String thumbnail_src=node.getString("thumbnail_src");
                        String photographerUrl="https://www.instagram.com/art_of_buildings/?hl=en";
                        InstagramModel instagramModel=new InstagramModel(displayUrl,thumbnail_src,photographerUrl);
                        lists.add(instagramModel);
                    }
                    Collections.shuffle(lists);
                    adapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, error -> {

                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        JSONObject obj = new JSONObject(res);
                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    } catch (JSONException e2) {
                        // returned data is not JSONObject?
                        e2.printStackTrace();
                    }
                }

            });
            stringRequest.setShouldCache(false);

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        }

    }

    private void seanView()
    {
        if (getActivity()!=null)
        {
            String Url="https://www.instagram.com/seanbagshaw/?__a=1";
            StringRequest stringRequest=new StringRequest(Request.Method.GET, Url, response -> {
                Log.d("betaTest", response);

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONObject graphQl=jsonObject.getJSONObject("graphql");
                    JSONObject user=graphQl.getJSONObject("user");
                    JSONObject edge_owner_to_timeline_media=user.getJSONObject("edge_owner_to_timeline_media");
                    JSONArray edges=edge_owner_to_timeline_media.getJSONArray("edges");
                    for (int i=0;i<edges.length();i++)
                    {
                        JSONObject jsonObject1=edges.getJSONObject(i);
                        JSONObject node=jsonObject1.getJSONObject("node");
                        String displayUrl=node.getString("display_url");
                        String thumbnail_src=node.getString("thumbnail_src");
                        String photoUrl="https://www.instagram.com/seanbagshaw/?hl=en";
                        InstagramModel instagramModel=new InstagramModel(displayUrl,thumbnail_src,photoUrl);
                        lists.add(instagramModel);
                    }
                    Collections.shuffle(lists);
                    adapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, error -> {

                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        JSONObject obj = new JSONObject(res);
                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    } catch (JSONException e2) {
                        // returned data is not JSONObject?
                        e2.printStackTrace();
                    }
                }

            });
            stringRequest.setShouldCache(false);

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);

        }


    }

    private void rachel_jones_ross()
    {
        if (getActivity()!=null)
        {
            String Url="https://www.instagram.com/rachel_jones_ross/?__a=1";
            StringRequest stringRequest=new StringRequest(Request.Method.GET, Url, response -> {
                Log.d("betaTest", response);

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONObject graphQl=jsonObject.getJSONObject("graphql");
                    JSONObject user=graphQl.getJSONObject("user");
                    JSONObject edge_owner_to_timeline_media=user.getJSONObject("edge_owner_to_timeline_media");
                    JSONArray edges=edge_owner_to_timeline_media.getJSONArray("edges");
                    for (int i=0;i<edges.length();i++)
                    {
                        JSONObject jsonObject1=edges.getJSONObject(i);
                        JSONObject node=jsonObject1.getJSONObject("node");
                        String displayUrl=node.getString("display_url");
                        String thumbnail_src=node.getString("thumbnail_src");
                        String photoUrl="https://www.instagram.com/rachel_jones_ross/?hl=en";
                        InstagramModel instagramModel=new InstagramModel(displayUrl,thumbnail_src,photoUrl);
                        lists.add(instagramModel);
                    }
                    Collections.shuffle(lists);
                    adapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, error -> {

                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        JSONObject obj = new JSONObject(res);
                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    } catch (JSONException e2) {
                        // returned data is not JSONObject?
                        e2.printStackTrace();
                    }
                }

            });
            stringRequest.setShouldCache(false);

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        }
    }

}
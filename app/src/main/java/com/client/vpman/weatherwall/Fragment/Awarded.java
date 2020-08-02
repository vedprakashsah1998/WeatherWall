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
import com.client.vpman.weatherwall.Adapter.AwardedAdapter;
import com.client.vpman.weatherwall.Model.PopularModel;
import com.client.vpman.weatherwall.databinding.FragmentAwardedBinding;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static ccy.focuslayoutmanager.FocusLayoutManager.dp2px;


public class Awarded extends Fragment {

    private String Url = "https://api.pexels.com/v1/popular?per_page=80&page=1";

    private FragmentAwardedBinding binding;
    private ArrayList<PopularModel> list;
    private AwardedAdapter awardedAdapter;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAwardedBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        PopularImage();
        return view;
    }

    private void PopularImage()
    {
        list=new ArrayList<>();
        if (getActivity()!=null)
        {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, response -> {
                Log.d("curatedResponse", response);
                try {
                    JSONObject obj = new JSONObject(response);
                    Log.d("mil gaya", String.valueOf(obj));
                    JSONArray wallArray = obj.getJSONArray("photos");
                    for (int i = 0; i < wallArray.length(); i++) {
                        JSONObject wallobj = wallArray.getJSONObject(i);
                        JSONObject photographer = new JSONObject(String.valueOf(wallobj));
                        Log.d("PhotoURL", wallobj.getString("url"));
                        JSONObject jsonObject = wallobj.getJSONObject("src");
                        JSONObject object = new JSONObject(String.valueOf(jsonObject));
                        PopularModel modelData1 = new PopularModel(object.getString("large2x"), photographer.getString("photographer"), object.getString("large"), object.getString("original"), wallobj.getString("url"));
                        list.add(modelData1);
                    }
                    Collections.shuffle(list);
                    awardedAdapter = new AwardedAdapter(getActivity(),list);
                    /* LinearLayoutManager linearLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);*/

                    FocusLayoutManager focusLayoutManager=new FocusLayoutManager.Builder()
                            .layerPadding(dp2px(getActivity(), 14))
                            .normalViewGap(dp2px(getActivity(), 14))
                            .focusOrientation(FocusLayoutManager.FOCUS_LEFT)
                            .isAutoSelect(true)
                            .maxLayerCount(3)
                            .setOnFocusChangeListener((focusdPosition, lastFocusedPosition) -> {
                            }).build();
                    binding.awardedRecyler.setHasFixedSize(true);
                    binding.awardedRecyler.setNestedScrollingEnabled(true);
                    binding.awardedRecyler.setLayoutManager(focusLayoutManager);
                    binding.awardedRecyler.setAdapter(awardedAdapter);
                    awardedAdapter.notifyDataSetChanged();
                } catch (Exception e) {
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

            }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<>();
                    params.put("Authorization", "563492ad6f917000010000010175b010e54243678613ef0d7fd3c497");
                    return params;
                }
            };

            stringRequest.setShouldCache(false);

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        }

    }



}



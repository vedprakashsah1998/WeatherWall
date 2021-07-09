package com.client.vpman.weatherwall.ui.Fragment;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
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
import com.client.vpman.weatherwall.Adapter.SearchAdapter;
import com.client.vpman.weatherwall.CustomeUsefullClass.Constant;
import com.client.vpman.weatherwall.CustomeUsefullClass.SharedPref1;
import com.client.vpman.weatherwall.R;
import com.client.vpman.weatherwall.databinding.FragmentSearchResultBinding;
import com.client.vpman.weatherwall.model.ModelData;
import com.client.vpman.weatherwall.model.PagerAgentViewModel;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

public class SearchResult extends Fragment {

    FragmentSearchResultBinding binding;
    PagerAgentViewModel viewModel;

    List<ModelData> list;
    SharedPref1 sharedPref1;
    SearchAdapter searchAdapter;
    private List<String> apiList;
    LinearLayoutManager linearLayoutManager;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchResultBinding.inflate(inflater, container, false);
        sharedPref1 = new SharedPref1(getContext());
        if (sharedPref1.getTheme().equals("Light")) {
            binding.backres.setImageResource(R.drawable.ic_arrow_back);
            binding.searchResult.setTextColor(Color.parseColor("#000000"));
            binding.resBackground.setBackgroundColor(Color.parseColor("#FFFFFF"));
            binding.resultToolbar.setBackgroundColor(Color.parseColor("#FFFFFF"));
        } else if (sharedPref1.getTheme().equals("Dark")) {
            binding.backres.setImageResource(R.drawable.ic_baseline_arrow_back_24_white);
            binding.searchResult.setTextColor(Color.parseColor("#FFFFFF"));
            binding.resBackground.setBackgroundColor(Color.parseColor("#000000"));
            binding.resultToolbar.setBackgroundColor(Color.parseColor("#000000"));
        } else {
            switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                case Configuration.UI_MODE_NIGHT_YES:
                    binding.backres.setImageResource(R.drawable.ic_baseline_arrow_back_24_white);
                    binding.searchResult.setTextColor(Color.parseColor("#FFFFFF"));
                    binding.resBackground.setBackgroundColor(Color.parseColor("#000000"));
                    binding.resultToolbar.setBackgroundColor(Color.parseColor("#000000"));

                    break;
                case Configuration.UI_MODE_NIGHT_NO:
                    binding.resBackground.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    binding.resultToolbar.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    binding.backres.setImageResource(R.drawable.ic_arrow_back);
                    binding.searchResult.setTextColor(Color.parseColor("#000000"));
                    break;
            }


        }
        binding.backres.setOnClickListener(v -> getActivity().onBackPressed());

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(PagerAgentViewModel.class);
        viewModel.getText().observe(getViewLifecycleOwner(), charSequence -> {
            binding.searchResult.setText(charSequence);
            LoadData(String.valueOf(charSequence));
            Log.d("defv", (String) charSequence);
        });
    }

    public void LoadData(String query) {
        list = new ArrayList<>();
        String[] data = {"sex", "nude", "porn", "fuck", "vagina", "orgasam", "sexy girl", "nude pic", "hot girl", "porn star", "xvideos",
                "chutiya", "lund", "dick", "pussy", "hot girl", "sexy", "Sex", "Sexy", "Porn", "Vagina", "nudes",
                "Sexy girl", "Porn star", "Xvideos", "Hot girl", "Nude", "Orgasam", "Fuck"};


        if (Arrays.asList(data).contains(query)) {
            if (sharedPref1.getTheme().equals("Light")) {
                binding.notfound.setVisibility(View.VISIBLE);
                binding.notFoundText.setTextColor(Color.parseColor("#000000"));
            } else if (sharedPref1.getTheme().equals("Dark")) {
                binding.notfound1.setVisibility(View.VISIBLE);
                binding.notFoundText.setTextColor(Color.parseColor("#FFFFFF"));

            } else {
                binding.notfound.setVisibility(View.VISIBLE);
                binding.notFoundText.setTextColor(Color.parseColor("#000000"));
            }
            binding.searchResultRecylerview.setVisibility(View.GONE);

            binding.notFoundText.setVisibility(View.VISIBLE);
        } else {
            binding.notFoundText.setVisibility(View.GONE);
            binding.notfound.setVisibility(View.GONE);
            binding.searchResultRecylerview.setVisibility(View.VISIBLE);
        }


        String Url = Constant.BASE_URL + query + "&per_page=80&page=1";
        Log.d("ewjoh", Url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, response -> {
            Log.d("searchResponse", response);
            try {
                JSONObject obj = new JSONObject(response);
                JSONArray wallArray = obj.getJSONArray("photos");
                for (int i = 0; i < wallArray.length(); i++) {
                    JSONObject wallobj = wallArray.getJSONObject(i);
                    JSONObject photographer = new JSONObject(String.valueOf(wallobj));
                    JSONObject PhotoUrl = new JSONObject(String.valueOf(wallobj));
                    Log.d("PhotoURL", wallobj.getString("url"));
                    JSONObject jsonObject = wallobj.getJSONObject("src");
                    JSONObject object = new JSONObject(String.valueOf(jsonObject));
                    ModelData modelData1 = new ModelData(object.getString("large2x"), photographer.getString("photographer"),
                            object.getString("large"), object.getString("original"), wallobj.getString("url"),wallobj.getString("photographer_url"));
                    list.add(modelData1);
                }
                Collections.shuffle(list);

                searchAdapter = new SearchAdapter(getContext(), list);
                linearLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
                binding.searchResultRecylerview.setLayoutManager(linearLayoutManager);
                binding.searchResultRecylerview.setHasFixedSize(true);
                binding.searchResultRecylerview.setItemAnimator(new DefaultItemAnimator());
                binding.searchResultRecylerview.setNestedScrollingEnabled(true);
                int itemViewType = 0;
                binding.searchResultRecylerview.getRecycledViewPool().setMaxRecycledViews(itemViewType, 0);
                binding.searchResultRecylerview.setAdapter(searchAdapter);

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
                apiList = new ArrayList<>();
                apiList.add(getString(R.string.APIKEY1));
                apiList.add(getString(R.string.APIKEY2));
                apiList.add(getString(R.string.APIKEY3));
                apiList.add(getString(R.string.APIKEY4));
                apiList.add(getString(R.string.APIKEY5));
                Random random = new Random();
                int n = random.nextInt(apiList.size());
                params.put("Authorization", apiList.get(n));

                return params;
            }
        };
        stringRequest.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);


    }


}
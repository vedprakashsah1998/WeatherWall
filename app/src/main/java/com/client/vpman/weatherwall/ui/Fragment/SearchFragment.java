package com.client.vpman.weatherwall.ui.Fragment;


import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

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
import com.client.vpman.weatherwall.databinding.FragmentSearchBinding;
import com.client.vpman.weatherwall.model.ModelData;
import com.client.vpman.weatherwall.model.PagerAgentViewModel;

import org.jetbrains.annotations.NotNull;
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

public class SearchFragment extends Fragment {

    private List<String> apiList;
    SearchAdapter adapter;
    List<ModelData> list;
    LinearLayoutManager linearLayoutManager;
    FragmentSearchBinding binding;
    SharedPref1 sharedPref1;
    PagerAgentViewModel viewModel;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        sharedPref1 = new SharedPref1(getContext());
        viewModel = ViewModelProviders.of(getActivity()).get(PagerAgentViewModel.class);
        if (sharedPref1.getTheme().equals("Light")) {
            binding.appBar.setBackgroundColor(Color.parseColor("#FFFFFF"));

            binding.searchData.setBackground(getResources().getDrawable(R.drawable.search_ui));
            binding.searchData.setHintTextColor(getResources().getColor(R.color.black));
            binding.searchData.setTextColor(Color.parseColor("#000000"));


            binding.close.setImageResource(R.drawable.ic_baseline_arrow_back_24);

            binding.natureSearch.setTextColor(Color.parseColor("#000000"));
            binding.travelSearch.setTextColor(Color.parseColor("#000000"));
            binding.architectureSearch.setTextColor(Color.parseColor("#000000"));
            binding.artSearch.setTextColor(Color.parseColor("#000000"));
            binding.beautySearch.setTextColor(Color.parseColor("#000000"));
            binding.decorSearch.setTextColor(Color.parseColor("#000000"));
            binding.foodSearch.setTextColor(Color.parseColor("#000000"));
            binding.musicSearch.setTextColor(Color.parseColor("#000000"));
            binding.sportsSearch.setTextColor(Color.parseColor("#000000"));

        } else if (sharedPref1.getTheme().equals("Dark")) {
            binding.searchData.setTextColor(Color.parseColor("#FFFFFF"));
            binding.searchData.setBackground(getResources().getDrawable(R.drawable.search_ui_dark));
            binding.searchData.setHintTextColor(getResources().getColor(R.color.white));

            binding.appBar.setBackgroundColor(Color.parseColor("#000000"));

            binding.close.setImageResource(R.drawable.ic_baseline_arrow_back_24_white);

            binding.natureSearch.setTextColor(Color.parseColor("#FFFFFF"));
            binding.travelSearch.setTextColor(Color.parseColor("#FFFFFF"));
            binding.architectureSearch.setTextColor(Color.parseColor("#FFFFFF"));
            binding.artSearch.setTextColor(Color.parseColor("#FFFFFF"));
            binding.beautySearch.setTextColor(Color.parseColor("#FFFFFF"));
            binding.decorSearch.setTextColor(Color.parseColor("#FFFFFF"));
            binding.foodSearch.setTextColor(Color.parseColor("#FFFFFF"));
            binding.musicSearch.setTextColor(Color.parseColor("#FFFFFF"));
            binding.sportsSearch.setTextColor(Color.parseColor("#FFFFFF"));
        } else {
            switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                case Configuration.UI_MODE_NIGHT_YES:
                    binding.searchData.setTextColor(Color.parseColor("#FFFFFF"));
                    binding.searchData.setBackground(getResources().getDrawable(R.drawable.search_ui_dark));
                    binding.searchData.setHintTextColor(getResources().getColor(R.color.white));

                    binding.appBar.setBackgroundColor(Color.parseColor("#000000"));

                    binding.close.setImageResource(R.drawable.ic_baseline_arrow_back_24_white);

                    binding.natureSearch.setTextColor(Color.parseColor("#FFFFFF"));
                    binding.travelSearch.setTextColor(Color.parseColor("#FFFFFF"));
                    binding.architectureSearch.setTextColor(Color.parseColor("#FFFFFF"));
                    binding.artSearch.setTextColor(Color.parseColor("#FFFFFF"));
                    binding.beautySearch.setTextColor(Color.parseColor("#FFFFFF"));
                    binding.decorSearch.setTextColor(Color.parseColor("#FFFFFF"));
                    binding.foodSearch.setTextColor(Color.parseColor("#FFFFFF"));
                    binding.musicSearch.setTextColor(Color.parseColor("#FFFFFF"));
                    binding.sportsSearch.setTextColor(Color.parseColor("#FFFFFF"));

                    break;
                case Configuration.UI_MODE_NIGHT_NO:
                    binding.searchData.setTextColor(Color.parseColor("#000000"));
                    binding.searchData.setBackground(getResources().getDrawable(R.drawable.search_ui));
                    binding.searchData.setHintTextColor(getResources().getColor(R.color.black));
                    binding.appBar.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    binding.close.setImageResource(R.drawable.ic_baseline_arrow_back_24);
                    binding.natureSearch.setTextColor(Color.parseColor("#000000"));
                    binding.travelSearch.setTextColor(Color.parseColor("#000000"));
                    binding.architectureSearch.setTextColor(Color.parseColor("#000000"));
                    binding.artSearch.setTextColor(Color.parseColor("#000000"));
                    binding.beautySearch.setTextColor(Color.parseColor("#000000"));
                    binding.decorSearch.setTextColor(Color.parseColor("#000000"));
                    binding.foodSearch.setTextColor(Color.parseColor("#000000"));
                    binding.musicSearch.setTextColor(Color.parseColor("#000000"));
                    binding.sportsSearch.setTextColor(Color.parseColor("#000000"));
                    break;
            }

        }

        binding.natureSearch.setOnClickListener(v -> {
            SentData("Nature");
        });
        binding.travelSearch.setOnClickListener(v -> SentData("Travel"));
        binding.architectureSearch.setOnClickListener(v -> SentData("Architecture"));
        binding.artSearch.setOnClickListener(v -> SentData("Art"));
        binding.beautySearch.setOnClickListener(v -> SentData("Beauty"));
        binding.decorSearch.setOnClickListener(v -> SentData("Decor"));
        binding.foodSearch.setOnClickListener(v -> SentData("food"));
        binding.musicSearch.setOnClickListener(v -> SentData("Music"));
        binding.sportsSearch.setOnClickListener(v -> SentData("Sports"));
        binding.searchData.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                SentData(String.valueOf(binding.searchData.getText()));

                return true;
            }
            return false;
        });


        binding.searchData.setOnFocusChangeListener((v, hasFocus) -> binding.searchData.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0));

        binding.close.setOnClickListener(v -> getActivity().onBackPressed());

        CuratedImages();
        PopularImage();
        return binding.getRoot();
    }

    private void CuratedImages() {
        list = new ArrayList<>();
        if (getActivity() != null) {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.CURATED_URL, response -> {
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
                        ModelData modelData1 = new ModelData(object.getString("large2x"), photographer.getString("photographer"), object.getString("large"), object.getString("original"), wallobj.getString("url"));
                        list.add(modelData1);
                    }
                    Collections.shuffle(list);
                    adapter = new SearchAdapter(getActivity(), list);
                    /* LinearLayoutManager linearLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);*/
                    linearLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
                    binding.recylerViewSearch.setLayoutManager(linearLayoutManager);
                    binding.recylerViewSearch.setHasFixedSize(true);
                    binding.recylerViewSearch.setItemAnimator(new DefaultItemAnimator());
                    binding.recylerViewSearch.setNestedScrollingEnabled(true);
                    int itemViewType = 0;
                    binding.recylerViewSearch.getRecycledViewPool().setMaxRecycledViews(itemViewType, 0);
                    binding.recylerViewSearch.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
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

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        }


    }

    private void PopularImage() {
        list = new ArrayList<>();
        long mRequestStartTime = System.currentTimeMillis();
        if (getActivity() != null) {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.POPULAR_URL, response -> {
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
                        ModelData modelData1 = new ModelData(object.getString("large2x"), photographer.getString("photographer"), object.getString("large"), object.getString("original"), wallobj.getString("url"));
                        list.add(modelData1);
                    }
                    Collections.shuffle(list);
                    adapter.notifyDataSetChanged();
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

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        }


    }

    public void SentData(String query) {
        viewModel.setText(query);
        SearchResult fragment = new SearchResult();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.main_fragment, fragment);
        fragmentTransaction.commit();
    }

}
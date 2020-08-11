package com.client.vpman.weatherwall.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
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
import com.client.vpman.weatherwall.CustomeUsefullClass.SharedPref1;
import com.client.vpman.weatherwall.Model.SearchModel;
import com.client.vpman.weatherwall.R;
import com.client.vpman.weatherwall.databinding.ActivitySearchBinding;
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

public class SearchActivity extends AppCompatActivity {

    ActivitySearchBinding binding;
    String query;
    List<SearchModel> list;
    SharedPref1 sharedPref1;
    SearchAdapter searchAdapter;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Intent intent = getIntent();
        query = intent.getStringExtra("searchQuery");

        sharedPref1=new SharedPref1(SearchActivity.this);
        if (sharedPref1.getTheme().equals("Light"))
        {
            binding.searchrel.setBackgroundColor(Color.parseColor("#F2F6F9"));
            binding.tool1barSearch.setBackgroundColor(Color.parseColor("#FFFFFF"));
            binding.queryText.setTextColor(Color.parseColor("#000000"));
            Resources res = getResources();
            binding.backactivity.setImageResource(R.drawable.ic_arrow_back);
            Drawable drawable = res.getDrawable(R.drawable.edit_text_bg);
            binding.searchViewData.setBackground(drawable);
            binding.searchViewData.setHintTextColor(Color.parseColor("#434343"));
            binding.searchViewData.setTextColor(Color.parseColor("#1A1A1A"));
            binding.searchViewData.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_twotone_search_24, 0);

        }
        else if (sharedPref1.getTheme().equals("Dark"))
        {
            binding.searchrel.setBackgroundColor(Color.parseColor("#000000"));
            binding.tool1barSearch.setBackgroundColor(Color.parseColor("#1A1A1A"));
            binding.queryText.setTextColor(Color.parseColor("#FFFFFF"));
            Resources res = getResources();
            Drawable drawable = res.getDrawable(R.drawable.edit_text_bg_dark);
            binding.searchViewData.setBackground(drawable);
            binding.searchViewData.setHintTextColor(Color.parseColor("#FFFFFF"));
            binding.searchViewData.setTextColor(Color.parseColor("#F2F6F9"));
            binding.searchViewData.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_twotone_search_24_white, 0);
            binding.backactivity.setImageResource(R.drawable.ic_arrow_back_black_24dp);

        }
        else
        {
            binding.backactivity.setImageResource(R.drawable.ic_arrow_back);

            binding.searchrel.setBackgroundColor(Color.parseColor("#F2F6F9"));
            binding.tool1barSearch.setBackgroundColor(Color.parseColor("#FFFFFF"));
            binding.queryText.setTextColor(Color.parseColor("#000000"));

            Resources res = getResources();
            Drawable drawable = res.getDrawable(R.drawable.edit_text_bg);
            binding.searchViewData.setBackground(drawable);
            binding.searchViewData.setHintTextColor(Color.parseColor("#434343"));
            binding.searchViewData.setTextColor(Color.parseColor("#1A1A1A"));
            binding.searchViewData.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_twotone_search_24, 0);

        }

        binding.backactivity.setOnClickListener(v -> onBackPressed());

        binding.searchViewData.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                LoadImage(binding.searchViewData.getText().toString());
                return true;
            }
            return false;
        });
        binding.searchViewData.setOnTouchListener((v, event) -> {

            final int DRAWABLE_LEFT = 0;
            final int DRAWABLE_TOP = 1;
            final int DRAWABLE_RIGHT = 2;
            final int DRAWABLE_BOTTOM = 3;

            if(event.getAction() == MotionEvent.ACTION_UP) {
                if(event.getRawX() >= (binding.searchViewData.getRight()-binding.searchViewData.getCompoundDrawables()
                        [DRAWABLE_RIGHT].getBounds().width())) {
                    // your action here
                    LoadImage(binding.searchViewData.getText().toString());
                    return true;
                }
            }
            return false;
        });
        binding.swipeRefreshLayout.post(() -> {
            binding.swipeRefreshLayout.setRefreshing(true);
            LoadImage(query);
        });
        binding.swipeRefreshLayout.setOnRefreshListener(() -> LoadImage(query));


        binding.searchData.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int topRowVerticalPosition =
                        (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                binding.swipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
            }
        });


        LoadImage(query);
    }

    private void LoadImage(String query)
    {

        list=new ArrayList<>();
        String[] data ={"sex","nude","porn","fuck","vagina","orgasam","sexy girl","nude pic","hot girl","porn star","xvideos",
                "chutiya","lund","dick","pussy","hot girl","sexy","Sex","Sexy","Porn","Vagina",
                "Sexy girl","Porn star","Xvideos","Hot girl","Nude","Orgasam","Fuck"};



        if (Arrays.asList(data).contains(query))
        {
            if (sharedPref1.getTheme().equals("Light"))
            {
                binding.notfound.setVisibility(View.VISIBLE);
                binding.notFoundText.setTextColor(Color.parseColor("#000000"));
            }
            else if (sharedPref1.getTheme().equals("Dark"))
            {
                binding.notfound1.setVisibility(View.VISIBLE);
                binding.notFoundText.setTextColor(Color.parseColor("#FFFFFF"));

            }
            else
            {
                binding.notfound.setVisibility(View.VISIBLE);
                binding.notFoundText.setTextColor(Color.parseColor("#000000"));
            }
            binding.searchData.setVisibility(View.GONE);

            binding.notFoundText.setVisibility(View.VISIBLE);
        }
        else
        {
            binding.notFoundText.setVisibility(View.GONE);
            binding.notfound.setVisibility(View.GONE);
            binding.searchData.setVisibility(View.VISIBLE);
        }

        binding.queryText.setText(query);

        String Url = "https://api.pexels.com/v1/search?query=" + query + "&per_page=100&page=1";
        Log.d("ewjoh",Url);

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
                    SearchModel modelData1 = new SearchModel(object.getString("large2x"), photographer.getString("photographer"), object.getString("large"), object.getString("original"), wallobj.getString("url"));
                    list.add(modelData1);
                }
                Collections.shuffle(list);
                searchAdapter = new SearchAdapter(SearchActivity.this, list);
                LinearLayoutManager linearLayoutManager = new GridLayoutManager(SearchActivity.this, 2, GridLayoutManager.VERTICAL, false);
                binding.searchData.setLayoutManager(linearLayoutManager);
                binding.searchData.setHasFixedSize(true);
                binding.searchData.setItemAnimator(new DefaultItemAnimator());
                binding.searchData.setNestedScrollingEnabled(true);
                int itemViewType = 0;
                binding.searchData.getRecycledViewPool().setMaxRecycledViews(itemViewType, 0);
                binding.searchData.setAdapter(searchAdapter);
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
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "563492ad6f917000010000010175b010e54243678613ef0d7fd3c497");
                return params;
            }
        };
        stringRequest.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(SearchActivity.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
        onItemsLoadComplete();
    }
    void onItemsLoadComplete() {
        binding.swipeRefreshLayout.setRefreshing(false);
    }

}
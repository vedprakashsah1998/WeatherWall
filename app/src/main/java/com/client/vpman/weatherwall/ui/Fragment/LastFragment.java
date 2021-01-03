package com.client.vpman.weatherwall.ui.Fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.ObjectKey;
import com.client.vpman.weatherwall.CustomeUsefullClass.Constant;
import com.client.vpman.weatherwall.ui.Activity.SearchActivity;
import com.client.vpman.weatherwall.ui.Activity.TestingMotionLayout;
import com.client.vpman.weatherwall.CustomeUsefullClass.SharedPref1;
import com.client.vpman.weatherwall.CustomeUsefullClass.Utils;
import com.client.vpman.weatherwall.R;
import com.client.vpman.weatherwall.databinding.FragmentLastBinding;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textview.MaterialTextView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class LastFragment extends Fragment {


    public LastFragment() {
        // Required empty public constructor
    }

    private List<String> apiList;


    Fragment fragment = null;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    private Animation bounce;


    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentLastBinding binding = FragmentLastBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        bounce = AnimationUtils.loadAnimation(getActivity(), R.anim.bounce);

        bounce.setRepeatCount(Animation.INFINITE);
        bounce.setRepeatMode(Animation.INFINITE);
        bounce.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                bounce = AnimationUtils.loadAnimation(getActivity(), R.anim.bounce);

                bounce.setRepeatCount(Animation.INFINITE);
                bounce.setRepeatMode(Animation.INFINITE);
                binding.SwipUpdisc.startAnimation(bounce);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        binding.SwipUpdisc.startAnimation(bounce);

        fragment = new CuratedList();
        if (getActivity() != null) {
            fragmentManager = getActivity().getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout, fragment);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.commit();
            SharedPref1 sharedPref1 = new SharedPref1(getActivity());
            if (sharedPref1.getTheme().equals("Light")) {
                binding.rlLayoutDisc.setBackgroundColor(Color.parseColor("#FFFFFF"));
                binding.discoverText.setTextColor(Color.parseColor("#1A1A1A"));
                binding.topic.setTextColor(getResources().getColor(R.color.black));
                binding.category.setTextColor(getResources().getColor(R.color.black));
                binding.searchIcon.setImageResource(R.drawable.ic_loupe);
                binding.tabLayoutLast.setTabTextColors(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                binding.SwipUpdisc.setImageResource(R.drawable.ic_up_arow_black);


            } else if (sharedPref1.getTheme().equals("Dark")) {
                binding.rlLayoutDisc.setBackgroundColor(Color.parseColor("#000000"));
                binding.discoverText.setTextColor(Color.parseColor("#FFFFFF"));
                binding.topic.setTextColor(getResources().getColor(R.color.white));
                binding.category.setTextColor(getResources().getColor(R.color.white));
                binding.SwipUpdisc.setImageResource(R.drawable.ic_up_arow);
                binding.searchIcon.setImageResource(R.drawable.ic_loupe_white);
                binding.tabLayoutLast.setTabTextColors(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            } else {
                binding.searchIcon.setImageResource(R.drawable.ic_loupe);
                binding.tabLayoutLast.setTabTextColors(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                binding.rlLayoutDisc.setBackgroundColor(Color.parseColor("#FFFFFF"));
                binding.discoverText.setTextColor(Color.parseColor("#1A1A1A"));
                binding.topic.setTextColor(getResources().getColor(R.color.black));
                binding.category.setTextColor(getResources().getColor(R.color.black));

                binding.SwipUpdisc.setImageResource(R.drawable.ic_up_arow_black);


            }
        }


        binding.tabLayoutLast.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        fragment = new CuratedList();
                        break;
                    case 1:
                        fragment = new Awarded();
                        break;
                    case 2:
                        fragment = new LatestFragment();
                        break;
                }
                FragmentManager fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.frameLayout, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        RequestOptions requestOptions = new RequestOptions();
        // requestOptions.error(Utils.getRandomDrawbleColor());
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
                .signature(new ObjectKey(System.currentTimeMillis())).encodeQuality(70);
        requestOptions.priority(Priority.IMMEDIATE);
        requestOptions.skipMemoryCache(false);
        requestOptions.onlyRetrieveFromCache(true);
        requestOptions.placeholder(Utils.getRandomDrawbleColor());
        requestOptions.priority(Priority.HIGH);
        requestOptions.isMemoryCacheable();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.DATA);

        requestOptions.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        //   requestOptions.placeholder(Utils.getRandomDrawbleColor());
        requestOptions.centerCrop();


        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        switch (day) {
            case Calendar.SUNDAY:
                // Current day is Sunday
                binding.fashionTextView.setText("fashion");

                setImage(requestOptions, "fashion", binding.fashionImageView, binding.fashionTextView);

                binding.bikeRideTextView.setText("bike ride");

                setImage(requestOptions, "bike ride", binding.bikeRideImageView, binding.bikeRideTextView);

                binding.abstractTextView.setText("abstract");
                setImage(requestOptions, "abstract", binding.abstractImageView, binding.abstractTextView);
                binding.coffeeShopTextView.setText("coffee shop");
                setImage(requestOptions, "coffee shop", binding.coffeeShopImageView, binding.coffeeShopTextView);
                binding.gameTextView.setText("game");
                setImage(requestOptions, "game", binding.gameImageView, binding.gameTextView);
                binding.vacationTextView.setText("vacation");
                setImage(requestOptions, "vacation", binding.vacationImageView, binding.vacationTextView);
                break;
            case Calendar.MONDAY:
                binding.fashionTextView.setText("bar");
                setImage(requestOptions, "bar", binding.fashionImageView, binding.fashionTextView);
                binding.bikeRideTextView.setText("london");
                setImage(requestOptions, "london", binding.bikeRideImageView, binding.bikeRideTextView);
                binding.abstractTextView.setText("puppy");
                setImage(requestOptions, "puppy", binding.abstractImageView, binding.abstractTextView);

                binding.coffeeShopTextView.setText("e commerce");
                setImage(requestOptions, "e commerce", binding.coffeeShopImageView, binding.coffeeShopTextView);

                binding.gameTextView.setText("camping");
                setImage(requestOptions, "camping", binding.gameImageView, binding.gameTextView);

                binding.vacationTextView.setText("basketball");
                setImage(requestOptions, "basketball", binding.vacationImageView, binding.vacationTextView);

                // Current day is Monday
                break;
            case Calendar.TUESDAY:
                binding.fashionTextView.setText("sleeping");
                setImage(requestOptions, "sleeping", binding.fashionImageView, binding.fashionTextView);

                binding.bikeRideTextView.setText("microphone");
                setImage(requestOptions, "microphone", binding.bikeRideImageView, binding.bikeRideTextView);

                binding.abstractTextView.setText("video conference");
                setImage(requestOptions, "video conference", binding.abstractImageView, binding.abstractTextView);
                binding.coffeeShopTextView.setText("strategy");
                setImage(requestOptions, "strategy", binding.coffeeShopImageView, binding.coffeeShopTextView);
                binding.gameTextView.setText("hiking");
                setImage(requestOptions, "hiking", binding.gameImageView, binding.gameTextView);

                binding.vacationTextView.setText("airport");
                setImage(requestOptions, "airport", binding.vacationImageView, binding.vacationTextView);

                // etc.
                break;
            case Calendar.WEDNESDAY:
                binding.fashionTextView.setText("dark and moody");
                setImage(requestOptions, "dark and moody", binding.fashionImageView, binding.fashionTextView);

                binding.bikeRideTextView.setText("Holiday Mood");
                setImage(requestOptions, "Holiday Mood", binding.bikeRideImageView, binding.bikeRideTextView);

                binding.abstractTextView.setText("Winter");
                setImage(requestOptions, "Winter", binding.abstractImageView, binding.abstractTextView);
                binding.coffeeShopTextView.setText("Dark Portraits");
                setImage(requestOptions, "Dark Portraits", binding.coffeeShopImageView, binding.coffeeShopTextView);

                binding.gameTextView.setText("Space Travel");
                setImage(requestOptions, "Space Travel", binding.gameImageView, binding.gameTextView);

                binding.vacationTextView.setText("Let's Party");
                setImage(requestOptions, "Let's Party", binding.vacationImageView, binding.vacationTextView);
                break;
            case Calendar.THURSDAY:
                binding.fashionTextView.setText("Cosmetics");
                setImage(requestOptions, "Cosmetics", binding.fashionImageView, binding.fashionTextView);
                binding.bikeRideTextView.setText("Retro");
                setImage(requestOptions, "Retro", binding.bikeRideImageView, binding.bikeRideTextView);

                binding.abstractTextView.setText("Summertime");
                setImage(requestOptions, "Summertime", binding.abstractImageView, binding.abstractTextView);

                binding.coffeeShopTextView.setText("Rainy Days");
                setImage(requestOptions, "Rainy Days", binding.coffeeShopImageView, binding.coffeeShopTextView);
                binding.gameTextView.setText("Floral Beauty");
                setImage(requestOptions, "Floral Beauty", binding.gameImageView, binding.gameTextView);
                binding.vacationTextView.setText("Home");
                setImage(requestOptions, "Home", binding.vacationImageView, binding.vacationTextView);

                break;
            case Calendar.FRIDAY:
                binding.fashionTextView.setText("Dancers");
                setImage(requestOptions, "Dancers", binding.fashionImageView, binding.fashionTextView);

                binding.bikeRideTextView.setText("Work");
                setImage(requestOptions, "Work", binding.bikeRideImageView, binding.bikeRideTextView);
                binding.abstractTextView.setText("marine");
                setImage(requestOptions, "marine", binding.abstractImageView, binding.abstractTextView);

                binding.coffeeShopTextView.setText("animals");
                setImage(requestOptions, "animals", binding.coffeeShopImageView, binding.coffeeShopTextView);
                binding.gameTextView.setText("Maldives");
                setImage(requestOptions, "Maldives", binding.gameImageView, binding.gameTextView);

                binding.vacationTextView.setText("Minimal black and white");
                setImage(requestOptions, "Minimal black and white", binding.vacationImageView, binding.vacationTextView);
                break;
            case Calendar.SATURDAY:
                binding.fashionTextView.setText("spectrum");
                setImage(requestOptions, "spectrum", binding.fashionImageView, binding.fashionTextView);
                binding.bikeRideTextView.setText("together");
                setImage(requestOptions, "together", binding.bikeRideImageView, binding.bikeRideTextView);

                binding.abstractTextView.setText("christmas");
                setImage(requestOptions, "christmas", binding.abstractImageView, binding.abstractTextView);

                binding.coffeeShopTextView.setText("party night");
                setImage(requestOptions, "party night", binding.coffeeShopImageView, binding.coffeeShopTextView);

                binding.gameTextView.setText("extream neon");
                setImage(requestOptions, "extream neon", binding.gameImageView, binding.gameTextView);
                binding.vacationTextView.setText("autumn");
                setImage(requestOptions, "autumn", binding.vacationImageView, binding.vacationTextView);

                break;
        }

        binding.searchIcon.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), SearchActivity.class));
        });

        return view;
    }

    private void setImage(RequestOptions requestOptions, String query, ShapeableImageView img, MaterialTextView textView) {

        String Url = Constant.BASE_URL + query + "&per_page=80&page=1";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, response -> {
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

                    LruCache<String, Bitmap> memCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / (1024 * 4))) {
                        @Override
                        protected int sizeOf(String key, Bitmap image) {
                            return image.getByteCount() / 1024;
                        }
                    };

                    Bitmap image = memCache.get("imagefile");
                    if (image != null) {
                        //Bitmap exists in cache.
                        img.setImageBitmap(image);
                    } else {
                        if (getActivity() != null) {
                            Glide.with(getActivity())
                                    .load(object.getString("large"))
                                    .thumbnail(
                                            Glide.with(Objects.requireNonNull(getActivity())).load(object.getString("large2x"))
                                    )
                                    .apply(requestOptions)
                                    .listener(new RequestListener<Drawable>() {
                                        @Override
                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                            //  spinKitView.setVisibility(View.GONE);


                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                                            //    spinKitView.setVisibility(View.GONE);

                                            return false;
                                        }
                                    })
                                    .into(img);

                            img.setOnClickListener(v -> {
                                Intent intent = new Intent(getActivity(), TestingMotionLayout.class);
                                try {
                                    intent.putExtra("img1", object.getString("large2x"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    intent.putExtra("img2", object.getString("large"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                intent.putExtra("query", query);
                                intent.putExtra("text", query);

                                Pair[] pairs = new Pair[2];
                                pairs[0] = new Pair<View, String>(img, "img1");
                                pairs[1] = new Pair<View, String>(textView, "text");

                                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                        getActivity(), pairs
                                );

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    startActivity(intent, optionsCompat.toBundle());
                                } else {
                                    startActivity(intent);
                                }
                            });

                        }

                    }
                }


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


    public static LastFragment newInstance(String text) {
        LastFragment f = new LastFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }


}

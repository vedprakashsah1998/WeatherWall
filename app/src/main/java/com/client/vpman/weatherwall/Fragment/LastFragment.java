package com.client.vpman.weatherwall.Fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
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

import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.ObjectKey;
import com.client.vpman.weatherwall.Activity.SearchActivity;
import com.client.vpman.weatherwall.Activity.TestingMotionLayout;
import com.client.vpman.weatherwall.CustomeUsefullClass.SharedPref1;
import com.client.vpman.weatherwall.CustomeUsefullClass.Utils;
import com.client.vpman.weatherwall.R;
import com.client.vpman.weatherwall.databinding.FragmentLastBinding;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textview.MaterialTextView;
import com.kc.unsplash.Unsplash;
import com.kc.unsplash.models.Photo;
import com.kc.unsplash.models.SearchResults;


import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class LastFragment extends Fragment {


    public LastFragment() {
        // Required empty public constructor
    }

    Fragment fragment = null;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    private Unsplash unsplash;
    private Animation fromtop, bounce;


    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentLastBinding binding = FragmentLastBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        fromtop = AnimationUtils.loadAnimation(getActivity(), R.anim.fromtop);
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
                /*binding.searchView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_twotone_search_24, 0);*/

                binding.SwipUpdisc.setImageResource(R.drawable.ic_up_arow_black);

                Resources res = getResources();
                Drawable drawable = res.getDrawable(R.drawable.edit_text_bg);
/*                binding.searchView.setBackground(drawable);
                binding.searchView.setHintTextColor(Color.parseColor("#434343"));
                binding.searchView.setTextColor(Color.parseColor("#1A1A1A"));*/

            } else if (sharedPref1.getTheme().equals("Dark")) {
                binding.rlLayoutDisc.setBackgroundColor(Color.parseColor("#000000"));
                binding.discoverText.setTextColor(Color.parseColor("#FFFFFF"));
                binding.topic.setTextColor(getResources().getColor(R.color.white));
                binding.category.setTextColor(getResources().getColor(R.color.white));
                Resources res = getResources();
 /*               Drawable drawable = res.getDrawable(R.drawable.edit_text_bg_dark);
                binding.searchView.setBackground(drawable);
                binding.searchView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_twotone_search_24_white, 0);
                binding.searchView.setHintTextColor(Color.parseColor("#FFFFFF"));*/
                binding.SwipUpdisc.setImageResource(R.drawable.ic_up_arow);
                binding.searchIcon.setImageResource(R.drawable.ic_loupe_white);
                /*binding.searchView.setTextColor(Color.parseColor("#F2F6F9"));*/
                binding.tabLayoutLast.setTabTextColors(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            } else {
                /*binding.searchView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_twotone_search_24, 0);*/
                binding.searchIcon.setImageResource(R.drawable.ic_loupe);
                binding.tabLayoutLast.setTabTextColors(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                binding.rlLayoutDisc.setBackgroundColor(Color.parseColor("#FFFFFF"));
                binding.discoverText.setTextColor(Color.parseColor("#1A1A1A"));
                binding.topic.setTextColor(getResources().getColor(R.color.black));
                binding.category.setTextColor(getResources().getColor(R.color.black));

                binding.SwipUpdisc.setImageResource(R.drawable.ic_up_arow_black);

 /*               Resources res = getResources();
                Drawable drawable = res.getDrawable(R.drawable.edit_text_bg);
                binding.searchView.setBackground(drawable);
                binding.searchView.setHintTextColor(Color.parseColor("#434343"));
                binding.searchView.setTextColor(Color.parseColor("#1A1A1A"));*/

            }
        }



      /*  binding.searchView.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                Intent intent=new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("searchQuery",binding.searchView.getText().toString());
                startActivity(intent);
                return true;
            }
            return false;
        });
        binding.searchView.setOnTouchListener((v, event) -> {

            final int DRAWABLE_LEFT = 0;
            final int DRAWABLE_TOP = 1;
            final int DRAWABLE_RIGHT = 2;
            final int DRAWABLE_BOTTOM = 3;

            if(event.getAction() == MotionEvent.ACTION_UP) {
                if(event.getRawX() >= (binding.searchView.getRight() - binding.searchView.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    // your action here
                    Intent intent=new Intent(getActivity(), SearchActivity.class);
                    intent.putExtra("searchQuery",binding.searchView.getText().toString());
                    startActivity(intent);
                    return true;
                }
            }
            return false;
        });*/


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

/*
        String CLIENT_ID = "WWNYnxHendMl4D2GIXD_l14mcK4x7QwFJ-56VtQPdF8";
*/

        String CLIENT_ID = "p8S-xjITsctkke0ZmKIdklrug3IMpYcMdObQuGx5xOY";

        unsplash = new Unsplash(CLIENT_ID);

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

                setImage(binding, requestOptions, "fashion", binding.fashionImageView, binding.fashionTextView);

                binding.bikeRideTextView.setText("bike ride");

                setImage(binding, requestOptions, "bike ride", binding.bikeRideImageView, binding.bikeRideTextView);

                binding.abstractTextView.setText("abstract");
                setImage(binding, requestOptions, "abstract", binding.abstractImageView, binding.abstractTextView);
                binding.coffeeShopTextView.setText("coffee shop");
                setImage(binding, requestOptions, "coffee shop", binding.coffeeShopImageView, binding.coffeeShopTextView);
                binding.gameTextView.setText("game");
                setImage(binding, requestOptions, "game", binding.gameImageView, binding.gameTextView);
                binding.vacationTextView.setText("vacation");
                setImage(binding, requestOptions, "vacation", binding.vacationImageView, binding.vacationTextView);
                break;
            case Calendar.MONDAY:
                binding.fashionTextView.setText("bar");
                setImage(binding, requestOptions, "bar", binding.fashionImageView, binding.fashionTextView);
                binding.bikeRideTextView.setText("london");
                setImage(binding, requestOptions, "london", binding.bikeRideImageView, binding.bikeRideTextView);
                binding.abstractTextView.setText("puppy");
                setImage(binding, requestOptions, "puppy", binding.abstractImageView, binding.abstractTextView);

                binding.coffeeShopTextView.setText("e commerce");
                setImage(binding, requestOptions, "e commerce", binding.coffeeShopImageView, binding.coffeeShopTextView);

                binding.gameTextView.setText("camping");
                setImage(binding, requestOptions, "camping", binding.gameImageView, binding.gameTextView);

                binding.vacationTextView.setText("basketball");
                setImage(binding, requestOptions, "basketball", binding.vacationImageView, binding.vacationTextView);

                // Current day is Monday
                break;
            case Calendar.TUESDAY:
                binding.fashionTextView.setText("sleeping");
                setImage(binding, requestOptions, "sleeping", binding.fashionImageView, binding.fashionTextView);

                binding.bikeRideTextView.setText("microphone");
                setImage(binding, requestOptions, "microphone", binding.bikeRideImageView, binding.bikeRideTextView);

                binding.abstractTextView.setText("video conference");
                setImage(binding, requestOptions, "video conference", binding.abstractImageView, binding.abstractTextView);
                binding.coffeeShopTextView.setText("strategy");
                setImage(binding, requestOptions, "strategy", binding.coffeeShopImageView, binding.coffeeShopTextView);
                binding.gameTextView.setText("hiking");
                setImage(binding, requestOptions, "hiking", binding.gameImageView, binding.gameTextView);

                binding.vacationTextView.setText("airport");
                setImage(binding, requestOptions, "airport", binding.vacationImageView, binding.vacationTextView);

                // etc.
                break;
            case Calendar.WEDNESDAY:
                binding.fashionTextView.setText("dark and moody");
                setImage(binding, requestOptions, "dark and moody", binding.fashionImageView, binding.fashionTextView);

                binding.bikeRideTextView.setText("Holiday Mood");
                setImage(binding, requestOptions, "Holiday Mood", binding.bikeRideImageView, binding.bikeRideTextView);

                binding.abstractTextView.setText("Winter");
                setImage(binding, requestOptions, "Winter", binding.abstractImageView, binding.abstractTextView);
                binding.coffeeShopTextView.setText("Dark Portraits");
                setImage(binding, requestOptions, "Dark Portraits", binding.coffeeShopImageView, binding.coffeeShopTextView);

                binding.gameTextView.setText("Space Travel");
                setImage(binding, requestOptions, "Space Travel", binding.gameImageView, binding.gameTextView);

                binding.vacationTextView.setText("Let's Party");
                setImage(binding, requestOptions, "Let's Party", binding.vacationImageView, binding.vacationTextView);
                break;
            case Calendar.THURSDAY:
                binding.fashionTextView.setText("Cosmetics");
                setImage(binding, requestOptions, "Cosmetics", binding.fashionImageView, binding.fashionTextView);
                binding.bikeRideTextView.setText("Retro");
                setImage(binding, requestOptions, "Retro", binding.bikeRideImageView, binding.bikeRideTextView);

                binding.abstractTextView.setText("Summertime");
                setImage(binding, requestOptions, "Summertime", binding.abstractImageView, binding.abstractTextView);

                binding.coffeeShopTextView.setText("Rainy Days");
                setImage(binding, requestOptions, "Rainy Days", binding.coffeeShopImageView, binding.coffeeShopTextView);
                binding.gameTextView.setText("Floral Beauty");
                setImage(binding, requestOptions, "Floral Beauty", binding.gameImageView, binding.gameTextView);
                binding.vacationTextView.setText("Home");
                setImage(binding, requestOptions, "Home", binding.vacationImageView, binding.vacationTextView);

                break;
            case Calendar.FRIDAY:
                binding.fashionTextView.setText("Dancers");
                setImage(binding, requestOptions, "Dancers", binding.fashionImageView, binding.fashionTextView);

                binding.bikeRideTextView.setText("Work");
                setImage(binding, requestOptions, "Work", binding.bikeRideImageView, binding.bikeRideTextView);
                binding.abstractTextView.setText("marine");
                setImage(binding, requestOptions, "marine", binding.abstractImageView, binding.abstractTextView);

                binding.coffeeShopTextView.setText("animals");
                setImage(binding, requestOptions, "animals", binding.coffeeShopImageView, binding.coffeeShopTextView);
                binding.gameTextView.setText("Maldives");
                setImage(binding, requestOptions, "Maldives", binding.gameImageView, binding.gameTextView);

                binding.vacationTextView.setText("Minimal black and white");
                setImage(binding, requestOptions, "Minimal black and white", binding.vacationImageView, binding.vacationTextView);
                break;
            case Calendar.SATURDAY:
                binding.fashionTextView.setText("spectrum");
                setImage(binding, requestOptions, "spectrum", binding.fashionImageView, binding.fashionTextView);
                binding.bikeRideTextView.setText("together");
                setImage(binding, requestOptions, "together", binding.bikeRideImageView, binding.bikeRideTextView);

                binding.abstractTextView.setText("christmas");
                setImage(binding, requestOptions, "christmas", binding.abstractImageView, binding.abstractTextView);

                binding.coffeeShopTextView.setText("party night");
                setImage(binding, requestOptions, "party night", binding.coffeeShopImageView, binding.coffeeShopTextView);

                binding.gameTextView.setText("extream neon");
                setImage(binding, requestOptions, "extream neon", binding.gameImageView, binding.gameTextView);
                binding.vacationTextView.setText("autumn");
                setImage(binding, requestOptions, "autumn", binding.vacationImageView, binding.vacationTextView);

                break;
        }

        binding.searchIcon.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(),SearchActivity.class));
        });

        return view;
    }

    private void setImage(FragmentLastBinding binding, RequestOptions requestOptions, String query, ShapeableImageView img, MaterialTextView textView) {
        unsplash.searchPhotos(query, 1, 20, "portrait", new Unsplash.OnSearchCompleteListener() {
            @Override
            public void onComplete(SearchResults results) {
                List<Photo> photos = results.getResults();

                Random random = new Random();
                int n = random.nextInt(photos.size());

                Collections.shuffle(photos);

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
                                .load(photos.get(n).getUrls().getFull())
                                .thumbnail(
                                        Glide.with(Objects.requireNonNull(getActivity())).load(photos.get(n).getUrls().getRegular())
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
                            intent.putExtra("img1", photos.get(n).getUrls().getFull());
                            intent.putExtra("img2", photos.get(n).getUrls().getRegular());
                            intent.putExtra("query", query);
                            intent.putExtra("text", query);

                            Pair[] pairs = new Pair[2];
                            pairs[0] = new Pair<View, String>(img, "img1");
                            pairs[1] = new Pair<View, String>(textView, "text");

                           /* Pair<View, String> pair = Pair.create((View)Landscape, ViewCompat.getTransitionName(Landscape));
                            Pair<View, String> pair1 = Pair.create((View)Landscape1, ViewCompat.getTransitionName(Landscape1));*/
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

            @Override
            public void onError(String error) {

            }
        });
    }


    public static LastFragment newInstance(String text) {
        LastFragment f = new LastFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }


}

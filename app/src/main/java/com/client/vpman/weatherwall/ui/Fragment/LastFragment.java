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

import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.ObjectKey;
import com.client.vpman.weatherwall.ui.Activity.SearchActivity;
import com.client.vpman.weatherwall.ui.Activity.TestingMotionLayout;
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
        unsplash.searchPhotos(query, 1, 20, "portrait", new Unsplash.OnSearchCompleteListener() {
            @Override
            public void onComplete(SearchResults results) {
                List<Photo> photos = results.getResults();

                if (photos != null) {
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
                else
                {
                    Toast.makeText(getContext(), "Unsplash Error", Toast.LENGTH_SHORT).show();
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

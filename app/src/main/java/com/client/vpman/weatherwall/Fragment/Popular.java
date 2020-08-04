package com.client.vpman.weatherwall.Fragment;


import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.LruCache;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.ObjectKey;
import com.client.vpman.weatherwall.Activity.TestingMotionLayout;
import com.client.vpman.weatherwall.CustomeUsefullClass.Connectivity;
import com.client.vpman.weatherwall.CustomeUsefullClass.SharedPref1;
import com.client.vpman.weatherwall.CustomeUsefullClass.Utils;
import com.client.vpman.weatherwall.R;
import com.client.vpman.weatherwall.databinding.FragmentPopularBinding;
import com.client.vpman.weatherwall.databinding.FragmentWeatherBinding;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.textview.MaterialTextView;
import com.kc.unsplash.Unsplash;
import com.kc.unsplash.models.Photo;
import com.kc.unsplash.models.SearchResults;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class Popular extends Fragment {

    View view;

    public Popular() {
        // Required empty public constructor
    }

    private String query, query1, query2, query3, query4, query5, query6, query7, query8;
    private SharedPref1 sharedPref1;
    private Animation fromtop, bounce;
    private FragmentPopularBinding binding;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= FragmentPopularBinding.inflate(inflater,container,false);
        view = binding.getRoot();
        String CLIENT_ID = "fcd5073926c7fdd11b9eb62887dbd6398eafbb8f3c56073035b141ad57d1ab5f";
        Unsplash unsplash = new Unsplash(CLIENT_ID);

        fromtop = AnimationUtils.loadAnimation(getActivity(), R.anim.fromtop);

        if (getActivity() != null) {
            sharedPref1 = new SharedPref1(getActivity());
            if (sharedPref1.getTheme().equals("Light")) {
                binding.relpop.setBackgroundColor(Color.parseColor("#FFFFFF"));
                binding.popPhoto1.setTextColor(Color.parseColor("#000000"));
                binding.SwipUp.setImageResource(R.drawable.ic_up_arow_black);
                binding.relLandscanpe.setBackgroundColor(Color.parseColor("#FFFFFF"));
            } else if (sharedPref1.getTheme().equals("Dark")) {
                binding.relpop.setBackgroundColor(Color.parseColor("#000000"));
                binding.popPhoto1.setTextColor(Color.parseColor("#FFFFFF"));
                binding.SwipUp.setImageResource(R.drawable.ic_up_arow);
                binding.relLandscanpe.setBackgroundColor(Color.parseColor("#000000"));
            } else {
                binding.relpop.setBackgroundColor(Color.parseColor("#FFFFFF"));
                binding.popPhoto1.setTextColor(Color.parseColor("#000000"));
                binding.SwipUp.setImageResource(R.drawable.ic_up_arow_black);
                binding.relLandscanpe.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        }


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
                binding.SwipUp.startAnimation(bounce);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        binding.SwipUp.startAnimation(bounce);

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

        query = "drone view";
        if (Connectivity.isConnected(getActivity()) && Connectivity.isConnectedMobile(getActivity()) && Connectivity.isConnectedFast(getActivity()) ||
                Connectivity.isConnected(getActivity()) && Connectivity.isConnectedWifi(getActivity()) && Connectivity.isConnectedFast(getActivity())) {

            binding.droneView.setShapeAppearanceModel(binding.droneView.getShapeAppearanceModel()
                    .toBuilder().setTopLeftCorner(CornerFamily.ROUNDED,150)
                    .setBottomLeftCorner(CornerFamily.ROUNDED,150).build());
            unsplash.searchPhotos(query, 1, 20, "portrait", new Unsplash.OnSearchCompleteListener() {
                @Override
                public void onComplete(SearchResults results) {
                    Log.d("Photos", "Total Results Found " + results.getTotal());

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
                        binding.droneView.setImageBitmap(image);
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

                                    .into(binding.droneView);


                            binding.droneView.setOnClickListener(view -> {
                                Intent intent = new Intent(getActivity(), TestingMotionLayout.class);
                                intent.putExtra("img1", photos.get(n).getUrls().getFull());
                                intent.putExtra("img2", photos.get(n).getUrls().getRegular());
                                intent.putExtra("query", query);
                                intent.putExtra("text", "Drone View");

                                Pair[] pairs = new Pair[1];
                                pairs[0] = new Pair<View, String>( binding.droneView, "img1");


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


                    // Glide.with(getActivity()).load(photos.get(n).getUrls().getFull()).into(imageView);

                }

                @Override
                public void onError(String error) {
                    Log.d("Unsplash", error);
                }
            });

            query1 = "nature";


            unsplash.searchPhotos(query1, 33, 20, "landscape", new Unsplash.OnSearchCompleteListener() {
                @Override
                public void onComplete(SearchResults results) {
                    Log.d("Photos", "Total Results Found " + results.getTotal());

                    List<Photo> photos = results.getResults();
                    Collections.shuffle(photos);
                    Random random = new Random();
                    int n = random.nextInt(photos.size());
                    LruCache<String, Bitmap> memCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / (1024 * 4))) {
                        @Override
                        protected int sizeOf(String key, Bitmap image) {
                            return image.getByteCount() / 1024;
                        }
                    };

                    binding.nature.setShapeAppearanceModel( binding.nature.getShapeAppearanceModel()
                            .toBuilder()
                            .setTopRightCorner(CornerFamily.ROUNDED,150)
                            .build());

                    Bitmap image = memCache.get("imagefile");
                    if (image != null) {
                        //Bitmap exists in cache.
                        binding.nature.setImageBitmap(image);
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

                                    .into( binding.nature);
                            binding.nature.setOnClickListener(view -> {
                                Intent intent = new Intent(getActivity(), TestingMotionLayout.class);
                                intent.putExtra("img1", photos.get(n).getUrls().getFull());
                                intent.putExtra("img2", photos.get(n).getUrls().getRegular());
                                intent.putExtra("query", query1);
                                intent.putExtra("text", "Nature");

                                Pair[] pairs = new Pair[1];
                                pairs[0] = new Pair<View, String>(  binding.nature, "img1");

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
                    Log.d("Unsplash", error);
                }
            });

            query2 = "food";

            unsplash.searchPhotos(query2,5,20,"portrait", new Unsplash.OnSearchCompleteListener() {
                @Override
                public void onComplete(SearchResults results) {
                    Log.d("Photos", "Total Results Found " + results.getTotal());

                    List<Photo> photos = results.getResults();

                    Collections.shuffle(photos);

                    Random random = new Random();
                    int n = random.nextInt(photos.size());


                    LruCache<String, Bitmap> memCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / (1024 * 4))) {
                        @Override
                        protected int sizeOf(String key, Bitmap image) {
                            return image.getByteCount() / 1024;
                        }
                    };
                    binding.food.setShapeAppearanceModel(binding.food.getShapeAppearanceModel()
                            .toBuilder()
                            .setBottomRightCorner(CornerFamily.ROUNDED,150).build());

                    Bitmap image = memCache.get("imagefile");
                    if (image != null) {
                        //Bitmap exists in cache.
                        binding.food.setImageBitmap(image);
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

                                    .into(binding.food);

                            binding.food.setOnClickListener(view -> {
                                Intent intent = new Intent(getActivity(), TestingMotionLayout.class);
                                intent.putExtra("img1", photos.get(n).getUrls().getFull());
                                intent.putExtra("img2", photos.get(n).getUrls().getRegular());
                                intent.putExtra("query", query2);
                                intent.putExtra("text", "Food");

                                Pair[] pairs = new Pair[1];
                                pairs[0] = new Pair<View, String>(  binding.food, "img1");


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
                    Log.d("Unsplash", error);
                }
            });


            query3 = "Landscape";

            unsplash.searchPhotos(query3,2,20,"portrait", new Unsplash.OnSearchCompleteListener() {
                @Override
                public void onComplete(SearchResults results) {
                    Log.d("Photos", "Total Results Found " + results.getTotal());

                    List<Photo> photos = results.getResults();
                    Collections.shuffle(photos);


                    Random random = new Random();
                    int n = random.nextInt(photos.size());

                    LruCache<String, Bitmap> memCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / (1024 * 4))) {
                        @Override
                        protected int sizeOf(String key, Bitmap image) {
                            return image.getByteCount() / 1024;
                        }
                    };

                    Bitmap image = memCache.get("imagefile");
                    if (image != null) {
                        //Bitmap exists in cache.
                        binding.Landscape.setImageBitmap(image);
                    } else {
                        if (getActivity() != null) {
                            Glide.with(getActivity())
                                    .load(photos.get(n).getUrls().getSmall())
                                    .thumbnail(
                                            Glide.with(Objects.requireNonNull(getActivity())).load(photos.get(n).getUrls().getSmall())
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

                                    .into(binding.Landscape);


                            binding.Landscape.setOnClickListener(view -> {
                                Intent intent = new Intent(getActivity(), TestingMotionLayout.class);
                                intent.putExtra("img1", photos.get(n).getUrls().getFull());
                                intent.putExtra("img2", photos.get(n).getUrls().getRegular());
                                intent.putExtra("query", query3);
                                intent.putExtra("text", "Landscape");

                                Pair[] pairs = new Pair[2];
                                pairs[0] = new Pair<View, String>(binding.Landscape, "img1");
                                pairs[1] = new Pair<View, String>(binding.landScape, "text");

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
                    Log.d("Unsplash", error);
                }
            });

            query4 = "Cityscape";
            unsplash.searchPhotos(query4,8,20,"portrait", new Unsplash.OnSearchCompleteListener() {
                @Override
                public void onComplete(SearchResults results) {
                    Log.d("Photos", "Total Results Found " + results.getTotal());

                    List<Photo> photos = results.getResults();

                    Collections.shuffle(photos);

                    Random random = new Random();
                    int n = random.nextInt(photos.size());


                    LruCache<String, Bitmap> memCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / (1024 * 4))) {
                        @Override
                        protected int sizeOf(String key, Bitmap image) {
                            return image.getByteCount() / 1024;
                        }
                    };

                    Bitmap image = memCache.get("imagefile");
                    if (image != null) {
                        //Bitmap exists in cache.
                        binding.Cityscape.setImageBitmap(image);
                    } else {

                        if (getActivity() != null) {
                            Glide.with(getActivity())
                                    .load(photos.get(n).getUrls().getSmall())
                                    .thumbnail(
                                            Glide.with(Objects.requireNonNull(getActivity())).load(photos.get(n).getUrls().getSmall())
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

                                    .into(binding.Cityscape);

                            binding.Cityscape.setOnClickListener(view -> {
                                Intent intent = new Intent(getActivity(), TestingMotionLayout.class);
                                intent.putExtra("img1", photos.get(n).getUrls().getFull());
                                intent.putExtra("img2", photos.get(n).getUrls().getRegular());
                                intent.putExtra("query", query4);
                                intent.putExtra("text", "Cityscape");

                                Pair[] pairs = new Pair[2];
                                pairs[0] = new Pair<View, String>(binding.Cityscape, "img1");
                                pairs[1] = new Pair<View, String>(binding.cityScape, "text");

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
                    Log.d("Unsplash", error);
                }
            });

            query5 = "Seascape";

            unsplash.searchPhotos(query5,9,20,"portrait", new Unsplash.OnSearchCompleteListener() {
                @Override
                public void onComplete(SearchResults results) {
                    Log.d("Photos", "Total Results Found " + results.getTotal());

                    List<Photo> photos = results.getResults();

                    Collections.shuffle(photos);

                    Random random = new Random();
                    int n = random.nextInt(photos.size());


                    LruCache<String, Bitmap> memCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / (1024 * 4))) {
                        @Override
                        protected int sizeOf(String key, Bitmap image) {
                            return image.getByteCount() / 1024;
                        }
                    };

                    Bitmap image = memCache.get("imagefile");
                    if (image != null) {
                        //Bitmap exists in cache.
                        binding.Seascape.setImageBitmap(image);
                    } else {
                        if (getActivity() != null) {
                            Glide.with(getActivity())
                                    .load(photos.get(n).getUrls().getSmall())
                                    .thumbnail(
                                            Glide.with(getActivity()).load(photos.get(n).getUrls().getSmall())
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

                                    .into(binding.Seascape);
                            binding.Seascape.setOnClickListener(view -> {
                                Intent intent = new Intent(getActivity(), TestingMotionLayout.class);
                                intent.putExtra("img1", photos.get(n).getUrls().getFull());
                                intent.putExtra("img2", photos.get(n).getUrls().getRegular());
                                intent.putExtra("query", query5);
                                intent.putExtra("text", "Seascape");

                                Pair[] pairs = new Pair[2];
                                pairs[0] = new Pair<View, String>(binding.Seascape, "img1");
                                pairs[1] = new Pair<View, String>(binding.seaScape, "text");

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


                    //Glide.with(getActivity()).load(photos.get(n).getUrls().getFull()).into(Seascape);

                }

                @Override
                public void onError(String error) {
                    Log.d("Unsplash", error);
                }
            });


            query6 = "Twilight";
            unsplash.searchPhotos(query6,11,20,"portrait", new Unsplash.OnSearchCompleteListener() {
                @Override
                public void onComplete(SearchResults results) {
                    Log.d("Photos", "Total Results Found " + results.getTotal());

                    List<Photo> photos = results.getResults();

                    Collections.shuffle(photos);
                    Random random = new Random();
                    int n = random.nextInt(photos.size());

                    LruCache<String, Bitmap> memCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / (1024 * 4))) {
                        @Override
                        protected int sizeOf(String key, Bitmap image) {
                            return image.getByteCount() / 1024;
                        }
                    };

                    Bitmap image = memCache.get("imagefile");
                    if (image != null) {
                        //Bitmap exists in cache.
                        binding.Twilight.setImageBitmap(image);
                    } else {
                        if (getActivity() != null) {
                            Glide.with(getActivity())
                                    .load(photos.get(n).getUrls().getSmall())
                                    .thumbnail(
                                            Glide.with(Objects.requireNonNull(getActivity())).load(photos.get(n).getUrls().getSmall())
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

                                    .into(binding.Twilight);
                            binding.Twilight.setOnClickListener(view -> {
                                Intent intent = new Intent(getActivity(), TestingMotionLayout.class);
                                intent.putExtra("img1", photos.get(n).getUrls().getFull());
                                intent.putExtra("img2", photos.get(n).getUrls().getRegular());
                                intent.putExtra("query", query6);
                                intent.putExtra("text", "Twilight");

                                Pair[] pairs = new Pair[2];
                                pairs[0] = new Pair<View, String>(binding.Twilight, "img1");
                                pairs[1] = new Pair<View, String>(binding.twiLight, "text");

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


                    // Glide.with(Objects.requireNonNull(getActivity())).load(photos.get(n).getUrls().getFull()).into(Twilight);

                }

                @Override
                public void onError(String error) {
                    Log.d("Unsplash", error);
                }
            });
            query7 = "Food";
            unsplash.searchPhotos(query7,12,20,"portrait", new Unsplash.OnSearchCompleteListener() {
                @Override
                public void onComplete(SearchResults results) {
                    Log.d("Photos", "Total Results Found " + results.getTotal());

                    List<Photo> photos = results.getResults();

                    Collections.shuffle(photos);

                    Random random = new Random();
                    int n = random.nextInt(photos.size());

                    LruCache<String, Bitmap> memCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / (1024 * 4))) {
                        @Override
                        protected int sizeOf(String key, Bitmap image) {
                            return image.getByteCount() / 1024;
                        }
                    };

                    Bitmap image = memCache.get("imagefile");
                    if (image != null) {
                        //Bitmap exists in cache.
                        binding.Food.setImageBitmap(image);
                    } else {
                        if (getActivity() != null) {
                            Glide.with(getActivity())
                                    .load(photos.get(n).getUrls().getSmall())
                                    .thumbnail(
                                            Glide.with(Objects.requireNonNull(getActivity())).load(photos.get(n).getUrls().getSmall())
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

                                    .into(binding.Food);
                            binding.Food.setOnClickListener(view -> {
                                Intent intent = new Intent(getActivity(), TestingMotionLayout.class);
                                intent.putExtra("img1", photos.get(n).getUrls().getFull());
                                intent.putExtra("img2", photos.get(n).getUrls().getRegular());
                                intent.putExtra("query", query7);
                                intent.putExtra("text", "Food");

                                Pair[] pairs = new Pair[2];
                                pairs[0] = new Pair<View, String>(binding.Food, "img1");
                                pairs[1] = new Pair<View, String>(binding.fOOD, "text");

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

                    //   Glide.with(Objects.requireNonNull(getActivity())).load(photos.get(n).getUrls().getFull()).into(Food);

                }

                @Override
                public void onError(String error) {
                    Log.d("Unsplash", error);
                }
            });

            query8 = "Drone View";
            unsplash.searchPhotos(query8, 8,20,"portrait",new Unsplash.OnSearchCompleteListener() {
                @Override
                public void onComplete(SearchResults results) {
                    Log.d("Photos", "Total Results Found " + results.getTotal());

                    List<Photo> photos = results.getResults();
                    Collections.shuffle(photos);

                    Random random = new Random();
                    int n = random.nextInt(photos.size());

                    LruCache<String, Bitmap> memCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / (1024 * 4))) {
                        @Override
                        protected int sizeOf(String key, Bitmap image) {
                            return image.getByteCount() / 1024;
                        }
                    };

                    Bitmap image = memCache.get("imagefile");
                    if (image != null) {
                        //Bitmap exists in cache.
                        binding.DronView.setImageBitmap(image);
                    } else {
                        if (getActivity() != null) {
                            Glide.with(getActivity())
                                    .load(photos.get(n).getUrls().getSmall())
                                    .thumbnail(
                                            Glide.with(Objects.requireNonNull(getActivity())).load(photos.get(n).getUrls().getSmall())
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

                                    .into(binding.DronView);
                            binding.DronView.setOnClickListener(view -> {
                                Intent intent = new Intent(getActivity(), TestingMotionLayout.class);
                                intent.putExtra("img1", photos.get(n).getUrls().getFull());
                                intent.putExtra("img2", photos.get(n).getUrls().getRegular());
                                intent.putExtra("query", query8);
                                intent.putExtra("text", "Drone View");

                                Pair[] pairs = new Pair[2];
                                pairs[0] = new Pair<View, String>(binding.DronView, "img1");
                                pairs[1] = new Pair<View, String>(binding.DroneView991, "text");

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


                    // Glide.with(Objects.requireNonNull(getActivity())).load(photos.get(n).getUrls().getFull()).into(DroneView);

                }

                @Override
                public void onError(String error) {
                    Log.d("Unsplash", error);
                }
            });


        }


        Log.d("Popular", "onCreateView:");

        return view;
    }

    public static Popular newInstance(String text) {
        Popular f = new Popular();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }




}
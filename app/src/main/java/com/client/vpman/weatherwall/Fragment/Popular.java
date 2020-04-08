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
import com.client.vpman.weatherwall.Activity.PopularList;
import com.client.vpman.weatherwall.CustomeUsefullClass.Connectivity;
import com.client.vpman.weatherwall.CustomeUsefullClass.SharedPref1;
import com.client.vpman.weatherwall.CustomeUsefullClass.Utils;
import com.client.vpman.weatherwall.R;
import com.google.android.material.textview.MaterialTextView;
import com.kc.unsplash.Unsplash;
import com.kc.unsplash.models.Photo;
import com.kc.unsplash.models.SearchResults;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mikhaellopez.circularimageview.CircularImageView;

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

RoundedImageView imageView,imageView1,imageView2;
    String query,query1,query2,query3,query4,query5,query6,query7,query8;
    CircularImageView Landscape,Cityscape,Seascape,Twilight,Food,DroneView;


    ImageView SwipeUp;
    Animation bounce;
    SharedPref1 sharedPref1;
    Animation fromtop;
    RelativeLayout relpop;
    MaterialTextView popPhoto1;

    MaterialTextView Landscape1,Cityscape1,Seascape1,Twilight1,Food1,DroneView1;
    RelativeLayout relLandscape;

    private final String CLIENT_ID="fcd5073926c7fdd11b9eb62887dbd6398eafbb8f3c56073035b141ad57d1ab5f";
    private Unsplash unsplash;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_popular, container, false);
        imageView=view.findViewById(R.id.droneView);
        imageView1=view.findViewById(R.id.nature);
        relLandscape=view.findViewById(R.id.relLandscanpe);
        imageView2=view.findViewById(R.id.food);
        Landscape=view.findViewById(R.id.Landscape);
        Cityscape=view.findViewById(R.id.Cityscape);
        Seascape=view.findViewById(R.id.Seascape);
        Twilight=view.findViewById(R.id.Twilight);
        Food=view.findViewById(R.id.Food);
        DroneView=view.findViewById(R.id.DronView);
        SwipeUp=view.findViewById(R.id.SwipUp);
        Landscape1=view.findViewById(R.id.landScape);
        Cityscape1=view.findViewById(R.id.cityScape);
        Seascape1=view.findViewById(R.id.seaScape);
        Twilight1=view.findViewById(R.id.twiLight);
        Food1=view.findViewById(R.id.fOOD);
        relpop=view.findViewById(R.id.relpop);
        popPhoto1=view.findViewById(R.id.popPhoto1);
        DroneView1=view.findViewById(R.id.DroneView991);
        unsplash=new Unsplash(CLIENT_ID);

        fromtop = AnimationUtils.loadAnimation(getActivity(), R.anim.fromtop);


/*        Resources res = getResources(); //resource handle
        Drawable drawable = res.getDrawable(R.drawable.ic_arrow_back_black_24dp); //new Image that was added to the res folder
        backtoMain.setBackground(drawable);*/
        if (getActivity()!=null)
        {
            sharedPref1=new SharedPref1(getActivity());
            if (sharedPref1.getTheme().equals("Light"))
            {
                relpop.setBackgroundColor(Color.parseColor("#FFFFFF"));
                popPhoto1.setTextColor(Color.parseColor("#000000"));
                SwipeUp.setImageResource(R.drawable.ic_up_arow_black);
                relLandscape.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
            else if (sharedPref1.getTheme().equals("Dark"))
            {
                relpop.setBackgroundColor(Color.parseColor("#000000"));
                popPhoto1.setTextColor(Color.parseColor("#FFFFFF"));
                SwipeUp.setImageResource(R.drawable.ic_up_arow);
                relLandscape.setBackgroundColor(Color.parseColor("#000000"));
            }
            else
            {
                relpop.setBackgroundColor(Color.parseColor("#FFFFFF"));
                popPhoto1.setTextColor(Color.parseColor("#000000"));
                SwipeUp.setImageResource(R.drawable.ic_up_arow_black);
                relLandscape.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        }




            bounce= AnimationUtils.loadAnimation(getActivity(),R.anim.bounce);

        bounce.setRepeatCount(Animation.INFINITE);
        bounce.setRepeatMode(Animation.INFINITE);
        bounce.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                bounce= AnimationUtils.loadAnimation(getActivity(),R.anim.bounce);

                bounce.setRepeatCount(Animation.INFINITE);
                bounce.setRepeatMode(Animation.INFINITE);
                SwipeUp.startAnimation(bounce);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        SwipeUp.startAnimation(bounce);

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

        query="drone view";
         String Url="https://api.pexels.com/v1/search?query="+query+"&per_page=150&page=1";


        if (Connectivity.isConnected(getActivity()) && Connectivity.isConnectedMobile(getActivity()) && Connectivity.isConnectedFast(getActivity()) ||
                Connectivity.isConnected(getActivity()) && Connectivity.isConnectedWifi(getActivity()) && Connectivity.isConnectedFast(getActivity()))
        {

            unsplash.searchPhotos(query, 3,20,"portrait",new Unsplash.OnSearchCompleteListener() {
                @Override
                public void onComplete(SearchResults results) {
                    Log.d("Photos", "Total Results Found " + results.getTotal());

                    List<Photo> photos = results.getResults();


                    Random random=new Random();
                    int n = random.nextInt(photos.size());

                    Collections.shuffle(photos);

                    LruCache<String, Bitmap> memCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / (1024 * 4))) {
                        @Override
                        protected int sizeOf(String key, Bitmap image) {
                            return image.getByteCount()/1024;
                        }
                    };
                    Bitmap image = memCache.get("imagefile");
                    if (image != null) {
                        //Bitmap exists in cache.
                        imageView.setImageBitmap(image);
                    } else
                    {
                        if (getActivity()!=null)
                        {
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
                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource)
                                        {

                                            //    spinKitView.setVisibility(View.GONE);

                                            return false;
                                        }
                                    })

                                    .into(imageView);


                            imageView.setOnClickListener(view -> {
                                Intent intent=new Intent(getActivity(), PopularList.class);
                                intent.putExtra("img1",photos.get(n).getUrls().getFull());
                                intent.putExtra("img2",photos.get(n).getUrls().getRegular());
                                intent.putExtra("query",query);
                                intent.putExtra("text","Drone View");

                                Pair[] pairs=new Pair[1];
                                pairs[0]=new Pair<View,String>(imageView,"img1");


                                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                        getActivity(),pairs
                                );

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    startActivity(intent, optionsCompat.toBundle());
                                }else {
                                    startActivity(intent);
                                }});

                        }
                    }






                    // Glide.with(getActivity()).load(photos.get(n).getUrls().getFull()).into(imageView);

                }

                @Override
                public void onError(String error) {
                    Log.d("Unsplash", error);
                }
            });

            query1="nature";


            unsplash.searchPhotos(query1,44,20,"landscape", new Unsplash.OnSearchCompleteListener() {
                @Override
                public void onComplete(SearchResults results) {
                    Log.d("Photos", "Total Results Found " + results.getTotal());

                    List<Photo> photos = results.getResults();
                    Collections.shuffle(photos);

                    Random random=new Random();
                    int n = random.nextInt(photos.size());

                    LruCache<String, Bitmap> memCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / (1024 * 4))) {
                        @Override
                        protected int sizeOf(String key, Bitmap image) {
                            return image.getByteCount()/1024;
                        }
                    };
                    Bitmap image = memCache.get("imagefile");
                    if (image != null) {
                        //Bitmap exists in cache.
                        imageView1.setImageBitmap(image);
                    } else
                    {
                        if (getActivity()!=null)
                        {
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
                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource)
                                        {

                                            //    spinKitView.setVisibility(View.GONE);

                                            return false;
                                        }
                                    })

                                    .into(imageView1);
                            imageView1.setOnClickListener(view -> {
                                Intent intent=new Intent(getActivity(), PopularList.class);
                                intent.putExtra("img1",photos.get(n).getUrls().getFull());
                                intent.putExtra("img2",photos.get(n).getUrls().getRegular());
                                intent.putExtra("query",query1);
                                intent.putExtra("text","Nature");

                                Pair[] pairs=new Pair[1];
                                pairs[0]=new Pair<View,String>(imageView1,"img1");

                               /* Pair<View, String> pair = Pair.create((View)Landscape, ViewCompat.getTransitionName(Landscape));
                                Pair<View, String> pair1 = Pair.create((View)Landscape1, ViewCompat.getTransitionName(Landscape1));*/
                                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                        getActivity(),pairs
                                );

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    startActivity(intent, optionsCompat.toBundle());
                                }else {
                                    startActivity(intent);
                                }});

                        }

                    }




                }

                @Override
                public void onError(String error) {
                    Log.d("Unsplash", error);
                }
            });

            query2="food";

            unsplash.searchPhotos(query2, new Unsplash.OnSearchCompleteListener() {
                @Override
                public void onComplete(SearchResults results) {
                    Log.d("Photos", "Total Results Found " + results.getTotal());

                    List<Photo> photos = results.getResults();

                    Collections.shuffle(photos);

                    Random random=new Random();
                    int n = random.nextInt(photos.size());


                    LruCache<String, Bitmap> memCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / (1024 * 4))) {
                        @Override
                        protected int sizeOf(String key, Bitmap image) {
                            return image.getByteCount()/1024;
                        }
                    };

                    Bitmap image = memCache.get("imagefile");
                    if (image != null) {
                        //Bitmap exists in cache.
                        imageView2.setImageBitmap(image);
                    } else
                    {
                        if (getActivity()!=null)
                        {
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
                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource)
                                        {

                                            //    spinKitView.setVisibility(View.GONE);

                                            return false;
                                        }
                                    })

                                    .into(imageView2);

                            imageView2.setOnClickListener(view -> {
                                Intent intent=new Intent(getActivity(), PopularList.class);
                                intent.putExtra("img1",photos.get(n).getUrls().getFull());
                                intent.putExtra("img2",photos.get(n).getUrls().getRegular());
                                intent.putExtra("query",query2);
                                intent.putExtra("text","Food");

                                Pair[] pairs=new Pair[1];
                                pairs[0]=new Pair<View,String>(imageView2,"img1");


                               /* Pair<View, String> pair = Pair.create((View)Landscape, ViewCompat.getTransitionName(Landscape));
                                Pair<View, String> pair1 = Pair.create((View)Landscape1, ViewCompat.getTransitionName(Landscape1));*/
                                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                        getActivity(),pairs
                                );

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    startActivity(intent, optionsCompat.toBundle());
                                }else {
                                    startActivity(intent);
                                }});

                        }
                    }



                }

                @Override
                public void onError(String error) {
                    Log.d("Unsplash", error);
                }
            });


            query3="Landscape";

            unsplash.searchPhotos(query3, new Unsplash.OnSearchCompleteListener() {
                @Override
                public void onComplete(SearchResults results) {
                    Log.d("Photos", "Total Results Found " + results.getTotal());

                    List<Photo> photos = results.getResults();
                    Collections.shuffle(photos);



                    Random random=new Random();
                    int n = random.nextInt(photos.size());

                    LruCache<String, Bitmap> memCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / (1024 * 4))) {
                        @Override
                        protected int sizeOf(String key, Bitmap image) {
                            return image.getByteCount()/1024;
                        }
                    };

                    Bitmap image = memCache.get("imagefile");
                    if (image != null) {
                        //Bitmap exists in cache.
                        Landscape.setImageBitmap(image);
                    } else
                    {
                        if (getActivity()!=null)
                        {
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
                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource)
                                        {

                                            //    spinKitView.setVisibility(View.GONE);

                                            return false;
                                        }
                                    })

                                    .into(Landscape);


                            Landscape.setOnClickListener(view -> {
                                Intent intent=new Intent(getActivity(), PopularList.class);
                                intent.putExtra("img1",photos.get(n).getUrls().getFull());
                                intent.putExtra("img2",photos.get(n).getUrls().getRegular());
                                intent.putExtra("query",query3);
                                intent.putExtra("text","Landscape");

                                Pair[] pairs=new Pair[2];
                                pairs[0]=new Pair<View,String>(Landscape,"img1");
                                pairs[1]=new Pair<View,String>(Landscape1,"text");

                               /* Pair<View, String> pair = Pair.create((View)Landscape, ViewCompat.getTransitionName(Landscape));
                                Pair<View, String> pair1 = Pair.create((View)Landscape1, ViewCompat.getTransitionName(Landscape1));*/
                                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                        getActivity(),pairs
                                );

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    startActivity(intent, optionsCompat.toBundle());
                                }else {
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

            query4="Cityscape";
            unsplash.searchPhotos(query4, new Unsplash.OnSearchCompleteListener() {
                @Override
                public void onComplete(SearchResults results) {
                    Log.d("Photos", "Total Results Found " + results.getTotal());

                    List<Photo> photos = results.getResults();

                    Collections.shuffle(photos);

                    Random random=new Random();
                    int n = random.nextInt(photos.size());


                    LruCache<String, Bitmap> memCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / (1024 * 4))) {
                        @Override
                        protected int sizeOf(String key, Bitmap image) {
                            return image.getByteCount()/1024;
                        }
                    };

                    Bitmap image = memCache.get("imagefile");
                    if (image != null) {
                        //Bitmap exists in cache.
                        Cityscape.setImageBitmap(image);
                    } else {

                        if (getActivity()!=null)
                        {
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
                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource)
                                        {

                                            //    spinKitView.setVisibility(View.GONE);

                                            return false;
                                        }
                                    })

                                    .into(Cityscape);

                            Cityscape.setOnClickListener(view -> {
                                Intent intent=new Intent(getActivity(), PopularList.class);
                                intent.putExtra("img1",photos.get(n).getUrls().getFull());
                                intent.putExtra("img2",photos.get(n).getUrls().getRegular());
                                intent.putExtra("query",query4);
                                intent.putExtra("text","Cityscape");

                                Pair[] pairs=new Pair[2];
                                pairs[0]=new Pair<View,String>(Cityscape,"img1");
                                pairs[1]=new Pair<View,String>(Cityscape1,"text");

                               /* Pair<View, String> pair = Pair.create((View)Landscape, ViewCompat.getTransitionName(Landscape));
                                Pair<View, String> pair1 = Pair.create((View)Landscape1, ViewCompat.getTransitionName(Landscape1));*/
                                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                        getActivity(),pairs
                                );

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    startActivity(intent, optionsCompat.toBundle());
                                }else {
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

            query5="Seascape";

            unsplash.searchPhotos(query5, new Unsplash.OnSearchCompleteListener() {
                @Override
                public void onComplete(SearchResults results) {
                    Log.d("Photos", "Total Results Found " + results.getTotal());

                    List<Photo> photos = results.getResults();

                    Collections.shuffle(photos);

                    Random random=new Random();
                    int n = random.nextInt(photos.size());


                    LruCache<String, Bitmap> memCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / (1024 * 4))) {
                        @Override
                        protected int sizeOf(String key, Bitmap image) {
                            return image.getByteCount()/1024;
                        }
                    };

                    Bitmap image = memCache.get("imagefile");
                    if (image != null) {
                        //Bitmap exists in cache.
                        Seascape.setImageBitmap(image);
                    } else {
                        if (getActivity()!=null)
                        {
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
                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource)
                                        {

                                            //    spinKitView.setVisibility(View.GONE);

                                            return false;
                                        }
                                    })

                                    .into(Seascape);
                        Seascape.setOnClickListener(view -> {
                            Intent intent=new Intent(getActivity(), PopularList.class);
                            intent.putExtra("img1",photos.get(n).getUrls().getFull());
                            intent.putExtra("img2",photos.get(n).getUrls().getRegular());
                            intent.putExtra("query",query5);
                            intent.putExtra("text","Seascape");

                            Pair[] pairs=new Pair[2];
                            pairs[0]=new Pair<View,String>(Seascape,"img1");
                            pairs[1]=new Pair<View,String>(Seascape1,"text");

                               /* Pair<View, String> pair = Pair.create((View)Landscape, ViewCompat.getTransitionName(Landscape));
                                Pair<View, String> pair1 = Pair.create((View)Landscape1, ViewCompat.getTransitionName(Landscape1));*/
                            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                    getActivity(),pairs
                            );

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                startActivity(intent, optionsCompat.toBundle());
                            }else {
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



            query6="Twilight";
            unsplash.searchPhotos(query6, new Unsplash.OnSearchCompleteListener() {
                @Override
                public void onComplete(SearchResults results) {
                    Log.d("Photos", "Total Results Found " + results.getTotal());

                    List<Photo> photos = results.getResults();

                    Collections.shuffle(photos);
                    Random random=new Random();
                    int n = random.nextInt(photos.size());

                    LruCache<String, Bitmap> memCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / (1024 * 4))) {
                        @Override
                        protected int sizeOf(String key, Bitmap image) {
                            return image.getByteCount()/1024;
                        }
                    };

                    Bitmap image = memCache.get("imagefile");
                    if (image != null) {
                        //Bitmap exists in cache.
                        Twilight.setImageBitmap(image);
                    } else {
                        if (getActivity()!=null)
                        {
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
                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource)
                                        {

                                            //    spinKitView.setVisibility(View.GONE);

                                            return false;
                                        }
                                    })

                                    .into(Twilight);
                            Twilight.setOnClickListener(view -> {
                                Intent intent=new Intent(getActivity(), PopularList.class);
                                intent.putExtra("img1",photos.get(n).getUrls().getFull());
                                intent.putExtra("img2",photos.get(n).getUrls().getRegular());
                                intent.putExtra("query",query6);
                                intent.putExtra("text","Twilight");

                                Pair[] pairs=new Pair[2];
                                pairs[0]=new Pair<View,String>(Twilight,"img1");
                                pairs[1]=new Pair<View,String>(Twilight1,"text");

                               /* Pair<View, String> pair = Pair.create((View)Landscape, ViewCompat.getTransitionName(Landscape));
                                Pair<View, String> pair1 = Pair.create((View)Landscape1, ViewCompat.getTransitionName(Landscape1));*/
                                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                        getActivity(),pairs
                                );

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    startActivity(intent, optionsCompat.toBundle());
                                }else {
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
            query7="Food";
            unsplash.searchPhotos(query7, new Unsplash.OnSearchCompleteListener() {
                @Override
                public void onComplete(SearchResults results) {
                    Log.d("Photos", "Total Results Found " + results.getTotal());

                    List<Photo> photos = results.getResults();

                    Collections.shuffle(photos);

                    Random random=new Random();
                    int n = random.nextInt(photos.size());

                    LruCache<String, Bitmap> memCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / (1024 * 4))) {
                        @Override
                        protected int sizeOf(String key, Bitmap image) {
                            return image.getByteCount()/1024;
                        }
                    };

                    Bitmap image = memCache.get("imagefile");
                    if (image != null) {
                        //Bitmap exists in cache.
                        Food.setImageBitmap(image);
                    } else {
                        if (getActivity()!=null)
                        {
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
                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource)
                                        {

                                            //    spinKitView.setVisibility(View.GONE);

                                            return false;
                                        }
                                    })

                                    .into(Food);
                            Food.setOnClickListener(view -> {
                                Intent intent=new Intent(getActivity(), PopularList.class);
                                intent.putExtra("img1",photos.get(n).getUrls().getFull());
                                intent.putExtra("img2",photos.get(n).getUrls().getRegular());
                                intent.putExtra("query",query7);
                                intent.putExtra("text","Food");

                                Pair[] pairs=new Pair[2];
                                pairs[0]=new Pair<View,String>(Food,"img1");
                                pairs[1]=new Pair<View,String>(Food1,"text");

                               /* Pair<View, String> pair = Pair.create((View)Landscape, ViewCompat.getTransitionName(Landscape));
                                Pair<View, String> pair1 = Pair.create((View)Landscape1, ViewCompat.getTransitionName(Landscape1));*/
                                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                        getActivity(),pairs
                                );

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    startActivity(intent, optionsCompat.toBundle());
                                }else {
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

            query8="Drone View";
            unsplash.searchPhotos(query8, new Unsplash.OnSearchCompleteListener() {
                @Override
                public void onComplete(SearchResults results) {
                    Log.d("Photos", "Total Results Found " + results.getTotal());

                    List<Photo> photos = results.getResults();
                    Collections.shuffle(photos);

                    Random random=new Random();
                    int n = random.nextInt(photos.size());

                    LruCache<String, Bitmap> memCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / (1024 * 4))) {
                        @Override
                        protected int sizeOf(String key, Bitmap image) {
                            return image.getByteCount()/1024;
                        }
                    };

                    Bitmap image = memCache.get("imagefile");
                    if (image != null) {
                        //Bitmap exists in cache.
                        DroneView.setImageBitmap(image);
                    } else
                    {
                        if (getActivity()!=null)
                        {
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
                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource)
                                        {

                                            //    spinKitView.setVisibility(View.GONE);

                                            return false;
                                        }
                                    })

                                    .into(DroneView);
                            DroneView.setOnClickListener(view -> {
                                Intent intent=new Intent(getActivity(), PopularList.class);
                                intent.putExtra("img1",photos.get(n).getUrls().getFull());
                                intent.putExtra("img2",photos.get(n).getUrls().getRegular());
                                intent.putExtra("query",query8);
                                intent.putExtra("text","Drone View");

                                Pair[] pairs=new Pair[2];
                                pairs[0]=new Pair<View,String>(DroneView,"img1");
                                pairs[1]=new Pair<View,String>(DroneView1,"text");

                               /* Pair<View, String> pair = Pair.create((View)Landscape, ViewCompat.getTransitionName(Landscape));
                                Pair<View, String> pair1 = Pair.create((View)Landscape1, ViewCompat.getTransitionName(Landscape1));*/
                                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                        getActivity(),pairs
                                );

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    startActivity(intent, optionsCompat.toBundle());
                                }else {
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
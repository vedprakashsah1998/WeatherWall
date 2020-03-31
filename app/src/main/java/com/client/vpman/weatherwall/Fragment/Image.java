package com.client.vpman.weatherwall.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;


import android.util.Log;
import android.util.LruCache;
import android.view.Display;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.ObjectKey;
import com.client.vpman.weatherwall.Activity.ExploreQuotesAndPhoto;
import com.client.vpman.weatherwall.CustomeDesignViewPager.VerticalViewPageAdapter;
import com.client.vpman.weatherwall.CustomeUsefullClass.Utils;
import com.client.vpman.weatherwall.R;
import com.kc.unsplash.Unsplash;
import com.kc.unsplash.models.Photo;
import com.kc.unsplash.models.SearchResults;


import java.util.List;

import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class Image extends Fragment implements GestureDetector.OnGestureListener
{

View view;
    public Image() {
        // Required empty public constructor
    }



    ImageView imageView;
    String query;

    VerticalViewPageAdapter verticalViewPageAdapter;

    private final String CLIENT_ID="fcd5073926c7fdd11b9eb62887dbd6398eafbb8f3c56073035b141ad57d1ab5f";
    private Unsplash unsplash;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_image, container, false);

        imageView=view.findViewById(R.id.NatureUn);
        unsplash=new Unsplash(CLIENT_ID);

         verticalViewPageAdapter=getActivity().findViewById(R.id.pager);




        RequestOptions requestOptions = new RequestOptions();
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

        query="Nature";



        unsplash.searchPhotos(query, new Unsplash.OnSearchCompleteListener() {
            @Override
            public void onComplete(SearchResults results) {
                Log.d("Photos", "Total Results Found " + results.getTotal());

                List<Photo> photos = results.getResults();


                Random random=new Random();
                int n = random.nextInt(photos.size());

                if (isAdded())
                {

                    LruCache<String, Bitmap> memCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / (1024 * 4))) {
                        @Override
                        protected int sizeOf(String key, Bitmap image) {
                            return image.getByteCount()/1024;
                        }
                    };
                    Display display = getActivity().getWindowManager().getDefaultDisplay();
                    Point size = new Point();
                    display.getSize(size);
                    int width = size.x; //width of screen in pixels
                    int height = size.y;
                    Bitmap image = memCache.get("imagefile");
                    if (image != null) {
                        //Bitmap exists in cache.
                        imageView.setImageBitmap(image);
                    } else
                    {
                        Glide.with(getContext())
                                .load(photos.get(n).getUrls().getFull())
                                .thumbnail(
                                        Glide.with(getActivity()).load(photos.get(n).getUrls().getRegular())
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






                                        return false;
                                    }
                                })

                                .into(imageView);
                        imageView.setOnClickListener(v -> {
                            Intent intent=new Intent(getActivity(), ExploreQuotesAndPhoto.class);
                            intent.putExtra("imgData",photos.get(n).getUrls().getFull());
                            intent.putExtra("imgDataSmall",photos.get(n).getUrls().getRegular());
                            intent.putExtra("query",query);
                            intent.putExtra("text","Nature");

                            Pair[] pairs=new Pair[1];
                            pairs[0]=new Pair<View,String>(imageView,"imgData");


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



        imageView.setTranslationZ(40);




        return view;
    }



    public static Image newInstance(String text) {
        Image f = new Image();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }


    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        switch (getSlope(e1.getX(), e1.getY(), e2.getX(), e2.getY())) {
            case 1:
                Log.d("Top", "top");
                verticalViewPageAdapter.setCurrentItem(3);
                return true;
            case 2:
                Log.d("Left", "left");
                return true;
            case 3:
                Log.d("Down", "down");
                verticalViewPageAdapter.setCurrentItem(1);
                return true;
            case 4:
                Log.d("Right", "right");
                return true;
        }
        return false;
    }

    private int getSlope(float x1, float y1, float x2, float y2) {
        Double angle = Math.toDegrees(Math.atan2(y1 - y2, x2 - x1));
        if (angle > 45 && angle <= 135)
            // top
            return 1;
        if (angle >= 135 && angle < 180 || angle < -135 && angle > -180)
            // left
            return 2;
        if (angle < -45 && angle>= -135)
            // down
            return 3;
        if (angle > -45 && angle <= 45)
            // right
            return 4;
        return 0;

    }
}

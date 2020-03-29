package com.client.vpman.weatherwall.Fragment;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.client.vpman.weatherwall.Adapter.DemoFragmentStateAdapter1;
import com.client.vpman.weatherwall.CustomeDesignViewPager.DepthTransformation;
import com.client.vpman.weatherwall.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Explore extends Fragment {

    private DemoFragmentStateAdapter1 adapter;

    View view;
    ViewPager mViewPager;

    ImageView SwipeUp,left,right;
    Animation bounce;

    public Explore() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_explore, container, false);

        mViewPager = view.findViewById(R.id.pager1);
        SwipeUp=view.findViewById(R.id.SwipUp009);
        left=view.findViewById(R.id.left);
        right=view.findViewById(R.id.right);
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

        DepthTransformation depthTransformation = new DepthTransformation();

        adapter = new DemoFragmentStateAdapter1(getChildFragmentManager());
        mViewPager.setPageTransformer(true,depthTransformation);
        mViewPager.setAdapter(adapter);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (position==0)
                {
                    mViewPager.setCurrentItem(0);
                    /*left.setVisibility(View.GONE);*/
                }
                else if(position==11)
                {
                    right.setVisibility(View.GONE);
                    mViewPager.setCurrentItem(11);
                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



    return view;
    }
    public static Explore newInstance(String text) {
        Explore f = new Explore();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }



}

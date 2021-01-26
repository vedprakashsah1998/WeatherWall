package com.client.vpman.weatherwall.ui.Fragment;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.client.vpman.weatherwall.Adapter.DemoFragmentStateAdapter1;
import com.client.vpman.weatherwall.CustomeDesignViewPager.DepthTransform;
import com.client.vpman.weatherwall.CustomeUsefullClass.SharedPref1;
import com.client.vpman.weatherwall.R;
import com.client.vpman.weatherwall.databinding.FragmentExploreBinding;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 */
public class Explore extends Fragment {

    private Animation bounce;
    private FragmentExploreBinding binding;

    public Explore() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentExploreBinding.inflate(inflater, container, false);

        View view = binding.getRoot();


        if (getActivity() != null) {
            SharedPref1 sharedPref1 = new SharedPref1(getActivity());
            if (sharedPref1.getTheme().equals("Light")) {
                binding.relexp.setBackgroundColor(Color.parseColor("#FFFFFF"));
                binding.exploreId.setTextColor(Color.parseColor("#000000"));
                binding.right.setImageResource(R.drawable.ic_right);
            } else if (sharedPref1.getTheme().equals("Dark")) {
                binding.relexp.setBackgroundColor(Color.parseColor("#000000"));
                binding.exploreId.setTextColor(Color.parseColor("#FFFFFF"));
                binding.right.setImageResource(R.drawable.ic_right_white);
            } else {
                switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                    case Configuration.UI_MODE_NIGHT_YES:
                        binding.relexp.setBackgroundColor(Color.parseColor("#000000"));
                        binding.exploreId.setTextColor(Color.parseColor("#FFFFFF"));
                        binding.right.setImageResource(R.drawable.ic_right_white);

                        break;
                    case Configuration.UI_MODE_NIGHT_NO:
                        binding.relexp.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        binding.exploreId.setTextColor(Color.parseColor("#000000"));
                        binding.right.setImageResource(R.drawable.ic_right);
                        break;
                }

            }
        }

        DepthTransform depthTransformation = new DepthTransform();

        DemoFragmentStateAdapter1 adapter = new DemoFragmentStateAdapter1(getChildFragmentManager());
        binding.pager1.setPageTransformer(true, depthTransformation);
        binding.pager1.setAdapter(adapter);

        binding.pager1.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (position == 0) {
                    binding.pager1.setCurrentItem(0);
                    /*left.setVisibility(View.GONE);*/
                } else if (position == 11) {
                    binding.right.setVisibility(View.GONE);
                    binding.pager1.setCurrentItem(11);
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

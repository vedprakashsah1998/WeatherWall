package com.client.vpman.weatherwall.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.client.vpman.weatherwall.CustomeUsefullClass.Constant;
import com.client.vpman.weatherwall.ui.Fragment.Image;


public class DemoFragmentStateAdapter1 extends FragmentPagerAdapter {
    public DemoFragmentStateAdapter1(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return Image.newInstance("Image, Instance 1", Constant.Nature);
            case 1:
                return Image.newInstance("Image1, Instance1", Constant.Building);
            case 2:
                return Image.newInstance("Sparkles, Instance1", Constant.Car);

            case 3:
                return Image.newInstance("Sparkles, Instance1", Constant.dark);

            case 4:
                return Image.newInstance("Buildings, Instance1", Constant.Bokeh);
            case 5:
                return Image.newInstance("Amoled, Instance1", Constant.minimal);
            case 6:
                return Image.newInstance("Minimal, Instance1", Constant.mountain);
            case 7:
                return Image.newInstance("Religion, Instance1", Constant.religion);

            case 8:
                return Image.newInstance("Religion, Instance1", Constant.Sparkles);
            case 9:
                return Image.newInstance("Dark, Instance1", Constant.star);
            case 10:
                return Image.newInstance("Mountain, Instance1", Constant.technology);
            case 11:
                return Image.newInstance("Star, Instance1", Constant.Luxury);

            default:
                return Image.newInstance("Image, Instance 1", Constant.Nature);
        }
    }

    @Override
    public int getCount() {
        return 12;
    }
}

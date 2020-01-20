package com.client.vpman.weatherwall.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.client.vpman.weatherwall.Fragment.Amoled;
import com.client.vpman.weatherwall.Fragment.Buildings;
import com.client.vpman.weatherwall.Fragment.Car;
import com.client.vpman.weatherwall.Fragment.Dark;
import com.client.vpman.weatherwall.Fragment.Explore;
import com.client.vpman.weatherwall.Fragment.Image;
import com.client.vpman.weatherwall.Fragment.Image1;
import com.client.vpman.weatherwall.Fragment.Minimal;
import com.client.vpman.weatherwall.Fragment.Mountain;
import com.client.vpman.weatherwall.Fragment.Popular;
import com.client.vpman.weatherwall.Fragment.Religion;
import com.client.vpman.weatherwall.Fragment.Sparkles;
import com.client.vpman.weatherwall.Fragment.Star;
import com.client.vpman.weatherwall.Fragment.Technology;
import com.client.vpman.weatherwall.Fragment.WeatherFragment;

public class DemoFragmentStateAdapter1 extends FragmentPagerAdapter
{
    public DemoFragmentStateAdapter1(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return Image.newInstance("Image, Instance 1");
            case 1:
              return  Image1.newInstance("Image1, Instance1");
            case 2:
                    return Sparkles.newInstance("Sparkles, Instance1");

            case 3:
                return Car.newInstance("Sparkles, Instance1");


            case 4:
                return Buildings.newInstance("Buildings, Instance1");
            case 5:
                return Amoled.newInstance("Amoled, Instance1");
            case 6:
                return Minimal.newInstance("Minimal, Instance1");
            case 7:
                return Religion.newInstance("Religion, Instance1");


            case 8:
                return Technology.newInstance("Religion, Instance1");
            case 9:
                return Dark.newInstance("Dark, Instance1");
            case 10:
                return Mountain.newInstance("Mountain, Instance1");
            case 11:
                return Star.newInstance("Star, Instance1");

            default:
                return Image.newInstance("Image, Instance 1");
        }
    }

    @Override
    public int getCount() {
        return 12;
    }
}

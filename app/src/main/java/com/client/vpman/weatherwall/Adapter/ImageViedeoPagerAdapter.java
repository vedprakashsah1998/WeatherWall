package com.client.vpman.weatherwall.Adapter;
import com.client.vpman.weatherwall.Fragment.ImageList;
import com.client.vpman.weatherwall.Fragment.VideoList;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ImageViedeoPagerAdapter extends FragmentStatePagerAdapter
{
    public ImageViedeoPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return ImageList.newInstance("ImageList, Instance 1");
            case 1:
              return  VideoList.newInstance("VideoList, Instance1");

/*            default:
                return ImageList.newInstance("ImageList, Instance 1");*/
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}

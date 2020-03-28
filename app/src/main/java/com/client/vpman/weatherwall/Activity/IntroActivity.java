package com.client.vpman.weatherwall.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.client.vpman.weatherwall.Adapter.CustomePagerAdapter;
import com.client.vpman.weatherwall.CustomeUsefullClass.OnBoardingModel;
import com.client.vpman.weatherwall.CustomeUsefullClass.SharedPref1;
import com.client.vpman.weatherwall.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {

    private CustomePagerAdapter customePagerAdapter;
    private LinearLayout layoutOnBoardingIndicator;
    private MaterialButton onBoardingButton;
    private SharedPref1 pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        pref=new SharedPref1(this);
        pref.setFirstState(false);
        layoutOnBoardingIndicator=findViewById(R.id.onBoardingIndicator);
        onBoardingButton=findViewById(R.id.buttonOnBoardingAction);
        setupOnBoardingItem();
        ViewPager2 viewPager2=findViewById(R.id.onBoardingViewPager);
        viewPager2.setAdapter(customePagerAdapter);
        setupOnBoardingIndicator();

        setCurrentBoardingIndicator(0);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentBoardingIndicator(position);
            }
        });

        onBoardingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager2.getCurrentItem()+1<customePagerAdapter.getItemCount())
                {
                    viewPager2.setCurrentItem(viewPager2.getCurrentItem()+1);
                }
                else
                {
                    startActivity(new Intent(IntroActivity.this,MainActivity.class));
                    finish();
                }
            }
        });
    }

    private void setupOnBoardingItem()
    {
        List<OnBoardingModel>onBoardingModels=new ArrayList<>();
        OnBoardingModel onBoardingModel=new OnBoardingModel();
        onBoardingModel.setTitle("Weather Forecast");
        onBoardingModel.setDescription("A small information about Weather of your current Location");
        onBoardingModel.setImage(R.drawable.ic_undraw_weather_app_i5sm);

        OnBoardingModel quotesItem=new OnBoardingModel();
        quotesItem.setTitle("Quotes for inspiration");
        quotesItem.setDescription("Quotes that inspire you and motivate you for your life");
        quotesItem.setImage(R.drawable.ic_undraw_social_bio_8pql);

        OnBoardingModel wallpaper=new OnBoardingModel();
        wallpaper.setTitle("Beautiful images");
        wallpaper.setDescription("Amazing images for your screen wallpaper");
        wallpaper.setImage(R.drawable.ic_undraw_inspiration_lecl);
        onBoardingModels.add(onBoardingModel);
        onBoardingModels.add(quotesItem);
        onBoardingModels.add(wallpaper);
        customePagerAdapter=new CustomePagerAdapter(onBoardingModels);
    }
    private void setupOnBoardingIndicator()
    {
        ImageView[] indicator=new ImageView[customePagerAdapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(8,0,8,0);
        for(int i=0;i<indicator.length;i++)
        {
            indicator[i]=new ImageView(getApplicationContext());
            indicator[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.onboarding_inactive));
            indicator[i].setLayoutParams(layoutParams);
            layoutOnBoardingIndicator.addView(indicator[i]);

        }
    }
    private void setCurrentBoardingIndicator(int index)
    {
        int childCount=layoutOnBoardingIndicator.getChildCount();
        for (int i=0;i<childCount;i++)
        {
            ImageView imageView=(ImageView)layoutOnBoardingIndicator.getChildAt(i);
            if (i==index)
            {
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.onborard_indicator));
            }
            else
            {
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.onboarding_inactive));

            }
        }
        if (index==customePagerAdapter.getItemCount()-1)
        {
            onBoardingButton.setText("Start");
        }
        else
        {
            onBoardingButton.setText("Next");
        }
    }

}

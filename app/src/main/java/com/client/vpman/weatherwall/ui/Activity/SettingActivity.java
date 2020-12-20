package com.client.vpman.weatherwall.ui.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.client.vpman.weatherwall.CustomeUsefullClass.SharedPref1;
import com.client.vpman.weatherwall.R;
import com.client.vpman.weatherwall.databinding.ActivitySettingBinding;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.Task;

import java.io.File;

public class SettingActivity extends AppCompatActivity {

    ActivitySettingBinding binding;
    SharedPref1 pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ClickAction();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(SettingActivity.this, R.layout.custome_spinner, getResources().getStringArray(R.array.list));

        dataAdapter.setDropDownViewResource(R.layout.custome_spinner_dropdown);
        pref = new SharedPref1(SettingActivity.this);
        binding.chooseImgQuality.append(pref.getImageQuality());

        // attaching data adapter to spinner
        binding.spinner.setAdapter(dataAdapter);
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();


                if (position != 0) {
                    pref.setImageQuality(item);
                    binding.chooseImgQuality.setText("Current Quality :\n" + item);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<>(SettingActivity.this, R.layout.custome_spinner_list, getResources().getStringArray(R.array.list));
        dataAdapter1.setDropDownViewResource(R.layout.custome_spinner_load);

        binding.loadQuality.append(pref.getImageLoadQuality());
        binding.spinner2.setAdapter(dataAdapter1);
        binding.spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();


                if (position != 0) {
                    pref.setImageLoadQuality(item);
                    binding.loadQuality.setText("Load image Quality :\n" + item);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (pref.getTheme().equals("Light")) {
            binding.stickySwitch1.setChecked(false);
        } else if (pref.getTheme().equals("Dark")) {
            binding.stickySwitch1.setChecked(true);
        } else {
            binding.stickySwitch1.setChecked(false);

        }

        SetTheme();

    }

    public void ClickAction() {
        binding.deleteTextMain.setOnClickListener(v -> deleteCache(SettingActivity.this));
        binding.deleteImg.setOnClickListener(v -> deleteCache(SettingActivity.this));
        binding.shareApp.setOnClickListener(v -> {
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Weather Wall");
                String shareMessage = "\nDownload this application from PlayStore\n\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=com.client.vpman.weatherwall";
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Weather Wall" + shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        binding.shareAppText.setOnClickListener(v -> {
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Weather Wall");
                String shareMessage = "\nDownload this application from PlayStore\n\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=com.client.vpman.weatherwall";
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Weather Wall" + shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        binding.reportText.setOnClickListener(v -> {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", "vp.mannu.kr@gmail.com", null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Weather Wall");
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
        });
        binding.reportUs.setOnClickListener(v -> {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", "vp.mannu.kr@gmail.com", null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Weather Wall");
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
        });

        binding.rateUsText.setOnClickListener(v -> {
            ReviewManager manager = ReviewManagerFactory.create(SettingActivity.this);
            Task<ReviewInfo> request = manager.requestReviewFlow();
            request.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // We can get the ReviewInfo object
                    ReviewInfo reviewInfo = task.getResult();
                    Task<Void> flow = manager.launchReviewFlow(SettingActivity.this, reviewInfo);
                    flow.addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            Toast.makeText(SettingActivity.this, "Review Done", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SettingActivity.this, "NOT Done", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    // There was some problem, continue regardless of the result.
                    Toast.makeText(SettingActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();
                }
            });

        });
        binding.rateUsImg.setOnClickListener(v -> {
            ReviewManager manager = ReviewManagerFactory.create(SettingActivity.this);
            Task<ReviewInfo> request = manager.requestReviewFlow();
            request.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // We can get the ReviewInfo object
                    ReviewInfo reviewInfo = task.getResult();
                    Task<Void> flow = manager.launchReviewFlow(SettingActivity.this, reviewInfo);
                    flow.addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            Toast.makeText(SettingActivity.this, "Review Done", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SettingActivity.this, "NOT Done", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    // There was some problem, continue regardless of the result.
                    Toast.makeText(SettingActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();
                }
            });

        });

        binding.instagramText.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.client.vpman.weatherwall"));
            startActivity(browserIntent);

        });

        binding.instagram.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.client.vpman.weatherwall"));
            startActivity(browserIntent);

        });

        binding.faceBookText.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/Weather-Wall-104577191240236/"));
            startActivity(browserIntent);
        });

        binding.facebook.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/Weather-Wall-104577191240236/"));
            startActivity(browserIntent);
        });

        binding.LinkedIn.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/vedprakash1998/"));
            startActivity(browserIntent);
        });

        binding.linkedIn.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/vedprakash1998/"));
            startActivity(browserIntent);
        });

        binding.github.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Vedprakash12/WeatherWall"));
            startActivity(browserIntent);
        });

        binding.Github.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Vedprakash12/WeatherWall"));
            startActivity(browserIntent);
        });

        binding.pexels.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.pexels.com/"));
            startActivity(browserIntent);
        });

        binding.flatIcon.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.flaticon.com/"));
            startActivity(browserIntent);
        });

        binding.Unsplash.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://unsplash.com/"));
            startActivity(browserIntent);
        });

        binding.privacyPolicy.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://weather-wall.flycricket.io/privacy.html"));
            startActivity(browserIntent);
        });

        binding.privacyImg.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://weather-wall.flycricket.io/privacy.html"));
            startActivity(browserIntent);
        });
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
            Toast.makeText(context, "Cache Memory is deleted", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            if (children != null) {
                for (String child : children) {
                    boolean success = deleteDir(new File(dir, child));
                    if (!success) {
                        return false;
                    }
                }
            }

            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    public void SetTheme() {
        binding.stickySwitch1.setOnCheckedChangeListener((view, isChecked) -> {
            if (!isChecked) {
                pref.setTheme("Light");
                binding.themeColor.setBackgroundColor(Color.parseColor("#F2F6F9"));
                binding.tool1barSetting.setBackgroundColor(Color.parseColor("#FFFFFF"));
                binding.deleteTextMain.setTextColor(Color.parseColor("#000000"));
                binding.setTheme.setTextColor(Color.parseColor("#000000"));
                binding.weatherWallText.setTextColor(Color.parseColor("#000000"));
                binding.poweredby.setTextColor(Color.parseColor("#000000"));
                binding.otherCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                binding.setTheme.setText("Light Mode");
                binding.toolBartext.setTextColor(Color.parseColor("#000000"));
                binding.privacyImg.setImageResource(R.drawable.ic_security_black_24dp);
                Resources res = getResources(); //resource handle
                Drawable drawable = res.getDrawable(R.drawable.ic_arrow_back); //new Image that was added to the res folder
                binding.backtoMain.setBackground(drawable);
                binding.loadQuality.setTextColor(Color.parseColor("#000000"));
                binding.deleteImg.setImageResource(R.drawable.ic_delete_black_24dp);
                binding.chooseImgQuality.setTextColor(Color.parseColor("#000000"));
                binding.settingCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                binding.cardSetting.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                binding.shareApp.setImageResource(R.drawable.ic_share_black_24dp);
                binding.reportUs.setImageResource(R.drawable.ic_report_problem);
                binding.rateUsImg.setImageResource(R.drawable.ic_rate_review);
                binding.ContactUsText.setTextColor(Color.parseColor("#000000"));
                binding.cardContact.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                binding.instagram.setImageResource(R.drawable.ic_instagram);
                binding.facebook.setImageResource(R.drawable.ic_facebook);
                binding.instagramText.setTextColor(Color.parseColor("#000000"));
                binding.faceBookText.setTextColor(Color.parseColor("#000000"));
                binding.linkedIn.setImageResource(R.drawable.ic_linkedin);
                binding.rateUsText.setTextColor(Color.parseColor("#000000"));
                binding.Github.setImageResource(R.drawable.ic_logo);
                binding.credit.setTextColor(Color.parseColor("#000000"));
                binding.cardCredit.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                binding.flatIcon.setImageResource(R.drawable.ic_flaticon);
                binding.Unsplash.setImageResource(R.drawable.ic_unsplash);
                binding.pexels.setImageResource(R.drawable.pexels);
                binding.shareAppText.setTextColor(Color.parseColor("#000000"));
                binding.privacyPolicy.setTextColor(Color.parseColor("#000000"));
                binding.reportText.setTextColor(Color.parseColor("#000000"));
                binding.github.setTextColor(Color.parseColor("#000000"));
                binding.LinkedIn.setTextColor(Color.parseColor("#000000"));
            } else {
                pref.setTheme("Dark");
                binding.weatherWallText.setTextColor(Color.parseColor("#FFFFFF"));
                binding.poweredby.setTextColor(Color.parseColor("#FFFFFF"));
                binding.LinkedIn.setTextColor(Color.parseColor("#FFFFFF"));
                binding.github.setTextColor(Color.parseColor("#FFFFFF"));
                binding.cardSetting.setCardBackgroundColor(Color.parseColor("#1A1A1A"));
                binding.otherCard.setCardBackgroundColor(Color.parseColor("#1A1A1A"));
                binding.setTheme.setText("Dark Mode");
                binding.faceBookText.setTextColor(Color.parseColor("#FFFFFF"));
                binding.instagramText.setTextColor(Color.parseColor("#FFFFFF"));
                binding.rateUsText.setTextColor(Color.parseColor("#FFFFFF"));
                binding.reportText.setTextColor(Color.parseColor("#FFFFFF"));
                binding.privacyImg.setImageResource(R.drawable.ic_security_black_24dp_white);
                binding.themeColor.setBackgroundColor(Color.parseColor("#000000"));
                binding.chooseImgQuality.setTextColor(Color.parseColor("#FFFFFF"));
                binding.tool1barSetting.setBackgroundColor(Color.parseColor("#1A1A1A"));
                binding.loadQuality.setTextColor(Color.parseColor("#FFFFFF"));
                binding.setTheme.setTextColor(Color.parseColor("#FFFFFF"));
                binding.toolBartext.setTextColor(Color.parseColor("#FFFFFF"));
                binding.deleteTextMain.setTextColor(Color.parseColor("#FFFFFF"));
                Resources res = getResources(); //resource handle
                Drawable drawable = res.getDrawable(R.drawable.ic_arrow_back_black_24dp); //new Image that was added to the res folder
                binding.backtoMain.setBackground(drawable);
                binding.ContactUsText.setTextColor(Color.parseColor("#FFFFFF"));
                binding.deleteImg.setImageResource(R.drawable.ic_delete_white_24dp);
                binding.settingCard.setCardBackgroundColor(Color.parseColor("#1A1A1A"));
                binding.rateUsImg.setImageResource(R.drawable.ic_rate_review_white);
                binding.shareApp.setImageResource(R.drawable.ic_share_white_24dp);
                binding.reportUs.setImageResource(R.drawable.ic_report_problem_white);
                binding.cardContact.setCardBackgroundColor(Color.parseColor("#1A1A1A"));
                binding.instagram.setImageResource(R.drawable.ic_instagram_white);
                binding.facebook.setImageResource(R.drawable.ic_facebook_white);
                binding.linkedIn.setImageResource(R.drawable.ic_linkedin_white);
                binding.Github.setImageResource(R.drawable.ic_logo_white);
                binding.cardCredit.setCardBackgroundColor(Color.parseColor("#1A1A1A"));
                binding.credit.setTextColor(Color.parseColor("#FFFFFF"));
                binding.flatIcon.setImageResource(R.drawable.ic_flaticon_white);
                binding.Unsplash.setImageResource(R.drawable.ic_unsplash_white);
                binding.pexels.setImageResource(R.drawable.pexels_white);
                binding.privacyPolicy.setTextColor(Color.parseColor("#FFFFFF"));
                binding.shareAppText.setTextColor(Color.parseColor("#FFFFFF"));
            }
        });

        if (pref.getTheme().equals("Light")) {
            binding.setTheme.setText("Light Mode");
            binding.LinkedIn.setTextColor(Color.parseColor("#000000"));
            binding.github.setTextColor(Color.parseColor("#000000"));
            binding.cardSetting.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
            binding.privacyImg.setImageResource(R.drawable.ic_security_black_24dp);
            binding.rateUsText.setTextColor(Color.parseColor("#000000"));
            binding.themeColor.setBackgroundColor(Color.parseColor("#F2F6F9"));
            binding.setTheme.setTextColor(Color.parseColor("#000000"));
            binding.loadQuality.setTextColor(Color.parseColor("#000000"));
            binding.chooseImgQuality.setTextColor(Color.parseColor("#000000"));
            binding.shareAppText.setTextColor(Color.parseColor("#000000"));
            binding.tool1barSetting.setBackgroundColor(Color.parseColor("#FFFFFF"));
            binding.toolBartext.setTextColor(Color.parseColor("#000000"));
            binding.deleteTextMain.setTextColor(Color.parseColor("#000000"));
            Resources res = getResources(); //resource handle
            Drawable drawable = res.getDrawable(R.drawable.ic_arrow_back); //new Image that was added to the res folder
            binding.backtoMain.setBackground(drawable);
            binding.otherCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
            binding.settingCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
            binding.deleteImg.setImageResource(R.drawable.ic_delete_black_24dp);
            binding.reportUs.setImageResource(R.drawable.ic_report_problem);
            binding.shareApp.setImageResource(R.drawable.ic_share_black_24dp);
            binding.rateUsImg.setImageResource(R.drawable.ic_rate_review);
            binding.ContactUsText.setTextColor(Color.parseColor("#000000"));
            binding.cardContact.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
            binding.instagram.setImageResource(R.drawable.ic_instagram);
            binding.facebook.setImageResource(R.drawable.ic_facebook);
            binding.linkedIn.setImageResource(R.drawable.ic_linkedin);
            binding.Github.setImageResource(R.drawable.ic_logo);
            binding.credit.setTextColor(Color.parseColor("#000000"));
            binding.cardCredit.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
            binding.flatIcon.setImageResource(R.drawable.ic_flaticon);
            binding.Unsplash.setImageResource(R.drawable.ic_unsplash);
            binding.pexels.setImageResource(R.drawable.pexels);
            binding.privacyPolicy.setTextColor(Color.parseColor("#000000"));
            binding.reportText.setTextColor(Color.parseColor("#000000"));
            binding.instagramText.setTextColor(Color.parseColor("#000000"));
            binding.faceBookText.setTextColor(Color.parseColor("#000000"));
            binding.weatherWallText.setTextColor(Color.parseColor("#000000"));
            binding.poweredby.setTextColor(Color.parseColor("#000000"));

        } else if (pref.getTheme().equals("Dark")) {
            binding.weatherWallText.setTextColor(Color.parseColor("#FFFFFF"));
            binding.poweredby.setTextColor(Color.parseColor("#FFFFFF"));
            binding.LinkedIn.setTextColor(Color.parseColor("#FFFFFF"));
            binding.faceBookText.setTextColor(Color.parseColor("#FFFFFF"));
            binding.setTheme.setText("Dark Mode");
            binding.cardSetting.setCardBackgroundColor(Color.parseColor("#1A1A1A"));
            binding.privacyImg.setImageResource(R.drawable.ic_security_black_24dp_white);
            binding.otherCard.setCardBackgroundColor(Color.parseColor("#1A1A1A"));
            binding.instagramText.setTextColor(Color.parseColor("#FFFFFF"));
            binding.rateUsText.setTextColor(Color.parseColor("#FFFFFF"));
            binding.reportText.setTextColor(Color.parseColor("#FFFFFF"));
            binding.themeColor.setBackgroundColor(Color.parseColor("#000000"));
            binding.tool1barSetting.setBackgroundColor(Color.parseColor("#1A1A1A"));
            binding.toolBartext.setTextColor(Color.parseColor("#FFFFFF"));
            Resources res = getResources(); //resource handle
            Drawable drawable = res.getDrawable(R.drawable.ic_arrow_back_black_24dp); //new Image that was added to the res folder
            binding.backtoMain.setBackground(drawable);
            binding.shareAppText.setTextColor(Color.parseColor("#FFFFFF"));
            binding.settingCard.setCardBackgroundColor(Color.parseColor("#1A1A1A"));

            binding.reportUs.setImageResource(R.drawable.ic_report_problem_white);
            binding.deleteImg.setImageResource(R.drawable.ic_delete_white_24dp);
            binding.cardSetting.setCardBackgroundColor(Color.parseColor("#1A1A1A"));
            binding.shareApp.setImageResource(R.drawable.ic_share_white_24dp);
            binding.rateUsImg.setImageResource(R.drawable.ic_rate_review_white);
            binding.ContactUsText.setTextColor(Color.parseColor("#FFFFFF"));
            binding.cardContact.setCardBackgroundColor(Color.parseColor("#1A1A1A"));
            binding.instagram.setImageResource(R.drawable.ic_instagram_white);
            binding.facebook.setImageResource(R.drawable.ic_facebook_white);
            binding.linkedIn.setImageResource(R.drawable.ic_linkedin_white);
            binding.Github.setImageResource(R.drawable.ic_logo_white);
            binding.credit.setTextColor(Color.parseColor("#FFFFFF"));
            binding.cardCredit.setCardBackgroundColor(Color.parseColor("#1A1A1A"));
            binding.flatIcon.setImageResource(R.drawable.ic_flaticon_white);
            binding.deleteTextMain.setTextColor(Color.parseColor("#FFFFFF"));
            binding.Unsplash.setImageResource(R.drawable.ic_unsplash_white);
            binding.pexels.setImageResource(R.drawable.pexels_white);
            binding.privacyPolicy.setTextColor(Color.parseColor("#FFFFFF"));
            binding.chooseImgQuality.setTextColor(Color.parseColor("#FFFFFF"));
            binding.loadQuality.setTextColor(Color.parseColor("#FFFFFF"));
            binding.setTheme.setTextColor(Color.parseColor("#FFFFFF"));
            binding.github.setTextColor(Color.parseColor("#FFFFFF"));

        } else {
            binding.setTheme.setText("Light Mode");
            binding.weatherWallText.setTextColor(Color.parseColor("#000000"));
            binding.poweredby.setTextColor(Color.parseColor("#000000"));
            binding.LinkedIn.setTextColor(Color.parseColor("#000000"));
            binding.github.setTextColor(Color.parseColor("#000000"));
            binding.cardSetting.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
            binding.privacyImg.setImageResource(R.drawable.ic_security_black_24dp);
            binding.otherCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
            binding.faceBookText.setTextColor(Color.parseColor("#000000"));
            binding.instagramText.setTextColor(Color.parseColor("#000000"));
            binding.setTheme.setTextColor(Color.parseColor("#000000"));
            binding.rateUsText.setTextColor(Color.parseColor("#000000"));
            binding.themeColor.setBackgroundColor(Color.parseColor("#F2F6F9"));
            binding.loadQuality.setTextColor(Color.parseColor("#000000"));
            binding.chooseImgQuality.setTextColor(Color.parseColor("#000000"));
            binding.reportText.setTextColor(Color.parseColor("#000000"));
            binding.tool1barSetting.setBackgroundColor(Color.parseColor("#FFFFFF"));
            binding.deleteTextMain.setTextColor(Color.parseColor("#000000"));
            binding.shareAppText.setTextColor(Color.parseColor("#000000"));
            binding.toolBartext.setTextColor(Color.parseColor("#000000"));
            Resources res = getResources(); //resource handle
            Drawable drawable = res.getDrawable(R.drawable.ic_arrow_back); //new Image that was added to the res folder
            binding.backtoMain.setBackground(drawable);
            binding.rateUsImg.setImageResource(R.drawable.ic_rate_review);
            binding.deleteImg.setImageResource(R.drawable.ic_delete_black_24dp);
            binding.shareApp.setImageResource(R.drawable.ic_share_black_24dp);
            binding.ContactUsText.setTextColor(Color.parseColor("#000000"));
            binding.cardContact.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
            binding.instagram.setImageResource(R.drawable.ic_instagram);
            binding.facebook.setImageResource(R.drawable.ic_facebook);
            binding.Github.setImageResource(R.drawable.ic_logo);
            binding.linkedIn.setImageResource(R.drawable.ic_linkedin);
            binding.credit.setTextColor(Color.parseColor("#000000"));
            binding.cardCredit.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
            binding.settingCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
            binding.flatIcon.setImageResource(R.drawable.ic_flaticon);
            binding.Unsplash.setImageResource(R.drawable.ic_unsplash);
            binding.pexels.setImageResource(R.drawable.pexels);
            binding.privacyPolicy.setTextColor(Color.parseColor("#000000"));


        }
    }

}
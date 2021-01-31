package com.client.vpman.weatherwall.ui.Fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.client.vpman.weatherwall.CustomeUsefullClass.Connectivity;
import com.client.vpman.weatherwall.CustomeUsefullClass.OnDataPass;
import com.client.vpman.weatherwall.CustomeUsefullClass.SharedPref1;
import com.client.vpman.weatherwall.R;
import com.client.vpman.weatherwall.databinding.FragmentWeatherBinding;
import com.client.vpman.weatherwall.databinding.LoadImgQualityBinding;
import com.client.vpman.weatherwall.databinding.QualitDialogSpinnerBinding;
import com.client.vpman.weatherwall.databinding.SettingDialogBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.button.MaterialButton;
import com.suke.widget.SwitchButton;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import static android.content.Context.LOCATION_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment {

    private List<String> apiList;
    private String JsonUrl;
    private List<String> list;
    private String cityname;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private Animation bounce;
    private boolean gps_enabled = false;
    private boolean network_enabled = false;
    private OnDataPass dataPasser;
    private Double lat, lon;
    private String countryCode = null;
    private List<Address> addresses;
    SettingDialogBinding binding1;
    private RotateAnimation rotate;

    public WeatherFragment() {
        // Required empty public constructor
    }

    private FragmentWeatherBinding binding;


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWeatherBinding.inflate(inflater, container, false);
        View view1 = binding.getRoot();
        list = new ArrayList<>();
        Log.d("hgfkj", "request");
        if (getActivity() != null) {
            SharedPref1 sharedPref1 = new SharedPref1(getActivity());
            if (sharedPref1.getTheme().equals("Light")) {

                Resources res = getResources(); //resource handle
                Drawable drawable = res.getDrawable(R.drawable.main_design_white);
                binding.relLayout.setBackground(drawable);
                binding.swipUp2.setTextColor(Color.parseColor("#000000"));
                binding.swipUp.setImageResource(R.drawable.ic_up_arow_black);

            } else if (sharedPref1.getTheme().equals("Dark")) {

                Resources res = getResources(); //resource handle
                Drawable drawable = res.getDrawable(R.drawable.main_design);
                binding.relLayout.setBackground(drawable);
                binding.swipUp2.setTextColor(Color.parseColor("#FFFFFF"));
                binding.swipUp.setImageResource(R.drawable.ic_up_arow);
            } else {
                switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                    case Configuration.UI_MODE_NIGHT_YES:
                        Resources res = getResources(); //resource handle
                        Drawable drawable = res.getDrawable(R.drawable.main_design);
                        binding.relLayout.setBackground(drawable);
                        binding.swipUp2.setTextColor(Color.parseColor("#FFFFFF"));
                        binding.swipUp.setImageResource(R.drawable.ic_up_arow);

                        break;
                    case Configuration.UI_MODE_NIGHT_NO:
                        Resources res1 = getResources(); //resource handle
                        Drawable drawable1 = res1.getDrawable(R.drawable.main_design_white);
                        binding.relLayout.setBackground(drawable1);
                        binding.swipUp2.setTextColor(Color.parseColor("#000000"));
                        binding.swipUp.setImageResource(R.drawable.ic_up_arow_black);
                        break;
                }

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
                binding.swipUp.startAnimation(bounce);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        binding.swipUp.startAnimation(bounce);

        rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(5000);
        rotate.setInterpolator(new LinearInterpolator());
        rotate.setRepeatCount(Animation.INFINITE);
        rotate.setRepeatMode(Animation.INFINITE);
        rotate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotate.setDuration(5000);
                rotate.setInterpolator(new LinearInterpolator());
                rotate.setRepeatCount(Animation.INFINITE);
                rotate.setRepeatMode(Animation.INFINITE);
                binding.settingImg.startAnimation(rotate);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        binding.settingImg.startAnimation(rotate);
                binding.settingImg.setOnClickListener(v -> settingDialog(getActivity()));
/*        binding.settingImg.setOnClickListener(v -> startActivity(new Intent(getActivity(), SettingsActivityMain.class)));*/


        if (Connectivity.isConnected(getActivity()) && Connectivity.isConnectedMobile(getActivity()) && Connectivity.isConnectedFast(getActivity()) ||
                Connectivity.isConnected(getActivity()) && Connectivity.isConnectedWifi(getActivity()) && Connectivity.isConnectedFast(getActivity())) {
            requestStoragePermission();
            Quotes();
        } else {
            showDialg(getActivity());
        }


        return view1;


    }

    @SuppressLint("MissingPermission")
    public void findWeather() {

        if (getActivity() != null) {
            FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

            apiList = new ArrayList<>();
            apiList.add("1f01ced93d6608528a3bc65ad580f9e4");
            apiList.add("ec55ea59368f44782fb4dcb6ab028f5a");
            apiList.add("4256b9145e7841dad1aa07b8b3ca5be3");
            apiList.add("667dcb63169e18e220f8ade175d2b016");
            apiList.add("6a1565969d4149752e9fa55a7bec0720");


            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), location -> {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            Geocoder gcd = new Geocoder(getActivity().getBaseContext(), Locale.getDefault());
                            try {
                                addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                                if (addresses.size() > 0) {
                                    cityname = addresses.get(0).getLocality();
                                    lat = addresses.get(0).getLatitude();
                                    lon = addresses.get(0).getLongitude();
                                    countryCode = addresses.get(0).getCountryCode();

                                    LocationRequest request = new LocationRequest();
                                    request.setInterval(10 * 60 * 1000);
                                    request.setMaxWaitTime(60 * 60 * 1000);

                                    Random random = new Random();
                                    int n = random.nextInt(apiList.size());
                                    if (cityname == null) {
                                        cityname = "Jaipur";
                                    }
                                    Log.d("cityName", cityname);
                                    JsonUrl = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&q=" + cityname + "," + countryCode + "&appid=" + apiList.get(n);


                                    JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, JsonUrl, null, response -> {


                                        Log.d("fjgj", JsonUrl);

                                        try {

                                            JSONObject main_Object = response.getJSONObject("main");
                                            JSONArray array = response.getJSONArray("weather");
                                            JSONObject object = array.getJSONObject(0);

                                            String temp = String.valueOf(main_Object.getDouble("temp")).substring(0, 2);
                                            String description = object.getString("description");
                                            String city = response.getString("name");

                                            binding.temp.setText(temp + "°C");
                                            binding.city.setText(city);
                                            binding.desc.setText(description);

                                            dataPasser.onDataPass(description);

                                            Calendar calendar = Calendar.getInstance();

                                            SimpleDateFormat sdf = new SimpleDateFormat("EEEE-MM-YYYY");
                                            String formated_date = sdf.format(calendar.getTime());

                                            binding.date.setText(formated_date);


                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }, error -> {

                                        NetworkResponse response = error.networkResponse;
                                        if (response != null && response.statusCode == 404) {
                                            try {
                                                String res = new String(response.data,
                                                        HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                                // Now you can use any deserializer to make sense of data
                                                JSONObject obj = new JSONObject(res);
                                                //use this json as you want

                                            } catch (UnsupportedEncodingException | JSONException e1) {
                                                // Couldn't properly decode data to string
                                                e1.printStackTrace();
                                            } // returned data is not JSONObject?

                                        }

                                    });
                                    jor.setShouldCache(false);
                                    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                                    jor.setRetryPolicy(new DefaultRetryPolicy(
                                            3000,
                                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                                    requestQueue.add(jor);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        } else {
                            if (checkLocationON()) {
                                findWeather();
                            } else {
                                checkGpsStatus();
                            }
                        }
                    });
        }

    }

    private void requestStoragePermission() {
        // Here, thisActivity is the current activity
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d("hgfkj", "requestStoragePermission: 007");
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE
            );


        } else {
            // Permission has already been granted
            Log.d("hgfkj", "requestStoragePermission: 009");
            if (checkLocationON()) {
                findWeather();

            } else {
                checkGpsStatus();
            }
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NotNull String[] permissions, @NotNull int[] grantResults) {
        Log.d("hgfkj", "onRequestPermissionsResult:989 ");
        if (requestCode == PERMISSION_REQUEST_CODE) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (checkLocationON()) {
                    findWeather();
                } else {
                    checkGpsStatus();
                }


            }  // permission denied, boo! Disable the
            // functionality that depends on this permission.

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }


    public static WeatherFragment newInstance(String text) {
        WeatherFragment f = new WeatherFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        dataPasser = (OnDataPass) context;
    }

    public void checkGpsStatus() {
        if (getActivity() != null) {
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

            try {
                if (locationManager != null) {
                    gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                }

            } catch (Exception ignored) {
            }

            try {
                if (locationManager != null) {
                    network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                }

            } catch (Exception ignored) {
            }

            if (!gps_enabled && !network_enabled) {
                // notify user
                new AlertDialog.Builder(getActivity())
                        .setMessage("GPS is not enabled")
                        .setPositiveButton("Open Location Setting", (paramDialogInterface, paramInt) -> {
                            if (getActivity() != null) {
                                getActivity().startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                            }

                        })
                        .setNegativeButton("Cancel", null)
                        .show();

            }

            if (checkLocationON()) {
                findWeather();
            }

        }


    }

    public boolean checkLocationON() {
        if (getActivity() != null) {
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
            try {
                if (locationManager != null) {
                    gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                }
            } catch (Exception ignored) {
            }

            try {
                if (locationManager != null) {
                    network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                }
            } catch (Exception ignored) {
            }

            // notify user
            return gps_enabled || network_enabled;
        }
        return false;


    }

    @Override
    public void onResume() {
        super.onResume();

        if (Connectivity.isConnected(getActivity())) {
            if (checkLocationON()) {

                findWeather();
            }
        } else {
            showDialg(getActivity());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            findWeather();
        }
    }

    public void showDialg(Activity activity) {

        final Dialog dialog1 = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setCancelable(false);
        dialog1.setContentView(R.layout.internet_connection);
        MaterialButton connectInternet = dialog1.findViewById(R.id.internet);
        connectInternet.setOnClickListener(view -> {
            Intent i = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
            startActivity(i);


        });

        dialog1.show();
        if (Connectivity.isConnected(getActivity())) {
            dialog1.dismiss();
        }
    }

    private void settingDialog(Activity activity) {
        Dialog dialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.show();
         binding1 = SettingDialogBinding.inflate(LayoutInflater.from(getContext()));
        dialog.setContentView(binding1.getRoot());

        binding1.deleteTextMain.setOnClickListener(v -> deleteCache(activity));

        binding1.deleteImg.setOnClickListener(v -> deleteCache(activity));
        binding1.shareAppText.setOnClickListener(v -> {
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
        binding1.shareApp.setOnClickListener(v -> {
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

        binding1.reportText.setOnClickListener(v -> {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", "vp.mannu.kr@gmail.com", null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Weather Wall");
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
        });

        binding1.reportUs.setOnClickListener(v -> {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", "vp.mannu.kr@gmail.com", null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Weather Wall");
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
        });

        binding1.rateUsText.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.client.vpman.weatherwall"));
            startActivity(browserIntent);

        });

        binding1.rateUsImg.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.client.vpman.weatherwall"));
            startActivity(browserIntent);
        });

        binding1.instagramText.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/weather_wall/"));
            startActivity(browserIntent);
        });

        binding1.instagram.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/weather_wall/"));
            startActivity(browserIntent);
        });

        binding1.faceBookText.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/Weather-Wall-104577191240236/"));
            startActivity(browserIntent);
        });

        binding1.facebook.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/Weather-Wall-104577191240236/"));
            startActivity(browserIntent);
        });

        binding1.LinkedIn.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/vedprakash1998/"));
            startActivity(browserIntent);
        });

        binding1.linkedIn.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/vedprakash1998/"));
            startActivity(browserIntent);
        });

        binding1.github.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/vedprakashsah1998/WeatherWall"));
            startActivity(browserIntent);
        });

        binding1.Github.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/vedprakashsah1998/WeatherWall"));
            startActivity(browserIntent);
        });

        binding1.pexels.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.pexels.com/"));
            startActivity(browserIntent);
        });

        binding1.flatIcon.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.flaticon.com/"));
            startActivity(browserIntent);
        });

        binding1.Unsplash.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://unsplash.com/"));
            startActivity(browserIntent);
        });

        binding1.privacyPolicy.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://weather-wall.flycricket.io/privacy.html"));
            startActivity(browserIntent);
        });
        Spinner spinner = dialog.findViewById(R.id.spinner);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(activity, R.layout.custome_spinner, getResources().getStringArray(R.array.list));

        dataAdapter.setDropDownViewResource(R.layout.custome_spinner_dropdown);
        SharedPref1 pref = new SharedPref1(activity);


        binding1.chooseImgQuality.append(pref.getImageQuality());
        binding1.chooseImgQuality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QualityDialog(activity);
            }
        });

/*        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                if (position != 0) {
                    pref.setImageQuality(item);
                    binding1.chooseImgQuality.setText("Current Quality :\n" + item);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

//        Spinner spinner1 = dialog.findViewById(R.id.spinner2);
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<>(activity, R.layout.custome_spinner_list, getResources().getStringArray(R.array.list));
        dataAdapter1.setDropDownViewResource(R.layout.custome_spinner_load);
/*        binding1.spinner2.setAdapter(dataAdapter1);
        binding1.spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();


                if (position != 0) {
                    pref.setImageLoadQuality(item);
                    binding1.loadQuality.setText("Load image Quality :\n" + item);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
        binding1.loadQuality.append(pref.getImageLoadQuality());

        binding1.loadQuality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QualityDialog(activity);
            }
        });

        SwitchButton switchButton = dialog.findViewById(R.id.sticky_switch1);


        if (pref.getTheme().equals("Light")) {
            switchButton.setChecked(false);
        } else if (pref.getTheme().equals("Dark")) {
            switchButton.setChecked(true);
        } else {
            switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                case Configuration.UI_MODE_NIGHT_YES:
                    switchButton.setChecked(true);

                    break;
                case Configuration.UI_MODE_NIGHT_NO:
                    switchButton.setChecked(false);

                    break;
            }

        }


        switchButton.setOnCheckedChangeListener((view, isChecked) -> {
            if (!isChecked) {
                pref.setTheme("Light");
                binding1.themeColor.setBackgroundColor(Color.parseColor("#F2F6F9"));
                binding1.tool1barSetting.setBackgroundColor(Color.parseColor("#FFFFFF"));
                binding1.deleteTextMain.setTextColor(Color.parseColor("#000000"));
                binding1.setTheme.setTextColor(Color.parseColor("#000000"));
                binding1.weatherWallText.setTextColor(Color.parseColor("#000000"));
                binding1.poweredby.setTextColor(Color.parseColor("#000000"));
                binding1.otherCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                binding1.setTheme.setText("Light Mode");
                binding1.toolBartext.setTextColor(Color.parseColor("#000000"));
                binding1.privacyImg.setImageResource(R.drawable.ic_security_black_24dp);
                Resources res = getResources(); //resource handle
                Drawable drawable = res.getDrawable(R.drawable.ic_arrow_back); //new Image that was added to the res folder
                binding1.backtoMain.setBackground(drawable);
                binding1.loadQuality.setTextColor(Color.parseColor("#000000"));
                binding1.deleteImg.setImageResource(R.drawable.ic_delete_black_24dp);
                binding1.chooseImgQuality.setTextColor(Color.parseColor("#000000"));
                binding1.cardSetting.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                binding1.settingCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                binding1.shareApp.setImageResource(R.drawable.ic_share_black_24dp);
                binding1.reportUs.setImageResource(R.drawable.ic_report_problem);
                binding1.rateUsImg.setImageResource(R.drawable.ic_rate_review);
                binding1.ContactUsText.setTextColor(Color.parseColor("#000000"));
                binding1.cardContact.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                binding1.instagram.setImageResource(R.drawable.ic_instagram);
                binding1.facebook.setImageResource(R.drawable.ic_facebook);
                binding1.instagramText.setTextColor(Color.parseColor("#000000"));
                binding1.faceBookText.setTextColor(Color.parseColor("#000000"));
                binding1.linkedIn.setImageResource(R.drawable.ic_linkedin);
                binding1.rateUsText.setTextColor(Color.parseColor("#000000"));
                binding1.Github.setImageResource(R.drawable.ic_logo);
                binding1.credit.setTextColor(Color.parseColor("#000000"));
                binding1.cardCredit.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                binding1.flatIcon.setImageResource(R.drawable.ic_flaticon);
                binding1.Unsplash.setImageResource(R.drawable.ic_unsplash);
                binding1.pexels.setImageResource(R.drawable.pexels);
                binding1.shareAppText.setTextColor(Color.parseColor("#000000"));
                binding1.privacyPolicy.setTextColor(Color.parseColor("#000000"));
                binding1.reportText.setTextColor(Color.parseColor("#000000"));
                binding1.github.setTextColor(Color.parseColor("#000000"));
                binding1.LinkedIn.setTextColor(Color.parseColor("#000000"));
            } else {
                pref.setTheme("Dark");
                binding1.weatherWallText.setTextColor(Color.parseColor("#FFFFFF"));
                binding1.poweredby.setTextColor(Color.parseColor("#FFFFFF"));
                binding1.LinkedIn.setTextColor(Color.parseColor("#FFFFFF"));
                binding1.github.setTextColor(Color.parseColor("#FFFFFF"));
                binding1.settingCard.setCardBackgroundColor(Color.parseColor("#1A1A1A"));
                binding1.otherCard.setCardBackgroundColor(Color.parseColor("#1A1A1A"));
                binding1.setTheme.setText("Dark Mode");
                binding1.faceBookText.setTextColor(Color.parseColor("#FFFFFF"));
                binding1.instagramText.setTextColor(Color.
                        parseColor("#FFFFFF"));
                binding1.rateUsText.setTextColor(Color.parseColor("#FFFFFF"));
                binding1.reportText.setTextColor(Color.parseColor("#FFFFFF"));
                binding1.privacyImg.setImageResource(R.drawable.ic_security_black_24dp_white);
                binding1.themeColor.setBackgroundColor(Color.parseColor("#000000"));
                binding1.chooseImgQuality.setTextColor(Color.parseColor("#FFFFFF"));
                binding1.tool1barSetting.setBackgroundColor(Color.parseColor("#1A1A1A"));
                binding1.loadQuality.setTextColor(Color.parseColor("#FFFFFF"));
                binding1.setTheme.setTextColor(Color.parseColor("#FFFFFF"));
                binding1.toolBartext.setTextColor(Color.parseColor("#FFFFFF"));
                binding1.deleteTextMain.setTextColor(Color.parseColor("#FFFFFF"));
                Resources res = getResources(); //resource handle
                Drawable drawable = res.getDrawable(R.drawable.ic_arrow_back_black_24dp); //new Image that was added to the res folder
                binding1.backtoMain.setBackground(drawable);
                binding1.ContactUsText.setTextColor(Color.parseColor("#FFFFFF"));
                binding1.deleteImg.setImageResource(R.drawable.ic_delete_white_24dp);
                binding1.cardSetting.setCardBackgroundColor(Color.parseColor("#1A1A1A"));
                binding1.rateUsImg.setImageResource(R.drawable.ic_rate_review_white);
                binding1.shareApp.setImageResource(R.drawable.ic_share_white_24dp);
                binding1.reportUs.setImageResource(R.drawable.ic_report_problem_white);
                binding1.cardContact.setCardBackgroundColor(Color.parseColor("#1A1A1A"));
                binding1.instagram.setImageResource(R.drawable.ic_instagram_white);
                binding1.facebook.setImageResource(R.drawable.ic_facebook_white);
                binding1.linkedIn.setImageResource(R.drawable.ic_linkedin_white);
                binding1.Github.setImageResource(R.drawable.ic_logo_white);
                binding1.cardCredit.setCardBackgroundColor(Color.parseColor("#1A1A1A"));
                binding1.credit.setTextColor(Color.parseColor("#FFFFFF"));
                binding1.flatIcon.setImageResource(R.drawable.ic_flaticon_white);
                binding1.Unsplash.setImageResource(R.drawable.ic_unsplash_white);
                binding1.pexels.setImageResource(R.drawable.pexels_white);
                binding1.privacyPolicy.setTextColor(Color.parseColor("#FFFFFF"));
                binding1.shareAppText.setTextColor(Color.parseColor("#FFFFFF"));
            }
        });

        if (pref.getTheme().equals("Light")) {
            binding1.setTheme.setText("Light Mode");
            binding1.LinkedIn.setTextColor(Color.parseColor("#000000"));
            binding1.github.setTextColor(Color.parseColor("#000000"));
            binding1.settingCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
            binding1.privacyImg.setImageResource(R.drawable.ic_security_black_24dp);
            binding1.rateUsText.setTextColor(Color.parseColor("#000000"));
            binding1.themeColor.setBackgroundColor(Color.parseColor("#F2F6F9"));
            binding1.setTheme.setTextColor(Color.parseColor("#000000"));
            binding1.loadQuality.setTextColor(Color.parseColor("#000000"));
            binding1.chooseImgQuality.setTextColor(Color.parseColor("#000000"));
            binding1.shareAppText.setTextColor(Color.parseColor("#000000"));
            binding1.tool1barSetting.setBackgroundColor(Color.parseColor("#FFFFFF"));
            binding1.toolBartext.setTextColor(Color.parseColor("#000000"));
            binding1.deleteTextMain.setTextColor(Color.parseColor("#000000"));
            Resources res = getResources(); //resource handle
            Drawable drawable = res.getDrawable(R.drawable.ic_arrow_back); //new Image that was added to the res folder
            binding1.backtoMain.setBackground(drawable);
            binding1.otherCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
            binding1.cardSetting.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
            binding1.deleteImg.setImageResource(R.drawable.ic_delete_black_24dp);
            binding1.reportUs.setImageResource(R.drawable.ic_report_problem);
            binding1.shareApp.setImageResource(R.drawable.ic_share_black_24dp);
            binding1.rateUsImg.setImageResource(R.drawable.ic_rate_review);
            binding1.ContactUsText.setTextColor(Color.parseColor("#000000"));
            binding1.cardContact.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
            binding1.instagram.setImageResource(R.drawable.ic_instagram);
            binding1.facebook.setImageResource(R.drawable.ic_facebook);
            binding1.linkedIn.setImageResource(R.drawable.ic_linkedin);
            binding1.Github.setImageResource(R.drawable.ic_logo);
            binding1.credit.setTextColor(Color.parseColor("#000000"));
            binding1.cardCredit.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
            binding1.flatIcon.setImageResource(R.drawable.ic_flaticon);
            binding1.Unsplash.setImageResource(R.drawable.ic_unsplash);
            binding1.pexels.setImageResource(R.drawable.pexels);
            binding1.privacyPolicy.setTextColor(Color.parseColor("#000000"));
            binding1.reportText.setTextColor(Color.parseColor("#000000"));
            binding1.instagramText.setTextColor(Color.parseColor("#000000"));
            binding1.faceBookText.setTextColor(Color.parseColor("#000000"));
            binding1.weatherWallText.setTextColor(Color.parseColor("#000000"));
            binding1.poweredby.setTextColor(Color.parseColor("#000000"));

        } else if (pref.getTheme().equals("Dark")) {
            binding1.weatherWallText.setTextColor(Color.parseColor("#FFFFFF"));
            binding1.poweredby.setTextColor(Color.parseColor("#FFFFFF"));
            binding1.LinkedIn.setTextColor(Color.parseColor("#FFFFFF"));
            binding1.faceBookText.setTextColor(Color.parseColor("#FFFFFF"));
            binding1.setTheme.setText("Dark Mode");
            binding1.settingCard.setCardBackgroundColor(Color.parseColor("#1A1A1A"));
            binding1.privacyImg.setImageResource(R.drawable.ic_security_black_24dp_white);
            binding1.otherCard.setCardBackgroundColor(Color.parseColor("#1A1A1A"));
            binding1.instagramText.setTextColor(Color.parseColor("#FFFFFF"));
            binding1.rateUsText.setTextColor(Color.parseColor("#FFFFFF"));
            binding1.reportText.setTextColor(Color.parseColor("#FFFFFF"));
            binding1.themeColor.setBackgroundColor(Color.parseColor("#000000"));
            binding1.tool1barSetting.setBackgroundColor(Color.parseColor("#1A1A1A"));
            binding1.toolBartext.setTextColor(Color.parseColor("#FFFFFF"));
            Resources res = getResources(); //resource handle
            Drawable drawable = res.getDrawable(R.drawable.ic_arrow_back_black_24dp); //new Image that was added to the res folder
            binding1.backtoMain.setBackground(drawable);
            binding1.shareAppText.setTextColor(Color.parseColor("#FFFFFF"));

            binding1.reportUs.setImageResource(R.drawable.ic_report_problem_white);
            binding1.deleteImg.setImageResource(R.drawable.ic_delete_white_24dp);
            binding1.cardSetting.setCardBackgroundColor(Color.parseColor("#1A1A1A"));
            binding1.shareApp.setImageResource(R.drawable.ic_share_white_24dp);
            binding1.rateUsImg.setImageResource(R.drawable.ic_rate_review_white);
            binding1.ContactUsText.setTextColor(Color.parseColor("#FFFFFF"));
            binding1.cardContact.setCardBackgroundColor(Color.parseColor("#1A1A1A"));
            binding1.instagram.setImageResource(R.drawable.ic_instagram_white);
            binding1.facebook.setImageResource(R.drawable.ic_facebook_white);
            binding1.linkedIn.setImageResource(R.drawable.ic_linkedin_white);
            binding1.Github.setImageResource(R.drawable.ic_logo_white);
            binding1.credit.setTextColor(Color.parseColor("#FFFFFF"));
            binding1.cardCredit.setCardBackgroundColor(Color.parseColor("#1A1A1A"));
            binding1.flatIcon.setImageResource(R.drawable.ic_flaticon_white);
            binding1.deleteTextMain.setTextColor(Color.parseColor("#FFFFFF"));
            binding1.Unsplash.setImageResource(R.drawable.ic_unsplash_white);
            binding1.pexels.setImageResource(R.drawable.pexels_white);
            binding1.privacyPolicy.setTextColor(Color.parseColor("#FFFFFF"));
            binding1.chooseImgQuality.setTextColor(Color.parseColor("#FFFFFF"));
            binding1.loadQuality.setTextColor(Color.parseColor("#FFFFFF"));
            binding1.setTheme.setTextColor(Color.parseColor("#FFFFFF"));
            binding1.github.setTextColor(Color.parseColor("#FFFFFF"));

        } else {

            switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                case Configuration.UI_MODE_NIGHT_YES:
                    binding1.weatherWallText.setTextColor(Color.parseColor("#FFFFFF"));
                    binding1.poweredby.setTextColor(Color.parseColor("#FFFFFF"));
                    binding1.LinkedIn.setTextColor(Color.parseColor("#FFFFFF"));
                    binding1.faceBookText.setTextColor(Color.parseColor("#FFFFFF"));
                    binding1.setTheme.setText("Dark Mode");
                    binding1.settingCard.setCardBackgroundColor(Color.parseColor("#1A1A1A"));
                    binding1.privacyImg.setImageResource(R.drawable.ic_security_black_24dp_white);
                    binding1.otherCard.setCardBackgroundColor(Color.parseColor("#1A1A1A"));
                    binding1.instagramText.setTextColor(Color.parseColor("#FFFFFF"));
                    binding1.rateUsText.setTextColor(Color.parseColor("#FFFFFF"));
                    binding1.reportText.setTextColor(Color.parseColor("#FFFFFF"));
                    binding1.themeColor.setBackgroundColor(Color.parseColor("#000000"));
                    binding1.tool1barSetting.setBackgroundColor(Color.parseColor("#1A1A1A"));
                    binding1.toolBartext.setTextColor(Color.parseColor("#FFFFFF"));
                    Resources res = getResources(); //resource handle
                    Drawable drawable = res.getDrawable(R.drawable.ic_arrow_back_black_24dp); //new Image that was added to the res folder
                    binding1.backtoMain.setBackground(drawable);
                    binding1.shareAppText.setTextColor(Color.parseColor("#FFFFFF"));

                    binding1.reportUs.setImageResource(R.drawable.ic_report_problem_white);
                    binding1.deleteImg.setImageResource(R.drawable.ic_delete_white_24dp);
                    binding1.cardSetting.setCardBackgroundColor(Color.parseColor("#1A1A1A"));
                    binding1.shareApp.setImageResource(R.drawable.ic_share_white_24dp);
                    binding1.rateUsImg.setImageResource(R.drawable.ic_rate_review_white);
                    binding1.ContactUsText.setTextColor(Color.parseColor("#FFFFFF"));
                    binding1.cardContact.setCardBackgroundColor(Color.parseColor("#1A1A1A"));
                    binding1.instagram.setImageResource(R.drawable.ic_instagram_white);
                    binding1.facebook.setImageResource(R.drawable.ic_facebook_white);
                    binding1.linkedIn.setImageResource(R.drawable.ic_linkedin_white);
                    binding1.Github.setImageResource(R.drawable.ic_logo_white);
                    binding1.credit.setTextColor(Color.parseColor("#FFFFFF"));
                    binding1.cardCredit.setCardBackgroundColor(Color.parseColor("#1A1A1A"));
                    binding1.flatIcon.setImageResource(R.drawable.ic_flaticon_white);
                    binding1.deleteTextMain.setTextColor(Color.parseColor("#FFFFFF"));
                    binding1.Unsplash.setImageResource(R.drawable.ic_unsplash_white);
                    binding1.pexels.setImageResource(R.drawable.pexels_white);
                    binding1.privacyPolicy.setTextColor(Color.parseColor("#FFFFFF"));
                    binding1.chooseImgQuality.setTextColor(Color.parseColor("#FFFFFF"));
                    binding1.loadQuality.setTextColor(Color.parseColor("#FFFFFF"));
                    binding1.setTheme.setTextColor(Color.parseColor("#FFFFFF"));
                    binding1.github.setTextColor(Color.parseColor("#FFFFFF"));
                    break;
                case Configuration.UI_MODE_NIGHT_NO:
                    binding1.setTheme.setText("Light Mode");
                    binding1.weatherWallText.setTextColor(Color.parseColor("#000000"));
                    binding1.poweredby.setTextColor(Color.parseColor("#000000"));
                    binding1.LinkedIn.setTextColor(Color.parseColor("#000000"));
                    binding1.github.setTextColor(Color.parseColor("#000000"));
                    binding1.settingCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                    binding1.privacyImg.setImageResource(R.drawable.ic_security_black_24dp);
                    binding1.otherCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                    binding1.faceBookText.setTextColor(Color.parseColor("#000000"));
                    binding1.instagramText.setTextColor(Color.parseColor("#000000"));
                    binding1.setTheme.setTextColor(Color.parseColor("#000000"));
                    binding1.rateUsText.setTextColor(Color.parseColor("#000000"));
                    binding1.themeColor.setBackgroundColor(Color.parseColor("#F2F6F9"));
                    binding1.loadQuality.setTextColor(Color.parseColor("#000000"));
                    binding1.chooseImgQuality.setTextColor(Color.parseColor("#000000"));
                    binding1.reportText.setTextColor(Color.parseColor("#000000"));
                    binding1.tool1barSetting.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    binding1.deleteTextMain.setTextColor(Color.parseColor("#000000"));
                    binding1.shareAppText.setTextColor(Color.parseColor("#000000"));
                    binding1.toolBartext.setTextColor(Color.parseColor("#000000"));
                    Resources res1 = getResources(); //resource handle
                    Drawable drawable1 = res1.getDrawable(R.drawable.ic_arrow_back); //new Image that was added to the res folder
                    binding1.backtoMain.setBackground(drawable1);
                    binding1.rateUsImg.setImageResource(R.drawable.ic_rate_review);
                    binding1.deleteImg.setImageResource(R.drawable.ic_delete_black_24dp);
                    binding1.shareApp.setImageResource(R.drawable.ic_share_black_24dp);
                    binding1.ContactUsText.setTextColor(Color.parseColor("#000000"));
                    binding1.cardContact.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                    binding1.instagram.setImageResource(R.drawable.ic_instagram);
                    binding1.facebook.setImageResource(R.drawable.ic_facebook);
                    binding1.Github.setImageResource(R.drawable.ic_logo);
                    binding1.linkedIn.setImageResource(R.drawable.ic_linkedin);
                    binding1.credit.setTextColor(Color.parseColor("#000000"));
                    binding1.cardCredit.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                    binding1.cardSetting.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                    binding1.flatIcon.setImageResource(R.drawable.ic_flaticon);
                    binding1.Unsplash.setImageResource(R.drawable.ic_unsplash);
                    binding1.pexels.setImageResource(R.drawable.pexels);
                    binding1.privacyPolicy.setTextColor(Color.parseColor("#000000"));

                    break;
            }


        }

        binding1.backtoMain.setOnClickListener(v -> {
            activity.overridePendingTransition(0, 0);

            if (getParentFragmentManager() != null) {
                getParentFragmentManager().beginTransaction().detach(WeatherFragment.this).attach(WeatherFragment.this).commit();
            }
            activity.recreate();
            activity.overridePendingTransition(0, 0);
        });
        dialog.setOnCancelListener(dialog1 -> {
            activity.overridePendingTransition(0, 0);
            if (getParentFragmentManager() != null) {
                getParentFragmentManager().beginTransaction().detach(WeatherFragment.this).attach(WeatherFragment.this).commit();
            }
            activity.recreate();
            activity.overridePendingTransition(0, 0);

        });
    }


    public void Quotes() {
        String QuotesUrl = "https://www.forbes.com/forbesapi/thought/uri.json?enrich=true&query=1&relatedlimit=100";

        Log.d("sdfljh", "khwqgdi");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, QuotesUrl, response -> {

            Log.d("qoefg", response);

            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(response);

                JSONObject jsonObject1 = jsonObject.getJSONObject("thought");
                Log.d("qeljg", String.valueOf(jsonObject1));

                JSONArray jsonArray = jsonObject1.getJSONArray("relatedThemeThoughts");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);

                    list.add(jsonObject2.getString("quote"));

                }
                Collections.shuffle(list);
                Random random = new Random();
                int n = random.nextInt(list.size());
                binding.quotesFirst.setText(list.get(n));
                Log.d("asljf", String.valueOf(n));
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }, error -> {

        });

        stringRequest.setShouldCache(false);
        if (getActivity() != null) {
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    3000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        }

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

    public void QualityDialog(Activity activity)
    {
        Dialog dialog = new Dialog(activity, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.show();
        QualitDialogSpinnerBinding binding=QualitDialogSpinnerBinding.inflate(LayoutInflater.from(getContext()));
        dialog.setContentView(binding.getRoot());
        SharedPref1 pref = new SharedPref1(activity);

        binding.radioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            switch(i){
                case R.id.default1:
                    // do operations specific to this selection
                    pref.setImageQuality("Default");
                    binding1.chooseImgQuality.setText("Current Quality :\n" + "Default");

                    dialog.dismiss();
                    break;
                case R.id.highQ:
                    // do operations specific to this selection
                    pref.setImageQuality("High Quality");
                    binding1.chooseImgQuality.setText("Current Quality :\n" + "High Quality");
                    dialog.dismiss();
                    break;
            }
        });
    }

    public void QualityDialog1(Activity activity)
    {
        Dialog dialog = new Dialog(activity, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.show();
        LoadImgQualityBinding binding=LoadImgQualityBinding.inflate(LayoutInflater.from(getContext()));
        dialog.setContentView(binding.getRoot());
        SharedPref1 pref = new SharedPref1(activity);

        binding.radioGroup1.setOnCheckedChangeListener((radioGroup, i) -> {
            switch(i){
                case R.id.default2:
                    // do operations specific to this selection
                    pref.setImageQuality("Default");
                    binding1.loadQuality.setText("Current Quality :\n" + "Default");

                    dialog.dismiss();
                    break;
                case R.id.highQ1:
                    // do operations specific to this selection
                    pref.setImageQuality("High Quality");
                    binding1.loadQuality.setText("Current Quality :\n" + "High Quality");
                    dialog.dismiss();
                    break;
            }
        });
    }

}
package com.client.vpman.weatherwall.Fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.client.vpman.weatherwall.Activity.FullImage;
import com.client.vpman.weatherwall.Activity.MainActivity;
import com.client.vpman.weatherwall.CustomeUsefullClass.Connectivity;
import com.client.vpman.weatherwall.CustomeUsefullClass.ForbesQuotesModel;
import com.client.vpman.weatherwall.CustomeUsefullClass.OnDataPass;
import com.client.vpman.weatherwall.CustomeUsefullClass.SharedPref1;
import com.client.vpman.weatherwall.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import static android.content.Context.LOCATION_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment {
    MaterialTextView t1_temp, t2_city, t3_description, t4_date, forbesQuotes;
    String JsonUrl = "https://api.openweathermap.org/data/2.5/weather?q=,in&appid=ec55ea59368f44782fb4dcb6ab028f5a";

    List<String> list;
    String cityname;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private View view;
    private StringBuilder msg = new StringBuilder(2048);
    Animation bounce;
    boolean gps_enabled = false;
    boolean network_enabled = false;
    OnDataPass dataPasser;
    ImageView swipeUp;
    Double lat, lon;
    String countryCode = null;

    public String bestProvider;
    MaterialTextView welcomeText, welcomeText1;

    int i = 0;
    ImageView image;
    List<Address> addresses;

    private FusedLocationProviderClient fusedLocationClient;

    RotateAnimation rotate;


    public WeatherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_weather, container, false);

        t1_temp = view.findViewById(R.id.temp);
        t2_city = view.findViewById(R.id.city);
        t3_description = view.findViewById(R.id.desc);
        t4_date = view.findViewById(R.id.date);
        swipeUp = view.findViewById(R.id.swipUp);
        list = new ArrayList<>();
/*
        welcomeText=view.findViewById(R.id.welcomeText);
        welcomeText1=view.findViewById(R.id.welcomeText1);*/
        forbesQuotes = view.findViewById(R.id.Forbes_quotes);


        Log.d("hgfkj", "request");

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
                swipeUp.startAnimation(bounce);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        swipeUp.startAnimation(bounce);

        image = view.findViewById(R.id.settingImg);
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
                image.startAnimation(rotate);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        image.startAnimation(rotate);
        image.setOnClickListener(v -> settingDialog(getActivity()));


        if (Connectivity.isConnected(getActivity()) && Connectivity.isConnectedMobile(getActivity()) && Connectivity.isConnectedFast(getActivity()) ||
                Connectivity.isConnected(getActivity()) && Connectivity.isConnectedWifi(getActivity()) && Connectivity.isConnectedFast(getActivity())) {
            requestStoragePermission();
            Quotes();
        } else {
            showDialg(getActivity());
        }


        return view;

    }

    @SuppressLint("MissingPermission")
    public void findWeather() {


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
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
                                Log.d("wkegfiewgj", String.valueOf(lat));
                                LocationRequest request = new LocationRequest();
                                request.setInterval(10 * 60 * 1000);
                                request.setMaxWaitTime(60 * 60 * 1000);

                                JsonUrl = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&q=" + cityname + "," + countryCode + "&appid=ec55ea59368f44782fb4dcb6ab028f5a";
                                Log.d("findWeather", cityname);


                                JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, JsonUrl, null, response -> {


                                    Log.d("fjgj", String.valueOf(response));

                                    try {
                  /*  JSONObject coord=response.getJSONObject("coord");
                    Log.d("latLong", String.valueOf(coord));
                    JSONObject longitude=coord.getJSONObject("lon");
                    JSONObject latitude=coord.getJSONObject("lat");*/
                                        JSONObject main_Object = response.getJSONObject("main");
                                        JSONArray array = response.getJSONArray("weather");
                                        JSONObject object = array.getJSONObject(0);

                                        String temp = String.valueOf(main_Object.getDouble("temp")).substring(0, 2);
                                        String description = object.getString("description");
                                        String city = response.getString("name");

                                        t1_temp.setText(temp + "°C");
                                        t2_city.setText(city);
                                        t3_description.setText(description);

                                        dataPasser.onDataPass(description);


                                        Calendar calendar = Calendar.getInstance();

                                        SimpleDateFormat sdf = new SimpleDateFormat("EEEE-MM-DD");
                                        String formated_date = sdf.format(calendar.getTime());

                                        t4_date.setText(formated_date);


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }, error -> {

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
                                           String[] permissions, int[] grantResults) {
        Log.d("hgfkj", "onRequestPermissionsResult:989 ");
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (checkLocationON()) {
                        findWeather();
                    } else {
                        checkGpsStatus();
                    }


                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

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
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            new AlertDialog.Builder(getActivity())
                    .setMessage("GPS is not enabled")
                    .setPositiveButton("Open Location Setting", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
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

    public boolean checkLocationON() {
        LocationManager locationManager = (LocationManager) Objects.requireNonNull(getActivity()).getSystemService(LOCATION_SERVICE);


        try {
            if (locationManager != null) {
                gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            }
        } catch (Exception ex) {
        }

        try {
            if (locationManager != null) {
                network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            }
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            return false;
        } else {
            return true;
        }

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




        /*Toast.makeText(getActivity(), "Resume", Toast.LENGTH_SHORT).show();*/

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            findWeather();
        }
    }

    public void showDialg(Activity activity) {

        final Dialog dialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.internet_connection);
        MaterialButton connectInternet = dialog.findViewById(R.id.internet);
        connectInternet.setOnClickListener(view -> {
            Intent i = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
            startActivity(i);


        });

        dialog.show();
        if (Connectivity.isConnected(getActivity())) {
            dialog.dismiss();
        }
    }

    public void settingDialog(Activity activity) {
        final Dialog dialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.show();
        dialog.setContentView(R.layout.setting_dialog);
        MaterialTextView clearCache = dialog.findViewById(R.id.deleteTextMain);
        clearCache.setOnClickListener(v -> deleteCache(activity));
        MaterialTextView shareApp = dialog.findViewById(R.id.shareAppText);
        shareApp.setOnClickListener(v -> {
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
        MaterialTextView reportText = dialog.findViewById(R.id.reportText);
        reportText.setOnClickListener(v -> {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", "vp.mannu.kr@gmail.com", null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Weather Wall");
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
        });
        MaterialTextView rateUsText = dialog.findViewById(R.id.rateUsText);
        rateUsText.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.client.vpman.weatherwall"));
            startActivity(browserIntent);

        });
        MaterialTextView instagramText = dialog.findViewById(R.id.instagramText);
        instagramText.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/vedprakash.sah.378/"));
            startActivity(browserIntent);
        });
        MaterialTextView faceBookText = dialog.findViewById(R.id.faceBookText);
        faceBookText.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/vedprakash.sah.378"));
            startActivity(browserIntent);
        });
        MaterialTextView LinkedIn = dialog.findViewById(R.id.LinkedIn);
        LinkedIn.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/vedprakash1998/"));
            startActivity(browserIntent);
        });
        MaterialTextView github = dialog.findViewById(R.id.github);
        github.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Vedprakash12"));
            startActivity(browserIntent);
        });
        ImageView pexels = dialog.findViewById(R.id.pexels);
        pexels.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.pexels.com/"));
            startActivity(browserIntent);
        });
        ImageView flatIcon = dialog.findViewById(R.id.flatIcon);
        flatIcon.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.flaticon.com/"));
            startActivity(browserIntent);
        });
        ImageView Unsplash = dialog.findViewById(R.id.Unsplash);
        Unsplash.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://unsplash.com/"));
            startActivity(browserIntent);
        });
        MaterialTextView privacyPolicy = dialog.findViewById(R.id.privacyPolicy);
        privacyPolicy.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://weather-wall.flycricket.io/privacy.html"));
            startActivity(browserIntent);
        });
        Spinner spinner = dialog.findViewById(R.id.spinner);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(activity, R.layout.custome_spinner, getResources().getStringArray(R.array.list));
        /*spinner.getBackground().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);*/
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(R.layout.custome_spinner_dropdown);
        SharedPref1 pref = new SharedPref1(activity);
        /*switch (pref.getImageQuality()){
            case "Default": spinner.setSelection(spinner.getAdapter().toString().indexOf("Default"));
            case "High Quality": spinner.setSelection(spinner.getAdapter().toString().indexOf("High Quality"));
        }*/

        Log.d("edho", pref.getImageQuality());


        MaterialTextView chooseImgQuality = dialog.findViewById(R.id.chooseImgQuality);
        chooseImgQuality.append(pref.getImageQuality());


        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();


                if (position != 0) {
                    pref.setImageQuality(item);
                    chooseImgQuality.setText("Current Quality :\n"+item);
                }

                /*Intent intent = new Intent("custom-message");
                intent.putExtra("quality",item);
                LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
                // Showing selected spinner item
                Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();*/

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ImageView backtoMain = dialog.findViewById(R.id.backtoMain);
        backtoMain.setOnClickListener(v -> dialog.dismiss());
        dialog.setOnCancelListener(DialogInterface::dismiss);
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
/*
                String quote=jsonObject1.getString("quote");
*/
                JSONArray jsonArray = jsonObject1.getJSONArray("relatedThemeThoughts");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                    /*  String quotes=jsonObject2.getString("quote");*/
/*
                    forbesQuotes.setText(quotes);
*/
                    list.add(jsonObject2.getString("quote"));

                }
                Collections.shuffle(list);
                Random random = new Random();
                int n = random.nextInt(list.size());
                forbesQuotes.setText(list.get(n));
                /*Log.d("asljf",quote);*/
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }, error -> {

        });

        stringRequest.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
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
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }


}

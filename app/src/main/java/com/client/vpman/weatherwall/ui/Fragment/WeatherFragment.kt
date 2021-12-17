package com.client.vpman.weatherwall.ui.Fragment

import android.Manifest
import com.client.vpman.weatherwall.CustomeUsefullClass.OnDataPass
import android.view.animation.RotateAnimation
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import com.client.vpman.weatherwall.CustomeUsefullClass.SharedPref1
import android.graphics.drawable.Drawable
import com.client.vpman.weatherwall.R
import android.view.animation.LinearInterpolator
import com.client.vpman.weatherwall.CustomeUsefullClass.Connectivity
import android.annotation.SuppressLint
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import android.location.Geocoder
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject
import org.json.JSONArray
import com.squareup.picasso.Picasso
import org.json.JSONException
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.Volley
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import com.client.vpman.weatherwall.ui.Fragment.WeatherFragment
import android.location.LocationManager
import android.content.Intent
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.PorterDuff
import android.location.Address
import android.location.Location
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.google.android.material.button.MaterialButton
import com.suke.widget.SwitchButton
import com.android.volley.toolbox.StringRequest
import android.widget.Toast
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.android.volley.*
import com.client.vpman.weatherwall.CustomeUsefullClass.CalculateCacheMemory
import com.client.vpman.weatherwall.databinding.FragmentWeatherBinding
import com.client.vpman.weatherwall.databinding.LoadImgQualityBinding
import com.client.vpman.weatherwall.databinding.QualitDialogSpinnerBinding
import com.client.vpman.weatherwall.databinding.SettingLayoutBinding
import com.google.android.gms.location.LocationRequest
import java.io.File
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class WeatherFragment
/**
 * My Map Api key= AIzaSyAskbLzNk57OECP2rRWBpGZ4nj_LHpKVCk
 */
    : Fragment() {
    private var apiList: ArrayList<String> = ArrayList()
    private var JsonUrl: String? = null
    private var list: ArrayList<String> = ArrayList()
    private var cityname: String? = null
    private lateinit var bounce: Animation
    private var gps_enabled = false
    private var network_enabled = false
    private var dataPasser: OnDataPass? = null
    private var lat: Double? = null
    private var lon: Double? = null
    private var countryCode: String? = null
    private var addresses: ArrayList<Address> = ArrayList()
    lateinit var binding2: SettingLayoutBinding
    private var rotate: RotateAnimation? = null
    private var binding: FragmentWeatherBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWeatherBinding.inflate(inflater, container, false)
        list = ArrayList()
        Log.d("hgfkj", "request")
        if (activity != null) {
            val sharedPref1 = SharedPref1(activity)
            when (sharedPref1.theme) {
                "Light" -> {
                    val res = resources //resource handle
                    val drawable = res.getDrawable(R.drawable.main_design_white)
                    binding!!.relLayout.background = drawable
                    binding!!.swipUp2.setTextColor(Color.parseColor("#000000"))
                    binding!!.swipUp.setImageResource(R.drawable.ic_up_arow_black)
                }
                "Dark" -> {
                    val res = resources //resource handle
                    val drawable = res.getDrawable(R.drawable.main_design)
                    binding!!.relLayout.background = drawable
                    binding!!.swipUp2.setTextColor(Color.parseColor("#FFFFFF"))
                    binding!!.swipUp.setImageResource(R.drawable.ic_up_arow)
                }
                else -> {
                    when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                        Configuration.UI_MODE_NIGHT_YES -> {
                            val res = resources //resource handle
                            val drawable = res.getDrawable(R.drawable.main_design)
                            binding!!.relLayout.background = drawable
                            binding!!.swipUp2.setTextColor(Color.parseColor("#FFFFFF"))
                            binding!!.swipUp.setImageResource(R.drawable.ic_up_arow)
                        }
                        Configuration.UI_MODE_NIGHT_NO -> {
                            val res1 = resources //resource handle
                            val drawable1 = res1.getDrawable(R.drawable.main_design_white)
                            binding!!.relLayout.background = drawable1
                            binding!!.swipUp2.setTextColor(Color.parseColor("#000000"))
                            binding!!.swipUp.setImageResource(R.drawable.ic_up_arow_black)
                        }
                    }
                }
            }
        }
        bounce = AnimationUtils.loadAnimation(activity, R.anim.bounce)
        bounce.repeatCount = Animation.INFINITE
        bounce.repeatMode = Animation.INFINITE
        bounce.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                bounce = AnimationUtils.loadAnimation(activity, R.anim.bounce)
                bounce.repeatCount = Animation.INFINITE
                bounce.repeatMode = Animation.INFINITE
                binding!!.swipUp.startAnimation(bounce)
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        binding!!.swipUp.startAnimation(bounce)
        rotate = RotateAnimation(
            0F,
            360F,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        rotate!!.duration = 5000
        rotate!!.interpolator = LinearInterpolator()
        rotate!!.repeatCount = Animation.INFINITE
        rotate!!.repeatMode = Animation.INFINITE
        rotate!!.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                rotate = RotateAnimation(
                    0F,
                    360F,
                    Animation.RELATIVE_TO_SELF,
                    0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f
                )
                rotate!!.duration = 5000
                rotate!!.interpolator = LinearInterpolator()
                rotate!!.repeatCount = Animation.INFINITE
                rotate!!.repeatMode = Animation.INFINITE
                binding!!.settingImg.startAnimation(rotate)
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        binding!!.settingImg.startAnimation(rotate)
        binding!!.settingImg.setOnClickListener { v: View? -> SettingDialog(activity) }
        /*        binding.settingImg.setOnClickListener(v -> startActivity(new Intent(getActivity(), SettingsActivityMain.class)));*/if (Connectivity.isConnected(
                activity
            ) && Connectivity.isConnectedMobile(activity) && Connectivity.isConnectedFast(activity) ||
            Connectivity.isConnected(activity) && Connectivity.isConnectedWifi(activity) && Connectivity.isConnectedFast(
                activity
            )
        ) {
            requestStoragePermission()
            Quotes()
        } else {
            showDialg(activity)
        }
        return binding!!.root
    }

    @SuppressLint("MissingPermission")
    fun findWeather() {
        if (activity != null) {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(
                requireActivity()
            )
            apiList = ArrayList()
            apiList.add("1f01ced93d6608528a3bc65ad580f9e4")
            apiList.add("ec55ea59368f44782fb4dcb6ab028f5a")
            apiList.add("4256b9145e7841dad1aa07b8b3ca5be3")
            apiList.add("667dcb63169e18e220f8ade175d2b016")
            apiList.add("6a1565969d4149752e9fa55a7bec0720")
            fusedLocationClient.lastLocation
                .addOnSuccessListener(requireActivity()) { location: Location? ->
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        // Logic to handle location object
                        val gcd = Geocoder(requireActivity().baseContext, Locale.getDefault())
                        try {
                            addresses =
                                gcd.getFromLocation(location.latitude, location.longitude, 1) as ArrayList<Address>
                            if (addresses.size > 0) {
                                cityname = addresses[0].locality
                                lat = addresses[0].latitude
                                lon = addresses[0].longitude
                                countryCode = addresses.get(0).countryCode
                                val request = LocationRequest()
                                request.interval = (10 * 60 * 1000).toLong()
                                request.maxWaitTime = (60 * 60 * 1000).toLong()
                                val random = Random()
                                val n = random.nextInt(apiList.size)
                                if (cityname == null) {
                                    cityname = "Jaipur"
                                }
                                Log.d("cityName", cityname!!)
                                JsonUrl =
                                    "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&q=" + cityname + "," + countryCode + "&appid=" + apiList.get(
                                        n
                                    )
                                val jor = JsonObjectRequest(
                                    Request.Method.GET,
                                    JsonUrl,
                                    null,
                                    { response: JSONObject ->
                                        Log.d("fjgj", JsonUrl!!)
                                        try {
                                            val main_Object = response.getJSONObject("main")
                                            val array = response.getJSONArray("weather")
                                            val `object` = array.getJSONObject(0)
                                            val temp = main_Object.getDouble("temp").toString()
                                                .substring(0, 2)
                                            val description = `object`.getString("description")
                                            val city = response.getString("name")
                                            val icon = `object`.getString("icon")
                                            val iconUrl =
                                                "http://openweathermap.org/img/w/$icon.png"

//                                            binding.sunImg.setImageURI(Uri.fromFile(new File("http://openweathermap.org/img/wn/" + icon + "@2x.png")));
                                            Picasso.get()
                                                .load(iconUrl)
                                                .resize(24, 24).into(binding!!.sunImg)
                                            Log.d(
                                                "iconImg",
                                                "http://openweathermap.org/img/wn/$icon@2x.png"
                                            )

//                                            Glide.with(getActivity()).load("http://openweathermap.org/img/wn/"+icon+"@2x.png").into(binding.sunImg);
                                            binding!!.temp.text = "$temp°C"
                                            binding!!.city.text = city
                                            binding!!.desc.text = description
                                            dataPasser!!.onDataPass(description)
                                            val calendar = Calendar.getInstance()
                                            val sdf = SimpleDateFormat("EEEE-MM-yyyy")
                                            val formated_date = sdf.format(calendar.time)
                                            binding!!.date.text = formated_date
                                        } catch (e: JSONException) {
                                            e.printStackTrace()
                                        }
                                    }) { error: VolleyError ->
                                    val response = error.networkResponse
                                    /*if (response != null && response.statusCode == 404) {
                                        try {
                                            val res = String(
                                                response.data,
                                                HttpHeaderParser.parseCharset(
                                                    response.headers,
                                                    "utf-8"
                                                )
                                            )
                                            // Now you can use any deserializer to make sense of data
                                            val obj = JSONObject(res)
                                            //use this json as you want
                                        } catch (e1: UnsupportedEncodingException) {
                                            // Couldn't properly decode data to string
                                            e1.printStackTrace()
                                        } // returned data is not JSONObject?
                                        catch (e1: JSONException) {
                                            e1.printStackTrace()
                                        }
                                    }*/
                                }
                                jor.setShouldCache(false)
                                val requestQueue = Volley.newRequestQueue(activity)
                                jor.retryPolicy = DefaultRetryPolicy(
                                    3000,
                                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                                )
                                requestQueue.add(jor)
                            }
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    } else {
                        if (checkLocationON()) {
                            findWeather()
                        } else {
                            checkGpsStatus()
                        }
                    }
                }
        }
    }

    private fun requestStoragePermission() {
        // Here, thisActivity is the current activity
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("hgfkj", "requestStoragePermission: 007")
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_CODE
            )
        } else {
            // Permission has already been granted
            Log.d("hgfkj", "requestStoragePermission: 009")
            if (checkLocationON()) {
                findWeather()
            } else {
                checkGpsStatus()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        Log.d("hgfkj", "onRequestPermissionsResult:989 ")
        if (requestCode == PERMISSION_REQUEST_CODE) { // If request is cancelled, the result arrays are empty.
            if (grantResults.isNotEmpty()
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                if (checkLocationON()) {
                    findWeather()
                } else {
                    checkGpsStatus()
                }
            } // permission denied, boo! Disable the
            // functionality that depends on this permission.

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataPasser = context as OnDataPass
    }

    fun checkGpsStatus() {
        if (activity != null) {
            val locationManager =
                requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
            try {
                if (locationManager != null) {
                    gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                }
            } catch (ignored: Exception) {
            }
            try {
                if (locationManager != null) {
                    network_enabled =
                        locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                }
            } catch (ignored: Exception) {
            }
            if (!gps_enabled && !network_enabled) {
                // notify user
                AlertDialog.Builder(requireActivity())
                    .setMessage("GPS is not enabled")
                    .setPositiveButton("Open Location Setting") { _: DialogInterface?, paramInt: Int ->
                        if (activity != null) {
                            requireActivity().startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                        }
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }
            if (checkLocationON()) {
                findWeather()
            }
        }
    }

    fun checkLocationON(): Boolean {
        if (activity != null) {
            val locationManager =
                requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
            try {
                if (locationManager != null) {
                    gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                }
            } catch (ignored: Exception) {
            }
            try {
                if (locationManager != null) {
                    network_enabled =
                        locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                }
            } catch (ignored: Exception) {
            }

            // notify user
            return gps_enabled || network_enabled
        }
        return false
    }

    override fun onResume() {
        super.onResume()
        if (Connectivity.isConnected(activity)) {
            if (checkLocationON()) {
                findWeather()
            }
        } else {
            showDialg(activity)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            findWeather()
        }
    }

    fun showDialg(activity: Activity?) {
        val dialog1 = Dialog(requireActivity(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen)
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog1.setCancelable(false)
        dialog1.setContentView(R.layout.internet_connection)
        val connectInternet: MaterialButton = dialog1.findViewById(R.id.internet)
        connectInternet.setOnClickListener {
            val i = Intent(Settings.ACTION_WIRELESS_SETTINGS)
            startActivity(i)
        }
        dialog1.show()
        if (Connectivity.isConnected(getActivity())) {
            dialog1.dismiss()
        }
    }

    /**
     * Setting Dialog System
     *
     * @param activity
     */
    private fun SettingDialog(activity: Activity?) {
        val dialog = Dialog(requireActivity(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.show()
        binding2 = SettingLayoutBinding.inflate(
            LayoutInflater.from(
                context
            )
        )
        dialog.setContentView(binding2.root)
        binding2.deleteDialog.setOnClickListener { view: View? -> deleteCache(activity) }
        analyseStorage(context)
        val pref = SharedPref1(activity)
        binding2.shareCard.setOnClickListener { view: View? ->
            try {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Weather Wall")
                var shareMessage = "\nDownload this application from PlayStore\n\n"
                shareMessage =
                    shareMessage + "https://play.google.com/store/apps/details?id=com.client.vpman.weatherwall"
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Weather Wall$shareMessage")
                startActivity(Intent.createChooser(shareIntent, "choose one"))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        binding2.reportCard.setOnClickListener { view: View? ->
            val emailIntent = Intent(
                Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", "vp.mannu.kr@gmail.com", null
                )
            )
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback")
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Weather Wall")
            startActivity(Intent.createChooser(emailIntent, "Send email..."))
        }
        binding2.ratusCard.setOnClickListener { view: View? ->
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=com.client.vpman.weatherwall")
            )
            startActivity(browserIntent)
        }
        binding2.instagramCard.setOnClickListener { view: View? ->
            val browserIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/weather_wall/"))
            startActivity(browserIntent)
        }
        binding2.privacyCard.setOnClickListener { view: View? ->
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://weather-wall.flycricket.io/privacy.html")
            )
            startActivity(browserIntent)
        }
        binding2.gitHubCard.setOnClickListener { view: View? ->
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://github.com/vedprakashsah1998/WeatherWall")
            )
            startActivity(browserIntent)
        }
        binding2.facebookCard.setOnClickListener { view: View? ->
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.facebook.com/Weather-Wall-104577191240236/")
            )
            startActivity(browserIntent)
        }
        binding2.linkedinCard.setOnClickListener { view: View? ->
            val browserIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/vedprakash1998/"))
            startActivity(browserIntent)
        }
        binding2.imgQualityCard.setOnClickListener { view: View? -> QualityDialog(activity) }
        binding2.setImgQlty.text = pref.imageLoadQuality
        binding2.downloadImgQuality.setOnClickListener { view: View? -> QualityDialog1(activity) }
        binding2.downloadQlty.text = pref.imageQuality
        if (pref.imageLoadQuality == "") {
            pref.imageLoadQuality = "Default"
            binding2.setImgQlty.text = pref.imageLoadQuality
        }
        Log.d("imgQlty", pref.imageLoadQuality)
        if (pref.imageQuality == "") {
            pref.imageQuality = "Default"
            binding2.downloadQlty.text = pref.imageQuality
        }
        binding2.stickySwitch1.setOnCheckedChangeListener { view: SwitchButton?, isChecked: Boolean ->
            if (!isChecked) {
                pref.theme = "Light"
                binding2.carrierTheme.text = "Light"
                binding2.sampleThemeImg.setImageResource(R.drawable.ic_moon)
                binding2.sampleThemeImg.background =
                    resources.getDrawable(R.drawable.round_button_white)
                binding2.setTheme.setTextColor(Color.parseColor("#000000"))
                binding2.mainSettingLayout.setBackgroundColor(Color.parseColor("#F2F6F9"))
                binding2.backres.setImageResource(R.drawable.ic_baseline_arrow_back_24)
                binding2.settingTitle.setTextColor(Color.parseColor("#000000"))
                binding2.settingCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
                binding2.deleteDialog.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
                binding2.clearImg.background =
                    resources.getDrawable(R.drawable.round_button_white)
                binding2.clearImg.setColorFilter(
                    Color.parseColor("#000000"),
                    PorterDuff.Mode.MULTIPLY
                )
                binding2.clearCache.setTextColor(Color.parseColor("#000000"))
                binding2.imgQualityCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
                binding2.setQualityCard.background =
                    resources.getDrawable(R.drawable.round_button_white)
                binding2.setQualtiText.setTextColor(Color.parseColor("#000000"))
                binding2.downloadQuality.background =
                    resources.getDrawable(R.drawable.round_button_white)
                binding2.downloadImgQuality.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
                binding2.downloadText.setTextColor(Color.parseColor("#000000"))
                binding2.shareCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
                //                binding2.shareImg.setColorFilter(Color.parseColor("#FFFFFF"), android.graphics.PorterDuff.Mode.MULTIPLY);
                binding2.shareImg.setImageResource(R.drawable.ic_share_black_24dp)
                binding2.shareText.setTextColor(Color.parseColor("#000000"))
                binding2.shareImg.background =
                    resources.getDrawable(R.drawable.round_button_white)
                binding2.reportCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
                binding2.reportImg.background =
                    resources.getDrawable(R.drawable.round_button_white)
                binding2.reportText.setTextColor(Color.parseColor("#000000"))
                binding2.ratusCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
                binding2.rateUsImg.background =
                    resources.getDrawable(R.drawable.round_button_white)
                binding2.ratUsText.setTextColor(Color.parseColor("#000000"))
                binding2.privacyCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
                binding2.privacyImg.background =
                    resources.getDrawable(R.drawable.round_button_white)
                binding2.privacyText.setTextColor(Color.parseColor("#000000"))
                binding2.facebookCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
                binding2.fbText.background = resources.getDrawable(R.drawable.round_button_white)
                binding2.fbTextFull.setTextColor(Color.parseColor("#000000"))
                binding2.instagramCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
                binding2.instaImg.background =
                    resources.getDrawable(R.drawable.round_button_white)
                binding2.instatext.setTextColor(Color.parseColor("#000000"))
                binding2.linkedinCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
                binding2.linkedinText.background =
                    resources.getDrawable(R.drawable.round_button_white)
                binding2.linkedinTextData.setTextColor(Color.parseColor("#000000"))
                binding2.gitHubCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
                binding2.gitHubImg.background =
                    resources.getDrawable(R.drawable.round_button_white)
                binding2.githubText.setTextColor(Color.parseColor("#000000"))
                binding2.gitHubImg.setImageResource(R.drawable.ic_logo)
                binding2.pexelsCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
                binding2.pexelsImg.setImageResource(R.drawable.pexels)
                binding2.flaticonCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
                binding2.flaticonText.setTextColor(Color.parseColor("#000000"))
                binding2.smallDesc.setTextColor(Color.parseColor("#000000"))
            } else {
                pref.theme = "Dark"
                binding2.carrierTheme.text = "Dark"
                binding2.sampleThemeImg.setImageResource(R.drawable.ic_sun)
                binding2.sampleThemeImg.background =
                    resources.getDrawable(R.drawable.round_button)
                binding2.setTheme.setTextColor(Color.parseColor("#FFFFFF"))
                binding2.mainSettingLayout.setBackgroundColor(Color.parseColor("#000000"))
                binding2.backres.setImageResource(R.drawable.ic_baseline_arrow_back_24_white)
                binding2.settingTitle.setTextColor(Color.parseColor("#FFFFFF"))
                binding2.settingCard.setCardBackgroundColor(Color.parseColor("#2F2F31"))
                binding2.deleteDialog.setCardBackgroundColor(Color.parseColor("#2F2F31"))
                binding2.clearCache.setTextColor(Color.parseColor("#FFFFFF"))
                binding2.clearImg.background = resources.getDrawable(R.drawable.round_button)
                binding2.clearImg.setColorFilter(
                    Color.parseColor("#FFFFFF"),
                    PorterDuff.Mode.MULTIPLY
                )
                binding2.imgQualityCard.setCardBackgroundColor(Color.parseColor("#2F2F31"))
                binding2.setQualityCard.background =
                    resources.getDrawable(R.drawable.round_button)
                binding2.setQualtiText.setTextColor(Color.parseColor("#FFFFFF"))
                binding2.downloadQuality.background =
                    resources.getDrawable(R.drawable.round_button)
                binding2.downloadImgQuality.setCardBackgroundColor(Color.parseColor("#2F2F31"))
                binding2.downloadText.setTextColor(Color.parseColor("#FFFFFF"))
                binding2.shareCard.setCardBackgroundColor(Color.parseColor("#2F2F31"))
                binding2.shareImg.setImageResource(R.drawable.ic_share_white_24dp)
                binding2.shareText.setTextColor(Color.parseColor("#FFFFFF"))
                binding2.shareImg.background = resources.getDrawable(R.drawable.round_button)
                binding2.reportCard.setCardBackgroundColor(Color.parseColor("#2F2F31"))
                binding2.reportImg.background = resources.getDrawable(R.drawable.round_button)
                binding2.reportText.setTextColor(Color.parseColor("#FFFFFF"))
                binding2.ratusCard.setCardBackgroundColor(Color.parseColor("#2F2F31"))
                binding2.rateUsImg.background = resources.getDrawable(R.drawable.round_button)
                binding2.ratUsText.setTextColor(Color.parseColor("#FFFFFF"))
                binding2.privacyCard.setCardBackgroundColor(Color.parseColor("#2F2F31"))
                binding2.privacyImg.background = resources.getDrawable(R.drawable.round_button)
                binding2.privacyText.setTextColor(Color.parseColor("#FFFFFF"))
                binding2.facebookCard.setCardBackgroundColor(Color.parseColor("#2F2F31"))
                binding2.fbText.background = resources.getDrawable(R.drawable.round_button)
                binding2.fbTextFull.setTextColor(Color.parseColor("#FFFFFF"))
                binding2.instagramCard.setCardBackgroundColor(Color.parseColor("#2F2F31"))
                binding2.instaImg.background = resources.getDrawable(R.drawable.round_button)
                binding2.instatext.setTextColor(Color.parseColor("#FFFFFF"))
                binding2.linkedinCard.setCardBackgroundColor(Color.parseColor("#2F2F31"))
                binding2.linkedinText.background = resources.getDrawable(R.drawable.round_button)
                binding2.linkedinTextData.setTextColor(Color.parseColor("#FFFFFF"))
                binding2.gitHubCard.setCardBackgroundColor(Color.parseColor("#2F2F31"))
                binding2.gitHubImg.background = resources.getDrawable(R.drawable.round_button)
                binding2.githubText.setTextColor(Color.parseColor("#FFFFFF"))
                binding2.gitHubImg.setImageResource(R.drawable.ic_logo_white)
                binding2.pexelsCard.setCardBackgroundColor(Color.parseColor("#2F2F31"))
                binding2.pexelsImg.setImageResource(R.drawable.pexels_white)
                binding2.flaticonCard.setCardBackgroundColor(Color.parseColor("#2F2F31"))
                binding2.flaticonText.setTextColor(Color.parseColor("#FFFFFF"))
                binding2.smallDesc.setTextColor(Color.parseColor("#FFFFFF"))
            }
        }
        if (pref.theme == "Light") {
            binding2.stickySwitch1.isChecked = false
            pref.theme = "Light"
            binding2.carrierTheme.text = "Light"
            binding2.sampleThemeImg.setImageResource(R.drawable.ic_moon)
            binding2.sampleThemeImg.background =
                resources.getDrawable(R.drawable.round_button_white)
            binding2.setTheme.setTextColor(Color.parseColor("#000000"))
            binding2.mainSettingLayout.setBackgroundColor(Color.parseColor("#F2F6F9"))
            binding2.backres.setImageResource(R.drawable.ic_baseline_arrow_back_24)
            binding2.settingTitle.setTextColor(Color.parseColor("#000000"))
            binding2.settingCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
            binding2.deleteDialog.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
            binding2.clearImg.background = resources.getDrawable(R.drawable.round_button_white)
            binding2.clearImg.setColorFilter(
                Color.parseColor("#000000"),
                PorterDuff.Mode.MULTIPLY
            )
            binding2.clearCache.setTextColor(Color.parseColor("#000000"))
            binding2.imgQualityCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
            binding2.setQualityCard.background =
                resources.getDrawable(R.drawable.round_button_white)
            binding2.setQualtiText.setTextColor(Color.parseColor("#000000"))
            binding2.downloadQuality.background =
                resources.getDrawable(R.drawable.round_button_white)
            binding2.downloadImgQuality.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
            binding2.downloadText.setTextColor(Color.parseColor("#000000"))
            binding2.shareCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
            //                binding2.shareImg.setColorFilter(Color.parseColor("#FFFFFF"), android.graphics.PorterDuff.Mode.MULTIPLY);
            binding2.shareImg.setImageResource(R.drawable.ic_share_black_24dp)
            binding2.shareText.setTextColor(Color.parseColor("#000000"))
            binding2.shareImg.background = resources.getDrawable(R.drawable.round_button_white)
            binding2.reportCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
            binding2.reportImg.background = resources.getDrawable(R.drawable.round_button_white)
            binding2.reportText.setTextColor(Color.parseColor("#000000"))
            binding2.ratusCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
            binding2.rateUsImg.background = resources.getDrawable(R.drawable.round_button_white)
            binding2.ratUsText.setTextColor(Color.parseColor("#000000"))
            binding2.privacyCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
            binding2.privacyImg.background = resources.getDrawable(R.drawable.round_button_white)
            binding2.privacyText.setTextColor(Color.parseColor("#000000"))
            binding2.facebookCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
            binding2.fbText.background = resources.getDrawable(R.drawable.round_button_white)
            binding2.fbTextFull.setTextColor(Color.parseColor("#000000"))
            binding2.instagramCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
            binding2.instaImg.background = resources.getDrawable(R.drawable.round_button_white)
            binding2.instatext.setTextColor(Color.parseColor("#000000"))
            binding2.linkedinCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
            binding2.linkedinText.background =
                resources.getDrawable(R.drawable.round_button_white)
            binding2.linkedinTextData.setTextColor(Color.parseColor("#000000"))
            binding2.gitHubCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
            binding2.gitHubImg.background = resources.getDrawable(R.drawable.round_button_white)
            binding2.githubText.setTextColor(Color.parseColor("#000000"))
            binding2.gitHubImg.setImageResource(R.drawable.ic_logo)
            binding2.pexelsCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
            binding2.pexelsImg.setImageResource(R.drawable.pexels)
            binding2.flaticonCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
            binding2.flaticonText.setTextColor(Color.parseColor("#000000"))
            binding2.smallDesc.setTextColor(Color.parseColor("#000000"))
        } else if (pref.theme == "Dark") {
            binding2.stickySwitch1.isChecked = true
            pref.theme = "Dark"
            binding2.carrierTheme.text = "Dark"
            binding2.sampleThemeImg.setImageResource(R.drawable.ic_sun)
            binding2.sampleThemeImg.background = resources.getDrawable(R.drawable.round_button)
            binding2.setTheme.setTextColor(Color.parseColor("#FFFFFF"))
            binding2.mainSettingLayout.setBackgroundColor(Color.parseColor("#000000"))
            binding2.backres.setImageResource(R.drawable.ic_baseline_arrow_back_24_white)
            binding2.settingTitle.setTextColor(Color.parseColor("#FFFFFF"))
            binding2.settingCard.setCardBackgroundColor(Color.parseColor("#2F2F31"))
            binding2.deleteDialog.setCardBackgroundColor(Color.parseColor("#2F2F31"))
            binding2.clearCache.setTextColor(Color.parseColor("#FFFFFF"))
            binding2.clearImg.background = resources.getDrawable(R.drawable.round_button)
            binding2.clearImg.setColorFilter(
                Color.parseColor("#FFFFFF"),
                PorterDuff.Mode.MULTIPLY
            )
            binding2.imgQualityCard.setCardBackgroundColor(Color.parseColor("#2F2F31"))
            binding2.setQualityCard.background = resources.getDrawable(R.drawable.round_button)
            binding2.setQualtiText.setTextColor(Color.parseColor("#FFFFFF"))
            binding2.downloadQuality.background = resources.getDrawable(R.drawable.round_button)
            binding2.downloadImgQuality.setCardBackgroundColor(Color.parseColor("#2F2F31"))
            binding2.downloadText.setTextColor(Color.parseColor("#FFFFFF"))
            binding2.shareCard.setCardBackgroundColor(Color.parseColor("#2F2F31"))
            binding2.shareImg.setImageResource(R.drawable.ic_share_white_24dp)
            binding2.shareText.setTextColor(Color.parseColor("#FFFFFF"))
            binding2.shareImg.background = resources.getDrawable(R.drawable.round_button)
            binding2.reportCard.setCardBackgroundColor(Color.parseColor("#2F2F31"))
            binding2.reportImg.background = resources.getDrawable(R.drawable.round_button)
            binding2.reportText.setTextColor(Color.parseColor("#FFFFFF"))
            binding2.ratusCard.setCardBackgroundColor(Color.parseColor("#2F2F31"))
            binding2.rateUsImg.background = resources.getDrawable(R.drawable.round_button)
            binding2.ratUsText.setTextColor(Color.parseColor("#FFFFFF"))
            binding2.privacyCard.setCardBackgroundColor(Color.parseColor("#2F2F31"))
            binding2.privacyImg.background = resources.getDrawable(R.drawable.round_button)
            binding2.privacyText.setTextColor(Color.parseColor("#FFFFFF"))
            binding2.facebookCard.setCardBackgroundColor(Color.parseColor("#2F2F31"))
            binding2.fbText.background = resources.getDrawable(R.drawable.round_button)
            binding2.fbTextFull.setTextColor(Color.parseColor("#FFFFFF"))
            binding2.instagramCard.setCardBackgroundColor(Color.parseColor("#2F2F31"))
            binding2.instaImg.background = resources.getDrawable(R.drawable.round_button)
            binding2.instatext.setTextColor(Color.parseColor("#FFFFFF"))
            binding2.linkedinCard.setCardBackgroundColor(Color.parseColor("#2F2F31"))
            binding2.linkedinText.background = resources.getDrawable(R.drawable.round_button)
            binding2.linkedinTextData.setTextColor(Color.parseColor("#FFFFFF"))
            binding2.gitHubCard.setCardBackgroundColor(Color.parseColor("#2F2F31"))
            binding2.gitHubImg.background = resources.getDrawable(R.drawable.round_button)
            binding2.githubText.setTextColor(Color.parseColor("#FFFFFF"))
            binding2.gitHubImg.setImageResource(R.drawable.ic_logo_white)
            binding2.pexelsCard.setCardBackgroundColor(Color.parseColor("#2F2F31"))
            binding2.pexelsImg.setImageResource(R.drawable.pexels_white)
            binding2.flaticonCard.setCardBackgroundColor(Color.parseColor("#2F2F31"))
            binding2.flaticonText.setTextColor(Color.parseColor("#FFFFFF"))
            binding2.smallDesc.setTextColor(Color.parseColor("#FFFFFF"))
        } else {
            when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    binding2.stickySwitch1.isChecked = true
                    binding2.stickySwitch1.isChecked = true
                    pref.theme = "Dark"
                    binding2.carrierTheme.text = "Dark"
                    binding2.sampleThemeImg.setImageResource(R.drawable.ic_sun)
                    binding2.sampleThemeImg.background =
                        resources.getDrawable(R.drawable.round_button)
                    binding2.setTheme.setTextColor(Color.parseColor("#FFFFFF"))
                    binding2.mainSettingLayout.setBackgroundColor(Color.parseColor("#000000"))
                    binding2.backres.setImageResource(R.drawable.ic_baseline_arrow_back_24_white)
                    binding2.settingTitle.setTextColor(Color.parseColor("#FFFFFF"))
                    binding2.settingCard.setCardBackgroundColor(Color.parseColor("#2F2F31"))
                    binding2.deleteDialog.setCardBackgroundColor(Color.parseColor("#2F2F31"))
                    binding2.clearCache.setTextColor(Color.parseColor("#FFFFFF"))
                    binding2.clearImg.background = resources.getDrawable(R.drawable.round_button)
                    binding2.clearImg.setColorFilter(
                        Color.parseColor("#FFFFFF"),
                        PorterDuff.Mode.MULTIPLY
                    )
                    binding2.imgQualityCard.setCardBackgroundColor(Color.parseColor("#2F2F31"))
                    binding2.setQualityCard.background =
                        resources.getDrawable(R.drawable.round_button)
                    binding2.setQualtiText.setTextColor(Color.parseColor("#FFFFFF"))
                    binding2.downloadQuality.background =
                        resources.getDrawable(R.drawable.round_button)
                    binding2.downloadImgQuality.setCardBackgroundColor(Color.parseColor("#2F2F31"))
                    binding2.downloadText.setTextColor(Color.parseColor("#FFFFFF"))
                    binding2.shareCard.setCardBackgroundColor(Color.parseColor("#2F2F31"))
                    binding2.shareImg.setImageResource(R.drawable.ic_share_white_24dp)
                    binding2.shareText.setTextColor(Color.parseColor("#FFFFFF"))
                    binding2.shareImg.background = resources.getDrawable(R.drawable.round_button)
                    binding2.reportCard.setCardBackgroundColor(Color.parseColor("#2F2F31"))
                    binding2.reportImg.background = resources.getDrawable(R.drawable.round_button)
                    binding2.reportText.setTextColor(Color.parseColor("#FFFFFF"))
                    binding2.ratusCard.setCardBackgroundColor(Color.parseColor("#2F2F31"))
                    binding2.rateUsImg.background = resources.getDrawable(R.drawable.round_button)
                    binding2.ratUsText.setTextColor(Color.parseColor("#FFFFFF"))
                    binding2.privacyCard.setCardBackgroundColor(Color.parseColor("#2F2F31"))
                    binding2.privacyImg.background =
                        resources.getDrawable(R.drawable.round_button)
                    binding2.privacyText.setTextColor(Color.parseColor("#FFFFFF"))
                    binding2.facebookCard.setCardBackgroundColor(Color.parseColor("#2F2F31"))
                    binding2.fbText.background = resources.getDrawable(R.drawable.round_button)
                    binding2.fbTextFull.setTextColor(Color.parseColor("#FFFFFF"))
                    binding2.instagramCard.setCardBackgroundColor(Color.parseColor("#2F2F31"))
                    binding2.instaImg.background = resources.getDrawable(R.drawable.round_button)
                    binding2.instatext.setTextColor(Color.parseColor("#FFFFFF"))
                    binding2.linkedinCard.setCardBackgroundColor(Color.parseColor("#2F2F31"))
                    binding2.linkedinText.background =
                        resources.getDrawable(R.drawable.round_button)
                    binding2.linkedinTextData.setTextColor(Color.parseColor("#FFFFFF"))
                    binding2.gitHubCard.setCardBackgroundColor(Color.parseColor("#2F2F31"))
                    binding2.gitHubImg.background = resources.getDrawable(R.drawable.round_button)
                    binding2.githubText.setTextColor(Color.parseColor("#FFFFFF"))
                    binding2.gitHubImg.setImageResource(R.drawable.ic_logo_white)
                    binding2.pexelsCard.setCardBackgroundColor(Color.parseColor("#2F2F31"))
                    binding2.pexelsImg.setImageResource(R.drawable.pexels_white)
                    binding2.flaticonCard.setCardBackgroundColor(Color.parseColor("#2F2F31"))
                    binding2.flaticonText.setTextColor(Color.parseColor("#FFFFFF"))
                    binding2.smallDesc.setTextColor(Color.parseColor("#FFFFFF"))
                }
                Configuration.UI_MODE_NIGHT_NO -> {
                    binding2.stickySwitch1.isChecked = false
                    pref.theme = "Light"
                    binding2.carrierTheme.text = "Light"
                    binding2.sampleThemeImg.setImageResource(R.drawable.ic_moon)
                    binding2.sampleThemeImg.background =
                        resources.getDrawable(R.drawable.round_button_white)
                    binding2.setTheme.setTextColor(Color.parseColor("#000000"))
                    binding2.mainSettingLayout.setBackgroundColor(Color.parseColor("#F2F6F9"))
                    binding2.backres.setImageResource(R.drawable.ic_baseline_arrow_back_24)
                    binding2.settingTitle.setTextColor(Color.parseColor("#000000"))
                    binding2.settingCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
                    binding2.deleteDialog.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
                    binding2.clearImg.background =
                        resources.getDrawable(R.drawable.round_button_white)
                    binding2.clearImg.setColorFilter(
                        Color.parseColor("#000000"),
                        PorterDuff.Mode.MULTIPLY
                    )
                    binding2.clearCache.setTextColor(Color.parseColor("#000000"))
                    binding2.imgQualityCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
                    binding2.setQualityCard.background =
                        resources.getDrawable(R.drawable.round_button_white)
                    binding2.setQualtiText.setTextColor(Color.parseColor("#000000"))
                    binding2.downloadQuality.background =
                        resources.getDrawable(R.drawable.round_button_white)
                    binding2.downloadImgQuality.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
                    binding2.downloadText.setTextColor(Color.parseColor("#000000"))
                    binding2.shareCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
                    //                binding2.shareImg.setColorFilter(Color.parseColor("#FFFFFF"), android.graphics.PorterDuff.Mode.MULTIPLY);
                    binding2.shareImg.setImageResource(R.drawable.ic_share_black_24dp)
                    binding2.shareText.setTextColor(Color.parseColor("#000000"))
                    binding2.shareImg.background =
                        resources.getDrawable(R.drawable.round_button_white)
                    binding2.reportCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
                    binding2.reportImg.background =
                        resources.getDrawable(R.drawable.round_button_white)
                    binding2.reportText.setTextColor(Color.parseColor("#000000"))
                    binding2.ratusCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
                    binding2.rateUsImg.background =
                        resources.getDrawable(R.drawable.round_button_white)
                    binding2.ratUsText.setTextColor(Color.parseColor("#000000"))
                    binding2.privacyCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
                    binding2.privacyImg.background =
                        resources.getDrawable(R.drawable.round_button_white)
                    binding2.privacyText.setTextColor(Color.parseColor("#000000"))
                    binding2.facebookCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
                    binding2.fbText.background =
                        resources.getDrawable(R.drawable.round_button_white)
                    binding2.fbTextFull.setTextColor(Color.parseColor("#000000"))
                    binding2.instagramCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
                    binding2.instaImg.background =
                        resources.getDrawable(R.drawable.round_button_white)
                    binding2.instatext.setTextColor(Color.parseColor("#000000"))
                    binding2.linkedinCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
                    binding2.linkedinText.background =
                        resources.getDrawable(R.drawable.round_button_white)
                    binding2.linkedinTextData.setTextColor(Color.parseColor("#000000"))
                    binding2.gitHubCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
                    binding2.gitHubImg.background =
                        resources.getDrawable(R.drawable.round_button_white)
                    binding2.githubText.setTextColor(Color.parseColor("#000000"))
                    binding2.gitHubImg.setImageResource(R.drawable.ic_logo)
                    binding2.pexelsCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
                    binding2.pexelsImg.setImageResource(R.drawable.pexels)
                    binding2.flaticonCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
                    binding2.flaticonText.setTextColor(Color.parseColor("#000000"))
                    binding2.smallDesc.setTextColor(Color.parseColor("#000000"))
                }
            }
        }
        binding2.backres.setOnClickListener { view: View? ->
            activity?.overridePendingTransition(0, 0)
            if (parentFragmentManager != null) {
                parentFragmentManager.beginTransaction().detach(this@WeatherFragment)
                    .attach(this@WeatherFragment).commit()
            }
            activity?.recreate()
            activity?.overridePendingTransition(0, 0)
        }
        dialog.setOnCancelListener { _: DialogInterface? ->
            activity?.overridePendingTransition(0, 0)
            if (parentFragmentManager != null) {
                parentFragmentManager.beginTransaction().detach(this@WeatherFragment)
                    .attach(this@WeatherFragment).commit()
            }
            activity?.recreate()
            activity?.overridePendingTransition(0, 0)
        }
    }

    fun Quotes() {
        val QuotesUrl =
            "https://www.forbes.com/forbesapi/thought/uri.json?enrich=true&query=1&relatedlimit=100"
        Log.d("sdfljh", "khwqgdi")
        val stringRequest = StringRequest(Request.Method.GET, QuotesUrl, { response: String? ->
            Log.d("qoefg", response!!)
            var jsonObject: JSONObject? = null
            try {
                jsonObject = JSONObject(response)
                val jsonObject1 = jsonObject.getJSONObject("thought")
                Log.d("qeljg", jsonObject1.toString())
                val jsonArray = jsonObject1.getJSONArray("relatedThemeThoughts")
                for (i in 0 until jsonArray.length()) {
                    val jsonObject2 = jsonArray.getJSONObject(i)
                    list.add(jsonObject2.getString("quote"))
                }
                list.shuffle()
                val random = Random()
                val n = random.nextInt(list.size)
                binding!!.quotesFirst.text = list[n]
                Log.d("asljf", n.toString())
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }) { _: VolleyError? -> }
        stringRequest.setShouldCache(false)
        if (activity != null) {
            val requestQueue = Volley.newRequestQueue(activity)
            stringRequest.retryPolicy = DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
            requestQueue.add(stringRequest)
        }
    }

    fun deleteCache(context: Context?) {
        try {
            val dir = requireContext().cacheDir
            deleteDir(dir)
            analyseStorage(context)
            Toast.makeText(context, "Cache Memory is deleted", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun QualityDialog(activity: Activity?) {
        val dialog = Dialog(requireActivity(), android.R.style.Theme_DeviceDefault_Dialog_NoActionBar)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.show()
        val binding = QualitDialogSpinnerBinding.inflate(
            LayoutInflater.from(
                context
            )
        )
        dialog.setContentView(binding.root)
        val pref = SharedPref1(activity)
        when (pref.theme) {
            "Light" -> {
                binding.downloadQualityDialog.setBackgroundColor(Color.parseColor("#FFFFFF"))
                binding.default1.setTextColor(Color.parseColor("#000000"))
                binding.highQ.setTextColor(Color.parseColor("#000000"))
                binding.titleDialog.setTextColor(Color.parseColor("#000000"))
            }
            "Dark" -> {
                binding.downloadQualityDialog.setBackgroundColor(Color.parseColor("#2F2F31"))
                binding.default1.setTextColor(Color.parseColor("#FFFFFF"))
                binding.highQ.setTextColor(Color.parseColor("#FFFFFF"))
                binding.titleDialog.setTextColor(Color.parseColor("#FFFFFF"))
            }
            else -> {
                when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                    Configuration.UI_MODE_NIGHT_YES -> {
                        binding.downloadQualityDialog.setBackgroundColor(Color.parseColor("#2F2F31"))
                        binding.default1.setTextColor(Color.parseColor("#FFFFFF"))
                        binding.highQ.setTextColor(Color.parseColor("#FFFFFF"))
                        binding.titleDialog.setTextColor(Color.parseColor("#FFFFFF"))
                    }
                    Configuration.UI_MODE_NIGHT_NO -> {
                        binding.downloadQualityDialog.setBackgroundColor(Color.parseColor("#FFFFFF"))
                        binding.default1.setTextColor(Color.parseColor("#000000"))
                        binding.highQ.setTextColor(Color.parseColor("#000000"))
                        binding.titleDialog.setTextColor(Color.parseColor("#000000"))
                    }
                }
            }
        }
        binding.radioGroup.setOnCheckedChangeListener { _: RadioGroup?, i: Int ->
            when (i) {
                R.id.default1 -> {
                    // do operations specific to this selection
                    pref.imageLoadQuality = "Default"
                    binding2.setImgQlty.text = "Default"
                    dialog.dismiss()
                }
                R.id.highQ -> {
                    // do operations specific to this selection
                    pref.imageLoadQuality = "High Quality"
                    binding2.setImgQlty.text = "High Quality"
                    dialog.dismiss()
                }
            }
        }
    }

    fun QualityDialog1(activity: Activity?) {
        val dialog = Dialog(requireActivity(), android.R.style.Theme_DeviceDefault_Dialog_NoActionBar)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.show()
        val binding = LoadImgQualityBinding.inflate(
            LayoutInflater.from(
                context
            )
        )
        dialog.setContentView(binding.root)
        val pref = SharedPref1(activity)
        when (pref.theme) {
            "Light" -> {
                binding.dialogQuality.setBackgroundColor(Color.parseColor("#FFFFFF"))
                binding.default2.setTextColor(Color.parseColor("#000000"))
                binding.highQ1.setTextColor(Color.parseColor("#000000"))
                binding.titleDialog1.setTextColor(Color.parseColor("#000000"))
            }
            "Dark" -> {
                binding.dialogQuality.setBackgroundColor(Color.parseColor("#2F2F31"))
                binding.default2.setTextColor(Color.parseColor("#FFFFFF"))
                binding.highQ1.setTextColor(Color.parseColor("#FFFFFF"))
                binding.titleDialog1.setTextColor(Color.parseColor("#FFFFFF"))
            }
            else -> {
                when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                    Configuration.UI_MODE_NIGHT_YES -> {
                        binding.dialogQuality.setBackgroundColor(Color.parseColor("#2F2F31"))
                        binding.default2.setTextColor(Color.parseColor("#FFFFFF"))
                        binding.highQ1.setTextColor(Color.parseColor("#FFFFFF"))
                        binding.titleDialog1.setTextColor(Color.parseColor("#FFFFFF"))
                    }
                    Configuration.UI_MODE_NIGHT_NO -> {
                        binding.dialogQuality.setBackgroundColor(Color.parseColor("#FFFFFF"))
                        binding.default2.setTextColor(Color.parseColor("#000000"))
                        binding.highQ1.setTextColor(Color.parseColor("#000000"))
                        binding.titleDialog1.setTextColor(Color.parseColor("#000000"))
                    }
                }
            }
        }
        binding.radioGroup1.setOnCheckedChangeListener { radioGroup: RadioGroup?, i: Int ->
            when (i) {
                R.id.default2 -> {
                    // do operations specific to this selection
                    pref.imageQuality = "Default"
                    binding2.downloadQlty.text = "Default"
                    dialog.dismiss()
                }
                R.id.highQ1 -> {
                    // do operations specific to this selection
                    pref.imageQuality = "High Quality"
                    binding2.downloadQlty.text = "High Quality"
                    dialog.dismiss()
                }
            }
        }
    }

    fun analyseStorage(context: Context?) {
        val appBaseFolder = requireContext().filesDir.parentFile
        val totalSize = browseFiles(appBaseFolder)
        binding2.celarData.text = CalculateCacheMemory.convertToStringRepresentation(totalSize)
        Log.d("STORAGE_TAG", "App uses $totalSize total bytes")
    }

    private fun browseFiles(dir: File): Long {
        var dirSize: Long = 0
        for (f in dir.listFiles()) {
            dirSize += f.length()
            Log.d("STORAGE_TAG", dir.absolutePath + "/" + f.name + " uses " + f.length() + " bytes")
            if (f.isDirectory) {
                dirSize += browseFiles(f)
            }
        }
        Log.d("STORAGE_TAG", dir.absolutePath + " uses " + dirSize + " bytes")
        return dirSize
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 200
        @JvmStatic
        fun newInstance(text: String?): WeatherFragment {
            val f = WeatherFragment()
            val b = Bundle()
            b.putString("msg", text)
            f.arguments = b
            return f
        }

        fun deleteDir(dir: File?): Boolean {
            return if (dir != null && dir.isDirectory) {
                val children = dir.list()
                if (children != null) {
                    for (child in children) {
                        val success = deleteDir(File(dir, child))
                        if (!success) {
                            return false
                        }
                    }
                }
                dir.delete()
            } else if (dir != null && dir.isFile) {
                dir.delete()
            } else {
                false
            }
        }
    }
}
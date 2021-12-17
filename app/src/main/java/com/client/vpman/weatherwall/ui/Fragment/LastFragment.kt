package com.client.vpman.weatherwall.ui.Fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.LruCache
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.signature.ObjectKey
import com.client.vpman.weatherwall.CustomeUsefullClass.Constant
import com.client.vpman.weatherwall.CustomeUsefullClass.SharedPref1
import com.client.vpman.weatherwall.CustomeUsefullClass.Utils
import com.client.vpman.weatherwall.R
import com.client.vpman.weatherwall.databinding.FragmentLastBinding
import com.client.vpman.weatherwall.ui.Activity.SearchActivity
import com.client.vpman.weatherwall.ui.Activity.TestingMotionLayout
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.textview.MaterialTextView
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class LastFragment : Fragment() {
    private var apiList: ArrayList<String> = ArrayList()
    var fragment: Fragment? = null
    lateinit var fragmentTransaction: FragmentTransaction
    private lateinit var bounce: Animation

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.Q)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentLastBinding.inflate(inflater, container, false)
        val view: View = binding.root
        bounce = AnimationUtils.loadAnimation(activity, R.anim.bounce)
        bounce.repeatCount = Animation.INFINITE
        bounce.repeatMode = Animation.INFINITE
        bounce.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                bounce = AnimationUtils.loadAnimation(activity, R.anim.bounce)
                bounce.repeatCount = Animation.INFINITE
                bounce.repeatMode = Animation.INFINITE
                binding.SwipUpdisc.startAnimation(bounce)
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        binding.SwipUpdisc.startAnimation(bounce)
        fragment = CuratedList()
        if (activity != null) {
            fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frameLayout, CuratedList())
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            fragmentTransaction.commit()
            val sharedPref1 = SharedPref1(activity)
            when (sharedPref1.theme) {
                "Light" -> {
                    binding.rlLayoutDisc.setBackgroundColor(Color.parseColor("#FFFFFF"))
                    binding.discoverText.setTextColor(Color.parseColor("#1A1A1A"))
                    binding.topic.setTextColor(resources.getColor(R.color.black))
                    binding.category.setTextColor(resources.getColor(R.color.black))
                    binding.searchIcon.setImageResource(R.drawable.ic_loupe)
                    binding.tabLayoutLast.tabTextColors =
                        ColorStateList.valueOf(resources.getColor(R.color.black))
                    binding.SwipUpdisc.setImageResource(R.drawable.ic_up_arow_black)
                }
                "Dark" -> {
                    binding.rlLayoutDisc.setBackgroundColor(Color.parseColor("#000000"))
                    binding.discoverText.setTextColor(Color.parseColor("#FFFFFF"))
                    binding.topic.setTextColor(resources.getColor(R.color.white))
                    binding.category.setTextColor(resources.getColor(R.color.white))
                    binding.SwipUpdisc.setImageResource(R.drawable.ic_up_arow)
                    binding.searchIcon.setImageResource(R.drawable.ic_loupe_white)
                    binding.tabLayoutLast.tabTextColors =
                        ColorStateList.valueOf(resources.getColor(R.color.white))
                }
                else -> {
                    when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                        Configuration.UI_MODE_NIGHT_YES -> {
                            binding.rlLayoutDisc.setBackgroundColor(Color.parseColor("#000000"))
                            binding.discoverText.setTextColor(Color.parseColor("#FFFFFF"))
                            binding.topic.setTextColor(resources.getColor(R.color.white))
                            binding.category.setTextColor(resources.getColor(R.color.white))
                            binding.SwipUpdisc.setImageResource(R.drawable.ic_up_arow)
                            binding.searchIcon.setImageResource(R.drawable.ic_loupe_white)
                            binding.tabLayoutLast.tabTextColors =
                                ColorStateList.valueOf(resources.getColor(R.color.white))
                        }
                        Configuration.UI_MODE_NIGHT_NO -> {
                            binding.searchIcon.setImageResource(R.drawable.ic_loupe)
                            binding.tabLayoutLast.tabTextColors =
                                ColorStateList.valueOf(resources.getColor(R.color.black))
                            binding.rlLayoutDisc.setBackgroundColor(Color.parseColor("#FFFFFF"))
                            binding.discoverText.setTextColor(Color.parseColor("#1A1A1A"))
                            binding.topic.setTextColor(resources.getColor(R.color.black))
                            binding.category.setTextColor(resources.getColor(R.color.black))
                            binding.SwipUpdisc.setImageResource(R.drawable.ic_up_arow_black)
                        }
                    }
                }
            }
        }
        binding.tabLayoutLast.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> fragment = CuratedList()
                    1 -> fragment = Awarded()
                    2 -> fragment = LatestFragment()
                }
                val fm = activity!!.supportFragmentManager
                val ft = fm.beginTransaction()
                ft.replace(R.id.frameLayout, fragment!!)
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                ft.commit()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        val requestOptions = RequestOptions()
        // requestOptions.error(Utils.getRandomDrawbleColor());
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
            .signature(ObjectKey(System.currentTimeMillis())).encodeQuality(70)
        requestOptions.priority(Priority.IMMEDIATE)
        requestOptions.skipMemoryCache(false)
        requestOptions.onlyRetrieveFromCache(true)
        requestOptions.placeholder(Utils.randomDrawbleColor)
        requestOptions.priority(Priority.HIGH)
        requestOptions.isMemoryCacheable
        requestOptions.diskCacheStrategy(DiskCacheStrategy.DATA)
        requestOptions.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        //   requestOptions.placeholder(Utils.getRandomDrawbleColor());
        requestOptions.centerCrop()
        val calendar = Calendar.getInstance()
        val day = calendar[Calendar.DAY_OF_WEEK]
        when (day) {
            Calendar.SUNDAY -> {
                // Current day is Sunday
                binding.fashionTextView.text = "fashion"
                setImage(
                    requestOptions,
                    "fashion",
                    binding.fashionImageView,
                    binding.fashionTextView
                )
                binding.bikeRideTextView.text = "bike ride"
                setImage(
                    requestOptions,
                    "bike ride",
                    binding.bikeRideImageView,
                    binding.bikeRideTextView
                )
                binding.abstractTextView.text = "abstract"
                setImage(
                    requestOptions,
                    "abstract",
                    binding.abstractImageView,
                    binding.abstractTextView
                )
                binding.coffeeShopTextView.text = "coffee shop"
                setImage(
                    requestOptions,
                    "coffee shop",
                    binding.coffeeShopImageView,
                    binding.coffeeShopTextView
                )
                binding.gameTextView.text = "game"
                setImage(requestOptions, "game", binding.gameImageView, binding.gameTextView)
                binding.vacationTextView.text = "vacation"
                setImage(
                    requestOptions,
                    "vacation",
                    binding.vacationImageView,
                    binding.vacationTextView
                )
            }
            Calendar.MONDAY -> {
                binding.fashionTextView.text = "bar"
                setImage(requestOptions, "bar", binding.fashionImageView, binding.fashionTextView)
                binding.bikeRideTextView.text = "london"
                setImage(
                    requestOptions,
                    "london",
                    binding.bikeRideImageView,
                    binding.bikeRideTextView
                )
                binding.abstractTextView.text = "puppy"
                setImage(
                    requestOptions,
                    "puppy",
                    binding.abstractImageView,
                    binding.abstractTextView
                )
                binding.coffeeShopTextView.text = "e commerce"
                setImage(
                    requestOptions,
                    "e commerce",
                    binding.coffeeShopImageView,
                    binding.coffeeShopTextView
                )
                binding.gameTextView.text = "camping"
                setImage(requestOptions, "camping", binding.gameImageView, binding.gameTextView)
                binding.vacationTextView.text = "basketball"
                setImage(
                    requestOptions,
                    "basketball",
                    binding.vacationImageView,
                    binding.vacationTextView
                )
            }
            Calendar.TUESDAY -> {
                binding.fashionTextView.text = "sleeping"
                setImage(
                    requestOptions,
                    "sleeping",
                    binding.fashionImageView,
                    binding.fashionTextView
                )
                binding.bikeRideTextView.text = "microphone"
                setImage(
                    requestOptions,
                    "microphone",
                    binding.bikeRideImageView,
                    binding.bikeRideTextView
                )
                binding.abstractTextView.text = "video conference"
                setImage(
                    requestOptions,
                    "video conference",
                    binding.abstractImageView,
                    binding.abstractTextView
                )
                binding.coffeeShopTextView.text = "strategy"
                setImage(
                    requestOptions,
                    "strategy",
                    binding.coffeeShopImageView,
                    binding.coffeeShopTextView
                )
                binding.gameTextView.text = "hiking"
                setImage(requestOptions, "hiking", binding.gameImageView, binding.gameTextView)
                binding.vacationTextView.text = "airport"
                setImage(
                    requestOptions,
                    "airport",
                    binding.vacationImageView,
                    binding.vacationTextView
                )
            }
            Calendar.WEDNESDAY -> {
                binding.fashionTextView.text = "dark and moody"
                setImage(
                    requestOptions,
                    "dark and moody",
                    binding.fashionImageView,
                    binding.fashionTextView
                )
                binding.bikeRideTextView.text = "Holiday Mood"
                setImage(
                    requestOptions,
                    "Holiday Mood",
                    binding.bikeRideImageView,
                    binding.bikeRideTextView
                )
                binding.abstractTextView.text = "Winter"
                setImage(
                    requestOptions,
                    "Winter",
                    binding.abstractImageView,
                    binding.abstractTextView
                )
                binding.coffeeShopTextView.text = "Dark Portraits"
                setImage(
                    requestOptions,
                    "Dark Portraits",
                    binding.coffeeShopImageView,
                    binding.coffeeShopTextView
                )
                binding.gameTextView.text = "Space Travel"
                setImage(
                    requestOptions,
                    "Space Travel",
                    binding.gameImageView,
                    binding.gameTextView
                )
                binding.vacationTextView.text = "Let's Party"
                setImage(
                    requestOptions,
                    "Let's Party",
                    binding.vacationImageView,
                    binding.vacationTextView
                )
            }
            Calendar.THURSDAY -> {
                binding.fashionTextView.text = "Cosmetics"
                setImage(
                    requestOptions,
                    "Cosmetics",
                    binding.fashionImageView,
                    binding.fashionTextView
                )
                binding.bikeRideTextView.text = "Retro"
                setImage(
                    requestOptions,
                    "Retro",
                    binding.bikeRideImageView,
                    binding.bikeRideTextView
                )
                binding.abstractTextView.text = "Summertime"
                setImage(
                    requestOptions,
                    "Summertime",
                    binding.abstractImageView,
                    binding.abstractTextView
                )
                binding.coffeeShopTextView.text = "Rainy Days"
                setImage(
                    requestOptions,
                    "Rainy Days",
                    binding.coffeeShopImageView,
                    binding.coffeeShopTextView
                )
                binding.gameTextView.text = "Floral Beauty"
                setImage(
                    requestOptions,
                    "Floral Beauty",
                    binding.gameImageView,
                    binding.gameTextView
                )
                binding.vacationTextView.text = "Home"
                setImage(
                    requestOptions,
                    "Home",
                    binding.vacationImageView,
                    binding.vacationTextView
                )
            }
            Calendar.FRIDAY -> {
                binding.fashionTextView.text = "Dancers"
                setImage(
                    requestOptions,
                    "Dancers",
                    binding.fashionImageView,
                    binding.fashionTextView
                )
                binding.bikeRideTextView.text = "Work"
                setImage(
                    requestOptions,
                    "Work",
                    binding.bikeRideImageView,
                    binding.bikeRideTextView
                )
                binding.abstractTextView.text = "marine"
                setImage(
                    requestOptions,
                    "marine",
                    binding.abstractImageView,
                    binding.abstractTextView
                )
                binding.coffeeShopTextView.text = "animals"
                setImage(
                    requestOptions,
                    "animals",
                    binding.coffeeShopImageView,
                    binding.coffeeShopTextView
                )
                binding.gameTextView.text = "Maldives"
                setImage(requestOptions, "Maldives", binding.gameImageView, binding.gameTextView)
                binding.vacationTextView.text = "Minimal black and white"
                setImage(
                    requestOptions,
                    "Minimal black and white",
                    binding.vacationImageView,
                    binding.vacationTextView
                )
            }
            Calendar.SATURDAY -> {
                binding.fashionTextView.text = "spectrum"
                setImage(
                    requestOptions,
                    "spectrum",
                    binding.fashionImageView,
                    binding.fashionTextView
                )
                binding.bikeRideTextView.text = "together"
                setImage(
                    requestOptions,
                    "together",
                    binding.bikeRideImageView,
                    binding.bikeRideTextView
                )
                binding.abstractTextView.text = "christmas"
                setImage(
                    requestOptions,
                    "christmas",
                    binding.abstractImageView,
                    binding.abstractTextView
                )
                binding.coffeeShopTextView.text = "party night"
                setImage(
                    requestOptions,
                    "party night",
                    binding.coffeeShopImageView,
                    binding.coffeeShopTextView
                )
                binding.gameTextView.text = "extream neon"
                setImage(
                    requestOptions,
                    "extream neon",
                    binding.gameImageView,
                    binding.gameTextView
                )
                binding.vacationTextView.text = "autumn"
                setImage(
                    requestOptions,
                    "autumn",
                    binding.vacationImageView,
                    binding.vacationTextView
                )
            }
        }
        binding.searchIcon.setOnClickListener { v: View? ->
            startActivity(
                Intent(
                    activity, SearchActivity::class.java
                )
            )
        }
        return view
    }

    private fun setImage(
        requestOptions: RequestOptions,
        query: String,
        img: ShapeableImageView,
        textView: MaterialTextView
    ) {
        val Url = Constant.BASE_URL + query + "&per_page=80&page=1"
        val stringRequest: StringRequest =
            object : StringRequest(Method.GET, Url, Response.Listener { response: String? ->
                try {
                    val obj = JSONObject(response)
                    val wallArray = obj.getJSONArray("photos")
                    var i = 0
                    while (i < wallArray.length()) {
                        val wallobj = wallArray.getJSONObject(i)
                        val photographer = JSONObject(wallobj.toString())
                        val PhotoUrl = JSONObject(wallobj.toString())
                        Log.d("PhotoURL", wallobj.getString("url"))
                        val jsonObject = wallobj.getJSONObject("src")
                        val `object` = JSONObject(jsonObject.toString())
                        val memCache: LruCache<String?, Bitmap> =
                            object : LruCache<String?, Bitmap>(
                                (Runtime.getRuntime().maxMemory() / (1024 * 4)).toInt()
                            ) {
                                override fun sizeOf(key: String?, image: Bitmap): Int {
                                    return image.byteCount / 1024
                                }
                            }
                        val image = memCache["imagefile"]
                        if (image != null) {
                            //Bitmap exists in cache.
                            img.setImageBitmap(image)
                        } else {
                            if (activity != null) {
                                Glide.with(requireActivity())
                                    .load(`object`.getString("large"))
                                    .thumbnail(
                                        Glide.with(requireContext())
                                            .load(`object`.getString("large2x"))
                                    )
                                    .apply(requestOptions)
                                    .listener(object : RequestListener<Drawable?> {
                                        override fun onLoadFailed(
                                            e: GlideException?,
                                            model: Any,
                                            target: Target<Drawable?>,
                                            isFirstResource: Boolean
                                        ): Boolean {
                                            //  spinKitView.setVisibility(View.GONE);
                                            return false
                                        }

                                        override fun onResourceReady(
                                            resource: Drawable?,
                                            model: Any,
                                            target: Target<Drawable?>,
                                            dataSource: DataSource,
                                            isFirstResource: Boolean
                                        ): Boolean {

                                            //    spinKitView.setVisibility(View.GONE);
                                            return false
                                        }
                                    })
                                    .into(img)
                                img.setOnClickListener {
                                    val intent = Intent(activity, TestingMotionLayout::class.java)
                                    try {
                                        intent.putExtra("img1", `object`.getString("large2x"))
                                    } catch (e: JSONException) {
                                        e.printStackTrace()
                                    }
                                    try {
                                        intent.putExtra("img2", `object`.getString("large"))
                                    } catch (e: JSONException) {
                                        e.printStackTrace()
                                    }
                                    intent.putExtra("query", query)
                                    intent.putExtra("text", query)
                                    val pairs = Pair.create<View, String>(img, "img1")
                                    val pairs1 = Pair.create<View, String>(textView, "text")
                                    val optionsCompat =
                                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                                            requireActivity(), pairs, pairs1
                                        )
                                    startActivity(intent, optionsCompat.toBundle())
                                }
                            }
                        }
                        i++
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }, Response.ErrorListener { error: VolleyError ->
                /*val response = error.networkResponse
                if (error is ServerError && response != null) {
                    try {
                        val res = String(
                            response.data,
                            HttpHeaderParser.parseCharset(response.headers, "utf-8")
                        )
                        // Now you can use any deserializer to make sense of data
                        val obj = JSONObject(res)
                    } catch (e1: UnsupportedEncodingException) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace()
                    } catch (e2: JSONException) {
                        // returned data is not JSONObject?
                        e2.printStackTrace()
                    }
                }*/
            }) {
                override fun getHeaders(): Map<String, String> {
                    val params: MutableMap<String, String> = HashMap()
                    apiList = ArrayList()
                    apiList.add(getString(R.string.APIKEY1))
                    apiList.add(getString(R.string.APIKEY2))
                    apiList.add(getString(R.string.APIKEY3))
                    apiList.add(getString(R.string.APIKEY4))
                    apiList.add(getString(R.string.APIKEY5))
                    val random = Random()
                    val n = random.nextInt(apiList.size)
                    params["Authorization"] = apiList.get(n)
                    return params
                }
            }
        stringRequest.setShouldCache(false)
        val requestQueue = Volley.newRequestQueue(context)
        stringRequest.retryPolicy = DefaultRetryPolicy(
            3000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(stringRequest)
    }

    companion object {
        @JvmStatic
        fun newInstance(text: String?): LastFragment {
            val f = LastFragment()
            val b = Bundle()
            b.putString("msg", text)
            f.arguments = b
            return f
        }
    }
}
package com.client.vpman.weatherwall.ui.Fragment

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.client.vpman.weatherwall.Adapter.SearchAdapter
import com.client.vpman.weatherwall.CustomeUsefullClass.Constant
import com.client.vpman.weatherwall.CustomeUsefullClass.SharedPref1
import com.client.vpman.weatherwall.R
import com.client.vpman.weatherwall.databinding.FragmentSearchBinding
import com.client.vpman.weatherwall.model.ModelData
import com.client.vpman.weatherwall.model.PagerAgentViewModel
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class SearchFragment : Fragment() {
    private var apiList: ArrayList<String> = ArrayList()
    var adapter: SearchAdapter? = null
    var list: ArrayList<ModelData> = ArrayList()
    var linearLayoutManager: LinearLayoutManager? = null
    var binding: FragmentSearchBinding? = null
    var sharedPref1: SharedPref1? = null
    var viewModel: PagerAgentViewModel? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        sharedPref1 = SharedPref1(context)
        viewModel = ViewModelProvider(requireActivity()).get(
            PagerAgentViewModel::class.java
        )
        when (sharedPref1!!.theme) {
            "Light" -> {
                binding!!.appBar.setBackgroundColor(Color.parseColor("#FFFFFF"))
                binding!!.searchData.background = resources.getDrawable(R.drawable.search_ui)
                binding!!.searchData.setHintTextColor(resources.getColor(R.color.black))
                binding!!.searchData.setTextColor(Color.parseColor("#000000"))
                binding!!.close.setImageResource(R.drawable.ic_baseline_arrow_back_24)
                binding!!.natureSearch.setTextColor(Color.parseColor("#000000"))
                binding!!.travelSearch.setTextColor(Color.parseColor("#000000"))
                binding!!.architectureSearch.setTextColor(Color.parseColor("#000000"))
                binding!!.artSearch.setTextColor(Color.parseColor("#000000"))
                binding!!.beautySearch.setTextColor(Color.parseColor("#000000"))
                binding!!.decorSearch.setTextColor(Color.parseColor("#000000"))
                binding!!.foodSearch.setTextColor(Color.parseColor("#000000"))
                binding!!.musicSearch.setTextColor(Color.parseColor("#000000"))
                binding!!.sportsSearch.setTextColor(Color.parseColor("#000000"))
            }
            "Dark" -> {
                binding!!.searchData.setTextColor(Color.parseColor("#FFFFFF"))
                binding!!.searchData.background = resources.getDrawable(R.drawable.search_ui_dark)
                binding!!.searchData.setHintTextColor(resources.getColor(R.color.white))
                binding!!.appBar.setBackgroundColor(Color.parseColor("#000000"))
                binding!!.close.setImageResource(R.drawable.ic_baseline_arrow_back_24_white)
                binding!!.natureSearch.setTextColor(Color.parseColor("#FFFFFF"))
                binding!!.travelSearch.setTextColor(Color.parseColor("#FFFFFF"))
                binding!!.architectureSearch.setTextColor(Color.parseColor("#FFFFFF"))
                binding!!.artSearch.setTextColor(Color.parseColor("#FFFFFF"))
                binding!!.beautySearch.setTextColor(Color.parseColor("#FFFFFF"))
                binding!!.decorSearch.setTextColor(Color.parseColor("#FFFFFF"))
                binding!!.foodSearch.setTextColor(Color.parseColor("#FFFFFF"))
                binding!!.musicSearch.setTextColor(Color.parseColor("#FFFFFF"))
                binding!!.sportsSearch.setTextColor(Color.parseColor("#FFFFFF"))
            }
            else -> {
                when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                    Configuration.UI_MODE_NIGHT_YES -> {
                        binding!!.searchData.setTextColor(Color.parseColor("#FFFFFF"))
                        binding!!.searchData.background =
                            resources.getDrawable(R.drawable.search_ui_dark)
                        binding!!.searchData.setHintTextColor(resources.getColor(R.color.white))
                        binding!!.appBar.setBackgroundColor(Color.parseColor("#000000"))
                        binding!!.close.setImageResource(R.drawable.ic_baseline_arrow_back_24_white)
                        binding!!.natureSearch.setTextColor(Color.parseColor("#FFFFFF"))
                        binding!!.travelSearch.setTextColor(Color.parseColor("#FFFFFF"))
                        binding!!.architectureSearch.setTextColor(Color.parseColor("#FFFFFF"))
                        binding!!.artSearch.setTextColor(Color.parseColor("#FFFFFF"))
                        binding!!.beautySearch.setTextColor(Color.parseColor("#FFFFFF"))
                        binding!!.decorSearch.setTextColor(Color.parseColor("#FFFFFF"))
                        binding!!.foodSearch.setTextColor(Color.parseColor("#FFFFFF"))
                        binding!!.musicSearch.setTextColor(Color.parseColor("#FFFFFF"))
                        binding!!.sportsSearch.setTextColor(Color.parseColor("#FFFFFF"))
                    }
                    Configuration.UI_MODE_NIGHT_NO -> {
                        binding!!.searchData.setTextColor(Color.parseColor("#000000"))
                        binding!!.searchData.background =
                            resources.getDrawable(R.drawable.search_ui)
                        binding!!.searchData.setHintTextColor(resources.getColor(R.color.black))
                        binding!!.appBar.setBackgroundColor(Color.parseColor("#FFFFFF"))
                        binding!!.close.setImageResource(R.drawable.ic_baseline_arrow_back_24)
                        binding!!.natureSearch.setTextColor(Color.parseColor("#000000"))
                        binding!!.travelSearch.setTextColor(Color.parseColor("#000000"))
                        binding!!.architectureSearch.setTextColor(Color.parseColor("#000000"))
                        binding!!.artSearch.setTextColor(Color.parseColor("#000000"))
                        binding!!.beautySearch.setTextColor(Color.parseColor("#000000"))
                        binding!!.decorSearch.setTextColor(Color.parseColor("#000000"))
                        binding!!.foodSearch.setTextColor(Color.parseColor("#000000"))
                        binding!!.musicSearch.setTextColor(Color.parseColor("#000000"))
                        binding!!.sportsSearch.setTextColor(Color.parseColor("#000000"))
                    }
                }
            }
        }
        binding!!.natureSearch.setOnClickListener { v: View? -> SentData("Nature") }
        binding!!.travelSearch.setOnClickListener { v: View? -> SentData("Travel") }
        binding!!.architectureSearch.setOnClickListener { v: View? -> SentData("Architecture") }
        binding!!.artSearch.setOnClickListener { v: View? -> SentData("Art") }
        binding!!.beautySearch.setOnClickListener { v: View? -> SentData("Beauty") }
        binding!!.decorSearch.setOnClickListener { v: View? -> SentData("Decor") }
        binding!!.foodSearch.setOnClickListener { v: View? -> SentData("food") }
        binding!!.musicSearch.setOnClickListener { v: View? -> SentData("Music") }
        binding!!.sportsSearch.setOnClickListener { v: View? -> SentData("Sports") }
        binding!!.searchData.setOnEditorActionListener { v: TextView?, actionId: Int, event: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                SentData(binding!!.searchData.text.toString())
                return@setOnEditorActionListener true
            }
            false
        }
        binding!!.searchData.onFocusChangeListener =
            View.OnFocusChangeListener { _: View?, _: Boolean ->
                binding!!.searchData.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    0,
                    0
                )
            }
        binding!!.close.setOnClickListener { v: View? -> requireActivity().onBackPressed() }
        CuratedImages()
        PopularImage()
        return binding!!.root
    }

    private fun CuratedImages() {
        list = ArrayList()
        if (activity != null) {
            val stringRequest: StringRequest = object : StringRequest(
                Method.GET,
                Constant.CURATED_URL,
                Response.Listener { response: String? ->
                    Log.d("curatedResponse", response!!)
                    try {
                        val obj = JSONObject(response)
                        Log.d("mil gaya", obj.toString())
                        val wallArray = obj.getJSONArray("photos")
                        for (i in 0 until wallArray.length()) {
                            val wallobj = wallArray.getJSONObject(i)
                            val photographer = JSONObject(wallobj.toString())
                            Log.d("PhotoURL", wallobj.getString("url"))
                            val jsonObject = wallobj.getJSONObject("src")
                            val `object` = JSONObject(jsonObject.toString())
                            val modelData1 = ModelData(
                                `object`.getString("large2x"),
                                photographer.getString("photographer"),
                                `object`.getString("large"),
                                `object`.getString("original"),
                                wallobj.getString("url"),
                                wallobj.getString("photographer_url")
                            )
                            list.add(modelData1)
                        }
                        list.shuffle()
                        adapter = SearchAdapter(requireActivity(), list)
                        /* LinearLayoutManager linearLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);*/linearLayoutManager =
                            GridLayoutManager(
                                context, 2, GridLayoutManager.VERTICAL, false
                            )
                        binding!!.recylerViewSearch.layoutManager = linearLayoutManager
                        binding!!.recylerViewSearch.setHasFixedSize(true)
                        binding!!.recylerViewSearch.itemAnimator = DefaultItemAnimator()
                        binding!!.recylerViewSearch.isNestedScrollingEnabled = true
                        val itemViewType = 0
                        binding!!.recylerViewSearch.recycledViewPool.setMaxRecycledViews(
                            itemViewType,
                            0
                        )
                        binding!!.recylerViewSearch.adapter = adapter
                        adapter!!.notifyDataSetChanged()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { error: VolleyError ->
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
            val requestQueue = Volley.newRequestQueue(activity)
            stringRequest.retryPolicy = DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
            requestQueue.add(stringRequest)
        }
    }

    private fun PopularImage() {
        list = ArrayList()
        val mRequestStartTime = System.currentTimeMillis()
        if (activity != null) {
            val stringRequest: StringRequest = object : StringRequest(
                Method.GET,
                Constant.POPULAR_URL,
                Response.Listener { response: String? ->
                    Log.d("curatedResponse", response!!)
                    try {
                        val obj = JSONObject(response)
                        Log.d("mil gaya", obj.toString())
                        val wallArray = obj.getJSONArray("photos")
                        for (i in 0 until wallArray.length()) {
                            val wallobj = wallArray.getJSONObject(i)
                            val photographer = JSONObject(wallobj.toString())
                            Log.d("PhotoURL", wallobj.getString("url"))
                            val jsonObject = wallobj.getJSONObject("src")
                            val `object` = JSONObject(jsonObject.toString())
                            val modelData1 = ModelData(
                                `object`.getString("large2x"),
                                photographer.getString("photographer"),
                                `object`.getString("large"),
                                `object`.getString("original"),
                                wallobj.getString("url"),
                                wallobj.getString("photographer_url")
                            )
                            list.add(modelData1)
                        }
                        list.shuffle()
                        adapter!!.notifyDataSetChanged()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { error: VolleyError ->
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
            val requestQueue = Volley.newRequestQueue(activity)
            stringRequest.retryPolicy = DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
            requestQueue.add(stringRequest)
        }
    }

    fun SentData(query: String?) {
        viewModel!!.setText(query!!)
        val fragment = SearchResult()
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.replace(R.id.main_fragment, fragment)
        fragmentTransaction.commit()
    }
}
package com.client.vpman.weatherwall.ui.Fragment

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.ServerError
import com.android.volley.VolleyError
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.client.vpman.weatherwall.Adapter.SearchAdapter
import com.client.vpman.weatherwall.CustomeUsefullClass.Constant
import com.client.vpman.weatherwall.CustomeUsefullClass.SharedPref1
import com.client.vpman.weatherwall.R
import com.client.vpman.weatherwall.databinding.FragmentSearchResultBinding
import com.client.vpman.weatherwall.model.ModelData
import com.client.vpman.weatherwall.model.PagerAgentViewModel
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.util.*
import kotlin.collections.ArrayList

class SearchResult : Fragment() {
    var binding: FragmentSearchResultBinding? = null
    var viewModel: PagerAgentViewModel? = null
    var list: ArrayList<ModelData> = ArrayList()
    var sharedPref1: SharedPref1? = null
    var searchAdapter: SearchAdapter? = null
    private var apiList: ArrayList<String> = ArrayList()
    var linearLayoutManager: LinearLayoutManager? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchResultBinding.inflate(inflater, container, false)
        sharedPref1 = SharedPref1(context)
        when (sharedPref1!!.theme) {
            "Light" -> {
                binding!!.backres.setImageResource(R.drawable.ic_arrow_back)
                binding!!.searchResult.setTextColor(Color.parseColor("#000000"))
                binding!!.resBackground.setBackgroundColor(Color.parseColor("#FFFFFF"))
                binding!!.resultToolbar.setBackgroundColor(Color.parseColor("#FFFFFF"))
            }
            "Dark" -> {
                binding!!.backres.setImageResource(R.drawable.ic_baseline_arrow_back_24_white)
                binding!!.searchResult.setTextColor(Color.parseColor("#FFFFFF"))
                binding!!.resBackground.setBackgroundColor(Color.parseColor("#000000"))
                binding!!.resultToolbar.setBackgroundColor(Color.parseColor("#000000"))
            }
            else -> {
                when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                    Configuration.UI_MODE_NIGHT_YES -> {
                        binding!!.backres.setImageResource(R.drawable.ic_baseline_arrow_back_24_white)
                        binding!!.searchResult.setTextColor(Color.parseColor("#FFFFFF"))
                        binding!!.resBackground.setBackgroundColor(Color.parseColor("#000000"))
                        binding!!.resultToolbar.setBackgroundColor(Color.parseColor("#000000"))
                    }
                    Configuration.UI_MODE_NIGHT_NO -> {
                        binding!!.resBackground.setBackgroundColor(Color.parseColor("#FFFFFF"))
                        binding!!.resultToolbar.setBackgroundColor(Color.parseColor("#FFFFFF"))
                        binding!!.backres.setImageResource(R.drawable.ic_arrow_back)
                        binding!!.searchResult.setTextColor(Color.parseColor("#000000"))
                    }
                }
            }
        }
        binding!!.backres.setOnClickListener { requireActivity().onBackPressed() }
        return binding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(
            PagerAgentViewModel::class.java
        )
        viewModel!!.getText().observe(viewLifecycleOwner, { charSequence: CharSequence ->
            binding!!.searchResult.text = charSequence
            LoadData(charSequence.toString())
            Log.d("defv", (charSequence as String))
        })
    }

    fun LoadData(query: String) {
        list = ArrayList()
        val data = arrayOf(
            "sex",
            "nude",
            "porn",
            "fuck",
            "vagina",
            "orgasam",
            "sexy girl",
            "nude pic",
            "hot girl",
            "porn star",
            "xvideos",
            "chutiya",
            "lund",
            "dick",
            "pussy",
            "hot girl",
            "sexy",
            "Sex",
            "Sexy",
            "Porn",
            "Vagina",
            "nudes",
            "Sexy girl",
            "Porn star",
            "Xvideos",
            "Hot girl",
            "Nude",
            "Orgasam",
            "Fuck"
        )
        if (Arrays.asList(*data).contains(query)) {
            if (sharedPref1!!.theme == "Light") {
                binding!!.notfound.visibility = View.VISIBLE
                binding!!.notFoundText.setTextColor(Color.parseColor("#000000"))
            } else if (sharedPref1!!.theme == "Dark") {
                binding!!.notfound1.visibility = View.VISIBLE
                binding!!.notFoundText.setTextColor(Color.parseColor("#FFFFFF"))
            } else {
                binding!!.notfound.visibility = View.VISIBLE
                binding!!.notFoundText.setTextColor(Color.parseColor("#000000"))
            }
            binding!!.searchResultRecylerview.visibility = View.GONE
            binding!!.notFoundText.visibility = View.VISIBLE
        } else {
            binding!!.notFoundText.visibility = View.GONE
            binding!!.notfound.visibility = View.GONE
            binding!!.searchResultRecylerview.visibility = View.VISIBLE
        }
        val Url = Constant.BASE_URL + query + "&per_page=80&page=1"
        Log.d("ewjoh", Url)
        val stringRequest: StringRequest =
            object : StringRequest(Method.GET, Url, Response.Listener { response: String? ->
                Log.d("searchResponse", response!!)
                try {
                    val obj = JSONObject(response)
                    val wallArray = obj.getJSONArray("photos")
                    for (i in 0 until wallArray.length()) {
                        val wallobj = wallArray.getJSONObject(i)
                        val photographer = JSONObject(wallobj.toString())
                        val PhotoUrl = JSONObject(wallobj.toString())
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
                    searchAdapter = SearchAdapter(requireContext(), list)
                    linearLayoutManager =
                        GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
                    binding!!.searchResultRecylerview.layoutManager = linearLayoutManager
                    binding!!.searchResultRecylerview.setHasFixedSize(true)
                    binding!!.searchResultRecylerview.itemAnimator = DefaultItemAnimator()
                    binding!!.searchResultRecylerview.isNestedScrollingEnabled = true
                    val itemViewType = 0
                    binding!!.searchResultRecylerview.recycledViewPool.setMaxRecycledViews(
                        itemViewType,
                        0
                    )
                    binding!!.searchResultRecylerview.adapter = searchAdapter
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
}
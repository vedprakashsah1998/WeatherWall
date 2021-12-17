package com.client.vpman.weatherwall.ui.Fragment

import com.client.vpman.weatherwall.model.ModelData
import com.client.vpman.weatherwall.Adapter.CuratedAdapter
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.android.volley.toolbox.StringRequest
import org.json.JSONObject
import org.json.JSONArray
import ccy.focuslayoutmanager.FocusLayoutManager
import com.android.volley.*
import com.android.volley.toolbox.HttpHeaderParser
import org.json.JSONException
import com.client.vpman.weatherwall.R
import com.android.volley.toolbox.Volley
import com.client.vpman.weatherwall.CustomeUsefullClass.Constant
import com.client.vpman.weatherwall.databinding.FragmentCuratedListBinding
import java.io.UnsupportedEncodingException
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class CuratedList : Fragment() {
    var list: ArrayList<ModelData> = ArrayList()
    private var adapter: CuratedAdapter? = null
    private var binding: FragmentCuratedListBinding? = null
    private var apiList: ArrayList<String> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCuratedListBinding.inflate(inflater, container, false)
        CuratedImages()
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
                        adapter = CuratedAdapter(requireActivity(), list)
                        /* LinearLayoutManager linearLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);*/
                        val focusLayoutManager = FocusLayoutManager.Builder()
                            .layerPadding(FocusLayoutManager.dp2px(activity, 14f))
                            .normalViewGap(FocusLayoutManager.dp2px(activity, 14f))
                            .focusOrientation(FocusLayoutManager.FOCUS_LEFT)
                            .isAutoSelect(true)
                            .maxLayerCount(3)
                            .setOnFocusChangeListener { _: Int, _: Int -> }
                            .build()
                        binding!!.curatedRecyler.setHasFixedSize(true)
                        binding!!.curatedRecyler.isNestedScrollingEnabled = true
                        binding!!.curatedRecyler.layoutManager = focusLayoutManager
                        binding!!.curatedRecyler.adapter = adapter
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
}
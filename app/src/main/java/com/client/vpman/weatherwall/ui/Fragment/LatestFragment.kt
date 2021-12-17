package com.client.vpman.weatherwall.ui.Fragment

import com.client.vpman.weatherwall.model.InstaModel
import com.client.vpman.weatherwall.Adapter.LatestAdapter
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
import org.json.JSONException
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.Volley
import com.client.vpman.weatherwall.CustomeUsefullClass.Constant
import com.client.vpman.weatherwall.databinding.FragmentLatestBinding
import java.io.UnsupportedEncodingException
import java.util.*
import kotlin.collections.ArrayList

class LatestFragment : Fragment() {
    var binding: FragmentLatestBinding? = null
    var lists: ArrayList<InstaModel> = ArrayList()
    private var adapter: LatestAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLatestBinding.inflate(inflater, container, false)
        adapter = LatestAdapter(requireActivity(), lists)
        instagramImageApi()
        isabellandscapes()
        artOFBuilding()
        seanView()
        rachel_jones_ross()
        return binding!!.root
    }

    private fun instagramImageApi() {
        if (activity != null) {
            lists = ArrayList()
            val stringRequest = StringRequest(
                Request.Method.GET,
                Constant.madspeteriversen_photography,
                { response: String? ->
                    Log.d("instaresponse", response!!)
                    try {
                        val jsonObject = JSONObject(response)
                        val graphQl = jsonObject.getJSONObject("graphql")
                        val user = graphQl.getJSONObject("user")
                        val edge_owner_to_timeline_media =
                            user.getJSONObject("edge_owner_to_timeline_media")
                        val edges = edge_owner_to_timeline_media.getJSONArray("edges")
                        for (i in 0 until edges.length()) {
                            val jsonObject1 = edges.getJSONObject(i)
                            val node = jsonObject1.getJSONObject("node")
                            val displayUrl = node.getString("display_url")
                            val thumbnail_src = node.getString("thumbnail_src")
                            val photographerUrl =
                                "https://www.instagram.com/madspeteriversen_photography/?hl=en"
                            val instagramModel =
                                InstaModel(displayUrl, thumbnail_src, photographerUrl)
                            lists.add(instagramModel)
                        }
                        lists.shuffle()
                        adapter = LatestAdapter(requireActivity(), lists)
                        val focusLayoutManager = FocusLayoutManager.Builder()
                            .layerPadding(FocusLayoutManager.dp2px(activity, 14f))
                            .normalViewGap(FocusLayoutManager.dp2px(activity, 14f))
                            .focusOrientation(FocusLayoutManager.FOCUS_LEFT)
                            .isAutoSelect(true)
                            .maxLayerCount(3)
                            .setOnFocusChangeListener { _: Int, _: Int -> }
                            .build()
                        binding!!.latestRecyler.setHasFixedSize(true)
                        binding!!.latestRecyler.layoutManager = focusLayoutManager
                        binding!!.latestRecyler.adapter = adapter
                        binding!!.latestRecyler.isNestedScrollingEnabled = true
                        adapter!!.notifyDataSetChanged()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }) { error: VolleyError ->
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
                    } // returned data is not JSONObject?
                    catch (e1: JSONException) {
                        e1.printStackTrace()
                    }
                }*/
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

    private fun isabellandscapes() {
        if (activity != null) {
            val stringRequest =
                StringRequest(Request.Method.GET, Constant.isabellandscapes, { response: String? ->
                    Log.d("betaTest", response!!)
                    try {
                        val jsonObject = JSONObject(response)
                        val graphQl = jsonObject.getJSONObject("graphql")
                        val user = graphQl.getJSONObject("user")
                        val edge_owner_to_timeline_media =
                            user.getJSONObject("edge_owner_to_timeline_media")
                        val edges = edge_owner_to_timeline_media.getJSONArray("edges")
                        for (i in 0 until edges.length()) {
                            val jsonObject1 = edges.getJSONObject(i)
                            val node = jsonObject1.getJSONObject("node")
                            val displayUrl = node.getString("display_url")
                            val thumbnail_src = node.getString("thumbnail_src")
                            val photGrapherUrl = "https://www.instagram.com/isabellandscapes/?hl=en"
                            val instagramModel =
                                InstaModel(displayUrl, thumbnail_src, photGrapherUrl)
                            lists!!.add(instagramModel)
                        }
                        lists.shuffle()
                        adapter!!.notifyDataSetChanged()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }) { error: VolleyError ->
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
                        } // returned data is not JSONObject?
                        catch (e1: JSONException) {
                            e1.printStackTrace()
                        }
                    }*/
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

    private fun artOFBuilding() {
        if (activity != null) {
            val stringRequest =
                StringRequest(Request.Method.GET, Constant.chrisburkard, { response: String? ->
                    Log.d("betaTest", response!!)
                    try {
                        val jsonObject = JSONObject(response)
                        val graphQl = jsonObject.getJSONObject("graphql")
                        val user = graphQl.getJSONObject("user")
                        val edge_owner_to_timeline_media =
                            user.getJSONObject("edge_owner_to_timeline_media")
                        val edges = edge_owner_to_timeline_media.getJSONArray("edges")
                        for (i in 0 until edges.length()) {
                            val jsonObject1 = edges.getJSONObject(i)
                            val node = jsonObject1.getJSONObject("node")
                            val displayUrl = node.getString("display_url")
                            val thumbnail_src = node.getString("thumbnail_src")
                            val photographerUrl = "https://www.instagram.com/chrisburkard/?hl=en"
                            val instagramModel =
                                InstaModel(displayUrl, thumbnail_src, photographerUrl)
                            lists.add(instagramModel)
                        }
                        lists.shuffle()
                        adapter!!.notifyDataSetChanged()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }) { error: VolleyError ->
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
                        } // returned data is not JSONObject?
                        catch (e1: JSONException) {
                            e1.printStackTrace()
                        }
                    }*/
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

    private fun seanView() {
        if (activity != null) {
            val stringRequest =
                StringRequest(Request.Method.GET, Constant.seanbagshaw, { response: String? ->
                    Log.d("betaTest", response!!)
                    try {
                        val jsonObject = JSONObject(response)
                        val graphQl = jsonObject.getJSONObject("graphql")
                        val user = graphQl.getJSONObject("user")
                        val edge_owner_to_timeline_media =
                            user.getJSONObject("edge_owner_to_timeline_media")
                        val edges = edge_owner_to_timeline_media.getJSONArray("edges")
                        for (i in 0 until edges.length()) {
                            val jsonObject1 = edges.getJSONObject(i)
                            val node = jsonObject1.getJSONObject("node")
                            val displayUrl = node.getString("display_url")
                            val thumbnail_src = node.getString("thumbnail_src")
                            val photoUrl = "https://www.instagram.com/seanbagshaw/?hl=en"
                            val instagramModel = InstaModel(displayUrl, thumbnail_src, photoUrl)
                            lists.add(instagramModel)
                        }
                        lists.shuffle()
                        adapter!!.notifyDataSetChanged()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }) { error: VolleyError ->
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
                        } // returned data is not JSONObject?
                        catch (e1: JSONException) {
                            e1.printStackTrace()
                        }
                    }*/
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

    private fun rachel_jones_ross() {
        if (activity != null) {
            val stringRequest =
                StringRequest(Request.Method.GET, Constant.rachel_jones_ross, { response: String? ->
                    Log.d("betaTest", response!!)
                    try {
                        val jsonObject = JSONObject(response)
                        val graphQl = jsonObject.getJSONObject("graphql")
                        val user = graphQl.getJSONObject("user")
                        val edge_owner_to_timeline_media =
                            user.getJSONObject("edge_owner_to_timeline_media")
                        val edges = edge_owner_to_timeline_media.getJSONArray("edges")
                        for (i in 0 until edges.length()) {
                            val jsonObject1 = edges.getJSONObject(i)
                            val node = jsonObject1.getJSONObject("node")
                            val displayUrl = node.getString("display_url")
                            val thumbnail_src = node.getString("thumbnail_src")
                            val photoUrl = "https://www.instagram.com/rachel_jones_ross/?hl=en"
                            val instagramModel = InstaModel(displayUrl, thumbnail_src, photoUrl)
                            lists.add(instagramModel)
                        }
                        lists.shuffle()
                        adapter!!.notifyDataSetChanged()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }) { error: VolleyError ->
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
                        } // returned data is not JSONObject?
                        catch (e1: JSONException) {
                            e1.printStackTrace()
                        }
                    }*/
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
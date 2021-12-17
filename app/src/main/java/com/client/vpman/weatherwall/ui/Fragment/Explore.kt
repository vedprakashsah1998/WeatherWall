package com.client.vpman.weatherwall.ui.Fragment

import android.content.res.Configuration
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.client.vpman.weatherwall.CustomeUsefullClass.SharedPref1
import com.client.vpman.weatherwall.R
import com.client.vpman.weatherwall.CustomeDesignViewPager.DepthTransform
import com.client.vpman.weatherwall.Adapter.DemoFragmentStateAdapter1
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.client.vpman.weatherwall.databinding.FragmentExploreBinding
import com.client.vpman.weatherwall.ui.Fragment.Explore

/**
 * A simple [Fragment] subclass.
 */
class Explore : Fragment() {
    private var binding: FragmentExploreBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExploreBinding.inflate(inflater, container, false)
        val view: View = binding!!.root
        if (activity != null) {
            val sharedPref1 = SharedPref1(activity)
            when (sharedPref1.theme) {
                "Light" -> {
                    binding!!.relexp.setBackgroundColor(Color.parseColor("#FFFFFF"))
                    binding!!.exploreId.setTextColor(Color.parseColor("#000000"))
                    binding!!.right.setImageResource(R.drawable.ic_right)
                }
                "Dark" -> {
                    binding!!.relexp.setBackgroundColor(Color.parseColor("#000000"))
                    binding!!.exploreId.setTextColor(Color.parseColor("#FFFFFF"))
                    binding!!.right.setImageResource(R.drawable.ic_right_white)
                }
                else -> {
                    when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                        Configuration.UI_MODE_NIGHT_YES -> {
                            binding!!.relexp.setBackgroundColor(Color.parseColor("#000000"))
                            binding!!.exploreId.setTextColor(Color.parseColor("#FFFFFF"))
                            binding!!.right.setImageResource(R.drawable.ic_right_white)
                        }
                        Configuration.UI_MODE_NIGHT_NO -> {
                            binding!!.relexp.setBackgroundColor(Color.parseColor("#FFFFFF"))
                            binding!!.exploreId.setTextColor(Color.parseColor("#000000"))
                            binding!!.right.setImageResource(R.drawable.ic_right)
                        }
                    }
                }
            }
        }
        val depthTransformation = DepthTransform()
        val adapter = DemoFragmentStateAdapter1(childFragmentManager)
        binding!!.pager1.setPageTransformer(true, depthTransformation)
        binding!!.pager1.adapter = adapter
        binding!!.pager1.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    binding!!.pager1.currentItem = 0
                    /*left.setVisibility(View.GONE);*/
                } else if (position == 11) {
                    binding!!.right.visibility = View.GONE
                    binding!!.pager1.currentItem = 11
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        return view
    }

    companion object {
        @JvmField
        var pagerPosition = 0
        @JvmStatic
        fun newInstance(text: String?): Explore {
            val f = Explore()
            val b = Bundle()
            b.putString("msg", text)
            f.arguments = b
            return f
        }
    }
}
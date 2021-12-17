package com.client.vpman.weatherwall.ui.Fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.client.vpman.weatherwall.CustomeUsefullClass.Constant
import com.client.vpman.weatherwall.CustomeUsefullClass.VolleyGlobalLization
import com.client.vpman.weatherwall.databinding.FragmentImageBinding
import com.client.vpman.weatherwall.ui.Fragment.Explore

/**
 * A simple [Fragment] subclass.
 */
class Image : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentImageBinding.inflate(inflater, container, false)
        val view: View = binding.root
        val type = requireArguments().getString("type")
        Log.d("typeData", type!!)
        VolleyGlobalLization.LoadImageDiff(type, binding.NatureUn, activity)
        Log.d("Page", java.lang.String.valueOf(Explore.pagerPosition))
        when (type) {
            Constant.Nature -> {
                binding.natureText.text = "N A T U R E"
                binding.desc.text = "Click here to explore nature photos"
            }
            Constant.Building -> {
                binding.natureText.text = "B U I L I D I N G"
                binding.desc.text = "Click here to explore building photos"
            }
            Constant.Car -> {
                binding.natureText.text = "C A R"
                binding.desc.text = "Click here to explore car photos"
            }
            Constant.dark -> {
                binding.natureText.text = "D A R K"
                binding.desc.text = "Click here to explore dark photos"
            }
            Constant.Bokeh -> {
                binding.natureText.text = "B O K E H"
                binding.desc.text = "Click here to explore bokeh photos"
            }
            Constant.minimal -> {
                binding.natureText.text = "M I N I M A L"
                binding.desc.text = "Click here to explore minimal photos"
            }
            Constant.mountain -> {
                binding.natureText.text = "M O U N T A I N"
                binding.desc.text = "Click here to explore mountain photos"
            }
            Constant.religion -> {
                binding.natureText.text = "R E L I G I O N"
                binding.desc.text = "Click here to explore religion photos"
            }
            Constant.Sparkles -> {
                binding.natureText.text = "S P A R K L E S"
                binding.desc.text = "Click here to explore sparkles photos"
            }
            Constant.star -> {
                binding.natureText.text = "S T A R"
                binding.desc.text = "Click here to explore star photos"
            }
            Constant.technology -> {
                binding.natureText.text = "T E C H N O L O G Y"
                binding.desc.text = "Click here to explore technology photos"
            }
            Constant.Luxury -> {
                binding.natureText.text = "L U X U R Y"
                binding.desc.text = "Click here to explore luxury photos"
            }
        }
        binding.NatureUn.translationZ = 40f
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(text: String?, type: String?): Image {
            val f = Image()
            val b = Bundle()
            b.putString("msg", text)
            b.putString("type", type)
            f.arguments = b
            return f
        }
    }
}
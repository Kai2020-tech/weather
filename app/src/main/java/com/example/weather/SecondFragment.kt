package com.example.weather

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import kotlinx.android.synthetic.main.fragment_first.*
import kotlinx.android.synthetic.main.fragment_second.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SecondFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SecondFragment : Fragment() {

    private lateinit var rootView: View

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        rootView = inflater.inflate(R.layout.fragment_second, container, false)
        // Inflate the layout for this fragment
        return rootView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as ILocationPublisher).remove(subscriber) //取消訂閱
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as ILocationPublisher).add(subscriber)    //訂閱

        secondFragment.setOnClickListener {
            hideKeyboard(rootView, tv_city2)
        }

        secondFragment.background.alpha = 99 //背景透明度 0-255

    }

    private val subscriber: () -> Unit = {
        val loc = (activity as ILocationPublisher).getCurrentLocationWeatherRecord()

        loc?.let {
            updateContent(it)
        }
    }

    //    隱藏鍵盤
    private fun hideKeyboard(view: View, nextFoocusView: View = view.rootView) {
        val imm = view.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
        view.clearFocus()
        nextFoocusView.requestFocus()
    }

    private fun updateContent(location: WeatherData.Records.Location) {

        /** ICON*/
        when {
            location.weatherElement[1].time[2].parameter.parameterName.toInt() >= 80 -> iv_status2.setImageResource(
                R.drawable.icon_rain
            )   // rainProbability > 80%
            location.weatherElement[1].time[2].parameter.parameterName.toInt() >= 30 -> iv_status2.setImageResource(
                R.drawable.icon_umbrella
            )   // rainProbability > 30%
            location.weatherElement[2].time[2].parameter.parameterName.toInt() >= 30 -> iv_status2.setImageResource(
                R.drawable.icon_hot
            )  // 最低溫 >= 30
            else -> iv_status2.setImageResource(R.drawable.icon_sun)
        }
        /** 背景*/

        when {
            location.weatherElement[0].time[2].parameter.parameterName.contains("雷") -> secondFragment.setBackgroundResource(
                R.drawable.thunderstorm
            )
            location.weatherElement[0].time[2].parameter.parameterName.contains("雨") -> secondFragment.setBackgroundResource(
                R.drawable.drizzle
            )
            location.weatherElement[0].time[2].parameter.parameterName.contains("多雲時晴") -> secondFragment.setBackgroundResource(
                R.drawable.clear
            )
            location.weatherElement[0].time[2].parameter.parameterName.contains("多雲") -> secondFragment.setBackgroundResource(
                R.drawable.mist
            )
            location.weatherElement[1].time[2].parameter.parameterName.toInt() > 80 -> secondFragment.setBackgroundResource(
                R.drawable.rain
            )
            location.weatherElement[1].time[2].parameter.parameterName.toInt() > 50 -> secondFragment.setBackgroundResource(
                R.drawable.drizzle
            )
        }

        //隔日
        tv_city2.text = location.locationName + "明日預報"
        tv_startTime3.text = location.weatherElement[0].time[2].startTime
        tv_endTime3.text =
            location.weatherElement[0].time[2].endTime//.split(" ")[1]
        tv_status3.text =
            location.weatherElement[0].time[2].parameter.parameterName
        tv_rainProbability3.text =
            "降雨機率 ${location.weatherElement[1].time[2].parameter.parameterName} %"
        tv_temperature3.text =
            location.weatherElement[2].time[2].parameter.parameterName + "-" + location.weatherElement[4].time[2].parameter.parameterName
        tv_CI3.text = location.weatherElement[3].time[2].parameter.parameterName

        secondFragment.background.alpha = 50 //背景透明度 0-255
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SecondFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SecondFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
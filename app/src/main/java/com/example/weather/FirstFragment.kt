package com.example.weather

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.example.weather.MainActivity.Companion.myLocation
import kotlinx.android.synthetic.main.fragment_first.*
import kotlinx.android.synthetic.main.fragment_second.*
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FirstFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FirstFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    /**origin*/
    private lateinit var rootView: View
    /**origin*/

    /**Aria*/
//    var rootView: View? = null
//    var fragmentLocation: WeatherData.Records.Location? = null
    /**Aria*/

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

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_first, container, false)
        /**Aria*/
//        rootView = inflater.inflate(R.layout.fragment_first, container, false)
//        fragmentLocation?.let { updateContent(it) }     //重建fragment 判斷
        /**Aria*/


        return rootView
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
            location.weatherElement[1].time[0].parameter.parameterName.toInt() >= 80 -> iv_status1.setImageResource(
                R.drawable.icon_rain
            )   // rainProbability > 80%
            location.weatherElement[1].time[0].parameter.parameterName.toInt() >= 30 -> iv_status1.setImageResource(
                R.drawable.icon_umbrella
            )   // rainProbability > 30%
            location.weatherElement[2].time[0].parameter.parameterName.toInt() >= 30 -> iv_status1.setImageResource(
                R.drawable.icon_hot
            )  // 最低溫 >= 30
            else -> iv_status1.setImageResource(R.drawable.icon_sun)
        }
        /** 背景*/
        when {
            location.weatherElement[0].time[0].parameter.parameterName.contains("雷") -> firstFragment.setBackgroundResource(
                R.drawable.thunderstorm
            )
            location.weatherElement[0].time[0].parameter.parameterName.contains("雨") -> firstFragment.setBackgroundResource(
                R.drawable.drizzle
            )
            location.weatherElement[0].time[0].parameter.parameterName.contains("多雲時晴") -> firstFragment.setBackgroundResource(
                R.drawable.clear
            )
            location.weatherElement[0].time[0].parameter.parameterName.contains("多雲") -> firstFragment.setBackgroundResource(
                R.drawable.mist
            )
            location.weatherElement[1].time[0].parameter.parameterName.toInt() >= 80 -> firstFragment.setBackgroundResource(
                R.drawable.rain
            )
            location.weatherElement[1].time[0].parameter.parameterName.toInt() > 50 -> firstFragment.setBackgroundResource(
                R.drawable.drizzle
            )
        }

        if (location.locationName == myLocation) {
            tv_myLocation.visibility = View.VISIBLE
        } else {
            tv_myLocation.visibility = View.INVISIBLE
        }
        /**origin*/
        val calendar = Calendar.getInstance()
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        tv_city.text = location.locationName
        tv_currentTime.text =
            (month + 1).toString() + "月" + day.toString() + "日" + SimpleDateFormat(" HH:mm").format(
                System.currentTimeMillis()
            )
        //當日
        tv_startTime1.text = location.weatherElement[0].time[0].startTime
        tv_endTime1.text =
            location.weatherElement[0].time[0].endTime//.split(" ")[1]
        tv_status1.text =
            location.weatherElement[0].time[0].parameter.parameterName
        tv_rainProbability1.text =
            "降雨機率 ${location.weatherElement[1].time[0].parameter.parameterName} %"
        tv_temperature1.text =
            location.weatherElement[2].time[0].parameter.parameterName + "-" + location.weatherElement[4].time[0].parameter.parameterName
        tv_CI1.text = location.weatherElement[3].time[0].parameter.parameterName

        tv_startTime2.text = location.weatherElement[0].time[1].startTime
        tv_endTime2.text =
            location.weatherElement[0].time[1].endTime//.split(" ")[1]
        tv_status2.text =
            location.weatherElement[0].time[1].parameter.parameterName
        tv_rainProbability2.text =
            "降雨機率 ${location.weatherElement[1].time[1].parameter.parameterName} %"
        tv_temperature2.text =
            location.weatherElement[2].time[1].parameter.parameterName + "-" + location.weatherElement[4].time[1].parameter.parameterName
        tv_CI2.text = location.weatherElement[3].time[1].parameter.parameterName

        /**origin*/

//        /**Aria*/
//        fragmentLocation = location
//        rootView?.let {
////            rootView不爲空執行
//            val calendar = Calendar.getInstance()
//            val month = calendar.get(Calendar.MONTH)
//            val day = calendar.get(Calendar.DAY_OF_MONTH)
//            it.tv_city.text = location.locationName
//            it.tv_currentTime.text =
//                (month + 1).toString() + "月" + day.toString() + "日" + SimpleDateFormat(" HH:mm").format(
//                    System.currentTimeMillis()
//                )
//            //當日
//            it.tv_startTime1.text = location.weatherElement[0].time[0].startTime
//            it.tv_endTime1.text =
//                location.weatherElement[0].time[0].endTime//.split(" ")[1]
//            it.tv_status1.text =
//                location.weatherElement[0].time[0].parameter.parameterName
//            it.tv_rainProbability1.text =
//                "降雨機率 ${location.weatherElement[1].time[0].parameter.parameterName} %"
//
//            it.tv_startTime2.text = location.weatherElement[0].time[1].startTime
//            it.tv_endTime2.text =
//                location.weatherElement[0].time[1].endTime//.split(" ")[1]
//            it.tv_status2.text =
//                location.weatherElement[0].time[1].parameter.parameterName
//            it.tv_rainProbability2.text =
//                "降雨機率 ${location.weatherElement[1].time[1].parameter.parameterName} %"
//        }
//        /**Aria*/
        firstFragment.background.alpha = 50 //背景透明度 0-255
    }

    private val subscriber: () -> Unit = {
//        val ac = activity
        val loc = (activity as ILocationPublisher).getCurrentLocationWeatherRecord()

        loc?.let {
            updateContent(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as ILocationPublisher).remove(subscriber)
        /**Aria*/
//        rootView = null
        /**Aria*/
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as ILocationPublisher).add(subscriber)

        firstFragment.setOnClickListener {
            hideKeyboard(rootView, textView4)
        }
        firstFragment.background.alpha = 99 //背景透明度 0-255
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FirstFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FirstFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
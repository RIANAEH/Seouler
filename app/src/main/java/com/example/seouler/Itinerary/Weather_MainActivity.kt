package com.example.seouler


import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import kotlinx.android.synthetic.main.activity_weather_main.*
import kotlinx.android.synthetic.main.fragment_weather_today.*
import kotlinx.android.synthetic.main.fragment_weather_weekly.*
import kotlinx.android.synthetic.main.weather_weekly_item.view.*
import java.text.SimpleDateFormat
import java.util.*


class Weather_MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_main)
        println("<We> ${intent.extras}")
        println("<We> ${intent.extras?.get("0max")}")
        println("<We> ${intent.extras?.get("nowSunset")}")
        println("<We> ${intent.extras?.get("nowHumidity")}")

        var format = SimpleDateFormat("HH:mm")
        frg_weather_today.tv_temp.text = intent.extras?.getString("nowTemp")
        var kl = intent.extras?.getLong("nowSunrise")
        var strtmp = Date(kl!! * 1000L)
        frg_weather_today.tv_sunrise.text = format.format(strtmp)

        kl = intent.extras?.getLong("nowSunset")
        strtmp = Date(kl!! * 1000L)
        frg_weather_today.tv_sunset.text = format.format(strtmp)

        frg_weather_today.tv_humidity.text = intent.extras?.getInt("nowHumidity").toString()+"%"
        frg_weather_today.imageView.setImageResource(getWeatherIcon_file(intent.extras?.get("nowIcon") as String))
        frg_weather_today.tv_description.text = intent.extras!!.getString("nowDesc")

        ftg_weather_weekly.weather_item1.tv_dayOfWeek.text = intent.extras?.get("0day") as String
        ftg_weather_weekly.weather_item1.icon_weather.setImageResource(getWeatherIcon_file(intent.extras?.get("0icon") as String))
        ftg_weather_weekly.weather_item1.tv_tmpMin.text = intent.extras?.getDouble("0min").toString()+"℃"
        ftg_weather_weekly.weather_item1.tv_tmpMax.text =intent.extras?.getDouble("0max").toString()+"℃"

        ftg_weather_weekly.weather_item2.tv_dayOfWeek.text = intent.extras?.get("1day") as String
        ftg_weather_weekly.weather_item2.icon_weather.setImageResource(getWeatherIcon_file(intent.extras?.get("1icon") as String))
        ftg_weather_weekly.weather_item2.tv_tmpMin.text = intent.extras?.getDouble("1min").toString()+"℃"
        ftg_weather_weekly.weather_item2.tv_tmpMax.text =intent.extras?.getDouble("1max").toString()+"℃"

        ftg_weather_weekly.weather_item3.tv_dayOfWeek.text = intent.extras?.get("2day") as String
        ftg_weather_weekly.weather_item3.icon_weather.setImageResource(getWeatherIcon_file(intent.extras?.get("2icon") as String))
        ftg_weather_weekly.weather_item3.tv_tmpMin.text = intent.extras?.getDouble("2min").toString()+"℃"
        ftg_weather_weekly.weather_item3.tv_tmpMax.text =intent.extras?.getDouble("2max").toString()+"℃"

        ftg_weather_weekly.weather_item4.tv_dayOfWeek.text = intent.extras?.get("3day") as String
        ftg_weather_weekly.weather_item4.icon_weather.setImageResource(getWeatherIcon_file(intent.extras?.get("3icon") as String))
        ftg_weather_weekly.weather_item4.tv_tmpMin.text = intent.extras?.getDouble("3min").toString()+"℃"
        ftg_weather_weekly.weather_item4.tv_tmpMax.text =intent.extras?.getDouble("3max").toString()+"℃"

        ftg_weather_weekly.weather_item5.tv_dayOfWeek.text = intent.extras?.get("4day") as String
        ftg_weather_weekly.weather_item5.icon_weather.setImageResource(getWeatherIcon_file(intent.extras?.get("4icon") as String))
        ftg_weather_weekly.weather_item5.tv_tmpMin.text = intent.extras?.getDouble("4min").toString()+"℃"
        ftg_weather_weekly.weather_item5.tv_tmpMax.text =intent.extras?.getDouble("4max").toString()+"℃"

    }
    fun getWeatherIcon_file(strIconNum: String): Int =
        when (strIconNum) {
            "01d" -> R.drawable.w01d
            "01n" -> R.drawable.w01n
            "02d" -> R.drawable.w02d
            "02n" -> R.drawable.w02n
            "03d" -> R.drawable.w03w04
            "03n" -> R.drawable.w03w04
            "04d" -> R.drawable.w03w04
            "04d" -> R.drawable.w03w04
            "09d" -> R.drawable.w09
            "09n" -> R.drawable.w09
            "10d" -> R.drawable.w10d
            "10n" -> R.drawable.w10n
            "11d" -> R.drawable.w11
            "11n" -> R.drawable.w11
            "13d" -> R.drawable.w13
            "13n" -> R.drawable.w13
            "50d" -> R.drawable.w50
            "50n" -> R.drawable.w50
            else -> R.drawable.w01d
        }


}
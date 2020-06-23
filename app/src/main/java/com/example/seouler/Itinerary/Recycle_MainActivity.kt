package com.example.seouler

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.drawable.Drawable
import android.icu.text.SimpleDateFormat
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.Volley
import com.example.seouler.Exc_Recycle_MainActivity
import com.example.seouler.VolleyService_rate
import com.example.seouler.Weather_MainActivity
import com.example.seouler.Itinerary.*
import com.example.seouler.R
import com.example.seouler.VolleyService_weather
import com.example.seouler.dataClass.a_plan
import kotlinx.android.synthetic.main.activity_recycle_main.*
import org.json.JSONArray
import org.json.JSONObject

import java.util.*



class Recycle_MainActivity : AppCompatActivity() {
    var weather_async = Weather_Async(this) // API
    var rate_async = Rate_Async(this)


    private var calendar = Calendar.getInstance()
    private var year = calendar.get(Calendar.YEAR)
    private var month = calendar.get(Calendar.MONTH)
    private var day = calendar.get(Calendar.DAY_OF_MONTH)

    var tformat = SimpleDateFormat("h:mm a")

    private val dateSetListener = DatePickerDialog.OnDateSetListener(){ datePicker: DatePicker, year:Int, monthOfYear: Int, dayOfMonth: Int ->
        tv_date.setText(year.toString() + "/ " + (monthOfYear+1).toString() + "/ " + dayOfMonth.toString());
        this.year = year
        this.month = monthOfYear
        this.day = dayOfMonth
    }



    private var planlist = arrayListOf<a_plan>(
        a_plan("11:00 AM", "Noryangjin", "a"),
        a_plan("12:00 AM", "Insadong", "b"),
        a_plan("1:00 PM", "Gangnam", "c"),
        a_plan("2:00 PM", "Yongsan", "d"),
        a_plan("3:00 PM", "Wangsimni", "e"),
        a_plan("4:00 PM", "Cheongdam", "f"),
        a_plan("5:00 PM", "Jongmyo", "g"),
        a_plan("6:00 PM", "Namdaemoon", "b"),
        a_plan("7:00 PM", "Dongdaemoon", "c"),
        a_plan("8:00 PM", "Seoul-Station", "d"),
        a_plan("9:00 PM", "Jamsil", "e"),
        a_plan("10:00 PM", "Soongsil Univ", "f")
    )

    /*00000000000000000000000000000000000000000000000000000000000000*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycle_main)

        tv_date.text = year.toString() + "/ " + (month+1).toString() + "/ " + day.toString()

        weather_async.execute()
        rate_async.execute()



        //날씨
        simple_weather.setOnClickListener {
            val go_to_weather_intent = Intent(applicationContext, Weather_MainActivity::class.java)
            startActivity(go_to_weather_intent)
        }


        //환율
        simple_exc.setOnClickListener {
            val go_to_exc_intent = Intent(applicationContext, Exc_Recycle_MainActivity::class.java)
            startActivityForResult(go_to_exc_intent,1) //////
        }


        //일정
        val mAdapter = MainRvAdapter(this, planlist)
        mAdapter.setOnItemClickListener(object : MainRvAdapter.OnItemClickListener {
            override fun onItemClick(
                v: View?,
                position: Int
            ) {
                //Toast.makeText(applicationContext,"asdfasdf",Toast.LENGTH_SHORT).show()
                if (position != RecyclerView.NO_POSITION) { // 리스너 객체의 메서드 호출.
                    //Toast.makeText(applicationContext,"position : "+position, Toast.LENGTH_SHORT).show()
                    var go_to_modify_intent = Intent(applicationContext, PlanModifyActivity::class.java)
                    go_to_modify_intent.putExtra("position",position) //일정 순번
                    go_to_modify_intent.putExtra("time", planlist[position].time) //시간
                    go_to_modify_intent.putExtra("destination",planlist[position].destination) //목적지

                    startActivityForResult(go_to_modify_intent, 2)

                }
            }
        })
        recycler_view.adapter = mAdapter

        val lm = LinearLayoutManager(this)
        recycler_view.layoutManager = lm
        recycler_view.setHasFixedSize(true)
    }

    fun OnClickHandler(view: View?) {calendar.get(Calendar.YEAR)
        Log.d("Recycle_MainActivity","onclicked~~~~~")
        val dialog = DatePickerDialog(this, dateSetListener, year, month, day)
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //환율 설정//
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            if(data != null){
                tv_exc_nation.text = data.getStringExtra("exc_nation")
                tv_exc_rateUnit.text = data.getStringExtra("exc_rateUnit")
                tv_exchangeRate.text = data.getDoubleExtra("exc_exchange_rate", 0.0).toString()
                onResume()
            }
        }

        //일정수정
        else if(requestCode == 2 && resultCode == Activity.RESULT_OK){
            if(data != null){
                //planlist[data.getIntExtra("position",-1)].time
                var tmp_h= data.getIntExtra("time_h", 0)
                var tmp_m = data.getIntExtra("time_m",0)

                var tmp_c = Calendar.getInstance()
                tmp_c.set(year,month,day,tmp_h,tmp_m,0)
                var strTime = tformat.format(tmp_c.time)



                //Toast.makeText(this,"AFTER "+strTime,Toast.LENGTH_SHORT).show()
                planlist[data.getIntExtra("position",-1)].time = strTime
                recycler_view.adapter?.notifyDataSetChanged()
                onResume()
            }


        }
    }


}

    class Rate_Async(mainActivity: Activity) : AsyncTask<Int?, Int, List<String>>() {
        private var con = mainActivity
        override fun doInBackground(vararg params: Int?): List<String>? {
            VolleyService_rate.testVolley(con) { testSuccess ->
                if (testSuccess){
                    Toast.makeText(con, "환율 통신 성공!", Toast.LENGTH_SHORT).show()
                    var jsontmp = VolleyService_rate.response_json
                    println("환율쓰....JSON: $jsontmp")
                    if(jsontmp == JSONArray()){

                    }

                } else{
                    Toast.makeText(con, "환율 실패...", Toast.LENGTH_SHORT).show()
                }

            }
            return null
        }

    }


    class Weather_Async(mainActivity: Activity) : AsyncTask<Int?, Int, List<String>>() {
        private var con = mainActivity

        override fun doInBackground(vararg params: Int?): List<String>? {
            VolleyService_weather.testVolley(con) { testSuccess ->
                if (testSuccess) {


                    Toast.makeText(con, "통신 성공!", Toast.LENGTH_LONG).show()
                    //Toast.makeText(con, VolleyService_weather.weather_current.get("temp").toString(),Toast.LENGTH_SHORT).show()

                    var tmp = VolleyService_weather.response_json.get("current") as JSONObject
                    println("AAAAAAA $tmp")

                    con.temperature_now.text = tmp.get("temp").toString() + "℃"
                    var tmp2 = tmp.get("weather") as JSONArray
                    var tmp3 = tmp2.getJSONObject(0)
                    var icon_num = tmp3.get("icon") as String
                    println("AAAAAAA $icon_num")


                    con.icon_weather.setImageResource(getWeatherIcon_file(icon_num))

                    con.icon_weather.setImageResource(R.drawable.w01d)
                } else {
                    Toast.makeText(con, "통신 실패...!", Toast.LENGTH_LONG).show()
                }

            }
            return null
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





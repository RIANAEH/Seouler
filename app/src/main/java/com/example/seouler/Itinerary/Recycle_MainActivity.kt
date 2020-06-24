package com.example.seouler

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
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
import com.example.seouler.dataClass.a_exchange
import com.example.seouler.dataClass.a_plan
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import kotlinx.android.synthetic.main.activity_recycle_main.*
import org.json.JSONArray
import org.json.JSONObject
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*
import javax.net.ssl.*


var exclist = arrayListOf<a_exchange>()


class Recycle_MainActivity : AppCompatActivity() {
    var set_rate_index = 0
    val lm = LinearLayoutManager(this)

    var lcDate: LocalDate = LocalDate.now()
    var lcDate_set: LocalDate = lcDate
    //private var calendar = Calendar.getInstance()
    private var year = lcDate.year// calendar.get(Calendar.YEAR)
    private var month = lcDate.monthValue//calendar.get(Calendar.MONTH)
    private var day = lcDate.dayOfMonth //calendar.get(Calendar.DAY_OF_MONTH)

    lateinit var firestore : FirebaseFirestore


    var tformat = SimpleDateFormat("h:mm a")

    private val dateSetListener = DatePickerDialog.OnDateSetListener(){ datePicker: DatePicker, year:Int, monthOfYear: Int, dayOfMonth: Int ->
        tv_date.setText(year.toString() + "/ " + (monthOfYear+1).toString() + "/ " + dayOfMonth.toString());
        this.year = year
        this.month = monthOfYear + 1
        this.day = dayOfMonth
        lcDate_set = LocalDate.of(this.year, this.month, this.day)
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
        set_rate_index = intent.getIntExtra("SetRateIndex",0 )

        //firestore for Iti
        firestore = FirebaseFirestore.getInstance()

        var weather_async = Weather_Async(this) // API
        var rate_async = Rate_Async(this, set_rate_index)

        tv_date.text = year.toString() + "/ " + month.toString() + "/ " + day.toString()

        tv_date.setOnClickListener{
            //calendar.get(Calendar.YEAR) //tv_date click
            Log.d("Recycle_MainActivity","onclicked~~~~~")
            val dialog = DatePickerDialog(this, dateSetListener, year, month-1, day)
            dialog.show()

        }

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

        if(rate_async.getStatus() == AsyncTask.Status.FINISHED){
            tv_exc_rateUnit.text = exclist[set_rate_index].rateUnit
            tv_exchangeRate.text = exclist[set_rate_index].exchangeRate
        }
        else{
            tv_exc_rateUnit.text = "Timeout"
            tv_exchangeRate.text = "Timeout"
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

        btn_left.setOnClickListener{
            lcDate_set = lcDate_set.minusDays(1)
            update_setDate(lcDate_set)
        }

        btn_right.setOnClickListener{
            lcDate_set = lcDate_set.plusDays(1)
            update_setDate(lcDate_set)
        }


        recycler_view.adapter = mAdapter
        recycler_view.layoutManager = lm as RecyclerView.LayoutManager?
        recycler_view.setHasFixedSize(true)
    }

    private fun update_setDate(WlcDate_set : LocalDate){

        this.year = WlcDate_set.year
        this.month = WlcDate_set.monthValue
        this.day = WlcDate_set.dayOfMonth
        tv_date.text = this.year.toString()  + "/ " +  this.month.toString()  + "/ " +  this.day.toString()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //환율 설정//
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            if(data != null){
                tv_exc_rateUnit.text = data.getStringExtra("exc_rateUnit")
                tv_exchangeRate.text = data.getStringExtra("exc_exchange_rate")
                set_rate_index = data.getIntExtra("SetRateIndex",0)
                onResume()
            }
        }

        //일정수정
        else if(requestCode == 2 && resultCode == Activity.RESULT_OK){
            if(data != null){
                //planlist[data.getIntExtra("position",-1)].time
                val docRef = firestore.collection("Users").document("User").collection("2020-6-25")

                val samplePlan = hashMapOf(
                    "time" to LocalTime.of(11,30),
                    "destName" to "SEOUL",
                    "geoLatlon" to GeoPoint(34.0, 66.0)
                )
                // DB UPDATE
                docRef.add(samplePlan)
                    .addOnSuccessListener { documentReference ->
                        println( "DocumentSnapshot added with ID: ${documentReference.id}")
                    }
                    .addOnFailureListener { e ->
                        println( "Error adding document $e" )
                    }

                Toast.makeText(this,"뭐지..",Toast.LENGTH_SHORT).show()


                // DONE UPDATE
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

    override fun onBackPressed() {

        intent.putExtra("SetRateIndex", set_rate_index)
        this.setResult(Activity.RESULT_OK,intent)
        super.onBackPressed()
        //this.finish()
    }
}


    class Rate_Async(mainActivity: Activity, setrateindex : Int) : AsyncTask<Int?, Int, List<String>>() {
        private var act = mainActivity
        private var set_rate_index = setrateindex
        override fun doInBackground(vararg params: Int?): List<String>? {

            VolleyService_rate.testVolley(act) { testSuccess ->
                if (testSuccess){
                    Toast.makeText(act, "환율 통신 성공!", Toast.LENGTH_SHORT).show()

                    var response_json = VolleyService_rate.response_json

                    println("환율쓰2....JSON: $response_json")
                    //for (i in 0 until response_json.length()){
                    //    var response_json_obj : JSONObject = response_json.get(i) as JSONObject
                    //    exclist.add(jsonToExc(response_json_obj.get("cur_unit") as String, response_json_obj.get("kftc_deal_bas_r") as String))
                    //}

                    act.tv_exc_rateUnit.text = exclist[set_rate_index].rateUnit
                    act.tv_exchangeRate.text = exclist[set_rate_index].exchangeRate

                } else{
                    Toast.makeText(act, "환율 실패...", Toast.LENGTH_SHORT).show()
                }

            }

            return null
        }

        override fun onPostExecute(result: List<String>?) {
            super.onPostExecute(result)
            println("환율 난 해방이다 <야호>")


        }

    //    fun jsonToExc(cur_unit: String, kftc: String) : a_exchange{
     //       val exc = a_exchange(cur_unit, kftc)
     //       return exc
     //   }

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

//HTTP SSL//
class HttpsTrustManager : X509TrustManager {
    override fun checkClientTrusted(
        x509Certificates: Array<X509Certificate?>?, s: String?
    ) {
    }

    override fun checkServerTrusted(
        x509Certificates: Array<X509Certificate?>?, s: String?
    ) {
    }

    fun isClientTrusted(chain: Array<X509Certificate?>?): Boolean {
        return true
    }

    fun isServerTrusted(chain: Array<X509Certificate?>?): Boolean {
        return true
    }

    override fun getAcceptedIssuers(): Array<X509Certificate> {
        return _AcceptedIssuers
    }

    companion object {
        private var trustManagers: Array<TrustManager>? = null
        private val _AcceptedIssuers =
            arrayOf<X509Certificate>()

        fun allowAllSSL() {
            HttpsURLConnection.setDefaultHostnameVerifier(object : HostnameVerifier {
                override fun verify(arg0: String?, arg1: SSLSession?): Boolean {
                    return true
                }
            })
            var context: SSLContext? = null
            if (trustManagers == null) {
                trustManagers =
                    arrayOf(HttpsTrustManager())
            }
            try {
                context = SSLContext.getInstance("TLS")
                context.init(null, trustManagers, SecureRandom())
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            } catch (e: KeyManagementException) {
                e.printStackTrace()
            }
            if (context != null) {
                HttpsURLConnection.setDefaultSSLSocketFactory(
                    context
                        .getSocketFactory()
                )
            }
        }
    }
}
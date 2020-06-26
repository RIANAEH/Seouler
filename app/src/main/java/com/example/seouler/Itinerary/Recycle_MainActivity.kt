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
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.seouler.dataClass.WeatherDaily
import com.example.seouler.dataClass.a_exchange
import com.example.seouler.dataClass.a_plan
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.*
import com.google.firebase.firestore.model.value.TimestampValue
import kotlinx.android.synthetic.main.activity_recycle_main.*
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Thread.sleep
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
    var lat = 0.0
    var lon = 0.0
    var planlist = arrayListOf<a_plan>()
    var set_rate_index = 0
    val lm = LinearLayoutManager(this)
    var lcDate: LocalDate = LocalDate.now()
    var lcDate_set: LocalDate = lcDate
    //private var calendar = Calendar.getInstance()
    var year = lcDate.year// calendar.get(Calendar.YEAR)
    var month = lcDate.monthValue//calendar.get(Calendar.MONTH)
    var day = lcDate.dayOfMonth //calendar.get(Calendar.DAY_OF_MONTH)
    var uid = ""
    var firestore = FirebaseFirestore.getInstance()
    var cUsersRef = firestore.collection("Users")
    lateinit var dUserPlanRef : DocumentReference
    //lateinit var cFirestore : FirestoreRefs
    lateinit var mAdapter : MainRvAdapter
    val dateSetListener =
    DatePickerDialog.OnDateSetListener() { datePicker: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
        //tv_date.setText(year.toString() + "/ " + (monthOfYear+1).toString() + "/ " + dayOfMonth.toString());
        tv_date.text = date_to_string(year, monthOfYear + 1, dayOfMonth, "/ ")
        this.year = year
        this.month = monthOfYear + 1
        this.day = dayOfMonth
        lcDate_set = LocalDate.of(this.year, this.month, this.day)
        update_setDate(lcDate_set)
        UpdatePlanListFromFirestore(mAdapter)

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycle_main)

        set_rate_index = intent.getIntExtra("SetRateIndex", 0)
        uid = intent.getLongExtra("userId", 0).toString()  ///UID 설정!
        lat = intent.getDoubleExtra("lat", 0.0)
        lon = intent.getDoubleExtra("lon", 0.0)
        var tmp = hashMapOf(
            "uid" to uid
        )
        dUserPlanRef = cUsersRef.document(uid)
        dUserPlanRef.set(tmp)
        var WeatherDetailPreIntent = Intent(applicationContext, Weather_MainActivity::class.java)
        //날씨 클릭리스너 설정
        simple_weather.setOnClickListener {
            sleep(1000)
            startActivity(WeatherDetailPreIntent)
        }

        //API 활성화// 날씨, 환율
        var weather_async = Weather_Async(this, WeatherDetailPreIntent, lat, lon) // API
        var rate_async = Rate_Async(this, set_rate_index)
        weather_async.execute()
        rate_async.execute()

        //파이어스토어 업데이트
        //날짜 텍스트 설정
        tv_date.text = date_to_string(year, month, day, "/ ")
        tv_date.setOnClickListener {
            //calendar.get(Calendar.YEAR) //tv_date click
            Log.d("Recycle_MainActivity", "onclicked~~~~~")
            val dialog = DatePickerDialog(this, dateSetListener, year, month - 1, day)
            dialog.show()

        }



        //환율 클릭 리스너 설정
        simple_exc.setOnClickListener {
            val go_to_exc_intent = Intent(applicationContext, Exc_Recycle_MainActivity::class.java)
            startActivityForResult(go_to_exc_intent, 1) //////
        }
        if (rate_async.getStatus() == AsyncTask.Status.FINISHED) {
            tv_exc_rateUnit.text = exclist[set_rate_index].rateUnit
            tv_exchangeRate.text = exclist[set_rate_index].exchangeRate
        } else {
            tv_exc_rateUnit.text = "Timeout"
            tv_exchangeRate.text = "Timeout"
        }

        //일정추가버튼 리스너 설정
        btn_addPlan.setOnClickListener {
            var go_to_add_intent = Intent(applicationContext, PlanModifyActivity::class.java)
            go_to_add_intent.putExtra("ACT", "add")
            go_to_add_intent.putExtra("position" , -1)
            go_to_add_intent.putExtra("date_y", lcDate_set.year)
            go_to_add_intent.putExtra("date_m", lcDate_set.monthValue)
            go_to_add_intent.putExtra("date_d", lcDate_set.dayOfMonth)
            startActivityForResult(go_to_add_intent, 2)
        }

        btn_left.setOnClickListener {
            lcDate_set = lcDate_set.minusDays(1)
            update_setDate(lcDate_set)
            UpdatePlanListFromFirestore(mAdapter)
        }

        btn_right.setOnClickListener {
            lcDate_set = lcDate_set.plusDays(1)
            update_setDate(lcDate_set)
            UpdatePlanListFromFirestore(mAdapter)
        }

        recycler_view.layoutManager = lm as RecyclerView.LayoutManager?
        recycler_view.setHasFixedSize(true)


        //일정 recyclerview 설정 -어댑터
        mAdapter = MainRvAdapter(this, planlist)
        mAdapter.setOnItemClickListener(object : MainRvAdapter.OnItemClickListener {
            override fun onItemClick(
                v: View?,
                position: Int
            ) {
                if (position != RecyclerView.NO_POSITION) { // 리스너 객체의 메서드 호출.
                    var go_to_modify_intent =
                        Intent(applicationContext, PlanModifyActivity::class.java)
                    go_to_modify_intent.putExtra("position", position) //일정 순번
                    go_to_modify_intent.putExtra("docId", planlist[position].documentId)
                    go_to_modify_intent.putExtra("time", time_to_string_A(planlist[position].time.hour,planlist[position].time.minute))
                    go_to_modify_intent.putExtra(
                        "destination",
                        planlist[position].destination
                    ) //목적지
                    go_to_modify_intent.putExtra("date_y", lcDate_set.year)
                    go_to_modify_intent.putExtra("date_m", lcDate_set.monthValue)
                    go_to_modify_intent.putExtra("date_d", lcDate_set.dayOfMonth)
                    go_to_modify_intent.putExtra("ACT", "modify")
                    println("<BIND> Planlist ${planlist[position].documentId}")
                    startActivityForResult(go_to_modify_intent, 2)

                }
            }
        })
        UpdatePlanListFromFirestore(mAdapter)
        recycler_view.adapter = mAdapter

        mAdapter.notifyDataSetChanged()


    }

    //파이어스토에서 값 가져와서 planlist에 얹히기
     fun UpdatePlanListFromFirestore(mAdapter :MainRvAdapter) {
        planlist.removeAll(planlist)
        dUserPlanRef.collection(date_to_string(year, month, day, "-"))
            .get()
            .addOnSuccessListener { documents ->
                    // println("<firestore> getDoc ${document.id} => ${document.data}")
                    //dUserPlanRef = document.reference                                       // DOC REF INIT 만약 doc 존재하지 않을때가 문제...

                    println("<firestore> $year, $month, $day")
                    for (document in documents) {
                        println("<firestore> Success : ${document.id} => ${document.data}")

                        var tmp_t = document.data.get("time") as Map<String, Unit>
                        var tmp_time = LocalTime.of(
                            (tmp_t.get("hour") as Long).toInt(),
                            (tmp_t.get("minute") as Long).toInt()
                        )
                        var tmp_geo = document.data.get("geoLatlon") as GeoPoint
                        var tmp_dest = document.data.get("destName") as String

                        planlist.add(a_plan(tmp_time, tmp_dest, document.id, tmp_geo))
                    }



                mAdapter.notifyDataSetChanged()

            }
            .addOnFailureListener{ e ->
                Log.d("<F>", "$e")

            }
        println("<BIND> In RecycleMain_ $planlist")

        onResume()
        println("<planlist> $planlist")
    }

    //날짜 세팅
     fun update_setDate(WlcDate_set: LocalDate) {
        this.year = WlcDate_set.year
        this.month = WlcDate_set.monthValue
        this.day = WlcDate_set.dayOfMonth
        tv_date.text = date_to_string(this.year, this.month, this.day, "/ ")
    }

    fun time_to_string_A(hour: Int, minute: Int): String {
        var strTmp = ""
        var a = hour
        if (a > 12) {
            a = a - 12
            strTmp = "PM"
        } else {
            strTmp = "AM"
        }

        return a.toString() + ":" + minute.toString() + " " + strTmp
    }

    fun date_to_string(year: Int, month: Int, day: Int, c: String): String {
        return year.toString() + c + month.toString() + c + day.toString()
    }

    //환율, 일정 수정폼 끝나는 것 처리
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //환율 설정//
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                tv_exc_rateUnit.text = data.getStringExtra("exc_rateUnit")
                tv_exchangeRate.text = data.getStringExtra("exc_exchange_rate")
                set_rate_index = data.getIntExtra("SetRateIndex", 0)
                onResume()
            }
        }

        //일정수정
        else if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                //planlist[data.getIntExtra("position",-1)].time

                var position = data.getIntExtra("position", -1)
                var docId = data.getStringExtra("docId")
                var dest = data.getStringExtra("dest") //목적지
                var tmp_h = data.getIntExtra("time_h", 0)
                var converted_h = tmp_h
                var tmp_m = data.getIntExtra("time_m", 0)
                var date_y = data.getIntExtra("date_y", 0)
                var date_m = data.getIntExtra("date_m", 0)
                var date_d = data.getIntExtra("date_d", 0)
                var time = LocalTime.of(tmp_h, tmp_m)
                var tmp_a = ""
                if (tmp_h > 12) {
                    converted_h = tmp_h - 12
                    tmp_a = "PM"

                } else {
                    tmp_a = "AM"
                }

                var strTime =
                    "%d".format(converted_h) + ":" + "%02d".format(tmp_m) + " " + tmp_a
                //var strDate = date_y.toString()+ "-" + date_m.toString() + "-" + date_d.toString() // Doc
                var strDate = date_to_string(date_y, date_m, date_d, "-")

                val samplePlan = hashMapOf(
                    "time" to time,
                    "destName" to dest ,
                    "geoLatlon" to GeoPoint(34.0, 66.0)
                )

                if (position != -1) {
                    //UpdatePlanListFromFirestore(mAdapter)
                    val docRef = dUserPlanRef.collection(strDate)
                    // DB UPDATE
                    if (date_y != this.year || date_m != this.month || date_d != this.day) {
                        //dUserPlanRef.document(docId).delete()
                        println("<INDATE> $date_y ${this.year}, $date_m ${this.month}, ... ${dUserPlanRef.id}")
                        dUserPlanRef.collection(
                            date_to_string(
                                this.year,
                                this.month,
                                this.day,
                                "-"
                            )
                        ).document(docId).delete()
                            .addOnSuccessListener { result ->
                                println("DocumentSnapshot deleted with ID: ${docId}")
                            }
                        planlist.removeAt(position)

                    }
                    docRef.document(docId).set(samplePlan)
                        .addOnSuccessListener { documentReference ->
                            println("DocumentSnapshot added with ID: ${docId}")
                        }
                        .addOnFailureListener { e ->
                            println("Error adding document $e")
                        }
                    // DONE UPDATE


                } else { //신규추가
                    val docRef = dUserPlanRef.collection(strDate)
                    docRef.whereEqualTo("destName", dest).whereEqualTo("time",time).get()
                        .addOnSuccessListener { documentReference ->
                            println("<PRINT> documentReference : ${documentReference.documents}")
                        }
                    docRef.add(samplePlan)
                        .addOnSuccessListener { documentReference ->
                            println("DocumentSnapshot added with ID: ${docId}")
                        }
                        .addOnFailureListener { e ->
                            println("Error adding document $e")
                        }
                }

                UpdatePlanListFromFirestore(mAdapter)
                mAdapter.notifyDataSetChanged()
            }


        }
    }

    override fun onBackPressed() {
        intent.putExtra("SetRateIndex", set_rate_index)
        this.setResult(Activity.RESULT_OK, intent)
        super.onBackPressed()
        //this.finish()
    }

    class Rate_Async(mainActivity: Activity, setrateindex: Int) :
        AsyncTask<Int?, Int, List<String>>() {
         var act = mainActivity
         var set_rate_index = setrateindex
        override fun doInBackground(vararg params: Int?): List<String>? {

            VolleyService_rate.testVolley(act) { testSuccess ->
                if (testSuccess) {
                    var response_json = VolleyService_rate.response_json

                    Log.d("<Rate>","환율쓰2....JSON: $response_json")

                    act.tv_exc_rateUnit.text = exclist[set_rate_index].rateUnit
                    act.tv_exchangeRate.text = exclist[set_rate_index].exchangeRate

                } else {
                    Log.d("<Rate>","환율 실패...")
                }

            }

            return null
        }


    }

    class Weather_Async(mainActivity: Activity, preIntent: Intent, lat : Double, lon : Double) : AsyncTask<Int?, Int, List<String>>() {

        var con = mainActivity
        var preIntent = preIntent
        var lat = lat
        var lon = lon
        override fun doInBackground(vararg params: Int?): List<String>? {
            VolleyService_weather.setlatlon(lat, lon)

            VolleyService_weather.testVolley(con) { testSuccess ->
                if (testSuccess) {


                    Log.d("<Weather>","통신 성공!")

                    var jWeatherCurrent = VolleyService_weather.response_json.get("current") as JSONObject
                    var jWeatherDailyArray = VolleyService_weather.response_json.get("daily") as JSONArray
                    var wDailyList = listOf(WeatherDaily(), WeatherDaily(), WeatherDaily(), WeatherDaily(), WeatherDaily())

                    for (i in 0..4){
                        var jTmp = jWeatherDailyArray.getJSONObject(i+1) // 내일 날씨 정보
                        var jtTmp = jTmp.get("temp") as JSONObject
                        var jwTmp = jTmp.get("weather") as JSONArray

                        wDailyList[i].tMin = jtTmp.getDouble("min")
                        wDailyList[i].tMax = jtTmp.getDouble("max")
                        wDailyList[i].stricon = jwTmp.getJSONObject(0).getString("icon")
                        preIntent.putExtra(i.toString()+"day", LocalDate.now().plusDays((i+1).toLong()).dayOfWeek.toString())
                        preIntent.putExtra(i.toString()+"order", i)
                        preIntent.putExtra(i.toString()+"min", wDailyList[i].tMin)
                        preIntent.putExtra(i.toString()+"max", wDailyList[i].tMax)
                        preIntent.putExtra(i.toString()+"icon", wDailyList[i].stricon)

                   }

                    var strWeatherNowTemp = jWeatherCurrent.get("temp").toString() + "℃"
                    var jWeatherNowWeatherArray = jWeatherCurrent.get("weather") as JSONArray
                    var jWeatherNowWeather = jWeatherNowWeatherArray.getJSONObject(0)
                    var strWeatherNowIconNum = jWeatherNowWeather.get("icon") as String

                    preIntent.putExtra("nowDesc", jWeatherNowWeather.getString("description"))
                    preIntent.putExtra("nowTemp", strWeatherNowTemp)
                    preIntent.putExtra("nowIcon", strWeatherNowIconNum)
                    preIntent.putExtra("nowSunrise", Date(jWeatherCurrent.getInt("sunrise").toLong()).time)
                    preIntent.putExtra("nowSunset",Date(jWeatherCurrent.getInt("sunset").toLong()).time )
                    preIntent.putExtra("nowHumidity", jWeatherCurrent.get("humidity") as Int)

                    con.temperature_now.text = strWeatherNowTemp
                    con.icon_weather.setImageResource(getWeatherIcon_file(strWeatherNowIconNum))
                } else {
                    println("통신 실패...!")
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

    //HTTP SSL/////////////////
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
             var trustManagers: Array<TrustManager>? = null
             val _AcceptedIssuers =
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
}
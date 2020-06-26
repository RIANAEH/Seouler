package com.example.seouler


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import com.example.seouler.R
import com.google.firebase.firestore.GeoPoint
import kotlinx.android.synthetic.main.activity_plan_modify.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*


class PlanModifyActivity : AppCompatActivity() {
    private var calendar = Calendar.getInstance()

    private var thisYear = calendar.get(Calendar.YEAR)
    private var month = calendar.get(Calendar.MONTH)
    private var day = calendar.get(Calendar.DAY_OF_MONTH)
    private var hour = calendar.get(Calendar.HOUR_OF_DAY)
    private var minute = calendar.get(Calendar.MINUTE)

    var tformat = SimpleDateFormat("HH:mm")

    var intent_from_modify = Intent()
    var intent_to_main = Intent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plan_modify)

        intent_from_modify = intent///////#######@@//!!!!!!!!!!!//위험///////////
        time_picker.setOnTimeChangedListener(myTimeListener)
        date_picker.setOnDateChangedListener(myDateListener)

        var act = intent_from_modify.getStringExtra("ACT")
        var docid = ""
        var destination = "tmp"
        var oldtime : String
        var oldtime_h :Int
        var oldtime_m :Int
        var oldtime_a :String
        if (act == "modify")  // 일정 수정일 경우
        {

            //*******DOCID******//
            docid = intent_from_modify.getStringExtra("docId") as String

            //*******시간********//
            oldtime = intent_from_modify.getStringExtra("time") //h:mm a
            oldtime_h = oldtime.substring(0,oldtime.lastIndexOf(":")).toInt()
            oldtime_m = oldtime.substring(oldtime.lastIndexOf(":")+1,oldtime.lastIndexOf(" ")).toInt()
            oldtime_a = oldtime.substring(oldtime.lastIndexOf(" ")+1)

            if(oldtime_a.equals("PM")||oldtime_a.equals("오후")){
                oldtime_h=oldtime_h+12
            }
            if(oldtime_h==12 && (oldtime_a.equals("AM")||oldtime_a.equals("오전"))){
                oldtime_h=0
            }

            if(oldtime_h==24 && (oldtime_a.equals("PM")||oldtime_a.equals("오후"))){
                oldtime_h=12
            }

            var time = Calendar.getInstance()

            time.set(thisYear,month,day,oldtime_h,oldtime_m,0)
            time_picker.hour = oldtime_h
            time_picker.minute = oldtime_m



            //*****목적지*****//
            tv_destination.setText(intent_from_modify.getStringExtra("destination"))



        }
        else if (act == "add"){ // 일정 신규일 경우
            var lct = LocalDate.now()
            time_picker.hour = 9
            time_picker.minute = 0
        }

        date_picker.init(intent_from_modify.getIntExtra("date_y",1), intent_from_modify.getIntExtra("date_m",1)-1,intent_from_modify.getIntExtra("date_d",1),myDateListener)



        btn_confirm.setOnClickListener {


            intent_to_main.putExtra("position",intent_from_modify.getIntExtra("position",-1))
            intent_to_main.putExtra("time_h", hour)
            intent_to_main.putExtra("time_m", minute)
            intent_to_main.putExtra("date_y", date_picker.year)
            intent_to_main.putExtra("date_m", date_picker.month+1)
            intent_to_main.putExtra("date_d", date_picker.dayOfMonth)
            intent_to_main.putExtra("dest", tv_destination.text.toString())
            intent_to_main.putExtra("docId", docid)
            setResult(Activity.RESULT_OK,intent_to_main)
            finish()
        }

    }

    private fun formatDate(year:Int, month:Int, day:Int):String{
        // Create a Date variable/object with user chosen date
        calendar.set(year, month, day, 0, 0, 0)
        val chosenDate = calendar.time

        // Format the date picker selected date
        val df = DateFormat.getDateInstance(DateFormat.MEDIUM)
        return df.format(chosenDate)
    }

    private val myTimeListener = TimePicker.OnTimeChangedListener(){ timePicker: TimePicker, hourOfDay: Int, minute: Int ->
        this.hour = hourOfDay
        this.minute = minute
        //Toast.makeText(this,"CHANGE "+hourOfDay, Toast.LENGTH_SHORT).show()

    }

    private val myDateListener = DatePicker.OnDateChangedListener(){ datePicker, year, month, day ->
        this.thisYear = year
        this.month = month
        this.day = day

    }


}


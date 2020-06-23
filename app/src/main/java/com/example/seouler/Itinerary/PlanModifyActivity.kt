package com.example.seouler


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import com.example.seouler.R
import kotlinx.android.synthetic.main.activity_iti_main.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class PlanModifyActivity : AppCompatActivity() {
    private var calendar = Calendar.getInstance()

    private val thisYear = calendar.get(Calendar.YEAR)
    private val month = calendar.get(Calendar.MONTH)
    private val day = calendar.get(Calendar.DAY_OF_MONTH)
    private var hour = calendar.get(Calendar.HOUR_OF_DAY)
    private var minute = calendar.get(Calendar.MINUTE)

    var tformat = SimpleDateFormat("HH:mm")

    var intent_from_modify = Intent()
    var intent_to_main = Intent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_iti_main)

        intent_from_modify = intent///////#######@@//!!!!!!!!!!!//위험///////////
        time_picker.setOnTimeChangedListener(myTimeListener)




        //*******시간********//
        var oldtime =intent_from_modify.getStringExtra("time") //h:mm a
        var oldtime_h = oldtime.substring(0,oldtime.lastIndexOf(":")).toInt()
        var oldtime_m = oldtime.substring(oldtime.lastIndexOf(":")+1,oldtime.lastIndexOf(" ")).toInt()
        var oldtime_a = oldtime.substring(oldtime.lastIndexOf(" ")+1)

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
        var destination = intent_from_modify.getStringExtra("destination")

        tv_destination.text = destination



        btn_confirm.setOnClickListener {


            intent_to_main.putExtra("position",intent_from_modify.getIntExtra("position",-1))
            intent_to_main.putExtra("time_h", hour)
            intent_to_main.putExtra("time_m", minute)
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


}


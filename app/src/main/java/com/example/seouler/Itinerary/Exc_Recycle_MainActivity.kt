package com.example.seouler

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.seouler.Itinerary.Exc_MainRvAdapter
import com.example.seouler.R
import com.example.seouler.dataClass.a_exchange
import kotlinx.android.synthetic.main.activity_exc_recycle_main.*
import java.util.*


class Exc_Recycle_MainActivity : AppCompatActivity() {
    private var calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH) + 1
    val day = calendar.get(Calendar.DAY_OF_MONTH)


    private var exclist = arrayListOf<a_exchange>(
        a_exchange("미국", "USD", 1500.0),
        a_exchange("호주", "AUD", 1400.0),
        a_exchange("중국", "CHY", 1300.0),
        a_exchange("일본", "JPY", 1200.0),
        a_exchange("캐나다", "CAD", 1100.0)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exc_recycle_main)

        val intent = Intent()


        val mAdapter = Exc_MainRvAdapter(this, exclist)
        mAdapter.setOnItemClickListener(object : Exc_MainRvAdapter.OnItemClickListener {
            override fun onItemClick(
                v: View?,
                position: Int
            ) {
                //*******환율 설정 여부********//
                if (position != RecyclerView.NO_POSITION) {
                    val builder: AlertDialog.Builder = AlertDialog.Builder(this@Exc_Recycle_MainActivity)
                    builder.setTitle("Choose Rate").setMessage("Set "+exclist[position].rateUnit+"?")

                    builder.setPositiveButton("OK") { dialogInterface: DialogInterface, i: Int ->
                        intent.putExtra("exc_nation",exclist[position].nation)
                        intent.putExtra("exc_rateUnit",exclist[position].rateUnit)
                        intent.putExtra("exc_exchange_rate",exclist[position].exchangeRate)

                        this@Exc_Recycle_MainActivity.setResult(Activity.RESULT_OK,intent)
                        this@Exc_Recycle_MainActivity.finish()
                    }

                    builder.setNegativeButton("Cancel") { dialog, id ->
                    }

                    val alertDialog: AlertDialog = builder.create()
                    alertDialog.show()


                }
            }
        })
        recycler_view.adapter = mAdapter

        val lm = LinearLayoutManager(this)
        recycler_view.layoutManager = lm
        recycler_view.setHasFixedSize(true)

        
        
    }


}
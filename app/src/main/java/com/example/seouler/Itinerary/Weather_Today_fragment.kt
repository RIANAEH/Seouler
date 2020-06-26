package com.example.seouler

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter


class Weather_Today_fragment: Fragment() {
    //val timez = arrayOf("1:00", "2:00", "3:00", "4:00","5:00","6:00","7:00","8:00",
     //   "9:00","10:00","11:00","12:00")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_weather_today, container, false)


        /*var line_chart :LineChart = view.findViewById(R.id.xml_line_chart)



        val entries:MutableList<Entry> = ArrayList()
        entries.add(Entry(0F, 3.3F))
        entries.add(Entry(1F, 2.5F))
        entries.add(Entry(2F, 0F))
        entries.add(Entry(3F, -8.2F))
        entries.add(Entry(4F, -7.5F))
        entries.add(Entry(5F, 5F))
        entries.add(Entry(6F, -3F))
        entries.add(Entry(7F, -7.5F))
        entries.add(Entry(8F, 5F))
        entries.add(Entry(9F, -3F))
        entries.add(Entry(10F, 5F))
        entries.add(Entry(11F, -3F))

        val lineDataSet = LineDataSet(entries, "temperature")

        lineDataSet.lineWidth = 2f
        lineDataSet.circleRadius = 6f
        lineDataSet.setCircleColor(Color.BLUE)
        lineDataSet.setCircleColorHole(Color.BLUE)
        lineDataSet.color = Color.parseColor("#FFA1B4DC")
        lineDataSet.setDrawCircleHole(true)
        lineDataSet.setDrawCircles(true)
        lineDataSet.setDrawHorizontalHighlightIndicator(false)
        lineDataSet.setDrawHighlightIndicators(false)
        lineDataSet.setDrawValues(true)



        val lineData = LineData(lineDataSet)
        line_chart.data = lineData
        lineData.setValueTextSize(14F)

        line_chart.axisRight.isEnabled = false; // no right axis
        line_chart.axisLeft.isEnabled=true;


        val xAxis: XAxis = line_chart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textSize = 10f
        xAxis.textColor = Color.BLACK
        xAxis.setDrawGridLines(false)
        xAxis.setDrawLimitLinesBehindData(false)
        xAxis.setDrawAxisLine(false)
        xAxis.valueFormatter = GraphAxisValueFormatter(timez);


       //xAxis.setLabelCount(10, true);

        val yLAxis: YAxis = line_chart.axisLeft
        yLAxis.textColor = Color.BLACK
        yLAxis.setDrawLabels(false); // no axis labels
        yLAxis.setDrawAxisLine(false); // no axis line
        yLAxis.setDrawGridLines(false); // no grid lines
        yLAxis.setDrawZeroLine(true); // draw a zero line

        //line_chart.setVisibleYRangeMaximum(-3f,yLAxis.axisDependency)

        val legend = line_chart.legend
        legend.isEnabled = false


        val description = Description()
        //description.text = "Description"

       // line_chart.setVisibleXRange(0f,7f);
        line_chart.zoom(2.5f,1f,0f,0f)
        line_chart.setVisibleXRange(7f,7f)

        line_chart.isDoubleTapToZoomEnabled = false
        line_chart.setDrawGridBackground(false)
        //line_chart.description = description
        line_chart.description.isEnabled = false

        line_chart.animateY(2000, Easing.EasingOption.EaseInCubic)
        line_chart.invalidate()
*/
        return view


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

}

class GraphAxisValueFormatter(dayOfWeek: Array<String>) : IAxisValueFormatter{
    var dow = dayOfWeek
    override fun getFormattedValue(value: Float, axis: AxisBase?): String {
        Log.d("Weather_Today_fragment","AAA"+dow[value.toInt()]!!)
        return dow[value.toInt()]!!
    }

}
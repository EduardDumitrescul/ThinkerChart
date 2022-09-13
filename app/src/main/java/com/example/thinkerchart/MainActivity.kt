package com.example.thinkerchart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val chartView: ChartView = findViewById(R.id.chartView)

        val pairList = mutableListOf<Pair<String, Int>>()
        for(i in 0 until 75 step 5)
            pairList.add(Pair("i${i}",i))
        chartView.setPairData(pairList)
    }
}
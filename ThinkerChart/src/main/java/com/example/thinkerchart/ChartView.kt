package com.example.thinkerchart

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat

private const val TAG = "BarChart"

class ChartView(context: Context, attributeSet: AttributeSet): ConstraintLayout(context, attributeSet) {

    private lateinit var horizontalAxis: LinearLayout
    private var verticalTextViews: MutableList<TextView> = mutableListOf()
    private var horizontalTextViews: MutableList<TextView> = mutableListOf()
    private lateinit var chartFrame: LinearLayout

    private var pairList: List<Pair<String, Int>> = listOf(Pair("item1", 10), Pair("item2", 0), Pair("item3", 0), Pair("item4", 0), Pair("item5", 0))
    fun setPairData(list: List<Pair<String, Int>>) {
        pairList = list
        buildView()

    }

    private var barViewList: MutableList<BarView> = mutableListOf()
    private var barDrawable: Drawable = ResourcesCompat.getDrawable(resources, R.drawable.rounded_background, context.theme)!!
    fun setBarDrawable(drawable: Drawable) {
        barViewList.forEach {
            it.setBackground(drawable)
        }
    }

    private var maxValue: Int = 0


    init {
        initDefault()
        buildView()
        
    }
    private fun buildView() {
        computeMaxValue()
        drawVerticalAxis()
        drawHorizontalAxis()
        post {
            drawBars()
        }


        setVerticalAxisPadding(8, 8)
        setHorizontalAxisPadding(4, 4)
    }

    private fun initDefault() {
        View.inflate(context, R.layout.chart_view, this)
        getViews()
    }

    private fun getViews() {
        horizontalAxis = findViewById(R.id.horizontal_axis)
        chartFrame = findViewById(R.id.chart_frame)
        verticalTextViews.add(findViewById(R.id.textView0))
        verticalTextViews.add(findViewById(R.id.textView1))
        verticalTextViews.add(findViewById(R.id.textView2))
        verticalTextViews.add(findViewById(R.id.textView3))
        verticalTextViews.add(findViewById(R.id.textView4))
        verticalTextViews.add(findViewById(R.id.textView5))
    }

    private fun computeMaxValue() {
        pairList.forEach {
            maxValue = maxOf(maxValue, it.second)
        }
        maxValue = ((maxValue - 1) / 25  + 1) * 25
    }

    private fun drawHorizontalAxis() {
        horizontalAxis.removeAllViews()
        horizontalTextViews.clear()
        pairList.forEach {
            val layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            val textView = TextView(context).apply {
                text = it.first
                gravity = Gravity.CENTER
            }
            horizontalTextViews.add(textView)
            horizontalAxis.addView(textView, layoutParams)
        }


    }

    private fun drawVerticalAxis() {
        val multiplier = maxValue / 25
        findViewById<TextView>(R.id.textView0).text = (0 * multiplier).toString()
        findViewById<TextView>(R.id.textView1).text = (5 * multiplier).toString()
        findViewById<TextView>(R.id.textView2).text = (10 * multiplier).toString()
        findViewById<TextView>(R.id.textView3).text = (15 * multiplier).toString()
        findViewById<TextView>(R.id.textView4).text = (20 * multiplier).toString()
        findViewById<TextView>(R.id.textView5).text = (25 * multiplier).toString()
    }

    private fun computeBarHeight(value: Int): Int {
        val heightUnitCount = 1f * value / (maxValue / 25f)
        val unitHeight = 1f * (chartFrame.height - verticalTextViews[0].height/2) / 25f
        val offset = 1f* verticalTextViews[0].height / 2
        Log.d(TAG, "${(heightUnitCount * unitHeight + offset).toInt()}   $heightUnitCount $unitHeight $offset ${chartFrame.measuredHeight}")
        return (heightUnitCount * unitHeight + offset).toInt()
    }

    private fun createBarView(value: Int): LinearLayout {
        val linearLayout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            background = barDrawable.constantState?.newDrawable() ?: barDrawable
            isClickable = true
        }



        return linearLayout
    }


    private fun drawBars() {
        barViewList.clear()
        chartFrame.removeAllViews()
        pairList.forEach {
            val barView = BarView(it.second)
            barViewList.add(barView)
            chartFrame.addView(barView.view, barView.layoutParams)
        }
    }

    private fun screenPixelDensity() = context.resources.displayMetrics.density

    private fun toPx(dp: Int): Int = (dp * screenPixelDensity()).toInt()

    /** All dimensions are in dp */
    fun setVerticalAxisPadding(start: Int, end: Int) {
        verticalTextViews.forEach {
            it.setPadding(toPx(start), 0, toPx(end), 0)
        }
    }

    /** All dimensions are in dp */
    fun setHorizontalAxisPadding(top: Int, bottom: Int) {
        horizontalAxis.setPadding(0, toPx(top), 0, toPx(bottom))
    }

    fun setTextSize(textSize: Int) {
        verticalTextViews.forEach {
            it.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize.toFloat())
        }
        horizontalTextViews.forEach {
            it.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize.toFloat())
        }
    }

    private inner class BarView(val value: Int) {
        val view: LinearLayout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            background = barDrawable.constantState?.newDrawable() ?: barDrawable
            isClickable = true
        }
        val layoutParams = LinearLayout.LayoutParams(0, computeBarHeight(value), 1f).apply {
            gravity = Gravity.BOTTOM
            setMargins(8, 0, 8, 0)
        }

        fun setBackground(background: Drawable) {
            view.background = background
        }
    }


}
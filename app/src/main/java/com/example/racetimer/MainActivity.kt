package com.example.racetimer

import androidx.appcompat.app.AppCompatActivity

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import java.util.Timer
import java.util.TimerTask

import android.view.KeyEvent.ACTION_DOWN
import android.view.KeyEvent.ACTION_UP
import android.view.MotionEvent.ACTION_BUTTON_PRESS
import android.view.MotionEvent.ACTION_POINTER_DOWN
import android.os.Looper
import android.widget.Toast


class MainActivity : AppCompatActivity() {
    var topStatus: Int = 0
    var bottomStatus: Int = 0
    var topTime: Double = 0.toDouble()
    var bottomTime: Double = 0.toDouble()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        topStatus = Constant.BEFORE_START
        bottomStatus = Constant.BEFORE_START
        val textViewTop = findViewById<TextView>(R.id.textViewTop)
        val textViewBottom = findViewById<TextView>(R.id.textViewBottom)
        //
        val textView = findViewById<TextView>(R.id.textView)
        val textView2 = findViewById<TextView>(R.id.textView2)
        //
        val LayoutTop = findViewById<ViewGroup>(R.id.LayoutTop)
        LayoutTop.setBackgroundColor(Color.WHITE)
        val LayoutBottom = findViewById<ViewGroup>(R.id.LayoutBottom)
        LayoutBottom.setBackgroundColor(Color.WHITE)
        //
        var topTimer = Timer()
        var bottomTimer = Timer()
        //
        val topCountDownTimer = object : CountDownTimer(500, 1) {
            override fun onTick(millisUntilFinished: Long) {
            }
            override fun onFinish() {
                topStatus = Constant.READY_TO_GO
                LayoutTop.setBackgroundColor(Color.GREEN)
            }
        }
        val bottomCountDownTimer = object : CountDownTimer(500, 1) {
            override fun onTick(millisUntilFinished: Long) {
            }
            override fun onFinish() {
                bottomStatus = Constant.READY_TO_GO
                LayoutBottom.setBackgroundColor(Color.GREEN)
            }
        }
        //
        val statusTimer = Timer()



        statusTimer.schedule(object : TimerTask() {
            override fun run() {
                Handler(Looper.getMainLooper()).post(Runnable { textView.text = topStatus.toString()
                    //
                    textView2.text = bottomStatus.toString() })
            }
        }, 10, 100)



        LayoutTop.setOnTouchListener { view, motionEvent ->
            when (motionEvent.actionMasked) {
                ACTION_UP -> when (topStatus) {
                    Constant.BEFORE_START -> {
                        LayoutTop.setBackgroundColor(Color.WHITE)
                        topCountDownTimer.cancel()
                        topStatus = Constant.BEFORE_START
                    }

                    Constant.READY_TO_GO -> if (bottomStatus == Constant.READY_TO_GO || bottomStatus == Constant.RUNNING || bottomStatus == Constant.ENDED) {
                        topStatus = Constant.RUNNING
                        LayoutTop.setBackgroundColor(Color.WHITE)
                        val a = System.currentTimeMillis().toDouble()
                        topTimer = Timer()
                        topTimer.schedule(object : TimerTask() {
                            override fun run() {
                                topTime = (System.currentTimeMillis() - a) / 1000
                                runOnUiThread { textViewTop.text = topTime.toString() }
                            }
                        }, 0, 1)
                    } else {
                        topStatus = Constant.BEFORE_START
                        LayoutTop.setBackgroundColor(Color.WHITE)
                    }
                    Constant.RUNNING -> {
                    }
                    Constant.ENDED -> {
                    }
                }

                ACTION_DOWN -> when (topStatus) {
                    Constant.BEFORE_START -> {
                        LayoutTop.setBackgroundColor(Color.RED)
                        topCountDownTimer.start()
                    }
                    Constant.READY_TO_GO -> {
                    }
                    Constant.RUNNING -> {
                        topStatus = Constant.ENDED
                        topTimer.purge()
                        topTimer.cancel()
                        if (topStatus == Constant.ENDED && bottomStatus == Constant.ENDED) {
                            topStatus = Constant.BEFORE_START
                            bottomStatus = Constant.BEFORE_START
                        }
                    }
                    Constant.ENDED -> {
                    }
                }
            }
            true
        }

        //==============================================================
        LayoutBottom.setOnTouchListener { view, motionEvent ->
            when (motionEvent.actionMasked) {
                ACTION_UP -> when (bottomStatus) {
                    Constant.BEFORE_START -> {
                        LayoutBottom.setBackgroundColor(Color.WHITE)
                        bottomCountDownTimer.cancel()
                        bottomStatus = Constant.BEFORE_START
                    }

                    Constant.READY_TO_GO -> if (topStatus == Constant.READY_TO_GO || topStatus == Constant.RUNNING || topStatus == Constant.ENDED) {
                        bottomStatus = Constant.RUNNING
                        LayoutBottom.setBackgroundColor(bottomColor)
                        val a = System.currentTimeMillis().toDouble()
                        bottomTimer = Timer()
                        bottomTimer.schedule(object : TimerTask() {
                            override fun run() {
                                bottomTime = (System.currentTimeMillis() - a) / 1000
                                runOnUiThread { textViewBottom.text = bottomTime.toString() }
                            }
                        }, 0, 1)
                    } else {
                        bottomStatus = Constant.BEFORE_START
                        LayoutBottom.setBackgroundColor(bottomColor)
                    }
                    Constant.RUNNING -> {
                    }
                    Constant.ENDED -> {
                    }
                }

                ACTION_DOWN -> when (bottomStatus) {
                    Constant.BEFORE_START -> {
                        LayoutBottom.setBackgroundColor(Color.RED)
                        bottomCountDownTimer.start()
                    }
                    Constant.READY_TO_GO -> {
                    }
                    Constant.RUNNING -> {
                        bottomStatus = Constant.ENDED
                        bottomTimer.purge()
                        bottomTimer.cancel()
                        if (topStatus == Constant.ENDED && bottomStatus == Constant.ENDED) {
                            topStatus = Constant.BEFORE_START
                            bottomStatus = Constant.BEFORE_START
                        }
                    }
                    Constant.ENDED -> {
                    }
                }
            }
            true
        }
    }

    companion object {
        private val topColor: Int = 0
        private val bottomColor: Int = 0
    }
}

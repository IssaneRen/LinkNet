package com.example.linknet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        tv_click_result1.setOnClickListener{
            testCoroutine()
        }
    }

    /**
     * 测试 协程代码
     */
    private fun testCoroutine() {
        val lastTime = System.currentTimeMillis()
        val coroutineScope = CoroutineScope(Dispatchers.Default)
        coroutineScope.launch{
            val one = async(Dispatchers.IO) { doSomethingUsefulOne() }
            val two = async(Dispatchers.IO) { doSomethingUsefulTwo() }
            withContext(Dispatchers.Main) {
                tv_desc1.text = (one.await() + two.await()).toString() + " Time: " + (System.currentTimeMillis() - lastTime).toString()
            }
        }
    }

    suspend fun doSomethingUsefulOne(): Int {
        delay(1000L) // 假设我们在这里做了一些有用的事
        return 13
    }

    suspend fun doSomethingUsefulTwo(): Int {
        withContext(Dispatchers.IO) {
            Thread.sleep(1000L) // 假设我们在这里也做了一些有用的事
        }
        return 29
    }

    private fun outPutThreadLog(index: Int, sleepTimeMillis : Long = 1500L) {
        Log.d(TAG, "$index : -- Current thread : ${Thread.currentThread().name}")
        if (sleepTimeMillis > 0) {
            Thread.sleep(sleepTimeMillis)
        }
    }

    private fun outPutThreadLog(index: Long, sleepTimeMillis : Long = 1500L) {
        Log.d(TAG, "$index : -- Current thread : ${Thread.currentThread().name}")
        if (sleepTimeMillis > 0) {
            Thread.sleep(sleepTimeMillis)
        }
    }
}

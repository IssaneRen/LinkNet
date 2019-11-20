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
        initData()
    }

    private fun initView() {
        tv_click_result1.setOnClickListener{
            testCoroutine()
        }
        tv_click_result2.setOnClickListener{
            testYield()
        }
    }

    private fun initData() {

    }

    /**
     * 测试 协程代码
     */
    private fun testCoroutine() {
        outPutThreadLog(0)

        val coroutineScope = CoroutineScope(Dispatchers.IO)
        var job1 =coroutineScope.launch {
            outPutThreadLog(1)

            delay(200L)

            yield()

            outPutThreadLog(2)
        }

        var job2 = GlobalScope.launch {
            outPutThreadLog(3)

            delay(400L)

            outPutThreadLog(4)
            job1.join()
        }

        outPutThreadLog(5)

        Thread.sleep(600L)

        outPutThreadLog(6)
    }

    @ObsoleteCoroutinesApi
    private fun testYield() {
        outPutThreadLog(0)
        val singleDispatcher = newSingleThreadContext("Single")

        runBlocking {
            val job = GlobalScope.launch {
                launch {
                    withContext(singleDispatcher) {
                        repeat(3) {
                            outPutThreadLog(1)
                            //yield()
                        }
                    }
                }

                launch {
                    withContext(singleDispatcher) {
                        repeat(3) {
                            outPutThreadLog(2)
                            //yield()
                        }
                    }
                }
            }

            job.join()
        }
    }

    private fun outPutThreadLog(index: Int) {
        Log.d(TAG, "$index : -- Current thread : ${Thread.currentThread().name}")
    }
}

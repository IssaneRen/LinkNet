package com.example.linknet

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Message
import android.util.Log
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"
    var bitmap: Bitmap? = null
    lateinit var image: ImageView
    var handler = myHandler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        image = iv_image_test
        tv_click_result1.setOnClickListener {
//            loadImageWithHandler()
            loadImageWithCoroutine()
        }
    }

    private fun loadImageWithCoroutine() {
        Log.e(TAG, "开始展示图片人物的协程前，当前线程： ${Thread.currentThread().name}")
        val coroutineContext = CoroutineScope(Dispatchers.Main)
        coroutineContext.launch {
            Log.e(TAG, "开始任务后，当前线程： ${Thread.currentThread().name}")
            val bitmap = getInternetImage("https://static.jam.vg/raw/658/d1/z/29f87.png")
            Log.e(TAG, "完成down图片的时候，当前线程： ${Thread.currentThread().name}")
            iv_image_test.setImageBitmap(bitmap)
        }
    }

    private fun loadImageWithHandler() {
        Upload("https://static.jam.vg/raw/658/d1/z/29f87.png").start()
//        Upload("https://static-cdn.jtvnw.net/previews-ttv/live_user_semiwork.jpg").start()
    }


    private suspend fun getInternetImage(url: String) : Bitmap? = withContext(Dispatchers.IO) {
        Log.e(TAG, "开始准备down图片的时候，当前线程： ${Thread.currentThread().name}")
        try {
            val url = URL(url)
            val inputStream = url.openStream()
            return@withContext BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        null
    }

    /*在主线程中更新UI的Handler*/
    inner class myHandler : Handler() {
        override fun handleMessage(msg: Message) {
            if (msg.what == 111) {
                iv_image_test.setImageBitmap(bitmap)
            }
        }
    }

    private inner class Upload(url: String) : Thread() {
        var mUrl: String? = url

        override fun run() {
            super.run()
            try {
                //对资源链接
                var url = URL(mUrl)
                //打开输入流
                var inputStream = url.openStream()
                //对网上资源进行下载并转换为位图图片
                bitmap = BitmapFactory.decodeStream(inputStream)
                handler.sendEmptyMessage(111)
                inputStream.close()

                inputStream = url.openStream()
                val file = File("${Environment.getExternalStorageDirectory()}/newImage.png")
                val fileOutputStream = FileOutputStream(file)
                var hasRead = 0

                while (hasRead != -1) {
                    hasRead = inputStream.read()
                    fileOutputStream.write(hasRead)
                }
                fileOutputStream.close()
                inputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

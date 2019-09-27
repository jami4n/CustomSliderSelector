package com.jamian.instatest

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log

class IconHandleService : Service(){
    val binder = SimpleBinder()

    override fun onBind(intent: Intent?): IBinder? {

        Log.d("12345","onBind from service --------------------------")

        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {


        Log.d("12345","onStartCommand from service --------------------------")
        return super.onStartCommand(intent, flags, startId)
    }

    inner class SimpleBinder : Binder() {
        val service = this@IconHandleService
    }

}
package com.jamian.instatest

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder

class ServiceCon : ServiceConnection{


    override fun onServiceDisconnected(name: ComponentName?) {

    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {

    }

}
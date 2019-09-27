package com.jamian.instatest

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.facebook.CallbackManager
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.graphics.drawable.shapes.RectShape
import android.graphics.drawable.PaintDrawable
import android.graphics.Shader
import android.support.v4.content.ContextCompat
import android.graphics.LinearGradient
import android.graphics.drawable.ShapeDrawable
import android.view.MotionEvent
import android.view.View

import android.support.constraint.ConstraintLayout
import android.util.Log
import android.opengl.ETC1.getHeight
import android.view.ViewTreeObserver
import android.support.v4.app.SupportActivity
import android.support.v4.app.SupportActivity.ExtraData
import android.support.v4.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.support.constraint.Constraints
import android.view.View.X
import android.view.animation.TranslateAnimation


class MainActivity : AppCompatActivity() {

    lateinit var callbackManager:CallbackManager

    var serviceCon = ServiceCon()

    var spaceArray:IntArray? = null
    var eachSpace = 0
    var leftMarginOfHolder = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        b.radius = 80F



/*        packageManager.setComponentEnabledSetting(
            ComponentName("com.jamian.instatest", "com.jamian.instatest.MainActivityR"),
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP
        )

        packageManager.setComponentEnabledSetting(
            ComponentName("com.jamian.instatest", "com.jamian.instatest.MainActivity"),
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP
        )*/

        val bindIntent = Intent(this, IconHandleService::class.java)
        bindService(bindIntent, serviceCon, BIND_AUTO_CREATE)
        startService(bindIntent)


        /*callbackManager = CallbackManager.Factory.create()
        loginButton.setReadPermissions(Arrays.asList("email"));
        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.d("12345","++++++++++++ " + loginResult.accessToken)
            }

            override fun onCancel() {
                
            }

            override fun onError(exception: FacebookException) {
                Log.d("12345"," ---------- " + exception.localizedMessage)
            }
        })

        btnGetToken.setOnClickListener {
            val accessToken = AccessToken.getCurrentAccessToken()
            val isLoggedIn = accessToken != null && !accessToken.isExpired

                Log.d("12345"," ---------- " + accessToken.token + " ------- Logged In : " + isLoggedIn)
            }
        }*/
//-------------------------------------------------------------

        val colorsFromServer = arrayListOf("#FFF0236E","#FFC21C59","#FF791238","#FF470B21")

        var _xDelta = -1
        var maxWidth = -1

        lltest.background = getMultiColorDrawable(colorsFromServer)

        lltest.getViewTreeObserver()
            .addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    lltest.getViewTreeObserver().removeOnGlobalLayoutListener(this)
                    var t= IntArray(2)
                    lltest.getLocationInWindow(t)
                    maxWidth = t[0] + lltest.width - (selector.width)
                    getBarBreakup(lltest,colorsFromServer)

                    var params = lltest.layoutParams as ConstraintLayout.LayoutParams

                }
            })

        lltest.setOnTouchListener { v, event ->

            val X = event.rawX.toInt()

            when(event.action){
                MotionEvent.ACTION_UP -> {
                    snapSelectorTo(X)
                }
            }

            true
        }

        selector.setOnTouchListener { v, event ->
            val X = event.rawX.toInt()
            Log.d("12345","moved to X $X")


            when(event.action){
                MotionEvent.ACTION_DOWN -> {
                    val lParams = v.getLayoutParams() as ConstraintLayout.LayoutParams
                    _xDelta = X - (lParams.leftMargin)
                }
                MotionEvent.ACTION_UP -> {

                    snapSelectorTo(X)

                }
                MotionEvent.ACTION_MOVE -> {

                    if(X < maxWidth){
                        val layoutParams = v.getLayoutParams() as ConstraintLayout.LayoutParams

                        layoutParams.leftMargin = X - _xDelta
                        layoutParams.rightMargin = 0
                        v.layoutParams = layoutParams
                    }else{
                        val layoutParams = v.getLayoutParams() as ConstraintLayout.LayoutParams
                        layoutParams.leftMargin = lltest.width - v.width
                        layoutParams.rightMargin = 0
                        v.layoutParams = layoutParams
                    }
                }
            }
            true
        }





//-------------------------------------------------------------end
    }

    //-------------------------------------------------------------start
    fun snapSelectorTo(movedTo: Int) {

        var selectedSpace:Int? = null
        var moveTo:Int? = null


        spaceArray?.let {array ->

            if(movedTo < array[1]){
                selectedSpace = 0
                moveTo = (array[0] + (eachSpace / 2)) - leftMarginOfHolder - (selector.width / 2) //((array[1] - array[0] - selector.width) / 2)
                Log.d("12345","animated to $moveTo")
                val layoutParams = selector.layoutParams as ConstraintLayout.LayoutParams

                layoutParams.leftMargin = moveTo?.toInt()?:0
                layoutParams.rightMargin = 0
                selector.layoutParams = layoutParams

            }else if(movedTo > array[array.lastIndex]){

                selectedSpace = array.lastIndex
                moveTo = (array[array.lastIndex] + (eachSpace / 2)) - leftMarginOfHolder - (selector.width / 2) //((array[1] - array[0] - selector.width) / 2)
                Log.d("12345","animated to $moveTo")
                val layoutParams = selector.layoutParams as ConstraintLayout.LayoutParams

                layoutParams.leftMargin = moveTo?:0
                layoutParams.rightMargin = 0
                selector.layoutParams = layoutParams

            }else{

                for(i in array.indices){
                    if(array[i] < movedTo && array[i + 1] > movedTo){
                        selectedSpace = i
                        break
                    }
                }

                //selectedSpace = array.lastIndex
                selectedSpace?.let {selected ->

                    moveTo = (array[selected] + (eachSpace / 2)) - leftMarginOfHolder - (selector.width / 2)
                    Log.d("12345","animated to $moveTo")
                    val layoutParams = selector.layoutParams as ConstraintLayout.LayoutParams

                    layoutParams.leftMargin = moveTo?:0
                    layoutParams.rightMargin = 0
                    selector.layoutParams = layoutParams
                }

            }
        }

    }

    private fun getBarBreakup(lltest: View, colorsFromServer: ArrayList<String>) {
        val t= IntArray(2)
        lltest.getLocationInWindow(t)
        var startPoint = t[0]
        leftMarginOfHolder = startPoint
        var endPoint = t[0] + lltest.width
        eachSpace = (lltest.width / colorsFromServer.size)

        /*Log.d("12345","startPoint $startPoint")
        Log.d("12345","endPoint $endPoint")
        Log.d("12345","lltest.width " + lltest.width)
        Log.d("12345", "each Quadrant  $eachSpace")*/

        val lengthBreakup = IntArray(colorsFromServer.size)
        lengthBreakup[0] = startPoint
        for(x in 1 until colorsFromServer.size){
            lengthBreakup[x] = lengthBreakup[x -1] + eachSpace }
        spaceArray = lengthBreakup

        //Log.d("12345",lengthBreakup.contentToString())
    }

    //-------------------------------------------------------------end

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}

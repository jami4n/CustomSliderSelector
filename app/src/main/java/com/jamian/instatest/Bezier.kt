package com.jamian.instatest

import android.content.Context
import android.graphics.*

import android.util.AttributeSet
import android.view.View



class Bezier @JvmOverloads constructor
    (context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0)
    : View(context,attrs,defStyleAttr,defStyleRes) {

    var mPaint: Paint = Paint()
    var mPath: Path = Path()

    var radius = 110F

    init {
        mPaint.style = Paint.Style.FILL_AND_STROKE
        mPaint.color = Color.WHITE
        setBackgroundColor(Color.TRANSPARENT)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val mNavigationBarWidth = width.toFloat()
        val mNavigationBarHeight = height.toFloat()

        val mFirstCurveStartPoint = Cordinates(((mNavigationBarWidth / 2) + (mNavigationBarWidth / 3)) -
                                (radius * 2) - (radius / 3), mNavigationBarHeight / 2)

        val mFirstCurveEndPoint = Cordinates(((mNavigationBarWidth / 2) + (mNavigationBarWidth / 3)),
                                radius - (radius / 8))

        val mSecondCurveStartPoint = mFirstCurveEndPoint

        val mSecondCurveEndPoint = Cordinates(((mNavigationBarWidth / 2) + (mNavigationBarWidth / 3)) +
                                (radius * 2) + (radius / 3), mNavigationBarHeight / 2)

        val mFirstCurveControlPoint1 = Cordinates(mFirstCurveStartPoint.x +
                                radius + (radius / 4), mFirstCurveStartPoint.y)

        val mFirstCurveControlPoint2 = Cordinates(mFirstCurveEndPoint.x -
                                (radius * 2) + radius, mFirstCurveEndPoint.y)

        val mSecondCurveControlPoint1 = Cordinates(mSecondCurveStartPoint.x +
                                (radius * 2) - radius, mSecondCurveStartPoint.y)

        val mSecondCurveControlPoint2 = Cordinates(mSecondCurveEndPoint.x -
                                (radius + (radius / 4)), mSecondCurveEndPoint.y)


        //--------------------------------------------------------------


        mPath?.reset()
        mPath?.moveTo(0F, mNavigationBarHeight / 2)
        mPath?.lineTo(mFirstCurveStartPoint.x, mFirstCurveStartPoint.y)

        mPath?.cubicTo(mFirstCurveControlPoint1.x, mFirstCurveControlPoint1.y,
            mFirstCurveControlPoint2.x, mFirstCurveControlPoint2.y,
            mFirstCurveEndPoint.x, mFirstCurveEndPoint.y)

        mPath?.cubicTo(mSecondCurveControlPoint1.x, mSecondCurveControlPoint1.y,
            mSecondCurveControlPoint2.x, mSecondCurveControlPoint2.y,
            mSecondCurveEndPoint.x, mSecondCurveEndPoint.y)

        mPath?.lineTo(mNavigationBarWidth, mNavigationBarHeight / 2)
        mPath?.lineTo(mNavigationBarWidth, mNavigationBarHeight)
        mPath?.lineTo(0F, mNavigationBarHeight)
        mPath?.close()

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawPath(mPath, mPaint)

    }

    data class Cordinates(var x:Float = 0F, var y:Float = 0F)
}
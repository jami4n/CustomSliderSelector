package com.jamian.instatest

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.drawable.Drawable
import android.graphics.drawable.PaintDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape

private fun createArrayWithColors(list: ArrayList<String>):Pair<IntArray,FloatArray>{

    var colorsList = IntArray(list.size *  2)
    var positionsList = FloatArray(list.size *  2)

    var i = 0
    var shadeWidth = 1.00f / list.size
    var shadeLastPosition = 0f

    for(value in list){

        positionsList[i] = shadeLastPosition
        colorsList[i] = Color.parseColor(value)
        i++

        shadeLastPosition += shadeWidth
        positionsList[i] = shadeLastPosition - 0.001f
        colorsList[i] = Color.parseColor(value)
        i++

    }

    return Pair(colorsList,positionsList)
}


fun getMultiColorDrawable(list: ArrayList<String>):Drawable{

    var colorsPair = createArrayWithColors(list)


    val sf = object : ShapeDrawable.ShaderFactory() {
        override fun resize(width: Int, height: Int): Shader {
            return LinearGradient(
                0f,
                0f,
                width.toFloat(),
                0f,
                colorsPair.first,
                colorsPair.second,
                Shader.TileMode.REPEAT
            )
        }
    }
    val p = PaintDrawable()
    p.shape = RectShape()
    p.setCornerRadius(40f)
    p.shaderFactory = sf

    return p
}
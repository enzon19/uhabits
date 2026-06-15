package org.isoron.uhabits.widgets.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.View
import java.io.File

class DailySummaryLockscreenWidgetView(context: Context) : View(context) {

    private var pendingCount: Int = 0
    private var totalCount: Int = 0
    private var bgAlpha: Int = 0

    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val trackPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val oval = RectF()

    private fun getSystemColor(attr: Int): Int {
        val typedArray = context.theme.obtainStyledAttributes(intArrayOf(attr))
        val color = typedArray.getColor(0, Color.WHITE)
        typedArray.recycle()
        return color
    }

    private fun adjustAlpha(color: Int, factor: Float): Int {
        val alpha = (255 * factor).toInt()
        return Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color))
    }

    private var fgColor: Int = Color.WHITE

    init {
        bgPaint.color = Color.TRANSPARENT
        bgPaint.style = Paint.Style.FILL

        //trackPaint.color = "#33FFFFFF".toColorInt()
        trackPaint.style = Paint.Style.STROKE
        trackPaint.strokeWidth = 18f
        trackPaint.strokeCap = Paint.Cap.ROUND

        progressPaint.style = Paint.Style.STROKE
        progressPaint.strokeWidth = 18f
        progressPaint.strokeCap = Paint.Cap.ROUND

        //textPaint.color = "#F6F6F5".toColorInt()
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.isFakeBoldText = true

        fgColor = getSystemColor(android.R.attr.colorForeground)
        trackPaint.color = adjustAlpha(fgColor, 0.25f)
        progressPaint.color = fgColor
        textPaint.color = fgColor

        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                try {
                    textPaint.typeface = android.graphics.Typeface.create("sec", android.graphics.Typeface.NORMAL)
                    textPaint.fontVariationSettings = "'wght' 500"
                } catch (e: Exception) {
                    val file = File("/system/fonts/OneUISans-VF.ttf")
                    val font = android.graphics.fonts.Font.Builder(file)
                        .setWeight(900)
                        .build()
                    val family = android.graphics.fonts.FontFamily.Builder(font).build()
                    textPaint.typeface =
                        android.graphics.Typeface.CustomFallbackBuilder(family).build()
                }
            } else {
                textPaint.typeface = android.graphics.Typeface.createFromFile("/system/fonts/OneUISans-VF.ttf")
            }
        } catch (e: Exception) {
            textPaint.typeface = android.graphics.Typeface.DEFAULT
        }
    }

    fun setPendingCount(count: Int) { pendingCount = count; invalidate() }
    fun setTotalCount(count: Int) { totalCount = count; invalidate() }
    fun setBackgroundAlpha(alpha: Int) { bgAlpha = alpha; invalidate() }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val w = width.toFloat()
        val h = height.toFloat()
        val cx = w / 2f
        val cy = h / 2f

        bgPaint.alpha = bgAlpha

        val margin = 26f
        val radius = Math.min(w, h) / 2f - margin
        val verticalOffset = 2.5f  // negativo sobe, positivo desce
        oval.set(cx - radius, cy - radius + verticalOffset, cx + radius, cy + radius + verticalOffset)

        //canvas.drawArc(oval, 180f, 180f, false, trackPaint)
        val sweepAngle = 222f  // menos graus = pontas mais afastadas
        val startAngle = 90f + (360f - sweepAngle) / 2f  // diminui o 80f para subir as pontas

        canvas.drawArc(oval, startAngle, sweepAngle, false, trackPaint)

        if (totalCount > 0) {
            val completed = totalCount - pendingCount
            val fraction = completed.toFloat() / totalCount.toFloat()

            /*if (pendingCount == 0) {
                progressPaint.color = fgColor
            } else {
                progressPaint.color = adjustAlpha(fgColor, 0.5f)
            }*/

            //canvas.drawArc(oval, 180f, progressSweep, false, progressPaint)
            canvas.drawArc(oval, startAngle, sweepAngle * fraction, false, progressPaint)
        }

        textPaint.textSize = Math.min(w, h) * 0.2f
        val completed = totalCount - pendingCount
        val textY = cy + radius * 1f
        canvas.drawText("$completed/$totalCount", cx, textY, textPaint)
    }
}
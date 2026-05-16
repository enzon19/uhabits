package org.isoron.uhabits.widgets.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.View
import androidx.core.graphics.toColorInt

class DailySummaryWidgetView(context: Context) : View(context) {

    private var pendingCount: Int = 0
    private var totalCount: Int = 0

    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val trackPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        bgPaint.color = Color.BLACK
        bgPaint.style = Paint.Style.FILL

        trackPaint.color = "#33FFFFFF".toColorInt()
        trackPaint.style = Paint.Style.STROKE
        trackPaint.strokeWidth = 16f
        trackPaint.strokeCap = Paint.Cap.ROUND

        progressPaint.style = Paint.Style.STROKE
        progressPaint.strokeWidth = 16f
        progressPaint.strokeCap = Paint.Cap.ROUND

        textPaint.color = Color.WHITE
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.isFakeBoldText = true
    }

    fun setPendingCount(count: Int) { pendingCount = count; invalidate() }
    fun setTotalCount(count: Int) { totalCount = count; invalidate() }

    private val oval = RectF()
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val w = width.toFloat()
        val h = height.toFloat()
        val cx = w / 2f
        val cy = h / 2f

        // Margem do arco
        val margin = 10f
        val radius = Math.min(w, h) / 2f - margin
        oval.set(cx - radius, cy - radius, cx + radius, cy + radius)

        // Arco começa na esquerda (180°) e vai 180° no sentido horário
        // formando um semicírculo na parte de baixo
        val startAngle = 180f
        val sweepAngle = 180f

        // Trilha (fundo do arco)
        canvas.drawArc(oval, startAngle, sweepAngle, false, trackPaint)

        // Progresso
        if (totalCount > 0) {
            val completed = totalCount - pendingCount
            val fraction = completed.toFloat() / totalCount.toFloat()
            val progressSweep = sweepAngle * fraction

            progressPaint.color = Color.WHITE

            canvas.drawArc(oval, startAngle, progressSweep, false, progressPaint)
        }

        // Texto "X/Y" centralizado
        textPaint.textSize = Math.min(w, h) * 0.2f
        val fm = textPaint.fontMetrics
        val textY = cy - (fm.ascent + fm.descent) / 2 - textPaint.textSize * 0.25f
        canvas.drawText("${ totalCount - pendingCount }/$totalCount", cx, textY, textPaint)
    }
}
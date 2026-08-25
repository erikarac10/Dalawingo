package com.example.dalawingo.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class SnakePathView extends View {

    private final Paint paint = new Paint();
    private int unitCount = 0;
    private float maxShiftPx = -1f; // <-- set from fragment

    public SnakePathView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint.setColor(0x66FF6F61);
        paint.setStrokeWidth(8f);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }

    public void setUnitCount(int count) {
        this.unitCount = count;
        invalidate();
    }

    public void setMaxShiftPx(float px) {
        this.maxShiftPx = px;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (unitCount < 2) return;

        float width = getWidth();
        float height = getHeight();

        float centerX = width / 2f;

        // Use the SAME shift as bubbles (fallback to width/2 if not set yet)
        float maxShift = (maxShiftPx > 0f) ? maxShiftPx : (width / 2f);

        Path path = new Path();
        int steps = 400;

        for (int i = 0; i <= steps; i++) {
            float progress = i / (float) steps;
            float y = progress * height;

            float sine = (float) Math.sin(progress * Math.PI * 1.5f - Math.PI / 2f);
            float x = centerX + sine * maxShift;

            if (i == 0) path.moveTo(x, y);
            else path.lineTo(x, y);
        }

        canvas.drawPath(path, paint);
    }
}
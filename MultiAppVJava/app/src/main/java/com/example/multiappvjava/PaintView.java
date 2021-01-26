package com.example.multiappvjava;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

public class PaintView extends View {

    public LayoutParams params;
    private static Path path = new Path();
    private static Paint brush = new Paint();
    Bitmap canvasBmp;
    Canvas drawCanvas;

    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);

        brush.setAntiAlias(true);
        brush.setColor(Color.GREEN);
        brush.setStyle(Paint.Style.STROKE);
        brush.setStrokeJoin(Paint.Join.ROUND);
        brush.setStrokeWidth(8f);

        params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

    }

    public static void setWidth(float v) {
        brush.setStrokeWidth(v);
    }

    public static void setColor(int color){
        brush.setColor(color);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBmp);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float pointX = event.getX();
        float pointY = event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                path.moveTo(pointX, pointY);
                return true;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(pointX, pointY);
                break;

            case MotionEvent.ACTION_UP:
                drawCanvas.drawPath(path, brush);
                path.reset();

            default:
                return false;
        }

        invalidate();
        return false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(canvasBmp, 0, 0, brush);
        canvas.drawPath(path, brush);
    }

}

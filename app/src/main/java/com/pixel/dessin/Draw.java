package com.pixel.dessin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import static android.graphics.Bitmap.CompressFormat.*;
import static android.graphics.Bitmap.Config.ARGB_8888;

/**
 * Created by pixel on 19/01/2018.
 */

public class Draw extends View {

    public int width;
    public int height;
    private Bitmap bitmap;
    private Canvas canvas;
    private float mx, my;
    private static final float TOLERANCE = 5;
    private Context context;
    private ArrayList<Path> paths;
    private ArrayList<Paint> paints;

    public Paint paint;
    public Path path;

    public Draw(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        paths = new ArrayList<Path>();
        paints = new ArrayList<Paint>();
        reset();
    }

    public void reset() {
        path = new Path();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(5f);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        invalidate();
    }

    private void onStartTouch(float x, float y) {
        path.moveTo(x, y);
        mx = x;
        my = y;
    }

    private void moveTouch(float x, float y) {
        float dx = Math.abs(x - mx);
        float dy = Math.abs(y - my);
        if (dx > TOLERANCE || dy > TOLERANCE) {
            path.quadTo(mx, my, (x + mx) / 2, (y + my) / 2);
            mx = x;
            my = y;
        }
    }

    private void upTouch() {
        path.lineTo(mx, my);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Path tmpPath;
        Paint tmpPaint;
        for (int i = 0; i < paths.size(); i++) {
            tmpPath = paths.get(i);
            tmpPaint = paints.get(i);
            canvas.drawPath(tmpPath, tmpPaint);
        }
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        invalidate();
        canvas.drawPath(path, paint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bitmap = Bitmap.createBitmap(w, h, ARGB_8888);
        canvas = new Canvas(bitmap);
    }

    public Bitmap onSave() {
        //createBitmap(int width, int height, Bitmap.Config config)
        //    Returns a mutable bitmap with the specified width and height.
        //bitmap = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.RGB_565);
       // this.draw(new Canvas(Bitmap.createBitmap(PNG, this.getWidth(), bitmap, ARGB_8888)));
        this.draw(new Canvas(bitmap));
        return bitmap;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                paths.add(path);
                paints.add(paint);
                path.moveTo(eventX, eventY);
                return true;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(eventX, eventY);
                break;
            case MotionEvent.ACTION_UP: // nothing to do
                path = new Path();
                paint = new Paint();
                break;
            default:
                return false;
        }
        // Schedules a repaint.
        invalidate();
        return true;
    }

    public void undo() {
        //clear();
        Path tmpPath;
        Paint tmpPaint;
        for (int i = 0; i < paths.size(); i++) {
            tmpPath = paths.get(i);
            tmpPaint = paints.get(i);
            canvas.drawPath(tmpPath, tmpPaint);
        }
    }

    public void clear() {
        path = new Path();
        paint = new Paint();
        paths = new ArrayList<Path>();
        paints = new ArrayList<Paint>();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(5f);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        invalidate();
    }
}

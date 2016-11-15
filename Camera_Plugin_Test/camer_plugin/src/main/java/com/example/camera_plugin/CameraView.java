package com.example.camera_plugin;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by NMSL-YingYi on 2016/11/15.
 */

public class CameraView extends View
{

    public CameraView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }
    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        int x=80;
        int y=80;
        int radius=40;
        Paint paint=new Paint();
        // Use Color.parseColor to define HTML colors
        paint.setColor(Color.parseColor("#CD5C5C"));
        canvas.drawCircle(x,x, radius, paint);
    }

}
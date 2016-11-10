package com.example.camera_plugin;

/**
 * Created by NMSL-YingYi on 2016/11/10.
 */
import android.app.Activity;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class Utils {
    public static FrameLayout AddFrameLayout(final Activity activity, final int x, final int y) {
        FrameLayout frameLayout = new FrameLayout(activity);

        frameLayout.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT));

        activity.addContentView(frameLayout, new ViewGroup.LayoutParams
                (ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

        MoveTo(frameLayout,x,y);
        return frameLayout;
    }

    public static void AddImageView(Activity activity, SurfaceView surfaceView, int x, int y) {
        FrameLayout frameLayout = AddFrameLayout(activity,x,y);
        frameLayout.addView(surfaceView);
    }

    public static void AddSurfaceView(final Activity activity, final SurfaceView surfaceView, final int x, final int y) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AddImageView(activity, surfaceView, x, y);
            }
        });
    }
    public static void MoveTo(View view, int newX, int newY){
        view.setX(newX);
        view.setY(newY);
    }
}

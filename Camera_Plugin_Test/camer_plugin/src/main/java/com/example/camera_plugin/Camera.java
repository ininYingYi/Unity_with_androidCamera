package com.example.camera_plugin;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.IOException;

import com.unity3d.player.UnityPlayerActivity;

/**
 * Created by NMSL-YingYi on 2016/11/7.
 */

public class Camera extends UnityPlayerActivity {
    private static Camera self = null;
    private static String TAG = "CameraPlugin";
    private MediaRecorder mediarecorder;// 錄製視頻的類
    private SurfaceView surfaceview;// 顯示視頻的控制項
    // 用來顯示視頻的一個介面，不用還不行，也就是說用mediarecorder錄製視頻還得給個介面看
// 想偷偷錄視頻的同學可以考慮別的辦法。。嗯需要實現這個介面的Callback介面
    private SurfaceHolder surfaceHolder;
    private android.hardware.Camera camera;

    UnityPlayerActivity unityPlayerActivity;
    public static Camera instance() {
        if (self == null) self = new Camera();
        return self;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        self = this;
//setContentView(R.layout.main);
        Toast.makeText(this,
                "onCreate.", // and re-run this program
                Toast.LENGTH_LONG).show();
    }

    public Camera() {

    }

    public void startRecord() {
        surfaceview = new SurfaceView(this);
        Utils.AddSurfaceView(this, surfaceview, 0, 0);
        surfaceHolder = surfaceview.getHolder();// 取得holder
        surfaceHolder.addCallback(callback); // holder加入回檔介面
// setType必須設置，要不出錯.
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        Toast.makeText(self,
                "create surface.", // and re-run this program
                Toast.LENGTH_LONG).show();

        Log.d(TAG, "startRecord");
        mediarecorder = new MediaRecorder();// 創建mediarecorder物件
        // 設置錄製視頻源為Camera(相機)
        mediarecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        // 設置錄製完成後視頻的封裝格式THREE_GPP為3gp.MPEG_4為mp4
        mediarecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        // 設置錄製的視頻編碼h263 h264
        mediarecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        // 設置視頻錄製的解析度。必須放在設置編碼和格式的後面，否則報錯
        mediarecorder.setVideoSize(176, 144);
        // 設置錄製的視頻幀率。必須放在設置編碼和格式的後面，否則報錯
        mediarecorder.setVideoFrameRate(20);
        mediarecorder.setPreviewDisplay(surfaceHolder.getSurface());
        // 設置視頻檔輸出的路徑
        mediarecorder.setOutputFile("/sdcard/love.3gp");
        try {
            // 準備錄製
            mediarecorder.prepare();
            // 開始錄製
            mediarecorder.start();
        } catch (IllegalStateException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void stopRecord() {
        Log.d(TAG, "stopRecord");
        // 停止錄製
        mediarecorder.stop();
        // 釋放資源
        mediarecorder.release();
        mediarecorder = null;
    }

    private SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            // 將holder，這個holder為開始在oncreat裡面取得的holder，將它賦給surfaceHolder
            surfaceHolder = holder;
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            // 將holder，這個holder為開始在oncreat裡面取得的holder，將它賦給surfaceHolder
            surfaceHolder = holder;
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // surfaceDestroyed的時候同時物件設置為null
            surfaceview = null;
            surfaceHolder = null;
            mediarecorder = null;
        }
    };

    public void onBackPressed()
    {
        // instead of calling UnityPlayerActivity.onBackPressed() we just ignore the back button event
        // super.onBackPressed();
    }

}

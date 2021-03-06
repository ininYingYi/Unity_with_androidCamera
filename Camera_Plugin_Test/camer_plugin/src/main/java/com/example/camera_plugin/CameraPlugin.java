package com.example.camera_plugin;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaCodec;
import android.media.MediaRecorder;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by NMSL-YingYi on 2016/11/7.
 */

public class CameraPlugin extends UnityPlayerActivity {
    private static final String TAG = "CameraPlugin";
    private MediaRecorder mMediaRecorder;// 錄製視頻的類
    private TextureView mPreview;// 顯示視頻的控制項
    // 用來顯示視頻的一個介面，我靠不用還不行，也就是說用mediarecorder錄製視頻還得給個介面看
// 想偷偷錄視頻的同學可以考慮別的辦法。。嗯需要實現這個介面的Callback介面
    private SurfaceHolder surfaceHolder;
    private Camera mCamera;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }
    private Timer timer;
    private int mGlSurfaceTexture;
    private void init() {
        //surface = MediaCodec.createPersistentInputSurface();
        /*timer = new Timer();
        timer.schedule(timerTask, 0, 1);*/
        //sf = new SurfaceTexture(0);
        //sf.setOnFrameAvailableListener(onFrameAvailableListener);
        //surface = new Surface (sf);
        //mPreview = new TextureView(this);
        //mPreview.setSurfaceTexture(sf);
        //mPreview.setSurfaceTextureListener(surfaceTextureListener);
        /*this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                addContentView(mPreview, new FrameLayout.LayoutParams(400, 400));
            }
        });*/
        Toast.makeText(CameraPlugin.this, "init done", Toast.LENGTH_LONG).show();
        mGlSurfaceTexture = this.createTexture();
        sf = new SurfaceTexture(mGlSurfaceTexture);
        surface = new Surface(sf);
        sf.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
            @Override
            public void onFrameAvailable(SurfaceTexture surfaceTexture) {

                Log.d(TAG, "onFrameAvailable");
            }
        });
    }

    public int getTextureID() {
        return mGlSurfaceTexture;
    }

    private Surface persistentSurface;
    public void setGLTextureID(int texName) {
        final int tex = texName;
        persistentSurface = MediaCodec.createPersistentInputSurface();
        //persistentSurface.writeToParcel();
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sf = new SurfaceTexture(tex);
                surface = new Surface(sf);
                sf.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
                    @Override
                    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                        sf.updateTexImage();
                        surface.writeToParcel(surfaceData, 0);
                        surfaceData.setDataPosition(0);
                        persistentSurface.readFromParcel(surfaceData);
                        Log.d(TAG, "onFrameAvailable");
                    }
                });
            }
        });



        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //mySurface = new MySurface(CameraPlugin.this);
                //addContentView(mySurface, new FrameLayout.LayoutParams(400, 400));
            }
        });

    }
    private Parcel surfaceData = Parcel.obtain();

    private SurfaceTexture sf;
    public void startRecord() {
        new MediaPrepareTask().execute(null, null, null);
    }
    private int videoWidth, videoHeight;
    private Surface surface;
    public boolean prepareVideoRecorder() {
        mCamera = CameraHelper.getDefaultCameraInstance();

        // We need to make sure that our preview and recording video size are supported by the
        // camera. Query camera to find all the sizes and choose the optimal size given the
        // dimensions of our preview surface.
        Camera.Parameters parameters = mCamera.getParameters();
        List<Camera.Size> mSupportedPreviewSizes = parameters.getSupportedPreviewSizes();
        List<Camera.Size> mSupportedVideoSizes = parameters.getSupportedVideoSizes();
        Camera.Size optimalSize = CameraHelper.getOptimalVideoSize(mSupportedVideoSizes,  mSupportedPreviewSizes, videoWidth, videoHeight);

        // Use the same size for recording profile.
        CamcorderProfile profile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
        profile.videoFrameWidth = videoWidth;
        profile.videoFrameHeight = videoHeight;

        // likewise for the camera object itself.
        //parameters.setPreviewSize(profile.videoFrameWidth, profile.videoFrameHeight);
        mCamera.setParameters(parameters);
        try {
            // Requires API level 11+, For backward compatibility use {@link setPreviewDisplay}
            // with {@link SurfaceView}
            mCamera.setPreviewTexture(sf);
        } catch (IOException e) {
            Log.e(TAG, "Surface texture is unavailable or unsuitable" + e.getMessage());
            return false;
        }

        // BEGIN_INCLUDE (configure_media_recorder)
        mMediaRecorder = new MediaRecorder();

        // Step 1: Unlock and set camera to MediaRecorder
        mCamera.unlock();
        mMediaRecorder.setCamera(mCamera);
        //mMediaRecorder.setInputSurface(persistentSurface);

        // Step 2: Set sources
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT );
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        // Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
        mMediaRecorder.setProfile(profile);

        // Step 4: Set output file
        mMediaRecorder.setOutputFile("/sdcard/love.mp4");

        // Step 5: Prepare configured MediaRecorder
        try {
            mMediaRecorder.prepare();
        } catch (IllegalStateException e) {
            Log.d(TAG, "IllegalStateException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            Log.d(TAG, "IOException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        }
        return true;
    }

    public void stopRecord() {

        // stop recording and release camera
        try {
            mMediaRecorder.stop();  // stop the recording
        } catch (RuntimeException e) {
            // RuntimeException is thrown when stop() is called immediately after start().
            // In this case the output file is not properly constructed ans should be deleted.
            Log.d(TAG, e.getMessage());
            Log.d(TAG, "RuntimeException: stop() is called immediately after start()");
            //noinspection ResultOfMethodCallIgnored
            //mOutputFile.delete();
        }
        releaseMediaRecorder(); // release the MediaRecorder object
        mCamera.lock();         // take camera access back from MediaRecorder

        // inform the user that recording has stopped
        releaseCamera();
    }

    private void releaseMediaRecorder(){
        if (mMediaRecorder != null) {
            // clear recorder configuration
            mMediaRecorder.reset();
            // release the recorder object
            mMediaRecorder.release();
            mMediaRecorder = null;
            // Lock camera for later use i.e taking it back from MediaRecorder.
            // MediaRecorder doesn't need it anymore and we will release it if the activity pauses.
            if (mCamera != null) {
                mCamera.lock();
            }
        }
    }

    private void releaseCamera() {
        if (mCamera != null){
            // release the camera for other applications
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // if we are using MediaRecorder, release it first
        releaseMediaRecorder();
        // release the camera immediately on pause event
        releaseCamera();
    }

    private int createTexture(){
        int[] textures = new int[1];

        // Generate the texture to where android view will be rendered
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glGenTextures(1, textures, 0);
        //checkGlError("Texture generate");

        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textures[0]);
        //checkGlError("Texture bind");

        GLES20.glTexParameterf(GLES11Ext.GL_BGRA, GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_LINEAR);
        GLES20.glTexParameterf(GLES11Ext.GL_BGRA, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameteri(GLES11Ext.GL_BGRA, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES11Ext.GL_BGRA, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);

        return textures[0];
    }

    public int getWidth() {
        return videoWidth;
    }

    public int getHeight() {
        return videoHeight;
    }

    public void setScreenSize(int width, int height) {
        this.videoWidth = width;
        this.videoHeight = height;
        sf.setDefaultBufferSize(width, height);
    }

    class MediaPrepareTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            // initialize video camera
            if (prepareVideoRecorder()) {
                // Camera is available and unlocked, MediaRecorder is prepared,
                // now you can start recording
                mMediaRecorder.start();
                Log.d(TAG, "mMediaRecorder.start");
            } else {
                // prepare didn't work, release the camera
                releaseMediaRecorder();
                return false;
            }
            return true;
        }
    }
    private ArrayList<byte[]> frameDatas = new ArrayList();
    public void receiveFrameData(byte[] data) {
        frameDatas.add(data);
        /*Canvas canvas = surface.lockHardwareCanvas();
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, 0, 0, new Paint());
        }
        surface.unlockCanvasAndPost(canvas);*/
    }

    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            byte[] data;
            Iterator<byte[]> iterator = frameDatas.iterator();
            if (iterator.hasNext()) {
                data = iterator.next();
                Canvas canvas = surface.lockHardwareCanvas();

                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                if (bitmap != null) {
                    canvas.drawBitmap(bitmap, 0, 0, new Paint());
                    FileOutputStream out = null;
                    try {
                        out = new FileOutputStream("/sdcard/love.png");
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                        // PNG is a lossless format, the compression factor (100) is ignored
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (out != null) {
                                out.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                surface.unlockCanvasAndPost(canvas);

                iterator.remove();
            }
        }
    };
}

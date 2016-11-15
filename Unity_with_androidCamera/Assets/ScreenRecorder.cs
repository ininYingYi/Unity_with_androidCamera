using System;
using System.Runtime.InteropServices;
using UnityEngine;
using System.Collections;
using System.Diagnostics;
using System.IO;
using System.Runtime.Serialization.Formatters.Binary;


public class ScreenRecorder {

	private AndroidJavaObject unityActivity = null;
	private AndroidJavaObject captureObject = null;
	private int width;
	private int height;
	// Use this for initialization
	public ScreenRecorder () {
		try{

			using (AndroidJavaClass unityPlayerActivityClass = new AndroidJavaClass("com.unity3d.player.UnityPlayer")) {
				unityActivity = unityPlayerActivityClass.GetStatic<AndroidJavaObject>("currentActivity");
			}
				
			/*AndroidJavaClass captureClass = new AndroidJavaClass ("com.example.camera_plugin.CameraPlugin");
//			AndroidJavaClass captureClass = new AndroidJavaClass ("com.example.ffmpegcodec.FFMpegCodec");
			if (captureClass != null) {
				captureObject = captureClass.CallStatic<AndroidJavaObject>("instance");
				captureObject.CallStatic("addView");
			}*/

		}catch(Exception ex){
			UnityEngine.Debug.Log(ex);
			//GameObject.Find ("debug").GetComponent<TextMesh>().text = ex.ToString();
		}

	}

	public void startRecord() {
		UnityEngine.Debug.Log("startRecord");
		unityActivity.Call("startRecord");
		//captureObject.Call ("startRecord", Settings.TEMP_IMAGE_PATH, imagePrefix);
	}

	public void stopRecord() {
		UnityEngine.Debug.Log("stopRecord");
		unityActivity.Call ("stopRecord");
		//captureObject.Call ("startRecord", Settings.TEMP_IMAGE_PATH, imagePrefix);
	}

	public byte[] getSnap() {
		return unityActivity.Call <byte[]>("snap");
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void setScreenSize(int width, int height) {
		this.width = width;
		this.height = height;
		unityActivity.Call ("setScreenSize", width, height);
	}

	public void requestUpdate() {
		unityActivity.Call ("requestUpdate");
	}

	public void sendFrame(byte[] data) {
		unityActivity.Call ("receiveFrameData", data);
	}

	public void setGLTextureID(System.IntPtr id) {
		int texName = id.ToInt32();
		unityActivity.Call ("setGLTextureID", texName);
	}

	public void invalidate() {
		unityActivity.Call ("invalidate");
	}
}
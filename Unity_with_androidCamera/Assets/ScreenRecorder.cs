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

	// Use this for initialization
	public ScreenRecorder () {
		try{

			using (AndroidJavaClass unityPlayerActivityClass = new AndroidJavaClass("com.unity3d.player.UnityPlayer")) {
				unityActivity = unityPlayerActivityClass.GetStatic<AndroidJavaObject>("currentActivity");
			}

			AndroidJavaClass captureClass = new AndroidJavaClass ("com.example.camera_plugin.Camera");
//			AndroidJavaClass captureClass = new AndroidJavaClass ("com.example.ffmpegcodec.FFMpegCodec");
			if (captureClass != null) {
				captureObject = captureClass.CallStatic<AndroidJavaObject>("instance");
				//captureObject.Call("setContext", unityActivity);
			}

		}catch(Exception ex){
			UnityEngine.Debug.Log(ex);
			//GameObject.Find ("debug").GetComponent<TextMesh>().text = ex.ToString();
		}
	}

	public void startRecord() {
		UnityEngine.Debug.Log("startRecord");
		captureObject.Call ("startRecord");
		//captureObject.Call ("startRecord", Settings.TEMP_IMAGE_PATH, imagePrefix);
	}

	public void stopRecord() {
		UnityEngine.Debug.Log("stopRecord");
		captureObject.Call ("stopRecord");
		//captureObject.Call ("startRecord", Settings.TEMP_IMAGE_PATH, imagePrefix);
	}

}
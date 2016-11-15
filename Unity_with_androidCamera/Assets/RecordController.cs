using UnityEngine;
using System.Collections;
using System.IO;
using System.Threading;
using System;
using System.Text;


public class RecordController : MonoBehaviour {
	private ScreenRecorder mScreenRecorder;
	private WebCamTexture mCamera = null;
	private GameObject plane;
	private bool startReocrd = false;
	private long count = 0;
	private string imagePrefix = "image-";
	private int width, height;
	private Thread recordThread;
	private Texture2D image;
	private byte[] imageData;
	private static object LockingVar = new object();
	private int textureID;
	// Use this for initialization
	void Start () {
		//GameObject.Find ("debug").GetComponent<TextMesh>().text = "HI";

		plane = GameObject.FindWithTag ("CameraView");
		//mCamera = new WebCamTexture ();

		//plane.GetComponent<Renderer>().material.mainTexture = mCamera;
		//mCamera.Play ();
		Settings.checkPath ();
		this.width = 800;
		this.height = 600;

		//image = new Texture2D(width, height);
		//System.IntPtr texturePtr = mCamera.GetNativeTextureID ();
		mScreenRecorder = new ScreenRecorder ();
		//mScreenRecorder.setGLTextureID (texturePtr);
		mScreenRecorder.setScreenSize (width, height);
		textureID = mScreenRecorder.getTextureID ();
		image = Texture2D.CreateExternalTexture (width, height, TextureFormat.BGRA32, false, true, (System.IntPtr) textureID);
		plane.GetComponent<Renderer>().material.mainTexture = image;
	
	}
	
	// Update is called once per frame
	void Update () {
		
	}

	void FixedUpdate() {
		UnityEngine.Debug.Log(textureID);
		image.UpdateExternalTexture ((System.IntPtr)textureID);
		plane.GetComponent<Renderer>().material.mainTexture = image;
		if (startReocrd) {
			//mScreenRecorder.invalidate ();
		}
		/*image.SetPixels (mCamera.GetPixels());
		image.Apply ();
		mScreenRecorder.sendFrame (image.GetRawTextureData ());*/

		//mScreenRecorder.requestUpdate ();
		/*if (false) {
			byte[] snap = mScreenRecorder.getSnap ();
			Texture2D image = new Texture2D(width, height);
			image.LoadImage (snap);
			image.Apply ();
			//System.IO.File.WriteAllBytes (Settings.TEMP_IMAGE_PATH + imagePrefix + count + ".png", snap);
			count++;
			plane.GetComponent<Renderer> ().material.mainTexture = image;
			Texture2D glTexture = Texture2D.CreateExternalTexture (500, 500, TextureFormat.ARGB32, false, false, (System.IntPtr) mScreenRecorder.getTexturePtr ());
			plane.GetComponent<Renderer> ().material.mainTexture = glTexture;
			System.IO.File.WriteAllBytes (Settings.TEMP_IMAGE_PATH + imagePrefix + count + ".png", glTexture.EncodeToPNG() );
		}*/
	}


	void OnGUI() {
		if (GUI.Button (new Rect (0, 0, 400, 200), "record") && !startReocrd) {
			startReocrd = true;
			mScreenRecorder.startRecord();
		}
		if (GUI.Button (new Rect (500, 0, 400, 200), "stop") && startReocrd) {
			//mScreenRecorder.getSnap ();
			startReocrd = false;
			new WaitForSecondsRealtime (3);
			mScreenRecorder.stopRecord();
		}
	}

}

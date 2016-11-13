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

	private byte[] imageData;
	// Use this for initialization
	void Start () {
		//GameObject.Find ("debug").GetComponent<TextMesh>().text = "HI";
		mScreenRecorder = new ScreenRecorder ();
		plane = GameObject.FindWithTag ("CameraView");
		/*mCamera = new WebCamTexture ();

		plane.GetComponent<Renderer>().material.mainTexture = mCamera;
		mCamera.Play ();*/
		Settings.checkPath ();
		this.width = mScreenRecorder.getWidth();
		this.height = mScreenRecorder.getHeight();
	}
	
	// Update is called once per frame
	void Update () {
		
	}

	void FixedUpdate() {
		//mScreenRecorder.requestUpdate ();
		if (false) {
			byte[] snap = mScreenRecorder.getSnap ();
			Texture2D image = new Texture2D(width, height);
			image.LoadImage (snap);
			image.Apply ();
			//System.IO.File.WriteAllBytes (Settings.TEMP_IMAGE_PATH + imagePrefix + count + ".png", snap);
			count++;
			plane.GetComponent<Renderer> ().material.mainTexture = image;
			/*Texture2D glTexture = Texture2D.CreateExternalTexture (500, 500, TextureFormat.ARGB32, false, false, (System.IntPtr) mScreenRecorder.getTexturePtr ());
			plane.GetComponent<Renderer> ().material.mainTexture = glTexture;
			System.IO.File.WriteAllBytes (Settings.TEMP_IMAGE_PATH + imagePrefix + count + ".png", glTexture.EncodeToPNG() );*/
		}
	}


	void OnGUI() {
		if (GUI.Button (new Rect (0, 0, 400, 200), "record") && !startReocrd) {
			startReocrd = true;
			mScreenRecorder.startRecord();
		}
		if (GUI.Button (new Rect (500, 0, 400, 200), "stop") && startReocrd) {
			//mScreenRecorder.getSnap ();
			startReocrd = false;
			new WaitForSeconds(5);
			mScreenRecorder.stopRecord();
		}
	}

	public void UpdatePreview(string bytes) {
		UnityEngine.Debug.Log("UpdatePreview");
		byte[] byteData = Convert.FromBase64String(bytes);
		Texture2D image = new Texture2D(width, height);
		image.LoadImage (byteData);
		image.Apply ();
		plane.GetComponent<Renderer> ().material.mainTexture = image;
	}
	/*private void Call()
	{
		System.IO.File.WriteAllBytes (Settings.TEMP_IMAGE_PATH + imagePrefix + count + ".png", imageData);
		count++;
	}*/
}

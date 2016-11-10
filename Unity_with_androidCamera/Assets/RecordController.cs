using UnityEngine;
using System.Collections;
using System.IO;
using System.Threading;

public class RecordController : MonoBehaviour {
	private ScreenRecorder mScreenRecorder;
	private WebCamTexture mCamera = null;
	private GameObject plane;
	private bool saveImage = false;
	private long count = 0;
	private string imagePrefix = "image-";
	private int width, height;
	Texture2D image;
	private byte[] imageData;
	// Use this for initialization
	void Start () {
		//GameObject.Find ("debug").GetComponent<TextMesh>().text = "HI";
		mScreenRecorder = new ScreenRecorder ();
		/*mCamera = new WebCamTexture ();
		plane = GameObject.FindWithTag ("CameraView");
		plane.GetComponent<Renderer>().material.mainTexture = mCamera;
		mCamera.Play ();
		Settings.checkPath ();
		width = mCamera.width;
		height = mCamera.height;*/
	}
	
	// Update is called once per frame
	void Update () {
		
	}

	void FixedUpdate() {
		if (saveImage) {
			/*Thread thread = new Thread(Call);
			image = new Texture2D(width, height);
			image.SetPixels (mCamera.GetPixels ());
			image.Apply ();
			imageData = image.EncodeToPNG ();
			thread.Start ();*/
		}
	}


	void OnGUI() {
		if (GUI.Button (new Rect (0, 0, 400, 200), "record")) {
			mScreenRecorder.startRecord();
		}
		if (GUI.Button (new Rect (500, 0, 400, 200), "stop")) {
			saveImage = false;
			mScreenRecorder.stopRecord();
		}
	}

	/*private void Call()
	{
		System.IO.File.WriteAllBytes (Settings.TEMP_IMAGE_PATH + imagePrefix + count + ".png", imageData);
		count++;
	}*/
}

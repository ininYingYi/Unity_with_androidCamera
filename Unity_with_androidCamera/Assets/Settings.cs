using UnityEngine;
using System.Collections;
using System.IO;

public class Settings {
	public static string TEMP_IMAGE_PATH = Application.persistentDataPath + "/tmp/image/";
	public static void checkPath() {
		try {
			if (!Directory.Exists (Settings.TEMP_IMAGE_PATH)) {
				Directory.CreateDirectory(Settings.TEMP_IMAGE_PATH);
			}
		}
		catch (IOException e) {
			UnityEngine.Debug.Log (e);
		}
	}
}

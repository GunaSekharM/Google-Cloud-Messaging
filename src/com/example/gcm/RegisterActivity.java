package com.example.gcm;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class RegisterActivity extends Activity{
	GoogleCloudMessaging gcm;
	Context context;
	String regId;
	public static final String REG_ID = "regId";
	private static final String APP_VERSION = "appVersion";
	private static final String MyPREFERENCES = "MyPreferences";

	static final String TAG = "Register Activity";
	
	public RegisterActivity(Context context)
	{
		this.context = context;
	}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = getApplicationContext();
	}
	public void getRegID(View view) {
	    if (TextUtils.isEmpty(regId)) {
			regId = registerGCM();
			Log.d("RegisterActivity", "GCM RegId: " + regId);
		} else {
			Toast.makeText(context, "Already Registered with GCM Server!",Toast.LENGTH_LONG).show();
			}
	}
	    
//	    public void getRegID() {
//	    	//RegisterId regid = new RegisterId(getApplicationContext());
//	    	//System.out.println("regid : "+regid);
//	    	if (TextUtils.isEmpty(regId)) {
//				regId = registerGCM();
//				Log.d("RegisterActivity", "GCM RegId: " + regId);
//			} else {
//				Toast.makeText(context,
//						"Already Registered with GCM Server!",
//						Toast.LENGTH_LONG).show();
//			}
//	            //return regId;
//			}

	public String registerGCM() {

		gcm = GoogleCloudMessaging.getInstance(context);
		regId = getRegistrationId(context);

		if (TextUtils.isEmpty(regId)) {

			registerInBackground();

			Log.d("RegisterActivity",
					"registerGCM - successfully registered with GCM server - regId: "
							+ regId);
		} else {
			Toast.makeText(context, "RegId already available. RegId: " + regId, Toast.LENGTH_LONG).show();
			System.out.println("GCM Registration id: "+regId);
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setMessage(regId);
			builder.setTitle("RegId");
			builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		            // continue with delete
		        }
		     });
			builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		            // do nothing
		        }
		     });  
			builder.show();
		}
		return regId;
	}

	private String getRegistrationId(Context context) {
		final SharedPreferences prefs = getSharedPreferences(MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
		String registrationId = prefs.getString(REG_ID, "");
		if (registrationId.isEmpty()) {
			Log.i(TAG, "Registration not found.");
			return "";
		}
		int registeredVersion = prefs.getInt(APP_VERSION, Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			Log.i(TAG, "App version changed.");
			return "";
	}
		return "";
	}

	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			Log.d("MainActivity", "I never expected this! Going down, going down!" + e);
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	private void registerInBackground() {
		new AsyncTask() {
			
			@Override
			protected String doInBackground(Object... params) {
				String msg = "";
				String url = "";
				HttpResponse response = null;
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(context);
					}
					regId = gcm.register(Config.GOOGLE_PROJECT_ID);
					Log.d("RegisterActivity", "registerInBackground - regId: "
							+ regId);
					msg = "Device registered, registration ID=" + regId;
					HttpClient httpclient = new DefaultHttpClient();
					url = "http://sumerudevserver.cloudapp.net/ENSourcePrototype/DeviceAPI/AddDeviceRegistrations?deviceRegistrationID="+regId;
					HttpPost httppost = new HttpPost(url);
					response = httpclient.execute(httppost);
					Log.d("RegisterActivity", "response: " + response);
					//storeRegistrationId(context, regId);
					
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
					Log.d("RegisterActivity", "Error: " + msg);
				}
				Log.d("RegisterActivity", "url in gcm: " + url);
				return msg;
			}

			@SuppressWarnings("unused")
			protected void onPostExecute(String msg) {
				Toast.makeText(context,
						"Registered with GCM Server." + msg, Toast.LENGTH_LONG)
						.show();
			}

			
		}.execute(null, null, null);
	}

	private void storeRegistrationId(Context context, String regId) 
	{
		SharedPreferences prefs = getSharedPreferences(MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(REG_ID, regId);
		editor.commit();
	}
}


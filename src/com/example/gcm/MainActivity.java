package com.example.gcm;

import java.io.IOException;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {//Button btnGCMRegister;
//Button btnAppShare;
//GoogleCloudMessaging gcm;
//Context context;
//String regId;

String regId;
AsyncTask shareRegidTask;

public static final String REG_ID = "regId";
private static final String APP_VERSION = "appVersion";

static final String TAG = "Main Activity";

@Override
public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);
	

	regId = getIntent().getStringExtra("regId");
	Log.d("MainActivity", "regId: " + regId);

	final Context context = this;
	shareRegidTask = new AsyncTask() {

		@Override
		protected String doInBackground(Object... params) {
			// TODO Auto-generated method stub
			//String result = appUtil.shareRegIdWithAppServer(context, regId);
			//System.out.println("result: "+result);
			return "";
		}
		
		protected void onPostExecute(String result) {
			shareRegidTask = null;
			Toast.makeText(getApplicationContext(), result,
					Toast.LENGTH_LONG).show();
		}
	};
	shareRegidTask.execute(null, null, null);
}

}
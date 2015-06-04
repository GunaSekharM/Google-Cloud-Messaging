package com.example.gcm;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals("com.google.android.c2dm.intent.RECEIVE"))
		{
		  System.out.println("coming here***************");
		  ComponentName comp = new ComponentName(context.getPackageName(), GCMNotificationIntentService.class.getName());
		  startWakefulService(context, (intent.setComponent(comp)));
		  setResultCode(Activity.RESULT_OK);
		}
	}
}

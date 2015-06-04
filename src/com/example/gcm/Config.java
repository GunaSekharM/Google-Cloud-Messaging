package com.example.gcm;



public class Config {

	// used to share GCM regId with application server - using php app server
		static final String APP_SERVER_URL = "https://android.googleapis.com/gcm/send";

		// GCM server using java
		// static final String APP_SERVER_URL =
		// "http://192.168.1.17:8080/GCM-App-Server/GCMNotification?shareRegId=1";

		// Google Project Number
		static final String GOOGLE_PROJECT_ID = "724307890872";//"1065186434544";
		static final String MESSAGE_KEY = "message";

	}
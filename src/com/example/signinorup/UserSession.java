package com.example.signinorup;

import java.lang.reflect.Constructor;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class UserSession {
	SharedPreferences pref;
	Editor editor;
	Context _context;
	
//	Shared Preferences MODE
	int PVT_MODE = 0;
	
//	SharedPreferences File Name
	public static final String PREFER_NAME = "Reg";
	
	public static final String IS_USER_LOGIN = "IsUserLoggedIn";
	
	public static final String KEY_NAME = "Name";
	public static final String KEY_EMAIL = "Email";
	public static final String KEY_PASSWORD = "Password";
	
//	Constructor
	public UserSession(Context context){
		this._context = context;
		pref = _context.getSharedPreferences(PREFER_NAME, PVT_MODE);
		editor = pref.edit();
	}
	
//	Create Login Session
	public void createUserLoginSession(String uEmail, String uPassword) {
//		Storing login value as TRUE
		editor.putBoolean(IS_USER_LOGIN, true);
		
//		Storing Email in Preferences
		editor.putString(KEY_EMAIL, uEmail);
		
//		Storing Password in Preferences
		editor.putString(KEY_PASSWORD, uPassword);
		
//		Commit changes
		editor.commit();
		
	}
	
	public boolean checkLogin() {
		if(!this.isUserLoggedIn()){
			Intent i = new Intent(_context, MainActivity.class);
			
//			Closing all activities from stack
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			
//			Add new flag to start new activity
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			
			_context.startActivity(i);
			return true;
		}
		return false;
	}
	
//	Get stored session data
	public HashMap<String, String> getUserDetails() {
		//USe HashMap to store your credentials
		HashMap<String, String> user = new HashMap<String, String>();
		
		//name
		user.put(KEY_NAME, pref.getString(KEY_NAME, null));
		//email
		user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
		//email
		user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));
		
		return user;
	}
	
	// Clear session details
	public void logoutUser() {
		editor.putBoolean("status", false);
		editor.putInt("pos", -1);
		editor.commit();
		pref.getBoolean(IS_USER_LOGIN, false);
		
		Intent i = new Intent(_context, MainActivity.class);
		
//		Closing all activities from stack
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);		
//		Add new flag to start new activity
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		_context.startActivity(i);
	}
	
	public boolean isUserLoggedIn() {
		return pref.getBoolean(IS_USER_LOGIN, false);
	}

}

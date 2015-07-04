package com.example.signinorup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity_Welcome extends Activity {

	public JSONArray jArray;
	public JSONObject jObject = null;
	public String userCredentials;
	private static final String PREFER_NAME = "Reg";
	private SharedPreferences sharedPreferences;
	UserSession session;
	String name = "Unknown User";
	public boolean IS_LOGGED_IN;
	public int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        
        //User session manager
      	session = new UserSession(getApplicationContext());
      	sharedPreferences = getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
		userCredentials = sharedPreferences.getString("User", null);
		try {
			jArray = new JSONArray(userCredentials);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TextView message = (TextView) findViewById(R.id.msg);
		IS_LOGGED_IN = sharedPreferences.getBoolean("status", false);
		position = sharedPreferences.getInt("pos", -1);
		try {
			jObject = jArray.getJSONObject(position);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			name = jObject.getString("Name");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		message.setText(name);
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        
        if (id == R.id.action_logout) {
            session.logoutUser();
            finish();
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
}

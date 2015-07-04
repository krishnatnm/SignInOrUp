package com.example.signinorup;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.bool;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity_SignUp extends Activity {

	private EditText nameEditText;
	private EditText emailEditText;
	private EditText pass1EditText;
	private EditText pass2EditText;

	public JSONObject obj = null;
	public JSONArray jsonArray;

	SharedPreferences sharedPreferences;
	Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);

		sharedPreferences = getApplicationContext().getSharedPreferences("Reg",
				0);

		String jsonString = sharedPreferences.getString("User", null);

		if (jsonString != null) {
			try {
				jsonArray = new JSONArray(jsonString);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// 0 is for private mode
		// 1st arg is the name of the file
		editor = sharedPreferences.edit();

		nameEditText = (EditText) findViewById(R.id.name);
		emailEditText = (EditText) findViewById(R.id.email);
		pass1EditText = (EditText) findViewById(R.id.pass1);
		pass2EditText = (EditText) findViewById(R.id.pass2);

		Button create = (Button) findViewById(R.id.create);
		create.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				final String name = nameEditText.getText().toString();
				final String email = emailEditText.getText().toString();
				final String pass1 = pass1EditText.getText().toString();
				final String pass2 = pass2EditText.getText().toString();
				if (name.isEmpty()) {
					nameEditText.setError("Cannot Be Left Blank");
				} else if (!isValidName(name)) {
					nameEditText.setError("Invalid Name");
				} else if (email.isEmpty()) {
					emailEditText.setError("Cannot Be Left Blank");
				} else if (!isValidEmail(email)) {
					emailEditText.setError("Invalid Email");
				} else if (pass1.isEmpty()) {
					pass1EditText.setError("Cannot Be Left Blank");
				} else if (!isValidPassword(pass1)) {
					pass1EditText.setError("Invalid Password");
				} else if (pass2.isEmpty()) {
					pass2EditText.setError("Cannot Be Left Blank");
				} else if (!pass2.equals(pass1)) {
					pass2EditText.setError("Password Don't Match");
				}

				if (isValidEmail(email) && isValidPassword(pass1)
						&& pass2.equals(pass1)) {
					obj = new JSONObject();
					try {
						obj.put("Name", name);
						obj.put("Email", email);
						obj.put("Password", pass1);
					} catch (JSONException e) {
						e.printStackTrace();
					}

					if (jsonArray == null) {
						jsonArray = new JSONArray();
						Toast.makeText(MainActivity_SignUp.this,
								"New JSON Array Created!", Toast.LENGTH_SHORT)
								.show();
					}

					jsonArray.put(obj);

					editor.putString("User", jsonArray.toString());
					editor.commit();

					Toast x = Toast.makeText(MainActivity_SignUp.this,
							"Account Successfully Created!", Toast.LENGTH_LONG);
					x.show();
					Intent i = new Intent(MainActivity_SignUp.this,
							MainActivity.class);
					startActivity(i);
					finish();
				}
			}
		});

		TextView button2 = (TextView) findViewById(R.id.signin);
		button2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent h = new Intent(MainActivity_SignUp.this,
						MainActivity.class);
				startActivity(h);
				finish();

			}
		});

	}

	private boolean isValidName(String name) {
		String NAME_PATTERN = "^[\\p{L} .'-]+$";
		Pattern pattern = Pattern.compile(NAME_PATTERN);
		Matcher matcher = pattern.matcher(name);
		return matcher.matches();
	}

	private boolean isValidEmail(String email) {
		String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	private boolean isValidPassword(String pass1) {
		if (pass1.length() >= 6) {
			return true;
		}
		return false;
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
		return super.onOptionsItemSelected(item);
	}
}

package com.example.signinorup;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.text.Editable;
import android.text.InputFilter.LengthFilter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	final Context context = this;

	private EditText emailEditText;
	private EditText passEditText;

	String a = "admin@gmail.com";
	public String b = "admin123";

	private String blank = "Cannot Be Left Blank!";
	private String dialog_title = "Reset Your Password!";
	private String dialog_toast = "Password Successfully Changed!";

	private static final String PREFER_NAME = "Reg";
	private SharedPreferences sharedPreferences;
	UserSession session;

	public JSONArray jArray;
	public JSONObject jObject;
	public String userCredentials;
	public int arrayLength = 0;
	int i;
	public boolean IS_LOGGED_IN = false;
	public int position;
	Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// User session manager
		session = new UserSession(getApplicationContext());
		sharedPreferences = getSharedPreferences("Reg", 0);

		emailEditText = (EditText) findViewById(R.id.email);
		passEditText = (EditText) findViewById(R.id.password);
		Button button1 = (Button) findViewById(R.id.signin);
		editor = sharedPreferences.edit();
		IS_LOGGED_IN = sharedPreferences.getBoolean("status", false);
		if(IS_LOGGED_IN){
			Intent i = new Intent(MainActivity.this, MainActivity_Welcome.class);
			startActivity(i);
		}
		
		userCredentials = sharedPreferences.getString("User", null);
		if (userCredentials != null) {

			try {
				jArray = new JSONArray(userCredentials);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			arrayLength = jArray.length();
			Toast.makeText(context, "The String ; " + userCredentials,
					Toast.LENGTH_SHORT).show();
		}

		button1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final String email = emailEditText.getText().toString();
				final String pass = passEditText.getText().toString();

				if (email.isEmpty()) {
					emailEditText.setError(blank);
				} else if (!isValidEmail(email)) {
					emailEditText.setError("Invalid Email");
				}

				else if (pass.isEmpty()) {
					passEditText.setError(blank);
				} else if (!isValidPassword(pass)) {
					passEditText.setError("Inavlid Password");
				}

				String uEmail = null;
				String uPassword = null;

				if (isValidEmail(email) && isValidPassword(pass)) {
					if (userCredentials != null) {
						for (i = 0; i < arrayLength; i++) {
							jObject = null;
							try {
								jObject = jArray.getJSONObject(i);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							try {
								uEmail = jObject.getString("Email");
								uPassword = jObject.getString("Password");
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if (uEmail.equals(email)) {
								if (uPassword.equals(pass)) {
									editor.putInt("pos", i);
									editor.putBoolean("status", true);
									editor.commit();
									Toast.makeText(context,
											"Log In Successful!",
											Toast.LENGTH_SHORT).show();
									Intent in = new Intent(MainActivity.this,
											MainActivity_Welcome.class);
									in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
									in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
									startActivity(in);
									finish();
								}

								else {
									Toast.makeText(context,
											"Incorrect Password!",
											Toast.LENGTH_SHORT).show();
								}

							}
						}
					} else {
						Toast.makeText(context, "User Not Registered!",
								Toast.LENGTH_SHORT).show();
					}
				}

			}
		});

		TextView button2 = (TextView) findViewById(R.id.signup);
		button2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(MainActivity.this,
						MainActivity_SignUp.class);
				startActivity(i);

			}
		});

		TextView forgot = (TextView) findViewById(R.id.forgot);
		forgot.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// TODO Auto-generated method stub

				LayoutInflater li = LayoutInflater.from(context);
				View custom_dialog = li.inflate(R.layout.dialog, null);
				AlertDialog.Builder alertDialoguilder = new AlertDialog.Builder(
						context);
				alertDialoguilder.setCancelable(true).setView(custom_dialog)
						.setTitle(dialog_title);
				final AlertDialog alertDialog = alertDialoguilder.create();
				final EditText f_email = (EditText) custom_dialog
						.findViewById(R.id.forgot_email);
				final EditText f_pass1 = (EditText) custom_dialog
						.findViewById(R.id.forgot_pass1);
				final EditText f_pass2 = (EditText) custom_dialog
						.findViewById(R.id.forgot_pass2);

				Button okay = (Button) custom_dialog
						.findViewById(R.id.forgot_button);
				okay.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						String emailET = f_email.getText().toString();
						String pass1ET = f_pass1.getText().toString();
						String pass2ET = f_pass2.getText().toString();

						if (emailET.isEmpty()) {
							f_email.setError(blank);
						} else if (!isValidEmail(emailET)) {
							f_email.setError("Invalid Email");
						} else if (pass1ET.isEmpty()) {
							f_pass1.setError(blank);
						} else if (!isValidPassword(pass1ET)) {
							f_pass1.setError("Invalid Password");
						} else if (pass2ET.isEmpty()) {
							f_pass2.setError(blank);
						} else if (!pass2ET.equals(pass1ET)) {
							f_pass2.setError("Passwords Don't Match");
						} else if (emailET.equals(a)) {
							b = pass1ET;
							Toast.makeText(MainActivity.this,
									"Password Changed Successfully",
									Toast.LENGTH_SHORT).show();
							alertDialog.cancel();
						} else {
							Toast.makeText(MainActivity.this,
									"This Account Doesn't Exist!",
									Toast.LENGTH_SHORT).show();
							alertDialog.cancel();
						}

					}
				});

				alertDialog.show();

			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		
		// User session manager
		session = new UserSession(getApplicationContext());
		sharedPreferences = getSharedPreferences("Reg", 0);

		emailEditText = (EditText) findViewById(R.id.email);
		passEditText = (EditText) findViewById(R.id.password);
		Button button1 = (Button) findViewById(R.id.signin);

		userCredentials = sharedPreferences.getString("User", null);
		if (userCredentials != null) {

			try {
				jArray = new JSONArray(userCredentials);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			arrayLength = jArray.length();
			Toast.makeText(context, "The String ; " + userCredentials,
					Toast.LENGTH_SHORT).show();
		}

		button1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final String email = emailEditText.getText().toString();
				final String pass = passEditText.getText().toString();

				if (email.isEmpty()) {
					emailEditText.setError(blank);
				} else if (!isValidEmail(email)) {
					emailEditText.setError("Invalid Email");
				}

				else if (pass.isEmpty()) {
					passEditText.setError(blank);
				} else if (!isValidPassword(pass)) {
					passEditText.setError("Inavlid Password");
				}

				String uEmail = null;
				String uPassword = null;

				if (isValidEmail(email) && isValidPassword(pass)) {
					if (userCredentials != null) {
						for (i = 0; i < arrayLength; i++) {
							jObject = null;
							try {
								jObject = jArray.getJSONObject(i);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							try {
								uEmail = jObject.getString("Email");
								uPassword = jObject.getString("Password");
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if (uEmail.equals(email)) {
								if (uPassword.equals(pass)) {
									editor.putInt("pos", i);
									editor.putBoolean("status", true);
									editor.commit();
									Toast.makeText(context,
											"Log In Successful!",
											Toast.LENGTH_SHORT).show();
									Intent in = new Intent(MainActivity.this,
											MainActivity_Welcome.class);
									in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
									in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
									startActivity(in);
									finish();
								}

								else {
									Toast.makeText(context,
											"Incorrect Password!",
											Toast.LENGTH_SHORT).show();
								}

							}
						}
					} else {
						Toast.makeText(context, "User Not Registered!",
								Toast.LENGTH_SHORT).show();
					}
				}

			}
		});

		TextView button2 = (TextView) findViewById(R.id.signup);
		button2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(MainActivity.this,
						MainActivity_SignUp.class);
				startActivity(i);

			}
		});

		TextView forgot = (TextView) findViewById(R.id.forgot);
		forgot.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// TODO Auto-generated method stub

				LayoutInflater li = LayoutInflater.from(context);
				View custom_dialog = li.inflate(R.layout.dialog, null);
				AlertDialog.Builder alertDialoguilder = new AlertDialog.Builder(
						context);
				alertDialoguilder.setCancelable(true).setView(custom_dialog)
						.setTitle(dialog_title);
				final AlertDialog alertDialog = alertDialoguilder.create();
				final EditText f_email = (EditText) custom_dialog
						.findViewById(R.id.forgot_email);
				final EditText f_pass1 = (EditText) custom_dialog
						.findViewById(R.id.forgot_pass1);
				final EditText f_pass2 = (EditText) custom_dialog
						.findViewById(R.id.forgot_pass2);

				Button okay = (Button) custom_dialog
						.findViewById(R.id.forgot_button);
				okay.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						String emailET = f_email.getText().toString();
						String pass1ET = f_pass1.getText().toString();
						String pass2ET = f_pass2.getText().toString();

						if (emailET.isEmpty()) {
							f_email.setError(blank);
						} else if (!isValidEmail(emailET)) {
							f_email.setError("Invalid Email");
						} else if (pass1ET.isEmpty()) {
							f_pass1.setError(blank);
						} else if (!isValidPassword(pass1ET)) {
							f_pass1.setError("Invalid Password");
						} else if (pass2ET.isEmpty()) {
							f_pass2.setError(blank);
						} else if (!pass2ET.equals(pass1ET)) {
							f_pass2.setError("Passwords Don't Match");
						} else if (emailET.equals(a)) {
							b = pass1ET;
							Toast.makeText(MainActivity.this,
									"Password Changed Successfully",
									Toast.LENGTH_SHORT).show();
							alertDialog.cancel();
						} else {
							Toast.makeText(MainActivity.this,
									"This Account Doesn't Exist!",
									Toast.LENGTH_SHORT).show();
							alertDialog.cancel();
						}

					}
				});

				alertDialog.show();

			}
		});

	}

	private boolean isValidEmail(String email) {
		String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	private boolean isValidPassword(String pass) {
		if (pass != null && pass.length() >= 6) {
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

package com.appholeex.madscalcy.activities;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.appholeex.madscalcy.data.SharedPreferenceManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class BaseActivity extends AppCompatActivity {
	
	public SharedPreferenceManager prefManager;
	public DisplayMetrics metrices;

	public boolean isLogin;
	public String mobNo, custID;

	FirebaseDatabase firebaseDatabase;
	DatabaseReference databaseReference;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		prefManager = new SharedPreferenceManager(this);
		metrices = getResources().getDisplayMetrics();

		firebaseDatabase = FirebaseDatabase.getInstance();
		databaseReference= firebaseDatabase.getReference();
	}

	public boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager =
				(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		boolean isConnected = false;
		if (connectivityManager != null) {
			NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
			isConnected = (activeNetwork != null) && (activeNetwork.isConnectedOrConnecting());
		}

		return isConnected;
	}

	public String getNewChildKey(){
		return databaseReference.push().getKey();
	}
	public void fullScreen(){
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
				| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
	}

	public void showAlterMsg(String msg,String positive,String negative)
	{
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setMessage(msg);
				alertDialogBuilder.setPositiveButton(positive,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {

							}
						});

		alertDialogBuilder.setNegativeButton(negative,new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		});

		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	public void showAlterWithSingleOption(String msg,String positive)
	{
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setMessage(msg);
		alertDialogBuilder.setPositiveButton(positive,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						arg0.dismiss();
					}
				});

		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}
}

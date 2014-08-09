package com.fundora.hangman.activities;

import com.fundora.hangman.R;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class AboutActivity extends Activity {

	ImageView btnExit;
	MediaPlayer mpHangman;
	MediaPlayer mpButtonClick;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_about);
		
		initializeUI();
	}

	void initializeUI(){
		try{
		mpButtonClick = MediaPlayer.create(this, R.raw.button_click);
		btnExit = (ImageView) findViewById(R.id.btn_back);
		btnExit.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				mpButtonClick.start();
				finish();
			}
		});
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater meniu = getMenuInflater();
		meniu.inflate(R.menu.main_menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == R.id.menu_settings) {
			mpButtonClick.start();
			Intent aboutIntent = new Intent(AboutActivity.this,SettingsActivity.class);
			AboutActivity.this.startActivity(aboutIntent);
			return true;
		} else if (itemId == R.id.Exit) {
			mpButtonClick.start();
			android.os.Process.killProcess(android.os.Process.myPid());
			return true;
		}
		return false;
	}
}

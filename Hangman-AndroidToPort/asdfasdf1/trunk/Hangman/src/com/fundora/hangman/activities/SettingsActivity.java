package com.fundora.hangman.activities;

import java.io.FileOutputStream;

import com.fundora.hangman.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ToggleButton;

public class SettingsActivity extends Activity implements OnClickListener {

	public static final String SETTING_ON_SCREEN_KEYBOARD = "on_screen_keybord";
	public static final String SETTING_SOUND_ENABLED = "sound_enabled";
	public static final String SETTING_FILENAME = "AppSettings";

	ToggleButton toggleButton1, toggleButton2;
	FileOutputStream fos;
	public final static String FILENAME = "settings.txt";
	boolean HardwareKeyBoard = true, SoundEnabled = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_settings);

		initializeUI();
	}

	void initializeUI() {
		try {
			Button save = (Button) findViewById(R.id.bSave);
			toggleButton1 = (ToggleButton) findViewById(R.id.toggleButton1);
			toggleButton2 = (ToggleButton) findViewById(R.id.toggleButton2);

			loadSettings();
			toggleButton1.setChecked(HardwareKeyBoard);
			toggleButton2.setChecked(SoundEnabled);

			save.setOnClickListener(this);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void onClick(View v) {
		try {
			int id = v.getId();
			if (id == R.id.bSave) {
				SharedPreferences settings = getSharedPreferences(SETTING_FILENAME, MODE_PRIVATE);
				SharedPreferences.Editor editor = settings.edit();
				editor.putBoolean(SETTING_ON_SCREEN_KEYBOARD, toggleButton1.isChecked());
				editor.putBoolean(SETTING_SOUND_ENABLED, toggleButton2.isChecked());
				editor.commit();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	void loadSettings() {

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
			Intent aboutIntent = new Intent(SettingsActivity.this,
					com.fundora.hangman.activities.SettingsActivity.class);
			SettingsActivity.this.startActivity(aboutIntent);
			return true;
		} else if (itemId == R.id.Exit) {
			android.os.Process.killProcess(android.os.Process.myPid());
			return true;
		}
		return false;
	}

}

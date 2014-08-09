package com.fundora.hangman.activities;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fundora.hangman.R;
import com.fundora.hangman.util.HighScoreOpenHelper;
import com.fundora.hangman.util.LetterButton;

public class GameActivity extends Activity {

	public static final String KEY_DIFFICULTY = "key_diff";
	public static final String GAME_MODE = "gamemode";
	public static final String PREFS_NAME = "Hangman_pref";

	public static final int DIFFICULTY_EASY = 0;
	public static final int DIFFICULTY_MEDIUM = 1;
	public static final int DIFFICULTY_HARD = 2;

	public static final long TIME_ATTACK_TIME = 60000; // millisecs

	int currentScore = 0;

	public static int MODE_SURVIVAL = 0;
	public static int MODE_NORMAL = 1;
	public static int MODE_TIME_ATTACK = 2;
	public static int MODE_SURVIVAL_TIME_ATTACK = 3;
	int[] IndexList;
	ArrayList<String> words = new ArrayList<String>();
	int index;

	public EditText name_field;

	LetterClickListener letterClickListener = new LetterClickListener();

	public int gameMode;

	TextView hiddenText, hintText, enteredText, scoreText, mChronometer;

	LetterButton letterA, letterB, letterC, letterD, letterE, letterF, letterG, letterH, letterI,
			letterJ, letterK, letterL, letterM, letterN, letterO, letterP, letterQ, letterR,
			letterS, letterT, letterU, letterV, letterW, letterX, letterY, letterZ;

	MediaPlayer mpTimeAttack, mpNormal, mpDeath, mpButtonClick;

	EditText input;

	RelativeLayout background;

	Button backBtn, newGameBtn;

	ArrayList<LetterButton> buttonList = new ArrayList<LetterButton>();

	HangmanCountDownTimer mTimer;

	// text stocheaza cuvintele ascunse
	String hideText;
	// indiciu
	String hint;
	// evidenta a literelor introduse
	String entered = " ";
	// contor de erori
	String score;

	int count;
	// nivelul de dificultate
	// 0 pentru usor
	// 1 pentru normal
	// 2 pentru greu
	// -1 pentru continuare (datele sunt salvate in variabilele principale(text,
	// entered, count)
	int diff;
	boolean onScreenKeyBoard, soundEnabled;

	protected void loadSettings() {
		SharedPreferences settings = getSharedPreferences(SettingsActivity.SETTING_FILENAME,
				MODE_PRIVATE);

		onScreenKeyBoard = settings.getBoolean(SettingsActivity.SETTING_ON_SCREEN_KEYBOARD, true);
		soundEnabled = settings.getBoolean(SettingsActivity.SETTING_SOUND_ENABLED, true);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		loadSettings();
		if (onScreenKeyBoard)
			setContentView(R.layout.activity_game_touchscreen);
		else
			setContentView(R.layout.activity_game_keyboard);
		
		populateGameWords();
		randomizeText();
		
		initializeUI();


	}

	void initializeUI() {
		try {
			name_field = new EditText(this);
			mpButtonClick = MediaPlayer.create(this, R.raw.button_click);
			// ia nivelul de dificultate
			diff = getIntent().getIntExtra(KEY_DIFFICULTY, -1);
			gameMode = getIntent().getIntExtra(GAME_MODE, -1);

			// ia un cuvant ascuns nou
			String texthint = getText();
			String[] pair = texthint.split("#");
			hideText = pair[0];
			hint = pair[1];
			String c;
			if (hideText.charAt(0) != hideText.charAt(hideText.length() - 1))
				c = "" + hideText.charAt(0) + hideText.charAt(hideText.length() - 1);
			else
				c = "" + hideText.charAt(0);
			entered += c;
			mChronometer = (TextView) findViewById(R.id.mChronometer);

			// asambleaza interfata utilizatorului
			hintText = (TextView) findViewById(R.id.hint_txt);
			hintText.setText(hint);

			hiddenText = (TextView) findViewById(R.id.hidden_txt);
			hiddenText.setText(encodeString(hideText, "*"));

			enteredText = (TextView) findViewById(R.id.entered_txt);
			enteredText.setVisibility(View.INVISIBLE);
			enteredText.setText(entered);

			scoreText = (TextView) findViewById(R.id.scor_txt);
			scoreText.setText(score);

			Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/GosmickSansBold.ttf");

			hintText.setTypeface(tf);
			hiddenText.setTypeface(tf);
			enteredText.setTypeface(tf);
			scoreText.setTypeface(tf);
			mChronometer.setTypeface(tf);

			background = (RelativeLayout) findViewById(R.id.rela);
			mTimer = null;
			count = setDifficulty(diff); // contorul setare dificultate
			changeImage(count); // contorul imaginilor
			if (onScreenKeyBoard) {
				letterA = (LetterButton) findViewById(R.id.letterA);
				letterB = (LetterButton) findViewById(R.id.letterB);
				letterC = (LetterButton) findViewById(R.id.letterC);
				letterD = (LetterButton) findViewById(R.id.letterD);
				letterE = (LetterButton) findViewById(R.id.letterE);
				letterF = (LetterButton) findViewById(R.id.letterF);
				letterG = (LetterButton) findViewById(R.id.letterG);
				letterH = (LetterButton) findViewById(R.id.letterH);
				letterI = (LetterButton) findViewById(R.id.letterI);
				letterJ = (LetterButton) findViewById(R.id.letterJ);
				letterK = (LetterButton) findViewById(R.id.letterK);
				letterL = (LetterButton) findViewById(R.id.letterL);
				letterM = (LetterButton) findViewById(R.id.letterM);
				letterN = (LetterButton) findViewById(R.id.letterN);
				letterO = (LetterButton) findViewById(R.id.letterO);
				letterP = (LetterButton) findViewById(R.id.letterP);
				letterQ = (LetterButton) findViewById(R.id.letterQ);
				letterR = (LetterButton) findViewById(R.id.letterR);
				letterS = (LetterButton) findViewById(R.id.letterS);
				letterT = (LetterButton) findViewById(R.id.letterT);
				letterU = (LetterButton) findViewById(R.id.letterU);
				letterV = (LetterButton) findViewById(R.id.letterV);
				letterW = (LetterButton) findViewById(R.id.letterW);
				letterX = (LetterButton) findViewById(R.id.letterX);
				letterY = (LetterButton) findViewById(R.id.letterY);
				letterZ = (LetterButton) findViewById(R.id.letterZ);

				buttonList.add(letterA);
				buttonList.add(letterB);
				buttonList.add(letterC);
				buttonList.add(letterD);
				buttonList.add(letterE);
				buttonList.add(letterF);
				buttonList.add(letterG);
				buttonList.add(letterH);
				buttonList.add(letterI);
				buttonList.add(letterJ);
				buttonList.add(letterK);
				buttonList.add(letterL);
				buttonList.add(letterM);
				buttonList.add(letterN);
				buttonList.add(letterO);
				buttonList.add(letterP);
				buttonList.add(letterQ);
				buttonList.add(letterR);
				buttonList.add(letterS);
				buttonList.add(letterT);
				buttonList.add(letterU);
				buttonList.add(letterV);
				buttonList.add(letterX);
				buttonList.add(letterY);
				buttonList.add(letterZ);
				buttonList.add(letterW);
				setOnClickHandler();
			}
			if (gameMode == MODE_SURVIVAL_TIME_ATTACK) {
				if (mTimer == null) {
					mTimer = new HangmanCountDownTimer(TIME_ATTACK_TIME);
					mTimer.start();
				}
			} else if (gameMode == MODE_SURVIVAL) {
				mChronometer.setVisibility(View.INVISIBLE);
				scoreText.setVisibility(View.VISIBLE);
			} else if (gameMode == MODE_TIME_ATTACK) {
				if (mTimer == null) {
					mTimer = new HangmanCountDownTimer(TIME_ATTACK_TIME);
					mTimer.start();
					scoreText.setVisibility(View.INVISIBLE);
				}
			} else {
				mChronometer.setVisibility(View.INVISIBLE);
				scoreText.setVisibility(View.INVISIBLE);
			}

			// setare input
			input = (EditText) findViewById(R.id.input_txt);
			if (input != null) {
				input.setFocusable(true);
				input.setOnKeyListener(new OnKeyListener() {

					public boolean onKey(View v, int keyCode, KeyEvent event) {

						String temp;
						String newLetter = "&";

						if (input.getText().length() == 0)
							return false;

						newLetter = input.getText().subSequence(0, 1).toString()
								.toUpperCase(Locale.getDefault());

						input.setText(""); // sterge textul tastat

						if (!((newLetter.charAt(0) >= 'A' && newLetter.charAt(0) <= 'Z')))
							return false;

						temp = (String) enteredText.getText();
						if (temp.indexOf(newLetter.toUpperCase(Locale.getDefault())) >= 0) {
							input.setText("");
							return false;
						}
						gameRules(newLetter);
						return false;
					}

				});
			}

			ImageView backBtn = (ImageView) findViewById(R.id.btn_back);
			backBtn.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					mpButtonClick.start();
					finish();
				}
			});

			ImageView newGameBtn = (ImageView) findViewById(R.id.btn_newGame);
			newGameBtn.setOnClickListener(new View.OnClickListener() {
				// Creare joc nou
				public void onClick(View v) {
					mpButtonClick.start();

					Toast.makeText(getApplicationContext(), getResources().getString(R.string.ACTIVITY_GAME_NEWGAME),
							Toast.LENGTH_SHORT).show();
					new AlertDialog.Builder(GameActivity.this).setTitle(getResources().getString(R.string.ACTIVITY_GAME_SELECT_DIFFICULTY))
							.setItems(R.array.difficulty, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialoginterface, int i) {
									startGame(i);
								}
							})

							.show();
				}
			});
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public boolean gameRules(String newLetter) {

		entered += newLetter.toUpperCase(Locale.getDefault()); // adauga litera
																// la stringul
		// introdus
		enteredText.setText(enteredText.getText() + newLetter.toUpperCase(Locale.getDefault()));
		hiddenText.setText(encodeString(hideText, newLetter.toUpperCase(Locale.getDefault())));

		// Verifica daca ai castigat
		if (win(hideText)) {
			if (gameMode != MODE_SURVIVAL && gameMode != MODE_SURVIVAL_TIME_ATTACK) {
				Toast.makeText(getApplicationContext(),getResources().getString(R.string.ACTIVITY_GAME_WINNER),
						Toast.LENGTH_LONG).show();
				changeImage(999);
				if (input != null)
					input.setFocusable(false);

			}
			if (gameMode == MODE_SURVIVAL) {

				String texthint = getText();
				String[] pair = texthint.split("#");
				String c;
				hideText = pair[0];
				hint = pair[1];

				hintText.setText(hint);
				scoreText.setText(score);

				if (hideText.charAt(0) != hideText.charAt(hideText.length() - 1))
					c = "" + hideText.charAt(0) + hideText.charAt(hideText.length() - 1);
				else
					c = "" + hideText.charAt(0);
				entered = " " + c;
				hiddenText.setText(encodeString(hideText, "*"));
				enteredText.setText(entered);
				makeAllVisible();

				currentScore = currentScore + 100;

				scoreText.setText(Integer.toString(currentScore));

				return false;

			}
			if (gameMode == MODE_SURVIVAL_TIME_ATTACK) {

				String texthint = getText();
				String[] pair = texthint.split("#");
				hideText = pair[0];
				hint = pair[1];

				hintText.setText(hint);
				scoreText.setText(score);

				String c;

				if (hideText.charAt(0) != hideText.charAt(hideText.length() - 1))
					c = "" + hideText.charAt(0) + hideText.charAt(hideText.length() - 1);
				else
					c = "" + hideText.charAt(0);
				entered = " " + c;
				hiddenText.setText(encodeString(hideText, "*"));
				enteredText.setText(entered);
				makeAllVisible();

				int secondsleft = 0;
				if (mTimer != null) {
					secondsleft = mTimer.getSecondsLeft();
					mTimer.cancel();
				}

				currentScore = currentScore + 100 + secondsleft;
				scoreText.setText(Integer.toString(currentScore));

				mTimer = new HangmanCountDownTimer(TIME_ATTACK_TIME);
				mTimer.start();
				return false;

			}
			if (gameMode == MODE_TIME_ATTACK) {

				Toast.makeText(getApplicationContext(), getResources().getString(R.string.ACTIVITY_GAME_WINNER),
						Toast.LENGTH_LONG).show();
				changeImage(999);
				if (input != null)
					input.setFocusable(false);
			}

		}
		if (hideText.indexOf(newLetter.toUpperCase(Locale.getDefault())) == -1) {
			count--;
			currentScore--;
			scoreText.setText(Integer.toString(currentScore));
		}
		if (count == 0) {
			Toast.makeText(getApplicationContext(), getResources().getString(R.string.ACTIVITY_GAME_LOOSER), Toast.LENGTH_LONG).show();
			hiddenText.setText(encodeString(hideText, "#"));
			if (input != null)
				input.setFocusable(false);
		}
		return false;
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater meniu = getMenuInflater();
		meniu.inflate(R.menu.main_menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_settings:
			mpButtonClick.start();
			Intent aboutIntent = new Intent(GameActivity.this,SettingsActivity.class);
			GameActivity.this.startActivity(aboutIntent);
			return true;
		case R.id.Exit:
			mpButtonClick.start();
			super.onDestroy();
			this.finish();
			return true;
		}
		return false;
	}

	public void setOnClickHandler() {
		for (LetterButton b : buttonList) {
			b.setOnClickListener(letterClickListener);
		}
	}

	protected void makeAllVisible() {
		for (LetterButton b : buttonList) {
			b.setVisibility(View.VISIBLE);
		}
	}

	protected void makeAllInVisible() {
		for (LetterButton b : buttonList) {
			b.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();

		// Salveaza jocul curent
		String old_entered = this.entered;
		String old_text = this.hideText;
		String old_hint = this.hint;
		String old_scor = this.scoreText.getText().toString();
		int old_count = this.count;

		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("old_text", old_text);
		editor.putString("old_entered", old_entered);
		editor.putString("old_hint", old_hint);
		editor.putString("old_scor", old_scor);
		editor.putInt("old_count", old_count);
		editor.putInt("old_GAME_MODE", this.gameMode);
		if (gameMode == MODE_SURVIVAL_TIME_ATTACK || gameMode == MODE_TIME_ATTACK) {
			editor.putLong("old_time_left", mTimer.getMillisecondsLeft());
			mTimer.cancel();
		}
		editor.commit();
		getIntent().putExtra(KEY_DIFFICULTY, -1);
	}

	private String encodeString(String target, String letter) {
		String result = "";
		if (letter.contains("*")) {
			for (int i = 0; i < target.length(); i++) {
				if (target.charAt(i) == letter.charAt(0)) {
					result += target.charAt(i) + " ";
				} else if (entered.indexOf(target.charAt(i)) != -1) {
					result += target.charAt(i) + " ";
				} else
					result += "_ ";
			}
			return result;
		} else if (letter.contains("#")) {
			for (int i = 0; i < target.length(); i++) {
				result += target.charAt(i) + " ";
			}
			return result;
		} else {
			for (int i = 0; i < target.length(); i++) {
				if (target.charAt(i) == letter.charAt(0)) {
					result += target.charAt(i) + " ";
					currentScore++;
					scoreText.setText(Integer.toString(currentScore));
				} else if (entered.indexOf(target.charAt(i)) != -1) {
					result += target.charAt(i) + " ";
				} else
					result += "_ ";
			}
			return result;
		}
	}

	private boolean win(String text) {
		if (text.length() > 0 && entered.length() > 0) {
			for (int i = 0; i < text.length(); i++) {
				if (entered.indexOf(text.charAt(i)) == -1) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	private void changeImage(int count) {
		switch (count) {
		case 6:
			background.setBackgroundResource(R.drawable.game0);
			break;
		case 5:
			background.setBackgroundResource(R.drawable.game1);
			break;
		case 4:
			background.setBackgroundResource(R.drawable.game2);
			break;
		case 3:
			background.setBackgroundResource(R.drawable.game3);
			break;
		case 2:
			background.setBackgroundResource(R.drawable.game4);
			break;
		case 1:
			background.setBackgroundResource(R.drawable.game5);
			break;
		case 0:
			background.setBackgroundResource(R.drawable.gamefinal);
			if (mTimer != null)
				mTimer.cancel();
			updateScore();
			makeAllInVisible();
			break;
		case 999:
			background.setBackgroundResource(R.drawable.gamewin);
			if (mTimer != null)
				mTimer.cancel();
			makeAllInVisible();
			break;

		default:
			background.setBackgroundResource(R.drawable.game0);
			break;
		}
	}

	private int setDifficulty(int dif) {
		switch (dif) {
		case -1:
			SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
			String old_text = settings.getString("old_text", hideText);
			String old_entered = settings.getString("old_entered", "");
			String old_hint = settings.getString("old_hint", hint);
			String old_scor = settings.getString("old_scor", score);
			gameMode = settings.getInt("old_GAME_MODE", MODE_NORMAL);
			if (gameMode == MODE_SURVIVAL_TIME_ATTACK || gameMode == MODE_TIME_ATTACK) {
				mTimer = new HangmanCountDownTimer(settings.getLong("old_time_left",
						TIME_ATTACK_TIME));
				mTimer.start();
			}
			int old_count = settings.getInt("old_count", count);

			this.hideText = old_text;
			this.entered = old_entered;
			this.count = old_count;
			this.hint = old_hint;
			this.score = old_scor;
			scoreText.setText(score);

			enteredText.setText(entered);
			hintText.setText(hint);
			if (entered.length() > 0) {
				for (int i = 0; i < entered.length(); i++) {
					hiddenText.setText(encodeString(hideText, entered.charAt(i) + ""));
				}
			}
			changeImage(count);
			return count;
		case 0:
			return 6;
		case 1:
			return 5;
		case 2:
			return 4;
		default:
			return 6;
		}
	}

	private String getText() {
		if (index < words.size()) {
			index++;
			return words.get(IndexList[index - 1]);
		} else {
			randomizeText();
			return getText();
		}
	}

	public void updateScore() {
		if (gameMode != MODE_SURVIVAL && gameMode != MODE_SURVIVAL_TIME_ATTACK)
			return;
		SQLiteDatabase db = (new HighScoreOpenHelper(this)).getWritableDatabase();
		String table;

		if (gameMode == MODE_SURVIVAL) {
			switch (diff) {
			case DIFFICULTY_HARD:
				table = HighScoreOpenHelper.HARD_TABLE_NAME;
				break;
			case DIFFICULTY_MEDIUM:
				table = HighScoreOpenHelper.MEDIUM_TABLE_NAME;
				break;
			default:
				table = HighScoreOpenHelper.EASY_TABLE_NAME;
				break;
			}
		} else {
			switch (diff) {
			case DIFFICULTY_HARD:
				table = HighScoreOpenHelper.HARD_TABLE_NAME_TIMEATTACK;
				break;
			case DIFFICULTY_MEDIUM:
				table = HighScoreOpenHelper.MEDIUM_TABLE_NAME_TIME_ATTACK;
				break;
			default:
				table = HighScoreOpenHelper.EASY_TABLE_NAME_TIME_ATTACK;
				break;
			}
		}
		long score = Long.parseLong(scoreText.getText().toString());
		Cursor cursor = db.query(table, null, null, null, null, null, "score");

		if (cursor.getCount() >= HighScoreOpenHelper.MAX_ENTRIES) {
			cursor.moveToLast();
			if (score > cursor.getLong(2))
				addHighScore(table, score, db);
		} else {
			addHighScore(table, score, db);
		}
		cursor.close();
	}

	public void addHighScore(final String table, final long score, final SQLiteDatabase db) {
		new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.ACTIVITY_GAME_ADDHIGHSCORE_CONGRATULATIONS))
				.setMessage(getResources().getString(R.string.ACTIVITY_GAME_ADDHIGHSCORE_YOUR_SCORE) + scoreText.getText() + "\nPlease enter your name")
				.setView(name_field).setPositiveButton(getResources().getString(R.string.ACTIVITY_GAME_ADDHIGHSCORE_OK), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						String name = name_field.getText().toString();
						if (name.compareTo("") == 0)
							name = getResources().getString(R.string.ACTIVITY_GAME_ADDHIGHSCORE_ANNONYMOUS_NAME);
						db.execSQL("INSERT INTO " + table + "VALUES(?,'" + name + "'," + score + ")");
						Intent intent = new Intent(GameActivity.this, ScoreActivity.class);
						intent.putExtra(ScoreActivity.EXTRA_DIFFICULTY, diff);
						startActivity(intent);
						db.close();
						finish();
					}
				}).setNegativeButton(getResources().getString(R.string.ACTIVITY_GAME_ADDHIGHSCORE_CANCEL), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					}
				}).show();
	}

	public void populateGameWords() {
		try 
		{
			InputStream database = getResources().openRawResource(R.raw.database);
			BufferedReader myBufferedReader = new BufferedReader(new InputStreamReader(database));
			String myLine;
			while ((myLine = myBufferedReader.readLine()) != null)
			{
				words.add(myLine);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void randomizeText() {
		index = 0;
		IndexList = new int[words.size()];
		Random generator = new Random();
		for (int i = 0; i < words.size(); i++) {
			IndexList[i] = generator.nextInt(words.size());
			for (int j = 0; j < i; j++)
				if (IndexList[i] == IndexList[j])
					i--;
		}
	}

	private void startGame(int i) {
		try {
			Intent intent = new Intent(GameActivity.this, GameActivity.class);
			intent.putExtra(GameActivity.KEY_DIFFICULTY, i);
			intent.putExtra(GameActivity.GAME_MODE, gameMode);
			startActivity(intent);

			finish();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public class HangmanCountDownTimer extends CountDownTimer {

		int min;
		int sec;

		public HangmanCountDownTimer(long millisInFuture) {
			super(millisInFuture, 1000);
			setTimeLeft(millisInFuture);
		}

		public void setTimeLeft(long millisInFuture) {
			// TODO Auto-generated method stub
			min = (int) (millisInFuture / 60000);
			sec = (int) (millisInFuture / 1000) % 60;
		}

		public long getMillisecondsLeft() {
			return sec * 1000 + min * 60000;
		}

		@Override
		public void onTick(long millisUntilFinished) {
			// TODO Auto-generated method stub
			if (sec > 0)
				sec--;
			else {

				if (min > 0) {
					min--;
					sec = 59;
				}
			}
			mChronometer.setText(min + ":" + sec);
		}

		@Override
		public void onFinish() {
			try {
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.ACTIVITY_GAME_ADDHIGHSCORE_TIMEOUT), Toast.LENGTH_LONG)
						.show();
				hiddenText.setText(encodeString(hideText, "#")); // show hidden
																	// word
				background.setBackgroundResource(R.drawable.gamefinal);
				if (input != null)
					input.setFocusable(false);
				mChronometer.setText("0:0");
				makeAllInVisible();
				updateScore();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		public int getSecondsLeft() {
			return min * 60 + sec;
		}
	}

	protected class LetterClickListener implements View.OnClickListener {

		String newLetter;
		String temp;

		public void onClick(View v) 
		{
			v.setVisibility(View.INVISIBLE);
			newLetter = ((LetterButton) v).getLetter().subSequence(0, 1).toString().toUpperCase(Locale.getDefault());
			temp = (String) enteredText.getText();
			if (temp.indexOf(newLetter.toUpperCase(Locale.getDefault())) >= 0) {
				if (input != null)
					input.setText("");
				return;
			}
			gameRules(newLetter);
			return;
		}
	}
}

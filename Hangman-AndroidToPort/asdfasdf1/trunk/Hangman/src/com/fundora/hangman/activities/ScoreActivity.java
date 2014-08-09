package com.fundora.hangman.activities;

import com.fundora.hangman.R;
import com.fundora.hangman.util.HighScoreOpenHelper;
import com.fundora.hangman.util.SelectableTextView;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

public class ScoreActivity extends Activity {
	public static final String EXTRA_DIFFICULTY = "extra_difficulty";
	public static final String EXTRA_GAME_MODE = "extra_game_mode";

	SelectableTextView difficultyEasy, difficultyMedium, difficultyHard,
			modeSurvival, modeSurvivalTimeAttack;
	TextView scoreText;
	int difficulty;
	int gameMode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.highscore);
		difficultyEasy = (SelectableTextView) findViewById(R.id.easyScore);
		difficultyHard = (SelectableTextView) findViewById(R.id.hardScore);
		difficultyMedium = (SelectableTextView) findViewById(R.id.mediumScore);
		modeSurvival = (SelectableTextView) findViewById(R.id.modeSurvival);
		modeSurvivalTimeAttack = (SelectableTextView) findViewById(R.id.modeSurvivalTimeAttack);

		scoreText = (TextView) findViewById(R.id.scoreText);
		OnClickListener difficulyListener = new OnClickListener() {

			public void onClick(View v) {
				difficultyEasy.setSelected(false);
				difficultyHard.setSelected(false);
				difficultyMedium.setSelected(false);
				((SelectableTextView) v).setSelected(true);
				if (difficultyEasy.isSelected())
					difficulty = GameActivity.DIFFICULTY_EASY;
				else if (difficultyHard.isSelected())
					difficulty = GameActivity.DIFFICULTY_HARD;
				else
					difficulty = GameActivity.DIFFICULTY_MEDIUM;
				populateScores();
			}
		};
		OnClickListener gameModeListener = new OnClickListener() {

			public void onClick(View v) {
				modeSurvival.setSelected(false);
				modeSurvivalTimeAttack.setSelected(false);
				((SelectableTextView) v).setSelected(true);
				if (modeSurvival.isSelected())
					gameMode = GameActivity.MODE_SURVIVAL;
				else
					gameMode = GameActivity.MODE_SURVIVAL_TIME_ATTACK;
				populateScores();
			}
		};
		difficultyEasy.setOnClickListener(difficulyListener);
		difficultyHard.setOnClickListener(difficulyListener);
		difficultyMedium.setOnClickListener(difficulyListener);
		modeSurvival.setOnClickListener(gameModeListener);
		modeSurvivalTimeAttack.setOnClickListener(gameModeListener);

		difficulty = getIntent().getIntExtra(EXTRA_DIFFICULTY,
				GameActivity.DIFFICULTY_MEDIUM);

		difficultyEasy.setSelected(false);
		difficultyHard.setSelected(false);
		difficultyMedium.setSelected(false);

		if (difficulty == GameActivity.DIFFICULTY_EASY)
			difficultyEasy.setSelected(true);
		else if (difficulty == GameActivity.DIFFICULTY_HARD)
			difficultyHard.setSelected(true);
		else
			difficultyMedium.setSelected(true);

		gameMode = getIntent().getIntExtra(EXTRA_GAME_MODE, GameActivity.MODE_SURVIVAL);

		populateScores();
	}

	private void populateScores() {
		String table;
		if (gameMode == GameActivity.MODE_SURVIVAL) {
			if (difficulty == GameActivity.DIFFICULTY_EASY)
				table = HighScoreOpenHelper.EASY_TABLE_NAME;
			else if (difficulty == GameActivity.DIFFICULTY_HARD)
				table = HighScoreOpenHelper.HARD_TABLE_NAME;
			else
				table = HighScoreOpenHelper.MEDIUM_TABLE_NAME;
		} else {
			if (difficulty == GameActivity.DIFFICULTY_EASY)
				table = HighScoreOpenHelper.EASY_TABLE_NAME_TIME_ATTACK;
			else if (difficulty == GameActivity.DIFFICULTY_HARD)
				table = HighScoreOpenHelper.HARD_TABLE_NAME_TIMEATTACK;
			else
				table = HighScoreOpenHelper.MEDIUM_TABLE_NAME_TIME_ATTACK;

		}
		SQLiteDatabase db = (new HighScoreOpenHelper(this))
				.getReadableDatabase();

		Cursor cursor = db.query(table, null, null, null, null, null,
				"score desc");

		String scoreString = "";
		int i = 0;
		while (cursor.moveToNext()) {
			i++;
			int score = cursor.getInt(2);
			scoreString = scoreString
					+ String.format("\n%03d. %20s  %07d", i,
							cursor.getString(1), score);
		}
		scoreText.setText(scoreString);
		cursor.close();
		db.close();

	}

}

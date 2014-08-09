package com.fundora.hangman.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HighScoreOpenHelper extends SQLiteOpenHelper {
	 private static final int DATABASE_VERSION = 2;
	 private static final String DATABASE_NAME = " hangman_score ";
	 public static final String EASY_TABLE_NAME = " easy ";
	 public static final String MEDIUM_TABLE_NAME = " medium ";
	 public static final String HARD_TABLE_NAME = " hard ";
	 public static final String EASY_TABLE_NAME_TIME_ATTACK = " easy_time_attack ";
	 public static final String MEDIUM_TABLE_NAME_TIME_ATTACK = " medium_time_attack ";
	 public static final String HARD_TABLE_NAME_TIMEATTACK = " hard_time_attack ";
	              
	 public static final int MAX_ENTRIES = 100;
	 
	public HighScoreOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	private void createScoreTable(String tableName,SQLiteDatabase db){
		db.execSQL("CREATE TABLE " + tableName + " (" +
                "id integer  primary key AUTOINCREMENT, " +
                "name varchar(20), "+
                "score long );");
	}
	
	public void resetTable(String tableName,int score,SQLiteDatabase db){
		db.delete(tableName, null, null);
		for (int i=0;i<10;i++)
			db.execSQL("INSERT INTO "+tableName+" VALUES(?,'Anon',"+score+")");
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		createScoreTable(EASY_TABLE_NAME, db);
		createScoreTable(MEDIUM_TABLE_NAME, db);
		createScoreTable(HARD_TABLE_NAME, db);
		createScoreTable(EASY_TABLE_NAME_TIME_ATTACK, db);
		createScoreTable(MEDIUM_TABLE_NAME_TIME_ATTACK, db);
		createScoreTable(HARD_TABLE_NAME_TIMEATTACK, db);
		resetTable(EASY_TABLE_NAME,240, db);
		resetTable(HARD_TABLE_NAME,380, db);
		resetTable(MEDIUM_TABLE_NAME,300, db);
		resetTable(EASY_TABLE_NAME_TIME_ATTACK,240, db);
		resetTable(HARD_TABLE_NAME_TIMEATTACK,380, db);
		resetTable(MEDIUM_TABLE_NAME_TIME_ATTACK,300, db);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
	}

}

package com.kth.ppomppu.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PpomppuDbHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "ppomppu.db";
	public static final String DATABASE_TABLE_REALTIME = "realtime_table";
	public static final String DATABASE_TABLE_SHOPING = "shoping_table";

	public static final String ID = "_id";
	public static final String RANKING_1 = "ranking_1";
	public static final String RANKING_2 = "ranking_2";
	public static final String RANKING_3 = "ranking_3";
	public static final String RANKING_4 = "ranking_4";
	public static final String RANKING_5 = "ranking_5";

	private static final String DATABASE_CREATE_REALTIME_TABLE = "create table "
			+ DATABASE_TABLE_REALTIME
			+ "( "
			+ ID
			+ " integer primary key autoincrement  , "
			+ RANKING_1
			+ " text not null, "
			+ RANKING_2
			+ " text not null, "
			+ RANKING_3
			+ " text not null, "
			+ RANKING_4
			+ " text not null, "
			+ RANKING_5
			+ " text not null " + " );";

	private static final String DATABASE_CREATE_SHOPING_TABLE = "create table "
			+ DATABASE_TABLE_SHOPING + "( " + ID + " text not null" + " );";

	private static int mVersion = 1;

	public PpomppuDbHelper(Context context, String aDataBaseName) {
		super(context, aDataBaseName, null, mVersion);
		// TODO Auto-generated constructor stub

	}

	// 데이터베이스 생성 및 테이블 생성.
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(DATABASE_CREATE_REALTIME_TABLE);
		db.execSQL(DATABASE_CREATE_SHOPING_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_REALTIME);
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_SHOPING);

		onCreate(db);
	}

}

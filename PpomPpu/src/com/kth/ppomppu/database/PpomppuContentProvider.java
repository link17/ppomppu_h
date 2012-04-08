package com.kth.ppomppu.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class PpomppuContentProvider extends ContentProvider {

	private PpomppuDbHelper mPpomppuDbHelper;
	private SQLiteDatabase mSqLiteDatabase;

	public static final String AUTHORITY = "com.kth.ppomppu.povider";
	public static final String REATIME_PATH = PpomppuDbHelper.DATABASE_TABLE_REALTIME;
	public static final String SHOPING_PATH = PpomppuDbHelper.DATABASE_TABLE_SHOPING;

	public static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);
	public static final Uri REALTIME_URI = Uri.withAppendedPath(BASE_URI,
			REATIME_PATH);
	public static final Uri SHOPING_URI = Uri.withAppendedPath(BASE_URI,
			SHOPING_PATH);

	@Override
	public boolean onCreate() {

		mPpomppuDbHelper = new PpomppuDbHelper(getContext(),PpomppuDbHelper.DATABASE_NAME);

		return (mPpomppuDbHelper == null) ? false : true;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {

		mSqLiteDatabase = mPpomppuDbHelper.getWritableDatabase();

		SqlArguments args = new SqlArguments(uri,selection,selectionArgs);
		
		mSqLiteDatabase = mPpomppuDbHelper.getReadableDatabase();
		
		int count= mSqLiteDatabase.delete(args.table, args.where, args.args);
		
		getContext().getContentResolver().notifyChange(uri, null);
		
		return count;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {

		mSqLiteDatabase = mPpomppuDbHelper.getWritableDatabase();

		SqlArguments args = new SqlArguments(uri);

		mSqLiteDatabase.insert(args.table, null, values);
		Log.d("TAG", "insert");
		
		//if(rowid <=0 )
		//	uri = ContentUris.withAppendedId(uri, rowid);
		
		getContext().getContentResolver().notifyChange(uri, null);
		
		return uri;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		mSqLiteDatabase = mPpomppuDbHelper.getWritableDatabase();

		SqlArguments args = new SqlArguments(uri,selection,selectionArgs);

		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(args.table);
		
		mSqLiteDatabase = mPpomppuDbHelper.getReadableDatabase();
		Cursor cursorResult = qb.query(mSqLiteDatabase, projection, selection, selectionArgs, null, null, sortOrder);
		
		
		
		return cursorResult;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {

		mSqLiteDatabase = mPpomppuDbHelper.getWritableDatabase();
		
		SqlArguments args = new SqlArguments(uri,selection,selectionArgs);
		
		int count = 0;
		
		count = mSqLiteDatabase.update(args.table, values, args.where, args.args);
		getContext().getContentResolver().notifyChange(uri, null);
		
		return count;
	}

}

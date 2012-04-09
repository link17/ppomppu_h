package com.kth.ppomppu;

import java.util.ArrayList;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.kth.ppomppu.appwidget.PpomppuAppWidget;
import com.kth.ppomppu.database.PpomppuContentProvider;
import com.kth.ppomppu.database.PpomppuDbHelper;
import com.kth.ppomppu.util.NaverParser;
import com.kth.ppomppu.util.XmlData;

public class RealTimeThread {

	private Context mContext;
	private Uri mUri;

	public RealTimeThread(Context aContext) {
		mContext = aContext;
	}

	public void startSearch() {

		
		// DataStore.getInstance().saveData(data);

		searchRank();

		// ContentValues values = new ContentValues();
		//
		// values.put(PpomppuDbHelper.ID, 0);
		// values.put(PpomppuDbHelper.RANKING, data);
		//

	}

	private void searchRank() {
		ArrayList<XmlData> arr = NaverParser.GetXmlData();
		if (!isRankData())
			insertRank(arr);
		else
			updateRank(arr);

		/*if (arr.size() > 0) {
			
			Log.d("HONG", "searchRank");
			String[] arrData = new String[5];

			for (int i = 0; i < 5; i++) {
				arrData[i] = arr.get(i).d_title;
			}

			Intent intent = new Intent(
					"android.appwidget.action.APPWIDGET_UPDATE");
			// intent.putExtra(PpomppuAppWidget.RANK_KEY, arrData);
			AppWidgetManager manager = AppWidgetManager.getInstance(mContext);
			int[] appWidgetIds = new int[2];
			appWidgetIds[0] = 10101010;
			appWidgetIds[1] = 10101010;
			intent.getExtras().putIntArray(AppWidgetManager.EXTRA_APPWIDGET_IDS,appWidgetIds);
			mContext.sendBroadcast(intent);
		}
		*/
	}

	private boolean isRankData() {
		boolean result = false;

		Cursor cursor = mContext.getContentResolver().query(
				PpomppuContentProvider.REALTIME_URI, null, null, null, null);

		if (cursor != null) {
			int count = cursor.getCount();
			if (count > 0)
				result = true;

			cursor.close();
		}

		return result;
	}

	private void insertRank(ArrayList<XmlData> arr) {

		ContentValues values = new ContentValues();

		values.put(PpomppuDbHelper.ID, 0);

		for (int i = 0; i < arr.size(); i++) {

			XmlData xmlData = arr.get(i);

			String titleRanking = getRanking(i);
			values.put(titleRanking, xmlData.d_title);

			

			if (i == 4)
				break;

		}

		mUri = mContext.getContentResolver().insert(
				PpomppuContentProvider.REALTIME_URI, values);

	}

	private void updateRank(ArrayList<XmlData> arr) {
		ContentValues values = new ContentValues();
		Log.d("HONG", "updateRank");
		for (int i = 0; i < arr.size(); i++) {

			XmlData xmlData = arr.get(i);

			String titleRanking = getRanking(i);
			values.put(titleRanking, xmlData.d_title);

			
			Log.d("HONG", "순위 "+(i+1)+"  "+xmlData.d_title);
			
			
			if (i == 4)
				break;

		}

		if (values.size() > 0) {

			mContext.getContentResolver().update(
					PpomppuContentProvider.REALTIME_URI, values,
					PpomppuDbHelper.ID + "=0", null);
		}
	}

	private String getRanking(int key) {

		String result = "";

		switch (key) {
		case 0:
			result = PpomppuDbHelper.RANKING_1;
			break;

		case 1:
			result = PpomppuDbHelper.RANKING_2;
			break;

		case 2:
			result = PpomppuDbHelper.RANKING_3;
			break;

		case 3:
			result = PpomppuDbHelper.RANKING_4;
			break;

		case 4:
			result = PpomppuDbHelper.RANKING_5;
			break;

		default:
			break;
		}

		return result;
	}

}

package com.kth.ppomppu.appwidget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.kth.ppomppu.R;
import com.kth.ppomppu.database.PpomppuContentProvider;

public class PpomppuAppWidget extends AppWidgetProvider {

	private String mRank_1 = "";
	public static String APPWIDGET_ACION = "com.kth.appwidget.action.UPDATE_RANK";
	public static String RANK_KEY = "rank_key";
	private int[] mAppId;

	public PpomppuAppWidget() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onEnabled(Context context) {
		// TODO Auto-generated method stub

		PackageManager pm = context.getPackageManager();
		pm.setComponentEnabledSetting(new ComponentName(
				"com.kth.ppomppu.appwidget", ".PpomppuAppWidget"),
				PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
				PackageManager.DONT_KILL_APP);

		// super.onEnabled(context);

	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		super.onReceive(context, intent);

		String action = intent.getAction();
		if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(action)) {
			Bundle extras = intent.getExtras();
			if (extras != null) {
				int[] appWidgetIds = extras
						.getIntArray(AppWidgetManager.EXTRA_APPWIDGET_IDS);
				if (appWidgetIds != null && appWidgetIds.length > 0) {

					// Bundle bnd=intent.getExtras();
					// String[] arr=bnd.getStringArray(RANK_KEY);
					// if(arr.length > 0)
					// {
					// mRank_1=arr[0];
					// // this.onUpdate(context,
					// // AppWidgetManager.getInstance(context), appWidgetIds);
					// }

					this.onUpdate(context,
							AppWidgetManager.getInstance(context), appWidgetIds);
				}
			}
		} else if (action.equals(APPWIDGET_ACION)) {
			Bundle bnd = intent.getExtras();
			String[] arr = bnd.getStringArray(RANK_KEY);
			if (arr.length > 0) {
				mRank_1 = arr[0];

				AppWidgetManager gm = AppWidgetManager.getInstance(context);
				int[] ids = gm.getAppWidgetIds(new ComponentName(
						"com.kth.ppomppu.appwidget", ".PpomppuAppWidget"));

				Log.d("HONG", "onReceive = " + ids);
				this.onUpdate(context, gm, ids);

				// this.onUpdate(context,
				// AppWidgetManager.getInstance(context), appWidgetIds);
			}
		}

	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		Log.d("HONG", "onUpdate  " + mAppId);
		for (int i = 0; i < appWidgetIds.length; i++) {
			int appWidgetId = appWidgetIds[i];

			mAppId = appWidgetIds;

			RemoteViews views = new RemoteViews(context.getPackageName(),
					R.layout.appwidget_layout);

			Cursor cursor = context.getContentResolver()
					.query(PpomppuContentProvider.REALTIME_URI, null, null,
							null, null);
			String[] rankArr = new String[5];
			String ranking = "";

			if (cursor != null && cursor.moveToFirst()) {

				int count = 0;

				

				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
					if (count == 5) // 1~5위 까지
						break;
					for (int j = 0; j < 5; j++) {
						ranking = cursor.getString(j + 1);

						rankArr[j] = ranking;
						Log.d("TAG", "title = " + ranking);
					}
					count++;

					cursor.moveToNext();
				}

				cursor.close();
				//sendMessage(rankArr);
			}

			views.setTextViewText(R.id.tv_appwidget_1, rankArr[i]);
			appWidgetManager.updateAppWidget(appWidgetId, views);
		}

		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}
}

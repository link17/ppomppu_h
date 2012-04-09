package com.kth.ppomppu;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.kth.ppomppu.database.PpomppuContentProvider;

/**
 * 
 * @author link17 (홍거)
 * @deprecated
 *  - 뽐뿌 DB에 값이 변경될 때 onChange 메소드가 호출된다.
 *  - context가 있는 객체에서는 모두 등록이 가능하다.
 *  - mMode를 통해서 어떤 테이블에 값이 변경되었는지 구별한다.
 *  
 *
 */
public class PContentObserver extends ContentObserver {

	Handler mHandler;
	private Context mContext;
	private int mMode = 0;
	public static final int REALTIME_OBSERVER = 0;
	public static final int SHOPING_OBSERVER = 1;

	public PContentObserver(Handler handler, Context aContext, int aMode) {
		super(handler);

		mHandler = handler;
		mContext = aContext;

		mMode = aMode;

	}

	@Override
	public void onChange(boolean selfChange) {
		// TODO Auto-generated method stub
		super.onChange(selfChange);

		switch (mMode) {
		case REALTIME_OBSERVER:

			realtimeFun();
			break;

		case SHOPING_OBSERVER:

			shopingFun();
			break;

		default:
			break;
		}

	}

	private void sendMessage(String[] ranking) {
		Message msg = new Message();
		Bundle bnd = new Bundle();
		bnd.putStringArray("data", ranking);
		msg.setData(bnd);

		mHandler.sendMessage(msg);
	}

	private void realtimeFun() {
		Cursor cursor = mContext.getContentResolver().query(
				PpomppuContentProvider.REALTIME_URI, null, null, null, null);

		String ranking = "";
		Log.d("HONG", "realtimeFun");

		if (cursor != null && cursor.moveToFirst()) {

			int count = 0;

			String[] rankArr = new String[5];

			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				if (count == 5) // 1~5위 까지
					break;
				for (int i = 0; i < 5; i++) {
					ranking = cursor.getString(i+1);
					
					rankArr[i] = ranking;
				}
				count++;
				
				cursor.moveToNext();
			}

			cursor.close();
			sendMessage(rankArr);
		}
	}

	private void shopingFun() {

	}
}

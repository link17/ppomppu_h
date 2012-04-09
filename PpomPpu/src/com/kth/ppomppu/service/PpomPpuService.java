package com.kth.ppomppu.service;

import com.kth.ppomppu.RealTimeThread;

import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
/**
 * 
 * @author link17 (홍거)
 * @deprecated
 *  - Activity 에 의존된 쓰레드가 아닌 화면과 별개에 백단 작업을 위한 클래스.
 *  - 두개의 핸들러 쓰레드 (mRealtimeThread,mShopingThread)가 돌고 있다.
 *  - 현재 바인더 객체는 사용되지 않고 있다.
 *  - 액티비티가 실행해주고 알람매니저를 통해 3초에 한번씩 불리고 있지만 이형태가 아닌 TimerTask를 이용하여 자체적으로 이용하는 게 효율적으로 생각됨.
 *  - 각각의 쓰레드에서 급상승어 검색과 쇼핑검색어를 넣어주어 검색을 해주는 형태이다.
 *  
 *
 */
public class PpomPpuService extends Service {

	public static String REALTIME_ACTION = "com.kth.realtime.action";
	public static String SHOPING_ACTION = "com.kth.shoping.action";
	public static String SEARCH_KEY = "search_key";

	private final IBinder mBinder = new LocalBinder();
	private HandlerThread mRealtimeThread;
	private HandlerThread mShopingThread;

	private Handler mRealTimeHandler = null;
	private Handler mShopingHandler = null;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		mRealtimeThread = new HandlerThread("realtime"); // 실시간 급상승
		mShopingThread = new HandlerThread("shoping"); // 쇼핑 검색

		mRealtimeThread.start();
		mShopingThread.start();

		mRealTimeHandler = new Handler(mRealtimeThread.getLooper()) {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);

				RealTimeThread rt = new RealTimeThread(getBaseContext());
				rt.startSearch();
			}
		};

		mShopingHandler = new Handler(mShopingThread.getLooper()) {
			@Override
			public void handleMessage(Message msg) {

				super.handleMessage(msg);

				Bundle bnd = msg.getData();

				String searchKey = bnd.getString(SEARCH_KEY);

				Log.d("TAG", "handleMessage searchKey= " + searchKey);
			}
		};
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		actionFun(intent);

		return mBinder;
	}

	@Override
	public void unbindService(ServiceConnection conn) {
		// TODO Auto-generated method stub
		super.unbindService(conn);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub

		actionFun(intent);

		super.onStart(intent, startId);

	}

	private void actionFun(Intent intent) {

		Log.d("HONG", "actionFun");
		
		if (intent != null) {
			String action = intent.getAction();

			if (action != null) {
				if (action.equals(REALTIME_ACTION)) {
					//
					if (mRealTimeHandler != null) {
						Message msg = new Message();
						mRealTimeHandler.sendMessage(msg);
					}

				} else if (action.equals(SHOPING_ACTION)) {

					if (mShopingHandler != null) {
						String searchKey = intent.getStringExtra(SEARCH_KEY);

						Message msg = new Message();
						Bundle bnd = new Bundle();
						bnd.putString(SEARCH_KEY, searchKey);
						msg.setData(bnd);

						mShopingHandler.sendMessage(msg);
					}
				}
			}
		}
	}

	public class LocalBinder extends Binder {
		void searchPriceMin(String aSearchText, Handler handler) {

			Message msg = new Message();
			handler.sendMessage(msg);

		}
	}

}

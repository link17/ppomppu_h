
package com.kth.ppomppu.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kth.ppomppu.PContentObserver;
import com.kth.ppomppu.R;
import com.kth.ppomppu.database.PpomppuContentProvider;
import com.kth.ppomppu.service.PpomPpuService;
import com.kth.ppomppu.util.Constants;

public class PpomPpuActivity extends Activity implements Constants {

    // View
    private EditText mEtSearchKeyword;
    private TextView mTvRTSearchRanking;
    private ListView mLvGoodsSearchResult;
    
    private PendingIntent mPendingIntent;

    private AlarmManager mAlarmManager;

    private PContentObserver mRealTimeContentObserver;
    private PContentObserver mShopingContentObserver;
    
    private String[] mRankArr = new String[5];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        settingRealtimeAlarm();

        mEtSearchKeyword = (EditText) findViewById(R.id.et_search_keyword);

        Button btn = (Button) findViewById(R.id.bt_search_start);
        btn.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub

                String searchKey = mEtSearchKeyword.getText().toString();

                if (searchKey.trim().length() > 0) {
                    settingShopingService(searchKey);
                } else {
                    Toast.makeText(PpomPpuActivity.this, TOAST_WARNING_NO_SEARCH_KEYWORD, 
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

		// 데이터 베이스 옵저버 등
		regObserver();

		mTvRTSearchRanking = (TextView) findViewById(R.id.tv_real_time_search_rank_result);
		mLvGoodsSearchResult = (ListView) findViewById(R.id.lv_goods_search_result);
    }
	
	private void regObserver(){
		 //실시간 급상승어 옵저버 등록.
		mRealTimeContentObserver = new PContentObserver(mHandler,this,PContentObserver.REALTIME_OBSERVER);
		getContentResolver().registerContentObserver(PpomppuContentProvider.REALTIME_URI, true, mRealTimeContentObserver);
		
		//쇼핑 검색 옵저버 등록.
		mShopingContentObserver = new PContentObserver(mHandler,this,PContentObserver.SHOPING_OBSERVER);
		getContentResolver().registerContentObserver(PpomppuContentProvider.SHOPING_URI, true, mShopingContentObserver);
		
	}

    private void settingRealtimeAlarm() {

    	// 알람매니저 얻어옴.
        mAlarmManager = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);

        // 뽐뿌서비스 인텐트 생성.
        Intent intent = new Intent(this, PpomPpuService.class);
        // 실시간 급상승어 구동 액션 설정.
        intent.setAction(PpomPpuService.REALTIME_ACTION);
        
        // 팬딩 인텐트 생성 
        mPendingIntent = PendingIntent.getService(this, 200, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        
        // 알람 매니저에 팬딩인텐트 설정. 간격 3초.
        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, 0, 3000, mPendingIntent);
    }
    
    private void settingShopingService(String aSearchKey) {
        Intent intent = new Intent(this, PpomPpuService.class);
        intent.setAction(PpomPpuService.SHOPING_ACTION);
        intent.putExtra(PpomPpuService.SEARCH_KEY, aSearchKey);

        startService(intent);
    }

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		//mAlarmManager.cancel(mPendingIntent);
		getContentResolver().unregisterContentObserver(mRealTimeContentObserver);
		getContentResolver().unregisterContentObserver(mShopingContentObserver);
	}
	
	// 컨텐트 옵저버를 통해서 DB가 변경시 값을 받아온다.
	private Handler mHandler = new Handler(){
	
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			
			
			Bundle bnd = msg.getData();
			String[] dataArr = bnd.getStringArray("data");
			mRankArr= dataArr;
			mTvRTSearchRanking.setText("[1]  "+mRankArr[0]);
			//Log.d("HONG"," data = "+mRankArr[0]);
		}
	};
}

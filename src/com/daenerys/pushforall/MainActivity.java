package com.daenerys.pushforall;

import com.igexin.slavesdk.MessageManager;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// (�Ÿ�)����logcat���������debug������ʱ��ر�
		XGPushConfig.enableDebug(this, true);
		XGPushManager.registerPush(this);
		//(����)
		MessageManager.getInstance().initialize(this.getApplicationContext());
		
		
		
		
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

package com.shiwen.multipush;

import static com.shiwen.multipush.CommonUtilities.EXTRA_MESSAGE;
import static com.shiwen.multipush.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.shiwen.multipush.CommonUtilities.USE_LOCATION;

import com.shiwen.multipush.R;
import com.shiwen.multipush.gcm.GCMRegistrar;
import com.igexin.slavesdk.MessageManager;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;

import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends AirBopActivity {
	
	final private String TAG = "MainActivity";
	/** gcm project id **/
	final private String GCM_SENDER_ID = "62722777599";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// (信鸽)开启logcat输出，方便debug，发布时请关闭
		XGPushConfig.enableDebug(this, false);
		XGPushManager.registerPush(this);
		//(个推)
		MessageManager.getInstance().initialize(this.getApplicationContext());
		
		// gcm
		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);
        String regId = GCMRegistrar.getRegistrationId(this);
        if (regId.equals("")) {
            Log.v(TAG, "GCM to register");
            GCMRegistrar.register(this, GCM_SENDER_ID);
            Log.v(TAG,"gcm regId:" + regId);
        }
        else {//GCMRegistrar.unregister(this);
            Log.v(TAG, "GCM RegId:" +  regId);
        }
        //airbop
        registerReceiver(mHandleMessageReceiver,
                new IntentFilter(DISPLAY_MESSAGE_ACTION));
       
        // Call the register function in the AirBopActivity 
        register(USE_LOCATION);  
        
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
    @Override
    protected void onDestroy() {
        
        unregisterReceiver(mHandleMessageReceiver);
        super.onDestroy();
    }

    
    private final BroadcastReceiver mHandleMessageReceiver =
            new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
        }
    };

}

package com.shiwen.multipush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.igexin.sdk.Consts;

public class GexinReceiver extends BroadcastReceiver {

	private final String TAG = "GexinDemoReceiver";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		Log.d(TAG, "onReceive() action=" + bundle.getInt("action"));
		
		switch (bundle.getInt(Consts.CMD_ACTION)) {

		case Consts.GET_MSG_DATA:
			// 鑾峰彇閫忎紶鏁版嵁
			// String appid = bundle.getString("appid");
			byte[] payload = bundle.getByteArray("payload");

			if (payload != null) {
				String data = new String(payload);
				Log.i(TAG, "Get data:"+data);
				
				// turn to the bitmap download
				Bundle dataBd = new Bundle();
				dataBd.putString("imageUrl", data);
				Message msg = new Message();
				msg.what = 0;
				msg.setData(dataBd);
				// MainActivity.handler.sendMessage(msg);
				
				Toast.makeText(context, data, Toast.LENGTH_SHORT).show();
			}
			break;
		case Consts.GET_CLIENTID:
			// 获取ClientID(CID)
			String cid = bundle.getString("clientid");
			Log.d("GexinSdkDemo", "Got ClientID:" + cid);
			/* 第三方应用需要将ClientID上传到第三方服务器，并且将当前用户帐号和ClientID进行关联，以便以后通过用户帐号查找ClientID进行消息推送
			有些情况下ClientID可能会发生变化，为保证获取最新的ClientID，请应用程序在每次获取ClientID广播后，都能进行一次关联绑定 */
			break;
		default:
			break;
		}
	}

}

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
			// 获取透传数据
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
			// ��ȡClientID(CID)
			String cid = bundle.getString("clientid");
			Log.d("GexinSdkDemo", "Got ClientID:" + cid);
			/* ������Ӧ����Ҫ��ClientID�ϴ��������������������ҽ���ǰ�û��ʺź�ClientID���й������Ա��Ժ�ͨ���û��ʺŲ���ClientID������Ϣ����
			��Щ�����ClientID���ܻᷢ���仯��Ϊ��֤��ȡ���µ�ClientID����Ӧ�ó�����ÿ�λ�ȡClientID�㲥�󣬶��ܽ���һ�ι����� */
			break;
		default:
			break;
		}
	}

}

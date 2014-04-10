package com.shiwen.multipush;

import java.util.Random;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gcm.GCMRegistrar;
import com.shiwen.multipush.R;
import com.shiwen.multipush.R.drawable;
import com.shiwen.multipush.gcm.GCMBaseIntentService;
import static com.shiwen.multipush.CommonUtilities.displayMessage;
public class GCMIntentService extends GCMBaseIntentService
{
    final private String TAG = "GCMIntentService";
    
    @Override
    protected void onError(Context context, String errorId)
    {
        Log.d(TAG, "onError: " + errorId);
        notification(context, "onError", errorId);
    }

    /**
     * get message and handle it
     */
    @Override
    protected void onMessage(Context context, Intent intent)
    {
    	
        String messageOriginal = intent.getStringExtra("msg");
        Log.d(TAG, "onMessage: " + messageOriginal);
        notification(context, "onMessage", messageOriginal);
        
        //the following are airbop messages--larry
        Log.i(TAG, "Received message");
        displayMessage(context, "Message Received" );
        String message = null;
        String title = null;
        String url = null;
        
        if (intent != null) {
        	
        	//Check the bundle for the pay load body and title
	        Bundle bundle = intent.getExtras();
	 	   	if (bundle != null) {
	 	   		displayMessage(context, "Message bundle: " +  bundle);
	 	   		
	 	   		Log.i(TAG, "Message bundle: " +  bundle);
	 	   		message = bundle.getString("message");
	 	   			 	   		
	 	   		title = bundle.getString("title");
	 	   		
	 	   		url = bundle.getString("url");
	 	   	} 
        }
 	   	// If there was no body just use a standard message
 	   	if (message == null) {
 	   		message = getString(R.string.airbop_message);
   		}
 	   	   		
 	   	generateNotification(context, title, message, url); 
        
    }
    
    @Override
    protected void onRegistered(Context context, String registrationId)
    {
        Log.d(TAG, "onRegistered: " + registrationId);
        notification(context, "onRegistered", registrationId);
//        registerWithAirBop(registrationId);
        displayMessage(context, getString(R.string.gcm_registered));
        //Get our data for the server
        AirBopServerUtilities server_data = AirBopServerUtilities.fillDefaults(registrationId);
        //server_data.loadCurrentLocation(context);
        server_data.loadDataFromPrefs(context);
        // Get rid of the location from the prefs so we requery next time
        AirBopServerUtilities.clearLocationPrefs(context);
        AirBopServerUtilities.register(getApplicationContext()
    			, server_data);
    }

    @Override
    protected void onUnregistered(Context context, String registrationId)
    {
        Log.d(TAG, "onUnregistered: " + registrationId);
        notification(context, "onUnregistered", registrationId);
        
        displayMessage(context, getString(R.string.gcm_unregistered));
        //If we are still registered with AirBop it is time to unregister
        if (GCMRegistrar.isRegisteredOnServer(context)) {
        	AirBopServerUtilities.unregister(context, registrationId);
        } else {
            // This callback results from the call to unregister made on
            // ServerUtilities when the registration to the server failed.
        	Log.i(TAG, "Ignoring unregister callback");
        	
        }
    }
    
    /**
     * show meesage in notification
     * @param iMessage the message to show
     */
    @SuppressWarnings("deprecation")
    protected void notification(Context iContext, String iTitle, String iMessage)
    {
        NotificationManager notificationManager = (NotificationManager) iContext.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(iContext, MainActivity.class);

        int requestCode, notificationId;
        String contentTitle, contentContent;

        final Random random = new Random(System.currentTimeMillis());
        requestCode = random.nextInt();
        notificationId = random.nextInt();
        contentTitle = iTitle;
        contentContent = iMessage;
        
        // Notification
        Notification notification = new Notification();
        notification.icon = R.drawable.ic_launcher;
        notification.tickerText = iTitle;
        notification.when = System.currentTimeMillis();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_LIGHTS;
        notification.defaults |= Notification.DEFAULT_SOUND;
        
        PendingIntent contentIntent = PendingIntent.getActivity(iContext, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setLatestEventInfo(iContext, contentTitle, contentContent, contentIntent);
        notificationManager.notify(notificationId, notification);
    }
	
    //the following are airbop generating notification functions--larry
    
    /**
     * Issues a notification to inform the user that server has sent a message.
     */
    private static void generateNotification(Context context
    		, String title
    		, String message) {
        int icon = R.drawable.ic_launcher;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        
        if ((title == null) || (title.equals(""))) {
        	title = context.getString(R.string.app_name);
        }
        Intent notificationIntent = new Intent(context, MainActivity.class);
        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent =
                PendingIntent.getActivity(context, 0, notificationIntent, 0);
                
        Notification notification = new NotificationCompat.Builder(context)
	        .setContentTitle(title)
	        .setContentText(message)
	        .setContentIntent(intent)
	        .setSmallIcon(icon)
	        .setWhen(when)
	        .setStyle(new NotificationCompat.BigTextStyle()
	        	.bigText(message))
	    .build();
        
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(0, notification);
    }
    
    private static void generateNotification(Context context
    		, String title
    		, String message
    		, String url) {
        int icon = R.drawable.ic_launcher;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        
        if ((title == null) || (title.equals(""))) {
        	title = context.getString(R.string.app_name);
        }
        
        Intent notificationIntent = null;
        if ((url == null) || (url.equals(""))) {
        	//just bring up the app
        	notificationIntent = new Intent(context, MainActivity.class);
        } else {
        	//Launch the URL
        	notificationIntent = new Intent(Intent.ACTION_VIEW);
            notificationIntent.setData(Uri.parse(url));
            notificationIntent.addCategory(Intent.CATEGORY_BROWSABLE);
        }
        
        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent =
                PendingIntent.getActivity(context, 0, notificationIntent, 0);
                
        Notification notification = new NotificationCompat.Builder(context)
		        .setContentTitle(title)
		        .setContentText(message)
		        .setContentIntent(intent)
		        .setSmallIcon(icon)
		        .setWhen(when)
		        .setStyle(new NotificationCompat.BigTextStyle()
	            	.bigText(message))
	        .build();
        
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(0, notification);
    }
	
}

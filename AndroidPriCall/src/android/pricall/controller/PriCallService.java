/**
 * 
 */
package android.pricall.controller;

import java.util.ArrayList;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;
import android.pricall.model.PriorityList;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * @author Corne
 *
 */
public class PriCallService extends Service {

	private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	
	private StateListener phoneStateListener;
	private SmsListener smsListener;
	private AudioManager audioManager;
	
	private NotificationManager notificationManager;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.i("SERVICE", "Service created");
		audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		
		phoneStateListener = new StateListener();
		smsListener = new SmsListener();
		
		//let the service listen to incoming calls
        TelephonyManager telephonymanager = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        telephonymanager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        //let the service listen to incoming SMS
        IntentFilter filter = new IntentFilter();
        filter.addAction(SMS_RECEIVED);
        registerReceiver(smsListener, filter);
        
        
        //TODO create separate class for notification handling
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int icon = android.R.drawable.ic_dialog_info;
        String notificationMessage = "Pri Call Activated";

        Notification notification = new Notification(icon, notificationMessage, System.currentTimeMillis());
        Context context = getApplicationContext();
        CharSequence contentTitle = "My notification";
        CharSequence contentText = "Hello World!";
        Intent notificationIntent = new Intent(this, SettingsActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
        notificationManager.notify(1, notification);
        
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		//stop listen to incoming calls
		TelephonyManager telephonymanager = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        telephonymanager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
    	audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
		Log.i("SERVICE", "Service destroyed");
		//stop listen to incoming SMS
		unregisterReceiver(smsListener);
		
		//remove notification
		notificationManager.cancel(1);
	}	
	
	
	private class StateListener extends PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            
            switch(state){
                case TelephonyManager.CALL_STATE_RINGING:
                	Log.i("SERVICE", "got called");
                	if(!callerIsPriCaller(incomingNumber)){
                		audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                	}
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:                	
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                	audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                    break;
            }
        }
        
    };
    
    private class SmsListener extends BroadcastReceiver {
    	
    	@Override
    	public void onReceive(Context context, Intent intent) {
    		AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    		Log.i("SMSListener", "intent action" + intent.getAction());
    		if(intent.getAction().equals("SMS_RECEIVED")){
    			Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
                SmsMessage[] msgs = null;
                String msg_from;
                
                if(bundle != null){
                	Object[] pdus = (Object[]) bundle.get("pdus");
                	msgs = new SmsMessage[pdus.length];
                	for(int i=0; i<msgs.length; i++){
                        msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        //String msgBody = msgs[i].getMessageBody();
                        Log.i("SMSListener", "incoming message:" + msg_from);
                        audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                    }
                }
    		}
    	}
    }

    private boolean callerIsPriCaller(String incomingNumber){
    	PriorityList priListInstance = PriorityList.getInstance(this);
    	ArrayList<String> priListPhoneNumbers = priListInstance.getPriorityPhoneNumbers();
    	
    	if(priListPhoneNumbers.contains(incomingNumber)){
    		return true;
    	}
    	return false;
    }

}

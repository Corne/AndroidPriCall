/**
 * 
 */
package android.pricall.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * @author Corne
 *
 */
public class SmsListener extends BroadcastReceiver {

	
	@Override
	public void onReceive(Context context, Intent intent) {
		AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		Log.i("SMSListener", "intent action" + intent.getAction());
		if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
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

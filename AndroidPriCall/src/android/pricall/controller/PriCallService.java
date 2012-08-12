/**
 * 
 */
package android.pricall.controller;

import java.util.ArrayList;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.IBinder;
import android.pricall.model.PriorityList;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * @author Corne
 *
 */
public class PriCallService extends Service {

	private StateListener phoneStateListener;
	private AudioManager audioManager;
	
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
        TelephonyManager telephonymanager = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        telephonymanager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		TelephonyManager telephonymanager = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        telephonymanager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
    	audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
		Log.i("SERVICE", "Service destroyed");
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

    private boolean callerIsPriCaller(String incomingNumber){
    	PriorityList priListInstance = PriorityList.getInstance(this);
    	ArrayList<String> priListPhoneNumbers = priListInstance.getPriorityPhoneNumbers();
    	
    	if(priListPhoneNumbers.contains(incomingNumber)){
    		return true;
    	}
    	return false;
    }

}

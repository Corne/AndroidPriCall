/**
 * 
 */
package android.pricall.controller;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.pricall.R;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TimePicker;
import android.widget.ToggleButton;

/**
 * @author Corne
 *
 */
public class TimerSettingsActivity extends Activity implements OnClickListener {

	//TODO create viewholder for timepicker + button,
	// and an arraylist that contains all created timers
	private ToggleButton timerStatusToggleButton;
	private TimePicker timerTimePicker;
	
	private PendingIntent pendingIntent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timersettingsview);
		
		timerStatusToggleButton = (ToggleButton) findViewById(R.id.timerStatusToggleButton);
		timerTimePicker = (TimePicker) findViewById(R.id.timerTimePicker);
		timerTimePicker.setIs24HourView(true);
		timerStatusToggleButton.setOnClickListener(this);
	}

	public void onClick(View v) {
		if(v.getId() == timerStatusToggleButton.getId()){
			startStopTimerService();
		}
		
	}
	
	private void startStopTimerService(){
		Intent serviceIntent = new Intent(this, PriCallService.class);
		pendingIntent = PendingIntent.getService(this, 0, serviceIntent, 0);
		
		AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
		
		if(timerStatusToggleButton.isChecked()){
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(System.currentTimeMillis());
			calendar.set(Calendar.HOUR_OF_DAY, timerTimePicker.getCurrentHour());
			calendar.set(Calendar.MINUTE, timerTimePicker.getCurrentMinute());

			alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
		}else{
			alarmManager.cancel(pendingIntent);			
		}
	}
}

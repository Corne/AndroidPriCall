/**
 * 
 */
package android.pricall.controller;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.pricall.R;
import android.pricall.view.TimerButton;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.ToggleButton;

/**
 * @author Corne
 *
 */
public class TimerSettingsActivity extends Activity implements OnClickListener {

	private ToggleButton timerStatusToggleButton;
	private TimerButton startTimeButton, endTimeButton;	
	private PriCallTimePickerListenter startButtonListener, endButtonListener;
	
	private PendingIntent pendingIntent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timersettingsview);
		
		timerStatusToggleButton = (ToggleButton) findViewById(R.id.timerStatusToggleButton);
		startTimeButton = (TimerButton) findViewById(R.id.starttimebutton);
		endTimeButton = (TimerButton) findViewById(R.id.endtimebutton);
		
		timerStatusToggleButton.setOnClickListener(this);
		startTimeButton.setOnClickListener(this);
		endTimeButton.setOnClickListener(this);
		
		startTimeButton.setTitle("Start time");
		endTimeButton.setTitle("End time");
		startButtonListener = new PriCallTimePickerListenter(startTimeButton);
		endButtonListener = new PriCallTimePickerListenter(endTimeButton);
	}

	public void onClick(View v) {
		if(v.getId() == timerStatusToggleButton.getId()){
			startStopTimerService();
		}else if(v.getId() == startTimeButton.getId() || v.getId() == endTimeButton.getId()){
			setTimer((TimerButton)v);
		}
		
	}
	
	private void startStopTimerService(){
		Intent serviceIntent = new Intent(this, PriCallService.class);
		pendingIntent = PendingIntent.getService(this, 0, serviceIntent, 0);
		
		AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
		
		if(timerStatusToggleButton.isChecked()){
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(System.currentTimeMillis());
			calendar.set(Calendar.HOUR_OF_DAY, startTimeButton.getHour());
			calendar.set(Calendar.MINUTE, startTimeButton.getMinute());

			alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
		}else{
			alarmManager.cancel(pendingIntent);			
		}
	}
	
	private void setTimer(Button timerButton){
		showDialog(timerButton.getId());
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		PriCallTimePickerListenter timeListener;
		if(id == startTimeButton.getId()){
			timeListener = startButtonListener;
		}else if(id == endTimeButton.getId()){
			timeListener = endButtonListener;
		}else{
			return super.onCreateDialog(id);
		}
		Calendar calendar = Calendar.getInstance();
		return new TimePickerDialog(this, timeListener, calendar.get(Calendar.HOUR_OF_DAY),
				calendar.get(Calendar.MINUTE), true);
	}
	
	private class PriCallTimePickerListenter implements TimePickerDialog.OnTimeSetListener {

		private TimerButton timerButton;
		
		public PriCallTimePickerListenter(TimerButton timerButton) {
			super();
			this.timerButton = timerButton;
		}
		
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			timerButton.setHour(hourOfDay);
			timerButton.setMinute(minute);
			timerButton.updateText();
		}
		
		
	};
	
	
}

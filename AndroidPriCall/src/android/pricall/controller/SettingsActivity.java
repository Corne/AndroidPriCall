/**
 * 
 */
package android.pricall.controller;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.pricall.R;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ToggleButton;

/**
 * @author Corne
 *
 */
public class SettingsActivity extends Activity implements OnClickListener {

	private static final String SETTINGPREFERENCES = "SettingsPrefs";
	private static final String TOGGLEBUTTONPREFERENCE = "ToggleButtonPreference";
	
	private ToggleButton activeStatusToggleButton;
	private Button priorityContactButton;
	private Button timerSettingsButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settingsview);
		
		//get the objects from the view
		activeStatusToggleButton = (ToggleButton) findViewById(R.id.app_status_togglebutton);
		priorityContactButton = (Button) findViewById(R.id.contactlistbutton);
		timerSettingsButton = (Button) findViewById(R.id.timerSettingsButton);
		//set OnClickListener for buttons
		activeStatusToggleButton.setOnClickListener(this);
		priorityContactButton.setOnClickListener(this);
		timerSettingsButton.setOnClickListener(this);
		//set 'saved' toggleButton value
		SharedPreferences settings = getSharedPreferences(SETTINGPREFERENCES, 0);
		boolean checkedToggleButton = settings.getBoolean(TOGGLEBUTTONPREFERENCE, false);
		activeStatusToggleButton.setChecked(checkedToggleButton);
		//make sure he activates again when button is checked on startup
		this.startPriCallService();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	public void onBackPressed() {
		//make sure app doesn't close on back button pressed
		Intent setIntent = new Intent(Intent.ACTION_MAIN);
		setIntent.addCategory(Intent.CATEGORY_HOME);
		setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(setIntent);
	}
	
	public void onClick(View v) {
		if(v.getId() == activeStatusToggleButton.getId()){
			this.startPriCallService();
		}else if(v.getId() == priorityContactButton.getId()){
			this.startContactPriorityListactivity();
		}else if(v.getId() == timerSettingsButton.getId()){
			this.startTimerSettingsActivity();
		}
	}
	
	private void startPriCallService(){
		saveStatusToggleButtonPreference();		
		Intent serviceIntent = new Intent(this, PriCallService.class);
		if(activeStatusToggleButton.isChecked()){
			startService(serviceIntent);
		}else{
			stopService(serviceIntent);
		}
	}
	
	private void saveStatusToggleButtonPreference(){
		SharedPreferences settings = getSharedPreferences(SETTINGPREFERENCES, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(TOGGLEBUTTONPREFERENCE, activeStatusToggleButton.isChecked());
		editor.commit();
	}
	
	private void startContactPriorityListactivity(){
		Intent intent = new Intent(this, ContactPriorityListActivity.class);
		startActivity(intent);
	}
	
	private void startTimerSettingsActivity(){
		Intent intent = new Intent(this, TimerSettingsActivity.class);
		startActivity(intent);
	}
}

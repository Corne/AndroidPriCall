/**
 * 
 */
package android.pricall.controller;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.pricall.R;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ToggleButton;

/**
 * @author Corne
 *
 */
public class SettingsActivity extends Activity implements OnClickListener {

	private static final String TOGGLEBUTTONPREFERENCE = "ToggleButtonPreference";
	
	private ToggleButton activeStatusToggleButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settingsview);
		
		//get the objects from the view
		activeStatusToggleButton = (ToggleButton) findViewById(R.id.app_status_togglebutton);
		//set OnClickListener
		activeStatusToggleButton.setOnClickListener(this);
		//set 'saved' toggleButton value
		SharedPreferences settings = getPreferences(0);
		boolean checkedToggleButton = settings.getBoolean(TOGGLEBUTTONPREFERENCE, false);
		activeStatusToggleButton.setChecked(checkedToggleButton);
		//make sure he activates again when button is checked on startup
		this.startPriCallService();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		//Save ToggleButton value
		SharedPreferences settings = getPreferences(0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(TOGGLEBUTTONPREFERENCE, activeStatusToggleButton.isChecked());
		editor.commit();
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
		}
	}
	
	private void startPriCallService(){
		if(activeStatusToggleButton.isChecked()){
			Log.i("SERVICE", "onClick: starting service");
			startService(new Intent(this, PriCallService.class));
		}else{
			Log.i("SERVICE", "onClick: stopping service");
			stopService(new Intent(this, PriCallService.class));
		}
	}
}

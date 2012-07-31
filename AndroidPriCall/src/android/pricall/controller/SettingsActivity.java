/**
 * 
 */
package android.pricall.controller;

import android.app.Activity;
import android.content.Intent;
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

	private ToggleButton activeStatusToggleButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settingsview);
		
		activeStatusToggleButton = (ToggleButton) findViewById(R.id.app_status_togglebutton);
		activeStatusToggleButton.setOnClickListener(this);
	}

	public void onClick(View v) {
		if(v.getId() == activeStatusToggleButton.getId()){
			if(activeStatusToggleButton.isChecked()){
				Log.i("SERVICE", "onClick: starting service");
				startService(new Intent(this, PriCallService.class));
			}else{
				Log.i("SERVICE", "onClick: stopping service");
				stopService(new Intent(this, PriCallService.class));
			}
		}
	}
}

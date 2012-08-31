/**
 * 
 */
package android.pricall.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * @author Corne
 *
 */
public class TimerButton extends Button {

	private String title = "";
	private int hour = -1;
	private int minute = -1;
	
	public TimerButton(Context context) {
		super(context);
	}
	
	public TimerButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public TimerButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the hour
	 */
	public int getHour() {
		return hour;
	}

	/**
	 * @param hour the hour to set
	 */
	public void setHour(int hour) {
		this.hour = hour;
	}

	/**
	 * @return the minute
	 */
	public int getMinute() {
		return minute;
	}

	/**
	 * @param minute the minute to set
	 */
	public void setMinute(int minute) {
		this.minute = minute;
	}
	
	public void updateText(){
		setText(title + "\n" + pad(hour) + ":" + pad(minute));
	}
	
	
	private static String pad(int c) {
		if (c >= 10)
		   return String.valueOf(c);
		else
		   return "0" + String.valueOf(c);
	}
	
}

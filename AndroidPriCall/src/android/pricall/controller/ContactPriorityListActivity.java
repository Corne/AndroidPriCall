/**
 * 
 */
package android.pricall.controller;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.pricall.R;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.widget.SimpleCursorAdapter;

/**
 * @author Corne
 *
 */
public class ContactPriorityListActivity extends ListActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.prioritylistview);
		
		Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
				new String[] {Phone._ID, Phone.DISPLAY_NAME, Phone.NUMBER}, null, null,  Phone.DISPLAY_NAME + " ASC");
        //Deprecated TODO fix
		startManagingCursor(cursor);

		String[] from = new String[] { Phone.DISPLAY_NAME };

        int[] to = new int[] { R.id.contacttextview };

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.prioritycontactview, cursor, from, to);
        this.setListAdapter(adapter);

	}

}

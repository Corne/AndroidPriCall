/**
 * 
 */
package android.pricall.controller;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.pricall.R;
import android.pricall.model.Contact;
import android.pricall.model.PriorityList;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * @author Corne
 *
 */
public class ContactPriorityListActivity extends ListActivity {
	
	private ArrayList<Contact> contactList = new ArrayList<Contact>();
	
	//@SuppressWarnings("deprecation") //should use suppresswarning for newer apk?
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.prioritylistview);
		
		Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
				new String[] {Phone._ID, Phone.DISPLAY_NAME, Phone.NUMBER}, null, null,  Phone.DISPLAY_NAME + " ASC");
		
		while(cursor.moveToNext()){
			String id = cursor.getString(cursor.getColumnIndex(Phone._ID));
			String name = cursor.getString(cursor.getColumnIndex(Phone.DISPLAY_NAME));
			String phoneNumber = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
			contactList.add(new Contact(id, name, phoneNumber));
		}
		
		this.setListAdapter(new ContactAdapter(this, R.layout.prioritycontactview, contactList));
	}
	
	/**
	 * use onBackPressed, because onDestroy listAdapter is empty
	 */
	@Override
	public void onBackPressed() {		
		super.onBackPressed();
		Log.i("ListAdapter", ((Contact) this.getListAdapter().getItem(1)).getName());
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	
	
	/** Holds child views for one row. */  
	private static class ContactViewHolder {  
	    private CheckBox checkBox ;  
	    private TextView textView ;  
	    //public ContactViewHolder() {}  
	    public ContactViewHolder( TextView textView, CheckBox checkBox ) {  
	    	this.checkBox = checkBox ;  
	    	this.textView = textView ;  
	    }  
	    public CheckBox getCheckBox() {  
	    	return checkBox;  
	    }
	    
	    @SuppressWarnings("unused")
		public void setCheckBox(CheckBox checkBox) {  
	    	this.checkBox = checkBox;  
	    } 
	    
	    public TextView getTextView() {  
	    	return textView;  
	    }  
	    @SuppressWarnings("unused")
		public void setTextView(TextView textView) {  
	    	this.textView = textView;  
	    }      
	}  
	
	private class ContactAdapter extends ArrayAdapter<Contact> {
		private LayoutInflater inflater;

		public ContactAdapter(Context context,
				int textViewResourceId, List<Contact> objects) {
			super(context, textViewResourceId, objects);
			inflater = LayoutInflater.from(context);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Contact contact = (Contact) this.getItem( position ); 
			// The child views in each row.  
		    CheckBox checkBox;   
		    TextView textView; 
			
		    if ( convertView == null ) {  
		        convertView = inflater.inflate(R.layout.prioritycontactview, null);  
		          
		        // Find the child views.  
		        textView = (TextView) convertView.findViewById( R.id.contacttextview );  
		        checkBox = (CheckBox) convertView.findViewById( R.id.prioritycheckbox );  
		          
		        // Optimization: Tag the row with it's child views, so we don't have to   
		        // call findViewById() later when we reuse the row.  
		        convertView.setTag( new ContactViewHolder(textView,checkBox) );  
		  
		        // If CheckBox is toggled, update the PriorityList. 
		        checkBox.setOnClickListener( new View.OnClickListener() {  
		        	public void onClick(View v) {  
		        		CheckBox cb = (CheckBox) v ;  
		        		Contact contact = (Contact) cb.getTag();  
		        		//contact.setChecked( cb.isChecked() );
		        		PriorityList priListInstance = PriorityList.getInstance(inflater.getContext());
		        		if(cb.isChecked()){
		        			priListInstance.addPriorityCaller(contact);
		        			Log.i("CheckList", "add " +  contact.getName());
		        		}else{
		        			priListInstance.removePriorityCaller(contact);
		        			Log.i("CheckList", "remove " + contact.getName());
		        		}
		        	}  
		        });          
		    }
		    // Reuse existing row view  
		    else {  
		    	// Because we use a ViewHolder, we avoid having to call findViewById().  
		    	ContactViewHolder viewHolder = (ContactViewHolder) convertView.getTag();  
		    	checkBox = viewHolder.getCheckBox() ;  
		    	textView = viewHolder.getTextView() ;  
		    }
		    
		    // Tag the CheckBox with the Planet it is displaying, so that we can  
		    // access the planet in onClick() when the CheckBox is toggled.  
		    checkBox.setTag( contact );   
		        
		    // Display planet data  
		    //checkBox.setChecked( contact.isChecked() );  
		    textView.setText( contact.getName() );
		    
			return convertView;
		}
		
	}
	

}

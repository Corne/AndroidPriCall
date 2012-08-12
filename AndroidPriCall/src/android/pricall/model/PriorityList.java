/**
 * 
 */
package android.pricall.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Observable;

import android.content.Context;



/**
 * @author Corne
 *
 */
public class PriorityList extends Observable {

	public static final String PRIORITYFILENAME = "prioritylist"; 
	
	private static PriorityList instance;
	private Context context;
	private ArrayList<Contact> prioritygroupList;
	
	private PriorityList(Context context) {
		this.context = context;
		prioritygroupList = new ArrayList<Contact>();
		this.getSavedPriorityList();
	}
	
	public static PriorityList getInstance(Context context){
		if(instance == null){
			instance = new PriorityList(context);
		}
		return instance;
	}
	
	@SuppressWarnings("unchecked")
	private void getSavedPriorityList(){
		FileInputStream fis = null;
        ObjectInputStream in = null;
        
    	try {
			fis = context.openFileInput(PRIORITYFILENAME);
			in = new ObjectInputStream(fis);
			prioritygroupList = (ArrayList<Contact>) in.readObject();
			in.close();
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (OptionalDataException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
    	setChanged();
    	notifyObservers();
	}
	
	public void savePriorityList(){
		FileOutputStream fus = null;
		ObjectOutputStream out = null;
		
		try {
			fus = context.openFileOutput(PRIORITYFILENAME, Context.MODE_PRIVATE);
			//TODO need to check if file overrides old arraylist or adds new arraylist to file
			out = new ObjectOutputStream(fus);
			out.writeObject(prioritygroupList);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		setChanged();
    	notifyObservers();
	}
	
	public ArrayList<Contact> getPriorityGroupList(){
		return prioritygroupList;
	}
	
	public ArrayList<String> getPriorityPhoneNumbers(){
		ArrayList<String> phoneNumbers = new ArrayList<String>();
		for(int i=0; i < prioritygroupList.size(); i++){
			phoneNumbers.add(prioritygroupList.get(i).getPhoneNumber());
		}
		return phoneNumbers;
	}
	
	public void addPriorityCaller(Contact priCaller){
		prioritygroupList.add(priCaller);
		savePriorityList();
	}
	
	public void removePriorityCaller(Contact priCaller){
		prioritygroupList.remove(priCaller);
		savePriorityList();
	}
}

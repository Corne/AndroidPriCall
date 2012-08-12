/**
 * 
 */
package android.pricall.model;

import java.io.Serializable;

/**
 * @author Corne
 *
 */
public class Contact implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id = "";
	private String name = "";
	private String phoneNumber = "";
	
	
	public Contact() {};
    
	public Contact(String id, String name, String phoneNumber){
		this.setId(id);
    	this.setName(name);
    	this.setPhoneNumber(phoneNumber);
    }
    
    /**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
}

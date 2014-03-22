package com.feth.play.module.ts;


import java.io.Serializable;

public interface TemporaryStorage {

	/**
	 * Gets an object from the temporary storage
	 * 
	 * @param key the unique key to find the object under
	 * @return the object or null if the object was not found or the expiration date exceeded
	 */
	public <T extends Serializable> T get(final String key);
	
	/**
	 * Puts an object into the temporary storage
	 * 
	 * @param key the unique key to register the object under. If this key has been used, the old value gets overwritten.
	 * @param something The object to store
	 */
	public <T extends Serializable> void set(final String key, final T something);
	
	/**
	 * Puts an object into the temporary storage with a defined expiration
	 * 
	 * @param key @see set()
	 * @param something @see set()
	 * @param expiration the expiration time of the object in the temporary storage in seconds
	 */
	public <T extends Serializable> void set(final String key, final T something, final int expiration);
	
	/**
	 * Removes an object from the temporary storage
	 * 
	 * @param key the unique key of the object to be removed
	 */
	public void remove(final String key);
}

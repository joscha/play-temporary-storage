package com.feth.play.module.ts;


import java.io.Serializable;

public interface TemporaryStorage {

	
	public <T extends Serializable> T get(final String key);
	public <T extends Serializable> void set(final String key, final T something);
	/**
	 * 
	 * @param key
	 * @param something
	 * @param expiration in seconds
	 */
	public <T extends Serializable> void set(final String key, final T something, final int expiration);
	public void remove(final String key);
}

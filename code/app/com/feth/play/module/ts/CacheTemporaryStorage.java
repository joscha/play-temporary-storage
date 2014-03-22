package com.feth.play.module.ts;


import java.io.Serializable;


public class CacheTemporaryStorage implements TemporaryStorage {

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Serializable> T get(final String x) {
		return (T) play.cache.Cache.get(x);
	}

	@Override
	public <T extends Serializable> void set(final String x, final T something) {
		play.cache.Cache.set(x, something);
	}

	@Override
	public <T extends Serializable> void set(final String x, final T something, final int until) {
		play.cache.Cache.set(x, something, until);
	}

	@Override
	public void remove(final String key) {
		play.cache.Cache.remove(key);
	}
}

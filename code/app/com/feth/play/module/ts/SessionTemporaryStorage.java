package com.feth.play.module.ts;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;

import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import play.libs.Crypto;
import play.mvc.Http.Session;

public class SessionTemporaryStorage implements TemporaryStorage {

	private static final long INFINITE_VALIDITY = -1L;

	private static class SessionTuple<T extends Serializable> implements
			Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public long validUntil;
		public T payload;

		public SessionTuple(T payload, int expires) {
			this.payload = payload;
			this.validUntil = expires <= 0 ? INFINITE_VALIDITY : new Date()
					.getTime() + expires * 1000;
		}
	}

	private final boolean encrypted;
	private final Session session;

	// Taken from: http://stackoverflow.com/questions/134492/
	/** Read the object from Base64 string. */
	private static Object fromString(final String s) throws IOException,
			ClassNotFoundException {
		final byte[] data = Base64Coder.decode(s);
		final ObjectInputStream ois = new ObjectInputStream(
				new ByteArrayInputStream(data));
		final Object o = ois.readObject();
		ois.close();
		return o;
	}

	// Taken from http://stackoverflow.com/questions/134492/
	/** Write the object to a Base64 string. */
	private static String toString(final Serializable o) throws IOException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(o);
		oos.close();
		return new String(Base64Coder.encode(baos.toByteArray()));
	}

	public SessionTemporaryStorage(final Session session) {
		this(session, false);
	}

	public SessionTemporaryStorage(final Session session,
			final boolean encrypted) {
		this.encrypted = encrypted;
		this.session = session;
	}

	private <T extends Serializable> String encodeValue(final T something,
			final int expiration) throws IOException {
		final SessionTuple<T> t = new SessionTuple<T>(something, expiration);
		return encrypted ? Crypto.encryptAES(toString(t)) : toString(t);
	}

	@SuppressWarnings("unchecked")
	private <T extends Serializable> SessionTuple<T> decodeValue(
			final String encodedValue) throws IOException {
		SessionTuple<T> t = null;
		try {
			t = (SessionTuple<T>) fromString(encrypted ? Crypto
					.decryptAES(encodedValue) : encodedValue);
		} catch (final ClassNotFoundException e) {
			// can't happen because SessionTuple is inner class
		} catch (final Exception e) {
			throw new RuntimeException(
					"Could not decode value from session. Has it been put in there using TemporaryStorage?",
					e);
		}
		return t;
	}

	@Override
	public <T extends Serializable> T get(final String key) {
		final String sessionValue = session.get(key);
		if (sessionValue != null) {
			SessionTuple<T> tuple;
			try {
				tuple = decodeValue(sessionValue);
			} catch (final IOException e) {
				throw new RuntimeException("Could not read data from session",
						e);
			}
			if (INFINITE_VALIDITY == tuple.validUntil
					|| new Date().getTime() <= tuple.validUntil) {
				return tuple.payload;
			}
		}
		return null;
	}

	@Override
	public <T extends Serializable> void set(final String key, final T something) {
		set(key, something, 0);
	}

	@Override
	public <T extends Serializable> void set(final String key,
			final T something, int expiration) {
		try {
			session.put(key, encodeValue(something, expiration));
		} catch (final IOException e) {
			throw new RuntimeException("Could not put data into session", e);
		}
	}

	@Override
	public void remove(final String key) {
		session.remove(key);
	}

}

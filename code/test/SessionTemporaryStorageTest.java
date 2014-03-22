import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.running;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import play.libs.Crypto;
import play.mvc.Http.Context;
import play.mvc.Http.Request;
import play.mvc.Http.Session;

import com.feth.play.module.ts.SessionTemporaryStorage;
import com.feth.play.module.ts.TemporaryStorage;

public class SessionTemporaryStorageTest {

	final static String SOME_KEY = "key";
	final static String SOME_VALUE = new String("foo");

	@Before
	public void init() {
		final Request requestMock = mock(Request.class);
		Context.current.set(new Context(null, null, requestMock,
				new HashMap<String, String>(), new HashMap<String, String>(),
				new HashMap<String, Object>()));
	}

	@Test
	public void itShouldUsePlaySession() {
		final TemporaryStorage ts = getStorage();
		ts.set(SOME_KEY, SOME_VALUE);
		assertThat(getSession().get(SOME_KEY)).isNotNull();
	}

	@Test
	public void testSet() {
		final TemporaryStorage ts = getStorage();
		ts.set(SOME_KEY, SOME_VALUE);
		assertThat(ts.get(SOME_KEY)).isEqualTo(SOME_VALUE);
	}

	@Test
	public void testSetNoExpiration() {
		final TemporaryStorage ts = getStorage();
		ts.set(SOME_KEY, SOME_VALUE, 0);
		assertThat(ts.get(SOME_KEY)).isEqualTo(SOME_VALUE);
	}

	@Test
	public void testSetXsExpiration() {
		int expiration = 1;
		final TemporaryStorage ts = getStorage();
		ts.set(SOME_KEY, SOME_VALUE, expiration);
		assertThat(ts.get(SOME_KEY)).isEqualTo(SOME_VALUE);
		try {
			Thread.sleep((expiration + 1) * 1000);
		} catch (InterruptedException e) {

		}
		assertThat(ts.get(SOME_KEY)).isNull();
	}

	@Test
	public void testGet() {
		final TemporaryStorage ts = getStorage();
		ts.set(SOME_KEY, SOME_VALUE);
		assertThat(ts.get(SOME_KEY)).isEqualTo(SOME_VALUE);
	}

	@Test
	public void testGetSessionValueNotFromTemporaryStorage() {
		getSession().put(SOME_KEY, SOME_VALUE);
		final TemporaryStorage ts = getStorage();
		Exception e = null;
		try {
			ts.get(SOME_KEY);
		} catch (final RuntimeException re) {
			e = re;
		}
		assertThat(e)
				.hasMessage(
						"Could not decode value from session. Has it been put in there using TemporaryStorage?");
	}
	
	@Test
	public void testGetSessionValueNotFromTemporaryStorageEncrypted() {
		running(fakeApplication(), new Runnable() {

			@Override
			public void run() {
				

		getSession().put(SOME_KEY, SOME_VALUE);
		final TemporaryStorage ts = getStorage(true);
		Exception e = null;
		try {
			ts.get(SOME_KEY);
		} catch (final RuntimeException re) {
			e = re;
		}
		assertThat(e)
				.hasMessage(
						"Could not decode value from session. Has it been put in there using TemporaryStorage?");
		}
	});
		}

	@Test
	public void testGetEncrypted() {
		running(fakeApplication(), new Runnable() {

			@Override
			public void run() {
				final TemporaryStorage ts = getStorage(true);
				ts.set(SOME_KEY, SOME_VALUE);
				try {
					Crypto.decryptAES(getSession().get(SOME_KEY));
				} catch (final Exception e) {
					throw new RuntimeException("decryption failed");
				}
				assertThat(ts.get(SOME_KEY)).isEqualTo(SOME_VALUE);
			}
		});

	}

	@Test
	public void testRemove() {
		final TemporaryStorage ts = getStorage();
		ts.set(SOME_KEY, SOME_VALUE);
		assertThat(ts.get(SOME_KEY)).isNotNull();
		ts.remove(SOME_KEY);
		assertThat(ts.get(SOME_KEY)).isNull();
	}

	private TemporaryStorage getStorage() {
		return getStorage(false);
	}

	private TemporaryStorage getStorage(boolean encrypted) {
		return new SessionTemporaryStorage(getSession(), encrypted);
	}

	private Session getSession() {
		return Context.current().session();
	}

}

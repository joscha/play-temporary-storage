import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.running;

import org.junit.Test;

import com.feth.play.module.ts.CacheTemporaryStorage;
import com.feth.play.module.ts.TemporaryStorage;

public class CacheTemporaryStorageTest {

	final static String SOME_KEY = "key";
	final static String SOME_VALUE = new String();

	@Test
	public void itShouldUsePlayCache() {
		running(fakeApplication(), new Runnable() {

			@Override
			public void run() {
				final TemporaryStorage ts = getStorage();
				ts.set(SOME_KEY, SOME_VALUE);
				assertThat(play.cache.Cache.get(SOME_KEY)).isSameAs(SOME_VALUE);
			}
		});
	}

	@Test
	public void testSet() {
		running(fakeApplication(), new Runnable() {

			@Override
			public void run() {
				final TemporaryStorage ts = getStorage();
				ts.set(SOME_KEY, SOME_VALUE);
				assertThat(ts.get(SOME_KEY)).isSameAs(SOME_VALUE);
			}
		});
	}

	@Test
	public void testSetNoExpiration() {
		running(fakeApplication(), new Runnable() {

			@Override
			public void run() {
				final TemporaryStorage ts = getStorage();
				ts.set(SOME_KEY, SOME_VALUE, 0);
				assertThat(ts.get(SOME_KEY)).isSameAs(SOME_VALUE);
			}
		});
	}

	@Test
	public void testSetXsExpiration() {
		running(fakeApplication(), new Runnable() {

			@Override
			public void run() {
				int expiration = 1;
				final TemporaryStorage ts = getStorage();
				ts.set(SOME_KEY, SOME_VALUE, expiration);
				assertThat(ts.get(SOME_KEY)).isSameAs(SOME_VALUE);
				try {
					Thread.sleep((expiration + 1) * 1000);
				} catch (final InterruptedException e) {
					throw new RuntimeException(e);
				}
				assertThat(ts.get(SOME_KEY)).isNull();
			}
		});
	}

	@Test
	public void testGet() {
		running(fakeApplication(), new Runnable() {

			@Override
			public void run() {
				final TemporaryStorage ts = getStorage();
				ts.set(SOME_KEY, SOME_VALUE);
				assertThat(ts.get(SOME_KEY)).isSameAs(SOME_VALUE);
			}
		});
	}

	@Test
	public void testRemove() {
		running(fakeApplication(), new Runnable() {

			@Override
			public void run() {
				final TemporaryStorage ts = getStorage();
				ts.set(SOME_KEY, SOME_VALUE);
				assertThat(ts.get(SOME_KEY)).isNotNull();
				ts.remove(SOME_KEY);
				assertThat(ts.get(SOME_KEY)).isNull();
			}
		});
	}

	private TemporaryStorage getStorage() {
		return new CacheTemporaryStorage();
	}

}

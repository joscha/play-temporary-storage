package controllers;

import play.cache.Cache;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

import com.feth.play.module.ts.CacheTemporaryStorage;
import com.feth.play.module.ts.SessionTemporaryStorage;
import com.feth.play.module.ts.TemporaryStorage;

public class Application extends Controller {

	private static final String KEY_A = "a";
	private static final String KEY_B = "b";
	private static final String KEY_C = "c";

	public static Result index() {
		final TemporaryStorage cachedStorage = new CacheTemporaryStorage();
		Integer a = cachedStorage.get(KEY_A);
		cachedStorage.set(KEY_A, a == null ? a = 0 : ++a);
		final String aRaw = "" + Cache.get(KEY_A);

		final TemporaryStorage sessionStorage = new SessionTemporaryStorage(
				session());
		Integer b = sessionStorage.get(KEY_B);
		sessionStorage.set(KEY_B, b == null ? b = 0 : ++b);
		final String bRaw = session(KEY_B);
		

		final TemporaryStorage encryptedSessionStorage = new SessionTemporaryStorage(
				session(), true);
		Integer c = encryptedSessionStorage.get(KEY_C);
		encryptedSessionStorage.set(KEY_C, c == null ? c = 0 : ++c);
		final String cRaw = session(KEY_C);

		return ok(index.render(a, aRaw, b, bRaw, c, cRaw));
	}

}

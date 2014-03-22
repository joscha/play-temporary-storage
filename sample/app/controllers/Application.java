package controllers;

import play.cache.Cache;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

import com.feth.play.module.ts.CacheTemporaryStorage;
import com.feth.play.module.ts.SessionTemporaryStorage;
import com.feth.play.module.ts.TemporaryStorage;

public class Application extends Controller {

	public static Result index() {
		final TemporaryStorage cachedStorage = new CacheTemporaryStorage();
		Integer a = cachedStorage.get("a");
		cachedStorage.set("a", a == null ? a = 0 : ++a);
		final String aRaw = "" + Cache.get("a");

		final TemporaryStorage sessionStorage = new SessionTemporaryStorage(
				session());
		Integer b = sessionStorage.get("b");
		sessionStorage.set("b", b == null ? b = 0 : ++b);
		final String bRaw = session("b");
		

		final TemporaryStorage encryptedSessionStorage = new SessionTemporaryStorage(
				session(), true);
		Integer c = encryptedSessionStorage.get("c");
		encryptedSessionStorage.set("c", c == null ? c = 0 : ++c);
		final String cRaw = session("c");		

		return ok(index.render(a, aRaw, b, bRaw, c, cRaw));
	}

}

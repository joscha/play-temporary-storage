play-temporary-storage [![Build Status](https://travis-ci.org/joscha/play-temporary-storage.svg)](https://travis-ci.org/joscha/play-temporary-storage)
======================

Temporary storage module for the Play Framework 2 that allows to put serializable objects into

* the Play cache
* the Play session 
  * with optional AES encryption

and set an expiration time.

The interface of `TemporaryStorage` is 100% compatible to the one of [`play.Cache`](http://www.playframework.com/documentation/2.0/JavaCache).
Have a look at the simple interface [here](code/app/com/feth/play/module/ts/TemporaryStorage.java) and don't forget to check out the [sample application](sample/).

## Sample
```java
final boolean encrypted = true;
// use the session to store our data and enable encryption
final TemporaryStorage storage = new SessionTemporaryStorage(session(), encrypted);
// fetch the count
Integer count = storage.get("count");
// set the count - it will expire in 5 seconds
storage.set("count", count == null ? count = 0 : ++count, 5);

if(count > 3) {
  // remove the count
  storage.remove("count");
}
```	

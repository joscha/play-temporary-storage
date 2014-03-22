play-temporary-storage [![Build Status](https://travis-ci.org/joscha/play-temporary-storage.svg)](https://travis-ci.org/joscha/play-temporary-storage)
======================

Temporary storage module for the Play Framework 2 that allows to put serializable objects into

* the Play cache
* the Play session 
  * with optional AES encryption

and set an expiration time.

The interface of `TemporaryStorage` is 100% compatible to the one of `play.Cache`.
Have a look [here](code/app/com/feth/play/module/ts/TemporaryStorage.java)

## Sample
```java
final boolean encrypted = true;
final TemporaryStorage storage = new SessionTemporaryStorage(session(), encrypted);
Integer count = storage("MY_KEY");
storage.set("MY_KEY", count == null ? count = 0 : ++count);
```	

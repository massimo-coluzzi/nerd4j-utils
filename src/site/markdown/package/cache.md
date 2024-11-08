# Package _cache_

In this package you will find utility classes to reduce the boilerplate code related to caching operations.

### Dealing with caches

Caching is one of the most common operation in software systems.\
Unfortunately the most of the caching systems perform the following behavior:

1. If the cache entry is available and is not expired then will be returned.
1. If the cache entry is not available or has expired then _null_ will be returned.

This will force the developer to reimplement always the same logic every time he wants to cache some data.\
For example he will need to write something like this:
```java
  Value value = cache.get( key ); 
  if( value == null )
  {
    value = loadData( someParamenters );
    cache.put( key, value, duration );
  }
  // Use the value in some way.
```

### Class _SelfLoadingCache_

Dealing with caches is always a tradeoff between performance and freshness of the data.\
Usually you need to cache some data because loading such data is time and resource consuming.\
When the cache expires, the system will suffer an increasing of the load because all the threads
that requires the expired data will fire a heavy loading process.\
There are several best practices to handle this problem but the base principle is to keep returning the cached
data while the fresh data will be loaded asynchronously.

The _SelfLoadingCache_ is based on this principle and its aim is to reduce the boilerplate code needed to deal with caches.\
This is not a caching framework, is more like a facade for caching like [SLF4J](http://www.slf4j.org/) is a facade
for logging.\
This utility class requires a _CacheProvider_ that is an interface to the actual caching system and a _DataProvider_
that can be implemented via _Java8 Lambdas_ and is used to load the data when needed.\
Using the _SelfLoadingCache_ the previous example becomes:
```java
  Value value = selfLoadingCache.get( key, k -> loadData( someParamenters ) );
```

By default this class will perform the following behavior:

1. If the entry is available and is not expired will return it.
1. If the entry is not available will load it synchronously and populate the cache.
1. If the entry is available but has expired will return the cached value and will update it asynchronously.
1. Any exception related to the cache will be logged but will not be propagated because caching should be as transparent
   as possible and therefore an application should not crash due to an error related to the cache.
   
Any of the previous points can be customized. There is a _SelfLoadingCacheBuilder_ that provides an easy and customizable
way to create instances of the _SelfLoadingCache_ facade.\
There are also an _EmptyCacheProvider_ for testing and a_HeapCacheProvider_ that implements a simple caching system
based on the _JVM_ heap memory.

Sometimes it can be useful to disable caching for debugging purposes. The _SelfLoadingCache_ allows to disable a single
instance or all instances at once. You can also start your application with all caches disabled by setting the _JVM_
property __"org.nerd4j.utils.cache.SelfLoadingCache.disabled=true"__.

I have also implemented some _CacheProviders_ to work with [Ehcache](https://www.ehcache.org/) and
[Memcached](https://github.com/memcached/memcached/wiki) but I did not include this implementations in this project
because I don't want this library to depend from third party software.\
So far I was able to keep things simple and depend only on the _Java Runtime Environment_ and I want to keep it this way.
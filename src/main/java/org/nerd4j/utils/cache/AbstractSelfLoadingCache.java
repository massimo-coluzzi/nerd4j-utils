/*
 * #%L
 * Nerd4j Utils
 * %%
 * Copyright (C) 2011 - 2016 Nerd4j
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 *
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */
package org.nerd4j.utils.cache;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.nerd4j.utils.lang.Require;

/**
 * Abstract implementation of {@link SelfLoadingCache}.
 *
 * <p>
 * This class provides most of the common logic and
 * some useful extension hooks. Any custom implementation
 * of the {@link SelfLoadingCache} interface should
 * extend this class.
 *
 * @param <V> type of the data model to cache.
 *
 * @author Massimo Coluzzi
 * @since 2.1.0
 */
public abstract class AbstractSelfLoadingCache<V> implements SelfLoadingCache<V>
{

    /**
     * Logging system.
     * <p>
     * By default the logging level is {@link Level#WARNING} and will only log exceptions.
     * <p>
     * If you want to log any caching operation you need to set the logging level to {@link Level#INFO}.
     * <p>
     * If you want to dig into deeper detail about cache loading and updating operations,
     * you need to set the logging level to {@link Level#FINE}.
     * <p>
     * If you want to log everything is happening in the cache, including the data that is going to be
     * cached, you need to set the logging level to {@link Level#FINEST}.
     * <p>
     * If you trust this class to work properly and you do not want your console output to get dirty
     * you can turn off this log by setting the logging level to {@link Level#OFF}.
     * <p>
   	 * To change the {@link Logger} levels and outputs follow the
  	 * <a href="https://docs.oracle.com/javase/6/docs/technotes/guides/logging/overview.html">Java documentation</a>.
     */
    public static final Logger logger;

    /**
     * The {@link ExecutorService} to use to insert or update
     * cache entries in an asynchronous way.
     */
    protected static final ExecutorService executorService;

    static
    {
        logger = Logger.getLogger( AbstractSelfLoadingCache.class.getName() );
        logger.setLevel( Level.WARNING );

        final AtomicInteger counter = new AtomicInteger(0);
        final ThreadFactory threadFactory
        = task -> new Thread( task, "SelfLoadingCache-thread-" + counter.getAndIncrement() );

        executorService = Executors.newCachedThreadPool( threadFactory );
    }


    /** Configurations to be used to define the cache behavior. */
    protected final CacheConfig config;

    /** Underlying caching system provider. */
    protected final CacheProvider<V> cacheProvider;

    /**
     * Tells if this {@link SelfLoadingCache} is disabled.
     * If this flag is set to {@code true} this instance
     * will skip any caching operation and all data will
     * be loaded on each request.
     * <p>
     * This option should be used to debugging purposes only,
     * using this option in production can be very dangerous.
     */
    protected boolean disabledThis;


    /**
     * Constructor with parameters.
     *
     * @param cacheConfig   configurations to be used to define the cache behavior.
     * @param cacheProvider underlying caching system provider.
     */
    public AbstractSelfLoadingCache( CacheConfig cacheConfig, CacheProvider<V> cacheProvider )
    {

        super();

        this.disabledThis = false;
        this.config = Require.nonNull( cacheConfig, "The cache configurations are mandatory" );
        this.cacheProvider = Require.nonNull( cacheProvider, "The cache provider is mandatory" );

    }


    /* ******************* */
    /*  INTERFACE METHODS  */
    /* ******************* */


    /**
     * {@inheritDoc}
     */
    @Override
    public void disableThis( boolean disable )
    {

        this.disabledThis = disable;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public V get( CacheKey key, DataProvider<V> dataProvider )
    {

        Require.nonNull( key, "The cache key to search for is mandatory" );
        Require.nonNull( dataProvider, "The data provider is mandatory" );

        /*
         * If this or all SelfLoadingCaches are disabled no caching
         * logic will be done and the data will be loaded from the DataProvider.
         */
        if( disabledThis || CacheConfig.disabledAll )
            return load( key, dataProvider );


        /* We try to get the entry from the cache. */
        final CacheEntry<V> entry = get( key );

        if( entry == null )
        {

            logger.log( Level.INFO, "Cache MISS: for {0}", key );

            /*
             * If the entry is null one of this happen:
             *  1. the entry is not present in cache;
             *  2. there was an error reaching the cache provider.
             * In both cases we try to load it from the data provider
             * and insert it on cache again.
             */
            return insert( key, dataProvider );

        }

        if( entry.isExpired() )
        {

            logger.log( Level.INFO, "Cache entry EXPIRED: for {0}", key );

            /*
             * The update will be executed in different ways from the extending
             * classes. This method can return the updated value or the old one
             * depending on the implementation.
             */
            return update( key, entry, dataProvider );

        }

        logCacheHit( key, entry );
        return entry.getValue();


    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void evict( CacheKey key )
    {

        /*
         * If this or all SelfLoadingCaches are disabled no caching
         * logic will be done.
         */
        if(  disabledThis || CacheConfig.disabledAll )
            return;


        try{

            logger.log( Level.INFO, "Going to EVICT cache for {0}", key );
            cacheProvider.remove( key );

        }catch( Exception ex )
        {

            throwCacheProviderException( "evicting", key, ex );

        }

    }


    /* ******************* */
    /*  PROTECTED METHODS  */
    /* ******************* */


    /**
     * Returns the value from the cache if available
     * and {@code null} otherwise.
     * <p>
     * This method returns {@code null} in both cases:
     * <ol>
     *  <li>If the cache provider returns {@code null}.</li>
     *  <li>If the cache provider throws an exception.</li>
     * </ol>
     *
     * @param key the key to search for.
     * @return the cached entry or {@code null}.
     * @throws CacheProviderException if an error occurred during
     *         cache operations and the flag
     *         {@link CacheConfig#isThrowCacheProviderExceptions()}
     *         is {@code true}.
     */
    protected CacheEntry<V> get( CacheKey key )
    {

        try{

            logger.log( Level.FINE, "Going to get cache entry for {0}", key );

            return cacheProvider.get( key );

        }catch( Exception ex )
        {

            throwCacheProviderException( "getting", key, ex );

        }

        return null;

    }

    /**
     * Puts the given {@code key-value} pair into the cache.
     *
     * @param key the key to cache.
     * @param value the value to cache.
     * @throws CacheProviderException if an error occurred during
     *         cache operations and the flag
     *         {@link CacheConfig#isThrowCacheProviderExceptions()}
     *         is {@code true}.
     */
    protected void put( CacheKey key, V value )
    {

        try{

            final long duration = config.getCacheDuration();

            if( logger.isLoggable(Level.FINEST) )
                logger.finest( "Going to put " + value + " into cache with " + key + " for " + duration + " ms" );

            else if( logger.isLoggable(Level.FINE) )
                logger.fine( "Going to set cache entry with " + key + " for " + duration + " ms" );

            cacheProvider.put( key, value, duration );

        }catch( Exception ex )
        {

            throwCacheProviderException( "putting", key, ex );

        }

    }

    /**
     * Loads the value from the {@link DataProvider}.
     *
     * @param key the key to search for.
     * @param dataProvider the {@link DataProvider} to invoke.
     * @return the data to cache.
     * @throws DataProviderException if there is a failure
     *         in the data loading.
     */
    protected V load( CacheKey key, DataProvider<V> dataProvider )
    {

        try{

            logger.log( Level.FINE, "Going to load data for {0}", key );

            return dataProvider.retrieve( key );

        }catch( Exception ex )
        {

            throw getDataProviderException( "loading", key, ex );

        }

    }

    /**
     * Returns {@code true} if the given cache key
     * has been touched.
     * <p>
     * This method returns {@code true} even if
     * {@link CacheProvider#touch(CacheKey,long)}
     * fails in exception because this forces the system
     * to load fresh data and update the cache.
     *
     * @param key the key to search for.
     * @return  {@code true} if the given cache key has been touched.
     * @throws CacheProviderException if an error occurred during
     *         cache operations and the flag
     *         {@link CacheConfig#isThrowCacheProviderExceptions()}
     *         is {@code true}.
     */
    private boolean touch( CacheKey key )
    {

        try {

            final long duration = config.getTouchDuration();

            if( logger.isLoggable(Level.FINE) )
                logger.fine( "Going to touch the cache entry for " + key +
                            " deferring the expiration by " + duration + " ms" );

            return cacheProvider.touch( key, duration );

        }catch( Exception ex )
        {

            throwCacheProviderException( "touching", key, ex );

        }

        /*
         * If exceptions are suppressed we return true
         * so the data will be reloaded and the application
         * will not crash if an error occurs in the cache.
         */
        return true;

    }

    /**
     * Performs the loading of te data related to the given
     * key and puts the {@code <key-value>} pair into the cache.
     *
     * @param operation    the operation to log.
     * @param key          key to cache.
     * @param dataProvider provider of the values to be cached.
     * @return the loaded value.
     * @throws CacheProviderException if an error occurred during
     *         cache operations and the flag
     *         {@link CacheConfig#isThrowCacheProviderExceptions()}
     *         is {@code true}.
     * @throws DataProviderException if there is a failure
     *         in the data loading.
     */
    protected V loadAndPut( String operation, CacheKey key, DataProvider<V> dataProvider )
    {

        if( logger.isLoggable(Level.FINE) )
            logger.fine( "Going to " + operation + " the cache entry for " + key );

        final V value = load( key, dataProvider );
        put( key, value );

        return value;

    }

    /**
     * Inserts the given key and the related value into the cache.
     * <p>
     * If this cache is configured to perform inserts synchronously
     * (the default) then this method will invoke {@link #loadAndPut(String,CacheKey,DataProvider)},
     * otherwise the insert will be performed by another thread while
     * this method returns {@code null}.
     *
     * @param key          key to cache.
     * @param dataProvider provider of the values to be cached.
     * @return the inserted value, or {@code null}.
     * @throws CacheProviderException if an error occurred during
     *         cache operations and the flag
     *         {@link CacheConfig#isThrowCacheProviderExceptions()}
     *         is {@code true}.
     * @throws DataProviderException if there is a failure
     *         in the data loading.
     */
    protected V insert( CacheKey key, DataProvider<V> dataProvider )
    {

        if( config.isAsyncInsert() )
        {
            executorService.execute( () -> loadAndPut("insert", key, dataProvider) );
            return null;
        }
        else
            return loadAndPut( "insert", key, dataProvider );

    }


    /**
     * Updates the cache for the given key and the related value.
     * <p>
     * If this cache is configured to perform updates asynchronously
     * (the default) then the update will be performed by another thread while
     * this method returns the current cached value, otherwise this method will
     * invoke {#loadAndPut(String,CacheKey,DataProvider)} and return the fresh
     * loaded value.
     * <p>
     * If multiple threads try to update the same key only one will succeed,
     * all the other will return the currently cached value.
     *
     * @param key          key to cache.
     * @param entry        the expired cache entry.
     * @param dataProvider provider of the values to be cached.
     * @return the fresh loaded value, or the cached one.
     * @throws CacheProviderException if an error occurred during
     *         cache operations and the flag
     *         {@link CacheConfig#isThrowCacheProviderExceptions()}
     *         is {@code true}.
     * @throws DataProviderException if there is a failure
     *         in the data loading.
     */
    protected V update( CacheKey key, CacheEntry<V> entry, DataProvider<V> dataProvider )
    {

        if( ! touch(key) )
        {
            /*
             * If the touch method fails then another thread is updating
             * this key, therefore we can return immediately.
             */
            logger.log( Level.FINE, "Touch failed, someone else is updating the cache entry for {0}", key );
            return entry.getValue();
        }

        /*
         * If we reach this point means that this thread won the race condition,
         * the update will be performed immediately or asynchronously depending
         * on how this cache has been configured.
         */
        if( config.isAsyncUpdate() )
        {
            executorService.execute( () -> loadAndPut("update", key, dataProvider) );
            return entry.getValue();
        }
        else
            return loadAndPut( "update", key, dataProvider );

    }


    /* ***************** */
    /*  PRIVATE METHODS  */
    /* ***************** */


    /**
     * Throws a new {@link DataProviderException} with the given cause
     * and the given message.
     *
     * @param operation the operation that caused the error.
     * @param key the cache key for which the error occurred.
     * @param cause the original exception.
     * @throws CacheProviderException
     */
    private DataProviderException getDataProviderException( String operation, CacheKey key, Exception cause )
    {

        if( logger.isLoggable(Level.WARNING) )
            logger.warning(  "An error occurred while " + operation + " data for key " + key + ": "  + cause );

        if( cause instanceof DataProviderException )
            throw (DataProviderException) cause;
        else
            throw new DataProviderException( "Unhandled exception occurred in data provider", cause );

    }

    /**
     * Throws a new {@link CacheProviderException} with the given cause
     * and the given message if the flag
     * {@link CacheConfig#isThrowCacheProviderExceptions()} is {@code true}.
     *
     * @param operation the operation that caused the error.
     * @param key the cache key for which the error occurred.
     * @param cause the original exception.
     * @throws CacheProviderException if required by the configuration.
     */
    private void throwCacheProviderException( String operation, CacheKey key, Exception cause )
    {

        if( logger.isLoggable(Level.WARNING) )
            logger.warning(  "An error occurred while " + operation + " a cache entry for key " + key + ": "  + cause );

        if( config.isThrowCacheProviderExceptions() )
        {
            if( cause instanceof CacheProviderException )
                throw (CacheProviderException) cause;
            else
                throw new CacheProviderException( "Unhandled exception occurred in cache provider", cause );
        }

    }

    /**
     * Logs the information related to a cache-hit.
     *
     * @param key   the cache key to log.
     * @param entry the cache entry to log.
     */
    private void logCacheHit( CacheKey key, CacheEntry<?> entry )
    {

        if( logger.isLoggable(Level.FINEST) )
            logger.fine( "Cache HIT: for key " + key + " with value " + entry.getValue() + " expiring at " + entry.getExpiration() );

        else if( logger.isLoggable(Level.FINE) )
            logger.fine( "Cache HIT: for key " + key + " expiring at " + entry.getExpiration() );

        else if( logger.isLoggable(Level.INFO) )
            logger.info( "Cache HIT: for key " + key );

    }

}
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

import org.nerd4j.utils.lang.Require;
import org.nerd4j.utils.lang.RequirementFailure;

import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Abstract implementation of the {@link CacheProvider} interface
 * that implements all the common checks and logic allowing the
 * extending classes to implement only the underlying caching
 * system related logic.
 *
 * @param <V> type of data in the cache entry.
 *
 * @author Massimo Coluzzi
 * @since 2.1.0
 */
public abstract class AbstractCacheProvider<V> implements CacheProvider<V>
{

	/**
	 * Logging system.
	 * <p>
	 * By default the logging level is {@link Level#WARNING} and will only log exceptions.
	 * <p>
	 * If you want to log any caching operation you need to set the logging level to {@link Level#INFO}.
	 * <p>
	 * If you want to dig into deeper detail about cache loading and updating operations,
	 * including the data that is going to be cached, you need to set the logging level to {@link Level#FINE}.
	 * <p>
	 * If you trust this class to work properly and you do not want your logs to get dirty
	 * you can turn off this log by setting the logging level to {@link Level#OFF}.
	 * <p>
  	 * To change the {@link Logger} levels and outputs follow the
  	 * <a href="https://docs.oracle.com/javase/6/docs/technotes/guides/logging/overview.html">Java documentation</a>.
	 */
    public static final Logger logger;

	static
	{
		logger = Logger.getLogger( AbstractCacheProvider.class.getName() );
		logger.setLevel( Level.WARNING );
	}


    /**
     * Default constructor.
     *
     */
    public AbstractCacheProvider()
    {

        super();

    }


	/* ******************* */
	/*  INTERFACE METHODS  */
	/* ******************* */


	/**
	 * {@inheritDoc}
	 */
	@Override
	public CacheEntry<V> get( CacheKey key )
	{

		logger.log( Level.INFO, "GET request for {0}", key );

        try{

			Require.nonNull( key, "The cache key must be not null" );
            final CacheEntry<V> entry =  doGet( key );

            if( logger.isLoggable(Level.FINEST) )
            {
                if( entry == null )
                    logger.finest( "Entry not found for " + key );
                else
                    logger.finest( "GET operation for " + key + " returned " + entry );
            }

            return entry;

		}catch( Exception ex )
		{

            throw cacheProviderException( "GET", key, ex );

		}

	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void put( CacheKey key, V value, long duration )
	{

        logger.log( Level.INFO, "PUT request for {0}", key );

        if( duration <= 0 )
		{
			logger.log( Level.INFO, "Requested durations is {0}ms so nothing will be done", duration );
			return;
		}

		try{

			Require.nonNull( key, "The cache key must be not null" );

			/* We create a new entry for the given key. */
			final CacheEntry<V> entry = getEntry( value, duration  );

			if( logger.isLoggable(Level.FINEST) )
			    logger.finest( "Going to PUT " + entry + " into cache with key " + key );

			/*
			 * We insert the new entry into the cache.
			 * Usually the underlying cache system requires
			 * a duration (or expiration) parameter by
			 * itself. To get a good balance between
			 * data availability and space occupation we
			 * send to the underlying caching system
			 * a duration double than the one requested.
			 */
			doPut( key, entry, duration << 1 );

		}catch( Exception ex )
		{

			throw cacheProviderException( "PUT", key, ex );

		}

	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean touch( CacheKey key, long duration )
	{

        logger.log( Level.INFO, "TOUCH request for {0}", key );

		if( duration <= 0 )
		{
			logger.log( Level.INFO, "Requested durations is {0}ms so nothing will be done", duration );
			return false;
		}

		try{

			return doTouch(
				Require.nonNull( key, "The cache key must be not null" ),
				Require.trueFor( duration, duration > 0, "Duration must be > 0" )
			);

		}catch( Exception ex )
		{

			throw cacheProviderException( "TOUCH", key, ex );

		}

	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove( CacheKey key )
	{

		logger.log( Level.INFO, "REMOVE request for {0}", key );
		try{

			Require.nonNull( key, "The cache key must be not null" );

			doRemove( key );

		}catch( Exception ex )
		{

			throw cacheProviderException( "REMOVE", key, ex );

		}

	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear()
	{

		logger.log( Level.INFO, "CLEAR-ALL request" );
		try{

			doClear();

		}catch( Exception ex )
		{

			logger.warning( "Unable to empty cache: " + ex );
			throw new CacheProviderException( "An error occurred while trying to empty the cache", ex );

		}

	}


	/* ***************** */
	/*  PRIVATE METHODS  */
	/* ***************** */

    /**
     * Throws a new {@link CacheProviderException} with the given cause
     * and the given message if the flag
     * {@link CacheConfig#isThrowCacheProviderExceptions()} is {@code true}.
     *
     * @param operation the operation that caused the error.
     * @param key the cache key for which the error occurred.
     * @param cause the original exception.
     * @@return a new {@link CacheProviderException}.
     */
    private CacheProviderException cacheProviderException( String operation, CacheKey key, Exception cause )
    {

        if( logger.isLoggable(Level.WARNING) )
            logger.log(  Level.WARNING, "An error occurred during " + operation + " for " + key, cause );

        return new CacheProviderException( "Unable to perform " + operation + " for key " + key, cause );

    }


	/* ******************* */
	/*  PROTECTED METHODS  */
	/* ******************* */


	/**
	 * Returns a new cache entry with the given value that will
     * expire after the given amount of milliseconds
	 *
	 * @param value    the value to put into the cache entry.
	 * @param duration the amount of milliseconds before expiration.
	 * @return the new cache entry
	 */
	protected CacheEntry<V> getEntry( V value, long duration )
	{

        /* The expiration time of this cache entry. */
        final long expiration
                = Require.trueFor( duration, duration > 0, "Duration must be > 0" )
                + System.currentTimeMillis();

		return new CacheEntry<>( value, expiration );

	}


	/* ***************** */
	/*  EXTENSION HOOKS  */
	/* ***************** */


	/**
	 * Returns the cache entry related to the given key.
	 * <p>
	 * If the given key was never being cached {@code null}
	 * will be returned, otherwise will be returned a
	 * {@link CacheEntry} containing the cached value
	 * and the expiration time.
	 *
	 * @param key the cache key to search for.
	 * @return the entry related to the given key if any, {@code null} otherwise.
	 * @throws Exception if the method execution fails.
	 */
	protected abstract CacheEntry<V> doGet( CacheKey key ) throws Exception;

	/**
	 * Binds the given entry to the given key and
	 * put it into the underlying cache.
	 * <p>
	 * If the given key is already present into the cache
	 * will be replaced.
	 *
	 * @param key      key to be cached.
	 * @param entry    entry to be cached.
	 * @param duration number of milliseconds until expiration.
	 * @throws Exception if the method execution fails.
	 */
	protected abstract void doPut( CacheKey key, CacheEntry<V> entry, long duration ) throws Exception;

	/**
	 * Defers the expiration time of the cache entry related to the given key.
	 * <p>
	 * If no entry is present for the given key a {@link RequirementFailure}
	 * should be thrown.
	 * <p>
	 * Only the first thread calling this method for the given key should be successful,
	 * any other thread should fail. This method returns {@code true} if the operation
	 * was successful and {@code false} otherwise.
	 *
	 * @param key      key to update.
	 * @param duration number of milliseconds until expiration.
	 * @return {@code true} if the related entry has been updated.
	 * @throws Exception if the method execution fails.
	 */
	protected abstract boolean doTouch( CacheKey key, long duration ) throws Exception;

	/**
	 * Removes the given key and the related entry from the cache.
	 * <p>
	 * If the key is not present nothing will be done.
	 *
	 * @param key key to be removed.
	 * @throws Exception if the method execution fails.
	 */
	protected abstract void doRemove( CacheKey key ) throws Exception;

	/**
	 * Removes all keys and related entries from the cache.
	 * <p>
	 * If cache is already empty nothing will be done.
	 *
	 * @throws Exception if the method execution fails.
	 */
	protected abstract void doClear() throws Exception;

}
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

import org.nerd4j.utils.lang.RequirementFailure;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Implementation of the {@link CacheProvider} interface
 * that uses the local heap as the cache storage.
 *
 * <p>
 * This implementation uses a {@link LinkedHashMap}
 * as cache engine and the local heap memory as storage.
 *
 * <p>
 * This class was first implemented using {@link ReadWriteLock}
 * but benchmarks showed that, in this case, synchronization is
 * 6 times faster because the overhead of acquiring the lock is
 * dominant related to the time requested by read/write operations.
 *
 * <p>
 * So, even if {@link ReadWriteLock}s allow concurrent reads, the time to
 * acquire the lock is greater than the time spent to perform a synchronized read.
 *
 * @param <V> data type to be returned by the cache.
 *
 * @author Massimo Coluzzi
 * @since 2.1.0
 */
public class HeapCacheProvider<V> extends AbstractCacheProvider<V>
{

	/**
	 * Logging system.
	 * <p>
	 * By default the logging level is {@link Level#INFO} and will log exceptions
	 * and creation parameters.
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
	public static final Logger logger = Logger.getLogger( HeapCacheProvider.class.getName() );

	/** Minimum number of entries the cache is able to store. */
	private static final int MIN_SIZE = 16;

	/** Default number of entries the cache is able to store. */
	private static final int DEFAULT_SIZE = 128;


	/** The cache storage, performs space occupation and eviction strategies. */
    private final LinkedHashMap<CacheKey,CacheEntry<V>> storage;

    /** The maximum number of entries this cache is able to keep. */
    private final int capacity;


	/**
	 * Default constructor.
	 *
	 */
	public HeapCacheProvider()
	{

		this( DEFAULT_SIZE );

	}

	/**
	 * Constructor with parameters.
	 *
	 * @param size the number of entries the cache is able to store.
	 */
	public HeapCacheProvider( int size )
	{

		super();

        this.capacity = size >= MIN_SIZE ? size : MIN_SIZE;        
        this.storage = createStorage();

		logger.info( "Created a new HeapCacheProvider with capacity " + capacity );

	}


	/* **************** */
	/*  PUBLIC METHODS  */
	/* **************** */


	/**
	 * Returns the number of entries in the cache.
	 *
	 * @return the number of entries in the cache.
	 */
	public synchronized int size()
	{

		return storage.size();

	}

	/**
	 * Returns the capacity of the cache i.e. the maximum number of
	 * entries this cache is able to keep.
	 *
	 * @return the maximum number of entries this cache is able to keep.
	 */
	public synchronized int capacity()
	{

		return capacity;

	}


	/* ***************** */
	/*  PRIVATE METHODS  */
	/* ***************** */


	/**
	 * Returns a new {@code spooling LinkedHashMap} i.e
	 * a {@link LinkedHashMap} removing the oldest element
	 * when the capacity has been reached.
	 *
	 * @return a new {@code SpoolingLinkedHashMap}
	 */
	public synchronized LinkedHashMap<CacheKey,CacheEntry<V>> createStorage()
	{

		/*
		 * We create a new LinkedHashMap overriding the method 'removeEldestEntry'
		 * to force the removal of the eldest entry when the capacity is reached.
		 */
		return new LinkedHashMap<CacheKey,CacheEntry<V>>( MIN_SIZE, 0.75f, true )
		{

            private static final long serialVersionUID = 1L;

			@Override
			protected boolean removeEldestEntry( Map.Entry<CacheKey,CacheEntry<V>> eldest )
			{

				return size() > capacity;

			}

		};

	}


	/* ***************** */
	/*  EXTENSION HOOKS  */
	/* ***************** */


	/**
	 * {@inheritDoc}
	 */
	@Override
	protected synchronized CacheEntry<V> doGet( CacheKey key )
	{

		return storage.get( key );

	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	protected synchronized boolean doTouch( CacheKey key, long duration )
	{

		final CacheEntry<V> currentEntry = get( key );

		/*
		 * By contract this method should be called when
		 * an entry has expired. If the entry does not
		 * exist an exception will be thrown.
		 */
		if( currentEntry == null )
		{

			final String errorMessage = "Entry for key " + key + " is not in cache";

			logger.warning( errorMessage );
			throw new RequirementFailure( errorMessage );

		}

		/*
		 * If the entry exists and is not expired means that:
		 * 1. the contract has been broken.
		 * 2. the entry has been "touched" by another thread short before.
		 * In both cases the operation fails.
		 */
		if( ! currentEntry.isExpired() )
		{

			if( logger.isLoggable(Level.FINE) )
				logger.fine( "Entry for key " + key + " has been already touched." );

			return false;

		}

		/*
		 * Otherwise the current entry
		 * will be replaced by a touched one.
		 */
		final CacheEntry<V> touchedEntry = getEntry( currentEntry.getValue(), duration );
		doPut( key, touchedEntry, duration );

		return true;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected synchronized void doPut( CacheKey key, CacheEntry<V> entry, long duration )
	{

		storage.put( key, entry );

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected synchronized void doRemove( CacheKey key )
	{

		storage.remove( key );

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected synchronized void doClear()
	{

		storage.clear();

	}

}
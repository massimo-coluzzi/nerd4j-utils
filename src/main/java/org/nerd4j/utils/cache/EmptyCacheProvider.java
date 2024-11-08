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

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Dummy implementation of the {@link CacheProvider} interface
 * that implements the design pattern {@code Empty Object}.
 *
 * <p>
 * It provides a caching system where all insertions
 * will take no effect and the cache will always be empty.
 *
 * <p>
 * This implementation can be useful for testing purposes
 * and in those cases where your code is invoking the cache
 * but you do not want to actually cache anything.
 *
 * @param <V> data type to be returned by the cache.
 *
 * @author Massimo Coluzzi
 * @since 2.1.0
 */
public class EmptyCacheProvider<V> extends AbstractCacheProvider<V>
{

	/**
	 * Logging system.
	 * <p>
	 * By default the logging level is {@link Level#WARNING} and will only log exceptions.
	 * <p>
	 * If you want to log any caching operation you need to set the logging level to {@link Level#FINE}.
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
		logger = Logger.getLogger( EmptyCacheProvider.class.getName() );
		logger.setLevel( Level.WARNING );
	}

	/** The singleton instance of the {@link EmptyCacheProvider}. */
	private static final EmptyCacheProvider<?> INSTANCE = new EmptyCacheProvider<>();


	/**
	 * Default constructor.
	 * <p>
	 * This constructor is intended to be used
	 * by extending classes only.
	 * <p>
	 * To get the singleton instance of the
	 * {@link EmptyCacheProvider} use the factory method.
	 */
	protected EmptyCacheProvider()
	{

		super();

	}


	/* ***************** */
	/*  FACTORY METHODS  */
	/* ***************** */


	/**
	 * Returns the singleton instance of the {@link EmptyCacheProvider}.
	 *
	 * @return the singleton instance of the {@link EmptyCacheProvider}.
	 */
	public static EmptyCacheProvider<?> get()
	{

		return INSTANCE;

	}


	/* ***************** */
	/*  EXTENSION HOOKS  */
	/* ***************** */


	/**
	 * {@inheritDoc}
	 */
	@Override
	public CacheEntry<V> doGet( CacheKey key )
	{

		if( logger.isLoggable(Level.FINE) )
        	logger.fine( "call to EmptyCache.get( " + key + " )" );

		return null;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doPut( CacheKey key, CacheEntry<V> entry, long duration )
	{

		if( logger.isLoggable(Level.FINE) )
			logger.fine( "call to EmptyCache.put( " + key + ", " + entry + ", " + duration + "ms )" );

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean doTouch( CacheKey key, long duration )
	{

		if( logger.isLoggable(Level.FINE) )
			logger.fine( "call to EmptyCache.touch( " + key + ", " + duration + "ms )" );

		final String errorMessage = "Unable to touch the cache entry for key " + key + " because it does not exist";

		logger.warning( errorMessage );
		throw new RequirementFailure( errorMessage );

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doRemove( CacheKey key )
	{

		if( logger.isLoggable(Level.FINE) )
			logger.fine( "call to EmptyCache.remove( " + key + " )" );

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doClear()
	{

		if( logger.isLoggable(Level.FINE) )
			logger.fine( "call to EmptyCache.clear()" );

	}

}
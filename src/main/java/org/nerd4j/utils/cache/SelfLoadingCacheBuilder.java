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

/**
 * Implementation of the pattern {@code Builder}
 * to build default instances of the {@link SelfLoadingCache}
 * interface.
 *
 * @param <V> type of the data to store in the cache.
 *
 * @author Massimo Coluzzi
 * @since 2.1.0
 */
public class SelfLoadingCacheBuilder<V>
{


	/** Duration of a cache entry in milliseconds. */
	private long cacheDuration;

	/** Number of milliseconds to defer a cache entry expiration to. */
	private long touchDuration;

	/**
	 * Tells if the insert of a new entry in the cache
	 * should be done asynchronously.
	 */
	private boolean asyncInsert;

	/**
	 * Tells if the update of an existing entry in the
	 * cache should be done asynchronously.
	 */
	private boolean asyncUpdate;

	/**
	 * Tells if any exception generated by the {@link CacheProvider}
	 * should be thrown.
	 */
	private boolean throwCacheProviderExceptions;

	/** The underlying caching system provider. */
	protected final CacheProvider<V> cacheProvider;



	/**
	 * Default constructor.
	 * <p>
	 * This constructor is intended to be used
	 * by reflection during de-serialization.
	 * <p>
	 * To create a new cache configuration
	 * use the factory methods.
	 *
	 * @param cacheProvider the {@link CacheProvider} to be injected into
	 *                      the {@link SelfLoadingCache} created by this builder.
	 * @throws RequirementFailure if the provided {@link CacheProvider} is {@code null}.
	 */
	protected SelfLoadingCacheBuilder( CacheProvider<V> cacheProvider )
	{

		super();

		this.cacheProvider = Require.nonNull( cacheProvider, "The cache provider cannot be null" );

		final CacheConfig defaultConfig = CacheConfig.getDefault();
		this.cacheDuration = defaultConfig.getCacheDuration();
		this.touchDuration = defaultConfig.getTouchDuration();
		this.asyncInsert   = defaultConfig.isAsyncInsert();
		this.asyncUpdate   = defaultConfig.isAsyncUpdate();
		this.throwCacheProviderExceptions = defaultConfig.isThrowCacheProviderExceptions();

	}


	/* ***************** */
	/*  FACTORY METHODS  */
	/* ***************** */


	/**
	 * Creates a new {@link SelfLoadingCacheBuilder}
	 * with the given {@link CacheProvider} and configurations
	 * taken from {@link CacheConfig#getDefault()}.
	 *
	 * @param <V> the type of data to store in the cache.
	 * @param cacheProvider the cache provider to set into the {@link SelfLoadingCache}
	 * @return a new instance of this builder.
	 * @throws RequirementFailure if the provided parameter is {@code null}.
	 */
	static <V> SelfLoadingCacheBuilder<V> withCacheProvider( CacheProvider<V> cacheProvider )
	{

		return new SelfLoadingCacheBuilder<>( cacheProvider );

	}


	/* *********************** */
	/*  CONFIGURATION METHODS  */
	/* *********************** */


	/**
	 * Sets the given value for the cache duration.
	 * <p>
	 * By default this values is set to 1 hour (3600000 ms).
	 *
	 * @param cacheDuration the cache duration to set.
	 * @throws RequirementFailure if the duration value is less or equal to zero.
	 */
	public void setCacheDuration( long cacheDuration )
	{

		this.cacheDuration = Require.trueFor( cacheDuration, cacheDuration > 0, "The cache duration must be > 0" );

	}

	/**
	 * Sets the given value for the cache duration
	 * and returns this builder instance.
	 * <p>
	 * By default this values is set to 1 hour (3600000 ms).
	 *
	 * @param cacheDuration the cache duration to set.
	 * @return this builder instance.
	 * @throws RequirementFailure if the duration value is less or equal to zero.
	 */
	public SelfLoadingCacheBuilder<V> withCacheDuration( long cacheDuration )
	{

		this.setCacheDuration( cacheDuration );
		return this;

	}

	/**
	 * Sets the given value for the touch duration.
	 * <p>
	 * By default this values is set to 10 minutes (600000 ms).
	 *
	 * @param touchDuration the touch duration to set.
	 * @throws RequirementFailure if the duration value is less or equal to zero.
	 */
	public void setTouchDuration( long touchDuration )
	{

		this.touchDuration = Require.trueFor( touchDuration, touchDuration > 0, "The touch duration must be > 0" );

	}

	/**
	 * Sets the given value for the touch duration
	 * and returns this builder instance.
	 * <p>
	 * By default this values is set to 10 minutes (600000 ms).
	 *
	 * @param touchDuration the touch duration to set.
	 * @return this builder instance.
	 * @throws RequirementFailure if the duration value is less or equal to zero.
	 */
	public SelfLoadingCacheBuilder<V> withTouchDuration( long touchDuration )
	{

		this.setTouchDuration( touchDuration );
		return this;

	}

	/**
	 * Sets the given flag to tell if the cache
	 * should insert new keys asynchronously.
	 * <p>
	 * By default this value is {@code false} forcing the
	 * requesting thread to load and return the expected data.
	 *
	 * @param asyncInsert asynchronous insert flag.
	 */
	public void setAsyncInsert( boolean asyncInsert )
	{

		this.asyncInsert = asyncInsert;

	}

	/**
	 * Sets the given flag to tell if the cache
	 * should insert new keys asynchronously and
	 * returns this builder instance.
	 * <p>
	 * By default this value is {@code false} forcing the
	 * requesting thread to load and return the expected data.
	 *
	 * @param asyncInsert asynchronous insert flag.
	 * @return this builder instance.
	 */
	public SelfLoadingCacheBuilder<V> withAsyncInsert( boolean asyncInsert )
	{

		this.setAsyncInsert( asyncInsert );
		return this;

	}

	/**
	 * Sets the given flag to tell if the cache
	 * should update existing keys asynchronously.
	 * <p>
	 * By default this value is {@code true} allowing
	 * the current thread to return immediately the cached value.
	 *
	 * @param asyncUpdate asynchronous update flag.
	 */
	public void setAsyncUpdate( boolean asyncUpdate )
	{

		this.asyncUpdate = asyncUpdate;

	}

	/**
	 * Sets the given flag to tell if the cache
	 * should update existing keys asynchronously
	 * and returns this builder instance.
	 * <p>
	 * By default this value is {@code true} allowing
	 * the current thread to return immediately the cached value.
	 *
	 * @param asyncUpdate asynchronous update flag.
	 * @return this builder instance.
	 */
	public SelfLoadingCacheBuilder<V> withAsyncUpdate( boolean asyncUpdate )
	{

		this.setAsyncUpdate( asyncUpdate );
		return this;

	}

	/**
	 * Sets the given flag to tell if the cache
	 * should propagate cache related exceptions.
	 * <p>
	 * By default this values is {@code false} because
	 * an error in the caching system should not cause
	 * a crash in the application.
	 *
	 * @param throwCacheProviderExceptions cache related propagation flag.
	 */
	public void setThrowCacheProviderExceptions( boolean throwCacheProviderExceptions )
	{

		this.throwCacheProviderExceptions = throwCacheProviderExceptions;

	}

	/**
	 * Sets the given flag to tell if the cache
	 * should propagate cache related exceptions
	 * and returns this builder instance.
	 * <p>
	 * By default this values is {@code false} because
	 * an error in the caching system should not cause
	 * a crash in the application.
	 *
	 * @param throwCacheProviderExceptions cache related propagation flag.
	 * @return this builder instance.
	 */
	public SelfLoadingCacheBuilder<V> withThrowCacheProviderExceptions( boolean throwCacheProviderExceptions )
	{

		this.setThrowCacheProviderExceptions( throwCacheProviderExceptions );
		return this;

	}


	/* **************** */
	/*  PUBLIC METHODS  */
	/* **************** */


	/**
	 * Creates a new default implementation of the
	 * {@link SelfLoadingCache} interface.
	 *
	 * @return new default {@link SelfLoadingCache}.
	 */
	public SelfLoadingCache<V> build()
	{

		final CacheConfig cacheConfig = CacheConfig.of(
			cacheDuration, touchDuration,
			asyncInsert, asyncUpdate,
			throwCacheProviderExceptions
		);

		return new DefaultSelfLoadingCache<>(
			cacheConfig, cacheProvider
		);

	}


	/* *************** */
	/*  INNER CLASSES  */
	/* *************** */


	/**
	 * The default implementation of the {@link SelfLoadingCache} interface
	 * returned by this {@code builder}.
	 *
	 * @param <V> type of the data model to cache.
	 */
	private static class DefaultSelfLoadingCache<V> extends AbstractSelfLoadingCache<V>
	{

		/**
		 * Constructor with parameters.
		 *
		 * @param cacheConfig   configurations to be used to define the cache behavior.
		 * @param cacheProvider underlying caching system provider.
		 */
		private DefaultSelfLoadingCache( CacheConfig cacheConfig, CacheProvider<V> cacheProvider )
		{

			super( cacheConfig, cacheProvider );

		}

	}


}
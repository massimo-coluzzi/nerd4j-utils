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


/**
 * Represents a cache facade able to query and
 * populate the underlying caching system.
 * <p>
 * This facade queries the underlying {@link CacheProvider}
 * for the given key, if the related entry is {@code null}
 * or expired uses the given {@link DataProvider} to retrieve
 * the related data and populate the cache.
 *
 * <p>
 * The assumption behind this interface is that the
 * given key is able to uniquely identify the related
 * data and therefore contains all the information
 * needed to retrieve it.
 *
 * <p>
 * If a given key is not present in the cache uses the given
 * {@link DataProvider} to retrieve the related data, if the
 * key is present but expired will be marked as still valid
 * and the {@link DataProvider} will be used to update the
 * related value.
 *
 * @param <V> type of the related data.
 *
 * @author Massimo Coluzzi
 * @since 2.1.0
 */
public interface SelfLoadingCache<V>
{


	/* ******************* */
	/*  INTERFACE METHODS  */
	/* ******************* */


	/**
	 * Returns the value related to the given key.
	 * <p>
	 * If the given key is not present in the cache or
	 * the related entry is expired the given
	 * {@link DataProvider} will be used to retrieve
	 * the data to store into the cache.
	 *
	 * @param key          the key to search for.
	 * @param dataProvider the provider of the information to cache.
	 * @return the value related to the given key, can be {@code null}.
	 */
	V get( CacheKey key, DataProvider<V> dataProvider );

	/**
	 * Removes the given key from the underlying cache system
	 * forcing to reload it the next time {@link #get(CacheKey, DataProvider)}
	 * is invoked.
	 *
	 * @param key key to be evicted.
	 */
	void evict( CacheKey key );

	/**
	 * Invoking this method with {@code true} will disable this {@link SelfLoadingCache}
	 * instance. All the caching will be skipped and all data will be loaded from the
	 * {@link DataProvider} on each request.
	 * <p>
	 * This option should be used to debugging purposes only, using this option
	 * in production can be very dangerous.
	 * <p>
	 * Invoking this method with {@code false} will restore the caching process.
	 *
	 * @param disable tells if this {@link SelfLoadingCache} instance needs to stop
	 *                all caching operations.
	 */
	void disableThis( boolean disable );

	/**
	 * Invoking this method with {@code true} will disable all {@link SelfLoadingCache}
	 * instances in the system. All the caching will be skipped and all data will be
	 * loaded from the {@link DataProvider} on each request.
	 * <p>
	 * This option should be used to debugging purposes only, using this option
	 * in production can be very dangerous.
	 * <p>
	 * Invoking this method with {@code false} will restore the caching process.
	 *
	 * @param disable tells if all {@link SelfLoadingCache} instances need to stop
	 *                all caching operations.
	 */
	static void disableAll( boolean disable )
	{

		CacheConfig.disableAll( disable );

	}

}
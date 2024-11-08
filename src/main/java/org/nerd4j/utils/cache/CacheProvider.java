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
 * Represents a provider of a caching system.
 *
 * <p>
 * Usually a caching system returns the value related to a given key
 * if it is present and still valid and {@code null} if it is not
 * present or expired. The implementations of this interface need
 * to return {@code null} if the given key is not present and an
 * object of type {@link CacheEntry} otherwise. The {@link CacheEntry}
 * need to be returned even if {@link CacheEntry#isExpired()} is {@code true}.
 *
 * <p>
 * The concept behind this choice is to perform the following behavior:
 * <ol>
 *  <li>
 *   if the key is not present (i.e. is never been cached)
 *   the related value will be retrieved and inserted.
 *  </li>
 *  <li>
 *   if the key is present and still valid, the related value will be returned.
 *  </li>
 *  <li>
 *   if the key is present but expired the expiration time will be updated
 *   (calling {@link CacheProvider#touch(CacheKey,long)}) then the value
 *   will be updated.
 *   The best practice to perform this point is to set the expected expiration
 *   time in the {@link CacheEntry} while the actual cache will expire later.
 *  </li>
 * </ol>
 *
 * <p>
 * If more threads are requesting the same key and the key is expired, only the
 * first thread invoking {@link CacheProvider#touch(CacheKey,long)} will
 * succeed, for the other threads the invocation will fail.
 * This way the first thread will update the value related to the key
 * while the other threads will consider the cached value as still valid.
 *
 * <p>
 * If {@link CacheProvider#touch(CacheKey,long)} is called for a key
 * not present in the cache a {@link CacheProviderException} will be thrown.
 *
 * @param <V> type of the objects stored in the cache.
 *
 * @author Massimo Coluzzi
 * @since 2.1.0
 */
public interface CacheProvider<V>
{

	/**
	 * Returns the cache entry related to the given key.
	 * <p>
	 * If the given key is never been cached {@code null} will be returned,
	 * otherwise an object of type {@link CacheEntry} will be returned
	 * containing the related value and expiration time.
	 * The value in the {@link CacheEntry} can be {@code null}.
	 *
	 * @param key the key to search for.
	 * @return the related entry if present, {@code null} otherwise.
	 * @throws CacheProviderException if an error occurs during the operation.
	 */
	CacheEntry<V> get( CacheKey key );

	/**
	 * Inserts the given key and the related value into the cache.
	 * <p>
	 * If the given key is already present, the related {@link CacheEntry}
     * will be replaced.
     * <p>
     * The duration is expressed in milliseconds. If the duration is {@code 0}
     * or less nothing will be done.
     *
	 *
	 * @param key      key to be cached.
	 * @param value    value to be cached.
	 * @param duration the amount of time the value will last in cache.
	 * @throws CacheProviderException if an error occurs during the operation.
	 */
	void put( CacheKey key, V value, long duration );

	/**
	 * Defers the expiration time of the entry related to the given key.
	 * <p>
	 * If the key is not present a {@link CacheProviderException}
	 * will be thrown.
     * <p>
     * The duration is expressed in milliseconds. If the duration is {@code 0}
     * or less nothing will be done.
	 *
	 * @param key      the key to search for.
	 * @param duration number of milliseconds until expiration.
	 * @return {@code true} if the operation was successful, @code false} otherwise.
     * @throws CacheProviderException if an error occurs during the operation.
	 */
	boolean touch( CacheKey key, long duration );

	/**
	 * Removes the given key from the cache.
	 * <p>
	 * If the key is not present nothing will be done.
	 *
	 * @param key    the key to be removed.
	 * @throws CacheProviderException if an error occurs during the operation.
	 */
	void remove( CacheKey key );

	/**
	 * Removes all elements from the cache.
	 * <p>
	 * This method will clear the underlying cache.
	 *
	 * @throws CacheProviderException if an error occurs during the operation.
	 */
	void clear();

}
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


import org.nerd4j.utils.lang.Equals;
import org.nerd4j.utils.lang.Hashcode;
import org.nerd4j.utils.lang.Require;
import org.nerd4j.utils.lang.ToString;

import java.io.Serializable;
import java.time.Instant;

/**
 * Represents an entry in the caching system related to a given key.
 *
 * <p>
 * This entry contains the actual data to be cached and an expiration token.
 *
 * <p>
 * The expiration token is used internally by the {@link SelfLoadingCache} to
 * decide if the entry needs to be updated. This allows to update che cache entry
 * asynchronously while the cache keep serving the stored data.
 *
 *
 * @param <V> type of data in the cache entry.
 *
 * @author Massimo Coluzzi
 * @since 2.1.0
 */
public class CacheEntry<V> implements Serializable
{

	/** The serial version UID required by {@link Serializable}. */
	private static final long serialVersionUID = 1L;


	/** Value of the current cache entry. */
	private final V value;

	/** Unix Timestamp (in milliseconds) that tells when the entry expires. */
	private final long expiration;

	/**
	 * This field will keep the expiration instant once generated
	 * to avoid the creation of a new {@link Instant} class at each
	 * request. But this field is marked as {@code transient} and
	 * therefore will not be serialised.
	 */
	private transient Instant expirationInstant;

	/**
	 * This field will keep the output of the method {@ling #toString()}
	 * to avoid the creation of a new {@link String} at each invocation.
	 * But this field is marked as {@code transient} and therefore will
	 * not be serialised.
	 */
	private transient String toStringOutcome;


    /**
     * Default constructor.
     * <p>
     * This constructor is intended to be used
     * by reflection during de-serialization.
     * <p>
     * To create a new cache entry
     * use the factory methods.
     */
	protected CacheEntry()
	{

		super();

		this.value = null;
		this.expiration = 0L;
		this.toStringOutcome = null;
		this.expirationInstant = null;

	}


	/**
	 * Constructor with parameters.
	 * <p>
     * This constructor is intended to be used
     * by extending classes only.
     * <p>
     * To create a new cache entry
     * use the factory methods.
     *
	 * @param value      value to be cached.
	 * @param expiration Unix Timestamp (in milliseconds) that tells when the entry expires.
	 */
	protected CacheEntry( V value, long expiration )
	{

		super();

		this.value = value;
		this.expiration = expiration;
		this.toStringOutcome = null;
		this.expirationInstant = null;

	}

	/**
	 * Constructor with parameters.
	 * <p>
     * This constructor is intended to be used
     * by extending classes only.
     * <p>
     * To create a new cache entry
     * use the factory methods.
     *
	 * @param value      value to be cached.
	 * @param expiration instant in time when the cache entry expires.
	 */
	protected CacheEntry( V value, Instant expiration )
	{

		super();

		this.value = value;
		this.expiration = expiration.toEpochMilli();
		this.toStringOutcome = null;
		this.expirationInstant = expiration;

	}


	/* ***************** */
	/*  FACTORY METHODS  */
	/* ***************** */


	/**
	 * Creates a new {@link CacheEntry} with the given value and duration.
	 *
	 * @param <V>        type of the value to put into cache.
	 * @param value      the value to put into cache.
	 * @param expiration Unix Timestamp (in milliseconds) when the entry expires.
	 * @return a new {@link CacheEntry} with the given value and duration.
	 */
	public static <V> CacheEntry<V> of( V value, long expiration )
	{

		return new CacheEntry<V>( value, expiration );

	}

	/**
	 * Creates a new {@link CacheEntry} with the given value and duration.
	 * <p>
	 * The value can also be {@code null} while the expiration instant
	 * must be not {@code null}
	 *
	 * @param <V>        type of the value to put into cache.
	 * @param value      the value to put into cache.
	 * @param expiration instant in time when the cache entry expires.
	 * @return a new {@link CacheEntry} with the given value and duration.
	 */
	public static <V> CacheEntry<V> of( V value, Instant expiration )
	{

		return new CacheEntry<V>(
			value, Require.nonNull( expiration, "The instant of expiration is mandatory" )
		);

	}


	/* *********************** */
	/*  CONFIGURATION METHODS  */
	/* *********************** */


    /**
     * Returns the cached value.
     *
     * @return the cached value.
     */
	public V getValue()
	{
		return value;
	}

    /**
     * Returns the instant in time when the cache entry expires.
	 *
     * @return the instant in time when the cache entry expires.
     */
	public Instant getExpiration()
	{

		if( expirationInstant == null )
			expirationInstant = Instant.ofEpochMilli( expiration );

		return expirationInstant;

	}

	/**
	 * Tells if the cache entry is expired.
	 *
	 * @return {@code true} if the cache entry is expired.
	 */
	public boolean isExpired()
	{

		return expiration < System.currentTimeMillis();

	}


	/* ****************** */
	/*  OBJECT OVERRIDES  */
	/* ****************** */


	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{

		return Hashcode.of( expiration, value );

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals( Object other )
	{

		return Equals.ifSameClass(
			this, other,
			o -> o.expiration,
			o -> o.value
		);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{

		if( toStringOutcome == null )
			toStringOutcome = ToString.of( this )
				.print( "expiration", getExpiration() )
				.print( "value", value )
				.likeEclipse();

		return toStringOutcome;

	}

}

/*-
 * #%L
 * Nerd4j Utils
 * %%
 * Copyright (C) 2011 - 2019 Nerd4j
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.nerd4j.utils.lang.RequirementFailure;
import org.nerd4j.utils.test.ObjectOverridesContract;

/**
 * Test suite for the class {@link CacheEntry}
 *
 * @author Massimo Coluzzi
 */
@DisplayName("Testing class: CacheEntry")
@DisplayNameGeneration(ReplaceUnderscores.class)
public class CacheEntryTests implements ObjectOverridesContract<CacheEntry<?>>
{


	/* ******************* */
	/*  INTERFACE METHODS  */
	/* ******************* */


	/**
	 * {@inheritDoc}
	 */
	@Override
	public CacheEntry<?> sampleValue()
	{

		return CacheEntry.of( "test", 1588362875085L );

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CacheEntry<?> notEqualValue()
	{

		return CacheEntry.of( "test", 1588362875086L );

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CacheEntry<?> equalButNotSameValue()
	{

		return CacheEntry.of( "test", 1588362875085L );

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CacheEntry<?> withDifferentHashcode()
	{

		return CacheEntry.of( "test", 1588362875086L );

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String sampleToString()
	{

		return "CacheEntry[expiration=2020-05-01T19:54:35.085Z, value=test]";

	}
	
	
	/* ************** */
	/*  TEST METHODS  */
	/* ************** */


	/**
     * Performs unit test on the {@link CacheEntry} factory methods.
     */
    @Nested
    @DisplayName("on cache entry factory methods")
    public class CacheKeyFactoryMethods
    {

    	@Test
		public void null_is_a_valid_value()
		{

			final long expiration = System.currentTimeMillis();
			final Instant instant = Instant.ofEpochMilli( expiration );
			final CacheEntry<?> entry = CacheEntry.of( null, expiration );

			assertNotNull( entry );
			assertNull( entry.getValue() );
			assertEquals( instant, entry.getExpiration() );

		}

    	@Test
		public void expiration_instant_cannot_be_null()
		{

			assertThrows( RequirementFailure.class, () -> CacheEntry.of(null, null) );

		}

    	@Test
		public void the_cache_entry_values_should_be_stored_properly()
		{

			final Instant expiration = Instant.now();
			final CacheEntry<String> entry = CacheEntry.of( "test", expiration );

			assertNotNull( entry );

			assertNotNull( entry.getValue() );
			assertSame( "test", entry.getValue() );

			assertNotNull( entry.getExpiration() );
			assertSame( expiration, entry.getExpiration() );

		}

		@Test
		public void the_expiration_instant_should_be_cached()
		{

			final CacheEntry<String> entry = CacheEntry.of( "test", Instant.now() );
			final Instant expiration = entry.getExpiration();

			assertSame( expiration, entry.getExpiration() );

		}

		@Test
		public void when_the_expiration_timestamp_is_reached_the_entry_expires()
		throws InterruptedException
		{

			final long expiration = System.currentTimeMillis() + 1;
			final CacheEntry<String> entry = CacheEntry.of( "test", expiration );

			assertFalse( entry.isExpired() );
			TimeUnit.MILLISECONDS.sleep( 2 );
			assertTrue( entry.isExpired() );

		}

		@Test
		public void when_the_expiration_instant_is_reached_the_entry_expires()
		throws InterruptedException
		{

			final Instant expiration = Instant.now().plusMillis( 1 );
			final CacheEntry<String> entry = CacheEntry.of( "test", expiration );

			assertFalse( entry.isExpired() );
			TimeUnit.MILLISECONDS.sleep( 2 );
			assertTrue( entry.isExpired() );

		}

		@Test
		public void factory_method_by_timestamp_should_create_a_new_instance_at_any_invocation()
		{

			final long expiration = System.currentTimeMillis();

			final CacheEntry<String> cacheEntry1 = CacheEntry.of( "test", expiration );
			final CacheEntry<String> cacheEntry2 = CacheEntry.of( "test", expiration );

			assertNotNull( cacheEntry1 );
			assertNotNull( cacheEntry2 );

			assertEquals( cacheEntry1, cacheEntry2 );
			assertNotSame( cacheEntry1, cacheEntry2 );

		}

		@Test
		public void factory_method_by_instant_should_create_a_new_instance_at_any_invocation()
		{

			final Instant expiration = Instant.now();

			final CacheEntry<String> cacheEntry1 = CacheEntry.of( "test", expiration );
			final CacheEntry<String> cacheEntry2 = CacheEntry.of( "test", expiration );

			assertNotNull( cacheEntry1 );
			assertNotNull( cacheEntry2 );

			assertEquals( cacheEntry1, cacheEntry2 );
			assertNotSame( cacheEntry1, cacheEntry2 );

		}

    }


	/**
	 * Performs unit test on the {@link CacheEntry} serialization properties.
	 */
	@Nested
	@DisplayName("on serialization and deserialization")
	public class SerializationAndDeserialization
	{

		@Test
		public void cache_entries_have_to_be_serializable() throws IOException, ClassNotFoundException
		{

			final CacheEntry<String> cacheEntry = CacheEntry.of( "test", Instant.now() );

			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			final ObjectOutputStream oos = new ObjectOutputStream( baos );

			oos.writeObject( cacheEntry );
			oos.close();

			final byte[] serialized = baos.toByteArray();

			final ByteArrayInputStream bais = new ByteArrayInputStream( serialized );
			final ObjectInputStream ois = new ObjectInputStream( bais );

			final Object deserialized = ois.readObject();

			assertNotNull( deserialized );
			assertEquals( CacheEntry.class, deserialized.getClass() );
			assertEquals( cacheEntry, deserialized );

		}

		@Test
		public void expiration_instant_should_not_be_serializable() throws IOException, ClassNotFoundException
		{

			final CacheEntry<String> cacheEntry = CacheEntry.of( "test", Instant.now() );
			
			final Instant expiration = cacheEntry.getExpiration();
			assertSame( expiration, cacheEntry.getExpiration() );

			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			final ObjectOutputStream oos = new ObjectOutputStream( baos );
			
			oos.writeObject( cacheEntry );
			oos.close();
			
			final byte[] serialized = baos.toByteArray();
			
			final ByteArrayInputStream bais = new ByteArrayInputStream( serialized );
			final ObjectInputStream ois = new ObjectInputStream( bais );
            
            @SuppressWarnings("unchecked")
			final CacheEntry<String> deserialized = (CacheEntry<String>) ois.readObject();
			final Instant deserializedExpiration = deserialized.getExpiration();
			assertSame( deserializedExpiration, deserialized.getExpiration() );
			
			final Instant expirationInMilliseconds
			= expiration.truncatedTo( ChronoUnit.MILLIS );

			assertEquals( expirationInMilliseconds, deserializedExpiration );
			assertNotSame( expirationInMilliseconds, deserializedExpiration );

		}

	}

}

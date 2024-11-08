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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Test suite for the class {@link AbstractCacheProvider}
 *
 * @author Massimo Coluzzi
 */
@DisplayName("Testing class: AbstractCacheProvider")
@DisplayNameGeneration(ReplaceUnderscores.class)
public class AbstractCacheProviderTests
{

	/** The cache key used in this tests. */
	private static final CacheKey testKey = CacheKey.of( String.class, "test" );
	

	@BeforeAll
	public static void disableLogging()
	{

		AbstractCacheProvider.logger.setLevel( Level.OFF );

	}

	@AfterAll
	public static void enableLogging()
	{

		AbstractCacheProvider.logger.setLevel( Level.WARNING );

	}


	/* ************** */
	/*  TEST METHODS  */
	/* ************** */


	/**
	 * Performs unit test on the method {@link AbstractCacheProvider#get(CacheKey)}.
	 */
	@Nested
	@DisplayName("on method get")
	public class Get
	{

		@Test
		public void if_the_cache_key_is_null_a_CacheProviderException_should_be_thrown()
		{

			final AbstractCacheProvider<?> cacheProvider = new EmptyAbstractCacheProvider<>();

			assertThrows( CacheProviderException.class, () -> cacheProvider.get( null ) );

		}

		@Test
		public void if_the_cache_key_is_NOT_null_the_method_doGet_should_be_invoked()
		{

			final AtomicBoolean invoked = new AtomicBoolean( false );
			final CacheEntry<String> expectedEntry = new CacheEntry<>( "get", 1 );

			final AbstractCacheProvider<String> cacheProvider = new EmptyAbstractCacheProvider<String>()
			{
				@Override
				protected CacheEntry<String> doGet( CacheKey key )
				{

					invoked.set( true );
					assertSame( testKey, key );

					return expectedEntry;

				}
			};

			assertFalse( invoked.get() );

			final CacheEntry<String> entry = cacheProvider.get( testKey );

			assertTrue( invoked.get() );
			assertSame( expectedEntry, entry );

		}

		@Test
		public void any_exception_should_be_wrapped_into_a_CacheProviderException()
		{

			final Exception expectedCause = new Exception( "Cause" );
			final AbstractCacheProvider<String> cacheProvider = new EmptyAbstractCacheProvider<String>()
			{
				@Override
				protected CacheEntry<String> doGet( CacheKey key ) throws Exception
				{
					throw expectedCause;
				}
			};

			assertThrowsCacheProviderException( () -> cacheProvider.get(testKey), expectedCause );

		}

	}

	/**
	 * Performs unit test on the method {@link AbstractCacheProvider#put(CacheKey,Object,long)}.
	 */
	@Nested
	@DisplayName("on method put")
	public class Put
	{

		@Test
		public void if_the_cache_key_is_null_a_CacheProviderException_should_be_thrown()
		{

			final AbstractCacheProvider<String> cacheProvider = new EmptyAbstractCacheProvider<>();

			assertThrows( CacheProviderException.class, () -> cacheProvider.put( null, null, 10 ) );
			assertThrows( CacheProviderException.class, () -> cacheProvider.put( null, "null", 10 ) );

		}

		@ValueSource(longs={Integer.MIN_VALUE,-1,0})
		@ParameterizedTest(name="with duration = {0}")
		public void if_the_duration_is_zero_or_less_nothing_should_be_done( long duration )
		{

			final AtomicBoolean invoked = new AtomicBoolean( false );
			final AbstractCacheProvider<String> cacheProvider = new EmptyAbstractCacheProvider<String>()
			{
				@Override
				protected void doPut( CacheKey key, CacheEntry<String> entry, long duration )
				{
					invoked.set( true );
				}
			};

			assertFalse( invoked.get() );
			cacheProvider.put( testKey, "value", duration );
			assertFalse( invoked.get() );

		}

		@Test
		public void if_the_parameters_are_valid_the_method_doPut_should_be_invoked()
		{

			final String expectedValue = "Put";

			final AtomicBoolean invoked = new AtomicBoolean( false );
			final AbstractCacheProvider<String> cacheProvider = new EmptyAbstractCacheProvider<String>()
			{
				@Override
				protected void doPut( CacheKey key, CacheEntry<String> entry, long duration )
				{
					invoked.set( true );
					assertSame( testKey, key );

					assertNotNull( entry );
					assertFalse( entry.isExpired() );
					assertSame( expectedValue, entry.getValue() );
					assertEquals( 20, duration );
				}
			};

			assertFalse( invoked.get() );
			cacheProvider.put( testKey, expectedValue, 10 );
			assertTrue( invoked.get() );

		}

		@Test
		public void any_exception_should_be_wrapped_into_a_CacheProviderException()
		{

			final Exception expectedCause = new Exception( "Cause" );
			final AbstractCacheProvider<String> cacheProvider = new EmptyAbstractCacheProvider<String>()
			{
				@Override
				protected void doPut( CacheKey key, CacheEntry<String> entry, long duration ) throws Exception
				{
					throw expectedCause;
				}
			};

			assertThrowsCacheProviderException( () -> cacheProvider.put(testKey,"Put",10), expectedCause );

		}

	}


	/**
	 * Performs unit test on the method {@link AbstractCacheProvider#touch(CacheKey,long)}.
	 */
	@Nested
	@DisplayName("on method touch")
	public class Touch
	{

		@Test
		public void if_the_cache_key_is_null_a_CacheProviderException_should_be_thrown()
		{

			final AbstractCacheProvider<String> cacheProvider = new EmptyAbstractCacheProvider<>();

			assertThrows( CacheProviderException.class, () -> cacheProvider.touch( null, 10 ) );

		}

		@ValueSource(longs={Integer.MIN_VALUE,-1,0})
		@ParameterizedTest(name="with duration = {0}")
		public void if_the_duration_is_zero_or_less_nothing_will_be_done( long duration )
		{

			final AtomicBoolean invoked = new AtomicBoolean( false );
			final AbstractCacheProvider<String> cacheProvider = new EmptyAbstractCacheProvider<String>()
			{
				@Override
				protected boolean doTouch( CacheKey key, long duration )
				{
					invoked.set( true );
					return true;
				}
			};

			assertFalse( invoked.get() );
			final boolean touched = cacheProvider.touch( testKey, duration );
			assertFalse( invoked.get() );
			assertFalse( touched );

		}

		@ValueSource(booleans={false,true})
		@ParameterizedTest(name="returning {0}")
		public void if_the_parameters_are_valid_the_method_doTouch_should_be_invoked( boolean expectedValue )
		{

			final AtomicBoolean invoked = new AtomicBoolean( false );
			final AbstractCacheProvider<String> cacheProvider = new EmptyAbstractCacheProvider<String>()
			{
				@Override
				protected boolean doTouch( CacheKey key, long duration )
				{
					invoked.set( true );
					assertSame( testKey, key );
					assertEquals( 10, duration );

					return expectedValue;
				}
			};

			assertFalse( invoked.get() );

			final boolean value = cacheProvider.touch( testKey, 10 );

			assertTrue( invoked.get() );
			assertEquals( expectedValue, value );

		}

		@Test
		public void any_exception_should_be_wrapped_into_a_CacheProviderException()
		{

			final Exception expectedCause = new Exception( "Cause" );
			final AbstractCacheProvider<String> cacheProvider = new EmptyAbstractCacheProvider<String>()
			{
				@Override
				protected boolean doTouch( CacheKey key, long duration ) throws Exception
				{
					throw expectedCause;
				}
			};

			assertThrowsCacheProviderException( () -> cacheProvider.touch(testKey,10), expectedCause );

		}

	}

	/**
	 * Performs unit test on the method {@link AbstractCacheProvider#remove(CacheKey)}.
	 */
	@Nested
	@DisplayName("on method remove")
	public class Remove
	{

		@Test
		public void if_the_cache_key_is_null_a_CacheProviderException_should_be_thrown()
		{

			final AbstractCacheProvider<String> cacheProvider = new EmptyAbstractCacheProvider<>();

			assertThrows( CacheProviderException.class, () -> cacheProvider.remove( null ) );

		}

		@Test
		public void if_the_cache_key_is_NOT_null_the_method_doRemove_should_be_invoked()
		{

			final AtomicBoolean invoked = new AtomicBoolean( false );
			final AbstractCacheProvider<String> cacheProvider = new EmptyAbstractCacheProvider<String>()
			{
				@Override
				protected void doRemove( CacheKey key )
				{
					invoked.set( true );
					assertSame( testKey, key );
				}
			};

			assertFalse( invoked.get() );
			cacheProvider.remove( testKey );
			assertTrue( invoked.get() );

		}


		@Test
		public void any_exception_should_be_wrapped_into_a_CacheProviderException()
		{

			final Exception expectedCause = new Exception( "Cause" );
			final AbstractCacheProvider<String> cacheProvider = new EmptyAbstractCacheProvider<String>()
			{
				@Override
				protected void doRemove( CacheKey key ) throws Exception
				{
					throw expectedCause;
				}
			};

			assertThrowsCacheProviderException( () -> cacheProvider.remove(testKey), expectedCause );

		}

	}

	/**
	 * Performs unit test on the method {@link AbstractCacheProvider#clear()}.
	 */
	@Nested
	@DisplayName("on method clear")
	public class Clear
	{

		@Test
		public void the_method_doClear_should_be_invoked()
		{

			final AtomicBoolean invoked = new AtomicBoolean( false );
			final AbstractCacheProvider<String> cacheProvider = new EmptyAbstractCacheProvider<String>()
			{
				@Override
				protected void doClear()
				{
					invoked.set( true );
				}
			};

			assertFalse( invoked.get() );
			cacheProvider.clear();
			assertTrue( invoked.get() );

		}

		@Test
		public void any_exception_should_be_wrapped_into_a_CacheProviderException()
		{

			final Exception expectedCause = new Exception( "Cause" );
			final AbstractCacheProvider<String> cacheProvider = new EmptyAbstractCacheProvider<String>()
			{
				@Override
				protected void doClear() throws Exception
				{
					throw expectedCause;
				}
			};

			assertThrowsCacheProviderException( () -> cacheProvider.clear(), expectedCause );

		}

	}


	/* **************** */
	/*  HELPER METHODS  */
	/* **************** */


	/**
	 * Expects the given executable to throw a {@link CacheProviderException}
	 * with the given cause.
	 *
	 * @param executable the executable to check.
	 * @param expectedCause the expected cause.
	 */
	private void assertThrowsCacheProviderException( Executable executable, Exception expectedCause )
	{

		try{

			executable.execute();
			fail( "Expected exception but nothing was thrown" );

		}catch( Throwable ex )
		{

			assertTrue( ex instanceof CacheProviderException );
			assertSame( expectedCause, ex.getCause() );

		}

	}


	/* *************** */
	/*  INNER CLASSES  */
	/* *************** */


	/**
	 * Empty implementation of the {@link AbstractCacheProvider} to be used in this tests.
	 */
	private static class EmptyAbstractCacheProvider<V> extends AbstractCacheProvider<V>
	{

		@Override
		protected CacheEntry<V> doGet( CacheKey key ) throws Exception
		{
			return null;
		}

		@Override
		protected void doPut( CacheKey key, CacheEntry<V> entry, long duration ) throws Exception
		{

		}

		@Override
		protected boolean doTouch( CacheKey key, long duration ) throws Exception
		{
			return false;
		}

		@Override
		protected void doRemove( CacheKey key ) throws Exception
		{

		}

		@Override
		protected void doClear() throws Exception
		{

		}

	}

}

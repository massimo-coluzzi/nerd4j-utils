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

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.nerd4j.utils.tuple.Pair;

import java.util.Random;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for the class {@link HeapCacheProvider}
 *
 * @author Massimo Coluzzi
 */
@DisplayName("Testing class: HeapCacheProvider")
@DisplayNameGeneration(ReplaceUnderscores.class)
public class HeapCacheProviderTests
{

	/** The prototype of all cache keys used in this tests. */
	private static final CacheKey.Prototype testKey = CacheKey.prototype( String.class, "test" );


	@BeforeAll
	public static void disableLogging()
	{

		AbstractCacheProvider.logger.setLevel( Level.OFF );
		HeapCacheProvider.logger.setLevel( Level.OFF );

	}

	@AfterAll
	public static void enableLogging()
	{

		AbstractCacheProvider.logger.setLevel( Level.WARNING );
		HeapCacheProvider.logger.setLevel( Level.WARNING );

	}


	/* ************** */
	/*  TEST METHODS  */
	/* ************** */


	/**
	 * Performs unit test on creation and capacity constraints.
	 */
	@Nested
	@DisplayName("on creation and capacity constraints")
	public class CreationAndCapacityConstraints
	{

		@Test
		public void default_constructor()
		{

			final HeapCacheProvider<?> cacheProvider = new HeapCacheProvider<>();
			assertEquals( 128, cacheProvider.capacity() );
			assertEquals( 0, cacheProvider.size() );

		}

		@CsvSource({
			Integer.MIN_VALUE + ",16",
			"-1,16",
			"0,16",
			"15,16",
			"16,16",
			"20,20",
			Integer.MAX_VALUE + "," + Integer.MAX_VALUE
		})
		@ParameterizedTest(name="new HeapCacheProvider({0}).capacity() = {1}")
		public void the_minimum_capacity_is_16( int providedSize, int expectedCapacity )
		{

			final HeapCacheProvider<?> cacheProvider = new HeapCacheProvider<>( providedSize );
			assertEquals( expectedCapacity, cacheProvider.capacity() );
			assertEquals( 0, cacheProvider.size() );

		}

		@Test
		public void putting_a_new_key_should_increase_the_cache_size()
		{

			final HeapCacheProvider<String> cacheProvider = new HeapCacheProvider<>();
			assertEquals( 0, cacheProvider.size() );

			final CacheKey key = testKey.of();
			cacheProvider.put( key, "value", 10 );

			assertEquals( 1, cacheProvider.size() );

		}

		@Test
		public void putting_the_same_key_should_not_increase_the_cache_size()
		{

			final HeapCacheProvider<String> cacheProvider = new HeapCacheProvider<>();
			assertEquals( 0, cacheProvider.size() );

			final CacheKey key = testKey.of();
			cacheProvider.put( key, "value", 10 );
			assertEquals( 1, cacheProvider.size() );

			cacheProvider.put( key, "value", 10 );
			assertEquals( 1, cacheProvider.size() );

		}

		@Test
		public void once_capacity_has_been_reached_size_should_stop_increasing()
		{

			final HeapCacheProvider<String> cacheProvider = new HeapCacheProvider<>( 16 );
			assertEquals( 0, cacheProvider.size() );

			for( int i = 0; i < 30; ++i )
			{

				final CacheKey key = testKey.of( i );
				cacheProvider.put( key, "value", 10 );
				assertTrue( cacheProvider.size() >= 0 );
				assertTrue( cacheProvider.size() <= 16 );

			}

		}

		@Test
		public void when_capacity_has_been_reached_the_least_frequently_requested_entry_should_be_replaced()
		{

			final HeapCacheProvider<String> cacheProvider = new HeapCacheProvider<>( 16 );
			assertEquals( 0, cacheProvider.size() );

			/* 1. We fill the capacity of the cache. */
			for( int i = 0; i < 16; ++i )
			{

				final CacheKey key = testKey.of( i );
				cacheProvider.put( key, "value", 10 );

			}

			/* 2. We retrieve elements in the cache randomly. */
			Pair<Integer,Integer> min = Pair.of( 16, 16 );
			int[] calls = shuffle( IntStream.range( 0, 16 ).toArray() );
			for( int i = 0; i < 16; ++i )
			{

				min = min.getValue() > calls[i] ? Pair.of( i, calls[i] ) : min;
				final CacheKey key = testKey.of( i );
				for( int j = 0; j < calls[i]; ++ j )
					assertNotNull( cacheProvider.get(key) );

			}

			/* 3. We get the min least requested key. */
			final CacheKey leastRequested = testKey.of( min.getKey() );

			/* 4. When we add a new key. */
			cacheProvider.put( testKey.of(16), "value", 10 );

			/* 5. Then the least requested key should not be in cache anymore. */
			assertNull( cacheProvider.get(leastRequested) );

		}

	}

	/**
	 * Performs unit test on the method {@link HeapCacheProvider#get(CacheKey)}.
	 */
	@Nested
	@DisplayName("on method get")
	public class Get
	{

		@Test
		public void if_the_cache_key_is_null_a_CacheProviderException_should_be_thrown()
		{

			final HeapCacheProvider<?> cacheProvider = new HeapCacheProvider<>();

			assertThrows( CacheProviderException.class, () -> cacheProvider.get( null ) );

		}

		@Test
		public void if_the_entry_is_present_should_be_returned()
		{

			final CacheKey key = testKey.of();
			final HeapCacheProvider<String> cacheProvider = new HeapCacheProvider<>();

			assertNull( cacheProvider.get(key) );

			cacheProvider.put( key, "value", 10 );

			assertNotNull( cacheProvider.get(key) );

		}

	}

	/**
	 * Performs unit test on the method {@link HeapCacheProvider#put(CacheKey,Object,long)}.
	 */
	@Nested
	@DisplayName("on method put")
	public class Put
	{

		@Test
		public void if_the_cache_key_is_null_a_CacheProviderException_should_be_thrown()
		{

			final HeapCacheProvider<String> cacheProvider = new HeapCacheProvider<>();

			assertThrows( CacheProviderException.class, () -> cacheProvider.put( null, null, 10 ) );
			assertThrows( CacheProviderException.class, () -> cacheProvider.put( null, "null", 10 ) );

		}

		@Test
		public void if_the_parameters_are_valid_a_new_cache_entry_should_be_stored()
		{

			final String expectedValue = "Put";
			final CacheKey key = testKey.of();

			final HeapCacheProvider<String> cacheProvider = new HeapCacheProvider<>();

			assertNull( cacheProvider.get(key) );

			cacheProvider.put( key, expectedValue, 10 );
			final CacheEntry<String> entry = cacheProvider.get( key );

			assertNotNull( entry );
			assertSame( expectedValue, entry.getValue() );
			assertFalse( entry.isExpired() );

		}

	}


	/**
	 * Performs unit test on the method {@link HeapCacheProvider#touch(CacheKey,long)}.
	 */
	@Nested
	@DisplayName("on method touch")
	public class Touch
	{

		@Test
		public void if_the_cache_key_is_null_a_CacheProviderException_should_be_thrown()
		{

			final HeapCacheProvider<?> cacheProvider = new HeapCacheProvider<>();

			assertThrows( CacheProviderException.class, () -> cacheProvider.touch( null, 10 ) );

		}

		@Test
		public void if_the_entry_does_not_exist_a_CacheProviderException_should_be_thrown()
		{

			final CacheKey key = testKey.of();
			final HeapCacheProvider<?> cacheProvider = new HeapCacheProvider<>();

			assertNull( cacheProvider.get(key) );
			assertThrows( CacheProviderException.class, () -> cacheProvider.touch( key, 10 ) );

		}

		@Test
		public void if_the_entry_is_not_expired_nothing_should_be_done()
		{

			final CacheKey key = testKey.of();
			final HeapCacheProvider<String> cacheProvider = new HeapCacheProvider<>();

			cacheProvider.put( key, "value", 10 );
			final CacheEntry<String> beforeTouch = cacheProvider.get( key );

			assertNotNull( beforeTouch );
			assertFalse( beforeTouch.isExpired() );

			/* The touch method should return false. */
			assertFalse( cacheProvider.touch(key, 10) );

			final CacheEntry<String> afterTouch = cacheProvider.get( key );

			assertNotNull( afterTouch );
			assertSame( beforeTouch, afterTouch );

		}

		@Test
		public void if_the_entry_has_expired_should_be_replaced() throws Exception
		{

			final CacheKey key = testKey.of();
			final HeapCacheProvider<String> cacheProvider = new HeapCacheProvider<>();

			cacheProvider.put( key, "value", 1 );
			final CacheEntry<String> beforeTouch = cacheProvider.get( key );
			assertNotNull( beforeTouch );

			TimeUnit.MILLISECONDS.sleep( 2 );
			assertTrue( beforeTouch.isExpired() );

			/* The touch method should return true. */
			assertTrue( cacheProvider.touch(key, 10) );

			final CacheEntry<String> afterTouch = cacheProvider.get( key );

			assertNotNull( afterTouch );
			assertFalse( afterTouch.isExpired() );
			assertNotEquals( beforeTouch, afterTouch );

			assertSame( beforeTouch.getValue(), afterTouch.getValue() );
			assertTrue( beforeTouch.getExpiration().isBefore(afterTouch.getExpiration()) );

		}

		@Test
		public void if_more_threads_touch_the_same_key_only_one_should_succeed() throws Exception
		{

			final int threadCount = 5;
			final CyclicBarrier startExecution = new CyclicBarrier( threadCount );
			final CyclicBarrier endExecution = new CyclicBarrier( threadCount + 1 );

			final CacheKey key = testKey.of();
			final HeapCacheProvider<String> cacheProvider = new HeapCacheProvider<>();

			cacheProvider.put( key, "value", 1 );
			final CacheEntry<String> beforeTouch = cacheProvider.get( key );
			assertNotNull( beforeTouch );

			TimeUnit.MILLISECONDS.sleep( 2 );
			assertTrue( beforeTouch.isExpired() );

			final AtomicInteger success = new AtomicInteger( 0 );
			final AtomicInteger failure = new AtomicInteger( 0 );

			final ExecutorService executorService = Executors.newCachedThreadPool();
			for( int i = 0; i < threadCount; ++i )
			{

				executorService.execute( () ->
				{

					try{

						startExecution.await();
						if( cacheProvider.touch(key,10) )
							success.incrementAndGet();
						else
							failure.incrementAndGet();
						endExecution.await();

					}catch( Exception ex ) {}

				});

			}

			/* We await until all task are done. */
			endExecution.await();

			final CacheEntry<String> afterTouch = cacheProvider.get( key );
			assertNotNull( afterTouch );
			assertFalse( afterTouch.isExpired() );

			assertEquals( 1, success.get() );
			assertEquals( threadCount - 1, failure.get() );


		}


	}

	/**
	 * Performs unit test on the method {@link HeapCacheProvider#remove(CacheKey)}.
	 */
	@Nested
	@DisplayName("on method remove")
	public class Remove
	{

		@Test
		public void if_the_cache_key_is_null_a_CacheProviderException_should_be_thrown()
		{

			final HeapCacheProvider<?> cacheProvider = new HeapCacheProvider<>();
			assertThrows( CacheProviderException.class, () -> cacheProvider.remove( null ) );

		}

		@Test
		public void if_the_cache_key_does_not_exist_nothing_should_be_done()
		{

			final HeapCacheProvider<String> cacheProvider = new HeapCacheProvider<>();
			cacheProvider.put( testKey.of(1), "value", 10 );
			assertEquals( 1, cacheProvider.size() );

			final CacheKey key =  testKey.of (2 );
			assertDoesNotThrow( () -> cacheProvider.remove(key) );

			assertNull( cacheProvider.get(key) );
			assertEquals( 1, cacheProvider.size() );

		}

		@Test
		public void if_the_cache_key_exist_should_be_removed()
		{

			final int size = 10;
			final CacheKey key = testKey.of( 1 );
			final HeapCacheProvider<String> cacheProvider = new HeapCacheProvider<>();

			for( int i = 0; i < size; ++i )
				cacheProvider.put( testKey.of(i), "value", 10 );

			assertEquals( size, cacheProvider.size() );
			assertNotNull( cacheProvider.get(key) );

			assertDoesNotThrow( () -> cacheProvider.remove(key) );
			assertNull( cacheProvider.get(key) );
			assertEquals( size - 1, cacheProvider.size() );

		}

	}

	/**
	 * Performs unit test on the method {@link HeapCacheProvider#clear()}.
	 */
	@Nested
	@DisplayName("on method clear")
	public class Clear
	{

		@Test
		public void if_the_cache_is_already_empty_nothing_should_be_done()
		{

			final HeapCacheProvider<String> cacheProvider = new HeapCacheProvider<>();
			assertEquals( 0, cacheProvider.size() );

			assertDoesNotThrow( () -> cacheProvider.clear() );

			assertEquals( 0, cacheProvider.size() );

		}

		@Test
		public void if_the_cache_is_not_empty_should_be_cleared()
		{

			final HeapCacheProvider<String> cacheProvider = new HeapCacheProvider<>();
			assertEquals( 0, cacheProvider.size() );

			final int size = 10;
			for( int i = 0; i < size; ++i )
				cacheProvider.put( testKey.of(i), "vlaue", 10 );

			assertEquals( size, cacheProvider.size() );

			assertDoesNotThrow( () -> cacheProvider.clear() );

			assertEquals( 0, cacheProvider.size() );

		}


	}


	/* **************** */
	/*  HELPER METHODS  */
	/* **************** */


	/**
	 * Performs the shuffle of the given array.
	 *
	 * @param array the array to shuffle.
	 * @return the same array after shuffle.
	 */
	private int[] shuffle( int[] array )
	{

		int ri, temp;
		final Random r = new Random();
		for( int i = array.length - 1; i > 0; --i )
		{

			ri = r.nextInt( i );
			temp = array[i];
			array[i] = array[ri];
			array[ri] = temp;
		}

		return array;

	}

}

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
import org.nerd4j.utils.lang.RequirementFailure;

import java.util.Random;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for the class {@link SelfLoadingCache}
 *
 * @author Massimo Coluzzi
 */
@DisplayName("Testing class: SelfLoadingCache")
@DisplayNameGeneration(ReplaceUnderscores.class)
public class SelfLoadingCacheTests
{


	/** The cache key used in this tests. */
	private static final CacheKey testKey = CacheKey.of( String.class, "test" );

	/** Random numbers generator. */
	private static final Random random = new Random();


	@BeforeAll
	public static void disableLogging()
	{

	    /* Needed to invoke the static block before changing the logger level. */
		AbstractCacheProvider.logger.setLevel( Level.OFF );
		AbstractSelfLoadingCache.logger.setLevel( Level.OFF );

	}

	@AfterAll
	public static void enableLogging()
	{

		AbstractCacheProvider.logger.setLevel( Level.WARNING );
		AbstractSelfLoadingCache.logger.setLevel( Level.WARNING );

	}


	/* ************** */
	/*  TEST METHODS  */
	/* ************** */


    @Nested
    @DisplayName("with throwCacheProviderExceptions = true")
    public class ThrowingCacheProviderException
    {

        /**
         * Performs unit test on the method {@link SelfLoadingCache#get(CacheKey, DataProvider)}.
         */
        @Nested
        @DisplayName("on method get")
        public class Get
        {

            @Test
            public void if_the_cache_key_is_null_a_RequirementFailure_should_be_thrown()
            {

                final MockCacheProvider<?> cacheProvider = new MockCacheProvider<>();
                final SelfLoadingCache<?> cache = SelfLoadingCacheBuilder
                        .withCacheProvider( cacheProvider )
						.withAsyncInsert( false )
						.withAsyncUpdate( false )
                        .withThrowCacheProviderExceptions( true )
                        .build();

                assertAll(
					() -> assertThrows( RequirementFailure.class, () -> cache.get( null, null ) ),
                    () -> assertThrows( RequirementFailure.class, () -> cache.get( null, key -> null ) )
                );

				assertEquals( 0, cacheProvider.totalInvocations );

            }

            @Test
            public void if_the_data_provider_is_null_a_RequirementFailure_should_be_thrown()
            {

                final MockCacheProvider<?> cacheProvider = new MockCacheProvider<>();
                final SelfLoadingCache<?> cache = SelfLoadingCacheBuilder
                        .withCacheProvider( cacheProvider )
						.withAsyncInsert( false )
						.withAsyncUpdate( false )
                        .withThrowCacheProviderExceptions( true )
                        .build();

                assertThrows( RequirementFailure.class, () -> cache.get( testKey, null ) );
                assertEquals( 0, cacheProvider.totalInvocations );

            }

            @Test
            public void if_the_method_CacheProvider_get_throws_an_exception_should_be_propagated()
            {

				final String expectedValue = "value_" + random.nextInt();
                final MockCacheProvider<String> cacheProvider = new MockCacheProvider<String>()
				{
					@Override
					protected CacheEntry<String> doGet( CacheKey key ) throws Exception
					{
						super.doGet( key );
						throw new Exception();
					}
				};

                final SelfLoadingCache<String> cache = SelfLoadingCacheBuilder
                        .withCacheProvider( cacheProvider )
						.withAsyncInsert( false )
						.withAsyncUpdate( false )
                        .withThrowCacheProviderExceptions( true )
                        .build();

				assertEquals( 0, cacheProvider.totalInvocations );

				final AtomicBoolean invoked = new AtomicBoolean( false );
				final DataProvider<String> dataProvider = key ->
				{
					invoked.set( true );
					return expectedValue;
				};

				assertThrows( CacheProviderException.class, () -> cache.get( testKey, dataProvider ) );

				assertFalse( invoked.get() );

				assertEquals( 1, cacheProvider.getInvocations );
				assertEquals( 1, cacheProvider.totalInvocations );

            }

            @Test
            public void if_the_method_CacheProvider_touch_throws_an_exception_should_be_propagated()
            {

				final String expectedValue = "value_" + random.nextInt();
                final MockCacheProvider<String> cacheProvider = new MockCacheProvider<String>()
				{
					@Override
					protected CacheEntry<String> doGet( CacheKey key ) throws Exception
					{
						super.doGet( key );
						return CacheEntry.of( expectedValue, 10 );
					}
					@Override
					protected boolean doTouch( CacheKey key, long duration ) throws Exception
					{
						super.doTouch( key, duration );
						throw new Exception();
					}
				};

                final SelfLoadingCache<String> cache = SelfLoadingCacheBuilder
                        .withCacheProvider( cacheProvider )
						.withAsyncInsert( false )
						.withAsyncUpdate( false )
                        .withThrowCacheProviderExceptions( true )
                        .build();

				assertEquals( 0, cacheProvider.totalInvocations );

				final AtomicBoolean invoked = new AtomicBoolean( false );
				final DataProvider<String> dataProvider = key ->
				{
					invoked.set( true );
					return expectedValue;
				};

				assertThrows( CacheProviderException.class, () -> cache.get( testKey, dataProvider ) );

				assertFalse( invoked.get() );

				assertEquals( 1, cacheProvider.getInvocations );
				assertEquals( 1, cacheProvider.touchInvocations );
				assertEquals( 2, cacheProvider.totalInvocations );

            }

            @Test
            public void if_the_method_CacheProvider_put_throws_an_exception_should_be_propagated()
            {

				final String expectedValue = "value_" + random.nextInt();
                final MockCacheProvider<String> cacheProvider = new MockCacheProvider<String>()
				{
					@Override
					protected CacheEntry<String> doGet( CacheKey key ) throws Exception
					{
						super.doGet( key );
						return CacheEntry.of( expectedValue, 10 );
					}
					@Override
					protected boolean doTouch( CacheKey key, long duration ) throws Exception
					{
						super.doTouch( key, duration );
						return true;
					}
					@Override
					protected void doPut( CacheKey key, CacheEntry<String> entry, long duration ) throws Exception
					{
						super.doPut( key, entry, duration );
						throw new Exception();
					}
				};

                final SelfLoadingCache<String> cache = SelfLoadingCacheBuilder
                        .withCacheProvider( cacheProvider )
						.withAsyncInsert( false )
						.withAsyncUpdate( false )
                        .withThrowCacheProviderExceptions( true )
                        .build();

				assertEquals( 0, cacheProvider.totalInvocations );

				final AtomicBoolean invoked = new AtomicBoolean( false );
				final DataProvider<String> dataProvider = key ->
				{
					invoked.set( true );
					return expectedValue;
				};

				assertThrows( CacheProviderException.class, () -> cache.get( testKey, dataProvider ) );

				assertTrue( invoked.get() );

				assertEquals( 1, cacheProvider.getInvocations );
				assertEquals( 1, cacheProvider.touchInvocations );
				assertEquals( 1, cacheProvider.putInvocations );
				assertEquals( 3, cacheProvider.totalInvocations );

            }

			@CsvSource({
					"true,false",
					"false,true",
					"true,true"
			})
			@ParameterizedTest(name="disabledAll={0},disabledThis={1}")
			public void if_this_cache_or_all_caches_are_disabled_nothing_should_be_done(
					boolean disableAll, boolean disableThis
			)
			{

				final String loadedValue = "loaded_value_" + random.nextInt();
				final MockCacheProvider<String> cacheProvider = new MockCacheProvider<String>()
				{
					@Override
					protected CacheEntry<String> doGet( CacheKey key ) throws Exception
					{
						super.doGet( key );
						throw new Exception();
					}
				};

				final SelfLoadingCache<String> cache = SelfLoadingCacheBuilder
						.withCacheProvider( cacheProvider )
						.withAsyncInsert( false )
						.withAsyncUpdate( false )
						.build();

				SelfLoadingCache.disableAll( disableAll );
				cache.disableThis( disableThis );

				assertEquals( 0, cacheProvider.totalInvocations );
				final String value = cache.get( testKey, key -> loadedValue );

				assertEquals( 0, cacheProvider.totalInvocations );
				assertSame( loadedValue, value );

			}

        }

		/**
		 * Performs unit test on the method {@link SelfLoadingCache#evict(CacheKey)}.
		 */
		@Nested
		@DisplayName("on method evict")
		public class Evict
		{

			@Test
			public void if_the_method_CacheProvider_remove_throws_an_exception_should_be_propagated()
			{

				final MockCacheProvider<String> cacheProvider = new MockCacheProvider<String>()
				{
					@Override
					protected void doRemove( CacheKey key ) throws Exception
					{
						super.doRemove( key );
						throw new Exception();
					}
				};

				final SelfLoadingCache<String> cache = SelfLoadingCacheBuilder
						.withCacheProvider( cacheProvider )
						.withAsyncInsert( false )
						.withAsyncUpdate( false )
						.withThrowCacheProviderExceptions( true )
						.build();

				assertEquals( 0, cacheProvider.totalInvocations );
				assertThrows( CacheProviderException.class, () -> cache.evict( testKey ) );

				assertEquals( 1, cacheProvider.removeInvocations );
				assertEquals( 1, cacheProvider.totalInvocations );

			}

			@CsvSource({
					"true,false",
					"false,true",
					"true,true"
			})
			@ParameterizedTest(name="disabledAll={0},disabledThis={1}")
			public void if_this_cache_or_all_caches_are_disabled_nothing_should_be_done(
					boolean disableAll, boolean disableThis
			)
			{

				final MockCacheProvider<String> cacheProvider = new MockCacheProvider<String>()
				{
					@Override
					protected CacheEntry<String> doGet( CacheKey key ) throws Exception
					{
						super.doGet( key );
						throw new Exception();
					}
				};

				final SelfLoadingCache<String> cache = SelfLoadingCacheBuilder
						.withCacheProvider( cacheProvider )
						.withAsyncInsert( false )
						.withAsyncUpdate( false )
						.build();

				SelfLoadingCache.disableAll( disableAll );
				cache.disableThis( disableThis );

				assertEquals( 0, cacheProvider.totalInvocations );
				assertDoesNotThrow( () -> cache.evict( testKey ) );

				assertEquals( 0, cacheProvider.removeInvocations );
				assertEquals( 0, cacheProvider.totalInvocations );

			}

		}

    }

    @Nested
    @DisplayName("with throwCacheProviderExceptions = false")
    public class NotThrowingCacheProviderException
    {

        /**
         * Performs unit test on the method {@link SelfLoadingCache#get(CacheKey, DataProvider)}.
         */
        @Nested
        @DisplayName("on method get")
        public class Get
        {

            @Test
            public void if_the_cache_key_is_null_a_RequirementFailure_should_be_thrown()
            {

                final MockCacheProvider<?> cacheProvider = new MockCacheProvider<>();
                final SelfLoadingCache<?> cache = SelfLoadingCacheBuilder.withCacheProvider( cacheProvider ).build();

                assertAll(
					() -> assertThrows( RequirementFailure.class, () -> cache.get( null, null ) ),
					() -> assertThrows( RequirementFailure.class, () -> cache.get( null, key -> null ) )
				);

				assertEquals( 0, cacheProvider.totalInvocations );

            }

            @Test
            public void if_the_data_provider_is_null_a_RequirementFailure_should_be_thrown()
            {

                final MockCacheProvider<?> cacheProvider = new MockCacheProvider<>();
                final SelfLoadingCache<?> cache = SelfLoadingCacheBuilder.withCacheProvider( cacheProvider ).build();

                assertThrows( RequirementFailure.class, () -> cache.get( testKey, null ) );
                assertEquals( 0, cacheProvider.totalInvocations );

            }

            @Test
            public void if_the_method_CacheProvider_get_throws_an_exception_should_NOT_be_propagated()
            {

				final String expectedValue = "value_" + random.nextInt();
                final MockCacheProvider<String> cacheProvider = new MockCacheProvider<String>()
				{
					@Override
					protected CacheEntry<String> doGet( CacheKey key ) throws Exception
					{
						super.doGet( key );
						throw new Exception();
					}
				};

                final SelfLoadingCache<String> cache = SelfLoadingCacheBuilder
						.withCacheProvider( cacheProvider )
						.withAsyncInsert( false )
						.withAsyncUpdate( false )
						.build();

				assertEquals( 0, cacheProvider.totalInvocations );

				final AtomicBoolean invoked = new AtomicBoolean( false );
				final String value = cache.get( testKey, key -> { invoked.set(true); return expectedValue; } );

				assertTrue( invoked.get() );
				assertEquals( expectedValue, value );
				assertEquals( 1, cacheProvider.getInvocations );
				assertEquals( 1, cacheProvider.putInvocations );
				assertEquals( 2, cacheProvider.totalInvocations );

            }

            @Test
            public void if_the_method_CacheProvider_touch_throws_an_exception_should_NOT_be_propagated()
            {

				final String cachedValue = "cached_" + random.nextInt();
				final String expectedValue = "value_" + random.nextInt();
                final MockCacheProvider<String> cacheProvider = new MockCacheProvider<String>()
				{
					@Override
					protected CacheEntry<String> doGet( CacheKey key ) throws Exception
					{
						super.doGet( key );
						return CacheEntry.of( cachedValue, 10 );
					}
					@Override
					protected boolean doTouch( CacheKey key, long duration ) throws Exception
					{
						super.doTouch( key, duration );
						throw new Exception();
					}
				};

				final SelfLoadingCache<String> cache = SelfLoadingCacheBuilder
						.withCacheProvider( cacheProvider )
						.withAsyncInsert( false )
						.withAsyncUpdate( false )
						.build();

				assertEquals( 0, cacheProvider.totalInvocations );

				final AtomicBoolean invoked = new AtomicBoolean( false );
				final String value = cache.get( testKey, key -> { invoked.set(true); return expectedValue; } );

				assertTrue( invoked.get() );
				assertEquals( expectedValue, value );

				assertEquals( 1, cacheProvider.getInvocations );
				assertEquals( 1, cacheProvider.touchInvocations );
				assertEquals( 1, cacheProvider.putInvocations );
				assertEquals( 3, cacheProvider.totalInvocations );

            }

            @Test
            public void if_the_method_CacheProvider_put_throws_an_exception_should_NOT_be_propagated()
            {

				final String expectedValue = "value_" + random.nextInt();
                final MockCacheProvider<String> cacheProvider = new MockCacheProvider<String>()
				{
					@Override
					protected CacheEntry<String> doGet( CacheKey key ) throws Exception
					{
						super.doGet( key );
						return CacheEntry.of( expectedValue, 10 );
					}
					@Override
					protected boolean doTouch( CacheKey key, long duration ) throws Exception
					{
						super.doTouch( key, duration );
						return true;
					}
					@Override
					protected void doPut( CacheKey key, CacheEntry<String> entry, long duration ) throws Exception
					{
						super.doPut( key, entry, duration );
						throw new Exception();
					}
				};

                final SelfLoadingCache<String> cache = SelfLoadingCacheBuilder
						.withCacheProvider( cacheProvider )
						.withAsyncInsert( false )
						.withAsyncUpdate( false )
						.build();

				assertEquals( 0, cacheProvider.totalInvocations );

				final AtomicBoolean invoked = new AtomicBoolean( false );
                final String value = cache.get( testKey, key -> { invoked.set(true); return expectedValue; } );

                assertTrue( invoked.get() );
                assertEquals( expectedValue, value );

				assertEquals( 1, cacheProvider.getInvocations );
				assertEquals( 1, cacheProvider.touchInvocations );
				assertEquals( 1, cacheProvider.putInvocations );
				assertEquals( 3, cacheProvider.totalInvocations );

            }

			@CsvSource({
					"true,false",
					"false,true",
					"true,true"
			})
			@ParameterizedTest(name="disabledAll={0},disabledThis={1}")
			public void if_this_cache_or_all_caches_are_disabled_nothing_should_be_done(
					boolean disableAll, boolean disableThis
			)
			{

				final String cachedValue = "cached_value_" + random.nextInt();
				final String loadedValue = "loaded_value_" + random.nextInt();
				final MockCacheProvider<String> cacheProvider = new MockCacheProvider<String>()
				{
					@Override
					protected CacheEntry<String> doGet( CacheKey key ) throws Exception
					{
						super.doGet( key );
						assertSame( testKey, key );
						return CacheEntry.of( cachedValue, System.currentTimeMillis() + 10 );
					}
				};

				final SelfLoadingCache<String> cache = SelfLoadingCacheBuilder
						.withCacheProvider( cacheProvider )
						.withAsyncInsert( false )
						.withAsyncUpdate( false )
						.build();

				SelfLoadingCache.disableAll( disableAll );
				cache.disableThis( disableThis );

				assertEquals( 0, cacheProvider.totalInvocations );
				final String value = cache.get( testKey, key -> loadedValue );

				assertEquals( 0, cacheProvider.totalInvocations );
				assertSame( loadedValue, value );

			}

        }

		/**
		 * Performs unit test on the method {@link SelfLoadingCache#evict(CacheKey)}.
		 */
		@Nested
		@DisplayName("on method evict")
		public class Evict
		{

			@Test
			public void if_the_method_CacheProvider_remove_throws_an_exception_should_NOT_be_propagated()
			{

				final MockCacheProvider<String> cacheProvider = new MockCacheProvider<String>()
				{
					@Override
					protected void doRemove( CacheKey key ) throws Exception
					{
						super.doRemove( key );
						throw new Exception();
					}
				};

				final SelfLoadingCache<?> cache = SelfLoadingCacheBuilder
						.withCacheProvider( cacheProvider )
						.withAsyncInsert( false )
						.withAsyncUpdate( false )
						.build();;

				assertEquals( 0, cacheProvider.totalInvocations );
				assertDoesNotThrow( () -> cache.evict( testKey ) );

				assertEquals( 1, cacheProvider.removeInvocations );
				assertEquals( 1, cacheProvider.totalInvocations );

			}

			@CsvSource({
					"true,false",
					"false,true",
					"true,true"
			})
			@ParameterizedTest(name="disabledAll={0},disabledThis={1}")
			public void if_this_cache_or_all_caches_are_disabled_nothing_should_be_done(
					boolean disableAll, boolean disableThis
			)
			{

				final MockCacheProvider<String> cacheProvider = new MockCacheProvider<String>()
				{
					@Override
					protected CacheEntry<String> doGet( CacheKey key ) throws Exception
					{
						super.doGet( key );
						throw new Exception();
					}
				};

				final SelfLoadingCache<String> cache = SelfLoadingCacheBuilder
						.withCacheProvider( cacheProvider )
						.withAsyncInsert( false )
						.withAsyncUpdate( false )
						.build();

				SelfLoadingCache.disableAll( disableAll );
				cache.disableThis( disableThis );

				assertEquals( 0, cacheProvider.totalInvocations );
				assertDoesNotThrow( () -> cache.evict( testKey ) );

				assertEquals( 0, cacheProvider.removeInvocations );
				assertEquals( 0, cacheProvider.totalInvocations );

			}

		}

    }


	@Nested
	@DisplayName("using synchronous operations")
	public class WithSynchronousOperations
	{

		/**
		 * Performs unit test on the method {@link SelfLoadingCache#get(CacheKey, DataProvider)}.
		 */
		@Nested
		@DisplayName("on method get")
		public class Get
		{

			@Test
			public void if_the_cache_entry_is_valid_the_related_value_should_be_returned()
			{

				final String cachedValue = "cached_value_" + random.nextInt();
				final MockCacheProvider<String> cacheProvider = new MockCacheProvider<String>()
				{
					@Override
					protected CacheEntry<String> doGet( CacheKey key ) throws Exception
					{
						super.doGet( key );
						assertSame( testKey, key );
						return CacheEntry.of( cachedValue, System.currentTimeMillis() + 10 );
					}
				};

				final SelfLoadingCache<String> cache = SelfLoadingCacheBuilder
						.withCacheProvider( cacheProvider )
						.withAsyncInsert( false )
						.withAsyncUpdate( false )
						.build();

				assertEquals( 0, cacheProvider.totalInvocations );
				final String value = cache.get( testKey, key -> null );

				assertEquals( 1, cacheProvider.getInvocations );
				assertEquals( 1, cacheProvider.totalInvocations );
				assertSame( cachedValue, value );

			}

			@Test
			public void if_the_cache_entry_is_not_stored_should_be_loaded()
			{

				final String loadedValue = "loaded_value_" + random.nextInt();

				final MockCacheProvider<String> cacheProvider = new MockCacheProvider<String>()
				{
					@Override
					protected CacheEntry<String> doGet( CacheKey key ) throws Exception
					{
						super.doGet( key );
						assertSame( testKey, key );
						return null;
					}
					@Override
					protected void doPut( CacheKey key, CacheEntry<String> entry, long duration ) throws Exception
					{
						super.doPut( key, entry, duration );

						assertSame( testKey, key );
						assertSame( loadedValue, entry.getValue() );
						assertEquals( CacheConfig.getDefault().getCacheDuration() << 1, duration );

					}
				};

				final SelfLoadingCache<String> cache = SelfLoadingCacheBuilder
						.withCacheProvider( cacheProvider )
						.withAsyncInsert( false )
						.withAsyncUpdate( false )
						.build();

				assertEquals( 0, cacheProvider.totalInvocations );
				final String value = cache.get( testKey, key -> loadedValue );

				assertEquals( 1, cacheProvider.getInvocations );
				assertEquals( 1, cacheProvider.putInvocations );
				assertEquals( 2, cacheProvider.totalInvocations );
				assertSame( loadedValue, value );

			}

			@Test
			public void if_touch_fails_the_current_value_should_be_returned()
			{

				final String cachedValue = "cached_value_" + random.nextInt();
				final String loadedValue = "loaded_value_" + random.nextInt();

				final MockCacheProvider<String> cacheProvider = new MockCacheProvider<String>()
				{
					@Override
					protected CacheEntry<String> doGet( CacheKey key ) throws Exception
					{
						super.doGet( key );
						assertSame( testKey, key );
						return CacheEntry.of( cachedValue, 10 );
					}
					@Override
					protected boolean doTouch( CacheKey key, long duration ) throws Exception
					{
						super.doTouch( key, duration );
						assertSame( testKey, key );
						assertEquals( CacheConfig.getDefault().getTouchDuration(), duration );
						return false;
					}
				};

				final SelfLoadingCache<String> cache = SelfLoadingCacheBuilder
						.withCacheProvider( cacheProvider )
						.withAsyncInsert( false )
						.withAsyncUpdate( false )
						.build();

				assertEquals( 0, cacheProvider.totalInvocations );
				final String value = cache.get( testKey, key -> loadedValue );

				assertEquals( 1, cacheProvider.getInvocations );
				assertEquals( 1, cacheProvider.touchInvocations );
				assertEquals( 2, cacheProvider.totalInvocations );
				assertSame( cachedValue, value );

			}


			@Test
			public void if_touch_succeeds_the_cache_entry_should_be_updated()
			{

				final String cachedValue = "cached_value_" + random.nextInt();
				final String loadedValue = "loaded_value_" + random.nextInt();

				final MockCacheProvider<String> cacheProvider = new MockCacheProvider<String>()
				{
					@Override
					protected CacheEntry<String> doGet( CacheKey key ) throws Exception
					{
						super.doGet( key );
						assertSame( testKey, key );
						return CacheEntry.of( cachedValue, 10 );
					}
					@Override
					protected boolean doTouch( CacheKey key, long duration ) throws Exception
					{
						super.doTouch( key, duration );
						assertSame( testKey, key );
						assertEquals( CacheConfig.getDefault().getTouchDuration(), duration );
						return true;
					}
					@Override
					protected void doPut( CacheKey key, CacheEntry<String> entry, long duration ) throws Exception
					{
						super.doPut( key, entry, duration );
						assertSame( testKey, key );
						assertSame( loadedValue, entry.getValue() );
						assertEquals( CacheConfig.getDefault().getCacheDuration() << 1, duration );
					}
				};

				final SelfLoadingCache<String> cache = SelfLoadingCacheBuilder
						.withCacheProvider( cacheProvider )
						.withAsyncInsert( false )
						.withAsyncUpdate( false )
						.build();

				assertEquals( 0, cacheProvider.totalInvocations );
				final String value = cache.get( testKey, key -> loadedValue );

				assertEquals( 1, cacheProvider.getInvocations );
				assertEquals( 1, cacheProvider.touchInvocations );
				assertEquals( 1, cacheProvider.putInvocations );
				assertEquals( 3, cacheProvider.totalInvocations );
				assertSame( loadedValue, value );

			}

			@CsvSource({
					"true,false",
					"false,true",
					"true,true"
			})
			@ParameterizedTest(name="disabledAll={0},disabledThis={1}")
			public void if_this_cache_or_all_caches_are_disabled_nothing_should_be_done(
					boolean disableAll, boolean disableThis
			)
			{

				final String cachedValue = "cached_value_" + random.nextInt();
				final String loadedValue = "loaded_value_" + random.nextInt();
				final MockCacheProvider<String> cacheProvider = new MockCacheProvider<String>()
				{
					@Override
					protected CacheEntry<String> doGet( CacheKey key ) throws Exception
					{
						super.doGet( key );
						assertSame( testKey, key );
						return CacheEntry.of( cachedValue, System.currentTimeMillis() + 10 );
					}
				};

				final SelfLoadingCache<String> cache = SelfLoadingCacheBuilder
						.withCacheProvider( cacheProvider )
						.withAsyncInsert( false )
						.withAsyncUpdate( false )
						.build();

				SelfLoadingCache.disableAll( disableAll );
				cache.disableThis( disableThis );

				assertEquals( 0, cacheProvider.totalInvocations );
				final String value = cache.get( testKey, key -> loadedValue );

				assertEquals( 0, cacheProvider.totalInvocations );
				assertSame( loadedValue, value );

			}

		}

		/**
		 * Performs unit test on the method {@link SelfLoadingCache#evict(CacheKey)}.
		 */
		@Nested
		@DisplayName("on method evict")
		public class Evict
		{

			@Test
			public void the_method_CacheProvider_remove_should_be_invoked()
			{

				final MockCacheProvider<String> cacheProvider = new MockCacheProvider<String>()
				{
					@Override
					protected void doRemove( CacheKey key ) throws Exception
					{
						super.doRemove( key );
						assertSame( testKey, key );
					}
				};

				final SelfLoadingCache<?> cache = SelfLoadingCacheBuilder
						.withCacheProvider( cacheProvider )
						.withAsyncInsert( false )
						.withAsyncUpdate( false )
						.build();

				assertEquals( 0, cacheProvider.totalInvocations );
				cache.evict( testKey );

				assertEquals( 1, cacheProvider.removeInvocations );
				assertEquals( 1, cacheProvider.totalInvocations );

			}

			@CsvSource({
					"true,false",
					"false,true",
					"true,true"
			})
			@ParameterizedTest(name="disabledAll={0},disabledThis={1}")
			public void if_this_cache_or_all_caches_are_disabled_nothing_should_be_done(
					boolean disableAll, boolean disableThis
			)
			{

				final MockCacheProvider<String> cacheProvider = new MockCacheProvider<String>()
				{
					@Override
					protected void doRemove( CacheKey key ) throws Exception
					{
						super.doRemove( key );
						assertSame( testKey, key );
					}
				};

				final SelfLoadingCache<?> cache = SelfLoadingCacheBuilder
						.withCacheProvider( cacheProvider )
						.withAsyncInsert( false )
						.withAsyncUpdate( false )
						.build();

				SelfLoadingCache.disableAll( disableAll );
				cache.disableThis( disableThis );

				assertEquals( 0, cacheProvider.totalInvocations );
				cache.evict( testKey );

				assertEquals( 0, cacheProvider.removeInvocations );
				assertEquals( 0, cacheProvider.totalInvocations );

			}

		}
	}

	@Nested
	@DisplayName("using asynchronous operations")
	public class WithAsynchronousOperations
	{

		/**
		 * Performs unit test on the method {@link SelfLoadingCache#get(CacheKey, DataProvider)}.
		 */
		@Nested
		@DisplayName("on method get")
		public class Get
		{

			@Test
			public void if_the_cache_entry_is_valid_the_related_value_should_be_returned()
			{

				final String cachedValue = "cached_value_" + random.nextInt();
				final MockCacheProvider<String> cacheProvider = new MockCacheProvider<String>()
				{
					@Override
					protected CacheEntry<String> doGet( CacheKey key ) throws Exception
					{
						super.doGet( key );
						assertSame( testKey, key );
						return CacheEntry.of( cachedValue, System.currentTimeMillis() + 10 );
					}
				};

				final SelfLoadingCache<String> cache = SelfLoadingCacheBuilder
						.withCacheProvider( cacheProvider )
						.withAsyncInsert( true )
						.withAsyncUpdate( true )
						.build();

				assertEquals( 0, cacheProvider.totalInvocations );
				final String value = cache.get( testKey, key -> null );

				assertEquals( 1, cacheProvider.getInvocations );
				assertEquals( 1, cacheProvider.totalInvocations );
				assertSame( cachedValue, value );

			}

			@Test
			public void if_the_cache_entry_is_not_stored_should_be_loaded_asynchronously() throws Exception
			{

				final String loadedValue = "loaded_value_" + random.nextInt();
				final CyclicBarrier beforePut = new CyclicBarrier( 2 );
				final CyclicBarrier afterPut = new CyclicBarrier( 2 );

				final MockCacheProvider<String> cacheProvider = new MockCacheProvider<String>()
				{
					@Override
					protected CacheEntry<String> doGet( CacheKey key ) throws Exception
					{
						super.doGet( key );
						assertSame( testKey, key );
						return null;
					}
					@Override
					protected void doPut( CacheKey key, CacheEntry<String> entry, long duration ) throws Exception
					{
						beforePut.await();

						super.doPut( key, entry, duration );
						assertSame( testKey, key );
						assertSame( loadedValue, entry.getValue() );
						assertEquals( CacheConfig.getDefault().getCacheDuration() << 1, duration );

						afterPut.await();
					}
				};

				final SelfLoadingCache<String> cache = SelfLoadingCacheBuilder
						.withCacheProvider( cacheProvider )
						.withAsyncInsert( true )
						.withAsyncUpdate( true )
						.build();

				assertEquals( 0, cacheProvider.totalInvocations );
				final String value = cache.get( testKey, key -> loadedValue );

				assertEquals( 1, cacheProvider.getInvocations );
				assertEquals( 0, cacheProvider.putInvocations );
				assertEquals( 1, cacheProvider.totalInvocations );
				assertNull( value );

				beforePut.await();
				afterPut.await();

				assertEquals( 1, cacheProvider.getInvocations );
				assertEquals( 1, cacheProvider.putInvocations );
				assertEquals( 2, cacheProvider.totalInvocations );

			}

			@Test
			public void if_touch_fails_the_current_value_should_be_returned()
			{

				final String cachedValue = "cached_value_" + random.nextInt();
				final String loadedValue = "loaded_value_" + random.nextInt();

				final MockCacheProvider<String> cacheProvider = new MockCacheProvider<String>()
				{
					@Override
					protected CacheEntry<String> doGet( CacheKey key ) throws Exception
					{
						super.doGet( key );
						assertSame( testKey, key );
						return CacheEntry.of( cachedValue, 10 );
					}
					@Override
					protected boolean doTouch( CacheKey key, long duration ) throws Exception
					{
						super.doTouch( key, duration );
						assertSame( testKey, key );
						assertEquals( CacheConfig.getDefault().getTouchDuration(), duration );
						return false;
					}
				};

				final SelfLoadingCache<String> cache = SelfLoadingCacheBuilder
						.withCacheProvider( cacheProvider )
						.withAsyncInsert( true )
						.withAsyncUpdate( true )
						.build();

				assertEquals( 0, cacheProvider.totalInvocations );
				final String value = cache.get( testKey, key -> loadedValue );

				assertEquals( 1, cacheProvider.getInvocations );
				assertEquals( 1, cacheProvider.touchInvocations );
				assertEquals( 0, cacheProvider.putInvocations );
				assertEquals( 2, cacheProvider.totalInvocations );
				assertSame( cachedValue, value );

			}


			@Test
			public void if_touch_succeeds_the_cache_entry_should_be_updated_asynchronously() throws Exception
			{

				final String cachedValue = "cached_value_" + random.nextInt();
				final String loadedValue = "loaded_value_" + random.nextInt();

				final CyclicBarrier beforePut = new CyclicBarrier( 2 );
				final CyclicBarrier afterPut = new CyclicBarrier( 2 );

				final MockCacheProvider<String> cacheProvider = new MockCacheProvider<String>()
				{
					@Override
					protected CacheEntry<String> doGet( CacheKey key ) throws Exception
					{
						super.doGet( key );
						assertSame( testKey, key );
						return CacheEntry.of( cachedValue, 10 );
					}
					@Override
					protected boolean doTouch( CacheKey key, long duration ) throws Exception
					{
						super.doTouch( key, duration );
						assertSame( testKey, key );
						assertEquals( CacheConfig.getDefault().getTouchDuration(), duration );
						return true;
					}
					@Override
					protected void doPut( CacheKey key, CacheEntry<String> entry, long duration ) throws Exception
					{
						beforePut.await();

						super.doPut( key, entry, duration );
						assertSame( testKey, key );
						assertSame( loadedValue, entry.getValue() );
						assertEquals( CacheConfig.getDefault().getCacheDuration() << 1, duration );

						afterPut.await();
					}
				};

				final SelfLoadingCache<String> cache = SelfLoadingCacheBuilder
						.withCacheProvider( cacheProvider )
						.withAsyncInsert( true )
						.withAsyncUpdate( true )
						.build();

				assertEquals( 0, cacheProvider.totalInvocations );
				final String value = cache.get( testKey, key -> loadedValue );

				assertEquals( 1, cacheProvider.getInvocations );
				assertEquals( 1, cacheProvider.touchInvocations );
				assertEquals( 0, cacheProvider.putInvocations );
				assertEquals( 2, cacheProvider.totalInvocations );
				assertSame( cachedValue, value );

				beforePut.await();
				afterPut.await();

				assertEquals( 1, cacheProvider.getInvocations );
				assertEquals( 1, cacheProvider.touchInvocations );
				assertEquals( 1, cacheProvider.putInvocations );
				assertEquals( 3, cacheProvider.totalInvocations );

			}

			@CsvSource({
					"true,false",
					"false,true",
					"true,true"
			})
			@ParameterizedTest(name="disabledAll={0},disabledThis={1}")
			public void if_this_cache_or_all_caches_are_disabled_nothing_should_be_done(
					boolean disableAll, boolean disableThis
			)
			{

				final String cachedValue = "cached_value_" + random.nextInt();
				final String loadedValue = "loaded_value_" + random.nextInt();
				final MockCacheProvider<String> cacheProvider = new MockCacheProvider<String>()
				{
					@Override
					protected CacheEntry<String> doGet( CacheKey key ) throws Exception
					{
						super.doGet( key );
						assertSame( testKey, key );
						return CacheEntry.of( cachedValue, System.currentTimeMillis() + 10 );
					}
				};

				final SelfLoadingCache<String> cache = SelfLoadingCacheBuilder
						.withCacheProvider( cacheProvider )
						.withAsyncInsert( true )
						.withAsyncUpdate( true )
						.build();

				SelfLoadingCache.disableAll( disableAll );
				cache.disableThis( disableThis );

				assertEquals( 0, cacheProvider.totalInvocations );
				final String value = cache.get( testKey, key -> loadedValue );

				assertEquals( 0, cacheProvider.totalInvocations );
				assertSame( loadedValue, value );

			}

		}

		/**
		 * Performs unit test on the method {@link SelfLoadingCache#evict(CacheKey)}.
		 */
		@Nested
		@DisplayName("on method evict")
		public class Evict
		{

			@Test
			public void the_method_CacheProvider_remove_should_be_invoked()
			{

				final MockCacheProvider<String> cacheProvider = new MockCacheProvider<String>()
				{
					@Override
					protected void doRemove( CacheKey key ) throws Exception
					{
						super.doRemove( key );
						assertSame( testKey, key );
					}
				};

				final SelfLoadingCache<?> cache = SelfLoadingCacheBuilder
						.withCacheProvider( cacheProvider )
						.withAsyncInsert( true )
						.withAsyncUpdate( true )
						.build();

				assertEquals( 0, cacheProvider.totalInvocations );
				cache.evict( testKey );

				assertEquals( 1, cacheProvider.removeInvocations );
				assertEquals( 1, cacheProvider.totalInvocations );

			}

			@CsvSource({
				"true,false",
				"false,true",
				"true,true"
			})
			@ParameterizedTest(name="disabledAll={0},disabledThis={1}")
			public void if_this_cache_or_all_caches_are_disabled_nothing_should_be_done(
				boolean disableAll, boolean disableThis
			)
			{

				final MockCacheProvider<String> cacheProvider = new MockCacheProvider<String>()
				{
					@Override
					protected void doRemove( CacheKey key ) throws Exception
					{
						super.doRemove( key );
						assertSame( testKey, key );
					}
				};

				final SelfLoadingCache<?> cache = SelfLoadingCacheBuilder
						.withCacheProvider( cacheProvider )
						.withAsyncInsert( true )
						.withAsyncUpdate( true )
						.build();

				SelfLoadingCache.disableAll( disableAll );
				cache.disableThis( disableThis );

				assertEquals( 0, cacheProvider.totalInvocations );
				cache.evict( testKey );

				assertEquals( 0, cacheProvider.removeInvocations );
				assertEquals( 0, cacheProvider.totalInvocations );

			}

		}

	}

	/**
	 * Restores the disabledAll flag to {@code false}
	 * after each test.
	 */
	@AfterEach
	public void resetDisabled()
	{
		SelfLoadingCache.disableAll( false );
	}


	/* *************** */
	/*  INNER CLASSES  */
	/* *************** */


	/**
	 * Empty implementation of the {@link AbstractCacheProvider} to be used in this tests.
	 */
	private static class MockCacheProvider<V> extends AbstractCacheProvider<V>
	{

		public int getInvocations    = 0;
		public int putInvocations    = 0;
		public int touchInvocations  = 0;
		public int removeInvocations = 0;
		public int clearInvocations  = 0;
		public int totalInvocations  = 0;


		@Override
		protected CacheEntry<V> doGet( CacheKey key ) throws Exception
		{
			++getInvocations;
			++totalInvocations;
			return null;
		}

		@Override
		protected void doPut( CacheKey key, CacheEntry<V> entry, long duration ) throws Exception
		{
			++putInvocations;
			++totalInvocations;
		}

		@Override
		protected boolean doTouch( CacheKey key, long duration ) throws Exception
		{
			++touchInvocations;
			++totalInvocations;
			return false;
		}

		@Override
		protected void doRemove( CacheKey key ) throws Exception
		{
			++removeInvocations;
			++totalInvocations;
		}

		@Override
		protected void doClear() throws Exception
		{
			++clearInvocations;
			++totalInvocations;
		}

	}

}

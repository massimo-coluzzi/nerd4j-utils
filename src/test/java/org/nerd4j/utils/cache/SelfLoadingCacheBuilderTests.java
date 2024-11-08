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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.nerd4j.utils.lang.RequirementFailure;

/**
 * Test suite for the class {@link SelfLoadingCacheBuilder}
 *
 * @author Massimo Coluzzi
 */
@DisplayName("Testing class: SelfLoadingCacheBuilder")
@DisplayNameGeneration(ReplaceUnderscores.class)
public class SelfLoadingCacheBuilderTests
{


	/* ************** */
	/*  TEST METHODS  */
	/* ************** */


	/**
	 * Performs unit test for mandatory fields checks.
	 */
	@Nested
	@DisplayName("on checks about mandatory fields")
	public class MandatoryFieldsChecks
	{

		@Test
		public void the_cache_provider_cannot_be_null()
		{

			assertThrows( RequirementFailure.class, () -> SelfLoadingCacheBuilder.withCacheProvider( null ) );

		}

		@ValueSource(longs={Integer.MIN_VALUE, -1, 0})
		@ParameterizedTest(name="cache duration = {0} => throws RequirementFailure")
		public void the_cache_duration_must_be_greater_than_zero( long duration )
		{

			assertAll(
				() -> assertThrows(
					RequirementFailure.class,
					() -> SelfLoadingCacheBuilder
						.withCacheProvider( EmptyCacheProvider.get() )
						.setCacheDuration( duration )
				),
				() -> assertThrows(
					RequirementFailure.class,
					() -> SelfLoadingCacheBuilder
						.withCacheProvider( EmptyCacheProvider.get() )
						.withCacheDuration( duration )
				)
			);

		}

		@ValueSource(longs={Integer.MIN_VALUE, -1, 0})
		@ParameterizedTest(name="touch duration = {0} => throws RequirementFailure")
		public void the_touch_duration_must_be_greater_than_zero( long duration )
		{

			assertAll(
				() -> assertThrows(
					RequirementFailure.class,
					() -> SelfLoadingCacheBuilder
						.withCacheProvider( EmptyCacheProvider.get() )
						.setTouchDuration( duration )
				),
				() -> assertThrows(
					RequirementFailure.class,
					() -> SelfLoadingCacheBuilder
						.withCacheProvider( EmptyCacheProvider.get() )
						.withTouchDuration( duration )
				)
			);

		}

	}

	/**
	 * Performs unit test for configuration settings.
	 */
	@Nested
	@DisplayName("on configuration settings")
	public class ConfigurationSettings
	{

		@Test
		public void default_configurations_should_be_set_as_expected()
		{

			final SelfLoadingCache<?> builtCache = SelfLoadingCacheBuilder
					.withCacheProvider( EmptyCacheProvider.get() )
					.build();

			assertTrue( builtCache instanceof AbstractSelfLoadingCache );
			final AbstractSelfLoadingCache<?> cache = (AbstractSelfLoadingCache<?>) builtCache;

			final CacheConfig defaultConfig = CacheConfig.getDefault();
			assertEquals( defaultConfig, cache.config );

		}

		@Test
		public void custom_configurations_should_be_set_as_expected()
		{

			final CacheConfig defaultConfig = CacheConfig.getDefault();
			final CacheConfig customConfig = CacheConfig.of(
				defaultConfig.getCacheDuration() >> 1,
				defaultConfig.getTouchDuration() >> 1,
				! defaultConfig.isAsyncInsert(),
				! defaultConfig.isAsyncUpdate(),
				! defaultConfig.isThrowCacheProviderExceptions()
			);

			final SelfLoadingCache<?> builtCache = SelfLoadingCacheBuilder
				.withCacheProvider( EmptyCacheProvider.get() )
				.withCacheDuration( customConfig.getCacheDuration() )
				.withTouchDuration( customConfig.getTouchDuration() )
				.withAsyncInsert( customConfig.isAsyncInsert() )
				.withAsyncUpdate( customConfig.isAsyncUpdate() )
				.withThrowCacheProviderExceptions( customConfig.isThrowCacheProviderExceptions() )
				.build();

			assertTrue( builtCache instanceof AbstractSelfLoadingCache );
			final AbstractSelfLoadingCache<?> cache = (AbstractSelfLoadingCache<?>) builtCache;

			assertEquals( customConfig, cache.config );

		}

		@Test
		public void using_set_or_with_should_produce_the_same_effect()
		{

			final CacheConfig defaultConfig = CacheConfig.getDefault();
			final CacheConfig customConfig = CacheConfig.of(
				defaultConfig.getCacheDuration() >> 1,
				defaultConfig.getTouchDuration() >> 1,
				! defaultConfig.isAsyncInsert(),
				! defaultConfig.isAsyncUpdate(),
				! defaultConfig.isThrowCacheProviderExceptions()
			);

			final SelfLoadingCacheBuilder<?> builder = SelfLoadingCacheBuilder
					.withCacheProvider( EmptyCacheProvider.get() );
			builder.setCacheDuration( customConfig.getCacheDuration() );
			builder.setTouchDuration( customConfig.getTouchDuration() );
			builder.setAsyncInsert( customConfig.isAsyncInsert() );
			builder.setAsyncUpdate( customConfig.isAsyncUpdate() );
			builder.setThrowCacheProviderExceptions( customConfig.isThrowCacheProviderExceptions() );
			final SelfLoadingCache<?> builtUsingSet = builder.build();

			final SelfLoadingCache<?> builtUsingWith = SelfLoadingCacheBuilder
				.withCacheProvider( EmptyCacheProvider.get() )
				.withCacheDuration( customConfig.getCacheDuration() )
				.withTouchDuration( customConfig.getTouchDuration() )
				.withAsyncInsert( customConfig.isAsyncInsert() )
				.withAsyncUpdate( customConfig.isAsyncUpdate() )
				.withThrowCacheProviderExceptions( customConfig.isThrowCacheProviderExceptions() )
				.build();

			assertTrue( builtUsingSet instanceof AbstractSelfLoadingCache );
			final AbstractSelfLoadingCache<?> usingSet = (AbstractSelfLoadingCache<?>) builtUsingSet;

			assertTrue( builtUsingWith instanceof AbstractSelfLoadingCache );
			final AbstractSelfLoadingCache<?> usingWith = (AbstractSelfLoadingCache<?>) builtUsingWith;

			assertEquals( usingSet.config, usingWith.config );

		}

	}

}

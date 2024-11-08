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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.nerd4j.utils.lang.RequirementFailure;
import org.nerd4j.utils.test.ObjectOverridesContract;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for the class {@link CacheConfig}
 *
 * @author Massimo Coluzzi
 */
@DisplayName("Testing class: CacheConfig")
@DisplayNameGeneration(ReplaceUnderscores.class)
public class CacheConfigTests implements ObjectOverridesContract<CacheConfig>
{


	/* ******************* */
	/*  INTERFACE METHODS  */
	/* ******************* */


	/**
	 * {@inheritDoc}
	 */
	@Override
	public CacheConfig sampleValue()
	{

		return CacheConfig.of( 10, 10, false, false, false );

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CacheConfig notEqualValue()
	{

		return CacheConfig.of( 10, 10, false, false, true );

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CacheConfig equalButNotSameValue()
	{

		return CacheConfig.of( 10, 10, false, false, false );

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CacheConfig withDifferentHashcode()
	{

		return CacheConfig.of( 10, 10, false, false, true );

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String sampleToString()
	{

		return "CacheConfig{cacheDuration=10, touchDuration=10, " +
               "asyncInsert=false, asyncUpdate=false, " +
			   "throwCacheProviderExceptions=false}";

	}
	
	
	/* ************** */
	/*  TEST METHODS  */
	/* ************** */


	@Test
	public void durations_must_be_greater_than_zero()
	{

		assertThrows( RequirementFailure.class, () -> CacheConfig.of(-1, 10, false, false, false) );
		assertThrows( RequirementFailure.class, () -> CacheConfig.of( 0, 10, false, false, false) );
		assertThrows( RequirementFailure.class, () -> CacheConfig.of(10, -1, false, false, false) );
		assertThrows( RequirementFailure.class, () -> CacheConfig.of(10,  0, false, false, false) );

	}

	@CsvSource({
		"10000000001,127,false,false,false",
		"127,10000000001,false,false,false ",
		"10000000001,127,true,true,true",
		"127,10000000001,true,true,true",
	})
	@ParameterizedTest(name="CacheConfig.of({0},{1},{2},{3},{4})")
	public void configuration_properties_should_be_as_expected(
		long cacheDuration, long touchDuration, boolean asyncInsert, boolean asyncUpdate,
		boolean throwCacheProviderExceptions
	)
	{

		final CacheConfig config = CacheConfig.of(
			cacheDuration, touchDuration, asyncInsert, asyncUpdate, throwCacheProviderExceptions
		);

		assertAll(
			() -> assertEquals( cacheDuration, config.getCacheDuration() ),
			() -> assertEquals( touchDuration, config.getTouchDuration() ),
			() -> assertEquals( asyncInsert, config.isAsyncInsert() ),
			() -> assertEquals( asyncUpdate, config.isAsyncUpdate() ),
			() -> assertEquals( throwCacheProviderExceptions, config.isThrowCacheProviderExceptions() )
		 );

	}

	@Test
	public void the_default_durations_should_be_as_expected()
	{

		final CacheConfig config = CacheConfig.of(
			false, false, false
		);

		assertAll(
			() -> assertEquals( CacheConfig.DEFAULT_CACHE_DURATION, config.getCacheDuration() ),
			() -> assertEquals( CacheConfig.DEFAULT_TOUCH_DURATION, config.getTouchDuration() )
		);

	}

	@Test
	public void the_default_config_should_have_the_expected_values()
	{

		final CacheConfig config = CacheConfig.getDefault();

		assertAll(
			() -> assertEquals( CacheConfig.DEFAULT_CACHE_DURATION, config.getCacheDuration() ),
			() -> assertEquals( CacheConfig.DEFAULT_TOUCH_DURATION, config.getTouchDuration() ),
			() -> assertEquals( false, config.isAsyncInsert() ),
			() -> assertEquals( true,  config.isAsyncUpdate() ),
			() -> assertEquals( false, config.isThrowCacheProviderExceptions() )
		);

	}

	@Test
	public void the_default_config_should_be_singleton()
	{

		assertSame( CacheConfig.getDefault(), CacheConfig.getDefault() );

	}

	@Test
	public void the_toString_method_should_return_the_same_instance()
	{

		final CacheConfig config = CacheConfig.getDefault();
		assertSame( config.toString(), config.toString() );

	}

	@Test
	public void the_flag_disabledAll_is_false_by_default()
	{

		assertFalse( CacheConfig.disabledAll );

	}

}

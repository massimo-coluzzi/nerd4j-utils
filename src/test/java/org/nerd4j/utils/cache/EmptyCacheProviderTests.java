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

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.logging.Level;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

/**
 * Test suite for the class {@link EmptyCacheProvider}
 *
 * @author Massimo Coluzzi
 */
@DisplayName("Testing class: EmptyCacheProvider")
@DisplayNameGeneration(ReplaceUnderscores.class)
public class EmptyCacheProviderTests
{

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


   	@Test
	public void the_method_get_should_always_return_null()
	{

		final EmptyCacheProvider<String> provider = new EmptyCacheProvider<>();

		final CacheKey key = CacheKey.of( String.class, "test", "test" );
		assertNull( provider.get(key) );

		provider.put( key, "value to cache", 10 );
		assertNull( provider.get(key) );

	}

	@Test
	public void the_method_touch_should_always_throw_a_CacheProviderException()
	{

		final EmptyCacheProvider<String> provider = new EmptyCacheProvider<>();

		final CacheKey key = CacheKey.of( String.class, "test", "test" );
		assertThrows( CacheProviderException.class, () -> provider.touch(key,10) );

		provider.put( key, "value to cache", 10 );
		assertThrows( CacheProviderException.class, () -> provider.touch(key,10) );

	}

}

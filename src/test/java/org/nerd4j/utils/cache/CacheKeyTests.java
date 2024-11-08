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

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.nerd4j.utils.lang.RequirementFailure;
import org.nerd4j.utils.test.ObjectOverridesContract;

/**
 * Test suite for the class {@link CacheKey}
 *
 * @author Massimo Coluzzi
 */
@DisplayName("Testing class: CacheKey")
@DisplayNameGeneration(ReplaceUnderscores.class)
public class CacheKeyTests implements ObjectOverridesContract<CacheKey>
{


	/* ******************* */
	/*  INTERFACE METHODS  */
	/* ******************* */


	/**
	 * {@inheritDoc}
	 */
	@Override
	public CacheKey sampleValue()
	{

		return CacheKey.of( Object.class, 1 );

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CacheKey notEqualValue()
	{

		return CacheKey.of( Object.class, 2 );

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CacheKey equalButNotSameValue()
	{

		return CacheKey.of( Object.class, 1 );

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CacheKey withDifferentHashcode()
	{

		return CacheKey.of( Object.class, 2 );

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String sampleToString()
	{

		return "CacheKey<Object-LATEST-[1]>";

	}
	
	
	/* ************** */
	/*  TEST METHODS  */
	/* ************** */


	/**
     * Performs unit test on the {@link CacheKey} factory methods.
     */
    @Nested
    @DisplayName("on cache key factory methods")
    public class CacheKeyFactoryMethods
    {

    	@Test
    	public void the_data_model_type_is_mandatory()
    	{

    		assertThrows( RequirementFailure.class, () -> CacheKey.of( null ) );

    	}

    	@NullAndEmptySource
    	@ValueSource(strings={" ","\t","\n"})
    	@ParameterizedTest(name="CacheKey.of(Object.class,{0}) -> RequirementFailure")
    	public void the_data_model_version_cannot_be_blank( String version )
    	{

    		assertThrows( RequirementFailure.class, () -> CacheKey.of(Object.class,version) );

    	}

    	@Test
    	public void a_valid_cache_key_can_have_no_attributes()
    	{

    		final CacheKey cacheKey = CacheKey.of( Object.class );
    		assertNotNull( cacheKey );
    		assertEquals( Object.class, cacheKey.dataModelType() );
    		assertEquals( CacheKey.DEFAULT_VERSION, cacheKey.version() );

    		final Stream<Object> attributes = cacheKey.attributes();
			assertNotNull( attributes );
			assertEquals( 0, attributes.count() );

    	}

		@Test
		public void if_version_is_defined_should_be_configured()
		{

			final CacheKey cacheKey = CacheKey.of( Object.class, "custom-version" );
			assertNotNull( cacheKey );
			assertEquals( Object.class, cacheKey.dataModelType() );
			assertEquals( "custom-version", cacheKey.version() );

			final Stream<Object> attributes = cacheKey.attributes();
			assertNotNull( attributes );
			assertEquals( 0, attributes.count() );

		}

		@Test
		public void if_attributes_are_defined_should_be_available()
		{

			final CacheKey cacheKey = CacheKey.of( Object.class, "custom-version", null, "", 1 );
			assertNotNull( cacheKey );
			assertEquals( Object.class, cacheKey.dataModelType() );
			assertEquals( "custom-version", cacheKey.version() );

			final Stream<Object> stream = cacheKey.attributes();
			assertNotNull( stream );

			final List<Object> attributes = stream.collect( toList() ) ;
			assertEquals( 3, attributes.size() );
			assertEquals( null, attributes.get(0) );
			assertEquals( "", attributes.get(1) );
			assertEquals( 1, attributes.get(2) );

		}

		@Test
		public void factory_methods_should_create_a_new_instance_at_any_invocation()
		{

			final CacheKey cacheKey1 = CacheKey.of( Object.class, "custom-version", null, "", 1 );
			final CacheKey cacheKey2 = CacheKey.of( Object.class, "custom-version", null, "", 1 );

			assertNotNull( cacheKey1 );
			assertNotNull( cacheKey2 );

			assertEquals( cacheKey1, cacheKey2 );
			assertNotSame( cacheKey1, cacheKey2 );

		}

    }

    /**
     * Performs unit test on the {@link CacheKey.Prototype} factory methods.
     */
    @Nested
    @DisplayName("on cache key prototype factory methods")
    public class PrototypeFactoryMethods
    {

    	@Test
    	public void the_data_model_type_is_mandatory()
    	{

    		assertThrows( RequirementFailure.class, () -> CacheKey.prototype( null ) );

    	}

    	@NullAndEmptySource
    	@ValueSource(strings={" ","\t","\n"})
    	@ParameterizedTest(name="CacheKey.prototype(Object.class,{0}) -> RequirementFailure")
    	public void the_data_model_version_cannot_be_blank( String version )
    	{

    		assertThrows( RequirementFailure.class, () -> CacheKey.prototype(Object.class,version) );

    	}

    	@Test
    	public void a_valid_cache_key_prototype_can_have_no_version()
    	{

    		final CacheKey.Prototype prototype = CacheKey.prototype( Object.class );
    		assertNotNull( prototype );
    		assertEquals( "CacheKeyPrototype<Object-LATEST>", prototype.toString() );

    	}

		@Test
		public void if_version_is_defined_should_be_configured()
		{

			final CacheKey.Prototype prototype = CacheKey.prototype( Object.class, "custom-version" );
			assertNotNull( prototype );
			assertEquals( "CacheKeyPrototype<Object-custom-version>", prototype.toString() );

		}

		@Test
		public void factory_methods_should_create_a_new_instance_aT_any_invocation()
		{

			final CacheKey.Prototype prototype1 = CacheKey.prototype( Object.class, "custom-version" );
			final CacheKey.Prototype prototype2 = CacheKey.prototype( Object.class, "custom-version" );

			assertNotNull( prototype1 );
			assertNotNull( prototype2 );

			assertEquals( prototype1, prototype2 );
			assertNotSame( prototype1, prototype2 );

		}

    }


    /**
     * Performs unit test to check the consistence between {@link CacheKey} factory methods
	 * and {@link CacheKey.Prototype}.
     */
    @Nested
    @DisplayName("on cache consistence between factory methods and prototype")
    public class FactoryMethodsVsPrototype
    {

    	@Test
    	public void factory_methods_and_prototype_should_create_the_same_key()
    	{

    		final CacheKey byFactoryMethod = CacheKey.of( Object.class, 1, '2', "3" );
    		final CacheKey byPrototype = CacheKey.prototype( Object.class ).of(  1, '2', "3" );

    		assertNotNull( byFactoryMethod );
    		assertNotNull( byPrototype );

    		assertEquals( byFactoryMethod, byPrototype );
    		assertNotSame( byFactoryMethod, byPrototype );

    	}

    	@Test
    	public void factory_methods_and_prototype_should_create_the_same_versioned_key()
    	{

    		final CacheKey byFactoryMethod = CacheKey.of( Object.class, "version", 1, '2', "3" );
    		final CacheKey byPrototype = CacheKey.prototype( Object.class, "version" ).of(  1, '2', "3" );

    		assertNotNull( byFactoryMethod );
    		assertNotNull( byPrototype );

    		assertEquals( byFactoryMethod, byPrototype );
    		assertNotSame( byFactoryMethod, byPrototype );

    	}


    }

	/**
	 * Performs unit test to check the {@code toString()} method of both
	 * the {@link CacheKey} and the {@link CacheKey.Prototype}.
	 */
	@Nested
	@DisplayName("on invocatin of toString()")
	public class ToStringMethod
	{

		@Test
		public void CacheKey_the_second_invocation_of_toString_should_return_the_same_object()
		{

			final CacheKey cacheKey = CacheKey.of( Object.class, "version", 1, '2', "3" );

			final String first = cacheKey.toString();
			assertNotNull( first );

			final String second = cacheKey.toString();
			assertSame( first, second );

		}

		@Test
		public void Prototype_the_second_invocation_of_toString_should_return_the_same_object()
		{

			final CacheKey.Prototype prototype = CacheKey.prototype( Object.class, "version" );

			final String first = prototype.toString();
			assertNotNull( first );

			final String second = prototype.toString();
			assertSame( first, second );

		}

	}

}

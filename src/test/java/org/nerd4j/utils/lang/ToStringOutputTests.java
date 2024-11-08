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
package org.nerd4j.utils.lang;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.nerd4j.utils.lang.ToString.Configuration;
import org.nerd4j.utils.lang.ToString.Configurator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for the utility class {@link ToStringOutput}.
 *
 * @author Massimo Coluzzi
 */
@DisplayName("Testing static class: ToStringOutput")
@DisplayNameGeneration(ReplaceUnderscores.class)
public class ToStringOutputTests
{

	@Test
	public void if_Configuration_is_null_an_exception_should_be_thrown()
	{
			
		assertThrows( RequirementFailure.class, () ->
		{
			
			ToStringOutput.using( null, null, null, null, null );
				
		});
			
	}
		
	@Test
	public void if_target_is_null_method_should_return_null()
	{
			
		final Configuration configuration = (Configuration) ToString.of( null );
		assertEquals( "null", ToStringOutput.using(configuration,null,null,null,null) );
			
	}

	@CsvSource({
		"false,SomeType",
		"true,org.nerd4j.utils.lang.TestClasses.SomeType",
	})
	@ParameterizedTest(name = "the expected output is: {2}")
	@DisplayName("the proper class name should ne used according to the configuration")
	public void the_proper_default_class_name_should_be_used(
		boolean isFullClassPath, String expected
	)
	{

		final Configurator configurator
		= ToString.of( new TestClasses.SomeType() );

		if( isFullClassPath )
			configurator.withFullClassName();

		final Configuration configuration = (Configuration) configurator;
		assertEquals( expected, ToStringOutput.using(configuration,null,null,null,null) );

	}

	@CsvSource({
		",false,null",
		",true,null",
		"'',false,''",
		"'',true,''",
		"SomeCustomType,false,SomeCustomType",
		"SomeCustomType,true,SomeCustomType"
	})
	@ParameterizedTest(name = "the expected output is: {2}")
	public void the_proper_custom_class_name_should_be_used(
		String customClassName, boolean isFullClassPath, String expected
	)
	{

		final Configurator configurator
		= ToString.of( new TestClasses.SomeType() )
		.withCustomClassName( customClassName );

		if( isFullClassPath )
			configurator.withFullClassName();

		final Configuration configuration = (Configuration) configurator;
		assertEquals( expected, ToStringOutput.using(configuration,null,null,null,null) );

	}

	@Test
	@DisplayName("withNoClassName() should equal to withCustomClassName(\"\")")
	public void withNoClassName_should_equal_to_withCustomClassName_empty()
	{

		final Configurator noClassName
		= ToString.of( new TestClasses.SomeType() )
				  .withNoClassName();

		final Configurator emptyCustomClassName
		= ToString.of( new TestClasses.SomeType() )
				  .withCustomClassName( "" );

		final Configuration noClassNameConf = (Configuration) noClassName;
		final Configuration emptyCustomClassNameConf = (Configuration) emptyCustomClassName;

		assertEquals(
			ToStringOutput.using( noClassNameConf, null, null, null, null ),
			ToStringOutput.using( emptyCustomClassNameConf, null, null, null, null )
		);

	}

	@Test
	public void a_null_field_value_should_be_printed_as_null()
	{

		final String value = null;
		final Configuration configuration
		= (Configuration) ToString.of( "SomeClass" )
			.withCustomClassName( "SomeClass" )
			.print( value );

		assertEquals( "SomeClass^null$", ToStringOutput.using(configuration,"^","-","|","$") );

	}

	@Test
	public void a_valued_field_value_should_be_printed()
	{

		final String value = "value";
		final Configuration configuration
		= (Configuration) ToString.of( "SomeClass" )
			.withCustomClassName( "SomeClass" )
			.print( value );

		assertEquals( "SomeClass^value$", ToStringOutput.using(configuration,"^","-","|","$") );

	}

	@Test
	public void a_null_field_name_should_not_be_printed()
	{

		final String value = "value";
		final Configuration configuration
		= (Configuration) ToString.of( "SomeClass" )
			.withCustomClassName( "SomeClass" )
			.print( null, value );

		assertEquals( "SomeClass^value$", ToStringOutput.using(configuration,"^","-","|","$") );

	}

	@Test
	public void a_valued_field_name_should_be_printed()
	{

		final String value = "value";
		final Configuration configuration
		= (Configuration) ToString.of( "SomeClass" )
			.withCustomClassName( "SomeClass" )
			.print( "key", value );

		assertEquals( "SomeClass^key-value$", ToStringOutput.using(configuration,"^","-","|","$") );

	}

	@CsvSource(value={
		"null,null,null,null,SomeType^null|null$",
		"null,null,null,value2,SomeType^null|value2$",
		"null,null,name2,null,SomeType^null|name2-null$",
		"null,null,name2,value2,SomeType^null|name2-value2$",
		"null,value1,null,null,SomeType^value1|null$",
		"null,value1,null,value2,SomeType^value1|value2$",
		"null,value1,name2,null,SomeType^value1|name2-null$",
		"null,value1,name2,value2,SomeType^value1|name2-value2$",
		"name1,null,null,null,SomeType^name1-null|null$",
		"name1,null,null,value2,SomeType^name1-null|value2$",
		"name1,null,name2,null,SomeType^name1-null|name2-null$",
		"name1,null,name2,value2,SomeType^name1-null|name2-value2$",
		"name1,value1,null,null,SomeType^name1-value1|null$",
		"name1,value1,null,value2,SomeType^name1-value1|value2$",
		"name1,value1,name2,null,SomeType^name1-value1|name2-null$",
		"name1,value1,name2,value2,SomeType^name1-value1|name2-value2$"
	},nullValues="null")
	@ParameterizedTest(name = "the expected output is: {4}")
	public void multiple_fields_should_be_printed_as_expected(
		String name1, String value1, String name2, String value2, String expected
	)
	{

		final Configuration configuration
		= (Configuration) ToString.of( this )
			.withCustomClassName( "SomeType" )
			.print( name1, value1 )
			.print( name2, value2 );

		assertEquals( expected, ToStringOutput.using(configuration,"^","-","|","$") );

	}

	@Test
	public void arrays_should_be_printed_as_expected()
	{

		assertAll(

			() -> {

				final Configuration configuration
				= (Configuration) ToString.of( this )
					.withCustomClassName( "SomeType" )
					.print( "array", new int[] {1,2,3} );

				assertEquals( "SomeType^array-[1, 2, 3]$",
					ToStringOutput.using(configuration,"^","-","|","$") );

			},

			() -> {

				final Configuration configuration
				= (Configuration) ToString.of( this )
					.withCustomClassName( "SomeType" )
					.print( "array", new String[] {"a","b","c"} );

				assertEquals( "SomeType^array-[a, b, c]$",
					ToStringOutput.using(configuration,"^","-","|","$") );

			},

			() -> {

				final Configuration configuration
				= (Configuration) ToString.of( this )
					.withCustomClassName( "SomeType" )
					.print( "array",
						new Object[] {
							new int[] { 1,2,3},
							new String[] {"a","b","c"}
						}
					);

				assertEquals( "SomeType^array-[[1, 2, 3], [a, b, c]]$",
					ToStringOutput.using(configuration,"^","-","|","$") );

			}

		);
	}

}

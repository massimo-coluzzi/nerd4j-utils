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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.nerd4j.utils.lang.ToString.Configuration;
import org.nerd4j.utils.lang.ToString.Configuration.Field;
import org.nerd4j.utils.tuple.Pair;
import org.nerd4j.utils.lang.ToString.Configurator;
import org.nerd4j.utils.lang.ToString.Printer;

import java.util.Iterator;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for the utility class {@link ToString}
 *
 * @author Massimo Coluzzi
 */
@DisplayName("Testing utility class: ToString")
@DisplayNameGeneration(ReplaceUnderscores.class)
public class ToStringTests
{

	/** Random values generator to use in tests. */
	private static final Random random = new Random();


	/**
	 * Performs unit tests on the mapping between
	 * {@link ToString.Configurator} and
	 * {@link ToString.Configuration}.
	 */
	@Nested
	@DisplayName("test mapping between Configurator and Configuration")
	public class ConfiguratorTests
	{

		@ValueSource(booleans={false,true})
		@ParameterizedTest(name="the expected output is: {2}")
		@DisplayName("no class name should be used according to the configuration")
		public void the_configurator_should_set_the_proper_values(
			boolean isFullClassPath
		)
		{

			final Configurator configurator
			= ToString.of( new TestClasses.SomeType() )
                      .withNoClassName();

			if( isFullClassPath )
				configurator.withFullClassName();

			final Configuration configuration = (Configuration) configurator;
			assertEquals( "", configuration.customClassName() );
			assertEquals( isFullClassPath, configuration.fullClassPath() );

		}

        @CsvSource(value={
		    ",false,null",
			",true,null",
			"SomeCustomType,false,SomeCustomType",
			"SomeCustomType,true,SomeCustomType"
		})
		@ParameterizedTest(name="the expected output is: {2}")
		@DisplayName("the proper class name should be used according to the configuration")
		public void the_configurator_should_set_the_proper_values(
			String customClassName, boolean isFullClassPath, String expectedClassName
		)
		{

			final Configurator configurator
			= ToString.of( new TestClasses.SomeType() )
				      .withCustomClassName( customClassName );

			if( isFullClassPath )
				configurator.withFullClassName();

			final Configuration configuration = (Configuration) configurator;
			assertEquals( expectedClassName, configuration.customClassName() );
			assertEquals( isFullClassPath, configuration.fullClassPath() );

		}

		@Test
		public void fields_added_with_name_to_the_configurator_should_be_visible_in_the_configuration()
		{

			final String name1 = "name_" + random.nextInt();
			final String name2 = "name_" + random.nextInt();
			final String value1 = "value_" + random.nextInt();
			final String value2 = "value_" + random.nextInt();

			final Configurator configurator = ToString.of( new TestClasses.SomeType() );
			configurator.print( name1, value1 )
					    .print( name2, value2 );

			final Configuration configuration = (Configuration) configurator;
			final Iterator<Field> iterator = configuration.fields().iterator();

			final Field field1 = iterator.next();
			final Field field2 = iterator.next();
			assertFalse( iterator.hasNext() );

			assertAll(
				() -> assertEquals( name1, field1.name ),
				() -> assertEquals( value1, field1.value ),
				() -> assertEquals( name2, field2.name ),
				() -> assertEquals( value2, field2.value )

			);

		}

		@Test
		public void non_null_values_added_with_name_to_the_configurator_should_be_visible_in_the_configuration()
		{

			final String name1 = "name_" + random.nextInt();
			final String name2 = "name_" + random.nextInt();
			final String value1 = "value_" + random.nextInt();
			final String value2 = null;

			final Configurator configurator = ToString.of( new TestClasses.SomeType() );
			configurator.printIfNotNull( name1, value1 )
					    .printIfNotNull( name2, value2 );

			final Configuration configuration = (Configuration) configurator;
			final Iterator<Field> iterator = configuration.fields().iterator();

			final Field field1 = iterator.next();
			assertFalse( iterator.hasNext() );

			assertAll(
				() -> assertEquals( name1, field1.name ),
				() -> assertEquals( value1, field1.value )

			);

		}

		@Test
		public void fields_added_without_name_to_the_configurator_should_be_visible_in_the_configuration()
		{

			final int value1 = random.nextInt();
			final int value2 = random.nextInt();

			final Configurator configurator = ToString.of( new TestClasses.SomeType() );
			configurator.print( value1, value2 );

			final Configuration configuration = (Configuration) configurator;
			final Iterator<Field> iterator = configuration.fields().iterator();

			final Field field1 = iterator.next();
			final Field field2 = iterator.next();
			assertFalse( iterator.hasNext() );

			assertAll(
				() -> assertEquals( null, field1.name ),
				() -> assertEquals( value1, field1.value ),
				() -> assertEquals( null, field2.name ),
				() -> assertEquals( value2, field2.value )

			);

		}

		@Test
		public void empty_values_without_name_should_not_be_visible_in_the_configuration()
		{

			final Configurator configurator = ToString.of( new TestClasses.SomeType() );
			configurator.print();

			final Configuration configuration = (Configuration) configurator;
			final Iterator<Field> iterator = configuration.fields().iterator();

			assertFalse( iterator.hasNext() );

		}

		@Test
		public void empty_non_null_values_without_name_should_not_be_visible_in_the_configuration()
		{

			final Configurator configurator = ToString.of( new TestClasses.SomeType() );
			configurator.printIfNotNull();

			final Configuration configuration = (Configuration) configurator;
			final Iterator<Field> iterator = configuration.fields().iterator();

			assertFalse( iterator.hasNext() );

		}

		@Test
		public void non_null_value_added_without_name_to_the_configurator_should_be_visible_in_the_configuration()
		{

			final Integer value1 = random.nextInt();
			final Integer value2 = null;

			final Configurator configurator = ToString.of( new TestClasses.SomeType() );
			configurator.printIfNotNull( value1, value2 );

			final Configuration configuration = (Configuration) configurator;
			final Iterator<Field> iterator = configuration.fields().iterator();

			final Field field1 = iterator.next();
			assertFalse( iterator.hasNext() );

			assertAll(
				() -> assertEquals( null, field1.name ),
				() -> assertEquals( value1, field1.value )

			);

		}

		@Test
		public void fields_streamed_without_name_to_the_configurator_should_be_visible_in_the_configuration()
		{

			final int value1 = random.nextInt();
			final int value2 = random.nextInt();

			final Configurator configurator = ToString.of( new TestClasses.SomeType() );
			configurator.print( Stream.of(value1, value2) );

			final Configuration configuration = (Configuration) configurator;
			final Iterator<Field> iterator = configuration.fields().iterator();

			final Field field1 = iterator.next();
			final Field field2 = iterator.next();
			assertFalse( iterator.hasNext() );

			assertAll(
				() -> assertEquals( null, field1.name ),
				() -> assertEquals( value1, field1.value ),
				() -> assertEquals( null, field2.name ),
				() -> assertEquals( value2, field2.value )

			);

		}

		@Test
		public void non_null_fields_streamed_without_name_to_the_configurator_should_be_visible_in_the_configuration()
		{

			final Integer value1 = null;
			final Integer value2 = random.nextInt();

			final Configurator configurator = ToString.of( new TestClasses.SomeType() );
			configurator.printIfNotNull( Stream.of(value1, value2) );

			final Configuration configuration = (Configuration) configurator;
			final Iterator<Field> iterator = configuration.fields().iterator();

			final Field field2 = iterator.next();
			assertFalse( iterator.hasNext() );

			assertAll(
				() -> assertEquals( null, field2.name ),
				() -> assertEquals( value2, field2.value )

			);

		}

		/**
		 * Performs unit tests on the method print
		 * with stream and mapping fuctions as arguments.
		 */
		@Nested
		@DisplayName("on print(Stream,Function,Function)")
		public class OnPrintWithStreamFunctionFunction
		{

			@Test
			public void when_both_functions_are_null()
			{

				final Pair<String,?> pair1 = Pair.of( "name_" + random.nextInt(), random.nextInt() );
				final Pair<String,?> pair2 = Pair.of( "name_" + random.nextInt(), random.nextInt() );

				final Stream<Pair<String,?>> stream = Stream.of( pair1, pair2 );
				final Configurator configurator = ToString.of( new TestClasses.SomeType() );
				configurator.print( stream, null, null );

				final Configuration configuration = (Configuration) configurator;
				final Iterator<Field> iterator = configuration.fields().iterator();

				final Field field1 = iterator.next();
				final Field field2 = iterator.next();
				assertFalse( iterator.hasNext() );

				assertAll(
					() -> assertNull( field1.name ),
					() -> assertNull( field1.value ),
					() -> assertNull( field2.name ),
					() -> assertNull( field2.value )

				);

			}

			@Test
			public void when_second_function_is_null()
			{

				final Pair<String,?> pair1 = Pair.of( "name_" + random.nextInt(), random.nextInt() );
				final Pair<String,?> pair2 = Pair.of( "name_" + random.nextInt(), random.nextInt() );

				final Stream<Pair<String,?>> stream = Stream.of( pair1, pair2 );
				final Configurator configurator = ToString.of( new TestClasses.SomeType() );
				configurator.print( stream, Pair::getKey, null );

				final Configuration configuration = (Configuration) configurator;
				final Iterator<Field> iterator = configuration.fields().iterator();

				final Field field1 = iterator.next();
				final Field field2 = iterator.next();
				assertFalse( iterator.hasNext() );

				assertAll(
					() -> assertEquals( pair1.getKey(), field1.name ),
					() -> assertNull( field1.value ),
					() -> assertEquals( pair2.getKey(), field2.name ),
					() -> assertNull( field2.value )

				);

			}

			@Test
			public void when_first_function_is_null()
			{

				final Pair<String,?> pair1 = Pair.of( "name_" + random.nextInt(), random.nextInt() );
				final Pair<String,?> pair2 = Pair.of( "name_" + random.nextInt(), random.nextInt() );

				final Stream<Pair<String,?>> stream = Stream.of( pair1, pair2 );
				final Configurator configurator = ToString.of( new TestClasses.SomeType() );
				configurator.print( stream, null, Pair::getValue );

				final Configuration configuration = (Configuration) configurator;
				final Iterator<Field> iterator = configuration.fields().iterator();

				final Field field1 = iterator.next();
				final Field field2 = iterator.next();
				assertFalse( iterator.hasNext() );

				assertAll(
					() -> assertNull( field1.name ),
					() -> assertEquals( pair1.getValue(), field1.value ),
					() -> assertNull( field2.name ),
					() -> assertEquals( pair2.getValue(), field2.value )

				);

			}

			@Test
			public void when_both_functions_are_not_null()
			{

				final Pair<String,?> pair1 = Pair.of( "name_" + random.nextInt(), random.nextInt() );
				final Pair<String,?> pair2 = Pair.of( "name_" + random.nextInt(), random.nextInt() );

				final Stream<Pair<String,?>> stream = Stream.of( pair1, pair2 );
				final Configurator configurator = ToString.of( new TestClasses.SomeType() );
				configurator.print( stream, Pair::getKey, Pair::getValue );

				final Configuration configuration = (Configuration) configurator;
				final Iterator<Field> iterator = configuration.fields().iterator();

				final Field field1 = iterator.next();
				final Field field2 = iterator.next();
				assertFalse( iterator.hasNext() );

				assertAll(
					() -> assertEquals( pair1.getKey(), field1.name ),
					() -> assertEquals( pair1.getValue(), field1.value ),
					() -> assertEquals( pair2.getKey(), field2.name ),
					() -> assertEquals( pair2.getValue(), field2.value )

				);

			}

		}


		/**
		 * Performs unit tests on the method printIfNotNull
		 * with stream and mapping fuctions as arguments.
		 */
		@Nested
		@DisplayName("on printIfNotNull(Stream,Function,Function)")
		public class OnPrintIfNotNullFWithStreamunctionFunction
		{

			@Test
			public void when_both_functions_are_null()
			{

				final Pair<String,?> pair1 = Pair.of( "name_" + random.nextInt(), random.nextInt() );
				final Pair<String,?> pair2 = null;

				final Stream<Pair<String,?>> stream = Stream.of( pair1, pair2 );
				final Configurator configurator = ToString.of( new TestClasses.SomeType() );
				configurator.printIfNotNull( stream, null, null );

				final Configuration configuration = (Configuration) configurator;
				final Iterator<Field> iterator = configuration.fields().iterator();

				assertFalse( iterator.hasNext() );

			}

			@Test
			public void when_second_function_is_null()
			{

				final Pair<String,?> pair1 = null;
				final Pair<String,?> pair2 = Pair.of( "name_" + random.nextInt(), random.nextInt() );

				final Stream<Pair<String,?>> stream = Stream.of( pair1, pair2 );
				final Configurator configurator = ToString.of( new TestClasses.SomeType() );
				configurator.printIfNotNull( stream, Pair::getKey, null );

				final Configuration configuration = (Configuration) configurator;
				final Iterator<Field> iterator = configuration.fields().iterator();

				assertFalse( iterator.hasNext() );

			}

			@Test
			public void when_first_function_is_null()
			{

				final Pair<String,?> pair1 = Pair.of( "name_" + random.nextInt(), random.nextInt() );
				final Pair<String,?> pair2 = null;

				final Stream<Pair<String,?>> stream = Stream.of( pair1, pair2 );
				final Configurator configurator = ToString.of( new TestClasses.SomeType() );
				configurator.printIfNotNull( stream, null, Pair::getValue );

				final Configuration configuration = (Configuration) configurator;
				final Iterator<Field> iterator = configuration.fields().iterator();

				final Field field1 = iterator.next();
				assertFalse( iterator.hasNext() );

				assertAll(
					() -> assertNull( field1.name ),
					() -> assertEquals( pair1.getValue(), field1.value )
				);

			}

			@Test
			public void when_both_functions_are_not_null()
			{

				final Pair<String,?> pair1 = null;
				final Pair<String,?> pair2 = Pair.of( "name_" + random.nextInt(), random.nextInt() );

				final Stream<Pair<String,?>> stream = Stream.of( pair1, pair2 );
				final Configurator configurator = ToString.of( new TestClasses.SomeType() );
				configurator.printIfNotNull( stream, Pair::getKey, Pair::getValue );

				final Configuration configuration = (Configuration) configurator;
				final Iterator<Field> iterator = configuration.fields().iterator();

				final Field field2 = iterator.next();
				assertFalse( iterator.hasNext() );

				assertAll(
					() -> assertEquals( pair2.getKey(), field2.name ),
					() -> assertEquals( pair2.getValue(), field2.value )
				);

			}

		}


	}


	/* ************** */
	/*  LIKE ECLIPSE  */
	/* ************** */


	/**
     * Performs unit test on the method
	 * {@link ToString.Configurator#likeEclipse()}.
     */
	@Nested
	@DisplayName("on invoking method: likeEclipse()")
	public class LikeEclipseTests
	{

		@Test
		public void if_target_is_null_method_should_return_null()
		{

			assertEquals( "null", ToString.of(null).likeEclipse() );

		}

		@CsvSource(value={
			"false,SomeType[]",
			"true,org.nerd4j.utils.lang.TestClasses.SomeType[]"
		})
		@ParameterizedTest(name="if fullClassName = {0} the expected output is: {1}")
		@DisplayName("the full class name should ne used according to the configuration")
		public void if_fullClassName_is_selected_the_proper_class_name_should_be_used(
			boolean isFullClassPath, String expected
		)
		{

			final Configurator configurator
			= ToString.of( new TestClasses.SomeType() );

			if( isFullClassPath )
				configurator.withFullClassName();

			assertEquals( expected, configurator.likeEclipse() );

		}

		@CsvSource(value={
			"false,[]",
			"true,[]"
		})
		@ParameterizedTest(name="if noClassName is used the expected output is: {1}")
		@DisplayName("if noClassName is selected no class name should be displayed")
		public void if_noClassName_is_selected_no_class_name_should_be_displayed(
			boolean isFullClassPath, String expected
		)
		{

			final Configurator configurator
			= ToString.of( new TestClasses.SomeType() )
					  .withNoClassName();

			if( isFullClassPath )
				configurator.withFullClassName();

			assertEquals( expected, configurator.likeEclipse() );

		}

		@CsvSource(value={
			"null,false,null[]",
			"null,true,null[]",
			"SomeCustomType,false,SomeCustomType[]",
			"SomeCustomType,true,SomeCustomType[]"
		},nullValues="null")
		@ParameterizedTest(name = "the expected output is: {2}")
		@DisplayName("the proper class name should ne used according to the configuration")
		public void the_proper_class_name_should_be_used(
			String customClassName, boolean isFullClassPath, String expected
		)
		{

			final Configurator configurator
			= ToString.of( new TestClasses.SomeType() )
			.withCustomClassName( customClassName );

			if( isFullClassPath )
				configurator.withFullClassName();

			assertEquals( expected, configurator.likeEclipse() );

		}

		@Nested
		@DisplayName("using method print")
		public class UsingPrint
		{

			@Test
			public void a_null_field_value_should_be_printed_as_null()
			{

				final String value = null;
				final Configurator configurator
				= ToString.of( "SomeClass" )
					.withCustomClassName( "SomeClass" )
					.print( value );

				assertEquals( "SomeClass[null]", configurator.likeEclipse() );

			}

			@Test
			public void a_valued_field_value_should_be_printed()
			{

				final String value = "value";
				final Configurator configurator
				= ToString.of( "SomeClass" )
					.withCustomClassName( "SomeClass" )
					.print( value );

				assertEquals( "SomeClass[value]", configurator.likeEclipse() );

			}

			@Test
			public void a_null_field_name_should_not_be_printed()
			{

				final String value = "value";
				final Configurator configurator
				= ToString.of( "SomeClass" )
					.withCustomClassName( "SomeClass" )
					.print( null, value );

				assertEquals( "SomeClass[value]", configurator.likeEclipse() );

			}

			@Test
			public void a_valued_field_name_should_be_printed()
			{

				final String value = "value";
				final Configurator configurator
				= ToString.of( "SomeClass" )
					.withCustomClassName( "SomeClass" )
					.print( "key", value );

				assertEquals( "SomeClass[key=value]", configurator.likeEclipse() );

			}

			@CsvSource(value={
				"null,null,null,null,'SomeType[null, null]'",
				"null,null,null,value2,'SomeType[null, value2]'",
				"null,null,name2,null,'SomeType[null, name2=null]'",
				"null,null,name2,value2,'SomeType[null, name2=value2]'",
				"null,value1,null,null,'SomeType[value1, null]'",
				"null,value1,null,value2,'SomeType[value1, value2]'",
				"null,value1,name2,null,'SomeType[value1, name2=null]'",
				"null,value1,name2,value2,'SomeType[value1, name2=value2]'",
				"name1,null,null,null,'SomeType[name1=null, null]'",
				"name1,null,null,value2,'SomeType[name1=null, value2]'",
				"name1,null,name2,null,'SomeType[name1=null, name2=null]'",
				"name1,null,name2,value2,'SomeType[name1=null, name2=value2]'",
				"name1,value1,null,null,'SomeType[name1=value1, null]'",
				"name1,value1,null,value2,'SomeType[name1=value1, value2]'",
				"name1,value1,name2,null,'SomeType[name1=value1, name2=null]'",
				"name1,value1,name2,value2,'SomeType[name1=value1, name2=value2]'"
			},nullValues="null")
			@ParameterizedTest(name = "the expected output is: {4}")
			public void multiple_fields_should_be_printed_as_expected(
				String name1, String value1, String name2, String value2, String expected
			)
			{

				final Configurator configurator
				= ToString.of( this )
					.withCustomClassName( "SomeType" )
					.print( name1, value1 )
					.print( name2, value2 );

				assertEquals( expected, configurator.likeEclipse() );

			}

			@Test
			public void arrays_should_be_printed_as_expected()
			{

				assertAll(

					() -> {

						final Configurator configurator
						= ToString.of( this )
							.withCustomClassName( "SomeType" )
							.print( "array", new int[] {1,2,3} );

						assertEquals( "SomeType[array=[1, 2, 3]]",
								configurator.likeEclipse() );

					},

					() -> {

						final Configurator configurator
						= ToString.of( this )
							.withCustomClassName( "SomeType" )
							.print( "array", new String[] {"a","b","c"} );

						assertEquals( "SomeType[array=[a, b, c]]",
								configurator.likeEclipse() );

					},

					() -> {

						final Configurator configurator
						=  ToString.of( this )
							.withCustomClassName( "SomeType" )
							.print( "array",
								new Object[] {
									new int[] { 1,2,3},
									new String[] {"a","b","c"}
								}
							);

						assertEquals( "SomeType[array=[[1, 2, 3], [a, b, c]]]",
								configurator.likeEclipse() );

					}

				);
			}

			/**
			 * Performs unit tests on the method print
			 * with stream and mapping fuctions as arguments.
			 */
			@Nested
			@DisplayName("using streams")
			public class UsingStreams
			{
				
				@Test
				public void the_null_stream_should_be_printed_as_expected()
				{

					final Configurator configurator
					= ToString.of( this )
						.withCustomClassName( "SomeType" )
						.print( (Stream<?>) null )
						.print( null, Function.identity(), Function.identity() );

					assertEquals( "SomeType[]", configurator.likeEclipse() );

				}

				@Test
				public void the_empty_stream_should_be_printed_as_expected()
				{

					final Configurator configurator
					= ToString.of( this )
						.withCustomClassName( "SomeType" )
						.print( Stream.empty() )
						.print( Stream.empty(), Function.identity(), Function.identity() );

					assertEquals( "SomeType[]", configurator.likeEclipse() );

				}


				@CsvSource(value={
					"null,null,'SomeType[null, null]'",
					"null,element2,'SomeType[null, element2]'",
					"element1,null,'SomeType[element1, null]'",
					"element1,element2,'SomeType[element1, element2]'",
				},nullValues="null")
				@ParameterizedTest(name = "the expected output is: {4}")
				public void multiple_elements_should_be_printed_as_expected(
					String element1, String element2, String expected
				)
				{

					final Stream<String> stream = Stream.of( element1, element2 );

					final Configurator configurator
					= ToString.of( this )
						.withCustomClassName( "SomeType" )
						.print( stream );

					assertEquals( expected, configurator.likeEclipse() );

				}

				@CsvSource(value={
					"null,null,null,null,'SomeType[null, null]'",
					"null,null,null,value2,'SomeType[null, value2]'",
					"null,null,name2,null,'SomeType[null, name2=null]'",
					"null,null,name2,value2,'SomeType[null, name2=value2]'",
					"null,value1,null,null,'SomeType[value1, null]'",
					"null,value1,null,value2,'SomeType[value1, value2]'",
					"null,value1,name2,null,'SomeType[value1, name2=null]'",
					"null,value1,name2,value2,'SomeType[value1, name2=value2]'",
					"name1,null,null,null,'SomeType[name1=null, null]'",
					"name1,null,null,value2,'SomeType[name1=null, value2]'",
					"name1,null,name2,null,'SomeType[name1=null, name2=null]'",
					"name1,null,name2,value2,'SomeType[name1=null, name2=value2]'",
					"name1,value1,null,null,'SomeType[name1=value1, null]'",
					"name1,value1,null,value2,'SomeType[name1=value1, value2]'",
					"name1,value1,name2,null,'SomeType[name1=value1, name2=null]'",
					"name1,value1,name2,value2,'SomeType[name1=value1, name2=value2]'"
				},nullValues="null")
				@ParameterizedTest(name = "the expected output is: {4}")
				public void multiple_fields_should_be_printed_as_expected(
					String name1, String value1, String name2, String value2, String expected
				)
				{

					final Pair<String,String> pair1 = Pair.of( name1, value1 );
					final Pair<String,String> pair2 = Pair.of( name2, value2 );

					final Stream<Pair<String,String>> stream = Stream.of( pair1, pair2 );
					final Configurator configurator
					= ToString.of( this )
						.withCustomClassName( "SomeType" )
						.print( stream, Pair::getKey, Pair::getValue );

					assertEquals( expected, configurator.likeEclipse() );

				}

			}

		}

		@Nested
		@DisplayName("using method printIfNotNull")
		public class UsingPrintIfNotNull
		{
			
			@Test
			public void a_null_field_value_should_not_be_printed()
			{

				final String value = null;
				final Configurator configurator
				= ToString.of( "SomeClass" )
					.withCustomClassName( "SomeClass" )
					.printIfNotNull( value );

				assertEquals( "SomeClass[]", configurator.likeEclipse() );

			}

			@Test
			public void a_valued_field_value_should_be_printed()
			{

				final String value = "value";
				final Configurator configurator
				= ToString.of( "SomeClass" )
					.withCustomClassName( "SomeClass" )
					.printIfNotNull( value );

				assertEquals( "SomeClass[value]", configurator.likeEclipse() );

			}

			@Test
			public void a_null_field_name_should_not_be_printed()
			{

				final String value = "value";
				final Configurator configurator
				= ToString.of( "SomeClass" )
					.withCustomClassName( "SomeClass" )
					.printIfNotNull( null, value );

				assertEquals( "SomeClass[value]", configurator.likeEclipse() );

			}

			@Test
			public void a_valued_field_name_should_be_printed()
			{

				final String value = "value";
				final Configurator configurator
				= ToString.of( "SomeClass" )
					.withCustomClassName( "SomeClass" )
					.printIfNotNull( "key", value );

				assertEquals( "SomeClass[key=value]", configurator.likeEclipse() );

			}

			@CsvSource(value={
				"null,null,null,null,'SomeType[]'",
				"null,null,null,value2,'SomeType[value2]'",
				"null,null,name2,null,'SomeType[]'",
				"null,null,name2,value2,'SomeType[name2=value2]'",
				"null,value1,null,null,'SomeType[value1]'",
				"null,value1,null,value2,'SomeType[value1, value2]'",
				"null,value1,name2,null,'SomeType[value1]'",
				"null,value1,name2,value2,'SomeType[value1, name2=value2]'",
				"name1,null,null,null,'SomeType[]'",
				"name1,null,null,value2,'SomeType[value2]'",
				"name1,null,name2,null,'SomeType[]'",
				"name1,null,name2,value2,'SomeType[name2=value2]'",
				"name1,value1,null,null,'SomeType[name1=value1]'",
				"name1,value1,null,value2,'SomeType[name1=value1, value2]'",
				"name1,value1,name2,null,'SomeType[name1=value1]'",
				"name1,value1,name2,value2,'SomeType[name1=value1, name2=value2]'"
			},nullValues="null")
			@ParameterizedTest(name = "the expected output is: {4}")
			public void multiple_fields_should_be_printed_as_expected(
				String name1, String value1, String name2, String value2, String expected
			)
			{

				final Configurator configurator
				= ToString.of( this )
					.withCustomClassName( "SomeType" )
					.printIfNotNull( name1, value1 )
					.printIfNotNull( name2, value2 );

				assertEquals( expected, configurator.likeEclipse() );

			}

			@Test
			public void arrays_should_be_printed_as_expected()
			{

				assertAll(

					() -> {

						final Configurator configurator
						= ToString.of( this )
							.withCustomClassName( "SomeType" )
							.printIfNotNull( "array", new Integer[] {null,2,3} );

						assertEquals( "SomeType[array=[null, 2, 3]]",
								configurator.likeEclipse() );

					},

					() -> {

						final Configurator configurator
						= ToString.of( this )
							.withCustomClassName( "SomeType" )
							.printIfNotNull( "array", new String[] {"a",null,"c"} );

						assertEquals( "SomeType[array=[a, null, c]]",
								configurator.likeEclipse() );

					},

					() -> {

						final Configurator configurator
						=  ToString.of( this )
							.withCustomClassName( "SomeType" )
							.printIfNotNull( "array",
								new Object[] {
									new Integer[] { 1,2,null},
									new String[] {"a","b",null}
								}
							);

						assertEquals( "SomeType[array=[[1, 2, null], [a, b, null]]]",
								configurator.likeEclipse() );

					}

				);
			}

			/**
			 * Performs unit tests on the method print
			 * with stream and mapping fuctions as arguments.
			 */
			@Nested
			@DisplayName("using streams")
			public class UsingStreams
			{
				
				@Test
				public void the_null_stream_should_be_printed_as_expected()
				{

					final Configurator configurator
					= ToString.of( this )
						.withCustomClassName( "SomeType" )
						.printIfNotNull( (Stream<?>) null )
						.printIfNotNull( null, Function.identity(), Function.identity() );

					assertEquals( "SomeType[]", configurator.likeEclipse() );

				}

				@Test
				public void the_empty_stream_should_be_printed_as_expected()
				{

					final Configurator configurator
					= ToString.of( this )
						.withCustomClassName( "SomeType" )
						.printIfNotNull( Stream.empty() )
						.printIfNotNull( Stream.empty(), Function.identity(), Function.identity() );

					assertEquals( "SomeType[]", configurator.likeEclipse() );

				}


				@CsvSource(value={
					"null,null,'SomeType[]'",
					"null,element2,'SomeType[element2]'",
					"element1,null,'SomeType[element1]'",
					"element1,element2,'SomeType[element1, element2]'",
				},nullValues="null")
				@ParameterizedTest(name = "the expected output is: {4}")
				public void multiple_elements_should_be_printed_as_expected(
					String element1, String element2, String expected
				)
				{

					final Stream<String> stream = Stream.of( element1, element2 );

					final Configurator configurator
					= ToString.of( this )
						.withCustomClassName( "SomeType" )
						.printIfNotNull( stream );

					assertEquals( expected, configurator.likeEclipse() );

				}

				@CsvSource(value={
					"null,null,null,null,'SomeType[]'",
					"null,null,null,value2,'SomeType[value2]'",
					"null,null,name2,null,'SomeType[]'",
					"null,null,name2,value2,'SomeType[name2=value2]'",
					"null,value1,null,null,'SomeType[value1]'",
					"null,value1,null,value2,'SomeType[value1, value2]'",
					"null,value1,name2,null,'SomeType[value1]'",
					"null,value1,name2,value2,'SomeType[value1, name2=value2]'",
					"name1,null,null,null,'SomeType[]'",
					"name1,null,null,value2,'SomeType[value2]'",
					"name1,null,name2,null,'SomeType[]'",
					"name1,null,name2,value2,'SomeType[name2=value2]'",
					"name1,value1,null,null,'SomeType[name1=value1]'",
					"name1,value1,null,value2,'SomeType[name1=value1, value2]'",
					"name1,value1,name2,null,'SomeType[name1=value1]'",
					"name1,value1,name2,value2,'SomeType[name1=value1, name2=value2]'"
				},nullValues="null")
				@ParameterizedTest(name = "the expected output is: {4}")
				public void multiple_fields_should_be_printed_as_expected(
					String name1, String value1, String name2, String value2, String expected
				)
				{

					final Pair<String,String> pair1 = Pair.of( name1, value1 );
					final Pair<String,String> pair2 = Pair.of( name2, value2 );

					final Stream<Pair<String,String>> stream = Stream.of( pair1, pair2 );
					final Configurator configurator
					= ToString.of( this )
						.withCustomClassName( "SomeType" )
						.printIfNotNull( stream, Pair::getKey, Pair::getValue );

					assertEquals( expected, configurator.likeEclipse() );

				}

			}

		}

	}


	/* *************** */
	/*  LIKE INTELLIJ  */
	/* *************** */


	/**
     * Performs unit test on the method {@link ToString.Configurator#likeIntellij()}.
     */
	@Nested
	@DisplayName("on invoking method: likeIntellij()")
	public class LikeIntellijTests
	{

		@Test
		public void if_target_is_null_method_should_return_null()
		{

			assertEquals( "null", ToString.of( null ).likeIntellij() );

		}

		@CsvSource(value={
			"false,SomeType{}",
			"true,org.nerd4j.utils.lang.TestClasses.SomeType{}"
		})
		@ParameterizedTest(name="if fullClassName = {0} the expected output is: {1}")
		@DisplayName("the full class name should ne used according to the configuration")
		public void if_fullClassName_is_selected_the_proper_class_name_should_be_used(
				boolean isFullClassPath, String expected
		)
		{

			final Configurator configurator
					= ToString.of( new TestClasses.SomeType() );

			if( isFullClassPath )
				configurator.withFullClassName();

			assertEquals( expected, configurator.likeIntellij() );

		}

		@CsvSource(value={
			"false,{}",
			"true,{}"
		})
		@ParameterizedTest(name="if noClassName is used the expected output is: {1}")
		@DisplayName("if noClassName is selected no class name should be displayed")
		public void if_noClassName_is_selected_no_class_name_should_be_displayed(
				boolean isFullClassPath, String expected
		)
		{

			final Configurator configurator
					= ToString.of( new TestClasses.SomeType() )
					.withNoClassName();

			if( isFullClassPath )
				configurator.withFullClassName();

			assertEquals( expected, configurator.likeIntellij() );

		}

		@CsvSource(value={
			"null,false,null{}",
			"null,true,null{}",
			"SomeCustomType,false,SomeCustomType{}",
			"SomeCustomType,true,SomeCustomType{}"
		},nullValues="null")
		@ParameterizedTest(name = "the expected output is: {2}")
		@DisplayName("the proper class name should ne used according to the configuration")
		public void the_proper_class_name_should_be_used(
				String customClassName, boolean isFullClassPath, String expected
		)
		{

			final Configurator configurator
					= ToString.of( new TestClasses.SomeType() )
					.withCustomClassName( customClassName );

			if( isFullClassPath )
				configurator.withFullClassName();

			assertEquals( expected, configurator.likeIntellij() );

		}

		@Nested
		@DisplayName("using method print")
		public class UsingPrint
		{

			@Test
			public void a_null_field_value_should_be_printed_as_null()
			{

				final String value = null;
				final Configurator configurator
				= ToString.of( "SomeClass" )
					.withCustomClassName( "SomeClass" )
					.print( value );

				assertEquals( "SomeClass{null}", configurator.likeIntellij() );

			}

			@Test
			public void a_valued_field_value_should_be_printed()
			{

				final String value = "value";
				final Configurator configurator
				=  ToString.of( "SomeClass" )
					.withCustomClassName( "SomeClass" )
					.print( value );

				assertEquals( "SomeClass{value}", configurator.likeIntellij() );

			}

			@Test
			public void a_null_field_name_should_not_be_printed()
			{

				final String value = "value";
				final Configurator configurator
				=  ToString.of( "SomeClass" )
					.withCustomClassName( "SomeClass" )
					.print( null, value );

				assertEquals( "SomeClass{value}", configurator.likeIntellij() );

			}

			@Test
			public void a_valued_field_name_should_be_printed()
			{

				final String value = "value";
				final Configurator configurator
				=  ToString.of( "SomeClass" )
					.withCustomClassName( "SomeClass" )
					.print( "key", value );

				assertEquals( "SomeClass{key=value}", configurator.likeIntellij() );

			}


			@CsvSource(value={
				"null,null,null,null,'SomeType{null, null}'",
				"null,null,null,value2,'SomeType{null, value2}'",
				"null,null,name2,null,'SomeType{null, name2=null}'",
				"null,null,name2,value2,'SomeType{null, name2=value2}'",
				"null,value1,null,null,'SomeType{value1, null}'",
				"null,value1,null,value2,'SomeType{value1, value2}'",
				"null,value1,name2,null,'SomeType{value1, name2=null}'",
				"null,value1,name2,value2,'SomeType{value1, name2=value2}'",
				"name1,null,null,null,'SomeType{name1=null, null}'",
				"name1,null,null,value2,'SomeType{name1=null, value2}'",
				"name1,null,name2,null,'SomeType{name1=null, name2=null}'",
				"name1,null,name2,value2,'SomeType{name1=null, name2=value2}'",
				"name1,value1,null,null,'SomeType{name1=value1, null}'",
				"name1,value1,null,value2,'SomeType{name1=value1, value2}'",
				"name1,value1,name2,null,'SomeType{name1=value1, name2=null}'",
				"name1,value1,name2,value2,'SomeType{name1=value1, name2=value2}'"
			},nullValues="null")
			@ParameterizedTest(name = "the expected output is: {4}")
			public void multiple_fields_should_be_printed_as_expected(
				String name1, String value1, String name2, String value2, String expected
			)
			{

				final Configurator configurator
				=  ToString.of( this )
					.withCustomClassName( "SomeType" )
					.print( name1, value1 )
					.print( name2, value2 );

				assertEquals( expected, configurator.likeIntellij() );

			}

			@Test
			public void arrays_should_be_printed_as_expected()
			{

				assertAll(

					() -> {

						final Configurator configurator
						=  ToString.of( this )
							.withCustomClassName( "SomeType" )
							.print( "array", new int[] {1,2,3} );

						assertEquals( "SomeType{array=[1, 2, 3]}",
								configurator.likeIntellij() );

					},

					() -> {

						final Configurator configurator
						=  ToString.of( this )
							.withCustomClassName( "SomeType" )
							.print( "array", new String[] {"a","b","c"} );

						assertEquals( "SomeType{array=[a, b, c]}",
								configurator.likeIntellij() );

					},

					() -> {

						final Configurator configurator
						=  ToString.of( this )
							.withCustomClassName( "SomeType" )
							.print( "array",
								new Object[] {
									new int[] { 1,2,3},
									new String[] {"a","b","c"}
								}
							);

						assertEquals( "SomeType{array=[[1, 2, 3], [a, b, c]]}",
								configurator.likeIntellij() );

					}

				);
			}

			/**
			 * Performs unit tests on the method print
			 * with stream and mapping fuctions as arguments.
			 */
			@Nested
			@DisplayName("using streams")
			public class UsingStreams
			{
				
				@Test
				public void the_null_stream_should_be_printed_as_expected()
				{

					final Configurator configurator
					= ToString.of( this )
						.withCustomClassName( "SomeType" )
						.print( (Stream<?>) null )
						.print( null, Function.identity(), Function.identity() );

					assertEquals( "SomeType{}", configurator.likeIntellij() );

				}

				@Test
				public void the_empty_stream_should_be_printed_as_expected()
				{

					final Configurator configurator
					= ToString.of( this )
						.withCustomClassName( "SomeType" )
						.print( Stream.empty() )
						.print( Stream.empty(), Function.identity(), Function.identity() );

					assertEquals( "SomeType{}", configurator.likeIntellij() );

				}


				@CsvSource(value={
					"null,null,'SomeType{null, null}'",
					"null,element2,'SomeType{null, element2}'",
					"element1,null,'SomeType{element1, null}'",
					"element1,element2,'SomeType{element1, element2}'",
				},nullValues="null")
				@ParameterizedTest(name = "the expected output is: {4}")
				public void multiple_elements_should_be_printed_as_expected(
					String element1, String element2, String expected
				)
				{

					final Stream<String> stream = Stream.of( element1, element2 );

					final Configurator configurator
					= ToString.of( this )
						.withCustomClassName( "SomeType" )
						.print( stream );

					assertEquals( expected, configurator.likeIntellij() );

				}

				@CsvSource(value={
					"null,null,null,null,'SomeType{null, null}'",
					"null,null,null,value2,'SomeType{null, value2}'",
					"null,null,name2,null,'SomeType{null, name2=null}'",
					"null,null,name2,value2,'SomeType{null, name2=value2}'",
					"null,value1,null,null,'SomeType{value1, null}'",
					"null,value1,null,value2,'SomeType{value1, value2}'",
					"null,value1,name2,null,'SomeType{value1, name2=null}'",
					"null,value1,name2,value2,'SomeType{value1, name2=value2}'",
					"name1,null,null,null,'SomeType{name1=null, null}'",
					"name1,null,null,value2,'SomeType{name1=null, value2}'",
					"name1,null,name2,null,'SomeType{name1=null, name2=null}'",
					"name1,null,name2,value2,'SomeType{name1=null, name2=value2}'",
					"name1,value1,null,null,'SomeType{name1=value1, null}'",
					"name1,value1,null,value2,'SomeType{name1=value1, value2}'",
					"name1,value1,name2,null,'SomeType{name1=value1, name2=null}'",
					"name1,value1,name2,value2,'SomeType{name1=value1, name2=value2}'"
				},nullValues="null")
				@ParameterizedTest(name = "the expected output is: {4}")
				public void multiple_fields_should_be_printed_as_expected(
					String name1, String value1, String name2, String value2, String expected
				)
				{

					final Pair<String,String> pair1 = Pair.of( name1, value1 );
					final Pair<String,String> pair2 = Pair.of( name2, value2 );

					final Stream<Pair<String,String>> stream = Stream.of( pair1, pair2 );
					final Configurator configurator
					= ToString.of( this )
						.withCustomClassName( "SomeType" )
						.print( stream, Pair::getKey, Pair::getValue );

					assertEquals( expected, configurator.likeIntellij() );

				}

			}

		}

		@Nested
		@DisplayName("using method printIfNotNull")
		public class UsingPrintIfNotNull
		{
			
			@Test
			public void a_null_field_value_should_not_be_printed()
			{

				final String value = null;
				final Configurator configurator
				= ToString.of( "SomeClass" )
					.withCustomClassName( "SomeClass" )
					.printIfNotNull( value );

				assertEquals( "SomeClass{}", configurator.likeIntellij() );

			}

			@Test
			public void a_valued_field_value_should_be_printed()
			{

				final String value = "value";
				final Configurator configurator
				= ToString.of( "SomeClass" )
					.withCustomClassName( "SomeClass" )
					.printIfNotNull( value );

				assertEquals( "SomeClass{value}", configurator.likeIntellij() );

			}

			@Test
			public void a_null_field_name_should_not_be_printed()
			{

				final String value = "value";
				final Configurator configurator
				= ToString.of( "SomeClass" )
					.withCustomClassName( "SomeClass" )
					.printIfNotNull( null, value );

				assertEquals( "SomeClass{value}", configurator.likeIntellij() );

			}

			@Test
			public void a_valued_field_name_should_be_printed()
			{

				final String value = "value";
				final Configurator configurator
				= ToString.of( "SomeClass" )
					.withCustomClassName( "SomeClass" )
					.printIfNotNull( "key", value );

				assertEquals( "SomeClass{key=value}", configurator.likeIntellij() );

			}

			@CsvSource(value={
				"null,null,null,null,'SomeType{}'",
				"null,null,null,value2,'SomeType{value2}'",
				"null,null,name2,null,'SomeType{}'",
				"null,null,name2,value2,'SomeType{name2=value2}'",
				"null,value1,null,null,'SomeType{value1}'",
				"null,value1,null,value2,'SomeType{value1, value2}'",
				"null,value1,name2,null,'SomeType{value1}'",
				"null,value1,name2,value2,'SomeType{value1, name2=value2}'",
				"name1,null,null,null,'SomeType{}'",
				"name1,null,null,value2,'SomeType{value2}'",
				"name1,null,name2,null,'SomeType{}'",
				"name1,null,name2,value2,'SomeType{name2=value2}'",
				"name1,value1,null,null,'SomeType{name1=value1}'",
				"name1,value1,null,value2,'SomeType{name1=value1, value2}'",
				"name1,value1,name2,null,'SomeType{name1=value1}'",
				"name1,value1,name2,value2,'SomeType{name1=value1, name2=value2}'"
			},nullValues="null")
			@ParameterizedTest(name = "the expected output is: {4}")
			public void multiple_fields_should_be_printed_as_expected(
				String name1, String value1, String name2, String value2, String expected
			)
			{

				final Configurator configurator
				= ToString.of( this )
					.withCustomClassName( "SomeType" )
					.printIfNotNull( name1, value1 )
					.printIfNotNull( name2, value2 );

				assertEquals( expected, configurator.likeIntellij() );

			}

			@Test
			public void arrays_should_be_printed_as_expected()
			{

				assertAll(

					() -> {

						final Configurator configurator
						= ToString.of( this )
							.withCustomClassName( "SomeType" )
							.printIfNotNull( "array", new Integer[] {null,2,3} );

						assertEquals( "SomeType{array=[null, 2, 3]}",
								configurator.likeIntellij() );

					},

					() -> {

						final Configurator configurator
						= ToString.of( this )
							.withCustomClassName( "SomeType" )
							.printIfNotNull( "array", new String[] {"a",null,"c"} );

						assertEquals( "SomeType{array=[a, null, c]}",
								configurator.likeIntellij() );

					},

					() -> {

						final Configurator configurator
						=  ToString.of( this )
							.withCustomClassName( "SomeType" )
							.printIfNotNull( "array",
								new Object[] {
									new Integer[] { 1,2,null},
									new String[] {"a","b",null}
								}
							);

						assertEquals( "SomeType{array=[[1, 2, null], [a, b, null]]}",
								configurator.likeIntellij() );

					}

				);
			}

			/**
			 * Performs unit tests on the method print
			 * with stream and mapping fuctions as arguments.
			 */
			@Nested
			@DisplayName("using streams")
			public class UsingStreams
			{
				
				@Test
				public void the_null_stream_should_be_printed_as_expected()
				{

					final Configurator configurator
					= ToString.of( this )
						.withCustomClassName( "SomeType" )
						.printIfNotNull( (Stream<?>) null )
						.printIfNotNull( null, Function.identity(), Function.identity() );

					assertEquals( "SomeType{}", configurator.likeIntellij() );

				}

				@Test
				public void the_empty_stream_should_be_printed_as_expected()
				{

					final Configurator configurator
					= ToString.of( this )
						.withCustomClassName( "SomeType" )
						.printIfNotNull( Stream.empty() )
						.printIfNotNull( Stream.empty(), Function.identity(), Function.identity() );

					assertEquals( "SomeType{}", configurator.likeIntellij() );

				}


				@CsvSource(value={
					"null,null,'SomeType{}'",
					"null,element2,'SomeType{element2}'",
					"element1,null,'SomeType{element1}'",
					"element1,element2,'SomeType{element1, element2}'",
				},nullValues="null")
				@ParameterizedTest(name = "the expected output is: {4}")
				public void multiple_elements_should_be_printed_as_expected(
					String element1, String element2, String expected
				)
				{

					final Stream<String> stream = Stream.of( element1, element2 );

					final Configurator configurator
					= ToString.of( this )
						.withCustomClassName( "SomeType" )
						.printIfNotNull( stream );

					assertEquals( expected, configurator.likeIntellij() );

				}

				@CsvSource(value={
					"null,null,null,null,'SomeType{}'",
					"null,null,null,value2,'SomeType{value2}'",
					"null,null,name2,null,'SomeType{}'",
					"null,null,name2,value2,'SomeType{name2=value2}'",
					"null,value1,null,null,'SomeType{value1}'",
					"null,value1,null,value2,'SomeType{value1, value2}'",
					"null,value1,name2,null,'SomeType{value1}'",
					"null,value1,name2,value2,'SomeType{value1, name2=value2}'",
					"name1,null,null,null,'SomeType{}'",
					"name1,null,null,value2,'SomeType{value2}'",
					"name1,null,name2,null,'SomeType{}'",
					"name1,null,name2,value2,'SomeType{name2=value2}'",
					"name1,value1,null,null,'SomeType{name1=value1}'",
					"name1,value1,null,value2,'SomeType{name1=value1, value2}'",
					"name1,value1,name2,null,'SomeType{name1=value1}'",
					"name1,value1,name2,value2,'SomeType{name1=value1, name2=value2}'"
				},nullValues="null")
				@ParameterizedTest(name = "the expected output is: {4}")
				public void multiple_fields_should_be_printed_as_expected(
					String name1, String value1, String name2, String value2, String expected
				)
				{

					final Pair<String,String> pair1 = Pair.of( name1, value1 );
					final Pair<String,String> pair2 = Pair.of( name2, value2 );

					final Stream<Pair<String,String>> stream = Stream.of( pair1, pair2 );
					final Configurator configurator
					= ToString.of( this )
						.withCustomClassName( "SomeType" )
						.printIfNotNull( stream, Pair::getKey, Pair::getValue );

					assertEquals( expected, configurator.likeIntellij() );

				}

			}

		}

	}


	/* *************** */
	/*  LIKE FUNCTION  */
	/* *************** */


	/**
     * Performs unit test on the method {@link ToString.Configurator#likeFunction()}.
     */
	@Nested
	@DisplayName("on invoking method: likeFunction()")
	public class LikeFunctionTests
	{


		@Test
		public void if_target_is_null_method_should_return_null()
		{

			assertEquals( "null", ToString.of( null ).likeFunction() );

		}


		@CsvSource(value={
			"false,SomeType()",
			"true,org.nerd4j.utils.lang.TestClasses.SomeType()"
		})
		@ParameterizedTest(name="if fullClassName = {0} the expected output is: {1}")
		@DisplayName("the full class name should ne used according to the configuration")
		public void if_fullClassName_is_selected_the_proper_class_name_should_be_used(
				boolean isFullClassPath, String expected
		)
		{

			final Configurator configurator
					= ToString.of( new TestClasses.SomeType() );

			if( isFullClassPath )
				configurator.withFullClassName();

			assertEquals( expected, configurator.likeFunction() );

		}

		@CsvSource(value={
			"false,()",
			"true,()"
		})
		@ParameterizedTest(name="if noClassName is used the expected output is: {1}")
		@DisplayName("if noClassName is selected no class name should be displayed")
		public void if_noClassName_is_selected_no_class_name_should_be_displayed(
				boolean isFullClassPath, String expected
		)
		{

			final Configurator configurator
					= ToString.of( new TestClasses.SomeType() )
					.withNoClassName();

			if( isFullClassPath )
				configurator.withFullClassName();

			assertEquals( expected, configurator.likeFunction() );

		}

		@CsvSource(value={
			"null,false,null()",
			"null,true,null()",
			"SomeCustomType,false,SomeCustomType()",
			"SomeCustomType,true,SomeCustomType()"
		},nullValues="null")
		@ParameterizedTest(name = "the expected output is: {2}")
		@DisplayName("the proper class name should ne used according to the configuration")
		public void the_proper_class_name_should_be_used(
				String customClassName, boolean isFullClassPath, String expected
		)
		{

			final Configurator configurator
					= ToString.of( new TestClasses.SomeType() )
					.withCustomClassName( customClassName );

			if( isFullClassPath )
				configurator.withFullClassName();

			assertEquals( expected, configurator.likeFunction() );

		}

		@Nested
		@DisplayName("using method print")
		public class UsingPrint
		{

			@Test
			public void a_null_field_value_should_be_printed_as_null()
			{

				final String value = null;
				final Configurator configurator
				=  ToString.of( "SomeClass" )
					.withCustomClassName( "SomeClass" )
					.print( value );

				assertEquals( "SomeClass(null)", configurator.likeFunction() );

			}

			@Test
			public void a_valued_field_value_should_be_printed()
			{

				final String value = "value";
				final Configurator configurator
				=  ToString.of( "SomeClass" )
					.withCustomClassName( "SomeClass" )
					.print( value );

				assertEquals( "SomeClass(value)", configurator.likeFunction() );

			}

			@Test
			public void a_null_field_name_should_not_be_printed()
			{

				final String value = "value";
				final Configurator configurator
				=  ToString.of( "SomeClass" )
					.withCustomClassName( "SomeClass" )
					.print( null, value );

				assertEquals( "SomeClass(value)", configurator.likeFunction() );

			}

			@Test
			public void a_valued_field_name_should_be_printed()
			{

				final String value = "value";
				final Configurator configurator
				=  ToString.of( "SomeClass" )
					.withCustomClassName( "SomeClass" )
					.print( "key", value );

				assertEquals( "SomeClass(key:value)", configurator.likeFunction() );

			}


			@CsvSource(value={
				"null,null,null,null,'SomeType(null, null)'",
				"null,null,null,value2,'SomeType(null, value2)'",
				"null,null,name2,null,'SomeType(null, name2:null)'",
				"null,null,name2,value2,'SomeType(null, name2:value2)'",
				"null,value1,null,null,'SomeType(value1, null)'",
				"null,value1,null,value2,'SomeType(value1, value2)'",
				"null,value1,name2,null,'SomeType(value1, name2:null)'",
				"null,value1,name2,value2,'SomeType(value1, name2:value2)'",
				"name1,null,null,null,'SomeType(name1:null, null)'",
				"name1,null,null,value2,'SomeType(name1:null, value2)'",
				"name1,null,name2,null,'SomeType(name1:null, name2:null)'",
				"name1,null,name2,value2,'SomeType(name1:null, name2:value2)'",
				"name1,value1,null,null,'SomeType(name1:value1, null)'",
				"name1,value1,null,value2,'SomeType(name1:value1, value2)'",
				"name1,value1,name2,null,'SomeType(name1:value1, name2:null)'",
				"name1,value1,name2,value2,'SomeType(name1:value1, name2:value2)'"
			},nullValues="null")
			@ParameterizedTest(name = "the expected output is: {4}")
			public void multiple_fields_should_be_printed_as_expected(
				String name1, String value1, String name2, String value2, String expected
			)
			{

				final Configurator configurator
				=  ToString.of( this )
					.withCustomClassName( "SomeType" )
					.print( name1, value1 )
					.print( name2, value2 );

				assertEquals( expected, configurator.likeFunction() );

			}


			@Test
			public void arrays_should_be_printed_as_expected()
			{

				assertAll(

					() -> {

						final Configurator configurator
						= ToString.of( this )
							.withCustomClassName( "SomeType" )
							.print( "array", new int[] {1,2,3} );

						assertEquals( "SomeType(array:[1, 2, 3])",
								configurator.likeFunction() );

					},

					() -> {

						final Configurator configurator
						= ToString.of( this )
							.withCustomClassName( "SomeType" )
							.print( "array", new String[] {"a","b","c"} );

						assertEquals( "SomeType(array:[a, b, c])",
								configurator.likeFunction() );

					},

					() -> {

						final Configurator configurator
						=  ToString.of( this )
							.withCustomClassName( "SomeType" )
							.print( "array",
								new Object[] {
									new int[] { 1,2,3},
									new String[] {"a","b","c"}
								}
							);

						assertEquals( "SomeType(array:[[1, 2, 3], [a, b, c]])",
								configurator.likeFunction() );

					}

				);
			}
					
			/**
			 * Performs unit tests on the method print
			 * with stream and mapping fuctions as arguments.
			 */
			@Nested
			@DisplayName("using streams")
			public class UsingStreams
			{
				
				@Test
				public void the_null_stream_should_be_printed_as_expected()
				{

					final Configurator configurator
					= ToString.of( this )
						.withCustomClassName( "SomeType" )
						.print( (Stream<?>) null )
						.print( null, Function.identity(), Function.identity() );

					assertEquals( "SomeType()", configurator.likeFunction() );

				}

				@Test
				public void the_empty_stream_should_be_printed_as_expected()
				{

					final Configurator configurator
					= ToString.of( this )
						.withCustomClassName( "SomeType" )
						.print( Stream.empty() )
						.print( Stream.empty(), Function.identity(), Function.identity() );

					assertEquals( "SomeType()", configurator.likeFunction() );

				}


				@CsvSource(value={
					"null,null,'SomeType(null, null)'",
					"null,element2,'SomeType(null, element2)'",
					"element1,null,'SomeType(element1, null)'",
					"element1,element2,'SomeType(element1, element2)'",
				},nullValues="null")
				@ParameterizedTest(name = "the expected output is: {4}")
				public void multiple_elements_should_be_printed_as_expected(
					String element1, String element2, String expected
				)
				{

					final Stream<String> stream = Stream.of( element1, element2 );

					final Configurator configurator
					= ToString.of( this )
						.withCustomClassName( "SomeType" )
						.print( stream );

					assertEquals( expected, configurator.likeFunction() );

				}

				@CsvSource(value={
					"null,null,null,null,'SomeType(null, null)'",
					"null,null,null,value2,'SomeType(null, value2)'",
					"null,null,name2,null,'SomeType(null, name2:null)'",
					"null,null,name2,value2,'SomeType(null, name2:value2)'",
					"null,value1,null,null,'SomeType(value1, null)'",
					"null,value1,null,value2,'SomeType(value1, value2)'",
					"null,value1,name2,null,'SomeType(value1, name2:null)'",
					"null,value1,name2,value2,'SomeType(value1, name2:value2)'",
					"name1,null,null,null,'SomeType(name1:null, null)'",
					"name1,null,null,value2,'SomeType(name1:null, value2)'",
					"name1,null,name2,null,'SomeType(name1:null, name2:null)'",
					"name1,null,name2,value2,'SomeType(name1:null, name2:value2)'",
					"name1,value1,null,null,'SomeType(name1:value1, null)'",
					"name1,value1,null,value2,'SomeType(name1:value1, value2)'",
					"name1,value1,name2,null,'SomeType(name1:value1, name2:null)'",
					"name1,value1,name2,value2,'SomeType(name1:value1, name2:value2)'"
				},nullValues="null")
				@ParameterizedTest(name = "the expected output is: {4}")
				public void multiple_fields_should_be_printed_as_expected(
					String name1, String value1, String name2, String value2, String expected
				)
				{

					final Pair<String,String> pair1 = Pair.of( name1, value1 );
					final Pair<String,String> pair2 = Pair.of( name2, value2 );

					final Stream<Pair<String,String>> stream = Stream.of( pair1, pair2 );
					final Configurator configurator
					= ToString.of( this )
						.withCustomClassName( "SomeType" )
						.print( stream, Pair::getKey, Pair::getValue );

					assertEquals( expected, configurator.likeFunction() );

				}

			}

		}

		@Nested
		@DisplayName("using method printIfNotNull")
		public class UsingPrintIfNotNull
		{
			
			@Test
			public void a_null_field_value_should_not_be_printed()
			{

				final String value = null;
				final Configurator configurator
				= ToString.of( "SomeClass" )
					.withCustomClassName( "SomeClass" )
					.printIfNotNull( value );

				assertEquals( "SomeClass()", configurator.likeFunction() );

			}

			@Test
			public void a_valued_field_value_should_be_printed()
			{

				final String value = "value";
				final Configurator configurator
				= ToString.of( "SomeClass" )
					.withCustomClassName( "SomeClass" )
					.printIfNotNull( value );

				assertEquals( "SomeClass(value)", configurator.likeFunction() );

			}

			@Test
			public void a_null_field_name_should_not_be_printed()
			{

				final String value = "value";
				final Configurator configurator
				= ToString.of( "SomeClass" )
					.withCustomClassName( "SomeClass" )
					.printIfNotNull( null, value );

				assertEquals( "SomeClass(value)", configurator.likeFunction() );

			}

			@Test
			public void a_valued_field_name_should_be_printed()
			{

				final String value = "value";
				final Configurator configurator
				= ToString.of( "SomeClass" )
					.withCustomClassName( "SomeClass" )
					.printIfNotNull( "key", value );

				assertEquals( "SomeClass(key:value)", configurator.likeFunction() );

			}

			@CsvSource(value={
				"null,null,null,null,'SomeType()'",
				"null,null,null,value2,'SomeType(value2)'",
				"null,null,name2,null,'SomeType()'",
				"null,null,name2,value2,'SomeType(name2:value2)'",
				"null,value1,null,null,'SomeType(value1)'",
				"null,value1,null,value2,'SomeType(value1, value2)'",
				"null,value1,name2,null,'SomeType(value1)'",
				"null,value1,name2,value2,'SomeType(value1, name2:value2)'",
				"name1,null,null,null,'SomeType()'",
				"name1,null,null,value2,'SomeType(value2)'",
				"name1,null,name2,null,'SomeType()'",
				"name1,null,name2,value2,'SomeType(name2:value2)'",
				"name1,value1,null,null,'SomeType(name1:value1)'",
				"name1,value1,null,value2,'SomeType(name1:value1, value2)'",
				"name1,value1,name2,null,'SomeType(name1:value1)'",
				"name1,value1,name2,value2,'SomeType(name1:value1, name2:value2)'"
			},nullValues="null")
			@ParameterizedTest(name = "the expected output is: {4}")
			public void multiple_fields_should_be_printed_as_expected(
				String name1, String value1, String name2, String value2, String expected
			)
			{

				final Configurator configurator
				= ToString.of( this )
					.withCustomClassName( "SomeType" )
					.printIfNotNull( name1, value1 )
					.printIfNotNull( name2, value2 );

				assertEquals( expected, configurator.likeFunction() );

			}

			@Test
			public void arrays_should_be_printed_as_expected()
			{

				assertAll(

					() -> {

						final Configurator configurator
						= ToString.of( this )
							.withCustomClassName( "SomeType" )
							.printIfNotNull( "array", new Integer[] {null,2,3} );

						assertEquals( "SomeType(array:[null, 2, 3])",
								configurator.likeFunction() );

					},

					() -> {

						final Configurator configurator
						= ToString.of( this )
							.withCustomClassName( "SomeType" )
							.printIfNotNull( "array", new String[] {"a",null,"c"} );

						assertEquals( "SomeType(array:[a, null, c])",
								configurator.likeFunction() );

					},

					() -> {

						final Configurator configurator
						=  ToString.of( this )
							.withCustomClassName( "SomeType" )
							.printIfNotNull( "array",
								new Object[] {
									new Integer[] { 1,2,null},
									new String[] {"a","b",null}
								}
							);

						assertEquals( "SomeType(array:[[1, 2, null], [a, b, null]])",
								configurator.likeFunction() );

					}

				);
			}

			/**
			 * Performs unit tests on the method print
			 * with stream and mapping fuctions as arguments.
			 */
			@Nested
			@DisplayName("using streams")
			public class UsingStreams
			{
				
				@Test
				public void the_null_stream_should_be_printed_as_expected()
				{

					final Configurator configurator
					= ToString.of( this )
						.withCustomClassName( "SomeType" )
						.printIfNotNull( (Stream<?>) null )
						.printIfNotNull( null, Function.identity(), Function.identity() );

					assertEquals( "SomeType()", configurator.likeFunction() );

				}

				@Test
				public void the_empty_stream_should_be_printed_as_expected()
				{

					final Configurator configurator
					= ToString.of( this )
						.withCustomClassName( "SomeType" )
						.printIfNotNull( Stream.empty() )
						.printIfNotNull( Stream.empty(), Function.identity(), Function.identity() );

					assertEquals( "SomeType()", configurator.likeFunction() );

				}

				@CsvSource(value={
					"null,null,'SomeType()'",
					"null,element2,'SomeType(element2)'",
					"element1,null,'SomeType(element1)'",
					"element1,element2,'SomeType(element1, element2)'",
				},nullValues="null")
				@ParameterizedTest(name = "the expected output is: {4}")
				public void multiple_elements_should_be_printed_as_expected(
					String element1, String element2, String expected
				)
				{

					final Stream<String> stream = Stream.of( element1, element2 );

					final Configurator configurator
					= ToString.of( this )
						.withCustomClassName( "SomeType" )
						.printIfNotNull( stream );

					assertEquals( expected, configurator.likeFunction() );

				}

				@CsvSource(value={
					"null,null,null,null,'SomeType()'",
					"null,null,null,value2,'SomeType(value2)'",
					"null,null,name2,null,'SomeType()'",
					"null,null,name2,value2,'SomeType(name2:value2)'",
					"null,value1,null,null,'SomeType(value1)'",
					"null,value1,null,value2,'SomeType(value1, value2)'",
					"null,value1,name2,null,'SomeType(value1)'",
					"null,value1,name2,value2,'SomeType(value1, name2:value2)'",
					"name1,null,null,null,'SomeType()'",
					"name1,null,null,value2,'SomeType(value2)'",
					"name1,null,name2,null,'SomeType()'",
					"name1,null,name2,value2,'SomeType(name2:value2)'",
					"name1,value1,null,null,'SomeType(name1:value1)'",
					"name1,value1,null,value2,'SomeType(name1:value1, value2)'",
					"name1,value1,name2,null,'SomeType(name1:value1)'",
					"name1,value1,name2,value2,'SomeType(name1:value1, name2:value2)'"
				},nullValues="null")
				@ParameterizedTest(name = "the expected output is: {4}")
				public void multiple_fields_should_be_printed_as_expected(
					String name1, String value1, String name2, String value2, String expected
				)
				{

					final Pair<String,String> pair1 = Pair.of( name1, value1 );
					final Pair<String,String> pair2 = Pair.of( name2, value2 );

					final Stream<Pair<String,String>> stream = Stream.of( pair1, pair2 );
					final Configurator configurator
					= ToString.of( this )
						.withCustomClassName( "SomeType" )
						.printIfNotNull( stream, Pair::getKey, Pair::getValue );

					assertEquals( expected, configurator.likeFunction() );

				}

			}

		}

	}


	/* ************ */
	/*  LIKE TUPLE  */
	/* ************ */


	/**
	 * Performs unit test on the method {@link ToString.Configurator#likeTuple()}.
	 */
	@Nested
	@DisplayName("on invoking method: likeTuple()")
	public class LikeTupleTests
	{


		@Test
		public void if_target_is_null_method_should_return_null()
		{

			assertEquals( "null", ToString.of( null ).likeTuple() );

		}


		@CsvSource(value={
			"false,SomeType<>",
			"true,org.nerd4j.utils.lang.TestClasses.SomeType<>"
		})
		@ParameterizedTest(name="if fullClassName = {0} the expected output is: {1}")
		@DisplayName("the full class name should ne used according to the configuration")
		public void if_fullClassName_is_selected_the_proper_class_name_should_be_used(
				boolean isFullClassPath, String expected
		)
		{

			final Configurator configurator
					= ToString.of( new TestClasses.SomeType() );

			if( isFullClassPath )
				configurator.withFullClassName();

			assertEquals( expected, configurator.likeTuple() );

		}

		@CsvSource(value={
			"false,<>",
			"true,<>"
		})
		@ParameterizedTest(name="if noClassName is used the expected output is: {1}")
		@DisplayName("if noClassName is selected no class name should be displayed")
		public void if_noClassName_is_selected_no_class_name_should_be_displayed(
				boolean isFullClassPath, String expected
		)
		{

			final Configurator configurator
					= ToString.of( new TestClasses.SomeType() )
					.withNoClassName();

			if( isFullClassPath )
				configurator.withFullClassName();

			assertEquals( expected, configurator.likeTuple() );

		}

		@CsvSource(value={
			"null,false,null<>",
			"null,true,null<>",
			"SomeCustomType,false,SomeCustomType<>",
			"SomeCustomType,true,SomeCustomType<>"
		},nullValues="null")
		@ParameterizedTest(name = "the expected output is: {2}")
		@DisplayName("the proper class name should ne used according to the configuration")
		public void the_proper_class_name_should_be_used(
				String customClassName, boolean isFullClassPath, String expected
		)
		{

			final Configurator configurator
					= ToString.of( new TestClasses.SomeType() )
					.withCustomClassName( customClassName );

			if( isFullClassPath )
				configurator.withFullClassName();

			assertEquals( expected, configurator.likeTuple() );

		}

		@Nested
		@DisplayName("using method print")
		public class UsingPrint
		{

			@Test
			public void a_null_field_value_should_be_printed_as_null()
			{

				final String value = null;
				final Configurator configurator
						=  ToString.of( "SomeClass" )
						.withCustomClassName( "SomeClass" )
						.print( value );

				assertEquals( "SomeClass<null>", configurator.likeTuple() );

			}

			@Test
			public void a_valued_field_value_should_be_printed()
			{

				final String value = "value";
				final Configurator configurator
						=  ToString.of( "SomeClass" )
						.withCustomClassName( "SomeClass" )
						.print( value );

				assertEquals( "SomeClass<value>", configurator.likeTuple() );

			}

			@Test
			public void a_null_field_name_should_not_be_printed()
			{

				final String value = "value";
				final Configurator configurator
						=  ToString.of( "SomeClass" )
						.withCustomClassName( "SomeClass" )
						.print( null, value );

				assertEquals( "SomeClass<value>", configurator.likeTuple() );

			}

			@Test
			public void a_valued_field_name_should_be_printed()
			{

				final String value = "value";
				final Configurator configurator
						=  ToString.of( "SomeClass" )
						.withCustomClassName( "SomeClass" )
						.print( "key", value );

				assertEquals( "SomeClass<key:value>", configurator.likeTuple() );

			}


			@CsvSource(value={
				"null,null,null,null,'SomeType<null, null>'",
				"null,null,null,value2,'SomeType<null, value2>'",
				"null,null,name2,null,'SomeType<null, name2:null>'",
				"null,null,name2,value2,'SomeType<null, name2:value2>'",
				"null,value1,null,null,'SomeType<value1, null>'",
				"null,value1,null,value2,'SomeType<value1, value2>'",
				"null,value1,name2,null,'SomeType<value1, name2:null>'",
				"null,value1,name2,value2,'SomeType<value1, name2:value2>'",
				"name1,null,null,null,'SomeType<name1:null, null>'",
				"name1,null,null,value2,'SomeType<name1:null, value2>'",
				"name1,null,name2,null,'SomeType<name1:null, name2:null>'",
				"name1,null,name2,value2,'SomeType<name1:null, name2:value2>'",
				"name1,value1,null,null,'SomeType<name1:value1, null>'",
				"name1,value1,null,value2,'SomeType<name1:value1, value2>'",
				"name1,value1,name2,null,'SomeType<name1:value1, name2:null>'",
				"name1,value1,name2,value2,'SomeType<name1:value1, name2:value2>'"
			},nullValues="null")
			@ParameterizedTest(name = "the expected output is: {4}")
			public void multiple_fields_should_be_printed_as_expected(
					String name1, String value1, String name2, String value2, String expected
			)
			{

				final Configurator configurator
						=  ToString.of( this )
						.withCustomClassName( "SomeType" )
						.print( name1, value1 )
						.print( name2, value2 );

				assertEquals( expected, configurator.likeTuple() );

			}

			@Test
			public void arrays_should_be_printed_as_expected()
			{

				assertAll(

					() -> {

						final Configurator configurator
						= ToString.of( this )
							.withCustomClassName( "SomeType" )
							.print( "array", new int[] {1,2,3} );

						assertEquals( "SomeType<array:[1, 2, 3]>",
								configurator.likeTuple() );

					},

					() -> {

						final Configurator configurator
						= ToString.of( this )
							.withCustomClassName( "SomeType" )
							.print( "array", new String[] {"a","b","c"} );

						assertEquals( "SomeType<array:[a, b, c]>",
								configurator.likeTuple() );

					},

					() -> {

						final Configurator configurator
						=  ToString.of( this )
							.withCustomClassName( "SomeType" )
							.print( "array",
								new Object[] {
									new int[] { 1,2,3},
									new String[] {"a","b","c"}
								}
							);

						assertEquals( "SomeType<array:[[1, 2, 3], [a, b, c]]>",
								configurator.likeTuple() );

					}

				);
			}
							
			/**
			 * Performs unit tests on the method print
			 * with stream and mapping fuctions as arguments.
			 */
			@Nested
			@DisplayName("using streams")
			public class UsingStreams
			{
				
				@Test
				public void the_null_stream_should_be_printed_as_expected()
				{

					final Configurator configurator
					= ToString.of( this )
						.withCustomClassName( "SomeType" )
						.print( (Stream<?>) null )
						.print( null, Function.identity(), Function.identity() );

					assertEquals( "SomeType<>", configurator.likeTuple() );

				}

				@Test
				public void the_empty_stream_should_be_printed_as_expected()
				{

					final Configurator configurator
					= ToString.of( this )
						.withCustomClassName( "SomeType" )
						.print( Stream.empty() )
						.print( Stream.empty(), Function.identity(), Function.identity() );

					assertEquals( "SomeType<>", configurator.likeTuple() );

				}


				@CsvSource(value={
					"null,null,'SomeType<null, null>'",
					"null,element2,'SomeType<null, element2>'",
					"element1,null,'SomeType<element1, null>'",
					"element1,element2,'SomeType<element1, element2>'",
				},nullValues="null")
				@ParameterizedTest(name = "the expected output is: {4}")
				public void multiple_elements_should_be_printed_as_expected(
					String element1, String element2, String expected
				)
				{

					final Stream<String> stream = Stream.of( element1, element2 );

					final Configurator configurator
					= ToString.of( this )
						.withCustomClassName( "SomeType" )
						.print( stream );

					assertEquals( expected, configurator.likeTuple() );

				}

				@CsvSource(value={
					"null,null,null,null,'SomeType<null, null>'",
					"null,null,null,value2,'SomeType<null, value2>'",
					"null,null,name2,null,'SomeType<null, name2:null>'",
					"null,null,name2,value2,'SomeType<null, name2:value2>'",
					"null,value1,null,null,'SomeType<value1, null>'",
					"null,value1,null,value2,'SomeType<value1, value2>'",
					"null,value1,name2,null,'SomeType<value1, name2:null>'",
					"null,value1,name2,value2,'SomeType<value1, name2:value2>'",
					"name1,null,null,null,'SomeType<name1:null, null>'",
					"name1,null,null,value2,'SomeType<name1:null, value2>'",
					"name1,null,name2,null,'SomeType<name1:null, name2:null>'",
					"name1,null,name2,value2,'SomeType<name1:null, name2:value2>'",
					"name1,value1,null,null,'SomeType<name1:value1, null>'",
					"name1,value1,null,value2,'SomeType<name1:value1, value2>'",
					"name1,value1,name2,null,'SomeType<name1:value1, name2:null>'",
					"name1,value1,name2,value2,'SomeType<name1:value1, name2:value2>'"
				},nullValues="null")
				@ParameterizedTest(name = "the expected output is: {4}")
				public void multiple_fields_should_be_printed_as_expected(
					String name1, String value1, String name2, String value2, String expected
				)
				{

					final Pair<String,String> pair1 = Pair.of( name1, value1 );
					final Pair<String,String> pair2 = Pair.of( name2, value2 );

					final Stream<Pair<String,String>> stream = Stream.of( pair1, pair2 );
					final Configurator configurator
					= ToString.of( this )
						.withCustomClassName( "SomeType" )
						.print( stream, Pair::getKey, Pair::getValue );

					assertEquals( expected, configurator.likeTuple() );

				}

			}

		}

		@Nested
		@DisplayName("using method printIfNotNull")
		public class UsingPrintIfNotNull
		{
			
			@Test
			public void a_null_field_value_should_not_be_printed()
			{

				final String value = null;
				final Configurator configurator
				= ToString.of( "SomeClass" )
					.withCustomClassName( "SomeClass" )
					.printIfNotNull( value );

				assertEquals( "SomeClass<>", configurator.likeTuple() );

			}

			@Test
			public void a_valued_field_value_should_be_printed()
			{

				final String value = "value";
				final Configurator configurator
				= ToString.of( "SomeClass" )
					.withCustomClassName( "SomeClass" )
					.printIfNotNull( value );

				assertEquals( "SomeClass<value>", configurator.likeTuple() );

			}

			@Test
			public void a_null_field_name_should_not_be_printed()
			{

				final String value = "value";
				final Configurator configurator
				= ToString.of( "SomeClass" )
					.withCustomClassName( "SomeClass" )
					.printIfNotNull( null, value );

				assertEquals( "SomeClass<value>", configurator.likeTuple() );

			}

			@Test
			public void a_valued_field_name_should_be_printed()
			{

				final String value = "value";
				final Configurator configurator
				= ToString.of( "SomeClass" )
					.withCustomClassName( "SomeClass" )
					.printIfNotNull( "key", value );

				assertEquals( "SomeClass<key:value>", configurator.likeTuple() );

			}

			@CsvSource(value={
				"null,null,null,null,'SomeType<>'",
				"null,null,null,value2,'SomeType<value2>'",
				"null,null,name2,null,'SomeType<>'",
				"null,null,name2,value2,'SomeType<name2:value2>'",
				"null,value1,null,null,'SomeType<value1>'",
				"null,value1,null,value2,'SomeType<value1, value2>'",
				"null,value1,name2,null,'SomeType<value1>'",
				"null,value1,name2,value2,'SomeType<value1, name2:value2>'",
				"name1,null,null,null,'SomeType<>'",
				"name1,null,null,value2,'SomeType<value2>'",
				"name1,null,name2,null,'SomeType<>'",
				"name1,null,name2,value2,'SomeType<name2:value2>'",
				"name1,value1,null,null,'SomeType<name1:value1>'",
				"name1,value1,null,value2,'SomeType<name1:value1, value2>'",
				"name1,value1,name2,null,'SomeType<name1:value1>'",
				"name1,value1,name2,value2,'SomeType<name1:value1, name2:value2>'"
			},nullValues="null")
			@ParameterizedTest(name = "the expected output is: {4}")
			public void multiple_fields_should_be_printed_as_expected(
				String name1, String value1, String name2, String value2, String expected
			)
			{

				final Configurator configurator
				= ToString.of( this )
					.withCustomClassName( "SomeType" )
					.printIfNotNull( name1, value1 )
					.printIfNotNull( name2, value2 );

				assertEquals( expected, configurator.likeTuple() );

			}

			@Test
			public void arrays_should_be_printed_as_expected()
			{

				assertAll(

					() -> {

						final Configurator configurator
						= ToString.of( this )
							.withCustomClassName( "SomeType" )
							.printIfNotNull( "array", new Integer[] {null,2,3} );

						assertEquals( "SomeType<array:[null, 2, 3]>",
								configurator.likeTuple() );

					},

					() -> {

						final Configurator configurator
						= ToString.of( this )
							.withCustomClassName( "SomeType" )
							.printIfNotNull( "array", new String[] {"a",null,"c"} );

						assertEquals( "SomeType<array:[a, null, c]>",
								configurator.likeTuple() );

					},

					() -> {

						final Configurator configurator
						=  ToString.of( this )
							.withCustomClassName( "SomeType" )
							.printIfNotNull( "array",
								new Object[] {
									new Integer[] { 1,2,null},
									new String[] {"a","b",null}
								}
							);

						assertEquals( "SomeType<array:[[1, 2, null], [a, b, null]]>",
								configurator.likeTuple() );

					}

				);
			}

			/**
			 * Performs unit tests on the method print
			 * with stream and mapping fuctions as arguments.
			 */
			@Nested
			@DisplayName("using streams")
			public class UsingStreams
			{
				
				@Test
				public void the_null_stream_should_be_printed_as_expected()
				{

					final Configurator configurator
					= ToString.of( this )
						.withCustomClassName( "SomeType" )
						.printIfNotNull( (Stream<?>) null )
						.printIfNotNull( null, Function.identity(), Function.identity() );

					assertEquals( "SomeType<>", configurator.likeTuple() );

				}

				@Test
				public void the_empty_stream_should_be_printed_as_expected()
				{

					final Configurator configurator
					= ToString.of( this )
						.withCustomClassName( "SomeType" )
						.printIfNotNull( Stream.empty() )
						.printIfNotNull( Stream.empty(), Function.identity(), Function.identity() );

					assertEquals( "SomeType<>", configurator.likeTuple() );

				}

				@CsvSource(value={
					"null,null,'SomeType<>'",
					"null,element2,'SomeType<element2>'",
					"element1,null,'SomeType<element1>'",
					"element1,element2,'SomeType<element1, element2>'",
				},nullValues="null")
				@ParameterizedTest(name = "the expected output is: {4}")
				public void multiple_elements_should_be_printed_as_expected(
					String element1, String element2, String expected
				)
				{

					final Stream<String> stream = Stream.of( element1, element2 );

					final Configurator configurator
					= ToString.of( this )
						.withCustomClassName( "SomeType" )
						.printIfNotNull( stream );

					assertEquals( expected, configurator.likeTuple() );

				}

				@CsvSource(value={
					"null,null,null,null,'SomeType<>'",
					"null,null,null,value2,'SomeType<value2>'",
					"null,null,name2,null,'SomeType<>'",
					"null,null,name2,value2,'SomeType<name2:value2>'",
					"null,value1,null,null,'SomeType<value1>'",
					"null,value1,null,value2,'SomeType<value1, value2>'",
					"null,value1,name2,null,'SomeType<value1>'",
					"null,value1,name2,value2,'SomeType<value1, name2:value2>'",
					"name1,null,null,null,'SomeType<>'",
					"name1,null,null,value2,'SomeType<value2>'",
					"name1,null,name2,null,'SomeType<>'",
					"name1,null,name2,value2,'SomeType<name2:value2>'",
					"name1,value1,null,null,'SomeType<name1:value1>'",
					"name1,value1,null,value2,'SomeType<name1:value1, value2>'",
					"name1,value1,name2,null,'SomeType<name1:value1>'",
					"name1,value1,name2,value2,'SomeType<name1:value1, name2:value2>'"
				},nullValues="null")
				@ParameterizedTest(name = "the expected output is: {4}")
				public void multiple_fields_should_be_printed_as_expected(
					String name1, String value1, String name2, String value2, String expected
				)
				{

					final Pair<String,String> pair1 = Pair.of( name1, value1 );
					final Pair<String,String> pair2 = Pair.of( name2, value2 );

					final Stream<Pair<String,String>> stream = Stream.of( pair1, pair2 );
					final Configurator configurator
					= ToString.of( this )
						.withCustomClassName( "SomeType" )
						.printIfNotNull( stream, Pair::getKey, Pair::getValue );

					assertEquals( expected, configurator.likeTuple() );

				}

			}

		}

	}


	/* ******************* */
	/*  USING 4 ARGUMENTS  */
	/* ******************* */


	/**
	 * Performs unit test on the method {@link ToString.Configurator#using(String,String,String,String)}.
	 */
	@Nested
	@DisplayName("on invoking method: using(String,String,String,String)")
	public class Using4ArgumentsTests
	{


		@Test
		public void if_target_is_null_method_should_return_null()
		{

			assertEquals( "null", ToString.of( null ).using("^","-","|","$") );

		}

		@CsvSource(value={
			"false,SomeType^$",
			"true,org.nerd4j.utils.lang.TestClasses.SomeType^$"
		})
		@ParameterizedTest(name="if fullClassName = {0} the expected output is: {1}")
		@DisplayName("the full class name should ne used according to the configuration")
		public void if_fullClassName_is_selected_the_proper_class_name_should_be_used(
			boolean isFullClassPath, String expected
		)
		{

			final Configurator configurator
					= ToString.of( new TestClasses.SomeType() );

			if( isFullClassPath )
				configurator.withFullClassName();

			assertEquals( expected, configurator.using("^","-","|","$") );

		}

		@CsvSource(value={
			"false,^$",
			"true,^$"
		})
		@ParameterizedTest(name="if noClassName is used the expected output is: {1}")
		@DisplayName("if noClassName is selected no class name should be displayed")
		public void if_noClassName_is_selected_no_class_name_should_be_displayed(
			boolean isFullClassPath, String expected
		)
		{

			final Configurator configurator
			= ToString.of( new TestClasses.SomeType() )
					  .withNoClassName();

			if( isFullClassPath )
				configurator.withFullClassName();

			assertEquals( expected, configurator.using("^","-","|","$") );

		}

		@CsvSource(value={
			"null,false,null^$",
			"null,true,null^$",
			"SomeCustomType,false,SomeCustomType^$",
			"SomeCustomType,true,SomeCustomType^$"
		},nullValues="null")
		@ParameterizedTest(name = "the expected output is: {2}")
		@DisplayName("the proper class name should ne used according to the configuration")
		public void the_proper_class_name_should_be_used(
				String customClassName, boolean isFullClassPath, String expected
		)
		{

			final Configurator configurator
			= ToString.of( new TestClasses.SomeType() )
					  .withCustomClassName( customClassName );

			if( isFullClassPath )
				configurator.withFullClassName();

			assertEquals( expected, configurator.using("^","-","|","$") );

		}

		@Nested
		@DisplayName("using method print")
		public class UsingPrint
		{

			@Test
			public void a_null_field_value_should_be_printed_as_null()
			{

				final String value = null;
				final Configurator configurator
				=  ToString.of( "SomeClass" )
						.withCustomClassName( "SomeClass" )
						.print( value );

				assertEquals( "SomeClass^null$", configurator.using("^","-","|","$") );

			}

			@Test
			public void a_valued_field_value_should_be_printed()
			{

				final String value = "value";
				final Configurator configurator
				=  ToString.of( "SomeClass" )
						.withCustomClassName( "SomeClass" )
						.print( value );

				assertEquals( "SomeClass^value$", configurator.using("^","-","|","$") );

			}

			@Test
			public void a_null_field_name_should_not_be_printed()
			{

				final String value = "value";
				final Configurator configurator
				=  ToString.of( "SomeClass" )
						.withCustomClassName( "SomeClass" )
						.print( null, value );

				assertEquals( "SomeClass^value$", configurator.using("^","-","|","$") );

			}

			@Test
			public void a_valued_field_name_should_be_printed()
			{

				final String value = "value";
				final Configurator configurator
				=  ToString.of( "SomeClass" )
						.withCustomClassName( "SomeClass" )
						.print( "key", value );

				assertEquals( "SomeClass^key-value$", configurator.using("^","-","|","$") );

			}


			@CsvSource(value={
				"null,null,null,null,'SomeType^null|null$'",
				"null,null,null,value2,'SomeType^null|value2$'",
				"null,null,name2,null,'SomeType^null|name2-null$'",
				"null,null,name2,value2,'SomeType^null|name2-value2$'",
				"null,value1,null,null,'SomeType^value1|null$'",
				"null,value1,null,value2,'SomeType^value1|value2$'",
				"null,value1,name2,null,'SomeType^value1|name2-null$'",
				"null,value1,name2,value2,'SomeType^value1|name2-value2$'",
				"name1,null,null,null,'SomeType^name1-null|null$'",
				"name1,null,null,value2,'SomeType^name1-null|value2$'",
				"name1,null,name2,null,'SomeType^name1-null|name2-null$'",
				"name1,null,name2,value2,'SomeType^name1-null|name2-value2$'",
				"name1,value1,null,null,'SomeType^name1-value1|null$'",
				"name1,value1,null,value2,'SomeType^name1-value1|value2$'",
				"name1,value1,name2,null,'SomeType^name1-value1|name2-null$'",
				"name1,value1,name2,value2,'SomeType^name1-value1|name2-value2$'"
			},nullValues="null")
			@ParameterizedTest(name = "the expected output is: {4}")
			public void multiple_fields_should_be_printed_as_expected(
				String name1, String value1, String name2, String value2, String expected
			)
			{

				final Configurator configurator
				=  ToString.of( this )
						.withCustomClassName( "SomeType" )
						.print( name1, value1 )
						.print( name2, value2 );

				assertEquals( expected, configurator.using("^","-","|","$") );

			}

			@Test
			public void arrays_should_be_printed_as_expected()
			{

				assertAll(

					() -> {

						final Configurator configurator
						= ToString.of( this )
							.withCustomClassName( "SomeType" )
							.print( "array", new int[] {1,2,3} );

						assertEquals( "SomeType^array-[1, 2, 3]$",
						configurator.using("^","-","|","$") );

					},

					() -> {

						final Configurator configurator
						= ToString.of( this )
							.withCustomClassName( "SomeType" )
							.print( "array", new String[] {"a","b","c"} );

						assertEquals( "SomeType^array-[a, b, c]$",
						configurator.using("^","-","|","$") );

					},

					() -> {

						final Configurator configurator
						=  ToString.of( this )
							.withCustomClassName( "SomeType" )
							.print( "array",
								new Object[] {
									new int[] { 1,2,3},
									new String[] {"a","b","c"}
								}
							);

						assertEquals( "SomeType^array-[[1, 2, 3], [a, b, c]]$",
						configurator.using("^","-","|","$") );

					}

				);
			}

			/**
			 * Performs unit tests on the method print
			 * with stream and mapping fuctions as arguments.
			 */
			@Nested
			@DisplayName("using streams")
			public class UsingStreams
			{
				
				@Test
				public void the_null_stream_should_be_printed_as_expected()
				{

					final Configurator configurator
					= ToString.of( this )
						.withCustomClassName( "SomeType" )
						.print( (Stream<?>) null )
						.print( null, Function.identity(), Function.identity() );

					assertEquals( "SomeType^$", configurator.using("^","-","|","$") );

				}

				@Test
				public void the_empty_stream_should_be_printed_as_expected()
				{

					final Configurator configurator
					= ToString.of( this )
						.withCustomClassName( "SomeType" )
						.print( Stream.empty() )
						.print( Stream.empty(), Function.identity(), Function.identity() );

					assertEquals( "SomeType^$", configurator.using("^","-","|","$") );

				}


				@CsvSource(value={
					"null,null,'SomeType^null|null$'",
					"null,element2,'SomeType^null|element2$'",
					"element1,null,'SomeType^element1|null$'",
					"element1,element2,'SomeType^element1|element2$'",
				},nullValues="null")
				@ParameterizedTest(name = "the expected output is: {4}")
				public void multiple_elements_should_be_printed_as_expected(
					String element1, String element2, String expected
				)
				{

					final Stream<String> stream = Stream.of( element1, element2 );

					final Configurator configurator
					= ToString.of( this )
						.withCustomClassName( "SomeType" )
						.print( stream );

					assertEquals( expected, configurator.using("^","-","|","$") );

				}

				@CsvSource(value={
					"null,null,null,null,'SomeType^null|null$'",
					"null,null,null,value2,'SomeType^null|value2$'",
					"null,null,name2,null,'SomeType^null|name2-null$'",
					"null,null,name2,value2,'SomeType^null|name2-value2$'",
					"null,value1,null,null,'SomeType^value1|null$'",
					"null,value1,null,value2,'SomeType^value1|value2$'",
					"null,value1,name2,null,'SomeType^value1|name2-null$'",
					"null,value1,name2,value2,'SomeType^value1|name2-value2$'",
					"name1,null,null,null,'SomeType^name1-null|null$'",
					"name1,null,null,value2,'SomeType^name1-null|value2$'",
					"name1,null,name2,null,'SomeType^name1-null|name2-null$'",
					"name1,null,name2,value2,'SomeType^name1-null|name2-value2$'",
					"name1,value1,null,null,'SomeType^name1-value1|null$'",
					"name1,value1,null,value2,'SomeType^name1-value1|value2$'",
					"name1,value1,name2,null,'SomeType^name1-value1|name2-null$'",
					"name1,value1,name2,value2,'SomeType^name1-value1|name2-value2$'"
				},nullValues="null")
				@ParameterizedTest(name = "the expected output is: {4}")
				public void multiple_fields_should_be_printed_as_expected(
					String name1, String value1, String name2, String value2, String expected
				)
				{

					final Pair<String,String> pair1 = Pair.of( name1, value1 );
					final Pair<String,String> pair2 = Pair.of( name2, value2 );

					final Stream<Pair<String,String>> stream = Stream.of( pair1, pair2 );
					final Configurator configurator
					= ToString.of( this )
						.withCustomClassName( "SomeType" )
						.print( stream, Pair::getKey, Pair::getValue );

					assertEquals( expected, configurator.using("^","-","|","$") );

				}

			}

		}

		@Nested
		@DisplayName("using method printIfNotNull")
		public class UsingPrintIfNotNull
		{
			
			@Test
			public void a_null_field_value_should_not_be_printed()
			{

				final String value = null;
				final Configurator configurator
				= ToString.of( "SomeClass" )
					.withCustomClassName( "SomeClass" )
					.printIfNotNull( value );

				assertEquals( "SomeClass^$", configurator.using("^","-","|","$") );

			}

			@Test
			public void a_valued_field_value_should_be_printed()
			{

				final String value = "value";
				final Configurator configurator
				= ToString.of( "SomeClass" )
					.withCustomClassName( "SomeClass" )
					.printIfNotNull( value );

				assertEquals( "SomeClass^value$", configurator.using("^","-","|","$") );

			}

			@Test
			public void a_null_field_name_should_not_be_printed()
			{

				final String value = "value";
				final Configurator configurator
				= ToString.of( "SomeClass" )
					.withCustomClassName( "SomeClass" )
					.printIfNotNull( null, value );

				assertEquals( "SomeClass^value$", configurator.using("^","-","|","$") );

			}

			@Test
			public void a_valued_field_name_should_be_printed()
			{

				final String value = "value";
				final Configurator configurator
				= ToString.of( "SomeClass" )
					.withCustomClassName( "SomeClass" )
					.printIfNotNull( "key", value );

				assertEquals( "SomeClass^key-value$", configurator.using("^","-","|","$") );

			}

			@CsvSource(value={
				"null,null,null,null,'SomeType^$'",
				"null,null,null,value2,'SomeType^value2$'",
				"null,null,name2,null,'SomeType^$'",
				"null,null,name2,value2,'SomeType^name2-value2$'",
				"null,value1,null,null,'SomeType^value1$'",
				"null,value1,null,value2,'SomeType^value1|value2$'",
				"null,value1,name2,null,'SomeType^value1$'",
				"null,value1,name2,value2,'SomeType^value1|name2-value2$'",
				"name1,null,null,null,'SomeType^$'",
				"name1,null,null,value2,'SomeType^value2$'",
				"name1,null,name2,null,'SomeType^$'",
				"name1,null,name2,value2,'SomeType^name2-value2$'",
				"name1,value1,null,null,'SomeType^name1-value1$'",
				"name1,value1,null,value2,'SomeType^name1-value1|value2$'",
				"name1,value1,name2,null,'SomeType^name1-value1$'",
				"name1,value1,name2,value2,'SomeType^name1-value1|name2-value2$'"
			},nullValues="null")
			@ParameterizedTest(name = "the expected output is: {4}")
			public void multiple_fields_should_be_printed_as_expected(
				String name1, String value1, String name2, String value2, String expected
			)
			{

				final Configurator configurator
				= ToString.of( this )
					.withCustomClassName( "SomeType" )
					.printIfNotNull( name1, value1 )
					.printIfNotNull( name2, value2 );

				assertEquals( expected, configurator.using("^","-","|","$") );

			}

			@Test
			public void arrays_should_be_printed_as_expected()
			{

				assertAll(

					() -> {

						final Configurator configurator
						= ToString.of( this )
							.withCustomClassName( "SomeType" )
							.printIfNotNull( "array", new Integer[] {null,2,3} );

						assertEquals( "SomeType^array-[null, 2, 3]$",
								configurator.using("^","-","|","$") );

					},

					() -> {

						final Configurator configurator
						= ToString.of( this )
							.withCustomClassName( "SomeType" )
							.printIfNotNull( "array", new String[] {"a",null,"c"} );

						assertEquals( "SomeType^array-[a, null, c]$",
								configurator.using("^","-","|","$") );

					},

					() -> {

						final Configurator configurator
						=  ToString.of( this )
							.withCustomClassName( "SomeType" )
							.printIfNotNull( "array",
								new Object[] {
									new Integer[] { 1,2,null},
									new String[] {"a","b",null}
								}
							);

						assertEquals( "SomeType^array-[[1, 2, null], [a, b, null]]$",
								configurator.using("^","-","|","$") );

					}

				);
			}

			/**
			 * Performs unit tests on the method print
			 * with stream and mapping fuctions as arguments.
			 */
			@Nested
			@DisplayName("using streams")
			public class UsingStreams
			{
				
				@Test
				public void the_null_stream_should_be_printed_as_expected()
				{

					final Configurator configurator
					= ToString.of( this )
						.withCustomClassName( "SomeType" )
						.printIfNotNull( (Stream<?>) null )
						.printIfNotNull( null, Function.identity(), Function.identity() );

					assertEquals( "SomeType^$", configurator.using("^","-","|","$") );

				}

				@Test
				public void the_empty_stream_should_be_printed_as_expected()
				{

					final Configurator configurator
					= ToString.of( this )
						.withCustomClassName( "SomeType" )
						.printIfNotNull( Stream.empty() )
						.printIfNotNull( Stream.empty(), Function.identity(), Function.identity() );

					assertEquals( "SomeType^$", configurator.using("^","-","|","$") );

				}

				@CsvSource(value={
					"null,null,'SomeType^$'",
					"null,element2,'SomeType^element2$'",
					"element1,null,'SomeType^element1$'",
					"element1,element2,'SomeType^element1|element2$'",
				},nullValues="null")
				@ParameterizedTest(name = "the expected output is: {4}")
				public void multiple_elements_should_be_printed_as_expected(
					String element1, String element2, String expected
				)
				{

					final Stream<String> stream = Stream.of( element1, element2 );

					final Configurator configurator
					= ToString.of( this )
						.withCustomClassName( "SomeType" )
						.printIfNotNull( stream );

					assertEquals( expected, configurator.using("^","-","|","$") );

				}

				@CsvSource(value={
					"null,null,null,null,'SomeType^$'",
					"null,null,null,value2,'SomeType^value2$'",
					"null,null,name2,null,'SomeType^$'",
					"null,null,name2,value2,'SomeType^name2-value2$'",
					"null,value1,null,null,'SomeType^value1$'",
					"null,value1,null,value2,'SomeType^value1|value2$'",
					"null,value1,name2,null,'SomeType^value1$'",
					"null,value1,name2,value2,'SomeType^value1|name2-value2$'",
					"name1,null,null,null,'SomeType^$'",
					"name1,null,null,value2,'SomeType^value2$'",
					"name1,null,name2,null,'SomeType^$'",
					"name1,null,name2,value2,'SomeType^name2-value2$'",
					"name1,value1,null,null,'SomeType^name1-value1$'",
					"name1,value1,null,value2,'SomeType^name1-value1|value2$'",
					"name1,value1,name2,null,'SomeType^name1-value1$'",
					"name1,value1,name2,value2,'SomeType^name1-value1|name2-value2$'"
				},nullValues="null")
				@ParameterizedTest(name = "the expected output is: {4}")
				public void multiple_fields_should_be_printed_as_expected(
					String name1, String value1, String name2, String value2, String expected
				)
				{

					final Pair<String,String> pair1 = Pair.of( name1, value1 );
					final Pair<String,String> pair2 = Pair.of( name2, value2 );

					final Stream<Pair<String,String>> stream = Stream.of( pair1, pair2 );
					final Configurator configurator
					= ToString.of( this )
						.withCustomClassName( "SomeType" )
						.printIfNotNull( stream, Pair::getKey, Pair::getValue );

					assertEquals( expected, configurator.using("^","-","|","$") );

				}

			}

		}

	}


	/* ******************* */
	/*  USING 3 ARGUMENTS  */
	/* ******************* */


	/**
	 * Performs unit test on the method {@link ToString.Configurator#using(String,String,String)}.
	 */
	@Nested
	@DisplayName("on invoking method: using(String,String,String)")
	public class Using3ArgumentsTests
	{


		@Test
		public void if_target_is_null_method_should_return_null()
		{

			assertEquals( "null", ToString.of( null ).using("^","|","$") );

		}


		@CsvSource(value={
			"false,SomeType^$",
			"true,org.nerd4j.utils.lang.TestClasses.SomeType^$"
		})
		@ParameterizedTest(name="if fullClassName = {0} the expected output is: {1}")
		@DisplayName("the full class name should ne used according to the configuration")
		public void if_fullClassName_is_selected_the_proper_class_name_should_be_used(
			boolean isFullClassPath, String expected
		)
		{

			final Configurator configurator
			= ToString.of( new TestClasses.SomeType() );

			if( isFullClassPath )
				configurator.withFullClassName();

			assertEquals( expected, configurator.using("^","|","$") );

		}

		@CsvSource(value={
			"false,^$",
			"true,^$"
		})
		@ParameterizedTest(name="if noClassName is used the expected output is: {1}")
		@DisplayName("if noClassName is selected no class name should be displayed")
		public void if_noClassName_is_selected_no_class_name_should_be_displayed(
			boolean isFullClassPath, String expected
		)
		{

			final Configurator configurator
			= ToString.of( new TestClasses.SomeType() )
					  .withNoClassName();

			if( isFullClassPath )
				configurator.withFullClassName();

			assertEquals( expected, configurator.using("^","|","$") );

		}

		@CsvSource(value={
			"null,false,null^$",
			"null,true,null^$",
			"SomeCustomType,false,SomeCustomType^$",
			"SomeCustomType,true,SomeCustomType^$"
		},nullValues="null")
		@ParameterizedTest(name = "the expected output is: {2}")
		@DisplayName("the proper class name should ne used according to the configuration")
		public void the_proper_class_name_should_be_used(
			String customClassName, boolean isFullClassPath, String expected
		)
		{

			final Configurator configurator
			= ToString.of( new TestClasses.SomeType() )
					  .withCustomClassName( customClassName );

			if( isFullClassPath )
				configurator.withFullClassName();

			assertEquals( expected, configurator.using("^","|","$") );

		}

		@Nested
		@DisplayName("using method print")
		public class UsingPrint
		{

			@Test
			public void a_null_field_value_should_be_printed_as_null()
			{

				final String value = null;
				final Configurator configurator
				=  ToString.of( "SomeClass" )
						.withCustomClassName( "SomeClass" )
						.print( value );

				assertEquals( "SomeClass^null$", configurator.using("^","|","$") );

			}

			@Test
			public void a_valued_field_value_should_be_printed()
			{

				final String value = "value";
				final Configurator configurator
				=  ToString.of( "SomeClass" )
						.withCustomClassName( "SomeClass" )
						.print( value );

				assertEquals( "SomeClass^value$", configurator.using("^","|","$") );

			}

			@Test
			public void a_null_field_name_should_not_be_printed()
			{

				final String value = "value";
				final Configurator configurator
				=  ToString.of( "SomeClass" )
						.withCustomClassName( "SomeClass" )
						.print( null, value );

				assertEquals( "SomeClass^value$", configurator.using("^","|","$") );

			}

			@Test
			public void a_valued_field_name_should_be_printed()
			{

				final String value = "value";
				final Configurator configurator
				=  ToString.of( "SomeClass" )
						.withCustomClassName( "SomeClass" )
						.print( "key", value );

				assertEquals( "SomeClass^keyvalue$", configurator.using("^","|","$") );

			}


			@CsvSource(value={
				"null,null,null,null,'SomeType^null|null$'",
				"null,null,null,value2,'SomeType^null|value2$'",
				"null,null,name2,null,'SomeType^null|name2null$'",
				"null,null,name2,value2,'SomeType^null|name2value2$'",
				"null,value1,null,null,'SomeType^value1|null$'",
				"null,value1,null,value2,'SomeType^value1|value2$'",
				"null,value1,name2,null,'SomeType^value1|name2null$'",
				"null,value1,name2,value2,'SomeType^value1|name2value2$'",
				"name1,null,null,null,'SomeType^name1null|null$'",
				"name1,null,null,value2,'SomeType^name1null|value2$'",
				"name1,null,name2,null,'SomeType^name1null|name2null$'",
				"name1,null,name2,value2,'SomeType^name1null|name2value2$'",
				"name1,value1,null,null,'SomeType^name1value1|null$'",
				"name1,value1,null,value2,'SomeType^name1value1|value2$'",
				"name1,value1,name2,null,'SomeType^name1value1|name2null$'",
				"name1,value1,name2,value2,'SomeType^name1value1|name2value2$'"
			},nullValues="null")
			@ParameterizedTest(name = "the expected output is: {4}")
			public void multiple_fields_should_be_printed_as_expected(
					String name1, String value1, String name2, String value2, String expected
			)
			{

				final Configurator configurator
				=  ToString.of( this )
						.withCustomClassName( "SomeType" )
							.print( name1, value1 )
							.print( name2, value2 );

				assertEquals( expected, configurator.using("^","|","$") );

			}


			@Test
			public void arrays_should_be_printed_as_expected()
			{

				assertAll(

					() -> {

						final Configurator configurator
						= ToString.of( this )
							.withCustomClassName( "SomeType" )
							.print( "array", new int[] {1,2,3} );

						assertEquals( "SomeType^array[1, 2, 3]$",
						configurator.using("^","|","$") );

					},

					() -> {

						final Configurator configurator
						= ToString.of( this )
							.withCustomClassName( "SomeType" )
							.print( "array", new String[] {"a","b","c"} );

						assertEquals( "SomeType^array[a, b, c]$",
						configurator.using("^","|","$") );

					},

					() -> {

						final Configurator configurator
						=  ToString.of( this )
							.withCustomClassName( "SomeType" )
							.print( "array",
								new Object[] {
									new int[] { 1,2,3},
									new String[] {"a","b","c"}
								}
							);

						assertEquals( "SomeType^array[[1, 2, 3], [a, b, c]]$",
						configurator.using("^","|","$") );

					}

				);
			}

			/**
			 * Performs unit tests on the method print
			 * with stream and mapping fuctions as arguments.
			 */
			@Nested
			@DisplayName("using streams")
			public class UsingStreams
			{
				
				@Test
				public void the_null_stream_should_be_printed_as_expected()
				{

					final Configurator configurator
					= ToString.of( this )
						.withCustomClassName( "SomeType" )
						.print( (Stream<?>) null )
						.print( null, Function.identity(), Function.identity() );

					assertEquals( "SomeType^$", configurator.using("^","|","$") );

				}

				@Test
				public void the_empty_stream_should_be_printed_as_expected()
				{

					final Configurator configurator
					= ToString.of( this )
						.withCustomClassName( "SomeType" )
						.print( Stream.empty() )
						.print( Stream.empty(), Function.identity(), Function.identity() );

					assertEquals( "SomeType^$", configurator.using("^","|","$") );

				}


				@CsvSource(value={
					"null,null,'SomeType^null|null$'",
					"null,element2,'SomeType^null|element2$'",
					"element1,null,'SomeType^element1|null$'",
					"element1,element2,'SomeType^element1|element2$'",
				},nullValues="null")
				@ParameterizedTest(name = "the expected output is: {4}")
				public void multiple_elements_should_be_printed_as_expected(
					String element1, String element2, String expected
				)
				{

					final Stream<String> stream = Stream.of( element1, element2 );

					final Configurator configurator
					= ToString.of( this )
						.withCustomClassName( "SomeType" )
						.print( stream );

					assertEquals( expected, configurator.using("^","|","$") );

				}

				@CsvSource(value={
					"null,null,null,null,'SomeType^null|null$'",
					"null,null,null,value2,'SomeType^null|value2$'",
					"null,null,name2,null,'SomeType^null|name2null$'",
					"null,null,name2,value2,'SomeType^null|name2value2$'",
					"null,value1,null,null,'SomeType^value1|null$'",
					"null,value1,null,value2,'SomeType^value1|value2$'",
					"null,value1,name2,null,'SomeType^value1|name2null$'",
					"null,value1,name2,value2,'SomeType^value1|name2value2$'",
					"name1,null,null,null,'SomeType^name1null|null$'",
					"name1,null,null,value2,'SomeType^name1null|value2$'",
					"name1,null,name2,null,'SomeType^name1null|name2null$'",
					"name1,null,name2,value2,'SomeType^name1null|name2value2$'",
					"name1,value1,null,null,'SomeType^name1value1|null$'",
					"name1,value1,null,value2,'SomeType^name1value1|value2$'",
					"name1,value1,name2,null,'SomeType^name1value1|name2null$'",
					"name1,value1,name2,value2,'SomeType^name1value1|name2value2$'"
				},nullValues="null")
				@ParameterizedTest(name = "the expected output is: {4}")
				public void multiple_fields_should_be_printed_as_expected(
					String name1, String value1, String name2, String value2, String expected
				)
				{

					final Pair<String,String> pair1 = Pair.of( name1, value1 );
					final Pair<String,String> pair2 = Pair.of( name2, value2 );

					final Stream<Pair<String,String>> stream = Stream.of( pair1, pair2 );
					final Configurator configurator
					= ToString.of( this )
						.withCustomClassName( "SomeType" )
						.print( stream, Pair::getKey, Pair::getValue );

					assertEquals( expected, configurator.using("^","|","$") );

				}

			}

		}

	}

	/* ******************* */
	/*  USING 3 ARGUMENTS  */
	/* ******************* */


	/**
	 * Performs unit test on the method {@link ToString.Configurator#using(String,String,String)}.
	 */
	@Nested
	@DisplayName("on invoking method: like(Printer)")
	public class UsingCustomPrinter
	{

		private class CustomPrinter implements Printer
		{

			public boolean invoked = false;

			@Override
			public String apply( Configuration configuration )
			{
				invoked = true;
				return "done";
			}
			
		}

		@Test
		public void method_like_Printer_should_invoke_the_provided_printer()
		{

			final CustomPrinter customPrinter = new CustomPrinter();
			final String value = ToString
				.of( new TestClasses.SomeType() )
				.like( customPrinter );

			assertTrue( customPrinter.invoked );
			assertEquals( "done", value );

		}

	}

	@Nested
	@DisplayName("using method printIfNotNull")
	public class UsingPrintIfNotNull
	{
		
		@Test
		public void a_null_field_value_should_not_be_printed()
		{

			final String value = null;
			final Configurator configurator
			= ToString.of( "SomeClass" )
				.withCustomClassName( "SomeClass" )
				.printIfNotNull( value );

			assertEquals( "SomeClass^$", configurator.using("^","|","$") );

		}

		@Test
		public void a_valued_field_value_should_be_printed()
		{

			final String value = "value";
			final Configurator configurator
			= ToString.of( "SomeClass" )
				.withCustomClassName( "SomeClass" )
				.printIfNotNull( value );

			assertEquals( "SomeClass^value$", configurator.using("^","|","$") );

		}

		@Test
		public void a_null_field_name_should_not_be_printed()
		{

			final String value = "value";
			final Configurator configurator
			= ToString.of( "SomeClass" )
				.withCustomClassName( "SomeClass" )
				.printIfNotNull( null, value );

			assertEquals( "SomeClass^value$", configurator.using("^","|","$") );

		}

		@Test
		public void a_valued_field_name_should_be_printed()
		{

			final String value = "value";
			final Configurator configurator
			= ToString.of( "SomeClass" )
				.withCustomClassName( "SomeClass" )
				.printIfNotNull( "key", value );

			assertEquals( "SomeClass^keyvalue$", configurator.using("^","|","$") );

		}

		@CsvSource(value={
			"null,null,null,null,'SomeType^$'",
			"null,null,null,value2,'SomeType^value2$'",
			"null,null,name2,null,'SomeType^$'",
			"null,null,name2,value2,'SomeType^name2value2$'",
			"null,value1,null,null,'SomeType^value1$'",
			"null,value1,null,value2,'SomeType^value1|value2$'",
			"null,value1,name2,null,'SomeType^value1$'",
			"null,value1,name2,value2,'SomeType^value1|name2value2$'",
			"name1,null,null,null,'SomeType^$'",
			"name1,null,null,value2,'SomeType^value2$'",
			"name1,null,name2,null,'SomeType^$'",
			"name1,null,name2,value2,'SomeType^name2value2$'",
			"name1,value1,null,null,'SomeType^name1value1$'",
			"name1,value1,null,value2,'SomeType^name1value1|value2$'",
			"name1,value1,name2,null,'SomeType^name1value1$'",
			"name1,value1,name2,value2,'SomeType^name1value1|name2value2$'"
		},nullValues="null")
		@ParameterizedTest(name = "the expected output is: {4}")
		public void multiple_fields_should_be_printed_as_expected(
			String name1, String value1, String name2, String value2, String expected
		)
		{

			final Configurator configurator
			= ToString.of( this )
				.withCustomClassName( "SomeType" )
				.printIfNotNull( name1, value1 )
				.printIfNotNull( name2, value2 );

			assertEquals( expected, configurator.using("^","|","$") );

		}

		@Test
		public void arrays_should_be_printed_as_expected()
		{

			assertAll(

				() -> {

					final Configurator configurator
					= ToString.of( this )
						.withCustomClassName( "SomeType" )
						.printIfNotNull( "array", new Integer[] {null,2,3} );

					assertEquals( "SomeType^array[null, 2, 3]$",
							configurator.using("^","|","$") );

				},

				() -> {

					final Configurator configurator
					= ToString.of( this )
						.withCustomClassName( "SomeType" )
						.printIfNotNull( "array", new String[] {"a",null,"c"} );

					assertEquals( "SomeType^array[a, null, c]$",
							configurator.using("^","|","$") );

				},

				() -> {

					final Configurator configurator
					=  ToString.of( this )
						.withCustomClassName( "SomeType" )
						.printIfNotNull( "array",
							new Object[] {
								new Integer[] { 1,2,null},
								new String[] {"a","b",null}
							}
						);

					assertEquals( "SomeType^array[[1, 2, null], [a, b, null]]$",
							configurator.using("^","|","$") );

				}

			);
		}

		/**
		 * Performs unit tests on the method print
		 * with stream and mapping fuctions as arguments.
		 */
		@Nested
		@DisplayName("using streams")
		public class UsingStreams
		{
			
			@Test
			public void the_null_stream_should_be_printed_as_expected()
			{

				final Configurator configurator
				= ToString.of( this )
					.withCustomClassName( "SomeType" )
					.printIfNotNull( (Stream<?>) null )
					.printIfNotNull( null, Function.identity(), Function.identity() );

				assertEquals( "SomeType^$", configurator.using("^","|","$") );

			}

			@Test
			public void the_empty_stream_should_be_printed_as_expected()
			{

				final Configurator configurator
				= ToString.of( this )
					.withCustomClassName( "SomeType" )
					.printIfNotNull( Stream.empty() )
					.printIfNotNull( Stream.empty(), Function.identity(), Function.identity() );

				assertEquals( "SomeType^$", configurator.using("^","|","$") );

			}

			@CsvSource(value={
				"null,null,'SomeType^$'",
				"null,element2,'SomeType^element2$'",
				"element1,null,'SomeType^element1$'",
				"element1,element2,'SomeType^element1|element2$'",
			},nullValues="null")
			@ParameterizedTest(name = "the expected output is: {4}")
			public void multiple_elements_should_be_printed_as_expected(
				String element1, String element2, String expected
			)
			{

				final Stream<String> stream = Stream.of( element1, element2 );

				final Configurator configurator
				= ToString.of( this )
					.withCustomClassName( "SomeType" )
					.printIfNotNull( stream );

				assertEquals( expected, configurator.using("^","|","$") );

			}

			@CsvSource(value={
				"null,null,null,null,'SomeType^$'",
				"null,null,null,value2,'SomeType^value2$'",
				"null,null,name2,null,'SomeType^$'",
				"null,null,name2,value2,'SomeType^name2value2$'",
				"null,value1,null,null,'SomeType^value1$'",
				"null,value1,null,value2,'SomeType^value1|value2$'",
				"null,value1,name2,null,'SomeType^value1$'",
				"null,value1,name2,value2,'SomeType^value1|name2value2$'",
				"name1,null,null,null,'SomeType^$'",
				"name1,null,null,value2,'SomeType^value2$'",
				"name1,null,name2,null,'SomeType^$'",
				"name1,null,name2,value2,'SomeType^name2value2$'",
				"name1,value1,null,null,'SomeType^name1value1$'",
				"name1,value1,null,value2,'SomeType^name1value1|value2$'",
				"name1,value1,name2,null,'SomeType^name1value1$'",
				"name1,value1,name2,value2,'SomeType^name1value1|name2value2$'"
			},nullValues="null")
			@ParameterizedTest(name = "the expected output is: {4}")
			public void multiple_fields_should_be_printed_as_expected(
				String name1, String value1, String name2, String value2, String expected
			)
			{

				final Pair<String,String> pair1 = Pair.of( name1, value1 );
				final Pair<String,String> pair2 = Pair.of( name2, value2 );

				final Stream<Pair<String,String>> stream = Stream.of( pair1, pair2 );
				final Configurator configurator
				= ToString.of( this )
					.withCustomClassName( "SomeType" )
					.printIfNotNull( stream, Pair::getKey, Pair::getValue );

				assertEquals( expected, configurator.using("^","|","$") );

			}

		}

	}
	
}

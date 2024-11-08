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
import org.junit.jupiter.api.function.Executable;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for the utility class {@link Require}.
 *
 * @author Massimo Coluzzi
 */
@DisplayName("Testing utility class: Require")
@DisplayNameGeneration(ReplaceUnderscores.class)
public class RequireTests
{

	/**
	 * Performs unit tests on method {@link Require#toHold(boolean)}.
	 */
	@Nested
	@DisplayName("on invoking method: toHold(Object)")
	public class ToHoldTests
	{

		@Test
		public void if_the_assertion_is_true_nothing_happens()
		{

			assertAll(
				() -> Require.toHold( true ),
				() -> Require.toHold( true, "error message" ),
				() -> Require.toHold( true, () -> "error message" )
			);

		}

		@Test
		public void if_the_assertion_is_false_should_throw_a_RequirementFailure()
		{

			assertAll(
				() -> assertRequirementFailure(
						() ->Require.toHold(false),
						"the required assertion must hold"
					),
				() -> assertRequirementFailure(
						() -> Require.toHold( false, "error message"),
						"error message"
					),
				() -> assertRequirementFailure(
						() -> Require.toHold( false, () -> "error message"),
							"error message"
					)
			);

		}

	}


	/**
	 * Performs unit tests on method {@link Require#trueFor(Object,boolean)}.
	 */
	@Nested
	@DisplayName("on invoking method: trueFor(Object,boolean)")
	public class TrueForTests
	{

		@Test
		public void if_the_assertion_is_true_value_should_be_returned()
		{

			final Object value = new Object();
			assertAll(
				() -> assertSame( value,
						Require.trueFor(value, true) ),
				() -> assertSame( value,
						Require.trueFor(value, true, "error message") ),
				() -> assertSame( value,
						Require.trueFor(value, true, () -> "error message") )
			);

		}

		@Test
		public void if_the_assertion_is_false_should_throw_a_RequirementFailure()
		{

			final Object value = new Object();
			assertAll(
				() -> assertRequirementFailure(
					() -> Require.trueFor(value, false),
						"the required assertion must hold for " + value
					),
				() -> assertRequirementFailure(
					() -> Require.trueFor( value, false, "error message"),
						"error message"
					),
				() -> assertRequirementFailure(
					() -> Require.trueFor( value, false, () -> "error message"),
						"error message"
					)
			);

		}

	}


	/**
	 * Performs unit tests on method {@link Require#nonNull(Object)}.
	 */
	@Nested
	@DisplayName("on invoking method: nonNull(Object)")
	public class NullTests
	{

		@Test
		public void if_the_argument_is_null_should_throw_a_RequirementFailure()
		{

			assertAll(
				() -> assertRequirementFailure(
					() -> Require.nonNull(null ),
						"this argument must not be null"
					),
				() -> assertRequirementFailure(
					() -> Require.nonNull( null, "error message" ),
						"error message"
					),
				() -> assertRequirementFailure(
					() -> Require.nonNull( null, () -> "error message" ),
						"error message"
					)
			);

		}

		@Test
		public void if_the_argument_is_NOT_null_should_be_returned()
		{

			final Object value = new Object();
			assertAll(
				() -> assertSame( value,
					Require.nonNull( value )
				),
				() -> assertSame( value,
					Require.nonNull( value, "error message" )
				),
				() -> assertSame( value,
					Require.nonNull( value, () -> "error message" )
				)
			);

		}

	}

	/**
	 * Performs unit tests on method {@link Require#nonEmpty(Emptily)}.
	 */
	@Nested
	@DisplayName("on invoking method: nonEmpty(Emptily)")
	public class EmptyEmptilyTests
	{

		@Test
		public void if_Emptily_is_null_or_empty_should_throw_a_RequirementFailure()
		{

			final TestClasses.EmptilyType emptyValue = new TestClasses.EmptilyType( true );
			assertAll(
				() -> assertRequirementFailure(
					() -> Require.nonEmpty( (Emptily) null ),
						"this argument must not be empty but was null"
					),
				() -> assertRequirementFailure(
					() -> Require.nonNull( (Emptily) null, "error message" ),
						"error message"
					),
				() -> assertRequirementFailure(
					() -> Require.nonNull( (Emptily) null, () -> "error message" ),
						"error message"
					),
				() -> assertRequirementFailure(
					() -> Require.nonEmpty( emptyValue ),
						"this argument must not be empty but was " + emptyValue
					),
				() -> assertRequirementFailure(
					() -> Require.nonNull( emptyValue, "error message" ),
					"error message"
				),
				() -> assertRequirementFailure(
					() -> Require.nonNull( emptyValue, () -> "error message" ),
					"error message"
				)
			);

		}

		@Test
		public void if_Emptily_is_NOT_null_or_empty_should_be_returned()
		{

			final TestClasses.EmptilyType value = new TestClasses.EmptilyType( false );
			assertAll(
				() -> assertSame( value,
					Require.nonNull( value )
				),
				() -> assertSame( value,
					Require.nonNull( value, "error message" )
				),
				() -> assertSame( value,
					Require.nonNull( value, () -> "error message" )
				)
			);

		}

	}

	/**
	 * Performs unit tests on method {@link Require#nonEmpty(String)}.
	 */
	@Nested
	@DisplayName("on invoking method: nonEmpty(String)")
	public class EmptyStringTests
	{

		@Test
		public void if_String_is_null_or_empty_should_throw_a_RequirementFailure()
		{


			final String emptyString = "";
			assertAll(
				() -> assertRequirementFailure(
					() -> Require.nonEmpty( (String) null ),
					"this argument must not be empty but was null"
				),
				() -> assertRequirementFailure(
					() -> Require.nonNull( (String) null, "error message" ),
					"error message"
				),
				() -> assertRequirementFailure(
					() -> Require.nonNull( (String) null, () -> "error message" ),
					"error message"
				),
				() -> assertRequirementFailure(
					() -> Require.nonEmpty( emptyString ),
					"this argument must not be empty but was " + emptyString
				),
				() -> assertRequirementFailure(
					() -> Require.nonNull( emptyString, "error message" ),
					"error message"
				),
				() -> assertRequirementFailure(
					() -> Require.nonNull( emptyString, () -> "error message" ),
					"error message"
				)
			);

		}

		@Test
		public void if_String_is_NOT_null_or_empty_should_be_returned()
		{

			final String value = " ";
			assertAll(
				() -> assertSame( value,
					Require.nonNull( value )
				),
				() -> assertSame( value,
					Require.nonNull( value, "error message" )
				),
				() -> assertSame( value,
					Require.nonNull( value, () -> "error message" )
				)
			);

		}

	}

	/**
	 * Performs unit tests on method {@link Require#nonEmpty(Collection)}.
	 */
	@Nested
	@DisplayName("on invoking method: nonEmpty(Collection)")
	public class EmptyCollectionTests
	{

		@Test
		public void if_Collection_is_null_or_empty_should_throw_a_RequirementFailure()
		{

			assertAll(
				() -> assertRequirementFailure(
					() -> Require.nonEmpty( (Collection<?>) null ),
						"this argument must not be empty but was null"
					),
				() -> assertRequirementFailure(
					() -> Require.nonNull( (Collection<?>) null, "error message" ),
						"error message"
					),
				() -> assertRequirementFailure(
					() -> Require.nonNull( (Collection<?>) null, () -> "error message" ),
						"error message"
					),
				() -> assertRequirementFailure(
					() -> Require.nonEmpty( Collections.emptySet() ),
						"this argument must not be empty but was []"
					),
				() -> assertRequirementFailure(
					() -> Require.nonNull( Collections.emptySet(), "error message" ),
						"error message"
					),
				() -> assertRequirementFailure(
					() -> Require.nonNull( Collections.emptySet(), () -> "error message" ),
						"error message"
					),
				() -> assertRequirementFailure(
					() -> Require.nonEmpty( Collections.emptyList() ),
						"this argument must not be empty but was []"
					),
				() -> assertRequirementFailure(
					() -> Require.nonNull( Collections.emptyList(), "error message" ),
						"error message"
					),
				() -> assertRequirementFailure(
					() -> Require.nonNull( Collections.emptyList(), () -> "error message" ),
						"error message"
					)
			);

		}

		@Test
		public void if_Collection_is_NOT_null_or_empty_should_be_returned()
		{

			final Set<?> set = Collections.singleton( null );
			final List<?> list = Collections.singletonList( null );
			assertAll(
				() -> assertSame( set,
					Require.nonNull( set )
				),
				() -> assertSame( set,
					Require.nonNull( set, "error message" )
				),
				() -> assertSame( set,
					Require.nonNull( set, () -> "error message" )
				),
				() -> assertSame( list,
					Require.nonNull( list )
				),
				() -> assertSame( list,
					Require.nonNull( list, "error message" )
				),
				() -> assertSame( list,
					Require.nonNull( list, () -> "error message" )
				)
			);

		}

	}

	/**
	 * Performs unit tests on method {@link Require#nonEmpty(Map)}.
	 */
	@Nested
	@DisplayName("on invoking method: nonEmpty(Map)")
	public class EmptyMapTests
	{

		@Test
		public void if_Map_is_null_or_empty_should_throw_a_RequirementFailure()
		{

			assertAll(
				() -> assertRequirementFailure(
					() -> Require.nonEmpty( (Map<?,?>) null ),
						"this argument must not be empty but was null"
					),
				() -> assertRequirementFailure(
					() -> Require.nonNull( (Map<?,?>) null, "error message" ),
						"error message"
					),
				() -> assertRequirementFailure(
					() -> Require.nonNull( (Map<?,?>) null, () -> "error message" ),
						"error message"
					),
				() -> assertRequirementFailure(
					() -> Require.nonEmpty( Collections.emptyMap() ),
						"this argument must not be empty but was {}"
					),
				() -> assertRequirementFailure(
					() -> Require.nonNull( Collections.emptyMap(), "error message" ),
						"error message"
					),
				() -> assertRequirementFailure(
					() -> Require.nonNull( Collections.emptyMap(), () -> "error message" ),
						"error message"
					)
			);

		}

		@Test
		public void if_Map_is_NOT_null_or_empty_should_be_returned()
		{

			final Map<?,?> map = Collections.singletonMap( null, null );
			assertAll(
				() -> assertSame( map,
					Require.nonNull( map )
				),
				() -> assertSame( map,
					Require.nonNull( map, "error message" )
				),
				() -> assertSame( map,
					Require.nonNull( map, () -> "error message" )
				)
			);

		}

	}

	/**
	 * Performs unit tests on method {@link Require#nonEmpty(Object[])}.
	 */
	@Nested
	@DisplayName("on invoking method: nonEmpty(Object[])")
	public class EmptyObjectArrayTests
	{

		@Test
		public void if_Object_array_is_null_or_empty_should_throw_a_RequirementFailure()
		{

			final Object[] emptyArray = new Object[0];
			assertAll(
				() -> assertRequirementFailure(
					() -> Require.nonEmpty( (Object[]) null ),
						"this argument must not be empty but was null"
					),
				() -> assertRequirementFailure(
					() -> Require.nonNull( (Object[]) null, "error message" ),
						"error message"
					),
				() -> assertRequirementFailure(
					() -> Require.nonNull( (Object[]) null, () -> "error message" ),
						"error message"
					),
				() -> assertRequirementFailure(
					() -> Require.nonEmpty( emptyArray ),
						"this argument must not be empty but was " + emptyArray
					),
				() -> assertRequirementFailure(
					() -> Require.nonNull( emptyArray, "error message" ),
						"error message"
					),
				() -> assertRequirementFailure(
					() -> Require.nonNull( emptyArray, () -> "error message" ),
						"error message"
					)
			);

		}

		@Test
		public void if_Object_array_is_NOT_null_or_empty_should_be_returned()
		{

			final Object[] array = new Object[] { null };
			assertAll(
				() -> assertSame( array,
					Require.nonNull( array )
				),
				() -> assertSame( array,
					Require.nonNull( array, "error message" )
				),
				() -> assertSame( array,
					Require.nonNull( array, () -> "error message" )
				)
			);

		}

	}

	/**
	 * Performs unit tests on method {@link Require#nonEmpty(boolean[])}.
	 */
	@Nested
	@DisplayName("on invoking method: nonEmpty(boolean[])")
	public class EmptyBooleanArrayTests
	{

		@Test
		public void if_boolean_array_is_null_or_empty_should_throw_a_RequirementFailure()
		{

			final boolean[] emptyArray = new boolean[0];
			assertAll(
				() -> assertRequirementFailure(
					() -> Require.nonEmpty( (boolean[]) null ),
						"this argument must not be empty but was null"
					),
				() -> assertRequirementFailure(
					() -> Require.nonNull( (boolean[]) null, "error message" ),
						"error message"
					),
				() -> assertRequirementFailure(
					() -> Require.nonNull( (boolean[]) null, () -> "error message" ),
						"error message"
					),
				() -> assertRequirementFailure(
					() -> Require.nonEmpty( emptyArray ),
						"this argument must not be empty but was " + emptyArray
					),
				() -> assertRequirementFailure(
					() -> Require.nonNull( emptyArray, "error message" ),
						"error message"
					),
				() -> assertRequirementFailure(
					() -> Require.nonNull( emptyArray, () -> "error message" ),
						"error message"
					)
			);

		}

		@Test
		public void if_boolean_array_is_NOT_null_or_empty_should_be_returned()
		{

			final boolean[] array = new boolean[] { false };
			assertAll(
				() -> assertSame( array,
					Require.nonNull( array )
				),
				() -> assertSame( array,
					Require.nonNull( array, "error message" )
				),
				() -> assertSame( array,
					Require.nonNull( array, () -> "error message" )
				)
			);

		}

	}

	/**
	 * Performs unit tests on method {@link Require#nonEmpty(char[])}.
	 */
	@Nested
	@DisplayName("on invoking method: nonEmpty(char[])")
	public class EmptyCharArrayTests
	{

		@Test
		public void if_char_array_is_null_or_empty_should_throw_a_RequirementFailure()
		{

			final char[] emptyArray = new char[0];
			assertAll(
				() -> assertRequirementFailure(
					() -> Require.nonEmpty( (char[]) null ),
						"this argument must not be empty but was null"
					),
				() -> assertRequirementFailure(
					() -> Require.nonNull( (char[]) null, "error message" ),
						"error message"
					),
				() -> assertRequirementFailure(
					() -> Require.nonNull( (char[]) null, () -> "error message" ),
						"error message"
					),
				() -> assertRequirementFailure(
					() -> Require.nonEmpty( emptyArray ),						
						"this argument must not be empty but was " + emptyArray.toString()
					),
				() -> assertRequirementFailure(
					() -> Require.nonNull( emptyArray, "error message" ),
						"error message"
					),
				() -> assertRequirementFailure(
					() -> Require.nonNull( emptyArray, () -> "error message" ),
						"error message"
					)
			);

		}

		@Test
		public void if_char_array_is_NOT_null_or_empty_should_be_returned()
		{

			final char[] array = new char[] { ' ' };
			assertAll(
				() -> assertSame( array,
					Require.nonNull( array )
				),
				() -> assertSame( array,
					Require.nonNull( array, "error message" )
				),
				() -> assertSame( array,
					Require.nonNull( array, () -> "error message" )
				)
			);

		}

	}

	/**
	 * Performs unit tests on method {@link Require#nonEmpty(byte[])}.
	 */
	@Nested
	@DisplayName("on invoking method: nonEmpty(byte[])")
	public class EmptyByteArrayTests
	{

		@Test
		public void if_byte_array_is_null_or_empty_should_throw_a_RequirementFailure()
		{

			final byte[] emptyArray = new byte[0];
			assertAll(
				() -> assertRequirementFailure(
					() -> Require.nonEmpty( (byte[]) null ),
						"this argument must not be empty but was null"
					),
				() -> assertRequirementFailure(
					() -> Require.nonNull( (byte[]) null, "error message" ),
						"error message"
					),
				() -> assertRequirementFailure(
					() -> Require.nonNull( (byte[]) null, () -> "error message" ),
						"error message"
					),
				() -> assertRequirementFailure(
					() -> Require.nonEmpty( emptyArray ),
						"this argument must not be empty but was " + emptyArray
					),
				() -> assertRequirementFailure(
					() -> Require.nonNull( emptyArray, "error message" ),
						"error message"
					),
				() -> assertRequirementFailure(
					() -> Require.nonNull( emptyArray, () -> "error message" ),
						"error message"
					)
			);

		}

		@Test
		public void if_byte_array_is_NOT_null_or_empty_should_be_returned()
		{

			final byte[] array = new byte[] { 0 };
			assertAll(
				() -> assertSame( array,
					Require.nonNull( array )
				),
				() -> assertSame( array,
					Require.nonNull( array, "error message" )
				),
				() -> assertSame( array,
					Require.nonNull( array, () -> "error message" )
				)
			);

		}

	}

	/**
	 * Performs unit tests on method {@link Require#nonEmpty(short[])}.
	 */
	@Nested
	@DisplayName("on invoking method: nonEmpty(short[])")
	public class EmptyShortArrayTests
	{

		@Test
		public void if_short_array_is_null_or_empty_should_throw_a_RequirementFailure()
		{

			final short[] emptyArray = new short[0];
			assertAll(
				() -> assertRequirementFailure(
					() -> Require.nonEmpty( (short[]) null ),
						"this argument must not be empty but was null"
					),
				() -> assertRequirementFailure(
					() -> Require.nonNull( (short[]) null, "error message" ),
						"error message"
					),
				() -> assertRequirementFailure(
					() -> Require.nonNull( (short[]) null, () -> "error message" ),
						"error message"
					),
				() -> assertRequirementFailure(
					() -> Require.nonEmpty( emptyArray ),
						"this argument must not be empty but was " + emptyArray
					),
				() -> assertRequirementFailure(
					() -> Require.nonNull( emptyArray, "error message" ),
						"error message"
					),
				() -> assertRequirementFailure(
					() -> Require.nonNull( emptyArray, () -> "error message" ),
						"error message"
					)
			);

		}

		@Test
		public void if_short_array_is_NOT_null_or_empty_should_be_returned()
		{

			final short[] array = new short[] { 0 };
			assertAll(
				() -> assertSame( array,
					Require.nonNull( array )
				),
				() -> assertSame( array,
					Require.nonNull( array, "error message" )
				),
				() -> assertSame( array,
					Require.nonNull( array, () -> "error message" )
				)
			);

		}

	}

	/**
	 * Performs unit tests on method {@link Require#nonEmpty(int[])}.
	 */
	@Nested
	@DisplayName("on invoking method: nonEmpty(int[])")
	public class EmptyIntArrayTests
	{

		@Test
		public void if_int_array_is_null_or_empty_should_throw_a_RequirementFailure()
		{

			final int[] emptyArray = new int[0];
			assertAll(
				() -> assertRequirementFailure(
					() -> Require.nonEmpty( (int[]) null ),
						"this argument must not be empty but was null"
					),
				() -> assertRequirementFailure(
					() -> Require.nonNull( (int[]) null, "error message" ),
						"error message"
					),
				() -> assertRequirementFailure(
					() -> Require.nonNull( (int[]) null, () -> "error message" ),
						"error message"
					),
				() -> assertRequirementFailure(
					() -> Require.nonEmpty( emptyArray ),
						"this argument must not be empty but was " + emptyArray
					),
				() -> assertRequirementFailure(
					() -> Require.nonNull( emptyArray, "error message" ),
						"error message"
					),
				() -> assertRequirementFailure(
					() -> Require.nonNull( emptyArray, () -> "error message" ),
						"error message"
					)
			);

		}

		@Test
		public void if_int_array_is_NOT_null_or_empty_should_be_returned()
		{

			final int[] array = new int[] { 0 };
			assertAll(
				() -> assertSame( array,
					Require.nonNull( array )
				),
				() -> assertSame( array,
					Require.nonNull( array, "error message" )
				),
				() -> assertSame( array,
					Require.nonNull( array, () -> "error message" )
				)
			);

		}

	}
	/**
	 * Performs unit tests on method {@link Require#nonEmpty(float[])}.
	 */
	@Nested
	@DisplayName("on invoking method: nonEmpty(float[])")
	public class EmptyFloatArrayTests
	{

		@Test
		public void if_float_array_is_null_or_empty_should_throw_a_RequirementFailure()
		{

			final float[] emptyArray = new float[0];
			assertAll(
				() -> assertRequirementFailure(
					() -> Require.nonEmpty( (float[]) null ),
						"this argument must not be empty but was null"
					),
				() -> assertRequirementFailure(
					() -> Require.nonNull( (float[]) null, "error message" ),
						"error message"
					),
				() -> assertRequirementFailure(
					() -> Require.nonNull( (float[]) null, () -> "error message" ),
						"error message"
					),
				() -> assertRequirementFailure(
					() -> Require.nonEmpty( emptyArray ),
						"this argument must not be empty but was " + emptyArray
					),
				() -> assertRequirementFailure(
					() -> Require.nonNull( emptyArray, "error message" ),
						"error message"
					),
				() -> assertRequirementFailure(
					() -> Require.nonNull( emptyArray, () -> "error message" ),
						"error message"
					)
			);

		}

		@Test
		public void if_float_array_is_NOT_null_or_empty_should_be_returned()
		{

			final float[] array = new float[] { 0.0f };
			assertAll(
				() -> assertSame( array,
					Require.nonNull( array )
				),
				() -> assertSame( array,
					Require.nonNull( array, "error message" )
				),
				() -> assertSame( array,
					Require.nonNull( array, () -> "error message" )
				)
			);

		}

	}

	/**
	 * Performs unit tests on method {@link Require#nonEmpty(long[])}.
	 */
	@Nested
	@DisplayName("on invoking method: nonEmpty(long[])")
	public class EmptyLongArrayTests
	{

		@Test
		public void if_long_array_is_null_or_empty_should_throw_a_RequirementFailure()
		{

			final long[] emptyArray = new long[0];
			assertAll(
				() -> assertRequirementFailure(
					() -> Require.nonEmpty( (long[]) null ),
						"this argument must not be empty but was null"
					),
				() -> assertRequirementFailure(
					() -> Require.nonNull( (long[]) null, "error message" ),
						"error message"
					),
				() -> assertRequirementFailure(
					() -> Require.nonNull( (long[]) null, () -> "error message" ),
						"error message"
					),
				() -> assertRequirementFailure(
					() -> Require.nonEmpty( emptyArray ),
						"this argument must not be empty but was " + emptyArray
					),
				() -> assertRequirementFailure(
					() -> Require.nonNull( emptyArray, "error message" ),
						"error message"
					),
				() -> assertRequirementFailure(
					() -> Require.nonNull( emptyArray, () -> "error message" ),
						"error message"
					)
			);

		}

		@Test
		public void if_long_array_is_NOT_null_or_empty_should_be_returned()
		{

			final long[] array = new long[] { 0L };
			assertAll(
				() -> assertSame( array,
					Require.nonNull( array )
				),
				() -> assertSame( array,
					Require.nonNull( array, "error message" )
				),
				() -> assertSame( array,
					Require.nonNull( array, () -> "error message" )
				)
			);

		}

	}

	/**
	 * Performs unit tests on method {@link Require#nonEmpty(double[])}.
	 */
	@Nested
	@DisplayName("on invoking method: nonEmpty(double[])")
	public class EmptyDoubleArrayTests
	{

		@Test
		public void if_double_array_is_null_or_empty_should_throw_a_RequirementFailure()
		{

			final double[] emptyArray = new double[0];
			assertAll(
				() -> assertRequirementFailure(
					() -> Require.nonEmpty( (double[]) null ),
						"this argument must not be empty but was null"
					),
				() -> assertRequirementFailure(
					() -> Require.nonNull( (double[]) null, "error message" ),
						"error message"
					),
				() -> assertRequirementFailure(
					() -> Require.nonNull( (double[]) null, () -> "error message" ),
						"error message"
					),
				() -> assertRequirementFailure(
					() -> Require.nonEmpty( emptyArray ),
						"this argument must not be empty but was " + emptyArray
					),
				() -> assertRequirementFailure(
					() -> Require.nonNull( emptyArray, "error message" ),
						"error message"
					),
				() -> assertRequirementFailure(
					() -> Require.nonNull( emptyArray, () -> "error message" ),
						"error message"
					)
			);

		}

		@Test
		public void if_double_array_is_NOT_null_or_empty_should_be_returned()
		{

			final double[] array = new double[] { 0.0d };
			assertAll(
				() -> assertSame( array,
					Require.nonNull( array )
				),
				() -> assertSame( array,
					Require.nonNull( array, "error message" )
				),
				() -> assertSame( array,
					Require.nonNull( array, () -> "error message" )
				)
			);

		}

	}

	/**
	 * Performs unit tests on method {@link Require#nonBlank(String)}.
	 */
	@Nested
	@DisplayName("on invoking method: nonBlank(String)")
	public class BlankStringTests
	{

		@Test
		public void if_String_is_null_or_empty_should_throw_a_RequirementFailure()
		{

			final String emptyString = "";
			assertAll(
				() -> assertRequirementFailure(
					() -> Require.nonBlank( null ),
						"this argument must not be blank but was null"
					),
				() -> assertRequirementFailure(
					() -> Require.nonBlank( null, "error message" ),
						"error message"
					),
				() -> assertRequirementFailure(
					() -> Require.nonBlank(  null, () -> "error message" ),
						"error message"
					),
				() -> assertRequirementFailure(
					() -> Require.nonBlank( emptyString ),
						"this argument must not be blank but was " + emptyString
					),
				() -> assertRequirementFailure(
					() -> Require.nonBlank( emptyString, "error message" ),
						"error message"
					),
				() -> assertRequirementFailure(
					() -> Require.nonBlank( emptyString, () -> "error message" ),
						"error message"
					)
			);

		}

		@Test
		public void if_String_contains_only_blank_chars_should_throw_a_RequirementFailure()
		{


			final String blankString = ""
					+ (char) Character.SPACE_SEPARATOR
					+ (char) Character.LINE_SEPARATOR
					+ '\u0009' // HORIZONTAL TABULATION
					+ '\u000B' // VERTICAL TABULATION
					+ '\u000C' // FORM FEED
					+ '\u001C' // FILE SEPARATOR
					+ '\u001D' // GROUP SEPARATOR
					+ '\u001E' // RECORD SEPARATOR
					+ '\u001F' // UNIT SEPARATOR
				;

			assertAll(
					() -> assertRequirementFailure(
							() -> Require.nonBlank( blankString ),
							"this argument must not be blank but was " + blankString
					),
					() -> assertRequirementFailure(
							() -> Require.nonBlank( blankString, "error message" ),
							"error message"
					),
					() -> assertRequirementFailure(
							() -> Require.nonBlank( blankString, () -> "error message" ),
							"error message"
					)
			);

		}

		@Test
		public void if_String_contains_NON_blank_chars_should_be_returned()
		{


			/* \u00A0 0 not breaking space, is not a blank character */
			final String string = "\u00A0";
			assertAll(
				() -> assertSame( string,
					Require.nonNull( string )
				),
				() -> assertSame( string,
					Require.nonNull( string, "error message" )
				),
				() -> assertSame( string,
					Require.nonNull( string, () -> "error message" )
				)
			);

		}

	}


	/* **************** */
	/*  HELPER METHODS  */
	/* **************** */


	/**
	 * Asserts that the given executable throws
	 * a {@link RequirementFailure} and that the
	 * error message looks like expected.
	 *
	 * @param executable   the executable that throwx the exception.
	 * @param errorMessage the expected error message.
	 */
	private void assertRequirementFailure( Executable executable, String errorMessage )
	{

		try{

			executable.execute();

		}catch( RequirementFailure ex )
		{

			assertEquals( "[Requirement failed] - " + errorMessage, ex.getMessage() );

		}catch( Throwable ex )
		{

			fail( "Unexpected exception type thrown ==> expected: <"
					+ RequirementFailure.class
					+ "> but was: <"
					+ ex.getClass() + ">" );
		}

	}

}

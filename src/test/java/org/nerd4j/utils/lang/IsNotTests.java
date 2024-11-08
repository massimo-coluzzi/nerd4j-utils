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

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for the utility class {@link IsNot}.
 *
 * @author Massimo Coluzzi
 */
@DisplayName("Testing utility class: IsNot")
@DisplayNameGeneration(ReplaceUnderscores.class)
public class IsNotTests
{


	/**
	 * Performs unit tests on method {@link IsNot#NULL(Object)}.
	 */
	@Nested
	@DisplayName("on invoking method: NULL(Object)")
	public class NullTests
	{

		@Test
		public void if_the_argument_is_null_should_return_false()
		{

			assertFalse( IsNot.NULL(null) );

		}

		@Test
		public void if_the_argument_is_NOT_null_should_return_true()
		{

			assertAll(
				() -> assertTrue( IsNot.NULL(new Object()) ),
				() -> assertTrue( IsNot.NULL(0) ),
				() -> assertTrue( IsNot.NULL("") ),
				() -> assertTrue( IsNot.NULL(new int[0]) )

			);

		}

	}

	/**
	 * Performs unit tests on method {@link IsNot#empty(Emptily)}.
	 */
	@Nested
	@DisplayName("on invoking method: empty(Emptily)")
	public class EmptyEmptilyTests
	{

		@Test
		public void if_Emptily_is_null_or_empty_should_return_false()
		{

			assertAll(
				() -> assertFalse(  IsNot.empty((Emptily) null) ),
				() -> assertFalse(  IsNot.empty(new TestClasses.EmptilyType(true)) )
			);

		}

		@Test
		public void if_Emptily_is_NOT_null_or_empty_should_return_true()
		{

			assertTrue( IsNot.empty(new TestClasses.EmptilyType(false)) );

		}

	}

	/**
	 * Performs unit tests on method {@link IsNot#empty(String)}.
	 */
	@Nested
	@DisplayName("on invoking method: empty(String)")
	public class EmptyStringTests
	{

		@Test
		public void if_String_is_null_or_empty_should_return_false()
		{

			assertAll(
				() -> assertFalse(  IsNot.empty((String) null) ),
				() -> assertFalse(  IsNot.empty("") )
			);

		}

		@Test
		public void if_String_is_NOT_null_or_empty_should_return_true()
		{

			assertTrue( IsNot.empty(" ") );

		}

	}

	/**
	 * Performs unit tests on method {@link IsNot#empty(Collection)}.
	 */
	@Nested
	@DisplayName("on invoking method: empty(Collection)")
	public class EmptyCollectionTests
	{

		@Test
		public void if_Collection_is_null_or_empty_should_return_false()
		{

			assertAll(
				() -> assertFalse(  IsNot.empty((Collection<?>) null) ),
				() -> assertFalse(  IsNot.empty(Collections.emptySet()) ),
				() -> assertFalse(  IsNot.empty(Collections.emptyList()) )
			);

		}

		@Test
		public void if_Collection_is_NOT_null_or_empty_should_return_true()
		{

			assertAll(
				() -> assertTrue( IsNot.empty(Collections.singleton(null)) ),
				() -> assertTrue( IsNot.empty(Collections.singletonList(null)) )
			);

		}

	}

	/**
	 * Performs unit tests on method {@link IsNot#empty(Map)}.
	 */
	@Nested
	@DisplayName("on invoking method: empty(Map)")
	public class EmptyMapTests
	{

		@Test
		public void if_Map_is_null_or_empty_should_return_false()
		{

			assertAll(
				() -> assertFalse(  IsNot.empty((Map<?,?>) null) ),
				() -> assertFalse(  IsNot.empty(Collections.emptyMap()) )
			);

		}

		@Test
		public void if_Map_is_NOT_null_or_empty_should_return_true()
		{

			assertTrue( IsNot.empty(Collections.singletonMap(null,null)) );

		}

	}

	/**
	 * Performs unit tests on method {@link IsNot#empty(Object[])}.
	 */
	@Nested
	@DisplayName("on invoking method: empty(Object[])")
	public class EmptyObjectArrayTests
	{

		@Test
		public void if_Object_array_is_null_or_empty_should_return_false()
		{

			assertAll(
				() -> assertFalse(  IsNot.empty((Object[]) null) ),
				() -> assertFalse(  IsNot.empty(new Object[0]) )
			);

		}

		@Test
		public void if_Object_array_is_NOT_null_or_empty_should_return_true()
		{

			assertTrue( IsNot.empty(new Object[]{null}) );

		}

	}

	/**
	 * Performs unit tests on method {@link IsNot#empty(boolean[])}.
	 */
	@Nested
	@DisplayName("on invoking method: empty(boolean[])")
	public class EmptyBooleanArrayTests
	{

		@Test
		public void if_boolean_array_is_null_or_empty_should_return_false()
		{

			assertAll(
				() -> assertFalse(  IsNot.empty((boolean[]) null) ),
				() -> assertFalse(  IsNot.empty(new boolean[0]) )
			);

		}

		@Test
		public void if_boolean_array_is_NOT_null_or_empty_should_return_true()
		{

			assertTrue( IsNot.empty(new boolean[]{false}) );

		}

	}

	/**
	 * Performs unit tests on method {@link IsNot#empty(char[])}.
	 */
	@Nested
	@DisplayName("on invoking method: empty(char[])")
	public class EmptyCharArrayTests
	{

		@Test
		public void if_char_array_is_null_or_empty_should_return_false()
		{

			assertAll(
				() -> assertFalse(  IsNot.empty((char[]) null) ),
				() -> assertFalse(  IsNot.empty(new char[0]) )
			);

		}

		@Test
		public void if_char_array_is_NOT_null_or_empty_should_return_true()
		{

			assertTrue( IsNot.empty(new char[]{' '}) );

		}

	}

	/**
	 * Performs unit tests on method {@link IsNot#empty(byte[])}.
	 */
	@Nested
	@DisplayName("on invoking method: empty(byte[])")
	public class EmptyByteArrayTests
	{

		@Test
		public void if_byte_array_is_null_or_empty_should_return_false()
		{

			assertAll(
				() -> assertFalse(  IsNot.empty((byte[]) null) ),
				() -> assertFalse(  IsNot.empty(new byte[0]) )
			);

		}

		@Test
		public void if_byte_array_is_NOT_null_or_empty_should_return_true()
		{

			assertTrue( IsNot.empty(new byte[]{0}) );

		}

	}

	/**
	 * Performs unit tests on method {@link IsNot#empty(short[])}.
	 */
	@Nested
	@DisplayName("on invoking method: empty(short[])")
	public class EmptyShortArrayTests
	{

		@Test
		public void if_short_array_is_null_or_empty_should_return_false()
		{

			assertAll(
				() -> assertFalse(  IsNot.empty((short[]) null) ),
				() -> assertFalse(  IsNot.empty(new short[0]) )
			);

		}

		@Test
		public void if_short_array_is_NOT_null_or_empty_should_return_true()
		{

			assertTrue( IsNot.empty(new short[]{0}) );

		}

	}

	/**
	 * Performs unit tests on method {@link IsNot#empty(int[])}.
	 */
	@Nested
	@DisplayName("on invoking method: empty(int[])")
	public class EmptyIntArrayTests
	{

		@Test
		public void if_int_array_is_null_or_empty_should_return_false()
		{

			assertAll(
				() -> assertFalse(  IsNot.empty((int[]) null) ),
				() -> assertFalse(  IsNot.empty(new int[0]) )
			);

		}

		@Test
		public void if_int_array_is_NOT_null_or_empty_should_return_true()
		{

			assertTrue( IsNot.empty(new int[]{0}) );

		}

	}

	/**
	 * Performs unit tests on method {@link IsNot#empty(float[])}.
	 */
	@Nested
	@DisplayName("on invoking method: empty(float[])")
	public class EmptyFloatArrayTests
	{

		@Test
		public void if_float_array_is_null_or_empty_should_return_false()
		{

			assertAll(
				() -> assertFalse(  IsNot.empty((float[]) null) ),
				() -> assertFalse(  IsNot.empty(new float[0]) )
			);

		}

		@Test
		public void if_float_array_is_NOT_null_or_empty_should_return_true()
		{

			assertTrue( IsNot.empty(new float[]{0}) );

		}

	}

	/**
	 * Performs unit tests on method {@link IsNot#empty(long[])}.
	 */
	@Nested
	@DisplayName("on invoking method: empty(long[])")
	public class EmptyLongArrayTests
	{

		@Test
		public void if_long_array_is_null_or_empty_should_return_false()
		{

			assertAll(
				() -> assertFalse(  IsNot.empty((long[]) null) ),
				() -> assertFalse(  IsNot.empty(new long[0]) )
			);

		}

		@Test
		public void if_long_array_is_NOT_null_or_empty_should_return_true()
		{

			assertTrue( IsNot.empty(new long[]{0L}) );

		}

	}

	/**
	 * Performs unit tests on method {@link IsNot#empty(double[])}.
	 */
	@Nested
	@DisplayName("on invoking method: empty(double[])")
	public class EmptyDoubleArrayTests
	{

		@Test
		public void if_double_array_is_null_or_empty_should_return_false()
		{

			assertAll(
				() -> assertFalse(  IsNot.empty((double[]) null) ),
				() -> assertFalse(  IsNot.empty(new double[0]) )
			);

		}

		@Test
		public void if_double_array_is_NOT_null_or_empty_should_return_true()
		{

			assertTrue( IsNot.empty(new double[]{0,0}) );

		}

	}

	/**
	 * Performs unit tests on method {@link IsNot#blank(String)}.
	 */
	@Nested
	@DisplayName("on invoking method: blank(String)")
	public class BlankStringTests
	{

		@Test
		public void if_String_is_null_or_empty_should_return_false()
		{

			assertAll(
					() -> assertFalse(  IsNot.empty((String) null) ),
					() -> assertFalse(  IsNot.empty("") )
			);

		}

		@Test
		public void if_String_contains_only_blank_chars_should_return_false()
		{


			final String blank = ""
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

			assertFalse( IsNot.blank(blank) );

		}

		@Test
		public void if_String_contains_NON_blank_chars_should_return_true()
		{


			/* \u00A0 0 not breaking space, is not a blank character */
			assertTrue( IsNot.blank("\u00A0") );

		}

	}


}

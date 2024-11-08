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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.nerd4j.utils.lang.TestClasses.EmptilyType;

/**
 * Test suite for the utility class {@link Is}.
 *
 * @author Massimo Coluzzi
 */
@DisplayName("Testing utility class: Is")
@DisplayNameGeneration(ReplaceUnderscores.class)
public class IsTests
{


	/**
	 * Performs unit tests on method {@link Is#NULL(Object)}.
	 */
	@Nested
	@DisplayName("on invoking method: NULL(Object)")
	public class NullTests
	{

		@Test
		public void if_the_argument_is_null_should_return_true()
		{

			assertTrue( Is.NULL(null) );

		}

		@Test
		public void if_the_argument_is_NOT_null_should_return_false()
		{

			assertAll(
				() -> assertFalse( Is.NULL(new Object()) ),
				() -> assertFalse( Is.NULL(0) ),
				() -> assertFalse( Is.NULL("") ),
				() -> assertFalse( Is.NULL(new int[0]) )

			);

		}

	}

	/**
	 * Performs unit tests on method {@link Is#empty(Emptily)}.
	 */
	@Nested
	@DisplayName("on invoking method: empty(Emptily)")
	public class EmptyEmptilyTests
	{

		@Test
		public void if_Emptily_is_null_or_empty_should_return_true()
		{

			assertAll(
				() -> assertTrue(  Is.empty((Emptily) null) ),
				() -> assertTrue(  Is.empty(new EmptilyType(true)) )
			);

		}

		@Test
		public void if_Emptily_is_NOT_null_or_empty_should_return_false()
		{

			assertFalse( Is.empty(new EmptilyType(false)) );

		}

	}

	/**
	 * Performs unit tests on method {@link Is#empty(String)}.
	 */
	@Nested
	@DisplayName("on invoking method: empty(String)")
	public class EmptyStringTests
	{

		@Test
		public void if_String_is_null_or_empty_should_return_true()
		{

			assertAll(
				() -> assertTrue(  Is.empty((String) null) ),
				() -> assertTrue(  Is.empty("") )
			);

		}

		@Test
		public void if_String_is_NOT_null_or_empty_should_return_false()
		{

			assertFalse( Is.empty(" ") );

		}

	}

	/**
	 * Performs unit tests on method {@link Is#empty(Collection)}.
	 */
	@Nested
	@DisplayName("on invoking method: empty(Collection)")
	public class EmptyCollectionTests
	{

		@Test
		public void if_Collection_is_null_or_empty_should_return_true()
		{

			assertAll(
				() -> assertTrue(  Is.empty((Collection<?>) null) ),
				() -> assertTrue(  Is.empty(Collections.emptySet()) ),
				() -> assertTrue(  Is.empty(Collections.emptyList()) )
			);

		}

		@Test
		public void if_Collection_is_NOT_null_or_empty_should_return_false()
		{

			assertAll(
				() -> assertFalse( Is.empty(Collections.singleton(null)) ),
				() -> assertFalse( Is.empty(Collections.singletonList(null)) )
			);

		}

	}

	/**
	 * Performs unit tests on method {@link Is#empty(Map)}.
	 */
	@Nested
	@DisplayName("on invoking method: empty(Map)")
	public class EmptyMapTests
	{

		@Test
		public void if_Map_is_null_or_empty_should_return_true()
		{

			assertAll(
				() -> assertTrue(  Is.empty((Map<?,?>) null) ),
				() -> assertTrue(  Is.empty(Collections.emptyMap()) )
			);

		}

		@Test
		public void if_Map_is_NOT_null_or_empty_should_return_false()
		{

			assertFalse( Is.empty(Collections.singletonMap(null,null)) );

		}

	}

	/**
	 * Performs unit tests on method {@link Is#empty(Object[])}.
	 */
	@Nested
	@DisplayName("on invoking method: empty(Object[])")
	public class EmptyObjectArrayTests
	{

		@Test
		public void if_Object_array_is_null_or_empty_should_return_true()
		{

			assertAll(
				() -> assertTrue(  Is.empty((Object[]) null) ),
				() -> assertTrue(  Is.empty(new Object[0]) )
			);

		}

		@Test
		public void if_Object_array_is_NOT_null_or_empty_should_return_false()
		{

			assertFalse( Is.empty(new Object[]{null}) );

		}

	}

	/**
	 * Performs unit tests on method {@link Is#empty(boolean[])}.
	 */
	@Nested
	@DisplayName("on invoking method: empty(boolean[])")
	public class EmptyBooleanArrayTests
	{

		@Test
		public void if_boolean_array_is_null_or_empty_should_return_true()
		{

			assertAll(
				() -> assertTrue(  Is.empty((boolean[]) null) ),
				() -> assertTrue(  Is.empty(new boolean[0]) )
			);

		}

		@Test
		public void if_boolean_array_is_NOT_null_or_empty_should_return_false()
		{

			assertFalse( Is.empty(new boolean[]{false}) );

		}

	}

	/**
	 * Performs unit tests on method {@link Is#empty(char[])}.
	 */
	@Nested
	@DisplayName("on invoking method: empty(char[])")
	public class EmptyCharArrayTests
	{

		@Test
		public void if_char_array_is_null_or_empty_should_return_true()
		{

			assertAll(
				() -> assertTrue(  Is.empty((char[]) null) ),
				() -> assertTrue(  Is.empty(new char[0]) )
			);

		}

		@Test
		public void if_char_array_is_NOT_null_or_empty_should_return_false()
		{

			assertFalse( Is.empty(new char[]{' '}) );

		}

	}

	/**
	 * Performs unit tests on method {@link Is#empty(byte[])}.
	 */
	@Nested
	@DisplayName("on invoking method: empty(byte[])")
	public class EmptyByteArrayTests
	{

		@Test
		public void if_byte_array_is_null_or_empty_should_return_true()
		{

			assertAll(
				() -> assertTrue(  Is.empty((byte[]) null) ),
				() -> assertTrue(  Is.empty(new byte[0]) )
			);

		}

		@Test
		public void if_byte_array_is_NOT_null_or_empty_should_return_false()
		{

			assertFalse( Is.empty(new byte[]{0}) );

		}

	}

	/**
	 * Performs unit tests on method {@link Is#empty(short[])}.
	 */
	@Nested
	@DisplayName("on invoking method: empty(short[])")
	public class EmptyShortArrayTests
	{

		@Test
		public void if_short_array_is_null_or_empty_should_return_true()
		{

			assertAll(
				() -> assertTrue(  Is.empty((short[]) null) ),
				() -> assertTrue(  Is.empty(new short[0]) )
			);

		}

		@Test
		public void if_short_array_is_NOT_null_or_empty_should_return_false()
		{

			assertFalse( Is.empty(new short[]{0}) );

		}

	}

	/**
	 * Performs unit tests on method {@link Is#empty(int[])}.
	 */
	@Nested
	@DisplayName("on invoking method: empty(int[])")
	public class EmptyIntArrayTests
	{

		@Test
		public void if_int_array_is_null_or_empty_should_return_true()
		{

			assertAll(
				() -> assertTrue(  Is.empty((int[]) null) ),
				() -> assertTrue(  Is.empty(new int[0]) )
			);

		}

		@Test
		public void if_int_array_is_NOT_null_or_empty_should_return_false()
		{

			assertFalse( Is.empty(new int[]{0}) );

		}

	}

	/**
	 * Performs unit tests on method {@link Is#empty(float[])}.
	 */
	@Nested
	@DisplayName("on invoking method: empty(float[])")
	public class EmptyFloatArrayTests
	{

		@Test
		public void if_float_array_is_null_or_empty_should_return_true()
		{

			assertAll(
				() -> assertTrue(  Is.empty((float[]) null) ),
				() -> assertTrue(  Is.empty(new float[0]) )
			);

		}

		@Test
		public void if_float_array_is_NOT_null_or_empty_should_return_false()
		{

			assertFalse( Is.empty(new float[]{0}) );

		}

	}

	/**
	 * Performs unit tests on method {@link Is#empty(long[])}.
	 */
	@Nested
	@DisplayName("on invoking method: empty(long[])")
	public class EmptyLongArrayTests
	{

		@Test
		public void if_long_array_is_null_or_empty_should_return_true()
		{

			assertAll(
				() -> assertTrue(  Is.empty((long[]) null) ),
				() -> assertTrue(  Is.empty(new long[0]) )
			);

		}

		@Test
		public void if_long_array_is_NOT_null_or_empty_should_return_false()
		{

			assertFalse( Is.empty(new long[]{0L}) );

		}

	}

	/**
	 * Performs unit tests on method {@link Is#empty(double[])}.
	 */
	@Nested
	@DisplayName("on invoking method: empty(double[])")
	public class EmptyDoubleArrayTests
	{

		@Test
		public void if_double_array_is_null_or_empty_should_return_true()
		{

			assertAll(
				() -> assertTrue(  Is.empty((double[]) null) ),
				() -> assertTrue(  Is.empty(new double[0]) )
			);

		}

		@Test
		public void if_double_array_is_NOT_null_or_empty_should_return_false()
		{

			assertFalse( Is.empty(new double[]{0,0}) );

		}

	}

	/**
	 * Performs unit tests on method {@link Is#blank(String)}.
	 */
	@Nested
	@DisplayName("on invoking method: blank(String)")
	public class BlankStringTests
	{

		@Test
		public void if_String_is_null_or_empty_should_return_true()
		{

			assertAll(
					() -> assertTrue(  Is.empty((String) null) ),
					() -> assertTrue(  Is.empty("") )
			);

		}

		@Test
		public void if_String_contains_only_blank_chars_should_return_true()
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

			assertTrue( Is.blank(blank) );

		}

		@Test
		public void if_String_contains_NON_blank_chars_should_return_false()
		{


			/* \u00A0 0 not breaking space, is not a blank character */
			assertFalse( Is.blank("\u00A0") );

		}

	}


}

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
package org.nerd4j.utils.math;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigInteger;
import java.util.function.BinaryOperator;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.nerd4j.utils.lang.RequirementFailure;

/**
 * Test suite for the utility class {@link BI}.
 *
 * @author Massimo Coluzzi
 */
@DisplayName("Testing utility class: BI")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class BITests
{

	/**
	 * Performs unit test on method {@code of}.
	 */
	@Nested
	@DisplayName("on invoking method 'of'")
	public class Of
	{

		@ValueSource(longs={Long.MIN_VALUE,-1,0,1,Long.MAX_VALUE})
		@ParameterizedTest(name="BI.of({0}) == {0}")
		public void method_of_long( long source )
		{

			final BigInteger target = BI.of( source );
			assertEquals( source, target.longValue() );

		}

		@NullAndEmptySource()
		@ParameterizedTest(name="BI.of({0}) == null")
		public void method_of_empty_string( String source )
		{

			final BigInteger target = BI.of( source );
			assertNull( target );

		}

		@ValueSource(strings={
			"92233720368547758089223372036854775808",
		    "-92233720368547758089223372036854775808"
		})
		@ParameterizedTest(name="BI.of({0}) == {0}")
		public void method_of_valued_string( String source )
		{

			final BigInteger target = BI.of( source );
			assertEquals( source, target.toString() );

		}

		@ValueSource(strings={
			" ", "\t", "0-0", "BigInteger"
		})
		@ParameterizedTest(name="BI.of({0}) => NumberFormatException")
		public void method_of_wrong_valued_string( String source )
		{

			assertThrows(
				NumberFormatException.class,
				() -> BI.of( source )
			);

		}

	}

	/**
	 * Performs unit test on method {@code sum}.
	 */
	@Nested
	@DisplayName("on invoking method 'sum'")
	public class Sum
	{

		@CsvSource(value={
			"null,null,null",
			"null,1000,1000",
			"1000,null,1000",
			"10000000000000000000,10000000000000000000,20000000000000000000"
		},nullValues="null")
		@ParameterizedTest(name="BI.sum({0},{1}) == {2}")
		public void method_sum_BigInteger_BigInteger(
			BigInteger a, BigInteger b, BigInteger expected )
		{

			assertEquals( expected, BI.sum(a,b) );

		}

		@CsvSource(value={
			"null,1000,1000",
			"10000000000000000000,1000,10000000000000001000"
		},nullValues="null")
		@ParameterizedTest(name="BI.sum({0},{1}) == {2}")
		public void method_sum_BigInteger_long(
			BigInteger a, long b, BigInteger expected )
		{

			assertEquals( expected, BI.sum(a,b) );

		}

		@CsvSource(value={
			"1000,null,1000",
			"1000,10000000000000000000,10000000000000001000"
		},nullValues="null")
		@ParameterizedTest(name="BI.sum({0},{1}) == {2}")
		public void method_sum_long_BigInteger(
			long a, BigInteger b, BigInteger expected )
		{

			assertEquals( expected, BI.sum(a,b) );

		}

		@Test
		public void method_sum_long_long()
		{

			final long a = 9000000000000000000L;
			final long b = 9000000000000000000L;
			final BigInteger expected = BI.of( "18000000000000000000" );

			assertEquals( expected, BI.sum(a,b) );

		}

	}

	/**
	 * Performs unit test on method {@code sub}.
	 */
	@Nested
	@DisplayName("on invoking method 'sub'")
	public class Sub
	{

		@CsvSource(value={
			"null,null,null",
			"null,1000,1000",
			"1000,null,1000",
			"30000000000000000000,10000000000000000000,20000000000000000000"
		},nullValues="null")
		@ParameterizedTest(name="BI.sub({0},{1}) == {2}")
		public void method_sub_BigInteger_BigInteger(
			BigInteger a, BigInteger b, BigInteger expected )
		{

			assertEquals( expected, BI.sub(a,b) );

		}

		@CsvSource(value={
			"null,1000,1000",
			"10000000000000000000,1000,9999999999999999000"
		},nullValues="null")
		@ParameterizedTest(name="BI.sub({0},{1}) == {2}")
		public void method_sub_BigInteger_long(
			BigInteger a, long b, BigInteger expected )
		{

			assertEquals( expected, BI.sub(a,b) );

		}

		@CsvSource(value={
			"1000,null,1000",
			"1000,10000000000000000000,-9999999999999999000"
		},nullValues="null")
		@ParameterizedTest(name="BI.sub({0},{1}) == {2}")
		public void method_sub_long_BigInteger(
			long a, BigInteger b, BigInteger expected )
		{

			assertEquals( expected, BI.sub(a,b) );

		}

		@Test
		public void method_sub_long_long()
		{

			final long a = -9000000000000000000L;
			final long b =  9000000000000000000L;
			final BigInteger expected = BI.of( "-18000000000000000000" );

			assertEquals( expected, BI.sub(a,b) );

		}

	}

	/**
	 * Performs unit test on method {@code mul}.
	 */
	@Nested
	@DisplayName("on invoking method 'mul'")
	public class Mul
	{

		@CsvSource(value={
			"null,null,null",
			"null,1000,1000",
			"1000,null,1000",
			"10000000000000000000,10000000000000000000,100000000000000000000000000000000000000"
		},nullValues="null")
		@ParameterizedTest(name="BI.mul({0},{1}) == {2}")
		public void method_mul_BigInteger_BigInteger(
			BigInteger a, BigInteger b, BigInteger expected )
		{

			assertEquals( expected, BI.mul(a,b) );

		}

		@CsvSource(value={
			"null,1000,1000",
			"10000000000000000000,1000,10000000000000000000000"
		},nullValues="null")
		@ParameterizedTest(name="BI.mul({0},{1}) == {2}")
		public void method_mul_BigInteger_long(
			BigInteger a, long b, BigInteger expected )
		{

			assertEquals( expected, BI.mul(a,b) );

		}

		@CsvSource(value={
			"1000,null,1000",
			"1000,10000000000000000000,10000000000000000000000"
		},nullValues="null")
		@ParameterizedTest(name="BI.mul({0},{1}) == {2}")
		public void method_mul_long_BigInteger(
			long a, BigInteger b, BigInteger expected )
		{

			assertEquals( expected, BI.mul(a,b) );

		}

		@Test
		public void method_mul_long_long()
		{

			final long a = 1000000000000000000L;
			final long b = 1000000000000000000L;
			final BigInteger expected = BI.of( "1000000000000000000000000000000000000" );

			assertEquals( expected, BI.mul(a,b) );

		}

	}

	/**
	 * Performs unit test on method {@code div}.
	 */
	@Nested
	@DisplayName("on invoking method 'div'")
	public class Div
	{

		@CsvSource(value={
			"null,null,null",
			"null,1000,1000",
			"1000,null,1000",
			"10000000000000000000,10000000000000000000,1"
		},nullValues="null")
		@ParameterizedTest(name="BI.div({0},{1}) == {2}")
		public void method_div_BigInteger_BigInteger(
			BigInteger a, BigInteger b, BigInteger expected )
		{

			assertEquals( expected, BI.div(a,b) );

		}

		@CsvSource(value={
			"null,1000,1000",
			"10000000000000000000,1000,10000000000000000"
		},nullValues="null")
		@ParameterizedTest(name="BI.div({0},{1}) == {2}")
		public void method_div_BigInteger_long(
			BigInteger a, long b, BigInteger expected )
		{

			assertEquals( expected, BI.div(a,b) );

		}

		@CsvSource(value={
			"1000,null,1000",
			"1000,10000000000000000000,0"
		},nullValues="null")
		@ParameterizedTest(name="BI.div({0},{1}) == {2}")
		public void method_div_long_BigInteger(
			long a, BigInteger b, BigInteger expected )
		{

			assertEquals( expected, BI.div(a,b) );

		}

		@Test
		public void method_div_long_long()
		{

			final long a = 2000000000000000000L;
			final long b = 1000000000000000000L;
			final BigInteger expected = BI.of( "2" );

			assertEquals( expected, BI.div(a,b) );

		}

	}

	/**
	 * Performs unit test on method {@code rem}.
	 */
	@Nested
	@DisplayName("on invoking method 'rem'")
	public class Rem
	{

		@CsvSource(value={
			"null,null,null",
			"null,1000,1000",
			"1000,null,1000",
			"-100000000000000000010,1000000000000000000,-10"
		},nullValues="null")
		@ParameterizedTest(name="BI.rem({0},{1}) == {2}")
		public void method_rem_BigInteger_BigInteger(
			BigInteger a, BigInteger b, BigInteger expected )
		{

			assertEquals( expected, BI.rem(a,b) );

		}

		@CsvSource(value={
			"null,1000,1000",
			"-10000000000000000010,1000,-10"
		},nullValues="null")
		@ParameterizedTest(name="BI.rem({0},{1}) == {2}")
		public void method_rem_BigInteger_long(
			BigInteger a, long b, BigInteger expected )
		{

			assertEquals( expected, BI.rem(a,b) );

		}

		@CsvSource(value={
			"1000,null,1000",
			"-1000,10000000000000000000,-1000"
		},nullValues="null")
		@ParameterizedTest(name="BI.rem({0},{1}) == {2}")
		public void method_rem_long_BigInteger(
			long a, BigInteger b, BigInteger expected )
		{

			assertEquals( expected, BI.rem(a,b) );

		}

		@Test
		public void method_rem_long_long()
		{

			final long a = -2000000000000000010L;
			final long b =  1000000000000000000L;
			final BigInteger expected = BI.of( -10L );

			assertEquals( expected, BI.rem(a,b) );

		}

	}

	/**
	 * Performs unit test on method {@code mod}.
	 */
	@Nested
	@DisplayName("on invoking method 'mod'")
	public class Mod
	{

		@CsvSource(value={
			"null,null,null",
			"null,1000,1000",
			"1000,null,1000",
			"-100000000000000000010,100,90"
		},nullValues="null")
		@ParameterizedTest(name="BI.mod({0},{1}) == {2}")
		public void method_mod_BigInteger_BigInteger(
			BigInteger a, BigInteger b, BigInteger expected )
		{

			assertEquals( expected, BI.mod(a,b) );

		}

		@CsvSource(value={
			"null,1000,1000",
			"-10000000000000000010,1000,990"
		},nullValues="null")
		@ParameterizedTest(name="BI.mod({0},{1}) == {2}")
		public void method_mod_BigInteger_long(
			BigInteger a, long b, BigInteger expected )
		{

			assertEquals( expected, BI.mod(a,b) );

		}

		@CsvSource(value={
			"1000,null,1000",
			"-1000,10000000000000000000,9999999999999999000"
		},nullValues="null")
		@ParameterizedTest(name="BI.mod({0},{1}) == {2}")
		public void method_mod_long_BigInteger(
			long a, BigInteger b, BigInteger expected )
		{

			assertEquals( expected, BI.mod(a,b) );

		}

		@Test
		public void method_mod_long_long()
		{

			final long a = -2000000000000000010L;
			final long b =  100L;
			final BigInteger expected = BI.of( 90L );

			assertEquals( expected, BI.mod(a,b) );

		}

	}

	/**
	 * Performs unit test on method {@code and}.
	 */
	@Nested
	@DisplayName("on invoking method 'and'")
	public class And
	{

		@CsvSource(value={
			"null,null,null",
			"null,1000,1000",
			"1000,null,1000",
			"3276,2730,2184"
		},nullValues="null")
		@ParameterizedTest(name="BI.and({0},{1}) == {2}")
		public void method_and_BigInteger_BigInteger(
			BigInteger a, BigInteger b, BigInteger expected )
		{

			assertEquals( expected, BI.and(a,b) );

		}

		@CsvSource(value={
			"null,1000,1000",
			"3276,2730,2184"
		},nullValues="null")
		@ParameterizedTest(name="BI.and({0},{1}) == {2}")
		public void method_and_BigInteger_long(
			BigInteger a, long b, BigInteger expected )
		{

			assertEquals( expected, BI.and(a,b) );

		}

		@CsvSource(value={
			"1000,null,1000",
			"3276,2730,2184"
		},nullValues="null")
		@ParameterizedTest(name="BI.and({0},{1}) == {2}")
		public void method_and_long_BigInteger(
			long a, BigInteger b, BigInteger expected )
		{

			assertEquals( expected, BI.and(a,b) );

		}

		@Test
		public void method_and_long_long()
		{

			final long a = 3276L;
			final long b = 2730L;
			final BigInteger expected = BI.of( 2184L );

			assertEquals( expected, BI.and(a,b) );

		}

	}

	/**
	 * Performs unit test on method {@code or}.
	 */
	@Nested
	@DisplayName("on invoking method 'or'")
	public class Or
	{

		@CsvSource(value={
			"null,null,null",
			"null,1000,1000",
			"1000,null,1000",
			"3276,2730,3822"
		},nullValues="null")
		@ParameterizedTest(name="BI.or({0},{1}) == {2}")
		public void method_or_BigInteger_BigInteger(
			BigInteger a, BigInteger b, BigInteger expected )
		{

			assertEquals( expected, BI.or(a,b) );

		}

		@CsvSource(value={
			"null,1000,1000",
			"3276,2730,3822"
		},nullValues="null")
		@ParameterizedTest(name="BI.or({0},{1}) == {2}")
		public void method_or_BigInteger_long(
			BigInteger a, long b, BigInteger expected )
		{

			assertEquals( expected, BI.or(a,b) );

		}

		@CsvSource(value={
			"1000,null,1000",
			"3276,2730,3822"
		},nullValues="null")
		@ParameterizedTest(name="BI.or({0},{1}) == {2}")
		public void method_or_long_BigInteger(
			long a, BigInteger b, BigInteger expected )
		{

			assertEquals( expected, BI.or(a,b) );

		}

		@Test
		public void method_or_long_long()
		{

			final long a = 3276L;
			final long b = 2730L;
			final BigInteger expected = BI.of( 3822L );

			assertEquals( expected, BI.or(a,b) );

		}

	}

	/**
	 * Performs unit test on method {@code xor}.
	 */
	@Nested
	@DisplayName("on invoking method 'xor'")
	public class Xor
	{

		@CsvSource(value={
			"null,null,null",
			"null,1000,1000",
			"1000,null,1000",
			"3276,2730,1638"
		},nullValues="null")
		@ParameterizedTest(name="BI.xor({0},{1}) == {2}")
		public void method_xor_BigInteger_BigInteger(
			BigInteger a, BigInteger b, BigInteger expected )
		{

			assertEquals( expected, BI.xor(a,b) );

		}

		@CsvSource(value={
			"null,1000,1000",
			"3276,2730,1638"
		},nullValues="null")
		@ParameterizedTest(name="BI.xor({0},{1}) == {2}")
		public void method_xor_BigInteger_long(
			BigInteger a, long b, BigInteger expected )
		{

			assertEquals( expected, BI.xor(a,b) );

		}

		@CsvSource(value={
			"1000,null,1000",
			"3276,2730,1638"
		},nullValues="null")
		@ParameterizedTest(name="BI.xor({0},{1}) == {2}")
		public void method_xor_long_BigInteger(
			long a, BigInteger b, BigInteger expected )
		{

			assertEquals( expected, BI.xor(a,b) );

		}

		@Test
		public void method_xor_long_long()
		{

			final long a = 3276L;
			final long b = 2730L;
			final BigInteger expected = BI.of( 1638L );

			assertEquals( expected, BI.xor(a,b) );

		}

	}


	/**
	 * Performs unit test on method {@code min}.
	 */
	@Nested
	@DisplayName("on invoking method 'min'")
	public class Min
	{

		@CsvSource(value={
			"null,1000000000000000000,null",
			"-100000000000000000000,1000000000000000000,-100000000000000000000",
			"1500000000000000000,1500000000000000000,1500000000000000000",
			"100000000000000000000,1000000000000000000,1000000000000000000"
		},nullValues="null")
		@ParameterizedTest(name="BI.min({0},{1}) == {2}")
		public void method_min_BigInteger_long(
			BigInteger a, long b, BigInteger expected )
		{

			assertEquals( expected, BI.min(a,b) );

		}

		@CsvSource(value={
			"null,1000,null",
			"-100000000000000000000,1000000000000000000,-100000000000000000000",
			"1500000000000000000,1500000000000000000,1500000000000000000",
			"100000000000000000000,1000000000000000000,1000000000000000000"
		},nullValues="null")
		@ParameterizedTest(name="BI.min({0},{1},false) == {2}")
		public void method_min_BigInteger_long_false(
			BigInteger a, long b, BigInteger expected )
		{

			assertEquals( expected, BI.min(a,b,false) );

		}


		@CsvSource(value={
			"null,1000000000000000000,1000000000000000000",
			"-100000000000000000000,1000000000000000000,-100000000000000000000",
			"1500000000000000000,1500000000000000000,1500000000000000000",
			"100000000000000000000,1000000000000000000,1000000000000000000"
		},nullValues="null")
		@ParameterizedTest(name="BI.min({0},{1},true) == {2}")
		public void method_min_BigInteger_long_true(
			BigInteger a, long b, BigInteger expected )
		{

			assertEquals( expected, BI.min(a,b,true) );

		}

		@CsvSource(value={
			"1000000000000000000,null,null",
			"1000000000000000000,100000000000000000000,1000000000000000000",
			"1500000000000000000,1500000000000000000,1500000000000000000",
			"1000000000000000000,-100000000000000000000,-100000000000000000000"
		},nullValues="null")
		@ParameterizedTest(name="BI.min({0},{1}) == {2}")
		public void method_min_long_BigInteger(
			long a, BigInteger b, BigInteger expected )
		{

			assertEquals( expected, BI.min(a,b) );

		}

		@CsvSource(value={
			"1000,null,null",
			"1000000000000000000,100000000000000000000,1000000000000000000",
			"1500000000000000000,1500000000000000000,1500000000000000000",
			"1000000000000000000,-100000000000000000000,-100000000000000000000"
		},nullValues="null")
		@ParameterizedTest(name="BI.min({0},{1},false) == {2}")
		public void method_min_long_BigInteger_false(
			long a, BigInteger b, BigInteger expected )
		{

			assertEquals( expected, BI.min(a,b,false) );

		}

		@CsvSource(value={
			"1000000000000000000,null,1000000000000000000",
			"1000000000000000000,100000000000000000000,1000000000000000000",
			"1500000000000000000,1500000000000000000,1500000000000000000",
			"1000000000000000000,-100000000000000000000,-100000000000000000000"
		},nullValues="null")
		@ParameterizedTest(name="BI.min({0},{1},true) == {2}")
		public void method_min_long_BigInteger_true(
			long a, BigInteger b, BigInteger expected )
		{

			assertEquals( expected, BI.min(a,b,true) );

		}


		@CsvSource(value={
			"1000000000000000000,2000000000000000000,1000000000000000000",
			"1500000000000000000,1500000000000000000,1500000000000000000",
			"2000000000000000000,1000000000000000000,1000000000000000000"
		},nullValues="null")
		@ParameterizedTest(name="BI.min({0},{1}) == {2}")
		public void method_min_long_long(
			long a, long b, BigInteger expected )
		{

			assertEquals( expected, BI.min(a,b) );

		}

	}


	/**
	 * Performs unit test on method {@code max}.
	 */
	@Nested
	@DisplayName("on invoking method 'max'")
	public class Max
	{

		@CsvSource(value={
			"null,1000000000000000000,1000000000000000000",
			"-100000000000000000000,1000000000000000000,1000000000000000000",
			"1500000000000000000,1500000000000000000,1500000000000000000",
			"100000000000000000000,1000000000000000000,100000000000000000000"
		},nullValues="null")
		@ParameterizedTest(name="BI.max({0},{1}) == {2}")
		public void method_max_BigInteger_long(
			BigInteger a, long b, BigInteger expected )
		{

			assertEquals( expected, BI.max(a,b) );

		}

		@CsvSource(value={
			"null,1000,1000",
			"-100000000000000000000,1000000000000000000,1000000000000000000",
			"1500000000000000000,1500000000000000000,1500000000000000000",
			"100000000000000000000,1000000000000000000,100000000000000000000"
		},nullValues="null")
		@ParameterizedTest(name="BI.max({0},{1},false) == {2}")
		public void method_max_BigInteger_long_false(
			BigInteger a, long b, BigInteger expected )
		{

			assertEquals( expected, BI.max(a,b,false) );

		}

		@CsvSource(value={
			"null,1000000000000000000,null",
			"-100000000000000000000,1000000000000000000,1000000000000000000",
			"1500000000000000000,1500000000000000000,1500000000000000000",
			"100000000000000000000,1000000000000000000,100000000000000000000"
		},nullValues="null")
		@ParameterizedTest(name="BI.max({0},{1},true) == {2}")
		public void method_max_BigInteger_long_true(
			BigInteger a, long b, BigInteger expected )
		{

			assertEquals( expected, BI.max(a,b,true) );

		}

		@CsvSource(value={
			"1000000000000000000,null,1000000000000000000",
			"1000000000000000000,100000000000000000000,100000000000000000000",
			"1500000000000000000,1500000000000000000,1500000000000000000",
			"1000000000000000000,-100000000000000000000,1000000000000000000"
		},nullValues="null")
		@ParameterizedTest(name="BI.max({0},{1}) == {2}")
		public void method_max_long_BigInteger(
			long a, BigInteger b, BigInteger expected )
		{

			assertEquals( expected, BI.max(a,b) );

		}

		@CsvSource(value={
			"1000000000000000000,null,1000000000000000000",
			"1000000000000000000,100000000000000000000,100000000000000000000",
			"1500000000000000000,1500000000000000000,1500000000000000000",
			"1000000000000000000,-100000000000000000000,1000000000000000000"
		},nullValues="null")
		@ParameterizedTest(name="BI.max({0},{1},false) == {2}")
		public void method_max_long_BigInteger_false(
			long a, BigInteger b, BigInteger expected )
		{

			assertEquals( expected, BI.max(a,b,false) );

		}

		@CsvSource(value={
			"1000000000000000000,null,null",
			"1000000000000000000,100000000000000000000,100000000000000000000",
			"1500000000000000000,1500000000000000000,1500000000000000000",
			"1000000000000000000,-100000000000000000000,1000000000000000000"
		},nullValues="null")
		@ParameterizedTest(name="BI.max({0},{1},true) == {2}")
		public void method_max_long_BigInteger_true(
			long a, BigInteger b, BigInteger expected )
		{

			assertEquals( expected, BI.max(a,b,true) );

		}

		@CsvSource(value={
			"1000000000000000000,2000000000000000000,2000000000000000000",
			"1500000000000000000,1500000000000000000,1500000000000000000",
			"2000000000000000000,1000000000000000000,2000000000000000000"
		},nullValues="null")
		@ParameterizedTest(name="BI.max({0},{1}) == {2}")
		public void method_max_long_long(
			long a, long b, BigInteger expected )
		{

			assertEquals( expected, BI.max(a,b) );

		}

	}


	/**
	 * Performs unit test on method {@code lt}.
	 */
	@Nested
	@DisplayName("on invoking method 'lt'")
	public class Lt
	{

		@CsvSource(value={
			"null,1000000000000000000,true",
			"-100000000000000000000,1000000000000000000,true",
			"1500000000000000000,1500000000000000000,false",
			"100000000000000000000,1000000000000000000,false"
		},nullValues="null")
		@ParameterizedTest(name="BI.lt({0},{1}) == {2}")
		public void method_lt_BigInteger_long(
			BigInteger a, long b, boolean expected )
		{

			assertEquals( expected, BI.lt(a,b) );

		}

		@CsvSource(value={
			"null,1000,true",
			"-100000000000000000000,1000000000000000000,true",
			"1500000000000000000,1500000000000000000,false",
			"100000000000000000000,1000000000000000000,false"
		},nullValues="null")
		@ParameterizedTest(name="BI.lt({0},{1},false) == {2}")
		public void method_lt_BigInteger_long_false(
				BigInteger a, long b, boolean expected )
		{

			assertEquals( expected, BI.lt(a,b,false) );

		}

		@CsvSource(value={
			"null,1000000000000000000,false",
			"-100000000000000000000,1000000000000000000,true",
			"1500000000000000000,1500000000000000000,false",
			"100000000000000000000,1000000000000000000,false"
		},nullValues="null")
		@ParameterizedTest(name="BI.lt({0},{1},true) == {2}")
		public void method_lt_BigInteger_long_true(
				BigInteger a, long b, boolean expected )
		{

			assertEquals( expected, BI.lt(a,b,true) );

		}

		@CsvSource(value={
			"1000000000000000000,null,false",
			"1000000000000000000,100000000000000000000,true",
			"1500000000000000000,1500000000000000000,false",
			"1000000000000000000,-100000000000000000000,false"
		},nullValues="null")
		@ParameterizedTest(name="BI.lt({0},{1}) == {2}")
		public void method_lt_long_BigInteger(
				long a, BigInteger b, boolean expected )
		{

			assertEquals( expected, BI.lt(a,b) );

		}

		@CsvSource(value={
			"1000000000000000000,null,false",
			"1000000000000000000,100000000000000000000,true",
			"1500000000000000000,1500000000000000000,false",
			"1000000000000000000,-100000000000000000000,false"
		},nullValues="null")
		@ParameterizedTest(name="BI.lt({0},{1},false) == {2}")
		public void method_lt_long_BigInteger_false(
				long a, BigInteger b, boolean expected )
		{

			assertEquals( expected, BI.lt(a,b,false) );

		}

		@CsvSource(value={
			"1000000000000000000,null,true",
			"1000000000000000000,100000000000000000000,true",
			"1500000000000000000,1500000000000000000,false",
			"1000000000000000000,-100000000000000000000,false"
		},nullValues="null")
		@ParameterizedTest(name="BI.lt({0},{1},true) == {2}")
		public void method_lt_long_BigInteger_true(
				long a, BigInteger b, boolean expected )
		{

			assertEquals( expected, BI.lt(a,b,true) );

		}

	}


	/**
	 * Performs unit test on method {@code le}.
	 */
	@Nested
	@DisplayName("on invoking method 'le'")
	public class Le
	{

		@CsvSource(value={
			"null,1000000000000000000,true",
			"-100000000000000000000,1000000000000000000,true",
			"1500000000000000000,1500000000000000000,true",
			"100000000000000000000,1000000000000000000,false"
		},nullValues="null")
		@ParameterizedTest(name="BI.le({0},{1}) == {2}")
		public void method_le_BigInteger_long(
			BigInteger a, long b, boolean expected )
		{

			assertEquals( expected, BI.le(a,b) );

		}

		@CsvSource(value={
			"null,1000,true",
			"-100000000000000000000,1000000000000000000,true",
			"1500000000000000000,1500000000000000000,true",
			"100000000000000000000,1000000000000000000,false"
		},nullValues="null")
		@ParameterizedTest(name="BI.le({0},{1},false) == {2}")
		public void method_le_BigInteger_long_false(
			BigInteger a, long b, boolean expected )
		{

			assertEquals( expected, BI.le(a,b,false) );

		}

		@CsvSource(value={
			"null,1000000000000000000,false",
			"-100000000000000000000,1000000000000000000,true",
			"1500000000000000000,1500000000000000000,true",
			"100000000000000000000,1000000000000000000,false"
		},nullValues="null")
		@ParameterizedTest(name="BI.le({0},{1},true) == {2}")
		public void method_le_BigInteger_long_true(
			BigInteger a, long b, boolean expected )
		{

			assertEquals( expected, BI.le(a,b,true) );

		}

		@CsvSource(value={
			"1000000000000000000,null,false",
			"1000000000000000000,100000000000000000000,true",
			"1500000000000000000,1500000000000000000,true",
			"1000000000000000000,-100000000000000000000,false"
		},nullValues="null")
		@ParameterizedTest(name="BI.le({0},{1}) == {2}")
		public void method_le_long_BigInteger(
			long a, BigInteger b, boolean expected )
		{

			assertEquals( expected, BI.le(a,b) );

		}

		@CsvSource(value={
			"1000000000000000000,null,false",
			"1000000000000000000,100000000000000000000,true",
			"1500000000000000000,1500000000000000000,true",
			"1000000000000000000,-100000000000000000000,false"
		},nullValues="null")
		@ParameterizedTest(name="BI.le({0},{1},false) == {2}")
		public void method_le_long_BigInteger_false(
			long a, BigInteger b, boolean expected )
		{

			assertEquals( expected, BI.le(a,b,false) );

		}

		@CsvSource(value={
			"1000000000000000000,null,true",
			"1000000000000000000,100000000000000000000,true",
			"1500000000000000000,1500000000000000000,true",
			"1000000000000000000,-100000000000000000000,false"
		},nullValues="null")
		@ParameterizedTest(name="BI.le({0},{1},true) == {2}")
		public void method_le_long_BigInteger_true(
			long a, BigInteger b, boolean expected )
		{

			assertEquals( expected, BI.le(a,b,true) );

		}

	}


	/**
	 * Performs unit test on method {@code eq}.
	 */
	@Nested
	@DisplayName("on invoking method 'eq'")
	public class Eq
	{

		@CsvSource(value={
			"null,1000000000000000000,false",
			"-100000000000000000000,1000000000000000000,false",
			"1500000000000000000,1500000000000000000,true",
			"100000000000000000000,1000000000000000000,false"
		},nullValues="null")
		@ParameterizedTest(name="BI.eq({0},{1}) == {2}")
		public void method_eq_BigInteger_long(
				BigInteger a, long b, boolean expected )
		{

			assertEquals( expected, BI.eq(a,b) );

		}

		@CsvSource(value={
			"null,1000,false",
			"-100000000000000000000,1000000000000000000,false",
			"1500000000000000000,1500000000000000000,true",
			"100000000000000000000,1000000000000000000,false"
		},nullValues="null")
		@ParameterizedTest(name="BI.eq({0},{1},false) == {2}")
		public void method_eq_BigInteger_long_false(
				BigInteger a, long b, boolean expected )
		{

			assertEquals( expected, BI.eq(a,b,false) );

		}

		@CsvSource(value={
			"null,1000000000000000000,false",
			"-100000000000000000000,1000000000000000000,false",
			"1500000000000000000,1500000000000000000,true",
			"100000000000000000000,1000000000000000000,false"
		},nullValues="null")
		@ParameterizedTest(name="BI.eq({0},{1},true) == {2}")
		public void method_eq_BigInteger_long_true(
				BigInteger a, long b, boolean expected )
		{

			assertEquals( expected, BI.eq(a,b,true) );

		}

		@CsvSource(value={
			"1000000000000000000,null,false",
			"1000000000000000000,100000000000000000000,false",
			"1500000000000000000,1500000000000000000,true",
			"1000000000000000000,-100000000000000000000,false"
		},nullValues="null")
		@ParameterizedTest(name="BI.eq({0},{1}) == {2}")
		public void method_eq_long_BigInteger(
				long a, BigInteger b, boolean expected )
		{

			assertEquals( expected, BI.eq(a,b) );

		}

		@CsvSource(value={
			"1000000000000000000,null,false",
			"1000000000000000000,100000000000000000000,false",
			"1500000000000000000,1500000000000000000,true",
			"1000000000000000000,-100000000000000000000,false"
		},nullValues="null")
		@ParameterizedTest(name="BI.eq({0},{1},false) == {2}")
		public void method_eq_long_BigInteger_false(
				long a, BigInteger b, boolean expected )
		{

			assertEquals( expected, BI.eq(a,b,false) );

		}

		@CsvSource(value={
			"1000000000000000000,null,false",
			"1000000000000000000,100000000000000000000,false",
			"1500000000000000000,1500000000000000000,true",
			"1000000000000000000,-100000000000000000000,false"
		},nullValues="null")
		@ParameterizedTest(name="BI.eq({0},{1},true) == {2}")
		public void method_eq_long_BigInteger_true(
				long a, BigInteger b, boolean expected )
		{

			assertEquals( expected, BI.eq(a,b,true) );

		}

	}


	/**
	 * Performs unit test on method {@code ne}.
	 */
	@Nested
	@DisplayName("on invoking method 'ne'")
	public class Ne
	{

		@CsvSource(value={
			"null,1000000000000000000,true",
			"-100000000000000000000,1000000000000000000,true",
			"1500000000000000000,1500000000000000000,false",
			"100000000000000000000,1000000000000000000,true"
		},nullValues="null")
		@ParameterizedTest(name="BI.ne({0},{1}) == {2}")
		public void method_ne_BigInteger_long(
				BigInteger a, long b, boolean expected )
		{

			assertEquals( expected, BI.ne(a,b) );

		}

		@CsvSource(value={
			"null,1000,true",
			"-100000000000000000000,1000000000000000000,true",
			"1500000000000000000,1500000000000000000,false",
			"100000000000000000000,1000000000000000000,true"
		},nullValues="null")
		@ParameterizedTest(name="BI.ne({0},{1},false) == {2}")
		public void method_ne_BigInteger_long_false(
				BigInteger a, long b, boolean expected )
		{

			assertEquals( expected, BI.ne(a,b,false) );

		}

		@CsvSource(value={
			"null,1000000000000000000,true",
			"-100000000000000000000,1000000000000000000,true",
			"1500000000000000000,1500000000000000000,false",
			"100000000000000000000,1000000000000000000,true"
		},nullValues="null")
		@ParameterizedTest(name="BI.ne({0},{1},true) == {2}")
		public void method_ne_BigInteger_long_true(
				BigInteger a, long b, boolean expected )
		{

			assertEquals( expected, BI.ne(a,b,true) );

		}

		@CsvSource(value={
			"1000000000000000000,null,true",
			"1000000000000000000,100000000000000000000,true",
			"1500000000000000000,1500000000000000000,false",
			"1000000000000000000,-100000000000000000000,true"
		},nullValues="null")
		@ParameterizedTest(name="BI.ne({0},{1}) == {2}")
		public void method_ne_long_BigInteger(
				long a, BigInteger b, boolean expected )
		{

			assertEquals( expected, BI.ne(a,b) );

		}

		@CsvSource(value={
			"1000000000000000000,null,true",
			"1000000000000000000,100000000000000000000,true",
			"1500000000000000000,1500000000000000000,false",
			"1000000000000000000,-100000000000000000000,true"
		},nullValues="null")
		@ParameterizedTest(name="BI.ne({0},{1},false) == {2}")
		public void method_ne_long_BigInteger_false(
				long a, BigInteger b, boolean expected )
		{

			assertEquals( expected, BI.ne(a,b,false) );

		}

		@CsvSource(value={
			"1000000000000000000,null,true",
			"1000000000000000000,100000000000000000000,true",
			"1500000000000000000,1500000000000000000,false",
			"1000000000000000000,-100000000000000000000,true"
		},nullValues="null")
		@ParameterizedTest(name="BI.ne({0},{1},true) == {2}")
		public void method_ne_long_BigInteger_true(
				long a, BigInteger b, boolean expected )
		{

			assertEquals( expected, BI.ne(a,b,true) );

		}

	}


	/**
	 * Performs unit test on method {@code ge}.
	 */
	@Nested
	@DisplayName("on invoking method 'ge'")
	public class Ge
	{

		@CsvSource(value={
			"null,1000000000000000000,false",
			"-100000000000000000000,1000000000000000000,false",
			"1500000000000000000,1500000000000000000,true",
			"100000000000000000000,1000000000000000000,true"
		},nullValues="null")
		@ParameterizedTest(name="BI.ge({0},{1}) == {2}")
		public void method_ge_BigInteger_long(
				BigInteger a, long b, boolean expected )
		{

			assertEquals( expected, BI.ge(a,b) );

		}

		@CsvSource(value={
			"null,1000,false",
			"-100000000000000000000,1000000000000000000,false",
			"1500000000000000000,1500000000000000000,true",
			"100000000000000000000,1000000000000000000,true"
		},nullValues="null")
		@ParameterizedTest(name="BI.ge({0},{1},false) == {2}")
		public void method_ge_BigInteger_long_false(
				BigInteger a, long b, boolean expected )
		{

			assertEquals( expected, BI.ge(a,b,false) );

		}

		@CsvSource(value={
			"null,1000000000000000000,true",
			"-100000000000000000000,1000000000000000000,false",
			"1500000000000000000,1500000000000000000,true",
			"100000000000000000000,1000000000000000000,true"
		},nullValues="null")
		@ParameterizedTest(name="BI.ge({0},{1},true) == {2}")
		public void method_ge_BigInteger_long_true(
				BigInteger a, long b, boolean expected )
		{

			assertEquals( expected, BI.ge(a,b,true) );

		}

		@CsvSource(value={
			"1000000000000000000,null,true",
			"1000000000000000000,100000000000000000000,false",
			"1500000000000000000,1500000000000000000,true",
			"1000000000000000000,-100000000000000000000,true"
		},nullValues="null")
		@ParameterizedTest(name="BI.ge({0},{1}) == {2}")
		public void method_ge_long_BigInteger(
				long a, BigInteger b, boolean expected )
		{

			assertEquals( expected, BI.ge(a,b) );

		}

		@CsvSource(value={
			"1000000000000000000,null,true",
			"1000000000000000000,100000000000000000000,false",
			"1500000000000000000,1500000000000000000,true",
			"1000000000000000000,-100000000000000000000,true"
		},nullValues="null")
		@ParameterizedTest(name="BI.ge({0},{1},false) == {2}")
		public void method_ge_long_BigInteger_false(
				long a, BigInteger b, boolean expected )
		{

			assertEquals( expected, BI.ge(a,b,false) );

		}

		@CsvSource(value={
			"1000000000000000000,null,false",
			"1000000000000000000,100000000000000000000,false",
			"1500000000000000000,1500000000000000000,true",
			"1000000000000000000,-100000000000000000000,true"
		},nullValues="null")
		@ParameterizedTest(name="BI.ge({0},{1},true) == {2}")
		public void method_ge_long_BigInteger_true(
				long a, BigInteger b, boolean expected )
		{

			assertEquals( expected, BI.ge(a,b,true) );

		}

	}


	/**
	 * Performs unit test on method {@code gt}.
	 */
	@Nested
	@DisplayName("on invoking method 'gt'")
	public class Gt
	{

		@CsvSource(value={
			"null,1000000000000000000,false",
			"-100000000000000000000,1000000000000000000,false",
			"1500000000000000000,1500000000000000000,false",
			"100000000000000000000,1000000000000000000,true"
		},nullValues="null")
		@ParameterizedTest(name="BI.gt({0},{1}) == {2}")
		public void method_gt_BigInteger_long(
				BigInteger a, long b, boolean expected )
		{

			assertEquals( expected, BI.gt(a,b) );

		}

		@CsvSource(value={
			"null,1000,false",
			"-100000000000000000000,1000000000000000000,false",
			"1500000000000000000,1500000000000000000,false",
			"100000000000000000000,1000000000000000000,true"
		},nullValues="null")
		@ParameterizedTest(name="BI.gt({0},{1},false) == {2}")
		public void method_gt_BigInteger_long_false(
				BigInteger a, long b, boolean expected )
		{

			assertEquals( expected, BI.gt(a,b,false) );

		}

		@CsvSource(value={
			"null,1000000000000000000,true",
			"-100000000000000000000,1000000000000000000,false",
			"1500000000000000000,1500000000000000000,false",
			"100000000000000000000,1000000000000000000,true"
		},nullValues="null")
		@ParameterizedTest(name="BI.gt({0},{1},true) == {2}")
		public void method_gt_BigInteger_long_true(
				BigInteger a, long b, boolean expected )
		{

			assertEquals( expected, BI.gt(a,b,true) );

		}

		@CsvSource(value={
			"1000000000000000000,null,true",
			"1000000000000000000,100000000000000000000,false",
			"1500000000000000000,1500000000000000000,false",
			"1000000000000000000,-100000000000000000000,true"
		},nullValues="null")
		@ParameterizedTest(name="BI.gt({0},{1}) == {2}")
		public void method_gt_long_BigInteger(
				long a, BigInteger b, boolean expected )
		{

			assertEquals( expected, BI.gt(a,b) );

		}

		@CsvSource(value={
			"1000000000000000000,null,true",
			"1000000000000000000,100000000000000000000,false",
			"1500000000000000000,1500000000000000000,false",
			"1000000000000000000,-100000000000000000000,true"
		},nullValues="null")
		@ParameterizedTest(name="BI.gt({0},{1},false) == {2}")
		public void method_gt_long_BigInteger_false(
				long a, BigInteger b, boolean expected )
		{

			assertEquals( expected, BI.gt(a,b,false) );

		}

		@CsvSource(value={
			"1000000000000000000,null,false",
			"1000000000000000000,100000000000000000000,false",
			"1500000000000000000,1500000000000000000,false",
			"1000000000000000000,-100000000000000000000,true"
		},nullValues="null")
		@ParameterizedTest(name="BI.gt({0},{1},true) == {2}")
		public void method_gt_long_BigInteger_true(
				long a, BigInteger b, boolean expected )
		{

			assertEquals( expected, BI.gt(a,b,true) );

		}

	}


	/**
	 * Performs unit test on method {@code apply}.
	 */
	@Nested
	@DisplayName("on invoking method 'apply'")
	public class Apply
	{

		@CsvSource(value = {
			"null,null,null",
			"null,100000000000000000000,100000000000000000000",
			"100000000000000000000,null,100000000000000000000",
		}, nullValues = "null")
		@ParameterizedTest(name = "BI.apply(null,{0},{1}) == {2}")
		public void if_one_argument_is_null_the_other_is_returned(
			BigInteger a, BigInteger b, BigInteger expected
		)
		{

			assertEquals( expected, BI.apply(null, a, b) );

		}

		@Test
		public void if_the_operator_is_null_a_RequirementFailure_is_thrown()
		{

			final BigInteger a = BI.of( "100000000000000000000" );
			final BigInteger b = BI.of( "100000000000000000000" );
			assertThrows(
				RequirementFailure.class,
				() -> BI.apply( null, a, b )
			);

		}


		@MethodSource("org.nerd4j.utils.math.BITests#operators")
		@ParameterizedTest(name = "BI.apply({0},a,b) == expected")
		public void if_all_arguments_are_not_null_should_return_as_expected(
			BinaryOperator<BigInteger> operator
		)
		{

			assertNotNull(operator);

			final BigInteger a = BI.of( "100000000000000000000" );
			final BigInteger b = BI.of( "100000000000000000000" );

			assertAll(

				() -> assertSame(null, BI.apply(operator, null, null)),
				() -> assertSame(a, BI.apply(operator, a, null)),
				() -> assertSame(b, BI.apply(operator, null, b)),
				() -> assertEquals(operator.apply(a, b), BI.apply(operator, a, b))

			);

		}

	}

	public static Stream<BinaryOperator<BigInteger>> operators()
	{

		return Stream.of(
			BI.SUM,
			BI.SUB,
			BI.MUL,
			BI.DIV,
			BI.REM,
			BI.MOD,
			BI.AND,
			BI.OR,
			BI.XOR,
			BI.MIN,
			BI.MAX
		);
	}

}

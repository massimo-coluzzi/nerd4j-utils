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

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.nerd4j.utils.lang.RequirementFailure;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.BinaryOperator;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for the utility class {@link BD}.
 *
 * @author Massimo Coluzzi
 */
@DisplayName("Testing utility class: BD")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class BDTests
{

	/**
	 * Performs unit test on method {@code of}.
	 */
	@Nested
	@DisplayName("on invoking method 'of'")
	public class Of
	{

		@ValueSource(doubles={
			Double.NEGATIVE_INFINITY,
			Double.NaN,
			Double.POSITIVE_INFINITY
		})
		@ParameterizedTest(name="BD.of({0}) == {0}")
		public void method_of_NaN( double source )
		{

			assertThrows(
				NumberFormatException.class,
				() -> BD.of( source )
			);

		}
		@ValueSource(doubles={
			Double.MIN_VALUE,
			Double.MIN_NORMAL,
			Double.MAX_VALUE
		})
		@ParameterizedTest(name="BD.of({0}) == {0}")
		public void method_of_double( double source )
		{

			final BigDecimal target = BD.of( source );
			assertEquals( source, target.doubleValue() );

		}

		@ValueSource(longs={Long.MIN_VALUE,-1,0,1,Long.MAX_VALUE})
		@ParameterizedTest(name="BD.of({0}) == {0}")
		public void method_of_long( long source )
		{

			final BigDecimal target = BD.of( source );
			assertEquals( source, target.longValue() );

		}

		@NullAndEmptySource()
		@ParameterizedTest(name="BD.of({0}) == null")
		public void method_of_empty_string( String source )
		{

			final BigDecimal target = BD.of( source );
			assertNull( target );

		}

		@ValueSource(strings={
			"92233720368547758089223372036854775808.9223372036854775808",
		    "-92233720368547758089223372036854775808.9223372036854775808"
		})
		@ParameterizedTest(name="BD.of({0}) == {0}")
		public void method_of_valued_string( String source )
		{

			final BigDecimal target = BD.of( source );
			assertEquals( source, target.toString() );

		}

		@ValueSource(strings={
			" ", "\t", "0-0", "BigDecimal"
		})
		@ParameterizedTest(name="BD.of({0}) => NumberFormatException")
		public void method_of_wrong_valued_string( String source )
		{

			assertThrows(
				NumberFormatException.class,
				() -> BD.of( source )
			);

		}


		@NullSource()
		@ParameterizedTest(name="BD.of({0}) == null")
		public void method_of_null_BigInteger( BigInteger source )
		{

			final BigDecimal target = BD.of( source );
			assertNull( target );

		}


		@CsvSource({
			"92233720368547758089223372036854775808",
			"-92233720368547758089223372036854775808"
		})
		@ParameterizedTest(name="BD.of({0}) == {0}")
		public void method_of_BigInteger( BigInteger source )
		{

			final BigDecimal target = BD.of( source );
			assertEquals( source.toString(), target.toString() );

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
			"null,1000.01,1000.01",
			"1000.01,null,1000.01",
			"10000000000000000000.01,10000000000000000000.01,20000000000000000000.02"
		},nullValues="null")
		@ParameterizedTest(name="BD.sum({0},{1}) == {2}")
		public void method_sum_BigDecimal_BigDecimal(
			BigDecimal a, BigDecimal b, BigDecimal expected )
		{

			assertEquals( expected, BD.sum(a,b) );

		}

		@CsvSource(value={
			"null,1000.01,1000.01",
			"10000000000000000000.01,1000.01,10000000000000001000.02"
		},nullValues="null")
		@ParameterizedTest(name="BD.sum({0},{1}) == {2}")
		public void method_sum_BigDecimal_double(
			BigDecimal a, double b, BigDecimal expected )
		{

			assertEquals( expected, BD.sum(a,b) );

		}

		@CsvSource(value={
			"1000.01,null,1000.01",
			"1000.01,10000000000000000000.01,10000000000000001000.02"
		},nullValues="null")
		@ParameterizedTest(name="BD.sum({0},{1}) == {2}")
		public void method_sum_double_BigDecimal(
			double a, BigDecimal b, BigDecimal expected )
		{

			assertEquals( expected, BD.sum(a,b) );

		}

		@Test
		public void method_sum_double_double()
		{

			final double a = 9000000000000.01d;
			final double b = 9000000000000.01d;
			final BigDecimal expected = BD.of( "18000000000000.02" );

			assertEquals( expected, BD.sum(a,b) );

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
			"null,1000.01,1000.01",
			"1000.01,null,1000.01",
			"30000000000000000000.03,10000000000000000000.01,20000000000000000000.02"
		},nullValues="null")
		@ParameterizedTest(name="BD.sub({0},{1}) == {2}")
		public void method_sub_BigDecimal_BigDecimal(
			BigDecimal a, BigDecimal b, BigDecimal expected )
		{

			assertEquals( expected, BD.sub(a,b) );

		}

		@CsvSource(value={
			"null,1000.01,1000.01",
			"10000000000000000000.02,1000.01,9999999999999999000.01"
		},nullValues="null")
		@ParameterizedTest(name="BD.sub({0},{1}) == {2}")
		public void method_sub_BigDecimal_double(
			BigDecimal a, double b, BigDecimal expected )
		{

			assertEquals( expected, BD.sub(a,b) );

		}

		@CsvSource(value={
			"1000.01,null,1000.01",
			"1000.00,10000000000000000000.01,-9999999999999999000.01"
		},nullValues="null")
		@ParameterizedTest(name="BD.sub({0},{1}) == {2}")
		public void method_sub_double_BigDecimal(
			double a, BigDecimal b, BigDecimal expected )
		{

			assertEquals( expected, BD.sub(a,b) );

		}

		@Test
		public void method_sub_double_double()
		{

			final double a = -9000000000000.01d;
			final double b =  9000000000000.01d;
			final BigDecimal expected = BD.of( "-18000000000000.02" );

			assertEquals( expected, BD.sub(a,b) );

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
			"null,1000.01,1000.01",
			"1000.01,null,1000.01",
			"10000000000000000000.01,10000000000000000000.00,100000000000000000000100000000000000000.0000"
		},nullValues="null")
		@ParameterizedTest(name="BD.mul({0},{1}) == {2}")
		public void method_mul_BigDecimal_BigDecimal(
			BigDecimal a, BigDecimal b, BigDecimal expected )
		{

			assertEquals( expected, BD.mul(a,b) );

		}

		@CsvSource(value={
			"null,1000.01,1000.01",
			"10000000000000000000.00,1000.01,10000100000000000000000.0000"
		},nullValues="null")
		@ParameterizedTest(name="BD.mul({0},{1}) == {2}")
		public void method_mul_BigDecimal_double(
			BigDecimal a, double b, BigDecimal expected )
		{

			assertEquals( expected, BD.mul(a,b) );

		}

		@CsvSource(value={
			"1000.01,null,1000.01",
			"1000.01,10000000000000000000.00,10000100000000000000000.0000"
		},nullValues="null")
		@ParameterizedTest(name="BD.mul({0},{1}) == {2}")
		public void method_mul_double_BigDecimal(
			double a, BigDecimal b, BigDecimal expected )
		{

			assertEquals( expected, BD.mul(a,b) );

		}

		@Test
		public void method_mul_double_double()
		{

			final double a = 1000000000000.01d;
			final double b = 1000000000000.01d;
			final BigDecimal expected = BD.of( "1000000000000020000000000.0001" );

			assertEquals( expected, BD.mul(a,b) );

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
			"null,1000.01,1000.01",
			"1000.01,null,1000.01",
			"10000000000000000000.01,10000000000000000000.01,1"
		},nullValues="null")
		@ParameterizedTest(name="BD.div({0},{1}) == {2}")
		public void method_div_BigDecimal_BigDecimal(
			BigDecimal a, BigDecimal b, BigDecimal expected )
		{

			assertEquals( expected, BD.div(a,b) );

		}

		@CsvSource(value={
			"null,1000.01,1000.01",
			"10000000000000000000.01,1000,10000000000000000.00001"
		},nullValues="null")
		@ParameterizedTest(name="BD.div({0},{1}) == {2}")
		public void method_div_BigDecimal_double(
			BigDecimal a, double b, BigDecimal expected )
		{

			assertEquals( expected, BD.div(a,b) );

		}

		@CsvSource(value={
			"1000.01,null,1000.01",
			"1000.01,10000000000000000000,0.000000000000000100001"
		},nullValues="null")
		@ParameterizedTest(name="BD.div({0},{1}) == {2}")
		public void method_div_double_BigDecimal(
			double a, BigDecimal b, BigDecimal expected )
		{

			assertEquals( expected, BD.div(a,b) );

		}

		@Test
		public void method_div_double_double()
		{

			final double a = 1000000000000.00d;
			final double b = 2000000000000.00d;
			final BigDecimal expected = BD.of( 0.5d );

			assertEquals( expected, BD.div(a,b) );

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
			"null,1000.01,1000.01",
			"1000.01,null,1000.01",
			"-1000000000000000000.10,10000000000000000.00,-0.10"
		},nullValues="null")
		@ParameterizedTest(name="BD.rem({0},{1}) == {2}")
		public void method_rem_BigDecimal_BigDecimal(
			BigDecimal a, BigDecimal b, BigDecimal expected )
		{

			assertEquals( expected, BD.rem(a,b) );

		}

		@CsvSource(value={
			"null,1000.01,1000.01",
			"-100000000000000000.10,1000,-0.10"
		},nullValues="null")
		@ParameterizedTest(name="BD.rem({0},{1}) == {2}")
		public void method_rem_BigDecimal_double(
			BigDecimal a, double b, BigDecimal expected )
		{

			assertEquals( expected, BD.rem(a,b) );

		}

		@CsvSource(value={
			"1000.01,null,1000.01",
			"-1000.01,10000000000000000000,-1000.01"
		},nullValues="null")
		@ParameterizedTest(name="BD.rem({0},{1}) == {2}")
		public void method_rem_double_BigDecimal(
			double a, BigDecimal b, BigDecimal expected )
		{

			assertEquals( expected, BD.rem(a,b) );

		}

		@Test
		public void method_rem_double_double()
		{

			final double a = -2000000000000.01d;
			final double b =  1000000000000.00d;
			final BigDecimal expected = BD.of( -0.01d );

			assertEquals( expected, BD.rem(a,b) );

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
			"null,1000000000000.01,null",
			"1000000000000.01,1000000000000.03,1000000000000.01",
			"1000000000000.02,1000000000000.02,1000000000000.02",
			"1000000000000.03,1000000000000.01,1000000000000.01"
		},nullValues="null")
		@ParameterizedTest(name="BD.min({0},{1}) == {2}")
		public void method_min_BigDecimal_double(
			BigDecimal a, double b, BigDecimal expected )
		{

			assertEquals( expected, BD.min(a,b) );

		}

		@CsvSource(value={
			"null,1000000000000.01,null",
			"1000000000000.01,1000000000000.03,1000000000000.01",
			"1000000000000.02,1000000000000.02,1000000000000.02",
			"1000000000000.03,1000000000000.01,1000000000000.01"
		},nullValues="null")
		@ParameterizedTest(name="BD.min({0},{1},false) == {2}")
		public void method_min_BigDecimal_double_false(
			BigDecimal a, double b, BigDecimal expected )
		{

			assertEquals( expected, BD.min(a,b,false) );

		}


		@CsvSource(value={
			"null,1000000000000.01,1000000000000.01",
			"1000000000000.01,1000000000000.03,1000000000000.01",
			"1000000000000.02,1000000000000.02,1000000000000.02",
			"1000000000000.03,1000000000000.01,1000000000000.01"
		},nullValues="null")
		@ParameterizedTest(name="BD.min({0},{1},true) == {2}")
		public void method_min_BigDecimal_double_true(
			BigDecimal a, double b, BigDecimal expected )
		{

			assertEquals( expected, BD.min(a,b,true) );

		}

		@CsvSource(value={
			"1000000000000.01,null,null",
			"1000000000000.01,1000000000000.03,1000000000000.01",
			"1000000000000.02,1000000000000.02,1000000000000.02",
			"1000000000000.03,1000000000000.01,1000000000000.01"
		},nullValues="null")
		@ParameterizedTest(name="BD.min({0},{1}) == {2}")
		public void method_min_double_BigDecimal(
			double a, BigDecimal b, BigDecimal expected )
		{

			assertEquals( expected, BD.min(a,b) );

		}

		@CsvSource(value={
			"1000000000000.01,null,null",
			"1000000000000.01,1000000000000.03,1000000000000.01",
			"1000000000000.02,1000000000000.02,1000000000000.02",
			"1000000000000.03,1000000000000.01,1000000000000.01"
		},nullValues="null")
		@ParameterizedTest(name="BD.min({0},{1},false) == {2}")
		public void method_min_double_BigDecimal_false(
			double a, BigDecimal b, BigDecimal expected )
		{

			assertEquals( expected, BD.min(a,b,false) );

		}

		@CsvSource(value={
			"1000000000000.01,null,1000000000000.01",
			"1000000000000.01,1000000000000.03,1000000000000.01",
			"1000000000000.02,1000000000000.02,1000000000000.02",
			"1000000000000.03,1000000000000.01,1000000000000.01"
		},nullValues="null")
		@ParameterizedTest(name="BD.min({0},{1},true) == {2}")
		public void method_min_double_BigDecimal_true(
			double a, BigDecimal b, BigDecimal expected )
		{

			assertEquals( expected, BD.min(a,b,true) );

		}


		@CsvSource(value={
			"1000000000000.01,1000000000000.03,1000000000000.01",
			"1000000000000.02,1000000000000.02,1000000000000.02",
			"1000000000000.03,1000000000000.01,1000000000000.01"
		},nullValues="null")
		@ParameterizedTest(name="BD.min({0},{1}) == {2}")
		public void method_min_double_double(
			double a, double b, BigDecimal expected )
		{

			assertEquals( expected, BD.min(a,b) );

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
			"null,1000000000000.01,1000000000000.01",
			"1000000000000.01,1000000000000.03,1000000000000.03",
			"1000000000000.02,1000000000000.02,1000000000000.02",
			"1000000000000.03,1000000000000.01,1000000000000.03"
		},nullValues="null")
		@ParameterizedTest(name="BD.max({0},{1}) == {2}")
		public void method_max_BigDecimal_double(
			BigDecimal a, double b, BigDecimal expected )
		{

			assertEquals( expected, BD.max(a,b) );

		}

		@CsvSource(value={
			"null,1000000000000.01,1000000000000.01",
			"1000000000000.01,1000000000000.03,1000000000000.03",
			"1000000000000.02,1000000000000.02,1000000000000.02",
			"1000000000000.03,1000000000000.01,1000000000000.03"
		},nullValues="null")
		@ParameterizedTest(name="BD.max({0},{1},false) == {2}")
		public void method_max_BigDecimal_double_false(
			BigDecimal a, double b, BigDecimal expected )
		{

			assertEquals( expected, BD.max(a,b,false) );

		}

		@CsvSource(value={
			"null,1000000000000.01,null",
			"1000000000000.01,1000000000000.03,1000000000000.03",
			"1000000000000.02,1000000000000.02,1000000000000.02",
			"1000000000000.03,1000000000000.01,1000000000000.03"
		},nullValues="null")
		@ParameterizedTest(name="BD.max({0},{1},true) == {2}")
		public void method_max_BigDecimal_double_true(
			BigDecimal a, double b, BigDecimal expected )
		{

			assertEquals( expected, BD.max(a,b,true) );

		}

		@CsvSource(value={
			"1000000000000.01,null,1000000000000.01",
			"1000000000000.01,1000000000000.03,1000000000000.03",
			"1000000000000.02,1000000000000.02,1000000000000.02",
			"1000000000000.03,1000000000000.01,1000000000000.03"
		},nullValues="null")
		@ParameterizedTest(name="BD.max({0},{1}) == {2}")
		public void method_max_double_BigDecimal(
			double a, BigDecimal b, BigDecimal expected )
		{

			assertEquals( expected, BD.max(a,b) );

		}

		@CsvSource(value={
			"1000000000000.01,null,1000000000000.01",
			"1000000000000.01,1000000000000.03,1000000000000.03",
			"1000000000000.02,1000000000000.02,1000000000000.02",
			"1000000000000.03,1000000000000.01,1000000000000.03"
		},nullValues="null")
		@ParameterizedTest(name="BD.max({0},{1},false) == {2}")
		public void method_max_double_BigDecimal_false(
			double a, BigDecimal b, BigDecimal expected )
		{

			assertEquals( expected, BD.max(a,b,false) );

		}

		@CsvSource(value={
			"1000000000000.01,null,null",
			"1000000000000.01,1000000000000.03,1000000000000.03",
			"1000000000000.02,1000000000000.02,1000000000000.02",
			"1000000000000.03,1000000000000.01,1000000000000.03"
		},nullValues="null")
		@ParameterizedTest(name="BD.max({0},{1},true) == {2}")
		public void method_max_double_BigDecimal_true(
			double a, BigDecimal b, BigDecimal expected )
		{

			assertEquals( expected, BD.max(a,b,true) );

		}

		@CsvSource(value={
			"1000000000000.01,1000000000000.03,1000000000000.03",
			"1000000000000.02,1000000000000.02,1000000000000.02",
			"1000000000000.03,1000000000000.01,1000000000000.03"
		},nullValues="null")
		@ParameterizedTest(name="BD.max({0},{1}) == {2}")
		public void method_max_double_double(
			double a, double b, BigDecimal expected )
		{

			assertEquals( expected, BD.max(a,b) );

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
			"null,1000000000000.01,true",
			"1000000000000.01,1000000000000.03,true",
			"1000000000000.02,1000000000000.02,false",
			"1000000000000.03,1000000000000.01,false"
		},nullValues="null")
		@ParameterizedTest(name="BD.lt({0},{1}) == {2}")
		public void method_lt_BigDecimal_double(
			BigDecimal a, double b, boolean expected )
		{

			assertEquals( expected, BD.lt(a,b) );

		}

		@CsvSource(value={
			"null,1000000000000.01,true",
			"1000000000000.01,1000000000000.03,true",
			"1000000000000.02,1000000000000.02,false",
			"1000000000000.03,1000000000000.01,false"
		},nullValues="null")
		@ParameterizedTest(name="BD.lt({0},{1},false) == {2}")
		public void method_lt_BigDecimal_double_false(
				BigDecimal a, double b, boolean expected )
		{

			assertEquals( expected, BD.lt(a,b,false) );

		}

		@CsvSource(value={
			"null,1000000000000.01,false",
			"1000000000000.01,1000000000000.03,true",
			"1000000000000.02,1000000000000.02,false",
			"1000000000000.03,1000000000000.01,false"
		},nullValues="null")
		@ParameterizedTest(name="BD.lt({0},{1},true) == {2}")
		public void method_lt_BigDecimal_double_true(
				BigDecimal a, double b, boolean expected )
		{

			assertEquals( expected, BD.lt(a,b,true) );

		}

		@CsvSource(value={
			"1000000000000.01,null,false",
			"1000000000000.01,1000000000000.03,true",
			"1000000000000.02,1000000000000.02,false",
			"1000000000000.03,1000000000000.01,false"
		},nullValues="null")
		@ParameterizedTest(name="BD.lt({0},{1}) == {2}")
		public void method_lt_double_BigDecimal(
				double a, BigDecimal b, boolean expected )
		{

			assertEquals( expected, BD.lt(a,b) );

		}

		@CsvSource(value={
			"1000000000000.01,null,false",
			"1000000000000.01,1000000000000.03,true",
			"1000000000000.02,1000000000000.02,false",
			"1000000000000.03,1000000000000.01,false"
		},nullValues="null")
		@ParameterizedTest(name="BD.lt({0},{1},false) == {2}")
		public void method_lt_double_BigDecimal_false(
				double a, BigDecimal b, boolean expected )
		{

			assertEquals( expected, BD.lt(a,b,false) );

		}

		@CsvSource(value={
			"1000000000000.01,null,true",
			"1000000000000.01,1000000000000.03,true",
			"1000000000000.02,1000000000000.02,false",
			"1000000000000.03,1000000000000.01,false"
		},nullValues="null")
		@ParameterizedTest(name="BD.lt({0},{1},true) == {2}")
		public void method_lt_double_BigDecimal_true(
				double a, BigDecimal b, boolean expected )
		{

			assertEquals( expected, BD.lt(a,b,true) );

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
			"null,1000000000000.01,true",
			"1000000000000.01,1000000000000.03,true",
			"1000000000000.02,1000000000000.02,true",
			"1000000000000.03,1000000000000.01,false"
		},nullValues="null")
		@ParameterizedTest(name="BD.le({0},{1}) == {2}")
		public void method_le_BigDecimal_double(
			BigDecimal a, double b, boolean expected )
		{

			assertEquals( expected, BD.le(a,b) );

		}

		@CsvSource(value={
			"null,1000000000000.01,true",
			"1000000000000.01,1000000000000.03,true",
			"1000000000000.02,1000000000000.02,true",
			"1000000000000.03,1000000000000.01,false"
		},nullValues="null")
		@ParameterizedTest(name="BD.le({0},{1},false) == {2}")
		public void method_le_BigDecimal_double_false(
			BigDecimal a, double b, boolean expected )
		{

			assertEquals( expected, BD.le(a,b,false) );

		}

		@CsvSource(value={
			"null,1000000000000.01,false",
			"1000000000000.01,1000000000000.03,true",
			"1000000000000.02,1000000000000.02,true",
			"1000000000000.03,1000000000000.01,false"
		},nullValues="null")
		@ParameterizedTest(name="BD.le({0},{1},true) == {2}")
		public void method_le_BigDecimal_double_true(
			BigDecimal a, double b, boolean expected )
		{

			assertEquals( expected, BD.le(a,b,true) );

		}

		@CsvSource(value={
			"1000000000000.01,null,false",
			"1000000000000.01,1000000000000.03,true",
			"1000000000000.02,1000000000000.02,true",
			"1000000000000.03,1000000000000.01,false"
		},nullValues="null")
		@ParameterizedTest(name="BD.le({0},{1}) == {2}")
		public void method_le_double_BigDecimal(
			double a, BigDecimal b, boolean expected )
		{

			assertEquals( expected, BD.le(a,b) );

		}

		@CsvSource(value={
			"1000000000000.01,null,false",
			"1000000000000.01,1000000000000.03,true",
			"1000000000000.02,1000000000000.02,true",
			"1000000000000.03,1000000000000.01,false"
		},nullValues="null")
		@ParameterizedTest(name="BD.le({0},{1},false) == {2}")
		public void method_le_double_BigDecimal_false(
			double a, BigDecimal b, boolean expected )
		{

			assertEquals( expected, BD.le(a,b,false) );

		}

		@CsvSource(value={
			"1000000000000.01,null,true",
			"1000000000000.01,1000000000000.03,true",
			"1000000000000.02,1000000000000.02,true",
			"1000000000000.03,1000000000000.01,false"
		},nullValues="null")
		@ParameterizedTest(name="BD.le({0},{1},true) == {2}")
		public void method_le_double_BigDecimal_true(
			double a, BigDecimal b, boolean expected )
		{

			assertEquals( expected, BD.le(a,b,true) );

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
			"null,1000000000000.01,false",
			"1000000000000.01,1000000000000.03,false",
			"1000000000000.02,1000000000000.02,true",
			"1000000000000.03,1000000000000.01,false"
		},nullValues="null")
		@ParameterizedTest(name="BD.eq({0},{1}) == {2}")
		public void method_eq_BigDecimal_double(
				BigDecimal a, double b, boolean expected )
		{

			assertEquals( expected, BD.eq(a,b) );

		}

		@CsvSource(value={
			"null,1000000000000.01,false",
			"1000000000000.01,1000000000000.03,false",
			"1000000000000.02,1000000000000.02,true",
			"1000000000000.03,1000000000000.01,false"
		},nullValues="null")
		@ParameterizedTest(name="BD.eq({0},{1},false) == {2}")
		public void method_eq_BigDecimal_double_false(
				BigDecimal a, double b, boolean expected )
		{

			assertEquals( expected, BD.eq(a,b,false) );

		}

		@CsvSource(value={
			"null,1000000000000.01,false",
			"1000000000000.01,1000000000000.03,false",
			"1000000000000.02,1000000000000.02,true",
			"1000000000000.03,1000000000000.01,false"
		},nullValues="null")
		@ParameterizedTest(name="BD.eq({0},{1},true) == {2}")
		public void method_eq_BigDecimal_double_true(
				BigDecimal a, double b, boolean expected )
		{

			assertEquals( expected, BD.eq(a,b,true) );

		}

		@CsvSource(value={
			"1000000000000.01,null,false",
			"1000000000000.01,1000000000000.03,false",
			"1000000000000.02,1000000000000.02,true",
			"1000000000000.03,1000000000000.01,false"
		},nullValues="null")
		@ParameterizedTest(name="BD.eq({0},{1}) == {2}")
		public void method_eq_double_BigDecimal(
				double a, BigDecimal b, boolean expected )
		{

			assertEquals( expected, BD.eq(a,b) );

		}

		@CsvSource(value={
			"1000000000000.01,null,false",
			"1000000000000.01,1000000000000.03,false",
			"1000000000000.02,1000000000000.02,true",
			"1000000000000.03,1000000000000.01,false"
		},nullValues="null")
		@ParameterizedTest(name="BD.eq({0},{1},false) == {2}")
		public void method_eq_double_BigDecimal_false(
				double a, BigDecimal b, boolean expected )
		{

			assertEquals( expected, BD.eq(a,b,false) );

		}

		@CsvSource(value={
			"1000000000000.01,null,false",
			"1000000000000.01,1000000000000.03,false",
			"1000000000000.02,1000000000000.02,true",
			"1000000000000.03,1000000000000.01,false"
		},nullValues="null")
		@ParameterizedTest(name="BD.eq({0},{1},true) == {2}")
		public void method_eq_double_BigDecimal_true(
				double a, BigDecimal b, boolean expected )
		{

			assertEquals( expected, BD.eq(a,b,true) );

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
			"null,1000000000000.01,true",
			"1000000000000.01,1000000000000.03,true",
			"1000000000000.02,1000000000000.02,false",
			"1000000000000.03,1000000000000.01,true"
		},nullValues="null")
		@ParameterizedTest(name="BD.ne({0},{1}) == {2}")
		public void method_ne_BigDecimal_double(
				BigDecimal a, double b, boolean expected )
		{

			assertEquals( expected, BD.ne(a,b) );

		}

		@CsvSource(value={
			"null,1000000000000.01,true",
			"1000000000000.01,1000000000000.03,true",
			"1000000000000.02,1000000000000.02,false",
			"1000000000000.03,1000000000000.01,true"
		},nullValues="null")
		@ParameterizedTest(name="BD.ne({0},{1},false) == {2}")
		public void method_ne_BigDecimal_double_false(
				BigDecimal a, double b, boolean expected )
		{

			assertEquals( expected, BD.ne(a,b,false) );

		}

		@CsvSource(value={
			"null,1000000000000.01,true",
			"1000000000000.01,1000000000000.03,true",
			"1000000000000.02,1000000000000.02,false",
			"1000000000000.03,1000000000000.01,true"
		},nullValues="null")
		@ParameterizedTest(name="BD.ne({0},{1},true) == {2}")
		public void method_ne_BigDecimal_double_true(
				BigDecimal a, double b, boolean expected )
		{

			assertEquals( expected, BD.ne(a,b,true) );

		}

		@CsvSource(value={
			"1000000000000.01,null,true",
			"1000000000000.01,1000000000000.03,true",
			"1000000000000.02,1000000000000.02,false",
			"1000000000000.03,1000000000000.01,true"
		},nullValues="null")
		@ParameterizedTest(name="BD.ne({0},{1}) == {2}")
		public void method_ne_double_BigDecimal(
				double a, BigDecimal b, boolean expected )
		{

			assertEquals( expected, BD.ne(a,b) );

		}

		@CsvSource(value={
			"1000000000000.01,null,true",
			"1000000000000.01,1000000000000.03,true",
			"1000000000000.02,1000000000000.02,false",
			"1000000000000.03,1000000000000.01,true"
		},nullValues="null")
		@ParameterizedTest(name="BD.ne({0},{1},false) == {2}")
		public void method_ne_double_BigDecimal_false(
				double a, BigDecimal b, boolean expected )
		{

			assertEquals( expected, BD.ne(a,b,false) );

		}

		@CsvSource(value={
			"1000000000000.01,null,true",
			"1000000000000.01,1000000000000.03,true",
			"1000000000000.02,1000000000000.02,false",
			"1000000000000.03,1000000000000.01,true"
		},nullValues="null")
		@ParameterizedTest(name="BD.ne({0},{1},true) == {2}")
		public void method_ne_double_BigDecimal_true(
				double a, BigDecimal b, boolean expected )
		{

			assertEquals( expected, BD.ne(a,b,true) );

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
			"null,1000000000000.01,false",
			"1000000000000.01,1000000000000.03,false",
			"1000000000000.02,1000000000000.02,true",
			"1000000000000.03,1000000000000.01,true"
		},nullValues="null")
		@ParameterizedTest(name="BD.ge({0},{1}) == {2}")
		public void method_ge_BigDecimal_double(
				BigDecimal a, double b, boolean expected )
		{

			assertEquals( expected, BD.ge(a,b) );

		}

		@CsvSource(value={
			"null,1000000000000.01,false",
			"1000000000000.01,1000000000000.03,false",
			"1000000000000.02,1000000000000.02,true",
			"1000000000000.03,1000000000000.01,true"
		},nullValues="null")
		@ParameterizedTest(name="BD.ge({0},{1},false) == {2}")
		public void method_ge_BigDecimal_double_false(
				BigDecimal a, double b, boolean expected )
		{

			assertEquals( expected, BD.ge(a,b,false) );

		}

		@CsvSource(value={
			"null,1000000000000.01,true",
			"1000000000000.01,1000000000000.03,false",
			"1000000000000.02,1000000000000.02,true",
			"1000000000000.03,1000000000000.01,true"
		},nullValues="null")
		@ParameterizedTest(name="BD.ge({0},{1},true) == {2}")
		public void method_ge_BigDecimal_double_true(
				BigDecimal a, double b, boolean expected )
		{

			assertEquals( expected, BD.ge(a,b,true) );

		}

		@CsvSource(value={
			"1000000000000.01,null,true",
			"1000000000000.01,1000000000000.03,false",
			"1000000000000.02,1000000000000.02,true",
			"1000000000000.03,1000000000000.01,true"
		},nullValues="null")
		@ParameterizedTest(name="BD.ge({0},{1}) == {2}")
		public void method_ge_double_BigDecimal(
				double a, BigDecimal b, boolean expected )
		{

			assertEquals( expected, BD.ge(a,b) );

		}

		@CsvSource(value={
			"1000000000000.01,null,true",
			"1000000000000.01,1000000000000.03,false",
			"1000000000000.02,1000000000000.02,true",
			"1000000000000.03,1000000000000.01,true"
		},nullValues="null")
		@ParameterizedTest(name="BD.ge({0},{1},false) == {2}")
		public void method_ge_double_BigDecimal_false(
				double a, BigDecimal b, boolean expected )
		{

			assertEquals( expected, BD.ge(a,b,false) );

		}

		@CsvSource(value={
			"1000000000000.01,null,false",
			"1000000000000.01,1000000000000.03,false",
			"1000000000000.02,1000000000000.02,true",
			"1000000000000.03,1000000000000.01,true"
		},nullValues="null")
		@ParameterizedTest(name="BD.ge({0},{1},true) == {2}")
		public void method_ge_double_BigDecimal_true(
				double a, BigDecimal b, boolean expected )
		{

			assertEquals( expected, BD.ge(a,b,true) );

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
			"null,1000000000000.01,false",
			"1000000000000.01,1000000000000.03,false",
			"1000000000000.02,1000000000000.02,false",
			"1000000000000.03,1000000000000.01,true"
		},nullValues="null")
		@ParameterizedTest(name="BD.gt({0},{1}) == {2}")
		public void method_gt_BigDecimal_double(
				BigDecimal a, double b, boolean expected )
		{

			assertEquals( expected, BD.gt(a,b) );

		}

		@CsvSource(value={
			"null,1000000000000.01,false",
			"1000000000000.01,1000000000000.03,false",
			"1000000000000.02,1000000000000.02,false",
			"1000000000000.03,1000000000000.01,true"
		},nullValues="null")
		@ParameterizedTest(name="BD.gt({0},{1},false) == {2}")
		public void method_gt_BigDecimal_double_false(
				BigDecimal a, double b, boolean expected )
		{

			assertEquals( expected, BD.gt(a,b,false) );

		}

		@CsvSource(value={
			"null,1000000000000.01,true",
			"1000000000000.01,1000000000000.03,false",
			"1000000000000.02,1000000000000.02,false",
			"1000000000000.03,1000000000000.01,true"
		},nullValues="null")
		@ParameterizedTest(name="BD.gt({0},{1},true) == {2}")
		public void method_gt_BigDecimal_double_true(
				BigDecimal a, double b, boolean expected )
		{

			assertEquals( expected, BD.gt(a,b,true) );

		}

		@CsvSource(value={
			"1000000000000.01,null,true",
			"1000000000000.01,1000000000000.03,false",
			"1000000000000.02,1000000000000.02,false",
			"1000000000000.03,1000000000000.01,true"
		},nullValues="null")
		@ParameterizedTest(name="BD.gt({0},{1}) == {2}")
		public void method_gt_double_BigDecimal(
				double a, BigDecimal b, boolean expected )
		{

			assertEquals( expected, BD.gt(a,b) );

		}

		@CsvSource(value={
			"1000000000000.01,null,true",
			"1000000000000.01,1000000000000.03,false",
			"1000000000000.02,1000000000000.02,false",
			"1000000000000.03,1000000000000.01,true"
		},nullValues="null")
		@ParameterizedTest(name="BD.gt({0},{1},false) == {2}")
		public void method_gt_double_BigDecimal_false(
				double a, BigDecimal b, boolean expected )
		{

			assertEquals( expected, BD.gt(a,b,false) );

		}

		@CsvSource(value={
			"1000000000000.01,null,false",
			"1000000000000.01,1000000000000.03,false",
			"1000000000000.02,1000000000000.02,false",
			"1000000000000.03,1000000000000.01,true"
		},nullValues="null")
		@ParameterizedTest(name="BD.gt({0},{1},true) == {2}")
		public void method_gt_double_BigDecimal_true(
				double a, BigDecimal b, boolean expected )
		{

			assertEquals( expected, BD.gt(a,b,true) );

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
			"null,100000000000000000.01,100000000000000000.01",
			"100000000000000000.01,null,100000000000000000.01",
		}, nullValues = "null")
		@ParameterizedTest(name = "BD.apply(null,{0},{1}) == {2}")
		public void if_one_argument_is_null_the_other_is_returned(
			BigDecimal a, BigDecimal b, BigDecimal expected)
		{

			assertEquals(expected, BD.apply(null, a, b));

		}

		@Test
		public void if_the_operator_is_null_a_RequirementFailure_is_thrown()
		{

			final BigDecimal a = BD.of( "100000000000000000.01" );
			final BigDecimal b = BD.of( "100000000000000000.01" );
			assertThrows(
				RequirementFailure.class,
				() -> BD.apply( null, a, b )
			);

		}


		@MethodSource("org.nerd4j.utils.math.BDTests#operators")
		@ParameterizedTest(name = "BD.apply({0},a,b) == expected")
		public void if_all_arguments_are_not_null_should_return_as_expected(
				BinaryOperator<BigDecimal> operator
		)
		{

			assertNotNull(operator);

			final BigDecimal a = BD.of( "100000000000000000.01" );
			final BigDecimal b = BD.of( "100000000000000000.01" );

			assertAll(

				() -> assertSame(null, BD.apply(operator, null, null)),
				() -> assertSame(a, BD.apply(operator, a, null)),
				() -> assertSame(b, BD.apply(operator, null, b)),
				() -> assertEquals(operator.apply(a, b), BD.apply(operator, a, b))

			);

		}

	}

	public static Stream<BinaryOperator<BigDecimal>> operators()
	{

		return Stream.of(
			BD.SUM,
			BD.SUB,
			BD.MUL,
			BD.DIV,
			BD.REM,
			BD.MIN,
			BD.MAX
		);
	}

}

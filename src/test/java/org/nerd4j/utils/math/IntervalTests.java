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

import static java.util.stream.Collectors.toMap;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.nerd4j.utils.lang.RequirementFailure;
import org.nerd4j.utils.test.ObjectOverridesContract;

/**
 * Test suite for the class {@link Interval}
 *
 * @author Massimo Coluzzi
 */
@DisplayName("Testing class: Interval")
@DisplayNameGeneration(ReplaceUnderscores.class)
public class IntervalTests
	   implements ObjectOverridesContract<Interval<Integer>>
{


	/* ******************* */
	/*  INTERFACE METHODS  */
	/* ******************* */


	/**
	 * {@inheritDoc}
	 */
	@Override
	public Interval<Integer> sampleValue()
	{

		return Interval.openSup( 0 );

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Interval<Integer> notEqualValue()
	{

		return Interval.closedSup( 0 );

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Interval<Integer> equalButNotSameValue()
	{

		return Interval.openSup( 0 );

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Interval<Integer> withDifferentHashcode()
	{

		return Interval.closedSup( 0 );

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String sampleToString()
	{

		return "(-\u221e,0)";

	}
	
	
	/* ************** */
	/*  TEST METHODS  */
	/* ************** */
	
	
	/**
     * Performs unit test on factory methods.
     */
    @Nested
    @DisplayName("on factory methods")
    public class FactoryMethods
    {

		@Test
		public void empty()
		{

			final Interval<?> interval = Interval.empty();
			assertAll(
				() -> assertTrue(  interval.isEmpty() ),
				() -> assertTrue(  interval.isEmptySet() ),
				() -> assertFalse( interval.isUnbounded() ),
				() -> assertFalse( interval.isUnboundedInf() ),
				() -> assertFalse( interval.isUnboundedSup() ),
				() -> assertFalse( interval.isOpen() ),
				() -> assertFalse( interval.isOpenInf() ),
				() -> assertFalse( interval.isOpenSup() ),
				() -> assertFalse( interval.isClosed() ),
				() -> assertFalse( interval.isClosedInf() ),
				() -> assertFalse( interval.isClosedSup() ),
				() -> assertThrows( NoSuchElementException.class, () -> interval.getInf() ),
				() -> assertThrows( NoSuchElementException.class, () -> interval.getSup() ),
				() -> assertEquals( "\u2205", interval.toString() ),
				() -> assertEquals( 0, interval.hashCode() )
			);

		}

		@Test
		public void unbounded()
		{

			final Interval<?> interval = Interval.unbounded();
			assertAll(
				() -> assertFalse( interval.isEmpty() ),
				() -> assertFalse( interval.isEmptySet() ),
				() -> assertTrue(  interval.isUnbounded() ),
				() -> assertTrue(  interval.isUnboundedInf() ),
				() -> assertTrue(  interval.isUnboundedSup() ),
				() -> assertFalse( interval.isOpen() ),
				() -> assertFalse( interval.isOpenInf() ),
				() -> assertFalse( interval.isOpenSup() ),
				() -> assertFalse( interval.isClosed() ),
				() -> assertFalse( interval.isClosedInf() ),
				() -> assertFalse( interval.isClosedSup() ),
				() -> assertThrows( NoSuchElementException.class, () -> interval.getInf() ),
				() -> assertThrows( NoSuchElementException.class, () -> interval.getSup() ),
				() -> assertEquals( "(-\u221e,+\u221e)", interval.toString() )
			);

		}

		@Test
		public void openInf()
		{

			final Interval<Integer> interval = Interval.openInf( 0 );
			assertAll(
				() -> assertFalse( interval.isEmpty() ),
				() -> assertFalse( interval.isEmptySet() ),
				() -> assertFalse( interval.isUnbounded() ),
				() -> assertFalse( interval.isUnboundedInf() ),
				() -> assertTrue(  interval.isUnboundedSup() ),
				() -> assertFalse( interval.isOpen() ),
				() -> assertTrue(  interval.isOpenInf() ),
				() -> assertFalse( interval.isOpenSup() ),
				() -> assertFalse( interval.isClosed() ),
				() -> assertFalse( interval.isClosedInf() ),
				() -> assertFalse( interval.isClosedSup() ),
				() -> assertEquals( 0, interval.getInf() ),
				() -> assertThrows( NoSuchElementException.class, () -> interval.getSup() ),
				() -> assertEquals( "(0,+\u221e)", interval.toString() )
			);

		}

		@Test
		public void openSup()
		{

			final Interval<Integer> interval = Interval.openSup( 0 );
			assertAll(
				() -> assertFalse( interval.isEmpty() ),
				() -> assertFalse( interval.isEmptySet() ),
				() -> assertFalse( interval.isUnbounded() ),
				() -> assertTrue(  interval.isUnboundedInf() ),
				() -> assertFalse( interval.isUnboundedSup() ),
				() -> assertFalse( interval.isOpen() ),
				() -> assertFalse( interval.isOpenInf() ),
				() -> assertTrue(  interval.isOpenSup() ),
				() -> assertFalse( interval.isClosed() ),
				() -> assertFalse( interval.isClosedInf() ),
				() -> assertFalse( interval.isClosedSup() ),
				() -> assertThrows( NoSuchElementException.class, () -> interval.getInf() ),
				() -> assertEquals( 0, interval.getSup() ),
				() -> assertEquals( "(-\u221e,0)", interval.toString() )
			);

		}

		@Test
		@DisplayName("open( 0, 0 )")
		public void open00()
		{

			final Interval<Integer> interval = Interval.open( 0, 0 );
			assertAll(
				() -> assertTrue(  interval.isEmpty() ),
				() -> assertFalse( interval.isEmptySet() ),
				() -> assertFalse( interval.isUnbounded() ),
				() -> assertFalse( interval.isUnboundedInf() ),
				() -> assertFalse( interval.isUnboundedSup() ),
				() -> assertTrue(  interval.isOpen() ),
				() -> assertTrue(  interval.isOpenInf() ),
				() -> assertTrue(  interval.isOpenSup() ),
				() -> assertFalse( interval.isClosed() ),
				() -> assertFalse( interval.isClosedInf() ),
				() -> assertFalse( interval.isClosedSup() ),
				() -> assertEquals( 0, interval.getInf() ),
				() -> assertEquals( 0, interval.getSup() ),
				() -> assertEquals( "(0,0)", interval.toString() )
			);

		}

		@Test
		@DisplayName("open( 0, 1 )")
		public void open01()
		{

			final Interval<Integer> interval = Interval.open( 0, 1 );
			assertAll(
				() -> assertFalse( interval.isEmpty() ),
				() -> assertFalse( interval.isEmptySet() ),
				() -> assertFalse( interval.isUnbounded() ),
				() -> assertFalse( interval.isUnboundedInf() ),
				() -> assertFalse( interval.isUnboundedSup() ),
				() -> assertTrue(  interval.isOpen() ),
				() -> assertTrue(  interval.isOpenInf() ),
				() -> assertTrue(  interval.isOpenSup() ),
				() -> assertFalse( interval.isClosed() ),
				() -> assertFalse( interval.isClosedInf() ),
				() -> assertFalse( interval.isClosedSup() ),
				() -> assertEquals( 0, interval.getInf() ),
				() -> assertEquals( 1, interval.getSup() ),
				() -> assertEquals( "(0,1)", interval.toString() )
			);

		}

		@Test
		@DisplayName("openClosed( 0, 0 )")
		public void openClosed00()
		{

			final Interval<Integer> interval = Interval.openClosed( 0, 0 );
			assertAll(
				() -> assertTrue(  interval.isEmpty() ),
				() -> assertFalse( interval.isEmptySet() ),
				() -> assertFalse( interval.isUnbounded() ),
				() -> assertFalse( interval.isUnboundedInf() ),
				() -> assertFalse( interval.isUnboundedSup() ),
				() -> assertFalse( interval.isOpen() ),
				() -> assertTrue(  interval.isOpenInf() ),
				() -> assertFalse( interval.isOpenSup() ),
				() -> assertFalse( interval.isClosed() ),
				() -> assertFalse( interval.isClosedInf() ),
				() -> assertTrue(  interval.isClosedSup() ),
				() -> assertEquals( 0, interval.getInf() ),
				() -> assertEquals( 0, interval.getSup() ),
				() -> assertEquals( "(0,0]", interval.toString() )
			);

		}

		@Test
		@DisplayName("openClosed( 0, 1 )")
		public void openClosed01()
		{

			final Interval<Integer> interval = Interval.openClosed( 0, 1 );
			assertAll(
				() -> assertFalse( interval.isEmpty() ),
				() -> assertFalse( interval.isEmptySet() ),
				() -> assertFalse( interval.isUnbounded() ),
				() -> assertFalse( interval.isUnboundedInf() ),
				() -> assertFalse( interval.isUnboundedSup() ),
				() -> assertFalse( interval.isOpen() ),
				() -> assertTrue(  interval.isOpenInf() ),
				() -> assertFalse( interval.isOpenSup() ),
				() -> assertFalse( interval.isClosed() ),
				() -> assertFalse( interval.isClosedInf() ),
				() -> assertTrue(  interval.isClosedSup() ),
				() -> assertEquals( 0, interval.getInf() ),
				() -> assertEquals( 1, interval.getSup() ),
				() -> assertEquals( "(0,1]", interval.toString() )
			);

		}
		@Test
		public void closedInf()
		{

			final Interval<Integer> interval = Interval.closedInf( 0 );
			assertAll(
				() -> assertFalse( interval.isEmpty() ),
				() -> assertFalse( interval.isEmptySet() ),
				() -> assertFalse( interval.isUnbounded() ),
				() -> assertFalse( interval.isUnboundedInf() ),
				() -> assertTrue(  interval.isUnboundedSup() ),
				() -> assertFalse( interval.isOpen() ),
				() -> assertFalse( interval.isOpenInf() ),
				() -> assertFalse( interval.isOpenSup() ),
				() -> assertFalse( interval.isClosed() ),
				() -> assertTrue(  interval.isClosedInf() ),
				() -> assertFalse( interval.isClosedSup() ),
				() -> assertEquals( 0, interval.getInf() ),
				() -> assertThrows( NoSuchElementException.class, () -> interval.getSup() ),
				() -> assertEquals( "[0,+\u221e)", interval.toString() )
			);

		}

		@Test
		public void closedSup()
		{

			final Interval<Integer> interval = Interval.closedSup( 0 );
			assertAll(
				() -> assertFalse( interval.isEmpty() ),
				() -> assertFalse( interval.isEmptySet() ),
				() -> assertFalse( interval.isUnbounded() ),
				() -> assertTrue(  interval.isUnboundedInf() ),
				() -> assertFalse( interval.isUnboundedSup() ),
				() -> assertFalse( interval.isOpen() ),
				() -> assertFalse( interval.isOpenInf() ),
				() -> assertFalse( interval.isOpenSup() ),
				() -> assertFalse( interval.isClosed() ),
				() -> assertFalse( interval.isClosedInf() ),
				() -> assertTrue(  interval.isClosedSup() ),
				() -> assertThrows( NoSuchElementException.class, () -> interval.getInf() ),
				() -> assertEquals( 0, interval.getSup() ),
				() -> assertEquals( "(-\u221e,0]", interval.toString() )
			);

		}

		@Test
		@DisplayName("closed( 0, 0 )")
		public void closed00()
		{

			final Interval<Integer> interval = Interval.closed( 0, 0 );
			assertAll(
				() -> assertFalse( interval.isEmpty() ),
				() -> assertFalse( interval.isEmptySet() ),
				() -> assertFalse( interval.isUnbounded() ),
				() -> assertFalse( interval.isUnboundedInf() ),
				() -> assertFalse( interval.isUnboundedSup() ),
				() -> assertFalse( interval.isOpen() ),
				() -> assertFalse( interval.isOpenInf() ),
				() -> assertFalse( interval.isOpenSup() ),
				() -> assertTrue(  interval.isClosed() ),
				() -> assertTrue(  interval.isClosedInf() ),
				() -> assertTrue(  interval.isClosedSup() ),
				() -> assertEquals( 0, interval.getInf() ),
				() -> assertEquals( 0, interval.getSup() ),
				() -> assertEquals( "[0,0]", interval.toString() )
			);

		}

		@Test
		@DisplayName("closed( 0, 1 )")
		public void closed01()
		{

			final Interval<Integer> interval = Interval.closed( 0, 1 );
			assertAll(
				() -> assertFalse( interval.isEmpty() ),
				() -> assertFalse( interval.isEmptySet() ),
				() -> assertFalse( interval.isUnbounded() ),
				() -> assertFalse( interval.isUnboundedInf() ),
				() -> assertFalse( interval.isUnboundedSup() ),
				() -> assertFalse( interval.isOpen() ),
				() -> assertFalse( interval.isOpenInf() ),
				() -> assertFalse( interval.isOpenSup() ),
				() -> assertTrue(  interval.isClosed() ),
				() -> assertTrue(  interval.isClosedInf() ),
				() -> assertTrue(  interval.isClosedSup() ),
				() -> assertEquals( 0, interval.getInf() ),
				() -> assertEquals( 1, interval.getSup() ),
				() -> assertEquals( "[0,1]", interval.toString() )
			);

		}

		@Test
		@DisplayName("closedOpen( 0, 0 )")
		public void closedOpen00()
		{

			final Interval<Integer> interval = Interval.closedOpen( 0, 0 );
			assertAll(
				() -> assertTrue(  interval.isEmpty() ),
				() -> assertFalse( interval.isEmptySet() ),
				() -> assertFalse( interval.isUnbounded() ),
				() -> assertFalse( interval.isUnboundedInf() ),
				() -> assertFalse( interval.isUnboundedSup() ),
				() -> assertFalse( interval.isOpen() ),
				() -> assertFalse( interval.isOpenInf() ),
				() -> assertTrue(  interval.isOpenSup() ),
				() -> assertFalse( interval.isClosed() ),
				() -> assertTrue(  interval.isClosedInf() ),
				() -> assertFalse( interval.isClosedSup() ),
				() -> assertEquals( 0, interval.getInf() ),
				() -> assertEquals( 0, interval.getSup() ),
				() -> assertEquals( "[0,0)", interval.toString() )
			);

		}

		@Test
		@DisplayName("closedOpen( 0, 1 )")
		public void closedOpen01()
		{

			final Interval<Integer> interval = Interval.closedOpen( 0, 1 );
			assertAll(
				() -> assertFalse( interval.isEmpty() ),
				() -> assertFalse( interval.isEmptySet() ),
				() -> assertFalse( interval.isUnbounded() ),
				() -> assertFalse( interval.isUnboundedInf() ),
				() -> assertFalse( interval.isUnboundedSup() ),
				() -> assertFalse( interval.isOpen() ),
				() -> assertFalse( interval.isOpenInf() ),
				() -> assertTrue(  interval.isOpenSup() ),
				() -> assertFalse( interval.isClosed() ),
				() -> assertTrue(  interval.isClosedInf() ),
				() -> assertFalse( interval.isClosedSup() ),
				() -> assertEquals( 0, interval.getInf() ),
				() -> assertEquals( 1, interval.getSup() ),
				() -> assertEquals( "[0,1)", interval.toString() )
			);

		}

		@Test
		public void test_inconsistent_limits()
		{

			assertAll(
				() -> assertThrows( RequirementFailure.class, () -> Interval.openInf(null)         ),
				() -> assertThrows( RequirementFailure.class, () -> Interval.openSup(null)         ),
				() -> assertThrows( RequirementFailure.class, () -> Interval.closedInf(null)       ),
				() -> assertThrows( RequirementFailure.class, () -> Interval.closedSup(null)       ),
				() -> assertThrows( RequirementFailure.class, () -> Interval.open(null,null)       ),
				() -> assertThrows( RequirementFailure.class, () -> Interval.open(null,0)          ),
				() -> assertThrows( RequirementFailure.class, () -> Interval.open(1,null)          ),
				() -> assertThrows( RequirementFailure.class, () -> Interval.open(1,0)             ),
				() -> assertThrows( RequirementFailure.class, () -> Interval.openClosed(null,null) ),
				() -> assertThrows( RequirementFailure.class, () -> Interval.openClosed(null,0)    ),
				() -> assertThrows( RequirementFailure.class, () -> Interval.openClosed(1,null)    ),
				() -> assertThrows( RequirementFailure.class, () -> Interval.openClosed(1,0)       ),
				() -> assertThrows( RequirementFailure.class, () -> Interval.closed(null,null)     ),
				() -> assertThrows( RequirementFailure.class, () -> Interval.closed(null,0)        ),
				() -> assertThrows( RequirementFailure.class, () -> Interval.closed(1,null)        ),
				() -> assertThrows( RequirementFailure.class, () -> Interval.closed(1,0)           ),
				() -> assertThrows( RequirementFailure.class, () -> Interval.closedOpen(null,null) ),
				() -> assertThrows( RequirementFailure.class, () -> Interval.closedOpen(null,0)    ),
				() -> assertThrows( RequirementFailure.class, () -> Interval.closedOpen(1,null)    ),
				() -> assertThrows( RequirementFailure.class, () -> Interval.closedOpen(1,0)       )
			);

		}

    }


	/**
	 * Performs unit test on method {@link Interval#contains(Comparable)}.
	 */
	@Nested
	@DisplayName("on method contains")
	public class Contains
	{

		@MethodSource("org.nerd4j.utils.math.IntervalTests#allIntervals")
		@ParameterizedTest(name="null \u2208 {0} ? RequirementFailure")
		public void with_null_argument_should_throw_a_RequirementFailure(
			Interval<?> interval
		)
		{

			assertThrows( RequirementFailure.class, () -> interval.contains(null) );

		}

		public void empty_interval_should_not_contain_any_value()
		{

			final Interval<Double> empty = Interval.empty();
			assertFalse( empty.contains(new Random().nextDouble()) );

		}

		@CsvSource({
			Integer.MIN_VALUE + ",\u2205,false",
				            0 + ",\u2205,false",
			Integer.MAX_VALUE + ",\u2205,false",
			Integer.MIN_VALUE + ",'(-\u221e,+\u221e)',true",
			                0 + ",'(-\u221e,+\u221e)',true",
			Integer.MAX_VALUE + ",'(-\u221e,+\u221e)',true",
			Integer.MIN_VALUE + ",'(-\u221e,0)',true",
			                0 + ",'(-\u221e,0)',false",
			                1 + ",'(-\u221e,0)',false",
			Integer.MAX_VALUE + ",'(-\u221e,0)',false",
			Integer.MIN_VALUE + ",'(-\u221e,0]',true",
			                0 + ",'(-\u221e,0]',true",
			                1 + ",'(-\u221e,0]',false",
			Integer.MAX_VALUE + ",'(-\u221e,0]',false",
			Integer.MIN_VALUE + ",'(0,+\u221e)',false",
			                0 + ",'(0,+\u221e)',false",
			                1 + ",'(0,+\u221e)',true",
			Integer.MAX_VALUE + ",'(0,+\u221e)',true",
			Integer.MIN_VALUE + ",'[0,+\u221e)',false",
			                0 + ",'[0,+\u221e)',true",
			                1 + ",'[0,+\u221e)',true",
			Integer.MAX_VALUE + ",'[0,+\u221e)',true",
			Integer.MIN_VALUE + ",'[0,10]',false",
			                0 + ",'[0,10]',true",
			                5 + ",'[0,10]',true",
			               10 + ",'[0,10]',true",
			Integer.MAX_VALUE + ",'[0,10]',false",
			Integer.MIN_VALUE + ",'[0,10)',false",
			                0 + ",'[0,10)',true",
			                5 + ",'[0,10)',true",
			               10 + ",'[0,10)',false",
			Integer.MAX_VALUE + ",'[0,10)',false",
			Integer.MIN_VALUE + ",'(0,10]',false",
			                0 + ",'(0,10]',false",
			                5 + ",'(0,10]',true",
			               10 + ",'(0,10]',true",
			Integer.MAX_VALUE + ",'(0,10]',false",
			Integer.MIN_VALUE + ",'(0,10)',false",
			                0 + ",'(0,10)',false",
			                5 + ",'(0,10)',true",
			               10 + ",'(0,10)',false",
			Integer.MAX_VALUE + ",'(0,10)',false",
			Integer.MIN_VALUE + ",'[0,0]',false",
			               -1 + ",'[0,0]',false",
			                0 + ",'[0,0]',true",
			                1 + ",'[0,0]',false",
			Integer.MAX_VALUE + ",'[0,0]',false",
			Integer.MIN_VALUE + ",'[0,0)',false",
			               -1 + ",'[0,0)',false",
			                0 + ",'[0,0)',false",
			                1 + ",'[0,0)',false",
			Integer.MAX_VALUE + ",'[0,0)',false",
			Integer.MIN_VALUE + ",'(0,0]',false",
			                0 + ",'(0,0]',false",
			                5 + ",'(0,0]',false",
			               10 + ",'(0,0]',false",
			Integer.MAX_VALUE + ",'(0,0]',false",
			Integer.MIN_VALUE + ",'(0,0)',false",
			                0 + ",'(0,0)',false",
			                5 + ",'(0,0)',false",
			               10 + ",'(0,0)',false",
			Integer.MAX_VALUE + ",'(0,0)',false"
		})
		@ParameterizedTest(name="{0} \u2208 {1} ? {2}")
		public void method_contains_should_return_as_expected(
			Integer value, String symbol, boolean expected
		)
		{

			final Interval<Integer> interval = intervals.get( symbol );
			assertEquals( expected, interval.contains(value) );

		}

	}


	/**
	 * Performs unit test on method {@link Interval#includes(Interval)}.
	 */
	@Nested
	@DisplayName("on method includes")
	public class Includes
	{

		@MethodSource("org.nerd4j.utils.math.IntervalTests#allIntervals")
		@ParameterizedTest(name="null \u2286 {0} ? RequirementFailure")
		public void with_null_argument_should_throw_a_RequirementFailure(
			Interval<?> interval
		)
		{

			assertThrows( RequirementFailure.class, () -> interval.includes(null) );

		}

		@MethodSource("org.nerd4j.utils.math.IntervalTests#allIntervals")
		@ParameterizedTest(name="\u2205 \u2286 {0} ? true")
		public void every_interval_should_include_the_empty_set(
			Interval<?> interval
		)
		{

			assertTrue( interval.includes(Interval.empty()) );

		}

		@MethodSource("org.nerd4j.utils.math.IntervalTests#allIntervals")
		@ParameterizedTest(name="{0} \u2286 {0} ? true")
		public void every_interval_should_include_itself(
			Interval<Integer> interval
		)
		{

			assertTrue( interval.includes(interval) );

		}

		@CsvSource({
			"'(-\u221e,+\u221e)','(-\u221e,0]',false",
			"'(-\u221e,+\u221e)','[0,+\u221e)',false",
			"'(-\u221e,+\u221e)','[0,10]',false",
			"'(-\u221e,+\u221e)','[0,0]',false",
			"'(-\u221e,+\u221e)','(0,0)',false",
			"'[0,5)','(15,20]',false",
			"'[0,10)','(10,20]',false",
			"'[0,10]','[10,20]',false",
			"'[0,10]','[5,15]',false",
			"'[10,20]','[5,15]',false",
			"'[0,20]','(0,20)',false",
			"'(0,20)','[0,20]',true",
			"'(0,10)','[0,20]',true",
			"'(10,20)','[0,20]',true",
			"'[5,15]','[0,20]',true",
			"'[0,20]','[0,+\u221e)',true",
			"'[0,20]','(-\u221e,+\u221e)',true",
		})
		@ParameterizedTest(name="{0} \u2286 {1} ? {2}")
		public void method_includes_should_return_as_expected(
			String a, String b, boolean expected
		)
		{

			final Interval<Integer> intervalA = intervals.get( a );
			final Interval<Integer> intervalB = intervals.get( b );

			assertEquals( expected, intervalB.includes(intervalA) );

		}

	}


	/**
	 * Performs unit test on method {@link Interval#overlaps(Interval)}.
	 */
	@Nested
	@DisplayName("on method overlaps")
	public class Overlaps
	{

		@MethodSource("org.nerd4j.utils.math.IntervalTests#allIntervals")
		@ParameterizedTest(name="null \u2229 {0} \u2260 \u2205 ? RequirementFailure")
		public void with_null_argument_should_throw_a_RequirementFailure(
			Interval<?> interval
		)
		{

			assertThrows( RequirementFailure.class, () -> interval.overlaps(null) );

		}

		@MethodSource("org.nerd4j.utils.math.IntervalTests#allIntervals")
		@ParameterizedTest(name="\u2205 \u2229 {0} \u2260 \u2205 ? false")
		public void the_empty_set_should_not_overlap_any_interval(
			Interval<Integer> interval
		)
		{

			final Interval<Integer> empty = Interval.empty();
			assertFalse( empty.overlaps(interval) );

		}


		@MethodSource("org.nerd4j.utils.math.IntervalTests#allIntervals")
		@ParameterizedTest(name="{0} \u2229 \u2205 \u2260 \u2205 ? false")
		public void no_interval_should_overlap_the_empty_set(
			Interval<Integer> interval
		)
		{

			assertFalse( interval.overlaps(Interval.empty()) );

		}

		@MethodSource("org.nerd4j.utils.math.IntervalTests#allIntervals")
		@ParameterizedTest(name="{0} \u2229 {0} \u2260 \u2205 ? false")
		public void no_interval_should_overlap_itself(
			Interval<Integer> interval
		)
		{

			assertFalse( interval.overlaps(interval) );

		}


		@CsvSource({
			"'(-\u221e,+\u221e)','(-\u221e,0]',false",
			"'(-\u221e,+\u221e)','[0,+\u221e)',false",
			"'(-\u221e,+\u221e)','[0,10]',false",
			"'(-\u221e,+\u221e)','[0,0]',false",
			"'(-\u221e,+\u221e)','(0,0)',false",
			"'(-\u221e,10]','(15,20]',false",
			"'(-\u221e,10]','(10,20]',false",
			"'(-\u221e,10]','[10,20]',true",
			"'(-\u221e,10]','[0,20]',true",
			"'(-\u221e,10]','[0,10]',false",
			"'[10,+\u221e)','[0,10)',false",
			"'[10,+\u221e)','[0,10]',true",
			"'[10,+\u221e)','[5,15]',true",
			"'[10,+\u221e)','[10,20]',false",
			"'[10,+\u221e)','(10,20]',false",
			"'[0,5)','(15,20]',false",
			"'[0,10)','(10,20]',false",
			"'[0,10]','[10,20]',true",
			"'[0,10]','[5,15]',true",
			"'[5,15]','[5,15]',false",
			"'(5,15)','[5,15]',false",
			"'[5,15]','(5,15)',false",
			"'[0,20]','[5,15]',false",
			"'[10,20]','[5,15]',true",
			"'[10,20)','[0,10]',true",
			"'(10,20)','[0,10]',false",
			"'[0,0]','[0,10]',false",
			"'[0,0)','[0,10]',false",
			"'(0,0]','[0,10]',false",
			"'(0,0)','[0,10]',false",
			"'(-\u221e,0]','(-\u221e,+\u221e)',false",
			"'[0,+\u221e)','(-\u221e,+\u221e)',false",
			"'[0,10]','(-\u221e,+\u221e)',false",
			"'[0,0]','(-\u221e,+\u221e)',false",
			"'(0,0)','(-\u221e,+\u221e)',false",
			"'(15,20]','(-\u221e,10]',false",
			"'(10,20]','(-\u221e,10]',false",
			"'[10,20]','(-\u221e,10]',true",
			"'[0,20]','(-\u221e,10]',true",
			"'[0,10]','(-\u221e,10]',false",
			"'[0,10)','[10,+\u221e)',false",
			"'[0,10]','[10,+\u221e)',true",
			"'[5,15]','[10,+\u221e)',true",
			"'[10,20]','[10,+\u221e)',false",
			"'(10,20]','[10,+\u221e)',false",
		})
		@ParameterizedTest(name="{0} \u2229 {1} \u2260 \u2205 ? {2}")
		public void method_overlaps_should_return_as_expected(
				String a, String b, boolean expected
		)
		{

			final Interval<Integer> intervalA = intervals.get( a );
			final Interval<Integer> intervalB = intervals.get( b );

			assertEquals( expected, intervalB.overlaps(intervalA) );

		}

	}


	/**
	 * Performs unit test on method {@link Interval#isDisjointFrom(Interval)}.
	 */
	@Nested
	@DisplayName("on method isDisjointFrom")
	public class IsDisjointFrom
	{

		@MethodSource("org.nerd4j.utils.math.IntervalTests#allIntervals")
		@ParameterizedTest(name="null \u2229 {0} = \u2205 ? RequirementFailure")
		public void with_null_argument_should_throw_a_RequirementFailure(
			Interval<?> interval
		)
		{

			assertThrows( RequirementFailure.class, () -> interval.isDisjointFrom(null) );

		}

		@MethodSource("org.nerd4j.utils.math.IntervalTests#allIntervals")
		@ParameterizedTest(name="{0} \u2229 \u2205 = \u2205 ? false")
		public void every_interval_should_be_disjoint_from_the_empty_set(
			Interval<?> interval
		)
		{

			assertTrue( interval.isDisjointFrom(Interval.empty()) );

		}

		@MethodSource("org.nerd4j.utils.math.IntervalTests#allIntervals")
		@ParameterizedTest(name="\u2205 \u2229 {0} = \u2205 ? false")
		public void the_empty_set_should_be_disjoint_from_any_interval(
			Interval<Integer> interval
		)
		{

			final Interval<Integer> empty = Interval.empty();
			assertTrue( empty.isDisjointFrom(interval) );

		}

		@MethodSource("org.nerd4j.utils.math.IntervalTests#allIntervals")
		@ParameterizedTest(name="{0} \u2229 {0} = \u2205 ?")
		public void every_non_empty_interval_should_not_be_disjoint_from_itself(
			Interval<Integer> interval
		)
		{

			assertEquals( interval.isEmpty(), interval.isDisjointFrom(interval) );

		}


		@CsvSource({
			"'(-\u221e,+\u221e)','(-\u221e,0]',false",
			"'(-\u221e,+\u221e)','[0,+\u221e)',false",
			"'(-\u221e,+\u221e)','[0,10]',false",
			"'(-\u221e,+\u221e)','[0,0]',false",
			"'(-\u221e,+\u221e)','(0,0)',true",
			"'(-\u221e,10]','(15,20]',true",
			"'(-\u221e,10]','(10,20]',true",
			"'(-\u221e,10]','[10,20]',false",
			"'(-\u221e,10]','[0,20]',false",
			"'(-\u221e,10]','[0,10]',false",
			"'[10,+\u221e)','[0,10)',true",
			"'[10,+\u221e)','[0,10]',false",
			"'[10,+\u221e)','[5,15]',false",
			"'[10,+\u221e)','[10,20]',false",
			"'[10,+\u221e)','(10,20]',false",
			"'[0,5)','(15,20]',true",
			"'[0,10)','(10,20]',true",
			"'[0,10]','[10,20]',false",
			"'[0,10]','[5,15]',false",
			"'[5,15]','[5,15]',false",
			"'(5,15)','[5,15]',false",
			"'[5,15]','(5,15)',false",
			"'[0,20]','[5,15]',false",
			"'[10,20]','[5,15]',false",
			"'[10,20)','[0,10]',false",
			"'(10,20)','[0,10]',true",
			"'[0,0]','[0,10]',false",
			"'[0,0)','[0,10]',true",
			"'(0,0]','[0,10]',true",
			"'(0,0)','[0,10]',true",
			"'(-\u221e,0]','(-\u221e,+\u221e)',false",
			"'[0,+\u221e)','(-\u221e,+\u221e)',false",
			"'[0,10]','(-\u221e,+\u221e)',false",
			"'[0,0]','(-\u221e,+\u221e)',false",
			"'(0,0)','(-\u221e,+\u221e)',true",
			"'(15,20]','(-\u221e,10]',true",
			"'(10,20]','(-\u221e,10]',true",
			"'[10,20]','(-\u221e,10]',false",
			"'[0,20]','(-\u221e,10]',false",
			"'[0,10]','(-\u221e,10]',false",
			"'[0,10)','[10,+\u221e)',true",
			"'[0,10]','[10,+\u221e)',false",
			"'[5,15]','[10,+\u221e)',false",
			"'[10,20]','[10,+\u221e)',false",
			"'(10,20]','[10,+\u221e)',false",
		})
		@ParameterizedTest(name="{0} \u2229 {1} = \u2205 ? {2}")
		public void method_isDisjointFrom_should_return_as_expected(
				String a, String b, boolean expected
		)
		{

			final Interval<Integer> intervalA = intervals.get( a );
			final Interval<Integer> intervalB = intervals.get( b );

			assertEquals( expected, intervalA.isDisjointFrom(intervalB) );

		}

	}


	/**
	 * Performs unit test on method {@link Interval#isConsecutiveTo(Interval)}.
	 */
	@Nested
	@DisplayName("on method isConsecutiveTo")
	public class IsConsecutiveTo
	{

		@MethodSource("org.nerd4j.utils.math.IntervalTests#allIntervals")
		@ParameterizedTest(name="null \u21f9 {0} ? RequirementFailure")
		public void with_null_argument_should_throw_a_RequirementFailure(
			Interval<?> interval
		)
		{

			assertThrows( RequirementFailure.class, () -> interval.isConsecutiveTo(null) );

		}

		@MethodSource("org.nerd4j.utils.math.IntervalTests#allIntervals")
		@ParameterizedTest(name="{0} \u21f9 \u2205 ? true")
		public void every_interval_should_be_consecutive_to_the_empty_set(
			Interval<?> interval
		)
		{

			assertTrue( interval.isConsecutiveTo(Interval.empty()) );

		}

		@MethodSource("org.nerd4j.utils.math.IntervalTests#allIntervals")
		@ParameterizedTest(name="\u2205 \u21f9 {0} ? true")
		public void empty_set_should_be_consecutive_to_the_every_interval(
			Interval<Integer> interval
		)
		{

			final Interval<Integer> empty = Interval.empty();
			assertTrue( empty.isConsecutiveTo(interval) );

		}

		@CsvSource({
			"'(-\u221e,+\u221e)','(-\u221e,0]',false",
			"'(-\u221e,+\u221e)','[0,+\u221e)',false",
			"'(-\u221e,+\u221e)','[0,10]',false",
			"'(-\u221e,+\u221e)','[0,0]',false",
			"'(-\u221e,+\u221e)','(0,0)',false",
			"'(-\u221e,10]','(15,20]',false",
			"'(-\u221e,10]','(10,20]',true",
			"'(-\u221e,10]','[10,20]',true",
			"'(-\u221e,10]','[0,20]',false",
			"'(-\u221e,10]','[0,10]',false",
			"'[10,+\u221e)','[0,10)',true",
			"'[10,+\u221e)','[0,10]',true",
			"'[10,+\u221e)','[5,15]',false",
			"'[10,+\u221e)','[10,20]',false",
			"'[10,+\u221e)','(10,20]',false",
			"'[0,5)','(15,20]',false",
			"'[0,10)','(10,20]',false",
			"'[0,10)','[10,20]',true",
			"'[0,10]','[10,20]',true",
			"'[0,10]','[5,15]',false",
			"'[5,15]','[5,15]',false",
			"'(5,15)','[5,15]',false",
			"'[5,15]','(5,15)',false",
			"'[0,20]','[5,15]',false",
			"'[10,20]','[5,15]',false",
			"'[10,20)','[0,10]',true",
			"'(10,20)','[0,10]',true",
			"'[0,0]','[0,10]',true",
			"'[0,0)','[0,10]',true",
			"'(0,0]','[0,10]',true",
			"'(0,0)','[0,10]',true",
			"\u2205,\u2205,true",
			"'(-\u221e,0]','(-\u221e,+\u221e)',false",
			"'[0,+\u221e)','(-\u221e,+\u221e)',false",
			"'[0,10]','(-\u221e,+\u221e)',false",
			"'[0,0]','(-\u221e,+\u221e)',false",
			"'(0,0)','(-\u221e,+\u221e)',false",
			"'(15,20]','(-\u221e,10]',false",
			"'(10,20]','(-\u221e,10]',true",
			"'[10,20]','(-\u221e,10]',true",
			"'[0,20]','(-\u221e,10]',false",
			"'[0,10]','(-\u221e,10]',false",
			"'[0,10)','[10,+\u221e)',true",
			"'[0,10]','[10,+\u221e)',true",
			"'[5,15]','[10,+\u221e)',false",
			"'[10,20]','[10,+\u221e)',false",
			"'(10,20]','[10,+\u221e)',false",
		})
		@ParameterizedTest(name="{0} \u21f9 {1} ? {2}")
		public void method_isConsecutiveTo_should_return_as_expected(
				String a, String b, boolean expected
		)
		{

			final Interval<Integer> intervalA = intervals.get( a );
			final Interval<Integer> intervalB = intervals.get( b );

			assertEquals( expected, intervalB.isConsecutiveTo(intervalA) );

		}

	}


	/**
	 * Performs unit test on method {@link Interval#isStrictlyConsecutiveTo(Interval)}.
	 */
	@Nested
	@DisplayName("on method isStrictlyConsecutiveTo")
	public class IsStrictlyConsecutiveTo
	{

		@MethodSource("org.nerd4j.utils.math.IntervalTests#allIntervals")
		@ParameterizedTest(name="null \u21f9 {0} ? RequirementFailure")
		public void with_null_argument_should_throw_a_RequirementFailure(
			Interval<?> interval
		)
		{

			assertThrows( RequirementFailure.class, () -> interval.isStrictlyConsecutiveTo(null) );

		}

		@MethodSource("org.nerd4j.utils.math.IntervalTests#allIntervals")
		@ParameterizedTest(name="{0} \u21f9 \u2205 ? true")
		public void every_interval_should_be_strictly_consecutive_to_the_empty_set(
			Interval<?> interval
		)
		{

			assertTrue( interval.isStrictlyConsecutiveTo(Interval.empty()) );

		}

		@MethodSource("org.nerd4j.utils.math.IntervalTests#allIntervals")
		@ParameterizedTest(name="\u2205 \u21f9 {0} ? true")
		public void the_empty_set_should_be_strictly_consecutive_to_every_interval(
			Interval<Integer> interval
		)
		{

			final Interval<Integer> empty = Interval.empty();
			assertTrue( empty.isStrictlyConsecutiveTo(interval) );

		}

		@CsvSource({
			"'(-\u221e,+\u221e)','(-\u221e,0]',false",
			"'(-\u221e,+\u221e)','[0,+\u221e)',false",
			"'(-\u221e,+\u221e)','[0,10]',false",
			"'(-\u221e,+\u221e)','[0,0]',false",
			"'(-\u221e,+\u221e)','(0,0)',false",
			"'(-\u221e,10]','(15,20]',false",
			"'(-\u221e,10]','(10,20]',true",
			"'(-\u221e,10]','[10,20]',false",
			"'(-\u221e,10]','[0,20]',false",
			"'(-\u221e,10]','[0,10]',false",
			"'[10,+\u221e)','[0,10)',true",
			"'[10,+\u221e)','[0,10]',false",
			"'[10,+\u221e)','[5,15]',false",
			"'[10,+\u221e)','[10,20]',false",
			"'[10,+\u221e)','(10,20]',false",
			"'[0,5)','(15,20]',false",
			"'[0,10)','(10,20]',false",
			"'[0,10)','[10,20]',true",
			"'[0,10]','[10,20]',false",
			"'[0,10]','[5,15]',false",
			"'[5,15]','[5,15]',false",
			"'(5,15)','[5,15]',false",
			"'[5,15]','(5,15)',false",
			"'[0,20]','[5,15]',false",
			"'[10,20]','[5,15]',false",
			"'[10,20)','[0,10]',false",
			"'(10,20)','[0,10]',true",
			"'[0,0]','[0,10]',false",
			"'[0,0)','[0,10]',true",
			"'(0,0]','[0,10]',false",
			"'(0,0)','[0,10]',true",
			"\u2205,\u2205,true",
			"'(-\u221e,0]','(-\u221e,+\u221e)',false",
			"'[0,+\u221e)','(-\u221e,+\u221e)',false",
			"'[0,10]','(-\u221e,+\u221e)',false",
			"'[0,0]','(-\u221e,+\u221e)',false",
			"'(0,0)','(-\u221e,+\u221e)',false",
			"'(15,20]','(-\u221e,10]',false",
			"'(10,20]','(-\u221e,10]',true",
			"'[10,20]','(-\u221e,10]',false",
			"'[0,20]','(-\u221e,10]',false",
			"'[0,10]','(-\u221e,10]',false",
			"'[0,10)','[10,+\u221e)',true",
			"'[0,10]','[10,+\u221e)',false",
			"'[5,15]','[10,+\u221e)',false",
			"'[10,20]','[10,+\u221e)',false",
			"'(10,20]','[10,+\u221e)',false",
		})
		@ParameterizedTest(name="{0} \u21f9 {1} ? {2}")
		public void method_isStrictlyConsecutiveTo_should_return_as_expected(
				String a, String b, boolean expected
		)
		{

			final Interval<Integer> intervalA = intervals.get( a );
			final Interval<Integer> intervalB = intervals.get( b );

			assertEquals( expected, intervalA.isStrictlyConsecutiveTo(intervalB) );

		}

	}


	/**
	 * Performs unit test on method {@link Interval#canUnify(Interval)}.
	 */
	@Nested
	@DisplayName("on method canUnify")
	public class CanUnify
	{

		@MethodSource("org.nerd4j.utils.math.IntervalTests#allIntervals")
		@ParameterizedTest(name="null \u222a {0} ? RequirementFailure")
		public void with_null_argument_should_throw_a_RequirementFailure(
			Interval<?> interval
		)
		{

			assertThrows( RequirementFailure.class, () -> interval.canUnify(null) );

		}

		@MethodSource("org.nerd4j.utils.math.IntervalTests#allIntervals")
		@ParameterizedTest(name="\u2205 \u222a {0} ? true")
		public void every_interval_can_unify_the_empty_set(
			Interval<?> interval
		)
		{

			assertTrue( interval.canUnify(Interval.empty()) );

		}

		@MethodSource("org.nerd4j.utils.math.IntervalTests#allIntervals")
		@ParameterizedTest(name="{0} \u222a {0} ? true")
		public void every_interval_can_unify_itself(
			Interval<Integer> interval
		)
		{

			assertTrue( interval.canUnify(interval) );

		}

		@CsvSource({
			"'(-\u221e,+\u221e)','(-\u221e,0]',true",
			"'(-\u221e,+\u221e)','[0,+\u221e)',true",
			"'(-\u221e,+\u221e)','[0,10]',true",
			"'(-\u221e,+\u221e)','[0,0]',true",
			"'(-\u221e,+\u221e)','(0,0)',true",
			"'(-\u221e,10]','(15,20]',false",
			"'(-\u221e,10]','(10,20]',true",
			"'(-\u221e,10]','[10,20]',true",
			"'(-\u221e,10]','[0,20]',true",
			"'(-\u221e,10]','[0,5]',true",
			"'[10,+\u221e)','[0,5]',false",
			"'[10,+\u221e)','[0,10)',true",
			"'[10,+\u221e)','[0,10]',true",
			"'[10,+\u221e)','[5,15]',true",
			"'[10,+\u221e)','[10,20]',true",
			"'[10,+\u221e)','(15,20]',true",
			"'[0,5)','(15,20]',false",
			"'[0,10)','(10,20]',false",
			"'[0,10]','(10,20]',true",
			"'[0,10)','[10,20]',true",
			"'[0,10]','[10,20]',true",
			"'[0,10]','[5,15]',true",
			"'[5,15]','[5,15]',true",
			"'(5,15)','[5,15]',true",
			"'[5,15]','(5,15)',true",
			"'[0,20]','[5,15]',true",
			"'[10,20]','[5,15]',true",
			"'[10,20)','[0,10]',true",
			"'(10,20)','[0,10]',true",
			"'[0,0]','[0,10]',true",
			"'[0,0)','[0,10]',true",
			"'(0,0]','[0,10]',true",
			"'(0,0)','[0,10]',true",
			"'(0,0)','(0,10]',true",
			"'(0,0)','[5,10]',false",
			"\u2205,\u2205,true",
			"'(-\u221e,0]','(-\u221e,+\u221e)',true",
			"'[0,+\u221e)','(-\u221e,+\u221e)',true",
			"'[0,10]','(-\u221e,+\u221e)',true",
			"'[0,0]','(-\u221e,+\u221e)',true",
			"'(0,0)','(-\u221e,+\u221e)',true",
			"'(15,20]','(-\u221e,10]',false",
			"'(10,20]','(-\u221e,10]',true",
			"'[10,20]','(-\u221e,10]',true",
			"'[0,20]','(-\u221e,10]',true",
			"'[0,10]','(-\u221e,10]',true",
			"'[0,10)','[10,+\u221e)',true",
			"'[0,10]','[10,+\u221e)',true",
			"'[5,15]','[10,+\u221e)',true",
			"'[10,20]','[10,+\u221e)',true",
			"'(10,20]','[10,+\u221e)',true",
		})
		@ParameterizedTest(name="{0} \u222a {1}? ? {2}")
		public void method_canUnify_should_return_as_expected(
				String a, String b, boolean expected
		)
		{

			final Interval<Integer> intervalA = intervals.get( a );
			final Interval<Integer> intervalB = intervals.get( b );

			assertEquals( expected, intervalA.canUnify(intervalB) );

		}

	}


	/**
	 * Performs unit test on method {@link Interval#canSubtract(Interval)} .
	 */
	@Nested
	@DisplayName("on method canSubtract")
	public class CanSubtract
	{

		@MethodSource("org.nerd4j.utils.math.IntervalTests#allIntervals")
		@ParameterizedTest(name="{0} \\ null ? RequirementFailure")
		public void with_null_argument_should_throw_a_RequirementFailure(
			Interval<?> interval
		)
		{

			assertThrows( RequirementFailure.class, () -> interval.canSubtract(null) );

		}

		@MethodSource("org.nerd4j.utils.math.IntervalTests#allIntervals")
		@ParameterizedTest(name="{0} \\ \u2205 ? true")
		public void every_interval_can_subtract_the_empty_set(
			Interval<?> interval
		)
		{

			assertTrue( interval.canSubtract(Interval.empty()) );

		}

		@MethodSource("org.nerd4j.utils.math.IntervalTests#allIntervals")
		@ParameterizedTest(name="{0} \\ {0} ? true")
		public void every_interval_can_subtract_itself(
			Interval<Integer> interval
		)
		{

			assertTrue( interval.canSubtract(interval) );

		}

		@CsvSource({
			"'(-\u221e,+\u221e)','(-\u221e,+\u221e)',true",
			"'(-\u221e,+\u221e)','(-\u221e,0]',true",
			"'(-\u221e,+\u221e)','[0,+\u221e)',true",
			"'(-\u221e,+\u221e)','[0,10]',false",
			"'(-\u221e,+\u221e)','[0,0]',false",
			"'(-\u221e,+\u221e)','(0,0)',true",
			"'(-\u221e,10]','(15,20]',true",
			"'(-\u221e,10]','(10,20]',true",
			"'(-\u221e,10]','[10,20]',true",
			"'(-\u221e,10]','[0,20]',true",
			"'(-\u221e,10]','[0,5]',false",
			"'[10,+\u221e)','[0,5]',true",
			"'[10,+\u221e)','[0,10)',true",
			"'[10,+\u221e)','[0,10]',true",
			"'[10,+\u221e)','[5,15]',true",
			"'[10,+\u221e)','[10,20]',true",
			"'[10,+\u221e)','(15,20]',false",
			"'[0,5)','(15,20]',true",
			"'[0,10)','(10,20]',true",
			"'[0,10]','(10,20]',true",
			"'[0,10)','[10,20]',true",
			"'[0,10]','[10,20]',true",
			"'[0,10]','[5,15]',true",
			"'[5,15]','[5,15]',true",
			"'(5,15)','[5,15]',true",
			"'[5,15]','(5,15)',false",
			"'[0,20]','[5,15]',false",
			"'[10,20]','[5,15]',true",
			"'[10,20)','[0,10]',true",
			"'(10,20)','[0,10]',true",
			"'[0,0]','[0,10]',true",
			"'[0,0)','[0,10]',true",
			"'(0,0]','[0,10]',true",
			"'(0,0)','[0,10]',true",
			"'(0,0)','(0,10]',true",
			"'(0,0)','[5,10]',true",
			"\u2205,\u2205,true",
			"'(-\u221e,0]','(-\u221e,+\u221e)',true",
			"'[0,+\u221e)','(-\u221e,+\u221e)',true",
			"'[0,10]','(-\u221e,+\u221e)',true",
			"'[0,0]','(-\u221e,+\u221e)',true",
			"'(0,0)','(-\u221e,+\u221e)',true",
			"'(15,20]','(-\u221e,10]',true",
			"'(10,20]','(-\u221e,10]',true",
			"'[10,20]','(-\u221e,10]',true",
			"'[0,20]','(-\u221e,10]',true",
			"'[0,10]','(-\u221e,10]',true",
			"'[0,10)','[10,+\u221e)',true",
			"'[0,10]','[10,+\u221e)',true",
			"'[5,15]','[10,+\u221e)',true",
			"'[10,20]','[10,+\u221e)',true",
			"'(10,20]','[10,+\u221e)',true",
		})
		@ParameterizedTest(name="{0} \\ {1}? ? {2}")
		public void method_canSubtract_should_return_as_expected(
				String a, String b, boolean expected
		)
		{

			final Interval<Integer> intervalA = intervals.get( a );
			final Interval<Integer> intervalB = intervals.get( b );

			assertEquals( expected, intervalA.canSubtract(intervalB) );

		}

	}


	/**
	 * Performs unit test on method {@link Interval#equals(Object)}.
	 */
	@Nested
	@DisplayName("on method equals")
	public class Equals
	{

		@MethodSource("org.nerd4j.utils.math.IntervalTests#allIntervals")
		@ParameterizedTest(name="{0} == null ? false")
		public void with_null_argument_should_always_return_false(
			Interval<?> interval
		)
		{

			assertFalse( interval.equals(null) );

		}

		@MethodSource("org.nerd4j.utils.math.IntervalTests#allIntervals")
		@ParameterizedTest(name="{0} \u222a {0} ? true")
		public void every_interval_equals_itself(
			Interval<Integer> interval
		)
		{

			assertTrue( interval.equals(interval) );

		}

		@CsvSource({
			"'(-\u221e,+\u221e)','(-\u221e,+\u221e)',true",
			"'(-\u221e,+\u221e)','(-\u221e,0]',false",
			"'(-\u221e,+\u221e)','[0,+\u221e)',false",
			"'(-\u221e,+\u221e)','[0,10]',false",
			"'(-\u221e,+\u221e)','[0,0]',false",
			"'(-\u221e,+\u221e)','(0,0)',false",
			"'(-\u221e,10]','[0,10]',false",
			"'(-\u221e,10]','[0,0]',false",
			"'(-\u221e,10]','(0,0)',false",
			"'[10,+\u221e)','(0,0)',false",
			"'[10,+\u221e)','[0,0]',false",
			"'[10,+\u221e)','[0,10]',false",
			"'[0,5)','(15,20]',false",
			"'[0,10)','(10,20]',false",
			"'[0,10]','[10,20]',false",
			"'[0,10]','[5,15]',false",
			"'[5,15]','[15,20]',false",
			"'[5,15]','[5,15]',true",
			"'(5,15)','[5,15]',false",
			"'[5,15]','(5,15)',false",
			"'[0,20]','[5,15]',false",
			"'[0,0]','[0,10]',false",
			"'[0,0]','[0,0]',true",
			"'[0,0)','[0,0]',false",
			"'(0,0]','[0,0]',false",
			"'(0,0)','[0,0]',false",
			"\u2205,\u2205,true",
			"'(-\u221e,+\u221e)','(-\u221e,+\u221e)',true",
			"'(-\u221e,0]','(-\u221e,+\u221e)',false",
			"'[0,+\u221e)','(-\u221e,+\u221e)',false",
			"'[0,10]','(-\u221e,+\u221e)',false",
			"'[0,0]','(-\u221e,+\u221e)',false",
			"'(0,0)','(-\u221e,+\u221e)',false",
			"'(15,20]','(-\u221e,10]',false",
			"'(10,20]','(-\u221e,10]',false",
			"'[10,20]','(-\u221e,10]',false",
			"'[0,20]','(-\u221e,10]',false",
			"'[0,10]','(-\u221e,10]',false",
			"'[0,10)','[10,+\u221e)',false",
			"'[0,10]','[10,+\u221e)',false",
			"'[5,15]','[10,+\u221e)',false",
			"'[10,20]','[10,+\u221e)',false",
			"'(10,20]','[10,+\u221e)',false",
		})
		@ParameterizedTest(name="{0} == {1} ? {2}")
		public void method_equals_should_return_as_expected(
				String a, String b, boolean expected
		)
		{

			final Interval<Integer> intervalA = intervals.get( a );
			final Interval<Integer> intervalB = intervals.get( b );

			assertEquals( expected, intervalA.equals(intervalB) );

		}

	}


	/**
	 * Performs unit test on method {@link Interval#intersect(Interval)}.
	 */
	@Nested
	@DisplayName("on method intersect")
	public class Intersect
	{

		@MethodSource("org.nerd4j.utils.math.IntervalTests#allIntervals")
		@ParameterizedTest(name="{0} \u2229 null => RequirementFailure")
		public void with_null_argument_should_throw_a_RequirementFailure(
			Interval<?> interval
		)
		{

			assertThrows( RequirementFailure.class, () -> interval.intersect(null) );

		}

		@MethodSource("org.nerd4j.utils.math.IntervalTests#allIntervals")
		@ParameterizedTest(name="{0} \u2229 {0} = {0}")
		public void every_interval_intersect_itself_is_itself(
			Interval<Integer> interval
		)
		{

			assertEquals( interval,  interval.intersect(interval) );

		}

		@MethodSource("org.nerd4j.utils.math.IntervalTests#allIntervals")
		@ParameterizedTest(name="{0} \u2229 \u2205 = \u2205")
		public void every_intersection_with_the_empty_set_produce_the_empty_set(
			Interval<Integer> interval
		)
		{

			final Interval<Integer> empty = Interval.empty();
			assertEquals( empty,  interval.intersect(empty) );
			assertEquals( empty,  empty.intersect(interval) );

		}

		@CsvSource({
			"'(-\u221e,+\u221e)','(-\u221e,+\u221e)','(-\u221e,+\u221e)'",
			"'(-\u221e,+\u221e)','(-\u221e,0]','(-\u221e,0]'",
			"'(-\u221e,+\u221e)','[0,+\u221e)','[0,+\u221e)'",
			"'(-\u221e,+\u221e)','[0,10]','[0,10]'",
			"'(-\u221e,+\u221e)','[0,0]','[0,0]'",
			"'(-\u221e,+\u221e)','(0,0)','(0,0)'",
			"'(-\u221e,10]','[0,10]','[0,10]'",
			"'(-\u221e,10]','[0,0]','[0,0]'",
			"'(-\u221e,10]','(0,0)','(0,0)'",
			"'[10,+\u221e)','(0,0)',\u2205",
			"'[10,+\u221e)','[0,0]',\u2205",
			"'[10,+\u221e)','[0,10]','[10,10]'",
			"'[0,5)','(15,20]',\u2205",
			"'[0,10)','(10,20]',\u2205",
			"'[0,10]','[10,20]','[10,10]'",
			"'[0,10]','[5,15]','[5,10]'",
			"'[5,15]','[5,15]','[5,15]'",
			"'(5,15)','[5,15]','(5,15)'",
			"'[5,15]','(5,15)','(5,15)'",
			"'[0,20]','[5,15]','[5,15]'",
			"'[0,0]','[0,10]','[0,0]'",
			"'[0,0]','[0,0]','[0,0]'",
			"'[0,0)','[0,0]','[0,0)'",
			"'(0,0]','[0,0]','(0,0]'",
			"'(0,0)','[0,0]','(0,0)'",
			"'(-\u221e,0]','(-\u221e,+\u221e)','(-\u221e,0]'",
			"'[0,+\u221e)','(-\u221e,+\u221e)','[0,+\u221e)'",
			"'[0,10]','(-\u221e,+\u221e)','[0,10]'",
			"'[0,0]','(-\u221e,+\u221e)','[0,0]'",
			"'(0,0)','(-\u221e,+\u221e)','(0,0)'",
			"'(15,20]','(-\u221e,10]',\u2205",
			"'(10,20]','(-\u221e,10]',\u2205",
			"'[10,20]','(-\u221e,10]','[10,10]'",
			"'[0,20]','(-\u221e,10]','[0,10]'",
			"'[0,10]','(-\u221e,10]','[0,10]'",
			"'[0,10)','[10,+\u221e)',\u2205",
			"'[0,10]','[10,+\u221e)','[10,10]'",
			"'[5,15]','[10,+\u221e)','[10,15]'",
			"'[10,20]','[10,+\u221e)','[10,20]'",
			"'(10,20]','[10,+\u221e)','(10,20]'",
		})
		@ParameterizedTest(name="{0} \u2229 {1} = {2}")
		public void method_intersect_should_return_as_expected(
				String a, String b, String expected
		)
		{

			final Interval<Integer> intervalA = intervals.get( a );
			final Interval<Integer> intervalB = intervals.get( b );
			final Interval<Integer> expectedInterval = intervals.get( expected );

			assertEquals( expectedInterval, intervalA.intersect(intervalB) );

		}

	}


	/**
	 * Performs unit test on method {@link Interval#unify(Interval)}.
	 */
	@Nested
	@DisplayName("on method unify")
	public class Unify
	{

		@MethodSource("org.nerd4j.utils.math.IntervalTests#allIntervals")
		@ParameterizedTest(name="{0} \u222a null => RequirementFailure")
		public void with_null_argument_should_throw_a_RequirementFailure(
			Interval<?> interval
		)
		{

			assertThrows( RequirementFailure.class, () -> interval.unify(null) );

		}

		@MethodSource("org.nerd4j.utils.math.IntervalTests#allIntervals")
		@ParameterizedTest(name="{0} \u222a {0} = {0}")
		public void every_interval_unified_with_itself_is_itself(
			Interval<Integer> interval
		)
		{

			assertEquals( interval,  interval.unify(interval) );

		}

		@MethodSource("org.nerd4j.utils.math.IntervalTests#allIntervals")
		@ParameterizedTest(name="{0} \u222a \u2205 = {0}}")
		public void every_union_with_the_empty_set_produce_the_other_interval(
			Interval<Integer> interval
		)
		{

			final Interval<Integer> empty = Interval.empty();
			assertEquals( interval,  interval.unify(empty) );
			assertEquals( interval,  empty.unify(interval) );

		}

		@CsvSource({
			"'(-\u221e,+\u221e)','(-\u221e,+\u221e)','(-\u221e,+\u221e)'",
			"'(-\u221e,+\u221e)','(-\u221e,0]','(-\u221e,+\u221e)'",
			"'(-\u221e,+\u221e)','[0,+\u221e)','(-\u221e,+\u221e)'",
			"'(-\u221e,+\u221e)','[0,10]','(-\u221e,+\u221e)'",
			"'(-\u221e,+\u221e)','[0,0]','(-\u221e,+\u221e)'",
			"'(-\u221e,+\u221e)','(0,0)','(-\u221e,+\u221e)'",
			"'(-\u221e,10]','[0,10]','(-\u221e,10]'",
			"'(-\u221e,10]','[0,0]','(-\u221e,10]'",
			"'(-\u221e,10]','(0,0)','(-\u221e,10]'",
			"'[10,+\u221e)','(0,0)',",
			"'[10,+\u221e)','[0,0]',",
			"'[10,+\u221e)','[0,10)','[0,+\u221e)'",
			"'[10,+\u221e)','[0,10]','[0,+\u221e)'",
			"'[0,5)','(15,20]',",
			"'[0,10)','(10,20]',",
			"'[0,10]','[10,20]','[0,20]'",
			"'[0,10]','[5,15]','[0,15]'",
			"'[5,15]','[5,15]','[5,15]'",
			"'(5,15)','[5,15]','[5,15]'",
			"'[5,15]','(5,15)','[5,15]'",
			"'[0,20]','[5,15]','[0,20]'",
			"'[0,0]','[0,10]','[0,10]'",
			"'[0,0]','[0,0]','[0,0]'",
			"'[0,0)','[0,0]','[0,0]'",
			"'(0,0]','[0,0]','[0,0]'",
			"'(0,0)','[0,0]','[0,0]'",
			"'(-\u221e,0]','(-\u221e,+\u221e)','(-\u221e,+\u221e)'",
			"'[0,+\u221e)','(-\u221e,+\u221e)','(-\u221e,+\u221e)'",
			"'[0,10]','(-\u221e,+\u221e)','(-\u221e,+\u221e)'",
			"'[0,0]','(-\u221e,+\u221e)','(-\u221e,+\u221e)'",
			"'(0,0)','(-\u221e,+\u221e)','(-\u221e,+\u221e)'",
			"'(15,20]','(-\u221e,10]',",
			"'(10,20]','(-\u221e,10]','(-\u221e,20]'",
			"'[10,20]','(-\u221e,10]','(-\u221e,20]'",
			"'[0,20]','(-\u221e,10]','(-\u221e,20]'",
			"'[0,10]','(-\u221e,10]','(-\u221e,10]'",
			"'[0,10)','[10,+\u221e)','[0,+\u221e)'",
			"'[0,10]','[10,+\u221e)','[0,+\u221e)'",
			"'[5,15]','[10,+\u221e)','[5,+\u221e)'",
			"'[10,20]','[10,+\u221e)','[10,+\u221e)'",
			"'(10,20]','[10,+\u221e)','[10,+\u221e)'",
		})
		@ParameterizedTest(name="{0} \u222a {1} = {2}")
		public void method_unify_should_return_as_expected(
				String a, String b, String expected
		)
		{

			final Interval<Integer> intervalA = intervals.get( a );
			final Interval<Integer> intervalB = intervals.get( b );
			final Interval<Integer> expectedInterval = intervals.get( expected );

			if( intervalA.canUnify(intervalB) )
				assertEquals( expectedInterval, intervalA.unify(intervalB) );
			else
				assertThrows( NoSuchIntervalException.class, () -> intervalA.unify(intervalB) );

		}

	}


	/**
	 * Performs unit test on method {@link Interval#subtract(Interval)}.
	 */
	@Nested
	@DisplayName("on method subtract")
	public class Subtract
	{

		@MethodSource("org.nerd4j.utils.math.IntervalTests#allIntervals")
		@ParameterizedTest(name="{0} \\ null => RequirementFailure")
		public void with_null_argument_should_throw_a_RequirementFailure(
			Interval<?> interval
		)
		{

			assertThrows( RequirementFailure.class, () -> interval.subtract(null) );

		}

		@MethodSource("org.nerd4j.utils.math.IntervalTests#allIntervals")
		@ParameterizedTest(name="{0} \\ {0} = \u2205")
		public void subtracting_an_interval_to_itself_will_return_the_empty_set(
			Interval<Integer> interval
		)
		{

			assertEquals( Interval.empty(), interval.subtract(interval) );

		}

		@MethodSource("org.nerd4j.utils.math.IntervalTests#allIntervals")
		@ParameterizedTest(name="{0} \\ \u2205 = {0}}")
		public void subtracting_the_empty_set_to_an_interval_will_return_the_interval_itself(
			Interval<Integer> interval
		)
		{

			assertEquals( interval,  interval.subtract(Interval.empty()) );

		}

		@CsvSource({
			"'(-\u221e,+\u221e)','(0,+\u221e)','(-\u221e,0]'",
			"'(-\u221e,+\u221e)','[0,+\u221e)','(-\u221e,0)'",
			"'(-\u221e,+\u221e)','(-\u221e,0)','[0,+\u221e)'",
			"'(-\u221e,+\u221e)','(-\u221e,0]','(0,+\u221e)'",
			"'(-\u221e,+\u221e)','(0,10)',",
			"'(-\u221e,+\u221e)','[0,10)',",
			"'(-\u221e,+\u221e)','(0,10]',",
			"'(-\u221e,+\u221e)','[0,10]',",
			"'(-\u221e,+\u221e)','(0,0)','(-\u221e,+\u221e)'",
			"'(-\u221e,+\u221e)','[0,0)','(-\u221e,+\u221e)'",
			"'(-\u221e,+\u221e)','(0,0]','(-\u221e,+\u221e)'",
			"'(-\u221e,+\u221e)','[0,0]',",
			"'(-\u221e,10)','(-\u221e,+\u221e)',\u2205",
			"'(-\u221e,10)','(0,+\u221e)','(-\u221e,0]'",
			"'(-\u221e,10)','[0,+\u221e)','(-\u221e,0)'",
			"'(-\u221e,10)','(-\u221e,0)','[0,10)'",
			"'(-\u221e,10)','(-\u221e,0]','(0,10)'",
			"'(-\u221e,10)','(0,10)','(-\u221e,0]'",
			"'(-\u221e,10)','[0,10)','(-\u221e,0)'",
			"'(-\u221e,10)','(0,10]','(-\u221e,0]'",
			"'(-\u221e,10)','[0,10]','(-\u221e,0)'",
            "'(-\u221e,10)','(0,0)','(-\u221e,10)'",
            "'(-\u221e,10)','[0,0)','(-\u221e,10)'",
            "'(-\u221e,10)','(0,0]','(-\u221e,10)'",
            "'(-\u221e,10)','[0,0]',",
            "'(0,+\u221e)','(-\u221e,+\u221e)',\u2205",
            "'(0,+\u221e)','(10,+\u221e)','(0,10]'",
            "'(0,+\u221e)','[10,+\u221e)','(0,10)'",
            "'(0,+\u221e)','(-\u221e,10)','[10,+\u221e)'",
            "'(0,+\u221e)','(-\u221e,10]','(10,+\u221e)'",
            "'(0,+\u221e)','(0,10)','[10,+\u221e)'",
            "'(0,+\u221e)','[0,10)','[10,+\u221e)'",
            "'(0,+\u221e)','(0,10]','(10,+\u221e)'",
            "'(0,+\u221e)','[0,10]','(10,+\u221e)'",
            "'(0,+\u221e)','(10,10)','(0,+\u221e)'",
            "'(0,+\u221e)','[10,10)','(0,+\u221e)'",
            "'(0,+\u221e)','(10,10]','(0,+\u221e)'",
            "'(0,+\u221e)','[10,10]',\u2205",
            "'[0,20]','(-\u221e,+\u221e)',\u2205",
            "'[0,20]','(10,+\u221e)','[0,10]'",
            "'[0,20]','[10,+\u221e)','[0,10)'",
            "'[0,20]','(-\u221e,10)','[10,20]'",
            "'[0,20]','(-\u221e,10]','(10,20]'",
            "'[0,20]','(0,10)',",
            "'[0,20]','[0,10)','[10,20]'",
            "'[0,20]','(0,10]',",
            "'[0,20]','[0,10]','(10,20]'",
            "'[0,20]','(10,10)','[0,20]'",
            "'[0,20]','[10,10)','[0,20]'",
            "'[0,20]','(10,10]','[0,20]'",
            "'[0,20]','[10,10]',",
            "'[0,10]','[15,20]','[0,10]'",
            "'[0,10]','(10,20]','[0,10]'",
            "'[0,10]','[10,20]','[0,10)'",
            "'(0,0)','(-\u221e,+\u221e)',\u2205",
            "'(0,0)','(10,+\u221e)','(0,0)'",
            "'(0,0)','[10,+\u221e)','(0,0)'",
            "'(0,0)','(-\u221e,10)',\u2205",
            "'(0,0)','(-\u221e,10]',\u2205",
            "'(0,0)','(0,10)',\u2205",
            "'(0,0)','[0,10)',\u2205",
            "'(0,0)','(0,10]',\u2205",
            "'(0,0)','[0,10]',\u2205",
            "'(0,0)','(10,10)','(0,0)'",
            "'(0,0)','[10,10)','(0,0)'",
            "'(0,0)','(10,10]','(0,0)'",
            "'(0,0)','[10,10]','(0,0)'"
		})
		@ParameterizedTest(name="{0} \\ {1} = {2}")
		public void method_subtract_should_return_as_expected(
				String a, String b, String expected
		)
		{

			final Interval<Integer> intervalA = intervals.get( a );
			final Interval<Integer> intervalB = intervals.get( b );
			final Interval<Integer> expectedInterval = intervals.get( expected );

			if( intervalA.canSubtract(intervalB) )
				assertEquals( expectedInterval, intervalA.subtract(intervalB) );
			else
				assertThrows( NoSuchIntervalException.class, () -> intervalA.subtract(intervalB) );

		}

	}


	/* ********* */
	/*  HELPERS  */
	/* ********* */



	/** Limits to use for test. */
	private static final Map<String,Interval<Integer>> intervals;

	static
	{

		final Stream<Interval<Integer>> stream = Stream.of(
			Interval.empty(),
			Interval.unbounded(),

			Interval.openSup( 0 ),
			Interval.closedSup( 0 ),
			Interval.openInf( 0 ),
			Interval.closedInf( 0 ),

			Interval.closedInf( 5 ),

			Interval.openSup( 10 ),
			Interval.closedSup( 10 ),
			Interval.openInf( 10 ),
			Interval.closedInf( 10 ),

			Interval.openSup( 20 ),
			Interval.closedSup( 20 ),
			Interval.openInf( 20 ),
			Interval.closedInf( 20 ),

			Interval.closed( 0, 20 ),
			Interval.closedOpen( 0, 20 ),
			Interval.openClosed( 0, 20 ),
			Interval.open( 0, 20 ),

			Interval.closed( 10, 20 ),
			Interval.closedOpen( 10, 20 ),
			Interval.openClosed( 10, 20 ),
			Interval.open( 10, 20 ),

			Interval.closed( 15, 20 ),
			Interval.closedOpen( 15, 20 ),
			Interval.openClosed( 15, 20 ),
			Interval.open( 15, 20 ),

			Interval.closed( 5, 15 ),
			Interval.closedOpen( 5, 15 ),
			Interval.openClosed( 5, 15 ),
			Interval.open( 5, 15 ),

			Interval.closed( 0, 15 ),

			Interval.closed( 10, 15 ),
			Interval.closedOpen( 10, 15 ),
			Interval.openClosed( 10, 15 ),
			Interval.open( 10, 15 ),

			Interval.closed( 0, 10 ),
			Interval.closedOpen( 0, 10 ),
			Interval.openClosed( 0, 10 ),
			Interval.open( 0, 10 ),

			Interval.closed( 5, 10 ),
			Interval.closedOpen( 5, 10 ),
			Interval.openClosed( 5, 10 ),
			Interval.open( 5, 10 ),

			Interval.closed( 10, 10 ),
			Interval.closedOpen( 10, 10 ),
			Interval.openClosed( 10, 10 ),
			Interval.open( 10, 10 ),

			Interval.closed( 0, 5 ),
			Interval.closedOpen( 0, 5 ),
			Interval.openClosed( 0, 5 ),
			Interval.open( 0, 5 ),

			Interval.closed( 0, 0 ),
			Interval.closedOpen( 0, 0 ),
			Interval.openClosed( 0, 0 ),
			Interval.open( 0, 0 )
		);

		intervals = stream.collect(
				toMap(Object::toString,Function.identity())
		);

	}

	/**
	 * Test cases factory method.
	 *
	 * @return all test intervals.
	 */
	public static Collection<Interval<Integer>> allIntervals()
	{

		return intervals.values();

	}

}

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
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.nerd4j.utils.lang.RequirementFailure;
import org.nerd4j.utils.test.ObjectOverridesContract;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for the class {@link PrimeSieve}.
 *
 * @author Massimo Coluzzi
 */
@DisplayName("Testing class: PrimeSieve")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class PrimeSieveTests implements ObjectOverridesContract<PrimeSieve>
{


	/* **************************************************** */
	/*  Implementations of ObjectOverridesContract methods  */
	/* **************************************************** */


	/**
	 * {@inheritDoc}
	 */
	@Override
	public PrimeSieve sampleValue()
	{
		return PrimeSieve.get();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PrimeSieve notEqualValue()
	{
		return PrimeSieve.withUpperLimit( 1000 );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PrimeSieve equalButNotSameValue()
	{
		return PrimeSieve.get();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PrimeSieve withDifferentHashcode()
	{
		return PrimeSieve.withUpperLimit( 1000 );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String sampleToString()
	{
		return "PrimeSieve( [0," + PrimeSieve.MAX_VALUE + ") )";
	}


	/* ************** */
	/*  TEST METHODS  */
	/* ************** */


	/**
	 * Performs unit tests on factory methods
	 * that are not factory methods.
	 */
	@Nested
	@DisplayName("on invoking factory methods")
	public class FactoryMethods
	{

		@Test
		public void method_get_should_create_an_instance_with_default_values()
		{

			final PrimeSieve sieve = PrimeSieve.get();

			assertNotNull( sieve );
			assertEquals( PrimeSieve.MAX_VALUE, sieve.getMaxValue() );
			assertEquals( 3072, sieve.getGreatestComputedValue() );
			assertEquals( 196, sieve.getCurrentMemoryConsumptionInBytes() );

		}

		@Test
		public void method_get_should_always_create_a_new_instance()
		{

			final PrimeSieve sieve1 = PrimeSieve.get();
			final PrimeSieve sieve2 = PrimeSieve.get();

			assertEquals( sieve1, sieve2 );
			assertNotSame( sieve1, sieve2 );

		}

		@ValueSource(longs={ -1, PrimeSieve.MAX_VALUE + 1 })
		@ParameterizedTest(name="PrimeSieve.withUpperLimit({0}) -> RequirementFailure")
		public void method_withUpperLimit_with_wrong_argument_should_throw_exception( long wrongArgument )
		{

			assertThrows(
				RequirementFailure.class,
				() -> PrimeSieve.withUpperLimit( wrongArgument )
			);

		}

		@CsvSource({
			"1000,1000,3072,196",
			"2000,2000,3072,196",
			"4000,4000,3072,196"
		})
		@ParameterizedTest(name="PrimeSieve.withUpperLimit({0}) -> <{1},{2},{3}>")
		public void method_withUpperLimit_should_create_an_instance_with_the_requested_limits(
			long limit, long maxValue, long greatestComputedValue, long memoryOccupation
		)
		{

			final PrimeSieve sieve = PrimeSieve.withUpperLimit( limit );

			assertNotNull( sieve );
			assertEquals( maxValue, sieve.getMaxValue() );
			assertEquals( greatestComputedValue, sieve.getGreatestComputedValue() );
			assertEquals( memoryOccupation, sieve.getCurrentMemoryConsumptionInBytes() );

		}

		@Test
		public void method_withUpperLimit_should_always_create_a_new_instance()
		{

			final PrimeSieve sieve1 = PrimeSieve.withUpperLimit( 1000 );
			final PrimeSieve sieve2 = PrimeSieve.withUpperLimit( 1000 );

			assertEquals( sieve1, sieve2 );
			assertNotSame( sieve1, sieve2 );

		}

		@ValueSource(ints={ -1, 0, 10, 50, 100, PrimeSieve.MIN_MEMORY_CONSUMPTION_IN_BYTES - 1 })
		@ParameterizedTest(name="PrimeSieve.withUpperLimit({0}) -> RequirementFailure")
		public void method_withMemoryLimit_with_wrong_argument_should_throw_exception( int wrongArgument )
		{

			assertThrows(
				RequirementFailure.class,
				() -> PrimeSieve.withMemoryLimit( wrongArgument )
			);

		}

		@CsvSource({
			"200,3072,3072,196",
			"1000,22272,3072,196",
			"1000000,23998272,3072,196",
			"1000000000,23999998272,3072,196",
			"1000000000000,412316859839,3072,196",
			"1000000000000000,412316859839,3072,196"
		})
		@ParameterizedTest(name="PrimeSieve.withMemoryLimit({0}) -> <{1},{2},{3}>")
		public void method_withMemoryLimit_should_create_an_instance_with_the_requested_limits(
			long limit, long maxValue, long greatestComputedValue, long memoryOccupation
		)
		{

			final PrimeSieve sieve = PrimeSieve.withMemoryLimit( limit );

			assertNotNull( sieve );
			assertEquals( maxValue, sieve.getMaxValue() );
			assertEquals( greatestComputedValue, sieve.getGreatestComputedValue() );
			assertEquals( memoryOccupation, sieve.getCurrentMemoryConsumptionInBytes() );

		}

		@Test
		public void method_withMemoryLimit_should_always_create_a_new_instance()
		{

			final PrimeSieve sieve1 = PrimeSieve.withMemoryLimit( 1000 );
			final PrimeSieve sieve2 = PrimeSieve.withMemoryLimit( 1000 );

			assertEquals( sieve1, sieve2 );
			assertNotSame( sieve1, sieve2 );

		}

	}

	/**
	 * Performs unit tests on the static methods
	 * that are not factory methods.
	 */
	@Nested
	@DisplayName("on invoking static methods")
	public class StaticMethods
	{

		/* Test method getMemoryOccupation */

		@ValueSource(longs = {-1, PrimeSieve.MAX_VALUE + 1})
		@ParameterizedTest(name = "PrimeSieve.getMemoryOccupation({0}) -> RequirementFailure")
		public void method_getMemoryOccupation_with_wrong_argument_should_throw_a_RequirementFailure(
				long wrongArgument
		)
		{

			assertThrows(
					RequirementFailure.class,
					() -> PrimeSieve.getMemoryConsumptionInBytes( wrongArgument )
			);

		}

		@CsvSource({
				"1000,196",
				"2000,196",
				"3000,196",
				"4000,228",
				"5000,276",
				"10000,484",
				"100000,4228",
				"1000000,41732",
				"1000000000,41666732",
				"100000000000,4166666732",
				"412316859839,17179869220"
		})
		@ParameterizedTest(name = "PrimeSieve.getMemoryOccupation({0}) -> {1}")
		public void method_getMemoryOccupation_should_return_as_expected(
				long upperLimit, long memoryOccupation
		)
		{

			assertEquals( memoryOccupation, PrimeSieve.getMemoryConsumptionInBytes( upperLimit ) );

		}


		/* Test method getGreatestComputableValue */

		@ValueSource(longs = {-1, 0, 10, 50, 100, PrimeSieve.MIN_MEMORY_CONSUMPTION_IN_BYTES - 1})
		@ParameterizedTest(name = "PrimeSieve.getGreatestComputableValue({0}) -> RequirementFailure")
		public void method_getGreatestComputableValue_with_wrong_argument_should_throw_a_RequirementFailure(
				long wrongArgument
		)
		{

			assertThrows(
					RequirementFailure.class,
					() -> PrimeSieve.getGreatestComputableValue( wrongArgument )
			);

		}

		@CsvSource({
				"200,3072",
				"400,7872",
				"1000,22272",
				"5000,118272",
				"10000,238272",
				"100000,2398272",
				"1000000,23998272",
				"1000000000,23999998272",
				"17179869244,412316859839",
				"100000000000,412316859839"
		})
		@ParameterizedTest(name = "PrimeSieve.getGreatestComputableValue({0}) -> {1}")
		public void method_getGreatestComputableValue_should_return_as_expected(
				long availableMemory, long greatestComputableValue
		)
		{

			assertEquals( greatestComputableValue, PrimeSieve.getGreatestComputableValue( availableMemory ) );

		}

	}


	/**
	 * Performs unit tests on the non static public methods.
	 */
	@Nested
	@DisplayName("on invoking public methods")
	public class MemberMethods
	{


		/* Test method isPrime */

		@ValueSource(longs={ -1, PrimeSieve.MAX_VALUE + 1 })
		@ParameterizedTest(name="PrimeSieve.isPrime({0}) -> RequirementFailure")
		public void method_isPrime_with_wrong_argument_should_throw_a_RequirementFailure(
			long wrongArgument
		)
		{

			assertThrows(
				RequirementFailure.class,
				() -> PrimeSieve.get().isPrime( wrongArgument )
			);

		}

		@Test
		public void method_isPrime_should_not_accept_values_greater_than_maxValue()
		{

			assertThrows(
				RequirementFailure.class,
				() -> PrimeSieve.withUpperLimit( 1000 ).isPrime( 1001 )
			);

		}

		@Test
		public void if_a_value_greater_than_getGreatestComputedValue_is_required_for_isPrime_then_the_memory_consumption_should_increase()
		{

			final PrimeSieve sieve = PrimeSieve.get();

			assertEquals( 196, sieve.getCurrentMemoryConsumptionInBytes() );
			assertEquals( 3072, sieve.getGreatestComputedValue() );

			assertEquals( isPrime(3073), sieve.isPrime(3073) );

			assertEquals( 324, sieve.getCurrentMemoryConsumptionInBytes() );
			assertEquals( 6144, sieve.getGreatestComputedValue() );

		}

		@Test
		public void method_isPrime_should_return_as_expected()
		{

			final PrimeSieve sieve = PrimeSieve.get();

			assertFalse( sieve.isPrime( 0 ) );
			assertFalse( sieve.isPrime( 1 ) );

			assertTrue( sieve.isPrime( 2 ) );

			final long limit = sieve.getGreatestComputedValue() << 1;
			for( int i = 3; i <= limit; ++i )
				assertEquals( isPrime( i ), sieve.isPrime( i ), "Inconsistent result for value " + i );

		}


		/* Test method getSmallestPrimeGreaterEqual */

		@ValueSource(longs={ -1, PrimeSieve.MAX_VALUE + 1 })
		@ParameterizedTest(name="PrimeSieve.getGreatestPrimeLessEqual({0}) -> RequirementFailure")
		public void method_getGreatestPrimeLessEqual_with_wrong_argument_should_throw_a_RequirementFailure(
			long wrongArgument
		)
		{

			assertThrows(
				RequirementFailure.class,
				() -> PrimeSieve.get().getGreatestPrimeLessEqual( wrongArgument )
			);

		}

		@Test
		public void method_getGreatestPrimeLessEqual_should_not_accept_values_greater_than_maxValue()
		{

			assertThrows(
				RequirementFailure.class,
				() -> PrimeSieve.withUpperLimit( 1000 ).getGreatestPrimeLessEqual( 1001 )
			);

		}

		@Test
		public void if_a_value_greater_than_getGreatestComputedValue_is_required_for_getGreatestPrimeLessEqual_then_the_memory_consumption_should_increase()
		{

			final PrimeSieve sieve = PrimeSieve.get();

			assertEquals( 196, sieve.getCurrentMemoryConsumptionInBytes() );
			assertEquals( 3072, sieve.getGreatestComputedValue() );

			final long prime = sieve.getGreatestPrimeLessEqual( 3080 );
			assertEquals( 3079, prime );
			assertTrue( isPrime(prime) );

			assertEquals( 324, sieve.getCurrentMemoryConsumptionInBytes() );
			assertEquals( 6144, sieve.getGreatestComputedValue() );

		}

		@CsvSource({
			"0,-1",
			"1,-1",
			"2,2",
			"3,3",
			"4,3",
			"5,5",
			"10,7",
			"100,97",
			"500,499",
			"1000,997",
			"2000,1999",
			"2818,2803",
			"3000,2999",
			"3010,3001",
			"3060,3049",
			"3070,3067",
			"3080,3079",
			"4228,4219"
		})
		@ParameterizedTest(name="PrimeSieve.getGreatestPrimeLessEqual({0}) -> {1}}")
		public void method_getGreatestPrimeLessEqual_should_return_as_expected(
			long value, long prime
		)
		{

			final PrimeSieve sieve = PrimeSieve.get();

			assertEquals( prime, sieve.getGreatestPrimeLessEqual(value) );
			if( prime != -1 )
				assertTrue( isPrime( prime ) );

		}

		/* Test method getSmallestPrimeGreaterEqual */

		@ValueSource(longs={ -1, PrimeSieve.MAX_VALUE + 1 })
		@ParameterizedTest(name="PrimeSieve.getSmallestPrimeGreaterEqual({0}) -> RequirementFailure")
		public void method_getSmallestPrimeGreaterEqual_with_wrong_argument_should_throw_a_RequirementFailure(
			long wrongArgument
		)
		{

			assertThrows(
				RequirementFailure.class,
				() -> PrimeSieve.get().getSmallestPrimeGreaterEqual( wrongArgument )
			);

		}

		@Test
		public void method_getSmallestPrimeGreaterEqual_should_not_accept_values_greater_than_maxValue()
		{

			assertThrows(
				RequirementFailure.class,
				() -> PrimeSieve.withUpperLimit( 1000 ).getSmallestPrimeGreaterEqual( 1001 )
			);

		}

		@Test
		public void if_a_value_greater_than_getGreatestComputedValue_is_required_for_getSmallestPrimeGreaterEqual_then_the_memory_consumption_should_increase()
		{

			final PrimeSieve sieve = PrimeSieve.get();

			assertEquals( 196, sieve.getCurrentMemoryConsumptionInBytes() );
			assertEquals( 3072, sieve.getGreatestComputedValue() );

			final long prime = sieve.getSmallestPrimeGreaterEqual( 3072 );
			assertEquals( 3079, prime );
			assertTrue( isPrime(prime) );

			assertEquals( 324, sieve.getCurrentMemoryConsumptionInBytes() );
			assertEquals( 6144, sieve.getGreatestComputedValue() );

		}

		@CsvSource({
			"0,2",
			"1,2",
			"2,2",
			"3,3",
			"4,5",
			"5,5",
			"10,11",
			"100,101",
			"500,503",
			"1000,1009",
			"2000,2003",
			"3000,3001",
			"3068,3079"
		})
		@ParameterizedTest(name="PrimeSieve.getSmallestPrimeGreaterEqual({0}) -> {1}}")
		public void method_getSmallestPrimeGreaterEqual_should_return_as_expected(
			long value, long prime
		)
		{

			final PrimeSieve sieve = PrimeSieve.get();

			assertEquals( prime, sieve.getSmallestPrimeGreaterEqual(value) );
			if( prime != -1 )
				assertTrue( isPrime( prime ) );

		}

		@ValueSource(longs={3068,3072})
		@ParameterizedTest(name="PrimeSieve.getSmallestPrimeGreaterEqual({0}) -> -1")
		public void if_maxPoolSize_has_been_reached_method_getSmallestPrimeGreaterEqual_should_stop_searching(
			long value
		)
		{

			/*
			 * This call creates a PrimeSieve with a pool size of 16 while
			 * the smallest prime greater then 3068 is in the block number 17.
			 * Since the maxPoolSize has been reached the sieve should return -1.
			 */
			final PrimeSieve sieve = PrimeSieve.withUpperLimit( 3072 );
			assertEquals( -1, sieve.getSmallestPrimeGreaterEqual(value) );

		}


		/**
		 * Checks if the given value is prime.
		 *
		 * @param value value to be checked.
		 * @return {@code true} if the value is prime.
		 */
		private boolean isPrime( long value )
		{

			if( value < 2 )
				return false;

			if( value == 2 )
				return true;

			if( value % 2 == 0 )
				return false;

			final long sqrt = Math.round( Math.sqrt(value) );
			for( long i = 3; i <= sqrt; i += 2 )
				if( value % i == 0 )
					return false;

			return true;

		}

	}

}

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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.nerd4j.utils.test.ComparableContract;
import org.nerd4j.utils.test.ObjectOverridesContract;

/**
 * Test suite for the class {@link IntervalLimit}
 *
 * @author Massimo Coluzzi
 */
@DisplayName("Testing class: IntervalLimit")
@DisplayNameGeneration(ReplaceUnderscores.class)
public class IntervalLimitTests
	   implements ObjectOverridesContract<IntervalLimit<Integer>>,
		          ComparableContract<IntervalLimit<Integer>>
{


	/* ******************* */
	/*  INTERFACE METHODS  */
	/* ******************* */


	/**
	 * {@inheritDoc}
	 */
	@Override
	public IntervalLimit<Integer> sampleValue()
	{

		return IntervalLimit.openSup( 0 );

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IntervalLimit<Integer> notEqualValue()
	{

		return IntervalLimit.closedSup( 0 );

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IntervalLimit<Integer> largerValue()
	{

		return IntervalLimit.closedSup( 0 );

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IntervalLimit<Integer> evenLargerValue()
	{

		return IntervalLimit.unboundedSup();

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IntervalLimit<Integer> equalButNotSameValue()
	{

		return IntervalLimit.openSup( 0 );

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IntervalLimit<Integer> withDifferentHashcode()
	{

		return IntervalLimit.closedSup( 0 );

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String sampleToString()
	{

		return "0)";

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
    	public void unboundedInf()
    	{
    		
    		final IntervalLimit<?> limit = IntervalLimit.unboundedInf();
    		assertAll(
				() -> assertNull( limit.value ),
				() -> assertFalse( limit.isOpen() ),
				() -> assertFalse( limit.isClosed() ),
				() -> assertTrue( limit.isUnbounded() ),
				() -> assertEquals( "(-\u221e", limit.toString() )
			);

    	}

    	@Test
    	public void closedInf()
    	{

    		final IntervalLimit<?> limit = IntervalLimit.closedInf( 0 );
			assertAll(
				() -> assertEquals( 0, limit.value ),
				() -> assertFalse( limit.isOpen() ),
				() -> assertTrue( limit.isClosed() ),
				() -> assertFalse( limit.isUnbounded() ),
				() -> assertEquals( "[0", limit.toString() )
			);

    	}

    	@Test
    	public void openInf()
    	{

    		final IntervalLimit<?> limit = IntervalLimit.openInf( 0 );
    		assertAll(
				() -> assertEquals( 0, limit.value ),
    			() -> assertTrue( limit.isOpen() ),
    			() -> assertFalse( limit.isClosed() ),
    			() -> assertFalse( limit.isUnbounded() ),
    			() -> assertEquals( "(0", limit.toString() )
    		);

    	}

    	@Test
    	public void openSup()
    	{

    		final IntervalLimit<?> limit = IntervalLimit.openSup( 0 );
    		assertAll(
				() -> assertEquals( 0, limit.value ),
    			() -> assertTrue( limit.isOpen() ),
    			() -> assertFalse( limit.isClosed() ),
    			() -> assertFalse( limit.isUnbounded() ),
    			() -> assertEquals( "0)", limit.toString() )
    		);

    	}

    	@Test
    	public void closedSup()
    	{

    		final IntervalLimit<?> limit = IntervalLimit.closedSup( 0 );
    		assertAll(
    			() -> assertEquals( 0, limit.value ),
    			() -> assertFalse( limit.isOpen() ),
    			() -> assertTrue( limit.isClosed() ),
    			() -> assertFalse( limit.isUnbounded() ),
    			() -> assertEquals( "0]", limit.toString() )
    		);

    	}

    	@Test
    	public void unboundedSup()
    	{

    		final IntervalLimit<?> limit = IntervalLimit.unboundedSup();
    		assertAll(
    			() -> assertNull( limit.value ),
    			() -> assertFalse( limit.isOpen() ),
    			() -> assertFalse( limit.isClosed() ),
    			() -> assertTrue( limit.isUnbounded() ),
    			() -> assertEquals( "+\u221e)", limit.toString() )
			);

    	}

    }


	/**
	 * Performs unit test on method {@link IntervalLimit#toString()}.
	 */
	@Nested
	@DisplayName("on method compareTo")
	public class CompareTo
	{

		@CsvSource({
			"(-\u221e,=,(-\u221e",
			"(-\u221e,<,[0",
			"(-\u221e,<,(0",
			"(-\u221e,<,0)",
			"(-\u221e,<,0]",
			"(-\u221e,<,+\u221e)"
		})
		@ParameterizedTest(name="{0} {1} {2}")
		public void unboundedInf(
			String a, String condition, String b
		)
		{

			final IntervalLimit<Integer> limitA = limits.get( a );
			final IntervalLimit<Integer> limitB = limits.get( b );

			final int comparison = limitA.compareTo( limitB );

			assertTrue( checks.get(condition).apply(comparison) );

		}

		@CsvSource({
			"[0,>,(-\u221e",
			"[0,=,[0",
			"[0,<,[1",
			"[0,<,(0",
			"[0,<,(1",
			"[0,>,0)",
			"[0,<,1)",
			"[0,=,0]",
			"[0,<,1]",
			"[0,<,+\u221e)"
		})
		@ParameterizedTest(name="{0} {1} {2}")
		public void closedInf0(
			String a, String condition, String b
		)
		{

			final IntervalLimit<Integer> limitA = limits.get( a );
			final IntervalLimit<Integer> limitB = limits.get( b );

			final int comparison = limitA.compareTo( limitB );

			assertTrue( checks.get(condition).apply(comparison) );

		}

		@CsvSource({
			"(0,>,(-\u221e",
			"(0,>,[0",
			"(0,<,[1",
			"(0,=,(0",
			"(0,<,(1",
			"(0,>,0)",
			"(0,<,1)",
			"(0,>,0]",
			"(0,<,1]",
			"(0,<,+\u221e)"
		})
		@ParameterizedTest(name="{0} {1} {2}")
		public void openInf0(
			String a, String condition, String b
		)
		{

			final IntervalLimit<Integer> limitA = limits.get( a );
			final IntervalLimit<Integer> limitB = limits.get( b );

			final int comparison = limitA.compareTo( limitB );

			assertTrue( checks.get(condition).apply(comparison) );

		}

		@CsvSource({
			"0),>,(-\u221e",
			"0),<,[0",
			"0),<,[1",
			"0),<,(0",
			"0),<,(1",
			"0),=,0)",
			"0),<,1)",
			"0),<,0]",
			"0),<,1]",
			"0),<,+\u221e)"
		})
		@ParameterizedTest(name="{0} {1} {2}")
		public void openSup0(
			String a, String condition, String b
		)
		{

			final IntervalLimit<Integer> limitA = limits.get( a );
			final IntervalLimit<Integer> limitB = limits.get( b );

			final int comparison = limitA.compareTo( limitB );

			assertTrue( checks.get(condition).apply(comparison) );

		}

		@CsvSource({
			"0],>,(-\u221e",
			"0],=,[0",
			"0],<,[1",
			"0],<,(0",
			"0],<,(1",
			"0],>,0)",
			"0],<,1)",
			"0],=,0]",
			"0],<,1]",
			"0],<,+\u221e)"
		})
		@ParameterizedTest(name="{0} {1} {2}")
		public void closedSup0(
			String a, String condition, String b
		)
		{

			final IntervalLimit<Integer> limitA = limits.get( a );
			final IntervalLimit<Integer> limitB = limits.get( b );

			final int comparison = limitA.compareTo( limitB );

			assertTrue( checks.get(condition).apply(comparison) );

		}

		@CsvSource({
			"[1,>,(-\u221e",
			"[1,>,[0",
			"[1,=,[1",
			"[1,>,(0",
			"[1,<,(1",
			"[1,>,0)",
			"[1,>,1)",
			"[1,>,0]",
			"[1,=,1]",
			"[1,<,+\u221e)"
		})
		@ParameterizedTest(name="{0} {1} {2}")
		public void closedInf1(
			String a, String condition, String b
		)
		{

			final IntervalLimit<Integer> limitA = limits.get( a );
			final IntervalLimit<Integer> limitB = limits.get( b );

			final int comparison = limitA.compareTo( limitB );

			assertTrue( checks.get(condition).apply(comparison) );

		}

		@CsvSource({
			"(1,>,(-\u221e",
			"(1,>,[0",
			"(1,>,[1",
			"(1,>,(0",
			"(1,=,(1",
			"(1,>,0)",
			"(1,>,1)",
			"(1,>,0]",
			"(1,>,1]",
			"(1,<,+\u221e)"
		})
		@ParameterizedTest(name="{0} {1} {2}")
		public void openInf1(
			String a, String condition, String b
		)
		{

			final IntervalLimit<Integer> limitA = limits.get( a );
			final IntervalLimit<Integer> limitB = limits.get( b );

			final int comparison = limitA.compareTo( limitB );

			assertTrue( checks.get(condition).apply(comparison) );

		}

		@CsvSource({
			"1),>,(-\u221e",
			"1),>,[0",
			"1),<,[1",
			"1),>,(0",
			"1),<,(1",
			"1),>,0)",
			"1),=,1)",
			"1),>,0]",
			"1),<,1]",
			"1),<,+\u221e)"
		})
		@ParameterizedTest(name="{0} {1} {2}")
		public void openSup1(
			String a, String condition, String b
		)
		{

			final IntervalLimit<Integer> limitA = limits.get( a );
			final IntervalLimit<Integer> limitB = limits.get( b );

			final int comparison = limitA.compareTo( limitB );

			assertTrue( checks.get(condition).apply(comparison) );

		}

		@CsvSource({
			"1],>,(-\u221e",
			"1],>,[0",
			"1],=,[1",
			"1],>,(0",
			"1],<,(1",
			"1],>,0)",
			"1],>,1)",
			"1],>,0]",
			"1],=,1]",
			"1],<,+\u221e)"
		})
		@ParameterizedTest(name="{0} {1} {2}")
		public void closedSup1(
			String a, String condition, String b
		)
		{

			final IntervalLimit<Integer> limitA = limits.get( a );
			final IntervalLimit<Integer> limitB = limits.get( b );

			final int comparison = limitA.compareTo( limitB );

			assertTrue( checks.get(condition).apply(comparison) );

		}

		@CsvSource({
			"+\u221e),>,(-\u221e",
			"+\u221e),>,[0",
			"+\u221e),>,(0",
			"+\u221e),>,0)",
			"+\u221e),>,0]",
			"+\u221e),=,+\u221e)"
		})
		@ParameterizedTest(name="{0} {1} {2}")
		public void unboundedSup(
			String a, String condition, String b
		)
		{

			final IntervalLimit<Integer> limitA = limits.get( a );
			final IntervalLimit<Integer> limitB = limits.get( b );

			final int comparison = limitA.compareTo( limitB );

			assertTrue( checks.get(condition).apply(comparison) );

		}

	}


	/**
	 * Performs unit test on method {@link IntervalLimit#contains(Comparable)}.
	 */
	@Nested
	@DisplayName("on method contains")
	public class Contains
	{

		@CsvSource({
			"(-\u221e,true",
			"[0,false",
			"(0,false",
			"0),true",
			"0],true",
			"+\u221e),true"
		})
		@ParameterizedTest(name="{0}.contains(" + Integer.MIN_VALUE + ") = {1}")
		public void contains_Integer_MIN_VALUE( String type, boolean contains )
		{

			final IntervalLimit<Integer> limit = limits.get( type );
			assertEquals( contains, limit.contains(Integer.MIN_VALUE) );

		}

		@CsvSource({
			"(-\u221e,true",
			"[0,true",
			"(0,false",
			"0),false",
			"0],true",
			"+\u221e),true"
		})
		@ParameterizedTest(name="{0}.contains(0) = {1}")
		public void contains_0( String type, boolean contains )
		{

			final IntervalLimit<Integer> limit = limits.get( type );
			assertEquals( contains, limit.contains(0) );

		}


		@CsvSource({
			"(-\u221e,true",
			"[0,true",
			"(0,true",
			"0),false",
			"0],false",
			"+\u221e),true"
		})
		@ParameterizedTest(name="{0}.contains(" + Integer.MAX_VALUE + ") = {1}")
		public void contains_Integer_MAX_VALUE( String type, boolean contains )
		{

			final IntervalLimit<Integer> limit = limits.get( type );
			assertEquals( contains, limit.contains(Integer.MAX_VALUE) );

		}


	}


	/** Checks to be performed. */
	private static final Map<String,Function<Integer,Boolean>> checks;

	/** Limits to use for test. */
	private static final Map<String,IntervalLimit<Integer>> limits;

	static
	{

		final Stream<IntervalLimit<Integer>> stream
		= Stream.of(
			IntervalLimit.unboundedInf(),
			IntervalLimit.closedInf( 0 ),
			IntervalLimit.closedInf( 1 ),
			IntervalLimit.openInf( 0 ),
			IntervalLimit.openInf( 1 ),
			IntervalLimit.openSup( 0 ),
			IntervalLimit.openSup( 1 ),
			IntervalLimit.closedSup( 0 ),
			IntervalLimit.closedSup( 1 ),
			IntervalLimit.unboundedSup()
		);

		limits = stream.collect(
			toMap(Object::toString,Function.identity())
		);

		checks = new HashMap<>();
		checks.put( "<", v -> v  < 0 );
		checks.put( "=", v -> v == 0 );
		checks.put( ">", v -> v  > 0 );

	}

}

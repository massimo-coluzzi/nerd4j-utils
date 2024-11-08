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
package org.nerd4j.utils.tuple;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Comparator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.nerd4j.utils.math.CU;
import org.nerd4j.utils.test.ComparableContract;
import org.nerd4j.utils.test.ObjectOverridesContract;

/**
 * Test suite for the class {@link ComparableTriple}
 *
 * @author Massimo Coluzzi
 */
@DisplayName("Testing class: ComparableTriple")
@DisplayNameGeneration(ReplaceUnderscores.class)
public class ComparableTripleTests<L,M,R>
	   implements ObjectOverridesContract<ComparableTriple<L,M,R>>,
	   			  ComparableContract<ComparableTriple<L,M,R>>

{


	/* ******************* */
	/*  INTERFACE METHODS  */
	/* ******************* */


	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public ComparableTriple<L,M,R> sampleValue()
	{

		return (ComparableTriple<L,M,R>) ComparableTriple.of( 11, 13, 17 );

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public ComparableTriple<L,M,R> notEqualValue()
	{

		return (ComparableTriple<L,M,R>) ComparableTriple.of( 11L, 13L, 17L );

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public ComparableTriple<L,M,R> equalButNotSameValue()
	{
		return (ComparableTriple<L,M,R>) ComparableTriple.of( 11, 13, 17 );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public ComparableTriple<L,M,R> withDifferentHashcode()
	{
		return (ComparableTriple<L,M,R>) ComparableTriple.empty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String sampleToString()
	{
		return "<11, 13, 17>";
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public ComparableTriple<L,M,R> largerValue()
	{
		return (ComparableTriple<L,M,R>) ComparableTriple.of( 11, 13, 19 );
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public ComparableTriple<L,M,R> evenLargerValue()
	{
		return (ComparableTriple<L,M,R>) ComparableTriple.of( 11, 17, 13 );
	}
	
	
	/* ************** */
	/*  TEST METHODS  */
	/* ************** */
	
	
	/**
     * Performs unit test on empty {@link ComparableTriple}.
     */
    @Nested
    @DisplayName("if the ComparableTriple is empty")
    public class EmptyComparableTriple
    {
    	
    	@Test
    	public void triple_returned_by_static_method_empty_should_be_empty()
    	{
    		
    		final ComparableTriple<?,?,?> triple = ComparableTriple.empty();
    		
    		assertTrue( triple.isEmpty() );
    		assertNull( triple.getLeft() );
    		assertNull( triple.getRight() );
    		assertNull( triple.getMiddle() );
    		
    	}
    	
    	@Test
    	public void all_empty_triples_should_be_the_same_instance()
    	{
    		
    		final ComparableTriple<?,?,?> triple = ComparableTriple.of( null, null, null );
    		
    		assertSame( ComparableTriple.empty(), triple );
    		
    	}
    	
    	@Test
    	public void the_hash_code_of_an_empty_triple_is_0()
    	{
    		
    		assertEquals( 0, ComparableTriple.empty().hashCode() );
    		
    	}

		@Test
		public void empty_triple_should_have_default_comparators()
		{

			final ComparableTriple<?,?,?> triple = ComparableTriple.empty();

			assertSame(  CU.nullFirstNaturalOrderComparator(), triple.getLeftComparator() );
			assertSame(  CU.nullFirstNaturalOrderComparator(), triple.getRightComparator() );

		}
    	
    }
    
    /**
     * Performs unit test on non empty {@link ComparableTriple}.
     */
    @Nested
    @DisplayName("if the ComparableTriple is not empty")
    public class NonEmptyComparableTriple
	{

		@Test
		@DisplayName("each invocation of the method 'of' creates a new instance")
		public void each_invocation_of_method_of_creates_a_new_instance()
		{

			final ComparableTriple<?,?,?> triple1 = ComparableTriple.of( "left", "middle", "right" );
			final ComparableTriple<?,?,?> triple2 = ComparableTriple.of( "left", "middle", "right" );

			assertEquals( triple1, triple2 );
			assertNotSame( triple1, triple2 );

		}

		@CsvSource(value={
    		"null,null,null",
    		"null,null,right",
    		"null,middle,null",
    		"null,middle,right",
			"left,null,null",
    		"left,null,right",
    		"left,middle,null",
    		"left,middle,right"
    	},nullValues="null")
    	@ParameterizedTest(name = "of({0},{1},{2}) => <{0},{1},{2}>")
    	public void method_of_should_create_the_ComparableTriple_as_expected( String left, String middle, String right )
    	{
    		
    		final ComparableTriple<?,?,?> triple = ComparableTriple.of( left, middle, right );
    		
    		assertSame( left, triple.getLeft() );
    		assertSame( right, triple.getRight() );
    		assertSame( middle, triple.getMiddle() );
    		
    	}

		@Test
		@DisplayName("method 'of(L,M,R)' should inject default comparators")
		public void default_method_of_should_inject_default_comparators()
		{

			final ComparableTriple<?,?,?> triple = ComparableTriple.of( "left", "middle", "right" );

			assertSame( CU.nullFirstNaturalOrderComparator(), triple.getLeftComparator() );
			assertSame( CU.nullFirstNaturalOrderComparator(), triple.getRightComparator() );
			assertSame( CU.nullFirstNaturalOrderComparator(), triple.getMiddleComparator() );

		}

		@Test
		@DisplayName("method 'of(L,boolean,M,boolean,R,boolean)' should inject comparators accordingly")
		public void method_of_with_null_sorting_should_inject_comparators_accordingly()
		{

			final ComparableTriple<?,?,?> triple = ComparableTriple.of(
				"left", true, "middle", true, "right", false
			);

			assertSame( CU.nullLastNaturalOrderComparator(),  triple.getLeftComparator() );
			assertSame( CU.nullFirstNaturalOrderComparator(), triple.getRightComparator() );
			assertSame( CU.nullLastNaturalOrderComparator(),  triple.getMiddleComparator() );

		}

		@Test
		@DisplayName("method 'of(L,Comparator<L>,M,Comparator<M>,R,Comparator<R>)' should inject comparators as expected")
		public void method_of_should_inject_comparators_as_expected()
		{

			final Comparator<Object> leftComparator   = Comparator.comparingInt( a -> a.toString().length() );
			final Comparator<Object> middleComparator = Comparator.comparingInt( a -> a.hashCode() );
			final Comparator<Object> rightComparator  = Comparator.comparing( Object::toString );
			final ComparableTriple<?,?,?> triple = ComparableTriple.of(
				new Object(), leftComparator,
				new Object(), middleComparator,
				new Object(), rightComparator
			);

			assertSame( leftComparator, triple.getLeftComparator() );
			assertSame( rightComparator, triple.getRightComparator() );
			assertSame( middleComparator, triple.getMiddleComparator() );

		}

		@CsvSource(value={
    		"null,null,null,true",
    		"null,null,right,false",
    		"null,middle,null,false",
    		"null,middle,right,false",
			"left,null,null,false",
    		"left,null,right,false",
    		"left,middle,null,false",
    		"left,middle,right,false"
    	},nullValues="null")
    	@ParameterizedTest(name = "<{0},{1},{2}>.isEmpty() => {3}")
    	public void method_isEmpty_should_return_as_expected( String left, String middle, String right, boolean expected )
    	{
    		
    		final ComparableTriple<?,?,?> triple = ComparableTriple.of( left, middle, right );
    		
    		assertEquals( expected, triple.isEmpty() );
    		
    	}

	}

	/**
	 * Performs unit test between {@link Triple} and {@link ComparableTriple}.
	 */
	@Nested
	@DisplayName("in relation with Triple")
	public class ComparableTripleVsTriple
	{

		@Test
		public void Triple_and_ComparableTriple_should_not_equal()
		{

			final Triple<String,String,String> triple = Triple.of( "left", "middle", "right" );
			final ComparableTriple<String,String,String> comparableTriple = ComparableTriple.of( "left", "middle", "right" );

			assertSame( triple.getLeft(), comparableTriple.getLeft() );
			assertSame( triple.getRight(), comparableTriple.getRight() );
			assertSame( triple.getMiddle(), comparableTriple.getMiddle() );
			assertNotEquals( triple, comparableTriple );

		}

		@Test
		public void method_compareTo_should_work_as_expected()
		{
			assertTrue( CU.lt(ComparableTriple.of(1,2,3),ComparableTriple.of(3,3,3)) );
			assertTrue( CU.lt(ComparableTriple.of(1,3,3),ComparableTriple.of(3,3,3)) );
			assertTrue( CU.lt(ComparableTriple.of(3,2,3),ComparableTriple.of(3,3,3)) );
			assertTrue( CU.eq(ComparableTriple.of(3,3,3),ComparableTriple.of(3,3,3)) );
			assertTrue( CU.gt(ComparableTriple.of(3,3,3),ComparableTriple.of(3,3,1)) );
			assertTrue( CU.gt(ComparableTriple.of(3,3,3),ComparableTriple.of(3,2,3)) );
			assertTrue( CU.gt(ComparableTriple.of(3,3,3),ComparableTriple.of(3,2,1)) );
		}

		@Test
        @DisplayName("method 'of(null)' return null")
    	public void if_source_Triple_is_null_method_of_should_return_null()
    	{

    		assertNull( ComparableTriple.of(null) );
    		assertNull( ComparableTriple.of(null,true,true,true) );
    		assertNull( ComparableTriple.of(null,
				CU.nullLastNaturalOrderComparator(),
				CU.nullLastNaturalOrderComparator(),
				CU.nullLastNaturalOrderComparator())
			);

    	}

		@Test
        @DisplayName("method 'of(Triple)' should inject default comparators")
    	public void default_method_of_should_inject_default_comparators()
    	{

    		final Triple<String,String,String> triple = Triple.of( "left", "middle", "right" );
    		final ComparableTriple<String,String,String> comparableTriple = ComparableTriple.of( triple );

    		assertSame( "left", comparableTriple.getLeft() );
    		assertSame( "right", comparableTriple.getRight() );
    		assertSame( "middle", comparableTriple.getMiddle() );
    		assertSame(  CU.nullFirstNaturalOrderComparator(), comparableTriple.getLeftComparator() );
			assertSame(  CU.nullFirstNaturalOrderComparator(), comparableTriple.getRightComparator() );
			assertSame(  CU.nullFirstNaturalOrderComparator(), comparableTriple.getMiddleComparator() );

    	}

    	@Test
        @DisplayName("method 'of(Triple,boolean,boolean,boolean)' with should inject comparators accordingly")
    	public void method_of_with_null_sorting_should_inject_comparators_accordingly()
    	{

			final Triple<String,String,String> triple = Triple.of( "left", "middle", "right" );
			final ComparableTriple<String,String,String> comparableTriple
			= ComparableTriple.of( triple, true, true, false );

			assertSame( "left", comparableTriple.getLeft() );
			assertSame( "right", comparableTriple.getRight() );
			assertSame( "middle", comparableTriple.getMiddle() );
    		assertSame(  CU.nullLastNaturalOrderComparator(),  comparableTriple.getLeftComparator() );
			assertSame(  CU.nullFirstNaturalOrderComparator(), comparableTriple.getRightComparator() );
			assertSame(  CU.nullLastNaturalOrderComparator(),  comparableTriple.getMiddleComparator() );

    	}

    	@Test
        @DisplayName("method 'of(Triple<L,M,R>,Comparator<L>,Comparator<M>,Comparator<R>)' should inject comparators as expected")
    	public void method_of_should_inject_comparators_as_expected()
    	{

    		final Object left   = new Object();
    		final Object right  = new Object();
    		final Object middle = new Object();
    		final Comparator<Object> leftComparator   = Comparator.comparingInt( a -> a.toString().length() );
    		final Comparator<Object> middleComparator = Comparator.comparingInt( a -> a.hashCode() );
    		final Comparator<Object> rightComparator  = Comparator.comparing( Object::toString );

			final Triple<Object,Object,Object> triple = Triple.of( left, middle, right );
    		final ComparableTriple<?,?,?> comparableTriple
			= ComparableTriple.of( triple, leftComparator, middleComparator, rightComparator );

			assertSame( left,  comparableTriple.getLeft() );
			assertSame( right, comparableTriple.getRight() );
			assertSame( middle, comparableTriple.getMiddle() );
    		assertSame(  leftComparator,   comparableTriple.getLeftComparator() );
			assertSame(  rightComparator,  comparableTriple.getRightComparator() );
			assertSame(  middleComparator, comparableTriple.getMiddleComparator() );

    	}

    }


    /**
	 * Performs unit test for {@link ComparableTriple.FinalBuilder}.
	 */
	@Nested
	@DisplayName("by using the Builders")
	public class ComparableTripleBuilders
	{

		@Test
		public void injecting_null_values_should_create_an_empty_triple()
		{

			final ComparableTriple<String,String,String> triple = ComparableTriple
				.withLeft( (String) null )
				.withMiddle( (String) null )
				.andRight( null );

			assertSame( ComparableTriple.empty(), triple );

		}

		@Test
    	public void to_use_the_Builder_should_be_the_same_as_to_use_the_factory_methods_1()
    	{

    		final Object left = new Object();
    		
    		final Comparator<Object> leftComparator
    		= Comparator.comparingInt( a -> a.toString().length() );
    		
    		final ComparableTriple<Object,Integer,String> withBuilder
    		= ComparableTriple
				.withLeft( left, leftComparator )
				.withMiddle( 1 )
				.andRight( "right", true );

    		final ComparableTriple<Object,Integer,String> withFactoryMethod
    		= ComparableTriple.of(
    			left, leftComparator,
    			1, CU.nullFirstNaturalOrderComparator(),
    			"right", CU.nullLastNaturalOrderComparator()
			);

    		assertEquals( withBuilder, withFactoryMethod );
    		assertSame( withBuilder.getLeftComparator(), withFactoryMethod.getLeftComparator() );
    		assertSame( withBuilder.getRightComparator(), withFactoryMethod.getRightComparator() );
    		assertSame( withBuilder.getMiddleComparator(), withFactoryMethod.getMiddleComparator() );

    	}

    	@Test
    	public void to_use_the_Builder_should_be_the_same_as_to_use_the_factory_methods_2()
    	{

    		final Object right = new Object();
    		final Triple<String,Integer,Object> source = Triple.of( "left", 1, right );
    		
    		final Comparator<Object> rightComparator
    		= Comparator.comparingInt( a -> a.toString().length() );
    		
    		final ComparableTriple<String,Integer,Object> withBuilder
    		= ComparableTriple
				.withLeft( "left", true )
				.withMiddle( 1 )
				.andRight( right, rightComparator );

    		final ComparableTriple<String,Integer,Object> withFactoryMethod
    		= ComparableTriple.of(
    			source, CU.nullLastNaturalOrderComparator(),
				CU.nullFirstNaturalOrderComparator(), rightComparator
			);

    		assertEquals( withBuilder, withFactoryMethod );
    		assertSame( withBuilder.getLeftComparator(), withFactoryMethod.getLeftComparator() );
    		assertSame( withBuilder.getRightComparator(), withFactoryMethod.getRightComparator() );
    		assertSame( withBuilder.getMiddleComparator(), withFactoryMethod.getMiddleComparator() );

    	}

    }

}

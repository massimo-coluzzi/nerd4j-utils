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

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for the class {@link ComparablePair}
 *
 * @author Massimo Coluzzi
 */
@DisplayName("Testing class: ComparablePair")
@DisplayNameGeneration(ReplaceUnderscores.class)
public class ComparablePairTests<L,R>
	   implements ObjectOverridesContract<ComparablePair<L,R>>,
	   			  ComparableContract<ComparablePair<L,R>>

{


	/* ******************* */
	/*  INTERFACE METHODS  */
	/* ******************* */


	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public ComparablePair<L,R> sampleValue()
	{

		return (ComparablePair<L,R>) ComparablePair.of( 11, 13 );

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public ComparablePair<L,R> notEqualValue()
	{

		return (ComparablePair<L,R>) ComparablePair.of( 11L, 13L );

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public ComparablePair<L,R> equalButNotSameValue()
	{
		return (ComparablePair<L,R>) ComparablePair.of( 11, 13 );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public ComparablePair<L,R> withDifferentHashcode()
	{
		return (ComparablePair<L,R>) ComparablePair.empty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String sampleToString()
	{
		return "<11, 13>";
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public ComparablePair<L,R> largerValue()
	{
		return (ComparablePair<L,R>) ComparablePair.of( 11, 15 );
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public ComparablePair<L,R> evenLargerValue()
	{
		return (ComparablePair<L,R>) ComparablePair.of( 12, 13 );
	}
	
	
	/* ************** */
	/*  TEST METHODS  */
	/* ************** */
	
	
	/**
     * Performs unit test on empty {@link ComparablePair}.
     */
    @Nested
    @DisplayName("if the ComparablePair is empty")
    public class EmptyComparablePair
    {
    	
    	@Test
    	public void pair_returned_by_static_method_empty_should_be_empty()
    	{
    		
    		final ComparablePair<?,?> pair = ComparablePair.empty();
    		
    		assertTrue( pair.isEmpty() );
    		assertNull( pair.getKey() );
    		assertNull( pair.getLeft() );
    		assertNull( pair.getRight() );
    		assertNull( pair.getValue() );
    		
    	}
    	
    	@Test
    	public void all_empty_pairs_should_be_the_same_instance()
    	{
    		
    		final ComparablePair<?,?> pair = ComparablePair.of( null, null );
    		
    		assertSame( ComparablePair.empty(), pair );
    		
    	}
    	
    	@Test
    	public void the_hash_code_of_an_empty_pair_is_0()
    	{
    		
    		assertEquals( 0, ComparablePair.empty().hashCode() );
    		
    	}

		@Test
		public void empty_pair_should_have_default_comparators()
		{

			final ComparablePair<?,?> pair = ComparablePair.empty();

			assertSame(  CU.nullFirstNaturalOrderComparator(), pair.getLeftComparator() );
			assertSame(  CU.nullFirstNaturalOrderComparator(), pair.getRightComparator() );

		}
    	
    }
    
    /**
     * Performs unit test on non empty {@link ComparablePair}.
     */
    @Nested
    @DisplayName("if the ComparablePair is not empty")
    public class NonEmptyComparablePair
	{

		@CsvSource(value={
    		"null,null",
    		"left,null",
    		"null,right",
    		"left,right"
    	},nullValues="null")
    	@ParameterizedTest(name = "of({0},{1}) => <{0},{1}>")
    	public void method_of_should_create_the_ComparablePair_as_expected( String left, String right )
    	{
    		
    		final ComparablePair<?,?> pair = ComparablePair.of( left, right );
    		
    		assertSame( left, pair.getLeft() );
    		assertSame( right, pair.getRight() );
    		
    	}

		@CsvSource(value = {
				"null,null",
				"left,null",
				"null,right",
				"left,right"
		}, nullValues = "null")
		@ParameterizedTest(name = "getKey() and getLeft() should both return {0}")
		public void getKey_and_getLeft_should_return_the_same_value( String left, String right )
		{

			final ComparablePair<?, ?> pair = ComparablePair.of( left, right );

			assertSame( pair.getKey(), pair.getLeft() );

		}

		@CsvSource(value = {
				"null,null",
				"left,null",
				"null,right",
				"left,right"
		}, nullValues = "null")
		@ParameterizedTest(name = "getRight() and getValue() should both return {0}")
		public void getRight_and_getValue_should_return_the_same_value( String left, String right )
		{

			final ComparablePair<?, ?> pair = ComparablePair.of( left, right );

			assertSame( pair.getRight(), pair.getValue() );

		}

		@Test
		@DisplayName("each invocation of the method 'of' creates a new instance")
		public void each_invocation_of_method_of_creates_a_new_instance()
		{

			final ComparablePair<?, ?> pair1 = ComparablePair.of( "left", "right" );
			final ComparablePair<?, ?> pair2 = ComparablePair.of( "left", "right" );

			assertEquals( pair1, pair2 );
			assertNotSame( pair1, pair2 );

		}

		@Test
		@DisplayName("method 'of(L,R)' should inject default comparators")
		public void default_method_of_should_inject_default_comparators()
		{

			final ComparablePair<?, ?> pair = ComparablePair.of( "left", "right" );

			assertSame( CU.nullFirstNaturalOrderComparator(), pair.getLeftComparator() );
			assertSame( CU.nullFirstNaturalOrderComparator(), pair.getRightComparator() );

		}

		@Test
		@DisplayName("method 'of(L,boolean,R,boolean)' should inject comparators accordingly")
		public void method_of_with_null_sorting_should_inject_comparators_accordingly()
		{

			final ComparablePair<?, ?> pair = ComparablePair.of( "left", true, "right", false );

			assertSame( CU.nullLastNaturalOrderComparator(),  pair.getLeftComparator() );
			assertSame( CU.nullFirstNaturalOrderComparator(), pair.getRightComparator() );

		}

		@Test
		@DisplayName("method 'of(L,Comparator<L>,R,Comparator<R>)' should inject comparators as expected")
		public void method_of_should_inject_comparators_as_expected()
		{

			final Comparator<Object> leftComparator = Comparator.comparingInt( a -> a.toString().length() );
			final Comparator<Object> rightComparator = Comparator.comparing( Object::toString );
			final ComparablePair<?, ?> pair = ComparablePair.of(
					new Object(), leftComparator,
					new Object(), rightComparator );

			assertSame( leftComparator, pair.getLeftComparator() );
			assertSame( rightComparator, pair.getRightComparator() );

		}

		@CsvSource(value={
    		"null,null,true",
    		"left,null,false",
    		"null,right,false",
    		"left,right,false"
    	},nullValues="null")
    	@ParameterizedTest(name = "<{0},{1}>.isEmpty() => {2}")
    	public void method_isEmpty_should_return_as_expected( String left, String right, boolean expected )
    	{
    		
    		final ComparablePair<?,?> pair = ComparablePair.of( left, right );
    		
    		assertEquals( expected, pair.isEmpty() );
    		
    	}

	}

	/**
	 * Performs unit test between {@link Pair} and {@link ComparablePair}.
	 */
	@Nested
	@DisplayName("in relation with Pair")
	public class ComparablePairVsPair
	{

		@Test
		public void Pair_and_ComparablePair_should_not_equal()
		{

			final Pair<String,String> pair = Pair.of( "left", "right" );
			final ComparablePair<String,String> comparablePair = ComparablePair.of( "left", "right" );

			assertSame( pair.getLeft(), comparablePair.getLeft() );
			assertSame( pair.getRight(), comparablePair.getRight() );
			assertNotEquals( pair, comparablePair );

		}

		@Test
        @DisplayName("method 'of(null)' return null")
    	public void if_source_Pair_is_null_method_of_should_return_null()
    	{

    		assertNull( ComparablePair.of(null) );
    		assertNull( ComparablePair.of(null,true,false) );
    		assertNull( ComparablePair.of(null,CU.nullLastNaturalOrderComparator(),CU.nullLastNaturalOrderComparator()) );

    	}

		@Test
        @DisplayName("method 'of(Pair)' should inject default comparators")
    	public void default_method_of_should_inject_default_comparators()
    	{

    		final Pair<String,String> pair = Pair.of( "left", "right" );
    		final ComparablePair<String,String> comparablePair = ComparablePair.of( pair );

    		assertSame( "left", comparablePair.getLeft() );
    		assertSame( "right", comparablePair.getRight() );
    		assertSame(  CU.nullFirstNaturalOrderComparator(), comparablePair.getLeftComparator() );
			assertSame(  CU.nullFirstNaturalOrderComparator(), comparablePair.getRightComparator() );

    	}

    	@Test
        @DisplayName("method 'of(Pair,boolean,boolean)' with should inject comparators accordingly")
    	public void method_of_with_null_sorting_should_inject_comparators_accordingly()
    	{

			final Pair<String,String> pair = Pair.of( "left", "right" );
			final ComparablePair<String,String> comparablePair = ComparablePair.of( pair, true, false );

			assertSame( "left", comparablePair.getLeft() );
			assertSame( "right", comparablePair.getRight() );
    		assertSame(  CU.nullLastNaturalOrderComparator(),  comparablePair.getLeftComparator() );
			assertSame(  CU.nullFirstNaturalOrderComparator(), comparablePair.getRightComparator() );

    	}

    	@Test
        @DisplayName("method 'of(Pair<L,R>,Comparator<L>,Comparator<R>)' should inject comparators as expected")
    	public void method_of_should_inject_comparators_as_expected()
    	{

    		final Object left  = new Object();
    		final Object right = new Object();
    		final Comparator<Object> leftComparator  = Comparator.comparingInt( a -> a.toString().length() );
    		final Comparator<Object> rightComparator = Comparator.comparing( Object::toString );

			final Pair<Object,Object> pair = Pair.of( left, right );
    		final ComparablePair<?,?> comparablePair = ComparablePair.of( pair, leftComparator, rightComparator );

			assertSame( left,  comparablePair.getLeft() );
			assertSame( right, comparablePair.getRight() );
    		assertSame(  leftComparator,  comparablePair.getLeftComparator() );
			assertSame(  rightComparator, comparablePair.getRightComparator() );

    	}

		@Test
		public void method_compareTo_should_work_as_expected()
		{
			assertTrue( CU.lt(ComparablePair.of(1,2),ComparablePair.of(2,2)) );
			assertTrue( CU.lt(ComparablePair.of(2,1),ComparablePair.of(2,2)) );
			assertTrue( CU.eq(ComparablePair.of(2,2),ComparablePair.of(2,2)) );
			assertTrue( CU.gt(ComparablePair.of(2,2),ComparablePair.of(2,1)) );
			assertTrue( CU.gt(ComparablePair.of(2,2),ComparablePair.of(1,2)) );
		}

    }


    /**
	 * Performs unit test for {@link ComparablePair.Builder}.
	 */
	@Nested
	@DisplayName("by using the Builder")
	public class ComparablePairBuilder
	{

		@Test
		public void injecting_null_values_should_create_an_empty_pair()
		{

			final ComparablePair<String,String> pair = ComparablePair
				.withLeft( (String) null )
				.andRight( null );

			assertSame( ComparablePair.empty(), pair );

		}

		@Test
    	public void to_use_the_Builder_should_be_the_same_as_to_use_the_factory_methods_1()
    	{

    		final Object left = new Object();
    		final Comparator<Object> leftComparator = Comparator.comparingInt( a -> a.toString().length() );
    		final ComparablePair<Object,String> withBuilder = ComparablePair
				.withLeft( left, leftComparator )
				.andRight( "right", true );

    		final ComparablePair<Object,String> withFactoryMethod = ComparablePair.of(
    			left, leftComparator,
    			"right", CU.nullLastNaturalOrderComparator()
			);

    		assertEquals( withBuilder, withFactoryMethod );
    		assertSame( withBuilder.getLeftComparator(), withFactoryMethod.getLeftComparator() );
    		assertSame( withBuilder.getRightComparator(), withFactoryMethod.getRightComparator() );

    	}

    	@Test
    	public void to_use_the_Builder_should_be_the_same_as_to_use_the_factory_methods_2()
    	{

    		final Object right = new Object();
    		final Pair<String,Object> source = Pair.of( "left", right );
    		final Comparator<Object> rightComparator = Comparator.comparingInt( a -> a.toString().length() );
    		final ComparablePair<String,Object> withBuilder = ComparablePair
				.withLeft( "left", true )
				.andRight( right, rightComparator );

    		final ComparablePair<String,Object> withFactoryMethod = ComparablePair.of(
    			source, CU.nullLastNaturalOrderComparator(), rightComparator
			);

    		assertEquals( withBuilder, withFactoryMethod );
    		assertSame( withBuilder.getLeftComparator(), withFactoryMethod.getLeftComparator() );
    		assertSame( withBuilder.getRightComparator(), withFactoryMethod.getRightComparator() );

    	}

    }

}

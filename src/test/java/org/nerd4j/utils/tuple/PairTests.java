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
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.nerd4j.utils.test.ObjectOverridesContract;

/**
 * Test suite for the class {@link Pair}
 *
 * @author Massimo Coluzzi
 */
@DisplayName("Testing class: Pair")
@DisplayNameGeneration(ReplaceUnderscores.class)
public class PairTests implements ObjectOverridesContract<Pair<?,?>>
{


	/* ******************* */
	/*  INTERFACE METHODS  */
	/* ******************* */


	/**
	 * {@inheritDoc}
	 */
	@Override
	public Pair<?,?> sampleValue()
	{

		return Pair.of( 11, 13 );

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Pair<?,?> notEqualValue()
	{

		return Pair.of( 11L, 13L );

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Pair<?,?> equalButNotSameValue()
	{
		return Pair.of( 11, 13 );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Pair<?,?> withDifferentHashcode()
	{
		return Pair.empty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String sampleToString()
	{
		return "<11, 13>";
	}
	
	
	/* ************** */
	/*  TEST METHODS  */
	/* ************** */
	
	
	/**
     * Performs unit test on the method  on empty {@link Pair}.
     */
    @Nested
    @DisplayName("if the Pair is empty")
    public class EmptyPair
    {
    	
    	@Test
    	public void pair_returned_by_static_method_empty_should_be_empty()
    	{
    		
    		final Pair<?,?> pair = Pair.empty();
    		
    		assertTrue( pair.isEmpty() );
    		assertNull( pair.getKey() );
    		assertNull( pair.getLeft() );
    		assertNull( pair.getRight() );
    		assertNull( pair.getValue() );
    		
    	}
    	
    	@Test
    	public void all_empty_pairs_should_be_the_same_instance()
    	{
    		
    		final Pair<?,?> pair = Pair.of( null, null );
    		
    		assertSame( Pair.empty(), pair );
    		
    	}
    	
    	@Test
    	public void the_hash_code_of_an_empty_pair_is_0()
    	{
    		
    		assertEquals( 0, Pair.empty().hashCode() );
    		
    	}
    	
    }
    
    /**
     * Performs unit test on the method  on empty {@link Pair}.
     */
    @Nested
    @DisplayName("if the Pair is not empty")
    public class NotEmptyPair
    {
    	
    	@CsvSource(value={
    		"null,null",
    		"left,null",
    		"null,right",
    		"left,right"
    	},nullValues="null")
    	@ParameterizedTest(name = "of({0},{1}) => <{0},{1}>")
    	public void method_of_should_create_the_Pair_as_expected( String left, String right )
    	{
    		
    		final Pair<?,?> pair = Pair.of( left, right );
    		
    		assertSame( left, pair.getLeft() );
    		assertSame( right, pair.getRight() );
    		
    	}

    	@CsvSource(value={
    		"null,null",
    		"left,null",
    		"null,right",
    		"left,right"
    	},nullValues="null")
    	@ParameterizedTest(name = "getKey() and getLeft() should both return {0}")
    	public void getKey_and_getLeft_should_return_the_same_value( String left, String right )
    	{
    		
    		final Pair<?,?> pair = Pair.of( left, right );
    		
    		assertSame( pair.getKey(), pair.getLeft() );
    		
    	}
    	
    	@CsvSource(value={
    		"null,null",
    		"left,null",
    		"null,right",
    		"left,right"
    	},nullValues="null")
    	@ParameterizedTest(name = "getRight() and getValue() should both return {0}")
    	public void getRight_and_getValue_should_return_the_same_value( String left, String right )
    	{
    		
    		final Pair<?,?> pair = Pair.of( left, right );
    		
    		assertSame( pair.getRight(), pair.getValue() );
    		
    	}
    	
    	@Test
        @DisplayName("each invocation of the method 'of' creates a new instance")
    	public void each_invocation_of_method_of_creates_a_new_instance()
    	{

    		final Pair<?,?> pair1 = Pair.of( "left", "right" );
    		final Pair<?,?> pair2 = Pair.of( "left", "right" );
    		
    		assertEquals(  pair1, pair2 );
    		assertNotSame( pair1, pair2 );

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
    		
    		final Pair<?,?> pair = Pair.of( left, right );
    		
    		assertEquals( expected, pair.isEmpty() );
    		
    	}
        
    }
    
}

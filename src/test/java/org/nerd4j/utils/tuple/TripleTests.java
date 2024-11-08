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
 * Test suite for the class {@link Triple}
 *
 * @author Massimo Coluzzi
 */
@DisplayName("Testing class: Triple")
@DisplayNameGeneration(ReplaceUnderscores.class)
public class TripleTests implements ObjectOverridesContract<Triple<?,?,?>>
{


	/* ******************* */
	/*  INTERFACE METHODS  */
	/* ******************* */


	/**
	 * {@inheritDoc}
	 */
	@Override
	public Triple<?,?,?> sampleValue()
	{

		return Triple.of( 11, 17, 13 );

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Triple<?,?,?> notEqualValue()
	{

		return Triple.of( 11L, 17L, 13L );

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Triple<?,?,?> equalButNotSameValue()
	{
		return Triple.of( 11, 17, 13 );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Triple<?,?,?> withDifferentHashcode()
	{
		return Triple.empty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String sampleToString()
	{
		return "<11, 17, 13>";
	}
	
	
	/* ************** */
	/*  TEST METHODS  */
	/* ************** */
	
	
	/**
     * Performs unit test on the method  on empty {@link Triple}.
     */
    @Nested
    @DisplayName("if the Triple is empty")
    public class EmptyTriple
    {
    	
    	@Test
    	public void triple_returned_by_static_method_empty_should_be_empty()
    	{
    		
    		final Triple<?,?,?> triple = Triple.empty();
    		
    		assertTrue( triple.isEmpty() );
    		assertNull( triple.getLeft() );
    		assertNull( triple.getRight() );
			assertNull( triple.getMiddle() );

    	}
    	
    	@Test
    	public void all_empty_triples_should_be_the_same_instance()
    	{
    		
    		final Triple<?,?,?> triple = Triple.of( null, null, null );
    		
    		assertSame( Triple.empty(), triple );
    		
    	}
    	
    	@Test
    	public void the_hash_code_of_an_empty_triple_is_0()
    	{
    		
    		assertEquals( 0, Triple.empty().hashCode() );
    		
    	}
    	
    }
    
    /**
     * Performs unit test on the method  on empty {@link Triple}.
     */
    @Nested
    @DisplayName("if the Triple is not empty")
    public class NotEmptyTriple
    {
    	
    	@Test
        @DisplayName("each invocation of the method 'of' creates a new instance")
    	public void each_invocation_of_method_of_creates_a_new_instance()
    	{

    		final Triple<?,?,?> triple1 = Triple.of( "left", "middle", "right" );
    		final Triple<?,?,?> triple2 = Triple.of( "left", "middle","right" );
    		
    		assertEquals(  triple1, triple2 );
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
    	public void method_of_should_create_the_Triple_as_expected( String left, String middle, String right )
    	{
    		
    		final Triple<?,?,?> triple = Triple.of( left, middle, right );
    		
    		assertSame( left, triple.getLeft() );
    		assertSame( right, triple.getRight() );
    		assertSame( middle, triple.getMiddle() );
    		
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
    		
    		final Triple<?,?,?> triple = Triple.of( left, middle, right );
    		
    		assertEquals( expected, triple.isEmpty() );
    		
    	}

    	
    }

}

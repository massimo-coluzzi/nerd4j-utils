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
package org.nerd4j.utils.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

/**
 * Suite to test the contract imposed by the interface {@link Comparable}.
 *
 * @author Massimo Coluzzi
 */
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public interface ComparableContract<T extends Comparable<T>> extends Contract<T>
{


    /* ******************* */
    /*  INTERFACE METHODS  */
    /* ******************* */

	
	/**
     * A comparable value larger than the one returned by {@link #sampleValue()}.
     *
     * @return value larger than the one returned by {@link #sampleValue()}.
     */
	T largerValue();
	
	/**
	 * A comparable value even larger than the one returned by {@link #largerValue()}.
	 *
	 * @return value even larger than the one returned by {@link #largerValue()}.
	 */
	T evenLargerValue();
	
    /**
     * A different instance that should be equal
     * to the one returned by {@link #sampleValue()}.
     * <p>
     * Any tests will be more meaningful if
     * the two instances have different non-key fields
     *
     * @return another instance of the class to test.
     */
    T equalButNotSameValue();



    /* ****************** */
    /*  CONTRACT METHODS  */
    /* ****************** */


	/**
	 * Every value, when compared to itself, should return {@code 0}.
	 *  
	 */
	@Test
    default void when_compared_to_itself_should_return_0()
	{
		
        final T value = sampleValue();
        assertEquals( 0, value.compareTo(value) );
        
    }
	
	/**
	 * Two values that compared return {@code 0} should be equal.
	 *  
	 */
	@Test
	default void if_two_values_are_equal_than_once_compared_should_return_0()
	{
		
		final T value = sampleValue();
		final T equalButNotSameValue = equalButNotSameValue();
		
		assertNotSame( value, equalButNotSameValue );
		assertEquals( value,  equalButNotSameValue );
		assertEquals( 0, value.compareTo(equalButNotSameValue) );
		
	}
	
	/**
	 * Two values that compared return {@code 0} should be equal.
	 *  
	 */
	@Test
	default void a_result_of_0_in_comparison_function_should_be_reflexive()
	{
		
		final T value = sampleValue();
		final T equalButNotSameValue = equalButNotSameValue();
		
		assertEquals( 0, value.compareTo(equalButNotSameValue) );
		assertEquals( 0, equalButNotSameValue.compareTo(value) );
		
	}
	
	/**
	 * A value compared with a larger value should return a negative number.
	 * 
	 */
    @Test
    default void when_compared_to_a_larger_value_should_return_a_negative_number()
    {
    	
        final T value = sampleValue();
        final T largerValue = largerValue();
        
        assertTrue( value.compareTo(largerValue) < 0);
        
    }
    
    /**
     * A value compared with a smaller value should return a positive number.
     * 
     */
    @Test
    default void when_compared_to_a_smaller_value_should_return_a_positive_number()
    {
    	
    	final T value = sampleValue();
    	final T largerValue = largerValue();
    	
    	assertTrue( largerValue.compareTo(value) > 0);
    	
    }
    
    /**
     * Checks that the order relation induced by the comparing
     * function is transitive, that is if {@code a < b} and
     * {@code b < c} then {@code a < c}.
     * 
     */
    @Test
    default void an_order_relation_should_be_transitive()
    {
    	
    	final T value = sampleValue();
    	final T largerValue = largerValue();
    	final T evenLargerValue = evenLargerValue();
    	
    	assertTrue( value.compareTo(largerValue) < 0);
    	assertTrue( largerValue.compareTo(evenLargerValue) < 0);
    	assertTrue( value.compareTo(evenLargerValue) < 0);
    	
    }

}

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
package org.nerd4j.utils.lang;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.nerd4j.utils.lang.TestClasses.ComparativeType;

/**
 * Test suite for the interface {@link Comparative}.
 *
 * @author Massimo Coluzzi
 */
@DisplayName("Testing default methods for interface: Comparative")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ComparativeTests
{


    /* ************************* */
    /*  TEST METHOD ifSameClass  */
    /* ************************* */


    /**
     * Performs unit test on all methods of {@link Comparative}
     * assuming the provided argument is {@code null}.
     */
    @Nested
    @DisplayName("if the other object is null")
    public class IfTheOtherObjectIsNull
    {

    	@Test
        public void method_compareTo_should_throw_a_NullPointerException()
        {

    		final ComparativeType sample = new ComparativeType( 10 );
    		assertThrows( NullPointerException.class, () -> sample.compareTo(null) );

        }
    	
    	@Test
    	public void method_lt_should_throw_a_NullPointerException()
    	{
    		
    		final ComparativeType sample = new ComparativeType( 10 );
    		assertThrows( NullPointerException.class, () -> sample.lt(null) );
    		
    	}
    	
    	@Test
    	public void method_le_should_throw_a_NullPointerException()
    	{
    		
    		final ComparativeType sample = new ComparativeType( 10 );
    		assertThrows( NullPointerException.class, () -> sample.le(null) );
    		
    	}
    	
    	@Test
    	public void method_eq_should_throw_a_NullPointerException()
    	{
    		
    		final ComparativeType sample = new ComparativeType( 10 );
    		assertThrows( NullPointerException.class, () -> sample.eq(null) );
    		
    	}
    	
    	@Test
    	public void method_ne_should_throw_a_NullPointerException()
    	{
    		
    		final ComparativeType sample = new ComparativeType( 10 );
    		assertThrows( NullPointerException.class, () -> sample.ne(null) );
    		
    	}
    	
    	@Test
    	public void method_ge_should_throw_a_NullPointerException()
    	{
    		
    		final ComparativeType sample = new ComparativeType( 10 );
    		assertThrows( NullPointerException.class, () -> sample.ge(null) );
    		
    	}
    	
    	@Test
    	public void method_gt_should_throw_a_NullPointerException()
    	{
    		
    		final ComparativeType sample = new ComparativeType( 10 );
    		assertThrows( NullPointerException.class, () -> sample.gt(null) );
    		
    	}

    }
    
    /**
     * Performs unit test on all methods of {@link Comparative}
     * assuming the provided argument is {@code not null}.
     */
    @Nested
    @DisplayName("if the other object is not null")
    public class IfTheOtherObjectIsNotNull
    {
    	
    	@CsvSource(value={
    		"15,25,-10",
    		"20,20,  0",
    		"25,15, 10"
    	})
    	@ParameterizedTest(name="{0}.compareTo({1}) == {2}")
    	public void method_compareTo_should_return_the_expected_value( int fst, int snd, int expected )
    	{
    		
    		final ComparativeType a = new ComparativeType( fst );
    		final ComparativeType b = new ComparativeType( snd );
    		
    		assertEquals( expected, a.compareTo(b) );
    		
    	}
    	
    	@CsvSource(value={
    		"15,25,true",
    		"20,20,false",
    		"25,15,false"
    	})
    	@ParameterizedTest(name="{0}.lt({1}) == {2}")
    	public void method_lt_should_return_the_expected_value( int fst, int snd, boolean expected )
    	{
    		
    		final ComparativeType a = new ComparativeType( fst );
    		final ComparativeType b = new ComparativeType( snd );
    		
    		assertEquals( expected, a.lt(b) );
    		
    	}
    	
    	@CsvSource(value={
    		"15,25,true",
    		"20,20,true",
    		"25,15,false"
    	})
    	@ParameterizedTest(name="{0}.le({1}) == {2}")
    	public void method_le_should_return_the_expected_value( int fst, int snd, boolean expected )
    	{
    		
    		final ComparativeType a = new ComparativeType( fst );
    		final ComparativeType b = new ComparativeType( snd );
    		
    		assertEquals( expected, a.le(b) );
    		
    	}

    	@CsvSource(value={
       		"15,25,false",
       		"20,20,true",
       		"25,15,false"
       	})
       	@ParameterizedTest(name="{0}.eq({1}) == {2}")
       	public void method_eq_should_return_the_expected_value( int fst, int snd, boolean expected )
       	{
        		
       		final ComparativeType a = new ComparativeType( fst );
       		final ComparativeType b = new ComparativeType( snd );
        		
       		assertEquals( expected, a.eq(b) );
        		
       	}
    	
    	@CsvSource(value={
    		"15,25,true",
    		"20,20,false",
    		"25,15,true"
    	})
    	@ParameterizedTest(name="{0}.ne({1}) == {2}")
    	public void method_ne_should_return_the_expected_value( int fst, int snd, boolean expected )
    	{
    		
    		final ComparativeType a = new ComparativeType( fst );
    		final ComparativeType b = new ComparativeType( snd );
    		
    		assertEquals( expected, a.ne(b) );
    		
    	}
    	
    	@CsvSource(value={
    		"15,25,false",
    		"20,20,true",
    		"25,15,true"
    	})
    	@ParameterizedTest(name="{0}.ge({1}) == {2}")
    	public void method_ge_should_return_the_expected_value( int fst, int snd, boolean expected )
    	{
    		
    		final ComparativeType a = new ComparativeType( fst );
    		final ComparativeType b = new ComparativeType( snd );
    		
    		assertEquals( expected, a.ge(b) );
    		
    	}
    	
    	@CsvSource(value={
    		"15,25,false",
    		"20,20,false",
    		"25,15,true"
    	})
    	@ParameterizedTest(name="{0}.gt({1}) == {2}")
    	public void method_gt_should_return_the_expected_value( int fst, int snd, boolean expected )
    	{
    		
    		final ComparativeType a = new ComparativeType( fst );
    		final ComparativeType b = new ComparativeType( snd );
    		
    		assertEquals( expected, a.gt(b) );
    		
    	}
    	
    }

}

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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

/**
 * Suite to test the contract imposed by the methods {@link #equals(Object)},
 * {@link #hashCode()} and {@link #toString()}.
 *
 * @author Massimo Coluzzi
 */
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public interface ObjectOverridesContract<T> extends Contract<T>
{


    /* ******************* */
    /*  INTERFACE METHODS  */
    /* ******************* */


    /**
     * A different instance that should not be equal
     * to the one returned by {@link #sampleValue()}.
     *
     * @return another instance of the class to test.
     */
    T notEqualValue();

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

    /**
     * Returns an instance with a different hash code
     * than the one returned by {@link #sampleValue()}.
     *
     * @return instance with different hash code.
     */
    T withDifferentHashcode();

    /**
     * Returns the expected {@link #toString()} output
     * for the instance returned by {@link #sampleValue()};
     *
     * @return the {@link #toString()} output of the sample value.
     */
    String sampleToString();



    /* ****************** */
    /*  CONTRACT METHODS  */
    /* ****************** */


    /*  Test equals  */

    /**
     * Checks that passing {@code null} to
     * the method {@link #equals(Object)}
     * always return {@code false}.
     */
    @Test
    default void value_should_not_equal_null()
    {

        final T value = sampleValue();
        assertFalse( value.equals(null) );

    }

    /**
     * Checks that an object always equals to itself.
     * <p>
     * This test is quite poor, to be exhaustive the
     * test should check every instance of the class
     * but this could be overwhelming.
     */
    @Test
    default void value_should_equal_itself()
    {

        final T value = sampleValue();
        assertEquals( value, value );

    }

    /**
     * Checks that two instances with different
     * key fields do not equal.
     * <p>
     * This test is quite poor, to be exhaustive the
     * test should check every combination of two
     * different instances of the class but this
     * could become overwhelming.
     */
    @Test
    default void value_should_not_equal_a_different_value()
    {

        final T value = sampleValue();
        final T notEqualValue = notEqualValue();

        assertNotEquals( value, notEqualValue );
        assertNotEquals( notEqualValue, value );

    }


    /**
     * Checks that two different instances can be equal
     * even if there are some different non-key field.
     */
    @Test
    default void instance_with_different_non_key_values_can_be_equal()
    {

        final T value = sampleValue();
        final T equalButNotSameValue = equalButNotSameValue();

        assertNotSame( value, equalButNotSameValue );
        assertEquals( value, equalButNotSameValue );

    }

    /**
     * Checks that the equality relation is reflexive.
     */
    @Test
    default void the_equality_relation_should_be_reflexive()
    {
    	
    	final T value = sampleValue();
    	final T equalButNotSameValue = equalButNotSameValue();
    	
    	assertEquals( value, equalButNotSameValue );
    	assertEquals( equalButNotSameValue, value );
    	
    }
    

    /*  Test hashCode  */

    /**
     * Checks that two instances that equals
     * have the same hash code.
     * <p>
     * This test is quite poor, to be exhaustive the
     * test should check every combination of two
     * different instances of the class but this
     * could become overwhelming.
     */
    @Test
    default void equal_values_should_have_the_same_hash_code()
    {

        final T value = sampleValue();
        final T equalButNotSameValue = equalButNotSameValue();

        assertNotSame( value, equalButNotSameValue );

        assertEquals( value, equalButNotSameValue );
        assertEquals( equalButNotSameValue, value );

        assertEquals( value.hashCode(), equalButNotSameValue.hashCode() );

    }

    /**
     * Checks that two instances that equals
     * have the same hash code.
     * <p>
     * This test is quite poor, to be exhaustive the
     * test should check every combination of two
     * different instances of the class but this
     * could become overwhelming.
     */
    @Test
    default void values_with_different_hash_codes_should_not_equal()
    {

        final T value = sampleValue();
        final T withDifferentHashcode = withDifferentHashcode();

        assertNotEquals( value.hashCode(), withDifferentHashcode.hashCode() );
        assertNotEquals( value, withDifferentHashcode );
        assertNotEquals( withDifferentHashcode, value );

    }


    /*  Test toString  */

    /**
     * Checks that two instances that equals
     * have the same hash code.
     * <p>
     * This test is quite poor, to be exhaustive the
     * test should check every combination of two
     * different instances of the class but this
     * could become overwhelming.
     */
    @Test
    default void to_string_output_should_look_like_expected()
    {

        final T value = sampleValue();
        final String toString = sampleToString();

        assertEquals( toString, value.toString() );

    }

}

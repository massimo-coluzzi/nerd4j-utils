/*
 * #%L
 * Nerd4j Core
 * %%
 * Copyright (C) 2011 - 2013 Nerd4j
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

import java.util.Arrays;

/**
 * This utility class is intended to be used inside the method {@link #hashCode()} of a class.
 *
 * <p>
 * The aim of this class, paired with the class {@link Equals}, is to avoid the boilerplate code
 * needed to implement the {@link #hashCode()} method and to provide an implementation
 * that is consistent with the related {@link #equals(Object)} method.
 *
 * <p>
 * Even if most of the IDEs provide tools to generate implementations of {@link #hashCode()}
 * and {@link #equals(Object)}, the generated code is ugly and hard to understand.
 * By using this utility class the resulting {@link #hashCode()} method will be small,
 * clean and easy to read.
 *
 * <p>
 * You may have seen a lot of times implementation of the method {@link #hashCode()}
 * in this form:
 * <pre>
 * public boolean hashCode()
 * {
 *
 *     int hashCode = 31
 *     hashCode *= field1 &amp; 0xFFFFF800
 *     hashCode = hashCode ^ (field2 &lt;&lt; 11);
 *     hashCode += field3 &lt;&lt; 6;
 *     hashCode += field4 == null 0 : field4.hashCode();
 *
 *     return hashCode ^ (hashCode &gt;&gt;&gt; 32);
 *
 * }
 * </pre>
 * It is quite hard to understand and definitely ugly!
 *
 * <p>
 * With this utility you can get the same result with a single instruction:
 * <pre>
 * public boolean hashCode()
 * {
 *
 *     return Hashcode.of( field1, field2, field3, field4 );
 *
 * }
 * </pre>
 *
 * <p>
 * This class can be used also to get the hash code of a generic object.
 * The values returned by this class will be the same between multiple
 * runs of the Java Virtual Machine.
 *
 * <p>
 * For example the hash code of an object of type {@code enum}
 * may change between different runs of the {@code JVM}
 * so {@code enum.hashCode()} may change while {@code Hashcode.of(enum)} will not.
 *
 * <p>
 * For example you may need to store a complex object and be able to
 * find it using a unique key. Such key may be based on several fields.
 * Using this utility you can easily build an hash key that will be the
 * same across different {@code JVM}s and different servers and therefore
 * can be stored.
 *
 * @author Massimo Coluzzi
 * @since 2.0.0
 */
public class Hashcode
{

	/**
	 * This class is intended to be static
	 * so there is no public constructor.
	 */
	Hashcode() {}


	/* **************** */
	/*  PUBLIC METHODS  */
	/* **************** */


	/**
     * Creates an hash code based on the given object.
	 * <p>
	 * The hash code for {@code null} is {@code 0}.
     * <p>
     * If the given object is an array the hash code
     * is based on its elements.
     *
     * @param object an object to use to compute the hash code.
     * @return the resulting hash code.
     */
    public static int of( Object object )
    {

    	return of( 79, object );

    }


    /**
	 * Creates an hash code based on the given base object
     * and an arbitrary long series of other objects.
	 * <p>
	 * It creates an hash code that is combination of the
	 * provided objects.
	 * <p>
	 * If all objects are {@code null} the hash code is {@code 0}.
     * <p>
     * If one or more of the given objects is an array the hash code
     * is based on its elements.
	 *
	 * @param object an object to use to compute the hash code.
	 * @param others other objects to combine with the first one.
	 * @return the resulting hash code.
	 */
    public static int of( Object object, Object... others )
    {

    	return of( 79, object, others );

    }


	/* ***************** */
    /*  PRIVATE METHODS  */
    /* ***************** */


	/**
	 * Creates an hash code based on a given seed and a given object.
	 * <p>
	 * If one of the given objects is an array the hashcode
     * is based on its elements.
	 *
	 * @param seed   a number to use as a seed to compute the hashcode.
     * @param object an object to combine with the seed.
	 * @return the resulting hash code.
	 */
	private static int of( int seed, Object object )
	{

		if( object == null )
			return 0;

		/*
		 * The hash of an enum changes between two different runs of a JVM.
		 * If the hash code is stored for some reason, after a reboot of
		 * the JVM will not match anymore. We chosen to use the hash of
		 * the name() instead of the ordinal() for the same reason,
		 * if the position of the enum changes the hash will change as well.
		 * Using the name() will require more computational resources
		 * but will be stable through different runs of the JVM.
		 */
		if( object instanceof Enum )
			return of( seed, ((Enum<?>) object).name().hashCode() );

		/*
		 * To be consistent with the utility class org.nerd4j.utils.lang.Equals
		 * we compute the hashcode of an array based on the hashcode of its elements.
		 */
		if( object.getClass().isArray() )
			return of( seed, hashArray(object) );

		return of( seed, object.hashCode() );

	}


	/**
	 * Creates an hash code based on a given seed,
	 * a base object and an arbitrary long series of other objects.
	 * <p>
	 * If one of the given objects is an array the hashcode
     * is based on its elements.
	 *
	 * @param seed   a number to use as a seed to compute the hashcode.
	 * @param object an object to combine with the seed.
	 * @param others the other objects to use.
	 * @return the resulting hash code.
	 */
	private static int of( int seed, Object object, Object... others )
	{

		int result = of( seed, object );
		for( Object other : others )
			result += of( result, other );

		return result;

	}


    /**
     * Creates an hash code based on a given prime number
     * and a given integer base.
     *
     * @param prime a number supposed to be prime.
     * @param base  a base number to combine with the prime.
     * @return the resulting hash code.
     */
    private static int of( int prime, int base )
    {

        final int actual = prime == 0 ? 79 : prime;
        return base == 0 ? actual : actual * base;

    }


    /**
	 * Returns a hash code based on the "deep contents" of the given array.
	 * It build the hash code based on each element of the given array.
	 *
	 * @param array the array to compute the hash code for.
	 * @return the "deep contents" hash code.
	 */
	private static int hashArray( Object array )
	{

		// This case handles also multi-dimensional array of natives.
		if ( array instanceof Object[] )
			return Arrays.deepHashCode( (Object[]) array );

		else if ( array instanceof int[] )
			return Arrays.hashCode( (int[]) array );

		else if ( array instanceof byte[] )
			return Arrays.hashCode( (byte[]) array );

		else if ( array instanceof long[] )
			return Arrays.hashCode( (long[]) array );

		else if ( array instanceof short[] )
			return Arrays.hashCode( (short[]) array );

		else if ( array instanceof boolean[] )
			return Arrays.hashCode( (boolean[]) array );

		else if ( array instanceof float[] )
			return Arrays.hashCode( (float[]) array );

		else if ( array instanceof double[] )
			return Arrays.hashCode( (double[]) array );

		else if ( array instanceof char[] )
			return Arrays.hashCode( (char[]) array );

		return 0;

	}

}

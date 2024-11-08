/*-
 * #%L
 * Nerd4j Utils
 * %%
 * Copyright (C) 2011 - 2020 Nerd4j
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


import org.nerd4j.utils.lang.ToString.Configuration;
import org.nerd4j.utils.lang.ToString.Configuration.Field;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * This class is intended to be used inside the {@link ToString}
 * utility class. It is an internal support class with the aim
 * to keep the {@link ToString} class clean and to make the
 * implementations more easy to understand and to test.
 *
 * @author Massimo Coluzzi
 * @since 2.0.0
 */
class ToStringOutput
{

    /**
     * This class is intended to be static
     * so there is no public constructor.
     */
    ToStringOutput() {}


    /* **************** */
    /*  PUBLIC METHODS  */
    /* **************** */


    /**
	 * Creates an output of the method {@link #toString()}
	 * using the given prefix and suffix and using the given
	 * separators.
	 * <p>
	 * This method provides a quite wide range of customizations.
	 *
	 * @param configuration      the parser configuration.
	 * @param prefix             the prefix to use before printing values.
	 * @param nameValueSeparator the text to use to separate name and value of a field.
	 * @param fieldSeparator     the text to use to separate fields.
	 * @param suffix             the suffix to use after printing values.
	 * @return a custom output using the given values.
	 */
    static String using( Configuration configuration,
						 String prefix, String nameValueSeparator,
						 String fieldSeparator, String suffix )
    {

		final Object target = Require.nonNull(
			configuration, "The ToString.Configuration is mandatory"
		).target();

    	if( target == null )
    		return "null";

    	final String className = className( configuration );
    	final StringBuilder sb = newStringBuilder( className, configuration);

    	sb.append( className );

    	if( IsNot.empty(prefix) )
    		sb.append( prefix );

    	final Iterator<Field> iterator
    	= configuration.fields().iterator();

    	printFirst( iterator, sb, nameValueSeparator );
    	printRemaining( iterator, sb, fieldSeparator, nameValueSeparator );

    	if( IsNot.empty(suffix) )
    		sb.append( suffix );

    	return sb.toString();

    }


    /* ***************** */
    /*  PRIVATE METHODS  */
    /* ***************** */


    /**
     * Prints into the given {@link StringBuilder}
     * the first non {@link null} element in the
     * given {@link Iterator}.
     *
     * @param iterator  - the {@link Iterator} to read.
     * @param sb        - the {@link StringBuilder} to write into.
     * @param separator - the separator character
     */
    private static void printFirst( Iterator<Field> iterator,
                                    StringBuilder sb,
                                    String separator )
    {

        while( iterator.hasNext() )
        {

            final Field field = iterator.next();
            if( field != null )
            {

                print( field, separator, sb );
                return;

            }

        }

    }


    /**
     * Prints into the given {@link StringBuilder}
     * all the remaining non {@link null} element in the
     * given {@link Iterator}.
     *
     * @param iterator  - the {@link Iterator} to read.
     * @param sb        - the {@link StringBuilder} to write into.
     * @param fieldSeparator     - character to use to separate fields.
     * @param nameValueSeparator - character to use to separate name and value.
     */
    private static void printRemaining( Iterator<Field> iterator,
                                        StringBuilder sb,
                                        String fieldSeparator,
                                        String nameValueSeparator )
    {

        iterator.forEachRemaining( field ->
        {

            if( field != null )
            {

                sb.append( fieldSeparator );
                print( field, nameValueSeparator, sb );

            }

        });

    }


    /**
     * Prints the given field in the given {@link StringBuilder}
     * using the given character to separate the field name
     * from the field value.
     * <p>
     * If the field name is {@code null} only the field value
     * will be printed.
     *
     * @param field     - field to print.
     * @param separator - character to use to separate name and value.
     * @param sb        - {@link StringBuilder} to write into.
     */
    private static void print( Field field,
                               String separator,
                               StringBuilder sb )
    {

        if( IsNot.empty(field.name) )
		{

			sb.append( field.name );

			if( IsNot.empty( separator ) )
				sb.append( separator );

		}

        if( field.value != null && field.value.getClass().isArray() )
        	printArray( field.value, sb );
        else
        	sb.append( field.value );

    }


	/**
	 * Uses the {@link Arrays} utility class to write the
	 * given array into the given {@link StringBuilder}.
	 * <p>
	 * This method writes multi-dimensional arrays recursively.
	 *
	 * @param array - the array to print.
	 * @param sb    - the {@link StringBuilder} where to write array.
	 */
    private static void printArray( Object array, StringBuilder sb )
	{

		// FIXME: Migrate to utility class "Native"
		// This case handles also multi-dimensional array of natives.
		if ( array instanceof Object[] )
			sb.append( Arrays.deepToString((Object[]) array) );

		else if ( array instanceof int[] )
			sb.append( Arrays.toString((int[]) array) );

		else if ( array instanceof byte[] )
			sb.append( Arrays.toString((byte[]) array) );

		else if ( array instanceof long[] )
			sb.append( Arrays.toString((long[]) array) );

		else if ( array instanceof short[] )
			sb.append( Arrays.toString((short[]) array) );

		else if ( array instanceof boolean[] )
			sb.append( Arrays.toString((boolean[]) array) );

		else if ( array instanceof float[] )
			sb.append( Arrays.toString((float[]) array) );

		else if ( array instanceof double[] )
			sb.append( Arrays.toString((double[]) array) );

		else if ( array instanceof char[] )
			sb.append( Arrays.toString((char[]) array) );

	}


    /**
     * Returns the {@code simple class name}
     * or the {@code canonical class name} depending
     * on the given configuration.
     *
     * @param configuration - printer configuration.
     * @return the requested class name.
     */
    private static String className( Configuration configuration )
    {

    	if( configuration.customClassName() != null )
    		return configuration.customClassName();
    	
        return configuration.fullClassPath()
                ? configuration.target().getClass().getCanonicalName()
                : configuration.target().getClass().getSimpleName();

    }


    /**
     * Returns a new {@link StringBuilder} with a proper
     * initial capacity.
     *
     * @param className     - name of the class to write.
     * @param configuration - printer configuration.
     * @return
     */
    private static StringBuilder newStringBuilder( String className,
                                                   Configuration configuration )
    {

    	int capacity = className.length() << 1;
    	if( configuration.fields() == null )
    		return new StringBuilder( capacity );

        final Stream<Field> fields
        = StreamSupport.stream(
            configuration.fields().spliterator(), false
        );

        final int sum = fields
                .filter( Objects::nonNull )
                .map( f -> f.name )
                .mapToInt( n -> n != null ? n.length() : 10 )
                .sum();

        capacity += sum * 3;
        return new StringBuilder( capacity );

    }

}

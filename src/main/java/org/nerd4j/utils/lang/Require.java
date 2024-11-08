/*
 * #%L
 * Nerd4j Core
 * %%
 * Copyright (C) 2011 - 2018 Nerd4j
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

import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Assertion utility class that assists in validating arguments.
 * Is inspired by the {@link java.util.Objects#requireNonNull(Object,String)} approach
 * but provides {@code null-safe} operations for the most common checks over {@link Object}s,
 * {@link String}s, {@link Collection}s, {@link Map}s, arrays and objects that implement
 * the {@link Emptily} interface.
 *
 * <p>
 * This class is designed primarily for doing parameter validation in methods
 * and constructors, like the following:
 * <pre>
 * public MyClass( int id, Foo foo, String bar )
 * {
 *    this.id  = Require.trueFor( id, id &gt; 0, "id must be positive" );
 *    this.foo = Require.nonNull( foo, "foo cannot be null" );
 *    this.val = Require.nonBlank( val, "val cannot be blank" );
 * }
 * </pre>
 *
 * <p>
 * The checks currently implemented are:
 * <ul>
 *  <li><b>Require.toHold</b>
 *      - requires the given assertion to be {@code true} otherwise throws a {@link RequirementFailure}</li>
 *  <li><b>Require.trueFor</b>
 *      - if the given assertion is valid for the given object returns the checked object otherwise
 *        throws a {@link RequirementFailure}</li>
 *  <li><b>Require.nonNull</b>
 *      - requires the given object to be not {@code null} otherwise throws a {@link RequirementFailure}</li><li><b>Require.nonNull</b>
 *  <li><b>Require.nonEmpty</b>
 *      - requires the given object to be not empty otherwise throws a {@link RequirementFailure}</li>
 *  <li><b>Require.nonBlank</b>
 *      - requires the given {@link String} to be not blank otherwise throws a {@link RequirementFailure}</li>
 * </ul>
 *
 * @author Massimo Coluzzi
 * @since 2.0.0
 */
public class Require
{

	/** Prefix to prepend to all messages. */
	private static final String PREFIX = "[Requirement failed] - ";


    /**
     * This class is intended to be static
     * so there is no public constructor.
     */
    Require() {}



	/* ************************* */
	/*  GENERIC ASSERTION CHECK  */
	/* ************************* */


	/**
     * Checks the given assertion to be {@code true}.
     *
     * @param assertion the assertion that has to be {@code true}
     * @throws RequirementFailure if the {@code assertion} is {@code false}
     */
    public static void toHold( boolean assertion )
    {

        Require.toHold( assertion, "the required assertion must hold" );

    }

    /**
     * Checks the given assertion to be {@code true} and
     * throws a customized {@link RequirementFailure} if not.
     *
     * @param assertion the assertion that has to be {@code true}
     * @param message   detail message to be used in the event that a
     *                    {@code RequirementFailure} is thrown
     * @throws RequirementFailure if the {@code assertion} is {@code false}
     */
    public static void toHold( boolean assertion, String message )
    {

        if( ! assertion )
            throw new RequirementFailure( PREFIX + message );

    }

    /**
     * Checks the given assertion to be {@code true} and
     * throws a customized {@link RequirementFailure} if not.
     *
     * <p>
     * Unlike the method {@link #toHold(boolean, String)},
     * this method allows creation of the message to be deferred until
     * after the null check is made. While this may confer a
     * performance advantage in the non-null case, when deciding to
     * call this method care should be taken that the costs of
     * creating the message supplier are less than the cost of just
     * creating the string message directly.
     *
     * @param assertion       the assertion that has to be {@code true}
     * @param messageSupplier supplier of the detail message to be
     *        used in the event that a {@code RequirementFailure} is thrown
     * @throws RequirementFailure if the {@code assertion} is {@code false}
     */
    public static void toHold( boolean assertion, Supplier<String> messageSupplier )
    {

        if( ! assertion )
            throw new RequirementFailure( PREFIX + messageSupplier.get() );

    }


    /* ************************************ */
	/*  ASSERTION FOR A GIVEN VALUE CHECKS  */
    /* ************************************ */


	/**
     * Checks the given assertion to hold for the specified value.
     *
     * <p>
     * The assertion passed to this method should check some property
     * of the given value. If the assertion holds the related value
     * will be returned.
     *
     * @param <V>       the type of the value.
     * @param value     the value to be checked.
     * @param assertion the assertion relating to the given value that has to be {@code true}
     * @return {@code value} if the related {@code assertion} holds.
     * @throws RequirementFailure if the {@code assertion} is {@code false},
     *         the given {@code value} otherwise.
     */
    public static <V> V trueFor( V value, boolean assertion )
    {

    	return Require.trueFor( value, assertion, () -> "the required assertion must hold for " + value );

    }

    /**
     * Checks the given assertion to hold for the specified value
     * and throws a customized {@link RequirementFailure} if not.
     *
     * <p>
     * The assertion passed to this method should check some property
     * of the given value. If the assertion holds the related value
     * will be returned
     *
     * @param <V>       the type of the value.
     * @param value     the value to be checked.
     * @param assertion the assertion that has to be {@code true}
     * @param message   detail message to be used in the event that a
     *                    {@code RequirementFailure} is thrown
     *  @return {@code value} if the related {@code assertion} holds.
     * @throws RequirementFailure if the {@code assertion} is {@code false},
     *         the given {@code value} otherwise.
     */
    public static<V> V trueFor( V value, boolean assertion, String message )
    {

    	Require.toHold( assertion, message );
        return value;

    }

    /**
     * Checks the given assertion to hold for the specified value.
     * and throws a customized {@link RequirementFailure} if not.
     *
     * <p>
     * The assertion passed to this method should check some property
     * of the given value. If the assertion holds the related value
     * will be returned.
     *
     * <p>Unlike the method {@link #trueFor(Object,boolean,String)},
     * this method allows creation of the message to be deferred until
     * after the assertion check is made. While this may confer a
     * performance advantage in the good case, when deciding to
     * call this method care should be taken that the costs of
     * creating the message supplier are less than the cost of just
     * creating the string message directly.
     *
     * @param <V>             the type of the value.
     * @param value           the value to be checked.
     * @param assertion       the assertion that has to be {@code true}
     * @param messageSupplier supplier of the detail message to be
     *        used in the event that a {@code RequirementFailure} is thrown
     * @return {@code value} if the related {@code assertion} holds.
     * @throws RequirementFailure if the {@code assertion} is {@code false},
     *         the given {@code value} otherwise.
     */
    public static <V> V trueFor( V value, boolean assertion, Supplier<String> messageSupplier )
    {

    	Require.toHold( assertion, messageSupplier );
    	return value;

    }


    /* ***************** */
    /*  NON NULL CHECKS  */
    /* ***************** */

    /**
     * Checks that the specified object reference is not {@code null}.
     *
     * @param <V>   the type of the reference
     * @param value the object reference to check for nullity
     * @return {@code value} if not {@code null}
     * @throws RequirementFailure if {@code value} is {@code null}
     */
    public static <V> V nonNull( V value )
    {

    	return Require.nonNull( value, "this argument must not be null" );

    }

    /**
     * Checks that the specified object reference is not {@code null} and
     * throws a customized {@link RequirementFailure} if it is.
     *
     * @param <V>     the type of the reference
     * @param value   the object reference to check for nullity
     * @param message detail message to be used in the event that a
     *                  {@code RequirementFailure} is thrown
     * @return {@code value} if not {@code null}
     * @throws RequirementFailure if {@code value} is {@code null}
     */
    public static <V> V nonNull( V value, String message )
    {

    	return Require.trueFor( value, value != null, message );

    }

    /**
     * Checks that the specified object reference is not {@code null} and
     * throws a customized {@link RequirementFailure} if it is.
     *
     * <p>
     * Unlike the method {@link #nonNull(Object, String)},
     * this method allows creation of the message to be deferred until
     * after the null check is made. While this may confer a
     * performance advantage in the non-null case, when deciding to
     * call this method care should be taken that the costs of
     * creating the message supplier are less than the cost of just
     * creating the string message directly.
     *
     * @param <V>             the type of the reference
     * @param value           the object reference to check for nullity
     * @param messageSupplier supplier of the detail message to be
     * used in the event that a {@code RequirementFailure} is thrown
     * @return {@code value} if not {@code null}
     * @throws RequirementFailure if {@code value} is {@code null}
     */
    public static <V> V nonNull( V value, Supplier<String> messageSupplier )
    {

    	return Require.trueFor( value, value != null, messageSupplier );

    }


    /* ************************* */
    /*  NON EMPTY EMPTIL CHECKS  */
    /* ************************* */


    /**
     * Checks that the specified {@link Emptily} reference is not {@code null} nor empty.
     *
     * @param value the {@link Emptily} reference to check for emptiness
     * @return {@code value} if not {@code null} nor empty
     * @throws RequirementFailure if {@code value} is {@code null}
     */
    public static Emptily nonEmpty(Emptily value )
    {

        return Require.nonEmpty( value, () -> "this argument must not be empty but was " + value );

    }

    /**
     * Checks that the specified {@link Emptily} reference is not {@code null} nor empty
     * and throws a customized {@link RequirementFailure} if it is.
     *
     * @param value   the {@link Emptily} reference to check for emptiness
     * @param message detail message to be used in the event that a
     *                  {@code RequirementFailure} is thrown
     * @return {@code value} if not {@code null} nor empty
     * @throws RequirementFailure if {@code value} is {@code null} nor empty
     */
    public static Emptily nonEmpty(Emptily value, String message )
    {

        return Require.trueFor( value, IsNot.empty(value), message );

    }

    /**
     * Checks that the specified {@link Emptily} reference is not {@code null} nor empty
     * and throws a customized {@link RequirementFailure} if it is.
     *
     * <p>Unlike the method {@link #nonEmpty(Emptily, String)},
     * this method allows creation of the message to be deferred until
     * after the null check is made. While this may confer a
     * performance advantage in the non-null case, when deciding to
     * call this method care should be taken that the costs of
     * creating the message supplier are less than the cost of just
     * creating the string message directly.
     *
     * @param value           the {@link Emptily} reference to check for emptiness
     * @param messageSupplier supplier of the detail message to be
     * used in the event that a {@code RequirementFailure} is thrown
     * @return {@code value} if not {@code null} nor empty
     * @throws RequirementFailure if {@code value} is {@code null} nor empty
     */
    public static Emptily nonEmpty(Emptily value, Supplier<String> messageSupplier )
    {

    	return Require.trueFor( value, IsNot.empty(value), messageSupplier );

    }

    /* ************************* */
    /*  NON EMPTY STRING CHECKS  */
    /* ************************* */


    /**
     * Checks that the specified {@link String} reference is not {@code null} nor empty.
     *
     * @param value the {@link String} reference to check for emptiness
     * @return {@code value} if not {@code null} nor empty
     * @throws RequirementFailure if {@code value} is {@code null}
     */
    public static String nonEmpty( String value )
    {

        return Require.nonEmpty( value, () -> "this argument must not be empty but was " + value );

    }

    /**
     * Checks that the specified {@link String} reference is not {@code null} nor empty
     * and throws a customized {@link RequirementFailure} if it is.
     *
     * @param value   the {@link String} reference to check for emptiness
     * @param message detail message to be used in the event that a
     *                  {@code RequirementFailure} is thrown
     * @return {@code value} if not {@code null} nor empty
     * @throws RequirementFailure if {@code value} is {@code null} nor empty
     */
    public static String nonEmpty( String value, String message )
    {

        return Require.trueFor( value, IsNot.empty(value), message );

    }

    /**
     * Checks that the specified {@link String} reference is not {@code null} nor empty
     * and throws a customized {@link RequirementFailure} if it is.
     *
     * <p>Unlike the method {@link #nonEmpty(String, String)},
     * this method allows creation of the message to be deferred until
     * after the null check is made. While this may confer a
     * performance advantage in the non-null case, when deciding to
     * call this method care should be taken that the costs of
     * creating the message supplier are less than the cost of just
     * creating the string message directly.
     *
     * @param value           the {@link String} reference to check for emptiness
     * @param messageSupplier supplier of the detail message to be
     * used in the event that a {@code RequirementFailure} is thrown
     * @return {@code value} if not {@code null} nor empty
     * @throws RequirementFailure if {@code value} is {@code null} nor empty
     */
    public static String nonEmpty( String value, Supplier<String> messageSupplier )
    {

        return Require.trueFor( value, IsNot.empty(value), messageSupplier );

    }


    /* ************************* */
    /*  NON BLANK STRING CHECKS  */
    /* ************************* */


    /**
     * Checks that the specified {@link String} satisfies {@link IsNot#blank(String)}.
     *
     * @param value the {@link String} reference to check
     * @return {@code value} if satisfies {@link IsNot#blank(String)}
     * @throws RequirementFailure if the check fails
     */
    public static String nonBlank( String value )
    {

    	return Require.nonBlank( value, () -> "this argument must not be blank but was " + value );

    }

    /**
     * Checks that the specified {@link String} satisfies {@link IsNot#blank(String)}
     * and throws a customized {@link RequirementFailure} if not.
     *
     * @param value   the {@link String} reference to check
     * @param message detail message to be used in the event that a
     *                  {@code RequirementFailure} is thrown
     * @return {@code value} if satisfies {@link IsNot#blank(String)}
     * @throws RequirementFailure if the check fails
     */
    public static String nonBlank( String value, String message )
    {

    	return Require.trueFor( value, IsNot.blank(value), message );

    }

    /**
     * Checks that the specified {@link String} satisfies {@link IsNot#blank(String)}
     * and throws a customized {@link RequirementFailure} if not.
     *
     * <p>Unlike the method {@link #nonEmpty(String, String)},
     * this method allows creation of the message to be deferred until
     * after the null check is made. While this may confer a
     * performance advantage in the non-null case, when deciding to
     * call this method care should be taken that the costs of
     * creating the message supplier are less than the cost of just
     * creating the string message directly.
     *
     * @param value           the {@link String} reference to check
     * @param messageSupplier supplier of the detail message to be
     * used in the event that a {@code RequirementFailure} is thrown
     * @return {@code value} if satisfies {@link IsNot#blank(String)}
     * @throws RequirementFailure if the check fails
     */
    public static String nonBlank( String value, Supplier<String> messageSupplier )
    {

    	return Require.trueFor( value, IsNot.blank(value), messageSupplier );

    }


    /* ***************************** */
    /*  NON EMPTY COLLECTION CHECKS  */
    /* ***************************** */


    /**
     * Checks that the specified {@link Collection} reference is not {@code null} nor empty.
     *
     * @param <V>   the type of the elements in the {@link Collection}
     * @param <C>   the actual implementation of the {@link Collection}
     * @param value the {@link Collection} reference to check for emptiness
     * @return {@code value} if not {@code null} nor empty
     * @throws RequirementFailure if {@code value} is {@code null}
     */
    public static <V,C extends Collection<V>> C nonEmpty( C value )
    {

    	return Require.nonEmpty( value, () -> "this argument must not be empty but was " + value );

    }

    /**
     * Checks that the specified {@link Collection} reference is not {@code null} nor empty
     * and throws a customized {@link RequirementFailure} if it is.
     *
     * @param <V>     the type of the elements in the {@link Collection}
     * @param <C>     the actual implementation of the {@link Collection}
     * @param value   the {@link Collection} reference to check for emptiness
     * @param message detail message to be used in the event that a
     *                  {@code RequirementFailure} is thrown
     * @return {@code value} if not {@code null} nor empty
     * @throws RequirementFailure if {@code value} is {@code null} nor empty
     */
    public static <V,C extends Collection<V>> C nonEmpty( C value, String message )
    {

    	return Require.trueFor( value, IsNot.empty(value), message );

    }

    /**
     * Checks that the specified {@link Collection} reference is not {@code null} nor empty
     * and throws a customized {@link RequirementFailure} if it is.
     *
     * <p>Unlike the method {@link #nonEmpty(Collection, String)},
     * this method allows creation of the message to be deferred until
     * after the null check is made. While this may confer a
     * performance advantage in the non-null case, when deciding to
     * call this method care should be taken that the costs of
     * creating the message supplier are less than the cost of just
     * creating the string message directly.
     *
     * @param <V>             the type of the elements in the {@link Collection}
     * @param <C>             the actual implementation of the {@link Collection}
     * @param value           the {@link Collection} reference to check for emptiness
     * @param messageSupplier supplier of the detail message to be
     * used in the event that a {@code RequirementFailure} is thrown
     * @return {@code value} if not {@code null} nor empty
     * @throws RequirementFailure if {@code value} is {@code null} nor empty
     */
    public static <V,C extends Collection<V>> C nonEmpty( C value, Supplier<String> messageSupplier )
    {

    	return Require.trueFor( value, IsNot.empty(value), messageSupplier );

    }


    /* ********************** */
    /*  NON EMPTY MAP CHECKS  */
    /* ********************** */


    /**
     * Checks that the specified {@link Map} reference is not {@code null} nor empty.
     *
     * @param <K>   the type of the key elements in the {@link Map}
     * @param <V>   the type of the value elements in the {@link Map}
     * @param <M>   the actual implementation of the {@link Map}
     * @param value the {@link Map} reference to check for emptiness
     * @return {@code value} if not {@code null} nor empty
     * @throws RequirementFailure if {@code value} is {@code null}
     */
    public static <K,V,M extends Map<K,V>> M nonEmpty( M value )
    {

    	return Require.nonEmpty( value, () -> "this argument must not be empty but was " + value );

    }

    /**
     * Checks that the specified {@link Map} reference is not {@code null} nor empty
     * and throws a customized {@link RequirementFailure} if it is.
     *
     * @param <K>     the type of the key elements in the {@link Map}
     * @param <V>     the type of the value elements in the {@link Map}
     * @param <M>     the actual implementation of the {@link Map}
     * @param value   the {@link Map} reference to check for emptiness
     * @param message detail message to be used in the event that a
     *                  {@code RequirementFailure} is thrown
     * @return {@code value} if not {@code null} nor empty
     * @throws RequirementFailure if {@code value} is {@code null} nor empty
     */
    public static <K,V,M extends Map<K,V>> M nonEmpty( M value, String message )
    {

    	return Require.trueFor( value, IsNot.empty(value), message );

    }

    /**
     * Checks that the specified {@link Map} reference is not {@code null} nor empty
     * and throws a customized {@link RequirementFailure} if it is.
     *
     * <p>Unlike the method {@link #nonEmpty(Map, String)},
     * this method allows creation of the message to be deferred until
     * after the null check is made. While this may confer a
     * performance advantage in the non-null case, when deciding to
     * call this method care should be taken that the costs of
     * creating the message supplier are less than the cost of just
     * creating the string message directly.
     *
     * @param <K>             the type of the key elements in the {@link Map}
     * @param <V>             the type of the value elements in the {@link Map}
     * @param <M>             the actual implementation of the {@link Map}
     * @param value           the {@link Map} reference to check for emptiness
     * @param messageSupplier supplier of the detail message to be
     * used in the event that a {@code RequirementFailure} is thrown
     * @return {@code value} if not {@code null} nor empty
     * @throws RequirementFailure if {@code value} is {@code null} nor empty
     */
    public static <K,V,M extends Map<K,V>> M nonEmpty( M value, Supplier<String> messageSupplier )
    {

    	return Require.trueFor( value, IsNot.empty(value), messageSupplier );

    }


    /* ************************ */
    /*  NON EMPTY ARRAY CHECKS  */
    /* ************************ */


    /**
     * Checks that the specified {@code array} reference is not {@code null} nor empty.
     *
     * @param <V>   the type of the elements in the {@code array}
     * @param value the {@code array} reference to check for emptiness
     * @return {@code value} if not {@code null} nor empty
     * @throws RequirementFailure if the check fails
     */
    public static <V> V[] nonEmpty( V[] value )
    {

    	return Require.nonEmpty( value, () -> "this argument must not be empty but was " + value );

    }

    /**
     * Checks that the specified {@code array} reference is not {@code null} nor empty
     * and throws a customized {@link RequirementFailure} if it is.
     *
     * @param <V>     the type of the elements in the {@code array}
     * @param value   the {@code array} reference to check for emptiness
     * @param message detail message to be used in the event that a
     *                  {@code RequirementFailure} is thrown
     * @return {@code value} if not {@code null} nor empty
     * @throws RequirementFailure if the check fails
     */
    public static <V> V[] nonEmpty( V[] value, String message )
    {

    	return Require.trueFor( value, IsNot.empty(value), message );

    }

    /**
     * Checks that the specified {@code array} reference is not {@code null} nor empty
     * and throws a customized {@link RequirementFailure} if it is.
     *
     * <p>Unlike the method {@link #nonEmpty(Map, String)},
     * this method allows creation of the message to be deferred until
     * after the null check is made. While this may confer a
     * performance advantage in the non-null case, when deciding to
     * call this method care should be taken that the costs of
     * creating the message supplier are less than the cost of just
     * creating the string message directly.
     *
     * @param <V>             the type of the elements in the {@code array}
     * @param value           the {@code array} reference to check for emptiness
     * @param messageSupplier supplier of the detail message to be
     * used in the event that a {@code RequirementFailure} is thrown
     * @return {@code value} if not {@code null} nor empty
     * @throws RequirementFailure if the check fails
     */
    public static <V> V[] nonEmpty( V[] value, Supplier<String> messageSupplier )
    {

    	return Require.trueFor( value, IsNot.empty(value), messageSupplier );

    }


    /*  ARRAY OF BOOLEAN  */

    /**
     * Checks that the specified {@code array} reference is not {@code null} nor empty.
     *
     * @param value the {@code array} reference to check for emptiness
     * @return {@code value} if not {@code null} nor empty
     * @throws RequirementFailure if the check fails
     */
    public static boolean[] nonEmpty( boolean[] value )
    {

    	return Require.nonEmpty( value, () -> "this argument must not be empty but was " + value );

    }

    /**
     * Checks that the specified {@code array} reference is not {@code null} nor empty
     * and throws a customized {@link RequirementFailure} if it is.
     *
     * @param value   the {@code array} reference to check for emptiness
     * @param message detail message to be used in the event that a
     *                  {@code RequirementFailure} is thrown
     * @return {@code value} if not {@code null} nor empty
     * @throws RequirementFailure if the check fails
     */
    public static boolean[] nonEmpty( boolean[] value, String message )
    {

    	return Require.trueFor( value, IsNot.empty(value), message );

    }

    /**
     * Checks that the specified {@code array} reference is not {@code null} nor empty
     * and throws a customized {@link RequirementFailure} if it is.
     *
     * <p>Unlike the method {@link #nonEmpty(Map, String)},
     * this method allows creation of the message to be deferred until
     * after the null check is made. While this may confer a
     * performance advantage in the non-null case, when deciding to
     * call this method care should be taken that the costs of
     * creating the message supplier are less than the cost of just
     * creating the string message directly.
     *
     * @param value           the {@code array} reference to check for emptiness
     * @param messageSupplier supplier of the detail message to be
     * used in the event that a {@code RequirementFailure} is thrown
     * @return {@code value} if not {@code null} nor empty
     * @throws RequirementFailure if the check fails
     */
    public static boolean[] nonEmpty( boolean[] value, Supplier<String> messageSupplier )
    {

    	return Require.trueFor( value, IsNot.empty(value), messageSupplier );

    }


    /*  ARRAY OF CHAR  */

    /**
     * Checks that the specified {@code array} reference is not {@code null} nor empty.
     *
     * @param value the {@code array} reference to check for emptiness
     * @return {@code value} if not {@code null} nor empty
     * @throws RequirementFailure if the check fails
     */
    public static char[] nonEmpty( char[] value )
    {

        final Object object = value;
    	return Require.nonEmpty( value, () -> "this argument must not be empty but was " + object );

    }

    /**
     * Checks that the specified {@code array} reference is not {@code null} nor empty
     * and throws a customized {@link RequirementFailure} if it is.
     *
     * @param value   the {@code array} reference to check for emptiness
     * @param message detail message to be used in the event that a
     *                  {@code RequirementFailure} is thrown
     * @return {@code value} if not {@code null} nor empty
     * @throws RequirementFailure if the check fails
     */
    public static char[] nonEmpty( char[] value, String message )
    {

    	return Require.trueFor( value, IsNot.empty(value), message );

    }

    /**
     * Checks that the specified {@code array} reference is not {@code null} nor empty
     * and throws a customized {@link RequirementFailure} if it is.
     *
     * <p>Unlike the method {@link #nonEmpty(Map, String)},
     * this method allows creation of the message to be deferred until
     * after the null check is made. While this may confer a
     * performance advantage in the non-null case, when deciding to
     * call this method care should be taken that the costs of
     * creating the message supplier are less than the cost of just
     * creating the string message directly.
     *
     * @param value           the {@code array} reference to check for emptiness
     * @param messageSupplier supplier of the detail message to be
     * used in the event that a {@code RequirementFailure} is thrown
     * @return {@code value} if not {@code null} nor empty
     * @throws RequirementFailure if the check fails
     */
    public static char[] nonEmpty( char[] value, Supplier<String> messageSupplier )
    {

    	return Require.trueFor( value, IsNot.empty(value), messageSupplier );

    }


    /*  ARRAY OF BYTE  */

    /**
     * Checks that the specified {@code array} reference is not {@code null} nor empty.
     *
     * @param value the {@code array} reference to check for emptiness
     * @return {@code value} if not {@code null} nor empty
     * @throws RequirementFailure if the check fails
     */
    public static byte[] nonEmpty( byte[] value )
    {

    	return Require.nonEmpty( value, () -> "this argument must not be empty but was " + value );

    }

    /**
     * Checks that the specified {@code array} reference is not {@code null} nor empty
     * and throws a customized {@link RequirementFailure} if it is.
     *
     * @param value   the {@code array} reference to check for emptiness
     * @param message detail message to be used in the event that a
     *                  {@code RequirementFailure} is thrown
     * @return {@code value} if not {@code null} nor empty
     * @throws RequirementFailure if the check fails
     */
    public static byte[] nonEmpty( byte[] value, String message )
    {

    	return Require.trueFor( value, IsNot.empty(value), message );

    }

    /**
     * Checks that the specified {@code array} reference is not {@code null} nor empty
     * and throws a customized {@link RequirementFailure} if it is.
     *
     * <p>Unlike the method {@link #nonEmpty(Map, String)},
     * this method allows creation of the message to be deferred until
     * after the null check is made. While this may confer a
     * performance advantage in the non-null case, when deciding to
     * call this method care should be taken that the costs of
     * creating the message supplier are less than the cost of just
     * creating the string message directly.
     *
     * @param value           the {@code array} reference to check for emptiness
     * @param messageSupplier supplier of the detail message to be
     * used in the event that a {@code RequirementFailure} is thrown
     * @return {@code value} if not {@code null} nor empty
     * @throws RequirementFailure if the check fails
     */
    public static byte[] nonEmpty( byte[] value, Supplier<String> messageSupplier )
    {

    	return Require.trueFor( value, IsNot.empty(value), messageSupplier );

    }


    /*  ARRAY OF SHORT  */

    /**
     * Checks that the specified {@code array} reference is not {@code null} nor empty.
     *
     * @param value the {@code array} reference to check for emptiness
     * @return {@code value} if not {@code null} nor empty
     * @throws RequirementFailure if the check fails
     */
    public static short[] nonEmpty( short[] value )
    {

    	return Require.nonEmpty( value, () -> "this argument must not be empty but was " + value );

    }

    /**
     * Checks that the specified {@code array} reference is not {@code null} nor empty
     * and throws a customized {@link RequirementFailure} if it is.
     *
     * @param value   the {@code array} reference to check for emptiness
     * @param message detail message to be used in the event that a
     *                  {@code RequirementFailure} is thrown
     * @return {@code value} if not {@code null} nor empty
     * @throws RequirementFailure if the check fails
     */
    public static short[] nonEmpty( short[] value, String message )
    {

    	return Require.trueFor( value, IsNot.empty(value), message );

    }

    /**
     * Checks that the specified {@code array} reference is not {@code null} nor empty
     * and throws a customized {@link RequirementFailure} if it is.
     *
     * <p>Unlike the method {@link #nonEmpty(Map, String)},
     * this method allows creation of the message to be deferred until
     * after the null check is made. While this may confer a
     * performance advantage in the non-null case, when deciding to
     * call this method care should be taken that the costs of
     * creating the message supplier are less than the cost of just
     * creating the string message directly.
     *
     * @param value           the {@code array} reference to check for emptiness
     * @param messageSupplier supplier of the detail message to be
     * used in the event that a {@code RequirementFailure} is thrown
     * @return {@code value} if not {@code null} nor empty
     * @throws RequirementFailure if the check fails
     */
    public static short[] nonEmpty( short[] value, Supplier<String> messageSupplier )
    {

    	return Require.trueFor( value, IsNot.empty(value), messageSupplier );

    }


    /*  ARRAY OF INT  */

    /**
     * Checks that the specified {@code array} reference is not {@code null} nor empty.
     *
     * @param value the {@code array} reference to check for emptiness
     * @return {@code value} if not {@code null} nor empty
     * @throws RequirementFailure if the check fails
     */
    public static int[] nonEmpty( int[] value )
    {

    	return Require.nonEmpty( value, () -> "this argument must not be empty but was " + value );

    }

    /**
     * Checks that the specified {@code array} reference is not {@code null} nor empty
     * and throws a customized {@link RequirementFailure} if it is.
     *
     * @param value   the {@code array} reference to check for emptiness
     * @param message detail message to be used in the event that a
     *                  {@code RequirementFailure} is thrown
     * @return {@code value} if not {@code null} nor empty
     * @throws RequirementFailure if the check fails
     */
    public static int[] nonEmpty( int[] value, String message )
    {

    	return Require.trueFor( value, IsNot.empty(value), message );

    }

    /**
     * Checks that the specified {@code array} reference is not {@code null} nor empty
     * and throws a customized {@link RequirementFailure} if it is.
     *
     * <p>Unlike the method {@link #nonEmpty(Map, String)},
     * this method allows creation of the message to be deferred until
     * after the null check is made. While this may confer a
     * performance advantage in the non-null case, when deciding to
     * call this method care should be taken that the costs of
     * creating the message supplier are less than the cost of just
     * creating the string message directly.
     *
     * @param value           the {@code array} reference to check for emptiness
     * @param messageSupplier supplier of the detail message to be
     * used in the event that a {@code RequirementFailure} is thrown
     * @return {@code value} if not {@code null} nor empty
     * @throws RequirementFailure if the check fails
     */
    public static int[] nonEmpty( int[] value, Supplier<String> messageSupplier )
    {

    	return Require.trueFor( value, IsNot.empty(value), messageSupplier );

    }


    /*  ARRAY OF FLOAT  */

    /**
     * Checks that the specified {@code array} reference is not {@code null} nor empty.
     *
     * @param value the {@code array} reference to check for emptiness
     * @return {@code value} if not {@code null} nor empty
     * @throws RequirementFailure if the check fails
     */
    public static float[] nonEmpty( float[] value )
    {

    	return Require.nonEmpty( value, () -> "this argument must not be empty but was " + value );

    }

    /**
     * Checks that the specified {@code array} reference is not {@code null} nor empty
     * and throws a customized {@link RequirementFailure} if it is.
     *
     * @param value   the {@code array} reference to check for emptiness
     * @param message detail message to be used in the event that a
     *                  {@code RequirementFailure} is thrown
     * @return {@code value} if not {@code null} nor empty
     * @throws RequirementFailure if the check fails
     */
    public static float[] nonEmpty( float[] value, String message )
    {

    	return Require.trueFor( value, IsNot.empty(value), message );

    }

    /**
     * Checks that the specified {@code array} reference is not {@code null} nor empty
     * and throws a customized {@link RequirementFailure} if it is.
     *
     * <p>Unlike the method {@link #nonEmpty(Map, String)},
     * this method allows creation of the message to be deferred until
     * after the null check is made. While this may confer a
     * performance advantage in the non-null case, when deciding to
     * call this method care should be taken that the costs of
     * creating the message supplier are less than the cost of just
     * creating the string message directly.
     *
     * @param value           the {@code array} reference to check for emptiness
     * @param messageSupplier supplier of the detail message to be
     * used in the event that a {@code RequirementFailure} is thrown
     * @return {@code value} if not {@code null} nor empty
     * @throws RequirementFailure if the check fails
     */
    public static float[] nonEmpty( float[] value, Supplier<String> messageSupplier )
    {

    	return Require.trueFor( value, IsNot.empty(value), messageSupplier );

    }


    /*  ARRAY OF LONG  */

    /**
     * Checks that the specified {@code array} reference is not {@code null} nor empty.
     *
     * @param value the {@code array} reference to check for emptiness
     * @return {@code value} if not {@code null} nor empty
     * @throws RequirementFailure if the check fails
     */
    public static long[] nonEmpty( long[] value )
    {

    	return Require.nonEmpty( value, () -> "this argument must not be empty but was " + value );

    }

    /**
     * Checks that the specified {@code array} reference is not {@code null} nor empty
     * and throws a customized {@link RequirementFailure} if it is.
     *
     * @param value   the {@code array} reference to check for emptiness
     * @param message detail message to be used in the event that a
     *                  {@code RequirementFailure} is thrown
     * @return {@code value} if not {@code null} nor empty
     * @throws RequirementFailure if the check fails
     */
    public static long[] nonEmpty( long[] value, String message )
    {

    	return Require.trueFor( value, IsNot.empty(value), message );

    }

    /**
     * Checks that the specified {@code array} reference is not {@code null} nor empty
     * and throws a customized {@link RequirementFailure} if it is.
     *
     * <p>Unlike the method {@link #nonEmpty(Map, String)},
     * this method allows creation of the message to be deferred until
     * after the null check is made. While this may confer a
     * performance advantage in the non-null case, when deciding to
     * call this method care should be taken that the costs of
     * creating the message supplier are less than the cost of just
     * creating the string message directly.
     *
     * @param value           the {@code array} reference to check for emptiness
     * @param messageSupplier supplier of the detail message to be
     * used in the event that a {@code RequirementFailure} is thrown
     * @return {@code value} if not {@code null} nor empty
     * @throws RequirementFailure if the check fails
     */
    public static long[] nonEmpty( long[] value, Supplier<String> messageSupplier )
    {

    	return Require.trueFor( value, IsNot.empty(value), messageSupplier );

    }


    /*  ARRAY OF DOUBLE  */

    /**
     * Checks that the specified {@code array} reference is not {@code null} nor empty.
     *
     * @param value the {@code array} reference to check for emptiness
     * @return {@code value} if not {@code null} nor empty
     * @throws RequirementFailure if the check fails
     */
    public static double[] nonEmpty( double[] value )
    {

    	return Require.nonEmpty( value, () -> "this argument must not be empty but was " + value );

    }

    /**
     * Checks that the specified {@code array} reference is not {@code null} nor empty
     * and throws a customized {@link RequirementFailure} if it is.
     *
     * @param value   the {@code array} reference to check for emptiness
     * @param message detail message to be used in the event that a
     *                  {@code RequirementFailure} is thrown
     * @return {@code value} if not {@code null} nor empty
     * @throws RequirementFailure if the check fails
     */
    public static double[] nonEmpty( double[] value, String message )
    {

    	return Require.trueFor( value, IsNot.empty(value), message );

    }

    /**
     * Checks that the specified {@code array} reference is not {@code null} nor empty
     * and throws a customized {@link RequirementFailure} if it is.
     *
     * <p>Unlike the method {@link #nonEmpty(Map, String)},
     * this method allows creation of the message to be deferred until
     * after the null check is made. While this may confer a
     * performance advantage in the non-null case, when deciding to
     * call this method care should be taken that the costs of
     * creating the message supplier are less than the cost of just
     * creating the string message directly.
     *
     * @param value           the {@code array} reference to check for emptiness
     * @param messageSupplier supplier of the detail message to be
     * used in the event that a {@code RequirementFailure} is thrown
     * @return {@code value} if not {@code null} nor empty
     * @throws RequirementFailure if the check fails
     */
    public static double[] nonEmpty( double[] value, Supplier<String> messageSupplier )
    {

    	return Require.trueFor( value, IsNot.empty(value), messageSupplier );

    }

}

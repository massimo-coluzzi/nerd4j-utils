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
package org.nerd4j.utils.math;

import org.nerd4j.utils.lang.IsNot;
import org.nerd4j.utils.lang.Require;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.BinaryOperator;

/**
 * This utility class provides some convenience methods
 * to deal with {@link BigDecimal}s.
 *
 * <p>
 * The name of this class is an abbreviation of
 * BigDecimal. We have intentionally used
 * a short name for readability because this class
 * is intended to be used in calculations and algebraic
 * statements, for example:
 * <pre>
 * double a = ...;
 * double b = ...;
 * BigDecimal c = ...;
 * if( BD.lt(a,BD.sum(b,c) )
 *   // Do something...
 * </pre>
 * instead of
 * <pre>
 * double a = ...;
 * double b = ...;
 * BigDecimal c = ...;
 * if( BigDecimal.valueOf(a).compareTo(BigDecimal.valueOf(b).add(c)) &lt; 0 )
 *   // Do something...
 * </pre>
 *
 * <p>
 * The aim of this utility class is to facilitate operations with {@link BigDecimal}s.
 *
 * @author Massimo Coluzzi
 * @since 2.0.0
 */
public class BD extends CU
{

    /** Performs the sum of two {@code non-null} {@link BigDecimal} values. */
    public static BinaryOperator<BigDecimal> SUM = ( a, b ) -> a.add( b );

    /** Performs the subtraction of two {@code non-null} {@link BigDecimal} values. */
    public static BinaryOperator<BigDecimal> SUB = ( a, b ) -> a.subtract( b );

    /** Performs the multiplication of two {@code non-null} {@link BigDecimal} values. */
    public static BinaryOperator<BigDecimal> MUL = ( a, b ) -> a.multiply( b );

    /** Performs the division of two {@code non-null} {@link BigDecimal} values. */
    public static BinaryOperator<BigDecimal> DIV = ( a, b ) -> a.divide( b );

    /** Performs the reminder of the division of two {@code non-null} {@link BigDecimal} values. */
    public static BinaryOperator<BigDecimal> REM = ( a, b ) -> a.remainder( b );

    /** Finds the minimum of two {@code non-null} {@link BigDecimal} values. */
    public static BinaryOperator<BigDecimal> MIN = ( a, b ) -> a.min( b );

    /** Finds the maximum of two {@code non-null} {@link BigDecimal} values. */
    public static BinaryOperator<BigDecimal> MAX = ( a, b ) -> a.max( b );


    /**
     * This class is intended to be static
     * so there is no public constructor.
     */
    private BD() {}


    /* ***************** */
    /*  FACTORY METHODS  */
    /* ***************** */


    /**
     * Wrapper for the method {@link BigDecimal#valueOf(double)}.
     *
     * @param value value of the BigDecimal to return.
     * @return a BigDecimal with the specified value.
     */
    public static BigDecimal of( double value )
    {

        return BigDecimal.valueOf( value );

    }

    /**
     * Wrapper for the method {@link BigDecimal#valueOf(long)}.
     *
     * @param value value of the BigDecimal to return.
     * @return a BigDecimal with the specified value.
     */
    public static BigDecimal of( long value )
    {

        return BigDecimal.valueOf( value );

    }

    /**
     * Transforms the string representation of a number
     * into a {@link BigDecimal}.
     * <p>
     * If the argument is {@code null} or empty
     * this method returns {@code null}.
     * <p>
     * If the argument is not empty and does not represent
     * a number this method throws an exception.
     *
     * @param value decimal String representation of BigDecimal.
     * @return a BigDecimal with the specified value.
     * @throws NumberFormatException if {@code value} is not a valid
     *         representation of a BigDecimal.
     */
    public static BigDecimal of( String value )
    {

        return IsNot.empty( value )
        ? new BigDecimal( value )
        : null;

    }

    /**
     * Transforms the given {@link BigInteger}
     * into a {@link BigDecimal}.
     * <p>
     * If the argument is {@code null}
     * this method returns {@code null}.
     *
     * @param value the BigInteger to be converted into a BigDecimal.
     * @return a BigDecimal with the specified value.
     */
    public static BigDecimal of( BigInteger value )
    {

        return value == null ? null : new BigDecimal( value );

    }


    /* ************ */
    /*  METHOD ADD  */
    /* ************ */


    /**
     * This is a {@code null-safe} method that, given two {@link BigDecimal}
     * values {@code a} and {@code b}, returns {@code a + b}.
     * <p>
     * If one of the values is {@code null}, the other will be returned.
     * If both values are {@code null}, then {@code null} will be returned.
     *
     * @param a value to add.
     * @param b value to add.
     * @return a {@link BigDecimal} representing {@code a + b}.
     */
    public static BigDecimal sum( BigDecimal a, BigDecimal b )
    {


        return apply( SUM, a, b );

    }

    /**
     * This is a {@code null-safe} method that, given a {@link BigDecimal}
     * value {@code a} and a {@code double} value {@code b}, returns {@code a + b}.
     * <p>
     * If the {@link BigDecimal} values is {@code null}, the other will be returned.
     *
     * @param a value to add.
     * @param b value to add.
     * @return a {@link BigDecimal} representing {@code a + b}.
     */
    public static BigDecimal sum( BigDecimal a, double b )
    {


        final BigDecimal bigB = of( b );
        return a == null ? bigB : a.add( bigB );

    }

    /**
     * This is a {@code null-safe} method that, given a {@code double} value
     * {@code a} and a {@link BigDecimal} value {@code b}, returns {@code a + b}.
     * <p>
     * If the {@link BigDecimal} values is {@code null}, the other will be returned.
     *
     * @param a value to add.
     * @param b value to add.
     * @return a {@link BigDecimal} representing {@code a + b}.
     */
    public static BigDecimal sum( double a, BigDecimal b )
    {

        final BigDecimal bigA = of( a );
        return b == null ? bigA : bigA.add( b );

    }

    /**
     * Given two {@code double} values {@code a} and {@code b},
     * returns a {@link BigDecimal} that represents the value
     * {@code a + b}.
     *
     * @param a value to add.
     * @param b value to add.
     * @return a {@link BigDecimal} representing {@code a + b}.
     */
    public static BigDecimal sum( double a, double b )
    {

        final BigDecimal bigA = of( a );
        final BigDecimal bigB = of( b );
        return bigA.add( bigB );

    }


    /* ************ */
    /*  METHOD SUB  */
    /* ************ */


    /**
     * This is a {@code null-safe} method that, given two {@link BigDecimal}
     * values {@code a} and {@code b}, returns {@code a - b}.
     * <p>
     * If one of the values is {@code null}, the other will be returned.
     * If both values are {@code null}, then {@code null} will be returned.
     *
     * @param a value to subtract from.
     * @param b value to be subtracted.
     * @return a {@link BigDecimal} representing {@code a - b}.
     */
    public static BigDecimal sub( BigDecimal a, BigDecimal b )
    {


        return apply( SUB, a, b );

    }

    /**
     * This is a {@code null-safe} method that, given a {@link BigDecimal}
     * value {@code a} and a {@code double} value {@code b}, returns {@code a - b}.
     * <p>
     * If the {@link BigDecimal} values is {@code null}, the other will be returned.
     *
     * @param a value to subtract from.
     * @param b value to be subtracted.
     * @return a {@link BigDecimal} representing {@code a - b}.
     */
    public static BigDecimal sub( BigDecimal a, double b )
    {

        final BigDecimal bigB = of( b );
        return a == null ? bigB : a.subtract( bigB );

    }

    /**
     * This is a {@code null-safe} method that, given a {@code double} value
     * {@code a} and a {@link BigDecimal} value {@code b}, returns {@code a - b}.
     * <p>
     * If the {@link BigDecimal} values is {@code null}, the other will be returned.
     *
     * @param a value to subtract from.
     * @param b value to be subtracted.
     * @return a {@link BigDecimal} representing {@code a - b}.
     */
    public static BigDecimal sub( double a, BigDecimal b )
    {


        final BigDecimal bigA = of( a );
        return b == null ? bigA : bigA.subtract( b );

    }

    /**
     * Given two {@code double} values {@code a} and {@code b},
     * returns a {@link BigDecimal} that represents the value
     * {@code a - b}.
     *
     * @param a value to subtract from.
     * @param b value to be subtracted.
     * @return a {@link BigDecimal} representing {@code a - b}.
     */
    public static BigDecimal sub( double a, double b )
    {

        final BigDecimal bigA = of( a );
        final BigDecimal bigB = of( b );
        return bigA.subtract( bigB );

    }


    /* ************ */
    /*  METHOD MUL  */
    /* ************ */


    /**
     * This is a {@code null-safe} method that, given two {@link BigDecimal}
     * values {@code a} and {@code b}, returns {@code a * b}.
     * <p>
     * If one of the values is {@code null}, the other will be returned.
     * If both values are {@code null}, then {@code null} will be returned.
     *
     * @param a value to multiply.
     * @param b value to multiply.
     * @return a {@link BigDecimal} representing {@code a * b}.
     */
    public static BigDecimal mul( BigDecimal a, BigDecimal b )
    {

        return apply( MUL, a, b );

    }

    /**
     * This is a {@code null-safe} method that, given a {@link BigDecimal}
     * value {@code a} and a {@code double} value {@code b}, returns {@code a * b}.
     * <p>
     * If the {@link BigDecimal} values is {@code null}, the other will be returned.
     *
     * @param a value to multiply.
     * @param b value to multiply.
     * @return a {@link BigDecimal} representing {@code a * b}.
     */
    public static BigDecimal mul( BigDecimal a, double b )
    {

        final BigDecimal bigB = of( b );
        return a == null ? bigB : a.multiply( bigB );

    }

    /**
     * This is a {@code null-safe} method that, given a {@code double} value
     * {@code a} and a {@link BigDecimal} value {@code b}, returns {@code a * b}.
     * <p>
     * If the {@link BigDecimal} values is {@code null}, the other will be returned.
     *
     * @param a value to multiply.
     * @param b value to multiply.
     * @return a {@link BigDecimal} representing {@code a * b}.
     */
    public static BigDecimal mul( double a, BigDecimal b )
    {

        final BigDecimal bigA = of( a );
        return b == null ? bigA : bigA.multiply( b );

    }

    /**
     * Given two {@code double} values {@code a} and {@code b},
     * returns a {@link BigDecimal} that represents the value
     * {@code a * b}.
     *
     * @param a value to multiply.
     * @param b value to multiply.
     * @return a {@link BigDecimal} representing {@code a * b}.
     */
    public static BigDecimal mul( double a, double b )
    {

        final BigDecimal bigA = of( a );
        final BigDecimal bigB = of( b );
        return bigA.multiply( bigB );

    }


    /* ************ */
    /*  METHOD DIV  */
    /* ************ */


    /**
     * This is a {@code null-safe} method that, given two {@link BigDecimal}
     * values {@code a} and {@code b}, returns {@code a / b}.
     * <p>
     * If one of the values is {@code null}, the other will be returned.
     * If both values are {@code null}, then {@code null} will be returned.
     *
     * @param a value to be divided.
     * @param b the divider value.
     * @return a {@link BigDecimal} representing {@code a / b}.
     */
    public static BigDecimal div( BigDecimal a, BigDecimal b )
    {


        return apply( DIV, a, b );

    }

    /**
     * This is a {@code null-safe} method that, given a {@link BigDecimal}
     * value {@code a} and a {@code double} value {@code b}, returns {@code a / b}.
     * <p>
     * If the {@link BigDecimal} values is {@code null}, the other will be returned.
     *
     * @param a value to be divided.
     * @param b the divider value.
     * @return a {@link BigDecimal} representing {@code a / b}.
     */
    public static BigDecimal div( BigDecimal a, double b )
    {

        final BigDecimal bigB = of( b );
        return a == null ? bigB : a.divide( bigB );

    }

    /**
     * This is a {@code null-safe} method that, given a {@code double} value
     * {@code a} and a {@link BigDecimal} value {@code b}, returns {@code a / b}.
     * <p>
     * If the {@link BigDecimal} values is {@code null}, the other will be returned.
     *
     * @param a value to be divided.
     * @param b the divider value.
     * @return a {@link BigDecimal} representing {@code a / b}.
     */
    public static BigDecimal div( double a, BigDecimal b )
    {

        final BigDecimal bigA = of( a );
        return b == null ? bigA : bigA.divide( b );

    }

    /**
     * Given two {@code double} values {@code a} and {@code b},
     * returns a {@link BigDecimal} that represents the value
     * {@code a / b}.
     *
     * @param a value to be divided.
     * @param b the divider value.
     * @return a {@link BigDecimal} representing {@code a / b}.
     */
    public static BigDecimal div( double a, double b )
    {

        return of( a / b );

    }


    /* ************ */
    /*  METHOD REM  */
    /* ************ */


    /**
     * This is a {@code null-safe} method that, given two {@link BigDecimal}
     * values {@code a} and {@code b}, returns {@code a % b}.
     * <p>
     * If one of the values is {@code null}, the other will be returned.
     * If both values are {@code null}, then {@code null} will be returned.
     *
     * @param a value to be divided.
     * @param b the divider value.
     * @return a {@link BigDecimal} representing {@code a % b}.
     */
    public static BigDecimal rem( BigDecimal a, BigDecimal b )
    {

        return apply( REM, a, b );

    }

    /**
     * This is a {@code null-safe} method that, given a {@link BigDecimal}
     * value {@code a} and a {@code double} value {@code b}, returns {@code a % b}.
     * <p>
     * If the {@link BigDecimal} values is {@code null}, the other will be returned.
     *
     * @param a value to be divided.
     * @param b the divider value.
     * @return a {@link BigDecimal} representing {@code a % b}.
     */
    public static BigDecimal rem( BigDecimal a, double b )
    {

        final BigDecimal bigB = of( b );
        return a == null ? bigB : a.remainder( bigB );

    }

    /**
     * This is a {@code null-safe} method that, given a {@code double} value
     * {@code a} and a {@link BigDecimal} value {@code b}, returns {@code a % b}.
     * <p>
     * If the {@link BigDecimal} values is {@code null}, the other will be returned.
     *
     * @param a value to be divided.
     * @param b the divider value.
     * @return a {@link BigDecimal} representing {@code a % b}.
     */
    public static BigDecimal rem( double a, BigDecimal b )
    {

        final BigDecimal bigA = of( a );
        return b == null ? bigA : bigA.remainder( b );

    }

    /**
     * Given two {@code double} values {@code a} and {@code b},
     * returns a {@link BigDecimal} that represents the value
     * {@code a % b}.
     *
     * @param a value to be divided.
     * @param b the divider value.
     * @return a {@link BigDecimal} representing {@code a % b}.
     */
    public static BigDecimal rem( double a, double b )
    {

        final BigDecimal bigA = of( a );
        final BigDecimal bigB = of( b );
        return bigA.remainder( bigB );

    }


    /* ************ */
    /*  METHOD MIN  */
    /* ************ */


    /**
     * This is a {@code null-safe} method that, given a {@link BigDecimal}
     * value {@code a} and a {@code double} value {@code b}, returns the
     * minimum value between them. If the values are equal returns {@code a}.
     * <p>
     * This method considers {@code null} to be less than {@code non-null}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @return the minimum between {@code a} and {@code b}.
     */
    public static BigDecimal min( BigDecimal a, double b )
    {

        return min( a, of(b) );

    }

    /**
     * This is a {@code null-safe} method that, given a {@link BigDecimal}
     * value {@code a} and a {@code double} value {@code b}, returns the
     * minimum value between them. If the values are equal returns {@code a}.
     * <p>
     * This method considers {@code null} to be less than or greater
     * than {@code non-null} based on the value of {@code nullLast}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @param nullLast tells if {@code null} is greater than {@code non-null}.
     * @return the minimum between {@code a} and {@code b}.
     */
    public static BigDecimal min( BigDecimal a, double b, boolean nullLast )
    {

        return min( a, of(b), nullLast );

    }

    /**
     * This is a {@code null-safe} method that, given a {@code double}
     * value {@code a} and a {@link BigDecimal} value {@code b}, returns the
     * minimum value between them. If the values are equal returns {@code a}.
     * <p>
     * This method considers {@code null} to be less than {@code non-null}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @return the minimum between {@code a} and {@code b}.
     */
    public static BigDecimal min( double a, BigDecimal b )
    {

        return min( of(a), b );

    }

    /**
     * This is a {@code null-safe} method that, given a {@code double}
     * value {@code a} and a {@link BigDecimal} value {@code b}, returns the
     * minimum value between them. If the values are equal returns {@code a}.
     * <p>
     * This method considers {@code null} to be less than or greater
     * than {@code non-null} based on the value of {@code nullLast}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @param nullLast tells if {@code null} is greater than {@code non-null}.
     * @return the minimum between {@code a} and {@code b}.
     */
    public static BigDecimal min( double a, BigDecimal b, boolean nullLast )
    {

        return min( of(a), b, nullLast );

    }

    /**
     * Given two {@code double} values {@code a} and {@code b},
     * returns a {@link BigDecimal} that represents the minimum
     * of them.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @return the minimum between {@code a} and {@code b}.
     */
    public static BigDecimal min( double a, double b )
    {

        return a <= b ? of( a ) : of( b );

    }


    /* ************ */
    /*  METHOD MAX  */
    /* ************ */


    /**
     * This is a {@code null-safe} method that, given a {@link BigDecimal}
     * value {@code a} and a {@code double} value {@code b}, returns the
     * maximum value between them. If the values are equal returns {@code a}.
     * <p>
     * This method considers {@code null} to be less than {@code non-null}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @return the maximum between {@code a} and {@code b}.
     */
    public static BigDecimal max( BigDecimal a, double b )
    {

        return max( a, of(b) );

    }

    /**
     * This is a {@code null-safe} method that, given a {@link BigDecimal}
     * value {@code a} and a {@code double} value {@code b}, returns the
     * maximum value between them. If the values are equal returns {@code a}.
     * <p>
     * This method considers {@code null} to be less than or greater
     * than {@code non-null} based on the value of {@code nullLast}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @param nullLast tells if {@code null} is greater than {@code non-null}.
     * @return the maximum between {@code a} and {@code b}.
     */
    public static BigDecimal max( BigDecimal a, double b, boolean nullLast )
    {

        return max( a, of(b), nullLast );

    }

    /**
     * This is a {@code null-safe} method that, given a {@code double}
     * value {@code a} and a {@link BigDecimal} value {@code b}, returns the
     * maximum value between them. If the values are equal returns {@code a}.
     * <p>
     * This method considers {@code null} to be less than {@code non-null}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @return the maximum between {@code a} and {@code b}.
     */
    public static BigDecimal max( double a, BigDecimal b )
    {

        return max( of(a), b );

    }

    /**
     * This is a {@code null-safe} method that, given a {@code double}
     * value {@code a} and a {@link BigDecimal} value {@code b}, returns the
     * maximum value between them. If the values are equal returns {@code a}.
     * <p>
     * This method considers {@code null} to be less than or greater
     * than {@code non-null} based on the value of {@code nullLast}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @param nullLast tells if {@code null} is greater than {@code non-null}.
     * @return the maximum between {@code a} and {@code b}.
     */
    public static BigDecimal max( double a, BigDecimal b, boolean nullLast )
    {

        return max( of(a), b, nullLast );

    }

    /**
     * Given two {@code double} values {@code a} and {@code b},
     * returns a {@link BigDecimal} that represents the maximum
     * of them.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @return the maximum between {@code a} and {@code b}.
     */
    public static BigDecimal max( double a, double b )
    {

        return a >= b ? of( a ) : of( b );

    }


    /* *********** */
    /*  METHOD LT  */
    /* *********** */

    /**
     * This is a {@code null-safe} method that, given a {@link BigDecimal}
     * value {@code a} and a {@code double} value {@code b}, returns {@code true}
     * if {@code a < b}.
     * <p>
     * This method considers {@code null} to be less than {@code non-null}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @return {@code a < b}.
     */
    public static boolean lt( BigDecimal a, double b )
    {

        return lt( a, of(b) );

    }

    /**
     * This is a {@code null-safe} method that, given a {@link BigDecimal}
     * value {@code a} and a {@code double} value {@code b}, returns {@code true}
     * if {@code a < b}.
     * <p>
     * This method considers {@code null} to be less than or greater
     * than {@code non-null} based on the value of {@code nullLast}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @param nullLast tells if {@code null} is greater than {@code non-null}.
     * @return {@code a < b}.
     */
    public static boolean lt( BigDecimal a, double b, boolean nullLast )
    {

        return lt( a, of(b), nullLast );

    }

    /**
     * This is a {@code null-safe} method that, given a {@code double}
     * value {@code a} and a {@link BigDecimal} value {@code b},
     * returns {@code true} if {@code a < b}.
     * <p>
     * This method considers {@code null} to be less than {@code non-null}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @return {@code a < b}.
     */
    public static boolean lt( double a, BigDecimal b )
    {

        return lt( of(a), b );

    }

    /**
     * This is a {@code null-safe} method that, given a {@code double}
     * value {@code a} and a {@link BigDecimal} value {@code b},
     * returns {@code true} if {@code a < b}.
     * <p>
     * This method considers {@code null} to be less than or greater
     * than {@code non-null} based on the value of {@code nullLast}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @param nullLast tells if {@code null} is greater than {@code non-null}.
     * @return {@code a < b}.
     */
    public static boolean lt( double a, BigDecimal b, boolean nullLast )
    {

        return lt( of(a), b, nullLast );

    }


    /* *********** */
    /*  METHOD LE  */
    /* *********** */

    /**
     * This is a {@code null-safe} method that, given a {@link BigDecimal}
     * value {@code a} and a {@code double} value {@code b}, returns {@code true}
     * if {@code a <= b}.
     * <p>
     * This method considers {@code null} to be less than {@code non-null}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @return {@code a <= b}.
     */
    public static boolean le( BigDecimal a, double b )
    {

        return le( a, of(b) );

    }

    /**
     * This is a {@code null-safe} method that, given a {@link BigDecimal}
     * value {@code a} and a {@code double} value {@code b}, returns {@code true}
     * if {@code a <= b}.
     * <p>
     * This method considers {@code null} to be less than or greater
     * than {@code non-null} based on the value of {@code nullLast}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @param nullLast tells if {@code null} is greater than {@code non-null}.
     * @return {@code a <= b}.
     */
    public static boolean le( BigDecimal a, double b, boolean nullLast )
    {

        return le( a, of(b), nullLast );

    }

    /**
     * This is a {@code null-safe} method that, given a {@code double}
     * value {@code a} and a {@link BigDecimal} value {@code b},
     * returns {@code true} if {@code a <= b}.
     * <p>
     * This method considers {@code null} to be less than {@code non-null}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @return {@code a <= b}.
     */
    public static boolean le( double a, BigDecimal b )
    {

        return le( of(a), b );

    }

    /**
     * This is a {@code null-safe} method that, given a {@code double}
     * value {@code a} and a {@link BigDecimal} value {@code b},
     * returns {@code true} if {@code a <= b}.
     * <p>
     * This method considers {@code null} to be less than or greater
     * than {@code non-null} based on the value of {@code nullLast}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @param nullLast tells if {@code null} is greater than {@code non-null}.
     * @return {@code a <= b}.
     */
    public static boolean le( double a, BigDecimal b, boolean nullLast )
    {

        return le( of(a), b, nullLast );

    }


    /* *********** */
    /*  METHOD EQ  */
    /* *********** */

    /**
     * This is a {@code null-safe} method that, given a {@link BigDecimal}
     * value {@code a} and a {@code double} value {@code b}, returns {@code true}
     * if {@code a == b}.
     * <p>
     * This method considers {@code null} to be less than {@code non-null}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @return {@code a == b}.
     */
    public static boolean eq( BigDecimal a, double b )
    {

        return eq( a, of(b) );

    }

    /**
     * This is a {@code null-safe} method that, given a {@link BigDecimal}
     * value {@code a} and a {@code double} value {@code b}, returns {@code true}
     * if {@code a == b}.
     * <p>
     * This method considers {@code null} to be less than or greater
     * than {@code non-null} based on the value of {@code nullLast}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @param nullLast tells if {@code null} is greater than {@code non-null}.
     * @return {@code a == b}.
     */
    public static boolean eq( BigDecimal a, double b, boolean nullLast )
    {

        return eq( a, of(b), nullLast );

    }

    /**
     * This is a {@code null-safe} method that, given a {@code double}
     * value {@code a} and a {@link BigDecimal} value {@code b},
     * returns {@code true} if {@code a == b}.
     * <p>
     * This method considers {@code null} to be less than {@code non-null}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @return {@code a == b}.
     */
    public static boolean eq( double a, BigDecimal b )
    {

        return eq( of(a), b );

    }

    /**
     * This is a {@code null-safe} method that, given a {@code double}
     * value {@code a} and a {@link BigDecimal} value {@code b},
     * returns {@code true} if {@code a == b}.
     * <p>
     * This method considers {@code null} to be less than or greater
     * than {@code non-null} based on the value of {@code nullLast}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @param nullLast tells if {@code null} is greater than {@code non-null}.
     * @return {@code a == b}.
     */
    public static boolean eq( double a, BigDecimal b, boolean nullLast )
    {

        return eq( of(a), b, nullLast );

    }


    /* *********** */
    /*  METHOD NE  */
    /* *********** */

    /**
     * This is a {@code null-safe} method that, given a {@link BigDecimal}
     * value {@code a} and a {@code double} value {@code b}, returns {@code true}
     * if {@code a != b}.
     * <p>
     * This method considers {@code null} to be less than {@code non-null}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @return {@code a != b}.
     */
    public static boolean ne( BigDecimal a, double b )
    {

        return ne( a, of(b) );

    }

    /**
     * This is a {@code null-safe} method that, given a {@link BigDecimal}
     * value {@code a} and a {@code double} value {@code b}, returns {@code true}
     * if {@code a != b}.
     * <p>
     * This method considers {@code null} to be less than or greater
     * than {@code non-null} based on the value of {@code nullLast}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @param nullLast tells if {@code null} is greater than {@code non-null}.
     * @return {@code a != b}.
     */
    public static boolean ne( BigDecimal a, double b, boolean nullLast )
    {

        return ne( a, of(b), nullLast );

    }

    /**
     * This is a {@code null-safe} method that, given a {@code double}
     * value {@code a} and a {@link BigDecimal} value {@code b},
     * returns {@code true} if {@code a != b}.
     * <p>
     * This method considers {@code null} to be less than {@code non-null}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @return {@code a != b}.
     */
    public static boolean ne( double a, BigDecimal b )
    {

        return ne( of(a), b );

    }

    /**
     * This is a {@code null-safe} method that, given a {@code double}
     * value {@code a} and a {@link BigDecimal} value {@code b},
     * returns {@code true} if {@code a != b}.
     * <p>
     * This method considers {@code null} to be less than or greater
     * than {@code non-null} based on the value of {@code nullLast}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @param nullLast tells if {@code null} is greater than {@code non-null}.
     * @return {@code a != b}.
     */
    public static boolean ne( double a, BigDecimal b, boolean nullLast )
    {

        return ne( of(a), b, nullLast );

    }


    /* *********** */
    /*  METHOD GE  */
    /* *********** */

    /**
     * This is a {@code null-safe} method that, given a {@link BigDecimal}
     * value {@code a} and a {@code double} value {@code b}, returns {@code true}
     * if {@code a >= b}.
     * <p>
     * This method considers {@code null} to be less than {@code non-null}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @return {@code a >= b}.
     */
    public static boolean ge( BigDecimal a, double b )
    {

        return ge( a, of(b) );

    }

    /**
     * This is a {@code null-safe} method that, given a {@link BigDecimal}
     * value {@code a} and a {@code double} value {@code b}, returns {@code true}
     * if {@code a >= b}.
     * <p>
     * This method considers {@code null} to be less than or greater
     * than {@code non-null} based on the value of {@code nullLast}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @param nullLast tells if {@code null} is greater than {@code non-null}.
     * @return {@code a >= b}.
     */
    public static boolean ge( BigDecimal a, double b, boolean nullLast )
    {

        return ge( a, of(b), nullLast );

    }

    /**
     * This is a {@code null-safe} method that, given a {@code double}
     * value {@code a} and a {@link BigDecimal} value {@code b},
     * returns {@code true} if {@code a >= b}.
     * <p>
     * This method considers {@code null} to be less than {@code non-null}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @return {@code a >= b}.
     */
    public static boolean ge( double a, BigDecimal b )
    {

        return ge( of(a), b );

    }

    /**
     * This is a {@code null-safe} method that, given a {@code double}
     * value {@code a} and a {@link BigDecimal} value {@code b},
     * returns {@code true} if {@code a >= b}.
     * <p>
     * This method considers {@code null} to be less than or greater
     * than {@code non-null} based on the value of {@code nullLast}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @param nullLast tells if {@code null} is greater than {@code non-null}.
     * @return {@code a >= b}.
     */
    public static boolean ge( double a, BigDecimal b, boolean nullLast )
    {

        return ge( of(a), b, nullLast );

    }


    /* *********** */
    /*  METHOD GT  */
    /* *********** */

    /**
     * This is a {@code null-safe} method that, given a {@link BigDecimal}
     * value {@code a} and a {@code double} value {@code b}, returns {@code true}
     * if {@code a > b}.
     * <p>
     * This method considers {@code null} to be less than {@code non-null}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @return {@code a > b}.
     */
    public static boolean gt( BigDecimal a, double b )
    {

        return gt( a, of(b) );

    }

    /**
     * This is a {@code null-safe} method that, given a {@link BigDecimal}
     * value {@code a} and a {@code double} value {@code b}, returns {@code true}
     * if {@code a > b}.
     * <p>
     * This method considers {@code null} to be less than or greater
     * than {@code non-null} based on the value of {@code nullLast}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @param nullLast tells if {@code null} is greater than {@code non-null}.
     * @return {@code a > b}.
     */
    public static boolean gt( BigDecimal a, double b, boolean nullLast )
    {

        return gt( a, of(b), nullLast );

    }

    /**
     * This is a {@code null-safe} method that, given a {@code double}
     * value {@code a} and a {@link BigDecimal} value {@code b},
     * returns {@code true} if {@code a > b}.
     * <p>
     * This method considers {@code null} to be less than {@code non-null}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @return {@code a > b}.
     */
    public static boolean gt( double a, BigDecimal b )
    {

        return gt( of(a), b );

    }

    /**
     * This is a {@code null-safe} method that, given a {@code double}
     * value {@code a} and a {@link BigDecimal} value {@code b},
     * returns {@code true} if {@code a > b}.
     * <p>
     * This method considers {@code null} to be less than or greater
     * than {@code non-null} based on the value of {@code nullLast}.
     *
     * @param a value to compare.
     * @param b value to compare.
     * @param nullLast tells if {@code null} is greater than {@code non-null}.
     * @return {@code a > b}.
     */
    public static boolean gt( double a, BigDecimal b, boolean nullLast )
    {

        return gt( of(a), b, nullLast );

    }


    /* ************** */
    /*  METHOD APPLY  */
    /* ************** */

    /**
     * This is a {@code null-safe} method that, given two {@link BigDecimal}
     * values {@code a} and {@code b}, returns the result of the given operator.
     * <p>
     * If one of the values is {@code null}, the other will be returned.
     * If both values are {@code null}, then {@code null} will be returned.
     *
     * @param operator the operator to be applied.
     * @param a an operand.
     * @param b an operand.
     * @return the result of the operation.
     */
    public static BigDecimal apply( BinaryOperator< BigDecimal> operator,
                                    BigDecimal a, BigDecimal b )
    {

        if( a == null ) return b;
        if( b == null ) return a;

        return Require.nonNull(
            operator, "The operator to apply is required"
        ).apply( a, b );

    }

}

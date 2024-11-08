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
import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * This utility class is intended to be used inside the method {@link #equals(Object)} of a class.
 *
 * <p>
 * The aim of this class, paired with the class {@link Hashcode}, is to avoid the boilerplate code
 * needed to implement the {@link #equals(Object)} method and to provide an implementation
 * that is consistent with the related {@link #hashCode()} method.
 *
 * <p>
 * Even if most of the IDEs provide tools to generate implementations of {@link #hashCode()}
 * and {@link #equals(Object)}, the generated code is ugly and hard to understand.
 * By using this utility class the resulting {@link #equals(Object)} method will be small,
 * clean and easy to read.
 *
 * <p>
 * You may have seen a lot of times implementation of the method {@link #equals(Object)}
 * in this form:
 * 
 * <pre>
 *  public boolean equals( Object obj )
 *  {
 *
 *      if( obj == this )
 *          return true;
 *      if( getClass() != obj.getClass() )
 *          return false;
 *
 *      ThisClass other = (ThisClass) obj;
 *      if( field == null )
 *      {
 *          if( other.field != null )
 *              return false;
 *      } else if( ! field.equals(other.field) )
 *          return false;
 *
 *      if( array == null )
 *          return other.array == null;
 *
 *      else if( other.array == null )
 *          return false;
 *
 *      if( array.length != other.array.length )
 *          return false;
 *
 *      for( int i=0; i&lt;array.length; i++ )
 *      {
 *          Object o1 = array[i];
 *          Object o2 = other.array[i];
 *          boolean e = o1==null ? o2==null : o1.equals( o2 );
 *          if( ! e )
 *              return false;
 *      }
 *
 *      return true;
 *
 *  }
 * </pre>
 * It is quite hard to understand and definitely ugly!
 *
 * <p>
 * With this utility you can get the same result with a single instruction:
 * <pre>
 *  public boolean equals( Object other )
 *  {
 *
 *      return Equals.ifSameClass(
 *          this, other,
 *          o -&gt; o.field,
 *          o -&gt; o.array
 *      );
 *
 *  }
 * </pre>
 * Much better!
 *
 * <p>
 * <b>Important</b>: This class can be used also to compare two objects as follows:
 * <pre>
 *  if( Equals.ifSameClass(anObject,anotherObject) )
 *    // Do something...
 * </pre>
 * but the result may not be what expected.
 * Since this class is intended to be used inside {@link #equals(Object)} none of its methods
 * invokes {@link #equals(Object)} on the provided objects, otherwise it will generate an
 * invocation loop and a {@link StackOverflowError} will be thrown.
 * <p>
 * So the method {@code Equals.ifSameClass( anObject, anotherObject )} will return {@code true}
 * if {@code anObject} and {@code anotherObject} are both {@code null} or if are both not
 * {@code null} and belong to the same class, but invoking {@code anObject.equals(anotherObject)}
 * on the same instances could return {@code false}.
 *
 * <p>
 * There are cases where you may want to consider two objects to be equal if both
 * implement the same interface, and they behave in the same way in relation to
 * the interface specifications.
 *
 * <p>
 * A well known example of this case are the java native implementations of
 * the {@link java.util.Collection} interface. If you take a look into
 * {@link java.util.AbstractList#equals(Object)} you will see something like this:
 * <pre>
 *  public boolean equals( Object o )
 *  {
 *
 *      if( o == this )
 *          return true;
 *      if( ! (o instanceof List) )
 *          return false;
 *
 *      ListIterator&lt;E&gt; e1 = listIterator();
 *      ListIterator&lt;?&gt; e2 = ((List&lt;?&gt;) o).listIterator();
 *      while( e1.hasNext() &amp;&amp; e2.hasNext() )
 *      {
 *          E o1 = e1.next();
 *          Object o2 = e2.next();
 *          if( !(o1==null ? o2==null : o1.equals(o2)) )
 *              return false;
 *      }
 *      return !(e1.hasNext() || e2.hasNext());
 *
 *  }
 * </pre>
 * As you can see, an object is considered to be equal to
 * the current list if it implements {@link java.util.List}
 * and if the elements in the same position are equal as well.
 *
 * <p>
 * The same result can be achieved using this utility like this:
 * <pre>
 *  public boolean equals( Object other )
 *  {
 *
 *      return Equals.ifInstancesOf( List.class, this, other, ( l1, l2 ) -&gt;
 *      {
 *          ListIterator&lt;?&gt; e1 = l1.listIterator();
 *          ListIterator&lt;?&gt; e2 = l2.listIterator();
 *          while( e1.hasNext() &amp;&amp; e2.hasNext() )
 *          {
 *              Object o1 = e1.next();
 *              Object o2 = e2.next();
 *              if( !(o1==null ? o2==null : o1.equals(o2)) )
 *                  return false;
 *          }
 *          return !(e1.hasNext() || e2.hasNext());
 *      }
 *
 *  }
 * </pre>
 * 
 * @author Massimo Coluzzi
 * @since 2.0.0
 */
public class Equals
{

	/**
	 * This class is intended to be static
	 * so there is no public constructor.
	 */
	Equals() {}


	/* **************** */
	/*  PUBLIC METHODS  */
	/* **************** */


	/**
	 * This method is intended to be used inside a {@link Object#equals(Object)}
	 * method, to check if the object to compare is not {@code null} and if it has
	 * the same class as {@code this} object.
	 * <p>
	 * If the previous condition holds then a field by field check will be performed
	 * using the provided {@link Function}s to extract the values to compare.
	 * <p>
	 * If one of the fields to compare is an array, the deep equality of the {@link Arrays}
	 * facility will be used. It performs a deep equality check to be consistent with the
	 * java native implementations of {@link Collection#equals(Object)}.
	 *
	 * @param thisObj  the object owner of the method {@link Object#equals(Object)}.
	 * @param otherObj the object to compare.
	 * @param fields   a list of functions that get the field to compare from the given object.
	 * @param <T>      the returned type.
	 * @return the {@code otherObj} casted to the right class if possible, {@code null} otherwise.
	 */
	@SafeVarargs
	@SuppressWarnings("unchecked")
	public static <T> boolean ifSameClass( T thisObj, Object otherObj,  Function<T,Object>... fields )
	{

		/* If the two objects are the same instance we can skip all other checks. */
		if( thisObj == otherObj ) return true;

		/* Checks if (thisObj == null && otherObj != null) || (thisObj != null && otherObj == null). */
		if( thisObj == null ^ otherObj == null ) return false;

		/*
		 * If we reach this point then the given
		 * objects are not the same instance and
		 * both are not null.
		 */
		if( thisObj.getClass() != otherObj.getClass() )
			return false;

		/* We can perform the field by field check. */		
		return equalsFields( thisObj, (T) otherObj, fields );

	}


	/**
	 * This method is intended to be used inside a {@link Object#equals(Object)}
	 * method, to check if the object to compare is not {@code null} and if both
	 * objects are instances of the given type.
	 * <p>
	 * If the previous condition holds then a custom check will be performed
	 * using the provided {@link BiFunction}.
	 * <p>
	 * This method is useful in those cases where two objects implement the same
	 * interface and are considered equals if behave in the same way in relation
	 * to the interface specifications.
	 * <p>
	 * A well known example of this case are the java native implementations of
	 * the {@link java.util.Collection} interface. If you take a look into
	 * {@link java.util.AbstractList#equals(Object)} you will see that another
	 * object is considered to be equal if implements {@link java.util.List}
	 * and elements in the same position are equal. A similar implementation is
	 * can be found in {@link java.util.AbstractSet#equals(Object)}.
	 *
	 * @param type     class representing the type to be implemented by both objects.
	 * @param thisObj  the object owner of the method {@link Object#equals(Object)}.
	 * @param otherObj the object to compare.
	 * @param check    the actual implementation of the logic needed to check the equality.
     * @param <T>      the type to be implemented by both objects.
	 * @return the {@code otherObj} casted to the right class if possible, {@code null} otherwise.
	 */
	@SuppressWarnings("unchecked")
	public static <T> boolean ifInstancesOf( Class<T> type,
										 	 Object thisObj, Object otherObj,
											 BiFunction<T,T,Boolean> check )
	{

		/* If the two objects are the both null we can skip all other checks. */
		if( thisObj == otherObj && otherObj == null ) return true;

		/* Checks if (thisObj == null && otherObj != null) || (thisObj != null && otherObj == null). */
		if( thisObj == null ^ otherObj == null ) return false;

		/* Checks if are both instances of the requested type. */
		if( ! type.isInstance(thisObj) || ! type.isInstance(otherObj) )
			return false;

		/*
		 * At this point we can check if the two objects are the same instance.
		 * We couldn't do it before because a call to Equals.ifInstanceOf(List.class,set,set,null)
		 * would return true even if the requested type is not implemented by the given instance.
		 */
		if( thisObj == otherObj ) return true;

		/* We can perform the field by field check. */
		return check != null
			? check.apply( (T) thisObj, (T) otherObj )
			: true;

	}



	/* ***************** */
	/*  PRIVATE METHODS  */
	/* ***************** */


	/**
	 * This method performs the equality check on each requested field.
	 * <p>
	 * This method assumes both of the objects to be not null.
	 *
	 * @param thisObject  the object owner of the method {@link Object#equals(Object)}.
	 * @param otherObject the object to be compared.
	 * @param fields      a list of functions that get the field to compare from the given object.
	 * @param <T>         the type of the objects to check.
	 * @return {@code true} if the given fields are two by two equal.
	 */
	@SafeVarargs
	private static <T> boolean equalsFields( T thisObject, T otherObject,
											Function<T,Object>... fields )
	{

		for( Function<T,Object> field : fields )
		{

			/* We use the provided function to retrieve the same field from both objects. */
			final Object thisField  = field.apply( thisObject  );
			final Object otherField = field.apply( otherObject );

			if( ! equalsFields( thisField, otherField) )
				return false;

		}

		return true;

	}


    /**
     * This method checks if the two given objects are equals.
     * <p>
     * If the given objects are  arrays this method performs
	 * an index by index check for each pair of elements
	 * in the arrays.
     * <p>
     * <b>Note</b>
     * This method checks equality by invoking
     * the {@link #equals(Object)} method on the
     * given objects, so it can't be used to check
     * {@code thisObject} and the {@code otherObject}
     * itself.
     *
     * @param thisField  field of  {@code thisObject}.
     * @param otherField related field of the {@code otherObject}.
     * @param <Field>    the type of the fields to check.
     * @return {@code true} if the two fields are equal.
     */
    private static <Field> boolean equalsFields( Field thisField, Field otherField )
    {

    	/* This check works also for thisField == otherField == null. */
    	if( thisField == otherField ) return true;

    	/* Checks if (thisField == null && otherField != null) || (thisField != null && otherField == null). */
    	if( thisField == null ^ otherField == null ) return false;

    	/*
    	 * If we are at this point then the given
    	 * objects are not the same instance and
    	 * both not null.
    	 */
    	return thisField.getClass().isArray()
    		? equalsArrays( thisField, otherField )
    		: thisField.equals( otherField );

    }


	/**
	 * Tells if the two arrays are equal
	 * and iterates the process for each
	 * couple of values in the arrays.
	 *
	 * @param fst first array to deep check.
	 * @param snd second array to deep check.
	 * @return {@code true} if the two arrays are deeply equal.
	 */
	private static boolean equalsArrays( Object fst, Object snd )
	{

		/* If we reach this point means that both objects are arrays. */
		if ( fst instanceof Object[] )
		{

			final Object[] fstArray = (Object[]) fst;
			final Object[] sndArray = (Object[]) snd;

			if( fstArray.length != sndArray.length ) return false;

			/* If are array of objects we perform an index by index check. */
			for( int i = 0; i < fstArray.length; ++i )
				if( ! equalsFields( fstArray[i], sndArray[i]) )
					return false;

			return true;

		}

		else if ( fst instanceof int[] )
			return Arrays.equals( (int[])fst, (int[])snd );

		else if ( fst instanceof byte[] )
			return Arrays.equals( (byte[])fst, (byte[])snd );

		else if ( fst instanceof long[] )
			return Arrays.equals( (long[])fst, (long[])snd );

		else if ( fst instanceof short[] )
			return Arrays.equals( (short[])fst, (short[])snd );

		else if ( fst instanceof boolean[] )
			return Arrays.equals( (boolean[])fst, (boolean[])snd );

		else if ( fst instanceof float[] )
			return Arrays.equals( (float[])fst, (float[])snd );

		else if ( fst instanceof double[] )
			return Arrays.equals( (double[])fst, (double[])snd );

		else if ( fst instanceof char[] )
			return Arrays.equals( (char[])fst, (char[])snd );

		return false;

	}

}

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

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;


/**
 * This utility class is intended to be used inside the method {@link #toString()} of a class.
 *
 * <p>
 * The aim of this class, is to avoid the boilerplate code needed to implement
 * the {@link #toString()} method.
 *
 * <p>
 * Even if most of the IDEs provide tools to generate implementations of {@link #toString()},
 * the generated code is ugly and hard to understand.
 * By using this utility class the resulting {@link #toString()} method will be small,
 * clean and easy to read.
 *
 * <p>
 * You may have seen a lot of times implementation of the method {@link #toString()}
 * in this form:
 * <pre>
 * public String toString()
 * {
 *
 *   return "SomeType{" +
 *     id=" + id +
 *     ", string='" + string + '\'' +
 *     ", intArray=" + Arrays.toString(intArray) +
 *     ", stringMatrix=" + Arrays.toString(stringMatrix) +
 *     '}';
 *
 * }
 * </pre>
 *
 * With this utility you can get the same result with some more concise
 * and elegant code:
 * <pre>
 *  public String toString()
 *  {
 *    return ToString.of( this )
 *      .print( "id", id )
 *      .print( "string", string )
 *      .print( "intArray", intArray )
 *      .print( "stringMatrix", stringMatrix )
 *      .likeIntellij();
 *  }
 * </pre>
 *
 * <p>
 * In addition, to change your objects layout the only one thing
 * you need to do is to change the last method of the functional
 * invocation chain.
 *
 * @author Massimo Coluzzi
 * @since 2.0.0
 */
public class ToString
{


	/**
	 * This class is intended to be static
	 * so there is no public constructor.
	 */
	ToString() {}


	/* **************** */
	/*  PUBLIC METHODS  */
	/* **************** */


	/**
     * Returns an instance of {@link ToString.Configurator}
	 * in order to instruct the {@link ToString} facility
	 * on how to build the text representation of the
	 * given object.
	 *
     * @param object an object to be converted to {@link String}.
     * @return an instance of the {@link ToString.Configurator}.
     */
    public static Configurator of( Object object )
    {

    	return new Config( object );

    }


    /* *************** */
    /*  INNER CLASSES  */
	/* *************** */


	/**
	 * Represents a printer able to build the {@link String}
	 * representation of an object using the given
	 * {@link ToString.Configuration}.
	 *
	 * @author Massimo Coluzzi
	 * @since 2.0.0
	 */
	public interface Printer
	{

		/**
		 * Applies the given {@link ToString.Configuration}
		 * to create the {@link String} representation of
		 * an object.
		 *
		 * @param configuration the {@link ToString.Configuration} to be applied.
		 * @return the {@link String} representation an object.
		 */
		String apply( Configuration configuration );

	}


	/**
	 * Contains the information needed by the {@link ToString.Printer}
	 * to know how to build the {@link String} representation of an object.
	 *
	 * @author Massimo Coluzzi
	 * @since 2.0.0
	 */
	public interface Configuration
	{

		/**
		 * Returns the object of which to build
		 * the {@link String} representation.
		 *
		 * @return the target object to print.
		 */
		Object target();

		/**
		 * List of fields to be printed.
		 *
		 * @return the list of fields to be printed.
		 */
		Iterable<Field> fields();

		/**
		 * Tells to the {@link ToString.Printer}
		 * to use the fully qualified class name.
		 *
		 * @return {@code true} if the class name
		 * should be fully qualified.
		 */
		boolean fullClassPath();
		
		/**
		 * Custom text to use instead of the target class name.
		 * This can be useful when the target is an instance of
		 * an anonymous class (like a lambda expression).
		 *
		 * @return custom text to use instead of the target class name.
		 */
		String customClassName();


		/* *************** */
		/*  INNER CLASSES  */
		/* *************** */


		/**
		 * Represents a field to be displayed
		 * in the resulting {@link String}.
		 *
		 * <p>
		 * Both the {@link Field#name} and
		 * the {@link Field#value}
		 * can be {@code null}.
		 *
		 * @author Massimo Coluzzi
		 * @since 2.0.0
		 */
		class Field
		{

			/** The name of the field. */
			public final String name;

			/** The value of the field. */
			public final Object value;


			/**
			 * Constructor with parameters.
			 *
			 * @param name  name of the field.
			 * @param value value of the field.
			 */
			public Field( String name, Object value )
			{

				super();

				this.name = name;
				this.value = value;

			}

		}

	}

	/**
	 * Implementation of the {@code Pattern Builder} with the
	 * purpose to properly create a {@link ToString.Configuration}.
	 *
	 * @author Massimo Coluzzi
	 * @since 2.0.0
	 */
	public interface Configurator
	{

		/**
		 * Adds each provided value as unnamed
		 * {@link Configuration.Field Fields} in the
		 * resulting {@link Configuration}.
		 *
		 * @param values values to add to the configuration.
		 * @return this {@link Configurator}.
		 */
		Configurator print( Object... values );

		/**
		 * Adds the given {@code <name,value>} pair
		 * as a named {@link Configuration.Field Field}
		 * in the resulting {@link Configuration}.
		 *
		 * @param name  the name of the field.
		 * @param value the value of the field.
		 * @return this {@link Configurator}.
		 */
		Configurator print( String name, Object value );

		/**
		 * Adds each element in the stream as unnamed
		 * {@link Configuration.Field Fields} in the
		 * resulting {@link Configuration}.
		 *
		 * @param stream values to add to the configuration.
		 * @return this {@link Configurator}.
		 */
		Configurator print( Stream<?> stream );

		/**
		 * Adds a {@code <name,value>} pair as a named
		 * {@link Configuration.Field Field} for each
		 * element in the given stream by using the
		 * provided functions to extract the values.
		 * <p>
		 * If one of the functions is {@code null}
		 * the related element will not be extracted.
		 * If both are {@code null} nothing will be
		 * added.
		 * 
		 * @param <T>         type of streamed objects.
		 * @param stream      values to add to the configuration.
		 * @param nameMapper  the {@link Function} to extract the name.
		 * @param valueMapper the {@link Function} to extract the value.
		 * @return this {@link Configurator}.
		 */
		<T> Configurator print( Stream<T> stream,
								Function<T,String> nameMapper,
								Function<T,?> valueMapper );



		/**
		 * Adds each provided non {@code null} value as unnamed
		 * {@link Configuration.Field Fields} in the resulting
		 * {@link Configuration}.
		 * <p>
		 * The difference between this method and the method
		 * {@link #print(Object...)} is that this version
		 * adds only the values that are not {@code null}.
		 *
		 * @param values values to add to the configuration.
		 * @return this {@link Configurator}.
		 */
		Configurator printIfNotNull( Object... values );

		/**
		 * Adds the given {@code <name,value>} pair
		 * as a named {@link Configuration.Field Field}
		 * in the resulting {@link Configuration} if
		 * the value is not null.
		 * <p>
		 * The difference between this method and the method
		 * {@link #print(String,Object)} is that this version
		 * adds only the pairs whose values are not {@code null}.
		 *
		 * @param name  the name of the field.
		 * @param value the value of the field.
		 * @return this {@link Configurator}.
		 */
		Configurator printIfNotNull( String name, Object value );

		/**
		 * Adds each non {@code null} element in the stream
		 * as unnamed {@link Configuration.Field Fields} in
		 * the resulting {@link Configuration}.
		 * <p>
		 * The difference between this method and the method
		 * {@link #print(Stream)} is that this version
		 * adds only the elements that are not {@code null}.
		 *
		 * @param stream values to add to the configuration.
		 * @return this {@link Configurator}.
		 */
		Configurator printIfNotNull( Stream<?> stream );

		/**
		 * Adds a {@code <name,value>} pair as a named
		 * {@link Configuration.Field Field} for each
		 * non {@code null} element in the given stream
		 * by using the provided functions to extract
		 * the values.
		 * <p>
		 * If one of the functions is {@code null}
		 * the related element will not be extracted.
		 * If both are {@code null} nothing will be
		 * added.
		 * <p>
		 * The difference between this method and the method
		 * {@link #print(Stream,Function,Function)} is that
		 * this version adds only the elements that are not
		 * {@code null}.
		 * 
		 * @param <T>         type of streamed objects.
		 * @param stream      values to add to the configuration.
		 * @param nameMapper  the {@link Function} to extract the name.
		 * @param valueMapper the {@link Function} to extract the value.
		 * @return this {@link Configurator}.
		 */
		<T> Configurator printIfNotNull( Stream<T> stream,
								Function<T,String> nameMapper,
								Function<T,?> valueMapper );


		/**
		 * Tells the {@link ToString} class to do not print
		 * the class name in any form.
		 *
		 * @return this {@link Configurator}.
		 */
		Configurator withNoClassName();

		/**
		 * Tells the {@link ToString} class to print
		 * the full qualified class name.
		 *
		 * @return this {@link Configurator}.
		 */
		Configurator withFullClassName();

		/**
		 * Tells the {@link ToString} class to print
		 * the given text instead of the class name.
		 * <p>
		 * If you invoke this method with {@code null}
		 * than {@code "null"} will be printed instead
		 * of the class name.
		 *
		 * @param value value of the custom text to set.
		 * @return this {@link Configurator}.
		 */
		Configurator withCustomClassName( String value );

		/**
		 * Creates the {@link Configuration} and invokes
		 * the provided {@link Printer} in order to
		 * build the requested {@link String} representation.
		 * <p>
		 * This method provides the widest range of customizations.
		 * You can implement is own favorite style to display objects.
		 *
		 * @param printer the printer to invoke.
		 * @return the requested {@link String} representation.
		 */
		String like( Printer printer );

		/**
		 * Creates an output of the method {@link #toString()}
		 * like the one generated by the {@code Eclipse IDE}.
		 *
		 * @return an output like the one generated by the {@code Eclipse IDE}.
		 */
		String likeEclipse();

		/**
		 * Creates an output of the method {@link #toString()}
		 * like the one generated by the {@code IntelliJ IDE}.
		 *
		 * @return an output like the one generated by the {@code IntelliJ IDE}.
		 */
		String likeIntellij();

		/**
		 * Creates an output of the method {@link #toString()}
		 * in the form of a function, where the function name is the
		 * target class name and the given fields are listed as the
		 * function arguments.
		 * <p>
		 * This method is intended to be used to implement the
		 * {@link #toString()} method of functional classes.
		 * <p>
		 * For example if you are developing a class to apply some
		 * operations dynamically, you may want the {@link #toString()}
		 * of your class to be something like: {@code Apply(MyOperation)}
		 * or {@code Apply(op:MyOperation)}
		 *
		 * @return an output similar to a function.
		 */
		String likeFunction();

		/**
		 * Creates an output of the method {@link #toString()}
		 * in the form of a tuple, optionally preceded by the
		 * class name.
		 * <p>
		 * This method is intended to be used to implement the
		 * {@link #toString()} method of classes that represents
		 * relations or ordered collections of objects.
		 * <p>
		 * For example if you develop a class that represents
		 * a pair of objects, you may want the {@link #toString()}
		 * of you class to be something like: {@code <key,value>}.
		 *
		 * @return an output similar to a tuple.
		 */
		String likeTuple();

		/**
		 * Creates an output of the method {@link #toString()}
		 * using the given prefix and suffix and using the given
		 * separators.
		 * <p>
		 * The resulting string will have the following components:
		 * <ol>
		 *  <li>Optionally: the name of the class or a custom text.</li>
		 *  <li>The given prefix.</li>
		 *  <li>
		 *   A list of fields separated by the given field separators
		 *   where each field can be:
		 *   <ul>
		 *    <li>a value or</li>
		 *    <li>a name-value pair separated by the given name-value separator</li>
		 *   </ul>
		 *  </li>
		 *  <li>The given suffix.</li>
		 * </ol>
		 * All parameters are optional and can be {@code null} or empty.
		 * <p>
		 * This method provides a quite wide range of customizations.
		 * If you need to customize your {@link #toString()} output even more
		 * you can use the method {@link #like(ToString.Printer)}.
		 *
		 * @param prefix             the prefix to use before printing values.
		 * @param nameValueSeparator the text to use to separate name and value of a field.
		 * @param fieldSeparator     the text to use to separate fields.
		 * @param suffix             the suffix to use after printing values.
		 * @return an output similar to a tuple.
		 */
		String using( String prefix, String nameValueSeparator, String fieldSeparator, String suffix );

		/**
		 * This is a convenience method. Invokng this method is the same as invoking
		 * {@link #using(String, String, String, String)} with an empty or {@code null}
		 * name-value separator.
		 *
		 * @param prefix             the prefix to use before printing values.
		 * @param fieldSeparator     the text to use to separate fields.
		 * @param suffix             the suffix to use after printing values.
		 * @return an output similar to a tuple.
		 */
		String using( String prefix, String fieldSeparator, String suffix );

	}


	/**
	 * Internal implementation of both the {@link Configuration}
	 * and the {@link Configurator}.
	 *
	 * @author Massimo Coluzzi
	 * @since 2.0.0
	 */
	private static class Config implements Configuration, Configurator
	{

		/**
		 * The target object to be returned
		 * by {@link Configuration#target()}
		 */
		private final Object target;

		/**
		 * List of fields to be returned
		 * by {@link Configuration#fields()}
		 */
		private final List<Field> fields;

		/** Flag to be returned by {@link Configuration#fullClassPath()} */
		public boolean fullClassPath;

		/** Text to be returned by {@link Configuration#customClassName()} */
		public String customClassName;
		

		/**
		 * Constructor fith parameters.
		 *
		 * @param target the target object.
		 */
		private Config( Object target )
		{

			super();

			/* The target object can be null. */
			this.target = target;
			this.fields = new LinkedList<>();
			this.fullClassPath = false;
			this.customClassName = null;

		}


		/* ******************* */
		/*  INTERFACE METHODS  */
		/* ******************* */


		/* OF CONFIGURATION */

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Object target()
		{

			return target;

		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Iterable<Field> fields()
		{

			return fields;

		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean fullClassPath()
		{

			return fullClassPath;

		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public String customClassName()
		{
			
			return customClassName;
			
		}


		/* OF CONFIGURATOR  */


		/**
		 * {@inheritDoc}
		 */
		@Override
		public Configurator print( Object... values )
		{

			if( IsNot.empty(values) )
				for( Object value : values )
					print( null, value );

			return this;

		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Configurator print( String key, Object value )
		{

			fields.add( new Field(key, value) );
			return this;

		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Configurator print( Stream<?> stream )
		{

			if( stream != null )
				stream.forEach( this::print );
			return this;

		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public <T> Configurator print( Stream<T> stream,
									   Function<T,String> nameMapper,
									   Function<T,?> valueMapper )
		{

			/* If the stream is null there is nothing to do. */
			if( stream == null )
				return this;

			/*
			 * We replace the null mappers with functions
			 * that return null.
			 */
			final Function<T,String> name = nameMapper != null ? nameMapper : t -> null;
			final Function<T,?> value = valueMapper != null ? valueMapper : t -> null;

			/*
			 * For each element in the stream we extract
			 * the name and value using the given functions
			 * and print them.
			 */
			stream
				.filter( IsNot::NULL )
				.forEach( element -> 
					print(
						name.apply( element ),
						value.apply( element )
					)
				);

			return this;

		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Configurator printIfNotNull( Object... values )
		{

			if( IsNot.empty(values) )
				for( Object value : values )
					printIfNotNull( null, value );

			return this;

		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Configurator printIfNotNull( String key, Object value )
		{

			if( value != null )
				fields.add( new Field(key, value) );

			return this;

		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Configurator printIfNotNull( Stream<?> stream )
		{

			if( stream != null )
				stream.forEach( this::printIfNotNull );

			return this;

		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public <T> Configurator printIfNotNull( Stream<T> stream,
									   			Function<T,String> nameMapper,
									   			Function<T,?> valueMapper )
		{

			/* If the stream is null there is nothing to do. */
			if( stream == null )
				return this;

			/*
			 * We replace the null mappers with functions
			 * that return null.
			 */
			final Function<T,String> name = nameMapper != null ? nameMapper : t -> null;
			final Function<T,?> value = valueMapper != null ? valueMapper : t -> null;

			/*
			 * For each element in the stream we extract
			 * the name and value using the given functions
			 * and print them.
			 */
			stream
				.filter( IsNot::NULL )
				.forEach( element -> 
					printIfNotNull(
						name.apply( element ),
						value.apply( element )
					)
				);

			return this;

		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Configurator withNoClassName()
		{

			this.customClassName = "";
			return this;

		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Configurator withFullClassName()
		{

			this.fullClassPath = true;
			return this;

		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Configurator withCustomClassName( String value )
		{
			
			this.customClassName = String.valueOf( value );
			return this;
			
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String like( Printer printer )
		{

			return printer.apply( this );

		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String likeEclipse()
		{

			return ToStringOutput.using( this, "[", "=", ", ", "]" );

		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String likeIntellij()
		{

			return ToStringOutput.using( this, "{", "=", ", ", "}" );

		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String likeFunction()
		{

			return ToStringOutput.using( this, "(", ":", ", ", ")" );

		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String likeTuple()
		{

			return ToStringOutput.using( this, "<", ":", ", ", ">" );

		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String using( String prefix, String nameValueSeparator, String fieldSeparator, String suffix )
		{

			return ToStringOutput.using( this, prefix, nameValueSeparator, fieldSeparator, suffix );

		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String using( String prefix, String fieldSeparator, String suffix )
		{

			return ToStringOutput.using( this, prefix, null, fieldSeparator, suffix );

		}

	}

}

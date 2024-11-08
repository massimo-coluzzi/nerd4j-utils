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


/**
 * A class implements this interface to tell that may has a content
 * or may be empty.
 *
 * <p>
 * This interface is intended to be used with the {@link Is}, {@link IsNot}
 * and {@link Require} utilities to perform emptiness checks in the form:
 * <pre>
 *     if( Is.empty(emptilyObject) )
 *         // Do something
 *
 *     if( IsNot.empty(emptilyObject) )
 *         // Do something
 *
 *     or
 *
 *     Require.nonEmpty( emptilyObject, "The given object cannot be empty" );
 *  </pre>
 *
 *  @author Massimo Coluzzi
 *  @since 2.0.0
 */
public interface Emptily
{

    /**
     * Returns {@code true} if this object is empty.
     *
     * @return {@code true} if this object is empty.
     */
    boolean isEmpty();

}

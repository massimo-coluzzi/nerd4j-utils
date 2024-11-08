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
package org.nerd4j.utils.test;

/**
 * All interfaces that extends {@link Contract} are intended
 * to test interface contracts.
 *
 * <p>
 * For example if we have an interface {@code I}, we can write
 * a contract for such interface and when a class {@code C}
 * implements the interface {@code I} the test class for
 * {@code C} should implement the contract to test that
 * {@code C} implements {@code I} in the expected way.
 *
 * @param <T> the type to test.
 * @author Massimo Coluzzi
 */
public interface Contract<T>
{

    /**
     * An instance of the class that should respect the contract.
     *
     * @return instance of the class to test.
     */
    T sampleValue();

}

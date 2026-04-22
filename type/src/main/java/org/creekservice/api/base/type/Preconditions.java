/*
 * Copyright 2022-2025 Creek Contributors (https://github.com/creek-service)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.creekservice.api.base.type;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/** Factory methods for asserting preconditions */
public final class Preconditions {

    private Preconditions() {}

    /**
     * Test that an array is not empty.
     *
     * @param value the collection to test.
     * @param name the name to use in the exception, if the check fails.
     * @param <T> the value type
     * @return {@code value} if the check passes.
     */
    public static <T> T[] requireNonEmpty(final T[] value, final String name) {
        requireNonNull(value, name);
        if (value.length == 0) {
            throw new IllegalArgumentException(name + " can not be empty");
        }
        return value;
    }

    /**
     * Test that a collection is not empty.
     *
     * @param value the collection to test.
     * @param name the name to use in the exception, if the check fails.
     * @param <T> the value type
     * @return {@code value} if the check passes.
     */
    public static <T extends Collection<?>> T requireNonEmpty(final T value, final String name) {
        requireNonNull(value, name);
        if (value.isEmpty()) {
            throw new IllegalArgumentException(name + " can not be empty");
        }
        return value;
    }

    /**
     * Test that a map is not empty.
     *
     * @param value the map to test.
     * @param name the name to use in the exception, if the check fails.
     * @param <T> the value type
     * @return {@code value} if the check passes.
     */
    public static <T extends Map<?, ?>> T requireNonEmpty(final T value, final String name) {
        requireNonNull(value, name);
        if (value.isEmpty()) {
            throw new IllegalArgumentException(name + " can not be empty");
        }
        return value;
    }

    /**
     * Test that a string is not empty, i.e. has non-zero length.
     *
     * @param value the string to test.
     * @param name the name to use in the exception, if the check fails.
     * @return {@code value} if the check passes.
     */
    public static String requireNonEmpty(final String value, final String name) {
        requireNonNull(value, name);
        if (value.isEmpty()) {
            throw new IllegalArgumentException(name + " can not be empty");
        }
        return value;
    }

    /**
     * Test that string is not blank, i.e. has a least one non-whitespace character.
     *
     * @param value the string to test.
     * @param name the name to use in the exception, if the check fails.
     * @return {@code value} if the check passes.
     */
    public static String requireNonBlank(final String value, final String name) {
        requireNonNull(value, name);
        if (value.isBlank()) {
            throw new IllegalArgumentException(name + " can not be blank");
        }
        return value;
    }

    /**
     * Test two values are equal.
     *
     * @param a the first value to test
     * @param b the second value to test
     * @param msg the message to use in the exception if the check fails.
     * @param <T> the value type
     */
    public static <T> void requireEqual(final T a, final T b, final String msg) {
        if (!Objects.equals(a, b)) {
            throw new IllegalArgumentException(msg + " " + a + " != " + b);
        }
    }

    /**
     * Test one value is less than another
     *
     * @param value the value to test
     * @param upperBound the upper bound of valid values for {@code value}
     * @param msg the message prefix
     * @param <T> the value type
     */
    public static <T extends Comparable<T>> void requireLessThan(
            final T value, final T upperBound, final String msg) {
        if (value.compareTo(upperBound) >= 0) {
            throw new IllegalArgumentException(
                    msg + " must be less than " + upperBound + ", but was " + value);
        }
    }

    /**
     * Test one value is less, or equal to, than another
     *
     * @param value the value to test
     * @param upperBound the upper bound of valid values for {@code value}
     * @param msg the message prefix
     * @param <T> the value type
     */
    public static <T extends Comparable<T>> void requireLessThanOrEqualTo(
            final T value, final T upperBound, final String msg) {
        if (value.compareTo(upperBound) > 0) {
            throw new IllegalArgumentException(
                    msg + " must be less than or equal to " + upperBound + ", but was " + value);
        }
    }

    /**
     * Test one value is greater than another
     *
     * @param value the value to test
     * @param lowerBound the lower bound of valid values for {@code value}
     * @param msg the message prefix
     * @param <T> the value type
     */
    public static <T extends Comparable<T>> void requireGreaterThan(
            final T value, final T lowerBound, final String msg) {
        if (value.compareTo(lowerBound) <= 0) {
            throw new IllegalArgumentException(
                    msg + " must be greater than " + lowerBound + ", but was " + value);
        }
    }

    /**
     * Test one value is greater than, or equal to, another
     *
     * @param value the value to test
     * @param lowerBound the lower bound of valid values for {@code value}
     * @param msg the message prefix
     * @param <T> the value type
     */
    public static <T extends Comparable<T>> void requireGreaterThanOrEqualTo(
            final T value, final T lowerBound, final String msg) {
        if (value.compareTo(lowerBound) < 0) {
            throw new IllegalArgumentException(
                    msg + " must be greater than or equal to " + lowerBound + ", but was " + value);
        }
    }

    /**
     * Generic requirement test.
     *
     * @param test boolean indicating if condition was met
     * @param msg the message to add to the exception if the condition is not met.
     */
    public static void require(final boolean test, final String msg) {
        if (!test) {
            throw new IllegalArgumentException(msg);
        }
    }
}

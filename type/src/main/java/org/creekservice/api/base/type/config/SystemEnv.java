/*
 * Copyright 2022 Creek Contributors (https://github.com/creek-service)
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

package org.creekservice.api.base.type.config;


import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.function.Supplier;

/** Utility class to get Creek config from environment variables */
public final class SystemEnv {

    private SystemEnv() {}

    /**
     * Get an integer environment variable or a default if the variable is not set.
     *
     * @param name the name of the variable to read
     * @param defaultValue the default value to return if the variable is not set.
     * @return the value of the variable, or {@code defaultValue} if not set.
     */
    public static Integer readInt(final String name, final Integer defaultValue) {
        final String value = System.getenv(name);
        if (value == null) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(value);
        } catch (final Exception e) {
            throw new EnvException("Failed to parse integer", name, value, e);
        }
    }

    /**
     * Get a long environment variable or a default if the variable is not set.
     *
     * @param name the name of the variable to read
     * @param defaultValue the default value to return if the variable is not set.
     * @return the value of the variable, or {@code defaultValue} if not set.
     */
    public static int readInt(final String name, final int defaultValue) {
        return readInt(name, (Integer) defaultValue);
    }

    /**
     * Get a long environment variable or a default if the variable is not set.
     *
     * @param name the name of the variable to read
     * @param defaultValue the default value to return if the variable is not set.
     * @return the value of the variable, or {@code defaultValue} if not set.
     */
    public static Long readLong(final String name, final Long defaultValue) {
        final String value = System.getenv(name);
        if (value == null) {
            return defaultValue;
        }

        try {
            return Long.parseLong(value);
        } catch (final Exception e) {
            throw new EnvException("Failed to parse long", name, value, e);
        }
    }

    /**
     * Get a long environment variable or a default if the variable is not set.
     *
     * @param name the name of the variable to read
     * @param defaultValue the default value to return if the variable is not set.
     * @return the value of the variable, or {@code defaultValue} if not set.
     */
    public static long readLong(final String name, final long defaultValue) {
        return readLong(name, (Long) defaultValue);
    }

    /**
     * Get a string environment variable or a default if the variable is not set.
     *
     * @param name the name of the variable to read
     * @param defaultValue the default value to return if the variable is not set.
     * @return the value of the variable, or {@code defaultValue} if not set.
     */
    public static String readString(final String name, final String defaultValue) {
        final String value = System.getenv(name);
        return value == null ? defaultValue : value;
    }

    /**
     * Read a {@link Duration} from an environment variable or a default if the variable is not set.
     *
     * <p>The value of the environment variable must be parsable as a long.
     *
     * @param name the name of the variable to read. Must be parsable as a long.
     * @param defaultValue the default value to use if the variable is not set.
     * @param unit the unit of time, i.e. the unit of the number read from the variable and {@code
     *     defaultValue}.
     * @return the duration parsed from the variable, or {@code defaultValue} if not set.
     */
    public static Duration readDuration(
            final String name, final long defaultValue, final ChronoUnit unit) {
        final long value = readLong(name, defaultValue);

        try {
            return unit.getDuration().multipliedBy(value);
        } catch (final Exception e) {
            throw new EnvException("Failed to parse duration", name, value, e);
        }
    }

    /**
     * Read a {@link Duration} from an environment variable, using the standard {@link
     * Duration#parse} method, or a default if the variable is not set.
     *
     * <p>For example, a variable containing {@code "PT29M"} would be parsed as 29 minutes. See
     * {@link Duration} for more info.
     *
     * @param name the name of the variable to read. Must be parsable by {@link Duration#parse}.
     * @param defaultValue the default value to use if the variable is not set.
     * @return the duration parsed from the variable, or {@code defaultValue} if not set.
     */
    public static Duration readDuration(final String name, final Duration defaultValue) {
        final String value = System.getenv(name);
        if (value == null) {
            return defaultValue;
        }

        try {
            return Duration.parse(value);
        } catch (final Exception e) {
            throw new EnvException("Failed to parse duration", name, value, e);
        }
    }

    /**
     * Instantiate an instance of a class defined in an environment variable.
     *
     * @param name the name of the variable to read
     * @param defaultValue supplier of a default insstance to use if the variable is not set.
     * @param <T> The type to instantiate
     * @return the instance of the type set in the variable, or the result of {@code
     *     defaultValue.get()} if not set.
     */
    @SuppressWarnings("unchecked")
    public static <T> T readInstance(final String name, final Supplier<? extends T> defaultValue) {
        final String value = System.getenv(name);
        if (value == null) {
            return defaultValue.get();
        }

        try {
            return (T) Class.forName(value).getDeclaredConstructor().newInstance();
        } catch (final Exception e) {
            throw new EnvException("Failed to create instance", name, value, e);
        }
    }

    /**
     * Hidden.
     *
     * @hidden
     */
    private static final class EnvException extends IllegalArgumentException {

        EnvException(
                final String msg, final String name, final Object value, final Throwable cause) {
            super(msg + " from environment variable. name: " + name + ", value: " + value, cause);
        }
    }
}

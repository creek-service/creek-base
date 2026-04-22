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

package org.creekservice.api.base.type.config;

import java.util.Optional;
import java.util.function.Function;
import org.creekservice.api.base.annotation.VisibleForTesting;

/** Utility class to get Creek config from System properties */
public final class SystemProperties {

    private SystemProperties() {}

    /**
     * Get a string system property
     *
     * @param name the property name
     * @return the property
     */
    public static Optional<String> getString(final String name) {
        return Optional.ofNullable(property(name, null, String.class, Function.identity()));
    }

    /**
     * Get a string system property
     *
     * @param name the property name
     * @param defaultVal the default value to use if property not set
     * @return the property, or the supplied default.
     */
    public static String getString(final String name, final String defaultVal) {
        return property(name, defaultVal, String.class, Function.identity());
    }

    /**
     * Get an int system property
     *
     * @param name the property name
     * @return the property
     */
    public static Optional<Integer> getInt(final String name) {
        return Optional.ofNullable(property(name, null, int.class, Integer::parseInt));
    }

    /**
     * Get an int system property
     *
     * @param name the property name
     * @param defaultVal the default value to use if property not set
     * @return the property, or the supplied default.
     */
    public static int getInt(final String name, final int defaultVal) {
        return property(name, defaultVal, int.class, Integer::parseInt);
    }

    /**
     * Get a long system property
     *
     * @param name the property name
     * @return the property
     */
    public static Optional<Long> getLong(final String name) {
        return Optional.ofNullable(property(name, null, long.class, Long::parseLong));
    }

    /**
     * Get a long system property
     *
     * @param name the property name
     * @param defaultVal the default value to use if property not set
     * @return the property, or the supplied default.
     */
    public static Long getLong(final String name, final long defaultVal) {
        return property(name, defaultVal, long.class, Long::parseLong);
    }

    private static <T> T property(
            final String name,
            final T defaultVal,
            final Class<T> type,
            final Function<String, T> parser) {
        final String value = System.getProperty(name);
        if (value == null) {
            return defaultVal;
        }

        try {
            return parser.apply(value);
        } catch (final Exception e) {
            throw new ParseException(name, value, type, e);
        }
    }

    /**
     * @hidden
     */
    @VisibleForTesting
    static final class ParseException extends RuntimeException {
        ParseException(
                final String propName,
                final String value,
                final Class<?> type,
                final Throwable cause) {
            super(
                    "Failed to parse system property: "
                            + propName
                            + ", value: "
                            + value
                            + ", as_type: "
                            + type,
                    cause);
        }
    }
}

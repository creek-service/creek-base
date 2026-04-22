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

import static java.util.stream.Collectors.toUnmodifiableMap;

import java.util.Map;

/** Helpers for working with boxed and unboxed primitives. */
public final class Primitives {

    private static final Map<Class<?>, Class<?>> PRIMITIVES =
            Map.of(
                    short.class, Short.class,
                    int.class, Integer.class,
                    long.class, Long.class,
                    float.class, Float.class,
                    double.class, Double.class,
                    boolean.class, Boolean.class,
                    byte.class, Byte.class,
                    char.class, Character.class,
                    void.class, Void.class);

    private static final Map<Class<?>, Class<?>> BOXED =
            PRIMITIVES.entrySet().stream()
                    .collect(toUnmodifiableMap(Map.Entry::getValue, Map.Entry::getKey));

    private Primitives() {}

    /**
     * Test if type is unboxed primitive.
     *
     * @param type the type to check
     * @return {@code true} if {@code type} is a primitive type
     */
    public static boolean isUnboxedPrimitive(final Class<?> type) {
        return PRIMITIVES.containsKey(type);
    }

    /**
     * Test if type is boxed primitive.
     *
     * @param type the type to check
     * @return {@code true} if {@code type} is a wrapper type for a primitive
     */
    public static boolean isBoxedPrimitive(final Class<?> type) {
        return BOXED.containsKey(type);
    }

    /**
     * Convert type to its boxed equivalent, where one exists
     *
     * @param type the type to box
     * @return if {@code type} is a primitive: it's boxed type, otherwise {@code type}.
     */
    public static Class<?> box(final Class<?> type) {
        return PRIMITIVES.getOrDefault(type, type);
    }

    /**
     * Convert type to its unboxed equivalent, where one exists
     *
     * @param type the type to unbox
     * @return if {@code type} is a boxed primitive: it's primitive type, otherwise {@code type}.
     */
    public static Class<?> unbox(final Class<?> type) {
        return BOXED.getOrDefault(type, type);
    }
}

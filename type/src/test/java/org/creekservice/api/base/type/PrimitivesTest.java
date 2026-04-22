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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PrimitivesTest {

    @ParameterizedTest
    @MethodSource("unboxedPrimitives")
    void shouldDetectUnboxedPrimitives(final Class<?> unboxed) {
        assertThat(Primitives.isUnboxedPrimitive(unboxed), is(true));
    }

    @Test
    void shouldDetectTypesThatAreNotUnboxedPrimitives() {
        assertThat(Primitives.isUnboxedPrimitive(Map.class), is(false));
        assertThat(Primitives.isUnboxedPrimitive(Double.class), is(false));
    }

    @ParameterizedTest
    @MethodSource("boxedPrimitives")
    void shouldDetectBoxedPrimitives(final Class<?> boxed) {
        assertThat(Primitives.isBoxedPrimitive(boxed), is(true));
    }

    @Test
    void shouldDetectTypesThatAreNotBoxedPrimitives() {
        assertThat(Primitives.isBoxedPrimitive(Set.class), is(false));
        assertThat(Primitives.isBoxedPrimitive(int.class), is(false));
        assertThat(Primitives.isBoxedPrimitive(Integer[].class), is(false));
    }

    @ParameterizedTest
    @MethodSource("primitives")
    void shouldBoxUnboxed(final Class<?> unboxed, final Class<?> boxed) {
        assertThat(Primitives.box(unboxed), is(boxed));
    }

    @ParameterizedTest
    @MethodSource("boxedPrimitives")
    void shouldBoxBoxed(final Class<?> boxed) {
        assertThat(Primitives.box(boxed), is(boxed));
    }

    @Test
    void shouldBoxOthers() {
        assertThat(Primitives.box(String.class), is(String.class));
    }

    @ParameterizedTest
    @MethodSource("primitives")
    void shouldUnboxBoxed(final Class<?> unboxed, final Class<?> boxed) {
        assertThat(Primitives.unbox(boxed), is(unboxed));
    }

    @ParameterizedTest
    @MethodSource("unboxedPrimitives")
    void shouldUnboxUnboxed(final Class<?> unboxed) {
        assertThat(Primitives.unbox(unboxed), is(unboxed));
    }

    @Test
    void shouldUnboxOthers() {
        assertThat(Primitives.unbox(String.class), is(String.class));
    }

    private static Stream<Class<?>> boxedPrimitives() {
        return Stream.of(
                Short.class,
                Integer.class,
                Long.class,
                Float.class,
                Double.class,
                Boolean.class,
                Byte.class,
                Character.class,
                Void.class);
    }

    private static Stream<Class<?>> unboxedPrimitives() {
        return Stream.of(
                short.class,
                int.class,
                long.class,
                float.class,
                double.class,
                boolean.class,
                byte.class,
                char.class,
                void.class);
    }

    private static Stream<Arguments> primitives() {
        return Stream.of(
                Arguments.of(short.class, Short.class),
                Arguments.of(int.class, Integer.class),
                Arguments.of(long.class, Long.class),
                Arguments.of(float.class, Float.class),
                Arguments.of(double.class, Double.class),
                Arguments.of(boolean.class, Boolean.class),
                Arguments.of(byte.class, Byte.class),
                Arguments.of(char.class, Character.class),
                Arguments.of(void.class, Void.class));
    }
}

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

import static org.creekservice.api.base.type.Preconditions.require;
import static org.creekservice.api.base.type.Preconditions.requireEqual;
import static org.creekservice.api.base.type.Preconditions.requireGreaterThan;
import static org.creekservice.api.base.type.Preconditions.requireGreaterThanOrEqualTo;
import static org.creekservice.api.base.type.Preconditions.requireLessThan;
import static org.creekservice.api.base.type.Preconditions.requireLessThanOrEqualTo;
import static org.creekservice.api.base.type.Preconditions.requireNonBlank;
import static org.creekservice.api.base.type.Preconditions.requireNonEmpty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class PreconditionsTest {

    private static final String ARG_NAME = "some-arg";

    @Nested
    final class NonEmptyArrayTest {
        @Test
        void shouldThrowOnNull() {
            // When:
            final Exception e =
                    assertThrows(
                            NullPointerException.class,
                            () -> requireNonEmpty((String[]) null, ARG_NAME));

            // Then:
            assertThat(e.getMessage(), containsString(ARG_NAME));
        }

        @Test
        void shouldThrowOnEmpty() {
            // When:
            final Exception e =
                    assertThrows(
                            IllegalArgumentException.class,
                            () -> requireNonEmpty(new String[0], ARG_NAME));

            // Then:
            assertThat(e.getMessage(), is(ARG_NAME + " can not be empty"));
        }

        @Test
        void shouldReturnValidParam() {
            // Given:
            final String[] valid = {""};

            // When:
            final String[] result = requireNonEmpty(valid, ARG_NAME);

            // Then:
            assertThat(result, is(sameInstance(valid)));
        }
    }

    @Nested
    final class NonEmptyCollectionTest {
        @Test
        void shouldThrowOnNull() {
            // When:
            final Exception e =
                    assertThrows(
                            NullPointerException.class,
                            () -> requireNonEmpty((Collection<?>) null, ARG_NAME));

            // Then:
            assertThat(e.getMessage(), containsString(ARG_NAME));
        }

        @Test
        void shouldThrowOnEmpty() {
            // When:
            final Exception e =
                    assertThrows(
                            IllegalArgumentException.class,
                            () -> requireNonEmpty(List.of(), ARG_NAME));

            // Then:
            assertThat(e.getMessage(), is(ARG_NAME + " can not be empty"));
        }

        @Test
        void shouldReturnValidParam() {
            // Given:
            final List<String> valid = List.of("x");

            // When:
            final List<String> result = requireNonEmpty(valid, ARG_NAME);

            // Then:
            assertThat(result, is(sameInstance(valid)));
        }
    }

    @Nested
    final class NonEmptyMapTest {
        @Test
        void shouldThrowOnNull() {
            // When:
            final Exception e =
                    assertThrows(
                            NullPointerException.class,
                            () -> requireNonEmpty((Map<?, ?>) null, ARG_NAME));

            // Then:
            assertThat(e.getMessage(), containsString(ARG_NAME));
        }

        @Test
        void shouldThrowOnEmpty() {
            // When:
            final Exception e =
                    assertThrows(
                            IllegalArgumentException.class,
                            () -> requireNonEmpty(Map.of(), ARG_NAME));

            // Then:
            assertThat(e.getMessage(), is(ARG_NAME + " can not be empty"));
        }

        @Test
        void shouldReturnValidParam() {
            // Given:
            final Map<Integer, String> valid = Map.of(1, "x");

            // When:
            final Map<Integer, String> result = requireNonEmpty(valid, ARG_NAME);

            // Then:
            assertThat(result, is(sameInstance(valid)));
        }
    }

    @Nested
    final class NonEmptyStringTest {
        @Test
        void shouldThrowOnNull() {
            // When:
            final Exception e =
                    assertThrows(
                            NullPointerException.class,
                            () -> requireNonEmpty((String) null, ARG_NAME));

            // Then:
            assertThat(e.getMessage(), containsString(ARG_NAME));
        }

        @Test
        void shouldThrowOnEmpty() {
            // When:
            final Exception e =
                    assertThrows(
                            IllegalArgumentException.class, () -> requireNonEmpty("", ARG_NAME));

            // Then:
            assertThat(e.getMessage(), is(ARG_NAME + " can not be empty"));
        }

        @Test
        void shouldReturnValidParam() {
            // Given:
            final String valid = "x";

            // When:
            final String result = requireNonEmpty(valid, ARG_NAME);

            // Then:
            assertThat(result, is(sameInstance(valid)));
        }
    }

    @Nested
    final class NonBlankTest {
        @Test
        void shouldThrowOnNull() {
            // When:
            final Exception e =
                    assertThrows(NullPointerException.class, () -> requireNonBlank(null, ARG_NAME));

            // Then:
            assertThat(e.getMessage(), containsString(ARG_NAME));
        }

        @Test
        void shouldThrowOnBlank() {
            // When:
            final Exception e =
                    assertThrows(
                            IllegalArgumentException.class, () -> requireNonBlank(" ", ARG_NAME));

            // Then:
            assertThat(e.getMessage(), is(ARG_NAME + " can not be blank"));
        }

        @Test
        void shouldReturnValidParam() {
            // Given:
            final String valid = "x";

            // When:
            final String result = requireNonBlank(valid, ARG_NAME);

            // Then:
            assertThat(result, is(sameInstance(valid)));
        }
    }

    @Nested
    final class EqualTest {
        @Test
        void shouldNotThrowIfBothNull() {
            // When:
            requireEqual(null, null, "msg");

            // Then: did not throw.
        }

        @Test
        void shouldThrowIfOneNull() {
            // When:
            final Exception e0 =
                    assertThrows(
                            IllegalArgumentException.class, () -> requireEqual(null, 1, "msg"));
            final Exception e1 =
                    assertThrows(
                            IllegalArgumentException.class, () -> requireEqual(1, null, "msg"));

            // Then:
            assertThat(e0.getMessage(), is("msg null != 1"));
            assertThat(e1.getMessage(), is("msg 1 != null"));
        }

        @Test
        void shouldThrowIfNotEqual() {
            // When:
            final Exception e =
                    assertThrows(IllegalArgumentException.class, () -> requireEqual(10, 11, "msg"));

            // Then:
            assertThat(e.getMessage(), is("msg 10 != 11"));
        }

        @Test
        void shouldNotThrowIfEqual() {
            // When:
            requireEqual(11L, 11L, "msg");

            // Then: did not throw.
        }
    }

    @Nested
    final class LessThanTest {
        @Test
        void shouldThrowOnFalse() {
            // When:
            final Exception e =
                    assertThrows(
                            IllegalArgumentException.class, () -> requireLessThan(10, 9, "msg"));
            assertThrows(IllegalArgumentException.class, () -> requireLessThan(10, 10, "msg"));

            // Then:
            assertThat(e.getMessage(), is("msg must be less than 9, but was 10"));
        }

        @Test
        void shouldNotThrowOnTrue() {
            // When:
            requireLessThan(10, 11, "msg");

            // Then: did not throw
        }
    }

    @Nested
    final class LessThanOrEqualToTest {
        @Test
        void shouldThrowOnFalse() {
            // When:
            final Exception e =
                    assertThrows(
                            IllegalArgumentException.class,
                            () -> requireLessThanOrEqualTo(10, 9, "msg"));

            // Then:
            assertThat(e.getMessage(), is("msg must be less than or equal to 9, but was 10"));
        }

        @Test
        void shouldNotThrowOnTrue() {
            // When:
            requireLessThanOrEqualTo(10, 10, "msg");
            requireLessThanOrEqualTo(9, 10, "msg");

            // Then: did not throw
        }
    }

    @Nested
    final class GreaterThanTest {
        @Test
        void shouldThrowOnFalse() {
            // When:
            final Exception e =
                    assertThrows(
                            IllegalArgumentException.class, () -> requireGreaterThan(9, 10, "msg"));
            assertThrows(IllegalArgumentException.class, () -> requireGreaterThan(10, 10, "msg"));

            // Then:
            assertThat(e.getMessage(), is("msg must be greater than 10, but was 9"));
        }

        @Test
        void shouldNotThrowOnTrue() {
            // When:
            requireGreaterThan(11, 10, "msg");

            // Then: did not throw
        }
    }

    @Nested
    final class GreaterThanOrEqualToTest {
        @Test
        void shouldThrowOnFalse() {
            // When:
            final Exception e =
                    assertThrows(
                            IllegalArgumentException.class,
                            () -> requireGreaterThanOrEqualTo(9, 10, "msg"));

            // Then:
            assertThat(e.getMessage(), is("msg must be greater than or equal to 10, but was 9"));
        }

        @Test
        void shouldNotThrowOnTrue() {
            // When:
            requireGreaterThanOrEqualTo(10, 10, "msg");
            requireGreaterThanOrEqualTo(10, 9, "msg");

            // Then: did not throw
        }
    }

    @Nested
    final class RequiredTest {
        @Test
        void shouldThrowOnFalse() {
            // When:
            final Exception e =
                    assertThrows(
                            IllegalArgumentException.class, () -> require(false, "some message"));

            // Then:
            assertThat(e.getMessage(), is("some message"));
        }

        @Test
        void shouldNotThrowOnTrue() {
            // When:
            require(true, "some message");

            // Then: did not throw
        }
    }
}

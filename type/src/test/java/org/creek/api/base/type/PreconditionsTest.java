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

package org.creek.api.base.type;

import static org.creek.api.base.type.Preconditions.requireNonBlank;
import static org.creek.api.base.type.Preconditions.requireNonEmpty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressFBWarnings(
        value = "SIC_INNER_SHOULD_BE_STATIC",
        justification = "https://github.com/spotbugs/spotbugs/issues/560")
class PreconditionsTest {

    private static final String ARG_NAME = "some-arg";

    @Nested
    final class NonEmptyTest {
        @Test
        void shouldThrowOnNullString() {
            // When:
            final Exception e =
                    assertThrows(NullPointerException.class, () -> requireNonEmpty(null, ARG_NAME));

            // Then:
            assertThat(e.getMessage(), containsString(ARG_NAME));
        }

        @Test
        void shouldThrowOnEmptyString() {
            // When:
            final Exception e =
                    assertThrows(
                            IllegalArgumentException.class, () -> requireNonEmpty("", ARG_NAME));

            // Then:
            assertThat(e.getMessage(), is(ARG_NAME + " can not be empty"));
        }

        @Test
        void shouldNotThrowOnNonEmptyString() {
            requireNonEmpty(" ", ARG_NAME);
        }
    }

    @Nested
    final class NonBlankTest {
        @Test
        void shouldThrowOnNullString() {
            // When:
            final Exception e =
                    assertThrows(NullPointerException.class, () -> requireNonBlank(null, ARG_NAME));

            // Then:
            assertThat(e.getMessage(), containsString(ARG_NAME));
        }

        @Test
        void shouldThrowOnBlankString() {
            // When:
            final Exception e =
                    assertThrows(
                            IllegalArgumentException.class, () -> requireNonBlank(" ", ARG_NAME));

            // Then:
            assertThat(e.getMessage(), is(ARG_NAME + " can not be blank"));
        }

        @Test
        void shouldNotThrowOnNonBlankString() {
            requireNonBlank(".", ARG_NAME);
        }
    }
}

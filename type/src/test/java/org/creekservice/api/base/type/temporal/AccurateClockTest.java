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

package org.creekservice.api.base.type.temporal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.both;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

import java.time.Instant;
import org.junit.jupiter.api.Test;

class AccurateClockTest {

    @Test
    void shouldGetTime() {
        // Given:
        final Clock clock = AccurateClock.create();
        final Instant start = Instant.now();

        // When:
        final Instant now = clock.get();

        // Then:
        final Instant end = Instant.now();
        assertThat(
                now.toEpochMilli(),
                is(
                        both(greaterThanOrEqualTo(start.toEpochMilli()))
                                .and(lessThanOrEqualTo(end.toEpochMilli()))));
    }
}

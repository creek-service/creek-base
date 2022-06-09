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

package org.creekservice.api.base.type;

import static org.creekservice.api.base.type.Iterators.reverseIterator;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;

class IteratorsTest {

    @Test
    void shouldHandleEmptyList() {
        // Given:
        final Iterator<?> it = reverseIterator(List.of());

        // Then:
        assertThat(it.hasNext(), is(false));
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    void shouldIterateBackwards() {
        // Given:
        final Iterator<Integer> it = reverseIterator(List.of(1, 2, 3, 4, 5));
        final List<Integer> result = new ArrayList<>(5);

        // When:
        it.forEachRemaining(result::add);

        // Then:
        assertThat(result, contains(5, 4, 3, 2, 1));
        assertThat(it.hasNext(), is(false));
        assertThrows(NoSuchElementException.class, it::next);
    }
}

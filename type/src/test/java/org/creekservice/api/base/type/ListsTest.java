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

import static org.creekservice.api.base.type.Lists.combineList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.List;
import org.junit.jupiter.api.Test;

class ListsTest {

    @Test
    void shouldCombineLists() {
        // Given:
        final List<Integer> one = List.of(1);
        final List<Integer> two = List.of(2, 3);

        // Then:
        assertThat(combineList(one, two), is(List.of(1, 2, 3)));
        assertThat(combineList(two, one), is(List.of(2, 3, 1)));
        assertThat(combineList(one, List.of()), is(one));
        assertThat(combineList(List.of(), List.of()), is(List.of()));
    }
}

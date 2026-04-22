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

import java.util.ArrayList;
import java.util.List;

/** Util class for working with {@code List}. */
public final class Lists {

    private Lists() {}

    /**
     * Combine to lists
     *
     * @param a first list
     * @param b second list
     * @param <T> element type
     * @return a list containing all the elements from {@code a} and then from {@code b}
     */
    public static <T> List<T> combineList(final List<? extends T> a, final List<? extends T> b) {
        final List<T> result = new ArrayList<>(a);
        result.addAll(b);
        return result;
    }
}

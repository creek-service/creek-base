/*
 * Copyright 2022-2023 Creek Contributors (https://github.com/creek-service)
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

import static java.util.Objects.requireNonNull;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/** Util methods for iterators. */
public final class Iterators {

    private Iterators() {}

    /**
     * An iterator that iterates over the elements in a list in reverse.
     *
     * @param list the list to reverse
     * @param <E> the element type.
     * @return reverse iterator
     */
    public static <E> Iterator<E> reverseIterator(final List<E> list) {
        return new ReverseListIterator<>(list);
    }

    private static final class ReverseListIterator<E> implements Iterator<E> {

        private final ListIterator<E> it;

        private ReverseListIterator(final List<E> list) {
            this.it = requireNonNull(list, "list").listIterator(list.size());
        }

        @Override
        public boolean hasNext() {
            return it.hasPrevious();
        }

        @Override
        public E next() {
            return it.previous();
        }
    }
}

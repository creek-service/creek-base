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

import static java.util.Objects.requireNonNull;

import java.util.function.Supplier;

/** Factory methods for creating {@link Supplier suppliers} */
public final class Suppliers {

    private Suppliers() {}

    /**
     * Create a Supplier that will cache the value returned from the {@code delegate} on first use.
     *
     * @param delegate the delegate that will be called once on first use.
     * @param <T> the type of the supplier
     * @return a caching Supplier.
     */
    public static <T> Supplier<T> memoize(final Supplier<T> delegate) {
        return (delegate instanceof MemorizingSupplier)
                ? delegate
                : new MemorizingSupplier<>(delegate);
    }

    private static final class MemorizingSupplier<T> implements Supplier<T> {

        private final Supplier<T> delegate;
        private final Object lock = new Object();
        private transient volatile boolean initialized;
        private transient T value;

        private MemorizingSupplier(final Supplier<T> delegate) {
            this.delegate = requireNonNull(delegate, "delegate");
        }

        @Override
        public T get() {
            if (!initialized) {
                synchronized (lock) {
                    if (!initialized) {
                        final T t = delegate.get();
                        value = t;
                        initialized = true;
                        return t;
                    }
                }
            }
            return value;
        }

        @Override
        public String toString() {
            return "Suppliers.memoize(" + delegate + ")";
        }
    }
}

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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.Supplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SuppliersTest {

    @Mock private Supplier<Object> inner;
    private Supplier<?> memoized;

    @BeforeEach
    void setUp() {
        memoized = Suppliers.memoize(inner);

        when(inner.get()).thenReturn("It worked");
    }

    @Test
    void shouldReturnValue() {
        assertThat(memoized.get(), is("It worked"));
    }

    @Test
    void shouldGetOnlyOnce() {
        // Given:
        memoized.get();

        // When:
        final Object result = memoized.get();

        // Then:
        verify(inner, times(1)).get();
        assertThat(result, is("It worked"));
    }

    @Test
    void shouldHandleNulls() {
        // Given:
        when(inner.get()).thenReturn(null);

        // When:
        final Object firstResult = memoized.get();
        final Object secondResult = memoized.get();

        // Then:
        assertThat(firstResult, is(nullValue()));
        assertThat(secondResult, is(nullValue()));
        verify(inner, times(1)).get();
    }

    @Test
    void shouldNotDoubleWrap() {
        assertThat(Suppliers.memoize(memoized), is(sameInstance(memoized)));
    }

    @Test
    void shouldWrapToString() {
        // Given:
        inner =
                new Supplier<>() {
                    @Override
                    public Object get() {
                        return null;
                    }

                    @Override
                    public String toString() {
                        return "inner";
                    }
                };
        memoized = Suppliers.memoize(inner);

        // When:
        final String text = memoized.toString();

        // Then:
        assertThat(text, is("Suppliers.memoize(inner)"));
    }

    @Test
    void shouldBeThreadSafe() throws Exception {
        // Given:
        final CompletableFuture<Object> blockFuture = new CompletableFuture<>();
        when(inner.get()).thenAnswer(inv -> blockFuture.get());

        final Future<?> firstResult = startThreadToGetValue();
        final Future<?> secondResult = startThreadToGetValue();

        // When:
        blockFuture.complete("It worked");

        // Then:
        assertThat(firstResult.get(), is("It worked"));
        assertThat(secondResult.get(), is("It worked"));
        verify(inner, times(1)).get();
    }

    private Future<?> startThreadToGetValue() {
        final CompletableFuture<Object> result = new CompletableFuture<>();
        new Thread(() -> result.complete(memoized.get())).start();
        return result;
    }
}

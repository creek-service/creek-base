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

package org.creekservice.api.base.type.config;

import static java.time.temporal.ChronoUnit.DECADES;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.SetEnvironmentVariable;

class SystemEnvTest {

    @SetEnvironmentVariable(key = "a-key", value = "-109")
    @Test
    void shouldReadInt() {
        assertThat(SystemEnv.readInt("a-key", 19), is(-109));
        assertThat(SystemEnv.readInt("missing", 19), is(19));
        assertThat(SystemEnv.readInt("missing", null), is(nullValue()));
    }

    @SetEnvironmentVariable(key = "a-key", value = "Not a number")
    @Test
    void shouldThrowOnInvalidInt() {
        // When:
        final Exception e =
                assertThrows(IllegalArgumentException.class, () -> SystemEnv.readInt("a-key", 19));

        // Then:
        assertThat(e.getMessage(), containsString("name: a-key"));
        assertThat(e.getMessage(), containsString("value: Not a number"));
    }

    @SetEnvironmentVariable(key = "a-key", value = "265")
    @Test
    void shouldReadLong() {
        assertThat(SystemEnv.readLong("a-key", 19L), is(265L));
        assertThat(SystemEnv.readLong("missing", 19L), is(19L));
        assertThat(SystemEnv.readLong("missing", null), is(nullValue()));
    }

    @SetEnvironmentVariable(key = "a-key", value = "Not a number")
    @Test
    void shouldThrowOnInvalidLong() {
        // When:
        final Exception e =
                assertThrows(IllegalArgumentException.class, () -> SystemEnv.readLong("a-key", 0L));

        // Then:
        assertThat(e.getMessage(), containsString("a-key"));
        assertThat(e.getMessage(), containsString("Not a number"));
    }

    @SetEnvironmentVariable(key = "a-key", value = "a-value")
    @Test
    void shouldReadString() {
        assertThat(SystemEnv.readString("a-key", "a-default"), is("a-value"));
        assertThat(SystemEnv.readString("missing", "a-default"), is("a-default"));
    }

    @SetEnvironmentVariable(key = "a-key", value = "Not a duration")
    @SetEnvironmentVariable(key = "overflow", value = "99999999999999999")
    @Test
    void shouldThrowOnInvalidDurationUnit() {
        // When:
        final Exception e =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> SystemEnv.readDuration("a-key", 5, SECONDS));

        final Exception e2 =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> SystemEnv.readDuration("overflow", 5, DECADES));

        // Then:
        assertThat(e.getMessage(), containsString("a-key"));
        assertThat(e.getMessage(), containsString("Not a duration"));

        assertThat(e2.getMessage(), containsString("overflow"));
        assertThat(e2.getCause(), is(instanceOf(ArithmeticException.class)));
    }

    @SetEnvironmentVariable(key = "a-key", value = "Not a duration")
    @Test
    void shouldThrowOnInvalidDuration() {
        // When:
        final Exception e =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> SystemEnv.readDuration("a-key", Duration.ofMinutes(19)));

        // Then:
        assertThat(e.getMessage(), containsString("a-key"));
        assertThat(e.getMessage(), containsString("Not a duration"));
    }

    @SetEnvironmentVariable(key = "a-key", value = "24")
    @SetEnvironmentVariable(key = "negative", value = "-99")
    @Test
    void shouldReadDurationUnit() {
        assertThat(SystemEnv.readDuration("a-key", 19, SECONDS), is(Duration.ofSeconds(24)));
        assertThat(SystemEnv.readDuration("negative", 19, SECONDS), is(Duration.ofSeconds(-99)));
        assertThat(SystemEnv.readDuration("missing", 19, SECONDS), is(Duration.ofSeconds(19)));
    }

    @SetEnvironmentVariable(key = "a-key", value = "PT28M")
    @Test
    void shouldReadDuration() {
        assertThat(
                SystemEnv.readDuration("a-key", Duration.ofMinutes(3)), is(Duration.ofMinutes(28)));
        assertThat(
                SystemEnv.readDuration("missing", Duration.ofMinutes(3)),
                is(Duration.ofMinutes(3)));
    }

    @SetEnvironmentVariable(key = "a-key", value = "Not a class")
    @Test
    void shouldThrowOnUnknownImpl() {
        // When:
        final Exception e =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> SystemEnv.<Thing>readInstance("a-key", Thing2::new));

        // Then:
        assertThat(e.getMessage(), containsString("a-key"));
        assertThat(e.getMessage(), containsString("Not a class"));
    }

    @SetEnvironmentVariable(
            key = "a-key",
            value = "org.creekservice.api.base.type.config.SystemEnvTest$Thing1")
    @Test
    void shouldReadInstance() {
        assertThat(
                SystemEnv.<Thing>readInstance("a-key", Thing2::new), is(instanceOf(Thing1.class)));
        assertThat(
                SystemEnv.<Thing>readInstance("missing", Thing2::new),
                is(instanceOf(Thing2.class)));
    }

    @SetEnvironmentVariable(
            key = "a-key",
            value = "org.creekservice.api.base.type.config.SystemEnvTest$BadThing")
    @Test
    void shouldNotSwallowExceptionsThrownByInstance() {
        // When:
        final Exception e =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> SystemEnv.<Thing>readInstance("a-key", Thing1::new));

        // Then:
        assertThat(e.getMessage(), containsString("a-key"));
        assertThat(
                e.getMessage(),
                containsString("org.creekservice.api.base.type.config.SystemEnvTest$BadThing"));
        assertThat(e.getCause(), is(instanceOf(InvocationTargetException.class)));
        assertThat(e.getCause().getCause(), is(instanceOf(RuntimeException.class)));
        assertThat(e.getCause().getCause().getMessage(), is("Big Bada Boom"));
    }

    interface Thing {}

    public static class Thing1 implements Thing {
        Thing1() {}
    }

    public static class Thing2 implements Thing {
        Thing2() {}
    }

    @SuppressWarnings("unused") // Invoked via reflection
    public static final class BadThing implements Thing {
        BadThing() {
            throw new RuntimeException("Big Bada Boom");
        }
    }
}

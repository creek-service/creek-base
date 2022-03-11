package org.creek.api.base.type.config;

import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.SetEnvironmentVariable;

import java.lang.reflect.InvocationTargetException;
import java.time.Duration;

import static java.time.temporal.ChronoUnit.DECADES;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.*;

class EnvTest {

    @SetEnvironmentVariable(key = "a-key", value = "-109")
    @Test
    void shouldReadInt() {
        assertThat(Env.readInt("a-key", 19), is(-109));
        assertThat(Env.readInt("missing", 19), is(19));
        assertThat(Env.readInt("missing", null), is(nullValue()));
    }

    @SetEnvironmentVariable(key = "a-key", value = "Not a number")
    @Test
    void shouldThrowOnInvalidInt() {
        // When:
        final Exception e =
                assertThrows(IllegalArgumentException.class, () -> Env.readInt("a-key", 19));

        // Then:
        assertThat(e.getMessage(), containsString("a-key"));
        assertThat(e.getMessage(), containsString("Not a number"));
    }

    @SetEnvironmentVariable(key = "a-key", value = "265")
    @Test
    void shouldReadLong() {
        assertThat(Env.readLong("a-key", 19L), is(265L));
        assertThat(Env.readLong("missing", 19L), is(19L));
        assertThat(Env.readLong("missing", null), is(nullValue()));
    }

    @SetEnvironmentVariable(key = "a-key", value = "Not a number")
    @Test
    void shouldThrowOnInvalidLong() {
        // When:
        final Exception e =
                assertThrows(IllegalArgumentException.class, () -> Env.readLong("a-key", 0L));

        // Then:
        assertThat(e.getMessage(), containsString("a-key"));
        assertThat(e.getMessage(), containsString("Not a number"));
    }

    @SetEnvironmentVariable(key = "a-key", value = "a-value")
    @Test
    void shouldReadString() {
        assertThat(Env.readString("a-key", "a-default"), is("a-value"));
        assertThat(Env.readString("missing", "a-default"), is("a-default"));
    }

    @SetEnvironmentVariable(key = "a-key", value = "Not a duration")
    @SetEnvironmentVariable(key = "overflow", value = "99999999999999999")
    @Test
    void shouldThrowOnInvalidDurationUnit() {
        // When:
        final Exception e =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> Env.readDuration("a-key", 5, SECONDS));

        final Exception e2 =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> Env.readDuration("overflow", 5, DECADES));

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
                        () -> Env.readDuration("a-key", Duration.ofMinutes(19)));

        // Then:
        assertThat(e.getMessage(), containsString("a-key"));
        assertThat(e.getMessage(), containsString("Not a duration"));
    }

    @SetEnvironmentVariable(key = "a-key", value = "24")
    @SetEnvironmentVariable(key = "negative", value = "-99")
    @Test
    void shouldReadDurationUnit() {
        assertThat(Env.readDuration("a-key", 19, SECONDS), is(Duration.ofSeconds(24)));
        assertThat(Env.readDuration("negative", 19, SECONDS), is(Duration.ofSeconds(-99)));
        assertThat(Env.readDuration("missing", 19, SECONDS), is(Duration.ofSeconds(19)));
    }

    @SetEnvironmentVariable(key = "a-key", value = "PT28M")
    @Test
    void shouldReadDuration() {
        assertThat(Env.readDuration("a-key", Duration.ofMinutes(3)), is(Duration.ofMinutes(28)));
        assertThat(Env.readDuration("missing", Duration.ofMinutes(3)), is(Duration.ofMinutes(3)));
    }

    @SetEnvironmentVariable(key = "a-key", value = "Not a class")
    @Test
    void shouldThrowOnUnknownImpl() {
        // When:
        final Exception e =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> Env.<Thing>readInstance("a-key", Thing2::new));

        // Then:
        assertThat(e.getMessage(), containsString("a-key"));
        assertThat(e.getMessage(), containsString("Not a class"));
    }

    @SetEnvironmentVariable(key = "a-key", value = "org.creek.api.base.type.config.EnvTest$Thing1")
    @Test
    void shouldReadInstance() {
        assertThat(Env.<Thing>readInstance("a-key", Thing2::new), is(instanceOf(Thing1.class)));
        assertThat(Env.<Thing>readInstance("missing", Thing2::new), is(instanceOf(Thing2.class)));
    }

    @SetEnvironmentVariable(key = "a-key", value = "org.creek.api.base.type.config.EnvTest$BadThing")
    @Test
    void shouldNotSwallowExceptionsThrownByInstance() {
        // When:
        final Exception e =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> Env.<Thing>readInstance("a-key", Thing1::new));

        // Then:
        assertThat(e.getMessage(), containsString("a-key"));
        assertThat(e.getMessage(), containsString("org.creek.api.base.type.config.EnvTest$BadThing"));
        assertThat(e.getCause(), is(instanceOf(InvocationTargetException.class)));
        assertThat(e.getCause().getCause(), is(instanceOf(RuntimeException.class)));
        assertThat(e.getCause().getCause().getMessage(), is("Big Bada Boom"));
    }

    interface Thing {}

    public static class Thing1 implements Thing {}

    public static class Thing2 implements Thing {}

    @SuppressWarnings("unused") // Invoked via reflection
    public static class BadThing implements Thing {
        BadThing() {
            throw new RuntimeException("Big Bada Boom");
        }
    }
}
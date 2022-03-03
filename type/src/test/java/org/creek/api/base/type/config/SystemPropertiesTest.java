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

package org.creek.api.base.type.config;

import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.SetSystemProperty;

import java.util.Optional;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class SystemPropertiesTest {

    @SetSystemProperty(key = "k", value = "text")
    @Test
    void shouldGetString() {
        assertThat(SystemProperties.getString("k"), is(Optional.of("text")));
        assertThat(SystemProperties.getString("missing", "default"), is("default"));
    }

    @SetSystemProperty(key = "k", value = "-101")
    @Test
    void shouldGetInt() {
        assertThat(SystemProperties.getInt("k"), is(Optional.of(-101)));
        assertThat(SystemProperties.getInt("missing", 22), is(22));
    }

    @SetSystemProperty(key = "k", value = "invalid")
    @Test
    void shouldThrowOnInvalidInt() {
        // When:
        final Exception e = assertThrows(SystemProperties.ParseException.class, () -> SystemProperties.getInt("k"));

        // Then:
        assertThat(e.getMessage(), is("Failed to parse system property: k, value: invalid, as_type: int"));
                assertThat(e.getCause(), is(instanceOf(NumberFormatException.class)));
    }

    @SetSystemProperty(key = "k", value = "-101")
    @Test
    void shouldGetLong() {
        assertThat(SystemProperties.getLong("k"), is(Optional.of(-101L)));
        assertThat(SystemProperties.getLong("missing", 22), is(22L));
    }

    @SetSystemProperty(key = "k", value = "invalid")
    @Test
    void shouldThrowOnInvalidLong() {
        // When:
        final Exception e = assertThrows(SystemProperties.ParseException.class, () -> SystemProperties.getLong("k"));

        // Then:
        assertThat(e.getMessage(), is("Failed to parse system property: k, value: invalid, as_type: long"));
        assertThat(e.getCause(), is(instanceOf(NumberFormatException.class)));
    }
}
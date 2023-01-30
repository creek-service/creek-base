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

import static org.creekservice.api.base.type.CodeLocation.codeLocation;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.nio.file.Path;
import org.creekservice.api.test.util.TestPaths;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

class CodeLocationTest {

    private static final Path CODE_LOCATION =
            TestPaths.moduleRoot("type").resolve("build/classes/java/test").toAbsolutePath();

    @Test
    void shouldGetLocationFromInstance() {
        assertThat(codeLocation(this), is("file:" + CODE_LOCATION + "/"));
    }

    @Test
    void shouldGetLocationFromType() {
        MatcherAssert.assertThat(
                codeLocation(CodeLocationTest.class), is("file:" + CODE_LOCATION + "/"));
    }

    @Test
    void shouldNotBlowUpOnNullInstance() {
        MatcherAssert.assertThat(codeLocation((Object) null), is("N/A"));
    }

    @SuppressWarnings("RedundantCast")
    @Test
    void shouldNotBlowUpOnNullType() {
        MatcherAssert.assertThat(codeLocation((Class<?>) null), is("N/A"));
    }

    @Test
    void shouldNotBlowUpOnNoCodeLocation() {
        MatcherAssert.assertThat(codeLocation(Object.class), is("N/A"));
    }
}

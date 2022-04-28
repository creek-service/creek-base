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

package org.creek.api.base.type;

import static org.creek.api.base.type.JarVersion.jarVersion;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import java.net.URI;
import java.util.Optional;
import org.creek.api.base.annotation.VisibleForTesting;
import org.junit.jupiter.api.Test;

class JarVersionTest {
    @Test
    void shouldGetVersionFromJar() {
        assertThat(JarVersion.jarVersion(VisibleForTesting.class), is(not(Optional.empty())));
    }

    @Test
    void shouldReturnEmptyWhenRunningInTheBuildAndThereIsNoJar() throws Exception {
        assertThat(
                jarVersion(URI.create("file:/blah/blah/type/build/classes/java/test/").toURL()),
                is(Optional.empty()));
    }

    @Test
    void shouldReturnEmptyIfSomeWeirdJarName() throws Exception {
        assertThat(
                jarVersion(URI.create("file:/blah/blah/a-version-less-jar.jar").toURL()),
                is(Optional.empty()));
    }

    @Test
    void shouldReturnVersionForSemantic() throws Exception {
        assertThat(
                jarVersion(URI.create("file:/blah/blah/some-jar-0.1.3.jar").toURL()),
                is(Optional.of("0.1.3")));
    }

    @Test
    void shouldReturnVersionForSemanticSnapshot() throws Exception {
        assertThat(
                jarVersion(URI.create("file:/blah/blah/some-jar-0.1.3-SNAPSHOT.jar").toURL()),
                is(Optional.of("0.1.3-SNAPSHOT")));
    }
}

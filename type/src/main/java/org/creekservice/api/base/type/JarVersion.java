/*
 * Copyright 2022-2024 Creek Contributors (https://github.com/creek-service)
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

import java.net.URL;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.creekservice.api.base.annotation.VisibleForTesting;

/** Util for extracting the version of a Creek Jar from its filename. */
public final class JarVersion {

    private static final Pattern JAR_PATTERN =
            Pattern.compile(".*-(?<version>\\d.*(?:-SNAPSHOT)?).jar$");

    private JarVersion() {}

    /**
     * Get the version text of the Creek jar that {@code typeInJar} belongs too.
     *
     * @param typeInJar any type from the jar in question.
     * @return the version, if it can be determined.
     */
    public static Optional<String> jarVersion(final Class<?> typeInJar) {
        return jarVersion(typeInJar.getProtectionDomain().getCodeSource().getLocation());
    }

    @VisibleForTesting
    static Optional<String> jarVersion(final URL location) {
        final Matcher jarMatcher = JAR_PATTERN.matcher(location.toString());
        if (!jarMatcher.matches()) {
            return Optional.empty();
        }

        return Optional.of(jarMatcher.group("version"));
    }
}

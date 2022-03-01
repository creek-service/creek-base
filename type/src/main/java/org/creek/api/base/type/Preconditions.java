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

import static java.util.Objects.requireNonNull;

public final class Preconditions {

    private Preconditions() {}

    /**
     * Test that string is not empty, i.e. has non-zero length.
     *
     * @param value the string to test.
     * @param name the name to use in the exception, if the check fails.
     * @return {@code value} if the check passes.
     */
    public static String requireNonEmpty(final String value, final String name) {
        requireNonNull(value, name);
        if (value.isEmpty()) {
            throw new IllegalArgumentException(name + " can not be empty");
        }
        return value;
    }

    /**
     * Test that string is not blank, i.e. has a least one non-whitespace character.
     *
     * @param value the string to test.
     * @param name the name to use in the exception, if the check fails.
     * @return {@code value} if the check passes.
     */
    public static String requireNonBlank(final String value, final String name) {
        requireNonNull(value, name);
        if (value.isBlank()) {
            throw new IllegalArgumentException(name + " can not be blank");
        }
        return value;
    }
}

/*
 * Copyright 2022-2025 Creek Contributors (https://github.com/creek-service)
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

import java.security.CodeSource;

/**
 * Util class for obtaining the location a type or instance is loaded from, i.e. the module/jar.
 *
 * <p>This can be useful in error messages to help users locate the jar(s) and issue is coming from.
 */
public final class CodeLocation {

    private static final String LOCATION_NOT_AVAILABLE = "N/A";

    private CodeLocation() {}

    /**
     * Get the code location for the type of the supplied {@code instance}.
     *
     * @param instance the instance
     * @return the code location, if it can be determined.
     */
    public static String codeLocation(final Object instance) {
        return instance == null ? LOCATION_NOT_AVAILABLE : codeLocation(instance.getClass());
    }

    /**
     * Get the code location for the supplied {@code type}.
     *
     * @param type the type.
     * @return the code location, if it can be determined.
     */
    public static String codeLocation(final Class<?> type) {
        if (type == null) {
            return LOCATION_NOT_AVAILABLE;
        }
        try {
            final CodeSource codeSource = type.getProtectionDomain().getCodeSource();
            if (codeSource == null) {
                return LOCATION_NOT_AVAILABLE;
            }
            return codeSource.getLocation().toString();
        } catch (final Exception e) {
            return LOCATION_NOT_AVAILABLE;
        }
    }
}

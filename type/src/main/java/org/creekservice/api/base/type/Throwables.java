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

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.PrintWriter;
import java.io.StringWriter;

/** Utility class for working with {@link Throwable} */
public final class Throwables {

    private Throwables() {}

    /**
     * Get the stack trace of the supplied {@link Throwable}.
     *
     * @param t the throwable
     * @return the stack trace
     */
    @SuppressFBWarnings(
            value = "INFORMATION_EXPOSURE_THROUGH_AN_ERROR_MESSAGE",
            justification = "Intentional")
    public static String stackTrace(final Throwable t) {
        final StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
}

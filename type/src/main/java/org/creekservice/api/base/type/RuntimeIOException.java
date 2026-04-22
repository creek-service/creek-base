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

import java.io.IOException;

/** As checked exceptions are a PITA. */
public final class RuntimeIOException extends RuntimeException {

    /**
     * Factory method
     *
     * @param cause exception to wrap
     * @return new instance
     */
    public static RuntimeIOException runtimeIOException(final IOException cause) {
        return new RuntimeIOException(cause);
    }

    /**
     * Factory method
     *
     * @param msg the exception msg
     * @param cause exception to wrap
     * @return new instance
     */
    public static RuntimeIOException runtimeIOException(final String msg, final IOException cause) {
        return new RuntimeIOException(msg, cause);
    }

    private RuntimeIOException(final IOException cause) {
        super(cause);
    }

    private RuntimeIOException(final String msg, final IOException cause) {
        super(msg, cause);
    }
}

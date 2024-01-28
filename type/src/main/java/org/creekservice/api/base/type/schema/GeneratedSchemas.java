/*
 * Copyright 2024 Creek Contributors (https://github.com/creek-service)
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

package org.creekservice.api.base.type.schema;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;

/**
 * Utility class for working with {@link
 * org.creekservice.api.base.annotation.schema.GeneratesSchema} types.
 */
public final class GeneratedSchemas {

    private GeneratedSchemas() {}

    /**
     * The path to a schema for a given type.
     *
     * <p>Schemas are generated into the same package as the source type.
     *
     * @param type the type
     * @param extension the expected file extension, e.g. {@code ".yml"}.
     * @return the filename the schema will be stored in.
     */
    @SuppressFBWarnings(value = "PATH_TRAVERSAL_IN", justification = "False positive")
    public static Path schemaFileName(final Class<?> type, final String extension) {
        final String fullName = type.getName();
        final int idx = fullName.lastIndexOf(".");

        final String className = idx == -1 ? fullName : fullName.substring(idx + 1);
        final String packageName = idx == -1 ? "" : fullName.substring(0, idx);

        final String directory =
                packageName.replaceAll("\\.", Matcher.quoteReplacement(File.separator));
        return Paths.get(directory, className + extension);
    }

    /**
     * @return the file extension used for YAML files.
     */
    public static String yamlExtension() {
        return ".yml";
    }
}

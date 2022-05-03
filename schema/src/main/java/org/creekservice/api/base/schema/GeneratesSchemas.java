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

package org.creekservice.api.base.schema;


import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import java.util.Set;
import java.util.stream.Collectors;
import org.creekservice.api.base.annotation.schema.GeneratesSchema;

/**
 * Used to find instances of classes on the class or module path that are annotated with {@link
 * GeneratesSchema}.
 */
public final class GeneratesSchemas {

    private GeneratesSchemas() {}

    /**
     * Find types annotated with {@link GeneratesSchema} on the class or module paths.
     *
     * @param packageName the name of the package to limit the search to. Sub packages are included.
     * @return the list of all types annotated with {@link GeneratesSchema}.
     */
    public static Set<Class<?>> findAnnotatedTypes(final String packageName) {
        try (ScanResult sr =
                new ClassGraph()
                        .enableClassInfo()
                        .enableAnnotationInfo()
                        .acceptPackages(packageName)
                        .scan()) {

            return sr.getClassesWithAnnotation(GeneratesSchema.class.getName()).stream()
                    .map(ClassInfo::loadClass)
                    .collect(Collectors.toUnmodifiableSet());
        }
    }
}

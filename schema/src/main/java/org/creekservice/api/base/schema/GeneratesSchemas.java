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

import static java.util.Objects.requireNonNull;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ModuleRef;
import io.github.classgraph.ScanResult;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.creekservice.api.base.annotation.schema.GeneratesSchema;

/**
 * Used to find instances of classes on the class or module path that are annotated with {@link
 * GeneratesSchema}.
 */
public final class GeneratesSchemas {

    private GeneratesSchemas() {}

    public static Scanner scanner() {
        return new Scanner();
    }

    public static final class Scanner {

        private final Set<String> allowedPackages = new HashSet<>();
        private final Set<String> allowedModules = new HashSet<>();

        private Scanner() {}

        /**
         * Add allowed packages.
         *
         * <p>By default, types from any package are returned. Specifying one or more allowed
         * packages restricts the returned types to only those under the allowed packages.
         *
         * @param allowedPackages allowed packages to add. Package names may include the glob
         *     wildcard {@code *} character.
         * @return self
         */
        public Scanner withAllowedPackages(final Collection<String> allowedPackages) {
            this.allowedPackages.addAll(allowedPackages);
            return this;
        }

        /**
         * Add allowed packages.
         *
         * <p>By default, types from any package are returned. Specifying one or more allowed
         * packages restricts the returned types to only those under the allowed packages.
         *
         * @param allowedPackages allowed packages to add. Package names may include the glob
         *     wildcard {@code *} character.
         * @return self
         */
        public Scanner withAllowedPackages(final String... allowedPackages) {
            return withAllowedPackages(List.of(allowedPackages));
        }

        /**
         * Add allowed modules.
         *
         * <p>By default, types from any module are returned. Specifying one or more allowed modules
         * restricts the returned types to only those types that belong to an allowed module.
         *
         * @param allowedModules allowed module names to add.
         * @return self
         */
        public Scanner withAllowedModules(final Collection<String> allowedModules) {
            this.allowedModules.addAll(allowedModules);
            return this;
        }

        /**
         * Add allowed modules.
         *
         * <p>By default, types from any module are returned. Specifying one or more allowed modules
         * restricts the returned types to only those types that belong to an allowed module.
         *
         * @param allowedModules allowed module names to add.
         * @return self
         */
        public Scanner withAllowedModules(final String... allowedModules) {
            return withAllowedModules(List.of(allowedModules));
        }

        /**
         * Find types annotated with {@link GeneratesSchema} on the class or module paths.
         *
         * @return the list of all types annotated with {@link GeneratesSchema}.
         */
        public Set<Class<?>> scan() {
            final ModuleFilter moduleFilter = new ModuleFilter(allowedModules);

            try (ScanResult sr =
                    new ClassGraph()
                            .enableClassInfo()
                            .enableAnnotationInfo()
                            .acceptPackages(allowedPackages.toArray(String[]::new))
                            .scan()) {

                return sr.getClassesWithAnnotation(GeneratesSchema.class.getName()).stream()
                        .filter(moduleFilter)
                        .map(ClassInfo::loadClass)
                        .collect(Collectors.toUnmodifiableSet());
            }
        }
    }

    private static final class ModuleFilter implements Predicate<ClassInfo> {
        private final Set<String> allowedModules;

        ModuleFilter(final Collection<String> allowedModules) {
            this.allowedModules = Set.copyOf(requireNonNull(allowedModules, "allowedModules"));
        }

        @Override
        public boolean test(final ClassInfo classInfo) {
            if (allowedModules.isEmpty()) {
                return true;
            }

            final ModuleRef moduleRef = classInfo.getModuleRef();
            return moduleRef != null
                    && moduleRef.getName() != null
                    && allowedModules.contains(moduleRef.getName());
        }
    }
}

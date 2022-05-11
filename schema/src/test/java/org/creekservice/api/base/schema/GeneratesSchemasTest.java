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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;

import java.util.Set;
import java.util.stream.Collectors;
import org.creekservice.api.base.annotation.schema.GeneratesSchema;
import org.creekservice.api.base.test.module.ApiModel;
import org.junit.jupiter.api.Test;

class GeneratesSchemasTest {

    private static final String INTERNAL_CLASS_NAME = "InternalModel";

    @Test
    void shouldFindPublicAnnotatedTypes() {
        // When:
        final Set<Class<?>> result = GeneratesSchemas.scanner().scan();

        // Then:
        assertThat(result, hasItems(PublicTestType.class, ApiModel.class));
    }

    @Test
    void shouldFindInternalAnnotatedTypes() {
        // When:
        final Set<Class<?>> result = GeneratesSchemas.scanner().scan();

        // Then:
        assertThat(classNames(result), hasItem(INTERNAL_CLASS_NAME));
    }

    @Test
    void shouldNotFindNonPublicAnnotatedTypes() {
        // When:
        final Set<Class<?>> result = GeneratesSchemas.scanner().scan();

        // Then:
        assertThat(result, not(hasItem(NonPublicTestType.class)));
    }

    @Test
    void shouldFindTypesThatPassLiteralPackageFilter() {
        // When:
        final Set<Class<?>> result =
                GeneratesSchemas.scanner()
                        .withAllowedPackages(GeneratesSchemasTest.class.getPackageName())
                        .scan();

        // Then:
        assertThat(result, hasItem(PublicTestType.class));
    }

    @Test
    void shouldFindTypesUsingWildcardInPackageName() {
        // When:
        final Set<Class<?>> result =
                GeneratesSchemas.scanner().withAllowedPackages("org.creekservice.*.schema").scan();

        // Then:
        assertThat(result, hasItem(PublicTestType.class));
    }

    @Test
    void shouldFindTypesUsingJustWildcard() {
        // When:
        final Set<Class<?>> result = GeneratesSchemas.scanner().withAllowedPackages("*").scan();

        // Then:
        assertThat(result, hasItem(PublicTestType.class));
    }

    @Test
    void shouldFindTypesInSubPackages() {
        // When:
        final Set<Class<?>> result =
                GeneratesSchemas.scanner().withAllowedPackages("org.creekservice").scan();

        // Then:
        assertThat(result, hasItem(PublicTestType.class));
    }

    @Test
    void shouldNotFindTypesInOtherPackages() {
        // When:
        final Set<Class<?>> result =
                GeneratesSchemas.scanner()
                        .withAllowedPackages("other.creekservice.api.base.schema")
                        .scan();

        // Then:
        assertThat(result, not(hasItem(PublicTestType.class)));
    }

    @Test
    void shouldNotFindTypesInOtherPackagesUsingWildcard() {
        // When:
        final Set<Class<?>> result =
                GeneratesSchemas.scanner()
                        .withAllowedPackages("other.creekservice.*.schema")
                        .scan();

        // Then:
        assertThat(result, not(hasItem(PublicTestType.class)));
    }

    @Test
    void shouldFindTypeInRightModule() {
        // When:
        final Set<Class<?>> result =
                GeneratesSchemas.scanner().withAllowedModules("creek.base.test.module").scan();

        // Then:
        assertThat(
                classNames(result), contains(ApiModel.class.getSimpleName(), INTERNAL_CLASS_NAME));
    }

    @Test
    void shouldFindTypeInModuleUsingWildcard() {
        // When:
        final Set<Class<?>> result =
                GeneratesSchemas.scanner().withAllowedModules("creek.base.*.module").scan();

        // Then:
        assertThat(
                classNames(result), contains(ApiModel.class.getSimpleName(), INTERNAL_CLASS_NAME));
    }

    @Test
    void shouldFindTypeInModuleUsingJustWildcard() {
        // When:
        final Set<Class<?>> result = GeneratesSchemas.scanner().withAllowedModules("*").scan();

        // Then:
        assertThat(
                classNames(result),
                hasItems(
                        ApiModel.class.getSimpleName(),
                        INTERNAL_CLASS_NAME,
                        PublicTestType.class.getSimpleName()));
    }

    @Test
    void shouldNotFindTypeInOtherModule() {
        // When:
        final Set<Class<?>> result =
                GeneratesSchemas.scanner().withAllowedModules("creek.base.test.module").scan();

        // Then:
        assertThat(result, not(hasItem(PublicTestType.class)));
    }

    @Test
    void shouldFilterBasedOnPackageAndModule() {
        // When:
        final Set<Class<?>> result =
                GeneratesSchemas.scanner()
                        .withAllowedPackages("*.api.*")
                        .withAllowedModules("creek.base.test.module")
                        .scan();

        // Then:
        assertThat(classNames(result), contains(ApiModel.class.getSimpleName()));
    }

    private static Set<String> classNames(final Set<Class<?>> result) {
        return result.stream().map(Class::getSimpleName).collect(Collectors.toSet());
    }

    @GeneratesSchema
    public static final class PublicTestType {}

    @GeneratesSchema
    static final class NonPublicTestType {}
}

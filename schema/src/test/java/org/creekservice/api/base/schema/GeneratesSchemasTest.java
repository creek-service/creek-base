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
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;

import java.util.Set;
import org.creekservice.api.base.annotation.schema.GeneratesSchema;
import org.junit.jupiter.api.Test;

class GeneratesSchemasTest {

    @Test
    void shouldFindPublicAnnotatedTypes() {
        // When:
        final Set<Class<?>> result =
                GeneratesSchemas.findAnnotatedTypes(GeneratesSchemasTest.class.getPackageName());

        // Then:
        assertThat(result, hasItem(PublicTestType.class));
    }

    @Test
    void shouldNotFindNonPublicAnnotatedTypes() {
        // When:
        final Set<Class<?>> result =
                GeneratesSchemas.findAnnotatedTypes(GeneratesSchemasTest.class.getPackageName());

        // Then:
        assertThat(result, not(hasItem(NonPublicTestType.class)));
    }

    @GeneratesSchema
    public static final class PublicTestType {}

    @GeneratesSchema
    static final class NonPublicTestType {}
}

/*
 * Copyright 2025 Creek Contributors (https://github.com/creek-service)
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.net.URL;
import org.junit.jupiter.api.Test;

class GeneratedSchemasTest {

    @Test
    void shouldGenerateFileName() {
        assertThat(
                GeneratedSchemas.schemaFileName(
                        GeneratedSchemasTest.class, GeneratedSchemas.yamlExtension()),
                is("org/creekservice/api/base/type/schema/GeneratedSchemasTest.yml"));
    }

    @Test
    void shouldWriteSchemaForNestedType() {
        assertThat(
                GeneratedSchemas.schemaFileName(Nested.class, GeneratedSchemas.yamlExtension()),
                is("org/creekservice/api/base/type/schema/GeneratedSchemasTest$Nested.yml"));
    }

    @Test
    void shouldWriteSchemaForLocalType() {
        // Given:
        final class Model {}

        // Then:
        assertThat(
                GeneratedSchemas.schemaFileName(Model.class, GeneratedSchemas.yamlExtension()),
                is("org/creekservice/api/base/type/schema/GeneratedSchemasTest$1Model.yml"));
    }

    @Test
    void shouldBuildPathThatCanBeLoadedAsResource() {
        // Given:
        final String path =
                GeneratedSchemas.schemaFileName(
                        GeneratedSchemasTest.class, GeneratedSchemas.yamlExtension());

        // When:
        final URL resource = getClass().getResource("/" + path);

        // Then:
        assertThat(resource, is(notNullValue()));
    }

    private static final class Nested {}
}

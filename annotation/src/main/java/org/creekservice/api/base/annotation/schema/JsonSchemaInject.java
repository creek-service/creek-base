/*
 * Copyright 2022-2026 Creek Contributors (https://github.com/creek-service)
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

package org.creekservice.api.base.annotation.schema;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for injecting raw JSON fragments into a generated JSON schema.
 *
 * <p>When applied to a class, the supplied JSON fragment is merged into the schema's root object.
 * When applied to a method (getter), it is merged into the property's schema node.
 *
 * <p>Example — adding an {@code anyOf} constraint to a class:
 *
 * <pre>{@code
 * @JsonSchemaInject("{\"anyOf\":[{\"required\":[\"a\"]},{\"required\":[\"b\"]}]}")
 * public class MyType { ... }
 * }</pre>
 *
 * <p>Example — adding {@code maxItems} to a property:
 *
 * <pre>{@code
 * @JsonSchemaInject("{\"maxItems\":5}")
 * public List<String> getItems() { ... }
 * }</pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface JsonSchemaInject {

    /**
     * A raw JSON schema fragment to be merged into the generated schema node.
     *
     * @return a valid JSON object string, e.g. {@code "{\"maxItems\":5}"}
     */
    String value();
}

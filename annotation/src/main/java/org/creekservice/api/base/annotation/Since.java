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

package org.creekservice.api.base.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/** Documentation annotation for marking code with the date a feature was introduced. */
@Retention(RetentionPolicy.SOURCE)
public @interface Since {
    /**
     * Date of change.
     *
     * @return the date the change was made
     */
    String value();

    /**
     * Additional documentation about the change.
     *
     * @return optional additional comment
     */
    String comment() default "";
}

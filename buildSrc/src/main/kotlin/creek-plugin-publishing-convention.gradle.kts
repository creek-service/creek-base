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

/**
 * Configuration for Creek Gradle plugin publishing.
 *
 * <p>Apply this plugin to any module publishing a Gradle plugin.
 *
 * <p>Do NOT ally the `creek-publishing-convention`.
 */

plugins {
    java
    signing
    `maven-publish`
    `java-gradle-plugin`
    id("com.gradle.plugin-publish")
}

java {
    withSourcesJar()
    withJavadocJar()
}

val prependRootName = rootProject.name != project.name

pluginBundle {
    website = "https://www.creekservie.org"
    vcsUrl = "https://github.com/creek-service/${rootProject.name}"

    tags = listOf("creek", "creekservice", "microservice", "docker", "containers")
}

if (prependRootName) {
    tasks.jar {
        archiveBaseName.set("${rootProject.name}-${project.name}")
    }
}

tasks.javadoc {
    if (JavaVersion.current().isJava9Compatible) {
        (options as StandardJavadocDocletOptions).apply {
            addBooleanOption("html5", true)
            // Why -quite? See: https://github.com/gradle/gradle/issues/2354
            addStringOption("Xwerror", "-quiet")
        }
    }
}

tasks.jar {
    manifest {
        if (prependRootName) {
            attributes("Automatic-Module-Name" to "${rootProject.name}-${project.name}")
        } else {
            attributes("Automatic-Module-Name" to project.name)
        }
    }
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/creek-service/${rootProject.name}")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }

    publications.withType<MavenPublication>().configureEach {

        if (prependRootName) {
            artifactId = "${rootProject.name}-${project.name}"
        }

        pom {
            name.set("${project.group}:${artifactId}")

            if (prependRootName) {
                description.set("${rootProject.name.capitalize()} ${project.name} library".replace("-", " "))
            } else {
                description.set("${project.name.capitalize()} library".replace("-", " "))
            }

            url.set("https://www.creekservice.org")

            licenses {
                license {
                    name.set("The Apache License, Version 2.0")
                    url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                }
            }

            developers {
                developer {
                    name.set("Andy Coates")
                    email.set("8012398+big-andy-coates@users.noreply.github.com")
                    organization.set("Creek Service")
                    organizationUrl.set("https://www.creekservice.org")
                }
            }

            scm {
                connection.set("scm:git:git://github.com/creek-service/${rootProject.name}.git")
                developerConnection.set("scm:git:ssh://github.com/creek-service/${rootProject.name}.git")
                url.set("https://github.com/creek-service/${rootProject.name}")
            }
        }
    }
}

/**
 * Configure signing of publication artifacts.
 *
 * <p>Can be skipped for snapshot builds.<p>
 *
 * <p>Even snapshot builds will be signed if signing credentials are available.
 * See https://central.sonatype.org/publish/publish-gradle/#credentials
 */
signing {
    setRequired {
        !project.version.toString().endsWith("-SNAPSHOT")
                && !project.hasProperty("skipSigning")
    }

    if (project.hasProperty("signingKey")) {
        useInMemoryPgpKeys(properties["signingKey"].toString(), properties["signingPassword"].toString())
    }
}
